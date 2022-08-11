package api.web.entities;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "imagen")
public class Imagen {

    @Id
    @Column(name = "id_imagen", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Lob
    @Column(nullable = false)
    private byte[] datos;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 80, nullable = false)
    private String tipo;

}
