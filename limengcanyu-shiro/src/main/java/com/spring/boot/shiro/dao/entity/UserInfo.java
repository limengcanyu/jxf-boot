package com.spring.boot.shiro.dao.entity;//package com.spring.boot.shiro.dao.entity;
//
//import com.baomidou.mybatisplus.annotations.TableId;
//import com.baomidou.mybatisplus.enums.IdType;
//
//import java.io.Serializable;
//
///**
// * <p>
// *
// * </p>
// *
// * @author huitu123
// * @since 2018-01-23
// */
//public class UserInfo implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    @TableId(value = "uid", type = IdType.AUTO)
//    private Integer uid;
//    private String name;
//    private String password;
//    /**
//     * 密码盐：用户密码加密
//     */
//    private String salt;
//    private Integer state;
//    private String username;
//    /**
//     * 记住我
//     */
//    private Boolean rememberMe;
//
//    public Integer getUid() {
//        return uid;
//    }
//
//    public void setUid(Integer uid) {
//        this.uid = uid;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getSalt() {
//        return salt;
//    }
//
//    public void setSalt(String salt) {
//        this.salt = salt;
//    }
//
//    public Integer getState() {
//        return state;
//    }
//
//    public void setState(Integer state) {
//        this.state = state;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public Boolean getRememberMe() {
//        return rememberMe;
//    }
//
//    public void setRememberMe(Boolean rememberMe) {
//        this.rememberMe = rememberMe;
//    }
//
//    /**
//     * 密码盐.
//     *
//     * @return
//     */
//    public String getCredentialsSalt() {
//        return this.username + this.salt;
//    }
//
//    @Override
//    public String toString() {
//        return "UserInfo{" +
//                "uid=" + uid +
//                ", name='" + name + '\'' +
//                ", password='" + password + '\'' +
//                ", salt='" + salt + '\'' +
//                ", state=" + state +
//                ", username='" + username + '\'' +
//                ", rememberMe=" + rememberMe +
//                '}';
//    }
//}
