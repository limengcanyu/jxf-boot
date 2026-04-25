package org.asura.actuator.controller;

import org.asura.actuator.dto.UserInfoDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoController {
    @RequestMapping("/userInfo/selectUserInfo")
    public UserInfoDTO selectUserInfo(){
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setRecordId(1L);
        userInfoDTO.setUsername("rock");
        userInfoDTO.setPassword("123456");

        return userInfoDTO;
    }
}
