package com.study.mysite;

import com.study.mysite.answer.Answer;
import com.study.mysite.answer.AnswerRepository;
import com.study.mysite.question.Question;
import com.study.mysite.question.QuestionRepository;
import com.study.mysite.question.QuestionService;
import com.study.mysite.user.SiteUser;
import com.study.mysite.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MysiteApplicationTests {

    //리포지토리 의존성 주입
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Test
    void testJpa() {
        Question q1 = new Question();
        q1.setSubject("리액트가 대체 무엇인가요?");
        q1.setContent("알고싶어요");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);
    }

    @Test
    void testJpa2() {
        List<Question> all = this.questionRepository.findAll();
        assertEquals(8, all.size());

        Question q = all.get(0);
        assertEquals("JPA가 무엇인가요?", q.getSubject());

        Question question = this.questionRepository.findBySubject("리액트가 대체 무엇인가요?");
        assertEquals(8, question.getId());

    }

    @Test
    void testJpa3() {
        List<Question> qList = this.questionRepository.findAllBySubjectLike("%무엇%");
        this.questionRepository.findAll();
    }

    @Test
    void testJpa4() {
        //1번이라는 아이디가 있으면 실행해주고, 없으면 undefinded라고 뜸
        Optional<Question> oq = this.questionRepository.findById(1); //해당하는 값을 찾았으면 oq에 넣음.
        assertTrue(oq.isPresent()); //

        Question q = oq.get();
        q.setSubject("무엇이든 물어보세요");
        this.questionRepository.save(q);
    }


    @Test
    void testJpa5() {
        //레포지토리의 데이터 개수가 7개인지 test
        assertEquals(7, this.questionRepository.count());

        // id=3인 데이터를 oq에 저장 후 삭제
        Optional<Question> oq = this.questionRepository.findById(3);
        assertTrue(oq.isPresent());
        Question q = oq.get(); //Optional 타입일 때 get메서드 사용 가능
        this.questionRepository.delete(q);

        //레포지토리의 데이터 개수가 6개인지 test
        assertEquals(6, this.questionRepository.count());
    }

    @Test
    void testJpa6() {
        //질문이 있는지 확인
        Optional<Question>  oq = this.questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        Answer a = new Answer();

        a.setContent("두번째 답변 드립니다.");
        a.setQuestion(q); // 어떤 질문에 대한 답변인지 연결
        a.setCreateDate(LocalDateTime.now());
        this.answerRepository.save(a);

    }

    @Transactional
    @Test
    void testJpa7() {
        //질문이 있는지 확인
        Optional<Question> oq = this.questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        List<Answer> answerList = q.getAnswerList();

        //Answer a = new Answer();

        //a.setContent("답변 드립니다. 4번");
        //a.setQuestion(q); // 어떤 질문에 대한 답변인지 연결
        //a.setCreateDate(LocalDateTime.now());
        //this.answerRepository.save(a);



        assertEquals(3, answerList.size());
        //assertEquals("내용", answerList.get(0).getContent());

    }


    /*게시글 대량 생성*/
    @Test
    void createTestRecords(){

        SiteUser user1 = userService.getUser("user01");
        SiteUser user2 = userService.getUser("user02");
        SiteUser user3 = userService.getUser("user03");
        for (int i = 0; i <= 100; i++) {
            String subject = String.format("테스트코드 제목 : [%03d]",i);
            String content = String.format("테스트코드 내용 : [%03d]",i);
            this.questionService.create(subject, content, user1);
            this.questionService.create(subject, content, user2);
            this.questionService.create(subject, content, user3);
        }
    }

    @Test
    void createUser(){

        //userService.create("user01", "user01@test.com", "1234");
        //userService.create("user02", "user02@test.com", "1234");
        //userService.create("user03", "user03@test.com", "1234");
    }



}
