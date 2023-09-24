package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.common.exception.InvalidValueException;
import org.zerock.moamoa.domain.DTO.announce.AnnounceMapper;
import org.zerock.moamoa.domain.DTO.announce.AnnounceRequest;
import org.zerock.moamoa.domain.DTO.announce.AnnounceResponse;
import org.zerock.moamoa.domain.DTO.notice.NoticeSaveRequest;
import org.zerock.moamoa.domain.entity.Announce;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.enums.NoticeType;
import org.zerock.moamoa.repository.AnnounceRepository;
import org.zerock.moamoa.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnounceService {
    private final AnnounceRepository announceRepository;
    private final ProductRepository productRepository;
    private final AnnounceMapper announceMapper;
    private final ApplicationEventPublisher eventPublisher;

    public AnnounceResponse findOne(Long pid, Long aid) {
        Product product = productRepository.findByIdOrThrow(pid);
        Announce announce = announceRepository.findByIdOrThrow(aid);
        if (product.equals(announce.getProduct())) {
            return announceMapper.toDto(announce);
        }
        throw new InvalidValueException(ErrorCode.ANNOUNCE_NOT_FOUND);
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
        Product product = productRepository.findByIdOrThrow(pid);
        List<Announce> announceList = product.getAnnounces();
        return announceList.stream().map(announceMapper::toDto).toList();
    }

    public AnnounceResponse saveAnnounce(AnnounceRequest request, Long pid) {
        Announce announce = announceMapper.toEntity(request);
        Product product = productRepository.findByIdOrThrow(pid);
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
            return true;
        }
        return false;
    }
}