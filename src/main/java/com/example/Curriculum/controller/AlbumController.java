package com.example.Curriculum.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Curriculum.models.Album;
import com.example.Curriculum.models.Imagen;
import com.example.Curriculum.services.AlbumServices;

@RestController
@RequestMapping("/api/v1/albumes")
public class AlbumController {

	@Autowired
	private AlbumServices albumServices;

	@PostMapping("/saveAlbumes")
	public ResponseEntity<Album> crearAlbum(@RequestParam String nombre) {
		try {
			if (albumServices.existeAlbum(nombre)) {
				return new ResponseEntity<>(HttpStatus.CONFLICT); 
			}
			Album nuevoAlbum = albumServices.crearAlbum(nombre);
			return new ResponseEntity<>(nuevoAlbum, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/{albumNombre}/imagenes")
	public ResponseEntity<Map<String, Object>> agregarImagen(@PathVariable String albumNombre,
	        @RequestParam("archivo") MultipartFile archivo, @RequestParam String descripcion) throws IOException {

	    if (archivo.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }

	    Imagen nuevaImagen = new Imagen();
	    nuevaImagen.setNombre(archivo.getOriginalFilename());
	    nuevaImagen.setContenido(archivo.getBytes());
	    nuevaImagen.setDescripcion(descripcion);

	    Album album = new Album();
	    album.setNombre(albumNombre);
	    nuevaImagen.setAlbum(album); 

	    try {
	        Album albumActualizado = albumServices.agregarImagen(nuevaImagen);

	        Map<String, Object> response = new HashMap<>();
	        response.put("mensaje", "Imagen agregada correctamente al álbum.");
	        response.put("album", albumActualizado);

	        return new ResponseEntity<>(response, HttpStatus.OK);

	    } catch (RuntimeException e) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
	    }
	}



}
