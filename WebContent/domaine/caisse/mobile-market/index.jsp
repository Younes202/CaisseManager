<%@page import="appli.model.domaine.vente.persistant.MenuCompositionPersistant"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.model.domaine.stock.persistant.FamillePersistant"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

		<div class="bg-primary p-3 d-none">
			<div class="text-white">
				<div class="title d-flex align-items-center">
					<a class="toggle" href="#"> <span></span>
					</a>
					<h4 class="font-weight-bold m-0 pl-5">Parcourir</h4>
					<a class="text-white font-weight-bold ml-auto" data-toggle="modal" data-target="#exampleModal" href="#">Filtrer</a>
				</div>
			</div>
			<div class="input-group mt-3 rounded shadow-sm overflow-hidden">
				<div class="input-group-prepend">
					<button class="border-0 btn btn-outline-secondary text-dark bg-white btn-block">
						<i class="feather-search"></i>
					</button>
				</div>
				<input type="text" class="shadow-none border-0 form-control" placeholder="Chercher un produit ou un menu">
			</div>
		</div>

		<div class="bg-theme-black py-3">
			<div class="container">
				<div class="offer-slider">
					<div class="cat-item px-1 py-3">
						<a class="d-block text-center shadow-sm" href="trending.html"> 
							<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/pro1.jpg" class="img-fluid rounded">
						</a>
					</div>
					<div class="cat-item px-1 py-3">
						<a class="d-block text-center shadow-sm" href="trending.html"> 
							<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/pro2.jpg" class="img-fluid rounded">
						</a>
					</div>
					<div class="cat-item px-1 py-3">
						<a class="d-block text-center shadow-sm" href="trending.html"> 
							<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/pro3.jpg" class="img-fluid rounded">
						</a>
					</div>
					<div class="cat-item px-1 py-3">
						<a class="d-block text-center shadow-sm" href="trending.html"> 
							<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/pro4.jpg" class="img-fluid rounded">
						</a>
					</div>
					<div class="cat-item px-1 py-3">
						<a class="d-block text-center shadow-sm" href="trending.html"> 
							<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/pro2.jpg" class="img-fluid rounded">
						</a>
					</div>
				</div>
			</div>
		</div>
		
		<!-- Catégories -->
		<div class="container">
			<div class="pt-4 pb-2 title d-flex align-items-center">
				<h5 class="m-0">Nos menus</h5>
				<a data-toggle="modal" data-target="#menu_det_div" class="font-weight-bold ml-auto" href="javascript:">Tout voir 
					<i class="feather-chevrons-right"></i>
				</a>
			</div>
			<!-- Modal -->
			<div class="modal fade" id="menu_det_div" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
				<div class="modal-dialog modal-dialog-centered">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title">Tous les menus</h5>
							<button type="button" class="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body p-0">
							<div class="row">
							<% 
							List<MenuCompositionPersistant> listMenu = (List<MenuCompositionPersistant>)request.getAttribute("listMenu");
							if(listMenu != null && listMenu.size()>0) {
								for (MenuCompositionPersistant menuP : listMenu) {
									String libelle = menuP.getLibelle().replaceAll("\\#", "");
								%>
								<div class="col-3 col-lg-4 mb-3">
									<std:link style="min-height: 116px;" classStyle="bg-white rounded d-block p-2 text-center shadow-sm" params="skipF=1" workId="<%=menuP.getId().toString() %>" action="caisse.clientMobile.loadMenus" targetDiv="detail-content"> 
										<img alt="" class="img-fluid mb-2" onerror="this.src='domaine/caisse/mobile-market/template/resources/img/icons/Pizza.png';" src="resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(menuP.getId().toString()) %>&path=menu&rdm=<%=menuP.getDate_maj()!=null?menuP.getDate_maj().getTime():""%>">
										<p class="m-0 small"><%=libelle %></p>
									</std:link>
								</div>
								<%} 
							}%>
							</div>
						</div>
					</div>
				</div>
			</div>			
			<!-- End -->
			
			<div class="cat-slider">
				<% 
				if(listMenu != null && listMenu.size()>0) {
					for (MenuCompositionPersistant menuP : listMenu) {
						String libelle = menuP.getLibelle().replaceAll("\\#", "");
					%>
					<div class="cat-item px-1 py-3">
						<std:link style="min-height: 116px;" classStyle="bg-white rounded d-block p-2 text-center shadow-sm" params="skipF=1" workId="<%=menuP.getId().toString() %>" action="caisse.clientMobile.loadMenus" targetDiv="detail-content"> 
							<img alt="" class="img-fluid mb-2" onerror="this.src='domaine/caisse/mobile-market/template/resources/img/icons/Pizza.png';" src="resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(menuP.getId().toString()) %>&path=menu&rdm=<%=menuP.getDate_maj()!=null?menuP.getDate_maj().getTime():""%>">
							<p class="m-0 small"><%=libelle %></p>
						</std:link>
					</div>
					<%} 
				}%>
			</div>
		
			<div class="pt-4 pb-2 title d-flex align-items-center">
				<h5 class="m-0">Nos catégories</h5>
				<a data-toggle="modal" data-target="#famille_det_div" class="font-weight-bold ml-auto" href="javascript:">Tout voir <i class="feather-chevrons-right"></i></a>
			</div>
			
			<!-- Modal -->
			<div class="modal fade" id="famille_det_div" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
				<div class="modal-dialog modal-dialog-centered">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title">Toutes les catégories</h5>
							<button type="button" class="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body p-0">
							<div class="row">
							<% 
							List<FamillePersistant> listFamille = (List<FamillePersistant>)request.getAttribute("listFamille");
							if(listFamille != null && listFamille.size()>0) {
								for (FamillePersistant familleP : listFamille) {
									String libelle = familleP.getLibelle().replaceAll("\\#", "");
								%>
								<div class="col-3 col-lg-4 mb-3">
									<std:link style="min-height: 116px;" classStyle="bg-white rounded d-block p-2 text-center shadow-sm" params="skipF=1" workId="<%=familleP.getId().toString() %>" action="caisse.clientMobile.loadFamilles" targetDiv="detail-content"> 
										<img alt="" class="img-fluid mb-2" onerror="this.src='domaine/caisse/mobile-market/template/resources/img/icons/Pizza.png';" src="resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(familleP.getId().toString()) %>&path=menu&rdm=<%=familleP.getDate_maj()!=null?familleP.getDate_maj().getTime():""%>">
										<p class="m-0 small"><%=libelle %></p>
									</std:link>
								</div>
								<%} 
							}%>
							</div>
						</div>
					</div>
				</div>
			</div>			
			<!-- End -->
			
			<div class="cat-slider">
				<% 
				if(listFamille != null && listFamille.size()>0) {
					for (FamillePersistant familleP : listFamille) {
						String libelle = familleP.getLibelle().replaceAll("\\#", "");
					%>
					<div class="cat-item px-1 py-3">
						<std:link style="min-height: 116px;" classStyle="bg-white rounded d-block p-2 text-center shadow-sm" params="skipF=1" workId="<%=familleP.getId().toString() %>" action="caisse.clientMobile.loadFamilles" targetDiv="detail-content"> 
							<img alt="" class="img-fluid mb-2" onerror="this.src='domaine/caisse/mobile-market/template/resources/img/icons/Pizza.png';" src="resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(familleP.getId().toString()) %>&path=famille&rdm=<%=familleP.getDate_maj()!=null?familleP.getDate_maj().getTime():""%>">
							<p class="m-0 small"><%=libelle %></p>
						</std:link>
					</div>
					<%} 
				}%>
			
<!-- 				<div class="cat-item px-1 py-3"> -->
<!-- 					<a class="bg-white rounded d-block p-2 text-center shadow-sm" href="trending.html">  -->
<!-- 						<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/icons/Pizza.png" class="img-fluid mb-2"> -->
<!-- 						<p class="m-0 small">Pizza</p> -->
<!-- 					</a> -->
<!-- 				</div> -->
<!-- 				<div class="cat-item px-1 py-3"> -->
<!-- 					<a class="bg-white rounded d-block p-2 text-center shadow-sm" href="trending.html">  -->
<!-- 						<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/icons/Burger.png" class="img-fluid mb-2"> -->
<!-- 						<p class="m-0 small">Burger</p> -->
<!-- 					</a> -->
<!-- 				</div> -->
<!-- 				<div class="cat-item px-1 py-3"> -->
<!-- 					<a class="bg-white rounded d-block p-2 text-center shadow-sm" href="trending.html">  -->
<!-- 						<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/icons/Sandwich.png" class="img-fluid mb-2"> -->
<!-- 						<p class="m-0 small">Sandwich</p> -->
<!-- 					</a> -->
<!-- 				</div> -->
<!-- 				<div class="cat-item px-1 py-3"> -->
<!-- 					<a class="bg-white rounded d-block p-2 text-center shadow-sm" href="trending.html">  -->
<!-- 						<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/icons/Coffee.png" class="img-fluid mb-2"> -->
<!-- 						<p class="m-0 small">Coffee</p> -->
<!-- 					</a> -->
<!-- 				</div> -->
<!-- 				<div class="cat-item px-1 py-3"> -->
<!-- 					<a class="bg-white rounded d-block p-2 text-center shadow-sm" href="trending.html">  -->
<!-- 						<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/icons/Steak.png" class="img-fluid mb-2"> -->
<!-- 						<p class="m-0 small">Steak</p> -->
<!-- 					</a> -->
<!-- 				</div> -->
<!-- 				<div class="cat-item px-1 py-3"> -->
<!-- 					<a class="bg-white rounded d-block p-2 text-center shadow-sm" href="trending.html">  -->
<!-- 						<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/icons/ColaCan.png" class="img-fluid mb-2"> -->
<!-- 						<p class="m-0 small">ColaCan</p> -->
<!-- 					</a> -->
<!-- 				</div> -->
<!-- 				<div class="cat-item px-1 py-3"> -->
<!-- 					<a class="bg-white rounded d-block p-2 text-center shadow-sm" href="trending.html">  -->
<!-- 						<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/icons/Breakfast.png" class="img-fluid mb-2"> -->
<!-- 						<p class="m-0 small">Breakfast</p> -->
<!-- 					</a> -->
<!-- 				</div> -->
<!-- 				<div class="cat-item px-1 py-3"> -->
<!-- 					<a class="bg-white rounded d-block p-2 text-center shadow-sm" href="trending.html">  -->
<!-- 						<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/icons/Salad.png" class="img-fluid mb-2"> -->
<!-- 						<p class="m-0 small">Salad</p> -->
<!-- 					</a> -->
<!-- 				</div> -->
			</div>
		</div>
		
		<!-- Tendaces -->
		<div class="container">

			<div class="pt-4 pb-2 title d-flex align-items-center">
				<h5 class="m-0">Tendances actuelle</h5>
				<a class="font-weight-bold ml-auto" href="trending.html">Tout voir <i class="feather-chevrons-right"></i></a>
			</div>

			<div class="trending-slider">
				<div class="osahan-slider-item">
					<div class="list-card bg-white h-100 rounded overflow-hidden position-relative shadow-sm">
						<div class="list-card-image">
							<div class="star position-absolute">
								<span class="badge badge-success"><i class="feather-star"></i> 3.1 (300+)</span>
							</div>
							<div class="favourite-heart text-danger position-absolute">
								<a href="#"><i class="feather-heart"></i></a>
							</div>
							<div class="member-plan position-absolute">
								<span class="badge badge-dark">Promoted</span>
							</div>
							<a href="restaurant.html"> <img alt="#" src="domaine/caisse/mobile-market/template/resources/img/trending1.png" class="img-fluid item-img w-100">
							</a>
						</div>
						<div class="p-3 position-relative">
							<div class="list-card-body">
								<h6 class="mb-1">
									<a href="restaurant.html" class="text-black">Famous Dave's Bar-B-Que </a>
								</h6>
								<p class="text-gray mb-3">Vegetarian • Indian • Pure veg</p>
								<p class="text-gray mb-3 time">
									<span class="bg-light text-dark rounded-sm pl-2 pb-1 pt-1 pr-2"><i class="feather-clock"></i> 15–30 min</span> <span class="float-right text-black-50"> $350 FOR TWO</span>
								</p>
							</div>
							<div class="list-card-badge">
								<span class="badge badge-danger">OFFER</span> <small> Use Coupon OSAHAN50</small>
							</div>
						</div>
					</div>
				</div>
				<div class="osahan-slider-item">
					<div class="list-card bg-white h-100 rounded overflow-hidden position-relative shadow-sm">
						<div class="list-card-image">
							<div class="star position-absolute">
								<span class="badge badge-success"><i class="feather-star"></i> 3.1 (300+)</span>
							</div>
							<div class="favourite-heart text-danger position-absolute">
								<a href="#"><i class="feather-heart"></i></a>
							</div>
							<div class="member-plan position-absolute">
								<span class="badge badge-dark">Promoted</span>
							</div>
							<a href="restaurant.html"> <img alt="#" src="domaine/caisse/mobile-market/template/resources/img/trending2.png" class="img-fluid item-img w-100">
							</a>
						</div>
						<div class="p-3 position-relative">
							<div class="list-card-body">
								<h6 class="mb-1">
									<a href="restaurant.html" class="text-black">Thai Famous Cuisine</a>
								</h6>
								<p class="text-gray mb-3">North Indian • Indian • Pure veg</p>
								<p class="text-gray mb-3 time">
									<span class="bg-light text-dark rounded-sm pl-2 pb-1 pt-1 pr-2"><i class="feather-clock"></i> 30–35 min</span> <span class="float-right text-black-50"> $250 FOR TWO</span>
								</p>
							</div>
							<div class="list-card-badge">
								<span class="badge badge-success">OFFER</span> <small>65% off</small>
							</div>
						</div>
					</div>
				</div>
				<div class="osahan-slider-item">
					<div class="list-card bg-white h-100 rounded overflow-hidden position-relative shadow-sm">
						<div class="list-card-image">
							<div class="star position-absolute">
								<span class="badge badge-success"><i class="feather-star"></i> 3.1 (300+)</span>
							</div>
							<div class="favourite-heart text-danger position-absolute">
								<a href="#"><i class="feather-heart"></i></a>
							</div>
							<div class="member-plan position-absolute">
								<span class="badge badge-dark">Promoted</span>
							</div>
							<a href="restaurant.html"> <img alt="#" src="domaine/caisse/mobile-market/template/resources/img/trending3.png" class="img-fluid item-img w-100">
							</a>
						</div>
						<div class="p-3 position-relative">
							<div class="list-card-body">
								<h6 class="mb-1">
									<a href="restaurant.html" class="text-black">The osahan Restaurant </a>
								</h6>
								<p class="text-gray mb-3">North • Hamburgers • Pure veg</p>
								<p class="text-gray mb-3 time">
									<span class="bg-light text-dark rounded-sm pl-2 pb-1 pt-1 pr-2"><i class="feather-clock"></i> 15–25 min</span> <span class="float-right text-black-50"> $500 FOR TWO</span>
								</p>
							</div>
							<div class="list-card-badge">
								<span class="badge badge-danger">OFFER</span> <small>65% OSAHAN50</small>
							</div>
						</div>
					</div>
				</div>
				<div class="osahan-slider-item">
					<div class="list-card bg-white h-100 rounded overflow-hidden position-relative shadow-sm">
						<div class="list-card-image">
							<div class="star position-absolute">
								<span class="badge badge-success"><i class="feather-star"></i> 3.1 (300+)</span>
							</div>
							<div class="favourite-heart text-danger position-absolute">
								<a href="#"><i class="feather-heart"></i></a>
							</div>
							<div class="member-plan position-absolute">
								<span class="badge badge-dark">Promoted</span>
							</div>
							<a href="restaurant.html"> <img alt="#" src="domaine/caisse/mobile-market/template/resources/img/trending2.png" class="img-fluid item-img w-100">
							</a>
						</div>
						<div class="p-3 position-relative">
							<div class="list-card-body">
								<h6 class="mb-1">
									<a href="restaurant.html" class="text-black">Thai Famous Cuisine</a>
								</h6>
								<p class="text-gray mb-3">North Indian • Indian • Pure veg</p>
								<p class="text-gray mb-3 time">
									<span class="bg-light text-dark rounded-sm pl-2 pb-1 pt-1 pr-2"><i class="feather-clock"></i> 30–35 min</span> <span class="float-right text-black-50"> $250 FOR TWO</span>
								</p>
							</div>
							<div class="list-card-badge">
								<span class="badge badge-success">OFFER</span> <small>65% off</small>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="py-3 title d-flex align-items-center">
				<h5 class="m-0">Les plus populaires</h5>
				<a class="font-weight-bold ml-auto" href="most_popular.html">26 places <i class="feather-chevrons-right"></i></a>
			</div>

			<div class="most_popular">
				<div class="row">
					<div class="col-md-3 pb-3">
						<div class="list-card bg-white h-100 rounded overflow-hidden position-relative shadow-sm">
							<div class="list-card-image">
								<div class="star position-absolute">
									<span class="badge badge-success"><i class="feather-star"></i> 3.1 (300+)</span>
								</div>
								<div class="favourite-heart text-danger position-absolute">
									<a href="#"><i class="feather-heart"></i></a>
								</div>
								<div class="member-plan position-absolute">
									<span class="badge badge-dark">Promoted</span>
								</div>
								<a href="restaurant.html"> <img alt="#" src="domaine/caisse/mobile-market/template/resources/img/popular1.png" class="img-fluid item-img w-100">
								</a>
							</div>
							<div class="p-3 position-relative">
								<div class="list-card-body">
									<h6 class="mb-1">
										<a href="restaurant.html" class="text-black">The osahan Restaurant </a>
									</h6>
									<p class="text-gray mb-1 small">• North • Hamburgers</p>
									<p class="text-gray mb-1 rating"></p>
									<ul class="rating-stars list-unstyled">
										<li><i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star"></i></li>
									</ul>
									<p></p>
								</div>
								<div class="list-card-badge">
									<span class="badge badge-danger">OFFER</span> <small>65% OSAHAN50</small>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-3 pb-3">
						<div class="list-card bg-white h-100 rounded overflow-hidden position-relative shadow-sm">
							<div class="list-card-image">
								<div class="star position-absolute">
									<span class="badge badge-success"><i class="feather-star"></i> 3.1 (300+)</span>
								</div>
								<div class="favourite-heart text-danger position-absolute">
									<a href="#"><i class="feather-heart"></i></a>
								</div>
								<div class="member-plan position-absolute">
									<span class="badge badge-dark">Promoted</span>
								</div>
								<a href="restaurant.html"> <img alt="#" src="domaine/caisse/mobile-market/template/resources/img/popular2.png" class="img-fluid item-img w-100">
								</a>
							</div>
							<div class="p-3 position-relative">
								<div class="list-card-body">
									<h6 class="mb-1">
										<a href="restaurant.html" class="text-black">Thai Famous Indian Cuisine</a>
									</h6>
									<p class="text-gray mb-1 small">• Indian • Pure veg</p>
									<p class="text-gray mb-1 rating"></p>
									<ul class="rating-stars list-unstyled">
										<li><i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star"></i></li>
									</ul>
									<p></p>
								</div>
								<div class="list-card-badge">
									<span class="badge badge-success">OFFER</span> <small>65% off</small>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-3 pb-3">
						<div class="list-card bg-white h-100 rounded overflow-hidden position-relative shadow-sm">
							<div class="list-card-image">
								<div class="star position-absolute">
									<span class="badge badge-success"><i class="feather-star"></i> 3.1 (300+)</span>
								</div>
								<div class="favourite-heart text-danger position-absolute">
									<a href="#"><i class="feather-heart"></i></a>
								</div>
								<div class="member-plan position-absolute">
									<span class="badge badge-dark">Promoted</span>
								</div>
								<a href="restaurant.html"> <img alt="#" src="domaine/caisse/mobile-market/template/resources/img/popular3.png" class="img-fluid item-img w-100">
								</a>
							</div>
							<div class="p-3 position-relative">
								<div class="list-card-body">
									<h6 class="mb-1">
										<a href="restaurant.html" class="text-black">The osahan Restaurant </a>
									</h6>
									<p class="text-gray mb-1 small">• Hamburgers • Pure veg</p>
									<p class="text-gray mb-1 rating"></p>
									<ul class="rating-stars list-unstyled">
										<li><i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star"></i></li>
									</ul>
									<p></p>
								</div>
								<div class="list-card-badge">
									<span class="badge badge-danger">OFFER</span> <small>65% OSAHAN50</small>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-3 pb-3">
						<div class="list-card bg-white h-100 rounded overflow-hidden position-relative shadow-sm">
							<div class="list-card-image">
								<div class="star position-absolute">
									<span class="badge badge-success"><i class="feather-star"></i> 3.1 (300+)</span>
								</div>
								<div class="favourite-heart text-danger position-absolute">
									<a href="#"><i class="feather-heart"></i></a>
								</div>
								<div class="member-plan position-absolute">
									<span class="badge badge-dark">Promoted</span>
								</div>
								<a href="restaurant.html"> <img alt="#" src="domaine/caisse/mobile-market/template/resources/img/popular4.png" class="img-fluid item-img w-100">
								</a>
							</div>
							<div class="p-3 position-relative">
								<div class="list-card-body">
									<h6 class="mb-1">
										<a href="restaurant.html" class="text-black">Bite Me Now Sandwiches</a>
									</h6>
									<p class="text-gray mb-1 small">American • Pure veg</p>
									<p class="text-gray mb-1 rating"></p>
									<ul class="rating-stars list-unstyled">
										<li><i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star"></i></li>
									</ul>
									<p></p>
								</div>
								<div class="list-card-badge">
									<span class="badge badge-success">OFFER</span> <small>65% off</small>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 pb-3">
						<div class="list-card bg-white h-100 rounded overflow-hidden position-relative shadow-sm">
							<div class="list-card-image">
								<div class="star position-absolute">
									<span class="badge badge-success"><i class="feather-star"></i> 3.1 (300+)</span>
								</div>
								<div class="favourite-heart text-danger position-absolute">
									<a href="#"><i class="feather-heart"></i></a>
								</div>
								<div class="member-plan position-absolute">
									<span class="badge badge-dark">Promoted</span>
								</div>
								<a href="restaurant.html"> <img alt="#" src="domaine/caisse/mobile-market/template/resources/img/popular5.png" class="img-fluid item-img w-100">
								</a>
							</div>
							<div class="p-3 position-relative">
								<div class="list-card-body">
									<h6 class="mb-1">
										<a href="restaurant.html" class="text-black">The osahan Restaurant </a>
									</h6>
									<p class="text-gray mb-1 small">• North • Hamburgers</p>
									<p class="text-gray mb-1 rating"></p>
									<ul class="rating-stars list-unstyled">
										<li><i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star"></i></li>
									</ul>
									<p></p>
								</div>
								<div class="list-card-badge">
									<span class="badge badge-danger">OFFER</span> <small>65% OSAHAN50</small>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-3 pb-3">
						<div class="list-card bg-white h-100 rounded overflow-hidden position-relative shadow-sm">
							<div class="list-card-image">
								<div class="star position-absolute">
									<span class="badge badge-success"><i class="feather-star"></i> 3.1 (300+)</span>
								</div>
								<div class="favourite-heart text-danger position-absolute">
									<a href="#"><i class="feather-heart"></i></a>
								</div>
								<div class="member-plan position-absolute">
									<span class="badge badge-dark">Promoted</span>
								</div>
								<a href="restaurant.html"> <img alt="#" src="domaine/caisse/mobile-market/template/resources/img/popular6.png" class="img-fluid item-img w-100">
								</a>
							</div>
							<div class="p-3 position-relative">
								<div class="list-card-body">
									<h6 class="mb-1">
										<a href="restaurant.html" class="text-black">Thai Famous Indian Cuisine</a>
									</h6>
									<p class="text-gray mb-1 small">• Indian • Pure veg</p>
									<p class="text-gray mb-1 rating"></p>
									<ul class="rating-stars list-unstyled">
										<li><i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star"></i></li>
									</ul>
									<p></p>
								</div>
								<div class="list-card-badge">
									<span class="badge badge-success">OFFER</span> <small>65% off</small>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-3 pb-3">
						<div class="list-card bg-white h-100 rounded overflow-hidden position-relative shadow-sm">
							<div class="list-card-image">
								<div class="star position-absolute">
									<span class="badge badge-success"><i class="feather-star"></i> 3.1 (300+)</span>
								</div>
								<div class="favourite-heart text-danger position-absolute">
									<a href="#"><i class="feather-heart"></i></a>
								</div>
								<div class="member-plan position-absolute">
									<span class="badge badge-dark">Promoted</span>
								</div>
								<a href="restaurant.html"> <img alt="#" src="domaine/caisse/mobile-market/template/resources/img/popular7.png" class="img-fluid item-img w-100">
								</a>
							</div>
							<div class="p-3 position-relative">
								<div class="list-card-body">
									<h6 class="mb-1">
										<a href="restaurant.html" class="text-black">The osahan Restaurant </a>
									</h6>
									<p class="text-gray mb-1 small">• Hamburgers • Pure veg</p>
									<p class="text-gray mb-1 rating"></p>
									<ul class="rating-stars list-unstyled">
										<li><i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star"></i></li>
									</ul>
									<p></p>
								</div>
								<div class="list-card-badge">
									<span class="badge badge-danger">OFFER</span> <small>65% OSAHAN50</small>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-3 pb-3">
						<div class="list-card bg-white h-100 rounded overflow-hidden position-relative shadow-sm">
							<div class="list-card-image">
								<div class="star position-absolute">
									<span class="badge badge-success"><i class="feather-star"></i> 3.1 (300+)</span>
								</div>
								<div class="favourite-heart text-danger position-absolute">
									<a href="#"><i class="feather-heart"></i></a>
								</div>
								<div class="member-plan position-absolute">
									<span class="badge badge-dark">Promoted</span>
								</div>
								<a href="restaurant.html"> <img alt="#" src="domaine/caisse/mobile-market/template/resources/img/popular8.png" class="img-fluid item-img w-100">
								</a>
							</div>
							<div class="p-3 position-relative">
								<div class="list-card-body">
									<h6 class="mb-1">
										<a href="restaurant.html" class="text-black">Bite Me Now Sandwiches</a>
									</h6>
									<p class="text-gray mb-1 small">American • Pure veg</p>
									<p class="text-gray mb-1 rating"></p>
									<ul class="rating-stars list-unstyled">
										<li><i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star star_active"></i> <i class="feather-star"></i></li>
									</ul>
									<p></p>
								</div>
								<div class="list-card-badge">
									<span class="badge badge-success">OFFER</span> <small>65% off</small>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="pt-2 pb-3 title d-flex align-items-center">
				<h5 class="m-0">Les plus vendus</h5>
				<a class="font-weight-bold ml-auto" href="#">26 places <i class="feather-chevrons-right"></i></a>
			</div>

			<div class="most_sale">
				<div class="row mb-3">
					<div class="col-md-4 mb-3">
						<div class="d-flex align-items-center list-card bg-white h-100 rounded overflow-hidden position-relative shadow-sm">
							<div class="list-card-image">
								<div class="star position-absolute">
									<span class="badge badge-success"><i class="feather-star"></i> 3.1 (300+)</span>
								</div>
								<div class="favourite-heart text-danger position-absolute">
									<a href="#"><i class="feather-heart"></i></a>
								</div>
								<div class="member-plan position-absolute">
									<span class="badge badge-dark">Promoted</span>
								</div>
								<a href="restaurant.html"> <img alt="#" src="domaine/caisse/mobile-market/template/resources/img/sales1.png" class="img-fluid item-img w-100">
								</a>
							</div>
							<div class="p-3 position-relative">
								<div class="list-card-body">
									<h6 class="mb-1">
										<a href="restaurant.html" class="text-black">The osahan Restaurant </a>
									</h6>
									<p class="text-gray mb-3">North • Hamburgers • Pure veg</p>
									<p class="text-gray mb-3 time">
										<span class="bg-light text-dark rounded-sm pl-2 pb-1 pt-1 pr-2"><i class="feather-clock"></i> 15–25 min</span> <span class="float-right text-black-50"> $500 FOR TWO</span>
									</p>
								</div>
								<div class="list-card-badge">
									<span class="badge badge-danger">OFFER</span> <small>65% OSAHAN50</small>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-4 mb-3">
						<div class="d-flex align-items-center list-card bg-white h-100 rounded overflow-hidden position-relative shadow-sm">
							<div class="list-card-image">
								<div class="star position-absolute">
									<span class="badge badge-success"><i class="feather-star"></i> 3.1 (300+)</span>
								</div>
								<div class="favourite-heart text-danger position-absolute">
									<a href="#"><i class="feather-heart"></i></a>
								</div>
								<div class="member-plan position-absolute">
									<span class="badge badge-dark">Promoted</span>
								</div>
								<a href="restaurant.html"> <img alt="#" src="domaine/caisse/mobile-market/template/resources/img/sales2.png" class="img-fluid item-img w-100">
								</a>
							</div>
							<div class="p-3 position-relative">
								<div class="list-card-body">
									<h6 class="mb-1">
										<a href="restaurant.html" class="text-black">Thai Famous Cuisine</a>
									</h6>
									<p class="text-gray mb-3">North Indian • Indian • Pure veg</p>
									<p class="text-gray mb-3 time">
										<span class="bg-light text-dark rounded-sm pl-2 pb-1 pt-1 pr-2"><i class="feather-clock"></i> 30–35 min</span> <span class="float-right text-black-50"> $250 FOR TWO</span>
									</p>
								</div>
								<div class="list-card-badge">
									<span class="badge badge-success">OFFER</span> <small>65% off</small>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-4 mb-3">
						<div class="d-flex align-items-center list-card bg-white h-100 rounded overflow-hidden position-relative shadow-sm">
							<div class="list-card-image">
								<div class="star position-absolute">
									<span class="badge badge-success"><i class="feather-star"></i> 3.1 (300+)</span>
								</div>
								<div class="favourite-heart text-danger position-absolute">
									<a href="#"><i class="feather-heart"></i></a>
								</div>
								<div class="member-plan position-absolute">
									<span class="badge badge-dark">Promoted</span>
								</div>
								<a href="restaurant.html"> <img alt="#" src="domaine/caisse/mobile-market/template/resources/img/sales3.png" class="img-fluid item-img w-100">
								</a>
							</div>
							<div class="p-3 position-relative">
								<div class="list-card-body">
									<h6 class="mb-1">
										<a href="restaurant.html" class="text-black">The osahan Restaurant </a>
									</h6>
									<p class="text-gray mb-3">North • Hamburgers • Pure veg</p>
									<p class="text-gray mb-3 time">
										<span class="bg-light text-dark rounded-sm pl-2 pb-1 pt-1 pr-2"><i class="feather-clock"></i> 15–25 min</span> <span class="float-right text-black-50"> $500 FOR TWO</span>
									</p>
								</div>
								<div class="list-card-badge">
									<span class="badge badge-danger">OFFER</span> <small>65% OSAHAN50</small>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
	<script type="a7fa0bc4ee59ea5ed9962834-text/javascript" src="domaine/caisse/mobile-market/template/resources/js/osahan.js"></script>
	<script src="domaine/caisse/mobile-market/template/resources/js/rocket-loader.min.js" data-cf-settings="a7fa0bc4ee59ea5ed9962834-|49" defer></script>
		