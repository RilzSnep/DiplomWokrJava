package ru.skypro.homework.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "images")
public class ImageEntity {

    @Id
    @Column(name = "id", length = 255)
    private String id;

    // УБЕРИ @Lob для PostgreSQL!
    @Column(name = "image", columnDefinition = "bytea")
    private byte[] image;

    @Column(name = "media_type", length = 50)
    private String mediaType;

    // Добавь конструкторы
    public ImageEntity() {}

    public ImageEntity(String id, byte[] image, String mediaType) {
        this.id = id;
        this.image = image;
        this.mediaType = mediaType;
    }
}