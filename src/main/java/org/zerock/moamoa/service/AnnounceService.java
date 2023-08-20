package org.zerock.moamoa.service;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.announce.AnnounceMapper;
import org.zerock.moamoa.domain.DTO.announce.AnnounceRequest;
import org.zerock.moamoa.domain.DTO.announce.AnnounceResponse;
import org.zerock.moamoa.domain.DTO.notice.NoticeSaveRequest;
import org.zerock.moamoa.domain.entity.Announce;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.enums.NoticeType;
import org.zerock.moamoa.repository.AnnounceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnnounceService {
	private final AnnounceRepository announceRepository;
	private final AnnounceMapper announceMapper;
	private final ProductService productService;
	private final ApplicationEventPublisher eventPublisher;

	public AnnounceResponse findOne(Long id) {
		return announceMapper.toDto(findById(id));
	}

	public Announce findById(Long id) {
		return announceRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.ANNOUNCE_NOT_FOUND));
	}

	public List<Announce> findAll() {
		return announceRepository.findAll();
	}

	public void remove(Long id) {
		if (announceRepository.findById(id).isEmpty())
			throw new EntityNotFoundException(ErrorCode.ANNOUNCE_NOT_FOUND);
		announceRepository.deleteById(id);
	}

	@Transactional
	public AnnounceResponse updateInfo(AnnounceRequest announce) {
		Announce temp = findById(announce.getId());
		temp.updateInfo(announce);
		return announceMapper.toDto(temp);
	}

	public List<AnnounceResponse> getByProduct(Long pid) {
		List<Announce> announceList = productService.findById(pid).getAnnounces();
		return announceList.stream().map(announceMapper::toDto).toList();
	}

	public AnnounceResponse saveAnnounce(AnnounceRequest request, Long pid) {
		Announce announce = announceMapper.toEntity(request);
		Product product = productService.findById(pid);
		announce.setProduct(product);
		product.addAnnounce(announce);

		// 알림 발송
		eventPublisher.publishEvent(new NoticeSaveRequest(product.getUser().getId(), null,
									NoticeType.NEW_ANNOUNCE, product.getId()));

		return announceMapper.toDto(announceRepository.save(announce));
	}

	public boolean removeAnnounce(Long id) {
		Announce announce = findById(id);
		if (announce.getId() != null) {
			announce.getProduct().removeAnnounce(announce);
			announceRepository.delete(announce);
			return true;
		}
		return false;
	}
}