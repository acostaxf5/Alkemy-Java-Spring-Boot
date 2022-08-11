package api.web.controllers;

import java.util.*;
import org.springframework.http.*;
import api.web.entities.Personaje;
import api.web.services.ImagenService;
import api.web.services.PersonajeService;
import api.web.entities.dto.PersonajeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping(value = "/personajes")
public class PersonajeController {

    private final ImagenService imagenService;
    private final PersonajeService personajeService;

    @Autowired
    public PersonajeController(ImagenService imagenService, PersonajeService personajeService) {
        this.imagenService = imagenService;
        this.personajeService = personajeService;
    }

    @Operation(summary = "OBTENER TODOS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping(value = "/obtener-todos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PersonajeDTO>> obtenerTodos(@RequestParam(name = "nombre", required = false) String nombre, @RequestParam(name = "edad", required = false) Integer edad, @RequestParam(name = "peso", required = false) Double peso, @RequestParam(name = "id_pelicula", required = false) Integer idPelicula) {
        if (nombre != null && !nombre.equals("")) {
            return ResponseEntity.ok(this.personajeService.obtenerPorNombre(nombre));
        }

        if (edad != null && edad > 0) {
            return ResponseEntity.ok(this.personajeService.obtenerTodosPorEdad(edad));
        }

        if (peso != null && peso > 0.0d) {
            return ResponseEntity.ok(this.personajeService.obtenerTodosPorPeso(peso));
        }

        if (idPelicula != null && idPelicula > 0) {
            return ResponseEntity.ok(this.personajeService.obtenerTodosPorIdPelicula(idPelicula));
        }

        return ResponseEntity.ok(this.personajeService.obtenerTodos());
    }

    @Operation(summary = "OBTENER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content()),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content())
    })
    @GetMapping(value = "/obtener/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Personaje> obtener(@PathVariable(name = "id") int id) {
        if (id <= 0) {
            return ResponseEntity.status(400).build();
        }

        Personaje personaje = this.personajeService.obtener(id);

        if (personaje == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(personaje);
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

        Personaje personaje = this.personajeService.obtener(id);

        if (personaje == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(personaje.getImagen().getDatos());
    }

    @Operation(summary = "GUARDAR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content()),
            @ApiResponse(responseCode = "422", description = "UNPROCESSABLE ENTITY", content = @Content())
    })
    @PostMapping(value = "/guardar/obtener-imagen/{id_imagen}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Personaje> guardar(@RequestBody Personaje personajeJson, @PathVariable(name = "id_imagen") int id_imagen) {
        if (personajeJson == null || id_imagen < 0) {
            return ResponseEntity.status(400).build();
        }

        personajeJson.setImagen(this.imagenService.obtener(id_imagen));

        Personaje personaje = this.personajeService.guardar(personajeJson);

        if (personaje == null) {
            return ResponseEntity.status(422).build();
        }

        return ResponseEntity.ok(personaje);
    }

    @Operation(summary = "ELIMINAR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content()),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content())
    })
    @DeleteMapping(value = "/eliminar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Personaje> delete(@PathVariable(name = "id") int id) {
        if (id <= 0) {
            return ResponseEntity.status(400).build();
        }

        Personaje personaje = this.personajeService.eliminar(id);

        if (personaje == null) {
            return ResponseEntity.status(404).build();
        }

        personaje.setPeliculas(new ArrayList<>());
        return ResponseEntity.ok(personaje);
    }

}
