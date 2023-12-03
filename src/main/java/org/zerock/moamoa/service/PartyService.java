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
            throw new AuthException(ErrorCode.INVALID_INPUT_VALUE);
        }

        List<Party> parties = partyRepository.findByProduct(product);
        return parties.stream().map(partyMapper::toUserDto).toList();
    }

    public Page<ProductListResponse> findPageByBuyer(String username, int pageNo, int pageSize) {
        User user = userRepository.findByEmailOrThrow(username);
        Pageable itemPage = PageRequest.of(pageNo, pageSize);
        Page<Party> parties = partyRepository.findByBuyer(user, itemPage);

        return parties.map(Party::getProduct).map(product -> productService.mapProductToListResponse(user, product));
    }

    @Transactional
    public PartytoClientResponse saveParty(String username, PartyRequest request, Long pid) {
        User user = userRepository.findByEmailOrThrow(username);

        Party party = partyMapper.toEntity(request);
        Product product = productRepository.findByIdOrThrow(pid);

        Optional<Party> ifpartyexist = partyRepository.findByBuyerAndProduct(user, product);

        if (ifpartyexist.isPresent()) {
            if (ifpartyexist.get().getStatus().equals(true)) {
                // 이미 있는 party의 status 값 T라면 이미 참여했습니다 메시지
                return PartytoClientResponse.toDto(pid, party.getStatus(), "이미 참여했습니다.");
            } else {
                // F라면 status 값 T로 바꿔주고 주소랑 count 값 변경해줌
                Party temp = ifpartyexist.get();
                PartyUpdateRequest req = new PartyUpdateRequest(request.getAddress(), request.getCount());
                temp.updateParty(req);
                product.addSellCount(party.getCount());
                return PartytoClientResponse.toDto(pid, party.getStatus(), "OK");
            }
        }
        if (product.getUser().equals(user)) // 본인일시 참여불가
            return PartytoClientResponse.toDto(pid, party.getStatus(), "자신의 게시글은 참여할 수 없습니다.");
        ;

        // Party 엔티티에 있음!! Product 엔티티에 해당 party가 없을시 추가
        partyRepository.save(party);
        party.setProduct(product);
        product.addSellCount(party.getCount());
        return PartytoClientResponse.toDto(pid, party.getStatus(), "OK");
    }

    // 참여취소하는것도 해줘야됨. 아니 근데 그러면... 알림보내는것도 리스트에서 보내줘야되나 흑흑 우울해요
    // 굳이 리스트를 만들어야되나... 솔직히 ㅂㄹ 필요없을거같은데
    // 일단 리스트삭제 + status true로 하는거랑 완전 삭제 두개 만들기
    @Transactional
    public ResultResponse removeParty(String username, Long pid) {
        User user = userRepository.findByEmailOrThrow(username);
        Product product = productRepository.findByIdOrThrow(pid);
        Party party = partyRepository.findByBuyerAndProductOrThrow(user, product);

        // 본인이 seller나 buyer가 아닐 시 삭제 불가
        if (!(user.equals(party.getBuyer()) || user.equals(product.getUser()))) {
            throw new AuthException(ErrorCode.AUTH_NOT_FOUND);
        }

        party.removeProduct(product);
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

}