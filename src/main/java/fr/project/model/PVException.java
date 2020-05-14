package fr.project.model;

import fr.project.service.MonsterService;

public class PVException extends Exception {

	public PVException(MonsterEntity mException) {
		super(debutPhrase(mException)+" ne peux plus se battre !");
	}

	private static String debutPhrase(MonsterEntity mException) {
		String texte = "";
		if(mException.getSituation().equals(Situation.valueOf("Joueur"))) {
			texte = "Votre "+mException.getNom();}
		else if (mException.getSituation().equals(Situation.valueOf("Sauvage"))) {
			texte = "Le "+mException.getNom()+" sauvage";}
		else if (mException.getSituation().equals(Situation.valueOf("Adversaire"))) {
			texte = "Le "+mException.getNom()+" du dresseur adverse";}
		return texte;
	}

}
