
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<style>
#generic_modal_body{
	width: 720px;
	margin-left: -10%;
}
.maj_tr{
	color: #9c27b0;
    font-size: 12px;
}
</style>

	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Informations logiciel</span>
			
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body">
			<div class="tabbable">
				<ul class="nav nav-tabs" id="myTab">
					<li class='active'>
						<a data-toggle="tab" href="#evols">MISES À JOUR LOGICIEL</a>
					</li>
					<li>
						<a data-toggle="tab" href="#formule">FORMULE D'ABONNEMENT</a>
					</li>
				</ul>
				<div class="tab-content">
					<div id="evols" class="tab-pane in active">
						<div class="row" style="margin-left: 0px;overflow: auto;max-height: 500px;">
							<table style="width: 100%;font-size: 11px;">
								<!-- 1.0994 -->
								<tr><td><h3>VERSION 1.0994 <span style="font-size: 12px;color: green;">17/09/2022</span></h3></td></tr>
								<tr class="maj_tr"><td>- Amélioration affichage commande en ligne client</td></tr>
								<tr class="maj_tr"><td>- Amélioration vitesse connexion en cas d'absence d'Internet</td></tr>
								<tr class="maj_tr"><td>- Ajout module suivi des chantiers et des travaux</td></tr>
								<tr class="maj_tr"><td>- Ajout plan comptable</td></tr>
								<tr class="maj_tr"><td>- Gestion des immobilisations</td></tr>
								<tr class="maj_tr"><td>- Ajout de nouveaux paramètres et droits</td></tr>
								<tr class="maj_tr"><td>- Amélioration graphique des écrans de cuisine</td></tr>
								<tr class="maj_tr"><td>- Correction problèmes imprimantes cuisine</td></tr>								
								
								<!-- 1.0993 -->
								<tr><td><h3>VERSION 1.0993 <span style="font-size: 12px;color: green;">10/05/2022</span></h3></td></tr>
								<tr class="maj_tr"><td>- Correction regroupement dépenses</td></tr>
								<tr class="maj_tr"><td>- Gestion de la balance</td></tr>
								<tr class="maj_tr"><td>- Ajout paramètre sélection client/serveur/livreur</td></tr>
								<tr class="maj_tr"><td>- Ajout calendrier réservation tables et dates</td></tr>
								<tr class="maj_tr"><td>- Correction affichage commentaire</td></tr>
								<tr class="maj_tr"><td>- Correction calcul montant remise en porcentage</td></tr>
								<tr class="maj_tr"><td>- Correction montants écart journée</td></tr>
								<tr class="maj_tr"><td>- Possibilité saisie sans chargement saisie rapide articles</td></tr>
								<tr class="maj_tr"><td>- Correction module livraison</td></tr>
								<tr class="maj_tr"><td>- Amélioration affichage tableau de bord financier</td></tr>
								
								<tr><td><h3>VERSION 1.0992 <span style="font-size: 12px;color: green;">01/02/2022</span></h3></td></tr>
								<tr class="maj_tr"><td>- Amélioration graphique écran historique commandes caisse</td></tr>
								<tr class="maj_tr"><td>- Pouvoir imprimer plusiers ticket de caisse, codes barre et étiquettes</td></tr>
								<tr class="maj_tr"><td>- Correction bug passation lors de la clôture finale</td></tr>
								<tr class="maj_tr"><td>- Correction bug autentification fenêtre lock commande</td></tr>
								
								<tr><td><h3>VERSION 1.0991 <span style="font-size: 12px;color: green;">17/11/2021</span></h3></td></tr>
								<tr class="maj_tr"><td>- Passation des shifts</td></tr>
								<tr class="maj_tr"><td>- Changement icone reprise table mobile</td></tr>
								<tr class="maj_tr"><td>- Amélioration affichage historique commandes mobile</td></tr>
								<tr class="maj_tr"><td>- Correction bug mise en attente mobile</td></tr>
								<tr class="maj_tr"><td>- Affichage entête menu dans les tickets cuisine</td></tr>
								
								<tr><td><h3>VERSION 1.0990 <span style="font-size: 12px;color: green;">25/09/2021</span></h3></td></tr>
								<tr class="maj_tr"><td>- Amélioration perfs affichage articles et état et synthèse des stocks</td></tr>
								<tr class="maj_tr"><td>- Pouvoir imprimer un nombre saisi d'étiquettes et codes barre</td></tr>
								<tr class="maj_tr"><td>- Amélioration graphiques divers de la caisse</td></tr>
								<tr class="maj_tr"><td>- Pouvoir dupliquer les menus dans la caisse</td></tr>
								<tr class="maj_tr"><td>- Pouvoir affecter un serveur à une commande</td></tr>
								
								<tr class="maj_tr"><td>- Meilleur contrôle des statuts des commandes lors de la mise en attente</td></tr>					
								<tr class="maj_tr"><td>- Correction bug liste des types</td></tr>
								<tr class="maj_tr"><td>- Correction bug ajout des articles</td></tr>
								
								<tr class="maj_tr"><td>&nbsp;</td></tr>
							</table>
						</div>
					</div>
					<div id="formule" class="tab-pane">
						<div class="row" style="margin-left: 0px;overflow: auto;max-height: 500px;">
							<h4>Formule d'abonnement</h4>
							<%=ContextAppli.getAbonementBean().getAbonnementStr() %>
						</div>
					</div>					
				</div>
			</div>	
		</div>
	</div>