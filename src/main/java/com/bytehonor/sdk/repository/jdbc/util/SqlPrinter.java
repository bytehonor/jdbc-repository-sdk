package com.bytehonor.sdk.repository.jdbc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlPrinter {

    private static final Logger LOG = LoggerFactory.getLogger(SqlPrinter.class);

    public static void print(String sql, Object[] args) {
        LOG.info("sql:[{}], args:[{}], length:{}", sql, SqlInjectUtils.toString(args), args != null ? args.length : 0);
    }
}
