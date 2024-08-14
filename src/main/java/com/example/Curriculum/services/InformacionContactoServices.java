package com.example.Curriculum.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Curriculum.models.InformacionContacto;
import com.example.Curriculum.repository.InformacionContactoDao;

@Service
public class InformacionContactoServices {
	
	@Autowired
	private InformacionContactoDao infoContDao;
	
	
	public InformacionContacto saveContato(InformacionContacto continfo) {
		return infoContDao.save(continfo);
	}

}
