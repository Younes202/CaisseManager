package appli.controller.domaine.administration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.servlet.http.HttpServletRequest;

import appli.controller.domaine.administration.action.LoginAction;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import appli.model.domaine.administration.persistant.ParametragePersistant;
import appli.model.domaine.administration.service.IParametrageService;
import appli.model.domaine.caisse.service.ICaisseService;
import appli.model.domaine.vente.persistant.CaissePersistant;
import framework.controller.ActionUtil;
import framework.controller.ContextGloabalAppli;
import framework.model.beanContext.AbonnementBean;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

public class ParametrageRightsConstantes {
	
	//*********************************** GLOBAL ****************************************//
	//***************************************************************************************//
	public static List<ParametragePersistant> GENERAL_GLOB_PARAMS = new ArrayList<>();
	public static List<ParametragePersistant> CAISSE_GLOB_PARAMS = new ArrayList<>();
	public static List<ParametragePersistant> CUISINE_GLOB_PARAMS = new ArrayList<>();
	
	public static List<ParametragePersistant> AFFICHEUR_SPEC_PARAMS = new ArrayList<>();
	public static List<ParametragePersistant> CAISSE_SPEC_PARAMS = new ArrayList<>();
	public static List<ParametragePersistant> CUISINE_SPEC_PARAMS = new ArrayList<>();
	public static List<ParametragePersistant> PILOTAGE_SPEC_PARAMS = new ArrayList<>();
	public static List<ParametragePersistant> PRESENTOIRE_SPEC_PARAMS = new ArrayList<>();
	public static List<ParametragePersistant> BALANCE_SPEC_PARAMS = new ArrayList<>();
	
	private static void loadParams(boolean isForce){
		if(GENERAL_GLOB_PARAMS.size() > 0 && !isForce){
			return;
		}
		
		AbonnementBean abonnementBean = ContextGloabalAppli.getAbonementBean();
		if(abonnementBean == null) {
			LoginAction.loadAbonnement();
			abonnementBean = ContextGloabalAppli.getAbonementBean(); 
		}
		
		GENERAL_GLOB_PARAMS.clear();
		CAISSE_GLOB_PARAMS.clear();
		CUISINE_GLOB_PARAMS.clear();
		
		CAISSE_SPEC_PARAMS.clear();
		CUISINE_SPEC_PARAMS.clear();
		PILOTAGE_SPEC_PARAMS.clear();
		PRESENTOIRE_SPEC_PARAMS.clear();
		BALANCE_SPEC_PARAMS.clear();
		AFFICHEUR_SPEC_PARAMS.clear();
		
		String soft = StrimUtil.getGlobalConfigPropertie("context.soft");
		boolean isRestau = SOFT_ENVS.restau.toString().equals(soft);
		boolean isMarket = SOFT_ENVS.market.toString().equals(soft);
		
		//
		GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "DECIMALES", "NBR_DECIMAL","Nombre de décimal", "Nombre de décimal à afficher dans les écrans", "NUMERIC", "2"));
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "DECIMALES", "NBR_DECIMAL_SAISIE","Nombre de décimales saisie", "Nombre de décimal autorisé lors de la saisie", "NUMERIC", "3"));
        if(BooleanUtil.isTrue(abonnementBean.isOptStock())){
        		GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "DECIMALES", "AROUNDI_PRIX_FOURN", "Arrondir offre prix achat", "Arroundir le montant des commandes employés pour ne pas avoir de virgule", "BOOLEAN", "false"));
        }
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "TVA", "TVA_VENTE", "Taux de tva de la vente", "Taux tva par défaut des ventes", "NUMERIC", "10"));
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "TVA", "TVA_ACHAT", "Taux de tva de l'achat", "Taux tva par défaut des achats", "NUMERIC", "10"));
        if(BooleanUtil.isTrue(abonnementBean.isOptCommercial())){
        	GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "TVA", "CALCUL_TVA_ETAT_FIN", "Calcul TVA état financier", "Inclure calcul TVA dans l'état financier", "BOOLEAN", "false"));
        }
		if(BooleanUtil.isTrue(abonnementBean.isOptStock())){
        	boolean isErp = "erp".equals(soft);
        	
        	GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "STOCK", "CTRL_EMPLACEMENT", "Contôler les emplacements", "Si les articles des mouvements stock ne font pas partie de la configuration de l'emplacement alors l'opération sera bloquée.", "BOOLEAN", "false"));
        	GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "STOCK", "INV_EMPL_ART", "Limiter les articles de l'inventaire à ceux configuré", "Limiter les artiles affichés lors de l'inventaire à ceux paramétrés pour l'emplacement", "BOOLEAN", "false"));
        	GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "STOCK", "CTRL_STOCK_MVM_BO", "Bloquer le mouvement si stock insuffisant vente back-office", "", "BOOLEAN", "false"));
        	GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "STOCK", "INVENTAIRE_OBLIGATOIRE_MVM", "Validation inventaire obligatoire", "Aucun mouvement ne peut être manipulé si un inventaire non validé", "BOOLEAN", ""));
        	// Vente
        	if(!isErp){
        		GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "VENTE", "MINIMUM_MARGE_CAISSE", "Minimum marge vente article caisse", "", "DECIMAL", ""));
        	}
        	GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "VENTE", "MINIMUM_MARGE_BO", "Minimum marge vente article back-office", "", "DECIMAL", ""));
        	GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "VENTE", "CONFIRM_DELETE_MVM", "Confirmation suppression mouvement", "Une confirmation du manageur sera demandée pour la suppression des mouvements", "BOOLEAN", ""));
        	GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "VENTE", "BLOCAGE_ECHEANCE_PAIE", "Blocage si dépassement échéance", "Une confirmation du manageur sera demandée si dépassement échéance paiement", "BOOLEAN", ""));
        	GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "VENTE", "VALIDITE_AVOIR", "Durée validité avoir en jours", "Ce délais passé, une confirmation manageur sera nécessaire", "NUMERIC", ""));
        	GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "VENTE", "DATE_REF_PRIX", "Mois max calcul prix moyen article", "On recalcul le prix moyen en remontant l'historique des achats sur cette période", "NUMERIC", ""));
        }
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "VENTE", "CAISSE_DETTE", "Autoriser vente à perte", "", "BOOLEAN", "1"));
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "VENTE", "CAISSE_DEPASS_DETTE", "Confirmation gérant si dépassement plafond dette", "", "BOOLEAN", "1"));
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "VENTE", "PRINT_RAZ", "Imprimante de la RAZ", "Imprimante dans laquelle, la RAZ sera imprimée", "STRING", ""));        
        
        if(BooleanUtil.isTrue(abonnementBean.isOptPlusEtsCentrale())){
        	GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BACK_OFF", "CENTRALE", "CTRL_CENTRALE", "Contrôle par la centrale", "Famille, articles, composants, ... seront piloté depuis la centrale exclusivement", "BOOLEAN", null));
        }
        
        //***************************************************************************//
		if(BooleanUtil.isTrue(abonnementBean.isSatBalance())){
			BALANCE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_BALANCE_ETQ", "ETIQUETTE BALANCE", "ETIQUETTE_BAL_PRINT", "Imprimante étiquette", "", "STRING", ""));
			BALANCE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_BALANCE_ETQ", "ETIQUETTE BALANCE", "ETIQUETTE_BAL_WIDTH", "Largeur de l'étiquette en CM", "", "DECIMAL", "8"));
			BALANCE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_BALANCE_ETQ", "ETIQUETTE BALANCE", "ETIQUETTE_BAL_HEIGHT", "Hauteur de l'étiquette en CM", "", "DECIMAL", "3.5"));
			BALANCE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_BALANCE_ETQ", "ETIQUETTE BALANCE", "ETIQUETTE_BAL_BAR_WIDTH", "Largeur du code barre", "", "DECIMAL", "7"));
			BALANCE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_BALANCE_ETQ", "ETIQUETTE BALANCE", "ETIQUETTE_BAL_BAR_HEIGHT", "Hauteur du code barre", "", "DECIMAL", "3"));
			BALANCE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_BALANCE_ETQ", "ETIQUETTE BALANCE", "ETIQUETTE_BAL_BIG_TXT", "Taille grand text", "", "NUMERIC", "11"));
			BALANCE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_BALANCE_ETQ", "ETIQUETTE BALANCE", "ETIQUETTE_BAL_XS_TXT", "Taille petit text", "", "NUMERIC", "7"));
			BALANCE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_BALANCE_ETQ", "ETIQUETTE BALANCE", "ETIQUETTE_BAL_X", "Position horizontale du texte", "Ce paramètre est utile si les ajustements de la hauteur et la largeur ne suffisent pas", "NUMERIC", ""));
			BALANCE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_BALANCE_ETQ", "ETIQUETTE BALANCE", "ETIQUETTE_BAL_Y", "Position verticale du texte", "Ce paramètre est utile si les ajustements de la hauteur et la largeur ne suffisent pas", "NUMERIC", ""));
			BALANCE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_BALANCE_ETQ", "ETIQUETTE BALANCE", "ETIQUETTE_BAL_ORIENTATION", "Orientation", "Orientation de l'étiquette", "STRING", "L"));
			
	        BALANCE_SPEC_PARAMS.add(new ParametragePersistant("BALANCE_CONF", "CONFIGURATION BALANCE", "CODE_BARRE_BALANCE", "Début code barre balance tablette", "Maximum 2 chiffres", "NUMERIC", "22"));
	        BALANCE_SPEC_PARAMS.add(new ParametragePersistant("BALANCE_CONF", "CONFIGURATION BALANCE", "BALANCE_MODE", "Mode de gestion balance", "Fonctionnement avec saisie du code généré ou impression code barre", "STRING", null));
	        BALANCE_SPEC_PARAMS.add(new ParametragePersistant("BALANCE_CONF", "CONFIGURATION BALANCE", "BALANCE_NUM", "Numéro de la balance", "Ce numéro sera ajouté au code barre", "NUMERIC", null));
	        BALANCE_SPEC_PARAMS.add(new ParametragePersistant("BALANCE_CONF", "CONFIGURATION BALANCE", "CODE_BARRE_BALANCE_COMPO", "Composition code barre balance", null, "STRING", null));
	        
	        BALANCE_SPEC_PARAMS.add(new ParametragePersistant("BALANCE_CONF", "BASE DE DONNEES BALANCE", "BALANCE_DB_TYPE", "Type", "Type base de données", "STRING", ""));
	        BALANCE_SPEC_PARAMS.add(new ParametragePersistant("BALANCE_CONF", "BASE DE DONNEES BALANCE", "BALANCE_DB_PATH", "Chemin base de données", "Cela permet de mettre à jour la balance électronique à travers sa base de données", "STRING", ""));
	        BALANCE_SPEC_PARAMS.add(new ParametragePersistant("BALANCE_CONF", "BASE DE DONNEES BALANCE", "BALANCE_DB_PORT", "Port", "", "STRING", ""));
	        BALANCE_SPEC_PARAMS.add(new ParametragePersistant("BALANCE_CONF", "BASE DE DONNEES BALANCE", "BALANCE_DB_USER", "Utilisateur", "", "STRING", ""));
	        BALANCE_SPEC_PARAMS.add(new ParametragePersistant("BALANCE_CONF", "BASE DE DONNEES BALANCE", "BALANCE_DB_PW", "Mot de passe", "", "STRING", ""));
	        BALANCE_SPEC_PARAMS.add(new ParametragePersistant("BALANCE_CONF", "BASE DE DONNEES BALANCE", "BALANCE_DB_NAME", "Nom base", "", "STRING", ""));
	        BALANCE_SPEC_PARAMS.add(new ParametragePersistant("BALANCE_CONF", "BASE DE DONNEES BALANCE", "BALANCE_EMPRESA", "Id Empresa (Dibal)", "Valable pour les balances DIBAL", "NUMERIC", ""));
		}
		
        if(isRestau && BooleanUtil.isTrue(abonnementBean.isSatCuisine())){
        	CUISINE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_PIL", "STRATEGIE ECRAN CUISINE", "ECRAN_CMD_VALIDE", "Changer statut <b>VALIDEE</b> vers <b>EN PREPARATION</b> depuis", "Source du changement du statut de la commande de VALIDE à EN PREPARATION", "STRING", null));
        	CUISINE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_PIL", "STRATEGIE ECRAN CUISINE", "ECRAN_CMD_ENPREPARATION", "Changer statut <b>EN PREPARATION</b> vers <b>PRETE</b> depuis", "Source du changement du statut de la commande de EN PREPARATION à PRETE", "STRING", null));
        	CUISINE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_PIL", "STRATEGIE ECRAN CUISINE", "ECRAN_STRATEGIE", "Comment gérer les écrans non automatiques ?", "Statégie d'affichage des commandes dans les écrans de cuisine non automatiques", "STRING", null));
        	CUISINE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_PIL", "STRATEGIE ECRAN CUISINE", "ECRAN_STATUT", "Onglets statuts à afficher dans la cuisine", "Onglets statuts commandes à afficher dans les écrans de cuisine", "STRING", null));
        	CUISINE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_PIL", "STRATEGIE ECRAN CUISINE", "ECRAN_CMD_AUTO", "Transférer automatiquement les commandes vers la cuisine en état EN PREPARATION", null, "BOOLEAN", null));
        	CUISINE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_PIL", "STRATEGIE ECRAN CUISINE", "MODE_TRAVAIL_CUISINE", "Gestion des commandes dans la cuisine", "", "STRING", ""));
			
        	CUISINE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_PIL", "TICKET DE CUISINE", "TICKET_CUIS_FONT_SMALL", "Taille petit texte ticket", null, "NUMERIC", null));//TC=Ticket ou A4=A4
        	CUISINE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_PIL", "TICKET DE CUISINE", "TICKET_CUIS_FONT_BIG", "Taille grand texte ticket", null, "NUMERIC", null));//TC=Ticket ou A4=A4
			
        	//------------------- SPEC-------------------------
			CUISINE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_PIL", "TICKET DE CUISINE", "TICKET_CUIS_NODET", "Imprimer uniquement articles paramétrés", "Limiter l'impression des articles à ceux paramétrés dans la configuration imprimante", "BOOLEAN", null));
			CUISINE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_PIL", "TICKET DE CUISINE", "TICKET_CUIS_NEW_ONLY", "Imprimer uniquement articles non préparés", "Limiter l'impression des articles à ceux qui ne sont pas déjà envoyés", "BOOLEAN", null));
			CUISINE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_PIL", "TICKET DE CUISINE", "BACKLINE_TICKET_CUIS", "Retour ligne libellé ticket", "Gérer le retout à la ligne si libellé trop grand", "NUMERIC", "45"));
			
			//
			CUISINE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_ETQ", "ETIQUETTE CUISINE", "ETIQUETTE_PRINT", "Imprimante étiquette", "", "STRING", ""));
			CUISINE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_ETQ", "ETIQUETTE CUISINE", "ETIQUETTE_WIDTH", "Largeur de l'étiquette en CM", "", "DECIMAL", "8"));
			CUISINE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_ETQ", "ETIQUETTE CUISINE", "ETIQUETTE_HEIGHT", "Hauteur de l'étiquette en CM", "", "DECIMAL", "3.5"));
			CUISINE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_ETQ", "ETIQUETTE CUISINE", "ETIQUETTE_BIG_TXT", "Taille grand text", "", "NUMERIC", "11"));
			CUISINE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_ETQ", "ETIQUETTE CUISINE", "ETIQUETTE_XS_TXT", "Taille petit text", "", "NUMERIC", "7"));
			CUISINE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_ETQ", "ETIQUETTE CUISINE", "ETIQUETTE_X", "Position horizontale du texte", "Ce paramètre est utile si les ajustements de la hauteur et la largeur ne suffisent pas", "NUMERIC", ""));
			CUISINE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_ETQ", "ETIQUETTE CUISINE", "ETIQUETTE_Y", "Position verticale du texte", "Ce paramètre est utile si les ajustements de la hauteur et la largeur ne suffisent pas", "NUMERIC", ""));
			CUISINE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_ETQ", "ETIQUETTE CUISINE", "ETIQUETTE_ORIENTATION", "Orientation", "Orientation de l'étiquette", "STRING", "L"));
			CUISINE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE_ETQ", "ETIQUETTE CUISINE", "ETIQUETTE_NBR", "Nombre par impression", "", "NUMERIC", "1"));
			
			CUISINE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE", "CONFIGURATION ECRAN CUISINE", "DELAIS_ALERT_CUISINE", "Délais alerte cuisine (min)", "Temps nécessaire pour déclencher une alerte sur la commande", "NUMERIC", "10"));
			CUISINE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE", "CONFIGURATION ECRAN CUISINE", "ALERT_SONOR_CUISINE", "Emettre une alerte sonore", "Alerte sonore en cas de dépassement du délais d'alerte cuisine", "BOOLEAN", "1"));
			CUISINE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE", "CONFIGURATION ECRAN CUISINE", "DELAIS_REFRESH_ECRAN", "Délais de rafraichissement des écrans (sec)", "Ecran cuisine et présentoire", "NUMERIC", "3"));
			
			// Pilotage
			PILOTAGE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_PIL_ETQ", "ETIQUETTE PILOTAGE", "ETIQUETTE_PRINT", "Imprimante étiquette", "", "STRING", ""));
			PILOTAGE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_PIL_ETQ", "ETIQUETTE PILOTAGE", "ETIQUETTE_WIDTH", "Largeur de l'étiquette en CM", "", "DECIMAL", "8"));
			PILOTAGE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_PIL_ETQ", "ETIQUETTE PILOTAGE", "ETIQUETTE_HEIGHT", "Hauteur de l'étiquette en CM", "", "DECIMAL", "3.5"));
			PILOTAGE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_PIL_ETQ", "ETIQUETTE PILOTAGE", "ETIQUETTE_BIG_TXT", "Taille grand text", "", "NUMERIC", "11"));
			PILOTAGE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_PIL_ETQ", "ETIQUETTE PILOTAGE", "ETIQUETTE_XS_TXT", "Taille petit text", "", "NUMERIC", "7"));
			PILOTAGE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_PIL_ETQ", "ETIQUETTE PILOTAGE", "ETIQUETTE_X", "Position horizontale du texte", "Ce paramètre est utile si les ajustements de la hauteur et la largeur ne suffisent pas", "NUMERIC", ""));
			PILOTAGE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_PIL_ETQ", "ETIQUETTE PILOTAGE", "ETIQUETTE_Y", "Position verticale du texte", "Ce paramètre est utile si les ajustements de la hauteur et la largeur ne suffisent pas", "NUMERIC", ""));
			PILOTAGE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_PIL_ETQ", "ETIQUETTE PILOTAGE", "ETIQUETTE_ORIENTATION", "Orientation", "Orientation de l'étiquette", "STRING", "L"));
			PILOTAGE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_PIL_ETQ", "ETIQUETTE PILOTAGE", "ETIQUETTE_NBR", "Nombre par impression", "", "NUMERIC", "1"));
			
			// Presentoire
			PRESENTOIRE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_PRES_ETQ", "ETIQUETTE PRESENTOIRE", "ETIQUETTE_PRINT", "Imprimante étiquette", "", "STRING", ""));
			PRESENTOIRE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_PRES_ETQ", "ETIQUETTE PRESENTOIRE", "ETIQUETTE_WIDTH", "Largeur de l'étiquette en CM", "", "DECIMAL", "8"));
			PRESENTOIRE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_PRES_ETQ", "ETIQUETTE PRESENTOIRE", "ETIQUETTE_HEIGHT", "Hauteur de l'étiquette en CM", "", "DECIMAL", "3.5"));
			PRESENTOIRE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_PRES_ETQ", "ETIQUETTE PRESENTOIRE", "ETIQUETTE_BIG_TXT", "Taille grand text", "", "NUMERIC", "11"));
			PRESENTOIRE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_PRES_ETQ", "ETIQUETTE PRESENTOIRE", "ETIQUETTE_XS_TXT", "Taille petit text", "", "NUMERIC", "7"));
			PRESENTOIRE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_PRES_ETQ", "ETIQUETTE PRESENTOIRE", "ETIQUETTE_X", "Position horizontale du texte", "Ce paramètre est utile si les ajustements de la hauteur et la largeur ne suffisent pas", "NUMERIC", ""));
			PRESENTOIRE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_PRES_ETQ", "ETIQUETTE PRESENTOIRE", "ETIQUETTE_Y", "Position verticale du texte", "Ce paramètre est utile si les ajustements de la hauteur et la largeur ne suffisent pas", "NUMERIC", ""));
			PRESENTOIRE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_PRES_ETQ", "ETIQUETTE PRESENTOIRE", "ETIQUETTE_ORIENTATION", "Orientation", "Orientation de l'étiquette", "STRING", "L"));
			PRESENTOIRE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_PRES_ETQ", "ETIQUETTE PRESENTOIRE", "ETIQUETTE_NBR", "Nombre par impression", "", "NUMERIC", "1"));
						
//			ALL_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE", "INVENTAIRE_ECRAN", "Autoriser inventaire depuis cuisine et présentoire", "Pouvoir faire l'inventaire depuis les écrans de cuisine et présentoire", "BOOLEAN", "1"));
//		    ALL_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE", "INVENTAIRE_CUISINE_DEBUT", "Inventaire cuisine début", "Obligation pour les cuisiniers de réaliser un inventaire en début d'activité", "BOOLEAN", ""));
//		    ALL_PARAMS.add(new ParametragePersistant("CAISSE_CUISINE", "INVENTAIRE_CUISINE_FIN", "Inventaire cuisine fin", "Obligation pour les cuisiniers de réaliser un inventaire en fin d'activité", "BOOLEAN", ""));
        }
		
		String[] imprimantesParams = {"ETIQUETTE_PRIX", "ETIQUETTE_BARRE"};
		for(String param : imprimantesParams){
			GENERAL_GLOB_PARAMS.add(new ParametragePersistant(param, param+"_PRINT", "IMPRIMANTE ETIQUETTE", "Imprimante étiquette", "", "STRING", ""));
		    GENERAL_GLOB_PARAMS.add(new ParametragePersistant(param, param+"_WIDTH", "IMPRIMANTE ETIQUETTE", "Largeur de l'étiquette en CM", "", "DECIMAL", "8"));
		    GENERAL_GLOB_PARAMS.add(new ParametragePersistant(param, param+"_HEIGHT","IMPRIMANTE ETIQUETTE",  "Hauteur de l'étiquette en CM", "", "DECIMAL", "3.5"));
		    if(param.equals("ETIQUETTE_BARRE")){
		    	GENERAL_GLOB_PARAMS.add(new ParametragePersistant(param, param+"_BAR_WIDTH", "IMPRIMANTE ETIQUETTE", "Largeur du code barre", "", "DECIMAL", "7"));
				GENERAL_GLOB_PARAMS.add(new ParametragePersistant(param, param+"_BAR_HEIGHT","IMPRIMANTE ETIQUETTE", "Hauteur du code barre", "", "DECIMAL", "3"));
		    } else if(param.equals("ETIQUETTE_PRIX")){
		    	GENERAL_GLOB_PARAMS.add(new ParametragePersistant(param, param+"_MAX_CARAC", "IMPRIMANTE ETIQUETTE", "Maximum caractères", "Avant retour à la ligne", "DECIMAL", ""));    	
		    }
		    GENERAL_GLOB_PARAMS.add(new ParametragePersistant(param, param+"_BIG_TXT", "IMPRIMANTE ETIQUETTE", "Taille grand text", "", "NUMERIC", "11"));
		    GENERAL_GLOB_PARAMS.add(new ParametragePersistant(param, param+"_XS_TXT", "IMPRIMANTE ETIQUETTE", "Taille petit text", "", "NUMERIC", "7"));
		    GENERAL_GLOB_PARAMS.add(new ParametragePersistant(param, param+"_X", "IMPRIMANTE ETIQUETTE", "Position horizontale du texte", "Ce paramètre est utile si les ajustements de la hauteur et la largeur ne suffisent pas", "NUMERIC", ""));
		    GENERAL_GLOB_PARAMS.add(new ParametragePersistant(param, param+"_Y", "IMPRIMANTE ETIQUETTE", "Position verticale du texte", "Ce paramètre est utile si les ajustements de la hauteur et la largeur ne suffisent pas", "NUMERIC", ""));
		    GENERAL_GLOB_PARAMS.add(new ParametragePersistant(param, param+"_ORIENTATION", "IMPRIMANTE ETIQUETTE", "Orientation", "Orientation de l'étiquette", "STRING", "L"));
		    GENERAL_GLOB_PARAMS.add(new ParametragePersistant(param, param+"_NBR", "IMPRIMANTE ETIQUETTE", "Nombre par impression", "", "NUMERIC", "1"));
		}
		
		//*******************************************************************************//
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "OPTIONS DIVERS", "NBR_NIVEAU_CAISSE", "Nombre de niveaux max dans la caisse", "", "NUMERIC", ""));
        CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "OPTIONS DIVERS", "MARGE_CAISSIER", "Activer les commission sur les articles", "Commission de vente sur les articles", "BOOLEAN", "false"));
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "OPTIONS DIVERS", "SHOW_MODE_CMD", "Afficher choix type commande", "Si on doit choisir : sur place, livraison, à emporter", "BOOLEAN", "true"));
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "OPTIONS DIVERS", "CLOSE_SELECT_PERS", "Fermer écran sélection client/serveur/livreur", "Fermer l'écran à chaque sélection", "BOOLEAN", "true"));
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "OPTIONS DIVERS", "CAISS_TYPE_CMD", "Type commande à EMPORTER par défaut", "Cela rend le type de commande non obligatoire dans la fenêtre de paiement", "BOOLEAN", "true"));		
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "OPTIONS DIVERS", "NUM_CMD_SIMPLIFIE", "chiffres/4 aléatoires dans numéro de commande", "Nombre de chiffres sur 4 à générer d'une façon aléatoire (de 0 à 3)", "NUMERIC", null));
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "OPTIONS DIVERS", "TABLE_MULTIPLE", "Autoriser plusieurs tables", "Autoriser la sélection de plusieurs tables pour une commande ", "BOOLEAN", "true"));
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "OPTIONS DIVERS", "CLAC_NBR_SHOW", "Fermer bloc quantité à chaque saisie", "Si non, alors on peut saisir des chiffres avec virgule et fermer manuellement ce bloc" , "BOOLEAN", "true"));
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "OPTIONS DIVERS", "DEVISE_TAUX", "Taux devise €", "Si saisi, alors l'option d'impression en devise sera activée dans la caisse" , "NUMERIC", null));
		
        if(isRestau){
        	CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "TOKEN D'APPEL", "SHOW_COSTOM_CALL", "Utiliser les boitier d'appel", "", "BOOLEAN", "false"));
        	CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "TOKEN D'APPEL", "CUSTOM_CALL", "Coaster call", "Saisir l'interval des numéros de coaster call disponible. Exemple 1-20", "STRING", "false"));
        }
        
        CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "INFORMATIONS HISTORIQUE", "SHOW_HISTO_ENCAISSE", "Voir commandes encaissées", "Pouvoir voir le commandes encaissées dans l'historique", "BOOLEAN", "true"));
        CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "INFORMATIONS HISTORIQUE", "PRINT_HISTO", "Impression historique", "Impresson des commandes depuis l'historique", "BOOLEAN", "true"));
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "INFORMATIONS HISTORIQUE", "SHOW_MTT_HISTO", "Voir montant commandes", "Pouvoir voir le montant des commandes dans l'historique", "BOOLEAN", "true"));
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "INFORMATIONS HISTORIQUE", "SHOW_MTT_HISTO_REPRISE", "Voir montant commandes reprise", "Pouvoir voir le montant des commandes dans reprise commande", "BOOLEAN", "true"));
		
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "SECURITE ET AUTORISATIONS", "ANNUL_CMD_ENCAISSEE", "Annulation commandes encaissées", "Pouvoir annuler les commandes encaissées depuis la caisse", "BOOLEAN", "true"));
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "SECURITE ET AUTORISATIONS", "UPD_CMD_HISTO", "Modification infos commande", "Pouvoir modifier certains détail de la commande depuis écrans de caisse, cuisine, présentoir et pilotage", "BOOLEAN", "false"));
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "SECURITE ET AUTORISATIONS", "CHIFFRE_JRN_NET", "Chiffres journée en net", "Afficher les chiffres de la journée sans le montant du portefeuille et points de fidélité", "BOOLEAN", "false"));
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "SECURITE ET AUTORISATIONS", "DEL_MNU_DET", "Pouvoir supprimer détail menu", "Pouvoir supprimer un détail d'un menu avec montant (admin et manager peuvent toujours)", "BOOLEAN", "true"));		
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "SECURITE ET AUTORISATIONS", "PRINT_FACTURE", "Imprimer facture depuis caisse", "Format A4", "BOOLEAN", "false"));
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "SECURITE ET AUTORISATIONS", "PRINT_FACTURE_PRINTER", "Imprimante facture A4", "", "STRING", ""));
		
		// Global
		if(BooleanUtil.isTrue(abonnementBean.isOptStock())){
			CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "STOCK CAISSE", "SHOW_STOCK_MVM_CAISSE", "Afficher le stock restant dans les commandes", "Contrôler les quantités restantes", "BOOLEAN", "false"));
			CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "STOCK CAISSE", "CTRL_STOCK_MVM_CAISSE", "Bloquer le mouvement si stock insuffisant vente caisse", "", "BOOLEAN", "false"));
        }
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "STOCK CAISSE", "INVENTAIRE_CAISSE", "Réaliser des inventaires articles depuis la caisse", "", "BOOLEAN", "false"));
				
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "CLOTURE ET JOURNEE", "DOUBLE_CLOTURE", "Activer la double clôture", "Re-clôturer la caisse par une deuxième personne pour confirmer les montants du caissier", "BOOLEAN", "false"));
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "CLOTURE ET JOURNEE", "DOUBLE_CLOTURE_REQ", "Double clôture obligatoire", "Bloquer la clôture de la journée si la double clôture n'a pas été effectuée", "BOOLEAN", "false"));
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "CLOTURE ET JOURNEE", "SHIFT_PASSASION", "Autoriser passation shifts", "Pouvoir reporter les commandes en attente de paiement sur le shift et le caissier suivant ", "BOOLEAN", "false"));
		GENERAL_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_ETS_CMD", "CLOTURE ET JOURNEE", "JOURNEE_AUTO", "Ouvrir et fermer journée automatiquement", "La journée s'ouvre et se ferme suivant les horaires d'ouverture et de fermeture du restaurant", "BOOLEAN", "false"));
		
		// Spécifique agenda -------------------------------------
		GENERAL_GLOB_PARAMS.add(new ParametragePersistant("AGENDA_ETS", "LIEUX TRAVAIL", "LIEU_TRAVAIL", "Lieu de tavail acteuel", "Dans le cas ou vous exercez dans divers lieux", "STRING", null));
		GENERAL_GLOB_PARAMS.add(new ParametragePersistant("AGENDA_ETS", "SPECIALITÉS", "SPECIALITE", "Spécialités de travail", "Spécialités qui permettent de vous trouver dans l'espace client", "STRING", null));
		//--------------------------------------------------------
		
        // ----------------------
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_PAIE", "MODES DE PAIEMENT", "PAIE_CHEQUE", "Chèque", "", "BOOLEAN", "true"));
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_PAIE", "MODES DE PAIEMENT", "PAIE_DEJ", "Chèque déjeuner", "", "BOOLEAN", "true"));
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_PAIE", "MODES DE PAIEMENT", "PAIE_POINT", "Point de fidélité", "", "BOOLEAN", "false"));
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_PAIE", "MODES DE PAIEMENT", "PAIE_PORTEFEUILLE", "Portefeuille", "", "BOOLEAN", "false"));
		CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_PAIE", "MODES DE PAIEMENT", "PAIE_CARTE", "Carte bancaire", "", "BOOLEAN", "true"));
        // ---------------------
        
        if(BooleanUtil.isTrue(abonnementBean.isOptCommercial())){
        	CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "POINTS DE FIDELITE ET PORTEFEUILLE", "POINTS", "Activer les points de fidelité", "Permet de gérer les points de fidelité clients", "BOOLEAN", "false"));
        	CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "POINTS DE FIDELITE ET PORTEFEUILLE", "PORTEFEUILLE", "Activer le portefeuille virtuel", "Permet de gérer les portefeuilles virtuel des clients", "BOOLEAN", "false"));
        }
        if(isRestau){
        	CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "CODE WIFI", "WIFI", "Code wifi", "Si ce code est renseigné alors il s'affichera dans le ticket de caisse", "STRING", ""));
        }
        //-----------------------------------------

        CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "ENVOI ALERTES", "MAIL_ALERT_SHIFT", "Mail notification shift", "Mail pour recevoir le rapport des shifts de toutes les caisses à leur clôture séparés par des ;", "STRING", ""));
        CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE", "ENVOI ALERTES", "MAIL_ALERT_JOUR", "Mail notification journée", "Mail pour recevoir le rapport de la journée avec ses shifts séparés par des ;", "STRING", ""));
        
        //*****************************************SPECEFIQUE*********************************************/
        CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE", "SECURITE ET AUTORISATIONS", "SECURE_DELETE_CMD", "Mot de passe manager pour annulation commande", "Demander mot de passe manager avant annulation de la commande depuis la caisse", "BOOLEAN", ""));
        CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE", "SECURITE ET AUTORISATIONS", "SECURE_DELETE_DETAIL", "Mot de passe manager pour annulation article commande", "Demander mot de passe manager avant annulation article commande depuis la caisse", "BOOLEAN", ""));
        CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE", "SECURITE ET AUTORISATIONS", "SECURE_DECREASE_QTE", "Mot de passe manager pour réduire quantité article commande", "Demander mot de passe manager avant de réduire la quantité d'un article commande depuis la caisse", "BOOLEAN", ""));
        CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE", "SECURITE ET AUTORISATIONS", "SECURE_TRANSFERT_ART", "Mot de passe manager pour transférer un élément", "Demander mot de passe manager avant de transférer des éléments", "BOOLEAN", ""));
        CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE", "SECURITE ET AUTORISATIONS", "SECURE_REDUCTION_CMD", "Mot de passe manager pour offre et réduction", "Demander mot de passe manager avant d'offrir ou réduire un élément depuis la caisse", "BOOLEAN", ""));
        CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE", "SECURITE ET AUTORISATIONS", "CAISS_AUTH_REQUIRED", "Authentification par commande sans déconnexion", "Authentification rapide aprés la validation de chaque commande", "BOOLEAN", ""));
        CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE", "SECURITE ET AUTORISATIONS", "CAISS_AUTH_REQUIRED_OUT", "Authentification par commande avec déconnexion", "Authentification login aprés la validation de chaque commande", "BOOLEAN", ""));

        if(isRestau){
	    	CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE", "SECURITE ET AUTORISATIONS", "AUTH_REQUIRED_CUIS", "Authentification par envoi cuisine sans déconnexion", "Authentification aprés l'envoi en cuisine de la commande", "BOOLEAN", ""));
	    	CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE", "SECURITE ET AUTORISATIONS", "AUTH_REQUIRED_CUIS_OUT", "Authentification par envoi cuisine avec déconnexion", "Authentification login aprés l'envoi en cuisine de la commande", "BOOLEAN", ""));
	    	
	    	CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE", "SECURITE ET AUTORISATIONS", "SELECT_TABLE_REQUIRED", "Obliger sélection table", "Cela concerne commande sur place", "BOOLEAN", ""));
	    }
        
        CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE", "OPTIONS DIVERS", "PRINT_TICKET_TEMP", "Impression ticket commandes en attente", "Autoriser l'impression du ticket de caisse des commandes en attente", "BOOLEAN", "1"));
        CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE", "OPTIONS DIVERS", "SHOW_MISE_ATT", "Ne pas afficher bouton mise en attente", "", "BOOLEAN", ""));
        CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE", "OPTIONS DIVERS", "AROUNDI_PRIX_VENTE", "Type arrondi prix de vente", "Si sélectionné, le prix de vente sera arroundi", "STRING", ""));

        CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE", "OPTIONS DIVERS", "CAISS_FILTER_COM", "Filtrer les commandes par caissier", "Afficher uniquement les commandes de ce caissier", "BOOLEAN", ""));
        CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE", "OPTIONS DIVERS", "CAISS_SELECT_DEFAUT", "Filtrer par caisse de connexion par défaut", "Les commandes seront filtrées par la caisse de connexion par défaut", "BOOLEAN", "true"));
	    
	    if(isRestau && BooleanUtil.isTrue(abonnementBean.isSatCuisine())) {
	    	CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "OPTIONS DIVERS", "TICKET_CAISSE_CUIS", "Imprimer le ticket de caisse dès que la commande est envoyée en cuisine", "", "BOOLEAN", null));
	    }
	    
		if(isRestau){
	    	CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "AFFICHAGE FONCTIONNALITES", "CAISSE_TABLE", "Tables restautant", "", "BOOLEAN", "1"));
	    }
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "AFFICHAGE FONCTIONNALITES", "CAISSE_OFFRE", "Offres et réductions", "", "BOOLEAN", "1"));
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "AFFICHAGE FONCTIONNALITES", "CAISSE_EMPLOYE", "Affectation des employés", "", "BOOLEAN", "1"));
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "AFFICHAGE FONCTIONNALITES", "CAISSE_CLIENT", "Affectation des clients", "", "BOOLEAN", "1"));
	    if(isRestau){
	    	CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "AFFICHAGE FONCTIONNALITES", "CAISSE_EMPL_SERV", "Affectation des serveurs", "", "BOOLEAN", "1"));
	    }
	    if(BooleanUtil.isTrue(abonnementBean.isOptLivraison())){
	    	CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "AFFICHAGE FONCTIONNALITES", "CAISSE_SOC_LIVR", "Affectation des société de livraison", "", "BOOLEAN", "1"));
	    	CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "AFFICHAGE FONCTIONNALITES", "CAISSE_LIVR", "Affectation d'un livreur", "", "BOOLEAN", "1"));
	    }
	    if(!isRestau){
	    	CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "AFFICHAGE FONCTIONNALITES", "RIGHT_ADD_ART", "Ajouter les articles", "", "BOOLEAN", ""));
	    }
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "AFFICHAGE FONCTIONNALITES", "RIGHT_ACHAT_ART_LIBRE", "Achat article libre", "Pouvoir effectuer des sorties en caisse avec libellé libre", "BOOLEAN", "1"));
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "AFFICHAGE FONCTIONNALITES", "RIGHT_VENTE_ART_LIBRE", "Vente article libre", "Pouvoir vendre en libre des articles non paramétrés", "BOOLEAN", "1"));
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "AFFICHAGE FONCTIONNALITES", "RIGHT_SITUATION_CLIENT", "Situation client", "Voir la situation des clients", "BOOLEAN", "1"));
	    if(BooleanUtil.isTrue(abonnementBean.isOptLivraison())){
	    	CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "AFFICHAGE FONCTIONNALITES", "RIGHT_SITUATION_SOC_LIVR", "Situation société livraison", "Voir la situation des société de livraison", "BOOLEAN", "1"));
	    }
	    if(BooleanUtil.isTrue(abonnementBean.isOptLivraison())){
	    	CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "AFFICHAGE FONCTIONNALITES", "RIGHT_SITUATION_LIVREUR", "Situation livraisons", "Voir la situation des livreurs", "BOOLEAN", "1"));
	    }
	    
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "IMPRESSION RAZ", "RIGHT_CLOSE_SHIFT", "Clôture shift", "Clôturer le shift depuis la caisse", "BOOLEAN", "1"));
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "IMPRESSION RAZ", "RIGHT_IMPRAZ_CAI", "RAZ journée", "Pour tous les caissiers", "BOOLEAN", "1"));
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "IMPRESSION RAZ", "RIGHT_IMPRAZ_EMPL_AL", "RAZ tout employé", "Pour tous les caissiers", "BOOLEAN", "1"));
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "IMPRESSION RAZ", "RIGHT_IMPRAZ_EMPL", "RAZ employé", "Pour le caissier connecté uniquement", "BOOLEAN", "1"));
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "IMPRESSION RAZ", "RIGHT_IMPRAZ_ART", "RAZ articles", "Répartition par article", "BOOLEAN", "1"));
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "IMPRESSION RAZ", "RIGHT_IMPRAZ_BOISS", "RAZ boisson", "Répartition des boisson", "BOOLEAN", "1"));
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "IMPRESSION RAZ", "RIGHT_IMPRAZ_MODE_PAIE", "RAZ mode paiement", "Répartition des modes de paiement", "BOOLEAN", "1"));
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "IMPRESSION RAZ", "RIGHT_IMPRAZ_LIVR", "RAZ livreur", "Répartition des ventes des société des livreurs", "BOOLEAN", "1"));
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "IMPRESSION RAZ", "RIGHT_IMPRAZ_GLOB", "RAZ globale", "Raz globale avec différents détails", "BOOLEAN", "1"));
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "IMPRESSION RAZ", "RIGHT_IMPRAZ_POS", "RAZ poste", "Raz poste avec différents détails", "BOOLEAN", "1"));
	    if(BooleanUtil.isTrue(abonnementBean.isOptLivraison())){
	    	CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "IMPRESSION RAZ", "RIGHT_IMPRAZ_SOC_LIVR", "RAZ société livraison", "Répartition des ventes des société des livraisons", "BOOLEAN", "1"));
	    }

	    if(isRestau || ContextAppli.SOFT_ENVS.market.toString().equals(soft)){
	    	CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "OPTIONS D'AFFICHAGE", "HAUTEUR_BLOC_FAMILLE", "Hauteur espace familles", "", "NUMERIC", "413"));
        }
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "OPTIONS D'AFFICHAGE", "AFFICHER_IMAGE_VEILLE", "Afficher image de veille", "", "BOOLEAN", "true"));
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "OPTIONS D'AFFICHAGE", "NBR_ELEMENT_PAGE_FAM", "Nombre familles par page", "", "NUMERIC", "20"));
	                                                                     
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "OPTIONS D'AFFICHAGE", "AFFICHEUR_PORT_COM", "Utiliser l'afficheur COM", "", "BOOLEAN", null));
	                                                                    
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "OPTIONS D'AFFICHAGE", "ECART_ENTETE_TICKET", "Ecart entête caisse", "Espace à laisser entre la première ligne du ticket et sa bordure supérieure", "NUMERIC", "10"));
	    CAISSE_SPEC_PARAMS.add(new ParametragePersistant("CAISSE_CONF", "OPTIONS D'AFFICHAGE", "BACKLINE_TICKET", "Retour ligne libellé ticket", "Gérer le retout à la ligne si libellé trop grand (par défaut 50)", "NUMERIC", "50"));
	    
        //**************************************************************************************/
	    if(BooleanUtil.isTrue(abonnementBean.isSatAffCaisse())) {
		    AFFICHEUR_SPEC_PARAMS.add(new ParametragePersistant("SLIDE", "ANIMATIONS AFFICHEUR", "SLIDE_TEMPO_VEILLE", "Durée mise en veille", "Durée de la mise en veille de l'afficheur", "NUMERIC", "45"));
		    AFFICHEUR_SPEC_PARAMS.add(new ParametragePersistant("SLIDE", "ANIMATIONS AFFICHEUR", "SLIDE_DURATION", "Durée animation", "", "NUMERIC", "15"));
		    AFFICHEUR_SPEC_PARAMS.add(new ParametragePersistant("SLIDE", "ANIMATIONS AFFICHEUR", "SLIDE_EFFET", "Effet animation", "", "STRING", "{$Duration:45000,$Opacity:2}"));
	    }
	    
	    GENERAL_GLOB_PARAMS.addAll(CAISSE_GLOB_PARAMS);
	    GENERAL_GLOB_PARAMS.addAll(CUISINE_GLOB_PARAMS);
	    
        //**************************************************************************************/
        // Affichage et interface -----------------------------------------------------------------------------------------
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("PANEL_COLOR", "PANNEAUX", "PANEL_ENETETE", "Bloc entête", "", "STRING", ""));
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("PANEL_COLOR", "PANNEAUX", "PANEL_COMMANDE", "Tableau des commandes", "", "STRING", ""));
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("PANEL_COLOR", "PANNEAUX", "PANEL_RIGHT", "Panneau droit", "", "STRING", ""));
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("PANEL_COLOR", "PANNEAUX", "PANEL_LEFT", "Panneau gauche", "", "STRING", ""));
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("PANEL_COLOR", "PANNEAUX", "PANEL_MENU_FAMILLE", "Panneau des menus et familles", "", "STRING", ""));
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("PANEL_COLOR", "PANNEAUX", "PANEL_DETAIL", "Panneau détail menus et familles", "", "STRING", ""));
                                                                         
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BUTTON_COLOR", "BOUTONS", "BUTTON_FAMILLE", "Bouton familles", "", "STRING", ""));
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BUTTON_COLOR", "BOUTONS", "BUTTON_SOUS_FAMILLE", "Bouton sous-familles", "", "STRING", ""));
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BUTTON_COLOR", "BOUTONS", "BUTTON_MENU", "Bouton menu", "", "STRING", ""));
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BUTTON_COLOR", "BOUTONS", "BUTTON_SOUS_MENU", "Bouton sous-menu", "", "STRING", ""));
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BUTTON_COLOR", "BOUTONS", "BUTTON_LIST_CHOIX", "Bouton liste de choix", "", "STRING", ""));
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("BUTTON_COLOR", "BOUTONS", "BUTTON_ARTICLE", "Bouton articles", "", "STRING", ""));
        
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("FONT_COLOR", "TEXTE", "COULEUR_TEXT_MENU", "Couleur text menus", "", "STRING", ""));
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("FONT_COLOR", "TEXTE", "COULEUR_TEXT_FAMILLE", "Couleur text familles", "", "STRING", ""));
        GENERAL_GLOB_PARAMS.add(new ParametragePersistant("FONT_COLOR", "TEXTE", "COULEUR_TEXT_DETAIL", "Couleur text détail", "", "STRING", ""));
                
      //**************************************************************************************/
	    CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_LIVR", "LIVRAISON", "FRAIS_LIVRAISON", "Frais de livraison", "Frais de livraison globaux (part livreur et part société). Ces frais sont ajoutés automatiquement aux commandes", "DECIMAL", ""));
	    CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_LIVR", "LIVRAISON", "FRAIS_LIVRAISON_PART", "Part frais de livraison", "Part que la société récupére des frais de livraison", "DECIMAL", ""));
	    CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_LIVR", "LIVRAISON", "LIVRAISON", "Activer le décaissement des livraisons", "Décaisser automatiquement les livraisons de la journée lors de la clôture de la journée", "BOOLEAN", ""));
	    CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_LIVR", "LIVRAISON", "LIVREUR_REQUIRED", "Sélection du livreur", "Livreur obligatoire pour la caisse livraison", "BOOLEAN", ""));
	    CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_LIVR", "LIVRAISON", "CLIENT_REQUIRED", "Sélection du client", "Client obligatoire pour la caisse livraison", "BOOLEAN", ""));
	    CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_LIVR", "LIVRAISON", "AUTH_REQUIRED", "Authentification par commande sans déconnexion", "Authentification aprés la validation de chaque commande", "BOOLEAN", ""));
	    CAISSE_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_LIVR", "LIVRAISON", "AUTH_REQUIRED_OUT", "Authentification par commande avec déconnexion", "Authentification login aprés la validation de chaque commande", "BOOLEAN", ""));

      //**************************************************************************************/
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_TICKET", "TICKET DE CAISSE", "TEXT_ENTETE_TICKET_1", "Texte entête ticket ligne 1", null, "STRING", null));
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_TICKET", "TICKET DE CAISSE", "TEXT_ENTETE_TICKET_2", "Texte entête ticket ligne 2", null, "STRING", null));
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_TICKET", "TICKET DE CAISSE", "NUM_ARTICLE", "Afficher numéro d'article", "Afficher le numéro d'ordre d'article", "BOOLEAN", null));
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_TICKET", "TICKET DE CAISSE", "COM_ARTICLE", "Ne pas afficher commentaires", "Ne pas afficher les commentaires liés aux articles dans le ticket de caisse", "BOOLEAN", null));
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_TICKET", "TICKET DE CAISSE", "ADRESSE_ETABLISSEMENT", "Afficher adresse de l'établissement", null, "BOOLEAN", null));
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_TICKET", "TICKET DE CAISSE", "ICE", "Affiche numéro ICE", null, "BOOLEAN", null));
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_TICKET", "TICKET DE CAISSE", "INFORMATION_CONTACT_PHONE", "Afficher téléphone", null, "BOOLEAN", null));
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_TICKET", "TICKET DE CAISSE", "INFORMATION_CONTACT_MAIL", "Afficher mail", null, "BOOLEAN", null));
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_TICKET", "TICKET DE CAISSE", "TEXT_PIED_TICKET", "Texte pied de page ticket", null, "STRING", "Merci pour votre achat"));
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_TICKET", "TICKET DE CAISSE", "LOGO_TICKET", "Imprimer le logo", "Imprimer le logo dans le ticket de caisse", "BOOLEAN", "1"));
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_TICKET", "TICKET DE CAISSE", "CODE_BARRE_TICKET", "Afficher code barre", "Afficher le code barre en bas du ticket. Permet de gérer les retours client", "BOOLEAN", "1"));
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_TICKET", "TICKET DE CAISSE", "NBR_NIVEAU_TICKET", "Nombre de niveaux max dans le ticket de caisse", "", "NUMERIC", ""));
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_TICKET", "TICKET DE CAISSE", "FORMAT_TICKET", "Format impression", null, "String", "TC"));//TC=Ticket ou A4=A4
	    
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_TICKET", "TICKET DA CAISSE", "TICKET_FONT_SMALL", "Taille petit texte", null, "NUMERIC", null));//TC=Ticket ou A4=A4
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("CAISSE_TICKET", "TICKET DA CAISSE", "TICKET_FONT_BIG", "Taille grand texte", null, "NUMERIC", null));//TC=Ticket ou A4=A4
	    
      //**************************************************************************************/
		// Divers optimisation -----------------------------------------------------------------------------------------
	    if(BooleanUtil.isTrue(abonnementBean.isOptPlusOptimisation())){
	    	GENERAL_GLOB_PARAMS.add(new ParametragePersistant("DIVERS_OPTIM", "OPTIMISATION", "TAUX_OPTIM", "Taux d'optimisation", "Taux d'optimisation partie espèces lors de l'impression de la RAZ", "NUMERIC", ""));
	    	GENERAL_GLOB_PARAMS.add(new ParametragePersistant("DIVERS_OPTIM", "OPTIMISATION", "SEUIL_OPTIM", "Seuil d'optimisation", "Montant du résultat à partir duquel l'optimisation va se déclencher", "NUMERIC", ""));
	    }
	   
	    // Divers compte -----------------------------------------------------------------------------------------
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("DIVERS_COMPTE", "COMPTE BANCAIRE", "COMPTE_BANCAIRE_CAISSE", "Compte bancaire espèces", "Compte à utiliser pour les espèces", "STRING", ""));
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("DIVERS_COMPTE", "COMPTE BANCAIRE", "COMPTE_BANCAIRE_ESP_CAISSE", "Compte bancaire espèces caisses", "Compte à utiliser pour les ventes caisse et commandes en ligne avec si paiement en espèces", "STRING", ""));
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("DIVERS_COMPTE", "COMPTE BANCAIRE", "COMPTE_BANCAIRE_CARTE", "Compte bancaire carte", "Compte à utiliser pour les ventes caisse et commandes en ligne avec si paiement par carte bancaire", "STRING", ""));
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("DIVERS_COMPTE", "COMPTE BANCAIRE", "COMPTE_BANCAIRE_CHEQUE", "Compte bancaire chèque", "Compte à utiliser pour les ventes caisse et commandes en ligne si avec paiement par chèque", "STRING", ""));
	    GENERAL_GLOB_PARAMS.add(new ParametragePersistant("DIVERS_COMPTE", "COMPTE BANCAIRE", "COMPTE_BANCAIRE_DEJ", "Compte bancaire chèque déjeuner", "Compte à utiliser pour les ventes caisse et commandes en ligne si avec paiement par chèque déjeuner", "STRING", ""));
	    //-----------------------------------------
	}
	
	public enum TYPE_PARAM{
		BACK_OFF("BACK_OFF"), 
		CAISSE_GLOB, 
		CAISSE_SPEC, 
		CUISINE_GLOB,
		IHM_GLOB("PANEL_COLOR", "BUTTON_COLOR", "FONT_COLOR"), 
		DIVERS_GLOB("DIVERS_OPTIM", "DIVERS_COMPTE"),
		TICKET_GLOB("CAISSE_TICKET"), 
		BALANCE_SPEC("BALANCE_CONF", "CAISSE_BALANCE_ETQ"), 
		CUISINE_SPEC("CAISSE_CUISINE", "CAISSE_CUISINE_PIL", "CAISSE_CUISINE_ETQ"), 
		AFFICHEUR_SALLE_SPEC, 
		LECTEUR_PRIX_SPEC, PILOTAGE_SPEC,
		AFFICHEUR_CAISSE_SPEC("SLIDE"), 
		CAISSE_ETS_CMD("CAISSE_ETS_CMD", "AGENDA_ETS"),
		PRESENTOIRE_SPEC, 
		CAISSE_SALLE_SPEC;
		
		private String[] groupes;
		private TYPE_PARAM(String ... groupes){
			this.groupes = groupes;
		}
		public String[] getGroupes(){
			return groupes;
		}
	}
	
	public static void setMapEffect(HttpServletRequest request){
		IParametrageService parametreService = ServiceUtil.getBusinessBean(IParametrageService.class);
		ParametragePersistant parameterDurantion = parametreService.getParameterByCode("SLIDE_DURATION");
		String duration = parameterDurantion != null ? parameterDurantion.getValeur() : null;
		duration = StringUtil.isEmpty(duration) ? "45" : duration;
		duration = ""+(Integer.valueOf(duration)*1000);
		
		Map<String, String> mapData = new HashMap<>();
		mapData.put("", "");
		mapData.put("null", "----- Fade Transitions -----");
		mapData.put("{$Duration:"+duration+",$Opacity:2}", "Fade");
		mapData.put("{$Duration:"+duration+",x:0.3,$During:{$Left:[0.3,0.7]},$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade in L");
		mapData.put("{$Duration:"+duration+",x:-0.3,$During:{$Left:[0.3,0.7]},$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade in R");
		mapData.put("{$Duration:"+duration+",y:0.3,$During:{$Top:[0.3,0.7]},$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade in T");
		mapData.put("{$Duration:"+duration+",y:-0.3,$During:{$Top:[0.3,0.7]},$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade in B");
		mapData.put("{$Duration:"+duration+",x:0.3,$Cols:2,$During:{$Left:[0.3,0.7]},$ChessMode:{$Column:3},$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade in LR");
		mapData.put("{$Duration:"+duration+",y:0.3,$Cols:2,$During:{$Top:[0.3,0.7]},$ChessMode:{$Column:12},$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade in LR Chess");
		mapData.put("{$Duration:"+duration+",y:0.3,$Rows:2,$During:{$Top:[0.3,0.7]},$ChessMode:{$Row:12},$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade in TB");
		mapData.put("{$Duration:"+duration+",x:0.3,$Rows:2,$During:{$Left:[0.3,0.7]},$ChessMode:{$Row:3},$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade in TB Chess");
		mapData.put("{$Duration:"+duration+",x:0.3,y:0.3,$Cols:2,$Rows:2,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$ChessMode:{$Column:3,$Row:12},$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade in Corners");
		mapData.put("{$Duration:"+duration+",x:0.3,$SlideOut:true,$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade out L");
		mapData.put("{$Duration:"+duration+",x:-0.3,$SlideOut:true,$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade out R");
		mapData.put("{$Duration:"+duration+",y:0.3,$SlideOut:true,$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade out T");
		mapData.put("{$Duration:"+duration+",y:-0.3,$SlideOut:true,$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade out B");
		mapData.put("{$Duration:"+duration+",x:0.3,$Cols:2,$SlideOut:true,$ChessMode:{$Column:3},$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade out LR");
		mapData.put("{$Duration:"+duration+",y:-0.3,$Cols:2,$SlideOut:true,$ChessMode:{$Column:12},$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade out LR Chess");
		mapData.put("{$Duration:"+duration+",y:0.3,$Rows:2,$SlideOut:true,$ChessMode:{$Row:12},$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade out TB");
		mapData.put("{$Duration:"+duration+",x:-0.3,$Rows:2,$SlideOut:true,$ChessMode:{$Row:3},$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade out TB Chess");
		mapData.put("{$Duration:"+duration+",x:0.3,y:0.3,$Cols:2,$Rows:2,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$SlideOut:true,$ChessMode:{$Column:3,$Row:12},$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade out Corners");
		mapData.put("{$Duration:"+duration+",x:0.3,$During:{$Left:[0.3,0.7]},$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Outside:true}", "Fade Fly in L");
		mapData.put("{$Duration:"+duration+",x:-0.3,$During:{$Left:[0.3,0.7]},$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Outside:true}", "Fade Fly in R");
		mapData.put("{$Duration:"+duration+",y:0.3,$During:{$Top:[0.3,0.7]},$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Outside:true}", "Fade Fly in T");
		mapData.put("{$Duration:"+duration+",y:-0.3,$During:{$Top:[0.3,0.7]},$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Outside:true}", "Fade Fly in B");
		mapData.put("{$Duration:"+duration+",x:0.3,$Cols:2,$During:{$Left:[0.3,0.7]},$ChessMode:{$Column:3},$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Outside:true}", "Fade Fly in LR");
		mapData.put("{$Duration:"+duration+",y:0.3,$Cols:2,$During:{$Top:[0.3,0.7]},$ChessMode:{$Column:12},$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Outside:true}", "Fade Fly in LR Chess");
		mapData.put("{$Duration:"+duration+",y:0.3,$Rows:2,$During:{$Top:[0.3,0.7]},$ChessMode:{$Row:12},$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Outside:true}", "Fade Fly in TB");
		mapData.put("{$Duration:"+duration+",x:0.3,$Rows:2,$During:{$Left:[0.3,0.7]},$ChessMode:{$Row:3},$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Outside:true}", "Fade Fly in TB Chess");
		mapData.put("{$Duration:"+duration+",x:0.3,y:0.3,$Cols:2,$Rows:2,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$ChessMode:{$Column:3,$Row:12},$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Outside:true}", "Fade Fly in Corners");
		mapData.put("{$Duration:"+duration+",x:0.3,$SlideOut:true,$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Outside:true}", "Fade Fly out L");
		mapData.put("{$Duration:"+duration+",x:-0.3,$SlideOut:true,$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Outside:true}", "Fade Fly out R");
		mapData.put("{$Duration:"+duration+",y:0.3,$SlideOut:true,$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Outside:true}", "Fade Fly out T");
		mapData.put("{$Duration:"+duration+",y:-0.3,$SlideOut:true,$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Outside:true}", "Fade Fly out B");
		mapData.put("{$Duration:"+duration+",x:0.3,$Cols:2,$SlideOut:true,$ChessMode:{$Column:3},$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Outside:true}", "Fade Fly out LR");
		mapData.put("{$Duration:"+duration+",y:0.3,$Cols:2,$SlideOut:true,$ChessMode:{$Column:12},$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Outside:true}", "Fade Fly out LR Chess");
		mapData.put("{$Duration:"+duration+",y:0.3,$Rows:2,$SlideOut:true,$ChessMode:{$Row:12},$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Outside:true}", "Fade Fly out TB");
		mapData.put("{$Duration:"+duration+",x:0.3,$Rows:2,$SlideOut:true,$ChessMode:{$Row:3},$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Outside:true}", "Fade Fly out TB Chess");
		mapData.put("{$Duration:"+duration+",x:0.3,y:0.3,$Cols:2,$Rows:2,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$SlideOut:true,$ChessMode:{$Column:3,$Row:12},$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Outside:true}", "Fade Fly out Corners");
		mapData.put("{$Duration:"+duration+",$Delay:20,$Clip:3,$Assembly:260,$Easing:{$Clip:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade Clip in H");
		mapData.put("{$Duration:"+duration+",$Delay:20,$Clip:12,$Assembly:260,$Easing:{$Clip:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade Clip in V");
		mapData.put("{$Duration:"+duration+",$Delay:20,$Clip:3,$SlideOut:true,$Assembly:260,$Easing:{$Clip:$Jease$.$OutCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade Clip out H");
		mapData.put("{$Duration:"+duration+",$Delay:20,$Clip:12,$SlideOut:true,$Assembly:260,$Easing:{$Clip:$Jease$.$OutCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Fade Clip out V");
		mapData.put("{$Duration:"+duration+",$Delay:20,$Cols:8,$Rows:4,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Assembly:2050,$Opacity:2}", "Fade Stairs");
		mapData.put("{$Duration:"+duration+",$Delay:60,$Cols:8,$Rows:4,$Opacity:2}", "Fade Random");
		mapData.put("{$Duration:"+duration+",$Delay:20,$Cols:8,$Rows:4,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Opacity:2}", "Fade Swirl");
		mapData.put("{$Duration:"+duration+",$Delay:20,$Cols:8,$Rows:4,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:260,$Opacity:2}", "Fade ZigZag");
		mapData.put("null", "----- Twins Transitions -----");
		mapData.put("{$Duration:"+duration+",$Opacity:2,$Brother:{$Duration:"+duration+",$Opacity:2}}", "Fade Twins");
		mapData.put("{$Duration:"+duration+",$Zoom:1,$Rotate:0.5,$Easing:{$Rotate:$Jease$.$InSine,$Zoom:$Jease$.$Swing},$Opacity:2,$Brother:{$Duration:"+duration+",$Zoom:11,$Rotate:-0.5,$Easing:{$Rotate:$Jease$.$InSine,$Zoom:$Jease$.$Swing},$Opacity:2,$Shift:200}}", "Rotate away");
		mapData.put("{$Duration:"+duration+",$Zoom:1,$Rotate:-0.5,$Easing:{$Rotate:$Jease$.$InSine,$Zoom:$Jease$.$Swing},$Opacity:2,$Brother:{$Duration:"+duration+",$Zoom:11,$Rotate:0.5,$Easing:{$Rotate:$Jease$.$InSine,$Zoom:$Jease$.$Swing},$Opacity:2,$Shift:200}}", "Rotate away acw");
		mapData.put("{$Duration:"+duration+",$Zoom:11,$Rotate:0.5,$Easing:{$Rotate:$Jease$.$InSine,$Zoom:$Jease$.$Swing},$Opacity:2,$Brother:{$Duration:"+duration+",$Zoom:1,$Rotate:-0.5,$Easing:{$Rotate:$Jease$.$InSine,$Zoom:$Jease$.$Swing},$Opacity:2,$Shift:200}}", "Rotate back");
		mapData.put("{$Duration:"+duration+",$Zoom:11,$Rotate:-0.5,$Easing:{$Rotate:$Jease$.$InSine,$Zoom:$Jease$.$Swing},$Opacity:2,$Brother:{$Duration:"+duration+",$Zoom:1,$Rotate:0.5,$Easing:{$Rotate:$Jease$.$InSine,$Zoom:$Jease$.$Swing},$Opacity:2,$Shift:200}}", "Rotate back acw");
		mapData.put("{$Duration:"+duration+",x:0.25,$Zoom:1.5,$Easing:{$Left:$Jease$.$InWave,$Zoom:$Jease$.$InCubic},$Opacity:2,$ZIndex:-10,$Brother:{$Duration:"+duration+",x:-0.25,$Zoom:1.5,$Easing:{$Left:$Jease$.$InWave,$Zoom:$Jease$.$InCubic},$Opacity:2,$ZIndex:-10}}", "Switch");
		mapData.put("{$Duration:"+duration+",$Zoom:11,$Rotate:1,$Easing:{$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InQuad,$Zoom:$Jease$.$Swing},$Opacity:2,$Round:{$Rotate:1},$ZIndex:-10,$Brother:{$Duration:"+duration+",$Zoom:11,$Rotate:-1,$Easing:{$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InQuad,$Zoom:$Jease$.$Swing},$Opacity:2,$Round:{$Rotate:1},$ZIndex:-10,$Shift:400}}", "Rotate Relay");
		mapData.put("{$Duration:"+duration+",$Zoom:11,$Rotate:-1,$Easing:{$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InQuad,$Zoom:$Jease$.$Swing},$Opacity:2,$Round:{$Rotate:1},$ZIndex:-10,$Brother:{$Duration:"+duration+",$Zoom:11,$Rotate:1,$Easing:{$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InQuad,$Zoom:$Jease$.$Swing},$Opacity:2,$Round:{$Rotate:1},$ZIndex:-10,$Shift:400}}", "Rotate Relay acw");
		mapData.put("{$Duration:"+duration+",x:0.5,$Cols:2,$ChessMode:{$Column:3},$Easing:{$Left:$Jease$.$InOutCubic},$Opacity:2,$Brother:{$Duration:"+duration+",$Opacity:2}}", "Doors");
		mapData.put("{$Duration:"+duration+",$Opacity:2,$Brother:{$Duration:"+duration+",x:0.5,$Cols:2,$ChessMode:{$Column:3},$Easing:{$Left:$Jease$.$InOutCubic},$Opacity:2}}", "Doors close");
		mapData.put("{$Duration:"+duration+",x:-0.3,y:0.5,$Zoom:1,$Rotate:0.1,$During:{$Left:[0.6,0.4],$Top:[0.6,0.4],$Rotate:[0.6,0.4],$Zoom:[0.6,0.4]},$Easing:{$Left:$Jease$.$InSine,$Top:$Jease$.$InSine,$Rotate:$Jease$.$InSine,$Zoom:$Jease$.$InSine},$Opacity:2,$Brother:{$Duration:"+duration+",$Zoom:11,$Rotate:-0.5,$Easing:{$Rotate:$Jease$.$InSine,$Zoom:$Jease$.$InSine},$Opacity:2}}", "Rotate in+ out-");
		mapData.put("{$Duration:"+duration+",x:-0.6,y:1,$Zoom:11,$Rotate:0.1,$During:{$Left:[0.6,0.4],$Top:[0.6,0.4],$Rotate:[0.6,0.4],$Zoom:[0.6,0.4]},$Easing:{$Left:$Jease$.$InSine,$Top:$Jease$.$InSine,$Rotate:$Jease$.$InSine,$Zoom:$Jease$.$InSine},$Opacity:2,$Brother:{$Duration:"+duration+",$Zoom:1,$Rotate:-0.5,$Easing:{$Rotate:$Jease$.$InCubic,$Zoom:$Jease$.$InSine},$Opacity:2}}", "Rotate in- ou+");
		mapData.put("{$Duration:"+duration+",x:0.3,$During:{$Left:[0.6,0.4]},$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2,$Brother:{$Duration:"+duration+",x:-0.3,$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}}", "Fly Twins");
		mapData.put("{$Duration:"+duration+",x:1,$Rows:2,$ChessMode:{$Row:3},$Easing:{$Left:$Jease$.$InOutQuart,$Opacity:$Jease$.$Linear},$Opacity:2,$Brother:{$Duration:"+duration+",x:-1,$Rows:2,$ChessMode:{$Row:3},$Easing:{$Left:$Jease$.$InOutQuart,$Opacity:$Jease$.$Linear},$Opacity:2}}", "Chess Replace TB");
		mapData.put("{$Duration:"+duration+",y:-1,$Cols:2,$ChessMode:{$Column:12},$Easing:{$Top:$Jease$.$InOutQuart,$Opacity:$Jease$.$Linear},$Opacity:2,$Brother:{$Duration:"+duration+",y:1,$Cols:2,$ChessMode:{$Column:12},$Easing:{$Top:$Jease$.$InOutQuart,$Opacity:$Jease$.$Linear},$Opacity:2}}", "Chess Replace LR");
		mapData.put("{$Duration:"+duration+",$Zoom:11,$Easing:{$Zoom:$Jease$.$InOutExpo},$Opacity:2,$Brother:{$Duration:"+duration+",$Zoom:1.5,$Easing:{$Zoom:$Jease$.$InOutExpo},$Opacity:2,$Shift:-100}}", "Zoom back");
		mapData.put("{$Duration:"+duration+",$Zoom:1.9,$Easing:{$Zoom:$Jease$.$InOutExpo},$Opacity:2,$Brother:{$Duration:"+duration+",$Zoom:11,$Easing:{$Zoom:$Jease$.$InOutExpo},$Opacity:2,$Shift:-100}}", "Zoom away");
		mapData.put("{$Duration:"+duration+",$Zoom:11,$Easing:{$Zoom:$Jease$.$InOutExpo},$Opacity:2,$Brother:{$Duration:"+duration+",$Zoom:11,$Easing:{$Zoom:$Jease$.$InOutExpo},$Opacity:2,$Shift:-100}}", "Zoom return");
		mapData.put("{$Duration:"+duration+",y:1,$Easing:{$Top:$Jease$.$InOutQuart,$Opacity:$Jease$.$Linear},$Opacity:2,$Brother:{$Duration:"+duration+",y:-1,$Easing:{$Top:$Jease$.$InOutQuart,$Opacity:$Jease$.$Linear},$Opacity:2}}", "Shift TB");
		mapData.put("{$Duration:"+duration+",x:1,$Easing:{$Left:$Jease$.$InOutQuart,$Opacity:$Jease$.$Linear},$Opacity:2,$Brother:{$Duration:"+duration+",x:-1,$Easing:{$Left:$Jease$.$InOutQuart,$Opacity:$Jease$.$Linear},$Opacity:2}}", "Shift LR");
		mapData.put("{$Duration:"+duration+",y:-1,$Easing:{$Top:$Jease$.$InOutQuart,$Opacity:$Jease$.$Linear},$Opacity:2,$ZIndex:-10,$Brother:{$Duration:"+duration+",y:-1,$Easing:{$Top:$Jease$.$InOutQuart,$Opacity:$Jease$.$Linear},$Opacity:2,$ZIndex:-10,$Shift:-100}}", "Return TB");
		mapData.put("{$Duration:"+duration+",x:1,$Delay:40,$Cols:6,$Formation:$JssorSlideshowFormations$.$FormationStraight,$Easing:{$Left:$Jease$.$InOutQuart,$Opacity:$Jease$.$Linear},$Opacity:2,$ZIndex:-10,$Brother:{$Duration:"+duration+",x:1,$Delay:40,$Cols:6,$Formation:$JssorSlideshowFormations$.$FormationStraight,$Easing:{$Left:$Jease$.$InOutQuart,$Opacity:$Jease$.$Linear},$Opacity:2,$ZIndex:-10,$Shift:-60}}", "Return LR");
		mapData.put("{$Duration:"+duration+",x:0.25,y:0.5,$Rotate:-0.1,$Easing:{$Left:$Jease$.$InQuad,$Top:$Jease$.$InQuad,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InQuad},$Opacity:2,$Brother:{$Duration:"+duration+",x:-0.1,y:-0.7,$Rotate:0.1,$Easing:{$Left:$Jease$.$InQuad,$Top:$Jease$.$InQuad,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InQuad},$Opacity:2}}", "Rotate Axis up");
		mapData.put("{$Duration:"+duration+",x:-0.1,y:-0.7,$Rotate:0.1,$During:{$Left:[0.6,0.4],$Top:[0.6,0.4],$Rotate:[0.6,0.4]},$Easing:{$Left:$Jease$.$InQuad,$Top:$Jease$.$InQuad,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InQuad},$Opacity:2,$Brother:{$Duration:"+duration+",x:0.2,y:0.5,$Rotate:-0.1,$Easing:{$Left:$Jease$.$InQuad,$Top:$Jease$.$InQuad,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InQuad},$Opacity:2}}", "Rotate Axis down");
		mapData.put("{$Duration:"+duration+",x:-0.2,$Delay:40,$Cols:12,$During:{$Left:[0.4,0.6]},$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraight,$Assembly:260,$Easing:{$Left:$Jease$.$InOutExpo,$Opacity:$Jease$.$InOutQuad},$Opacity:2,$Outside:true,$Round:{$Top:0.5},$Brother:{$Duration:"+duration+",x:0.2,$Delay:40,$Cols:12,$Formation:$JssorSlideshowFormations$.$FormationStraight,$Assembly:1028,$Easing:{$Left:$Jease$.$InOutExpo,$Opacity:$Jease$.$InOutQuad},$Opacity:2,$Round:{$Top:0.5},$Shift:-200}}", "Extrude Replace");
		mapData.put("{$Duration:"+duration+",x:0.2,$Delay:40,$Cols:12,$During:{$Left:[0.4,0.6]},$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraight,$Easing:{$Left:$Jease$.$InOutExpo,$Opacity:$Jease$.$InOutQuad},$Opacity:2,$Outside:true,$Round:{$Top:0.5},$Brother:{$Duration:"+duration+",x:0.2,$Delay:40,$Cols:12,$Formation:$JssorSlideshowFormations$.$FormationStraight,$Assembly:1028,$Easing:{$Left:$Jease$.$InOutExpo,$Opacity:$Jease$.$InOutQuad},$Opacity:2,$Round:{$Top:0.5},$Shift:-200}}", "Extrude Return");
		mapData.put("null", "----- Rotate Transitions -----");
		mapData.put("{$Duration:"+duration+",x:-1,y:2,$Rows:2,$Zoom:11,$Rotate:1,$Assembly:2049,$ChessMode:{$Row:15},$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.8}}", "Rotate VDouble+ in");
		mapData.put("{$Duration:"+duration+",x:2,y:1,$Cols:2,$Zoom:11,$Rotate:1,$Assembly:2049,$ChessMode:{$Column:15},$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.7}}", "Rotate HDouble+ in");
		mapData.put("{$Duration:"+duration+",x:-0.5,y:1,$Rows:2,$Zoom:1,$Rotate:1,$Assembly:2049,$ChessMode:{$Row:15},$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.7}}", "Rotate VDouble- in");
		mapData.put("{$Duration:"+duration+",x:0.5,y:0.3,$Cols:2,$Zoom:1,$Rotate:1,$Assembly:2049,$ChessMode:{$Column:15},$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.7}}", "Rotate HDouble- in");
		mapData.put("{$Duration:"+duration+",x:-1,y:2,$Rows:2,$Zoom:11,$Rotate:1,$SlideOut:true,$Assembly:2049,$ChessMode:{$Row:15},$Easing:{$Left:$Jease$.$InExpo,$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InExpo},$Opacity:2,$Round:{$Rotate:0.85}}", "Rotate VDouble+ out");
		mapData.put("{$Duration:"+duration+",x:4,y:2,$Cols:2,$Zoom:11,$Rotate:1,$SlideOut:true,$Assembly:2049,$ChessMode:{$Column:15},$Easing:{$Left:$Jease$.$InExpo,$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InExpo},$Opacity:2,$Round:{$Rotate:0.8}}", "Rotate HDouble+ out");
		mapData.put("{$Duration:"+duration+",x:-0.5,y:1,$Rows:2,$Zoom:1,$Rotate:1,$SlideOut:true,$Assembly:2049,$ChessMode:{$Row:15},$Easing:{$Left:$Jease$.$InExpo,$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InExpo},$Opacity:2,$Round:{$Rotate:0.7}}", "Rotate VDouble- out");
		mapData.put("{$Duration:"+duration+",x:0.5,y:0.3,$Cols:2,$Zoom:1,$Rotate:1,$SlideOut:true,$Assembly:2049,$ChessMode:{$Column:15},$Easing:{$Left:$Jease$.$InExpo,$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InExpo},$Opacity:2,$Round:{$Rotate:0.7}}", "Rotate HDouble- out");
		mapData.put("{$Duration:"+duration+",x:-4,y:2,$Rows:2,$Zoom:11,$Rotate:1,$Assembly:2049,$ChessMode:{$Row:28},$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.7}}", "Rotate VFork+ in");
		mapData.put("{$Duration:"+duration+",x:1,y:2,$Cols:2,$Zoom:11,$Rotate:1,$Assembly:2049,$ChessMode:{$Column:19},$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.8}}", "Rotate HFork+ in");
		mapData.put("{$Duration:"+duration+",x:-3,y:1,$Rows:2,$Zoom:11,$Rotate:1,$SlideOut:true,$Assembly:2049,$ChessMode:{$Row:28},$Easing:{$Left:$Jease$.$InExpo,$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InExpo},$Opacity:2,$Round:{$Rotate:0.7}}", "Rotate VFork+ out");
		mapData.put("{$Duration:"+duration+",x:1,y:2,$Cols:2,$Zoom:11,$Rotate:1,$SlideOut:true,$Assembly:2049,$ChessMode:{$Column:19},$Easing:{$Left:$Jease$.$InExpo,$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InExpo},$Opacity:2,$Round:{$Rotate:0.8}}", "Rotate HFork+ out");
		mapData.put("{$Duration:"+duration+",$Zoom:11,$Rotate:1,$Easing:{$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.7}}", "Rotate Zoom+ in");
		mapData.put("{$Duration:"+duration+",x:4,$Zoom:11,$Rotate:1,$Easing:{$Left:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.7}}", "Rotate Zoom+ in L");
		mapData.put("{$Duration:"+duration+",x:-4,$Zoom:11,$Rotate:1,$Easing:{$Left:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.7}}", "Rotate Zoom+ in R");
		mapData.put("{$Duration:"+duration+",y:4,$Zoom:11,$Rotate:1,$Easing:{$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.7}}", "Rotate Zoom+ in T");
		mapData.put("{$Duration:"+duration+",y:-4,$Zoom:11,$Rotate:1,$Easing:{$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.7}}", "Rotate Zoom+ in B");
		mapData.put("{$Duration:"+duration+",x:4,y:4,$Zoom:11,$Rotate:1,$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.7}}", "Rotate Zoom+ in TL");
		mapData.put("{$Duration:"+duration+",x:-4,y:4,$Zoom:11,$Rotate:1,$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.7}}", "Rotate Zoom+ in TR");
		mapData.put("{$Duration:"+duration+",x:4,y:-4,$Zoom:11,$Rotate:1,$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.7}}", "Rotate Zoom+ in BL");
		mapData.put("{$Duration:"+duration+",x:-4,y:-4,$Zoom:11,$Rotate:1,$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.7}}", "Rotate Zoom+ in BR");
		mapData.put("{$Duration:"+duration+",$Zoom:11,$Rotate:1,$SlideOut:true,$Easing:{$Zoom:$Jease$.$InQuint,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InQuint},$Opacity:2,$Round:{$Rotate:0.8}}", "Rotate Zoom+ out");
		mapData.put("{$Duration:"+duration+",x:4,$Zoom:11,$Rotate:1,$SlideOut:true,$Easing:{$Left:$Jease$.$InQuint,$Zoom:$Jease$.$InQuart,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InQuint},$Opacity:2,$Round:{$Rotate:0.8}}", "Rotate Zoom+ out L");
		mapData.put("{$Duration:"+duration+",x:-4,$Zoom:11,$Rotate:1,$SlideOut:true,$Easing:{$Left:$Jease$.$InQuint,$Zoom:$Jease$.$InQuart,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InQuint},$Opacity:2,$Round:{$Rotate:0.8}}", "Rotate Zoom+ out R");
		mapData.put("{$Duration:"+duration+",y:4,$Zoom:11,$Rotate:1,$SlideOut:true,$Easing:{$Top:$Jease$.$InQuint,$Zoom:$Jease$.$InQuart,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InQuint},$Opacity:2,$Round:{$Rotate:0.8}}", "Rotate Zoom+ out T");
		mapData.put("{$Duration:"+duration+",y:-4,$Zoom:11,$Rotate:1,$SlideOut:true,$Easing:{$Top:$Jease$.$InQuint,$Zoom:$Jease$.$InQuart,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InQuint},$Opacity:2,$Round:{$Rotate:0.8}}", "Rotate Zoom+ out B");
		mapData.put("{$Duration:"+duration+",x:4,y:4,$Zoom:11,$Rotate:1,$SlideOut:true,$Easing:{$Left:$Jease$.$InQuint,$Top:$Jease$.$InQuint,$Zoom:$Jease$.$InQuart,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InQuint},$Opacity:2,$Round:{$Rotate:0.8}}", "Rotate Zoom+ out TL");
		mapData.put("{$Duration:"+duration+",x:-4,y:4,$Zoom:11,$Rotate:1,$SlideOut:true,$Easing:{$Left:$Jease$.$InQuint,$Top:$Jease$.$InQuint,$Zoom:$Jease$.$InQuart,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InQuint},$Opacity:2,$Round:{$Rotate:0.8}}", "Rotate Zoom+ out TR");
		mapData.put("{$Duration:"+duration+",x:4,y:-4,$Zoom:11,$Rotate:1,$SlideOut:true,$Easing:{$Left:$Jease$.$InQuint,$Top:$Jease$.$InQuint,$Zoom:$Jease$.$InQuart,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InQuint},$Opacity:2,$Round:{$Rotate:0.8}}", "Rotate Zoom+ out BL");
		mapData.put("{$Duration:"+duration+",x:-4,y:-4,$Zoom:11,$Rotate:1,$SlideOut:true,$Easing:{$Left:$Jease$.$InQuint,$Top:$Jease$.$InQuint,$Zoom:$Jease$.$InQuart,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InQuint},$Opacity:2,$Round:{$Rotate:0.8}}", "Rotate Zoom+ out BR");
		mapData.put("{$Duration:"+duration+",$Zoom:1,$Rotate:1,$During:{$Zoom:[0.2,0.8],$Rotate:[0.2,0.8]},$Easing:{$Zoom:$Jease$.$Swing,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$Swing},$Opacity:2,$Round:{$Rotate:0.5}}", "Rotate Zoom- in");
		mapData.put("{$Duration:"+duration+",x:0.6,$Zoom:1,$Rotate:1,$During:{$Left:[0.2,0.8],$Zoom:[0.2,0.8],$Rotate:[0.2,0.8]},$Easing:$Jease$.$Swing,$Opacity:2,$Round:{$Rotate:0.5}}", "Rotate Zoom- in L");
		mapData.put("{$Duration:"+duration+",x:-0.6,$Zoom:1,$Rotate:1,$During:{$Left:[0.2,0.8],$Zoom:[0.2,0.8],$Rotate:[0.2,0.8]},$Easing:$Jease$.$Swing,$Opacity:2,$Round:{$Rotate:0.5}}", "Rotate Zoom- in R");
		mapData.put("{$Duration:"+duration+",y:0.6,$Zoom:1,$Rotate:1,$During:{$Top:[0.2,0.8],$Zoom:[0.2,0.8],$Rotate:[0.2,0.8]},$Easing:$Jease$.$Swing,$Opacity:2,$Round:{$Rotate:0.5}}", "Rotate Zoom- in T");
		mapData.put("{$Duration:"+duration+",y:-0.6,$Zoom:1,$Rotate:1,$During:{$Top:[0.2,0.8],$Zoom:[0.2,0.8],$Rotate:[0.2,0.8]},$Easing:$Jease$.$Swing,$Opacity:2,$Round:{$Rotate:0.5}}", "Rotate Zoom- in B");
		mapData.put("{$Duration:"+duration+",x:0.6,y:0.6,$Zoom:1,$Rotate:1,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8],$Zoom:[0.2,0.8],$Rotate:[0.2,0.8]},$Easing:$Jease$.$Swing,$Opacity:2,$Round:{$Rotate:0.5}}", "Rotate Zoom- in TL");
		mapData.put("{$Duration:"+duration+",x:-0.6,y:0.6,$Zoom:1,$Rotate:1,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8],$Zoom:[0.2,0.8],$Rotate:[0.2,0.8]},$Easing:$Jease$.$Swing,$Opacity:2,$Round:{$Rotate:0.5}}", "Rotate Zoom- in TR");
		mapData.put("{$Duration:"+duration+",x:0.6,y:-0.6,$Zoom:1,$Rotate:1,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8],$Zoom:[0.2,0.8],$Rotate:[0.2,0.8]},$Easing:$Jease$.$Swing,$Opacity:2,$Round:{$Rotate:0.5}}", "Rotate Zoom- in BL");
		mapData.put("{$Duration:"+duration+",x:-0.6,y:-0.6,$Zoom:1,$Rotate:1,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8],$Zoom:[0.2,0.8],$Rotate:[0.2,0.8]},$Easing:$Jease$.$Swing,$Opacity:2,$Round:{$Rotate:0.5}}", "Rotate Zoom- in BR");
		mapData.put("{$Duration:"+duration+",$Zoom:1,$Rotate:1,$SlideOut:true,$Easing:{$Opacity:$Jease$.$Linear},$Opacity:2,$Round:{$Rotate:0.5}}", "Rotate Zoom- out");
		mapData.put("{$Duration:"+duration+",x:0.5,$Zoom:1,$Rotate:1,$SlideOut:true,$Easing:{$Left:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.5}}", "Rotate Zoom- out L");
		mapData.put("{$Duration:"+duration+",x:-0.5,$Zoom:1,$Rotate:1,$SlideOut:true,$Easing:{$Left:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.5}}", "Rotate Zoom- out R");
		mapData.put("{$Duration:"+duration+",y:0.5,$Zoom:1,$Rotate:1,$SlideOut:true,$Easing:{$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.5}}", "Rotate Zoom- out T");
		mapData.put("{$Duration:"+duration+",y:-0.5,$Zoom:1,$Rotate:1,$SlideOut:true,$Easing:{$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.5}}", "Rotate Zoom- out B");
		mapData.put("{$Duration:"+duration+",x:0.5,y:0.5,$Zoom:1,$Rotate:1,$SlideOut:true,$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.5}}", "Rotate Zoom- out TL");
		mapData.put("{$Duration:"+duration+",x:-0.5,y:0.5,$Zoom:1,$Rotate:1,$SlideOut:true,$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.5}}", "Rotate Zoom- out TR");
		mapData.put("{$Duration:"+duration+",x:0.5,y:-0.5,$Zoom:1,$Rotate:1,$SlideOut:true,$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.5}}", "Rotate Zoom- out BL");
		mapData.put("{$Duration:"+duration+",x:-0.5,y:-0.5,$Zoom:1,$Rotate:1,$SlideOut:true,$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$Linear,$Rotate:$Jease$.$InCubic},$Opacity:2,$Round:{$Rotate:0.5}}", "Rotate Zoom- out BR");
		mapData.put("null", "----- Zoom Transitions -----");
		mapData.put("{$Duration:"+duration+",y:2,$Rows:2,$Zoom:11,$Assembly:2049,$ChessMode:{$Row:15},$Easing:{$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom VDouble+ in");
		mapData.put("{$Duration:"+duration+",x:4,$Cols:2,$Zoom:11,$Assembly:2049,$ChessMode:{$Column:15},$Easing:{$Left:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom HDouble+ in");
		mapData.put("{$Duration:"+duration+",y:1,$Rows:2,$Zoom:1,$Assembly:2049,$ChessMode:{$Row:15},$Easing:{$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom VDouble- in");
		mapData.put("{$Duration:"+duration+",x:0.5,$Cols:2,$Zoom:1,$Assembly:2049,$ChessMode:{$Column:15},$Easing:{$Left:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom HDouble- in");
		mapData.put("{$Duration:"+duration+",y:2,$Rows:2,$Zoom:11,$SlideOut:true,$Assembly:2049,$ChessMode:{$Row:15},$Easing:{$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom VDouble+ out");
		mapData.put("{$Duration:"+duration+",x:4,$Cols:2,$Zoom:11,$SlideOut:true,$Assembly:2049,$ChessMode:{$Column:15},$Easing:{$Left:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom HDouble+ out");
		mapData.put("{$Duration:"+duration+",y:1,$Rows:2,$Zoom:1,$SlideOut:true,$Assembly:2049,$ChessMode:{$Row:15},$Easing:{$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom VDouble- out");
		mapData.put("{$Duration:"+duration+",x:0.5,$Cols:2,$Zoom:1,$SlideOut:true,$Assembly:2049,$ChessMode:{$Column:15},$Easing:{$Left:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom HDouble- out");
		mapData.put("{$Duration:"+duration+",$Zoom:11,$Easing:{$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom+ in");
		mapData.put("{$Duration:"+duration+",x:4,$Zoom:11,$Easing:{$Left:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom+ in L");
		mapData.put("{$Duration:"+duration+",x:-4,$Zoom:11,$Easing:{$Left:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2,$Round:{$Top:2.5}}", "Zoom+ in R");
		mapData.put("{$Duration:"+duration+",y:4,$Zoom:11,$Easing:{$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom+ in T");
		mapData.put("{$Duration:"+duration+",y:-4,$Zoom:11,$Easing:{$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom+ in B");
		mapData.put("{$Duration:"+duration+",x:4,y:4,$Zoom:11,$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom+ in TL");
		mapData.put("{$Duration:"+duration+",x:-4,y:4,$Zoom:11,$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom+ in TR");
		mapData.put("{$Duration:"+duration+",x:4,y:-4,$Zoom:11,$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom+ in BL");
		mapData.put("{$Duration:"+duration+",x:-4,y:-4,$Zoom:11,$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom+ in BR");
		mapData.put("{$Duration:"+duration+",$Zoom:11,$SlideOut:true,$Easing:{$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom+ out");
		mapData.put("{$Duration:"+duration+",x:4,$Zoom:11,$SlideOut:true,$Easing:{$Left:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom+ out L");
		mapData.put("{$Duration:"+duration+",x:-4,$Zoom:11,$SlideOut:true,$Easing:{$Left:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom+ out R");
		mapData.put("{$Duration:"+duration+",y:4,$Zoom:11,$SlideOut:true,$Easing:{$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom+ out T");
		mapData.put("{$Duration:"+duration+",y:-4,$Zoom:11,$SlideOut:true,$Easing:{$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom+ out B");
		mapData.put("{$Duration:"+duration+",x:4,y:4,$Zoom:11,$SlideOut:true,$Easing:{$Left:$Jease$.$InExpo,$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom+ out TL");
		mapData.put("{$Duration:"+duration+",x:-4,y:4,$Zoom:11,$SlideOut:true,$Easing:{$Left:$Jease$.$InExpo,$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom+ out TR");
		mapData.put("{$Duration:"+duration+",x:4,y:-4,$Zoom:11,$SlideOut:true,$Easing:{$Left:$Jease$.$InExpo,$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom+ out BL");
		mapData.put("{$Duration:"+duration+",x:-4,y:-4,$Zoom:11,$SlideOut:true,$Easing:{$Left:$Jease$.$InExpo,$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom+ out BR");
		mapData.put("{$Duration:"+duration+",$Zoom:1,$Easing:{$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom- in");
		mapData.put("{$Duration:"+duration+",x:0.6,$Zoom:1,$Easing:{$Left:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom- in L");
		mapData.put("{$Duration:"+duration+",x:-0.6,$Zoom:1,$Easing:{$Left:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom- in R");
		mapData.put("{$Duration:"+duration+",y:0.6,$Zoom:1,$Easing:{$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom- in T");
		mapData.put("{$Duration:"+duration+",y:-0.6,$Zoom:1,$Easing:{$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom- in B");
		mapData.put("{$Duration:"+duration+",x:0.6,y:0.6,$Zoom:1,$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom- in TL");
		mapData.put("{$Duration:"+duration+",x:-0.6,y:0.6,$Zoom:1,$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom- in TR");
		mapData.put("{$Duration:"+duration+",x:0.6,y:-0.6,$Zoom:1,$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom- in BL");
		mapData.put("{$Duration:"+duration+",x:-0.6,y:-0.6,$Zoom:1,$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Zoom:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Zoom- in BR");
		mapData.put("{$Duration:"+duration+",$Zoom:1,$SlideOut:true,$Easing:{$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom- out");
		mapData.put("{$Duration:"+duration+",x:1,$Zoom:1,$SlideOut:true,$Easing:{$Left:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom- out L");
		mapData.put("{$Duration:"+duration+",x:-1,$Zoom:1,$SlideOut:true,$Easing:{$Left:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom- out R");
		mapData.put("{$Duration:"+duration+",y:1,$Zoom:1,$SlideOut:true,$Easing:{$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom- out T");
		mapData.put("{$Duration:"+duration+",y:-1,$Zoom:1,$SlideOut:true,$Easing:{$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom- out B");
		mapData.put("{$Duration:"+duration+",x:1,y:1,$Zoom:1,$SlideOut:true,$Easing:{$Left:$Jease$.$InExpo,$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom- out TL");
		mapData.put("{$Duration:"+duration+",x:-1,y:1,$Zoom:1,$SlideOut:true,$Easing:{$Left:$Jease$.$InExpo,$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom- out TR");
		mapData.put("{$Duration:"+duration+",x:1,y:-1,$Zoom:1,$SlideOut:true,$Easing:{$Left:$Jease$.$InExpo,$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom- out BL");
		mapData.put("{$Duration:"+duration+",x:-1,y:-1,$Zoom:1,$SlideOut:true,$Easing:{$Left:$Jease$.$InExpo,$Top:$Jease$.$InExpo,$Zoom:$Jease$.$InExpo,$Opacity:$Jease$.$Linear},$Opacity:2}", "Zoom- out BR");
		mapData.put("null", "----- Collapse Transitions -----");
		mapData.put("{$Duration:"+duration+",$Delay:12,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Assembly:2049,$Easing:$Jease$.$OutQuad}", "Collapse Stairs");
		mapData.put("{$Duration:"+duration+",$Delay:12,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Easing:$Jease$.$OutQuad}", "Collapse Swirl");
		mapData.put("{$Duration:"+duration+",$Delay:50,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationRectangleCross,$Easing:$Jease$.$OutQuad}", "Collapse Rectangle Cross");
		mapData.put("{$Duration:"+duration+",$Delay:50,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationRectangle,$Easing:$Jease$.$OutQuad}", "Collapse Rectangle");
		mapData.put("{$Duration:"+duration+",$Delay:80,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationCross,$Easing:$Jease$.$OutQuad}", "Collapse Cross");
		mapData.put("{$Duration:"+duration+",$Delay:50,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationCircle,$Assembly:2049}", "Collapse Circle");
		mapData.put("{$Duration:"+duration+",$Delay:12,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Easing:$Jease$.$OutQuad}", "Collapse ZigZag");
		mapData.put("{$Duration:"+duration+",$Delay:40,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$SlideOut:true,$Easing:$Jease$.$OutQuad}", "Collapse Random");
		mapData.put("null", "----- Expand Transitions -----");
		mapData.put("{$Duration:"+duration+",$Delay:12,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Assembly:2050,$Easing:{$Clip:$Jease$.$InSine}}", "Expand Stairs");
		mapData.put("{$Duration:"+duration+",$Delay:12,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Easing:{$Clip:$Jease$.$InSine}}", "Expand Swirl");
		mapData.put("{$Duration:"+duration+",$Delay:50,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$Formation:$JssorSlideshowFormations$.$FormationRectangleCross,$Easing:{$Clip:$Jease$.$InSine}}", "Expand Rectangle Cross");
		mapData.put("{$Duration:"+duration+",$Delay:50,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$Formation:$JssorSlideshowFormations$.$FormationRectangle,$Easing:{$Clip:$Jease$.$InSine}}", "Expand Rectangle");
		mapData.put("{$Duration:"+duration+",$Delay:80,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$Formation:$JssorSlideshowFormations$.$FormationCross,$Easing:{$Clip:$Jease$.$InSine}}", "Expand Cross");
		mapData.put("{$Duration:"+duration+",$Delay:12,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:260,$Easing:{$Clip:$Jease$.$InSine}}", "Expand ZigZag");
		mapData.put("{$Duration:"+duration+",$Delay:40,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$Easing:$Jease$.$InSine}", "Expand Random");
		mapData.put("null", "----- Float Transitions -----");
		mapData.put("{$Duration:"+duration+",x:-1,$Delay:40,$Cols:10,$Rows:5,$SlideOut:true,$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Float Right Random");
		mapData.put("{$Duration:"+duration+",y:1,$Delay:40,$Cols:10,$Rows:5,$SlideOut:true,$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Float up Random");
		mapData.put("{$Duration:"+duration+",x:1,y:-1,$Delay:40,$Cols:10,$Rows:5,$SlideOut:true,$ChessMode:{$Column:3,$Row:12},$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Float up Random with Chess");
		mapData.put("{$Duration:"+duration+",x:-1,$Delay:12,$Cols:10,$Rows:5,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:513,$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Float Right ZigZag");
		mapData.put("{$Duration:"+duration+",y:1,$Delay:12,$Cols:10,$Rows:5,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:264,$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Float up ZigZag");
		mapData.put("{$Duration:"+duration+",x:-1,y:-1,$Delay:12,$Cols:10,$Rows:5,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:1028,$ChessMode:{$Column:3,$Row:12},$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Float up ZigZag with Chess");
		mapData.put("{$Duration:"+duration+",x:-1,$Delay:12,$Cols:10,$Rows:5,$SlideOut:true,$Reverse:true,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:513,$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Float Right Swirl");
		mapData.put("{$Duration:"+duration+",y:1,$Delay:12,$Cols:10,$Rows:5,$SlideOut:true,$Reverse:true,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:2049,$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Float up Swirl");
		mapData.put("{$Duration:"+duration+",x:1,y:1,$Delay:12,$Cols:10,$Rows:5,$SlideOut:true,$Reverse:true,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:513,$ChessMode:{$Column:3,$Row:12},$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Float	up Swirl with Chess");
		mapData.put("null", "----- Fly Transitions -----");
		mapData.put("{$Duration:"+duration+",x:1,$Delay:40,$Cols:10,$Rows:5,$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Fly Right Random");
		mapData.put("{$Duration:"+duration+",y:-1,$Delay:40,$Cols:10,$Rows:5,$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Fly up Random");
		mapData.put("{$Duration:"+duration+",x:-1,y:1,$Delay:40,$Cols:10,$Rows:5,$ChessMode:{$Column:3,$Row:12},$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Fly up Random with Chess");
		mapData.put("{$Duration:"+duration+",x:1,$Delay:12,$Cols:10,$Rows:5,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:514,$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Fly Right ZigZag");
		mapData.put("{$Duration:"+duration+",y:-1,$Delay:12,$Cols:10,$Rows:5,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:260,$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Fly up ZigZag");
		mapData.put("{$Duration:"+duration+",x:1,y:1,$Delay:12,$Cols:10,$Rows:5,$Reverse:true,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:260,$ChessMode:{$Column:3,$Row:12},$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Fly up ZigZag with Chess");
		mapData.put("{$Duration:"+duration+",x:1,$Delay:12,$Cols:10,$Rows:5,$Reverse:true,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:513,$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Fly Right Swirl");
		mapData.put("{$Duration:"+duration+",y:-1,$Delay:12,$Cols:10,$Rows:5,$Reverse:true,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:2049,$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Fly up Swirl");
		mapData.put("{$Duration:"+duration+",x:-1,y:-1,$Delay:12,$Cols:10,$Rows:5,$Reverse:true,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:513,$ChessMode:{$Column:3,$Row:12},$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Fly up Swirl with Chess");
		mapData.put("null", "----- Stripe Transitions -----");
		mapData.put("{$Duration:"+duration+",y:-1,$Delay:40,$Cols:24,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraight,$Easing:$Jease$.$OutJump,$Round:{$Top:1.5}}", "Dominoes Stripe");
		mapData.put("{$Duration:"+duration+",x:-0.2,$Delay:20,$Cols:16,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraight,$Assembly:260,$Easing:{$Left:$Jease$.$InOutExpo,$Opacity:$Jease$.$InOutQuad},$Opacity:2,$Outside:true,$Round:{$Top:0.5}}", "Extrude out Stripe");
		mapData.put("{$Duration:"+duration+",x:0.2,$Delay:20,$Cols:16,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Assembly:260,$Easing:{$Left:$Jease$.$InOutExpo,$Opacity:$Jease$.$InOutQuad},$Opacity:2,$Outside:true,$Round:{$Top:0.5}}", "Extrude in Stripe");
		mapData.put("{$Duration:"+duration+",$Delay:50,$Rows:7,$Clip:4,$Formation:$JssorSlideshowFormations$.$FormationStraight}", "Horizontal Blind Stripe");
		mapData.put("{$Duration:"+duration+",$Delay:50,$Cols:10,$Clip:2,$Formation:$JssorSlideshowFormations$.$FormationStraight}", "Vertical Blind Stripe");
		mapData.put("{$Duration:"+duration+",$Rows:6,$Clip:4}", "Horizontal Stripe");
		mapData.put("{$Duration:"+duration+",$Cols:8,$Clip:1}", "Vertical Stripe");
		mapData.put("{$Duration:"+duration+",$Rows:6,$Clip:4,$Move:true}", "Horizontal Moving Stripe");
		mapData.put("{$Duration:"+duration+",$Cols:8,$Clip:1,$Move:true}", "Vertical Moving Stripe");
		mapData.put("{$Duration:"+duration+",$Delay:40,$Rows:10,$Formation:$JssorSlideshowFormations$.$FormationStraight,$Opacity:2,$Assembly:260}", "Horizontal Fade Stripe");
		mapData.put("{$Duration:"+duration+",$Delay:40,$Rows:10,$Formation:$JssorSlideshowFormations$.$FormationStraight,$Opacity:2}", "Horizontal Fade Stripe Reverse");
		mapData.put("{$Duration:"+duration+",$Delay:40,$Cols:16,$Formation:$JssorSlideshowFormations$.$FormationStraight,$Opacity:2,$Assembly:260}", "Vertical Fade Stripe");
		mapData.put("{$Duration:"+duration+",$Delay:40,$Cols:16,$Formation:$JssorSlideshowFormations$.$FormationStraight,$Opacity:2}", "Vertical Fade Stripe Reverse");
		mapData.put("{$Duration:"+duration+",x:1,$Delay:50,$Rows:8,$Formation:$JssorSlideshowFormations$.$FormationStraight,$Assembly:513,$Easing:{$Left:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Horizontal Fly Stripe");
		mapData.put("{$Duration:"+duration+",y:1,$Delay:50,$Cols:12,$Formation:$JssorSlideshowFormations$.$FormationStraight,$Assembly:513,$Easing:{$Top:$Jease$.$InCubic,$Opacity:$Jease$.$OutQuad},$Opacity:2}", "Vertical Fly Stripe");
		mapData.put("{$Duration:"+duration+",x:-1,$Rows:10,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationStraight,$ChessMode:{$Row:3},$Easing:$Jease$.$InCubic}", "Horizontal Chess Stripe");
		mapData.put("{$Duration:"+duration+",y:-1,$Cols:12,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationStraight,$ChessMode:{$Column:12},$Easing:$Jease$.$InCubic}", "Vertical Chess Stripe");
		mapData.put("{$Duration:"+duration+",$Delay:40,$Rows:10,$Opacity:2}", "Horizontal Random Fade Stripe");
		mapData.put("{$Duration:"+duration+",$Delay:40,$Cols:16,$Opacity:2}", "Vertical Random Fade Stripe");
		mapData.put("{$Duration:"+duration+",$Delay:40,$Rows:10,$Clip:8,$Move:true,$Formation:$JssorSlideshowFormations$.$FormationCircle,$Assembly:264,$Easing:$Jease$.$InBounce}", "Horizontal Bounce Stripe");
		mapData.put("{$Duration:"+duration+",$Delay:40,$Cols:16,$Clip:1,$Move:true,$Formation:$JssorSlideshowFormations$.$FormationCircle,$Assembly:264,$Easing:$Jease$.$InBounce}", "Vertical Bounce Stripe");
		mapData.put("null", "----- Parabola Transitions -----");
		mapData.put("{$Duration:"+duration+",x:-1,y:1,$Delay:50,$Cols:10,$Rows:5,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:264,$Easing:{$Top:$Jease$.$InQuart,$Opacity:$Jease$.$Linear}}", "Parabola Swirl in");
		mapData.put("{$Duration:"+duration+",x:-1,y:1,$Delay:50,$Cols:10,$Rows:5,$Opacity:2,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:264,$Easing:{$Top:$Jease$.$InQuart,$Opacity:$Jease$.$Linear}}", "Parabola Swirl out");
		mapData.put("{$Duration:"+duration+",x:-1,y:1,$Delay:50,$Cols:10,$Rows:5,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:260,$ChessMode:{$Row:3},$Easing:{$Top:$Jease$.$InQuart,$Opacity:$Jease$.$Linear}}", "Parabola ZigZag in");
		mapData.put("{$Duration:"+duration+",x:-1,y:1,$Delay:50,$Cols:10,$Rows:5,$Opacity:2,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:260,$ChessMode:{$Row:3},$Easing:{$Top:$Jease$.$InQuart,$Opacity:$Jease$.$Linear}}", "Parabola ZigZag out");
		mapData.put("{$Duration:"+duration+",x:-1,y:1,$Delay:50,$Cols:10,$Rows:5,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Easing:{$Left:$Jease$.$InQuart,$Top:$Jease$.$InQuart,$Opacity:$Jease$.$Linear}}", "Parabola Stairs in");
		mapData.put("{$Duration:"+duration+",x:-1,y:1,$Delay:50,$Cols:10,$Rows:5,$Opacity:2,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Easing:{$Left:$Jease$.$InQuart,$Top:$Jease$.$InQuart,$Opacity:$Jease$.$Linear}}", "Parabola Stairs out");
		mapData.put("null", "----- Swing Inside Transitions -----");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:16,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Assembly:260,$Easing:{$Left:$Jease$.$InWave,$Top:$Jease$.$InWave,$Clip:$Jease$.$OutQuad},$Round:{$Left:1.3,$Top:2.5}}", "Swing Inside in Stairs");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:12,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:260,$Easing:{$Left:$Jease$.$InWave,$Top:$Jease$.$InWave,$Clip:$Jease$.$OutQuad},$Round:{$Left:1.3,$Top:2.5}}", "Swing Inside in ZigZag");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:12,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:260,$Easing:{$Left:$Jease$.$InWave,$Top:$Jease$.$InWave,$Clip:$Jease$.$OutQuad},$Round:{$Left:1.3,$Top:2.5}}", "Swing Inside in Swirl");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:40,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$Easing:{$Left:$Jease$.$InWave,$Top:$Jease$.$InWave,$Clip:$Jease$.$OutQuad},$Round:{$Left:1.3,$Top:2.5}}", "Swing Inside in Random");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:40,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$ChessMode:{$Column:3,$Row:3},$Easing:{$Left:$Jease$.$InWave,$Top:$Jease$.$InWave,$Clip:$Jease$.$OutQuad},$Round:{$Left:1.3,$Top:2.5}}", "Swing Inside in Random Chess");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:12,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:260,$Easing:{$Left:$Jease$.$InWave,$Top:$Jease$.$InWave,$Clip:$Jease$.$OutQuad},$Round:{$Left:1.3,$Top:2.5}}", "Swing Inside out ZigZag");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:12,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:260,$Easing:{$Left:$Jease$.$InWave,$Top:$Jease$.$InWave,$Clip:$Jease$.$OutQuad},$Round:{$Left:1.3,$Top:2.5}}", "Swing Inside out Swirl");
		mapData.put("null", "----- Dodge Dance Inside Transitions -----");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8]},$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Assembly:260,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$OutQuad},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Dance Inside in Stairs");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8]},$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:260,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$OutQuad},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Dance Inside in Swirl");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8]},$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:260,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$OutQuad},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Dance Inside in ZigZag");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:80,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$OutQuad},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Dance Inside in Random");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:80,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$ChessMode:{$Column:15,$Row:15},$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$OutQuad},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Dance Inside in Random Chess");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.1,0.9],$Top:[0.1,0.9]},$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Assembly:260,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$OutQuad},$Round:{$Left:0.8,$Top:2.5}}", "Dodge		Dance Inside out Stairs");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.1,0.9],$Top:[0.1,0.9]},$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:260,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$OutQuad},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Dance Inside out Swirl");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.1,0.9],$Top:[0.1,0.9]},$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:260,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$OutQuad},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Dance Inside out ZigZag");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:80,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$SlideOut:true,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$OutQuad},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Dance Inside out Random");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:80,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$SlideOut:true,$ChessMode:{$Column:15,$Row:15},$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$OutQuad},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Dance Inside out Random Chess");
		mapData.put("null", "----- Dodge Pet Inside Transitions -----");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Assembly:260,$Easing:{$Left:$Jease$.$InWave,$Top:$Jease$.$InWave,$Clip:$Jease$.$OutQuad},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Pet Inside in Stairs");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:260,$Easing:{$Left:$Jease$.$InWave,$Top:$Jease$.$InWave,$Clip:$Jease$.$OutQuad},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Pet Inside in Swirl");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:260,$Easing:{$Left:$Jease$.$InWave,$Top:$Jease$.$InWave,$Clip:$Jease$.$OutQuad},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Pet Inside in ZigZag");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:80,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8]},$Easing:{$Left:$Jease$.$InWave,$Top:$Jease$.$InWave,$Clip:$Jease$.$Linear},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Pet Inside in Random");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:80,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8]},$ChessMode:{$Column:15,$Row:15},$Easing:{$Left:$Jease$.$InWave,$Top:$Jease$.$InWave,$Clip:$Jease$.$Linear},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Pet Inside in Random Chess");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Assembly:260,$Easing:{$Left:$Jease$.$InWave,$Top:$Jease$.$InWave,$Clip:$Jease$.$OutQuad},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Pet Inside out Stairs");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:260,$Easing:{$Left:$Jease$.$InWave,$Top:$Jease$.$InWave,$Clip:$Jease$.$OutQuad},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Pet Inside out Swirl");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:260,$Easing:{$Left:$Jease$.$InWave,$Top:$Jease$.$InWave,$Clip:$Jease$.$OutQuad},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Pet Inside out ZigZag");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:80,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8]},$SlideOut:true,$Easing:{$Left:$Jease$.$InWave,$Top:$Jease$.$InWave,$Clip:$Jease$.$Linear},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Pet Inside out Random");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:80,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8]},$SlideOut:true,$ChessMode:{$Column:15,$Row:15},$Easing:{$Left:$Jease$.$InWave,$Top:$Jease$.$InWave,$Clip:$Jease$.$Linear},$Round:{$Left:0.8,$Top:2.5}}", "Dodge Pet Inside out Random Chess");
		mapData.put("null", "----- Dodge Inside Transitions -----");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8]},$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Assembly:260,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$Swing},$Round:{$Left:0.8,$Top:0.8}}", "Dodge	Inside out Stairs");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8]},$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:260,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$Swing},$Round:{$Left:0.8,$Top:0.8}}", "Dodge Inside out Swirl");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8]},$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:260,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$Swing},$Round:{$Left:0.8,$Top:0.8}}", "Dodge Inside out ZigZag");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:40,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8]},$SlideOut:true,$Assembly:260,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$Swing},$Round:{$Left:0.8,$Top:0.8}}", "Dodge Inside out Random");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:40,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8]},$SlideOut:true,$Assembly:260,$ChessMode:{$Column:15,$Row:15},$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$Swing},$Round:{$Left:0.8,$Top:0.8}}", "Dodge Inside out Random Chess");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8]},$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Assembly:260,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$Swing},$Round:{$Left:0.8,$Top:0.8}}", "Dodge Inside in Stairs");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8]},$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:260,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$Swing},$Round:{$Left:0.8,$Top:0.8}}", "Dodge	Inside in Swirl");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8]},$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:260,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$Swing},$Round:{$Left:0.8,$Top:0.8}}", "Dodge Inside in ZigZag");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:80,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8]},$Assembly:260,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$Swing},$Round:{$Left:0.8,$Top:0.8}}", "Dodge Inside in Random");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:80,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8]},$Assembly:260,$ChessMode:{$Column:15,$Row:15},$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Clip:$Jease$.$Swing},$Round:{$Left:0.8,$Top:0.8}}", "Dodge Inside in Random Chess");
		mapData.put("{$Duration:"+duration+",x:0.3,y:0.3,$Delay:60,$Zoom:1,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Opacity:$Jease$.$Linear,$Zoom:$Jease$.$Swing},$Opacity:2,$Round:{$Left:0.8,$Top:0.8}}", "Dodge Inside in TL");
		mapData.put("{$Duration:"+duration+",x:-0.3,y:0.3,$Delay:60,$Zoom:1,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Opacity:$Jease$.$Linear,$Zoom:$Jease$.$Swing},$Opacity:2,$Round:{$Left:0.8,$Top:0.8}}", "Dodge Inside in TR");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:60,$Zoom:1,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Opacity:$Jease$.$Linear,$Zoom:$Jease$.$Swing},$Opacity:2,$Round:{$Left:0.8,$Top:0.8}}", "Dodge Inside in BL");
		mapData.put("{$Duration:"+duration+",x:-0.3,y:-0.3,$Delay:60,$Zoom:1,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Opacity:$Jease$.$Linear,$Zoom:$Jease$.$Swing},$Opacity:2,$Round:{$Left:0.8,$Top:0.8}}", "Dodge Inside in BR");
		mapData.put("{$Duration:"+duration+",x:0.3,y:0.3,$Delay:60,$Zoom:1,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Opacity:$Jease$.$Linear,$Zoom:$Jease$.$Swing},$Opacity:2,$Round:{$Left:0.8,$Top:0.8}}", "Dodge Inside out TL");
		mapData.put("{$Duration:"+duration+",x:-0.3,y:0.3,$Delay:60,$Zoom:1,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Opacity:$Jease$.$Linear,$Zoom:$Jease$.$Swing},$Opacity:2,$Round:{$Left:0.8,$Top:0.8}}", "Dodge Inside out TR");
		mapData.put("{$Duration:"+duration+",x:0.3,y:-0.3,$Delay:60,$Zoom:1,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Opacity:$Jease$.$Linear,$Zoom:$Jease$.$Swing},$Opacity:2,$Round:{$Left:0.8,$Top:0.8}}", "DodgeInside out BL");
		mapData.put("{$Duration:"+duration+",x:-0.3,y:-0.3,$Delay:60,$Zoom:1,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Easing:{$Left:$Jease$.$InJump,$Top:$Jease$.$InJump,$Opacity:$Jease$.$Linear,$Zoom:$Jease$.$Swing},$Opacity:2,$Round:{$Left:0.8,$Top:0.8}}", "Dodge Inside out BR");
		mapData.put("null", "----- Flutter Inside Transitions -----");
		mapData.put("{$Duration:"+duration+",x:1,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7]},$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Assembly:260,$Easing:{$Left:$Jease$.$InOutExpo,$Clip:$Jease$.$InOutQuad},$Round:{$Top:0.8}}", "Flutter Inside in");
		mapData.put("{$Duration:"+duration+",x:1,y:0.2,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$Reverse:true,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Assembly:2050,$Easing:{$Left:$Jease$.$InOutSine,$Top:$Jease$.$OutWave,$Clip:$Jease$.$InOutQuad},$Round:{$Top:1.3}}", "Flutter Inside in Wind");
		mapData.put("{$Duration:"+duration+",x:1,y:0.2,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$Reverse:true,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:2050,$Easing:{$Left:$Jease$.$InOutSine,$Top:$Jease$.$OutWave,$Clip:$Jease$.$InOutQuad},$Round:{$Top:1.3}}", "Flutter Inside in Swirl");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:150,$Cols:12,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Assembly:260,$Easing:{$Left:$Jease$.$Linear,$Top:$Jease$.$OutWave,$Opacity:$Jease$.$Linear},$Round:{$Top:2}}", "Flutter Inside in Column");
		mapData.put("{$Duration:"+duration+",x:1,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7]},$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Assembly:260,$Easing:{$Left:$Jease$.$InOutExpo,$Clip:$Jease$.$InOutQuad},$Round:{$Top:0.8}}", "Flutter Inside out");
		mapData.put("{$Duration:"+duration+",x:1,y:0.2,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$SlideOut:true,$Reverse:true,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Assembly:2050,$Easing:{$Left:$Jease$.$InOutSine,$Top:$Jease$.$OutWave,$Clip:$Jease$.$InOutQuad},$Round:{$Top:1.3}}", "Flutter Inside out Wind");
		mapData.put("{$Duration:"+duration+",x:1,y:0.2,$Delay:20,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Left:[0.3,0.7],$Top:[0.3,0.7]},$SlideOut:true,$Reverse:true,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:2050,$Easing:{$Left:$Jease$.$InOutSine,$Top:$Jease$.$OutWave,$Clip:$Jease$.$InOutQuad},$Round:{$Top:1.3}}", "Flutter Inside out Swirl");
		mapData.put("{$Duration:"+duration+",x:0.2,y:-0.1,$Delay:150,$Cols:12,$Opacity:2,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Assembly:260,$Easing:{$Left:$Jease$.$Linear,$Top:$Jease$.$OutWave,$Opacity:$Jease$.$Linear},$Round:{$Top:2}}", "Flutter Inside out Column");
		mapData.put("null", "----- Compound Transitions -----");
		mapData.put("{$Duration:"+duration+",y:-1,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Top:[0.5,0.5],$Clip:[0,0.5]},$Formation:$JssorSlideshowFormations$.$FormationStraight,$ChessMode:{$Column:12},$ScaleClip:0.5}", "Clip &amp; Chess in");
		mapData.put("{$Duration:"+duration+",y:-1,$Cols:10,$Rows:5,$Opacity:2,$Clip:15,$During:{$Top:[0.5,0.5],$Clip:[0,0.5]},$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraight,$ChessMode:{$Column:12},$ScaleClip:0.5}", "Clip &amp; Chess out");
		mapData.put("{$Duration:"+duration+",x:-1,y:-1,$Cols:6,$Rows:6,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8],$Clip:[0,0.2]},$Formation:$JssorSlideshowFormations$.$FormationStraight,$ChessMode:{$Column:15,$Row:15},$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Clip:$Jease$.$Swing},$ScaleClip:0.5}", "Clip &amp; Oblique Chess in");
		mapData.put("{$Duration:"+duration+",x:-1,y:-1,$Cols:6,$Rows:6,$Opacity:2,$Clip:15,$During:{$Left:[0.2,0.8],$Top:[0.2,0.8],$Clip:[0,0.2]},$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraight,$ChessMode:{$Column:15,$Row:15},$Easing:{$Left:$Jease$.$InCubic,$Top:$Jease$.$InCubic,$Clip:$Jease$.$Swing},$ScaleClip:0.5}", "Clip &amp; Oblique Chess out");
		mapData.put("{$Duration:"+duration+",x:-1,y:0.45,$Delay:80,$Cols:12,$Opacity:2,$Clip:15,$During:{$Left:[0.35,0.65],$Top:[0.35,0.65],$Clip:[0,0.15]},$Formation:$JssorSlideshowFormations$.$FormationStraight,$Assembly:2049,$Easing:{$Left:$Jease$.$Linear,$Top:$Jease$.$OutWave,$Clip:$Jease$.$OutQuad},$ScaleClip:0.7,$Round:{$Top:4}}", "Clip &amp; Wave in");
		mapData.put("{$Duration:"+duration+",x:-1,y:0.45,$Delay:80,$Cols:12,$Opacity:2,$Clip:15,$During:{$Left:[0.35,0.65],$Top:[0.35,0.65],$Clip:[0,0.15]},$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraight,$Assembly:2049,$Easing:{$Left:$Jease$.$Linear,$Top:$Jease$.$OutWave,$Clip:$Jease$.$OutQuad},$ScaleClip:0.7,$Round:{$Top:4}}", "Clip &amp; Wave out");
		mapData.put("{$Duration:"+duration+",x:-1,y:0.7,$Delay:80,$Cols:12,$Opacity:2,$Clip:11,$Move:true,$During:{$Left:[0.35,0.65],$Top:[0.35,0.65],$Clip:[0,0.1]},$Formation:$JssorSlideshowFormations$.$FormationStraight,$Assembly:2049,$Easing:{$Left:$Jease$.$OutQuad,$Top:$Jease$.$OutJump,$Clip:$Jease$.$OutQuad},$ScaleClip:0.7,$Round:{$Top:4}}", "Clip &amp; Jump in");
		mapData.put("{$Duration:"+duration+",x:-1,y:0.7,$Delay:80,$Cols:12,$Opacity:2,$Clip:11,$Move:true,$During:{$Left:[0.35,0.65],$Top:[0.35,0.65],$Clip:[0,0.1]},$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraight,$Assembly:2049,$Easing:{$Left:$Jease$.$OutQuad,$Top:$Jease$.$OutJump,$Clip:$Jease$.$OutQuad},$ScaleClip:0.7,$Round:{$Top:4}}", "Clip	&amp; Jump out");
		mapData.put("null", "----- Wave out Transitions -----");
		mapData.put("{$Duration:"+duration+",y:-0.5,$Delay:60,$Cols:16,$Opacity:2,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Easing:{$Top:$Jease$.$InWave,$Opacity:$Jease$.$Linear},$Round:{$Top:1.5}}", "Wave out");
		mapData.put("{$Duration:"+duration+",y:-0.5,$Delay:30,$Cols:15,$Opacity:2,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationCircle,$Easing:{$Top:$Jease$.$InWave,$Opacity:$Jease$.$Linear},$Round:{$Top:1.5}}", "Wave out Eagle");
		mapData.put("{$Duration:"+duration+",x:-1,y:0.5,$Delay:30,$Cols:10,$Rows:5,$Opacity:2,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:260,$Easing:{$Left:$Jease$.$Linear,$Top:$Jease$.$OutWave,$Opacity:$Jease$.$Linear},$Round:{$Top:1.5}}", "Wave out Swirl");
		mapData.put("{$Duration:"+duration+",x:-1,y:0.5,$Delay:60,$Cols:10,$Rows:5,$Opacity:2,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:260,$ChessMode:{$Row:3},$Easing:{$Left:$Jease$.$Linear,$Top:$Jease$.$OutWave,$Opacity:$Jease$.$Linear},$Round:{$Top:1.5}}", "Wave out ZigZag");
		mapData.put("{$Duration:"+duration+",x:1,y:0.5,$Delay:60,$Cols:10,$Rows:5,$Opacity:2,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationRectangle,$Assembly:260,$Easing:{$Left:$Jease$.$Linear,$Top:$Jease$.$OutWave,$Opacity:$Jease$.$Linear},$Round:{$Top:1.5}}", "Wave out Rectangle");
		mapData.put("{$Duration:"+duration+",x:1,y:0.5,$Delay:60,$Cols:10,$Rows:5,$Opacity:2,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationCircle,$Assembly:260,$Easing:{$Left:$Jease$.$Linear,$Top:$Jease$.$OutWave,$Opacity:$Jease$.$Linear},$Round:{$Top:1.5}}", "Wave out Circle");
		mapData.put("{$Duration:"+duration+",x:1,y:0.5,$Delay:60,$Cols:10,$Rows:5,$Opacity:2,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationCross,$Assembly:260,$Easing:{$Left:$Jease$.$Linear,$Top:$Jease$.$OutWave,$Opacity:$Jease$.$Linear},$Round:{$Top:1.5}}", "Wave out Cross");
		mapData.put("{$Duration:"+duration+",x:1,y:0.5,$Delay:60,$Cols:10,$Rows:5,$Opacity:2,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationRectangleCross,$Assembly:260,$Easing:{$Left:$Jease$.$Linear,$Top:$Jease$.$OutWave,$Opacity:$Jease$.$Linear},$Round:{$Top:1.5}}", "Wave out Rectangle Cross");
		mapData.put("null", "----- Wave in Transitions -----");
		mapData.put("{$Duration:"+duration+",y:-0.5,$Delay:60,$Cols:12,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationStraightStairs,$Easing:{$Top:$Jease$.$InWave,$Opacity:$Jease$.$Linear},$Round:{$Top:1.5}}", "Wave in");
		mapData.put("{$Duration:"+duration+",y:-0.5,$Delay:30,$Cols:15,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationCircle,$Easing:{$Top:$Jease$.$InWave,$Opacity:$Jease$.$Linear},$Round:{$Top:1.5}}", "Wave in Eagle");
		mapData.put("{$Duration:"+duration+",x:-1,y:0.5,$Delay:30,$Cols:10,$Rows:5,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:260,$Easing:{$Left:$Jease$.$Swing,$Top:$Jease$.$InWave,$Opacity:$Jease$.$Linear},$Round:{$Top:1.5}}", "Wave in Swirl");
		mapData.put("{$Duration:"+duration+",x:1,y:0.5,$Delay:60,$Cols:10,$Rows:5,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:260,$ChessMode:{$Row:3},$Easing:{$Left:$Jease$.$Swing,$Top:$Jease$.$InWave,$Opacity:$Jease$.$Linear},$Round:{$Top:1.5}}", "Wave in ZigZag");
		mapData.put("{$Duration:"+duration+",x:-1,y:0.5,$Delay:60,$Cols:10,$Rows:5,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationRectangle,$Assembly:260,$Easing:{$Left:$Jease$.$Swing,$Top:$Jease$.$InWave,$Opacity:$Jease$.$Linear},$Round:{$Top:1.5}}", "Wave in Rectangle");
		mapData.put("{$Duration:"+duration+",x:-1,y:0.5,$Delay:60,$Cols:10,$Rows:5,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationCircle,$Assembly:260,$Easing:{$Left:$Jease$.$Swing,$Top:$Jease$.$InWave,$Opacity:$Jease$.$Linear},$Round:{$Top:1.5}}", "Wave in Circle");
		mapData.put("{$Duration:"+duration+",x:-1,y:0.5,$Delay:60,$Cols:10,$Rows:5,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationCross,$Assembly:260,$Easing:{$Left:$Jease$.$Swing,$Top:$Jease$.$InWave,$Opacity:$Jease$.$Linear},$Round:{$Top:1.5}}", "Wave in Cross");
		mapData.put("{$Duration:"+duration+",x:-1,y:0.5,$Delay:60,$Cols:10,$Rows:5,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationRectangleCross,$Assembly:260,$Easing:{$Left:$Jease$.$Swing,$Top:$Jease$.$InWave,$Opacity:$Jease$.$Linear},$Round:{$Top:1.5}}", "Wave in Rectangle Cross");
		mapData.put("null", "----- Jump out Transitions -----");
		mapData.put("{$Duration:"+duration+",x:-1,y:0.5,$Delay:100,$Cols:10,$Rows:5,$Opacity:2,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationStraight,$Assembly:513,$Easing:{$Left:$Jease$.$Linear,$Top:$Jease$.$OutJump},$Round:{$Top:1.5}}", "Jump out Straight");
		mapData.put("{$Duration:"+duration+",x:-1,y:0.5,$Delay:100,$Cols:10,$Rows:5,$Opacity:2,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:260,$Easing:{$Left:$Jease$.$Linear,$Top:$Jease$.$OutJump},$Round:{$Top:1.5}}", "Jump out Swirl");
		mapData.put("{$Duration:"+duration+",x:-1,y:0.5,$Delay:100,$Cols:10,$Rows:5,$Opacity:2,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:260,$Easing:{$Left:$Jease$.$Linear,$Top:$Jease$.$OutJump},$Round:{$Top:1.5}}", "Jump out ZigZag");
		mapData.put("{$Duration:"+duration+",x:-1,y:0.5,$Delay:800,$Cols:10,$Rows:5,$Opacity:2,$SlideOut:true,$Reverse:true,$Formation:$JssorSlideshowFormations$.$FormationRectangle,$Assembly:260,$Easing:{$Left:$Jease$.$Linear,$Top:$Jease$.$OutJump},$Round:{$Top:1.5}}", "Jump out Rectangle");
		mapData.put("{$Duration:"+duration+",x:-1,y:0.5,$Delay:100,$Cols:10,$Rows:5,$Opacity:2,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationCircle,$Assembly:260,$Easing:{$Left:$Jease$.$Linear,$Top:$Jease$.$OutJump},$Round:{$Top:1.5}}", "Jump out Circle");
		mapData.put("{$Duration:"+duration+",x:-1,y:0.5,$Delay:100,$Cols:10,$Rows:5,$Opacity:2,$SlideOut:true,$Formation:$JssorSlideshowFormations$.$FormationRectangleCross,$Assembly:260,$Easing:{$Left:$Jease$.$Linear,$Top:$Jease$.$OutJump},$Round:{$Top:1.5}}", "Jump out Rectangle Cross");
		mapData.put("null", "----- Jump in Transitions -----");
		mapData.put("{$Duration:"+duration+",x:-1,y:-0.5,$Delay:50,$Cols:10,$Rows:5,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationStraight,$Assembly:513,$Easing:{$Left:$Jease$.$Swing,$Top:$Jease$.$InJump},$Round:{$Top:1.5}}", "Jump in Straight");
		mapData.put("{$Duration:"+duration+",x:-1,y:-0.5,$Delay:50,$Cols:10,$Rows:5,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationSwirl,$Assembly:260,$Easing:{$Left:$Jease$.$Swing,$Top:$Jease$.$InJump},$Round:{$Top:1.5}}", "Jump in Swirl");
		mapData.put("{$Duration:"+duration+",x:-1,y:-0.5,$Delay:50,$Cols:10,$Rows:5,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationZigZag,$Assembly:260,$Easing:{$Left:$Jease$.$Swing,$Top:$Jease$.$InJump},$Round:{$Top:1.5}}", "Jump in ZigZag");
		mapData.put("{$Duration:"+duration+",x:-1,y:-0.5,$Delay:800,$Cols:10,$Rows:5,$Opacity:2,$Reverse:true,$Formation:$JssorSlideshowFormations$.$FormationRectangle,$Assembly:260,$Easing:{$Left:$Jease$.$Swing,$Top:$Jease$.$InJump},$Round:{$Top:1.5}}", "Jump in Rectangle");
		mapData.put("{$Duration:"+duration+",x:-1,y:-0.5,$Delay:50,$Cols:10,$Rows:5,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationCircle,$Assembly:260,$Easing:{$Left:$Jease$.$Swing,$Top:$Jease$.$InJump},$Round:{$Top:1.5}}", "Jump in Circle");
		mapData.put("{$Duration:"+duration+",x:-1,y:-0.5,$Delay:50,$Cols:10,$Rows:5,$Opacity:2,$Formation:$JssorSlideshowFormations$.$FormationRectangleCross,$Assembly:260,$Easing:{$Left:$Jease$.$Swing,$Top:$Jease$.$InJump},$Round:{$Top:1.5}}", "Jump in Rectangle Cross");
		mapData.put("null", "----- Stone Transitions -----");
		mapData.put("{$Duration:"+duration+",y:1,$Opacity:2,$Easing:$Jease$.$InQuad}", "Slide Down");
		mapData.put("{$Duration:"+duration+",x:1,$Opacity:2,$Easing:$Jease$.$InQuad}", "Slide Right");
		mapData.put("{$Duration:"+duration+",y:1,$Opacity:2,$Easing:$Jease$.$InBounce}", "Bounce Down");
		mapData.put("{$Duration:"+duration+",x:1,$Opacity:2,$Easing:$Jease$.$InBounce}", "Bounce Right");
		
		Map<String, String> data2 = new HashMap<>();
		for(String key : mapData.keySet()){
			data2.put(mapData.get(key), key);
		}
		request.setAttribute("mapDataEffect", data2);
	}
	
	/**
	 * @return
	 */
	public static String[][] getListPrinters(){
		// Liste des imprimantes
		List<String> listPrinter = new ArrayList<>();
		if(!StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_PRINT"))){
			String[] priters = StringUtil.getArrayFromStringDelim(ContextGloabalAppli.getEtablissementBean().getImprimantes(), "|");
			if(priters != null){
				for (String printer : priters) {
					listPrinter.add(printer);
				}
			}
		} else{
			PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
			if(printServices != null) {
				for (PrintService printer : printServices){
					listPrinter.add(printer.getName());		
				}
			}
		}
        String[][] listImprimantes = new String[listPrinter.size()][2];
    	//
        int idx = 0;
        for (String printer : listPrinter){
          	listImprimantes[idx][0] = printer;
           	listImprimantes[idx][1] = printer;
           	idx++;
       	}
        
        return listImprimantes;
	}
	
	public static String getAdminValidationMsg(){
		 return "<br><span style='color:#1e1c1c;'>UNE AUTORISATION DU MANAGER EST NÉCESSAIRE</span> ** "
		 		+ " <a href='javascript:' noval=\"true\" targetDiv=\"generic_modal_body\" "
		 		+ " data-backdrop='static' data-keyboard='false' data-toggle='modal' data-target='#generic_modal'"
		 		+ " wact='"+EncryptionUtil.encrypt("admin.user.loadConfirmAnnule")+"' style='color:blue;text-decoration: underline;'>Cliquer sur ce lien</a> **";
	}
	
	public static void loadAllMapGlobParams(boolean isForce) {
		loadParams(isForce);
		
		if(MessageService.getGlobalMap().get("GLOBAL_CONFIG")!=null
				&& !isForce 
				&& ParametrageRightsConstantes.GENERAL_GLOB_PARAMS != null 
				&& ParametrageRightsConstantes.GENERAL_GLOB_PARAMS.size() > 0) {
			return;
		}
		
		Map<String, String> mapParams = new HashMap<>();
		List<ParametragePersistant> listParams = ParametrageRightsConstantes.GENERAL_GLOB_PARAMS;
		listParams.addAll(ParametrageRightsConstantes.CAISSE_GLOB_PARAMS);
		listParams.addAll(ParametrageRightsConstantes.CUISINE_GLOB_PARAMS);
		
		for (ParametragePersistant parametrageP : listParams) {
			if(parametrageP == null) {
				continue;
			}
			mapParams.put(parametrageP.getCode(), parametrageP.getValeur());
		}
		setParamDbGlobalValue(mapParams);
		
		MessageService.getGlobalMap().put("GLOBAL_CONFIG",  mapParams); 
	}
	
	public static void loadAllMapSpecParams(boolean isForce){
		loadParams(isForce);
		Map<Long, Map<String, String>> mapParams = (Map<Long, Map<String, String>>) MessageService.getGlobalMap().get("GLOBAL_CONFIG_SPEC");
		
		if(MessageService.getGlobalMap().get("GLOBAL_CONFIG")!=null
				&& !isForce 
				&& ParametrageRightsConstantes.CAISSE_SPEC_PARAMS != null 
				&& ParametrageRightsConstantes.CAISSE_SPEC_PARAMS.size() > 0) {
			return;
		}
		
		mapParams = new HashMap<>();
				
		ICaisseService caisseServcice = ServiceUtil.getBusinessBean(ICaisseService.class);
		// Balance
		List<CaissePersistant> lisBalance = caisseServcice.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.BALANCE.toString(), false);
		for (CaissePersistant caissePersistant : lisBalance) {
			for (ParametragePersistant parametrageP : ParametrageRightsConstantes.BALANCE_SPEC_PARAMS) {
				Map<String, String> mapDet = mapParams.get(caissePersistant.getId());
				if(mapDet == null) {
					mapDet = new HashMap<>();
					mapParams.put(caissePersistant.getId(), mapDet);
				}
				mapDet.put(parametrageP.getCode(), parametrageP.getValeur());
			}
		}
		// Cuisine
		List<CaissePersistant> lisCuisine = caisseServcice.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.CUISINE.toString(), false);
		for (CaissePersistant caissePersistant : lisCuisine) {
			for (ParametragePersistant parametrageP : ParametrageRightsConstantes.CUISINE_SPEC_PARAMS) {
				Map<String, String> mapDet = mapParams.get(caissePersistant.getId());
				if(mapDet == null) {
					mapDet = new HashMap<>();
					mapParams.put(caissePersistant.getId(), mapDet);
				}
				mapDet.put(parametrageP.getCode(), parametrageP.getValeur());
			}
		}
		// Pilotage
		List<CaissePersistant> lisPilotage = caisseServcice.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.PILOTAGE.toString(), false);
		for (CaissePersistant caissePersistant : lisPilotage) {
			for (ParametragePersistant parametrageP : ParametrageRightsConstantes.PILOTAGE_SPEC_PARAMS) {
				Map<String, String> mapDet = mapParams.get(caissePersistant.getId());
				if(mapDet == null) {
					mapDet = new HashMap<>();
					mapParams.put(caissePersistant.getId(), mapDet);
				}
				mapDet.put(parametrageP.getCode(), parametrageP.getValeur());
			}
		}
		// Presentoire
		List<CaissePersistant> lisPresentoire = caisseServcice.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.PRESENTOIRE.toString(), false);
		for (CaissePersistant caissePersistant : lisPresentoire) {
			for (ParametragePersistant parametrageP : ParametrageRightsConstantes.PRESENTOIRE_SPEC_PARAMS) {
				Map<String, String> mapDet = mapParams.get(caissePersistant.getId());
				if(mapDet == null) {
					mapDet = new HashMap<>();
					mapParams.put(caissePersistant.getId(), mapDet);
				}
				mapDet.put(parametrageP.getCode(), parametrageP.getValeur());
			}
		}
		// Caisses
		List<CaissePersistant> lisCaisse = caisseServcice.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.CAISSE.toString(), false);
		for (CaissePersistant caissePersistant : lisCaisse) {
			for (ParametragePersistant parametrageP : ParametrageRightsConstantes.CAISSE_SPEC_PARAMS) {
				Map<String, String> mapDet = mapParams.get(caissePersistant.getId());
				if(mapDet == null) {
					mapDet = new HashMap<>();
					mapParams.put(caissePersistant.getId(), mapDet);
				}
				mapDet.put(parametrageP.getCode(), parametrageP.getValeur());
			}
		}
		// Afficheur caisse
		List<CaissePersistant> lisAfficherCaisse = caisseServcice.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.AFFICHEUR.toString(), false);
		for (CaissePersistant caissePersistant : lisAfficherCaisse) {
			for (ParametragePersistant parametrageP : ParametrageRightsConstantes.AFFICHEUR_SPEC_PARAMS) {
				Map<String, String> mapDet = mapParams.get(caissePersistant.getId());
				if(mapDet == null) {
					mapDet = new HashMap<>();
					mapParams.put(caissePersistant.getId(), mapDet);
				}
				mapDet.put(parametrageP.getCode(), parametrageP.getValeur());
			}
		}
		
		
		for(Long caisseId : mapParams.keySet()) {
			setParamDbSpecValue(mapParams.get(caisseId), caisseId);
		}
		
		MessageService.getGlobalMap().put("GLOBAL_CONFIG_SPEC",  mapParams);
	}
	
	public static List<ParametragePersistant> loadParamsGlobalByGroup(ActionUtil httpUtil, TYPE_PARAM type){
		List<String> listCode = new ArrayList<>();
		List<ParametragePersistant> listParamType = new ArrayList<>();
		
		List<ParametragePersistant> listParam = new ArrayList<>();
		listParam.addAll(GENERAL_GLOB_PARAMS);
		listParam.addAll(CAISSE_GLOB_PARAMS);
		listParam.addAll(CUISINE_GLOB_PARAMS);
		
		for(ParametragePersistant paramP : listParam){
			if(type.getGroupes() == null || type.getGroupes().length == 0) {
				if(!listCode.contains(paramP.getCode())) {
					listParamType.add(paramP);
					listCode.add(paramP.getCode());
				}
			} else {
				if(!listCode.contains(paramP.getCode()) && StringUtil.contains(paramP.getGroupe(), type.getGroupes())){
					listParamType.add(paramP);
					listCode.add(paramP.getCode());
				}
			}
		}
		
		IParametrageService paramServcice = ServiceUtil.getBusinessBean(IParametrageService.class);
		List<ParametragePersistant> listParamDb = paramServcice.findAll();
		
		for(ParametragePersistant paramType : listParamType) {
			for (ParametragePersistant paramDb : listParamDb) {
				if(paramDb.getCode().equals(paramType.getCode())){
					paramType.setValeur(paramDb.getValeur());
					break;
				}
			}
		}
		
		httpUtil.setRequestAttribute("list_imprimante", getListPrinters());
        //
        String[][] orientationBalance = {{"L", "Paysage"}, {"LR", "Paysage inversé"}, {"P", "Portrait"}};
        httpUtil.setRequestAttribute("orientationBalance", orientationBalance);
		
		httpUtil.setRequestAttribute("listModeTravail", new String[][]{{"PO", "Imprimantes seulement"}, {"EO", "Ecran seulement"}, {"PE", "Ecran et imprimanate"}});
		httpUtil.setRequestAttribute("dataSourceStatut", new String[][]{{"EP", "Uniquement depuis l'écran de pilotage"}, {"EC", "Uniquement depuis l'écran de cuisine"}, {"ECP", "Les deux écrans"}});
		httpUtil.setRequestAttribute("dataStrategieEcran", new String[][]{{"A", "Alterner les commandes"}, {"D", "Dupliquer les commandes"}});
		httpUtil.setRequestAttribute("dataStatut", new String[][]{{"V", "Validée"}, {"EP", "En préparation"}, {"P", "Prête"}});
		
		httpUtil.setRequestAttribute("listParams", listParamType);
		
		return listParamType;
	}
	
	public static List<ParametragePersistant> loadParamsSpecByGroup(ActionUtil httpUtil, TYPE_PARAM type, Long caisseId){
		List<String> listCode = new ArrayList<>();
		List<ParametragePersistant> listParam = new ArrayList<>();
		List<ParametragePersistant> listParamType = new ArrayList<>();
		
		if(type.equals(TYPE_PARAM.CUISINE_SPEC)){
			listParam = CUISINE_SPEC_PARAMS;
		} else if(type.equals(TYPE_PARAM.PILOTAGE_SPEC)){
			listParam = PILOTAGE_SPEC_PARAMS;
		} else if(type.equals(TYPE_PARAM.PRESENTOIRE_SPEC)){
			listParam = PRESENTOIRE_SPEC_PARAMS;
		} else if(type.equals(TYPE_PARAM.BALANCE_SPEC)){
			listParam = BALANCE_SPEC_PARAMS;
		} else if(type.equals(TYPE_PARAM.CAISSE_SPEC)){
			listParam = CAISSE_SPEC_PARAMS;
		} else if(type.equals(TYPE_PARAM.AFFICHEUR_CAISSE_SPEC)){
			listParam = AFFICHEUR_SPEC_PARAMS;
			setMapEffect(httpUtil.getRequest());
		}
		
		for(ParametragePersistant paramP : listParam){
			if(type.getGroupes() == null || type.getGroupes().length == 0) {
				if(!listCode.contains(paramP.getCode())) {
					listParamType.add(paramP);
					listCode.add(paramP.getCode());
				}
			} else {
				if(!listCode.contains(paramP.getCode()) && StringUtil.contains(paramP.getGroupe(), type.getGroupes())){
					listParamType.add(paramP);
					listCode.add(paramP.getCode());
				}
			}
		}
		
		IParametrageService paramServcice = ServiceUtil.getBusinessBean(IParametrageService.class);
		List<ParametragePersistant> listParamDb = paramServcice.findAll();
		
		for(ParametragePersistant paramType : listParamType) {
			for (ParametragePersistant paramDb : listParamDb) {
				if(paramDb.getCode().equals(paramType.getCode()) && paramDb.getOpc_terminal()!=null && caisseId.equals(paramDb.getOpc_terminal().getId())){
					paramType.setValeur(paramDb.getValeur());
					break;
				}
			}
		}
		
		httpUtil.setRequestAttribute("listParams", listParamType);
		
		return listParamType;
	}
	
	private static void setParamDbGlobalValue(Map<String, String> mapParam){
		IParametrageService paramServcice = ServiceUtil.getBusinessBean(IParametrageService.class);
		List<ParametragePersistant> listParamDb = paramServcice.findAll();
		
		for(String code : mapParam.keySet()) {
			for (ParametragePersistant paramDb : listParamDb) {
				if(paramDb.getCode().equals(code)){
					mapParam.put(code, paramDb.getValeur());
					break;
				}
			}
		}
	}
	
	private static void setParamDbSpecValue(Map<String, String> mapParam, Long caisseId){
		IParametrageService paramServcice = ServiceUtil.getBusinessBean(IParametrageService.class);
		List<ParametragePersistant> listParamDb = paramServcice.findAll();
		
		for(String code : mapParam.keySet()) {
			for (ParametragePersistant paramDb : listParamDb) {
				if(paramDb.getCode().equals(code) && paramDb.getOpc_terminal()!=null && caisseId.equals(paramDb.getOpc_terminal().getId())){
					mapParam.put(code, paramDb.getValeur());
					break;
				}
			}
		}
	}	
}
