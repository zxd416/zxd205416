package com.zxd.inter.service.impl;

import com.zxd.inter.common.Const;
import com.zxd.inter.common.ResponseCode;
import com.zxd.inter.dao.UserMapper;
import com.zxd.inter.pojo.User;
import com.zxd.inter.service.IUserService;
import com.zxd.inter.utils.DateUtil;
import com.zxd.inter.utils.ServerResponse;
import com.zxd.inter.utils.MD5Utils;
import com.zxd.inter.vo.UserVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {


    @Autowired
    UserMapper userMapper;
    @Override
    public ServerResponse loginLogic(String username, String password) {

        //1.用户名和密码的非空判断
        if(StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EMPTY.getCode(),ResponseCode.USERNAME_NOT_EMPTY.getMsg());
        }
        if(StringUtils.isBlank(password)){
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_NOT_EMPTY.getCode(),ResponseCode.PASSWORD_NOT_EMPTY.getMsg());
        }
        //2.查看用户名是否存在
        Integer count = userMapper.findByUsername(username);
        if(count==0){
            //用户名不存在
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EXISTS.getCode(),ResponseCode.USERNAME_NOT_EXISTS.getMsg());
        }
        //3.根据用户名和密码查询
        User user = userMapper.findByUsernameAndPassword(username, MD5Utils.getMD5Code(password));

        if(user ==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_ERROR.getCode(),ResponseCode.PASSWORD_ERROR.getMsg());
        }


        //4.返回结果
        return ServerResponse.createServerResponseBySucess(convert(user));
    }


    private UserVO convert(User user){
        UserVO userVO=new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setEmail(user.getEmail());
        userVO.setPhone(user.getPhone());
        userVO.setCreateTime(DateUtil.date2String(user.getCreateTime()));
        userVO.setUpdateTime(DateUtil.date2String(user.getUpdateTime()));

        return userVO;
    }
    @Override
    public ServerResponse registerLogic(User user){
        if(user==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMTER_NOT_EMPTY.getCode(),ResponseCode.PARAMTER_NOT_EMPTY.getMsg());
        }
        String username=user.getUsername();
        String password=user.getPassword();
        String email=user.getEmail();
        String question=user.getQuestion();
        String answer=user.getAnswer();
        String phone=user.getPhone();

        if(StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EMPTY.getCode(),ResponseCode.USERNAME_NOT_EMPTY.getMsg());
        }
        if(StringUtils.isBlank(password)){
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_NOT_EMPTY.getCode(),ResponseCode.PASSWORD_NOT_EMPTY.getMsg());
        }
        if(StringUtils.isBlank(email)){
            return ServerResponse.createServerResponseByFail(ResponseCode.Email_NOT_EMPTY.getCode(),ResponseCode.Email_NOT_EMPTY.getMsg());

        }
        if(StringUtils.isBlank(question)){
            return ServerResponse.createServerResponseByFail(ResponseCode.QUESTION_NOT_EMPTY.getCode(),ResponseCode.QUESTION_NOT_EMPTY.getMsg());
        }
        if(StringUtils.isBlank(answer)){
            return ServerResponse.createServerResponseByFail(ResponseCode.ANSWER_NOT_EMPTY.getCode(),ResponseCode.ANSWER_NOT_EMPTY.getMsg());
        }
        if(StringUtils.isBlank(phone)){
            return ServerResponse.createServerResponseByFail(ResponseCode.PHONE_NOT_EMPTY.getCode(),ResponseCode.PHONE_NOT_EMPTY.getMsg());
        }

        //判断用户名是否存在
        Integer count=userMapper.findByUsername(username);
        if(count>0){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_EXISTS.getCode(),ResponseCode.USERNAME_EXISTS.getMsg());

        }
        //判断邮箱是否存在
        Integer email_count=userMapper.findByEmail(email);
        if(count>0){
            return ServerResponse.createServerResponseByFail(ResponseCode.EMAIL_EXISTS.getCode(),ResponseCode.EMAIL_EXISTS.getMsg());

        }
        //注册
        user.setPassword(MD5Utils.getMD5Code(user.getPassword()));
        user.setRole(Const.NORMAL_USER);//设置角色
        Integer result=userMapper.insert(user);
        if(result==0){
            //注册失败
            return ServerResponse.createServerResponseByFail(ResponseCode.REGISTER_FAIL.getCode(),ResponseCode.REGISTER_FAIL.getMsg());

        }
        return ServerResponse.createServerResponseBySucess();
    }
}
