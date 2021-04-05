package com.zxd.inter.controller;

import com.zxd.inter.common.Const;
import com.zxd.inter.pojo.User;
import com.zxd.inter.service.IUserService;
import com.zxd.inter.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/portal/")
public class UserController {

    //登录
    @Autowired
    IUserService userService;
    @RequestMapping(value="user/login.do")
    public ServerResponse login(String username, String password , HttpSession session){
        ServerResponse serverResponse = userService.loginLogic(username,password);
        if(serverResponse.isSucess()){
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }
        return serverResponse;

    }
    @RequestMapping(value="user/register.do")
    public ServerResponse login(User user){

        return userService.registerLogic(user);

    }

}
