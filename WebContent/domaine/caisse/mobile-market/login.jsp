<!DOCTYPE html>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.model.domaine.vente.persistant.MenuCompositionPersistant"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.model.domaine.stock.persistant.FamillePersistant"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<html lang="fr">

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<meta name="description" content="Gurdeep Osahan">
	<meta name="author" content="Gurdeep Osahan">
	<link rel="icon" type="image/png" href="domaine/caisse/mobile-market/template/resources/img/logo_web_red.png">
	<title>Caisse manager - Commande en ligne</title>
	
	<link rel="stylesheet" type="text/css" href="domaine/caisse/mobile-market/template/resources/vendor/slick/slick.min.css" />
	<link rel="stylesheet" type="text/css" href="domaine/caisse/mobile-market/template/resources/vendor/slick/slick-theme.min.css" />
	<link href="domaine/caisse/mobile-market/template/resources/vendor/icons/feather.css" rel="stylesheet" type="text/css">
	<link href="domaine/caisse/mobile-market/template/resources/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link href="domaine/caisse/mobile-market/template/resources/css/style.css" rel="stylesheet">
	<link href="domaine/caisse/mobile-market/template/resources/vendor/sidebar/demo.css" rel="stylesheet">
	<link href="resources/framework/css/select2/select2.css" rel="stylesheet" type="text/css"/>
	<link href="resources/framework/css/alertify.core.css" rel="stylesheet" type="text/css"/>
	
	<script src="resources/framework/js/jquery.min.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function (){
			$(".select2").select2({
				  allowClear: true
			});			
		});
	</script>
	
</head>
<body class="fixed-bottom-bar">
<std:form name="data_form" classStyle="">
	<header class="section-header">
		<section class="header-main shadow-sm bg-primary-style2">
			<div class="container">
				<div class="row align-items-center">
					<div class="col-1">
						<a href="mob-market" class="brand-wrap mb-0"> 
							<img alt="#" class="img-fluid" src="resources/<%=StrimUtil.getGlobalConfigPropertie("context.soft") %>/img/logo_caisse_red.png?v=1.66">
						</a>

					</div>
					<div class="col-3 d-flex align-items-center m-none">
						<div class="dropdown mr-3">
							<a class="btn-border-1 text-white dropdown-toggle d-flex align-items-center py-3" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								<div>
									<i class="feather-map-pin mr-2 primary-dark rounded-pill p-2 icofont-size"></i>
								</div>
								<div>
									<p class="text-white mb-0 small">Votre ville</p>
									Agadir...
								</div>
							</a>
							<div class="dropdown-menu p-0 drop-loc" aria-labelledby="navbarDropdown">
								<div class="osahan-country">
									<div class="search_location bg-primary p-3 text-right">
										<div class="input-group rounded shadow-sm overflow-hidden">
<!-- 											<div class="input-group-prepend"> -->
<!-- 												<button class="border-0 btn btn-outline-secondary text-dark bg-white btn-block"> -->
<!-- 													<i class="feather-search"></i> -->
<!-- 												</button> -->
<!-- 											</div> -->
<!-- 											<input type="text" class="shadow-none border-0 form-control" placeholder="Enter Your Location"> -->
											<std:select name="ville" data="${listVille }" key="id" labels="libelle" groupKey="opc_region.id" groupLabels="opc_region.libelle" type="long"></std:select>
										</div>
									</div>
<!-- 									<div class="p-3 border-bottom"> -->
<!-- 										<a href="mob-market" class="text-decoration-none"> -->
<!-- 											<p class="font-weight-bold text-primary m-0"> -->
<!-- 												<i class="feather-navigation"></i> New York, USA -->
<!-- 											</p> -->
<!-- 										</a> -->
<!-- 									</div> -->
<!-- 									<div class="filter"> -->
<!-- 										<h6 class="px-3 py-3 bg-light pb-1 m-0 border-bottom">Choose your country</h6> -->
<!-- 										<div class="custom-control  border-bottom px-0 custom-radio"> -->
<!-- 											<input type="radio" id="customRadio1" name="location" class="custom-control-input"> <label class="custom-control-label py-3 w-100 px-3" for="customRadio1">Afghanistan</label> -->
<!-- 										</div> -->
<!-- 										<div class="custom-control  border-bottom px-0 custom-radio"> -->
<!-- 											<input type="radio" id="customRadio2" name="location" class="custom-control-input" checked> <label class="custom-control-label py-3 w-100 px-3" for="customRadio2">India</label> -->
<!-- 										</div> -->
<!-- 										<div class="custom-control  border-bottom px-0 custom-radio"> -->
<!-- 											<input type="radio" id="customRadio3" name="location" class="custom-control-input"> <label class="custom-control-label py-3 w-100 px-3" for="customRadio3">USA</label> -->
<!-- 										</div> -->
<!-- 										<div class="custom-control  border-bottom px-0 custom-radio"> -->
<!-- 											<input type="radio" id="customRadio4" name="location" class="custom-control-input"> <label class="custom-control-label py-3 w-100 px-3" for="customRadio4">Australia</label> -->
<!-- 										</div> -->
<!-- 										<div class="custom-control  border-bottom px-0 custom-radio"> -->
<!-- 											<input type="radio" id="customRadio5" name="location" class="custom-control-input"> <label class="custom-control-label py-3 w-100 px-3" for="customRadio5">Japan</label> -->
<!-- 										</div> -->
<!-- 										<div class="custom-control  px-0 custom-radio"> -->
<!-- 											<input type="radio" id="customRadio6" name="location" class="custom-control-input"> <label class="custom-control-label py-3 w-100 px-3" for="customRadio6">China</label> -->
<!-- 										</div> -->
<!-- 									</div> -->
								</div>
							</div>
						</div>
					</div>

					<div class="col-8">
						<div class="d-flex align-items-center justify-content-end pr-5">

							<a href="search.html" class="widget-header mr-4 text-white">
								<div class="icon d-flex align-items-center">
									<i class="feather-search h6 mr-2 mb-0"></i> <span>Recherche</span>
								</div>
							</a> 
							<a href="offers.html" class="golden-btn widget-header mr-4 text-dark btn m-none">
								<div class="icon d-flex align-items-center">
									<i class="feather-disc h6 mr-2 mb-0"></i> <span>Offres</span>
								</div>
							</a> 
<%-- 							<%if(ContextAppli.getUserBean() == null){ %> --%>
								<a href="front?w_f_act=<%=EncryptionUtil.encrypt("caisse.clientMobile.init_login")%>&skipF=1" class="widget-header mr-4 text-white m-none">
									<div class="icon d-flex align-items-center">
										<i class="feather-user h6 mr-2 mb-0"></i> <span>Se connecter</span>
									</div>
								</a>
<%-- 							<%} else{ %> --%>
								<std:link action="caisse.clientMobile.logout" classStyle="widget-header mr-4 text-white m-none">
									<div class="icon d-flex align-items-center">
										<i class="feather-user h6 mr-2 mb-0"></i> <span>Se déconnecter</span>
									</div>
								</std:link>
<%-- 							<%} %> --%>

							<div class="dropdown mr-4 m-none">
								<a href="#" class="dropdown-toggle text-white py-3 d-block" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> 
									<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/user/1.jpg" class="img-fluid rounded-circle header-user mr-2 header-user"> Hi Osahan
								</a>
								<div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton">
									<a class="dropdown-item" href="profile.html">Mon compte</a> 
									<a class="dropdown-item" href="faq.html">Support livraison</a> 
									<a class="dropdown-item" href="contact-us.html">Nous contacter</a> 
									<a class="dropdown-item" href="terms.html">Conditions</a> 
									<a class="dropdown-item" href="privacy.html">Vie privée</a> 
									<a class="dropdown-item" href="login.html">Se déconnecter</a>
								</div>
							</div>

							<std:link classStyle="widget-header mr-4 text-white" action="caisse.clientMobile.init_panier" targetDiv="detail-content" params="skipF=1">
								<div class="icon d-flex align-items-center">
									<i class="feather-shopping-cart h6 mr-2 mb-0"></i> <span>Panier</span>
								</div>
							</std:link> 
							<a class="toggle" href="#"> <span></span>
							</a>
						</div>
					</div>
				</div>
			</div>
		</section>
	</header>
	<div class="osahan-home-page" id="detail-content">
		<jsp:include page="index.jsp" />
	</div>

	<div class="osahan-menu-fotter fixed-bottom bg-white px-3 py-2 text-center d-none">
		<jsp:include page="menu-left-mobile.jsp" />
	</div>

	<footer class="section-footer border-top bg-dark">
		<div class="container">
			<section class="footer-top padding-y py-5">
				<div class="row">
					<aside class="col-md-4 footer-about">
						<article class="d-flex pb-3">
							<div>
								<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/logo_web_red.png" class="logo-footer mr-3">
							</div>
							<div>
								<h6 class="title text-white">About Us</h6>
								<p class="text-muted">Some short text about company like You might remember the Dell computer commercials in which a youth reports.</p>
								<div class="d-flex align-items-center">
									<a class="btn btn-icon btn-outline-light mr-1 btn-sm" title="Facebook" target="_blank" href="#"><i class="feather-facebook"></i></a> <a class="btn btn-icon btn-outline-light mr-1 btn-sm" title="Instagram" target="_blank" href="#"><i class="feather-instagram"></i></a> <a class="btn btn-icon btn-outline-light mr-1 btn-sm" title="Youtube" target="_blank" href="#"><i class="feather-youtube"></i></a> <a class="btn btn-icon btn-outline-light mr-1 btn-sm" title="Twitter" target="_blank" href="#"><i class="feather-twitter"></i></a>
								</div>
							</div>
						</article>
					</aside>
					<aside class="col-sm-3 col-md-2 text-white">
						<h6 class="title">Error Pages</h6>
						<ul class="list-unstyled hov_footer">
							<li><a href="not-found.html" class="text-muted">Not found</a></li>
							<li><a href="maintence.html" class="text-muted">Maintence</a></li>
							<li><a href="coming-soon.html" class="text-muted">Coming Soon</a></li>
						</ul>
					</aside>
					<aside class="col-sm-3 col-md-2 text-white">
						<h6 class="title">Services</h6>
						<ul class="list-unstyled hov_footer">
							<li><a href="faq.html" class="text-muted">Delivery Support</a></li>
							<li><a href="contact-us.html" class="text-muted">Contact Us</a></li>
							<li><a href="terms.html" class="text-muted">Terms of use</a></li>
							<li><a href="privacy.html" class="text-muted">Privacy policy</a></li>
						</ul>
					</aside>
					<aside class="col-sm-3  col-md-2 text-white">
						<h6 class="title">For users</h6>
						<ul class="list-unstyled hov_footer">
							<li><a href="login.html" class="text-muted"> User Login </a></li>
							<li><a href="signup.html" class="text-muted"> User register </a></li>
							<li><a href="forgot_password.html" class="text-muted"> Forgot Password </a></li>
							<li><a href="profile.html" class="text-muted"> Account Setting </a></li>
						</ul>
					</aside>
					<aside class="col-sm-3  col-md-2 text-white">
						<h6 class="title">More Pages</h6>
						<ul class="list-unstyled hov_footer">
							<li><a href="trending.html" class="text-muted"> Trending </a></li>
							<li><a href="most_popular.html" class="text-muted"> Most popular </a></li>
							<li><a href="restaurant.html" class="text-muted"> Restaurant Details </a></li>
							<li><a href="favorites.html" class="text-muted"> Favorites </a></li>
						</ul>
					</aside>
				</div>

			</section>

<!-- 			<section class="footer-center border-top padding-y py-5"> -->
<!-- 				<h6 class="title text-white">Countries</h6> -->
<!-- 				<div class="row"> -->
<!-- 					<aside class="col-sm-2 col-md-2 text-white"> -->
<!-- 						<ul class="list-unstyled hov_footer"> -->
<!-- 							<li><a href="#" class="text-muted">India</a></li> -->
<!-- 							<li><a href="#" class="text-muted">Indonesia</a></li> -->
<!-- 							<li><a href="#" class="text-muted">Ireland</a></li> -->
<!-- 							<li><a href="#" class="text-muted">Italy</a></li> -->
<!-- 							<li><a href="#" class="text-muted">Lebanon</a></li> -->
<!-- 						</ul> -->
<!-- 					</aside> -->
<!-- 					<aside class="col-sm-2 col-md-2 text-white"> -->
<!-- 						<ul class="list-unstyled hov_footer"> -->
<!-- 							<li><a href="#" class="text-muted">Malaysia</a></li> -->
<!-- 							<li><a href="#" class="text-muted">New Zealand</a></li> -->
<!-- 							<li><a href="#" class="text-muted">Philippines</a></li> -->
<!-- 							<li><a href="#" class="text-muted">Poland</a></li> -->
<!-- 							<li><a href="#" class="text-muted">Portugal</a></li> -->
<!-- 						</ul> -->
<!-- 					</aside> -->
<!-- 					<aside class="col-sm-2 col-md-2 text-white"> -->
<!-- 						<ul class="list-unstyled hov_footer"> -->
<!-- 							<li><a href="#" class="text-muted">Australia</a></li> -->
<!-- 							<li><a href="#" class="text-muted">Brasil</a></li> -->
<!-- 							<li><a href="#" class="text-muted">Canada</a></li> -->
<!-- 							<li><a href="#" class="text-muted">Chile</a></li> -->
<!-- 							<li><a href="#" class="text-muted">Czech Republic</a></li> -->
<!-- 						</ul> -->
<!-- 					</aside> -->
<!-- 					<aside class="col-sm-2 col-md-2 text-white"> -->
<!-- 						<ul class="list-unstyled hov_footer"> -->
<!-- 							<li><a href="#" class="text-muted">Turkey</a></li> -->
<!-- 							<li><a href="#" class="text-muted">UAE</a></li> -->
<!-- 							<li><a href="#" class="text-muted">United Kingdom</a></li> -->
<!-- 							<li><a href="#" class="text-muted">United States</a></li> -->
<!-- 							<li><a href="#" class="text-muted">Sri Lanka</a></li> -->
<!-- 						</ul> -->
<!-- 					</aside> -->
<!-- 					<aside class="col-sm-2 col-md-2 text-white"> -->
<!-- 						<ul class="list-unstyled hov_footer"> -->
<!-- 							<li><a href="#" class="text-muted">Qatar</a></li> -->
<!-- 							<li><a href="#" class="text-muted">Singapore</a></li> -->
<!-- 							<li><a href="#" class="text-muted">Slovakia</a></li> -->
<!-- 							<li><a href="#" class="text-muted">South Africa</a></li> -->
<!-- 							<li><a href="#" class="text-muted">Green Land</a></li> -->
<!-- 						</ul> -->
<!-- 					</aside> -->
<!-- 					<aside class="col-sm-2 col-md-2 text-white"> -->
<!-- 						<ul class="list-unstyled hov_footer"> -->
<!-- 							<li><a href="#" class="text-muted">Pakistan</a></li> -->
<!-- 							<li><a href="#" class="text-muted">Bangladesh</a></li> -->
<!-- 							<li><a href="#" class="text-muted">Bhutaan</a></li> -->
<!-- 							<li><a href="#" class="text-muted">Nepal</a></li> -->
<!-- 						</ul> -->
<!-- 					</aside> -->
<!-- 				</div> -->

<!-- 			</section> -->
		</div>

		<section class="footer-copyright border-top py-3 bg-light">
			<div class="container d-flex align-items-center">
				<p class="mb-0">© 2023 Company All rights reserved</p>
				<p class="text-muted mb-0 ml-auto d-flex align-items-center">
					<a href="#" class="d-block"><img alt="#" src="domaine/caisse/mobile-market/template/resources/img/appstore.png" height="40"></a> <a href="#" class="d-block ml-3"><img alt="#" src="domaine/caisse/mobile-market/template/resources/img/playmarket.png" height="40"></a>
				</p>
			</div>
		</section>
	</footer>
	<nav id="main-nav">
		<jsp:include page="menu-left.jsp" />
	</nav>
	<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Filter</h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body p-0">
					<div class="osahan-filter">
						<div class="filter">

							<div class="p-3 bg-light border-bottom">
								<h6 class="m-0">TRIER PAR</h6>
							</div>
							<div class="custom-control border-bottom px-0  custom-radio">
								<input type="radio" id="customRadio1f" name="location" class="custom-control-input" checked> <label class="custom-control-label py-3 w-100 px-3" for="customRadio1f">Top Rated</label>
							</div>
							<div class="custom-control border-bottom px-0  custom-radio">
								<input type="radio" id="customRadio2f" name="location" class="custom-control-input"> <label class="custom-control-label py-3 w-100 px-3" for="customRadio2f">Nearest Me</label>
							</div>
							<div class="custom-control border-bottom px-0  custom-radio">
								<input type="radio" id="customRadio3f" name="location" class="custom-control-input"> <label class="custom-control-label py-3 w-100 px-3" for="customRadio3f">Cost High to Low</label>
							</div>
							<div class="custom-control border-bottom px-0  custom-radio">
								<input type="radio" id="customRadio4f" name="location" class="custom-control-input"> <label class="custom-control-label py-3 w-100 px-3" for="customRadio4f">Cost Low to High</label>
							</div>
							<div class="custom-control border-bottom px-0  custom-radio">
								<input type="radio" id="customRadio5f" name="location" class="custom-control-input"> <label class="custom-control-label py-3 w-100 px-3" for="customRadio5f">Most Popular</label>
							</div>

							<div class="p-3 bg-light border-bottom">
								<h6 class="m-0">FILTRER</h6>
							</div>
							<div class="custom-control border-bottom px-0  custom-checkbox">
								<input type="checkbox" class="custom-control-input" id="defaultCheck1" checked> <label class="custom-control-label py-3 w-100 px-3" for="defaultCheck1">Open Now</label>
							</div>
							<div class="custom-control border-bottom px-0  custom-checkbox">
								<input type="checkbox" class="custom-control-input" id="defaultCheck2"> <label class="custom-control-label py-3 w-100 px-3" for="defaultCheck2">Credit Cards</label>
							</div>
							<div class="custom-control border-bottom px-0  custom-checkbox">
								<input type="checkbox" class="custom-control-input" id="defaultCheck3"> <label class="custom-control-label py-3 w-100 px-3" for="defaultCheck3">Alcohol Served</label>
							</div>

							<div class="p-3 bg-light border-bottom">
								<h6 class="m-0">ADDITIONAL FILTERS</h6>
							</div>
							<div class="px-3 pt-3">
								<input type="range" class="custom-range" min="0" max="100" name="minmax">
								<div class="form-row">
									<div class="form-group col-6">
										<label>Min</label> <input class="form-control" placeholder="0dhs" type="number">
									</div>
									<div class="form-group text-right col-6">
										<label>Max</label> <input class="form-control" placeholder="10dhs" type="number">
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer p-0 border-0">
					<div class="col-6 m-0 p-0">
						<button type="button" class="btn border-top btn-lg btn-block" data-dismiss="modal">Close</button>
					</div>
					<div class="col-6 m-0 p-0">
						<button type="button" class="btn btn-primary btn-lg btn-block">Appliquer</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
</std:form>
	<script type="a7fa0bc4ee59ea5ed9962834-text/javascript" src="domaine/caisse/mobile-market/template/resources/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
	<script type="a7fa0bc4ee59ea5ed9962834-text/javascript" src="domaine/caisse/mobile-market/template/resources/vendor/slick/slick.min.js"></script>
	<script type="a7fa0bc4ee59ea5ed9962834-text/javascript" src="domaine/caisse/mobile-market/template/resources/vendor/sidebar/hc-offcanvas-nav.js"></script>
	<script type="a7fa0bc4ee59ea5ed9962834-text/javascript" src="domaine/caisse/mobile-market/template/resources/js/osahan.js"></script>
	<script src="domaine/caisse/mobile-market/template/resources/js/rocket-loader.min.js" data-cf-settings="a7fa0bc4ee59ea5ed9962834-|49" defer></script>
	<script defer src="https://static.cloudflareinsights.com/beacon.min.js/v52afc6f149f6479b8c77fa569edb01181681764108816" integrity="sha512-jGCTpDpBAYDGNYR5ztKt4BQPGef1P0giN6ZGVUi835kFF88FOmmn8jBQWNgrNd8g/Yu421NdgWhwQoaOPFflDw==" data-cf-beacon='{"rayId":"7c9641c1084c036b","version":"2023.4.0","r":1,"b":1,"token":"dd471ab1978346bbb991feaa79e6ce5c","si":100}' crossorigin="anonymous"></script>
	
	<script type="text/javascript" src="resources/framework/js/labels_fr.js"></script>
	<script type="text/javascript" src="resources/framework/js/jquery.cookie.js"></script>
	<script type="text/javascript" src="resources/framework/js/autoNumeric.js"></script>
	<script type="text/javascript" src="resources/framework/js/alertify.min.js"></script>
	<script type="text/javascript" src="resources/framework/js/select2/select2.full.min.js"></script>
	<script type="text/javascript" src="resources/framework/js/jquery.maskedinput-1.4.1.js"></script>
	<script type="text/javascript" src="resources/framework/js/jquery.validate.min.js?v=1.0"></script>
	<script type="text/javascript" src="resources/framework/js/util.js?v=1.2"></script>
	
</body>

</html>