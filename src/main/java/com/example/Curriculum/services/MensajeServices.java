package com.example.Curriculum.services;

import java.math.BigInteger;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Curriculum.models.Mensaje;
import com.example.Curriculum.repository.MensajeDao;

@Service
public class MensajeServices {

	@Autowired
	private MensajeDao mensajeDao;
	
	public Mensaje saveMensaje(Mensaje msj) {
		return mensajeDao.save(msj);
	}
	
	public ArrayList<Object[]> findMensajeByCedula(BigInteger cedula){
		return mensajeDao.findMensajeByCedula(cedula);
	}

	public ArrayList<Object[]> findMensajeAll(){
		return mensajeDao.findMensajeAll();
	}
	
	public Mensaje updateMensaje(Mensaje msj) {
		return mensajeDao.save(msj);
	}
}
