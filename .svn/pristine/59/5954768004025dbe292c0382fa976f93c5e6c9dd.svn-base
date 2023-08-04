<%@page import="appli.model.domaine.util_srv.raz.RazService"%>
<%@page import="framework.controller.Context"%>
<%@page import="appli.model.domaine.stock.service.impl.RepartitionBean"%>
<%@page import="java.math.BigInteger"%>
<%@page import="java.util.Arrays"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="appli.model.domaine.stock.persistant.FamillePersistant"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
	boolean isDetailJourneeDroit = Context.isOperationAvailable("DETJRN");
%>

<style>
	#tab_rep tr:hover{
		background: #DBEDF3;
	}
	#tab_rep td {
		border-bottom: 1px dashed #ff9900;
		padding-right: 5px;
	}
</style>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de la journ&eacute;</li>
		<li class="active">R&eacute;parition des ventes</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
	<%if(ControllerUtil.getMenuAttribute("IS_MNU", request) == null) { %>
		<std:link classStyle="btn btn-default" action="caisse.journee.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
	<%} %>
	</div>
</div>
<!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>

		<std:form name="search-form">
         <div class="widget-body">
			<div class="row" style="margin-left: 10px;">
<%if(ControllerUtil.getMenuAttribute("IS_MNU", request) != null) { %>
	<div class="row">
        <div class="form-group">
        	<std:label classStyle="control-label col-md-2" value="Date d&eacute;but" />
            <div class="col-md-2">
                 <std:date name="dateDebut" required="true" value="${dateDebut }"/>
            </div>
            <div class="col-md-2" style="text-align: center;">
            	<std:link action="caisse.journee.find_repartition" params="prev=1&is_fltr=1" icon="fa fa-arrow-circle-left" tooltip="Journ&eacute;e pr&eacute;c&eacute;dente" />
            	<std:link action="caisse.journee.find_repartition" params="next=1&is_fltr=1" icon="fa fa-arrow-circle-right" tooltip="Journ&eacute;e suivante" />
            </div>
            
            <std:label classStyle="control-label col-md-1" value="Date fin" />
            <div class="col-md-2">
                 <std:date name="dateFin" required="true" value="${dateFin }"/>
            </div>
       </div>
       <div class="form-group">
	       <std:label classStyle="control-label col-md-2" value="Famille" />
			<div class="col-md-7">	 
				<std:select type="string" name="curr_famille" data="${list_famille }" key="id" labels="libelle" width="100%;" value="${curr_famille }" isTree="true" />
			</div>
			<div class="col-md-2">
           	 	<std:button action="caisse.journee.find_repartition" value="Filtrer" params="is_fltr=1" classStyle="btn btn-primary" />
           	 </div>	
		</div>	
   </div>
   <hr>
<%} else{ %>   
   <div class="row" style="height: 50px;margin-top: 7px;">
   	 <div class="col-md-8">
		<div class="col-lg-2 col-md-2">
			<span style="float: left;margin-top: 5px;">Famille &nbsp;</span>
		</div>
		<div class="col-lg-9 col-md-9">	 
			<std:select type="string" name="curr_famille" data="${list_famille }" key="id" labels="libelle" width="100%;" value="${curr_famille }" isTree="true" />
		</div>
		<div class="col-lg-1 col-md-1">
			<a href="javascript:" class="btn btn-sm shiny btn-palegreen" wact="<%=EncryptionUtil.encrypt("caisse.journee.find_repartition")%>" params="isFltr=1" style="margin-top: 4px;position: absolute;left: 0px;">
				<img src="resources/framework/img/refresh.png"/> Filtrer
			</a>
		</div>	
	  </div>
	</div>	
	<hr>
<%} %>	
   <div class="row" style="width: 100%;">
   		<div class="alert alert-warning fade in">
             <button class="close" data-dismiss="alert">
                 x
             </button>
             <i class="fa-fw fa fa-warning"></i>
             <span>
             	Les montants affichés sont à titre indicatif. Ils ne prennent pas en compte le tarif 
             	dans un menu ni les offres globales sur les commandes ni l'heure de début et de fin de la journée.<br>
             	Pour les montants exactes, veuillez consulter la liste des journées via 
             	
             	<a href="#lmnu=cai-journee&rdm=qnqj" style="color: blue;">
             		CE LIEN
				</a>
             </span>
        </div>     
   </div>
   	
   <div class="row">
        <div class="col-md-7" style="margin-left: -13px;">
        	<div class="widget">
				<div class="widget-header ">
					<span class="widget-caption">D&eacute;tail des ventes </span>
				</div>
				<div class="widget-body">
   
<%
Map mapData = (Map)request.getAttribute("dataRepartion");

Map<Long, RepartitionBean> mapArticle = (Map<Long, RepartitionBean>)mapData.get("ARTS");
BigDecimal mttOffre = (BigDecimal)mapData.get("OFFRE");

if(mapArticle.size()==0){%>
	<span style="padding-left: 20px;">Aucune donn&eacute;e &agrave; afficher.</span>
<%} else{%>

	<!-- Liste des articles -->
	<table style="background-color: white;width: 100%;border: 1px solid #2196F3;" id="tab_rep">
		<tr style="background-color: #c6cec6;">
			<th>ARTICLE</th>
			<th style="text-align: right;width: 70px;">QUANTITE</th>
			<th style="text-align: right;width: 70px;">MONTANT</th>
		</tr>
		<%
		BigDecimal mttTotalArticle = null;
		String oldFamille = null;
		for(Long menuId : mapArticle.keySet()){
			RepartitionBean data = mapArticle.get(menuId);
			//
			mttTotalArticle = BigDecimalUtil.add(mttTotalArticle, data.getMontant());
			
			String qteHM = RazService.getQteFormatted(data.getQuantite());
			%>
			<%
			if(oldFamille == null || !oldFamille.equals(data.getFamille())){%>
			<tr>
				<td colspan="3" style="font-weight: bold;"><%=data.getFamille() %></td>
			</tr>	
			<%} %>
			<tr>
				<td style="color: #777;padding-left: 25px;"><%=data.getLibelle() %></td>
				<td style="text-align: right;color: black;"><%=qteHM %></td>
				<td style="text-align: right;color: black;"><%=BigDecimalUtil.formatNumberZero(data.getMontant()) %></td>
			</tr>
			<%
			oldFamille = data.getFamille(); 
		}
		%>
		<tr style="line-height: 50px;">
			<td align="right" style="font-weight: bold;font-size: 16px;">TOTAL</td>
			<td></td>
			<td style="text-align: right;">
				<span style="font-size: 20px !important;font-weight: bold;height: 28px;" class="badge badge-green">
					<%=BigDecimalUtil.formatNumberZero(mttTotalArticle) %>	
	 			</span>
			</td>
		</tr>
		<tr style="height: 34px;">
			<td align="right" style="font-weight: bold;font-size: 13px;">TOTAL REDUCTIONS</td>
			<td style="text-align: right;" colspan="2">
				<span style="font-size: 14px !important;font-weight: bold;height: 20px;color: red;" class="badge badge-green">
					<%=BigDecimalUtil.formatNumberZero(mttOffre!=null?mttOffre.negate():BigDecimalUtil.get(0)) %>	
	 			</span>
			</td>
		</tr>
		<tr style="line-height: 50px;">
			<td align="right" style="font-weight: bold;font-size: 16px;">TOTAL NET VENTE</td> 
			<td style="text-align: right;" colspan="2">
				<span style="font-size: 20px !important;font-weight: bold;height: 28px;" class="badge badge-green">
					<%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.substract(mttTotalArticle, mttOffre)) %>	
	 			</span>
			</td>
		</tr>
	</table>
<%} %>	
	</div>
	</div>
	</div>
	<div class="col-md-5"> 
		<div class="widget">
			<div class="widget-header ">
				<span class="widget-caption">R&eacute;partition des ventes </span>
			</div>
			<div class="widget-body">
				<div class="row" id="repartition_div">
					<jsp:include page="/domaine/dashboard_erp/dashboard_repartition_include.jsp"></jsp:include>
				</div>	
			</div>
		</div>
	</div>
</div>
</div>
</div>
</std:form>
</div>
