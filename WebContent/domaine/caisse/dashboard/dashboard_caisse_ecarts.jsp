<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>

<%@page errorPage="/commun/error.jsp"%> 

<%
	List<Object[]> ecartEvolution = (List<Object[]>)request.getAttribute("ecartEvol");
List<Object[]> livraisonEvolution = (List<Object[]>)request.getAttribute("livraisonEvol");
BigDecimal fraisLivraison = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig("FRAIS_LIVRAISON"));
BigDecimal fraisLivraisonPart = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig("FRAIS_LIVRAISON_PART"));
fraisLivraison = (fraisLivraison == null) ? BigDecimalUtil.ZERO : fraisLivraison;
fraisLivraisonPart = (fraisLivraisonPart == null) ? BigDecimalUtil.ZERO : fraisLivraisonPart;
String devise = StrimUtil.getGlobalConfigPropertie("devise.symbole");
%>

		<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
			<div class="well with-header" style="height: 470px;">
				<div class="header bordered-darkorange" style="margin-left: 0px;">
					Ecarts caisses par employ&eacute;
				</div>
				<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 400px;">
					<div class="row">
						<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
							<span style="float: left;margin-top: 5px;">De &nbsp;</span>
							<std:date name="ecart_dt_debut" value="${curr_ecart_dtDebut }"/>
						</div>
						<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
							<span style="float: left;margin-top: 5px;">A &nbsp;</span>
							<std:date name="ecart_dt_fin" value="${curr_ecart_dtFin }"/>
						</div>
						<div class="col-lg-2 col-md-2 col-sm-2 col-xs-2">
							<a href="javascript:" wact="<%=EncryptionUtil.encrypt("dash.dashCaisse.loadDataEcart")%>" targetDiv="ecart_div" style="margin-top: 4px;position: absolute;left: 0px;">
								<img src="resources/framework/img/refresh.png"/>
							</a>
						</div>
					</div>
					<div class="row" style="margin-top: 11px;margin-left: 0px;max-height: 460px;overflow: auto;">
						<%if(ecartEvolution == null || ecartEvolution.size() == 0){ %>
						<span style="font-size: 13px;line-height: 50px;font-style: italic;color: gray;">Aucun &eacute;cart.</span>
					<%} else{ %>
					<table style="width: 95%;" class="table table-hover">
						<thead class="bordered-darkorange">
							<tr class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="font-size: 12px;">
								<th>EMPLOYE</th>
								<th style="width: 120px;text-align: right;">ECART NEGATIF </th>
								<th style="width: 120px;text-align: right;">ECART POSITIF</th>
								<th style="width: 120px;text-align: right;">SOLDE</th>
							</tr>
						</thead>
						<tbody>	
						<%
							BigDecimal totalPositif = null, totalNegatif = null, totalSolde = null;
							for(Object[] data : ecartEvolution){
								BigDecimal mtt_ecart_positif = (BigDecimal)data[0];
								BigDecimal mtt_ecart_negatif = (BigDecimal)data[1];
								BigDecimal mtt_solde = BigDecimalUtil.add(mtt_ecart_negatif, mtt_ecart_positif);
								String libelle = (String)data[2];
								
								totalPositif = BigDecimalUtil.add(mtt_ecart_positif, totalPositif);
								totalNegatif = BigDecimalUtil.add(mtt_ecart_negatif, totalNegatif);
								totalSolde = BigDecimalUtil.add(mtt_solde, totalSolde);
							%>   
						<tr class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="font-size: 11px;line-height: 20px;">
							<td style="text-align: left;text-transform: uppercase;"><%=StringUtil.isEmpty(libelle)?"SUPER ADMIN":libelle %></td> 
							<td style="text-align: right;color:red !important;"> <%=BigDecimalUtil.formatNumberZeroBd(mtt_ecart_negatif) %></td>
							<td style="text-align: right;color: green !important;"> <%=BigDecimalUtil.formatNumberZeroBd(mtt_ecart_positif) %></td>
							<td style="text-align: right;color: <%=mtt_solde.compareTo(BigDecimalUtil.ZERO)>=0?"green":"red"%> !important;"> <%=BigDecimalUtil.formatNumberZeroBd(mtt_solde) %></td>
						</tr>
						<%} %>
						<tr style="line-height: 40px;">
							<td style="text-align: left;font-weight: bold;font-size: 13px;color: blue;">Total</td> 
							<td style="text-align: right;font-weight: bold;font-size: 13px;color: blue;color: red !important;"> <%=BigDecimalUtil.formatNumberZero(totalNegatif)%></td>
							<td style="text-align: right;font-weight: bold;font-size: 13px;color: blue;color: green !important;"> <%=BigDecimalUtil.formatNumberZero(totalPositif)%></td>
							<td style="text-align: right;font-weight: bold;font-size: 13px;color: blue;color: <%=(totalSolde==null||totalSolde.compareTo(BigDecimalUtil.ZERO)>=0)?"green":"red"%> !important;"> <%=BigDecimalUtil.formatNumberZero(totalSolde)%></td>
						</tr>
						</tbody>
					</table>
					<%} %>
					</div>
				</div>
			</div>
		</div>		
		<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
			<div class="well with-header" style="height: 470px;">
				<div class="header bordered-darkorange">Montants livraison par employ&eacute;</div>
				<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 350px;">
					<div class="row">
						<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
							<span style="float: left;margin-top: 5px;">De &nbsp;</span>
							<std:date name="liv_dt_debut" value="${curr_liv_dtDebut }"/>
						</div>
						<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
							<span style="float: left;margin-top: 5px;">A &nbsp;</span>
							<std:date name="liv_dt_fin" value="${curr_liv_dtFin }"/>
						</div>
						<div class="col-lg-2 col-md-2 col-sm-2 col-xs-2">
							<a href="javascript:" wact="<%=EncryptionUtil.encrypt("dash.dashCaisse.loadDataEcart")%>" targetDiv="ecart_div" style="margin-top: 4px;position: absolute;left: 0px;">
								<img src="resources/framework/img/refresh.png"/>
							</a>
						</div>
					</div>
					<div class="row" style="margin-top: 11px;margin-left: 0px;max-height: 320px;overflow: auto;">
						<%if(livraisonEvolution == null || livraisonEvolution.size() == 0){ %>
					<span style="font-size: 13px;line-height: 50px;font-style: italic;color: gray;">Aucune livraison.</span>
				<%} else{ %>
				<table style="width: 95%;" class="table table-hover">
					<thead class="bordered-darkorange">
						<tr class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="font-size: 12px;">
							<th>EMPLOYE</th>
							<th style="width: 120px;text-align: right;">NBR LIVRAISON </th>
							<th style="width: 120px;text-align: right;">TOTAL</th>
						</tr>
					</thead>
					<tbody>	
					<%
						BigDecimal total_nbr_livraison = null, total_mtt_livraison = null;
						for(Object[] data : livraisonEvolution){
							BigDecimal nbr_livraison = (BigDecimal)data[0];
							BigDecimal mtt_livraison = BigDecimalUtil.multiply(fraisLivraison, nbr_livraison);
							String libelle = (String)data[1];
							
							total_nbr_livraison = BigDecimalUtil.add(nbr_livraison, total_nbr_livraison);
							total_mtt_livraison = BigDecimalUtil.add(mtt_livraison, total_mtt_livraison);
						%>   
					<tr class="databox-row row-2 bordered-bottom bordered-ivory padding-10" style="font-size: 11px;line-height: 20px;">
						<td style="text-align: left;text-transform: uppercase;"><%=StringUtil.isEmpty(libelle)?"SUPER ADMIN":libelle %></td> 
						<td style="text-align: right;color:blue !important;"> <%=BigDecimalUtil.formatNumberZeroBd(nbr_livraison) %></td>
						<td style="text-align: right;color: green !important;"> <%=BigDecimalUtil.formatNumberZeroBd(mtt_livraison) %></td>
					</tr>
					<%} %>
					<tr style="line-height: 40px;">
						<td style="text-align: left;font-weight: bold;font-size: 13px;color: blue;">Total</td> 
						<td style="text-align: right;font-weight: bold;font-size: 13px;color: blue;color: blue !important;"> <%=BigDecimalUtil.formatNumberZero(total_nbr_livraison)%></td>
						<td style="text-align: right;font-weight: bold;font-size: 13px;color: blue;color: green !important;"> <%=BigDecimalUtil.formatNumberZero(total_mtt_livraison)%></td>
					</tr>
					</tbody>
				</table>
				<span style="font-size: 11px;color: orange;">
					* Tarif livraison : <%=BigDecimalUtil.formatNumberZeroBd(fraisLivraison) %> <%=devise %>
					<%if(!BigDecimalUtil.isZero(fraisLivraisonPart)){ %>
					- Part soci&eacute;t&eacute; : <%=BigDecimalUtil.formatNumberZeroBd(fraisLivraisonPart) %> <%=devise %>
					<%} %>
					</span>
				<%} %>
							</div>
						</div>
					</div>
				</div>
