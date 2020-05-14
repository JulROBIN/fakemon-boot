package fr.project;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import fr.project.model.Dresseur;
import fr.project.model.Monster;
import fr.project.model.PVException;
import fr.project.model.Player;
import fr.project.service.ContextService;
import fr.project.service.PlayerService;

@SpringBootApplication
public class FakemonBootApplication {

	public static int saisieInt(String msg) {
		Scanner sc = new Scanner(System.in);
		System.out.println(msg);
		int i = sc.nextInt();
		return i;
	}
	public static String saisieString(String msg) {
		Scanner sc = new Scanner(System.in);
		System.out.println(msg);
		String i = sc.next();
		return i;
	}	

	//________________________________________________________________________
	@Autowired
	PlayerService player;

	@Autowired
	ContextService ctxtsvc;
	
	private static Logger logger = LoggerFactory.getLogger(FakemonBootApplication.class);

	/** Fonction qui va lancer le combat : calcul quel monstre a l'initiative (fonction appelee) puis passe a la phase de combat (fonction appelee) dans le bon ordre.
	 * La sortie d'une PVexception dans la fonction combat apppelee signifie que l'un des deux monstre au combat est KO
	 * @param m1 : Monster ; le monstre du joueur, cad le premier de sa liste au debut du combat puis celui actif lors des tours suivants (si KO ou switch)
	 * @param m2 : Monster ; Le monstre sauvage ou du dresseur adverse
	 **/
	public void combat(Player sacha, Monster m2){

		Monster m1 = sacha.getEquipePlayer().getFirst();
		try {
			while (m1.getPv()>0 && m2.getPv()>0) {
				if (m1.initiative(m2).equals(m1)) {
					System.out.println(m1.getNom()+" attaque "+m2.getNom()+" en premier");
					m1.selectionAttaqueCombat(m2, ctxtsvc);
					System.out.println(m2.getNom()+" attaque "+m1.getNom());
					m2.selectionAttaqueCombat(m1, ctxtsvc);
				}
				else {
					System.out.println(m2.getNom()+" attaque "+m1.getNom()+" en premier");
					m2.selectionAttaqueCombat(m1, ctxtsvc);
					System.out.println(m1.getNom()+" attaque "+m2.getNom());
					m1.selectionAttaqueCombat(m2, ctxtsvc);
				}
			}
		}
		catch (PVException e) {System.err.println(e);
		if (sacha.getEquipePlayer().getFirst().getPv()<=0 && sacha.checkEquipeJoueur()) {
			sacha.changeMonsterActif(1);
			combat(sacha, m2);
		}
		}
	}

	/** Permet de lancer plusieurs combat contre des monstres sauvages gÃ©nÃ©rÃ©s alÃ©atoirement
	 * Pour le moment, le nombre de rencontres est parametrÃ© de base avec 10 en entrÃ©
	 * Les monstres rencontrÃ©s sont : 5 x niveau 1, 3 x niveau 2 (le 6eme+) et 2 x niveau 3(le 9eme+)
	 * Il y a de l'affichage dans la console
	 * @param nbSauvage : int ; nombre de crÃ©atures sauvages rencontrÃ©es d'affillÃ©es
	 **/
	public void rencontreSauvage(int nbSauvage, Player sacha) {

		System.out.println("Vous allez rencontrer "+nbSauvage+" Fakemon sauvages.");
		Monster m = null;	

		ArrayList<Monster> fakemonSauvage = new ArrayList<Monster>();
		fakemonSauvage = player.tableRencontre(nbSauvage);

		for(int i=0;i<nbSauvage;i++) {
			System.out.println("\n---------\nRencontre nÂ°"+(i+1)+" :");
			m = fakemonSauvage.get(i);

			if (i>=8) {
				m.levelUp();
				m.levelUp();
			}
			else if (i>=5) {
				m.levelUp();
			}

			System.out.println("Vous allez combatre un "+m.getNom()+" sauvage de niveau "+m.getLevel()+".");
			combat(sacha,m);
			sacha.soinEquipeJoueur();
		}
	}

	public void combatDresseur(Player sacha, Dresseur dresseur){
		Monster mPlayer = sacha.getEquipePlayer().getFirst();
		Monster mDresseur = dresseur.getEquipeDresseur().getFirst();
		String scString = "chose"; int scInt = 0;
		try {
			while (mPlayer.getPv()>0 && mDresseur.getPv()>0) {
				while (scString.equalsIgnoreCase("y") && scString.equalsIgnoreCase("n")) {
					scString = saisieString("Voulez-vous changer de monstre actif ?");
				}
				if(scString.equalsIgnoreCase("y")) {
					while (scInt < 1 || scInt > sacha.getEquipePlayer().size()) {
						for (Monster m : sacha.getEquipePlayer()) {System.out.println(m.toStringGeneralPV());}
						scInt = saisieInt("Quel monstre voulez-vous en monstre actif?");
					}
					sacha.changeMonsterActif(scInt);
				}
				else if (mPlayer.initiative(mDresseur).equals(mPlayer)) {
					System.out.println("Votre "+mPlayer.getNom()+" attaque le "+mDresseur.getNom()+" adverse en premier");
					mPlayer.selectionAttaqueCombat(mDresseur, ctxtsvc);
					System.out.println("Le "+mDresseur.getNom()+" adverse attaque votre "+mPlayer.getNom());
					mDresseur.selectionAttaqueCombat(mPlayer, ctxtsvc);
				}
				else {
					System.out.println("Le "+mDresseur.getNom()+" adverse attaque votre "+mPlayer.getNom()+" en premier");
					mDresseur.selectionAttaqueCombat(mPlayer, ctxtsvc);
					System.out.println("Votre "+mPlayer.getNom()+" attaque le "+mDresseur.getNom()+" adverse");
					mPlayer.selectionAttaqueCombat(mDresseur, ctxtsvc);
				}
			}
		}
		catch (PVException e) {System.err.println(e);
		if (mPlayer.getPv()<=0 && sacha.checkEquipeJoueur()) {
			System.out.println("Vous devez changer de Fakemon. Qui voulez-vous envoyer ?");
			sacha.changeMonsterActif(1);
			combatDresseur(sacha, dresseur);
		}
		else if (mDresseur.getPv()<=0 && dresseur.checkEquipeDresseur()) {
			System.out.println("Le dresseur adverse change de fakemon.");
			dresseur.fakemonSuivant();
			combatDresseur(sacha, dresseur);
		}
		}
	}

	//___________________________________________________________________________________________________

	public int calcPointsEquipe(LinkedList<Monster> listeMonstre) {
		int pts = 0;
		for (Monster m : listeMonstre) {

			logger.debug("Level du monstre avant boucle = {}!", m.getLevel());
			for (int lv = m.getLevel()-1; lv >= 1; lv--) {
				m.setLevel(lv);
				logger.debug("Level du monstre dans la boucle = {}!", m.getLevel());
				pts += m.getExpNextLevel();
				logger.debug("Valeur de pts dans la boucle lv d'un monstre= {}!", pts);
			}
			pts += 3;
			logger.debug("Valeur de pts à la fin d'un monstre = {}!", pts);
		}
		return pts;
	}








	/**	Grosse mÃ©thode de combat avec les dresseurs : l'arÃ¨ne
	 * 
	 * @param nbDresseurIntermediaires int ; nombre de dresseurs intermédiaire, c'est a dire en dehors du premier et dernier dresseur qui eux sont fixes
	 */
	public void arene(Player sacha) {

		System.out.println("\n**(o)***(o)***(o)**\nBienvenue dans l'arène ! Préparez-vous à affronter des adversaires de plus en plus corriaces.");
		int pts = 35;	//	Pensez à mettre la nouvelle fonction de calcPoints !

		Dresseur d = new Dresseur("FragileJordan", pts, player);
		System.out.println("Premier duel d'échauffement contre FragileJordan.");
		System.out.println(d.toStringEquipe());
		combatDresseur(sacha, d);
		sacha.soinEquipeJoueur();
		for (Monster m : d.getEquipeDresseur()) {
			pts+=m.getExpGain();
		}
		pts=(int)(pts*1.08);

		for (int i = 0;i<sacha.getMaxArene()-2;i++) {
			d = new Dresseur(pts, player);
			System.out.println("Duel numÃ©ro "+(i+1)+" contre "+d.getNom()+".");
			System.out.println(d.toStringEquipe());
			combatDresseur(sacha, d);
			sacha.soinEquipeJoueur();
			for (Monster m : d.getEquipeDresseur()) {
				pts+=m.getExpGain();
			}
			pts=(int)(pts*1.08);
		}

		d = new Dresseur("BlackJordan",(int)(pts*1.1574), player);
		System.out.println("Dernier duel contre le maître BlackJordan.");
		System.out.println(d.toStringEquipe());
		combatDresseur(sacha, d);
		sacha.soinEquipeJoueur();
		System.out.println("Bravo l'arène est finie !");

	}


	//___________________________________________________________________________________________________
	//	Méthodes de test de fonction
	public void run(Player sacha) {
		sacha.selectionStarter(player.tableRencontre(6));
		rencontreSauvage(10, sacha);
		arene(sacha);
	}

	public void test1(Player sacha) {
		ArrayList<Monster> ltArray = player.tableRencontre(3);
		LinkedList<Monster> ltLinked = new LinkedList<>();
		ltLinked.addAll(ltArray);
		sacha.setEquipePlayer(ltLinked);
		for (Monster m : sacha.getEquipePlayer()) {
			m.setContextService(ctxtsvc);
			m.levelUp();
			m.levelUp();
			m.levelUp();
		}
		arene(sacha);
	}
	public void test(Player sacha) {
		ArrayList<Monster> ltArray = player.tableRencontre(3);
		LinkedList<Monster> ltLinked = new LinkedList<>();
		ltLinked.addAll(ltArray);
		sacha.setEquipePlayer(ltLinked);
		for (Monster m : sacha.getEquipePlayer()) {
			m.setContextService(ctxtsvc);
			m.levelUp();
			//	m.levelUp();
			//	m.levelUp();
		}
		sacha.getEquipePlayer().getFirst().setPv(0);
		sacha.changeMonsterActif(1);
		for (Monster m : sacha.getEquipePlayer()) {
			System.out.println(m.toStringGeneral());
		}
	}

	public void init(AnnotationConfigApplicationContext monContext) {
		Player sacha = new Player();
		player = monContext.getBean(PlayerService.class);
		ctxtsvc = monContext.getBean(ContextService.class);
		run(sacha);
	}

	public static void main(String[] args) {
		SpringApplication.run(FakemonBootApplication.class, args);


	}

}
