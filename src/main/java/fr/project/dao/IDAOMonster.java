package fr.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.project.service.MonsterService;

public interface IDAOMonster extends JpaRepository<MonsterService, Integer> {
	
	public MonsterService findByNom(String nom);
	
	@Query("SELECT COUNT(m) FROM Monster m")
	public Integer countNombreMonstre();

}
