<%@ page buffer="64kb" autoFlush="false"%>
<%@ page import="ar.gov.rosario.siat.base.view.util.*" %>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<% 
   UserSession userSession = (UserSession) session.getAttribute("userSession");
   boolean showAnonimo = userSession == null ? true : userSession.getIsAnonimo();
   
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

<% if (!showAnonimo) { %>

	<div id="userinfo"><b><bean:write name="userSession" property="longUserName"/> (<bean:write name="userSession" property="userName"/>)</b> &nbsp; <bean:write name="userSession" property="desOficina"/> / <bean:write name="userSession" property="desArea"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>

<% } %>
	<!-- siempre muestro el menu vacio -->
	<div id="menu">
	    <ul>
	        <li>
				<span>&nbsp;</span>
		    </li>
		</ul>
	</div>

<ul> </ul>