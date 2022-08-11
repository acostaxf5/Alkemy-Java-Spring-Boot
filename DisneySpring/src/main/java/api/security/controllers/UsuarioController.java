package api.security.controllers;

import java.io.IOException;
import api.sendgrid.MailService;
import org.springframework.http.*;
import api.security.entities.Usuario;
import api.security.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping(value = "/autenticacion")
public class UsuarioController {

    private final MailService mailService;
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(MailService mailService, UsuarioService usuarioService) {
        this.mailService = mailService;
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "SALIR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping(value = "/salir", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> salir() {
        SecurityContextHolder.getContext().setAuthentication(null);

        return ResponseEntity.status(200).build();
    }

    @Operation(summary = "INGRESAR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content()),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content())
    })
    @PostMapping(value = "/ingresar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> ingresar(@RequestBody Usuario usuario) {
        UserDetails userDetails;

        try {
            userDetails = this.usuarioService.loadUserByUsername(usuario.getCorreo());
        } catch (Exception ex) {
            return ResponseEntity.status(404).build();
        }

        if (this.usuarioService.validarIngreso(usuario, userDetails)) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.status(200).build();
    }

    @Operation(summary = "REGISTRAR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content()),
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content()),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content())
    })
    @PostMapping(value = "/registrar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        if (usuario == null) {
            return ResponseEntity.status(400).build();
        }

        try {
            this.mailService.enviarEmail(usuario.getCorreo());
            return ResponseEntity.ok(this.usuarioService.saveUser(usuario));
        } catch (IOException ex) {
            return ResponseEntity.status(500).build();
        } catch (Exception ex) {
            return ResponseEntity.status(409).build();
        }
    }

    @Operation(summary = "ELIMINAR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content()),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content())
    })
    @DeleteMapping(value = "/eliminar/{correo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Usuario> eliminar(@PathVariable(name = "correo") String correo) {
        if (correo.equals("")) {
            return ResponseEntity.status(400).build();
        }

        try {
            return ResponseEntity.ok(this.usuarioService.eliminar(correo));
        } catch (Exception ex) {
            return ResponseEntity.status(404).build();
        }
    }

}
