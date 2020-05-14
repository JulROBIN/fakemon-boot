package fr.project.service;

import java.util.ArrayList;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.project.model.Monster;
import fr.project.model.Player;

@Service
public class PlayerService {

	@Autowired
	private ContextService context;
	
	//______________________________________________________________________________
		
	public ContextService getContext() {
		return context;
	}
	public void setContext(ContextService context) {
		this.context = context;
	}
	
	//______________________________________________________________________________
	//	Méthodes

	/**Revoie une liste de monstres de niveau 1 crée aléatoirement
	 * @param nbRencontre int ; le nombre de monstres souhaités
	 * @return ArrayList<Monster> ; La liste des monstres aléatoire va rencontrer
	 **/
	public ArrayList<Monster> tableRencontre(int nbRencontre) {

		ArrayList<Monster> tableRencontre = new ArrayList<Monster>();
		Monster m = null;

		for (int i=0;i<nbRencontre;i++) {

			Random r = new Random();
			int choixMonstre = r.nextInt(this.context.getDaoMonster().countNombreMonstre());
			m = this.context.getDaoMonster().findById(choixMonstre+1).get();

			m.setListAttaque(context.creationAttaque(m.poolAtkStringToInt(m.getPoolAtkString())));

			tableRencontre.add(m);
		}
		return tableRencontre;
	}

	
	/**	Generation d'une liste de 6 monstres pour faire les starters
	 * @return ArrayList<Monster> ; Liste des 6 monstres servant de starters
	 **/
	public ArrayList<Monster> getStarters(Player sacha) {
		if(sacha.getStarters().isEmpty()) {
			sacha.setStarters(tableRencontre(6));
		}
		return sacha.getStarters();
	}


	



}
