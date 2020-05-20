package fr.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.project.model.MonsterEntity;

public interface IDAOMonsterBase extends JpaRepository<MonsterEntity, Integer> {

	@Query("SELECT COUNT(m) FROM MonsterEntity m")
	public Integer countNombreMonstre();

	
}
