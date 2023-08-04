<%@page import="framework.model.common.util.StrimUtil"%>
<%@taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<!DOCTYPE html>
<html lang="fr">

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<meta name="description" content="Gurdeep Osahan">
	<meta name="author" content="Gurdeep Osahan">
	<link rel="icon" type="image/png" href="img/logo_web_red.png">
	<title>Caisse manager - Commande en ligne</title>
	
	<link rel="stylesheet" type="text/css" href="domaine/caisse/mobile-market/template/resources/vendor/slick/slick.min.css" />
	<link rel="stylesheet" type="text/css" href="domaine/caisse/mobile-market/template/resources/vendor/slick/slick-theme.min.css" />
	<link href="domaine/caisse/mobile-market/template/resources/vendor/icons/feather.css" rel="stylesheet" type="text/css">
	<link href="domaine/caisse/mobile-market/template/resources/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link href="domaine/caisse/mobile-market/template/resources/css/style.css" rel="stylesheet">
	<link href="domaine/caisse/mobile-market/template/resources/vendor/sidebar/demo.css" rel="stylesheet">
	
	<script src="resources/framework/js/jquery.min.js"></script>
</head>
<body>
	<div class="login-page vh-100">
		<video loop autoplay muted id="vid">
			<source src="domaine/caisse/mobile-market/template/resources/img/bg.mp4" type="video/mp4">
			<source src="domaine/caisse/mobile-market/template/resources/img/bg.mp4" type="video/ogg">
			Votre navigateur ne supporte ette vidéo.
		</video>
		<div class="d-flex align-items-center justify-content-center vh-100">
			<div class="px-5 col-md-6 ml-auto">
				<a href="mob-market" class="brand-wrap mb-0" style="position: fixed;top: 22px;">
					<h6 class="title"><i style="border: 1px solid #b91e1e;padding: 2px;" class="feather-arrow-left"></i>  Retour</h6>
				</a>
				<img alt="#" class="img-fluid" style="position: fixed;top: 11px;right: 0px;width: 110px;" src="resources/<%=StrimUtil.getGlobalConfigPropertie("context.soft") %>/img/logo_caisse_red.png?v=1.66">
					
				<div class="px-5 col-10 mx-auto" id="auth_dest_div">
					<std:form name="data_form">
						<jsp:include page="auth-login.jsp" />
					</std:form>
				</div>
			</div>
		</div>
	</div>

	<script type="9c8fe1a5986c0f7b512b09e4-text/javascript" src="domaine/caisse/mobile-market/template/resources/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
	<script type="9c8fe1a5986c0f7b512b09e4-text/javascript" src="domaine/caisse/mobile-market/template/resources/vendor/slick/slick.min.js"></script>
	<script type="9c8fe1a5986c0f7b512b09e4-text/javascript" src="domaine/caisse/mobile-market/template/resources/vendor/sidebar/hc-offcanvas-nav.js"></script>
	<script type="9c8fe1a5986c0f7b512b09e4-text/javascript" src="domaine/caisse/mobile-market/template/resources/js/osahan.js"></script>
	<script src="domaine/caisse/mobile-market/template/resources/js/rocket-loader.min.js" data-cf-settings="9c8fe1a5986c0f7b512b09e4-|49" defer></script>
	<script defer src="https://static.cloudflareinsights.com/beacon.min.js/v52afc6f149f6479b8c77fa569edb01181681764108816" integrity="sha512-jGCTpDpBAYDGNYR5ztKt4BQPGef1P0giN6ZGVUi835kFF88FOmmn8jBQWNgrNd8g/Yu421NdgWhwQoaOPFflDw==" data-cf-beacon='{"rayId":"7c9641e58cb40fe2","version":"2023.4.0","r":1,"b":1,"token":"dd471ab1978346bbb991feaa79e6ce5c","si":100}' crossorigin="anonymous"></script>
	
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