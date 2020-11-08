package com.desarrolloweb.microservicios.commons.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.desarrolloweb.microservicios.commons.services.CommonService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;



//@CrossOrigin({"http://localhost:4200"})
public class CommonController<E, S extends CommonService<E>> {
	@Autowired
	protected S service;
	
	@RolesAllowed("backend-admin")
	@GetMapping 
	public ResponseEntity<?> listar(){
		return ResponseEntity.ok().body(service.findAll());
		
	}
	
	
	@GetMapping ("/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id){
    Optional<E> o = service.findById(id);  
   if (o.isEmpty()) {
	   return ResponseEntity.notFound().build();
   }
	return ResponseEntity.ok(o.get());
	}
	
	@RolesAllowed("backend-admin")
	@PostMapping
	public ResponseEntity<?> crear (@Valid @RequestBody E entity, BindingResult result){
		
		if (result.hasErrors()) {
			return this.validar(result);
			
		}
		E entityDb = service.save(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(entityDb);
	}

	//@RolesAllowed("backend-admin")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar (@PathVariable Long id){
		service.deleteById(id);
		return ResponseEntity.noContent().build();
		
	}
	
	protected ResponseEntity<?> validar(BindingResult result){
		Map<String, Object> errores = new HashMap<>();
		result.getFieldErrors().forEach(err ->{
			errores.put(err.getField(),"El campo "+err.getField()+" "+ err.getDefaultMessage() );
		});
		return ResponseEntity.badRequest().body(errores);
	}
	
}
