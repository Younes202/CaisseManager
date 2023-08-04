<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.model.domaine.administration.persistant.AgencementPersistant"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
boolean isConfirmTransfert = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SECURE_TRANSFERT_ART"));
%>
<script type="text/javascript">
$(document).ready(function (){
	  <%if(isConfirmTransfert){%>
  	  setTimeout(function(){
		$("#div_auth").show(200);
	  }, 1000);
  	<%}%>
  
    $("#target_tbl").select2({
        allowClear: true,
        tags: true
    });
    
    init_keyboard_events();
    //
    var barcodeLockQte="";
	$(document).off('keydown').on('keydown', function (e) {
        var code = (e.keyCode ? e.keyCode : e.which);
        var sourceEvent = $(e.target).prop('nodeName');
        var isInput = (sourceEvent == 'INPUT') ? true : false;
        
        //
        if(!isInput && code==13 && $.trim(barcodeLockQte) != ''){
        	barcodeLockQte = barcodeLockQte.substring(barcodeLockQte.length-10);
        	
        	if(barcodeLockQte.length==10){
        		submitAjaxForm('<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.transfertArtMnu")%>', 'qte.tkn='+barcodeLockQte, $("#data-formTrs"), $("#trigSubTransBtn"));
        		return false;
        	}
        	barcodeLockQte="";
        } else{
        	barcodeLockQte = barcodeLockQte + String.fromCharCode(code);
        }
    });
    $("#checkFocQte").focus();
    
    
	$(".spinbox-up").click(function(){
		$("#quantiteTrans_cust").val(parseInt($("#quantiteTrans_cust").val())+1);
	});
	$(".spinbox-down").click(function(){
		if($("#quantiteTrans_cust").val() != '' && parseInt($("#quantiteTrans_cust").val()) > 1){
			$("#quantiteTrans_cust").val(parseInt($("#quantiteTrans_cust").val())-1);
		}
	});

});
function setClientsIdx(){
	if($("#target_tbl").val() == ''){
		return;
	}
	showSpinner();
    //
    $.ajax({
        beforeSend: function(data) {
        },
        url: "front?w_f_act=<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.getClientsTable")%>&curr_tab="+$("#target_tbl").val()+"&wpaj=true&wfaj=true&skipI=true&skipP=true",
        data: null,
        type: "POST",
        cache: false,
        dataType: 'text',
        error: function(data) {
        	hideSpinner();
        },
        success: function(data) {
        	 hideSpinner();
        	 
        	 $('#target_client')
        	    .find('option')
        	    .remove()
        	    .end()
        	    .append(data).trigger("change");
        }
    });
	
}
</script>

<%
List<AgencementPersistant> listAgencement = (List<AgencementPersistant>)request.getAttribute("listAgencement");
String refTabeTraget = (String) ControllerUtil.getUserAttribute("CURRENT_TABLE_REF", request);
%>
<std:form name="data-formTrs">
	<input type="hidden" name="checkFocQte" id="checkFocQte">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Transférer ** <span style="color: blue;font-weight: bold;">${currArt }</span> ** vers </span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			
			<%if(isConfirmTransfert){ %>
				<div class="row" style="
							margin-left: 0px;
							margin-right: 0px;
							display: none;
							margin-left: 0px;
						    margin-right: 0px;
						    background-color: #ffddac;
						    padding: 17px;
						    border-radius: 13px;" id="div_auth">
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
				<hr>
			<%} %>
			
						
			<div class="row">
				<%if(listAgencement.size() > 0){ %> 
					<div class="form-group">
						<std:label classStyle="control-label col-md-3" value="Table destination" />
						<div class="col-md-5">
							<select id="target_tbl" name="target_tbl" style="width:100%;" onchange="setClientsIdx();">
								<option></option>
								<% 
									for(AgencementPersistant agencement : listAgencement){%>
										<optgroup label="<%=agencement.getEmplacement()%>"></optgroup>
									<%
										String[] data = StringUtil.getArrayFromStringDelim(agencement.getTable_coords(), ";");
										if(data != null){
											List<String> list = new ArrayList<>();
											for(String tbl : data){
												String[] datatab = StringUtil.getArrayFromStringDelim(tbl, ":");
												list.add(datatab[0]);
											}
											Collections.sort(list);
											
											for(String tab : list){
												String selected = (tab.equals(refTabeTraget) ? " selected='selected'" : "");
											%>
												<option <%=selected %> value="<%=tab%>">&nbsp;&nbsp;&nbsp;&nbsp;<%=tab %></option>
											<%}
										}
									}
								%>
							</select>
						</div>
						<std:label classStyle="control-label col-md-2" value="Table vide?" />
						<div class="col-md-2">
							<std:checkbox forceWriten="true" name="target_emptyTable" />
							<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Cocher cette case si la table destination est vide et ainsi créer une nouvelle commande pour cette table." src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
						</div>
					</div>
				<%} %>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Client destination" />
					<div class="col-md-9">
						<std:select name="target_client" required="true" forceWriten="true" type="string" width="80%" data="${clientIdxArray }" addBlank="false" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Quantité" />
					<div class="col-md-2">
						<div class="spinbox spinbox-horizontal spinbox-two-sided">
		                     <div class="spinbox-buttons btn-group spinbox-buttons-left">
		                         <button type="button" class="btn spinbox-down danger">
		                             <i class="fa fa-minus"></i>
		                         </button>
		                     </div>
		                     
		                     <c:choose>
                      			<c:when test="${CURR_ART_TRANS.quantite.doubleValue() % 1 != 0 }">
	                     			<std:text name="quantiteTrans_cust" forceWriten="true" type="decimal" style="width: 83%;font-size: 20px;" value="${CURR_ART_TRANS.quantite }" classStyle="spinbox-input form-control" />
	                     		</c:when>
		                      	<c:otherwise>
	                     			<std:text name="quantiteTrans_cust" forceWriten="true" type="long" style="width: 83%;font-size: 20px;" value="${CURR_ART_TRANS.quantite }" classStyle="spinbox-input form-control" />
	                 			</c:otherwise>
		                      </c:choose>
		                     			
		                     <div class="spinbox-buttons btn-group spinbox-buttons-right">
		                         <button type="button" class="btn spinbox-up blue">
		                             <i class="fa fa-plus"></i>
		                         </button>
		                     </div>
	                 	</div>
	                 </div>	
				</div>
				
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="C" id="trigSubTransBtn" style="border-radius: 37px;height: 52px;font-size: 21px;" classStyle="btn btn-success" targetDiv="left-div" action="caisse-web.caisseWeb.transfertArtMnu" icon="fa-mail-forward" value="Transf&eacute;rer" />
						<button type="button" id="close_modal" style="border-radius: 37px;height: 52px;font-size: 21px;" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</std:form>

<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script> 