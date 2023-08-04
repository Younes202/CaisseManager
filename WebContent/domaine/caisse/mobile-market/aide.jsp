<%@taglib uri="http://www.customtaglib.com/standard" prefix="std"%>	

	
	<div class="osahan-profile">
		<div class="d-none">
			<div class="bg-primary border-bottom p-3 d-flex align-items-center">
				<a class="toggle togglew toggle-2" href="#"><span></span></a>
				<h4 class="font-weight-bold m-0 text-white">Aide et contact</h4>
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
										<span class="__cf_email__" data-cfemail="7019111d1f031118111e30171d11191c5e131f1d">[email&#160;protected]</span>
									</p>
								</div>
							</div>
						</a>
						<div class="osahan-credits d-flex align-items-center p-3 bg-light">
							<p class="m-0">Solde portefeuille Credits</p>
							<h5 class="m-0 ml-auto text-primary">520.00dhs</h5>
						</div>

						<div class="bg-white profile-details">
							<std:link action="caisse.clientMobile.loadContact" params="act=faq&skipF=1" targetDiv="aide_det_div" classStyle="d-flex w-100 align-items-center border-bottom px-3 py-4">
								<div class="left mr-3">
									<h6 class="font-weight-bold m-0 text-dark">
										<i class="feather-truck bg-danger text-white p-2 rounded-circle mr-2"></i> Modalités de livraison
									</h6>
								</div>
								<div class="right ml-auto">
									<h6 class="font-weight-bold m-0">
										<i class="feather-chevron-right"></i>
									</h6>
								</div>
							</std:link> 
							<std:link action="caisse.clientMobile.loadContact" params="act=contact&skipF=1" targetDiv="aide_det_div" classStyle="d-flex w-100 align-items-center border-bottom px-3 py-4">
								<div class="left mr-3">
									<h6 class="font-weight-bold m-0 text-dark">
										<i class="feather-phone bg-primary text-white p-2 rounded-circle mr-2"></i> Contact
									</h6>
								</div>
								<div class="right ml-auto">
									<h6 class="font-weight-bold m-0">
										<i class="feather-chevron-right"></i>
									</h6>
								</div>
							</std:link> 
							<std:link action="caisse.clientMobile.loadContact" params="act=condition&skipF=1" targetDiv="aide_det_div" classStyle="d-flex w-100 align-items-center border-bottom px-3 py-4">
								<div class="left mr-3">
									<h6 class="font-weight-bold m-0 text-dark">
										<i class="feather-info bg-success text-white p-2 rounded-circle mr-2"></i> Conditions d'utilisation
									</h6>
								</div>
								<div class="right ml-auto">
									<h6 class="font-weight-bold m-0">
										<i class="feather-chevron-right"></i>
									</h6>
								</div>
							</std:link> 
							<std:link action="caisse.clientMobile.loadContact" params="act=prive&skipF=1" targetDiv="aide_det_div" classStyle="d-flex w-100 align-items-center px-3 py-4">
								<div class="left mr-3">
									<h6 class="font-weight-bold m-0 text-dark">
										<i class="feather-lock bg-warning text-white p-2 rounded-circle mr-2"></i>Vie privée
									</h6>
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
					<jsp:include page="aide-livraison.jsp" />
				</div>
			</div>
		</div>
	</div>
<script type="a7fa0bc4ee59ea5ed9962834-text/javascript" src="domaine/caisse/mobile-market/template/resources/js/osahan.js"></script>