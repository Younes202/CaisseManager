<%@page import="java.util.Arrays"%>
<%@page import="java.math.BigInteger"%>
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

<style>
	#tab_rep tr:hover{
		background: #DBEDF3;
	}
	#tab_rep td {
		border-bottom: 1px dashed #ff9900;
	}
</style>
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link classStyle="btn btn-default" action="caisse.journee.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
	</div>
</div>
<!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>

	<div class="row">
		<div class="col-lg-12 col-sm-6 col-xs-12">
			<div class="widget">
				<div class="widget-main ">
					<div class="tabbable">
						<ul class="nav nav-tabs" id="myTab9">
							<li><a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("caisse.journee.work_edit")%>"> Fiche </a></li>
							<li><a id="tab_mvm" data-toggle="tab" href="#mouvementCaisse" wact="<%=EncryptionUtil.encrypt("caisse.journee.find_mouvement")%>"> Mouvements caisses </a></li>
							<li class="active"><a data-toggle="tab" href="#repartitionVente" wact="<%=EncryptionUtil.encrypt("caisse.journee.find_repartition")%>"> R&eacute;partition des ventes </a></li>
						</ul>

						<!-- row -->
						<div class="tab-content">
							<std:form name="search-form">
	
	<!-- row -->
	<div class="row" style="margin-left: 15%;">
<%
Map mapData = (Map)request.getAttribute("dataRepartion");
Map<BigInteger, Object[]> mapArticle = (Map<BigInteger, Object[]>)mapData.get("ART_MAP");
BigDecimal qteTotal = null;
BigDecimal mttTotal = null;
Map<String, BigDecimal[]> mapTotauxFamille = (Map<String, BigDecimal[]>)mapData.get("TOTAUX_FAMILLE");
Map<String, BigDecimal[]> mapTotauxMenu = (Map<String, BigDecimal[]>)mapData.get("TOTAUX_MENU");
%>

	<!-- Liste des articles -->
	<table style="background-color: white;" id="tab_rep">
		<tr>
			<th></th>
			<th style="text-align: right;width: 150px;">Qte vendue</th>
			<th style="text-align: right;width: 150px;">Montant</th>
		</tr>
		<%
		// Recensement de tous les articles disponibles
		List<BigInteger> listAllArt = new ArrayList();
		for(BigInteger key : mapArticle.keySet()){
			if(!listAllArt.contains(key)){
				listAllArt.add(key);
			}
		}
		List<String> oldFamilleStr = null;
		List<FamillePersistant> oldFamilleStrArt = null;
		int pleft = 20;
		//
		for(BigInteger artId : listAllArt){
			Object[] dataArt = mapArticle.get(artId);
			String libelle = (String)dataArt[4];
			
			qteTotal = BigDecimalUtil.add(qteTotal, (BigDecimal)(dataArt==null?null:dataArt[2]));
			mttTotal = BigDecimalUtil.add(mttTotal, (BigDecimal)(dataArt==null?null:dataArt[3]));
			
			String[] currFamilleStrM = StringUtil.getArrayFromStringDelim(libelle, "->");
			List<String> currFamilleStr = new ArrayList<String>(Arrays.asList(currFamilleStrM));
			String lib = currFamilleStr.get(currFamilleStr.size()-1);
			//
			
			for(int i=0; i<currFamilleStr.size()-1; i++){
				if(oldFamilleStr == null || (i > oldFamilleStr.size()-1) || !currFamilleStr.get(i).equals(oldFamilleStr.get(i))){
					%>
					<tr>
						<td colspan="3" class="separator-group">
							<span class="fa fa-fw fa-folder-open-o separator-icon"></span><%=currFamilleStr.get(i) %>
						</td>
					</tr>
					<%
				}
			}
			oldFamilleStr = currFamilleStr;
			%>
			
			<tr>
				<td style="color: #777;padding-left: <%=pleft%>px;"><%=lib %></td>
				<td style="text-align: right;color: black;font-weight: bold;"><%=(dataArt != null?((BigDecimal)dataArt[2]).intValue():"") %></td>
				<td style="text-align: right;color: black;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero((dataArt != null?((BigDecimal)dataArt[3]):null)) %></td>
			</tr>
<%		}
		%>
		<tr>
			<td colspan="3"><hr></td>
		</tr>
		<tr>
			<td align="right" style="font-weight: bold;font-size: 16px;">Total :</td>
			<td style="text-align: right;">
				<span style="font-size: 20px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
	 				<%=BigDecimalUtil.formatNumberZero(qteTotal) %>
	 			</span>
	 		</td>
			<td style="text-align: right;">
				<span style="font-size: 20px !important;font-weight: bold;height: 28px;" class="badge badge-green">
					<%=BigDecimalUtil.formatNumberZero(mttTotal) %>	
	 			</span>
			</td>
		</tr>
	</table>

</div>
</std:form>
</div>
</div>
</div>
</div>
</div>
</div>
</div>