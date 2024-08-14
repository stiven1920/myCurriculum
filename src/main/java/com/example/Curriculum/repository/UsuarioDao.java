package com.example.Curriculum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Curriculum.models.Usuario;

import java.math.BigInteger;
import java.util.Optional;


@Repository
public interface UsuarioDao extends JpaRepository<Usuario, Long>{

    Optional<Usuario> findByCedula(BigInteger cedula);

    boolean deleteByCedula(BigInteger cedula);

    Optional<Usuario> findByUsername(String username);
    
    Optional<Usuario> findById(Long id);
    
}
