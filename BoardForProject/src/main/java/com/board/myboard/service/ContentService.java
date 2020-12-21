package com.board.myboard.service;

import com.board.myboard.domain.Account;
import com.board.myboard.domain.Content;
import com.board.myboard.form.BoardForm;
import com.board.myboard.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional
public class ContentService {

    private final ContentRepository contentRepository;

    public Content write(Account account, BoardForm boardForm) {
        Content content=new Content();
        content.setAccount(account);
        content.setSubject(boardForm.getSubject());
        content.setText(boardForm.getText());
        content.setUrl(boardForm.getImgUrl());
        content.setLocalDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        contentRepository.save(content);
        return content;
    }

    public void updateBoard(Long id,BoardForm boardForm) {
        Content content = contentRepository.findById(id).get();

        content.setText(boardForm.getText());
        content.setSubject(boardForm.getSubject());
        content.setUrl(boardForm.getImgUrl());

        contentRepository.save(content);
    }
}
