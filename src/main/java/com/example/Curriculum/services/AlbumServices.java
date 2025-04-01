package com.example.Curriculum.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Curriculum.models.Album;
import com.example.Curriculum.models.Imagen;
import com.example.Curriculum.repository.AlbumDao;

@Service
public class AlbumServices {
    @Autowired
    private AlbumDao albumDao;

    public Album crearAlbum(String nombre) {
        if (existeAlbum(nombre)) {
            throw new RuntimeException("El álbum ya existe");
        }

        Album album = new Album();
        album.setNombre(nombre);
        album.setImagenes(new ArrayList<>());
        return albumDao.save(album);
    }

    public boolean existeAlbum(String nombre) {
        return albumDao.findByNombre(nombre).isPresent();
    }

    public Album agregarImagen(Imagen imagen) { 
        Album album = albumDao.findByNombre(imagen.getAlbum().getNombre())
                              .orElseThrow(() -> new RuntimeException("Álbum no encontrado"));

        imagen.setAlbum(album);
        if (album.getImagenes() == null) {
            album.setImagenes(new ArrayList<>());
        }

        // Agregar la nueva imagen a la lista de imágenes del álbum
        album.getImagenes().add(imagen);
        return albumDao.save(album);
    }



}
