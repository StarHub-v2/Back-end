package com.example.starhub.service;

import com.example.starhub.dto.user.UserLoginDTO;
import com.example.starhub.dto.user.UserRegisterDTO;
import com.example.starhub.projection.user.GetUser;

public interface UserService {

    GetUser register(UserRegisterDTO userRegisterDTO);
    GetUser login(UserLoginDTO userLoginDTO);

    void logout(String loginId);

}
