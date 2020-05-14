package fr.project.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.project.model.Monster;
import fr.project.service.PlayerService;

@Controller
public class MainController {
	
	@Autowired
	PlayerService player;
	
	@GetMapping({"/index", "", "/"})
	public String accessIndex() {
		System.out.println("dans le main");
		return "index";
	}
	
	@GetMapping("/scene")
	public String toScene() {
		return "gamescene";
	}
	
	@GetMapping("/select")
	public String getSelectMenu() {
		return "selectMonster";
	}
	
	@PostMapping("/combat")
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
	
}
