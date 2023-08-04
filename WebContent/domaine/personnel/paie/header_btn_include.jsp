<%@page import="appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.model.domaine.personnel.persistant.paie.PointagePersistant"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>

<%
boolean isMnuPointage = "mnu".equals(ControllerUtil.getMenuAttribute("tp", request));
boolean isRh = ContextGloabalAppli.getAbonementBean().isOptRh();
boolean isPointeuse = (StringUtil.isNotEmpty(ContextGloabalAppli.getEtablissementBean().getPointeuse_db_path()) || 
		(StringUtil.isNotEmpty(ContextGloabalAppli.getEtablissementBean().getPointeuse_ip()) 
		&& StringUtil.isNotEmpty(ContextGloabalAppli.getEtablissementBean().getPointeuse_port())));
%>

<div style="width:40%;float: left;">
	<std:link noJsValidate="true" actionGroup="C" params="tpMnu=H" style='font-weight:bold;margin-left: 0px;' action="paie.salariePaie.loadVueJour" classStyle="btn btn-link ${mnuTop=='H'?' label label-warning graded':'' }" value="Jour" icon="fa fa-reorder" tooltip="Vue jour" />
	<std:link noJsValidate="true" actionGroup="C" params="tpMnu=JD" style='font-weight:bold;margin-left: 0px;' action="paie.salariePaie.loadVueJour" classStyle="btn btn-link ${mnuTop=='JD'?' label label-warning graded':'' }" value="Mois" icon="fa fa-reorder" tooltip="Vue mois" />
	<std:link noJsValidate="true" actionGroup="C" params="tpMnu=J" style='font-weight:bold;margin-left: 0px;' action="paie.salariePaie.loadVueJour" classStyle="btn btn-link ${mnuTop=='J'?' label label-warning graded':'' }" value="Mois détail" icon="fa fa-reorder" tooltip="Vue Mois détaillée" />
	<std:link noJsValidate="true" actionGroup="C" params="tpMnu=M" style='font-weight:bold;margin-left: 0px;' action="paie.salariePaie.loadVueMois" classStyle="btn btn-link ${mnuTop=='M'?' label label-warning graded':'' }" value="Année" icon="fa fa-reorder" tooltip="Vue année" />
	
	| <std:linkPopup noJsValidate="true" actionGroup="C" style='margin-left: 10px;' action="pers.pointage.init_config" icon="fa fa-cogs" tooltip="Param&egrave;trage pointeuse" />
	| <std:link noJsValidate="true" action="pers.pointage.runUploadPointeuseZktEcoIp" params="isFromP=1" icon="fa fa-arrow-circle-o-down" tooltip="Télécharger les données de la pointeuse" />
			
	<%if(ControllerUtil.getMenuAttribute("IS_TRV_MNU", request) != null){%>
	  | <std:link classStyle="btn btn-default" action="stock.travaux.work_edit" params="bck=1&isFltr=1" icon="fa fa-3x fa-mail-reply-all" value="Retour aux travaux" tooltip="Retour aux travaux" /> 
	<%}%>
</div>
<div style="width:60%;float: left;">
	<jsp:include page="/domaine/personnel/paie/header_filter_include.jsp" />
</div>
