package me.lukaszpisarczyk.Hospital.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
@Getter
@Setter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String type;

    @Lob
    private byte[] imageData;
}
