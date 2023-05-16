package org.zerock.moamoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.entity.UserBlocked;
import org.zerock.moamoa.repository.UserBlockedRepository;
import org.zerock.moamoa.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserBlockedService {
    private final UserBlockedRepository userBlockedRepository;
    private final UserService userService;

    @Autowired
    public UserBlockedService(UserBlockedRepository userBlockedRepository, UserService userService) {
        this.userBlockedRepository = userBlockedRepository;
        this.userService = userService;
    }


    @Transactional
    public Optional<UserBlocked> findById(Long id){
        return this.userBlockedRepository.findById(id);
    }

    @Transactional
    public List<UserBlocked> findAll(){
        return this.userBlockedRepository.findAll();
    }

    @Transactional
    public UserBlocked saveUserBlocked(Long user_id, Long target_id){
        User user = userService.findById(user_id)
                .orElseThrow(()->new IllegalArgumentException("사용자 id를 검색할 수 없습니다."));
        User target = userService.findById(target_id).orElseThrow(() -> new RuntimeException("차단 대상이 존재하지 않습니다."));
        UserBlocked userBlocked = new UserBlocked();
        userBlocked.setUser(user);
        userBlocked.setTarget(target);
        return this.userBlockedRepository.save(userBlocked);
    }

    @Transactional
    public void removeUserBlocked(Long id){
        UserBlocked user = userBlockedRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 아이템이 없습니다. id=" + id));
        this.userBlockedRepository.delete(user);
    }

}
