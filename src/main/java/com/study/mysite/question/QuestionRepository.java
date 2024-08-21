package com.study.mysite.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Integer>{
	Question findBySubject(String subject);
	Question findBySubjectAndContent(String subject, String content);
	List<Question> findBySubjectLike(String subject);

	Page<Question> findAll(Pageable pageable);
	Page<Question> findAll(Specification<Question> spec, Pageable pageable);

    List<Question> findAllBySubjectLike(String s);

	//사용자가 입력한 키워드로 목록을 검색
	@Query("select distinct q " +
		"from Question q " +
		"left outer join SiteUser u1 on q.author.id = u1.id " +
		"left outer join Answer a on q.id = a.question.id " +
		"left outer join SiteUser u2 on a.author.id = u2.id " +
		"where q.subject like %:kw% " +
		"or q.content like %:kw% " +
		"or u1.username like %:kw% " +
		"or a.content like %:kw% " +
		"or u2.username like %:kw%")
	Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);





}
