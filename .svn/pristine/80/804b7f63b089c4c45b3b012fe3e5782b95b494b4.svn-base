package framework.model.common.constante;

import framework.model.common.util.StrimUtil;


public class ProjectConstante {

	// Complex table name
	public static final String WORK_CPLX_TABLE 	= "work_cplxTable";
	// Ajax
	public static final String IS_AJAX_BACK_JOB_CALL 	= "wibaj";// Lancement d'un traitement sans page de sortie
	public static final String IS_AJAX_PARTIAL_INJECT 	= "wpaj";// Inject partielle du résultat
	public static final String IS_READ_ONLY_FORM 	= "read_only";
	public static final String IS_SET_READ_ONLY_FORM 	= "set_read_only";
	public static final String IS_FORWARD_ACTION 	= "isForwardAct";

	// Messages
	public static final String WORK_ID_QUEST_DIALOG = "w_qstid";

	// Session scope variables
    public static final String SESSION_GLOBAL_USER 	= "global_user";
    public static final String SESSION_GLOBAL_RIGHT	= "global_rights";
    public static final String WORK_DATA_NAVIG 		= "work_data_navig";
    public static final String MAP_VALIDATORS 		= "work_map_val";
    public static final String MAP_BEAN_VALIDATORS 	= "work_map_bean_val";
    public static final String ELEMENTS_IDS 		= "work_check_ids";
    public static final String ELEMENTS_URL_IDS 	= "work_chckurl_ids";
    public static final String CHECK_SAVE_STR 		= "check_save";
    public static final String CHECK_CHECK_ALL 		= "check_all";

	// Params map
    public static final String WORK_ID 						= "workId";
    public static final String WORK_TABLE_ID 				= "w_tbid";
	public static final String WORK_PARAMS 					= "work_params";
	
	public static final String WORK_FORM_ACTION 			= "w_f_act";
	
	public static final String WORK_NO_SAVE_ACTION			= "w_nos";/*Si on doit sauvegarder l'action*/
	
	public static final String WORK_CONTROLLER 				= "w_ctrl";// Pour composants 
	public static final String WORK_ACTION 					= "w_act";// Pour composants
	
	public static final String WORK_TABLE_NAME	 			= "work_table";

	// Validators
	public static final String MIN 					= "min";
	public static final String MAX 					= "max";
	public static final String MINLENGTH 			= "minlength";
	public static final String MAXLENGTH 			= "maxlength";
	public static final String REQUIR 				= "requir";
	public static final String VALID 				= "valid";
	public static final String REQUIRED_VALIDATOR 	= "required";
	public static final String TYPE 				= "type";

	public static final String VAL_VALIDATOR 		= "val";
	public static final String VAL_EXCLUDE 			= "exc";
	public static final String VAL_FORMAT 			= "fmt";
	public static final String VAL_MAX				= "max";
	public static final String VAL_MIN 				= "min";
	public static final String VAL_MAXLENGTH 		= "maxLength";
	public static final String VAL_MINLENGTH 		= "minLength";

	// Components
	public static final String MAP_COMPONENT 	= "work_map_comp";
	public static final String TOP_MENU_ID 		= "tmnu";
	public static final String LEFT_MENU_ID	 	= "lmnu";

	// Filter selects
//	public static final String CONDITION_LIKE = "%%";
//	public static final String CONDITION_EQUALS = "=";
//	public static final String CONDITION_START = "*%";
//	public static final String CONDITION_END = "%*";
//	public static final String CONDITION_BETWEEN = "> | <";
//	public static final String CONDITION_BETWEEN_EQUALS = ">= | <="; 
//	public static final String CONDITION_UP = ">";
//	public static final String CONDITION_LOW = "<";
//	public static final String CONDITION_UP_EQUALS = ">=";
//	public static final String CONDITION_LOW_EQUALS = "<=";
	
	public enum QUERY_CONDITIONS {
		STRING_CONDITION_LIKE("%", "Contient"), 
		STRING_CONDITION_START("*%", "Commence par"), 
		STRING_CONDITION_END("%*", "Se termine par"), 
		STRING_CONDITION_EQUALS("=", "Egale"),

		NUMERIC_CONDITION_EQUALS("=", "Egale"), 
		NUMERIC_CONDITION_UP(">=", "Supérieur"), 
		NUMERIC_CONDITION_UP_EQUALS(">=", "Supérieur ou égale"), 
		NUMERIC_CONDITION_LOW("<", "Inférieur"), 
		NUMERIC_CONDITION_LOW_EQUALS("<=", "Inférieur ou égale"), 
		NUMERIC_CONDITION_BETWEEN("> | <", "Entre"), 
		NUMERIC_CONDITION_BETWEEN_EQUALS(">= | <=", "Entre ou égale"),

		BOOLEAN_CONDITION_EQUALS("=", "Egale");
		
		private String libelle;
		private String code;
		QUERY_CONDITIONS(String code, String libelle){
			this.libelle = libelle;
			this.code = code;
		}
		public String getLibelle(){
			return this.libelle;
		}
		public String getCode(){
			return this.code;
		}
	}
	
	public static final String[][] YES_NO_CONDITIONS 		= {{"", ""}, {"true", StrimUtil.label("yes")}, {"false", StrimUtil.label("no")}};

	// Enum type of messages
	public static enum MSG_TYPE {
		ERROR, INFO, WARNING, SUCCES, QUESTION
	}
    public static final String SIGNATURE = "signature";
	public static final String DATE_MAJ = "date_maj";
	public static final String DATE_CREATION = "date_creation";
	public static final String ETABLISSEMENT = "opc_etablissement";
	public static final String SOCIETE = "opc_societe";
	public static final String ABONNE = "opc_abonne";
	
	public static final class DEFAULT {}

	// Type data for components
	public static enum TYPE_DATA_ENUM{
		STRING("string"),
		DECIMAL("decimal"),
		LONG("long"),
		INTEGER("integer"),
		DATE("date"),
		DATE_TIME("dateTime"),
		BOOLEAN("boolean"),
		
		INTEGER_ARRAY("integer[]"),
		LONG_ARRAY("long[]"),
		DECIMAL_ARRAY("decimal[]"),
		STRING_ARRAY("string[]");

		TYPE_DATA_ENUM(String type){
			this.type = type;
		}

		private String type;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}

		public static TYPE_DATA_ENUM getTypeData(String type){
			for(TYPE_DATA_ENUM t : TYPE_DATA_ENUM.values()){
				if(t.getType().equalsIgnoreCase(type)){
					return t;
				}
			}

			return null;
		}
	}

	// Type session
	public static enum SESSION_SCOPE_ENUM {
		MENU("menu_scope."),
		USER("");

		SESSION_SCOPE_ENUM(String type){
			this.type = type;
		}

		private String type;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}

	public static final int NOT_VALUE			= -9999;

	// Actions not real
	public static final String VALIDATION		 		= "work_validation";

	// Pagger
	public static final String PAGER_FILTER_PREFIX 		= "work_filter_";
	public static final String PAGER_CONDITION_PREFIX 	= "work_cond_";
	public static final String PAGER_TYPE_PREFIX 		= "work_type_";
	public static final String PAGER_JOIN_PREFIX 		= "work_join_";
	// Image paths
	public static final String IMG_DELETE_PATH 		= "resources/framework/img/table/action/delete.png";
	public static final String IMG_EDIT_PATH 		= "resources/framework/img/table/action/edit.png";
	public static final String IMG_EDIT_CURR_PATH 	= "resources/framework/img/table/action/edit_curr.gif";
	public static final String IMG_UPDATE_PATH 		= "resources/framework/img/table/action/update.png";
//	public static final String IMG_EXPORT_PATH 		= "resources/framework/img/table/action/export.png";
	public static final String IMG_HELP_ADD_PATH 	= "resources/framework/img/table/action/add.gif";
	public static final String IMG_ADD_PATH		 	= "resources/framework/img/table/action/add.png";
	public static final String IMG_REFRESH_PATH		= "resources/framework/img/table/refresh.png";
	public static final String IMG_REINIT_PATH		= "resources/framework/img/table/action/arrow_undo.png";
	public static final String IMG_LOAD_PATH		= "resources/framework/img/table/load.gif";
//	public static final String IMG_HIDE_CONDITION 	= "resources/framework/img/table/hide_rcondition.gif";
//	public static final String IMG_SHOW_CONDITION 	= "resources/framework/img/table/show_rcondition.gif";
	public static final String IMG_SHOW_HIDE_COL 	= "resources/framework/img/table/selector";
	public static final String IMG_CREATE_PATH 		= "resources/framework/img/table/action/add_new.png";
	public static final String IMG_DUPLIC_PATH 		= "resources/framework/img/table/action/duplicate.png";
	public static final String IMG_SUBMIT_PATH		= "resources/framework/img/table/action/submit.png";
	public static final String IMG_SUBMIT2_PATH		= "resources/framework/img/table/action/save.png";
	public static final String IMG_MINUS_PATH		= "resources/framework/img/minus.gif";
	public static final String IMG_MAXUS_PATH		= "resources/framework/img/maxus.gif";

	/*--------------------------------------------- Dans la conf ----------------------------------------------------*/
	public static final String DEFAULT_LINE_COUNT 	= StrimUtil.getGlobalConfigPropertie("table.rows.count");
	public static final String ROWS_IN_PAGE 	= StrimUtil.getGlobalConfigPropertie("table.row.range");
	public static final String MAX_BUFFER_SIZE 	= StrimUtil.getGlobalConfigPropertie("max.buffer.size");
	public static final String TD_MIN_WIDTH 	= "45px";

    public static final String DEFAULT_HELPER_HEIGHT 	= StrimUtil.getGlobalConfigPropertie("helper.default.height");
    public final static String DEFAULT_FRAME_WIDTH 		= StrimUtil.getGlobalConfigPropertie("frame.default.width");
	public final static String DEFAULT_FRAME_HEIGHT 	= StrimUtil.getGlobalConfigPropertie("frame.default.height");

	// Separator
	public static final String SEPARATOR 		= "|";
	public static final String HOME_PAGE_ACTION = "hab.widget.load_alert";
	public static final String CONNECT_PAGE_ACTION = "commun.login.connect";
	public static final String DISCONNECT_PAGE_ACTION = "commun.login.disconnect";

	// Real actions
	/*public static enum RIGHT_ENUM {
		RIGHT_SHOW("SHO"),
		RIGHT_EDIT("EDI"),
		RIGHT_UPDATE("UPD"),
		RIGHT_CREATE("CRE"),
		RIGHT_DUPLIC("DUP"),
		RIGHT_DELETE("DEL"),
		RIGHT_DELETE_GRP("DGR");
		
		RIGHT_ENUM(String type){
			this.type = type;
		}

		private String type;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}*/
}

