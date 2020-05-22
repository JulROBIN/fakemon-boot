package fr.project.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

@Entity
@DiscriminatorValue("joueur")
public class CompteJoueur extends Compte {
	
	@OneToMany
	@JoinTable(
		name = "saves",
		uniqueConstraints = @UniqueConstraint(columnNames = { "id_joueur", "id_player" }),
		joinColumns = @JoinColumn(name = "id_joueur", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "id_player", referencedColumnName = "id")
	)
	List<Player> characters = new ArrayList<>();
	
}
