package com.bytehonor.sdk.starter.jdbc.statement;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetter;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetterMapper;

public class InsertPrepareStatementTest {

    private static final Logger LOG = LoggerFactory.getLogger(InsertPrepareStatementTest.class);

    private static final ModelGetterMapper<Student> MAPPER = new ModelGetterMapper<Student>() {

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

    @Test
    public void testGetterLess() {

        ModelGetterMapper<Student> less = new ModelGetterMapper<Student>() {

            @Override
            public ModelGetter<Student> create(Student model) {
                ModelGetter<Student> getters = new ModelGetter<Student>(model);

                getters.add(Student::getNickname);
                return getters;
            }

        };

        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("TimeNull");
        student.setCreateAt(null);
        student.setUpdateAt(null);

        PrepareStatement statement = new InsertPrepareStatement(Student.class);
        statement.prepare(student, less);

        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testGetterLess sql:{}", sql);
        statement.check();

        String target = "INSERT INTO tbl_student (nickname,update_at,create_at) VALUES (?,?,?)";
        assertTrue("testGetterLess", target.equals(sql) && args.length == 3);
    }

    @Test
    public void testCost() {

        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("TimeNull");
        student.setCreateAt(1L);
        student.setUpdateAt(2L);

        int size = 10000;

        long start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            PrepareStatement statement = new InsertPrepareStatement(Student.class);
            statement.prepare(student, MAPPER);

            statement.sql();
            statement.args();
        }

        LOG.info("testCost cost:{}", System.currentTimeMillis() - start);
    }

    @Test
    public void testAllValueNull() {

        Student student = new Student();
        student.setId(1L);
        student.setAge(null);
        student.setNickname(null);
        student.setCreateAt(null);
        student.setUpdateAt(null);

        boolean isOk = false;
        try {
            PrepareStatement statement = new InsertPrepareStatement(Student.class);
            statement.prepare(student, MAPPER);
        } catch (Exception e) {
            isOk = true;
            LOG.info("testAllValueNull {}", e.getMessage());
        }

        assertTrue("testAllValueNull", isOk);
    }
}
