package fr.project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.project.dao.IDAOMonsterBase;
import fr.project.dao.IDAOPlayer;
import fr.project.model.MonsterEntity;
import fr.project.model.Player;
import fr.project.service.PlayerService;

@RestController
@CrossOrigin("*")
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
	@PostMapping("{id}/starter/")
	@ResponseBody
	public MonsterEntity selectStarter(@PathVariable int id,@RequestBody MonsterEntity m) {
		Player p = daoP.getOne(id);
		p.addEquipePlayer(m);
		mb.save(m);
		daoP.save(p);
		return m;
	}
	
	@GetMapping("{id}/starter/pop")
	@ResponseBody
	public String popStarter(@PathVariable int id) {
		Player p  = daoP.getOne(id);
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
	
	@GetMapping("/starter")
	@ResponseBody
	public String getStarters(HttpServletRequest request) {

		List<MonsterEntity> response  = ps.getStarters();
		String returnBody = "{}";
		if(!response.isEmpty()) {
			ObjectMapper om = new ObjectMapper();
			try {
				returnBody = om.writeValueAsString(response);
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
	
	@GetMapping("/{id}")
	@ResponseBody
	public Player getPlayerInfosTest(@PathVariable int id) {
		Player p = daoP.getOne(id);
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
