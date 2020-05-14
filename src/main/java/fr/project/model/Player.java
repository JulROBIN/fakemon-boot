package fr.project.model;

import static fr.project.FakemonBootApplication.saisieInt;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import fr.project.model.MonsterEntity;
@Entity
@Table(name= "player")
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int id;
	
	@Column(name="nom")
	protected String nom = "Sacha";
	
	@ManyToMany
	@JoinTable(
			name = "equipes",
			uniqueConstraints = @UniqueConstraint(columnNames = { "id_player", "id_monstre" }),
			joinColumns = @JoinColumn(name = "id_player", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "id_monstre", referencedColumnName = "id")
		)
	protected List<MonsterEntity> equipePlayer = new LinkedList<MonsterEntity>();
	//protected ArrayList<Monster> starters = new ArrayList<Monster>();	//	Est-ce-que ça sert vraiment à quelque chose de conserver la liste des starter ???
	
	protected int[] position = new int[] {0,0};
	
	@Column(name="id_scene")
	private int idScene = 0;
	@Column(name="maxRencontre")
	private int maxRencontre = 10;
	@Column(name="cptRencontre")
	private int cptRencontre = 0;
	@Column(name="maxArene")
	private int maxArene = 4;
	@Column(name="cptArene")//	Nombre de dresseur de l'arène, sachant que le 1 et le dernier sont fixé. Doit être >= 2
	private int cptArene = 0;

	private PlayerService player;*/
/*	@Autowired

>>>>>>> Stashed changes

	//	Getters Setters et apparentés
	public LinkedList<MonsterEntity> getEquipePlayer() {
		return (LinkedList)this.equipePlayer;
	}
	public void setEquipePlayer(LinkedList<MonsterEntity> equipePlayer) {
		for (MonsterEntity m : equipePlayer) {
			m.setEquipeJoueur();
		}
		this.equipePlayer = equipePlayer;
	}
	public void addEquipePlayer(MonsterEntity m) {
		m.setEquipeJoueur();
		this.equipePlayer.add(m);
	}
	public int[] getPosition() {
		return this.position;
	}
	public void setPosition(int[] position) {
		this.position = position;
	}
	public int getIdScene() {
		return this.idScene;
	}
	public void setIdScene(int idScene) {
		this.idScene = idScene;
	}
/*	public PlayerService getPlayerService() {
		return this.player;
	}
	public void setPlayerService(PlayerService player) {
		this.player = player;
	}*/
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public int getMaxRencontre() {
		return maxRencontre;
	}
	public void setMaxRencontre(int maxRencontre) {
		this.maxRencontre = maxRencontre;
	}
	public int getCptRencontre() {
		return cptRencontre;
	}
	public void setCptRencontre(int cptRencontre) {
		this.cptRencontre = cptRencontre;
	}
	public int getMaxArene() {
		return maxArene;
	}
	public void setMaxArene(int maxArene) {
		this.maxArene = maxArene;
	}
	public int getCptArene() {
		return cptArene;
	}
	public void setCptArene(int cptArene) {
		this.cptArene = cptArene;
	}
	public ArrayList<Monster> getStarters() {
		if(starters.isEmpty()) {
			starters = player.tableRencontre(6);
		}
		return starters;
	}
	public PlayerService getPlayerService() {
		return this.player;
	}
=======

>>>>>>> Stashed changes
	
	//______________________________________________________________________________
	//	Méthodes

	/** Remet tout les monstres du joueur en sitation quiescente (PV, modifsStats...), par exemple après un combat 
	 **/
	public void soinEquipeJoueur() {
		for (MonsterEntity m : equipePlayer) {
			m.setPv(m.getPvMax());
			m.setModifAtk(1);
			m.setModifDef(1);
			m.setModifASp(1);
			m.setModifDSp(1);
			m.setModifVit(1);
		}
	}


	/**	Échange la place de deux monstres de l'équipe du joueur
	 * Version avec scanner pour le back
	 **/
	public void changeMonster() {
		equipePlayer.forEach(m -> System.out.println(m.toStringGeneral()));
		int im = saisieInt("Quel monstre voulez-vous changer de position ?");
		int ip = saisieInt("À quelle position voulez-vous le mettre ?");
		MonsterEntity m = equipePlayer.get(im-1);
		equipePlayer.set(im-1, equipePlayer.get(ip-1));
		equipePlayer.set(ip-1, m);
	}

	
	/**	Échange la place de deux monstres de l'équipe du joueur
	 * Version avec les deux index en entrée pour le front, aucune vérification des monstres
	 * @param position1 int ; Index du premier monstre
	 * @param position2 int ; Index du second monstre
	 **/
	public void changeMonster(int position1, int position2) {
		MonsterEntity m = equipePlayer.get(position1-1);
		equipePlayer.set(position1-1, equipePlayer.get(position2-1));
		equipePlayer.set(position2-1, m);
	}

	
	/**	Remplace le monstre dit "actif" (celui en première position de la liste de monstre du joueur) par un autre qui peux se battre
	 * il y a des vérification sur ce swap et le reset des modifStat
	 * @param i int ; Index du monstre souhaité en remplacement du monstre actif
	 **/
	public void changeMonsterActif(int i) {
		while (equipePlayer.get(i-1).getPv()<=0 && (i<1 || i>6) ) {			
			for (MonsterEntity m : equipePlayer) {
				System.out.println(m.toStringGeneral());
			}
			i = saisieInt("Le monstre sélectionné est hors-combat. Veuillez en sélectionner un autre");
		}
		this.getEquipePlayer().getFirst().setModifAtk(1);
		getEquipePlayer().getFirst().setModifDef(1);
		getEquipePlayer().getFirst().setModifASp(1);
		getEquipePlayer().getFirst().setModifDSp(1);
		getEquipePlayer().getFirst().setModifVit(1);
		changeMonster(1, i);
	}

	
	/**	Vérifie et renvoie "true" s'il reste dans l'équipe du joueur un monstre capable de se battre 
	 * Il faut qu'après le joueur selectionne un fakemon compatible pour continuer le combat
	 * @return boolean ; renvoie "true" s'il reste un monstre capable de se battre, "false" sinon
	 **/
	public boolean checkEquipeJoueur() {
		boolean reponse = false;
		for (MonsterEntity m : equipePlayer) {
			if (m.getPv()>0) {
				reponse = true;
			}
		}
		return reponse;
	}

	
	/**	Verifie s'il reste des rencontres à faire contre des monstres sauvages
	 * @return boolean ; renvoie "true" s'il est encore possible de faire des rencontres de monstre sauvage
	 **/
	public boolean peutRencontrer() {
		return this.cptRencontre <= this.maxRencontre;
	}
	
	
<<<<<<< Updated upstream
	/**	Crée une sélection aléatoire de six monstres puis le joueur doit en choisir un comme monstre de départ
	 **/
	public void selectionStarter () {

		ArrayList<Monster> table2Chen = player.tableRencontre(6);
		table2Chen.forEach(mi -> System.out.println(mi.toStringGeneral()));
		int i=0;
		while (i<1 || i>6) {
			i = saisieInt("Quel Fakemon souhaitez-vous comme starter ? (1 à 6)");
		}
		addEquipePlayer(table2Chen.get(i-1));
		System.out.println("Vous avez choisi "+table2Chen.get(i-1).getNom()+" !");
		System.out.println("Ses moves sont : "+table2Chen.get(i-1).toStringDetailAttaque());
		System.out.println("Ses statistiques sont : "+table2Chen.get(i-1).toStringDetailStat()+"\n");
	}
=======

>>>>>>> Stashed changes
	
	
	/**	Met dans l'equipe du joueur le starter sélectionné
	 * @param index int ; index dans la liste de du starter choisis
	 */
	public void selectStarter(int index) {
		//addEquipePlayer(starters.get(index));
	}

	
<<<<<<< Updated upstream
	/**	FRONT UNIQUEMENT
	 * @return
	 **/
	public Monster rencontreSauvage() {
		this.cptRencontre++;
		Monster  m = null;
		if(this.cptRencontre <= this.maxRencontre) {
			m = this.player.tableRencontre(1).get(0);
			if (this.cptRencontre >= (this.maxRencontre*4)/5) {
				m.levelUp();
				m.levelUp();
			}
			else if (this.cptRencontre >= this.maxRencontre/2) {
				m.levelUp();
			}
		}
		return m;
	}
=======
>>>>>>> Stashed changes

	
	/**	Vérifie si le monstre est capturable et réalise le test de capture
	 * Si la capture est réussie, ajoute le monstre à l'équipe du joueur
	 * @param m Monster ; Monster adverse qui doit être capturé
	 **/
	public Action captureMonstre(MonsterEntity m) {	// Fusion des méthodes de capture Front et back pour homogénéisation
		Action a = new Action();
		
		if (m.getSituation().equals(Situation.valueOf("Sauvage"))) {

			System.out.println("Tentative de capture du "+m.getNom()+" sauvage");
			double txCapPv = 1;
			if ((double) (m.getPv()/m.getPvMax()) <= 0.05) {
				txCapPv = 4;
			}
			else if ((double) (m.getPv()/m.getPvMax()) <= 0.15) {
				txCapPv = 3;
			}
			else if ((double) (m.getPv()/m.getPvMax()) <= 0.25) {
				txCapPv = 2.5;
			}
			else if ((double) (m.getPv()/m.getPvMax()) <= 0.5) {
				txCapPv = 2;
			}
			else if ((double) (m.getPv()/m.getPvMax()) <= 0.75) {
				txCapPv = 1.5;
			}

		//	double txCapNiveau = 21-m.getLevel();	//	Trop facile
		//	double txCapNiveau = 21-Math.pow(m.getLevel(), 2);	//	Trop rapidement impossible
			double txCapNiveau = 20+m.getLevel()-Math.pow(m.getLevel(), 2);
			if (txCapNiveau < 5) {
				txCapNiveau = 5;
			}
			
			int captureRate = (int) (2 * txCapPv * txCapNiveau);
			
			Random r = new Random();
			if (r.nextInt(100)+1>captureRate) {
				System.out.println("La capture de "+m.getNom()+" a échouée");
				a.setMessage("La capture de "+m.getNom()+" a échouée");
			}
			else {
				System.out.println("La capture de "+m.getNom()+" a réussi !");
				a.setM(m);
				a.setMessage("La capture de "+m.getNom()+" a réussie !");
				this.addEquipePlayer(m);
			}

		}
		else {
			System.out.println("Le monstre adverse n'est pas capturable");
			a.setMessage("Le monstre adverse n'est pas capturable");
		}
		return a;
	}
	@Override
	public String toString() {
		return "Player [id=" + id + ", nom=" + nom + ", equipePlayer=" + equipePlayer + ", position="
				+ Arrays.toString(position) + ", idScene=" + idScene + ", maxRencontre=" + maxRencontre
				+ ", cptRencontre=" + cptRencontre + ", maxArene=" + maxArene + ", cptArene=" + cptArene + "]";
	}
	
	

}
