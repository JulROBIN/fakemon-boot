package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping({"/index", "", "/"})
	public String accessIndex() {
		System.out.println("dans le main");
		return "index";
	}
	/*
	@GetMapping("/")
	public String accessIndexBase() {
		System.out.println("dans le main");
		return "/index";
	}*/
	
	@GetMapping("/scene")
	public String toScene() {
		return "gamescene";
	}
	
}
