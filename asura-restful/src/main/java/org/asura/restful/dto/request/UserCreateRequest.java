package org.asura.restful.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "创建用户请求")
public class UserCreateRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50之间")
    @Schema(description = "用户名", example = "zhangsan", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱", example = "zhangsan@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在6-100之间")
    @Schema(description = "密码", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Size(max = 50, message = "昵称长度不能超过50")
    @Schema(description = "昵称", example = "张三")
    private String nickname;

    public UserCreateRequest() {}

    public UserCreateRequest(String username, String email, String phone, String password, String nickname) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.nickname = nickname;
    }

    public static UserCreateRequestBuilder builder() {
        return new UserCreateRequestBuilder();
    }

    public static class UserCreateRequestBuilder {
        private String username;
        private String email;
        private String phone;
        private String password;
        private String nickname;

        public UserCreateRequestBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserCreateRequestBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserCreateRequestBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserCreateRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserCreateRequestBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public UserCreateRequest build() {
            return new UserCreateRequest(username, email, phone, password, nickname);
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
}