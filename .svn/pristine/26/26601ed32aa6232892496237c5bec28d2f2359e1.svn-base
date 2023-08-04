<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		resetInputFileEvents();
		loadInputFileEvents();
		
		$('.pdf_link').click(function(){
			$("#pdf_load_trig").attr("href", "front?w_f_act=<%=EncryptionUtil.encrypt("stock.composant.telechargerModel")%>&"+$(this).attr("params"));
			document.getElementById("pdf_load_trig").click();
		});
	});
</script>	
		
<std:form name="data-form">
<a href="" id="pdf_load_trig" target="downloadframe" style="display:none;"></a>
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche import articles</span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Joindre le fichier" />
					<div class="col-md-9">
						<div class="col-sm-12">	
							<div id="photo1_div" style="width:30%; border: 1px solid #c6d983; border-radius: 25px; text-align: center; height: 120px; background: url('resources/framework/img/picture_file.png') no-repeat center;cursor: pointer;">
								<span style="font-size: 11px;">Fichier</span>
							</div>
							<span id="photo1_name_span"></span>
							<input type="hidden" name="photo1_name" id="photo1_name">
						</div>	
						<div class="col-sm-12">
							<!-- Separator -->
							<div id="sep_photo1" style="width:30%;margin-bottom: 5px; height: 20px; text-align: center;">
								<a href="javascript:"><b>X</b></a>
							</div>
							<!-- End -->
							<input type="file" name="photo1" id="photo1_div_file" style="display: none;" accept="xls">
						</div>
						<div class="col-sm-12">
							<std:link tooltip="Télécharger le modèle" params="skipI=true&skipP=true" icon="fa-file-excel-o" value="Télécharger modèle Excel" classStyle="pdf_link" />
						</div>	
					</div>
				</div>
				<div class="form-group">	
					<std:label classStyle="control-label col-md-2" value="Générer code barre" />
					<div class="col-md-2">
						<std:checkbox name="genereCB" />
					</div>
					<std:label classStyle="control-label col-md-2" value="Réaliser inventaire" />
					<div class="col-md-2">
						<std:checkbox name="genereINV" onChange="$('#inv_div').css('display', $('#genereINV').prop('checked')?'':'none');" />
						<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Si vous souhaitez réaliser un inventaire basé sur les quantités saisies dans le fichier importé.<br>Les articles sans quantité(vide) n seront pas concernés." src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
					</div>
					<std:label classStyle="control-label col-md-2" value="Désactiver inexistants" />
					<div class="col-md-2">
						<std:checkbox name="disArt" />
						<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Désactiver les articles qui ne se trouvent pas dans ce fichier importé." src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
					</div>
				</div>
				
				<div class="form-group" id="inv_div" style="display: none;">	
					<div class="col-sm-12">
						<std:label classStyle="control-label col-md-4" value="Date inventaire" />
						<div class="col-md-6">
							<std:date name="date_inv" />
						</div>
					</div>
					<div class="col-sm-12" style="margin-top: 10px;">	
						<std:label classStyle="control-label col-md-4" value="Emplacement" />
						<div class="col-md-6">
							<std:select name="emplacement_inv" key="id" labels="titre" type="long" data="${listEmplacement }" width="100%" />
						</div>
					</div>	
				</div>
				
				<!-- *********** -->
				<div class="row" style="text-align: center;">
					<div style="margin-top: 10px;display: none;margin-bottom: 15px;" id="barre_update">
                      	Veuillez patienter ... <img src='resources/framework/img/ajax-loader.gif' />
                     </div>
                </div>      	
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-success" onComplete="$('#barre_update').hide();$('.closeImport').close();" params="isSub=1" onClick="$('#barre_update').show(1000);" action="stock.composant.importerComposants" icon="fa-save" value="Importer le fichier" />
						<button type="button" id="close_modal" class="btn btn-primary closeImport" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>	
				</div>
			</div>
		</div>
	</div>
</std:form>

<script type="text/javascript">
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>    