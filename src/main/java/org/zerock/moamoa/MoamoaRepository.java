package org.zerock.moamoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.moamoa.domain.entity.User;

public interface MoamoaRepository extends JpaRepository<User,Integer> {
}