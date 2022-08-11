package api.web.repositories;

import java.util.List;
import javax.persistence.*;
import api.web.entities.Personaje;
import javax.transaction.Transactional;
import api.web.entities.dto.PersonajeDTO;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

@Repository
@ApplicationScope
public class PersonajeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PersonajeDTO> obtenerTodos() {
        return this.entityManager.createQuery("select new api.web.entities.dto.PersonajeDTO(p.imagen, p.nombre) from Personaje p", PersonajeDTO.class).getResultList();
    }

    public List<PersonajeDTO> obtenerPorNombre(String nombre) {
        return this.entityManager.createQuery("select new api.web.entities.dto.PersonajeDTO(p.imagen, p.nombre) from Personaje p where p.nombre = :nombre", PersonajeDTO.class).setParameter("nombre", nombre).getResultList();
    }

    public List<PersonajeDTO> obtenerTodosPorEdad(int edad) {
        return this.entityManager.createQuery("select new api.web.entities.dto.PersonajeDTO(p.imagen, p.nombre) from Personaje p where p.edad = :edad", PersonajeDTO.class).setParameter("edad", edad).getResultList();
    }

    public List<PersonajeDTO> obtenerTodosPorPeso(double peso) {
        return this.entityManager.createQuery("select new api.web.entities.dto.PersonajeDTO(p.imagen, p.nombre) from Personaje p where p.peso = :peso", PersonajeDTO.class).setParameter("peso", peso).getResultList();
    }

    public List<PersonajeDTO> obtenerTodosPorIdPelicula(int idPelicula) {
        return this.entityManager.createQuery("select new api.web.entities.dto.PersonajeDTO(per.imagen, per.nombre) from Personaje per join per.peliculas pel where pel.id = :id", PersonajeDTO.class).setParameter("id", idPelicula).getResultList();
    }

    public Personaje obtener(int id) {
        return this.entityManager.find(Personaje.class, id);
    }

    @Transactional
    public void guardar(Personaje personaje) {
        if (personaje != null) {
            if (this.obtener(personaje.getId()) == null) {
                this.entityManager.persist(personaje);
            } else {
                this.entityManager.merge(personaje);
            }
        }
    }

    @Transactional
    public void eliminar(Personaje personaje) {
        if (personaje != null) {
            this.entityManager.remove(personaje);
        }
    }

}
