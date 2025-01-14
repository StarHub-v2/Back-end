package com.example.starhub.service;

import com.example.starhub.code.ErrorCode;
import com.example.starhub.dto.post.PostResponseDto;
import com.example.starhub.dto.user.UserLoginDTO;
import com.example.starhub.dto.user.UserRegisterDTO;
import com.example.starhub.entity.PostEntity;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.exception.LoginIdNotFoundException;
import com.example.starhub.exception.LoginPasswordNotMatchException;
import com.example.starhub.projection.user.GetUser;
import com.example.starhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    //김가빈이 임의로 주석처리한거임
//    @Value("${upload.dir}")
//    private String uploadDir;

    @Override
    public GetUser register(UserRegisterDTO userRegisterDTO) {

        //            byte[] imageBytes = image.getBytes();

        UserEntity user = UserEntity.builder()
                .loginId(userRegisterDTO.getLoginId())
                .password(userRegisterDTO.getPassword())
                .name(userRegisterDTO.getName())
                .age(userRegisterDTO.getAge())
                .email(userRegisterDTO.getEmail())
                .phoneNum(userRegisterDTO.getPhoneNum())
                .introduction(userRegisterDTO.getIntroduction())
//                    .imageData(imageBytes)
                .build();

        UserEntity createUser = userRepository.save(user);
        return EntityToProjectionUser(createUser);

    }

    @Override
    public GetUser login(UserLoginDTO userLoginDTO) {
        Optional<UserEntity> findUser = userRepository.findByLoginId(userLoginDTO.loginId);

        // 아이디 존재하는지 확인
        if(!findUser.isPresent()) throw new LoginIdNotFoundException(ErrorCode.USERID_NOT_FOUND);
            // 비밀번호가 같은지 확인
        else if(!findUser.get().getPassword().equals(userLoginDTO.password)) throw new LoginPasswordNotMatchException(ErrorCode.PASSWORD_NOT_MATCH);


        GetUser user = EntityToProjectionUser(findUser.get());
        return user;
    }

    @Override
    public void logout(String loginId) {
        Optional<UserEntity> optionalUser = userRepository.findByLoginId(loginId);
        optionalUser.ifPresent(user -> {
            userRepository.delete(user);
        });
    }


    private GetUser EntityToProjectionUser(UserEntity user){
        GetUser userInfo = new GetUser() {

            @Override
            public Integer getUserId() {
                return user.getUserId();
            }

            @Override
            public String getLoginId() {
                return user.getLoginId();
            }

            @Override
            public String getName() {
                return user.getName();
            }

            @Override
            public Integer getAge() {
                return user.getAge();
            }

            @Override
            public String getEmail() {
                return user.getEmail();
            }

            @Override
            public String getPhoneNum() {
                return user.getPhoneNum();
            }

            @Override
            public String getIntroduction() {
                return user.getIntroduction();
            }

//            @Override
//            public byte[] getImageData() {
//                return user.getImageData();
//            }
        };

        return userInfo;
    }




}

