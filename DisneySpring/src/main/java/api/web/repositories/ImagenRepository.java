package api.web.repositories;

import javax.persistence.*;
import api.web.entities.Imagen;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

@Repository
@ApplicationScope
public class ImagenRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Imagen obtener(int id) {
        return this.entityManager.find(Imagen.class, id);
    }

    @Transactional
    public void guardar(Imagen imagen) {
        if (imagen != null) {
            if (this.obtener(imagen.getId()) == null) {
                this.entityManager.persist(imagen);
            } else {
                this.entityManager.merge(imagen);
            }
        }
    }

    @Transactional
    public void eliminar(Imagen imagen) {
        if (imagen != null) {
            this.entityManager.remove(imagen);
        }
    }

}
