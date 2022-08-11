package api.web.services;

import java.io.IOException;
import api.web.entities.Imagen;
import api.web.repositories.ImagenRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.ApplicationScope;

@Service
@ApplicationScope
public class ImagenService {

    private final ImagenRepository imagenRepository;

    @Autowired
    public ImagenService(ImagenRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    public Imagen obtener(int id) {
        return this.imagenRepository.obtener(id);
    }

    public Imagen guardar(MultipartFile archivo) throws IOException {
        Imagen imagen = new Imagen();

        if (archivo != null) {
            imagen.setDatos(archivo.getBytes());
            imagen.setNombre(archivo.getOriginalFilename());
            imagen.setTipo(archivo.getContentType());

            this.imagenRepository.guardar(imagen);
        }

        return this.imagenRepository.obtener(imagen.getId());
    }

    public Imagen eliminar(int id) {
        Imagen imagen = this.imagenRepository.obtener(id);

        if (imagen != null) {
            this.imagenRepository.eliminar(imagen);
        }

        return imagen;
    }

}
