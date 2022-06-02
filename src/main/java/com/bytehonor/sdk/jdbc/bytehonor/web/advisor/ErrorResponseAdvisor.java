package com.bytehonor.sdk.jdbc.bytehonor.web.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bytehonor.sdk.jdbc.bytehonor.config.SpringBootStandardProperties;
import com.bytehonor.sdk.jdbc.bytehonor.web.error.ExceptionHolder;
import com.bytehonor.sdk.jdbc.bytehonor.web.error.ExceptionStragety;
import com.bytehonor.sdk.jdbc.bytehonor.web.error.ExceptionStragetyFactory;

/**
 * 
 * @author lijianqiang
 *
 */
@ControllerAdvice
public class ErrorResponseAdvisor {

    private static final Logger LOG = LoggerFactory.getLogger(ErrorResponseAdvisor.class);

    @Autowired
    private SpringBootStandardProperties springBootStandardProperties;

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public final ExceptionHolder defaultErrorHandler(Exception ex) {
        // print(ex);
        LOG.error("defaultErrorHandler", ex);
        ExceptionStragety exceptionStragety = ExceptionStragetyFactory.build(ex);
        ExceptionHolder holder = exceptionStragety.hold();
        return holder;
    }

    public void print(Exception ex) {
        if (LOG.isWarnEnabled()) {
            // 错误栈太多无用信息, 只打5行
            StackTraceElement[] stackTrace = ex.getStackTrace();
            StringBuilder sb = new StringBuilder("\n *** ").append(ex.getClass().getSimpleName()).append(" : ")
                    .append(ex.getMessage());
            int errorTraceLines = springBootStandardProperties.getErrorTraceLines();
            if (errorTraceLines > stackTrace.length) {
                errorTraceLines = stackTrace.length;
            }
            for (int i = 0; i < errorTraceLines; i++) {
                if (stackTrace[i] == null) {
                    continue;
                }
                sb.append("\n *** ").append(i).append(" : ").append(stackTrace[i].toString());
            }
            LOG.warn(sb.toString());
        }
    }

}
