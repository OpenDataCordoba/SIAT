<%@ page buffer="64kb" autoFlush="false"%>
<%@ page import="ar.gov.rosario.siat.base.view.util.*, ar.gov.rosario.siat.base.iface.model.*" %>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<% 
   UserSession userSession = (UserSession) session.getAttribute("userSession");
   boolean showAnonimo = userSession == null ? true : userSession.getIsAnonimo();
%>

<% if (showAnonimo) { %>
	<div id="top">
	</div>
<% } else { %>
	<div id="top">
		<div id="iconostop">
      			<ul>
				<li><a href="/siat/seg/Login.do?method=logout" title="Salir">Salir</a></li>
				<!-- menu de solicitudes emitidas -->    
				<logic:equal name="userSession" property="canAccessSolEmitidasMenu" value="true">    
		        	<li><a href="/siat/cas/BuscarSolicitud.do?method=irEmitidasArea">&nbsp;&nbsp;Solicitudes Emitidas&nbsp;&nbsp;</a></li>
		   		</logic:equal> 
		    		
				<!-- menu de solicitudes pendientes -->
		    		<logic:equal name="userSession" property="canAccessSolPendMenu" value="true"> 
		        	<li><a href="/siat/cas/BuscarSolicitud.do?method=irPendientesArea">&nbsp;&nbsp;Solicitudes Pendientes&nbsp;&nbsp;</a></li>
				</logic:equal>
			
	        		<li><a href="/siat/seg/SiatMenu.do?method=build">&nbsp;&nbsp;Men&uacute;&nbsp;&nbsp;</a></li>
      			</ul>
		</div>
	</div>
      	<div style="float:right; margin-right: 8px; margin-top: -18px">
		<div>
		<bean:write name="userSession" property="longUserName"/> (<bean:write name="userSession" property="userName"/>)&nbsp; 
		<bean:write name="userSession" property="desOficina"/> / <bean:write name="userSession" property="desArea"/>
      		</div>
	</div>
<% } %>

<% if(ar.gov.rosario.siat.base.iface.model.SiatParam.isWebSiat() &&
	"0".equals(ar.gov.rosario.siat.base.iface.model.SiatParam.getString("webSiatOn")) && 
	userSession.getEsAdmin()){%>
	<div class="messages"> 
		La instancia Web de la Aplicacion se encuentra deshabilitada. Intente en unos minutos.
	</div>
<%}%>
	
<% if(ar.gov.rosario.siat.base.iface.model.SiatParam.isIntranetSiat() &&
		"0".equals(ar.gov.rosario.siat.base.iface.model.SiatParam.getString("intraSiatOn")) && 
		userSession.getEsAdmin()){%>
		<div class="messages">
			La instancia Intranet de la Aplicacion se encuentra deshabilitada. Intente en unos minutos.
		</div>
<%}%>

