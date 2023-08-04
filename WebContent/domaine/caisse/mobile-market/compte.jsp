	<%@taglib uri="http://www.customtaglib.com/standard" prefix="std"%>	
	
	<div class="osahan-profile">
		<div class="d-none">
			<div class="bg-primary border-bottom p-3 d-flex align-items-center">
				<a class="toggle togglew toggle-2" href="#"><span></span></a>
				<h4 class="font-weight-bold m-0 text-white">Mon compte</h4>
			</div>
		</div>

		<div class="container position-relative">
			<div class="py-5 osahan-profile row">
				<div class="col-md-4 mb-3">
					<div class="bg-white rounded shadow-sm sticky_sidebar overflow-hidden">
						<a href="profile.html" class>
							<div class="d-flex align-items-center p-3">
								<div class="left mr-3">
									<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/user1.jpg" class="rounded-circle">
								</div>
								<div class="right">
									<h6 class="mb-1 font-weight-bold">
										Gurdeep Singh <i class="feather-check-circle text-success"></i>
									</h6>
									<p class="text-muted m-0 small">
										<span class="__cf_email__" data-cfemail="274e464a4854464f464967404a464e4b0944484a">[email&#160;protected]</span>
									</p>
								</div>
							</div>
						</a>
						<div class="osahan-credits d-flex align-items-center p-3 bg-light">
							<p class="m-0">Solde portefeuille Credits</p>
							<h5 class="m-0 ml-auto text-primary">520.00dhs</h5>
						</div>

						<div class="bg-white profile-details">
							<std:link action="caisse.clientMobile.loadCompte" params="act=manage&skipF=1" targetDiv="aide_det_div" classStyle="d-flex w-100 align-items-center border-bottom p-3">
								<div class="left mr-3">
									<h6 class="font-weight-bold mb-1 text-dark">Compte</h6>
									<p class="small text-muted m-0">Gérer mon compte</p>
								</div>
								<div class="right ml-auto">
									<h6 class="font-weight-bold m-0">
										<i class="feather-chevron-right"></i>
									</h6>
								</div>
							</std:link> 
							<std:link action="caisse.clientMobile.loadCompte" params="act=paiement&skipF=1" targetDiv="aide_det_div" classStyle="d-flex w-100 align-items-center border-bottom p-3">
								<div class="left mr-3">
									<h6 class="font-weight-bold mb-1 text-dark">Carte de paiement</h6>
									<p class="small text-muted m-0">Ajouter une carte</p>
								</div>
								<div class="right ml-auto">
									<h6 class="font-weight-bold m-0">
										<i class="feather-chevron-right"></i>
									</h6>
								</div>
							</std:link> 
							<std:link action="caisse.clientMobile.loadCompte" params="act=adresse&skipF=1" targetDiv="aide_det_div" classStyle="d-flex w-100 align-items-center border-bottom p-3">
								<div class="left mr-3">
									<h6 class="font-weight-bold mb-1 text-dark">Addresses</h6>
									<p class="small text-muted m-0">Gérer vos adresses de livraison et facturation</p>
								</div>
								<div class="right ml-auto">
									<h6 class="font-weight-bold m-0">
										<i class="feather-chevron-right"></i>
									</h6>
								</div>
							</std:link> 
							<std:link action="caisse.clientMobile.loadCompte" params="act=invite&skipF=1" targetDiv="aide_det_div" classStyle="d-flex align-items-center border-bottom p-3">
								<div class="left mr-3">
									<h6 class="font-weight-bold mb-1">Inviter des amis</h6>
									<p class="small text-primary m-0">Gagner gratuitement 100dhs</p>
								</div>
								<div class="right ml-auto">
									<h6 class="font-weight-bold m-0">
										<i class="feather-chevron-right"></i>
									</h6>
								</div>
							</std:link> 
						</div>
					</div>
				</div>
				<div class="col-md-8 mb-3" id="aide_det_div">
					<jsp:include page="compte-manage.jsp" />
				</div>
			</div>
		</div>
</div>

<script type="a7fa0bc4ee59ea5ed9962834-text/javascript" src="domaine/caisse/mobile-market/template/resources/js/osahan.js"></script>