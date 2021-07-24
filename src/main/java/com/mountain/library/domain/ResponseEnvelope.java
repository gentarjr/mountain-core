package com.mountain.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.collections4.MapUtils;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseEnvelope extends RepresentationModel<ResponseEnvelope> implements Serializable {
    enum META {
        code, message;
    }

    private Map<String, String> meta;
    private Map<String, Object> data;

    public ResponseEnvelope() {
        meta = new HashMap<>();
        data = new HashMap<>();
    }

    public ResponseEnvelope(int code, String message) {
        this();
        setCode(code + "");
        setMessage(message);
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Map<String, String> getMeta() {
        return meta;
    }

    @JsonIgnore
    public String getCode() {
        return MapUtils.getString(meta, META.code.name());
    }

    @JsonIgnore
    public int getCodeAsInt() {
        return MapUtils.getIntValue(meta, META.code.name(), 200);
    }

    public void setCode(String code) {
        meta.put(META.code.name(), code);
    }

    public void setCode(int code) {
        meta.put(META.code.name(), code + "");
    }

    @JsonIgnore
    public String getMessage() {
        return MapUtils.getString(meta, META.message.name());
    }

    public void setMessage(String message) {
        meta.put(META.message.name(), message);
    }


    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void putData(String key, Object value) {
        data.put(key, value);
    }

    public Object removeData(String key) {
        return data.remove(key);
    }

    public Object get(String key) {
        return data == null ? null : data.get(key);
    }

    @Override
    public String toString() {
        return "ResponseEnvelope{" + "meta=" + meta + '}';
    }
}
