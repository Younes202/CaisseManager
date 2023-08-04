<%@taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<h2 class="mb-3">Vérifier votre numéro de téléphone</h2>
<h6 class="text-black-50">Enterer le code reçu ici</h6>
<div class="row my-5 mx-0 otp">
	<div class="col pr-1 pl-0">
		<input type="text" value="4" class="form-control form-control-lg">
	</div>
	<div class="col px-2">
		<input type="text" value="0" class="form-control form-control-lg">
	</div>
	<div class="col px-2">
		<input type="text" value="8" class="form-control form-control-lg">
	</div>
	<div class="col pl-1 pr-0">
		<input type="text" value="0" class="form-control form-control-lg">
	</div>
</div>
<button class="btn btn-lg btn-primary btn-block">Vérifier maintenant</button>

<div class="new-acc d-flex align-items-center justify-content-center">
	<std:link action="caisse.clientMobile.init_login" params="act=log&skipF=1" targetDiv="auth_dest_div">
		<p class="text-center m-0">Vous avez déjà un compte? Se connecter</p>
	</std:link>
</div>
>
