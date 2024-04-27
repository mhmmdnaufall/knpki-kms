package org.knpkid.kms.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToMany(mappedBy = "authors")
    private List<Article> articles;

    @OneToMany(mappedBy = "author")
    private List<Quote> quotes;

}
