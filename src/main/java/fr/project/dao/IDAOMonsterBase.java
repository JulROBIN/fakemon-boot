package fr.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.project.model.MonsterEntity;

public interface IDAOMonsterBase extends JpaRepository<MonsterEntity, Integer> {

}
