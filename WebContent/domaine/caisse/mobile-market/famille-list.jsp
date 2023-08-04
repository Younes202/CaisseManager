<%@page import="appli.model.domaine.stock.persistant.ArticlePersistant"%>
<%@page import="java.util.List"%>
<%@taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<%
List<ArticlePersistant> listArticle = (List<ArticlePersistant>)request.getAttribute("listArticle");
%>

<script type="text/javascript">
$(document).ready(function (){
	$(".modal-backdrop").hide();
});
</script>

	<div class="d-none">
		<div class="bg-primary p-3 d-flex align-items-center">
			<a class="toggle togglew toggle-2" href="#"><span></span></a>
			<h4 class="font-weight-bold m-0 text-white">
				<std:link action="caisse.clientMobile.loadHome" targetDiv="detail-content" params="skipF=1" style="color: white;" classStyle="">
					<i style="border: 1px solid white;padding: 2px;" class="feather-arrow-left"></i> 
				</std:link> 
				${famille.libelle }
			</h4>
		</div>
	</div>
	<div class="osahan-trending">

		<div class="container">
			<div class="most_popular py-5">
				<div class="d-flex align-items-center mb-4">
					<h3 class="font-weight-bold text-dark mb-0">
						<std:link action="caisse.clientMobile.loadHome" targetDiv="detail-content" params="skipF=1" style="color: #b91e1e;" classStyle="">
							<i style="border: 1px solid #b91e1e;padding: 2px;" class="feather-arrow-left"></i> 
						</std:link> 
						${famille.libelle }
					</h3>
					<a href="#" data-toggle="modal" data-target="#filters" class="ml-auto btn btn-primary">Filtrer</a>
				</div>
				<div class="row">
				
				<%for(ArticlePersistant article : listArticle){ %>
					<div class="col-6 col-lg-3 mb-3">
						<div class="list-card bg-white h-100 rounded overflow-hidden position-relative shadow-sm grid-card">
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
										<a href="restaurant.html" class="text-black"><%=article.getLibelle() %> </a>
									</h6>
									<p class="text-gray mb-3">Détail article ...</p>
									<p class="text-gray mb-3 time">
										<span class="bg-light text-dark rounded-sm pl-2 pb-1 pt-1 pr-2">
											<i class="feather-clock"></i> 15–25 min
										</span> 
										<span style="float: left;font-size: 13px;color: #b10101;"> 500dhs</span>
									</p>
								</div>
								<div class="list-card-badge">
									<span class="badge badge-danger">OFFER</span> <small>65% OSAHAN50</small>
								</div>
							</div>
							
							<!-- Add -->
							<div class="d-flex align-items-center" style="margin: -8px 0px 10px 25%;">
								<span class="count-number float-right">
									<button type="button" class="btn-sm left dec btn btn-outline-secondary"> 
										<i class="feather-minus"></i> 
									</button>
									<input class="count-number-input" type="text" readonly="" value="2">
									<button type="button" class="btn-sm right inc btn btn-outline-secondary"> 
										<i class="feather-plus"></i> 
									</button>
								</span>
<!-- 								<p class="text-gray mb-0 float-right ml-2 text-muted small">$628</p> -->
							</div>
							<!-- End -->

						</div>
					</div>
				<%} %>	
				</div>
			</div>
		</div>

	</div>
