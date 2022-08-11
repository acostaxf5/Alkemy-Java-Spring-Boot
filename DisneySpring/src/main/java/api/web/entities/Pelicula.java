package api.web.entities;

import lombok.Data;
import java.util.*;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.*;

@Data
@Entity
@Table(name = "pelicula")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Pelicula {

    @Id
    @Column(name = "id_pelicula", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_imagen", nullable = false, updatable = false)
    private Imagen imagen;

    @Column(length = 80, nullable = false)
    private String titulo;

    @Column(name = "fecha_creacion", nullable = false)
    private Date fechaCreacion;

    @Column(nullable = false)
    private int calificacion;

    @ManyToOne
    @JoinColumn(name = "id_genero", nullable = false, updatable = false)
    private Genero genero;

    @ManyToMany(mappedBy = "peliculas")
    private List<Personaje> personajes;

    public Pelicula() {
        this.personajes = new ArrayList<>();
    }

    public void addPersonaje(Personaje personaje) {
        if (personaje != null) {
            this.personajes.add(personaje);
        }
    }

    public void removePersonaje(Personaje personaje) {
        if (personaje != null) {
            this.personajes.remove(personaje);
        }
    }

}
