package fr.project.model;

import fr.project.model.MonsterEntity;

public class Action {
	
	MonsterEntity m;
	String message;
	
	/** Constructeur avec paramettres
	 * @param m : Monster
	 * @param msg : String
	 **/
	public Action(MonsterEntity m, String msg) {
		this.m = m;
		this.message = msg;
	}
	
	/** Constructeur vide JPA ?
	 **/
	public Action() {
	}

	//Getters et Setters
	public MonsterEntity getM() {
		return m;
	}
	public void setM(MonsterEntity m) {
		this.m = m;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**	Ajoute un message au(x) message(x) existant(s) ou crée un nouveau message si pas de message déjà existant
	 * @param str String ; Le message à ajouter pour affichage dans le front
	 * @return Action ; retourne l'action avec ses messages updatés
	 */
	public Action append(String str) {
		if(this.message == null) {
			setMessage(str);
		}else {
			this.message += "\\n"+str;
		}
		return this;
	}

	
	@Override
	public String toString() {
		return "Action [m=" + m + ", message=" + message + "]";
	}
}
