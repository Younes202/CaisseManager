<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<std:form name="data-form">
<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption"> Changer votre mot de passe
			</span>
		</div>
		
		<div class="widget-body">
		<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
				<div class="row">
					<div class="form-group">
                        <label class="col-sm-4 control-label">Ancien mot de passe</label>
                        <div class="col-sm-6">
                        	<std:password type="string" name="old_pw" placeholder="Ancien" style="width: 150px;" required="true"/>
                        </div>
                      </div>
                     <div class="form-group">
                        <label class="col-sm-4 control-label">Nouveau mot de passe</label>
                        <div class="col-sm-6">
                        	<std:password type="string" name="new_pw" placeholder="Nouveau" style="width: 150px;margin-top: 5px;" required="true"/>
                        </div>
                      </div>
                     <div class="form-group">
                        <label class="col-sm-4 control-label">Confirmer le nouveau mot de passe</label>
                        <div class="col-sm-6">
                        	<std:password type="string" name="new_pw2" placeholder="Confirmer" style="width: 150px;margin-top: 5px;" required="true"/>
                        </div>
                    </div>
				</div>
			</div>
		</div>

		<div class="modal-footer">
  		    <std:button actionGroup="U" classStyle="btn btn-success" action="admin.user.changerPw" icon="fa-save" value="Valider" targetDiv="generic_modal_body"/>
            <button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
	</std:form>
