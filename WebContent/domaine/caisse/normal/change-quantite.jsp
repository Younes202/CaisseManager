<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) ControllerUtil.getUserAttribute("CURRENT_COMMANDE", request);
boolean isConfirmReduceQte = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SECURE_DECREASE_QTE"));
BigDecimal customQte = (BigDecimal)request.getAttribute("quantite_custom");
BigDecimal origineQte = (BigDecimal)request.getAttribute("quantite_ori");
%>
<script type="text/javascript">
	function manageQteAuth(){
		<%if(CURRENT_COMMANDE.getId() != null){%>
			if($('#quantite_custom').val() < <%=origineQte.intValue() %>){
				$("#div_auth").show(200);
			} else{
				$("#div_auth").hide(100);
			}
		<%}%> 
	}
	$(document).ready(function (){
		$(".spinbox-up").click(function(){
			<%if(customQte != null && customQte.doubleValue() % 1 != 0){%>
				$("#quantite_custom").val(parseFloat($("#quantite_custom").val())+1);
			<%} else{%>
				$("#quantite_custom").val(parseInt($("#quantite_custom").val())+1);
			<%}%>
			manageQteAuth();
		});
		$(".spinbox-down").click(function(){
			if($("#quantite_custom").val() != '' && parseInt($("#quantite_custom").val()) > 1){
				<%if(customQte != null && customQte.doubleValue() % 1 != 0){%>
					$("#quantite_custom").val(parseFloat($("#quantite_custom").val())-1);
				<%} else{%>
					$("#quantite_custom").val(parseInt($("#quantite_custom").val())-1);
				<%}%>
			}
			manageQteAuth();
		});
		$('#quantite_custom').change(function(){
			manageQteAuth();
		});
		
		//-----------------------------------------
	<%if(CURRENT_COMMANDE.getId() != null){%>	
		init_keyboard_events();
		
		var barcodeLockQte="";
		$(document).off('keydown').on('keydown', function (e) {
	        var code = (e.keyCode ? e.keyCode : e.which);
	        var sourceEvent = $(e.target).prop('nodeName');
	        var isInput = (sourceEvent == 'INPUT') ? true : false;
	        
	        //
	        if(!isInput && code==13 && $.trim(barcodeLockQte) != ''){
	        	barcodeLockQte = barcodeLockQte.substring(barcodeLockQte.length-10);
	        	
	        	if(barcodeLockQte.length==10){
	        		submitAjaxForm('<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.changeQuantite")%>', '<%=request.getAttribute("params")+"&orqt="+request.getAttribute("quantite_ori")%>&qte.tkn='+barcodeLockQte, $("#data-formQte"), $("#trigSubQteBtn"));
	        		return false;
	        	}
	        	barcodeLockQte="";
	        } else{
	        	barcodeLockQte = barcodeLockQte + String.fromCharCode(code);
	        }
	    });
	    $("#checkFocQte").focus();
	<%} %>  
	});
</script>
	
<std:form name="data-formQte">
	<input type="hidden" name="checkFocQte" id="checkFocQte">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Changer la quantit&eacute;</span>
			<img src="resources/framework/img/badge_scanner.png" style="width: 20px;margin-top: 8px;margin-right: 5px;" title="Lecteur badge utilisable sur cet écran">
			
			<div class="widget-buttons buttons-bordered" style="margin-bottom: 10px;">
        		<i class="fa fa-keyboard-o" style="font-size: 20px;"></i>         
        		<label>
                 <input class="checkbox-slider toggle colored-blue" type="checkbox" id="keyboard-activator" style="display: none;">
                 <span class="text"></span>
             </label>
        	</div>
        	
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="margin-top: 2px;margin-right: 5px;">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin-left: 0px;margin-right: 0px;">
				<div class="col-lg-4 col-sm-4 col-xs-4"></div>
				<div class="col-lg-3 col-sm-3 col-xs-3">
					 <div class="spinbox spinbox-horizontal spinbox-two-sided">
	                      <div class="spinbox-buttons btn-group spinbox-buttons-left">
	                          <button type="button" class="btn spinbox-down danger">
	                              <i class="fa fa-minus"></i>
	                          </button>
	                      </div>
	                      <c:choose>
	                      	<c:when test="${quantite_custom.doubleValue() % 1 != 0 }">
	                      		<std:text forceWriten="true" name="quantite_custom" type="decimal" style="width: 83%;font-size: 20px;" value="${quantite_custom }" classStyle="spinbox-input form-control" />
	                      	</c:when>
	                      	<c:otherwise>
								<std:text forceWriten="true" name="quantite_custom" type="long" style="width: 83%;font-size: 20px;" value="${quantite_custom }" classStyle="spinbox-input form-control" />	                      	
	                      	</c:otherwise>
	                      </c:choose>
	                      
	                      <div class="spinbox-buttons btn-group spinbox-buttons-right">
	                          <button type="button" class="btn spinbox-up blue">
	                              <i class="fa fa-plus"></i>
	                          </button>
	                      </div>
	                  </div>
	               </div>  
	               <div class="col-lg-5 col-sm-5 col-xs-5"></div>
			</div>	
			
			<%if(isConfirmReduceQte){ %>
				<div class="row" style="margin-left: 0px;margin-right: 0px;display: none;" id="div_auth">
					<div class="col-md-12">
						<h3>Autorisation manager</h3>
					</div>
					<div class="col-md-12">
						<std:label classStyle="control-label col-md-4" value="Login" style="font-size: 19px;"/>&nbsp;
						<div class="col-md-8" style="margin-top: -15px;">
							<std:select forceWriten="true" name="unlockQte.login" type="long" style="width:100%;font-size: 25px;" data="${listUser }" key="id" labels="login" />
						</div>
					</div>
					<div class="col-md-12">
						<std:label classStyle="control-label col-md-4" value="Mot de passe" style="font-size: 19px;"/>&nbsp;
						<div class="col-md-8">
							<std:password forceWriten="true" name="unlockQte.password" placeholder="Mot de passe" type="string" style="width:140px;font-size: 18px;margin-top: -15px;" maxlength="80" />
						</div>
					</div>
				</div>
			<%} %>
			
			<hr>	
			<div class="row" style="text-align: center;">
				<div class="col-md-12">
					<std:button actionGroup="X" classStyle="btn btn-success" id="trigSubQteBtn" style="border-radius: 37px;height: 52px;font-size: 21px;" action="caisse-web.caisseWeb.changeQuantite" targetDiv="left-div" params="${params }&orqt=${quantite_ori }" icon="fa-save" value="Valider" closeOnSubmit="true" />
				</div>
			</div>
		</div>
	</div>
</std:form>