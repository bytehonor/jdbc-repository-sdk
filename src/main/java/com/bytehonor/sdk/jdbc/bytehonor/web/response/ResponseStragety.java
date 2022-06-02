package com.bytehonor.sdk.jdbc.bytehonor.web.response;

import org.springframework.http.server.ServerHttpRequest;

import com.bytehonor.sdk.define.bytehonor.result.JsonResponse;

public interface ResponseStragety {

	JsonResponse<?> process(ServerHttpRequest request);
}
