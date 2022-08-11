package api.web.services;

import java.util.List;
import api.web.entities.*;
import api.web.entities.dto.PersonajeDTO;
import org.springframework.stereotype.Service;
import api.web.repositories.PersonajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.ApplicationScope;

@Service
@ApplicationScope
public class PersonajeService {

    private final PersonajeRepository personajeRepository;

    @Autowired
    public PersonajeService(PersonajeRepository personajeRepository) {
        this.personajeRepository = personajeRepository;
    }

    public List<PersonajeDTO> obtenerTodos() {
        return this.personajeRepository.obtenerTodos();
    }

    public List<PersonajeDTO> obtenerPorNombre(String nombre) {
        return this.personajeRepository.obtenerPorNombre(nombre);
    }

    public List<PersonajeDTO> obtenerTodosPorEdad(int edad) {
        return this.personajeRepository.obtenerTodosPorEdad(edad);
    }

    public List<PersonajeDTO> obtenerTodosPorPeso(double peso) {
        return this.personajeRepository.obtenerTodosPorPeso(peso);
    }

    public List<PersonajeDTO> obtenerTodosPorIdPelicula(int idPelicula) {
        return this.personajeRepository.obtenerTodosPorIdPelicula(idPelicula);
    }

    public Personaje obtener(int id) {
        return this.personajeRepository.obtener(id);
    }

    public Personaje guardar(Personaje personaje) {
        if (personaje == null) {
            return null;
        }

        int id = personaje.getId();
        Imagen imagen = personaje.getImagen();
        String nombre = personaje.getNombre();
        int edad = personaje.getEdad();
        double peso = personaje.getPeso();
        String historia = personaje.getHistoria();

        if (id < 0 || imagen == null || nombre.equals("") || edad <= 0 || peso <= 0.0f || historia.equals("")) {
            return null;
        }

        this.personajeRepository.guardar(personaje);
        return this.obtener(personaje.getId());
    }

    public Personaje eliminar(int id) {
        Personaje personaje = this.obtener(id);
        this.personajeRepository.eliminar(personaje);
        return personaje;
    }

}
