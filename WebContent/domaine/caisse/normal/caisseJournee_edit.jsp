<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>


<% Long caisseJourneeId = (Long) ControllerUtil.getMenuAttribute("caisseJourneeId", request);
%>

<!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de caisse</li>
         <li>Fiche de la journ&eacute;e de caisse
		</li>
         <li class="active">Edition</li>
     </ul>
 </div>

<div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
		<std:link classStyle="btn btn-default" action="caisse.caisseJournee.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
      </div>
      <!--Header Buttons-->
      <div class="header-buttons">
          <a class="sidebar-toggler" href="#">
              <i class="fa fa-arrows-h"></i>
          </a>
          <a class="refresh" id="refresh-toggler" href="#">
              <i class="glyphicon glyphicon-refresh"></i>
          </a>
          <a class="fullscreen" id="fullscreen-toggler" href="#">
              <i class="glyphicon glyphicon-fullscreen"></i>
          </a>
      </div>
      <!--Header Buttons End-->
  </div>
  <!-- /Page Header -->

<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
	
	
	
	<!-- widget grid -->
	
	<div class="widget">
	<std:form name="data-form">
         
       <div class="row">
        <div class="col-lg-12 col-sm-6 col-xs-12">
              <div class="tabbable">
             		<ul class="nav nav-tabs" id="myTab">
                          <li >
                              <a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("caisse.caisse.work_edit")%>" >
                               Fiche
                              </a>
                           </li>
                           <%if(ControllerUtil.getMenuAttribute("caisseId", request)!=null){ %>
                            <li class="active">
                              <a data-toggle="tab" href="#journeeCaisse" wact="<%=EncryptionUtil.encrypt("caisse.caisseJournee.work_find")%>">
                               Journ&eacute;es caisse
                              </a>
                            </li>
                           <%} %>
                     </ul>
                </div>
          </div>
      </div>
 

<div class="row">
	<div class="col-lg-12 col-sm-6 col-xs-12">
         <div class="widget">
         <div class="widget-body" >
                   <div class="widget-main ">
                         <div class="tabbable">
                         	<ul class="nav nav-tabs" id="myTab9" style="margin-left: 4%;">
                               <li class="active">
                                  <a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("caisse.caisseJournee.work_edit")%>" >
                               		Fiche
                              	  </a>
	                           </li>
	                           <%if(StringUtil.isNotEmpty(caisseJourneeId)){ %>
	                            <li>
	                              <a data-toggle="tab" href="#mouvementCaisse" wact="<%=EncryptionUtil.encrypt("caisse.caisseMouvement.work_find")%>">
	                               Mouvements
	                              </a>
	                            </li>
	                            <%} %>                                                            
	                         </ul>
	                         
                        
                     
              <div class="tab-content" style="margin-left: 4%">
				<div class="form-group">
	
					<std:label classStyle="control-label col-md-2" valueKey="caisseJournee.date_journee" />
					<div class="col-md-2">
						<std:date name="caisseJournee.date_journee" />
					</div>
					<div class="col-md-2">
					</div>
					<std:label classStyle="control-label col-md-2" valueKey="caisseJournee.opc_employe" />
					<div class="col-md-4">
						<std:text name="caisseJournee.opc_employe" type="string" value="${employe }" style="width: 50%;"/>
					</div>
				</div>	
				<div class="form-group">
				
					<std:label classStyle="control-label col-md-2" valueKey="caisseJournee.mtt_ouverture"/>
					<div class="col-md-4">
						<std:text name="caisseJournee.mtt_ouverture" type="decimal" placeholderKey="caisseJournee.mtt_ouverture" maxlength="15" style="width: 50%;"/>
					</div>
					<std:label classStyle="control-label col-md-2" valueKey="caisseJournee.mtt_cloture"/>
					<div class="col-md-4">
						<std:text name="caisseJournee.mtt_cloture" type="decimal" placeholderKey="caisseJournee.mtt_cloture" maxlength="15" style="width: 50%;"/>
					</div>
				</div>
				<div class="form-group">
				
					<std:label classStyle="control-label col-md-2" valueKey="caisseJournee.mtt_calcule"/>
					<div class="col-md-4">
						<std:text name="caisseJournee.mtt_calcule" type="decimal" placeholderKey="caisseJournee.mtt_calcule" maxlength="15" style="width: 50%;"/>
					</div>
					<std:label classStyle="control-label col-md-2" valueKey="caisseJournee.mtt_cloture_caissier"/>
					<div class="col-md-4">
						<std:text name="caisseJournee.mtt_cloture_caissier" type="decimal" placeholderKey="caisseJournee.mtt_cloture_caissier" maxlength="15" style="width: 50%;"/>
					</div>
				</div>
				<div class="form-group">
				
					<std:label classStyle="control-label col-md-2" valueKey="caisseJournee.nbr_vente"/>
					<div class="col-md-4">
						<std:text name="caisseJournee.nbr_vente" type="long" placeholderKey="caisseJournee.nbr_vente" maxlength="10" style="width: 50%;"/>
					</div>
					<std:label classStyle="control-label col-md-2" valueKey="caisseJournee.mtt_espece"/>
					<div class="col-md-4">
						<std:text name="caisseJournee.mtt_espece" type="decimal" placeholderKey="caisseJournee.mtt_espece" maxlength="15" style="width: 50%;"/>
					</div>
				</div>
				<div class="form-group">
				
					<std:label classStyle="control-label col-md-2" valueKey="caisseJournee.mtt_cheque"/>
					<div class="col-md-4">
						<std:text name="caisseJournee.mtt_cheque" type="decimal" placeholderKey="caisseJournee.mtt_cheque" maxlength="15" style="width: 50%;"/>
					</div>
					<std:label classStyle="control-label col-md-2" valueKey="caisseJournee.mtt_cb"/>
					<div class="col-md-4">
						<std:text name="caisseJournee.mtt_cb" type="decimal" placeholderKey="caisseJournee.mtt_cb" maxlength="15" style="width: 50%;"/>
					</div>
				</div>
				<div class="form-group">
				
					<std:label classStyle="control-label col-md-2" valueKey="caisseJournee.mtt_ticket"/>
					<div class="col-md-4">
						<std:text name="caisseJournee.mtt_ticket" type="decimal" placeholderKey="caisseJournee.mtt_ticket" maxlength="15" style="width: 50%;"/>
					</div>
					<std:label classStyle="control-label col-md-2" valueKey="caisseJournee.mtt_avoir"/>
					<div class="col-md-4">
						<std:text name="caisseJournee.mtt_avoir" type="decimal" placeholderKey="caisseJournee.mtt_avoir" maxlength="15" style="width: 50%;"/>
					</div>
				</div>
				
			
		
	<hr>
			<div class="form-actions">
				<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
						<std:button actionGroup="M" classStyle="btn btn-success" action="caisse.caisseJournee.work_merge" workId="${caisseJournee.id }" icon="fa-save" value="Sauvegarder" />
						<std:button actionGroup="D" classStyle="btn btn-danger" action="caisse.caisseJournee.work_delete" workId="${caisseJournee.id }" icon="fa-trash-o" value="Supprimer" />
					</div>
					
				</div>
			</div>
                    </div>
                     </div>  
               </div>
         </div>
     </div>
</div>
	
	</std:form>
</div>
</div>
