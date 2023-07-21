package org.zerock.moamoa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.moamoa.domain.entity.ChatRoom;
import org.zerock.moamoa.domain.entity.Product;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Page<ChatRoom> findAllByProductId(Product product, Pageable pageable);
}
