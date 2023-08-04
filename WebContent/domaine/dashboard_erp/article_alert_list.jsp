<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>

<div class="row">
		<div class="tabbable" style="margin-left: 20px;margin-right: 20px;">
			<ul class="nav nav-tabs" id="myTab">
				<li class="active">
					<a data-toggle="tab" href="#descripton"> Stocks insuffisants
							(<span style="color: red;">${nbrArtTotal }</span>)
					</a>
				</li>
				<li>
					<a data-toggle="tab" href="#mouvement"> Articles p&eacute;remption 
							(<span style="color: red;">${nbrArtTotalPeremption }</span>)
					</a>
				</li>
			</ul>
		 
		 <div class="tab-content">
            <div id="descripton" class="tab-pane active">
				<jsp:include page="article_seuil_list.jsp"></jsp:include>
			</div>
			
			<div id="mouvement" class="tab-pane">
				<jsp:include page="article_peremption_list.jsp"></jsp:include>
			</div>
		</div>
	</div>
</div>		