package com.mountain.library.domain;

public enum ErrCode {

    ERR_SERVER(500,"Internal Server Error."),
    ERR_UNKNOWN(2001,"Terjadi kesalahan. Silahkan coba beberapa saat lagi."),
    ERR_NOTFOUND(404, "Server tidak tersedia."),
    ERR_METHOD(405, "Method tidak tersedia."),
    ERR_TIMEOUT(408, "Request timeout."),
    ERR_TIMELONG(414, "Jaringan sedang lambat"),
    ERR_MANYREQ(429, "Terlalu banyak request."),
    ERR_BADCRADENTIAL(410, "Bad Credentials"),
    ERR_FORBIDDEN(2002, "Operasi tidak diijinkan"),

    PROCESSING(102, "Request sedang diproses."),
    SUCCESS(200, "Sukses"),

    INF_USEREMPTY(100,"User tidak terdaftar."),
    INF_USERNOTVERIFIED(101, "User belum diverfikasi."),
    INF_USERSUSPENDED(202, "User disuspend."),
    INF_USERNOTEMPTY(204, "User sudah terdaftar."),

    INF_FIELDEMPTY(200, "Field tidak boleh kosong"),
    INF_FIELDSPACE(201, "Field mengandung spasi"),
    INF_FIELDINVALID(4003, "Field tidak valid");

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
        ErrCode errCodeMapping = ERR_UNKNOWN;
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
