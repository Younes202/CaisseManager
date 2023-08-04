<%@page import="org.apache.log4j.Logger"%>
<%@ page isErrorPage="true" import="java.io.*" %>

<%
	Logger LOGGER = Logger.getLogger(this.getClass());
	if(exception != null){
		LOGGER.error(exception.getMessage(), exception);
	}
%>

    <div class="error-header"> </div>
    <div class="container body-500">
        <section class="error-container text-center">
            <h1 style="color: orange;">PAGE NON AUTORISÉE</h1>
            <div class="error-divider">
                <h2>Acc&egrave;s non autoris&eacute;</h2>
                <br>
                <p class="description">Votre profil ne permet pas de consulter cette page !</p>
                <br><br>
                <a href="#lmnu=lgo" style="text-decoration: underline;color: blue;font-size: 20px;">
                   <i class="fa fa-power-off"></i> RETOUR VERS LA PAGE DE CONNEXION
                </a>
            </div>
        </section>
    </div>
