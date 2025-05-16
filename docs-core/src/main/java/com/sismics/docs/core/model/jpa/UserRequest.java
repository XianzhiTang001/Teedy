package com.sismics.docs.core.model.jpa;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "T_USER_REQUEST")
public class UserRequest {
    @Id
    @Column(name = "REQ_ID_C", length = 36)
    private String id;

    @Column(name = "REQ_USERNAME_C", nullable = false, length = 50)
    private String username;

    @Column(name = "REQ_PASSWORD_C", nullable = false, length = 100)
    private String password;

    @Column(name = "REQ_EMAIL_C", nullable = false, length = 100)
    private String email;

    @Column(name = "REQ_STATUS_C", nullable = false)
    private String status;

    @Column(name = "REQ_CREATEDATE_D", nullable = false)
    private Date createDate;

    public String getId() {
        return id;
    }

    public UserRequest setId(String id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public UserRequest setStatus(String status) {
        this.status = status;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public UserRequest setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("username", username)
                .add("email", email)
                .add("status", status)
                .toString();
    }

    public String toMessage() {
        return username + " (" + status + ")";
    }
}