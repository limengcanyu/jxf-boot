package org.asura.restful.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "更新用户请求")
public class UserUpdateRequest {

    @Size(max = 50, message = "用户名长度不能超过50")
    @Schema(description = "用户名", example = "zhangsan")
    private String username;

    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Size(min = 6, max = 100, message = "密码长度必须在6-100之间")
    @Schema(description = "密码", example = "newpassword123")
    private String password;

    @Size(max = 50, message = "昵称长度不能超过50")
    @Schema(description = "昵称", example = "张三")
    private String nickname;

    @Schema(description = "状态", example = "1")
    private Integer status;

    public UserUpdateRequest() {}

    public UserUpdateRequest(String username, String email, String phone, String password, String nickname, Integer status) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.nickname = nickname;
        this.status = status;
    }

    public static UserUpdateRequestBuilder builder() {
        return new UserUpdateRequestBuilder();
    }

    public static class UserUpdateRequestBuilder {
        private String username;
        private String email;
        private String phone;
        private String password;
        private String nickname;
        private Integer status;

        public UserUpdateRequestBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserUpdateRequestBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserUpdateRequestBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserUpdateRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserUpdateRequestBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public UserUpdateRequestBuilder status(Integer status) {
            this.status = status;
            return this;
        }

        public UserUpdateRequest build() {
            return new UserUpdateRequest(username, email, phone, password, nickname, status);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}