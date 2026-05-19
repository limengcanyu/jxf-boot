package org.asura.ddd.structure.user.application.dto.query;

import org.asura.ddd.structure.common.dto.request.PageQueryRequest;

public class UserPageQuery extends PageQueryRequest {

    private String username;
    private String email;
    private Boolean enabled;

    public UserPageQuery() {
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}