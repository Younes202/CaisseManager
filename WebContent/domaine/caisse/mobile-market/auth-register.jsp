<%@taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<h2 class="text-dark my-0">Créer un compte, c'est simple.</h2>
<p class="text-50">Remplir le formulaire</p>

<div class="form-group">
	<label for="exampleInputName1" class="text-dark">Nom</label> 
	<input type="text" placeholder="Votre nom" class="form-control" id="exampleInputName1" aria-describedby="nameHelp">
</div>
<div class="form-group">
	<label for="exampleInputNumber1" class="text-dark">Mobile </label> 
	<input type="number" placeholder="Mobile" class="form-control" id="exampleInputNumber1" aria-describedby="numberHelp">
</div>
<div class="form-group">
	<label for="exampleInputPassword1" class="text-dark">Mot de passe</label> 
	<input type="password" placeholder="Mot de passe" class="form-control" id="exampleInputPassword1">
</div>
<button class="btn btn-primary btn-lg btn-block">CREER MON COMPTE</button>
<div class="py-2"></div>

<div class="new-acc d-flex align-items-center justify-content-center">
	<std:link action="caisse.clientMobile.init_login" params="act=log&skipF=1" targetDiv="auth_dest_div">
		<p class="text-center m-0">Vous avez déjà un compte? Se connecter</p>
	</std:link>
</div>