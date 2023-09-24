package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.entity.UserBlocked;
import org.zerock.moamoa.repository.UserBlockedRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBlockedService {
    private final UserBlockedRepository userBlockedRepository;
    private final UserRepository userRepository;


    @Transactional
    public List<UserBlocked> findAll() {
        return this.userBlockedRepository.findAll();
    }

    @Transactional
    public UserBlocked saveUserBlocked(Long user_id, Long target_id) {
        User user = userRepository.findByIdOrThrow(user_id);
        User target = userRepository.findByIdOrThrow(target_id);
        UserBlocked userBlocked = new UserBlocked();
//		userBlocked.setUser(user);
//		userBlocked.setTarget(target);
        return this.userBlockedRepository.save(userBlocked);
    }

    @Transactional
    public void removeUserBlocked(Long id) {
        UserBlocked user = userBlockedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
        this.userBlockedRepository.delete(user);
    }

}
