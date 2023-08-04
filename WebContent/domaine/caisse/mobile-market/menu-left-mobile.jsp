<%@taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

		<div class="row">
			<div class="col selected">
				<a href="mob-market" class="text-danger small font-weight-bold text-decoration-none">
					<p class="h4 m-0">
						<i class="feather-home text-danger"></i>
					</p> Accueil
				</a>
			</div>
			<div class="col">
				<std:link action="caisse.clientMobile.loadTendance" targetDiv="detail-content" params="skipF=1" classStyle="text-dark small font-weight-bold text-decoration-none">
					<p class="h4 m-0">
						<i class="feather-map-pin"></i>
					</p> Tendances
				</std:link>
			</div>
			<div class="col bg-white rounded-circle mt-n4 px-3 py-2">
				<div class="bg-danger rounded-circle mt-n0 shadow">
					<std:link action="caisse.clientMobile.init_panier" targetDiv="detail-content" params="skipF=1" classStyle="text-white small font-weight-bold text-decoration-none"> 
						<i class="feather-shopping-cart"></i>
					</std:link>
				</div>
			</div>
			<div class="col">
				<std:link action="caisse.clientMobile.loadFavoris" targetDiv="detail-content" params="skipF=1" classStyle="text-dark small font-weight-bold text-decoration-none">
					<p class="h4 m-0">
						<i class="feather-heart"></i>
					</p> Favoris
				</std:link>
			</div>
			<div class="col">
				<std:link action="caisse.clientMobile.loadCompte" targetDiv="detail-content" params="skipF=1" classStyle="text-dark small font-weight-bold text-decoration-none">
					<p class="h4 m-0">
						<i class="feather-user"></i>
					</p> Compte
				</std:link>
			</div>
		</div>