package com.bytehonor.sdk.starter.jdbc;

import com.bytehonor.sdk.starter.jdbc.annotation.SqlTableLeftJoin;

@SqlTableLeftJoin(main = Student.class, sub = UserProfile.class, on = "nickname")
public class StudengContact {

    private Long id;

    private String nickname;

    private String phone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
