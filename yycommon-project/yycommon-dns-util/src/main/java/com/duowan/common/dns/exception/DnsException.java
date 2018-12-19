package com.duowan.common.dns.exception;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/18 17:09
 */
public class DnsException extends RuntimeException {

    public DnsException() {
    }

    public DnsException(String message) {
        super(message);
    }

    public DnsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DnsException(Throwable cause) {
        super(cause);
    }
}
