package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.party.PartyMapper;
import org.zerock.moamoa.domain.DTO.party.PartyRequest;
import org.zerock.moamoa.domain.DTO.party.PartyResponse;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.entity.WishList;
import org.zerock.moamoa.repository.PartyRepository;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;
    private final ProductRepository productRepository;
    private final PartyMapper partyMapper;
    private final UserRepository userRepository;


    public List<Party> findAll() {
        return this.partyRepository.findAll();
    }

    public List<PartyResponse> getByProduct(Long pid) {
        Product product = productRepository.findByIdOrThrow(pid);
        List<Party> parties = partyRepository.findByProduct(product);
        return parties.stream().map(partyMapper::toDto).toList();
    }

    public List<PartyResponse> getByBuyer(Long uid) {
        User user = userRepository.findByIdOrThrow(uid);
        List<Party> parties = partyRepository.findByBuyer(user);

        return parties.stream().map(partyMapper::toDto).toList();
    }

    public PartyResponse saveParty(PartyRequest request, Long pid) {
        Party party = partyMapper.toEntity(request);
        Product product = productRepository.findByIdOrThrow(pid);
        party.setProduct(product);
        return partyMapper.toDto(partyRepository.save(party));

    }

    public Page<PartyResponse> findPageByBuyer(String username, int pageNo, int pageSize){
        User user = userRepository.findByEmailOrThrow(username);
        Pageable itemPage = PageRequest.of(pageNo, pageSize);
        Page<Party> parties = partyRepository.findByBuyer(user, itemPage);
        if (parties.isEmpty()) {
            throw new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return parties.map(partyMapper::toDto);
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

