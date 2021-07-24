package com.mountain.advice;

import com.mountain.library.domain.ErrCode;
import com.mountain.library.domain.RespEnvelope;
import com.mountain.library.exceptions.WinterfellException;
import com.mountain.library.helper.MarshallingUtils;
import feign.Contract;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class MountainCoreServiceFeignClientConfig {
    private final ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public Decoder springDecoder() {
        return new ResponseEntityDecoder(new SpringDecoder(messageConverters));
    }

    @Bean
    public Contract feignContract() {
        return new SpringMvcContract();
    }

    public ErrorDecoder errorDecoder(Decoder decoder) {
        return (String methodKey, Response response) -> {
            try {
                log.warn("Request to [{}] returned status {}", methodKey, response.status());
                if (response.body() != null) {
                    RespEnvelope respEnvelope = (RespEnvelope) decoder.decode(response, RespEnvelope.class);
                    MarshallingUtils.printJson(respEnvelope);
                    ErrCode errCode = ErrCode.toError(Integer.parseInt(respEnvelope.getMeta().getCode()));
                    String errMessage = respEnvelope.getMeta().getMessage();
                    return new WinterfellException(errCode, errMessage);
                }
                return new WinterfellException(ErrCode.ERR_UNKNOWN, ErrCode.ERR_UNKNOWN.getMessage());
            } catch (Exception e) {
                return e;
            }
        };
    }
}
