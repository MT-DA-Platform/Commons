package com.MTDap.commons.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MTDapException extends Exception {

    private static final Logger logger = LoggerFactory.getLogger(MTDapException.class);

    public MTDapException(String message) {
        super(message);
        logger.info("Exception message : " + message);
    }

    public MTDapException(String message, Throwable cause) {
        super(message, cause);
        logger.info("Exception message : " + message + "| Cause : " + cause);
    }

    public MTDapException(Throwable cause) {
        super(cause);
        logger.info("Cause : " + cause);
    }

    public MTDapException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        logger.info("Exception message : " + message + "| Cause : " + cause);
    }
}
