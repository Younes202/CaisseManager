	<%@taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
	<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
	
	<script src="resources/framework/js/jquery.min.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function (){
			$(".second-nav a").click(function(){
				  
			});			
		});
	</script>
	
	
	<ul class="second-nav">
			<li><a href="mob-market"><i class="feather-home mr-2"></i> Accueil</a></li>
			
<%-- 			<%if(ContextAppli.getUserBean() != null){ %> --%>
				<li>
					<std:link action="caisse.clientMobile.loadHistorique" targetDiv="detail-content" classStyle="" params="skipF=1">
						<i class="feather-list mr-2"></i> Mes commandes
					</std:link>
				</li>
				<li>
					<a href="my_order.html"><i class="feather-list mr-2"></i> Mes points</a>
				</li>
				<li>
					<a href="my_order.html"><i class="feather-list mr-2"></i> Mon portefeuille</a>
				</li>
				<li>
					<std:link action="caisse.clientMobile.loadFavoris" targetDiv="detail-content" classStyle="" params="skipF=1">
						<i class="feather-heart mr-2"></i> Mes Favoris
					</std:link>
				</li>
<%-- 			<%} %> --%>
			<li>
				<a href="#"><i class="feather-edit-2 mr-2"></i> Authentification</a>
				<ul>
<%-- 					<%if(ContextAppli.getUserBean() == null){ %> --%>
						<li>
							<std:link action="caisse.clientMobile.init_login" params="act=log&skipF=1" classStyle="" targetDiv="detail-content">Se connecter</std:link>
						</li>
						<li>
							<std:link action="caisse.clientMobile.init_login" params="act=reg&skipF=1" classStyle="" targetDiv="detail-content">Créer mon compte</std:link>
						</li>
						<li>
							<std:link action="caisse.clientMobile.init_login" params="act=pw&skipF=1" classStyle="" targetDiv="detail-content">Mot de passe </std:link>
						</li>
						<li>
							<std:link action="caisse.clientMobile.init_login" params="act=verif&skipF=1" classStyle="" targetDiv="detail-content">Vérification</std:link>
						</li>
						<li>
							<std:link action="caisse.clientMobile.loadTrackCmd" params="skipF=1" classStyle="" targetDiv="detail-content">Suivi livraison</std:link>
						</li>
<%-- 					<%} else{ %> --%>
						<li>
							<std:link action="caisse.clientMobile.logout" params="skipF=1" classStyle="" targetDiv="detail-content">Se déconnecter</std:link>
						</li>
<%-- 					<%} %> --%>
				</ul>
			</li>
			
<%-- 			<%if(ContextAppli.getUserBean() != null){ %> --%>
				<li>
					<std:link action="caisse.clientMobile.loadCompte" targetDiv="detail-content" classStyle="" params="skipF=1">
						<i class="feather-heart mr-2"></i> Mon compte
					</std:link>
				</li>
<%-- 			<%} %> --%>
			
			<li>
				<std:link action="caisse.clientMobile.loadContact" targetDiv="detail-content" classStyle="" params="skipF=1">
					<i class="feather-heart mr-2"></i> Aide et contact
				</std:link>
			</li>
				
<!-- 			<li><a href="#"><i class="feather-user mr-2"></i> Aide</a> -->
<!-- 				<ul> -->
<!-- 					<li><a href="profile.html">FAQ</a></li> -->
<!-- 					<li><a href="profile.html">Guide d'utilisation</a></li> -->
<!-- 					<li><a href="favorites.html">Livraison</a></li> -->
<!-- 					<li> -->
<%-- 						<std:link action="caisse.clientMobile.loadContact" targetDiv="detail-content" classStyle="" params="skipF=1">Nous contacter</std:link> --%>
<!-- 					</li> -->
<!-- 					<li><a href="terms.html">Condition d'utilisation</a></li> -->
<!-- 					<li><a href="privacy.html">Vie privée</a></li> -->
<!-- 				</ul> -->
<!-- 			</li> -->
		</ul>
