package com.swa2502.backend.exception.jwt;

import com.swa2502.backend.exception.CustomException;
import com.swa2502.backend.exception.ErrorCode;
public class JwtSignatureException extends CustomException {
    public JwtSignatureException() {
        super(ErrorCode.JWT_SIGNATURE_ERROR);
    }
}
