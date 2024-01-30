package org.knpkid.kms.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String username;

    private String password;

    private byte[] image;

    private String name;

    @OneToMany(mappedBy = "admin")
    private List<Article> articles;

}
