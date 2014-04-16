<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<form id="filter" action="<%=request.getAttribute("path")%>">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<h1>Impresi&oacute;n</h1>
			</td>				
			<td align="right">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="history.back();">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

	
	<fieldset style="clear: both;">
		<p>
			<iframe src="<%=request.getAttribute("path")%>?method=<%=request.getParameter("method")%>&responseFile=1" height="500" width="650">
			</iframe>
		</p>
    </fieldset>	
	
	<table class="tablabotones" width="100%">
	  	<tr>
  	 		<td align="left" width="50%">
	 	    	<html:button property="btnVolver"  styleClass="boton" onclick="history.back();">
				<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
	    	</td>
		</tr>
	</table>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</form>
<!-- Fin formulario -->