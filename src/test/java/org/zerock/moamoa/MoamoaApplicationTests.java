package org.zerock.moamoa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.moamoa.domain.entity.User;

@SpringBootTest
class MoamoaApplicationTests {


	@Autowired
	private MoamoaRepository moamoaRepository;

	@Test
	void testJpa(){
		User user1 = new User();
		user1.setContent("밥먹기");
		user1.setCompleted(Boolean.TRUE);
		this.moamoaRepository.save(user1);

		User user2 = new User();
		user2.setContent("스프링 공부하기");
		user2.setCompleted(Boolean.FALSE);
		this.moamoaRepository.save(user2);
	}

}
