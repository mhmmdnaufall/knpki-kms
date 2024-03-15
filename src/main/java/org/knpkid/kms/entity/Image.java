package org.knpkid.kms.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "images")
public class Image {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private ImageFormat format;

    /**
     * @return image file name
     */
    @Override
    public String toString() {
        return "%s.%s".formatted(id, format.name().toLowerCase());
    }

}
