package com.board.myboard.form;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class JoinForm {

    @NotBlank
    @Size(min = 3,max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
    private String nickname;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 4,max = 50)
    private String password;

    @NotBlank
    @Size(min = 4,max = 50)
    private String passwordConfirm;


}
