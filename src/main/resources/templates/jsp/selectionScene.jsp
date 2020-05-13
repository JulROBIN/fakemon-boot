<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script>
$(document).ready(function(){
	var noMove = loadStarters();
	console.log(noMove)
	/**
	* Gestion des mouvements dans la scene
	* @param e event lorsque la touche est relachee
	*/
	$(document).keyup(function(e){
		avatar = $("#avatar")
		posY = parseInt(avatar.attr("posY"))
		posX = parseInt(avatar.attr("posX"))
		if(e.keyCode == 38){
			//haut
			actualY = (posY-1)*20
			if(checkWalk(posX,posY-1)){	
				avatar.css("top",actualY+"px")
				avatar.attr("posY",posY-1)
			}
		}else if(e.keyCode==40){
			//bas
			actualY = (posY+1)*20
			if(checkWalk(posX,posY+1)){
				avatar.css("top",actualY+"px")
				avatar.attr("posY",posY+1)
			}
		}else if(e.keyCode == 39){
			//droite
			
			newX = (posX+1) * 20
			if(checkWalk(posX+1,posY)){
				avatar.css("left",newX+"px")
				avatar.attr("posX",posX+1)
			}
		}else if(e.keyCode == 37){
			//gauche
			newX=(posX-1) * 20
			if(checkWalk(posX-1,posY)){
				avatar.css("left",newX+"px")
				avatar.attr("posX",posX-1)
			}
		}
		posY = parseInt(avatar.attr("posY"))
		posX = parseInt(avatar.attr("posX"))
		//checkEncounter(posX,posY)
	})
	
	function loadStarters(){
		var toReturn={};
		$.ajax({
			type:"POST",
			url:'${pageContext.request.contextPath}/mechanics',
			data:{"activity":"starterLoad"},
			}).done(function(reponse){
				rep = JSON.parse(reponse)
				toReturn["data"] = rep.nogo
				console.log(rep.nogo)
				$(rep.starters).each(function(i,e){
					
					elem = $(document.createElement("img"));
					elem.attr("posX",i+2)
					elem.attr("posY",0)
					elem.addClass("start")
					elem.attr("src","${pageContext.request.contextPath}/assets/img/boxMon.png")
					elem.css("z-index",1)
					elem.css("position","absolute")
					elem.css("width","20px")
					elem.css("height","20px")
					$("#scene").append(elem)
				})
				
				$(".start").each(function(i,e){
					
					posY = parseInt($(e).attr("posY"))
					posX = parseInt($(e).attr("posX"))
					realY = posY * 20
					realX = posX * 20
					$(e).css("top",realY+"px")
					$(e).css("left",realX+"px")
				})					
			
		})
	return toReturn
	}
	
	/***
	 * TODO aller chercher les rencontres
	 */
	function getEncounters(){
		
	}
	
	/**
	*	Regarde si la position en x,y donnee contient une rencontre
	*	@param x,y int,int position sur la grille
	
	function checkEncounter(x,y){
		encounterX = ${encounter.x}
		encounterY = ${encounter.y}
		if(encounterX == x && encounterY == y){
			$.ajax({
				type:"POST",
				url:'${pageContext.request.contextPath}/selection',
				data:"newSession="+true,
				success: function(){
					window.location.href='${pageContext.request.contextPath}'+'/selection'
				}
			})
		}
	}
	*/
	/**
	*	Regarde si la position donnee est accessible par l'avatar
	*	@param x,y int,int position sur la grille
	*/
	function checkWalk(x,y){
		jsonNoWalk = noMove.data
		returnBool = true
		if(jsonNoWalk.hasOwnProperty(y)){
			returnBool = $.inArray(x,jsonNoWalk[y])
			if(returnBool === -1){
				returnBool = true
			}else{
				returnBool=false
			}
		}
		if(x == 10 || x < 0 || y == 10 || y < 0){
			returnBool = false
		}

		return returnBool
	}
});
</script>
</head>
<body>
	<div id="game" class="container">
		<div class="row h-100 justify-content-around align-items-center">
			<div class="col-4 p-0">
				<div id="scene" style="width:200px;height:200px;border-radius:2px;background-color:white;background-image:url('${pageContext.request.contextPath}/assets/img/fondScene.png')">
					<img
						id="avatar" 
						src="${pageContext.request.contextPath}/assets/img/avatar.png"
						style="position:relative;z-index:2;"
						posX="0"
						posY="0"
					/>
				</div>
			</div>
		</div>
	</div>
</body>
</html>