package org.asura.modulith.structure.user.mapper;

import org.asura.modulith.structure.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User selectById(Long userId) {
        return new User(userId, "akuma");
    }

    public void insert(User user) {
        System.out.println(user);
    }
}
