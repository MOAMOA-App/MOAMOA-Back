package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.common.exception.InvalidValueException;
import org.zerock.moamoa.domain.DTO.announce.AnnounceMapper;
import org.zerock.moamoa.domain.DTO.announce.AnnounceRequest;
import org.zerock.moamoa.domain.DTO.announce.AnnounceResponse;
import org.zerock.moamoa.domain.DTO.notice.NoticeSaveRequest;
import org.zerock.moamoa.domain.entity.Announce;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.NoticeType;
import org.zerock.moamoa.repository.AnnounceRepository;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnounceService {
    private final AnnounceRepository announceRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
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

    @Transactional
    public AnnounceResponse updateInfo(AnnounceRequest request, String username) {
        User user = userRepository.findByEmailOrThrow(username);
        Announce announce = announceRepository.findByIdOrThrow(request.getId());

        if (announce.getProduct().getUser().equals(user)) {
            if (announce.getId() != null) {
                //TODO remove
                return AnnounceResponse.toMessage("OK");
            }
            return AnnounceResponse.toMessage("FAIL");
        }
        return AnnounceResponse.toMessage("FAIL");
    }

    public List<AnnounceResponse> getByProduct(Long pid) {
        Product product = productRepository.findByIdOrThrow(pid);
        List<Announce> announceList = product.getAnnounces();
        return announceList.stream().map(announceMapper::toDto).toList();
    }

    public AnnounceResponse saveAnnounce(AnnounceRequest request, Long pid, String username) {
        User user = userRepository.findByEmailOrThrow(username);
        Product product = productRepository.findByIdOrThrow(pid);
        if (product.getUser().equals(user)) {
            Announce announce = announceMapper.toEntity(request);
            announce.setProduct(product);
            product.addAnnounce(announce);

            // 알림 발송
            eventPublisher.publishEvent(new NoticeSaveRequest(product.getUser().getId(), null,
                    NoticeType.NEW_ANNOUNCE, product.getId()));

            return announceMapper.toDto(announceRepository.save(announce));
        }
        return AnnounceResponse.toMessage("AUTH_FAIL");
    }

    public AnnounceResponse remove(AnnounceRequest request, String username) {
        User user = userRepository.findByEmailOrThrow(username);
        Announce announce = announceRepository.findByIdOrThrow(request.getId());

        if (announce.getProduct().getUser().equals(user)) {
            if (announce.getId() != null) {
                //TODO remove
                return AnnounceResponse.toMessage("OK");
            }
            return AnnounceResponse.toMessage("FAIL");
        }
        return AnnounceResponse.toMessage("FAIL");
    }
}