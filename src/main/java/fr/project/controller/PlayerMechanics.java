package fr.project.controller;

import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.project.dao.IDAOMonsterBase;
import fr.project.dao.IDAOPlayer;
import fr.project.model.MonsterEntity;
import fr.project.model.Player;
import fr.project.service.MonsterService;
import fr.project.service.PlayerService;

@Controller
@RequestMapping("/player")
public class PlayerMechanics {
	@Autowired
	Player p;
	
	@Autowired
	IDAOMonsterBase mb;
	
	@Autowired
	IDAOPlayer daoP;
	
	// Attribut temporaire le temps de trouver mieux
	ArrayList<String> seenMonsters = new ArrayList<String>();
	/**
	 * 
	 * @param id (un UUID en string)
	 * @return
	 */
	@PostMapping("/starter/{id}")
	@ResponseBody
	public String selectStarter(@PathVariable String id,HttpServletRequest request) {
		p = daoP.getOne((int)request.getSession().getAttribute("player"));
		MonsterEntity m = player.getStarters().stream().filter(monster -> monster.getUniqueId().toString().equals(id)).findFirst().get();
		p.addEquipePlayer(m);
		return "";
	}
	
	@PostMapping("/starter/pop")
	@ResponseBody
	public String popStarter(HttpServletRequest request) {
		p = daoP.getOne((int)request.getSession().getAttribute("player"));
		Optional<MonsterEntity> response  = player.getStarters().stream().filter(monster -> !seenMonsters.contains(monster.getUniqueId().toString())).findAny();
		String returnBody = "{}";
		if(response.isPresent()) {
			MonsterEntity m = response.get();
			seenMonsters.add(m.getUniqueId().toString());
			ObjectMapper om = new ObjectMapper();
			try {
				returnBody = om.writeValueAsString(m);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return returnBody;
	}
	
	@PostMapping("/posupdate")
	@ResponseBody
	public boolean playerInfos(@RequestParam int x, @RequestParam int y, @RequestParam int scene,@RequestParam String localisation, HttpServletRequest request) {
		p = daoP.getOne((int)request.getSession().getAttribute("player"));
		p.setIdScene(scene);
		p.setPosition(new int[]{x,y});
		request.getSession().setAttribute("localisation", localisation);
		return true;
	}
	
	@GetMapping("/infos")
	@ResponseBody
	public String getPlayerInfos(HttpServletRequest request) {
		p = daoP.getOne((int)request.getSession().getAttribute("player"));
		return "{ \"playerPos\" : ["+p.getPosition()[0]+","+p.getPosition()[1]+"]}";
	}
	
	@GetMapping("/infosTest")
	@ResponseBody
	public String getPlayerInfosTest(HttpServletRequest request) {
		p = daoP.getOne((int)request.getSession().getAttribute("player"));
		ObjectMapper om = new ObjectMapper();
		String playerInfos ="";
		try {
			 playerInfos = om.writeValueAsString(player);
			//System.out.println(playerInfos);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return playerInfos;
	}
	
	@GetMapping("/heal")
	@ResponseBody
	public void healSquad(HttpServletRequest request) {
		p = daoP.getOne((int)request.getSession().getAttribute("player"));
		p.soinEquipeJoueur();
	}
	
	@GetMapping("/squad")
	@ResponseBody
	public String getSquad(HttpServletRequest request) {
		p = daoP.getOne((int)request.getSession().getAttribute("player"));
		System.out.println("Accessing squad");
		int size = p.getEquipePlayer().size();
		System.out.println("equipe");
		System.out.println(p.getEquipePlayer());
		if(size == 0) {
			p.addEquipePlayer(player.tableRencontre(1).get(0));
		}
		//Gson gson = new Gson();
		
		ObjectMapper om = new ObjectMapper();
		
		String jsonToReturn = "";
		try {
			
			jsonToReturn = om.writeValueAsString(p.getEquipePlayer());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return jsonToReturn;
		
	}
	
	@PostMapping
	public void test(MonsterEntity mb) {
		System.out.println(mb);
	}
	
	
	
}
