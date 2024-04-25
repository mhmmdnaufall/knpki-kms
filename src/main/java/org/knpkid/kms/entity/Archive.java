package org.knpkid.kms.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "archives")
public class Archive {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private ArchiveFormat format;

}
