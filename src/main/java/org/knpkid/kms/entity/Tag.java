package org.knpkid.kms.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    private String id;

    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Article> articles;

}
