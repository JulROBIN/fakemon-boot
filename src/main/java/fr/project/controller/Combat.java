package fr.project.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.project.dao.IDAOMonster;
import fr.project.dao.IDAOPlayer;
import fr.project.model.Action;
import fr.project.model.Dresseur;
import fr.project.model.MonsterEntity;
import fr.project.model.PVException;
import fr.project.model.Player;
import fr.project.service.ContextService;
import fr.project.service.MonsterService;
import fr.project.service.PlayerService;

@Controller
@RequestMapping("/combat")
public class Combat {

	@Autowired
	PlayerService player;
	
	@Autowired
	ContextService ctx;
	
	@Autowired
	MonsterService ms;
	
	@Autowired
	IDAOPlayer daoP;
	
	Player p;
	
	@Autowired
	IDAOMonster daoM;
	
	@PostMapping("/")
	public String launchCombat(@RequestParam Map<String,String> data, HttpServletRequest request) {
		p = daoP.getOne((int)request.getSession().getAttribute("player"));
		System.out.println(p == null);
		System.out.println("Test "+data.get("mstrId"));
		System.out.println("taille equipe : "+p.getEquipePlayer().size());
		MonsterEntity playerMonster =  p.getEquipePlayer().stream().filter(m -> m.getId() == Integer.valueOf(data.get("mstrId"))).findAny().get();
		request.getSession().setAttribute("attaquant", playerMonster);
		try {
			if(request.getSession().getAttribute("localisation").equals("wilds")) {
				request.getSession().setAttribute("adversaire", player.tableRencontre(1).get(0));
			}
		}catch(Exception e) {
			System.out.println("pas d'attribut \"location\"");
		}
		
		return "combat";
	}
	
	@PostMapping("switch")
	@ResponseBody
	public boolean switchMonster(@RequestParam String entity, @RequestParam int id, HttpServletRequest request) {
		p = daoP.getOne((int)request.getSession().getAttribute("player"));
		if(entity.contentEquals("player")) {
			MonsterEntity m = p.getEquipePlayer().parallelStream().filter(mon -> mon.getId() == id).findFirst().get();
			System.out.println("LE MONSTRE : "+m);
			request.getSession().setAttribute("attaquant", m);
		}
		
		
		
		return true;
	}
	
	
	@GetMapping("/capture")
	@ResponseBody
	public String capture(HttpServletRequest request) {
		p = daoP.getOne((int)request.getSession().getAttribute("player"));
		MonsterEntity m = (MonsterEntity) request.getSession().getAttribute("adversaire");
		StringBuffer sb = new StringBuffer();
		Action a = p.captureMonstre(m);
		sb.append("{");
		if(a.getM() == null) {
			sb.append("\"playerTurn\" : false,\"msg\" : \"").append(a.getMessage()).append("\",\"endFight\" : false, \"status\" : \"capture\"");
			
		}else {
			sb.append("\"playerTurn\" : false,\"msg\" : \"").append(a.getMessage()).append("\",\"endFight\" : true, \"status\" : \"capture\"");
		}
		sb.append("}");
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	@GetMapping("/setup")
	@ResponseBody
	public Map<String,MonsterEntity> setup(HttpServletRequest request) {

		StringBuffer sb = new StringBuffer();
		
		ObjectMapper om = new ObjectMapper();
		om.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		HashMap parametres = new HashMap<String,MonsterEntity>();
		
		/*try {
			sb.append("{ \"attaquant\" : ").append(om.writeValueAsString((MonsterEntity)request.getSession().getAttribute("attaquant"))).append(",");
			sb.append(" \"adversaire\" : ").append(om.writeValueAsString((MonsterEntity)request.getSession().getAttribute("adversaire"))).append("}");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}*/
		parametres.put("attaquant", (MonsterEntity)request.getSession().getAttribute("attaquant"));
		parametres.put("adversaire", (MonsterEntity)request.getSession().getAttribute("adversaire"));
		return parametres;
	}
	
	@PostMapping("/attaque")
	@ResponseBody
	public String combat(@RequestParam Map<String,String> data, HttpServletRequest request) {
		ObjectMapper om = new ObjectMapper();
		
		Player p = daoP.getOne((int)request.getSession().getAttribute("player"));
		
		int atkId = Integer.valueOf(data.get("atkId"));
		System.out.println("Atttaque : "+atkId);
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		MonsterEntity m1;
		MonsterEntity m2;
		

			MonsterEntity monster = (MonsterEntity)request.getSession().getAttribute("attaquant");
			m1 = p.getEquipePlayer().stream().filter(m -> m.getId() == monster.getId()).findAny().get();
			m2 = (MonsterEntity)request.getSession().getAttribute("adversaire");

			try {
				
				Action act = ms.combat(m2, m1, atkId);
				m2 = act.getM();
				sb.append("\"playerTurn\":false");
				sb.append(",\"endFight\":"+false+",\"msg\": \""+act.getMessage()+"\"");
			} catch (PVException e) {
				e.printStackTrace();
				
				String localisation = (String) request.getSession().getAttribute("localisation");
				if(localisation.contentEquals("arena")) {
					//player turn false,switch en face
					Dresseur d =  ctx.getArene().stream().filter(dres -> dres.getUniqueId().toString().equals(request.getSession().getAttribute("dresseur"))).findFirst().get();
					int uidMonstre = m2.getId();
					System.out.println(uidMonstre);
					System.out.println(d);
					if(d.checkEquipeDresseur()) {
						m1.expGain(m2);
						d.fakemonSuivant();
						m2 = d.getEquipeDresseur().peek();
						sb.append("\"playerTurn\":"+true);
						sb.append(",\"endFight\":"+false+",\"msg\": \""+d.getNom()+" change de monstre pour "+m2.getNom()+"\"");
					}else {
						m1.getExpGain();
						p.soinEquipeJoueur();
						sb.append("\"playerTurn\":false");
						sb.append(",\"endFight\":"+true+",\"msg\": \"Fin du combat !!\\n Tu as battu "+d.getNom()+"\"");
					}
					
				}else {
					if(m1.getPv() > 0)
						m1.getExpGain();
					p.soinEquipeJoueur();
					sb.append("\"playerTurn\":false");
					sb.append(",\"endFight\":"+true+",\"msg\": \"Fin du combat !!\"");
				}
					

			}
			
			request.getSession().setAttribute("attaquant", m1);
			request.getSession().setAttribute("adversaire", m2);
			

		
		sb.append(",\"status\" : \"attaque\"}");
		
		return sb.toString();

	}
	
	@PostMapping("/attaquebot")
	@ResponseBody
	public String combatBot(@RequestParam Map<String,String> data, HttpServletRequest request) {
		
		
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		
		MonsterEntity m1;
		MonsterEntity m2;
		ObjectMapper om = new ObjectMapper();

		MonsterEntity monster = (MonsterEntity)request.getSession().getAttribute("attaquant");
		m2 = p.getEquipePlayer().stream().filter(m -> m.getId() == monster.getId()).findAny().get();
		m1 = (MonsterEntity)request.getSession().getAttribute("adversaire");
		
		try {
			int atkId = ms.choixAttaqueBOT(m1).getId();
			Action act = ms.combat(m2,m1,atkId);
			m2 = act.getM();
			sb.append("\"playerTurn\":true");
			sb.append(",\"endFight\":"+false+",\"msg\": \""+act.getMessage()+"\"");
		} catch (PVException e) {
			e.printStackTrace();
			System.out.println("taille equipe joueur : "+p.getEquipePlayer().size());
			if(p.checkEquipeJoueur()) {
				sb.append("\"playerTurn\":true");
				sb.append(",\"endFight\":"+false+",\"msg\": \""+m2.getNom()+" est K.O !\"");
			}else {
				p.soinEquipeJoueur();
				sb.append("\"playerTurn\":false");
				sb.append(",\"endFight\":"+true+",\"msg\": \"Fin du combat !!\"");
			}
		}
		request.getSession().setAttribute("attaquant", m2);
		request.getSession().setAttribute("adversaire", m1);
		
		sb.append(",\"status\" : \"attaque\"}");
		System.out.println(sb.toString());
		return sb.toString();

	}
	
}
