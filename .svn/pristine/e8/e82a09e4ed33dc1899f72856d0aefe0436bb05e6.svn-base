<%@page import="framework.model.common.util.EncryptionUtil"%>

<%
String mnu = (String)request.getAttribute("curMnu");
%>

<div class="col-lg-12 col-sm-12 col-xs-12">
    <div class="tabbable">
          <ul class="nav nav-tabs" id="myTab">
                <li <%="edit".equals(mnu)?" class='active'":"" %>>
                    <a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("stock.fournisseur.work_edit")%>" >
                     Fiche
                    </a>
                 </li>
                  <li <%="mvm".equals(mnu)?" class='active'":"" %>>
                    <a data-toggle="tab" href="#journeeCaisse" wact="<%=EncryptionUtil.encrypt("stock.fournisseur.findMouvement")%>">
                     Mouvements
                    </a>
                  </li>
                  <li <%="sit".equals(mnu)?" class='active'":"" %>>
                    <a data-toggle="tab" href="#journeeCaisse" wact="<%=EncryptionUtil.encrypt("stock.fournisseur.init_situation")%>">
                     Situation
                    </a>
                  </li>
                  <li <%="ged".equals(mnu)?" class='active'":"" %>>
                    <a data-toggle="tab" href="#journeeCaisse" wact="<%=EncryptionUtil.encrypt("admin.ged.work_find")%>">
                     Documents
                    </a>
                  </li>
           </ul>
      </div>
</div>