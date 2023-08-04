<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="java.util.Date"%>
<%@page import="appli.model.domaine.personnel.persistant.EmployePersistant"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>


	<script type="text/javascript">
		$(document).ready(function (){
			$("#salariePaie\\.prime_lib, #salariePaie\\.indemnite_lib").select2({
				 tags: true
			});
		});
	</script>
		
<%
EmployePersistant employeP = (EmployePersistant)ControllerUtil.getMenuAttribute("CURR_EMPL_PER", request);
String idxMois = (String)request.getAttribute("idxMois");
Long employe_id = (Long)request.getAttribute("employe_id");
String targetId = "td_"+employe_id+"-"+idxMois;
%>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<std:hidden name="employe_id" id="employe_id" value="<%=employe_id %>" />
			<std:hidden name="empl" id="empl" value="<%=employe_id %>" />
			<std:hidden name="idxMois" id="idxMois" value="<%=idxMois %>" />
			<std:link targetDiv="generic_modal_body" actionGroup="U" classStyle="btn btn-default" action="paie.salariePaie.work_init_update" workId="${salariePaie.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
			
			<span class="widget-caption">Saisie pour le mois <b><%=idxMois %></b></span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin-right:0px !important; margin-left:0px !important;">
				
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Date paiement" />
					<div class="col-md-4">
						<std:date name="salariePaie.date_paiement" required="true" value="${salariePaie.date_paiement }" />
					</div>
              		<std:label addSeparator="false" classStyle="col-sm-1 control-label" value="Nbr jours" />
                   <div class="col-sm-2">
                   		<std:text name="salariePaie.nbr_jours" type="long" style="width:50px;" value="${salariePaie.nbr_jours }" />
                   </div>
                   <std:label addSeparator="false" classStyle="col-sm-1 control-label" value="Tarif jour" />
                   <div class="col-sm-2">
                   		<std:text name="salariePaie.tarif_jour" type="decimal" style="width:80px;" placeholder="Tarif jour" value="${salariePaie.tarif_jour }" />
                   </div>
              	</div>       
              	
				<div class="form-group">
					<std:label addSeparator="false" classStyle="control-label col-md-2" value="Date début" />
					<div class="col-md-4">
						<std:date name="salariePaie.date_debut" required="true" value="${salariePaie.date_debut }"/>
					</div>
					<std:label addSeparator="false" classStyle="control-label col-md-2" value="Date fin" />
					<div class="col-md-4">
						<std:date name="salariePaie.date_fin" required="true" value="${salariePaie.date_fin }" />
					</div>
				</div>
				
              	<hr>
              	<div class="form-group">
              		<div class="col-sm-6">
	              		<b style="color: orange;">Primes</b>
			            <br>
			             <div class="col-sm-8">
			             	<std:select width="100%" name="salariePaie.indemnite_lib" type="string" data="${indemniteHisto }" />
			             </div>	
			             <div class="col-sm-4">
			                   <std:text name="salariePaie.mt_indemnite" type="decimal" style="margin-top: 7px;" />
			              </div>
              		</div>
              		<div class="col-sm-6">
		            	<b style="color: orange;">Indemnité</b>
			            <br>
			             <div class="col-sm-8">
			             	<std:select width="100%" name="salariePaie.prime_lib" type="string" data="${primeHisto }" />
			             </div>	
	                   	 <div class="col-sm-4">
	                   		<std:text name="salariePaie.mt_prime" type="decimal" style="margin-top: 7px;" />
	                  	 </div>
	            	</div>
	            </div>
              	
              	<b style="color: orange;">Autres montants</b>
              	<br>
              	<div class="form-group">
              		<std:label addSeparator="false" classStyle="col-sm-2 control-label" value="Avance" />
                   <div class="col-sm-2">
                   		<std:text name="salariePaie.mt_avance" type="decimal" placeholder="Avance" />
                   </div>
                   <std:label addSeparator="false" classStyle="col-sm-2 control-label" value="Prêt" />
                   <div class="col-sm-2">
                   		<std:text name="salariePaie.mt_pret" type="decimal" placeholder="Prêt" />
                   </div>
                   <std:label addSeparator="false" classStyle="col-sm-2 control-label" value="Reliquat" />
                   <div class="col-sm-2">
                   		<std:text name="salariePaie.mt_reliquat" type="decimal" placeholder="Reliquat" />
                   </div>
               </div>
               <b style="color: orange;">Congés</b>
               <br>
               <div class="form-group">    
					<std:label addSeparator="false" classStyle="col-sm-2 control-label" value="Congés payés" />
					<div class="col-md-2">
						<std:text name="salariePaie.nbr_conge" type="decimal" maxlength="14" />
					</div>
					<std:label addSeparator="false" classStyle="col-sm-2 control-label" value="Congés non payés" />
					<div class="col-md-2">
						<std:text name="salariePaie.nbr_conge_np" type="decimal" maxlength="14" />
					</div>
					
              	</div>
              	<hr>
              	<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="salaire.montant_brut" />
					<div class="col-md-4">
						<std:text name="salariePaie.montant_brut" type="decimal" maxlength="14" style="width: 100px;"/>
					</div>
					<std:label classStyle="control-label col-md-2" valueKey="salaire.montant_net" />
					<div class="col-md-4">
						<std:text name="salariePaie.montant_net" type="decimal" maxlength="14" required="true" style="width: 100px;" />
					</div>
				</div>
				
                <!-- **************************** FINANCEMENT BLOC ********************** -->
				<c:set var="menu_scope.PAIEMENT_DATA" value="${salaire.getList_paiement() }" scope="session" />
				<div class="form-group" id="finance_bloc">
					<jsp:include page="/domaine/compta/paiement_consult.jsp"></jsp:include>
				</div>
			  <!-- **************************** FIN FINANCEMENT BLOC ********************** -->
				<hr>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="salaire.commentaire" />
					<div class="col-md-6">
						<std:textarea name="salariePaie.commentaire" maxlength="255" cols="60" rows="3" />
					</div>
				</div>            
            </div>
            <div class="modal-footer" id="button_down">
           			<std:button actionGroup="M" closeOnSubmit="true" action="paie.salariePaie.work_merge" workId="${salariePaie.id }" classStyle="btn btn-info" value="Sauvegarder" targetDiv="<%=targetId %>"  />
           			<std:button actionGroup="D" action="paie.salariePaie.work_delete" classStyle="btn warning" value="Supprimer" />
              		<button id="close_modal" type="button" id="close_modal" class="btn btn-danger" data-dismiss="modal"><i class="fa fa-times"></i> Fermer</button>
            </div>
          </div>
</div></std:form>