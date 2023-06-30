package org.zerock.moamoa.common.message;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

public class OkResponse {

	private final SuccessMessage message;

	public OkResponse(SuccessMessage message) {
		this.message = message;
	}

	public ResponseEntity<Map<String, String>> makeAnswer() {
		Map<String, String> map = new HashMap<>();
		map.put("message", message.getMessage());
		return new ResponseEntity<>(map, message.getHttpStatus());
	}
}
