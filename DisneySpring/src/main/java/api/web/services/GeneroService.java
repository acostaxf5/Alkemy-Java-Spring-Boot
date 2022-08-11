package api.web.services;

import java.util.List;
import api.web.entities.*;
import api.web.repositories.GeneroRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.ApplicationScope;

@Service
@ApplicationScope
public class GeneroService {

    private final GeneroRepository generoRepository;

    @Autowired
    public GeneroService(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    public List<Genero> obtenerTodos() {
        return this.generoRepository.obtenerTodos();
    }

    public Genero obtener(int id) {
        return this.generoRepository.obtener(id);
    }

    public Genero guardar(Genero genero) {
        if (genero == null) {
            return null;
        }

        int id = genero.getId();
        String nombre = genero.getNombre();
        Imagen imagen = genero.getImagen();

        if (id < 0 || nombre.equals("") || imagen == null) {
            return null;
        }

        this.generoRepository.guardar(genero);
        return this.generoRepository.obtener(genero.getId());
    }

    public Genero eliminar(int id) {
        Genero genero = this.generoRepository.obtener(id);

        if (genero != null) {
            this.generoRepository.eliminar(genero);
        }

        return genero;
    }

}
