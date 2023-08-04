<!-- widget grid -->
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.model.domaine.stock.persistant.FamillePersistant"%>
<%@page import="java.util.List"%>


<script type="text/javascript">
	$(document).ready(function() {
		$("#treeattack-ul").treeview({
			collapsed: true,
			animated: "fast",
			persist: "cookie",
			unique: true,
			control: "#div-control",
			cookieId: "treefamille_inv"
		});
	});	
</script>

<div class="widget" id="tree_div">
       <div class="widget-header bordered-bottom bordered-blue">
          <span class="widget-caption">Familles</span>
       </div>
       <div class="widget-body">
	<div class="row" style="margin-left: 0px;">
			<div id="div-control">
	<a title="Collapse the entire tree below" href="#"><img src="resources/framework/img/tree/minus.gif" /> Fermer</a> |
	<a title="Expand the entire tree below" href="#"><img src="resources/framework/img/tree/plus.gif" /> Ouvrir</a> |
</div>
<hr>
				
<%
List<FamillePersistant> listFamille = (List<FamillePersistant>) request.getAttribute("listFamilleEmpl");
if(listFamille !=  null){%>
				<ul id="treeattack-ul" class="filetree">
					 <%
Integer oldLevel = null;
for(FamillePersistant famillePersistant : listFamille){
	//if(famillePersistant.getLevel() == 0){
		//continue;
	//}
	
	boolean isNode = (famillePersistant.getB_right()- famillePersistant.getB_left() > 1);
	String wid = EncryptionUtil.encrypt(""+famillePersistant.getId());
	
	if(oldLevel != null){
		if(oldLevel > famillePersistant.getLevel()){
			while(oldLevel > famillePersistant.getLevel()){	%>
					</li> 
				</ul>
				<%
				oldLevel--;
			}
		} else if(oldLevel == famillePersistant.getLevel()){%>
				</li>
				<%	}
	}
	%>
				<li> 
				<span class="<%=isNode ? "folder" :"file"%>" style="border-bottom: 1px dashed #FFEB3B;"> 
					<a href="javascript:" style="<%=isNode ? "color:black;" :""%>" fam="<%=wid%>"> <%=famillePersistant.getCode() %> - <%=famillePersistant.getLibelle() %></a>
					<img alt="" style="border-radius: 15px;" src='resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(famillePersistant.getId().toString()) %>&path=famille&rdm=<%=famillePersistant.getDate_maj().getTime() %>' width='32' height='32' onerror="this.onerror=null;this.remove();"/>
				</span> 
				 
	<%
	if(isNode){ %>
					<ul>
			<%} %>
						<%
	oldLevel = famillePersistant.getLevel();
}
%>
					</li>
				</ul>
				</ul>
<%} %>
		</div>
	</div>
</div>