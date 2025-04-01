package com.example.Curriculum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Curriculum.models.Album;
import java.util.List;
import java.util.Optional;


@Repository
public interface AlbumDao extends JpaRepository<Album, Long>{
	
	Optional<Album> findByNombre(String nombre);

}
