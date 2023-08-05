package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.party.PartyMapper;
import org.zerock.moamoa.domain.DTO.party.PartyRequest;
import org.zerock.moamoa.domain.DTO.party.PartyResponse;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.PartyRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;
    private final PartyMapper partyMapper;
    private final ProductService productService;
    private final UserService userService;

    public PartyResponse findOne(Long id) {
        return partyMapper.toDto(findById(id));
    }

    public Party findById(Long id) {
        return partyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PARTY_NOT_FOUND));
    }

    public List<Party> findAll() {
        return this.partyRepository.findAll();
    }

    public List<PartyResponse> getByProduct(Long pid) {
        Product product = productService.findById(pid);
        List<Party> parties = partyRepository.findByProduct(product);
        return parties.stream().map(partyMapper::toDto).toList();
    }

    public List<PartyResponse> getByBuyer(Long uid) {
        User user = userService.findById(uid);
        List<Party> parties = user.getParties();
        // partyRepository.findByBuyer(user)

        return parties.stream().map(partyMapper::toDto).toList();
    }

    public PartyResponse saveParty(PartyRequest request, Long pid) {
        Party party = partyMapper.toEntity(request);
        Product product = productService.findById(pid);
        party.setProduct(product);

        // request의 구매자 정보를 불러와서 user의 parties에 저장
        User buyer = request.getBuyer();
        party.setUser(buyer);

        return partyMapper.toDto(partyRepository.save(party));

    }

    public List<Product> partyToProduct(Long userId) {
        User buyer = userService.findById(userId);
        List<Party> parties = partyRepository.findByBuyer(buyer);

        // YJ: 파티 존재하지 않을 시의 경우도 고려해야 함
        // 상품 가져와서 리스트 추가
        List<Product> products = new ArrayList<>();
        for (Party party : parties) {
            Product product = party.getProduct();
            products.add(product);
        }
        return products;
    }
}

// public boolean removeParty(Long id) {
//     Party party = findById(id);
//     if (party.getId() != null) {
//         party.getBuyer().removeParty(party);
//         party.getProduct().removeParty(party);
//         partyRepository.delete(party);
//         return true;
//     }
//     return false;
// }
//
// @Transactional
// public Party updateParty(Party party) {
//     Party temp = findById(party.getId());
//     temp.setAddress(party.getAddress());
//     temp.setCount(party.getCount());
//     return this.partyRepository.save(temp);
// }

