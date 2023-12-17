package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.domain.DTO.announce.AnnounceMapper;
import org.zerock.moamoa.domain.DTO.announce.AnnounceRequest;
import org.zerock.moamoa.domain.DTO.announce.AnnounceResponse;
import org.zerock.moamoa.domain.DTO.announce.AnnounceResultResponse;
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

    public AnnounceResultResponse findOne(Long pid, Long aid) {
        Product product = productRepository.findByIdOrThrow(pid);
        Announce announce = announceRepository.findByIdOrThrow(aid);

        if (!product.equals(announce.getProduct()))
            return AnnounceResultResponse.toMessage("PRODUCT_NOT_EQUAL");

        if (!isAnnounceActive(announce))
            return AnnounceResultResponse.toMessage("ALREADY_REMOVED");

        return AnnounceResultResponse.toDto("OK", announceMapper.toDto(announce));
    }

    @Transactional
    public AnnounceResultResponse updateInfo(AnnounceRequest request, long pid, String username) {
        User user = userRepository.findByEmailOrThrow(username);
        Announce announce = announceRepository.findByIdOrThrow(request.getId());

        if (!isRightAuth(pid, user)) return AnnounceResultResponse.toMessage("AUTH_FAIL");
        if (!isAnnounceActive(announce)) return AnnounceResultResponse.toMessage("ALREADY_REMOVED");

        announce.updateInfo(request);
        return AnnounceResultResponse.toDto("OK", announceMapper.toDto(announce));
    }

    public List<AnnounceResponse> getByProduct(Long pid) {
        Product product = productRepository.findByIdOrThrow(pid);
//        List<Announce> announceList = product.getAnnounces();
        List<Announce> announceList = announceRepository.findByProduct(product);
        return announceList.stream().filter(Announce::getActivate).map(announceMapper::toDto).toList();
    }

    public AnnounceResultResponse saveAnnounce(AnnounceRequest request, Long pid, String username) {
        User user = userRepository.findByEmailOrThrow(username);
        Product product = productRepository.findByIdOrThrow(pid);

        if (!product.getUser().equals(user)) return AnnounceResultResponse.toMessage("AUTH_FAIL");

        request.setProduct(product);
        Announce announce = announceRepository.save(announceMapper.toEntity(request));

        // 알림 발송
        eventPublisher.publishEvent(new NoticeSaveRequest(product.getUser(), null,
                NoticeType.NEW_ANNOUNCE, product));

        return AnnounceResultResponse.toDto("OK", announceMapper.toDto(announce));
    }

    @Transactional
    public AnnounceResultResponse remove(long aid, long pid, String username) {
        User user = userRepository.findByEmailOrThrow(username);
        Announce announce = announceRepository.findByIdOrThrow(aid);

        if (!isRightAuth(pid, user)) return AnnounceResultResponse.toMessage("AUTH_FAIL");

        if (announce.getId() != null) {
            if (!isAnnounceActive(announce)) {
                return AnnounceResultResponse.toMessage("ALREADY_REMOVED");
            }
            announce.remove();
            return AnnounceResultResponse.toMessage("OK");
        }
        return AnnounceResultResponse.toMessage("REQUEST_FAIL");

    }

    private boolean isAnnounceActive(Announce announce) {
        return announce.getActivate();
    }

    private boolean isRightAuth(long pid, User user) {
        Product product = productRepository.findByIdOrThrow(pid);
        return product.getUser().equals(user) && product.getId().equals(pid);
    }
}