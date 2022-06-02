package com.bytehonor.sdk.jdbc.bytehonor.web.response.stragety;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import com.bytehonor.sdk.define.bytehonor.code.StandardCode;
import com.bytehonor.sdk.define.bytehonor.result.JsonResponse;
import com.bytehonor.sdk.jdbc.bytehonor.config.SpringBootStandardProperties;
import com.bytehonor.sdk.jdbc.bytehonor.web.response.ResponseStragety;
import com.bytehonor.sdk.jdbc.bytehonor.web.response.helper.ResponseStragetyHelper;

public final class ReturnDirectResponseStragety implements ResponseStragety {

	private static final Logger LOG = LoggerFactory.getLogger(ReturnDirectResponseStragety.class);

	private final JsonResponse<?> jsonResponse;

	private final ServerHttpResponse response;

	private final SpringBootStandardProperties standardSpringBootProperties;

	private final boolean enableForceStatus;

	public ReturnDirectResponseStragety(JsonResponse<?> jsonResponse, ServerHttpResponse response,
			SpringBootStandardProperties standardSpringBootProperties) {
		this.response = response;
		this.jsonResponse = jsonResponse;
		this.standardSpringBootProperties = standardSpringBootProperties;
		this.enableForceStatus = standardSpringBootProperties != null ? standardSpringBootProperties.isForceHttpStatus()
				: false;
	}

	@Override
	public JsonResponse<?> process(ServerHttpRequest request) {
		boolean isError = jsonResponse.getCode() != StandardCode.OK;
		HttpStatus httpStatus = isError ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		boolean force = enableForceStatus || ResponseStragetyHelper.isForceHttpStatusOk(request.getHeaders());
		if (force) {
			httpStatus = HttpStatus.OK;
		}

		if (response != null) {
			response.setStatusCode(httpStatus);
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("ErrorCode:{}, HttpStatus:{}", jsonResponse.getCode(), httpStatus.value());
		}
		return jsonResponse;
	}

	public JsonResponse<?> getJsonResponse() {
		return jsonResponse;
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
