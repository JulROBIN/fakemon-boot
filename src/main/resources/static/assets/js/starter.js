/**
 * Obligatoire pour un script de trigger interaction : bloquer les mouvements et en fonction de la resolution autoriser a nouvea les mouvements
 * Pour afficher un message ingame, utiliser la boite de dialogue
 * @returns
 */

avatarMove = false;

starter = {}

if(!starterSelected){
	starterPop()
}else{
	$('#boiteMsg').empty()
	$('#boiteMsg').append("<p>Abuse pas de ma generosite. Tu sais combien c'est difficile de chopper des monstres ?<button class='btn btn-link' onclick='no()'>ok</button></p>")
	$('#boiteMsg').css("display","block")
}

function starterPop(){
	$.ajax({
		type:'POST',
		url:'player/starter/pop',
		success:function(resp){
			console.log(resp)
			starter = JSON.parse(resp)
			if(starter.hasOwnProperty('uniqueId')){
				$('#boiteMsg').empty()
				$('#boiteMsg').append("<p>tu veux tu prendre "+starter.nom+" ?</p><button class='btn btn-link' onclick='yes()'>yes papa</button><button class='btn btn-link' onclick='no()'>no</button>")
				$('#boiteMsg').css("display","block")
			}else{
				avatarMove =true;
			}
		}
	})
}
function yes(){
	starterSelected = true
	$.ajax({
		type:'POST',
		url:'player/starter/'+starter.uniqueId
	})
	
	$('#boiteMsg').empty()
	$('#boiteMsg').css("display",'none')
	avatarMove=true;
	updatePlayerView()
}

function no(){
	$('#boiteMsg').empty()
	$('#boiteMsg').css("display",'none')
	avatarMove=true;
}
