<%@ page buffer="64kb" autoFlush="false"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ page import="ar.gov.rosario.siat.base.view.util.*" %>

<% 
   UserSession userSession = (UserSession) session.getAttribute("userSession");
   boolean showAnonimo = userSession == null ? true : userSession.getIsAnonimo();
%>
<h2>&nbsp;</h2>

<div id="pie">
<p>
Desarrollado con Software Libre por la <a href="http://www.rosario.gob.ar">Municipalidad de Rosario</a> y <a href="http://www.tecso.coop" target="_blank">Coop. Tecso Ltda</a> | Rosario | Santa Fe | Argentina | 1997 - 2011
<p/>

<p>
<a href="http://siat.tecso.coop/terminos.html" target="_blank">T&eacute;rminos y condiciones.</a>&nbsp;
<a href="http://siat.tecso.coop/download.html" target="_blank">Contacto</a>
<p/>
</div>
