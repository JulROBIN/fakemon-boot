package controller;

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

import dao.IDAOMonster;
import model.Action;
import model.Dresseur;
import model.Monster;
import model.PVException;
import service.ContextService;
import service.PlayerService;

@Controller
@RequestMapping("/combat")
public class Combat {

	@Autowired
	PlayerService player;
	@Autowired
	ContextService ctx;
	
	
	@Autowired
	IDAOMonster daoM;
	
	@PostMapping("/")
	public String launchCombat(@RequestParam Map<String,String> data, HttpServletRequest request) {

		System.out.println("Test "+data.get("mstrId"));
		Monster playerMonster =  player.getEquipePlayer().stream().filter(m -> m.getUniqueId().toString().equals(data.get("mstrId"))).findAny().get();
		request.getSession().setAttribute("attaquant", playerMonster);
		try {
			if(request.getSession().getAttribute("localisation").equals("wilds")) {
				request.getSession().setAttribute("adversaire", player.rencontreSauvage());
			}
		}catch(Exception e) {
			System.out.println("pas d'attribut \"location\"");
		}
		
		return "combat";
	}
	
	@PostMapping("switch")
	@ResponseBody
	public boolean switchMonster(@RequestParam String entity, @RequestParam String id, HttpServletRequest request) {
		
		if(entity.contentEquals("player")) {
			Monster m = player.getEquipePlayer().parallelStream().filter(mon -> mon.getUniqueId().toString().equals(id)).findFirst().get();
			System.out.println("LE MONSTRE : "+m);
			request.getSession().setAttribute("attaquant", m);
		}
		
		
		
		return true;
	}
	
	
	@GetMapping("/capture")
	@ResponseBody
	public String capture(HttpServletRequest request) {
		Monster m = (Monster) request.getSession().getAttribute("adversaire");
		StringBuffer sb = new StringBuffer();
		Action a = player.captureMonstreFront(m);
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
	public String setup(HttpServletRequest request) {

		StringBuffer sb = new StringBuffer();
		
		ObjectMapper om = new ObjectMapper();
		om.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

		try {
			sb.append("{ \"attaquant\" : ").append(om.writeValueAsString((Monster)request.getSession().getAttribute("attaquant"))).append(",");
			sb.append(" \"adversaire\" : ").append(om.writeValueAsString((Monster)request.getSession().getAttribute("adversaire"))).append("}");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return sb.toString();
	}
	
	@PostMapping("/attaque")
	@ResponseBody
	public String combat(@RequestParam Map<String,String> data, HttpServletRequest request) {
		ObjectMapper om = new ObjectMapper();
		
		
		int atkId = Integer.valueOf(data.get("atkId"));
		System.out.println("Atttaque : "+atkId);
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		Monster m1;
		Monster m2;
		
		try {
			Monster monster = om.readValue(data.get("attaquant"), Monster.class);
			m1 = player.getEquipePlayer().stream().filter(m -> m.getUniqueId().toString().equals(monster.getUniqueId().toString())).findAny().get();
			m2 = om.readValue(data.get("adversaire"), Monster.class);

			try {
				
				Action act = m1.combat(m2,atkId,ctx);
				m2 = act.getM();
				sb.append("\"playerTurn\":false");
				sb.append(",\"endFight\":"+false+",\"msg\": \""+act.getMessage()+"\"");
			} catch (PVException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				String localisation = (String) request.getSession().getAttribute("localisation");
				if(localisation.contentEquals("arena")) {
					//player turn false,switch en face
					Dresseur d =  ctx.getArene().stream().filter(dres -> dres.getUniqueId().toString().equals(request.getSession().getAttribute("dresseur"))).findFirst().get();
					String uidMonstre = m2.getUniqueId().toString();
					System.out.println(uidMonstre);
					System.out.println(d);
					if(d.getEquipeDresseur().size()>0) {
						d.getEquipeDresseur().forEach(System.out::println);
						Monster mTemp = d.getEquipeDresseur().stream().filter(mDresseur -> mDresseur.getUniqueId().toString().equals(uidMonstre)).findFirst().get();
						d.getEquipeDresseur().remove(mTemp);
						if(d.checkEquipeDresseur()) {
							m2 = d.getEquipeDresseur().peek();
							sb.append("\"playerTurn\":"+true);
							sb.append(",\"endFight\":"+false+",\"msg\": \""+d.getNom()+" change de monstre pour "+m2.getNom()+"\"");
						}else {
							m1.getExpGain();
							player.soinEquipeJoueur();
							sb.append("\"playerTurn\":false");
							sb.append(",\"endFight\":"+true+",\"msg\": \"Fin du combat !!\\n Tu as battu "+d.getNom()+"\"");
						}
					}else {
						m1.getExpGain();
						player.soinEquipeJoueur();
						sb.append("\"playerTurn\":false");
						sb.append(",\"endFight\":"+true+",\"msg\": \"Fin du combat !!\\n Tu as battu "+d.getNom()+"\"");						
					}
				}else {
					if(m1.getPv() > 0)
						m1.getExpGain();
					player.soinEquipeJoueur();
					sb.append("\"playerTurn\":false");
					sb.append(",\"endFight\":"+true+",\"msg\": \"Fin du combat !!\"");
				}
					

			}
			
			request.getSession().setAttribute("attaquant", m1);
			request.getSession().setAttribute("adversaire", m2);
			
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		sb.append(",\"status\" : \"attaque\"}");
		
		return sb.toString();

	}
	
	@PostMapping("/attaquebot")
	@ResponseBody
	public String combatBot(@RequestParam Map<String,String> data, HttpServletRequest request) {
		//Gson gson = new Gson();
		//Monster m2 = gson.fromJson(data.get("attaquant"), Monster.class);
		//Monster m1 = gson.fromJson(data.get("adversaire"), Monster.class);
		
		
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		
		Monster m1;
		Monster m2;
		ObjectMapper om = new ObjectMapper();
		try {
			Monster monster = om.readValue(data.get("attaquant"), Monster.class);
			m2 = player.getEquipePlayer().stream().filter(m -> m.getUniqueId().toString().equals(monster.getUniqueId().toString())).findAny().get();
			m1 = om.readValue(data.get("adversaire"), Monster.class);	
			
			try {
				int atkId = m1.choixAttaqueBOT(m2,ctx).getId();
				Action act = m1.combat(m2,atkId,ctx);
				m2 = act.getM();
				sb.append("\"playerTurn\":true");
				sb.append(",\"endFight\":"+false+",\"msg\": \""+act.getMessage()+"\"");
			} catch (PVException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("taille equipe joueur : "+player.getEquipePlayer().size());
				if(player.checkEquipeJoueur()) {
					sb.append("\"playerTurn\":true");
					sb.append(",\"endFight\":"+false+",\"msg\": \""+m2.getNom()+" est K.O !\"");
				}else {
					player.soinEquipeJoueur();
					sb.append("\"playerTurn\":false");
					sb.append(",\"endFight\":"+true+",\"msg\": \"Fin du combat !!\"");
				}
			}
			request.getSession().setAttribute("attaquant", m2);
			request.getSession().setAttribute("adversaire", m1);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sb.append(",\"status\" : \"attaque\"}");
		System.out.println(sb.toString());
		return sb.toString();

	}
	
}
