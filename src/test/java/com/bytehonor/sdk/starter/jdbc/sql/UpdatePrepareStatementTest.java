package com.bytehonor.sdk.starter.jdbc.sql;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.define.spring.query.QueryCondition;
import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.model.ModelConvertMapper;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetterGroup;
import com.bytehonor.sdk.starter.jdbc.util.SqlAdaptUtils;

public class UpdatePrepareStatementTest {

    private static final Logger LOG = LoggerFactory.getLogger(UpdatePrepareStatementTest.class);

    private static final ModelConvertMapper<Student> MAPPER = new ModelConvertMapper<Student>() {

        @Override
        public ModelGetterGroup<Student> create() {
            ModelGetterGroup<Student> group = ModelGetterGroup.create(Student.class);
            group.add("age", Student::getAge);
            group.add("nickname", Student::getNickname);
            group.add("createAt", Student::getCreateAt);
            return group;
        }

    };

    @Test
    public void test() {

        QueryCondition condition = QueryCondition.create();
        condition.gt("createAt", System.currentTimeMillis());
        condition.descBy("age");

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("boy");
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new UpdatePrepareStatement(Student.class, SqlAdaptUtils.from(condition));
        statement.prepare(student, MAPPER);

        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("sql:{}", sql);
        statement.check();

        String target = "UPDATE tbl_student SET age = ?,nickname = ?,update_at = ? WHERE create_at > ?";
        assertTrue("test", target.equals(sql) && args.length == 4);
    }

    @Test
    public void testSetValueEmpty() {

        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);
        QueryCondition condition = QueryCondition.create();
        condition.integers("age", set); // conflict 不会被更新
        condition.gt("createAt", System.currentTimeMillis());

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1); // conflict 不会被更新
        student.setNickname(""); // empty 也会被更新
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new UpdatePrepareStatement(Student.class, SqlAdaptUtils.from(condition));
        statement.prepare(student, MAPPER);

        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testSetValueEmpty sql:{}", sql);
        statement.check();

        String target = "UPDATE tbl_student SET nickname = ?,update_at = ? WHERE age IN (1,2,3) AND create_at > ?";
        assertTrue("testSetValueEmpty", target.equals(sql) && args.length == 3);
    }

    @Test
    public void testSetValueNull() {
        QueryCondition condition = QueryCondition.create();
        condition.gt("createAt", System.currentTimeMillis());

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname(null); // null 不会被更新
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new UpdatePrepareStatement(Student.class, SqlAdaptUtils.from(condition));
        statement.prepare(student, MAPPER);

        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testSetValueNull sql:{}", sql);
        statement.check();

        String target = "UPDATE tbl_student SET age = ?,update_at = ? WHERE create_at > ?";
        assertTrue("testSetValueNull", target.equals(sql) && args.length == 3);
    }

    @Test
    public void testSetValueConflict() {
        QueryCondition condition = QueryCondition.create();
        condition.eq("nickname", "boy");

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("boy"); // conflict 不会被更新
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new UpdatePrepareStatement(Student.class, SqlAdaptUtils.from(condition));
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
        QueryCondition condition = QueryCondition.id(1L);

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("boy");
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new UpdatePrepareStatement(Student.class, SqlAdaptUtils.from(condition));
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

        QueryCondition condition = QueryCondition.create();
        condition.gt("updateAt", System.currentTimeMillis());
        condition.descBy("age");

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("boy");
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new UpdatePrepareStatement(Student.class, SqlAdaptUtils.from(condition));
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
