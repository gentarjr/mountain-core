package com.mountain.service.internal;

import com.mountain.advice.MountainCoreServiceFeignClientConfig;
import com.mountain.domain.form.RequestMountainForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "mountain-notification", url = "${mountain.service.local.url}", configuration = {MountainCoreServiceFeignClientConfig.class})
public interface MountainServiceClient {

    @PostMapping("/v1/notification/registered-mountain")
    Map<String, Object> notificationRegisterClimber(@RequestBody RequestMountainForm requestMountainForm);

    @PostMapping("/v1/notification/edit-status-mountain")
    Map<String, Object> notificationUpdateStatusClimber(@RequestBody RequestMountainForm requestMountainForm);

    @PostMapping("/v1/notification/edit-status-transaction")
    Map<String, Object> notificationUpdateStatusTransaction(@RequestBody RequestMountainForm requestMountainForm);
}
