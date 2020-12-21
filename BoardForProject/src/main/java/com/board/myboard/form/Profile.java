package com.board.myboard.form;

import com.board.myboard.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Profile {

    private String bio;

    private String job;

    public Profile(Account account) {
        this.bio = account.getBio();
        this.job = account.getJob();
    }
}
