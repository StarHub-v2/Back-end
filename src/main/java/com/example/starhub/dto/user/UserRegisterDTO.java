package com.example.starhub.dto.user;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {

    @NotNull
    private String loginId;
    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    private int age;

    @NotNull
    private String email;

    @NotNull
    private String phoneNum;

    @NotNull
    private String introduction;

}
