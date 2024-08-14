package com.example.Curriculum.repository;

import java.math.BigInteger;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Curriculum.models.Mensaje;

@Repository
public interface MensajeDao extends JpaRepository<Mensaje, Long> {

	   @Query("SELECT u.username, m.fecha, m.mensaje " +
	           "FROM Mensaje m " +
	           "JOIN m.usuario u " +
	           "WHERE u.cedula = :cedulaParam")
	    ArrayList<Object[]> findMensajeByCedula(@Param("cedulaParam") BigInteger cedula);

	@Query("SELECT u.username, m.fecha, m.mensaje FROM Usuario u "
			+ "INNER JOIN Mensaje m ON u.id = m.usuario.id ")
	ArrayList<Object[]> findMensajeAll();

}
