package org.zerock.moamoa.common.exception;

public class FileProcessingException extends BusinessException {
    public FileProcessingException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }

    public FileProcessingException(ErrorCode errorCode) {
        super(errorCode);
    }
}
