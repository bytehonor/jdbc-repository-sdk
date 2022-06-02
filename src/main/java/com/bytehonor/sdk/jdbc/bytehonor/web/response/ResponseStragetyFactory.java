package com.bytehonor.sdk.jdbc.bytehonor.web.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.server.ServerHttpResponse;

import com.bytehonor.sdk.define.bytehonor.result.JsonResponse;
import com.bytehonor.sdk.jdbc.bytehonor.config.SpringBootStandardProperties;
import com.bytehonor.sdk.jdbc.bytehonor.web.error.ExceptionHolder;
import com.bytehonor.sdk.jdbc.bytehonor.web.response.stragety.ReturnDirectResponseStragety;
import com.bytehonor.sdk.jdbc.bytehonor.web.response.stragety.ReturnErrorResponseStragety;
import com.bytehonor.sdk.jdbc.bytehonor.web.response.stragety.ReturnNormalResponseStragety;
import com.bytehonor.sdk.jdbc.bytehonor.web.response.stragety.ReturnNullResponseStragety;

public class ResponseStragetyFactory {

	@SuppressWarnings("rawtypes")
	public static ResponseStragety build(Object body, ServerHttpResponse response, MethodParameter returnType,
			SpringBootStandardProperties standardSpringBootProperties) {
		if (body == null) {
			return new ReturnNullResponseStragety(response, standardSpringBootProperties);
		}

		if (body instanceof ExceptionHolder) {
			return new ReturnErrorResponseStragety((ExceptionHolder) body, response, standardSpringBootProperties);
		}

		if (body instanceof JsonResponse) {
			return new ReturnDirectResponseStragety((JsonResponse) body, response, standardSpringBootProperties);
		}

		return new ReturnNormalResponseStragety(body, response);
	}

}
