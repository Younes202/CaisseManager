package framework.model.util;

import java.util.ArrayList;
import java.util.List;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import framework.controller.bean.MenuBean;
import framework.controller.bean.RightBean;
import framework.model.common.util.StrimUtil;

public class MenuAdmin {
	public static List<RightBean> loadRights(){
		String soft = StrimUtil.getGlobalConfigPropertie("context.soft");
		
		List<RightBean> listRights = new ArrayList<>();
		
		listRights.add(new RightBean("SHO", null, "Affichage", true));
		listRights.add(new RightBean("EDI", "SHO", "Consult.", true));
		listRights.add(new RightBean("UPD", "EDI", "Modif.", true));
		listRights.add(new RightBean("CRE", "EDI", "Créat.", true));
		listRights.add(new RightBean("DUP", "EDI", "Duplic.", true));
		listRights.add(new RightBean("DEL", "EDI", "Supp.", true));
		listRights.add(new RightBean("DGR", "DEL", "Supp. groupée", true));
		// <!-- Spécifique --));
		
		if(soft.equals(SOFT_ENVS.restau.toString()) 
				|| soft.equals(SOFT_ENVS.market.toString())){
			listRights.add(new RightBean("CLOJRN", "EDI", "Clôture/ouverture journée", false));
			listRights.add(new RightBean("SHIFT", "EDI", "Shift ouvert", false));
			listRights.add(new RightBean("SHIFTCL", "EDI", "Shift clos", false));
			listRights.add(new RightBean("ECARTINV", "EDI", "Voir écart", false));
			listRights.add(new RightBean("DETJRN", "EDI", "Voir détail journée", false));
			listRights.add(new RightBean("DBLCLO", "EDI", "Double clôture", false));
			listRights.add(new RightBean("DETHISTO", "EDI", "Détail historique", false));
			listRights.add(new RightBean("BILAN", "EDI", "Voir bilan", false));
			listRights.add(new RightBean("ANNULCMD", "EDI", "Annulation commande", false));
		}
		
		return listRights;
	}
	
	/*----------------------------------------LOAD MNU------------------------------------------------*/
	private static void loadPosMenus(List<MenuBean> listMenus){
		listMenus.add(new MenuBean("dashboard", "dash.dashBoard.init_dashBoard", false, 1));
		
		MenuBean mnuDash = new MenuBean("cai-jrn", "dash.dashBoard2.detail_journee", false, 1);
		mnuDash.setAdditionalRrights(new String[]{"DETJRN","DBLCLO","SHIFT","SHIFTCL","DETHISTO"});
		listMenus.add(mnuDash);

		listMenus.add(new MenuBean("agenda", "pers.planning.work_find", false, 1));
		listMenus.add(new MenuBean("fichiers", "admin.ged.work_find", false, 1));
		
		addCaisseMenu(listMenus);
		
		listMenus.add(new MenuBean("cai-pers-client", "Personnel/Client", "fa-crosshairs", 2));
			listMenus.add(new MenuBean("cai-client", "Client" , "Client" , "pers.client.work_find" , "fa-user-plus", null, 3));
			listMenus.add(new MenuBean("cai-employe", "Employés" , "Employé" , "pers.employe.work_find" , "fa-male", null, 3));

		listMenus.add(new MenuBean("refdash", "Référentiel", "fa-crosshairs", 2));
			listMenus.add(new MenuBean("cai-article", "Articles" , "Article" , "stock.composant.work_find" , "fa-ellipsis-v", "tp=C", 3));
			listMenus.add(new MenuBean("cai-famille", "Familles" , "Familles" , "stock.famille.work_find" , "fa-sitemap" , "tp=ST", 3));
		
			listMenus.add(new MenuBean("cai-ticket-caisse", "Ticket" , "Ticket" , "caisse.ticketCaisseConf.work_find" , "fa-file", null, 3));
		
		addLivraisonMenu(listMenus);
		addStockMenu(listMenus);
		addGestionCommercialMenu(listMenus);
		addComptaMenu(listMenus);
		addPaieMenu(listMenus);
		addModuleAvanceMenu(listMenus);
		addDroitMenu(listMenus);
	}
	
	private static void loadRestauMenus(List<MenuBean> listMenus){
		listMenus.add(new MenuBean("dashboard", "dash.dashBoard.init_dashBoard", false, 1));
		
		MenuBean mnuDash = new MenuBean("cai-jrn", "dash.dashBoard2.detail_journee", false, 1);
		mnuDash.setAdditionalRrights(new String[]{"DETJRN","DBLCLO","SHIFT","SHIFTCL","DETHISTO"});
		listMenus.add(mnuDash);

		listMenus.add(new MenuBean("agenda", "pers.planning.work_find", false, 1));
		listMenus.add(new MenuBean("fichiers", "admin.ged.work_find", false, 1));
		
		addCaisseMenu(listMenus);
		
		listMenus.add(new MenuBean("suivicuisine", "Cuisine", "fa-cube", 2));
			listMenus.add(new MenuBean("cai-ecran", "Suivi cuisine" , "Ecrans cuisine" , "caisse_restau.caisseConfigurationRestau.find_ecrans_cuisine" , "fa-desktop" , "tp=mnu", 3));
			listMenus.add(new MenuBean("cai-time", "Suivi CMD" , "Temps commandes" , "caisse_restau.caisseConfigurationRestau.load_temps_cuisine" , "fa-clock-o" , "tp=mnu", 3));
			listMenus.add(new MenuBean("cai-cuis-conf", "Vérifier conf" , "Vérifier conf" , "caisse_restau.caisseConfigurationRestau.check_conf" , "fa-check-square-o" , null, 3));
			listMenus.add(new MenuBean("cai-cuis-param", "Paramétres" , "Paramétres" , "admin.parametrage.work_edit" , "fa-check-square-o" , "fmnu=cuis&tp=cuis", 3));
		
		listMenus.add(new MenuBean("cai-pers-client", "Personnel/Client", "fa-crosshairs", 2));
		listMenus.add(new MenuBean("cai-client", "Client" , "Client" , "pers.client.work_find" , "fa-user-plus", null, 3));
		listMenus.add(new MenuBean("cai-employe", "Employés" , "Employé" , "pers.employe.work_find" , "fa-male", null, 3));

		listMenus.add(new MenuBean("refdash", "Référentiel", "fa-crosshairs", 2));
			listMenus.add(new MenuBean("cai-menu", "Menus" , "Composition menus" , "caisse.menuComposition.work_find" , "fa-cutlery", null, 3));
			listMenus.add(new MenuBean("cai-list", "Liste de choix" , "Liste de choix" , "caisse.listChoix.work_find" , "fa-bars", null, 3));
			listMenus.add(new MenuBean("cai-agencement", "Agencement" , "Agencement des tables" , "admin.agencement.work_find" , "fa-magic", null, 3));
			listMenus.add(new MenuBean("cai-article", "Articles" , "Article" , "stock.article.work_find", "fa-ellipsis-v", "tp=A", 3));
			listMenus.add(new MenuBean("cai-famille", "Familles" , "Familles" , "stock.famille.work_find" , "fa-sitemap" , "tp=CU", 3));
			
		listMenus.add(new MenuBean("cai-ticket-caisse", "Ticket" , "Ticket" , "caisse.ticketCaisseConf.work_find" , "fa-file", null, 3));
		
		addLivraisonMenu(listMenus);
		addStockMenu(listMenus);
		addGestionCommercialMenu(listMenus);
		addComptaMenu(listMenus);
		addPaieMenu(listMenus);
		addModuleAvanceMenu(listMenus);
		addDroitMenu(listMenus);
	}
	
	/**
	 * @param listMenus
	 */
	private static void loadAdminAssocieMenus(List<MenuBean> listMenus){
		listMenus.add(new MenuBean("dashboard", "dash.dashBoard.work_init" , false, 1));
		
		listMenus.add(new MenuBean("gestion-cli", "Gestion clients" , "fa-user-plus", 1));
			listMenus.add(new MenuBean("client", "Clients" , "Clients", "admin.client.work_find", "fa-child", "isAbn=0", 2));
			listMenus.add(new MenuBean("version_cli", "Versions", "Versions clients", "admin.clientAppli.find_last_version" , "fa-server", null, 2));
			listMenus.add(new MenuBean("echeance_cli", "Echéances" , "Echéances clients", "admin.etablissementPaiement.find_echeance" , "fa-server", null, 2));
			listMenus.add(new MenuBean("paiement_cli", "Paiements" , "Paiements clients", "admin.etablissementPaiement.work_find" , "fa fa-money", null, 2));
			
		listMenus.add(new MenuBean("gestion-log", "Gestion logiciel" , "fa fa-cogs", 1));
			listMenus.add(new MenuBean("theme", "Thème gaphique", "Thème gaphique", "admin.application.manage_theme" , "fa-paw", null, 2));
			listMenus.add(new MenuBean("version", "Versions logiciels", "Versions logiciels", "admin.application.work_find" , "fa-barcode", null, 2));
			listMenus.add(new MenuBean("ets-notif", "Notifications", "Notifications", "notif.notification.work_find", "fa-bell", null, 2));
		
		listMenus.add(new MenuBean("droits-secure", "Droits" , "fa-crosshairs", 1));
			listMenus.add(new MenuBean("utilisateurs", "Utilisateurs", "Gestion des utilisateurs" , "admin.user.work_find" , "fa-user", null, 2));
			listMenus.add(new MenuBean("profil", "Accès", "Gestion des droits profiles" , "hab.profile.work_find", "fa-key", null, 2));
		
		listMenus.add(new MenuBean("gestion-div", "Divers", "fa-random", 1));
			listMenus.add(new MenuBean("liste-enumeree", "Listes des types" , "Gestion des listes de valeurs" , "admin.valTypeEnum.work_find" , "fa-list-ol", null, 2));
			listMenus.add(new MenuBean("job", "Traitements", "Traitements asynchrones" , "admin.job.work_find" , "fa fa-flash", null, 2));
	}

	/*----------------------------------------LOAD MNU------------------------------------------------*/
	/**
	 * @return
	 */
	public static List<MenuBean> loadMenus(){
		String mnu = StrimUtil.getGlobalConfigPropertie("context.soft");
		SOFT_ENVS mnuCons = ContextAppli.SOFT_ENVS.valueOf(mnu);
		//
		List<MenuBean> listMenus = new ArrayList<>();
		listMenus.add(new MenuBean("lgo", "commun.login.disconnect", false, 1));
		// Lien libre non visible
		listMenus.add(new MenuBean("cai-web", "caisse-web.caisseWeb.work_init", false, 1));

		//
		switch (mnuCons) {
			case restau:	{ loadRestauMenus(listMenus); }; break;
			case market:	{ loadPosMenus(listMenus); }; break;
			case admin:		{ loadAdminAssocieMenus(listMenus); }; break;
		}
			
		return listMenus;
	}

	/**
	 * @param listMenus
	 */
	private static void addCaisseMenu(List<MenuBean> listMenus) {
		listMenus.add(new MenuBean("cai", "Caisse enregistreuse", "fa-barcode", 1));
			listMenus.add(new MenuBean("gestion-caisse", "Gestion", "fa fa-cogs", 2));
				listMenus.add(new MenuBean("cai-journee", "Journées", "", "caisse.journee.work_find", "fa-calendar", null, new String[]{"DETJRN","DBLCLO","SHIFT","SHIFTCL","DETHISTO"}, null, 3));
				listMenus.add(new MenuBean("cai-gestion", "Terminaux", "", "caisse.caisse.work_find", "fa fa-fax", null, 3));
			listMenus.add(new MenuBean("suivicaisse", "Infos ventes", "fa-eye", 2));
				listMenus.add(new MenuBean("cai-reduc", "Réductions", "Réductions", "caisse.caisse.find_reduction", "fa-pie-chart", null, 3));
				listMenus.add(new MenuBean("cai-ecart", "Ecarts", "Ecarts divers", "caisse.caisse.find_ecarts", "fa-pie-chart", null, 3));
				listMenus.add(new MenuBean("cai-annul", "Annulations", "Annulations commandes", "caisse.caisse.find_annulation", "fa-pie-chart", null, 3));
				listMenus.add(new MenuBean("cai-changeQte", "Réductions Qte", "Réductions Qte", "dash.dashCaisse.loadDataChangeQte", "fa-pie-chart", null, 3));
				listMenus.add(new MenuBean("cai-retour", "Retours", "Retours", "stock-caisse.mouvementStock.findRetourCmdCaisse", "fa-mail-reply-all", "tp=mn", 3));
			listMenus.add(new MenuBean("synrhesecaisse", "Synthèses ventes", "fa-eye", 2));
				listMenus.add(new MenuBean("cai-marge", "Marges", "Marges", "caisse.caisse.find_marge_vente", "fa-pie-chart", null, 3));
				listMenus.add(new MenuBean("cai-rapartition", "Répartition", "Répartition", "caisse.journee.find_repartition", "fa-pie-chart", "tp=REP", 3));
				listMenus.add(new MenuBean("cai-raz", "Raz", "Raz", "caisse.razPrint.init_raz_bo", "fa-file-text-o", null, 3));
	}

	/**
	 * @param listMenus
	 */
	private static void addLivraisonMenu(List<MenuBean> listMenus) {
		listMenus.add(new MenuBean("livr-cmd", "Cmd ligne/Livraison", "fa-motorcycle", 1));
			listMenus.add(new MenuBean("cmd-ligne", "Cmd en ligne", "fa-globe", 2));
				listMenus.add(new MenuBean("cmd-cli-ligne", "Commandes" , "Commandes" , "caisse.clientMobile.initCmdLigne" , "fa-dropbox", null, 3));
				listMenus.add(new MenuBean("cpt-ligne", "Comptes client" , "Comptes clients" , "caisse.clientMobile.initCmdLigneClient" , "fa-user-plus", null, 3));
			
			listMenus.add(new MenuBean("livr-pos", "Positions", "fa-map-marker", 2));
				listMenus.add(new MenuBean("livr-posLiv", "Position livreurs" , "Position livreurs" , "pers.societeLivr.find_position" , "fa-street-view", null, 3));
				listMenus.add(new MenuBean("livr-trjLiv", "Trajet livreurs" , "Trajet livreurs" , "pers.societeLivr.find_trajets" , "fa-street-view", null, 3));
			
			listMenus.add(new MenuBean("livr-pos-sit", "Situations", "fa-qrcode", 2));
				listMenus.add(new MenuBean("livr-mvm", "Livraisons", "Livraisons", "caisse.caisse.find_livraison", "fa-history", null, 3));
				listMenus.add(new MenuBean("livr-sitLiv", "Situation livreur", "Situation livreur", "caisse-web.caisseWeb.init_situation", "fa-tag", "curMnu=livr", 3));
				listMenus.add(new MenuBean("livr-sitSocLiv", "Situation société", "Situation société", "caisse-web.caisseWeb.init_situation", "fa-tags", "curMnu=socLivr", 3));
			
			listMenus.add(new MenuBean("livr-ref", "Référentiel", "fa-crosshairs", 2));
				listMenus.add(new MenuBean("livr-socLivr", "Société livraison" , "Société livraison" , "pers.societeLivr.work_find" , "fa-truck", null, 3));
				listMenus.add(new MenuBean("livr-livr", "Livreurs" , "Livreurs" , "admin.user.work_find" , "fa-users", "tpEmpl=LIVREUR", 3));	
	}

	/**
	 * @param listMenus
	 */
	private static void addGestionCommercialMenu(List<MenuBean> listMenus) {
		listMenus.add(new MenuBean("commercial", "Gestion Commerciale", "fa-bar-chart", 1));
			listMenus.add(new MenuBean("commercial-syn", "Synthèse clients" , "fa-line-chart", 2));
				listMenus.add(new MenuBean("commercial-client-etat", "Situation client" , "Situation client" , "pers.client.init_situation" , "fa-bar-chart" , "tp=mnu", 3));
			
			listMenus.add(new MenuBean("commercial-fidelite-carte", "Carte/portefeuille", "fa-credit-card", 2));
				listMenus.add(new MenuBean("commercial-fidelite-carte-client", "Carte fidélité" , "Cartes clients" , "fidelite.carteFideliteClient.work_find" , "fa-user-plus", null, 3));
				listMenus.add(new MenuBean("commercial-fidelite-portefeuille", "Portefeuille" , "Portefeuille" , "fidelite.portefeuille.work_find" , "fa-gift", null, 3));
						
			listMenus.add(new MenuBean("commercial-fidelite-ref", "Référentiel", "fa-crosshairs", 2));
				listMenus.add(new MenuBean("commercial-fidelite-client", "Client" , "Client" , "pers.client.work_find" , "fa-user-plus", null, 3));
				listMenus.add(new MenuBean("commercial-fidelite-offre", "Offres" , "Offres" , "pers.offre.work_find" , "fa-codepen", null, 3));
				listMenus.add(new MenuBean("commercial-fidelite-carte-param", "Cartes" , "Cartes" , "fidelite.carteFidelite.work_find" , "fa-credit-card", null, 3));
	}

	/**
	 * @param listMenus
	 */
	private static void addPaieMenu(List<MenuBean> listMenus) {
		listMenus.add(new MenuBean("paaie", "RH/Paie", "fa-group", 1));
			listMenus.add(new MenuBean("rh-paie-syn", "Synthèse paie", "fa-money", 2));
			listMenus.add(new MenuBean("empl-synthese", "Synthèse employé" , "Gestion des salairies et pointages" , "pers.pointage.loadEmployeSynthese" , "fa-users", null, 3));
			listMenus.add(new MenuBean("paie-salarie", "Pointage/Paie [Exp]" , "Gestion des salairies et pointages" , "paie.salariePaie.loadVueJour" , "fa-users", null, 3));
				
			listMenus.add(new MenuBean("paie-ref", "Référentiel", "fa-crosshairs", 2));
				listMenus.add(new MenuBean("tache-grp", "Familles employé" , "Famille employés" , "stock.famille.work_find" , "fa-sitemap", "tp=EM", 3));
				listMenus.add(new MenuBean("pers-ges_rh-employe", "Employés" , "Employé" , "pers.employe.work_find" , "fa-male", null, 3));
				listMenus.add(new MenuBean("pers-ges_rh-poste", "Postes" , "Postes" , "pers.poste.work_find" , "fa-shield", null, 3));
	}

	/**
	 * @param listMenus
	 */
	private static void addDroitMenu(List<MenuBean> listMenus) {
		listMenus.add(new MenuBean("droits-secure", "Droits", "fa-crosshairs", 1));
			listMenus.add(new MenuBean("utilisateurs", "Utilisateurs" , "Gestion des utilisateurs" , "admin.user.work_find" , "fa-user", null, 2));
			listMenus.add(new MenuBean("profil", "Accès" , "Gestion des droits profiles" , "hab.profile.work_find" , "fa-key", null, 2));
	}

	/**
	 * @param listMenus
	 */
	private static void addModuleAvanceMenu(List<MenuBean> listMenus) {
		String mnu = StrimUtil.getGlobalConfigPropertie("context.soft");
		listMenus.add(new MenuBean("fichiers-module", "Modules avancés", "fa-cog", 1));
		
		if(SOFT_ENVS.restau.toString().equals(mnu) 
				|| SOFT_ENVS.market.toString().equals(mnu) 
				){
			listMenus.add(new MenuBean("centrale-sync", "Centrale" , "Synchronisation des données" , "stock.centraleSynchro.work_find" , "fa-forumbee", null, 2));
		}
			listMenus.add(new MenuBean("fichiers-parametrage", "Paramètrage" , "Paramètrage de l'application" , "admin.parametrage.work_edit" , "fa-cogs", null, 2));
			listMenus.add(new MenuBean("liste-societe", "Etablissement" , "Etablissement" , "admin.societe.work_edit" , "fa-institution", null, 2));
			listMenus.add(new MenuBean("fichiers-liste-enumeree", "Listes des types" , "Gestion des listes de valeurs" , "admin.valTypeEnum.work_find" , "fa-list-ol", null, 2));
			listMenus.add(new MenuBean("fichiers-job", "Traitements" , "Traitements asynchrones" , "admin.job.work_find" , "fa fa-flash", null, 2));
	}

	/**
	 * @param listMenus
	 */
	private static void addStockMenu(List<MenuBean> listMenus) {
		String mnu = StrimUtil.getGlobalConfigPropertie("context.soft");
		boolean isRestau = SOFT_ENVS.restau.toString().equals(mnu);
		
		listMenus.add(new MenuBean("stock", "Stock/Mouvements", "fa-exchange", 1));
			listMenus.add(new MenuBean("vnt", "Synthèse articles" , "fa-line-chart", 2));
				listMenus.add(new MenuBean("controle-ges-synthese", "Etat stocks" , "Etat stocks" , "stock.etatStock.work_find" , "fa-bar-chart", null, 3));
				listMenus.add(new MenuBean("stock-etat_stock", "État articles" , "État articles" , "stock.mouvement.etat_article_work_find" , "fa-line-chart", null, 3));
				
			listMenus.add(new MenuBean("vnt-stck-syn", "Synthèses fournisseurs" , "fa-line-chart", 2));
				listMenus.add(new MenuBean("stock-fourn-etat", "Situation fourn." , "Situation fournisseur" , "stock.fournisseur.init_situation" , "fa-bar-chart" , "tp=mnu", 3));
			
			listMenus.add(new MenuBean("stock-achat", "Achats/Ventes", "fa-newspaper-o", 2));
				listMenus.add(new MenuBean("stock-vnt-achat", "Achats" , "Gestion des achats" , "stock.mouvement.work_find" , "fa-cart-plus", "tp=a", 3));
				listMenus.add(new MenuBean("stock-vnt-vente", "Ventes" , "Gestion des ventes" , "stock.mouvement.work_find" , "fa-cart-arrow-down" , "tp=v", 3));
				listMenus.add(new MenuBean("stock-vnt-devis", "Devis" , "Gestion des devis" , "stock.mouvement.work_find" , "fa-cart-plus" , "tp=dv", 3));
				listMenus.add(new MenuBean("stock-vnt-avoir", "Avoirs" , "Gestion des avoirs" , "stock.mouvement.work_find" , "fa-share-square-o" , "tp=av", 3));
				listMenus.add(new MenuBean("retour-client", "Retours" , "Retours des clients" , "stock.mouvement.work_find" , "fa-mail-reply-all", "tp=rt", 3));
				listMenus.add(new MenuBean("bon-cmd", "Commandes" , "Bons de commandes" , "stock.mouvement.work_find" , "fa-indent", "tp=cm", 3));
			
			listMenus.add(new MenuBean("stock-achat", "Dépense/Recette", "fa-newspaper-o", 2));
				listMenus.add(new MenuBean("stock-charges-depense", "Dépenses" , "Dépense" , "stock.chargeDivers.work_find" , "fa-exchange" , "tp=D", 3));
				listMenus.add(new MenuBean("compta-stock-charges-recette", "Recettes" , "Recette" , "stock.chargeDivers.work_find" , "fa-exchange", "tp=C", 3));
			
			listMenus.add(new MenuBean("stock-mouvement", "Mouvement stock", "fa-random", 2));
				listMenus.add(new MenuBean("stock-dem-transf", "Demande transfert" , "Demande transfert" , "stock.demandeTransfert.work_find" , "fa-share", null, 3));
				listMenus.add(new MenuBean("stock-transferts", "Transferts" , "Transferts" , "stock.mouvement.work_find" , "fa-share", "tp=t", 3));
				listMenus.add(new MenuBean("stock-transfo", "Transformation" , "Transformation" , "stock.transformation.work_find" , "fa-circle-o-notch", null, 3));
				listMenus.add(new MenuBean("stock-perte", "Pertes" , "Perte" , "stock.mouvement.work_find" , "fa-level-down", "tp=p", 3));
				listMenus.add(new MenuBean("stock-conso", "Consommations" , "Consommation" , "stock.mouvement.work_find" , "fa-upload", "tp=c", 3));
				listMenus.add(new MenuBean("stock-inventaire", "Inventaires" , "Inventaire" , "stock.inventaire.work_find" , "fa-calculator" , null, new String[]{"ECARTINV"}, null, 3));
	
			listMenus.add(new MenuBean("stock-ref", "Référentiel", "fa-crosshairs", 2));
				listMenus.add(new MenuBean("stock-composante", (isRestau ?"Composants":"Article") , (isRestau ?"Composants":"Article") , "stock.composant.work_find" , "fa-dot-circle-o" , "tp=C", 3));
				listMenus.add(new MenuBean("stock-fichecomposante", (isRestau ?"F. composants":"F. Article") , (isRestau ?"Fiche composant":"Fiche article") , "stock.composant.work_find" , "fa-dot-circle-o" , "tp=FC", 3));
				listMenus.add(new MenuBean("stock-emplacement", "Emplacements" , "Emplacement" , "stock.emplacement.work_find" , "fa-inbox", null, 3));
				listMenus.add(new MenuBean("stock-fournisseur", "Fournisseurs" , "Fournisseur" , "stock.fournisseur.work_find" , "fa-user-secret", null, 3));
				listMenus.add(new MenuBean("stock-marque", "Marques" , "Marques" , "stock.marque.work_find" , "fa fa-fw fa-compass", null, 3));
				listMenus.add(new MenuBean("stock-famille", "Familles" , "Familles" , "stock.famille.work_find" , "fa-sitemap" , "tp=ST", 3));
	}

	/**
	 * @param listMenus
	 */
	private static void addComptaMenu(List<MenuBean> listMenus) {
		listMenus.add(new MenuBean("compta", "Comptabilité", "fa-table", 1));
			listMenus.add(new MenuBean("compta-gen", "Générale", "fa-table", 2)); 
				//listMenus.add(new MenuBean("compta-tva", "Etat TVA" , "Etat de la TVA" , "dash.dashBoard2.etat_tva" , "fa-inbox", null, 3));
				listMenus.add(new MenuBean("compta-livre", "Grand livre" , "Grand livre" , "admin.compteBancaire.find_ecriture_livre" , "fa-leanpub", null, 3));
				listMenus.add(new MenuBean("compta-journal", "Journal" , "Journal de banque" , "admin.compteBancaire.find_ecriture_journal" , "fa-leanpub", null, 3));
				listMenus.add(new MenuBean("compta-cheque-fourn", "Chèques" , "Référentiel des chèques" , "stock.fournisseurCheque.work_find" , "fa-money", null, 3));
			
			listMenus.add(new MenuBean("compta-analytiaue", "Analytique", "fa-pie-chart", 2));
				listMenus.add(new MenuBean("compta-cheque-a", "Pointage chèques" , "Pointage des chèques" , "admin.compteBancaire.gestion_cheque" , "fa-money" , "tp=nonp", 3));
				listMenus.add(new MenuBean("compte-bancaire-fonds", "Mouvements fonds" , "Mouvement de fonds" , "admin.compteBancaireFonds.work_find" , "fa-mail-forward", null, 3));
			
			listMenus.add(new MenuBean("compta-ref", "Référentiel", "fa-crosshairs", 2));
				listMenus.add(new MenuBean("compta-bancaire", "Trésorerie" , "Compte bancaire" , "admin.compteBancaire.work_find" , "fa-bank", null, 3));
	}
}
