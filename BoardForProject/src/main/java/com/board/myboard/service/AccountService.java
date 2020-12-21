package com.board.myboard.service;

import com.board.myboard.controller.UserAccount;
import com.board.myboard.domain.Account;
import com.board.myboard.form.BoardForm;
import com.board.myboard.form.JoinForm;
import com.board.myboard.form.Profile;
import com.board.myboard.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Account saveNewAccount(JoinForm joinForm) {
        Account account = Account.builder()
                .email(joinForm.getEmail())
                .password(passwordEncoder.encode(joinForm.getPassword()))
                .nickname(joinForm.getNickname())
                .build();

        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email);

        if (account == null) {
            throw new UsernameNotFoundException(email);
        }

        return new UserAccount(account);
    }

    public void updateProfile(Account account, Profile profile) {
        account.setBio(profile.getBio());
        account.setJob(profile.getJob());
        accountRepository.save(account);
    }
}
