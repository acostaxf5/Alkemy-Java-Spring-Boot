package api.security.repositories;

import javax.persistence.*;
import api.security.entities.Usuario;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

@Repository
@ApplicationScope
public class UsuarioRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Usuario obtener(int id) {
        return this.entityManager.find(Usuario.class, id);
    }

    public Usuario obtenerPorNombre(String correo) {
        return this.entityManager.createQuery("select u from Usuario u where u.correo = :correo", Usuario.class).setParameter("correo", correo).getSingleResult();
    }

    @Transactional
    public void guardar(Usuario usuario) {
        if (usuario != null) {
            if (this.obtener(usuario.getId()) == null) {
                this.entityManager.persist(usuario);
            } else {
                this.entityManager.merge(usuario);
            }
        }
    }

    @Transactional
    public void eliminar(Usuario usuario) {
        if (usuario != null) {
            this.entityManager.remove(usuario);
        }
    }

}
