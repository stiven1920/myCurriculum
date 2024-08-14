package com.example.Curriculum.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Curriculum.models.Usuario;
import com.example.Curriculum.repository.UsuarioDao;

@Service
public class UsuarioServices {

	@Autowired
	private UsuarioDao userDao;

	public Usuario saveUser(Usuario user) {
		return userDao.save(user);
	}

	public Optional<Usuario> getUserByDni(BigInteger cedula) {
		return userDao.findByCedula(cedula);
	}

	public Optional<Usuario> findUserName(String user){
		return userDao.findByUsername(user);
	}

	public Usuario updateUser(Usuario user) {
		return userDao.save(user);
	}

	public boolean deleteUser(BigInteger cedula) {
		return userDao.deleteByCedula(cedula);
	}

	public ArrayList<Usuario> getAll() {
		return (ArrayList<Usuario>) userDao.findAll();
	}

	public Optional<Usuario>  findById(Long id) {
		return userDao.findById(id);
	}

}
