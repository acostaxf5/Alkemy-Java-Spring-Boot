package api.web.repositories;

import java.util.List;
import javax.persistence.*;
import api.web.entities.Pelicula;
import javax.transaction.Transactional;
import api.web.entities.dto.PeliculaDTO;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

@Repository
@ApplicationScope
public class PeliculaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PeliculaDTO> obtenerTodos() {
        return this.entityManager.createQuery("select new api.web.entities.dto.PeliculaDTO(p.imagen, p.titulo, p.fechaCreacion) from Pelicula p", PeliculaDTO.class).getResultList();
    }

    public List<PeliculaDTO> obtenerPorTitulo(String titulo) {
        return this.entityManager.createQuery("select new api.web.entities.dto.PeliculaDTO(p.imagen, p.titulo, p.fechaCreacion) from Pelicula p where p.titulo = :titulo", PeliculaDTO.class).setParameter("titulo", titulo).getResultList();
    }

    public List<PeliculaDTO> obtenerPorGeneroOrdenado(int idGenero, boolean orden) {
        String query = "select new api.web.entities.dto.PeliculaDTO(p.imagen, p.titulo, p.fechaCreacion) from Pelicula p where p.genero.id = :id order by p.fechaCreacion " + (orden ? "asc" : "desc");

        return this.entityManager.createQuery(query, PeliculaDTO.class).setParameter("id", idGenero).getResultList();
    }

    public Pelicula obtener(int id) {
        return this.entityManager.find(Pelicula.class, id);
    }

    @Transactional
    public void guardar(Pelicula pelicula) {
        if (pelicula != null) {
            if (this.obtener(pelicula.getId()) == null) {
                this.entityManager.persist(pelicula);
            } else {
                this.entityManager.merge(pelicula);
            }
        }
    }

    @Transactional
    public void eliminar(Pelicula pelicula) {
        if (pelicula != null) {
            this.entityManager.remove(pelicula);
        }
    }

}
