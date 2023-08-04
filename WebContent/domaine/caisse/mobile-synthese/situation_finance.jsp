<%@page import="framework.model.common.util.StrimUtil"%>
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
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page import="framework.controller.Context"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
	#tab_rep td{
		padding-right: 5px;
	}
</style>
<%
	BigDecimal mttTotalAll = null;
	Map mapData = (Map)request.getAttribute("dataRepartion");
	
	Map<Long, RepartitionBean> mapMenu = (Map<Long, RepartitionBean>)mapData.get("MENU");
	if(mapMenu == null){
		mapMenu = new HashMap<>();
	}
	Map<Long, RepartitionBean> mapMenuArticle = (Map<Long, RepartitionBean>)mapData.get("MENU_ARTS");
	if(mapMenuArticle == null){
		mapMenuArticle = new HashMap<>();
	}
	Map<Long, RepartitionBean> mapArticle = (Map<Long, RepartitionBean>)mapData.get("ARTS");
	if(mapArticle == null){
		mapArticle = new HashMap<>();
	}
	BigDecimal mttOffre = (BigDecimal)mapData.get("OFFRE");
	BigDecimal mttLivraison = (BigDecimal)mapData.get("LIVRAISON");
	BigDecimal mttRecharge = (BigDecimal)mapData.get("RECHARGE");
%>

   <div class="row" style="margin-left: 10px;margin-right: 13px;">
	   <div class="col-md-12"> 
			<div class="row" style="padding-left: 10px;">
				<table>
					<tr>
						<td><h4>Total articles et menus :</h4></td>
						<td style="text-align:right;"><h4><span style="color: blue;"><%=BigDecimalUtil.formatNumberZero(mttTotalAll) %></span></h4></td>
					</tr>
					<tr>
						<td><h4>Frais de livraisons :</h4></td>
						<td style="text-align:right;"><h4><span style="color: blue;"><%=BigDecimalUtil.formatNumberZero(mttLivraison) %></span></h4></td>
					</tr>	
					<tr>	
						<td><h4>Offres et réduction :</h4></td>
						<td style="text-align:right;"><h4><span style="color: blue;"><%=BigDecimalUtil.formatNumberZero(mttOffre) %></span></h4></td>
					</tr>
					<tr>
						<td><h4>Recharge portefeuille :</h4></td>
						<td style="text-align:right;"><h4><span style="color: blue;"><%=BigDecimalUtil.formatNumberZero(mttRecharge) %></span></h4></td>
					</tr>
					<tr>
						<td><h4>Ventes net :</h4></td>
						<td style="text-align:right;"><h4><span style="color: blue;font-weight: bold;"><%=BigDecimalUtil.formatNumber(BigDecimalUtil.add(BigDecimalUtil.substract(mttTotalAll, mttLivraison, mttOffre), mttRecharge)) %></span></h4></td>
					</tr>	
				</table>
				<br>
			</div>
			<div class="row">
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
	
        <div class="col-md-12" style="margin-left: -13px;">
        	<div class="widget">
				<div class="widget-header ">
					<span class="widget-caption">D&eacute;tail des ventes </span>
				</div>
				<div class="widget-body">
<%
if(mapMenu.size()==0 && mapArticle.size()==0){%>
	<span style="padding-left: 20px;">Aucune donn&eacute;e &agrave; afficher.</span>
<%} else{%>

	<!-- Liste des articles -->
	<table style="background-color: white;width: 100%;border: 1px solid #2196F3;font-size: 11px;" id="tab_rep">
		<tr style="background-color: #c6cec6;">
			<th></th>
			<th style="text-align: right;width: 100px;">Qte</th>
			<th style="text-align: right;width: 100px;">Montant</th>
		</tr>
		
<%if(mapMenu.size() > 0){ %>		
		<tr style="background-color: #fff59d;line-height: 37px;">
			<th colspan="3" style="font-weight: bold;font-size: 19px;">LES MENUS</th>
		</tr>	
		<%
		BigDecimal mttTotalMenu = BigDecimalUtil.ZERO, qteTotalMnu = BigDecimalUtil.ZERO;
		
		for(Long menuId : mapMenu.keySet()){
			RepartitionBean data = mapMenu.get(menuId);
			%>
			<tr>
				<td style="color: #777;padding-left: 25px;"><%=data.getLibelle() %></td>
				<td style="text-align: right;color: black;"><%=data.getQuantite() %></td>
				<td style="text-align: right;color: black;"><%=BigDecimalUtil.formatNumberZero(data.getMontant()) %></td>
			</tr>
			<%
			mttTotalMenu = BigDecimalUtil.add(mttTotalMenu, data.getMontant());
			qteTotalMnu = BigDecimalUtil.add(qteTotalMnu, data.getQuantite());
			
			mttTotalAll = BigDecimalUtil.add(mttTotalAll, data.getMontant());
		}
		%>
			<tr style="background-color: #fdf8c9;font-size: 12px;">
				<td style="color: #777;padding-left: 5px;text-align:right;">TOTAL</td>
				<td style="text-align: right;color: black;font-weight: bold;"><%=qteTotalMnu.intValue() %></td>
				<td style="text-align: right;color: black;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(mttTotalMenu) %></td>
			</tr>
<%} %>
		<!-- ********************************************************************************************* -->
		
		
<%
String oldFamille = null;
BigDecimal mttTotalArtBloc = BigDecimalUtil.ZERO, qteTotalArtBloc = BigDecimalUtil.ZERO;
BigDecimal mttTotalArtAll = BigDecimalUtil.ZERO, qteTotalArtAll = BigDecimalUtil.ZERO;

if(mapMenuArticle.size() > 0){ %>		
		<tr>
			<th colspan="3">&nbsp;</th>
		</tr>	
		<tr style="background-color: #fff59d;line-height: 37px;">
			<th colspan="3" style="font-weight: bold;font-size: 19px;">LES ARTICLES MENUS</th>
		</tr>	
<%
		for(Long menuId : mapMenuArticle.keySet()){
			RepartitionBean data = mapMenuArticle.get(menuId);
			%>
			<%
			if(oldFamille == null || !oldFamille.equals(data.getFamille())){%>
			<!-- Sous total -->
				<%if(oldFamille != null){ %>
				<tr style="font-weight: bold;color: #FF5722;background-color: #fff385;">
					<td style="text-align: right;">SOUS TOTAL</td>
					<td style="font-weight: bold;text-align: right;"><%=qteTotalArtBloc.intValue() %></td>
					<td style="font-weight: bold;text-align: right;"><%=BigDecimalUtil.formatNumberZero(mttTotalArtBloc) %></td>
				</tr>
				<%
				mttTotalArtBloc = null;
				qteTotalArtBloc = null;
				} %>
				
			<tr>
				<td colspan="3" style="font-weight: bold;"><%=data.getFamille() %></td>
			</tr>	
			<%} %>
			<tr>
				<td style="color: #777;padding-left: 25px;"><%=data.getLibelle() %></td>
				<td style="text-align: right;color: black;"><%=(data.getQuantite()!=null?data.getQuantite().intValue():0) %></td>
				<td style="text-align: right;color: black;"><%=BigDecimalUtil.formatNumberZero(data.getMontant()) %></td>
			</tr>
			<%
			oldFamille = data.getFamille();
			mttTotalArtBloc = BigDecimalUtil.add(mttTotalArtBloc, data.getMontant());
			qteTotalArtBloc = BigDecimalUtil.add(qteTotalArtBloc, data.getQuantite());
			
			mttTotalArtAll = BigDecimalUtil.add(mttTotalArtAll, data.getMontant());
			qteTotalArtAll = BigDecimalUtil.add(qteTotalArtAll, data.getQuantite());
			
			mttTotalAll = BigDecimalUtil.add(mttTotalAll, data.getMontant());
		}
		%>
		<!-- Sous total -->
		<%if(oldFamille != null){ %>
			<tr style="font-weight: bold;color: #FF5722;background-color: #fff385;">
				<td style="text-align: right;">SOUS TOTAL</td>
				<td style="font-weight: bold;text-align: right;"><%=qteTotalArtBloc.intValue() %></td>
				<td style="font-weight: bold;text-align: right;"><%=BigDecimalUtil.formatNumberZero(mttTotalArtBloc) %></td>
			</tr>
		<%} %>	


		
		<!-- CUMUL DES ARTICLES -->
		<tr>
			<th colspan="3">&nbsp;</th>
		</tr>
		<tr style="background-color: #fdf8c9;font-size: 12px;">
			<td style="color: #777;padding-left: 5px;text-align:right;">TOTAL</td>
			<td style="text-align: right;color: black;font-weight: bold;"><%=qteTotalArtAll.intValue() %></td>
			<td style="text-align: right;color: black;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(mttTotalArtAll) %></td>
		</tr>		
<%} %>
		<!-- ***************************************************************************************************** -->

<%if(mapArticle.size() > 0){ %>
		<tr>
			<th colspan="3">&nbsp;</th>
		</tr>	
		<tr style="background-color: #fff59d;line-height: 37px;">
			<th colspan="3" style="font-weight: bold;font-size: 19px;">LES ARTICLES</th>
		</tr>	
		
		<%
		oldFamille = null;
		mttTotalArtBloc = BigDecimalUtil.ZERO; qteTotalArtBloc = BigDecimalUtil.ZERO;
		mttTotalArtAll = BigDecimalUtil.ZERO; qteTotalArtAll = BigDecimalUtil.ZERO;
		
		for(Long menuId : mapArticle.keySet()){
			RepartitionBean data = mapArticle.get(menuId);
			%>
			<%
			if(oldFamille == null || !oldFamille.equals(data.getFamille())){%>
			<!-- Sous total -->
				<%if(oldFamille != null){ %>
				<tr style="font-weight: bold;color: #FF5722;background-color: #fff385;">
					<td style="text-align: right;">SOUS TOTAL</td>
					<td style="font-weight: bold;text-align: right;"><%=qteTotalArtBloc.intValue() %></td>
					<td style="font-weight: bold;text-align: right;"><%=BigDecimalUtil.formatNumberZero(mttTotalArtBloc) %></td>
				</tr>
				<%
				mttTotalArtBloc = null;
				qteTotalArtBloc = null;
				} %>
				
			<tr>
				<td colspan="3" style="font-weight: bold;"><%=data.getFamille() %></td>
			</tr>	
			<%} %>
			<tr>
				<td style="color: #777;padding-left: 25px;"><%=data.getLibelle() %></td>
				<td style="text-align: right;color: black;"><%=(data.getQuantite()!=null?data.getQuantite().intValue():0) %></td>
				<td style="text-align: right;color: black;"><%=BigDecimalUtil.formatNumberZero(data.getMontant()) %></td>
			</tr>
			<%
			oldFamille = data.getFamille();
			mttTotalArtBloc = BigDecimalUtil.add(mttTotalArtBloc, data.getMontant());
			qteTotalArtBloc = BigDecimalUtil.add(qteTotalArtBloc, data.getQuantite());
			
			mttTotalArtAll = BigDecimalUtil.add(mttTotalArtAll, data.getMontant());
			qteTotalArtAll = BigDecimalUtil.add(qteTotalArtAll, data.getQuantite());
			
			mttTotalAll = BigDecimalUtil.add(mttTotalAll, data.getMontant());
		}
		%>
		<!-- Sous total -->
		<%if(oldFamille != null){ %>
			<tr style="font-weight: bold;color: #FF5722;background-color: #fff385;">
				<td style="text-align: right;">SOUS TOTAL</td>
				<td style="font-weight: bold;text-align: right;"><%=qteTotalArtBloc.intValue() %></td>
				<td style="font-weight: bold;text-align: right;"><%=BigDecimalUtil.formatNumberZero(mttTotalArtBloc) %></td>
			</tr>
		<%} %>	
		<!-- CUMUL DES ARTICLES -->
		<tr>
			<th colspan="3">&nbsp;</th>
		</tr>
		<tr style="background-color: #fdf8c9;font-size: 12px;">
			<td style="color: #777;padding-left: 5px;text-align:right;">TOTAL</td>
			<td style="text-align: right;color: black;font-weight: bold;"><%=qteTotalArtAll.intValue() %></td>
			<td style="text-align: right;color: black;font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(mttTotalArtAll) %></td>
		</tr>
<%} %>		
		
		<tr style="background-color: #fdf8c9;font-size: 12px;">
			<td style="color: #777;padding-left: 5px;text-align:right;"></td>
			<td style="text-align: right;color: black;font-weight: bold;"></td>
			<td style="text-align: right;color: black;font-weight: bold;font-size: 16px;"><%=BigDecimalUtil.formatNumberZero(mttTotalAll) %></td>
		</tr>
		
		
	</table>
	<hr>
	
	
	
<%if(mapMenuArticle.size() > 0){ %>	
	
	<table style="background-color: white;width: 100%;border: 1px solid #2196F3;font-size: 11px;" id="tab_rep">
		<tr style="background-color: #fff59d;line-height: 37px;">
			<th colspan="4" style="font-weight: bold;font-size: 19px;">LES ARTICLES EN DETAIL [menu et hors menu]</th>
		</tr>	
		
		<tr style="background-color: #c6cec6;">
			<th></th>
			<th style="text-align: right;width: 50px;">Qte hors menu</th>
			<th style="text-align: right;width: 50px;">Qte menu</th>
			<th style="text-align: right;width: 50px;">Total</th>
		</tr>
	
		<%
		BigDecimal qteTotalArticleALL = BigDecimalUtil.ZERO;
		// Ajout articles menus manquants
		for(Long artId : mapMenuArticle.keySet()){
			if(mapArticle.get(artId) == null){
				mapArticle.put(artId, mapMenuArticle.get(artId));
			}
		}
		
		//
		for(Long menuId : mapArticle.keySet()){
			RepartitionBean data = mapArticle.get(menuId);
			RepartitionBean dataMnu = mapMenuArticle.get(menuId);
			
			BigDecimal qteHorsMnu = (data!=null && data.getQuantite()!=null ? data.getQuantite():BigDecimalUtil.get(0));
			BigDecimal qteMnu = (dataMnu!=null && dataMnu.getQuantite()!=null ? dataMnu.getQuantite() : BigDecimalUtil.get(0));
			
			if(oldFamille == null || !oldFamille.equals(data.getFamille())){%>
				<tr>
					<td colspan="7" style="font-weight: bold;"><%=data.getFamille() %></td>
				</tr>	
			<%}%>
			
			<tr>
				<td style="color: #777;padding-left: 25px;"><%=data.getLibelle() %></td>
				<td style="color: #777;padding-left: 25px;"><%=qteHorsMnu.intValue() %></td>
				<td style="text-align: right;color: black;"><%=qteMnu.intValue() %></td>
				<td style="text-align: right;color: black;"><%=BigDecimalUtil.add(qteHorsMnu, qteMnu).intValue() %></td>
			</tr>
			<%
			oldFamille = data.getFamille();
		}
		%>
	</table>
<%} %>

<%} %>
	
	</div>
	</div>
	</div>
	
</div>
