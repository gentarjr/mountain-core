package com.mountain.service.user;

import java.util.Map;

public interface UserService {
    Map<String, Object> listStatus(String id);

    Map<String, Object> listStatusUser(String id);
}
