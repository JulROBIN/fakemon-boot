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
import fr.project.dao.IDAOMonsterBase;
import fr.project.model.Attaque;
import fr.project.model.MonsterEntity;
import fr.project.projection.Views;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/monstre")
public class MonstreApiController {
	@Autowired
	private IDAOMonsterBase daoMonsterBase;
	
	@GetMapping
	@JsonView(Views.Common.class)
	public List<MonsterEntity> findAllMonster() {
		return daoMonsterBase.findAll();
	}
	
	@GetMapping("/{id}")
	@JsonView(Views.MonsterP.class)
	public MonsterEntity findMonsterById(@PathVariable int id) {
		return daoMonsterBase.findById(id).orElse(new MonsterEntity());
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
