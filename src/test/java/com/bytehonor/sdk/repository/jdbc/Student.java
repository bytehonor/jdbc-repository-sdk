package com.bytehonor.sdk.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.repository.jdbc.annotation.SqlColumn;
import com.bytehonor.sdk.repository.jdbc.annotation.SqlPrimary;
import com.bytehonor.sdk.repository.jdbc.annotation.SqlTable;
import com.bytehonor.sdk.repository.jdbc.model.ModelGetter;
import com.bytehonor.sdk.repository.jdbc.model.ModelGetterMapper;

@SqlTable(name = "tbl_student", primary = "id")
public class Student {

    private static final Logger LOG = LoggerFactory.getLogger(Student.class);

    @SqlPrimary
    private Long id;

    @SqlColumn(name = "nickname", ignore = false)
    private String nickname;

    @SqlColumn
    private Integer age;

    private Long updateAt;

    @SqlColumn(name = "create_at")
    private Long createAt;

    public static String hello(String msg) {
        LOG.info("hello {}", msg);
        return "hello" + msg;
    }

    public static String hi(String msg) {
        LOG.info("hi {}", msg);
        return "hi" + msg;
    }

    public String repeat(String msg) {
        return nickname + "repeat" + msg;
    }

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public static final ModelGetterMapper<Student> MAPPER = new ModelGetterMapper<Student>() {

        @Override
        public ModelGetter<Student> create(Student model) {
            ModelGetter<Student> getter = new ModelGetter<Student>(model);

            getter.add(Student::getId);
            getter.add(Student::getAge);
            getter.add(Student::getNickname);
            getter.add(Student::getUpdateAt);
            getter.add(Student::getCreateAt);
            return getter;
        }

    };
}
