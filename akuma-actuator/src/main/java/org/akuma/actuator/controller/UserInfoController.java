package org.akuma.actuator.controller;

import org.akuma.actuator.dto.UserInfoDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/7/30 0030
 */
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
