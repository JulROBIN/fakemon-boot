<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Combat</title>
<script src="/fakemon-front/assets/js/combatMechanics.js">

</script>
<style>
@keyframes toaster{
	0%	{left:-100vw}
	100%	{left:-15vw}
}
.toast-it{
	animation-name:toaster;
	animation-duration:1s;
	animation-direction:alternate;
}
</style>
</head>
<body>
	<div class="container" style="max-height:400px">
		<div class="row h-100 align-items-center">
			<img id="toastyJordan" style="position:absolute;left:-100vw;bottom:-21vh;z-index:1" src="${pageContext.request.contextPath}/assets/img/BJ.png"/>
			<audio id="toastyAudio">
				<source src='http://soundfxcenter.com/video-games/mortal-kombat-3-trilogy/8d82b5_Mortal_Kombat_3_Toasty_Sound_Effect.mp3' type="audio/mpeg"/>
			</audio>
		
			<div class="col" id="sceneCombat" style="margin:0 auto; border:black 2px solid;background-color:#eaeaea">
				<div class="row">
					<!-- stat adverse -->
					<div id="blocAdv" class="font-mine" style="width:100%;padding:10px">
						<div class="row no-gutters">
							<div class="col text-right"><h4>${sessionScope.adversaire.nom}</h4></div>
						</div>
						<div class="row no-gutters">
							<div class="col text-right"><h5>lvl : ${sessionScope.adversaire.level}</h5></div>
						</div>
						<div class="progress float-right text-right" style="width:40%;border:black 2px solid;">
							<div class="progress-bar progress-bar-striped bg-danger progress-bar-animated" 
								role="progressbar" 
								id="progressAdv"
								aria-now="${sessionScope.adversaire.pv}"
								aria-min="0"
								aria-max="${sessionScope.adversaire.pvMax}"
								>
								
							</div>
						</div>
					</div>
				</div>
				<div class="row justify-content-end">
					<img 
						id="imgAdv" 
						src="assets/img/monsters/1.png" 
						height="25" 
						style="transform: scaleX(-1)"
					/>
				</div>
				<div class="row">
					<img 
						id="imgPlay" 
						src="assets/img/monsters/2.png" 
						height="25" 
					/>
				</div>
				<div class="row border-black" id="encartMsg"  style="min-height:20px;border-radius:5px;margin: 5 10px 0 10px; box-shadow: 2px 2px">
					<div class="font-mine" id="encartMsgPlace" style="margin-left:20px;"></div>
				</div>
				<div class="row">
					<!--  nos stat + actions -->
					
					<div id="menu_actions"  class="font-mine w-100" style="padding:10px">
						<h4 id="nomAttaquant"></h4>
						<h5 id="lvlAttaquant"></h5>
						<div class="row" style="margin-left:20px;">
							<div class="progress" style="width:40%; border:black solid 2px">
								<div class="progress-bar" 
										role="progressbar" 
										id="progressPlayMon"
										aria-min="0"
								>
								</div>
								
							</div>
							<span id="xp"></span>
							<button class="btn" onclick="listSwitch()">switch</button>
							<button class="btn btn-link text-dark" 
											data-toggle="collapse" 
											data-target="#captureCol" 
											onclick="capture()">Capture	
							</button>
						</div>
						
						<div id="menuSelectAtk" style="border-left:1px solid black;border-top: black 1px solid;border-radius:5px;margin-top:2px; box-shadow: 2px 2px">

						</div>
					</div>
				</div>
				<!--  <div class="row">
					${sessionScope.attaquant}<br>
					${sessionScope.adversaire}
				</div>-->
			</div>
		</div>
	</div>

</body>
</html>