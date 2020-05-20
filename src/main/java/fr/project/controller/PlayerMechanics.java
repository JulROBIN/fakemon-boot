package fr.project.controller;

import java.util.ArrayList;
import java.util.List;
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
import fr.project.service.PlayerService;

@Controller
@RequestMapping("/player")
public class PlayerMechanics {
	@Autowired
	PlayerService ps;
	
	@Autowired
	IDAOMonsterBase mb;
	
	@Autowired
	IDAOPlayer daoP;
	
	// Attribut temporaire le temps de trouver mieux
	ArrayList<Integer> seenMonsters = new ArrayList<>();
	/**
	 * 
	 * @param id (un UUID en string)
	 * @return
	 */
	@PostMapping("/starter/{id}")
	@ResponseBody
	public String selectStarter(@PathVariable int id,HttpServletRequest request) {
		Player p = daoP.getOne((int)request.getSession().getAttribute("player"));
		MonsterEntity m = ps.getStarters().stream().filter(monster -> monster.getId() == id).findFirst().get();
		System.out.println(m);
		p.addEquipePlayer(m);
		mb.save(m);
		daoP.save(p);
		System.out.println(p);
		return "";
	}
	
	@PostMapping("/starter/pop")
	@ResponseBody
	public String popStarter(HttpServletRequest request) {
		Player p = daoP.getOne((int)request.getSession().getAttribute("player"));
		Optional<MonsterEntity> response  = ps.getStarters().stream().findAny();
		String returnBody = "{}";
		if(response.isPresent()) {
			MonsterEntity m = response.get();
			seenMonsters.add(m.getId());
			ObjectMapper om = new ObjectMapper();
			try {
				returnBody = om.writeValueAsString(m);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}else {
			System.err.println("YA PAS");
		}
		return returnBody;
	}
	
	@PostMapping("/posupdate")
	@ResponseBody
	public boolean playerInfos(@RequestParam int x, @RequestParam int y, @RequestParam int scene,@RequestParam String localisation, HttpServletRequest request) {
		Player p = daoP.getOne((int)request.getSession().getAttribute("player"));
		p.setIdScene(scene);
		p.setPosition(new int[]{x,y});
		request.getSession().setAttribute("localisation", localisation);
		return true;
	}
	
	@GetMapping("/infos")
	@ResponseBody
	public String getPlayerInfos(HttpServletRequest request) {
		Player p = daoP.getOne((int)request.getSession().getAttribute("player"));
		return "{ \"playerPos\" : ["+p.getPosition()[0]+","+p.getPosition()[1]+"]}";
	}
	
	@GetMapping("/infosTest")
	@ResponseBody
	public Player getPlayerInfosTest(HttpServletRequest request) {
		Player p = daoP.getOne((int)request.getSession().getAttribute("player"));
		System.out.println("Player p "+request.getSession().getAttribute("player"));
		System.out.println("player objet : "+p);
		ObjectMapper om = new ObjectMapper();
		String playerInfos ="{}";
		/*try {
			 playerInfos = om.writeValueAsString(p);
			//System.out.println(playerInfos);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}*/
		return p;
	}
	
	@GetMapping("/heal")
	@ResponseBody
	public void healSquad(HttpServletRequest request) {
		Player p = daoP.getOne((int)request.getSession().getAttribute("player"));
		p.soinEquipeJoueur();
	}
	
	@GetMapping("/squad")
	@ResponseBody
	public List<MonsterEntity> getSquad(HttpServletRequest request) {
		Player p = daoP.getOne((int)request.getSession().getAttribute("player"));
		System.out.println("Accessing squad");
		int size = p.getEquipePlayer().size();
		System.out.println("equipe");
		p.getEquipePlayer().forEach(System.out::println);
		//Gson gson = new Gson();
		
		ObjectMapper om = new ObjectMapper();
		
		/*String jsonToReturn = "";
		try {
			
			jsonToReturn = om.writeValueAsString(p.getEquipePlayer());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}*/
		
		//return jsonToReturn;
		return p.getEquipePlayer();
		
	}
	
	@PostMapping
	public void test(MonsterEntity mb) {
		System.out.println(mb);
	}
	
	
	
}
