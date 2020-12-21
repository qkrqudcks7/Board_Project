package com.board.myboard.repository;

import com.board.myboard.domain.Content;
import com.board.myboard.form.BoardForm;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ContentRepository extends JpaRepository<Content,Long> {

}
