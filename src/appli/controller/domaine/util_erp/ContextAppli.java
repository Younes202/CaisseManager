package appli.controller.domaine.util_erp;

import appli.controller.domaine.administration.bean.UserBean;
import framework.model.beanContext.AbonnePersistant;
import framework.model.beanContext.AbonnementBean;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.beanContext.ExercicePersistant;
import framework.model.beanContext.SocietePersistant;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

public class ContextAppli {
//	String[] ALPHABET = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};	
	public static String[] ALPHABET = {"a","z","e","r","t","y","u","i","o","p","q","s","d","f","g","h","j","k","l","m","w","x","c","v","b","n"};
	
	
	public enum SOFT_ENVS{
		agri, 	// Agriculture
		restau, // Restaurant et fast food
		erp, 	// ERP
		market, // Points de vente
		syndic, // Syndic de copropriété
		pharma, // Pharmacie
		
		// Espace admin
		juniv, // Environnement admin Juniv
		admin // Environnement admin CaisseManager
	}
	
	public static boolean IS_CAISSE_EXIST() {
		return IS_MARKET_ENV() || IS_RESTAU_ENV() || SOFT_ENVS.pharma.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
	}
	public static boolean IS_RESTAU_ENV() {
		return SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
	}
	public static boolean IS_ERP_ENV() {
		return SOFT_ENVS.erp.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
	}
	public static boolean IS_SYNDIC_ENV() {
		return SOFT_ENVS.syndic.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
	}

	public static boolean IS_MARKET_ENV() {
		return SOFT_ENVS.market.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
	}
	public static boolean IS_CLOUD_MASTER() {
		String contextInstall = StrimUtil.getGlobalConfigPropertieIgnoreErreur("context.install");
		return "cloudMaster".equals(contextInstall);
	}
	public static boolean IS_FULL_CLOUD() {
		String instanceUrl = StrimUtil.getGlobalConfigPropertieIgnoreErreur("instance.url");
		
		return (instanceUrl != null && instanceUrl.indexOf("elogconcept") != -1) 
				&& !"juniv".equals(StrimUtil.getGlobalConfigPropertieIgnoreErreur("context.soft"));
	}
	public enum TYPE_LIGNE_COMMANDE {
	    OFFRE, GROUPE_FAMILLE, ART, MENU, GROUPE_MENU, ART_MENU, LIVRAISON, RECHARGE_PF, GARANTIE /*Recharge Portefeuille virtuel*/
	 }

	public enum SRC_CMD {
	 	MOB_ENV, 
	 	WEB_CLI,
	 	LOCAL_CLI,
	 	LOCAL,
	 	CLOUD
	}
	
	//
	public enum TYPE_ECRITURE {
		INCIDENT, VIDANGE, ASSURANCE, VIGNETTE, VISITE, CARBURANT, 
		ACHAT, AVOIR, VENTE/*vente stock*/,
		LIVRAISON, CLOTURE_ETAT, DEPENSE, RECETTE,
		PORTEFEUILLE, TRANSFERT, CLOTURE, OUVERTURE, 
		PRET, PRET_REMBOURS, REMBOURS, RETOUR,
		PAIE_SAL, ECRITURE, INITEXE, PAIE_EMPL,
		VENTE_MAR, VENTE_EMB,
		//COPRO
		PROCEDURE, APPEL_FOND, EMPRUNT_REMBOURSEMENT, EMPRUNT_MUTATION, MUTATION, FONDS_TRAVAUX, SUBVENTION,
		SUBVENTION_RECEPTION, EMPRUNT, AVANCE, SUBVENTION_AFFECTATION
	}
	public static String getLibelleEcriture(String enumValSt){
		if(StringUtil.isEmpty(enumValSt)){
			return "";
		}
		TYPE_ECRITURE enumVal = null;
		try{
			enumVal = TYPE_ECRITURE.valueOf(enumValSt);
		} catch(Exception e){
			return "?";
		}
		switch (enumVal) {
			case ASSURANCE : return "Assurance";
			case INCIDENT : return "Incident";
			case AVOIR : return "Avoir";
			case ACHAT : return "Achat";
			case VENTE : return "Vente hors caisse";
			case VIDANGE : return "Vidange";
			case VIGNETTE : return "Vignette";
			case LIVRAISON : return "Paiement livreurs";
			case CLOTURE_ETAT : return "Clôture état";
			case VISITE : return "Visite technique";
			case DEPENSE : return "Dépense";
			case RECETTE : return "Recette";
			case CARBURANT : return "Carburant";
			case PORTEFEUILLE : return "Portefeuille virtuel"; 
			case TRANSFERT : return "Transfert de fonds";
			case PRET : return "Prêt employé";
			case PRET_REMBOURS : return "Remboursement prêt";
			case PAIE_SAL : return "Paiement salaire";
			case CLOTURE : return "Clôture journée";
			case OUVERTURE : return "Ouverture journée";
		}
		return "";
	}
	
	public enum STATUT_CAISSE_MOUVEMENT_ENUM {
		TEMP("Temporaire"), 
		ANNUL("Annulée"), 
		LIVRE("Livrée"), 
		VALIDE("Validée"), 
		PREP("Préparation"), 
		PRETE("Prête"),
		TRANS("Transferation");
		
		private String libelle;
		STATUT_CAISSE_MOUVEMENT_ENUM(String libelle) {
			this.libelle = libelle;
		}
		public String getLibelle() {
			return libelle;
		}
	}
	
	public enum STATUT_LIVREUR_COMMANDE {
		EN_ATTENTE("En attente"), EN_ROUTE("En route"), LIVRE("Livrée");
		private String libelle;
		STATUT_LIVREUR_COMMANDE(String libelle) {
			this.libelle = libelle;
		}
		public String getLibelle() {
			return libelle;
		}
	}
	
	public enum TYPE_NOTIFICATION {
		ETS_CLIENT_CMD_ENROUTE, ETS_CLIENT_CMD_LIVRE,ETS_CLIENT_CMD_VALIDE;
	}
	
	public static String getLibelleStatut(String statut){
		if(StringUtil.isEmpty(statut)){
			return "";
		}
		STATUT_CAISSE_MOUVEMENT_ENUM statutE = STATUT_CAISSE_MOUVEMENT_ENUM.valueOf(statut);
		return statutE.getLibelle();
	}
	
	public enum APPLI_ENV {
		cuis("/domaine/caisse_restau/cuisine", TYPE_CAISSE_ENUM.CUISINE.toString()), 
		pres("/domaine/caisse_restau/prsentoire", TYPE_CAISSE_ENUM.PRESENTOIRE.toString()), 
		pil("/domaine/caisse_restau/pilotage", TYPE_CAISSE_ENUM.PILOTAGE.toString()), 
		affi_salle("/domaine/caisse_restau/afficheur", TYPE_CAISSE_ENUM.AFFICLIENT.toString()),
		cais_cli("/domaine/caisse_restau/client-place", TYPE_CAISSE_ENUM.CAISSE_CLIENT.toString()),
		
		affi_caisse("/domaine/caisse/afficheur", TYPE_CAISSE_ENUM.AFFICHEUR.toString()), 
		
		// Spécifique ----
		cais("/domaine/§§/normal", TYPE_CAISSE_ENUM.CAISSE.toString()), 
		cais_mob("/domaine/§§/mobile", TYPE_CAISSE_ENUM.CAISSE.toString()),
		// ----------------
		
		back(null, null), 
		
		bal("/domaine/caisse/balance", TYPE_CAISSE_ENUM.BALANCE.toString()), 
		lect("/domaine/caisse/lecteur-prix", TYPE_CAISSE_ENUM.LECTEUR.toString()),
		
		livreur("/domaine/§§/mobile", "LIVREUR"),
		synthese("/domaine/§§/mobile", "SYNTHESE"),
		utilitaire("/domaine/§§/mobile", "UTILITAIRE")
		;
		
		private String jspPath;
		private String typeEcran;

		APPLI_ENV(String jspPath, String typeEcran) {
			this.jspPath = jspPath;
			this.typeEcran = typeEcran;
		}

		public String getJspPath() {
			return jspPath;
		}
		public String getTypeEcran() {
			return typeEcran;
		}
	}
	public enum TYPE_CAISSE_ENUM {
		AFFICHEUR("Afficheur caisse"), 
		AFFICLIENT("Afficheur client"),
		
		CAISSE("Caisse tactile"), 
		CAISSE_CLIENT("Caisse client"), 
		
		
		PRESENTOIRE("Ecran présentoire"), 
		CUISINE("Ecran cuisine"), 
		PILOTAGE("Pilotage cuisine"), 
		
		BALANCE("Balance"), 
		LECTEUR("Lecteur prix"),
		
		BACKOFFICE("Back-office");
		
		private String libelle;
		
		TYPE_CAISSE_ENUM(String libelle) {
			this.libelle = libelle;
		}

		public String getLibelle() {
			return libelle;
		}
	}
	public static String getLibelleCaisse(String typeEcran){
		if(StringUtil.isEmpty(typeEcran)){
			return "";
		}
		TYPE_CAISSE_ENUM tpEcran = TYPE_CAISSE_ENUM.valueOf(typeEcran);
		return tpEcran.getLibelle();
	}
	

	public enum TYPE_COMMANDE {
		P("SUR PLACE"), E("A EMPORTER"), L("LIVRAISON");
		private String libelle;

		TYPE_COMMANDE(String libelle) {
			this.libelle = libelle;
		}

		public String getLibelle() {
			return libelle;
		}
	}
	public static String getLibelleStatus(String type){
		if(StringUtil.isEmpty(type)){
			return "";
		}
		TYPE_COMMANDE tp = TYPE_COMMANDE.valueOf(type);
		return tp.getLibelle();
	}

	public enum TYPE_MOUVEMENT_ENUM {
		v("Vente"),// Vente 
		vc("Vente caisse"),// Vente caisse 
		a("Achat"),// Achat 
		dv("Devis"),// Devis
		av("Avoir"),// Avoir 
		p("Perte"),// Perte 
		c("Consommation"),// Consommation
		cm("Commande"),// Commande
		t("Transfert"),// Transfert
		tr("Transformation"),// Transformation
		rt("Retour"),// Retour
		i("Inventaire");// Inventaire
		
		private String libelle;
		TYPE_MOUVEMENT_ENUM(String libelle) { 
			this.libelle = libelle;
		}
		public String getLibelle() {
			return libelle;
		}
	}

	public enum MODE_PAIEMENT {
		CARTE, ESPECES, CHEQUE, DEJ, CHEQUE_F, VIREMENT
	}

	public enum PARAM_APPLI_ENUM {
		TEXT_ENTETE_TICKET_1, TEXT_ENTETE_TICKET_2, ADRESSE_ETABLISSEMENT, INFORMATION_CONTACT, TEXT_PIED_TICKET, DELAIS_ALERT_CUISINE, 
		ALERT_SONOR_CUISINE, DELAIS_REFRESH_ECRAN, NBR_DECIMAL, NBR_DECIMAL_SAISIE, AROUNDI_PRIX_FOURN,CUSTOM_CALL,WIFI,
		TVA_VENTE, TVA_ACHAT, COMPTE_BANCAIRE_CAISSE, COMPTE_BANCAIRE_ESP_CAISSE, COMPTE_BANCAIRE_CARTE, COMPTE_BANCAIRE_CHEQUE,
		INFORMATION_CONTACT_MAIL, INFORMATION_CONTACT_PHONE, ICE, FRAIS_LIVRAISON, TAUX_OPTIM, SEUIL_OPTIM,
		NBR_NIVEAU_CAISSE, NBR_NIVEAU_TICKET, NUM_ARTICLE
	}

	public enum PARAM_COLOR {
        PANEL_ENETETE, PANEL_FOND, PANEL_BUTTON, PANEL_COMMANDE, PANEL_CALCULATRICE, PANEL_MENU,
        PANEL_FAMILLE, PANEL_DETAIL, PANEL_MENU_FLESH, PANEL_FAMILLE_FLESH,
        BUTTON_FAMILLE, BUTTON_SOUS_FAMILLE, BUTTON_MENU, BUTTON_SOUS_MENU, BUTTON_LIST_CHOIX, BUTTON_ARTICLE, PANEL_COMMANDE_FOND
    }

	public enum STATUT_JOURNEE {
		CLOTURE("C", "Cloturée"), OUVERTE("O", "Ouverte");

		private String statut;
		private String libelle;

		STATUT_JOURNEE(String statut, String libelle) {
			this.statut = statut;
			this.libelle = libelle;
		}

		public String getStatut() {
			return statut;
		}

		public String getLibelle() {
			return libelle;
		}

		public static String getLibelleFromStatut(String exeStatut) {
			for (STATUT_JOURNEE currStatut : STATUT_JOURNEE.values()) {
				if (currStatut.getStatut().equals(exeStatut)) {
					return currStatut.getLibelle();
				}
			}
			return STATUT_JOURNEE.OUVERTE.getLibelle();
		}
	};

	/**
	 * @return
	 */
	public static EtablissementPersistant getEtablissementBean() {
		return (EtablissementPersistant) MessageService.getGlobalMap().get("GLOBAL_ETABLISSEMENT");
	}
	public static AbonnePersistant getAbonneBean() {
		return (AbonnePersistant) MessageService.getGlobalMap().get("GLOBAL_ABONNE");
	}
	public static SocietePersistant getSocieteBean() {
		return (SocietePersistant) MessageService.getGlobalMap().get("GLOBAL_SOCIETE");
	}
	public static ExercicePersistant getExerciceBean() {
		return (ExercicePersistant) MessageService.getGlobalMap().get("GLOBAL_EXERCICE");
	}
	
	/**
	 * @return
	 */
	public static AbonnementBean getAbonementBean() {
		return (AbonnementBean) MessageService.getGlobalMap().get("ABONNEMENT_BEAN");
	}
	
	/**
	 * @return
	 */
	public static UserBean getUserBean() {
		return (UserBean) MessageService.getGlobalMap().get(ProjectConstante.SESSION_GLOBAL_USER);
	}
}
