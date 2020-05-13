$(document).ready(function(){
	killSession();
	// attribution des fonctions
	$("#btnContinuer").click(autofire)
	$("#btnOptions").click(autofire)
	$("#btnDemo").click(versCombat)
	$("#btnNouvelle").click(autofire)
	btnMenu = $("#btnMenuJoueur")
	btnMenu.click(versMenuJoueur)

	$("#menuList > li").hover(
		function(){
			$(this).find("button").text("["+$(this).find("button").text()+"]")
		},
		function(){
			$(this).find("button").text($(this).find("button").text().replace("[",''))
			$(this).find("button").text($(this).find("button").text().replace("]",''))
	});
	
	function autofire(){
		$(".collapse").collapse()
		$("#rickVid").get(0).play()
	}
	
	function versCombat(){
		$.ajax({
			type:"POST",
			url:'${pageContext.request.contextPath}/selection',
			data:"newSession="+true,
			success: function(){
				window.location.href='/selection'
			}
		})
	}
	
	function versMenuJoueur(){
		$.ajax({
			type:"POST",
			url:'/menujoueur',
			success: function(){
				window.location.href='/menujoueur'
			}
		})
	}
	
	function killSession(){
		$.ajax({
			type:'POST',
			url:'mechanics/session'
		})
	}
});