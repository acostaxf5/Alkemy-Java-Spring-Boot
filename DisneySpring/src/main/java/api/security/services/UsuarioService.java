package api.security.services;

import java.util.*;
import api.security.entities.Usuario;
import org.springframework.stereotype.Service;
import api.security.repositories.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
@ApplicationScope
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) {
        Usuario usuario = this.usuarioRepository.obtenerPorNombre(correo);

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new User(usuario.getCorreo(), usuario.getClave(), roles);
    }

    public boolean validarIngreso(Usuario usuario, UserDetails userDetails) {
        return this.passwordEncoder.matches(usuario.getClave(), userDetails.getPassword());
    }

    public Usuario saveUser(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        usuario.setClave(this.passwordEncoder.encode(usuario.getClave()));
        this.usuarioRepository.guardar(usuario);

        return this.usuarioRepository.obtener(usuario.getId());
    }

    public Usuario eliminar(String correo) {
        Usuario usuario = this.usuarioRepository.obtenerPorNombre(correo);
        this.usuarioRepository.eliminar(usuario);
        return usuario;
    }

}
