

<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@taglib uri="http://www.customtaglib.com/standard" prefix="std"%>


<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">AUTHENTIFICATION</span>
			<button type="button" id="close_modal" class="btn btn-lg btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			
			<!-- LOGIN -->
			<div id="login_div" class="row" style="margin-left: 10px;margin-right: 0px;">
				<div class="form-group">
					<a href="javascript:" style="font-size: 15px;text-decoration: underline;color: #d73d32;" onclick="$('#login_div').hide(100);$('#account_div').show(100);">Créer mon compte</a>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Login" style="margin-bottom: 15px;font-weight:bold;font-size: 19px;"/>&nbsp;
					<div class="col-md-9" style="margin-top: -15px;">
						<std:text name="log.login" type="string" style="width:100%;font-size: 25px;" required="true" />
					</div>
				</div>		
				<div class="form-group">
					<std:label classStyle="control-label col-md-5" value="Mot de passe" style="margin-bottom: 15px;font-weight:bold;font-size: 19px;"/>&nbsp;
					<div class="col-md-7">
						<std:password name="log.password" placeholder="Mot de passe" type="string" style="width:140px;font-size: 18px;margin-top: -15px;" required="true" maxlength="80" />
					</div>
				</div>
				<div class="form-group" style="text-align: center;margin-top: 50px;">
					<std:button actionGroup="C" style="font-size: 20px;border-radius: 10px;" classStyle="btn btn-lg btn-primary" params="isSub=1&mnt=log" action="caisse.clientMobile.create_cpt" closeOnSubmit="true" icon="fa-save" value="S'authenfier" />
				</div>	
			</div>
			
			<!-- CPT -->
			<div id="account_div" class="row" style="margin-left: 15px;margin-right: 0px;display: none;">
				<div class="form-group">
					<a href="javascript:" style="font-size: 15px;text-decoration: underline;color: #d73d32;" onclick="$('#account_div').hide(100);$('#login_div').show(100);">J'ai déjà un compte</a>
				</div>
				
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Login" />&nbsp;
					<div class="col-md-9">
						<std:text name="cmd.user" type="string" style="width:100%;" required="true" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-5" value="Mot de passe"/>&nbsp;
					<div class="col-md-7">
						<std:password name="cmd.password" placeholder="Mot de passe" type="string" style="width:140px;" required="true" maxlength="80" />
					</div> 
				</div>
				
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Nom" />&nbsp;
					<div class="col-md-9">
						<std:text name="cmd.nom" type="string" style="width:100%;" required="true" />
					</div>
				</div>
<!-- 				<div class="form-group"> -->
<%-- 					<std:label classStyle="control-label col-md-3" value="Email" />&nbsp; --%>
<!-- 					<div class="col-md-9"> -->
<%-- 						<std:text name="cmd.mail" type="string" style="width:100%;" required="true" /> --%>
<!-- 					</div> -->
<!-- 				</div> -->
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Téléphone" />&nbsp;
					<div class="col-md-9">
						<std:text name="cmd.phone" type="string" mask="99-99-99-99-99" style="width:50%;" required="true" />
					</div>
				</div>		
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Adresse" />&nbsp;
					<div class="col-md-9">
						<std:textarea name="cmd.adresse" style="width: 100%;" />
					</div>
				</div>		
				
				<div class="form-group" style="text-align: center;margin-top: 50px;">
					<std:button actionGroup="C" style="font-size: 20px;border-radius: 10px;" classStyle="btn btn-lg btn-primary" params="isSub=1&mnt=acc" action="caisse.clientMobile.create_cpt" closeOnSubmit="true" icon="fa-save" value="Créer mon compte" />
				</div>	
			</div>	
        </div>
    </div>
</std:form>    
