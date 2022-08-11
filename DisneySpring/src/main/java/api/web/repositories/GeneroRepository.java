package api.web.repositories;

import java.util.List;
import javax.persistence.*;
import api.web.entities.Genero;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

@Repository
@ApplicationScope
public class GeneroRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Genero> obtenerTodos() {
        return this.entityManager.createQuery("select g from Genero g", Genero.class).getResultList();
    }

    public Genero obtener(int id) {
        return this.entityManager.find(Genero.class, id);
    }

    @Transactional
    public void guardar(Genero genero) {
        if (genero != null) {
            if (this.obtener(genero.getId()) == null) {
                this.entityManager.persist(genero);
            } else {
                this.entityManager.merge(genero);
            }
        }
    }

    @Transactional
    public void eliminar(Genero genero) {
        if (genero != null) {
            this.entityManager.remove(genero);
        }
    }

}
