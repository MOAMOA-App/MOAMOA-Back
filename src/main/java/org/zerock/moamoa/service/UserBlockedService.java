package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.entity.UserBlocked;
import org.zerock.moamoa.repository.UserBlockedRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBlockedService {
    private final UserBlockedRepository userBlockedRepository;
    private final UserService userService;

    @Transactional
    public Optional<UserBlocked> findById(Long id) {
        return this.userBlockedRepository.findById(id);
    }

    @Transactional
    public List<UserBlocked> findAll() {
        return this.userBlockedRepository.findAll();
    }

    @Transactional
    public UserBlocked saveUserBlocked(Long user_id, Long target_id) {
        User user = userService.findById(user_id);
        User target = userService.findById(target_id);
        UserBlocked userBlocked = new UserBlocked();
//		userBlocked.setUser(user);
//		userBlocked.setTarget(target);
        return this.userBlockedRepository.save(userBlocked);
    }

    @Transactional
    public void removeUserBlocked(Long id) {
        UserBlocked user = userBlockedRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 없습니다. id=" + id));
        this.userBlockedRepository.delete(user);
    }

}
