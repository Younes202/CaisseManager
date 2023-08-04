<%@page import="java.util.Map"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Configuration de la pointeuse</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="pers.pointage.init_config" workId="${emplacement.id}" icon="fa fa-pencil" tooltip="Cr&eacute;er" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin: -8px;">
				<div class="alert alert-warning fade in">
                    <button class="close" data-dismiss="alert">
                        x
                    </button>
                    <i class="fa-fw fa fa-warning"></i>
                    <strong style="color: orange;text-align: center;">Cette configuration concerne les pointeuses ZKTEco.</strong><br>
                    <i class="fa fa-info-circle" style="color: red;"></i>Saisir l'adresse IP et le PORT de connexion &agrave; la pointeuse.<br>
                    Le lien entre le backoffice et la pointeuse se fait par le num&eacute;ro employ&eacute; qui doit &ecirc;tre identique des deux cot&eacute;s.
                </div>
			</div>
			<hr>
			<div class="row">
				<div class="col-md-12">
<!-- 					<div class="form-group"> -->
<%-- 						<std:label classStyle="control-label col-md-3" value="Chemin base Access pointeuse" /> --%>
<!-- 						<div class="col-md-9"> -->
<%-- 							<std:text name="pointeuse.path" type="string" placeholder="Chemin absolu" value="${path }" style="float:left;width:90%;" /> --%>
<!-- 							<span class="glyphicon glyphicon-info-sign" style="color: blue;padding-top: 9px;" title="Si lecture directe de la base de donn&eacute;es de la pointeuse"></span> -->
<!-- 						</div> -->
<!-- 					</div> -->
					<div class="form-group">
						<std:label classStyle="control-label col-md-3" value="Adresse Ip pointeuse" />
						<div class="col-md-9">
							<std:text name="pointeuse.zkt_ip" type="string" placeholder="Adresse IP" style="width:150px;float:left;" value="${zktIp }" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-3" value="Port pointeuse" />
						<div class="col-md-9">
							<std:text name="pointeuse.zkt_port" type="string" placeholder="Port pointeuse" style="width:80px;" mask="9999" value="${empty(zktPort)?'4370':zktPort }" />
						</div>
					</div>
				</div>
			</div>
			<hr>
			<div class="row" style="text-align: center;">
				<div class="col-md-12">
					<std:button actionGroup="M" classStyle="btn btn-success" action="pers.pointage.init_config" params="issave=1" icon="fa-save" value="Sauvegarder" />
					<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
						<i class="fa fa-times"></i> Fermer
					</button>
				</div>
				</div>
			</div>
		</div>
</std:form>