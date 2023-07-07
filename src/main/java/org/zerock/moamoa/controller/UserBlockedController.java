package org.zerock.moamoa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.moamoa.service.UserBlockedService;

@RestController
@RequestMapping("/userblocked")
public class UserBlockedController {
    // 유저랑 연결해야되는거 고민하다가 차단한 사람들 리스트 나열하는거 그냥 임시로 만듦...

    private final UserBlockedService userBlockedService;

    @Autowired
    public UserBlockedController(UserBlockedService userBlockedService) {
        this.userBlockedService = userBlockedService;
    }

//    @GetMapping("/{id}")
//    public List<UserBlockedDTO> getBlockedUsers(@PathVariable("userId") Long userId) {
//        List<UserBlocked> blockedUsers = userBlockedService.findByUserList(userId);
//        List<UserBlockedDTO> blockedUsersDTO = new ArrayList<>();
//
//        for (UserBlocked userBlocked : blockedUsers) {
//            UserBlockedDTO userBlockedDTO = new UserBlockedDTO(userBlocked);
//            blockedUsersDTO.add(userBlockedDTO);
//        }
//
//        return blockedUsersDTO;
//    }

}
