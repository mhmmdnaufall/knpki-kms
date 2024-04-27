package org.knpkid.kms.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "quotes")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Author author;

}
