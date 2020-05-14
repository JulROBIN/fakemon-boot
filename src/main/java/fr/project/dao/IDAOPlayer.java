package fr.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.project.model.MonsterEntity;
import fr.project.model.Player;

public interface IDAOPlayer extends JpaRepository<Player, Integer> {

}
