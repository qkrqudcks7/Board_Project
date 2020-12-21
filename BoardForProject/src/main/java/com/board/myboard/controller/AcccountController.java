package com.board.myboard.controller;

import com.board.myboard.customValidation.JoinValidator;
import com.board.myboard.domain.Account;
import com.board.myboard.domain.Content;
import com.board.myboard.form.JoinForm;
import com.board.myboard.form.Profile;
import com.board.myboard.repository.AccountRepository;
import com.board.myboard.repository.ContentRepository;
import com.board.myboard.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AcccountController {

    private final ContentRepository contentRepository;
    private final AccountService accountService;
    private final JoinValidator joinValidator;

    @InitBinder("joinForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(joinValidator);
    }

    @GetMapping("/")
    public String home(Model model,
                       @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Content> contentList = contentRepository.findAll(pageable);
        model.addAttribute("contentList",contentList);
        return "index";
    }

    @GetMapping("/account/join")
    public String join(Model model) {
        model.addAttribute("joinForm",new JoinForm());
        return "account/join";
    }

    @PostMapping("/account/join")
    public String joinUpdate(@Valid JoinForm joinForm, Errors errors) {
        if (errors.hasErrors()){
            return "account/join";
        }
        accountService.saveNewAccount(joinForm);

        return "redirect:/";
    }

    @GetMapping("/account/login")
    public String login() {
        return "account/login";
    }

    @GetMapping("/account/modify")
    public String modify(@CurrentAccount Account account,Model model) {

        model.addAttribute("account",account);
        model.addAttribute(new Profile(account));

        return "account/modify";
    }

    @PostMapping("/account/modify")
    public String modifyUpdate(@CurrentAccount Account account,
                               @ModelAttribute Profile profile, Model model,
                               RedirectAttributes attributes) {
        model.addAttribute("account",account);
        accountService.updateProfile(account,profile);

        attributes.addFlashAttribute("message","프로필을 수정했습니다.");

        return "redirect:/account/modify";
    }
}
