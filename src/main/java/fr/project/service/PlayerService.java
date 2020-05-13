package fr.project.service;

import java.util.ArrayList;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.project.model.Monster;

@Service
public class PlayerService {

	@Autowired
	private ContextService context;


	



	
	
	
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


	



}
