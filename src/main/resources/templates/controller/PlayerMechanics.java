package controller;

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

import model.Monster;
import service.PlayerService;
@Controller
@RequestMapping("/player")
public class PlayerMechanics {
	@Autowired
	PlayerService player;
	// Attribut temporaire le temps de trouver mieux
	ArrayList<String> seenMonsters = new ArrayList<String>();
	/**
	 * 
	 * @param id (un UUID en string)
	 * @return
	 */
	@PostMapping("/starter/{id}")
	@ResponseBody
	public String selectStarter(@PathVariable String id) {
		Monster m = player.getStarters().stream().filter(monster -> monster.getUniqueId().toString().equals(id)).findFirst().get();
		player.addEquipePlayer(m);
		return "";
	}
	
	@PostMapping("/starter/pop")
	@ResponseBody
	public String popStarter() {
		Optional<Monster> response  = player.getStarters().stream().filter(monster -> !seenMonsters.contains(monster.getUniqueId().toString())).findAny();
		String returnBody = "{}";
		if(response.isPresent()) {
			Monster m = response.get();
			seenMonsters.add(m.getUniqueId().toString());
			ObjectMapper om = new ObjectMapper();
			try {
				returnBody = om.writeValueAsString(m);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return returnBody;
	}
	
	@PostMapping("/posupdate")
	@ResponseBody
	public boolean playerInfos(@RequestParam int x, @RequestParam int y, @RequestParam int scene,@RequestParam String localisation, HttpServletRequest request) {
		//System.out.println("Updating player informations : "+localisation);
		player.setIdScene(scene);
		player.setPosition(new int[]{x,y});
		request.getSession().setAttribute("localisation", localisation);
		return true;
	}
	
	@GetMapping("/infos")
	@ResponseBody
	public String getPlayerInfos() {
		return "{ \"playerPos\" : ["+player.getPosition()[0]+","+player.getPosition()[1]+"]}";
	}
	
	@GetMapping("/infosTest")
	@ResponseBody
	public String getPlayerInfosTest() {
		ObjectMapper om = new ObjectMapper();
		String playerInfos ="";
		try {
			 playerInfos = om.writeValueAsString(player);
			//System.out.println(playerInfos);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return playerInfos;
	}
	
	@GetMapping("/heal")
	@ResponseBody
	public void healSquad() {
		player.soinEquipeJoueur();
	}
	
	@GetMapping("/squad")
	@ResponseBody
	public String getSquad() {
		System.out.println("Accessing squad");
		int size = player.getEquipePlayer().size();
		System.out.println("equipe");
		System.out.println(player.getEquipePlayer());
		if(size == 0) {
			player.addEquipePlayer(player.rencontreSauvage());
		}
		//Gson gson = new Gson();
		
		ObjectMapper om = new ObjectMapper();
		
		String jsonToReturn = "";
		try {
			jsonToReturn = om.writeValueAsString(player.getEquipePlayer());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonToReturn;
		
	}
	
	
	
}
