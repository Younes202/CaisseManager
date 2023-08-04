<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fn" prefix="fn"%>
<%@page errorPage="/commun/error.jsp" %>


<style>
#list_article th{
	font-size: 11px;
	vertical-align: middle;
	text-align: center;
}
#list_article td{
	font-size: 11px;
}

</style>

<script type="text/javascript">
	$(document).ready(function (){
		$('.input-group.date, #dateDebut').datepicker({
	    	clearBtn: true,
		    language: "fr",
		    autoclose: true,
		    format: "mm/yyyy",
		    startView: 1,
		    startDate : '<%=request.getAttribute("minDate")%>m',
		    endDate : '<%=request.getAttribute("maxDate")%>m',
		    minViewMode: 1
	    });
		$('.input-group.date').datepicker().on("changeDate", function(e) {
	        var currDate = $('#dateDebut').datepicker('getFormattedDate');
	        submitAjaxForm('<%=EncryptionUtil.encrypt("stock.etatStock.work_find")%>', 'article_fltr=1&dateDebut='+currDate, $("#search-form"), null, 'stock_recap_div');
	    });
		
		$(document).off('click', '#list_article th a').on('click', '#list_article th a', function(){
			var thId = $(this).attr("id");
			
			if($("#"+thId+"_det").css("display") == 'none'){
				$(this).find("span").attr("class", 'fa fa-minus');
			} else{
				$(this).find("span").attr("class", 'fa fa-plus');
			}
			
			manageTh(thId);
			
			setDataCookie();
			
			$("td[id='familleTd']").attr("colspan", $("#tr_detail th:visible").length+2);
		});
		
		//
		loadSelectAuto("article_fltr");
	});
	
	function getIdxTh(thId){
		var idx = 1;
		var  ret = 0;
		$("#list_article tr").each(function(){
			idx = 1;
			$(this).find("th").each(function(){
				if($(this).attr("id") == thId){
					ret = idx;
					return;
				}
				idx++;
			});
		});
	
		return ret;
	}
	function manageTh(thId){
		var startIdx = parseInt(getIdxTh("idx_"+thId))+2;
		
		thId = thId+"_det";
		var nbr = ($("#"+thId).attr("colspan") ? parseInt($("#"+thId).attr("colspan")) : 1);
		
		$("th[id^='"+thId+"']").css('background-color', '#bbf0f7').toggle();	
		for(var i=0; i<nbr; i++){
			$('#list_article tr th:nth-child('+((startIdx+i)-1)+'):not([colspan])').css('background-color', '#bbf0f7').toggle();
			$('#list_article tr td:nth-child('+(startIdx+i)+'):not([colspan])').css('background-color', '#bbf0f7').toggle();
		}
	}
	function manageFullTh(state){
		$("#list_article th a").each(function(){
			var thId = $(this).attr("id");
			var startIdx = parseInt(getIdxTh("idx_"+thId))+2;
			
			thId = thId+"_det";
			var nbr = ($("#"+thId).attr("colspan") ? parseInt($("#"+thId).attr("colspan")) : 1);
			
			if(state){
				$(this).find("span").attr("class", "fa fa-minus");
				$("th[id^='"+thId+"']").css('background-color', '#bbf0f7').show();	
				for(var i=0; i<nbr; i++){
					$('#list_article tr th:nth-child('+((startIdx+i)-1)+'):not([colspan])').css('background-color', '#bbf0f7').show();
					$('#list_article tr td:nth-child('+(startIdx+i)+'):not([colspan])').css('background-color', '#f5daa1').show();
				}
			} else{
				$(this).find("span").attr("class", "fa fa-plus");
				$("th[id^='"+thId+"']").css('background-color', '#bbf0f7').hide();	
				for(var i=0; i<nbr; i++){
					$('#list_article tr th:nth-child('+((startIdx+i)-1)+'):not([colspan])').css('background-color', '#bbf0f7').hide();
					$('#list_article tr td:nth-child('+(startIdx+i)+'):not([colspan])').css('background-color', '#f5daa1').hide();
				}
			}
		});
		setDataCookie();
		$("td[id='familleTd']").attr("colspan", $("#tr_detail th:visible").length+2);
	}
	
	function setDataCookie() {
		var dataThOuvert = "";
		$("#list_article th a").each(function(){
			if($(this).find("span").attr("class") == 'fa fa-minus'){
				dataThOuvert = dataThOuvert + ";" + $(this).attr("id");
			}
		});
		writeLocalStorage('etat_stock', dataThOuvert+";");
	}
	
	function refreshArt(){
		submitAjaxForm('<%=EncryptionUtil.encrypt("stock.etatStock.work_find")%>', '', $("#search-form"), null, 'stock_recap_div');
	}
	function loadSelectAuto(idSelect){
		 $("#"+getJQueryName(idSelect)).select2({
	            language: "fr",
	            minimumInputLength: 3,
	            tags: [],
	            placeholder: "Article",
	            allowClear: true,
	            ajax: {
	                url: 'front?w_f_act=<%=EncryptionUtil.encrypt("stock.mouvement.getListArticles")%>&isAct=1',
	                dataType: 'json',
	                type: "GET",
	                quietMillis: 50,
	                processResults: function (data) {
	                    return {
	                        results: $.map(data.items, function (item) {
	                            return {
	                                text: getArtLib(item),
	                                value: item.prix_achat_ht+"_"+(item.opc_tva_enum!=null?item.opc_tva_enum.id+"_"+item.opc_tva_enum.libelle:""),
	                                id: item.id
	                            }
	                        })
	                    };
	                }
	            }
	        }); 
		 	$("#"+getJQueryName(idSelect)).off('select2:select').on('select2:select', function (e) {
		 		var data = e.params.data;
		 	    var opt = $("select option[value=" + data.id + "]");
		 	    opt.attr('infos', data.value);
			});
		}
	function getArtLib(item){
		var val = '';
		if(item.code_barre && item.code_barre != ''){
			val += item.code_barre+'-';	
		}
		if(item.code && item.code != ''){
			val += item.code+'-';	
		}
		val += item.libelle;
		if(item.composition && item.composition != ''){
			val += '('+item.composition+')';	
		}
		
		return val;
	}
</script>
<std:form name="search-form">
 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de stock</li>
         <li>Etat des stocks</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <span style="font-size: 22px;float: left;">Etat du mois de </span>
  	<div class="input-group date" style="width: 156px;float: left;">
  		<input type="text" class="form-control" name="dateDebut" id="dateDebut" style="font-size: 22px;color:green !important;font-weight: bold;border: 0px;" value="<%=StringUtil.getValueOrEmpty(request.getAttribute("dateDebut"))%>">
  			<span class="input-group-addon" style="border: 1px solid #f3f3f3;padding-top: 9px;">
  				<i class="fa fa-calendar" style="font-size: 22px;color: #9C27B0;"></i>
  			</span>
	</div>
	<div style="float: left;margin-top: 4px;">
		<std:select name="article_fltr" type="string[]" onChange="refreshArt();" multiple="true" value="${selectedArticles }" key="id" labels="code;'-';libelle" width="450px" />
	</div>
	<div style="float: left;margin-top: 10px;margin-left: 20px;">
		<a href="javascript:" onclick="manageFullTh(true);">Tout ouvrir</a> | 
		<a href="javascript:" onclick="manageFullTh(false);">Tout fermer</a> |
		<std:link action="stock.etatStock.work_find" targetDiv="stock_recap_div" icon="fa fa-refresh" style="margin-top: -6px;" />
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

	<!-- row -->
	<div class="row" style="overflow: auto;margin-left: -7px;margin-right: 3px;" id="stock_recap_div">
		<jsp:include page="/domaine/stock/etat_stock_list_include.jsp" />
	 </div>
	<!-- end widget content -->

	</div>
	<!-- end widget div -->
 </std:form>