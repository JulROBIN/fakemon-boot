$(document).ready(function(){

	//setInterval(updatePlayerView,5000)
	
	getScene()
	updatePlayer()
		/**
	* Gestion des mouvements dans la scene
	* @param e event lorsque la touche est relachee
	*/
	$(document).keyup(function(e){
		if(avatarMove){
			if(e.keyCode == 27){
				console.log("Prochain emplacement menu")
			}else{
				avatar = $("#avatar")
				posY = parseInt(avatar.attr("posY"))
				posX = parseInt(avatar.attr("posX"))
				if(e.keyCode == 38){
					//haut
					actualY = (posY-1)*tailleCase
					if(checkWalk(posX,posY-1)){
						/*avatar.css("top",actualY+"px")
						avatar.attr("posY",posY-1)*/
						avatarPosition(posX,posY-1)
					}
				}else if(e.keyCode==40){
					//bas
					//actualY = (posY+1)*tailleCase
					if(checkWalk(posX,posY+1)){
						avatarPosition(posX,posY+1)
					}
				}else if(e.keyCode == 39){
					//droite
					
					newX = (posX+1) * tailleCase
					if(checkWalk(posX+1,posY)){
						/*avatar.css("left",newX+"px")
						avatar.attr("posX",posX+1)*/
						avatarPosition(posX+1,posY)
					}
				}else if(e.keyCode == 37){
					//gauche
					newX=(posX-1) * tailleCase
					if(checkWalk(posX-1,posY)){
						/*avatar.css("left",newX+"px")
						avatar.attr("posX",posX-1)*/
						avatarPosition(posX-1,posY)
					}
				}
				posY = parseInt(avatar.attr("posY"))
				posX = parseInt(avatar.attr("posX"))
				checkEncounter(posX,posY)
				checkTrigger(posX,posY)
			}
		}
	})
});

var tailleCase = 40
var scene = {}
var avatarMove = true;
var starterSelected = false; // je sais pas si c'est vraiment le top de la mettre ici cette variable. Elle est très contextuelle au final.
	
	function avatarPosition(x,y){
		console.log(avatarMove)
		if(avatarMove){
			avatar = $("#avatar")
			avatar.css("left",(x*tailleCase)+"px")
			avatar.css("top",(y*tailleCase)+"px")
			avatar.attr("posX",x)
			avatar.attr("posY",y)
			console.log($("#avatar"))
		}
	}
	
	/**
	*	Regarde si la position en x,y donnee contient une rencontre
	*	@param x,y int,int position sur la grille
	*/
	function checkEncounter(x,y){
		encounterX = scene.triggers.encounter[0]
		encounterY = scene.triggers.encounter[1]
		posY = parseInt(avatar.attr("posY"))
		posX = parseInt(avatar.attr("posX"))
		
		if(encounterX == x && encounterY == y){
			updatePlayerInfos()
			selectMonsterMenu();
		}
	}
	

	
	/**
	 * Lance l'affichage de la liste de selection du monstre avant un combat
	 * @returns
	 */
	function selectMonsterMenu(){
		console.log("selecting")
		$.ajax({
			
			type:"GET",
			url:'mechanics/select',
			success: function(resp){
				$("#scene").html(resp)
			}
			
		})
	}

	
	/**
	*	Regarde si la position donnee est accessible par l'avatar
	*	@param x,y int,int position sur la grille
	*/
	function checkWalk(x,y){
		returnBool = true
		if(scene.nowalk.hasOwnProperty(y)){
			returnBool = $.inArray(x,scene.nowalk[y])
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
	
	/**
	 * Verifie que la position donnee contient un trigger, si c'est le cas lance l'action correspondante
	 * @param x
	 * @param y
	 * @returns
	 */
	function checkTrigger(x,y){
		console.log("trigger")
		interactions = scene.triggers.interact
		sceneChange = scene.triggers.scenes
		updatePlayerInfos()
		if(interactions.length > 0){
			$.each(interactions,function(idx){
				inter = interactions[idx]
				if(inter.pos[0] == x && inter.pos[1] == y){
					if(inter.event_type == "script"){
						var script = inter.script
						console.log("eval script")
						$.getScript(script)
					}else if(inter.event_type == "dresseur"){
						console.log("dresseur")
						selectMonsterMenu()
					}
				}
			})

		}

		if(sceneChange.length > 0){
			$.each(sceneChange,function(idx){
				sceneToCheck = sceneChange[idx]
				if(sceneToCheck.pos[0] == x && sceneToCheck.pos[1] == y){
					sceneSetupId(sceneToCheck.id)
					updatePlayerInfos()
				}
			})
		}
		
		
		
	}
	
	/**
	 * Mets a jour les donnees du joueur cote vue
	 * @returns
	 */
	function updatePlayer(){
		$.ajax({
			type:"GET",
			url:'player/infosTest',
			success: function(resp){
				data = JSON.parse(resp)
				console.log("update position : "+data.idScene+" | "+scene.id)
				if(data.idScene != scene.id){
					avatarPosition(scene.startpos[0],scene.startpos[1])
					updatePlayerInfos()
				}else{
					posX = data.position[0]
					posY = data.position[1]
					avatarPosition(posX,posY)
				}
				updatePlayerView()
			}
		})
	}
	
	/**
	 * Met a jour les infos du joueur dans le back
	 * @returns
	 */
	function updatePlayerInfos(){
		console.log("updating")
		$.ajax({
			type:'POST',
			url:'player/posupdate',
			data:{'y':parseInt($("#avatar").attr("posY")),'x':parseInt($("#avatar").attr("posX")),'scene' : scene.id,'localisation':scene.type}
		})
	}
	
	/**
	 * Mets a jour les infos du joueur cote vue (liste de monstre)
	 * @returns
	 */
	function updatePlayerView(){
		console.log("update view of player")
		$.ajax({
			type:"GET",
			url:'player/infosTest',
			success: function(resp){
				data = JSON.parse(resp)
				$("#playerInfos").empty()
				list = $("<div class='row font-mine align-items-start' style='height:400px;background-color:#eaeaea;padding:5px; margin-right:5px; border-radius:5px;border:black 2px solid' ></div>")
				itemContainer = $("<div class='col-6'></div>")
				itemContainer.append("<h5>Equipe</h5>")
				$.each(data.equipePlayer,function(idx){
					monstre = data.equipePlayer[idx]
					
					item = $("<span class='list-group-item dialog-box'></span>")
					item.css({"box-shadow":"black 2px 0px","margin-bottom":"2px"})
					item.text(monstre.nom+" [lvl : "+monstre.level+"]")
					itemContainer.append(item)
					
				})
				
				list.append(itemContainer)
				$("#playerInfos").append(list)
			}
		})
	}
	
	function sceneSetupId(id){
		$.ajax({
			type:"GET",
			url:'/fakemon-front/mechanics/scene/'+id,
			success:function(resp){
				data = JSON.parse(resp)
				console.log(data)
				scene = data
				sceneSetup()
			}
		});
		
		$.ajax({
			type:"GET",
			url:'player/infosTest',
			success:function(resp){
				data = JSON.parse(resp)
				//console.log(scene.id+" : "+data.idScene)
				//if(scene.id != data.idScene){
					//console.log("onUpdate boiii")

					updatePlayer()
					//updatePlayerInfos()

					
				//}else{
					//console.log("moving boiii")
					//updatePlayerInfos()
				//}
			}
		});
	}
	
	function getScene(){
		if($.isEmptyObject(scene)){
			console.log("scene vide")
			$.ajax({
				type:"GET",
				url:'/fakemon-front/mechanics/scene/setup',
				success:function(resp){
					console.log(resp)
					data = JSON.parse(resp)
					console.log(data)
					scene = data
					sceneSetup()
				}
			});
			
			$.ajax({
				type:"GET",
				url:'player/infosTest',
				success:function(resp){
					data = JSON.parse(resp)
					console.log(scene.id+" : "+data.idScene)
					//if(scene.id != data.idScene){
						/*posX = scene.startpos[0]
						posY = scene.startpos[1]
						avatarPosition(posX,posY)*/
						updatePlayer()
						//updatePlayerInfos()
						
					//}else{
						//console.log("moving boiii")
						//updatePlayerInfos()
					//}
				}
			});
		}
	}
	
	/**
	 * Rafraichit la scene avec les infos de l'objet scene
	 * @returns
	 */
	function sceneSetup(){
		console.log(scene)
		$("#scene").css("background-image","url("+scene.background+")")
		$("#scene").css("background-color","none")
		setTriggersTiles()
		if(scene.hasOwnProperty("script")){
			console.log("cette scene execute un script")
			$.getScript(scene.script)
		}
		updatePlayerView()
	}
	
	function setTriggersTiles(){
		$("img[type='dresseur']").remove()
		$("img[type='prop']").remove()
		$("div[type='portail']").remove()
		if(scene.triggers.scenes.length > 0){
			scenesArray = scene.triggers.scenes
			
			$.each(scenesArray,function(idx){
				item = scenesArray[idx]
				porte = $("<div type='portail' style=' display:block; position:absolute; z-index:1; height:40px; width:40px;'></div>")
				porte.css("top",(tailleCase*item.pos[1])+"px")
				porte.css("left",(tailleCase*item.pos[0])+"px")
				if(scene.style != undefined){
					porte.css('background-image',"url("+scene.style.portail+")")
					porte.css('background-size','cover')
					if(item.orientation == "east"){
						porte.css("transform",'rotate(-90deg)')
					}else if(item.orientation == "west"){
						porte.css("transform",'rotate(90deg)')
					}else if(item.orientation == 'north'){
						porte.css("transform",'rotate(180deg)')
					}
				}

				$("#scene").append(porte)
			})
		}
		
		if(scene.triggers.interact.length > 0){
			
			interactions = scene.triggers.interact

			$.each(interactions,function(idx){
				console.log(interactions[idx])
				if(interactions[idx].event_type == "dresseur"){
					//console.log("OMG un dresseur!!")
					item = interactions[idx]
					dresseur = $("<img src='assets/img/monsters/1.png' type='dresseur' style='position:absolute; z-index:1; height:40px; width:40px;' />")
					dresseur.css("top",(tailleCase*item.pos[1])+"px")
					dresseur.css("left",(tailleCase*item.pos[0])+"px")
					$("#scene").append(dresseur)
				}
				if(interactions[idx].hasOwnProperty("prop")){
					item = interactions[idx].prop
					//console.log(item)
					
					prop = $("<img type='prop' src='"+item.asset+"' style='position:absolute; z-index:1;'/>")
					prop.css("top",(tailleCase*item.pos[1])+"px")
					prop.css("left",(tailleCase*item.pos[0])+"px")
					$("#scene").append(prop)
				}
			})
		}
	}
	
	btnHeal = $("#healBtn")
	
	function heal(){
		$.ajax({
			type:"POST",
			url:'/mechanics',
			data:{"activity":"heal"}
		})
	}
