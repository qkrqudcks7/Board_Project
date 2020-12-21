package com.board.myboard.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Content {

    @Id @GeneratedValue
    @Column(name = "content_id")
    private Long id;

    private String subject;

    private String text;

    @Column(nullable = true)
    private String url;

    private String localDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "content")
    private List<Comment> comments = new ArrayList<>();
}
