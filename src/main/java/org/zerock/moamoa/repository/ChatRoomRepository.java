package org.zerock.moamoa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.entity.ChatRoom;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    default ChatRoom findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CHATROOM_NOT_FOUND));
    }

    Page<ChatRoom> findAllByProduct(Product product, Pageable pageable);

    Page<ChatRoom> findAllBySellerOrUser(User seller, User user, Pageable pageable);

    boolean existsByProductAndUser(Optional<Product> byId, Optional<User> byId2);
}
