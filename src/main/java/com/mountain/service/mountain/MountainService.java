package com.mountain.service.mountain;

import java.util.Map;

public interface MountainService {
    Map<String, Object> listMountain(String id);

    Map<String, Object> listBasecamp(String mountainId);

    Map<String, Object> listRanger(String mountainId);

    Map<String, Object> listStatusMountain(String mountainId);
}
