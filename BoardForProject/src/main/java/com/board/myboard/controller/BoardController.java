package com.board.myboard.controller;

import com.board.myboard.domain.Account;
import com.board.myboard.domain.Content;
import com.board.myboard.form.BoardForm;
import com.board.myboard.repository.ContentRepository;
import com.board.myboard.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final ContentRepository contentRepository;
    private final ContentService contentService;

    @GetMapping("/board/main")
    public String main1(Model model,
                       @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Content> contentList = contentRepository.findAll(pageable);
        model.addAttribute("contentList",contentList);
        model.addAttribute("maxPage",10);
        model.addAttribute("check",0);
        return "board/main";
    }


    @GetMapping("/board/write")
    public String write(Model model) {
        model.addAttribute("boardForm", new BoardForm());
        return "board/write";
    }

    @PostMapping("/board/write")
    public String writeUpdate(@CurrentAccount Account account,
                              @Valid BoardForm boardForm , Errors errors,
                              @RequestParam("file") MultipartFile files) throws IOException {
        if (errors.hasErrors()){
            return "board/write";
        }

        if (files.getSize()>0) {
            String baseDir = "C:\\Users\\박병찬\\IdeaProjects\\BoardForProject\\src\\main\\resources\\static\\images";
            String filePath = baseDir + "\\" + files.getOriginalFilename();
            files.transferTo(new File(filePath));
            boardForm.setImgUrl(filePath);
        }

        contentService.write(account,boardForm);
        return "redirect:/board/main";
    }

    @GetMapping("/board/read")
    public String read(@RequestParam("id") Long id,Model model,
                         @CurrentAccount Account account) {
        Content content = contentRepository.findById(id).get();
        model.addAttribute("content",content);
        model.addAttribute("id",id);
        model.addAttribute("account",account);
        String url = content.getUrl();
        if (url == null){
            return "board/read";
        }
        String realUrl = url.substring(67);
        model.addAttribute("realUrl",realUrl);

        return "board/read";
    }

    @GetMapping("/board/modify")
    public String modify(@RequestParam("id") Long id,Model model) {
        Content content = contentRepository.findById(id).get();

        BoardForm boardForm = new BoardForm();
        boardForm.setSubject(content.getSubject());
        boardForm.setText(content.getText());
        boardForm.setImgUrl(content.getUrl());

        String url = content.getUrl();
        if (url == null){
            return "board/modify";
        }
        String realUrl = url.substring(67);
        model.addAttribute("realUrl",realUrl);

        model.addAttribute("content",content);
        model.addAttribute("boardForm",boardForm);
        model.addAttribute("id",id);

        return "board/modify";
    }

    @PostMapping("/board/modify")
    public String modifyUpdate(@RequestParam("id") Long id,
                               @ModelAttribute Content content,
                               @Valid BoardForm boardForm, Errors errors,
                               @RequestParam("file") MultipartFile files) throws IOException {
        if (errors.hasErrors()){
            return "board/modify";
        }
        if (files.getSize()>0) {
            String baseDir = "C:\\Users\\박병찬\\IdeaProjects\\BoardForProject\\src\\main\\resources\\static\\images";
            String filePath = baseDir + "\\" + files.getOriginalFilename();
            files.transferTo(new File(filePath));
            boardForm.setImgUrl(filePath);
        }

        contentService.updateBoard(id,boardForm);

        return "redirect:/board/main";
    }

    @GetMapping("/board/delete")
    public String delete(@RequestParam("id") Long id){
        contentRepository.deleteById(id);

        return "board/delete";
    }

}
