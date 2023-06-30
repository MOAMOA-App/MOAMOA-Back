package org.zerock.moamoa.common.exception.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zerock.moamoa.common.exception.BusinessException;

@RestControllerAdvice
public class ControllerHandler {
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Map<String, String>> handleMyException(BusinessException ex) {
		Map<String, String> map = new HashMap<>();
		map.put("message", ex.getErrorCode().getMessage());
		return new ResponseEntity<>(map, ex.getErrorCode().getHttpStatus());
	}
}
