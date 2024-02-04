package com.bytehonor.sdk.starter.jdbc;

import com.bytehonor.sdk.starter.jdbc.annotation.SqlColumn;
import com.bytehonor.sdk.starter.jdbc.annotation.SqlTableLeftJoin;

@SqlTableLeftJoin(main = Student.class, sub = UserProfile.class, on = "nickname")
public class StudentContact {

    private Long id;

    private String nickname;

    private String phone;

    private Long updateAt;

    @SqlColumn(name = "create_at")
    private Long createAt;

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

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

}
