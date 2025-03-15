package com.bytehonor.sdk.starter.jdbc.query;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.bytehonor.sdk.lang.spring.query.QueryCondition;
import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetter;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetterMapper;
import com.bytehonor.sdk.starter.jdbc.sql.SqlConvertor;
import com.bytehonor.sdk.starter.jdbc.statement.PrepareStatement;
import com.bytehonor.sdk.starter.jdbc.statement.UpdatePrepareStatement;
import com.bytehonor.sdk.starter.jdbc.util.SqlPrinter;

public class UpdateQueryTestValueBlank {

    private static final ModelGetterMapper<Student> MAPPER = new ModelGetterMapper<Student>() {

        @Override
        public ModelGetter<Student> create(Student model) {
            ModelGetter<Student> getters = new ModelGetter<Student>(model);

            getters.add(Student::getId);
            getters.add(Student::getAge);
            getters.add(Student::getNickname);
            getters.add(Student::getUpdateAt);
            getters.add(Student::getCreateAt);
            return getters;
        }

    };

    @Test
    public void test() {

        Set<Integer> ages = new HashSet<Integer>();
        ages.add(1);
        ages.add(2);
        ages.add(3);
        QueryCondition condition = QueryCondition.and();
        condition.in(Student::getAge, ages); // conflict 不会被更新
        condition.gt(Student::getCreateAt, System.currentTimeMillis());

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1); // conflict 不会被更新
        student.setNickname(""); // empty 也会被更新
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new UpdatePrepareStatement(Student.class, SqlConvertor.convert(condition));
        statement.prepare(student, MAPPER);

        String sql = statement.sql();
        Object[] args = statement.args();
        SqlPrinter.print(sql, args);
        statement.check();

        String target = "UPDATE tbl_student SET nickname = ?,update_at = ? WHERE age IN (1,2,3) AND create_at > ?";
        assertTrue("test", target.equals(sql) && args.length == 3);
    }

}
