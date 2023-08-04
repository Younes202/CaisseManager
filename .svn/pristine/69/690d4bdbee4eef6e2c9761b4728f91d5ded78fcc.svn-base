<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#ect_table tr {
	border-bottom: 1px dotted #53a93f;
}
#ect_table tr:HOVER{
	background-color: orange;
}
#ect_table{
	background-color: white !important;
}
</style>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Finance</li>
		<li class="active">Grand livre</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link classStyle="btn btn-default" action="admin.compteBancaire.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>

<c:set var="dateUtil" value="<%=new DateUtil() %>" />

<std:form name="search-form">	
	<div class="row">
		<div class="widget-body">
		<%if(ControllerUtil.getMenuAttribute("banqueId", request) == null){ %>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Compte" />
					<div class="col-md-4">
		               	  <std:select name="compte.id" type="long" key="id" labels="libelle" data="${listeBanque }" addBlank="false" required="true">
		               	  		<std:ajax action="admin.compteBancaire.getSoldeCompte" event="change" target="span_solde" isInput="true"/>
		               	  </std:select>
		            </div>
		            <std:label classStyle="control-label col-md-2" style="font-size: 23px;font-weight: bold;color: blue;line-height: 4px;" value="Solde actuel" />
		            <div class="col-md-1">
		            	<span style="font-size: 23px;font-weight: bold;color: #6caf1c;line-height: 18px;" id="span_solde"><fmt:formatDecimal value="${soldeActuel }"/></span>
		            </div>
		        </div>
		    </div> 
		  <%} %>
		    <div class="row">
		        <div class="form-group">
		        	<std:label classStyle="control-label col-md-2" value="Date d&eacute;but" />
		            <div class="col-md-2">
		                 <std:date name="dateDebut" required="true" value="${dateDebut }"/>
		            </div>
		            <div class="col-md-2" style="text-align: center;">
		            	<std:link action="admin.compteBancaire.find_ecriture_livre" params="prev=1&is_fltr=1" icon="fa fa-arrow-circle-left" tooltip="Journ&eacute;e pr&eacute;c&eacute;dente" />
		            	<std:link action="admin.compteBancaire.find_ecriture_livre" params="next=1&is_fltr=1" icon="fa fa-arrow-circle-right" tooltip="Journ&eacute;e suivante" />
		            </div>
		            
		            <std:label classStyle="control-label col-md-1" value="Date fin" />
		            <div class="col-md-2">
		                 <std:date name="dateFin" required="true" value="${dateFin }"/>
		            </div>
		            <div class="col-md-2">
		           	 	<std:button action="admin.compteBancaire.find_ecriture_livre" value="Filtrer" params="is_fltr=1" classStyle="btn btn-primary" />
		           	 </div>	
		       </div>
		   </div>    
        </div>
	</div>
	<div class="widget-body" style="margin-top: 10px;margin-right: -15px;margin-left: -15px;">
		<div class="row">
	        <div class="col-lg-12 col-sm-12 col-xs-12">
					<!-- Liste des ecritureBanques -->
					<table  width="100%" id="ect_table">
						<tr>
							<th style="background-color: #D8D8D8" align="left">Libell&eacute;</th>
							<th style="background-color: #D8D8D8;width: 150px;text-align: right;">D&eacute;bit</th>
							<th style="background-color: #D8D8D8;width: 150px;text-align: right;">Credit</th>
							<th style="background-color: #D8D8D8;width: 150px;text-align: right;">Solde</th>
						</tr>
						<c:if test="${list_ecriture.size() > 0 }">	
							<tr>
								<td colspan="3" style="text-align: right;">Solde au ${dateFinSt }</td>
								<td style="text-align: right;font-weight: bold;"><fmt:formatDecimal value="${soldeActuelDate }"/></td>
							</tr>
						</c:if>	
						
						<c:set var="oldDate" value="${null }" />
						<c:set var="solde" value="${soldeActuelDate }" />
						<c:set var="soldeDebitDay" value="${0 }" />
						<c:set var="soldeCreditDay" value="${0 }" />
						
							<c:forEach items="${list_ecriture }" var="ecritureBanque">
								<c:if test="${empty oldDate or !dateUtil.dateToString(oldDate).equals(dateUtil.dateToString(ecritureBanque.date_mouvement))}"> 
									<c:if test="${not empty oldDate}">
										<tr>
											<td align="right" style="font-size: 15px;font-weight: bold;color:green;background-color: #daebef;">
											</td> 
											<td align="right" style="font-size: 15px;font-weight: bold;color:red;background-color: #daebef;">
												 - <fmt:formatDecimal value="${soldeDebitDay }"/> 
											</td>
											<td align="right" style="font-size: 15px;font-weight: bold;color:green;background-color: #daebef;">
												 + <fmt:formatDecimal value="${soldeCreditDay }"/> 
											</td>
											<td align="right" style="font-size: 15px;font-weight: bold;color:green;background-color: #daebef;">
												
											</td>
										</tr>
										
										<c:set var="soldeDebitDay" value="${0 }" />
										<c:set var="soldeCreditDay" value="${0 }" />
									</c:if>	
								     <tr>
										<td colspan="4" noresize="true" class="separator-group">
											<fmt:formatDate value="${ecritureBanque.date_mouvement }"/> 
										</td>
									</tr>
								</c:if>
								
									<!-- Solde jour  -->	
									<c:if test="${ecritureBanque.sens=='D' }">								
										<c:set var="soldeDebitDay" value="${soldeDebitDay + ecritureBanque.montant }" />
										<c:set var="soldeDebit" value="${soldeDebit + ecritureBanque.montant }" />
									</c:if>
									<c:if test="${ecritureBanque.sens=='C' }">
										<c:set var="soldeCreditDay" value="${soldeCreditDay + ecritureBanque.montant }" />
										<c:set var="soldeCredit" value="${soldeCredit + ecritureBanque.montant }" />
									</c:if>
								<c:set var="oldDate" value="${ecritureBanque.date_mouvement }" />		
							
								<tr>
									<td style="padding-left: 20px;">${ecritureBanque.libelle}</td>
									<td align="right" style="color:red;">
										<c:if test="${ecritureBanque.sens=='D' }">
										 	- <fmt:formatDecimal value="${ecritureBanque.montant}"/>
										 	<c:set var="solde" value="${solde-ecritureBanque.montant}"/>
										</c:if> 
									</td>
									<td align="right" style="color:green;">
										<c:if test="${ecritureBanque.sens=='C' }">
										 	+ <fmt:formatDecimal value="${ecritureBanque.montant}"/>
										 	<c:set var="solde"  value="${solde+ecritureBanque.montant}"/>
										</c:if> 
									</td>
									<td align="right" style="color:green;">
										<fmt:formatDecimal value="${solde}"/>
									</td>
								</tr>
							</c:forEach>
							
							<c:if test="${not empty oldDate}">
								<tr>
									<td align="right" style="font-size: 15px;font-weight: bold;color:green;background-color: #daebef;">
									</td> 
									<td align="right" style="font-size: 15px;font-weight: bold;color:red;background-color: #daebef;">
										 - <fmt:formatDecimal value="${soldeDebitDay }"/> 
									</td>
									<td align="right" style="font-size: 15px;font-weight: bold;color:green;background-color: #daebef;">
										 + <fmt:formatDecimal value="${soldeCreditDay }"/> 
									</td>
									<td align="right" style="font-size: 15px;font-weight: bold;color:green;background-color: #daebef;">
										 
									</td>
								</tr>
							
							
								<tr>
<!-- 									<td align="right" style="font-size: 15px;font-weight: bold;color:green;background-color: orange;"> -->
										
<!-- 									</td>  -->
<!-- 									<td align="right" style="font-size: 15px;font-weight: bold;color:red;background-color: orange;"> -->
<%-- 										 	- <fmt:formatDecimal value="${soldeDebit }"/>  --%>
<!-- 									</td> -->
									<td align="right" colspan="3">
<%-- 										 	+ <fmt:formatDecimal value="${soldeCredit }"/>  --%>
											Solde au ${dateDebutSt } 
									</td>
									<td align="right" style="font-size: 15px;font-weight: bold;color:green;">
											<fmt:formatDecimal value="${solde }"/>								
									</td>
								</tr>
							</c:if>	
						</table>
					</div>
				</div>
		</div>
	</std:form>
	</div>
	<!-- end widget content -->
