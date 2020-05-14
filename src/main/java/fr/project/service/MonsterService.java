package fr.project.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.project.model.Action;
import fr.project.model.Attaque;
import fr.project.model.MonsterEntity;
import fr.project.model.PVException;
import fr.project.model.Situation;
import fr.project.model.Type;

//	Déclaration Attribut
@Entity
@Table(name = "fakemon_stats")
public class MonsterService {

	//Pour le calcul des stats : base commune de l'espèce
	@JsonIgnore
	@Column (name = "pv", nullable = false)
	protected double basePV;

	@JsonIgnore
	@Column (name = "attaque", nullable = false)
	protected double baseAtk;	

	@JsonIgnore
	@Column (name = "defense", nullable = false)
	protected double baseDef;

	@JsonIgnore
	@Column (name = "atk_speciale", nullable = false)
	protected double baseASp;

	@JsonIgnore
	@Column (name = "def_speciale", nullable = false)
	protected double baseDSp;

	@JsonIgnore
	@Column (name = "vitesse", nullable = false)
	protected double baseVit;


	@Column(name = "type", length = 15, nullable = false)
	@Enumerated(EnumType.STRING)
	protected Type type; 

	@Column (name ="espece", length = 15, nullable = false, insertable = false, updatable = false)
	protected String nom;

	@Column(name = "movepool", nullable = false)
	private String poolAtkString;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;


	@Autowired
	@Transient
	private ContextService ctxtsvc;

	private ArrayList<Attaque> listAttaque;


	/* 	Stats non-utiles pour le moment : futures implementation ?
	 * 	Mana ? Remplace les PP des attaques
	 *	protected int modifEsquive;
	 *	protected int modifPrécision;
	 *	protected int modifCritique;
	 */


	//___________________________________________
	//	Constructeur


	/** Constructeur pour JPA avec initialisation à partir de la BDD
	 * L'ajout des attaques ne fonctionne pas dans ce construteur, probablement pas un effet de timing
	 * Donc l'ajout des attaques se réalise dans une autre méthode : init()
	 */
	public MonsterService() {

	}

	/** Initilisation des attaques du monstre en dehors du constructeur car bug avec JPA
	 * Cette fonction est appellée après la construction de l'objet (PostLoad)
	 */
	//	@PostLoad
	//	public void init() {
	//		this.listAttaque = ctxtsvc.creationAttaque(poolAtkStringToInt(poolAtkString));
	//	}


	//___________________________________________
	//	Getters/Setters

	public MonsterEntity createMonsterEntity() {
		List<Attaque>attaques = ctxtsvc.creationAttaque(this.poolAtkStringToInt(poolAtkString));
		MonsterEntity m = new MonsterEntity(basePV, baseAtk, baseDef, baseASp, baseDSp, baseVit, nom, (ArrayList)attaques, type);
		return m;
	}

	//___________________________________________
	//	Méthodes

	/**	Calcule des dégâts et update les PV des monstres en fonction des différents paramettre : constantes, stab, efficacité, statistiques physiques ou spéciales
	 * 	Le premier test est de vérifier si l'attaque touche l'adversaire
	 * @param m : Monster ; Le monstre adverse qui vas se prendre l'attaque du monstre présent.
	 * @param idMove : int ; cet id est celui de l'attaque sélectionné dans une méthode précédente (ou par le formulaire en Front)
	 * @throws PVException : cette exception est renvoyée lorsque l'un des deux monstre ne peux plus se battre !
	 */
	@JsonIgnore
	public Action combat(MonsterEntity mDef,MonsterEntity mAtk, int idMove) throws PVException {

	/*	System.out.println(listAttaque);
		listAttaque.parallelStream().forEach(a -> System.out.println(a.getId()+" : "+a));*/
		Attaque a = listAttaque.parallelStream().filter(atk -> atk.getId() == idMove).findFirst().get();
		Random r = new Random();
		Action action = new Action();
		action.setM(mDef);
		action.append(mDef.getNom()+" utilise "+a.getNom());
		System.out.println(mDef.getNom()+" utilise "+a.getNom());
		
		if (r.nextInt(100)+1>a.getPrecision()) {
			System.out.println("L'attaque de "+mAtk.getNom()+" a ratée !");
			action.setMessage("L'attaque de "+mAtk.getNom()+" a ratée !");
		}
		else {
			//	set les paramettres de calcul des dégâts
			final double k1 = (double) 2/5;
			final double k2 = 50;

			//	set le bonus de stab
			double stab = 1.0;
			if (a.getType().equals(mAtk.getType())) {
				stab = 1.5;
			}


			//	set si l'attaque utilis�e est efficace ou non
			double type = ctxtsvc.getRatioEfficacite(a,mDef);
			if (type == 2) {
				System.out.println("L'attaque est super efficace !");
				action.setMessage("L'attaque est super efficace !");
			}
			if (type == 0.5) {
				System.out.println("L'attaque est peu efficace ...");
				action.setMessage("L'attaque est peu efficace ...");
			}

			//	détermine si l'attaque est physique ou spéciale
			double statDegat = 0;
			double statProtection = 0;
			switch (a.getEtat()) {
			case "Physique": statDegat=mAtk.getAtk()*mAtk.getModifAtk() ; statProtection=mDef.getDef()*mDef.getModifDef(); break;
			case "Special" : statDegat=mAtk.getaSp()*mAtk.getModifASp() ; statProtection=mDef.getdSp()*mDef.getModifDSp(); break;
			case "Statut" : break;
			default : System.out.println("erreur de dégât");break;
			}

			//	calcul des dégats
			int degat = (int) (((k1 * mAtk.getLevel() + 2) * a.getPuissance() * (double) statDegat / (k2 * statProtection) + 2 ) * stab * type );
			mDef.setPv(mDef.getPv() - degat);

			mAtk.integrationEffetCumule(a, mDef);

			if (mDef.getPv()<=0) {
				if (mAtk.getEquipeJoueur().equals(Situation.valueOf("Joueur"))) {
					mAtk.expGain(mDef);
				}
				throw new PVException(mDef);
			}
			else {
				System.out.println("Il reste "+mDef.getPv()+" PV (/"+mDef.getPvMax()+") a "+mDef.getNom()+".\n");
				action.setM(mDef);
			}
		}
		return action;
	}


	@JsonIgnore
	public Attaque choixAttaqueBOT(MonsterEntity m) {

		Random r = new Random();
		Attaque a = listAttaque.get(r.nextInt(this.listAttaque.size()));

		for (Attaque i : listAttaque) {
			if ( ctxtsvc.getRatioEfficacite(i,m) == 2 ) {
				r = new Random();
				if( r.nextInt(4) == 0 ) {
					a=i;
				}
			}
		}
		return a;
	}



	/** Converti à partir du movepool en String issu de la base de donnée la liste d'Integer necessaire pour les requêtes
	 * @param movepool String ; contenu dans la base de donnée avec les stats des fakemons
	 * @return 
	 */
	@JsonIgnore
	public Integer[] poolAtkStringToInt(String movepool) {

		String[] ids = movepool.split(",");
		Integer[] poolEntier = new Integer[ids.length];

		for(int i = 0; i < ids.length; i++){
			poolEntier[i]=Integer.valueOf(ids[i]);
		}	
		return poolEntier;
	}




	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public Attaque newAttaque(ContextService ctxtsvc, MonsterEntity me) {

		MonsterService m = ctxtsvc.getDaoMonster().findByNom(me.getNom());
		//		Récupère et converti la liste de tous les moves dans une List<>  
		Integer[] listIdTotal = this.poolAtkStringToInt(m.poolAtkString);
		List<Integer> listeFormate = Arrays.asList(listIdTotal);

		//	Retire les id des attaques déjà connues. Ne fonctionne pas avec remove()
		for (Attaque a : this.listAttaque) {
			listeFormate = listeFormate.stream().filter(mon -> mon != a.getId()).collect(Collectors.toList());
		}

		//	Récupère id de la nouvelle attaque
		Random r = new Random();
		int idNewMove =listeFormate.get(r.nextInt(listeFormate.size()));

		return ctxtsvc.getAttaqueid(idNewMove);
	}




	@JsonIgnore
	public ArrayList<Attaque> poolAttaque(ArrayList<Integer> ids) {

		this.listAttaque = ctxtsvc.poolAttaque(ids);
		return listAttaque;
	}





}
