package controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Dresseur;
import service.ContextService;
import service.PlayerService;

@Controller
@RequestMapping("/mechanics")
public class GameMechanics {
	
	@Autowired
	PlayerService player;
	@Autowired
	ContextService ctx;
	
	
	@PostMapping("/session")
	@ResponseBody
	public void resetSession(HttpServletRequest request) {
		request.getSession().invalidate();
	}
	
	@GetMapping("/scene/setup")
	@ResponseBody
	public String getSceneSetup(HttpServletRequest request) {

		String rencontre = "";
		if(player.peutRencontrer()) {
			Random r = new Random();
			//	Set la position de la rencontre avec le fakemon sauvage. La position ne peut être sur les bordures et est positionné sur la partie basse de la carte
			rencontre =  "["+(r.nextInt(8)+1)+","+(r.nextInt(4)+5)+"]";
		}
		ArrayList<String> scenes = new ArrayList<String>();
		String interaction0 = "[{\"pos\" : [3,1],\"event_type\" : \"script\", \"script\" : \"assets/js/starter.js\",\"prop\" : {\"pos\":[3,0],\"asset\":\"assets/img/Poke-Ball-32.png\"}}]";
		String goAilleurs = "[{\"pos\" : [9,5],\"orientation\" : \"east\",\"id\" : 2},{\"pos\" : [5,9],\"orientation\" : \"south\",\"id\" : 3}]";
		String style0 = "{\"portail\" : \"assets/img/peronIndoor.png\"}";
		String scene0 = "{\"type\" : \"indoor\",\"style\" :"+style0+",\"id\" : 1, \"nowalk\" : {\"0\":[0,1,2,3,4,5,6,7,8]}, \"triggers\" : {\"encounter\":[],\"interact\" : "+interaction0+",\"scenes\" : "+goAilleurs+"}, \"startpos\" : [5,5], \"background\" : \"assets/img/fond2.png\"}";
		
		String interaction1 = "[]";
		String goAilleurs1 = "[{\"pos\" : [0,5],\"orientation\" : \"west\",\"id\" : 1}]";
		String style1 = "{\"portail\" : \"assets/img/peronOutdoor.png\"}";
		String scene1 = "{\"type\" : \"wilds\",\"style\" :"+style1+",\"id\" : 2, \"nowalk\" : {}, \"triggers\" : {\"encounter\":"+rencontre+",\"interact\" : "+interaction1+",\"scenes\" : "+goAilleurs1+"}, \"startpos\" : [0,5], \"background\" : \"assets/img/outdoor.png\"}";
		
		
		String interaction2 = "[]";
		String goAilleurs2 = "[{\"pos\" : [5,0],\"orientation\" : \"north\",\"id\" : 1}]";
		String style2 = "{\"portail\" : \"assets/img/peronIndoor.png\"}";
		String scene2 = "{\"type\" : \"arena\",\"script\" : \"assets/js/arene.js\",\"style\" :"+style2+",\"id\" : 3, \"nowalk\" : {\"4\":[0,1,2,3,4,5,6,7]}, \"triggers\" : {\"encounter\":[],\"interact\" : "+interaction2+",\"scenes\" : "+goAilleurs2+"}, \"startpos\" : [5,0], \"background\" : \"assets/img/fondScene.png\"}";

		scenes.add(scene0);
		scenes.add(scene1);
		scenes.add(scene2);
		if(player.getIdScene() == 0) {
			return scenes.get(player.getIdScene());
		}else {
			return scenes.get(player.getIdScene()-1);
		}
		
	}
	
	@GetMapping("scene/{id}")
	@ResponseBody
	public String getSceneById(@PathVariable int id) {
		String rencontre = "";
		if(player.peutRencontrer()) {
			Random r = new Random();
			rencontre =  "["+r.nextInt(9)+","+(r.nextInt(4)+5)+"]";
		}
		ArrayList<String> scenes = new ArrayList<String>();
		String interaction0 = "[{\"pos\" : [3,1],\"event_type\" : \"script\", \"script\" : \"assets/js/starter.js\",\"prop\" : {\"pos\":[3,0],\"asset\":\"assets/img/Poke-Ball-32.png\"}}]";
		String goAilleurs = "[{\"pos\" : [9,5],\"orientation\" : \"east\",\"id\" : 2},{\"pos\" : [5,9],\"orientation\" : \"south\",\"id\" : 3}]";
		String style0 = "{\"portail\" : \"assets/img/peronIndoor.png\"}";
		String scene0 = "{\"type\" : \"indoor\",\"style\" :"+style0+",\"id\" : 1, \"nowalk\" : {\"0\":[0,1,2,3,4,5,6,7,8]}, \"triggers\" : {\"encounter\":[],\"interact\" : "+interaction0+",\"scenes\" : "+goAilleurs+"}, \"startpos\" : [5,5], \"background\" : \"assets/img/fond2.png\"}";
		
		String interaction1 = "[]";
		String goAilleurs1 = "[{\"pos\" : [0,5],\"orientation\" : \"west\",\"id\" : 1}]";
		String style1 = "{\"portail\" : \"assets/img/peronOutdoor.png\"}";
		String scene1 = "{\"type\" : \"wilds\",\"style\" :"+style1+",\"id\" : 2, \"nowalk\" : {}, \"triggers\" : {\"encounter\":"+rencontre+",\"interact\" : "+interaction1+",\"scenes\" : "+goAilleurs1+"}, \"startpos\" : [0,5], \"background\" : \"assets/img/outdoor.png\"}";
		
		
		String interaction2 = "[]";
		String goAilleurs2 = "[{\"pos\" : [5,0],\"orientation\" : \"north\",\"id\" : 1}]";
		String style2 = "{\"portail\" : \"assets/img/peronIndoor.png\"}";
		String scene2 = "{\"script\" : \"assets/js/arene.js\",\"type\":\"arena\",\"style\" :"+style2+",\"id\" : 3, \"nowalk\" : {\"4\":[0,1,2,3,4,5,6,7]}, \"triggers\" : {\"encounter\":[],\"interact\" : "+interaction2+",\"scenes\" : "+goAilleurs2+"}, \"startpos\" : [5,0], \"background\" : \"assets/img/fondScene.png\"}";

		scenes.add(scene0);
		scenes.add(scene1);
		scenes.add(scene2);
		return scenes.get(id-1);
	}
	
	@GetMapping("/select")
	public String getSelectMenu() {
		System.out.println("Go select");
		return "selectMonster";
	}
	
	public void generateArena(HttpServletRequest request) {
		LinkedList<Dresseur> arene = new LinkedList<Dresseur>();
		System.out.println("genere une arene");
		for(int i =0; i<1;i++) {
			arene.add(new Dresseur(i, player));
		}
		
		request.getSession().setAttribute("arene", arene);
	}
	
	@GetMapping("/arene/pop")
	@ResponseBody
	public String getDresseur(HttpServletRequest request) {
		System.out.println("taille arene : "+ctx.getArene().size());
		ObjectMapper om = new ObjectMapper();
		String dresseur="";
		System.out.println("dresseur");
		System.out.println(request.getSession().getAttribute("dresseur"));
		System.out.println(request.getSession().getAttribute("dresseur") == null);
		if(!ctx.getArene().isEmpty()) {
			if(request.getSession().getAttribute("dresseur") == null || ((String)request.getSession().getAttribute("dresseur")).length() <= 0) {
				System.out.println("Il reste des dresseur, setting attribut");
				System.out.println((String)request.getSession().getAttribute("dresseur"));
				Dresseur d = ctx.getArene().peek();
				request.getSession().setAttribute("dresseur", d.getUniqueId().toString());
				request.getSession().setAttribute("adversaire", d.getEquipeDresseur().peek());
				dresseur = "{\"pos\" : [5,9],\"event_type\" : \"dresseur\"}";
			}else {
				System.out.println("Il reste des dresseur, attribut deja set");
				ctx.getArene().forEach(d -> System.out.println(d.getNom()+" : "+d.getUniqueId()));
				System.out.println((String)request.getSession().getAttribute("dresseur"));
				Dresseur dres = ctx.getArene().stream().filter(d -> d.getUniqueId().toString().equals((String)request.getSession().getAttribute("dresseur"))).findFirst().get();
				System.out.println("nom : "+dres.getNom());
				if(!dres.checkEquipeDresseur() && dres.getEquipeDresseur().size() == 0) {
					System.out.println("All check pass");
					ctx.getArene().remove(dres);
				}
				if(ctx.getArene().size() > 0) {
					request.getSession().setAttribute("dresseur", ctx.getArene().peek().getUniqueId().toString());
					request.getSession().setAttribute("adversaire", ctx.getArene().peek().getEquipeDresseur().peek());
					dresseur = "{\"pos\" : [5,9],\"event_type\" : \"dresseur\"}";
				}
			}
		}
		System.out.println(dresseur);
		return dresseur;
	}
	
	/*@GetMapping("/arene/left")
	@ResponseBody
	public boolean trainerSleft(HttpServletRequest request) {
		return !ctx.getArene().isEmpty();
	}*/
	
}
