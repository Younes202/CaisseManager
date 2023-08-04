<%@taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

					<h2 class="text-dark my-0">Bienvenue</h2>
					<p class="text-50">Se connecter</p>
						<div class="form-group">
							<label for="exampleInputEmail1" class="text-dark">Email ou téléphone</label> 
							<input type="email" placeholder="Votre Email" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp">
						</div>
						<div class="form-group">
							<label for="exampleInputPassword1" class="text-dark">Mot de passe</label> 
							<input type="password" placeholder="Mot de passe" class="form-control" id="exampleInputPassword1">
						</div>
						<button class="btn btn-primary btn-lg btn-block">SE CONNECTER</button>
						<div class="py-2">
<!-- 							<button class="btn btn-lg btn-facebook btn-block"> -->
<!-- 								<i class="feather-facebook"></i> Connect with Facebook -->
<!-- 							</button> -->
						</div>
					<std:link action="caisse.clientMobile.init_login" params="act=pw&skipF=1" targetDiv="auth_dest_div" classStyle="text-decoration-none">
						<p class="text-center">Mot de passe oublié?</p>
					</std:link>
					<div class="d-flex align-items-center justify-content-center">
						<std:link action="caisse.clientMobile.init_login" params="act=reg&skipF=1" targetDiv="auth_dest_div">
							<p class="text-center m-0">Je n'ai pas de compte? Le créer</p>
						</std:link>
					</div>