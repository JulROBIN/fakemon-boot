package fr.project.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import fr.project.dao.IDAOAttaque;
import fr.project.model.Attaque;
import fr.project.projection.Views;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/attaque")
public class AttaqueApiController {
	@Autowired
	private IDAOAttaque daoAttaque;
	
	@GetMapping
	@JsonView(Views.Common.class)
	public List<Attaque> findAttaque() {
		return this.daoAttaque.findAll();
	}
	
	@GetMapping("/{id}")
	@JsonView(Views.AttaqueP.class)
	public Attaque findAttaque(@PathVariable int id) {
		return this.daoAttaque.findById(id).orElse(new Attaque());
	}





	
	/*
	
	@Autowired
	private IDAOCompte daoCompte;


	@GetMapping
	@JsonView(Views.Medecin.class)
	//	Pour obtenir la liste de tous les Medecins
	public List<Compte> findMedecins() {
		return this.daoCompte.findByTypeCompteContaining("Medecin");
	}

	@GetMapping("/{id}")
	@JsonView(Views.Medecin.class)
	// Pour obtenir un seul medecin
	public Compte finById(@PathVariable int secu) {
		return this.daoCompte.findById(secu).orElse(new Compte());
	}

	@PostMapping
	@JsonView(Views.Medecin.class)
	//	Pour ajouter un seul patient
	public Compte add(@Valid @RequestBody Compte compte, BindingResult result) {
		if (result.hasErrors()) {
			throw new PatientValidationException();
		}
		this.daoCompte.save(compte);
		return compte;
	}

	@PutMapping("/{id}")
	@JsonView(Views.Medecin.class)
	//	Pour modifier un patient
	public Compte update(@PathVariable int id, @RequestBody Compte compte) {
		compte.setId(id);
		this.daoCompte.save(compte);
		return compte;
	}

	@DeleteMapping("/{id}") 
	@JsonView(Views.Medecin.class)
	// Pour supprimer un patient
	public boolean delete(@PathVariable int id) {
		try {
			this.daoCompte.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}*/

}
