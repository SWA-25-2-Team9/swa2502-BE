package com.swa2502.backend.exception.jwt;

import com.swa2502.backend.exception.CustomException;
import com.swa2502.backend.exception.ErrorCode;
public class JwtInvalidException extends CustomException {
    public JwtInvalidException() {
        super(ErrorCode.JWT_INVALID);
    }
}
