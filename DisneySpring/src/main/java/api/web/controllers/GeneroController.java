package api.web.controllers;

import java.util.List;
import api.web.entities.*;
import api.web.services.*;
import org.springframework.http.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping(value = "/genero")
public class GeneroController {

    private final GeneroService generoService;
    private final ImagenService imagenService;

    @Autowired
    public GeneroController(GeneroService generoService, ImagenService imagenService) {
        this.generoService = generoService;
        this.imagenService = imagenService;
    }

    @Operation(summary = "OBTENER TODOS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping(value = "/obtener-todos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Genero>> obtenerTodos() {
        return ResponseEntity.ok(this.generoService.obtenerTodos());
    }

    @Operation(summary = "OBTENER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content())
    })
    @GetMapping(value = "/obtener/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Genero> obtener(@PathVariable(name = "id") int id) {
        Genero genero = this.generoService.obtener(id);

        if (genero == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(genero);
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

        Genero genero = this.generoService.obtener(id);

        if (genero == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(genero.getImagen().getDatos());
    }

    @Operation(summary = "GUARDAR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "422", description = "UNPROCESSABLE ENTITY", content = @Content())
    })
    @PostMapping(value = "/guardar/obtener-imagen/{id_imagen}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Genero> guardar(@RequestBody Genero generoJson, @PathVariable(value = "id_imagen") int id_imagen) {
        if (generoJson == null || id_imagen < 0) {
            return ResponseEntity.status(422).build();
        }

        generoJson.setImagen(this.imagenService.obtener(id_imagen));

        Genero genero = this.generoService.guardar(generoJson);

        if (genero == null) {
            return ResponseEntity.status(422).build();
        }

        return ResponseEntity.ok(genero);
    }

    @Operation(summary = "ELIMINAR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content()),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content())
    })
    @DeleteMapping(value = "/eliminar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Genero> eliminar(@PathVariable(name = "id") int id) {
        if (id < 0) {
            return ResponseEntity.status(400).build();
        }

        Genero genero = this.generoService.eliminar(id);

        if (genero == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(genero);
    }

}
