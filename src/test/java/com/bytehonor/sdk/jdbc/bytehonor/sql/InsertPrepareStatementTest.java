package com.bytehonor.sdk.jdbc.bytehonor.sql;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.jdbc.bytehonor.Student;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelConvertMapper;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelGetterGroup;

public class InsertPrepareStatementTest {

    private static final Logger LOG = LoggerFactory.getLogger(InsertPrepareStatementTest.class);

    private static final ModelConvertMapper<Student> MAPPER = new ModelConvertMapper<Student>() {

        @Override
        public ModelGetterGroup<Student> create() {
            ModelGetterGroup<Student> group = ModelGetterGroup.create(Student.class);
            group.add("id", Student::getId);
            group.add("age", Student::getAge);
            group.add("nickname", Student::getNickname);
            group.add("create_at", Student::getCreateAt);
            group.add("updateAt", Student::getUpdateAt);
            return group;
        }

    };

    @Test
    public void test() {

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("boy");
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new InsertPrepareStatement(Student.class);
        statement.prepare(student, MAPPER);

        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("sql:{}", sql);
        statement.check();

        String target = "INSERT INTO tbl_student (age,nickname,update_at,create_at) VALUES (?,?,?,?)";
        assertTrue("test", target.equals(sql) && args.length == 4);
    }

    @Test
    public void testValueNull() {

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname(null); // null, 不入
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new InsertPrepareStatement(Student.class);
        statement.prepare(student, MAPPER);

        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testValueNull sql:{}", sql);
        statement.check();

        String target = "INSERT INTO tbl_student (age,update_at,create_at) VALUES (?,?,?)";
        assertTrue("testValueNull", target.equals(sql) && args.length == 3);
    }

    @Test
    public void testValueEmpty() {

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname(""); // 空, 入
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new InsertPrepareStatement(Student.class);
        statement.prepare(student, MAPPER);

        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testValueEmpty sql:{}", sql);
        statement.check();

        String target = "INSERT INTO tbl_student (age,nickname,update_at,create_at) VALUES (?,?,?,?)";
        assertTrue("testValueEmpty", target.equals(sql) && args.length == 4);
    }

    @Test
    public void testTimeNull() {

        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("TimeNull");
        student.setCreateAt(null);
        student.setUpdateAt(null);

        PrepareStatement statement = new InsertPrepareStatement(Student.class);
        statement.prepare(student, MAPPER);

        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testTimeNull sql:{}", sql);
        statement.check();

        String target = "INSERT INTO tbl_student (age,nickname,update_at,create_at) VALUES (?,?,?,?)";
        assertTrue("testTimeNull", target.equals(sql) && args.length == 4);
    }
}
