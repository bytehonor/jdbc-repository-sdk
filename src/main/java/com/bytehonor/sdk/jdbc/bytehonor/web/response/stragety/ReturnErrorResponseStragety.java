package com.bytehonor.sdk.jdbc.bytehonor.web.response.stragety;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import com.bytehonor.sdk.define.bytehonor.result.JsonResponse;
import com.bytehonor.sdk.jdbc.bytehonor.config.SpringBootStandardProperties;
import com.bytehonor.sdk.jdbc.bytehonor.web.error.ExceptionHolder;
import com.bytehonor.sdk.jdbc.bytehonor.web.error.message.DebugErrorMessage;
import com.bytehonor.sdk.jdbc.bytehonor.web.error.message.NormalErrorMessage;
import com.bytehonor.sdk.jdbc.bytehonor.web.response.ResponseStragety;
import com.bytehonor.sdk.jdbc.bytehonor.web.response.helper.ResponseStragetyHelper;

public final class ReturnErrorResponseStragety implements ResponseStragety {

    private static final Logger LOG = LoggerFactory.getLogger(ReturnErrorResponseStragety.class);

    private ExceptionHolder error;

    private final ServerHttpResponse response;

    private final SpringBootStandardProperties standardSpringBootProperties;

    private final boolean enableForceStatus;

    public ReturnErrorResponseStragety(ExceptionHolder error, ServerHttpResponse response,
            SpringBootStandardProperties standardSpringBootProperties) {
        this.response = response;
        this.error = error;
        this.standardSpringBootProperties = standardSpringBootProperties;
        this.enableForceStatus = standardSpringBootProperties != null ? standardSpringBootProperties.isForceHttpStatus()
                : false;
    }

    @Override
    public JsonResponse<?> process(ServerHttpRequest request) {
        JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
        HttpStatus httpStatus = HttpStatus.valueOf(error.getStatus());
        String showTip = "";

//		if (enableDebugRequest) {
//			// 打印错误栈
//			jsonResponse.setTrace(buildDebugErrorTrace(error));
//		}
        NormalErrorMessage normalMessage = buildNormalErrorMessage(error);
        showTip = normalMessage.toString();

        jsonResponse.setCode(error.getCode());
        jsonResponse.setMessage(filterShowTip(showTip));

        // 重置Http status code 200
        if (enableForceStatus || ResponseStragetyHelper.isForceHttpStatusOk(request.getHeaders())) {
            httpStatus = HttpStatus.OK;
        }

        if (response != null) {
            response.setStatusCode(httpStatus);
        }

        if (LOG.isWarnEnabled()) {
            LOG.warn("ErrorCode:{}, HttpStatus:{}", jsonResponse.getCode(), httpStatus.value());
        }

        return jsonResponse;
    }

    @SuppressWarnings("unused")
    private List<String> buildDebugErrorTrace(ExceptionHolder error) {
        List<String> trace = new ArrayList<String>();
        Exception e = error.getException();
        if (e == null) {
            return trace;
        }
        StackTraceElement[] elements = e.getStackTrace();
        for (int i = 0; i < 3; i++) {
            if (elements[i] != null) {
                trace.add(elements[i].toString());
            }
        }
        return trace;
    }

    @SuppressWarnings("unused")
    private DebugErrorMessage buildDebugErrorMessage(ExceptionHolder error, ServerHttpRequest request) {
        DebugErrorMessage message = new DebugErrorMessage();
        Exception ex = error.getException();
        message.setUri(formatUri(request.getURI()));
        // message.setClientIp(request.getRemoteAddress().getAddress().getHostAddress());
        if (ex != null) {
            message.setError(ex.getClass().getSimpleName());
            message.setDetail(ex.getMessage());
            message.setStackTrace(formatTrace(ex)); // TODO
        }
        return message;
    }

    private NormalErrorMessage buildNormalErrorMessage(ExceptionHolder error) {
        NormalErrorMessage message = new NormalErrorMessage();
        Exception ex = error.getException();
        if (ex != null) {
            message.setContent(ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName());
        }
        return message;
    }

    private String filterShowTip(String showTip) {
        if (showTip == null || showTip.length() < 1) {
            return "unknown";
        }
        if (showTip.length() > 512) {
            return showTip.substring(0, 512);
        }
        return showTip;
    }

    private String formatTrace(Exception ex) {
        if (ex == null || ex.getStackTrace() == null || ex.getStackTrace().length < 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(ex.getStackTrace()[0].toString()); // first line
        return sb.toString();
    }

    private String formatUri(URI uri) {
        StringBuilder sb = new StringBuilder();
        sb.append(uri.getPort()).append(uri.getPath());
        if (uri.getQuery() != null) {
            sb.append("?");
            sb.append(uri.getQuery());
        }
        return sb.toString();
    }

    public ExceptionHolder getError() {
        return error;
    }

    public void setError(ExceptionHolder error) {
        this.error = error;
    }

    public ServerHttpResponse getResponse() {
        return response;
    }

    public SpringBootStandardProperties getStandardSpringBootProperties() {
        return standardSpringBootProperties;
    }

    public boolean isEnableForceStatus() {
        return enableForceStatus;
    }

}
