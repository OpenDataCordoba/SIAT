<%@ page buffer="64kb" autoFlush="false"%>
<%@ page import="ar.gov.rosario.siat.base.view.util.*" %>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<% 
   UserSession userSession = (UserSession) session.getAttribute("userSession");
   boolean showAnonimo = userSession == null ? true : userSession.getIsAnonimo();
   boolean showMenu = false; 
   // si no hay oficina seleccinada no mostramos boton de menu
   if (userSession != null && (userSession.getIdOficina() == null || userSession.getIdOficina().longValue() != -1)) {
   	showMenu = true;
   }
   
%>

<script type="text/javascript">
var TxInicial = 12;
var textoHtml;
var numCol = 1;
function zoom(Factor) {
    tx = document.getElementById("container");
    TxInicial = TxInicial + Factor;
    tx.style.fontSize = TxInicial;
    if (numCol > 1) {
        tb = document.getElementById("container");
        tb.style.fontSize = TxInicial;
    }
}
</script>

	<div id="userinfo">
	<% if(ar.gov.rosario.siat.base.iface.model.SiatParam.isWebSiat() &&
			"0".equals(ar.gov.rosario.siat.base.iface.model.SiatParam.getString("webSiatOn")) && 
			userSession.getEsAdmin()){%>
			<div class="messages"> 
				"La instancia Web de la Aplicacion se encuentra deshabilitada"
			</div>
	<%}%>
	
	<% if(ar.gov.rosario.siat.base.iface.model.SiatParam.isIntranetSiat() &&
			"0".equals(ar.gov.rosario.siat.base.iface.model.SiatParam.getString("intraSiatOn")) && 
			userSession.getEsAdmin()){%>
			<div class="messages">
				"La instancia Intra de la Aplicacion se encuentra deshabilitada"
			</div>
	<%}%>
	
<ul>
</ul>
