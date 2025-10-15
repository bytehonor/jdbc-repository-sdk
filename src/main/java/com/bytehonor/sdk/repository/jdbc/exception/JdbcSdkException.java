package com.bytehonor.sdk.repository.jdbc.exception;

/**
 * @author lijianqiang
 *
 */
public class JdbcSdkException extends RuntimeException {

    private static final long serialVersionUID = 4388273149783193642L;

    public JdbcSdkException() {
        super();
    }

    public JdbcSdkException(String message) {
        super(message);
    }

    public JdbcSdkException(String message, Throwable cause) {
        super(message, cause);
    }
}
