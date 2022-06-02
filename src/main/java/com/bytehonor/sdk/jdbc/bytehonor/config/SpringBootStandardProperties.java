package com.bytehonor.sdk.jdbc.bytehonor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "standard.springboot")
public class SpringBootStandardProperties {

    /**
     * Server Response Handler Use Global Advisor, default true
     */
    private boolean responseAdvisorEnable = true;

    /**
     * Server Error Handler Use Global Advisor, default true
     */
    private boolean errorAdvisorEnable = true;

    /**
     * Server Use Global Error Controller, default true
     */
    private boolean errorControllerEnable = true;

    /**
     * Server Enable Global Json Converter, default true
     */
    private boolean mvcCustomEnable = true;

    /**
     * Server Api Resfult Http Status force 200, default true
     */
    private boolean forceHttpStatus = true;

    /**
     * Server Print Error Trace Lines, default 32
     */
    private int errorTraceLines = 32;

    public boolean isResponseAdvisorEnable() {
        return responseAdvisorEnable;
    }

    public void setResponseAdvisorEnable(boolean responseAdvisorEnable) {
        this.responseAdvisorEnable = responseAdvisorEnable;
    }

    public boolean isErrorAdvisorEnable() {
        return errorAdvisorEnable;
    }

    public void setErrorAdvisorEnable(boolean errorAdvisorEnable) {
        this.errorAdvisorEnable = errorAdvisorEnable;
    }

    public boolean isErrorControllerEnable() {
        return errorControllerEnable;
    }

    public void setErrorControllerEnable(boolean errorControllerEnable) {
        this.errorControllerEnable = errorControllerEnable;
    }

    public boolean isMvcCustomEnable() {
        return mvcCustomEnable;
    }

    public void setMvcCustomEnable(boolean mvcCustomEnable) {
        this.mvcCustomEnable = mvcCustomEnable;
    }

    public boolean isForceHttpStatus() {
        return forceHttpStatus;
    }

    public void setForceHttpStatus(boolean forceHttpStatus) {
        this.forceHttpStatus = forceHttpStatus;
    }

    public int getErrorTraceLines() {
        return errorTraceLines;
    }

    public void setErrorTraceLines(int errorTraceLines) {
        this.errorTraceLines = errorTraceLines;
    }

}
