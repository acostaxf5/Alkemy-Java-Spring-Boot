package api.web.services;

import java.text.*;
import java.util.*;
import api.web.entities.*;
import api.web.repositories.*;
import api.web.entities.dto.PeliculaDTO;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.ApplicationScope;

@Service
@ApplicationScope
public class PeliculaService {

    private final ImagenRepository imagenRepository;
    private final GeneroRepository generoRepository;
    private final PeliculaRepository peliculaRepository;
    private final PersonajeRepository personajeRepository;

    @Autowired
    public PeliculaService(ImagenRepository imagenRepository, GeneroRepository generoRepository, PeliculaRepository peliculaRepository, PersonajeRepository personajeRepository) {
        this.imagenRepository = imagenRepository;
        this.generoRepository = generoRepository;
        this.peliculaRepository = peliculaRepository;
        this.personajeRepository = personajeRepository;
    }

    public List<PeliculaDTO> obtenerTodos() {
        return this.peliculaRepository.obtenerTodos();
    }

    public List<PeliculaDTO> obtenerPorTitulo(String titulo) {
        return this.peliculaRepository.obtenerPorTitulo(titulo);
    }

    public List<PeliculaDTO> obtenerPorGeneroOrdenado(int idGenero, boolean orden) {
        return this.peliculaRepository.obtenerPorGeneroOrdenado(idGenero, orden);
    }

    public Pelicula obtener(int id) {
        return this.peliculaRepository.obtener(id);
    }

    public Pelicula guardar(int idImagen, String titulo, String fechaCreacion, int calificacion, int idGenero, List<Integer> idPersonajes) throws ParseException {
        Imagen imagen = this.imagenRepository.obtener(idImagen);
        Genero genero = this.generoRepository.obtener(idGenero);
        List<Personaje> personajes = new ArrayList<>();

        if (idPersonajes != null) {
            idPersonajes.forEach(ip -> {
                Personaje personaje = this.personajeRepository.obtener(ip);

                if (personaje != null) {
                    personajes.add(personaje);
                }
            });
        }

        if (imagen == null || genero == null || personajes.isEmpty()) {
            return null;
        }

        Pelicula pelicula = new Pelicula();
        pelicula.setImagen(imagen);
        pelicula.setTitulo(titulo);
        pelicula.setFechaCreacion(new SimpleDateFormat("yyyy/MM/dd").parse(fechaCreacion));
        pelicula.setCalificacion(calificacion);
        pelicula.setGenero(genero);
        personajes.forEach(personaje -> {
            pelicula.addPersonaje(personaje);

            if (personaje != null) {
                personaje.addPelicula(pelicula);
            }
        });

        this.peliculaRepository.guardar(pelicula);
        return this.obtener(pelicula.getId());
    }

    public Pelicula agregarPersonaje(Pelicula pelicula, int idPersonaje) {
        Personaje personaje = this.personajeRepository.obtener(idPersonaje);

        if (personaje == null) {
            return null;
        }

        if (pelicula.getPersonajes().contains(personaje)) {
            return null;
        } else {
            pelicula.addPersonaje(personaje);
            personaje.addPelicula(pelicula);
        }

        this.peliculaRepository.guardar(pelicula);
        return this.obtener(pelicula.getId());
    }

    public Pelicula eliminar(int id) {
        Pelicula pelicula = this.peliculaRepository.obtener(id);
        this.peliculaRepository.eliminar(pelicula);
        return pelicula;
    }

    public Pelicula eliminarPersonaje(Pelicula pelicula, int idPersonaje) {
        Personaje personaje = this.personajeRepository.obtener(idPersonaje);

        if (personaje == null) {
            return null;
        }

        if (pelicula.getPersonajes().contains(personaje)) {
            pelicula.removePersonaje(personaje);
            personaje.removePelicula(pelicula);
        } else {
            return null;
        }

        this.peliculaRepository.guardar(pelicula);
        return this.obtener(pelicula.getId());
    }

}
