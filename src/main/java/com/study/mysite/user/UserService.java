package com.study.mysite.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.study.mysite.DataNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	//프로젝트 경로로 이미지
	private static final String UPLOAD_DIR = "src/main/resources/static/img/user/";

	public SiteUser create(String username, String email, String password, MultipartFile imageFile) throws IOException {
		SiteUser user = new SiteUser();
		user.setUsername(username);
		user.setEmail(email);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(password));

		//회원 프로필 이미지 등록 시, 해당 이미지 이름도 db에 저장
		if(!imageFile.isEmpty()) {
			//고유 이미지 이름 생성
			String fileName = UUID.randomUUID().toString()+"_"+imageFile.getOriginalFilename();
			//파일 저장 경로
			Path filePath = Paths.get(UPLOAD_DIR, fileName);
			//디렉토리 없으면 생성
			Files.createDirectories(filePath.getParent() );
			Files.write(filePath, imageFile.getBytes());
			//사용자 엔티티에 이미지 경로 설정
			user.setImageUrl("/img/user/"+fileName);
		}

		this.userRepository.save(user);
		return user;
	}

	//로그인한 사용자명을 알 수 있는 메소드
	public SiteUser getUser(String username) {
		Optional<SiteUser> siteUser = this.userRepository.findByusername(username);

		if(siteUser.isPresent()) {
			return siteUser.get();
		}else {
			throw new DataNotFoundException("해당 회원이 없습니다.");
		}
	}
}





