package api.web.entities.dto;

import lombok.Data;
import javax.persistence.*;
import api.web.entities.Imagen;

@Data
@Table(name = "personaje")
public class PersonajeDTO {

    @ManyToOne
    @JoinColumn(name = "id_imagen", nullable = false, updatable = false)
    private Imagen imagen;

    @Column(length = 80, nullable = false)
    private String nombre;

    public PersonajeDTO(Imagen imagen, String nombre) {
        this.imagen = imagen;
        this.nombre = nombre;
    }

}
