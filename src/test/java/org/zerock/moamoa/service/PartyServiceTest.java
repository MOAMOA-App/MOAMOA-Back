package org.zerock.moamoa.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.moamoa.repository.PartyRepository;

@SpringBootTest
class PartyServiceTest {

	@Autowired
	PartyService partyService;
	@Autowired
	PartyRepository partyRepository;

	@Test
	void findById() {
		System.out.println(partyService.findById(Long.valueOf(2)));
	}

	@Test
	void findAll() {
		System.out.println(partyService.findAll());
	}
	//     @Test
	//     void saveParty() {
	//         // Mocked Product and User
	//         int i = 1;
	//         String temp = Integer.toString(1);
	//         Product product = new Product();
	//
	//         // Mocked User
	//         Party party = new Party();
	//         User user = new User();
	// //        party.set(21L);
	//
	//         party.setProduct(product);
	//         party.setAddress(temp);
	//         party.setBuyerId(31L);
	//         partyService.saveParty(party, product.getId());
	//
	//     }

	@Test
	void removeParty() {
//		partyService.removeParty(Long.valueOf(2));
	}

	@Test
	void updateParty() {
	}
}