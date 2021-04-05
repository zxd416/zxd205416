package com.zxd.inter.service;

import com.zxd.inter.pojo.User;
import com.zxd.inter.utils.ServerResponse;

public interface IUserService {
    /**
     * 登录
     */
    public ServerResponse loginLogic(String username,String password);

    /**
     * 注冊
     */
    public ServerResponse registerLogic(User user);
}
