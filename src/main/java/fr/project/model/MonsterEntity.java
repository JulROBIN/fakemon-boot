package fr.project.model;

import static fr.project.FakemonBootApplication.saisieInt;
import static fr.project.FakemonBootApplication.saisieString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import fr.project.projection.Views;

@Entity
@Table(name = "fakemons")
public class MonsterEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(Views.Common.class)
	private int id;
	
	@Column(name = "type", length = 15, nullable = false)
	@Enumerated(EnumType.STRING)
	@JsonView(Views.MonsterP.class)
	protected Type type; 

	@Column (name ="espece")
	@JsonView(Views.Common.class)
	protected String espece;
	
	@Transient
	protected String nom;
	
	@Column(name = "level")
	@JsonView(Views.MonsterP.class)
	protected int level; 
	
	@Column(name = "pv")
	@JsonView(Views.MonsterP.class)
	protected int pv;
	
	@Column(name = "pvmax")
	@JsonView(Views.MonsterP.class)
	protected int pvMax;
	
	@Column(name = "atk")
	@JsonView(Views.MonsterP.class)
	protected int atk;	
	
	@Column(name = "def")
	@JsonView(Views.MonsterP.class)
	protected int def;
	
	@Column(name = "aSp")
	@JsonView(Views.MonsterP.class)
	protected int aSp;	
	
	@Column(name = "dSp")
	@JsonView(Views.MonsterP.class)
	protected int dSp;
	
	@Column(name = "vit")
	@JsonView(Views.MonsterP.class)
	protected int vit;
	

	@Column (name = "basepv", nullable = false)
	protected double basePV;

	@Column (name = "attaque", nullable = false)
	protected double baseAtk;	

	@Column (name = "defense", nullable = false)
	protected double baseDef;

	@Column (name = "atk_speciale", nullable = false)
	protected double baseASp;

	@Column (name = "def_speciale", nullable = false)
	protected double baseDSp;

	@Column (name = "vitesse", nullable = false)
	protected double baseVit;
	
	@Column(name = "ivPV")
	protected double ivPV;
	@Column(name = "ivAtk")
	protected double ivAtk;
	@Column(name = "ivDef")
	protected double ivDef;
	@Column(name = "ivASp")
	protected double ivASp;
	@Column(name = "ivDSp")
	protected double ivDSp;
	@Column(name = "ivVit")
	protected double ivVit;
	
	@JsonIgnore
	@Transient
	protected double modifAtk=1;
	@JsonIgnore
	@Transient	
	protected double modifDef=1;
	@JsonIgnore
	@Transient
	protected double modifASp=1;
	@JsonIgnore
	@Transient
	protected double modifDSp=1;
	@JsonIgnore
	@Transient
	protected double modifVit=1;
	
	protected double[] tabNature = {1,1,1,1,1,1};
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "liste_attaques",
			uniqueConstraints = @UniqueConstraint(columnNames = { "id_monstre", "id_attaque" }),
			joinColumns = @JoinColumn(name = "id_monstre", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "id_attaque", referencedColumnName = "id")
		)
	@JsonView(Views.MonsterP.class)
	protected List<Attaque> listAttaque = new ArrayList<Attaque>();

	private Situation equipeJoueur = Situation.valueOf("Sauvage");

	@JsonView(Views.MonsterP.class)
	private int expNextLevel=5;
	
	@Column(name="exp")
	@JsonView(Views.MonsterP.class)
	private int exp=0;
	
	
	public MonsterEntity() {};
	public MonsterEntity(double basePV,double baseAtk,double baseDef,double baseASp,double baseDSp,double baseVit,String nom,List<Attaque> attaques,Type type) {
		this.setLevel(1);
		this.basePV = basePV;
		this.baseAtk = baseAtk;
		this.baseDef = baseDef;
		this.baseASp = baseASp;
		this.baseDSp = baseDSp;
		this.baseVit = baseVit;
		this.nom = nom;
		this.espece = nom;
		this.type = type;
		this.listAttaque = attaques;
		generationIV();	
		nature();
		calcStat();
	}
	
	public Situation getSituation() {
        return getEquipeJoueur();
    }
	
	public void setEquipeDresseur() {

		equipeJoueur=Situation.valueOf("Adversaire");
	}

	
	
	public String getEspece() {
		return espece;
	}
	public void setEspece(String espece) {
		this.espece = espece;
	}
	public void setEquipeJoueur() {
        equipeJoueur=Situation.valueOf("Joueur");
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setLevel(int level) {
		this.level = level;
		expNextLevel = ( (int) (7*this.level + Math.pow(this.level,2) )/2 )+1;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getLevel() {
		return level;
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public int getPvMax() {
		return pvMax;
	}

	public void setPvMax(int pvMax) {
		this.pvMax = pvMax;
	}

	public int getAtk() {
		return atk;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}

	public int getDef() {
		return def;
	}

	public void setDef(int def) {
		this.def = def;
	}

	public int getaSp() {
		return aSp;
	}

	public void setaSp(int aSp) {
		this.aSp = aSp;
	}

	public int getdSp() {
		return dSp;
	}

	public void setdSp(int dSp) {
		this.dSp = dSp;
	}


	public int getVit() {
		return vit;
	}


	public void setVit(int vit) {
		this.vit = vit;
	}

	public double getBasePV() {
		return basePV;
	}

	public void setBasePV(double basePV) {
		this.basePV = basePV;
	}

	public double getBaseAtk() {
		return baseAtk;
	}

	public void setBaseAtk(double baseAtk) {
		this.baseAtk = baseAtk;
	}

	public List<Attaque> getListAttaque() {
		return listAttaque;	
	}


	public double getBaseDef() {
		return baseDef;
	}

	public void setBaseDef(double baseDef) {
		this.baseDef = baseDef;
	}

	public double getBaseASp() {
		return baseASp;
	}

	public void setBaseASp(double baseASp) {
		this.baseASp = baseASp;
	}

	public double getBaseDSp() {
		return baseDSp;
	}

	public void setBaseDSp(double baseDSp) {
		this.baseDSp = baseDSp;
	}

	public double getBaseVit() {
		return baseVit;
	}

	public void setBaseVit(double baseVit) {
		this.baseVit = baseVit;
	}

	public double getIvPV() {
		return ivPV;
	}

	public void setIvPV(double ivPV) {
		this.ivPV = ivPV;
	}

	public double getIvAtk() {
		return ivAtk;
	}

	public void setIvAtk(double ivAtk) {
		this.ivAtk = ivAtk;
	}

	public double getIvDef() {
		return ivDef;
	}

	public void setIvDef(double ivDef) {
		this.ivDef = ivDef;
	}

	public double getIvASp() {
		return ivASp;
	}

	public void setIvASp(double ivASp) {
		this.ivASp = ivASp;
	}

	public double getIvDSp() {
		return ivDSp;
	}

	public void setIvDSp(double ivDSp) {
		this.ivDSp = ivDSp;
	}

	public double getIvVit() {
		return ivVit;
	}

	public void setIvVit(double ivVit) {
		this.ivVit = ivVit;
	}

	public double getModifAtk() {
		return modifAtk;
	}

	public void setModifAtk(double modifAtk) {
		this.modifAtk = modifAtk;
	}

	public double getModifDef() {
		return modifDef;
	}

	public void setModifDef(double modifDef) {
		this.modifDef = modifDef;
	}

	public double getModifASp() {
		return modifASp;
	}

	public void setModifASp(double modifASp) {
		this.modifASp = modifASp;
	}

	public double getModifDSp() {
		return modifDSp;
	}

	public void setModifDSp(double modifDSp) {
		this.modifDSp = modifDSp;
	}

	public double getModifVit() {
		return modifVit;
	}

	public void setModifVit(double modifVit) {
		this.modifVit = modifVit;
	}

	public void setListAttaque(List<Attaque> listAttaque) {
		this.listAttaque = listAttaque;
	};
	
	public int getExpGain() {
        int c = (2 + (int)(level + Math.pow(level,2)) /2);
        return c;
    }
	
	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	/**	Défini les IV (les statistiques cachées) du fakemon
	 *  N'est appellée que dans le constructeur et à aucun autre moment pour ne pas modifier ces valeurs en cours de route
	 */
	private void generationIV () {
		Random r=new Random();
		ivPV=r.nextInt(6);
		r=new Random();
		ivAtk=r.nextInt(6);
		r=new Random();
		ivDef=r.nextInt(6);
		r=new Random();
		ivASp=r.nextInt(6);
		r=new Random();
		ivDSp=r.nextInt(6);
		r=new Random();
		ivVit=r.nextInt(6);
	}
	
	/**	Génére une nature (fictivement car elle n'est pas réellement définie) qui module 2 statistiques du monstre
	 * N'est appellée que dans le constructeur et à aucun autre moment pour ne pas modifier ces valeurs en cours de route
	 */
	@JsonIgnore
	private void nature() {
		Random r=new Random();
		int stUp=r.nextInt(6);
		r=new Random();
		int stDown=r.nextInt(6);
		if (stUp!=stDown) {
			tabNature[stUp]=1.1;
			tabNature[stDown]=0.9;
		}	
	}
	
	/**	Calcule les nouvelles stats du niveau actuel et met à jour les points de vie du monstre
	 * 	Utilisée dans constructeur et aussi dans levelUp()
	 **/
	@JsonIgnore
	public void calcStat() {
		final double cstLv = (double) 1/5;
		pvMax = (int) (10+level+((basePV+ivPV)*tabNature[0]*(level*cstLv)));
		pv = pvMax;
		atk = (int) (5+((baseAtk+ivAtk)*tabNature[1]*(level*cstLv)));	
		def = (int) (5+((baseDef+ivDef)*tabNature[2]*(level*cstLv)));
		aSp = (int) (5+((baseASp+ivASp)*tabNature[3]*(level*cstLv)));	
		dSp = (int) (5+((baseDSp+ivDSp)*tabNature[4]*(level*cstLv)));
		vit = (int) (5+((baseVit+ivVit)*tabNature[5]*(level*cstLv)));
	}

	public int getExpNextLevel() {
		return expNextLevel;
	}

	public void setExpNextLevel(int expNextLevel) {
		this.expNextLevel = expNextLevel;
	}
	


	
	public void integrationEffetCumule(Attaque a, MonsterEntity m) throws PVException {
		if (a.getEffetCumule() != null) {


			//	Transforme le string de la BDD en liste, les infos sont organisé en Proba,Cible,Statistique,Sens,Cran
			String[] listeEffetCumule = a.getEffetCumule().split(",");
			Random r = new Random();
			MonsterEntity cible = null;

			if (r.nextInt(100)+1 < Integer.parseInt(listeEffetCumule[0])) {

				switch (listeEffetCumule[1]) {
				case "self" : cible = this; break;
				case "other" : cible = m; break;
				default : System.out.println("erreur de cible"); break;
				}

				switch (listeEffetCumule[2]) {
				case "pv" : modifPVCombat(listeEffetCumule[3], listeEffetCumule[4], cible);
				System.out.println("Les points de vie de "+cible.getNom()+" ont été "+listeEffetCumule[3]+" de "+listeEffetCumule[4]+" %"); break;
				case "atk" : cible.setModifAtk(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifAtk())); 
				System.out.println("L'attaque de "+cible.getNom()+" a été "+listeEffetCumule[3]+" de "+listeEffetCumule[4]+" cran"); break;
				case "def" : cible.setModifDef(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifDef())); 
				System.out.println("La défense de "+cible.getNom()+" a été "+listeEffetCumule[3]+" de "+listeEffetCumule[4]+" cran"); break;
				case "asp" : cible.setModifASp(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifASp())); 
				System.out.println("L'attaque spéciale de "+cible.getNom()+" a été "+listeEffetCumule[3]+" de "+listeEffetCumule[4]+" cran"); break;
				case "dsp" : cible.setModifDSp(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifDSp())); 
				System.out.println("La défense spéciale de "+cible.getNom()+" a été "+listeEffetCumule[3]+" de "+listeEffetCumule[4]+" cran"); break;
				case "vit" : cible.setModifVit(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifVit())); 
				System.out.println("La vitesse de "+cible.getNom()+" a été "+listeEffetCumule[3]+" de "+listeEffetCumule[4]+" cran"); break;
				case "all" : cible.setModifAtk(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifAtk())); 
				cible.setModifDef(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifDef()));
				cible.setModifASp(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifASp()));
				cible.setModifDSp(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifDSp()));
				cible.setModifVit(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifVit()));  
				System.out.println("Toutes les statistiques de "+cible.getNom()+" a été "+listeEffetCumule[3]+" de "+listeEffetCumule[4]+" cran");break;
				default : System.out.println("erreur de stat"); break;
				}
			}
		}
	}


	/** Methode qui met a jour la modif de stat du monstre actuel
	 * 
	 * @param sens : "up" ou "down" selon si la stat doit augmentée ou diminuée
	 * @param valeur : nombre de rang d'évolution, donné en string car converti en int à l'intérieur
	 * @param valeurModifActuelle : valeur de la modifStat à modifiée
	 * @return nouvelle valeur de la modification de statistique en combat
	 */
	@JsonIgnore
	private double modifStatCombat(String sens, String valeur, double valeurModifActuelle) {

		// Array de 13 valeurs avec valeur de base en position 6 
		double[] modifStats = {0.25, (double) 2/7, (double) 2/6, 0.4, 0.5, (double) 2/3, 1, 1.5, 2, 2.5, 3, 3.5, 4};

		int position = 0;
		int i = 0;

		for (double v : modifStats) {
			if (v == valeurModifActuelle) {
				position = i;
			}
			i++;
		}

		double newModif = 1;

		try  {
			switch (sens) {
			case "up" : newModif=modifStats[position+Integer.parseInt(valeur)]; break;
			case "down" : newModif=modifStats[position-Integer.parseInt(valeur)];break;
			default : System.out.println("Problem de sens"); break;
			}
		}catch (Exception e) {e.printStackTrace();
		if (position+Integer.parseInt(valeur)>13) {newModif = 4;}
		else if (position-Integer.parseInt(valeur)<0) {newModif = 0.25;}
		}

		return newModif;
	}

	@JsonIgnore
	private void modifPVCombat(String sens, String valeur, MonsterEntity cible) throws PVException {

		double ratio = 1;
		if (sens.equals("up")) {
			ratio = (double) Integer.parseInt(valeur)/100;	
		}
		else if (sens.equals("down")) {
			ratio = (double) -Integer.parseInt(valeur)/100;	
		}
		else {
			System.out.println("Problème de sens aux modifPV");
		}

		cible.setPv((int) (cible.getPv() + cible.getPvMax() * ratio));

		if (cible.getPv()>cible.getPvMax()) {
			cible.setPv(cible.getPvMax());
		}
		else if (cible.getPv()<=0) {
			throw new PVException(cible); 
		}
	}
	
	
	public Attaque choixAttaque() {
		this.listAttaque.forEach(a -> System.out.println("- "+a.getNom()+" ["+a.getType()+", "+a.getEtat()+"] : Puissance = "+a.getPuissance()+", Precision = "+a.getPrecision()));
		int sc = saisieInt("Quelle attaque ? (1 � "+this.listAttaque.size()+")");

		while ( sc < 1 || sc > this.listAttaque.size() ) {
			System.out.println("Mauvaise saisie. Veuillez recommencer");
			sc = saisieInt("Quelle attaque ? (1 � "+this.listAttaque.size()+")");
		}
		
		return listAttaque.get(sc-1);
	}
	
	/**	Permet d'initialiser le combat dans la console
	 * Si c'est un monstre de l'équipe joueur, on utilise la méthode pour sélectionné son attaque
	 * Si c'est un monstre sauvage ou de dresseur, on utilise la méthode automatique
	 * @param m Monster ; Il s'agit du monstre adverse. Le monstre qui lance attaque est le "this"
	 * @throws PVException
	 
	@JsonIgnore
	public void selectionAttaqueCombat(MonsterEntity m) throws PVException {
		//		Boolean qui permet soit au joueur de choisir son attaque, soit à l'IA de le faire
		Attaque a = (equipeJoueur.equals(Situation.valueOf("Joueur"))) ? choixAttaque() : choixAttaqueBOT(m);	
		combat(m,a.getId());

	}*/
	

	

	/** Compare les deux vitesses des monstres en combat pour déterminer le plus rapide
	 * 	Si les deux monstres ont la même vitesse, réalise un 50/50 pour choisir le plus rapide
	 * @param m2 Monster ; Il s'agit du monstre adverse, qui n'appartient pas au Player
	 * @return Monster ; le monstre étant le plus rapide
	 */
	@JsonIgnore
	public MonsterEntity initiative(MonsterEntity m2) {

		MonsterEntity m = null;

		if (this.getVit()*this.modifVit > m2.getVit()*m2.getModifVit()) {
			m = this;
		}
		else if (this.getVit()*this.modifVit < m2.getVit()*m2.getModifVit()) {
			m = m2;
		}
		else {
			Random r = new Random();
			
			switch (r.nextInt(2)) {
			case 0 : m = this ; break;
			case 1 : m = m2 ; break;
			}
		}
		return m;
	}
	
	
	
	/** Gére le gain d'exp pour le monstre en cours et fait le levelUp si besoin
	 * 	Texte indicatif dans console
	 * @param m Monster ; Monstre qui a été mis KO par le monstre actuel du joueur
	 */
	@JsonIgnore
	public boolean expGain(MonsterEntity m) {

		exp += m.getExpGain();
		boolean doUp = false;
		if (exp>=expNextLevel) {
			System.out.println("Gain de niveau !");
			exp-=expNextLevel;
			System.out.println(this.getNom()+" est maintenant niveau "+(this.getLevel()+1)+" !");
			levelUp();
			doUp = true;
			System.out.println(this.toStringDetailStat());
		}
		System.out.println("Il reste "+(expNextLevel-exp)+" points d'expérience avant le niveau suivant\n");
		
		return doUp;
	}


	/** Fait prendre un niveau et recalcule les statistiques
	 * Aucune sortie dans la console
	 
	@JsonIgnore
	public void levelUpBack() {
		setLevel(level++);
		//expNextLevel=( (int) (7*this.getLevel() + Math.pow(this.getLevel(),2) )/2 )+1;
		calcStat();

		//--------------

		//--> ouvre un slot d'attaque!
		if (level==5) {

			//	Récupère nouvelle attaque et l'ajoute au moves du monstre
			listAttaque.add(a);
			if (this.equipeJoueur.equals(Situation.valueOf("Joueur"))) {
				System.out.println(this.nom+" à appris un nouveau move : "+listAttaque.get(3).getNom());
			}
		}

		//	-> propose trois nouvelle attaque en remplacement d'une actuelle
		if ( (level == 3 || level == 5 || level == 8 || level == 10) && this.equipeJoueur.equals(Situation.valueOf("Joueur"))) {
			System.out.println(this.getNom()+" peut remplacer une de ses attaque par l'une de ces attaque :");
			List<Attaque> proposition = new ArrayList<>();
			Attaque a = this.newAttaque(ctxtsvc);
			proposition.add(a);
			boolean b;

			while (proposition.size()<3) {
				a =  this.newAttaque(ctxtsvc);
				b = true;
				for (Attaque atk : proposition) {
					if (atk.getId() == a.getId()) {
						b = false;
					}
				}
				if (b) {
					proposition.add(a);
				}
			}
			System.out.println(proposition.stream().map(atk -> atk.toStringDetailAttaque()).collect(Collectors.joining("")));
			remplacementMove(0, 0, proposition, 0);
		}
	}*/
	
	/**
	 * 
	 * @param idMoveOublie
	 * @param idMoveAppris
	 * @param proposition
	 */
	@JsonIgnore
	private void remplacementMove(int idMoveOublie, int idMoveAppris, List<Attaque> proposition, int ouiOuNon) {

		boolean b = true;
		if (ouiOuNon == 0) {
			String sc = saisieString("\nVoulez-vous remplacer une attaque existante ? (Y : oui / N : non)");
			switch (sc) {
			case "Y" : b=false;break;
			case "N" : System.out.println("Pas de remplacement de move");break;
			default : System.out.println("Mauvaise saisie, veuillez recommencer");remplacementMove(0, 0, proposition, 0);break;
			}
		}

		if (b) {
			while (idMoveAppris<1) {
				int sc = saisieInt("Quelle move voulez-vous apprendre ? (1 à 3) : ");
				idMoveAppris=proposition.get(sc-1).getId();
			}

			while (idMoveOublie<1) {
				this.toStringDetailAttaque();
				int sc = saisieInt("\nQuelle move voulez-vous oublier ? (1 à 3) : ");
				idMoveOublie=this.getListAttaque().get(sc-1).getId();
			}

			int ocnzi = idMoveOublie;
			Attaque moveOublie = this.getListAttaque().stream().filter(atk -> atk.getId() == ocnzi ).findFirst().get();
			int dzdzd = idMoveAppris;
			Attaque moveAppris = proposition.stream().filter(atk -> atk.getId() == dzdzd).findFirst().get();
			this.getListAttaque().add(this.getListAttaque().indexOf(moveOublie), moveAppris);
			this.getListAttaque().remove(moveOublie);
			System.out.println("le move "+moveAppris.getNom()+" a remplacé le move "+moveOublie.getNom());
		}
	}
	
	/** Prise en compte des effets cumulé de l'attaque utilisée
	 *  Pour le moment un seul effet cumulé par attaque
	 *  Effet cumulé sur la modification temporaire de statistique ou des pv
	 */
	public void levelUp() {
			setLevel(level++);
		//expNextLevel=( (int) (7*this.getLevel() + Math.pow(this.getLevel(),2) )/2 )+1;
			calcStat();
		}

	@JsonIgnore
	public String toStringGeneralPV() {
		return "- "+nom+" ["+type+"] (PV = "+pv+"/"+pvMax+"): Attaques = "+listAttaque.stream().map( a -> a.getNom()).collect(Collectors.joining(", "));
	}
	@JsonIgnore
	public String toStringGeneral() {
		return "- "+nom+" ["+type+"] : Attaques = "+listAttaque.stream().map( a -> a.getNom()).collect(Collectors.joining(", "));
	}
	@JsonIgnore
	public String toStringDetailAttaque() {
		return "\n* "+listAttaque.stream().map( a -> a.getNom()+" ["+a.getType().toString()+", "+a.getEtat()+"] : Puissance = "+a.getPuissance()+", Precision = "+a.getPrecision()+" ("+a.getDescription()+")").collect(Collectors.joining("\n* "));
	}
	@JsonIgnore
	public String toStringDetailStat() {
	
		return "Niveau = " + level + ", Point de Vie = " + pv + ", Attaque = " + atk + ", D�fense = " + def + ", Attaque Spéciale = " + aSp + ", Défense Spéciale = " + dSp + ", Vitesse = " + vit + ", tabNature = " + Arrays.toString(tabNature);
	
	}
	
	

	@Override
	public String toString() {
		return "MonsterEntity [id=" + id + ", type=" + type + ", nom=" + nom + ", level=" + level + ", pv=" + pv
				+ ", pvMax=" + pvMax + ", atk=" + atk + ", def=" + def + ", aSp=" + aSp + ", dSp=" + dSp + ", vit="
				+ vit + ", basePV=" + basePV + ", baseAtk=" + baseAtk + ", baseDef=" + baseDef + ", baseASp=" + baseASp
				+ ", baseDSp=" + baseDSp + ", baseVit=" + baseVit + ", ivPV=" + ivPV + ", ivAtk=" + ivAtk + ", ivDef="
				+ ivDef + ", ivASp=" + ivASp + ", ivDSp=" + ivDSp + ", ivVit=" + ivVit + ", modifAtk=" + modifAtk
				+ ", modifDef=" + modifDef + ", modifASp=" + modifASp + ", modifDSp=" + modifDSp + ", modifVit="
				+ modifVit + ", tabNature=" + Arrays.toString(tabNature) + ", listAttaque=" + listAttaque
				+ ", equipeJoueur=" + equipeJoueur + ", expNextLevel=" + expNextLevel + ", exp=" + exp + "]";
	}

	public Situation getEquipeJoueur() {
		return equipeJoueur;
	}

	public void setEquipeJoueur(Situation equipeJoueur) {
		this.equipeJoueur = equipeJoueur;
	}
	
}
