package api.web.entities;

import lombok.Data;
import java.util.*;
import javax.persistence.*;

@Data
@Entity
@Table(name = "genero")
public class Genero {

    @Id
    @Column(name = "id_genero", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 80, nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_imagen", nullable = false, updatable = false)
    private Imagen imagen;

    @OneToMany(mappedBy = "genero", cascade = CascadeType.ALL)
    private List<Pelicula> peliculas;

    public Genero() {
        this.peliculas = new ArrayList<>();
    }

}
