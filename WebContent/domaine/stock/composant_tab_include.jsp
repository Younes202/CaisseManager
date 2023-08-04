<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>

<%
String currTab = (String)request.getAttribute("comp_tab");
%>

<div class="tabbable">
    <ul class="nav nav-tabs" id="myTab">
          <li <%="comp".equals(currTab) ? " class='active'":"" %>>
              <a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("stock.composant.work_edit")%>" >
               Fiche composant
              </a>
           </li>
           <%if(ControllerUtil.getMenuAttribute("articleId", request) != null){ %>
            <li <%="mvm".equals(currTab) ? " class='active'":"" %>>
              <a data-toggle="tab" href="#mouvement" wact="<%=EncryptionUtil.encrypt("stock.composant.find_mouvement")%>">
               Mouvements
              </a>
            </li>
            <li <%="fourn".equals(currTab) ? " class='active'":"" %>>
              <a data-toggle="tab" href="#mouvement" wact="<%=EncryptionUtil.encrypt("stock.composant.find_fournisseur")%>">
               Fournisseurs
              </a>
            </li>
            <% }%>
     </ul>
</div>