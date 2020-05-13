<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<script type="text/javascript" src="assets/js/index.js"></script>
</head>
<body>
    <div 
    	id="main-container"
        class="container" 
        style="min-height:100%;padding-top:50px"
    >
        <div class="row align-items-center justify-content-around">
                <h1 
                    class="pok-font-full font-orange font-outline text-center"
                    style="margin-bottom:50px;"
                >
                    fakemon 
                </h1>
        </div>
        <div class="row align-items-center justify-content-around">
            <div class="col-3">
                <div 
                    id="carouselFakemons" 
                    class="carousel slide" 
                    data-ride="carousel"
                >
                    <div class="carousel-inner" style="border-radius:5px; border:2px black solid;">
                        <div class="carousel-item">
                            <h3 
                                class="pok-font-full font-outline" 
                                style="position:absolute; bottom:0;left:10px;"
                            >
                                faible</h3>
                            <img 
                                src="https://media-exp1.licdn.com/dms/image/C4E03AQFg79yI87iO_w/profile-displayphoto-shrink_800_800/0?e=1591228800&v=beta&t=1zKlJeElDkBgypXFXiu--R6hMina-5Gu2JlUozBnJzc" 
                                class="d-block w-100"
                                height="200px"
                            >
                        </div>
                        <div class="carousel-item">
                            <h3 
                                class="pok-font-full font-outline" 
                                style="position:absolute; bottom:0;left:10px;"
                            >
                                bebesalt
                            </h3>
                            <img 
                                src="./assets/img/bebesalt.jpg" 
                                class="d-block w-100"
                                height="200px"
                            >
                        </div>
                        <div class="carousel-item">
                            <h3 
                                class="pok-font-full font-outline" 
                                style="position:absolute; bottom:0;left:10px;"
                            >
                                    crameleon
                            </h3>
                            <img 
                                src="./assets/img/crameleon.jpg" 
                                class="d-block w-100"
                                height="200px" 
                            >
                        </div>
                        <div class="carousel-item">
                            <h3 
                                class="pok-font-full font-outline" 
                                style="position:absolute; bottom:0;left:10px;"
                            >
                                    foufoudre
                            </h3>
                            <img 
                                src="./assets/img/foufoudre.jpg" 
                                class="d-block w-100"
                                height="200px"
                            >
                        </div>
                        <div class="carousel-item active">
                            <h3 
                                class="pok-font-full font-outline" 
                                style="position:absolute; bottom:0;left:10px;"
                            >
                                    pipeau
                            </h3>
                            <img 
                                src="./assets/img/pipeau.jpg" 
                                class="d-block w-100"
                                height="200px" 
                            >
                        </div>
                        <div class="carousel-item">
                            <h3 
                                class="pok-font-full font-outline" 
                                style="position:absolute; bottom:0;left:10px;"
                            >
                                    renargile
                            </h3>
                            <img 
                                src="./assets/img/renargile.jpg" 
                                class="d-block w-100"
                                height="200px" 
                            >
                        </div>
                        <div class="carousel-item">
                            <h3 
                                class="pok-font-full font-outline" 
                                style="position:absolute; bottom:0;left:10px;"
                            >
                                    thymtamare
                            </h3>
                            <img 
                                src="./assets/img/thymtamarre.jpg" 
                                class="d-block w-100"
                                height="200px"
                            >
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row align-items-center justify-content-around">

            <div class="col-3">
                <div class="menu font-mine">
                    <ul id="menuList">
                    	<li><button class="btn btn-link text-dark" id="btnDemo">demo combat</button></li>
                        <li><button class="btn btn-link text-dark"  id="btnContinuer">continuer</button></li>
                        <li><button class="btn btn-link text-dark" id="btnNouvelle" disabled>nouvelle partie</button></li>
                        <li><button class="btn btn-link text-dark"  id="btnOptions">options</button></li>
                       <ul>
                        	<li><button class="btn btn-link text-dark" id="btnMenuJoueur">menu</button></li>
                        	<li><button class="btn btn-link text-dark" id="btnScene"> <a href="scene">scene</a></button></li>
                        </ul>
                    </ul>
                </div>
                <div id="rickrollu" class="collapse" style="margin-top:10px;position:relative">
                    <video id="rickVid" controls>
                        <source src='./assets/medias/videos/rickrolld.mp4' />
                    </video>
                </div>
               
            </div>

        </div>
        <div class="row align-items-center justify-content-around">

            <div class="col-4" style="padding:40px 0 40px 0;">
                <div id="carouselScoring" class="carousel slide" data-ride="carousel">
                    <div class="carousel-inner">
                      <div class="carousel-item active" data-interval="4500">
                          <div class="card">
                              <div class="card-body">
                                  <h5 class="card-title">Jordan</h5>
                                  <p class="card-text scoring">kill max : 1</p>
                                  <p class="card-text scoring">Victoires totales : 0</p>
                                  <p class="card-text scoring">Degats max : 2</p>
                              </div>
                          </div>
                      </div>
                      <div class="carousel-item" data-interval="5000">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">Joueur 2</h5>
                                <p class="card-text scoring">kill max : 90</p>
                                <p class="card-text scoring">Victoires totales : 1M</p>
                                <p class="card-text scoring">Degats max : 1 trillion</p>
                            </div>
                        </div>
                    </div>
                  </div>
                <!--<h3>Scoring passé</h3>
                <ul>
                    <li>kill max</li>
                    <li>victoires totales</li>
                    <li>dégats max</li>
                </ul>-->
            </div>
            <div class="col" cols="4"></div>
        </div>
    </div>
</body>
</html>