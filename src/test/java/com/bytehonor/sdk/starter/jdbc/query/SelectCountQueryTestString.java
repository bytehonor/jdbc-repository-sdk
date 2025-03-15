package com.bytehonor.sdk.starter.jdbc.query;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.lang.spring.query.QueryCondition;
import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.sql.SqlConvertor;
import com.bytehonor.sdk.starter.jdbc.statement.PrepareStatement;
import com.bytehonor.sdk.starter.jdbc.statement.SelectCountPrepareStatement;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;

public class SelectCountQueryTestString {

    private static final Logger LOG = LoggerFactory.getLogger(SelectCountQueryTestString.class);

    @Test
    public void test() {
        Set<Integer> ages = new HashSet<Integer>();
        ages.add(1);
        ages.add(2);
        ages.add(3);
        Set<String> names = new HashSet<String>();
        names.add("aa");
        names.add("bb");
        names.add("cc");
        QueryCondition condition = QueryCondition.and();
        condition.in(Student::getAge, ages);
        condition.in(Student::getNickname, names);
        condition.gt(Student::getCreateAt, System.currentTimeMillis());
        condition.desc(Student::getAge);
        PrepareStatement statement = new SelectCountPrepareStatement(Student.class, SqlConvertor.convert(condition));
        String sql = statement.sql();
        Object[] args = statement.args();

        int length = args.length;
        LOG.info("sql:[{}], args:[{}], length:{}", sql, SqlInjectUtils.toString(args), length);
        statement.check();

        String target = "SELECT COUNT(id) FROM tbl_student WHERE age IN (1,2,3) AND nickname IN ('aa','bb','cc') AND create_at > ?";
        assertTrue("test", target.equals(sql) && length == 1);
    }

    @Test
    public void testNonFilter() {
        QueryCondition condition = QueryCondition.and();
        PrepareStatement statement = new SelectCountPrepareStatement(Student.class, SqlConvertor.convert(condition));
        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testNonFilter:{}", sql);
        statement.check();

        assertTrue("testNonFilter", "SELECT COUNT(id) FROM tbl_student".equals(sql) && args.length == 0);
    }
}
