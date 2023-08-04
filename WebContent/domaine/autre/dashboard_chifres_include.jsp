<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.model.domaine.caisse.persistant.JourneeVenteErpView"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.Context"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.math.BigDecimal"%>

<%
	String devise = StrimUtil.getGlobalConfigPropertie("devise.symbole");
JourneeVenteErpView journeeVente = (JourneeVenteErpView)request.getAttribute("journeeVente");
if(journeeVente == null){
	journeeVente = new JourneeVenteErpView();
}
boolean isShiftRight = Context.isOperationAvailable("SHIFT");
boolean isPortefeuille =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PORTEFEUILLE"));
boolean isPoints =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("POINTS"));
%>

<% 
if(isShiftRight){
%>
		
			<div class="header bordered-darkorange">R&eacute;partition des montants des ventes [
				<span style="color: green;font-style: italic;">
					<%if(journeeVente.getDate_vente() != null){ %>
						Journ&eacute;e du <%=DateUtil.dateToString(journeeVente.getDate_vente()) %>
					<%} else{ %>
						Aucune journ&eacute;e disponible
					<%} %>	
				</span>
				]
			</div>
			<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px;">
				<div class="databox-top">
					<div class="databox-row row-12" style="border-bottom: 1px solid gray;height: 122%;">
						<div class="databox-cell cell-7 text-center">
							<div class="databox-number number-xxlg sonic-silver"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_total_net())%> DH</div>
							<div class="databox-text storm-cloud">Montant total HT</div>
						</div>
						<div class="databox-cell cell-5 text-align-center">
							<div class="databox-number number-xxlg sonic-silver"><%=BigDecimalUtil.formatNumberZero(journeeVente.getNbr_vente())%></div>
							<div class="databox-text storm-cloud">Commandes<br></div>
						</div>
					</div>
				</div>
				<div class="databox-bottom">
					<div class="databox-row row-12">
						<div class="databox-cell cell-12 text-center  padding-12">
							<div id="pie-chart" class="chart chart"></div>
						</div>
					</div>
					
					<div class="databox-row row-12">	
						<div class="databox-cell cell-6 text-center no-padding-left padding-bottom-30">
							<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
								<span class="databox-text sonic-silver pull-left no-margin" style="font-weight: bold;">Type de paiement</span> <span class="databox-text sonic-silver pull-right no-margin uppercase">DH</span>
							</div>
							<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
								<span class="badge badge-empty pull-left margin-5" style="background-color: #2dc3e8;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">Especes</span> <span
									class="databox-text darkgray pull-right no-margin uppercase"> <%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_espece())%> </span>
							</div>
							<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
								<span class="badge badge-empty pull-left margin-5" style="background-color: #fb6e52;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">Ch&egrave;ques</span> <span
									class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_cheque())%> </span>
							</div>
							<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
								<span class="badge badge-empty pull-left margin-5" style="background-color: #ffce55;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">Carte bancaire</span> <span
									class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_cb())%> </span>
							</div>
							
							<%if(isPoints){ %>
							<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
								<span class="badge badge-empty pull-left margin-5" style="background-color: green;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">Points fid&eacute;lit&eacute;</span> <span
									class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_donne_point())%> </span>
							</div>
							<%} %>
							<%if(isPortefeuille){ %>
							<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
								<span class="badge badge-empty pull-left margin-5" style="background-color: #a0d468;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">Portefeuille virtuel</span> <span
									class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_portefeuille())%> </span>
							</div>
							<%} %>
						</div>
						<div class="databox-cell cell-6 text-center no-padding-left padding-bottom-30">	
							
							<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
								<span class="databox-text sonic-silver pull-left no-margin" style="font-weight: bold;">Autre</span> <span class="databox-text sonic-silver pull-right no-margin uppercase">DH</span>
							</div>
							<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
								<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">Annul&eacute;es</span> <span
									class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_annule())%> </span>
							</div>
							<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
								<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">R&eacute;ductions</span> <span
									class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_reduction())%> </span>
							</div>
							
							<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
								<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">Art. offerts</span> <span
									class="databox-text darkgray pull-right no-margin uppercase"><%=BigDecimalUtil.formatNumberZero(journeeVente.getMtt_art_offert())%> </span>
							</div>
						
							<div class="databox-row row-2 bordered-bottom bordered-ivory padding-10">
								<span class="badge badge-empty pull-left margin-5" style="background-color: #2196f3;"></span> <span class="databox-text darkgray pull-left no-margin hidden-xs">
									<b style="color: green;">Marge brute</b> <i class="fa fa-info-circle" title="Prix de vente - Prix d'achat"></i>
								</span> 
							</div>
						
						</div>
						</div>
					</div>
				</div>
<%} %>
	
<script>
        // If you want to draw your charts with Theme colors you must run initiating charts after that current skin is loaded   
        $(window).ready(function () {
        	$(document).ready(function (){
        		$('.input-group-addon, #situation_dt_debut').datepicker({
        	    	clearBtn: true,
        		    language: "fr",
        		    autoclose: true,
        		    format: "mm/yyyy",
        		    startView: 1,
        		    minViewMode: 1
        	    });
        		$('.input-group-addon, #situation_dt_debut').datepicker().on("changeDate", function(e) {
        	        var currDate = $('#dateDebut').datepicker('getFormattedDate');
        	        submitAjaxForm('<%=EncryptionUtil.encrypt("dash.dashBoard.init_situation_chiffre")%>', 'dt='+currDate, $("#search-form"), $(this));
        	    });
        	});
        	
        	
        	  /*Sets Themed Colors Based on Themes*/
            themeprimary = getThemeColorFromCss('themeprimary');// bleu
            themesecondary = getThemeColorFromCss('themesecondary');// rouge
            themethirdcolor = getThemeColorFromCss('themethirdcolor'); // jaune
            themefourthcolor = getThemeColorFromCss('themefourthcolor');
            themefifthcolor = getThemeColorFromCss('themefifthcolor');
            themesixthcolor = 'green';
        	

            //Sets The Hidden Chart Width
            $('#dashboard-bandwidth-chart')
                .data('width', $('.box-tabbs')
                    .width() - 20);
			$(".databox-cell cell-4 no-padding text-align-center bordered-right bordered-platinum").css("background-color",themefifthcolor);

//-------------------------les commandes Pie Chart----------------------------------------//
		<%BigDecimal mttReel = journeeVente.getMtt_total_net();
		mttReel = (mttReel == null || mttReel.compareTo(BigDecimalUtil.ZERO)==0) ? BigDecimalUtil.get(1) : mttReel;
		%>
		var v1= <%=BigDecimalUtil.divide(BigDecimalUtil.multiply(journeeVente.getMtt_espece(), BigDecimalUtil.get(100)), mttReel) %>;
		var v2= <%=BigDecimalUtil.divide(BigDecimalUtil.multiply(journeeVente.getMtt_cheque(), BigDecimalUtil.get(100)), mttReel)%>;
		var v3= <%=BigDecimalUtil.divide(BigDecimalUtil.multiply(journeeVente.getMtt_cb(), BigDecimalUtil.get(100)), mttReel)%>;
		var v4= <%=BigDecimalUtil.divide(BigDecimalUtil.multiply(journeeVente.getMtt_portefeuille(), BigDecimalUtil.get(100)), mttReel)%>;
		var v6= <%=BigDecimalUtil.divide(BigDecimalUtil.multiply(journeeVente.getMtt_donne_point(), BigDecimalUtil.get(100)), mttReel)%>;

		<%if(isShiftRight){%>
		        
	            var InitiatePieChart = function () {
	                return {
	                    init: function () {
	                        var data = [
	                        	{ label: "Especes", data: [[1, v1]], color: themeprimary },
	                        	{ label: "Ch&egrave;que", data: [[1, v2]], color: themesecondary },
	                        	{ label: "Carte", data: [[1, v3]], color: themethirdcolor },
	                        	{ label: "Portefeuille", data: [[1, v4]], color: themefourthcolor },
	                        	{ label: "Points", data: [[1, v6]], color: themesixthcolor }
	                        ];
	                        var placeholder = $("#pie-chart");
	                        $("#title").text("Hidden Labels");

	                        $.plot(placeholder, data, {
	                            series: {
	                                pie: {
	                                    show: true,
	                                    radius: 1,
	                                    label: {
	                                        show: true,
	                                        radius: 2 / 3,
	                                        formatter: labelFormatter,
	                                        threshold: 0.1
	                                    }
	                                }
	                            },
	                            legend: {
	                                show: false
	                            }
	                        });
	                        function labelFormatter(label, series) {
	                            return "<div style='font-size:8pt; text-align:center; padding:2px; color:white;'>" + label + "<br/>" + Math.round(series.percent) + "%</div>";
	                        }
	                    }
	                };
	            }();
               	InitiatePieChart.init();
      <%}%>
});
        </script>