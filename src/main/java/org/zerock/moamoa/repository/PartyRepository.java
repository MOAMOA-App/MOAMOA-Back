package org.zerock.moamoa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {
    default Party findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PARTY_NOT_FOUND));
    }

    boolean existsByBuyerAndProduct(User buyer, Product product);

    default Party findByBuyerAndProductOrThrow(User buyer, Product product){
        return findByBuyerAndProduct(buyer, product)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PARTY_NOT_FOUND));
    }
    Optional<Party> findByBuyerAndProduct(User buyer, Product product);

    Page<Party> findByBuyer(User buyer, Pageable pageable);

    List<Party> findByProduct(Product product);

    List<Party> findByProductAndStatus(Product product, Boolean status);
}
