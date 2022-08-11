package api.web.controllers;

import java.io.IOException;
import api.web.entities.Imagen;
import org.springframework.http.*;
import api.web.services.ImagenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping(value = "/imagenes")
public class ImagenController {

    private final ImagenService imagenService;

    @Autowired
    public ImagenController(ImagenService imagenService) {
        this.imagenService = imagenService;
    }

    @Operation(summary = "OBTENER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE)),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content()),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content())
    })
    @GetMapping(value = "/obtener/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> obtener(@PathVariable(value = "id") int id) {
        if (id < 0) {
            return ResponseEntity.status(400).build();
        }

        Imagen imagen = this.imagenService.obtener(id);

        if (imagen == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(imagen.getDatos());
    }

    @Operation(summary = "GUARDAR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content()),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content())
    })
    @PostMapping(value = "/guardar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Imagen> guardar(@RequestParam(value = "imagen") MultipartFile imagen) {
        if (imagen == null) {
            return ResponseEntity.status(400).build();
        }

        try {
            return ResponseEntity.ok(this.imagenService.guardar(imagen));
        } catch (IOException ex) {
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "ELIMINAR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content()),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content())
    })
    @DeleteMapping(value = "/eliminar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Imagen> eliminar(@PathVariable(value = "id") int id) {
        if (id < 0) {
            return ResponseEntity.status(400).build();
        }

        Imagen imagen = this.imagenService.eliminar(id);

        if (imagen == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(imagen);
    }

}
