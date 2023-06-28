package org.zerock.moamoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.moamoa.repository.AnnounceRepository;

@SpringBootTest
class AnnounceServiceTest {

	@Autowired
	AnnounceService announceService;
	@Autowired
	AnnounceRepository announceRepository;
	@Autowired
	ProductService productService;

	// @Test
	// void findByProduct() {
	// 	List<AnnounceDTO> announces = announceService.getAnnounce(11L);
	// 	for (AnnounceDTO announce : announces)
	// 		System.out.println(announce.getId() + ":" + announce.getContents());
	// }
	//
	// @Test
	// void findById() {
	// 	Announce announce = announceService.findById(12L);
	// 	System.out.println(announce.getId() + ":" + announce.getContents());
	// }
	//
	// @Test
	// void findAll() {
	// 	System.out.println(announceService.findAll());
	// }
	//
	// @Test
	// void saveAnnounce() {
	// 	Announce announce = new Announce();
	//
	// 	announce.setLock(1);
	// 	announce.setContents("");
	// 	announceService.saveAnnounce(announce, 11L);
	//
	// }
	//
	// @Test
	// void removeAnnounce() {
	// 	announceService.removeAnnounce(11L);
	// }
	//
	// @Test
	// void updateAnnounce() {
	// }

}