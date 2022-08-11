package api.web.entities.dto;

import lombok.Data;
import java.util.Date;
import javax.persistence.*;
import api.web.entities.Imagen;

@Data
@Table(name = "pelicula")
public class PeliculaDTO {

    @ManyToOne
    @JoinColumn(name = "id_imagen", nullable = false, updatable = false)
    private Imagen imagen;

    @Column(length = 80, nullable = false)
    private String titulo;

    @Column(name = "fecha_creacion", nullable = false)
    private Date fechaCreacion;

    public PeliculaDTO(Imagen imagen, String titulo, Date fechaCreacion) {
        this.imagen = imagen;
        this.titulo = titulo;
        this.fechaCreacion = fechaCreacion;
    }

}
