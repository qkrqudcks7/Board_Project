package com.board.myboard.customValidation;

import com.board.myboard.domain.Account;
import com.board.myboard.form.JoinForm;
import com.board.myboard.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class JoinValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return JoinForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        JoinForm joinForm= (JoinForm) target;
        if (accountRepository.existsByEmail(joinForm.getEmail())) {
            errors.rejectValue("email","invalid.email" , new Object[]{joinForm.getEmail()},"이미 사용중인 이메일입니다.");
        }

        if (accountRepository.existsByNickname(joinForm.getNickname())){
            errors.rejectValue("nickname","invalid.nickname",new Object[]{joinForm.getNickname()},"이미 사용중인 닉네임입니다.");
        }

        if (!joinForm.getPassword().equals(joinForm.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm","wrong.value","입력하신 패스워드가 일치하지 않습니다.");
        }
    }
}
