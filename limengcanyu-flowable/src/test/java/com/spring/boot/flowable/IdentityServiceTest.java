package com.spring.boot.flowable;

import org.flowable.engine.IdentityService;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * <p>Description: </p>
 * <p>
 * IdentityService很简单。它用于管理（创建，更新，删除，查询……）组与用户。请注意，Flowable实际上在运行时并不做任何用户检查。
 * 例如任务可以分派给任何用户，而引擎并不会验证系统中是否存在该用户。这是因为Flowable有时要与LDAP、Active Directory等服务结合使用。
 *
 * @author rock.jxf
 * @date 2020/8/15 17:34
 */
@SpringBootTest
class IdentityServiceTest {

    @Autowired
    private IdentityService identityService;

    @Test
    public void createUser() {
        String password = "123456789";
        String firstName = "rock";
        String lastName = "jessica";
        String displayName = "rock";
        String email = "111@163.ocm";
        String tenantId = "";

        User user = identityService.newUser("user001");
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDisplayName(displayName);
        user.setEmail(email);
        user.setTenantId(tenantId);
        identityService.saveUser(user);
    }

    @Test
    public void createGroup() {
        Group group = identityService.newGroup("group001");
        group.setName("测试组");
        group.setType("type1");
        identityService.saveGroup(group);
    }

    @Test
    public void createMembership() {
        identityService.createMembership("user001", "group001");
    }

    @Test
    public void createUserQuery() {
        // 查询所有用户
        List<User> userList = identityService.createUserQuery().list();
        for (User user : userList) {
            System.out.println(user.getId() + " " + user.getFirstName() + " " + user.getLastName());
        }

        // 查询指定group中用户
        userList = identityService.createUserQuery().memberOfGroup("group001").list();
        for (User user : userList) {
            System.out.println(user.getId() + " " + user.getFirstName() + " " + user.getLastName());
        }

    }
}
