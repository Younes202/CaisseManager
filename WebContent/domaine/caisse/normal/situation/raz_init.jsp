<%@page import="appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#generic_modal_body{
	width: 920px;
	margin-left: -10%;
}
</style>

<%
boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
%>
<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Remise à zéro</span>
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin-left: 10px;margin-right: 10px;">
				<div class="form-group">
					<%if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("RIGHT_IMPRAZ_CAI"))){%>
					<div class="col-md-4" style="margin-top: 5px;">
						<std:button style="margin-left:15px;border-radius: 37px;height: 52px;font-size: 21px;width: 85%;background-color: #9b5a05;color: white;" closeOnSubmit="true"  action="caisse.razPrint.imprimer_raz_caisse" params="format=PRINT&mode=JRN&mnu=RCJ" targetDiv="div_gen_printer" icon="fa-print" value="RAZ journée"/>
					</div>
					<%} %>
					<%if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("RIGHT_IMPRAZ_EMPL_AL"))){ %>
					<div class="col-md-4" style="margin-top: 5px;">
						<std:button style="margin-left:15px;border-radius: 37px;height: 52px;font-size: 21px;width: 85%;background-color: #a98d04;color: white;" closeOnSubmit="true"  action="caisse.razPrint.imprimer_raz_caisse" params="format=PRINT&mode=JRN&mnu=REA" targetDiv="div_gen_printer" icon="fa-print" value="RAZ tout employé"/>
					</div>
					<%} %>
					<%if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("RIGHT_IMPRAZ_EMPL"))){ %>
					<div class="col-md-4" style="margin-top: 5px;">
						<std:button style="margin-left:15px;border-radius: 37px;height: 52px;font-size: 21px;width: 85%;background-color: #06977f;color: white;" closeOnSubmit="true"  action="caisse.razPrint.imprimer_raz_caisse" params="format=PRINT&mode=JRN&mnu=RE" targetDiv="div_gen_printer" icon="fa-print" value="RAZ employé"/>
					</div>
					<%} %>
					<%if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("RIGHT_IMPRAZ_ART"))){ %>
					<div class="col-md-4" style="margin-top: 5px;">
						<std:button style="margin-left:15px;border-radius: 37px;height: 52px;font-size: 21px;width: 85%;background-color: #a98d04;color: white;" closeOnSubmit="true"  action="caisse.razPrint.imprimer_raz_caisse" params="format=PRINT&mode=JRN&mnu=RA" targetDiv="div_gen_printer" icon="fa-print" value="RAZ articles"/>
					</div>
					<%} %>
				<%if(isRestau && StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("RIGHT_IMPRAZ_BOISS"))){ %>
					<div class="col-md-4" style="margin-top: 5px;">
						<std:button style="margin-left:15px;border-radius: 37px;height: 52px;font-size: 21px;width: 85%;background-color: #06977f;color: white;" closeOnSubmit="true"  action="caisse.razPrint.imprimer_raz_caisse" params="format=PRINT&mode=JRN&mnu=RB" targetDiv="div_gen_printer" icon="fa-print" value="RAZ boisson"/>
					</div>
					<%} %>
				<%if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("RIGHT_IMPRAZ_MODE_PAIE"))){ %>	
					<div class="col-md-4" style="margin-top: 5px;">
						<std:button style="margin-left:15px;border-radius: 37px;height: 52px;font-size: 21px;width: 85%;background-color: #9b5a05;color: white;" closeOnSubmit="true"  action="caisse.razPrint.imprimer_raz_caisse" params="format=PRINT&mode=JRN&mnu=RMP" targetDiv="div_gen_printer" icon="fa-print" value="RAZ mode paiements"/>
					</div>
					<%} %>
				<%if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("RIGHT_IMPRAZ_LIVR"))){ %>	
					<div class="col-md-4" style="margin-top: 5px;">
						<std:button style="margin-left:15px;border-radius: 37px;height: 52px;font-size: 21px;width: 85%;background-color: #06977f;color: white;" closeOnSubmit="true"  action="caisse.razPrint.imprimer_raz_caisse" params="format=PRINT&mode=JRN&mnu=RL" targetDiv="div_gen_printer" icon="fa-print" value="RAZ livreurs"/>
					</div>
				<%} %>
				<%if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("RIGHT_IMPRAZ_SOC_LIVR"))){ %>	
					<div class="col-md-4" style="margin-top: 5px;">
						<std:button style="margin-left:15px;border-radius: 37px;height: 52px;font-size: 21px;width: 85%;background-color: #9b5a05;color: white;" closeOnSubmit="true"  action="caisse.razPrint.imprimer_raz_caisse" params="format=PRINT&mode=JRN&mnu=RSL" targetDiv="div_gen_printer" icon="fa-print" value="RAZ sociétés livraison"/>
					</div>
				<%} %>
				<%if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("RIGHT_IMPRAZ_GLOB"))){ %>	
					<div class="col-md-4" style="margin-top: 5px;">
						<std:button style="margin-left:15px;border-radius: 37px;height: 52px;font-size: 21px;width: 85%;background-color: #a98d04;color: white;" closeOnSubmit="true"  action="caisse.razPrint.imprimer_raz_caisse" params="format=PRINT&mode=JRN&mnu=GLO" targetDiv="div_gen_printer" icon="fa-print" value="RAZ globale"/>
					</div>
				<%} %>
				<%if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("RIGHT_IMPRAZ_POS"))){ %>	
					<div class="col-md-4" style="margin-top: 5px;">
						<std:button style="margin-left:15px;border-radius: 37px;height: 52px;font-size: 21px;width: 85%;background-color: #a98d04;color: white;" closeOnSubmit="true"  action="caisse.razPrint.imprimer_raz_caisse" params="format=PRINT&mode=JRN&mnu=POS" targetDiv="div_gen_printer" icon="fa-print" value="RAZ poste"/>
					</div>
				<%} %>
				</div>
			</div>	
		</div>
	</div>
</std:form>