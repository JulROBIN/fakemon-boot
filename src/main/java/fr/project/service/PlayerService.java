package fr.project.service;

import static fr.project.FakemonBootApplication.saisieInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.project.model.MonsterEntity;

import fr.project.model.Player;


@Service
public class PlayerService {

	@Autowired
	private ContextService context;



	/**	Crée une sélection aléatoire de six monstres puis le joueur doit en choisir un comme monstre de départ
	 **/
	public void selectionStarterBack (Player player) {

		ArrayList<MonsterEntity> table2Chen = this.tableRencontre(6);
		table2Chen.forEach(mi -> System.out.println(mi.toStringGeneral()));
		int i=0;
		while (i<1 || i>6) {
			i = saisieInt("Quel Fakemon souhaitez-vous comme starter ? (1 à 6)");
		}
		player.addEquipePlayer(table2Chen.get(i-1));
		System.out.println("Vous avez choisi "+table2Chen.get(i-1).getNom()+" !");
		System.out.println("Ses moves sont : "+table2Chen.get(i-1).toStringDetailAttaque());
		System.out.println("Ses statistiques sont : "+table2Chen.get(i-1).toStringDetailStat()+"\n");
	}
	
	public List<MonsterEntity> getStarters () {
		System.out.println("table rencontre");
		ArrayList<MonsterEntity> table2Chen = this.tableRencontre(6);
		table2Chen.forEach(mi -> System.out.println(mi.toStringGeneral()));
		return table2Chen;

	}

	
	//______________________________________________________________________________
	//	Méthodes

	/**Revoie une liste de monstres de niveau 1 crée aléatoirement
	 * @param nbRencontre int ; le nombre de monstres souhaités
	 * @return ArrayList<Monster> ; La liste des monstres aléatoire va rencontrer
	 **/
	public ArrayList<MonsterEntity> tableRencontre(int nbRencontre) {

		ArrayList<MonsterEntity> tableRencontre = new ArrayList<>();
		MonsterEntity m = null;

		for (int i=0;i<nbRencontre;i++) {
			System.out.println("int i : "+i);
			Random r = new Random();
			int choixMonstre = r.nextInt(this.context.getDaoMonster().countNombreMonstre());
			System.out.println(this.context == null);
			System.out.println(this.context.getDaoMonster().findById(1).get());
			m = this.context.getDaoMonster().findById(choixMonstre+1).get().createMonsterEntity(context);
			tableRencontre.add(m);
		}
		return tableRencontre;
	}


	



}
