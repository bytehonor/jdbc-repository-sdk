package com.bytehonor.sdk.repository.jdbc.query;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.framework.lang.query.QueryCondition;
import com.bytehonor.sdk.repository.jdbc.Student;
import com.bytehonor.sdk.repository.jdbc.model.ModelGetter;
import com.bytehonor.sdk.repository.jdbc.model.ModelGetterMapper;
import com.bytehonor.sdk.repository.jdbc.sql.SqlConvertor;
import com.bytehonor.sdk.repository.jdbc.statement.PrepareStatement;
import com.bytehonor.sdk.repository.jdbc.statement.UpdatePrepareStatement;

public class UpdateQueryTestValueNull {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateQueryTestValueNull.class);

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
        QueryCondition condition = QueryCondition.and();
        condition.gt(Student::getCreateAt, System.currentTimeMillis());

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname(null); // null 不会被更新
        student.setCreateAt(now);
        // student.setUpdateAt(now);

        PrepareStatement statement = new UpdatePrepareStatement(Student.class, SqlConvertor.convert(condition));
        statement.prepare(student, MAPPER);

        String sql = statement.sql();
        Object[] args = statement.args();
        int length = args.length;

        LOG.info("sql:{}, length:{}", sql, length);
        statement.check();

        String target = "UPDATE tbl_student SET age = ?,update_at = ? WHERE create_at > ?";
        assertTrue("testSetValueNull", target.equals(sql) && length == 3);
    }
}
