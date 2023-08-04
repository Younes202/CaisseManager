<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<c:set var="dateUtil" value="<%=new DateUtil() %>" />

	<div class="well with-header" style="height: 420px;">
		<div class="header bordered-darkorange">Inventaire du <b style="color:blue;">${dateUtil.dateToString(dateDebut) }</b> au <b style="color:blue;">${dateUtil.dateToString(dateFin) }</b></div>
		<div class="row">
	        <div class="form-group">
	        	<std:label classStyle="control-label col-md-2" value="Date d&eacute;but" />
	            <div class="col-md-2">
	                 <std:date name="dateDebut" required="true" value="${dateDebut }"/>
	            </div>
	            <div class="col-md-2" style="text-align: center;">
	            	<std:link action="dash.dashStock.init_inventaire" params="prev=1&is_fltr=1" icon="fa fa-arrow-circle-left" tooltip="Mois pr&eacute;c&eacute;dent" targetDiv="dash_inv"/>
	            	<std:link action="dash.dashStock.init_inventaire" params="next=1&is_fltr=1" icon="fa fa-arrow-circle-right" tooltip="Mois suivant" targetDiv="dash_inv"/>
	            </div>
	            
	            <std:label classStyle="control-label col-md-1" value="Date fin" />
	            <div class="col-md-2">
	                 <std:date name="dateFin" required="true" value="${dateFin }"/>
	            </div>
	            <div class="col-md-2">
	           	 	<std:button action="dash.dashStock.init_inventaire" value="Filtrer" params="is_fltr=1" classStyle="btn btn-primary" targetDiv="dash_inv"/>
	           	 </div>	
	       </div>
	   </div>
		<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 350px;">
			<div class="databox-row row-12 bordered-bottom bordered-ivory padding-10" style="height: 43px;">
				<div class="databox-cell cell-6 text-center no-padding-left padding-bottom-30">	
					<span class="badge badge-empty pull-left margin-5" style="background-color: transparent;"></span> 
					<span class="databox-text darkgray pull-left no-margin hidden-xs">
					<span style="color: black;font-size: 13px;color: blue;">Responsable :</span></span> 
					<span class="databox-text darkgray pull-left no-margin uppercase"> 
						<b style="color: black;font-size: 13px;">
						
						</b>
					</span>
				</div>
				<div class="databox-cell cell-6 text-center no-padding-left padding-bottom-30">	
					<span class="badge badge-empty pull-left margin-5" style="background-color: transparent;"></span> 
					<span class="databox-text darkgray pull-left no-margin hidden-xs">
					<span style="color: black;font-size: 13px;color: blue;">Saisisseur :</span></span> 
					<span class="databox-text darkgray pull-left no-margin uppercase"> 
						<b style="color: black;font-size: 13px;">
	
						</b>
					</span>
				</div>
			</div>
			<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" >
				<c:if test="${list_inv_dash.size() > 0 }">
					<table class="table table-condensed table-striped table-bordered table-hover no-margin" style="font-size: 12px;">
						<thead>
							<tr>
								<th>Article</th>
								<th>Qte th&eacute;orique</th>
								<th>Qte r&eacute;elle</th>
								<th>Qte &eacute;cart</th>
								<th>Montant &eacute;cart</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="inv" items="${list_inv_dash }">
								<tr>
									<td>${inv.opc_article.libelle }</td>
									<td><fmt:formatDecimal value="${inv.qte_theorique }" /></td>
									<td><fmt:formatDecimal value="${inv.qte_reel }" /> </td>
									<td>${inv.qte_ecart }</td>
									<td></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:if>
			</div>
		</div>
	</div>
