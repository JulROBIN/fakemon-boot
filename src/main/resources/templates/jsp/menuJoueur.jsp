<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Menu</title>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
$(document).ready(
		function(){
			$("#listSelect").children().hover(
					function(){
						$(this).css("box-shadow","black 2px 2px")
					},
					function(){
						$(this).css("box-shadow","#eaeaea 2px 2px")
					}
				);
			$("#listSelect").sortable({stop:sortEventhandler})
		}
);
function sortEventhandler(event, ui){
    console.log($("#listSelect li:first-child").attr("index"))
};
</script>
</head>
<body>
	<div class="container">
		<div class="row h-100 align-items-center justify-content-around">
			<div class="col-4" style="background-color:#eaeaea;padding:5px; border-radius:5px;border:black 2px solid">
				<h5 class="font-mine">Ton equipe</h5>
				<p class="no-gutters font-mine">Positionne tes monstres (en drag-n-drop)</p>
				<ul id="listSelect" class="list-group">
					<c:forEach items="${sessionScope.joueur}" var="m" varStatus="loop">
						<li class="font-mine list-group-item" style="box-shadow:#eaeaea 2px 4px; margin-bottom:2px">${m.nom}  ${loop.index}</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>