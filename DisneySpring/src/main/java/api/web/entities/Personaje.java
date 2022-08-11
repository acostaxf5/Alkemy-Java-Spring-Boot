package api.web.entities;

import lombok.Data;
import java.util.*;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.*;

@Data
@Entity
@Table(name = "personaje")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Personaje {

    @Id
    @Column(name = "id_personaje", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_imagen", nullable = false, updatable = false)
    private Imagen imagen;

    @Column(length = 80, nullable = false)
    private String nombre;

    @Column(nullable = false)
    private int edad;

    @Column(nullable = false)
    private double peso;

    @Column(nullable = false)
    private String historia;

    @ManyToMany
    @JoinTable(name = "personaje_pelicula", joinColumns = { @JoinColumn(name = "id_personaje") }, inverseJoinColumns = { @JoinColumn(name = "id_pelicula") })
    private List<Pelicula> peliculas;

    public Personaje() {
        this.peliculas = new ArrayList<>();
    }

    public void addPelicula(Pelicula pelicula) {
        if (pelicula != null) {
            this.peliculas.add(pelicula);
        }
    }

    public void removePelicula(Pelicula pelicula) {
        if (pelicula != null) {
            this.peliculas.remove(pelicula);
        }
    }

}
