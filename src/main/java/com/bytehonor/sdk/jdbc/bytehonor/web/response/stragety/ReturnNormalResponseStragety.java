package com.bytehonor.sdk.jdbc.bytehonor.web.response.stragety;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import com.bytehonor.sdk.define.bytehonor.code.StandardCode;
import com.bytehonor.sdk.define.bytehonor.result.JsonResponse;
import com.bytehonor.sdk.jdbc.bytehonor.web.response.ResponseStragety;

public final class ReturnNormalResponseStragety implements ResponseStragety {

	private static final Logger LOG = LoggerFactory.getLogger(ReturnNormalResponseStragety.class);

	private final Object body;

	private final ServerHttpResponse response;

	public ReturnNormalResponseStragety(Object body, ServerHttpResponse response) {
		this.response = response;
		this.body = body;
	}

	@Override
	public JsonResponse<?> process(ServerHttpRequest request) {
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		jsonResponse.setCode(StandardCode.OK);
		jsonResponse.setMessage(StandardCode.SUCCESS);
		jsonResponse.setData(body);

		if (LOG.isDebugEnabled()) {
			LOG.debug("ErrorCode:{}", jsonResponse.getCode());
		}
		return jsonResponse;
	}

	public Object getBody() {
		return body;
	}

	public ServerHttpResponse getResponse() {
		return response;
	}

}
