package com.mountain.library.domain;

public enum ErrCode {

    INF_CONTINUE(100, "Continue."),
    INF_SWITCH(101, "Switching protocols."),
    INF_PROCESS(102, "Processing."),

    SUCCESS(200, "OK."),
    CREATED(201, "Created."),
    ACCEPTED(202, "Accepted."),
    NON_AUTHORITATIVE(203, "Non-Authoritative Information."),
    NO_CONTENT(204, "No Content."),
    RESET_CONTENT(205, "Reset Content."),
    PARTIAL_CONTENT(206, "Partial Content."),
    MULTI_STATUS(207, "Multi Status."),
    ALREADY_REPORTED(208, "Already Reported."),
    IM_USED(226, "I'm Used."),

    MULTIPLE_CHOICE(300, "Multiple Choice."),
    MOVED_PERMANENT(301, "Move Permanently."),
    FOUND(302, "Found."),
    SEE_OTHER(303, "See Other."),
    NOT_MODIFIED(304, "Not Modified."),
    USE_PROXY(305, "Use Proxy."),
    UNUSED(306, "Unused."),
    TEMPORARY_REDIRECT(307, "Temporary Redirect."),
    PERMANENT_REDIRECT(308, "Permanent Redirect."),

    BAD_REQUEST(400, "Bad Request."),
    UNAUTHORIZED(401, "Unauthorized."),
    PAYMENT_REQUIRED(402, "Payment Required."),
    FORBIDDEN(403, "Forbidden."),
    NOT_FOUND(404, "Not Found."),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed."),
    NOT_ACCEPTABLE(406, "Not Acceptable."),
    PROXY_AUTH_REQUIRED(407, "Proxy Authentication Required."),
    REQUEST_TIMEOUT(408, "Request Timeout."),
    CONFLICT(409, "Conflict."),
    GONE(410, "Gone."),
    LENGTH_REQUIRED(411, "Length Required."),
    PRECONDITION_FAILED(412, "Precondition Failed."),
    REQUEST_TO_LARGE(413, "Request Entity to Large."),
    REQUEST_URI_LONG(414, "Request-URI to Long."),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type."),
    REQUEST_NOT_SATISFIABLE(416, "Request Range Not Satisfiable."),
    EXPECTATION_FAILED(417, "Expectation Failed."),
    TEAPOT(418, "I'm a Teapot."),
    ENHANCE_YOUR_CALM(420, "Enhance Your Calm."),
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity."),
    LOCKED(423, "Locked."),
    FAILED_DEPENDENCY(424, "Failed Dependency."),
    UPGRADE_REQUIRED(426, "Upgrade Required."),
    PRECONDITION_REQUIRED(428, "Precondition Required."),
    TOO_MANY_REQUEST(429, "Too Many Request."),
    REQUEST_HEADER_TOO_LARGE(431, "Request Header Fields Too Large."),
    NO_RESPONSE(444, "No Response."),
    RETRY(449, "Retry."),
    BLOCKED_WINDOWS_PARENTAL(450, "Blocked by Windows Parental Controls."),
    UNAVAILABLE_LEGAL_REASON(451, "Unavailable For Legal Reasons."),
    CLIENT_CLOSE_REQUEST(499, "Client Close Request."),

    SERVER_ERROR(500, "Internal Server Error."),
    NOT_IMPLEMENTED(501, "Not Implemented."),
    BAD_GATEWAY(502, "Bad Gateway."),
    SERVICE_UNAVAILABLE(503, "Service Unavailable."),
    GATEWAY_TIMEOUT(504, "Gateway Timeout."),
    HTTP_NOT_SUPPORTED(505, "Http Version Not Supported."),
    VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates."),
    INSUFFICIENT_STORAGE(507, "Insufficient Storage."),
    LOOP_DETECTED(508, "Loop Detected."),
    BANDWIDTH_LIMIT(509, "Bandwidth Limit Exceeded."),
    NOT_EXTENDED(510, "Not Extended."),
    NETWORK_AUTH_REQUIRED(511, "Network Authentication Required."),
    NETWORK_READ_TIMEOUT(598, "Network Read Timeout Error."),
    NETWORK_CONNECT_TIMEOUT(599, "Network Connect Timeout Error."),
    WITH_SPACE(600,"Field cannot with space");


    private int code;
    private String message;

    private ErrCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ErrCode toError(int errCode) {
        ErrCode errCodeMapping = BAD_REQUEST;
        ErrCode[] values = ErrCode.values();
        for (ErrCode err : values) {
            if (err.getCode() == errCode) {
                errCodeMapping = err;
            }
            break;
        }
        return errCodeMapping;
    }

    @Override
    public String toString() {
        return "ErrCode{" + "code=" + code + ", message=" + message + '}';
    }
}
