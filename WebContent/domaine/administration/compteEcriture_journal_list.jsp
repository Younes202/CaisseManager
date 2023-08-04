<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
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
		<li>Ecriture compte</li>
		<li class="active">Recherche</li>
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

<c:set var="dateUtil" value="<%=new DateUtil()%>" />

<std:form name="search-form">	
	<div class="row">
		<div class="widget-body">
		<%
			if(ControllerUtil.getMenuAttribute("banqueId", request) == null){
		%>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Compte" />
					<div class="col-md-4">
		               	  <std:select name="compte.id" type="long" key="id" labels="libelle" data="${listeBanque }" required="true" addBlank="false" />
		            </div>
		            <std:label classStyle="control-label col-md-2" value="Source" />
		            <div class="col-md-4">
		            	<std:select name="compte.source" type="string" data="${sourceArray }" />
		            </div>
		        </div>
		    </div> 
		  <%
 		  	}
 		  %>
		    <div class="row">
		        <div class="form-group">
		        	<std:label classStyle="control-label col-md-2" value="Date d&eacute;but" />
		            <div class="col-md-2">
		                 <std:date name="dateDebut" required="true" value="${dateDebut }"/>
		            </div>
		            <div class="col-md-2" style="text-align: center;">
		            	<std:link action="admin.compteBancaire.find_ecriture_journal" params="prev=1&is_fltr=1" icon="fa fa-arrow-circle-left" tooltip="Journ&eacute;e pr&eacute;c&eacute;dente" />
		            	<std:link action="admin.compteBancaire.find_ecriture_journal" params="next=1&is_fltr=1" icon="fa fa-arrow-circle-right" tooltip="Journ&eacute;e suivante" />
		            </div>
		            
		            <std:label classStyle="control-label col-md-1" value="Date fin" />
		            <div class="col-md-2">
		                 <std:date name="dateFin" required="true" value="${dateFin }"/>
		            </div>
		            <div class="col-md-2">
		           	 	<std:button action="admin.compteBancaire.find_ecriture_journal" value="Filtrer" params="is_fltr=1" classStyle="btn btn-primary" />
		           	 </div>	
		       </div>
		       
		        <div class="form-group">
				      <std:label classStyle="control-label col-md-1" value="Client" />
				      <div class="col-md-3">
				           <std:select name="client" value="${client }" data="${listClient }" key="id" labels="nom;' ';prenom" type="long" placeholder="Client"/>
				      </div>
				       <std:label classStyle="control-label col-md-1" value="Fournisseir" />
				         <div class="col-md-3">
				            <std:select name="fournisseur" value="${fournisseur }" data="${listFournisseur }" key="id" labels="libelle" type="long" placeholder="Fournisseur"/>
				         </div>
				       <std:label classStyle="control-label col-md-1" value="Compte" />
				       	 <div class="col-md-3">
				       <std:select name="compte" value="${compte }" style="width: 265px;" data="${listCompte }" key="id" labels="libelle" type="long" placeholder="Compte"/>
				      </div>
		   </div>    
        </div>
	</div>
<c:set var="contextRestau" value="<%=new ContextAppli()%>" />
	
	<div class="widget-body" style="margin-top: 10px;margin-right: -15px;margin-left: -15px;">
		<div class="row">
	        <div class="col-lg-12 col-sm-12 col-xs-12">
					<!-- Liste des ecritureBanques -->
					<table  width="100%" id="ect_table">
						<tr>
							<th style="background-color: #D8D8D8" align="left">Libell&eacute;</th>
							<th style="background-color: #D8D8D8;width: 150px;text-align: right;">D&eacute;bit</th>
							<th style="background-color: #D8D8D8;width: 150px;text-align: right;">Credit</th>
						</tr>
						
							
						<c:set var="oldSource" value="${null }" />
						<c:set var="oldDate" value="${null }" />
						<c:set var="soldeDebitDay" value="${0 }" />
						<c:set var="soldeCreditDay" value="${0 }" />
						<c:set var="soldeDebitSource" value="${0 }" />
						<c:set var="soldeCreditSource" value="${0 }" />
						
							<c:forEach items="${list_ecriture }" var="ecritureBanque">
								<c:if test="${empty oldDate or !dateUtil.dateToString(oldDate).equals(dateUtil.dateToString(ecritureBanque.date_mouvement))}"> 
									<c:if test="${not empty oldDate}">
									
										<c:if test="${not empty oldSource}">
											<tr>
												<td style="font-size: 13px;font-weight: bold;color:blue;text-align: right;">
												
												</td> 
												<td align="right" style="font-size: 13px;font-weight: bold;color:red;background-color: #daebef;">
													- <fmt:formatDecimal value="${soldeDebitSource }" />
												</td>
												<td align="right" style="font-size: 13px;font-weight: bold;color:green;background-color: #daebef;">
													+ <fmt:formatDecimal value="${soldeCreditSource }" />
												</td>
											</tr>	
										</c:if>
									
									
									
										<tr>
											<td align="right" style="font-size: 15px;font-weight: bold;color:green;">
												
											</td> 
											<td align="right" style="font-size: 15px;font-weight: bold;color:red;background-color: #a0d468;">
												 - <fmt:formatDecimal value="${soldeDebitDay }"/> 
											</td>
											<td align="right" style="font-size: 15px;font-weight: bold;color:green;background-color: #a0d468;">
												 + <fmt:formatDecimal value="${soldeCreditDay }"/> 
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
									
								<c:set var="isDateDiff" value="${!dateUtil.dateToString(oldDate).equals(dateUtil.dateToString(ecritureBanque.date_mouvement)) }" />	
									
								<c:if test="${empty oldSource or oldSource != ecritureBanque.source or isDateDiff}"> 
									<c:if test="${not empty oldSource and !isDateDiff}">
										<tr>
											<td style="font-size: 13px;font-weight: bold;color:blue;text-align: right;">
												
											</td> 
											<td align="right" style="font-size: 13px;font-weight: bold;color:red;background-color: #daebef;">
												- <fmt:formatDecimal value="${soldeDebitSource }" />
											</td>
											<td align="right" style="font-size: 13px;font-weight: bold;color:green;background-color: #daebef;">
												+ <fmt:formatDecimal value="${soldeCreditSource }" />
											</td>
										</tr>	
									</c:if>
									<tr>
										<td colspan="3" style="font-size: 13px;font-weight: bold;color:blue;">
											${contextRestau.getLibelleEcriture(ecritureBanque.source) }
										</td> 
									</tr>
									
									<c:set var="soldeDebitSource" value="${0 }" />
									<c:set var="soldeCreditSource" value="${0 }" />
								</c:if>
								
								
								
								<c:if test="${ecritureBanque.sens=='D' }">								
									<c:set var="soldeDebitSource" value="${soldeDebitSource + ecritureBanque.montant }" />
								</c:if>
								<c:if test="${ecritureBanque.sens=='C' }">
									<c:set var="soldeCreditSource" value="${soldeCreditSource + ecritureBanque.montant }" />
								</c:if>
								
								
								
								
								<c:set var="oldSource" value="${ecritureBanque.source }" />
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
								</tr>
							</c:forEach>
							
							<c:if test="${not empty oldSource and !isDateDiff}">
							
								<tr>
									<td style="font-size: 13px;font-weight: bold;color:blue;text-align: right;">
										
									</td> 
									<td align="right" style="font-size: 13px;font-weight: bold;color:red;background-color: #daebef;">
										- <fmt:formatDecimal value="${soldeDebitSource }" />
									</td>
									<td align="right" style="font-size: 13px;font-weight: bold;color:green;background-color: #daebef;">
										+ <fmt:formatDecimal value="${soldeCreditSource }" />
									</td>
								</tr>	
							</c:if>
									
							<c:if test="${not empty oldDate}">
								<tr>
									<td align="right"  style="font-size: 15px;font-weight: bold;color:green;">
									
									</td> 
									<td align="right" style="font-size: 15px;font-weight: bold;color:red;background-color: #a0d468;">
										 - <fmt:formatDecimal value="${soldeDebitDay }"/> 
									</td>
									<td align="right" style="font-size: 15px;font-weight: bold;color:green;background-color: #a0d468;">
										 + <fmt:formatDecimal value="${soldeCreditDay }"/> 
									</td>
								</tr>
								<tr>
									<td align="right" style="font-size: 15px;font-weight: bold;color:green;background-color: #f8e999;">
										TOTAL
									</td> 
									<td align="right" style="font-size: 15px;font-weight: bold;color:red;background-color: #f8e999;">
										 	- <fmt:formatDecimal value="${soldeDebit }"/> 
									</td>
									<td align="right" style="font-size: 15px;font-weight: bold;color:green;background-color: #f8e999;">
										 	+ <fmt:formatDecimal value="${soldeCredit }"/> 
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
