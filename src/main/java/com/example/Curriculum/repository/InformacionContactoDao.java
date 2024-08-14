package com.example.Curriculum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Curriculum.models.InformacionContacto;

@Repository
public interface InformacionContactoDao extends JpaRepository<InformacionContacto, Long>{

}
