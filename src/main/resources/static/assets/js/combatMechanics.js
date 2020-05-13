$(document).ready(function(){
	var adversaire;
	var attaquant;
	setupMonsters();
	
});
	function heal(){
		console.log("healing")
		$.ajax({
			type:"GET",
			url:'player/heal'
		})
	}
	
	function setupMonsters(){
		
		if(scene.type=="arena"){
			$("#btnCapture").attr('disabled',true)
		}
		
		$.ajax({
			type:'GET',
			url: 'combat/setup',
			data:{'activity':'monstersInfos'},
			success: function(resp){
				data = JSON.parse(resp)
				console.log("SETUP")
				console.log(data)
				adversaire = data.adversaire
				attaquant = data.attaquant
				
				pvBarPlayMon = $("#progressPlayMon")
				pvPlayMonPrg = data.attaquant.pv/data.attaquant.pvMax*100
				
				pvBarPlayMon.css("width",pvPlayMonPrg+"%")
				
				pvBarAdvMon = $("#progressAdv")
				pvBarAdvMon.css("position","relative")
				pvAdvMonPrg = data.adversaire.pv/data.adversaire.pvMax*100
				pvBarAdvMon.css("width",pvAdvMonPrg+"%")
				$("#xp").text(data.attaquant.exp+"/"+data.attaquant.expNextLevel)
				
				$("#nomAttaquant").text(attaquant.nom)
				$("#lvlAttaquant").text("lvl : "+attaquant.level)
				$("#nomAdv").text(adversaire.nom)
				$("#lvlAdv").text("lvl : "+adversaire.level)
				
				atks = attaquant.listAttaque
				console.log(atks)
				$("#menuSelectAtk").empty()
				$.each(atks,function(i,atk){
					console.log(atk)
					row = $('<div class="row"></div>')
					btnContainer = $('<div class="col-4"></div>')
					btnAtk = $('<button class="btn btn-link text-dark"></button>')
					btnAtk.attr('data-toggle','collapse')
					btnAtk.attr('data-target',atk.nom+'Col')
					btnAtk.click({id:atk.id},sendCombat)
					btnAtk.text(atk.nom)
					
					indic = $('<div class="col text-left"></div>')
					indic.text("puissance : "+atk.puissance+" ["+atk.type+"]")
					
					btnContainer.append(btnAtk)
					row.append(btnContainer)
					row.append(indic)
					$('#menuSelectAtk').append(row)
				})
				
			}
		})
	}
	
	function hasPlayed(playerTurn, finCombat){
		if(!playerTurn){
			$("#menuSelectAtk").find("button").each(function(){
				$(this).prop("disabled",true)
			})
			if(!finCombat){
				$("#imgAdv").addClass("shaking-adv")
				setTimeout(sendCombatBot,3000);
			}
		}else{
			$("#menuSelectAtk").find("button").each(function(){
				$(this).prop("disabled",false)
			})
		}
	}
	
	function sendCombatBot(){
			$.ajax({
			
			type:"POST",
			url:'combat/attaquebot',
			data:{'attaquant' : JSON.stringify(attaquant),'adversaire' : JSON.stringify(adversaire)},
			success: function(response){
				rep = JSON.parse(response)
				console.log("bot")
				
				hasMsg(rep.msg)
				setupMonsters()
				isFightEnded(rep.endFight)
				hasPlayed(rep.playerTurn, rep.endFight)
			}
			
		})
	}
	
	function hasMsg(msg){
		console.log("in hasMsg")
		console.log(msg)
		if( msg != "null"){
			console.log("msg not null")
			$("#encartMsgPlace").text(msg)
			$("#encartMsg").css("display","block")
			console.log($("#encartMsg").css("display"))
			
		}else{
			console.log("msg null")
			$("#encartMsg").css("display","none")
		}
		
	}
	
	function isFightEnded(endFight){
		if(endFight){
			$("#menuSelectAtk").find("button").each(function(){
				$(this).prop("disabled",true)
			})
			heal()
			toasty()
			setTimeout(moveToIndex,4000)
		}
	}
	
	function switchMonster(e){
		
		$.ajax({
			
			type:"POST",
			url:'combat/switch',
			data:{'entity':'player','id':e.data.id},
			success: function(response){
				setupMonsters()

			}
			
		})
	}
	
	function listSwitch(){
		$.ajax({
			type:'GET',
			url:'player/squad',
			success: function(resp){
				data = JSON.parse(resp)
				listBody = $('<div style="background-color:white;position:absolute; z-index:0; top:50%,left:50%;border-left:1px solid black;border-top: black 1px solid;border-radius:5px;margin-top:2px; box-shadow: 2px 2px"></div>')
				$.each(data,function(k,v){
					listItem = $('<div class="row"></div>')
					listItemContent = $('<button class="btn btn-link"></button>')
					listItemContent.click({id : v.uniqueId},switchMonster)
					listItemContent.text(v.nom+":"+v.id)
					listItem.append(listItemContent)
					listBody.append(listItem)
				})
				$("#menuSelectAtk").empty()
				$("#menuSelectAtk").append(listBody)
			}
		})
	}
	
	function capture(){
		$.ajax({
			
			type:"GET",
			url:'combat/capture',
			data:{'action':'capture'},
			success: function(response){
				console.log("capture")
				rep = JSON.parse(response)
				hasMsg(rep.msg)
				isFightEnded(rep.endFight)
				hasPlayed(rep.playerTurn,rep.endFight)
			}
			
		})
	}
	
	function moveToIndex(){
		window.location.href="scene"
	}
	
	function sendCombat(e){
		atkId = e.data.id
		console.log(attaquant)
		$.ajax({
			
			type:"POST",
			url:'combat/attaque',
			data:{'atkId' : atkId,'playerPlays':true,'handTo':'player','action':'attaque', 'attaquant' : JSON.stringify(attaquant),'adversaire' : JSON.stringify(adversaire)},
			success: function(response){
				console.log("player")
				rep = JSON.parse(response)
				console.log(rep)
				console.log("Player")
				
				hasMsg(rep.msg)
				setupMonsters()
				isFightEnded(rep.endFight)
				hasPlayed(rep.playerTurn,rep.endFight)
				
			}
			
		})
		
	}
	
	function toasty(){
		$("#toastyAudio").get(0).play()
		//$("#toastyJordan").addClass("toast-it")
	}
