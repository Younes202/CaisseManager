<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="com.google.firebase.database.core.Context"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
	
	<%
	String mnu = (String)request.getAttribute("mnu_param");
	%>
	
	  <li <%=mnu.equals("general") ? " class='active'":"" %>>
       <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.parametrage.work_edit")%>" params="tp=gen">
        G&eacute;n&eacute;rale
       </a>
      </li>
      
 <%if(ContextAppli.IS_CAISSE_EXIST()){ %>     
	  <li <%=mnu.equals("caisse") ? " class='active'":"" %>>
         <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.parametrage.work_edit")%>" params="tp=cais">
          Caisse
         </a>
      </li>
      <li <%=mnu.equals("interface") ? " class='active'":"" %>>
         <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.parametrage.work_edit")%>" params="tp=ihm">
          Interface caisse
         </a>
       </li>
       <li <%=mnu.equals("ticket") ? " class='active'":"" %>>
         <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.parametrage.work_edit")%>" params="tp=tick">
          Ticket de caisse
         </a>
       </li>
   <%} %>   
       
    <%if(ContextAppli.IS_RESTAU_ENV()){ %>	                           
        <li <%=mnu.equals("cuis") ? " class='active'":"" %>>
         <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.parametrage.work_edit")%>" params="tp=cuis">
          Cuisine
         </a>
       </li>
       <li <%=mnu.equals("token") ? " class='active'":"" %>>
         <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.token.work_find")%>">
          Tokens
         </a>
       </li>
     <%} %>
     
       <li <%=mnu.equals("divers") ? " class='active'":"" %>>
         <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.parametrage.work_edit")%>" params="tp=div">
          Divers
         </a>
       </li>
       <li <%=mnu.equals("cmdline") ? " class='active'":"" %>>
		  <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.parametrage.work_edit")%>" params="tp=cmdl">
           Etablissement/Cmd en ligne
          </a>
       </li> 
      	<li <%=mnu.equals("maintenance") ? " class='active'":"" %>>
          <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.parametrage.work_edit")%>" params="tp=mnt">
           Maintenance
          </a>
        </li>