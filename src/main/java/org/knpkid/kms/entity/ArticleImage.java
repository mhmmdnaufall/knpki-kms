package org.knpkid.kms.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "article_images")
public class ArticleImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private Article article;

}
