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
			<span class="widget-caption">Fiche type de planning</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="pers.typePlanning.work_init_update" workId="${typePlanning.id}" icon="fa fa-pencil"
				tooltip="Créer" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<div class="form-group">
						<std:label classStyle="control-label col-md-4" value="Libellé" />
						<div class="col-md-7">
							<std:text name="typePlanning.libelle" type="string" maxlength="50" required="true" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-4" value="Afficher situation client" />
						<div class="col-md-7">
							<std:checkbox name="typePlanning.is_situation_cli" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-4" value="Durée" />
						<div class="col-md-7">
							<std:text name="typePlanning.duree" type="long" maxlength="10" style="width:50px;float:left;"/> <span style="line-height: 32px;"> Heures</span>
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-4" value="Couleur" />
						<div class="col-md-4">
							<input type="text" id="hue-demo" name="typePlanning.color" value="${typePlanning.color }" class="form-control colorpicker" data-control="hue" >
						</div>
					</div>
				</div>	
			</div>		
			<div class="modal-footer">
				<std:button actionGroup="M" classStyle="btn btn-success" action="pers.typePlanning.work_merge" workId="${typePlanning.id }" icon="fa-save" value="Sauvegarder" />
				<std:button actionGroup="D" classStyle="btn btn-danger" action="pers.typePlanning.work_delete" workId="${typePlanning.id }" icon="fa-trash-o" value="Supprimer" />
				<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
					<i class="fa fa-times"></i> Fermer
				</button>
			</div>
	</div>
</div>
</std:form>

<script src="resources/framework/js/colorpicker/jquery.minicolors.js"></script>
<script type="text/javascript">
	$(document).ready(function (){
		$('.colorpicker').each(function () {
            $(this).minicolors({
                control: $(this).attr('data-control') || 'hue',
                defaultValue: $(this).attr('data-defaultValue') || '',
                inline: $(this).attr('data-inline') === 'true',
                letterCase: $(this).attr('data-letterCase') || 'lowercase',
                opacity: $(this).attr('data-opacity'),
                position: $(this).attr('data-position') || 'bottom left',
                change: function (hex, opacity) {
                    if (!hex) return;
                    if (opacity) hex += ', ' + opacity;
                    try {
                        console.log(hex);
                    } catch (e) { }
                },
                theme: 'bootstrap'
            });

        });
	});
	</script> 


