package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.common.exception.AuthException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.party.*;
import org.zerock.moamoa.domain.DTO.product.ProductListResponse;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.PartyRepository;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;
    private final ProductRepository productRepository;
    private final PartyMapper partyMapper;
    private final ProductService productService;
    private final UserRepository userRepository;


    public List<PartyUserInfoResponse> findUserByProduct(String username, Long pid) {
        User user = userRepository.findByEmailOrThrow(username);
        Product product = productRepository.findByIdOrThrow(pid);

        // 접근자와 seller가 같지 않으면 오류 발생
        if (!user.equals(product.getUser())) {
            throw new AuthException(ErrorCode.USER_ACCESS_REJECTED);
        }

        List<Party> parties = partyRepository.findByProduct(product);
        return parties.stream().map(partyMapper::toUserDto).toList();
    }

    /**
     * 참여자 기준 참여한 party 조회
     */
    public Page<ProductListResponse> findPageByBuyer(String username, int pageNo, int pageSize) {
        User user = userRepository.findByEmailOrThrow(username);
        Pageable itemPage = PageRequest.of(pageNo, pageSize);
        Page<Party> parties = partyRepository.findByBuyer(user, itemPage);

        return parties.map(Party::getProduct).map(product -> productService.mapProductToListResponse(user, product));
    }

    @Transactional
    public PartytoClientResponse saveParty(String username, PartyRequest request, Long pid) {
        User user = userRepository.findByEmailOrThrow(username);
        Product product = productRepository.findByIdOrThrow(pid);

        // 파티가 존재하면 이미 참여했다는 알림
        Optional<Party> ifpartyexist = partyRepository.findByBuyerAndProduct(user, product);
        if (ifpartyexist.isPresent()) {
            return PartytoClientResponse.toDto(pid, "이미 참여했습니다.");
        }
        // 본인의 글은 참여 불가
        if (product.getUser().equals(user))
            return PartytoClientResponse.toDto(pid, "자신의 게시글은 참여할 수 없습니다.");;

        request.setBuyer(user);
        request.setProduct(product);
        Party party = partyMapper.INSTANCE.toEntity(request);
        partyRepository.save(party);

        // Product 엔티티에 해당 party가 없을시 추가
        product.addSellCount(party.getCount());
        return PartytoClientResponse.toDto(pid, "OK");
    }

    @Transactional
    public ResultResponse removeParty(String username, Long pid) {
        User user = userRepository.findByEmailOrThrow(username);
        Product product = productRepository.findByIdOrThrow(pid);
        Party party = partyRepository.findByBuyerAndProductOrThrow(user, product);

        // 본인이 seller나 buyer가 아닐 시 삭제 불가
        if (!(user.equals(party.getBuyer()) || user.equals(product.getUser()))) {
            throw new AuthException(ErrorCode.AUTH_NOT_FOUND);
        }

        partyRepository.delete(party);
        product.subSellCount(party.getCount());

        return ResultResponse.toDto("OK");
    }

    @Transactional
    public ResultResponse updateParty(String username, Long pid, PartyUpdateRequest req) {
        User user = userRepository.findByEmailOrThrow(username);
        Product product = productRepository.findByIdOrThrow(pid);
        Party temp = partyRepository.findByBuyerAndProductOrThrow(user, product);
        product.subSellCount(temp.getCount());
        temp.updateParty(req);
        product.addSellCount(temp.getCount());
        return ResultResponse.toDto("OK");
    }

    /**
     * 입금 상태 변경
     */
    @Transactional
    public ResultResponse updatePartyStatus(String username, Long pid, Long partyid){
        User user = userRepository.findByEmailOrThrow(username);
        Product product = productRepository.findByIdOrThrow(pid);
        if (!product.getUser().equals(user)){
            throw new AuthException(ErrorCode.USER_ACCESS_REJECTED);
        }

        // 현 입금상태에 따라 true면 false로, false면 true로
        Party temp = partyRepository.findByIdOrThrow(partyid);
        temp.updatePartyStatus(temp.getStatus().equals(false));

        return ResultResponse.toDto("OK");
    }
}

