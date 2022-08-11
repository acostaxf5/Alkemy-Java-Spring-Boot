package api.web.controllers;

import java.util.*;
import java.text.ParseException;
import api.web.entities.Pelicula;
import org.springframework.http.*;
import api.web.entities.dto.PeliculaDTO;
import api.web.services.PeliculaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping(value = "/peliculas")
public class PeliculaController {

    private final PeliculaService peliculaService;

    @Autowired
    public PeliculaController(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    @Operation(summary = "OBTENER TODOS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping(value = "/obtener-todos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PeliculaDTO>> obtenerTodos(@RequestParam(name = "titulo", required = false) String titulo, @RequestParam(name = "id_genero", required = false) Integer idGenero, @RequestParam(name = "orden", required = false) Boolean orden) {
        if (titulo != null && !titulo.equals("")) {
            return ResponseEntity.ok(this.peliculaService.obtenerPorTitulo(titulo));
        }

        if (idGenero != null && orden != null && idGenero > 0) {
            return ResponseEntity.ok(this.peliculaService.obtenerPorGeneroOrdenado(idGenero, orden));
        }

        return ResponseEntity.ok(this.peliculaService.obtenerTodos());
    }

    @Operation(summary = "OBTENER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content()),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content())
    })
    @GetMapping(value = "/obtener/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pelicula> obtener(@PathVariable(name = "id") int id) {
        if (id <= 0) {
            return ResponseEntity.status(400).build();
        }

        Pelicula pelicula = this.peliculaService.obtener(id);

        if (pelicula == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(pelicula);
    }

    @Operation(summary = "OBTENER IMAGEN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE)),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content()),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content())
    })
    @GetMapping(value = "/obtener/{id}/obtener-imagen", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable(name = "id") int id) {
        if (id <= 0) {
            return ResponseEntity.status(400).build();
        }

        Pelicula pelicula = this.peliculaService.obtener(id);

        if (pelicula == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(pelicula.getImagen().getDatos());
    }

    @Operation(summary = "GUARDAR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content()),
            @ApiResponse(responseCode = "422", description = "UNPROCESSABLE ENTITY", content = @Content())
    })
    @PostMapping(value = "/guardar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pelicula> guardar(@RequestParam(name = "id_imagen") int idImagen, @RequestParam(name = "titulo") String titulo, @RequestParam(name = "fecha_creacion") String fechaCreacion, @RequestParam(name = "calificacion") int calificacion, @RequestParam(name = "id_genero") int idGenero, @RequestParam(name = "id_personajes") List<Integer> idPersonajes) {
        if (idImagen <= 0 || titulo.equals("") || fechaCreacion.equals("") || calificacion < 1 || calificacion > 5 || idGenero <= 0 || idPersonajes == null || idPersonajes.isEmpty()) {
            return ResponseEntity.status(400).build();
        }

        try {
            Pelicula pelicula = this.peliculaService.guardar(idImagen, titulo, fechaCreacion, calificacion, idGenero, idPersonajes);

            if (pelicula == null) {
                return ResponseEntity.status(422).build();
            }

            return ResponseEntity.ok(pelicula);
        } catch (ParseException ex) {
            return ResponseEntity.status(400).build();
        }
    }

    @Operation(summary = "AGREGAR PERSONAJE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content()),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content())
    })
    @PostMapping(value = "/obtener/{id_pelicula}/agregar-personaje/{id_personaje}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pelicula> agregarPersonaje(@PathVariable(name = "id_pelicula") int idPelicula, @PathVariable(name = "id_personaje") int idPersonaje) {
        if (idPelicula <= 0 || idPersonaje <= 0) {
            return ResponseEntity.status(400).build();
        }

        Pelicula pelicula = this.peliculaService.obtener(idPelicula);

        if (pelicula == null) {
            return ResponseEntity.status(404).build();
        }

        Pelicula peliculaModificada = this.peliculaService.agregarPersonaje(pelicula, idPersonaje);

        if (peliculaModificada == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(peliculaModificada);
    }

    @Operation(summary = "ELIMINAR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content()),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content())
    })
    @DeleteMapping(value = "/eliminar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pelicula> eliminar(@PathVariable(name = "id") int id) {
        if (id <= 0) {
            return ResponseEntity.status(400).build();
        }

        Pelicula pelicula = this.peliculaService.eliminar(id);

        if (pelicula == null) {
            return ResponseEntity.status(404).build();
        }

        pelicula.setPersonajes(new ArrayList<>());
        return ResponseEntity.ok(pelicula);
    }

    @Operation(summary = "ELIMINAR PERSONAJE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content()),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content())
    })
    @DeleteMapping(value = "/obtener/{id_pelicula}/eliminar-personaje/{id_personaje}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pelicula> eliminarPersonaje(@PathVariable(name = "id_pelicula") int idPelicula, @PathVariable(name = "id_personaje") int idPersonaje) {
        if (idPelicula <= 0 || idPersonaje <= 0) {
            return ResponseEntity.status(400).build();
        }

        Pelicula pelicula = this.peliculaService.obtener(idPelicula);

        if (pelicula == null) {
            return ResponseEntity.status(404).build();
        }

        Pelicula peliculaModificada = this.peliculaService.eliminarPersonaje(pelicula, idPersonaje);

        if (peliculaModificada == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(peliculaModificada);
    }

}
