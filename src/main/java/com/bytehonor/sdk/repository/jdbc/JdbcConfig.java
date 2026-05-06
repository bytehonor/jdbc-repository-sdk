package com.bytehonor.sdk.repository.jdbc;

/**
 * SDK 运行时开关配置（当前用于 SQL info 日志开关）。
 *
 * @author lijianqiang
 */
public class JdbcConfig {

    private boolean infoEnabled;

    private JdbcConfig() {
        this.infoEnabled = false;
    }

    private static class LazyHolder {
        private static JdbcConfig SINGLE = new JdbcConfig();
    }

    private static JdbcConfig self() {
        return LazyHolder.SINGLE;
    }

    public static boolean isInfoEnabled() {
        return self().infoEnabled;
    }

    public static boolean setInfoEnabled(boolean infoEnabled) {
        return self().infoEnabled = infoEnabled;
    }

}
