<%@page import="framework.model.common.util.StrimUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
String tp = (String)request.getAttribute("tp");
String labelBtn = (tp.equals("ouv") ? "Clore": "Ouvrir")+" le shift";
%>

<style>
.div_shift {
	color: black;
}
.div_shift tr {
    line-height: 45px;
 }
</style>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget" id="shift-div">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption"><%=labelBtn %></span>
			
			<button type="button" id="close_modal" class="btn btn-primary" style="margin-top: 2px;" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
	<%if(tp.equals("ouv")){ %>		
			<div class="row div_shift" style="margin-left: 10px;margin-right: 0px;">
				<table>
					<tr>
						<td><std:label classStyle="div_shift" value="Montant esp&egrave;ces" /> </td>
						<td><std:text autocomplete="false" name="caisseJournee.mtt_cloture_caissier_espece" type="string" maxlength="12" style="text-align:right;"/></td>
					</tr>
					<tr>
						<td><std:label classStyle="div_shift" value="Montant carte" /> </td>
						<td><std:text autocomplete="false" name="caisseJournee.mtt_cloture_caissier_cb" type="string" maxlength="12" style="text-align:right;" /></td>
					</tr>
					<tr>
						<td><std:label classStyle="div_shift" value="Montant ch&egrave;que" /> </td>
						<td><std:text autocomplete="false" name="caisseJournee.mtt_cloture_caissier_cheque" type="string" maxlength="12" style="text-align:right;" /></td>
					</tr>
					<tr>
						<td><std:label classStyle="div_shift" value="Montant chq d&eacute;j." /> </td>
						<td><std:text autocomplete="false" name="caisseJournee.mtt_cloture_caissier_dej" type="string" maxlength="12" style="text-align:right;" /></td>
					</tr>
					<tr>
						<td><std:label classStyle="div_shift" value="Total" style="font-weight: bold;color: #ed4e2a;font-size: 18px;" /> </td>
						<td><std:text autocomplete="false" name="caisseJournee.mtt_cloture_caissier" type="string" maxlength="12" required="true" style="text-align:right;" /></td>
					</tr>
				</table>
			</div>	
		<%} else{ %>
			<div class="row" style="margin-left: 10px;margin-right: 0px;">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Montant esp&egrave;ces" />
					<div class="col-md-3">
						<std:text autocomplete="false" name="caisseJournee.mtt_cloture_caissier_espece" type="string" maxlength="12" style="text-align:right;" />
					</div>
					
				</div>
			</div>	
		<%} %>
		<div class="row" style="text-align: center;margin-top: 20px;">
			<div class="col-md-12">
			<%
			tp = "tp="+tp;
			%>
				<std:button actionGroup="M" style="border-radius: 37px;height: 42px;font-size: 21px;" classStyle="btn btn-success" action="caisse-web.caisseWeb.ouvrirCloreShift" params="<%=tp %>" icon="fa-save" value="<%=labelBtn %>" />
			</div>
		</div>
	</div>
</div>
</std:form>