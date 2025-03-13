package com.bytehonor.sdk.starter.jdbc.query;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.lang.spring.query.QueryCondition;
import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetter;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetterMapper;
import com.bytehonor.sdk.starter.jdbc.sql.SqlAdapter;
import com.bytehonor.sdk.starter.jdbc.statement.PrepareStatement;
import com.bytehonor.sdk.starter.jdbc.statement.UpdatePrepareStatement;

public class UpdatePrepareStatementTest {

    private static final Logger LOG = LoggerFactory.getLogger(UpdatePrepareStatementTest.class);

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
        condition.desc(Student::getAge);

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("boy");
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new UpdatePrepareStatement(Student.class, SqlAdapter.convert(condition));
        statement.prepare(student, MAPPER);

        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("sql:{}", sql);
        statement.check();

        String target = "UPDATE tbl_student SET age = ?,nickname = ?,update_at = ? WHERE create_at > ?";
        assertTrue("test", target.equals(sql) && args.length == 4);
    }

    @Test
    public void testSetValueConflict() {
        QueryCondition condition = QueryCondition.and();
        condition.eq(Student::getNickname, "boy");

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("boy"); // conflict 不会被更新
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new UpdatePrepareStatement(Student.class, SqlAdapter.convert(condition));
        statement.prepare(student, MAPPER);

        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testSetValueConflict sql:{}", sql);
        statement.check();

        String target = "UPDATE tbl_student SET age = ?,update_at = ? WHERE nickname = ?";
        assertTrue("testSetValueConflict", target.equals(sql) && args.length == 3);
    }

    @Test
    public void testUpdateById() {
        QueryCondition condition = QueryCondition.one().eq(Student::getId, 1L);

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("boy");
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new UpdatePrepareStatement(Student.class, SqlAdapter.convert(condition));
        statement.prepare(student, MAPPER);

        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testUpdateById sql:{}", sql);
        statement.check();

        String target = "UPDATE tbl_student SET age = ?,nickname = ?,update_at = ? WHERE id = ?";
        assertTrue("testUpdateById", target.equals(sql) && args.length == 4);
    }

    @Test
    public void testUpdateAtConflict() {

        QueryCondition condition = QueryCondition.and();
        condition.gt(Student::getUpdateAt, System.currentTimeMillis());
        condition.desc(Student::getAge);

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("boy");
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new UpdatePrepareStatement(Student.class, SqlAdapter.convert(condition));
        statement.prepare(student, MAPPER);

        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testUpdateAtConflict sql:{}", sql);
        statement.check();

        // TODO 问题
        String target = "UPDATE tbl_student SET age = ?,nickname = ?,update_at = ? WHERE update_at > ?";
        assertTrue("test", target.equals(sql) && args.length == 4);
    }
}
