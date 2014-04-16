<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/emi/AdministrarEmisionPuntual.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="emi" key="emi.emisionPuntualRecibosAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
				<!-- Volver -->
				<button type="button" name="btnVolver" class="boton" onclick="submitForm('volver', '');">
			  	    <bean:message bundle="base" key="abm.button.volver"/>
				</button>
			</td>
		</tr>
	</table>
	
	<fieldset style="clear: both;">
		<p>
			<iframe src="<%=request.getContextPath()%>/emi/AdministrarEmisionPuntual.do?method=getRecibosPDF" height="500" width="650">
			</iframe>
		</p>
	</fieldset>	
				
	<!-- Volver -->
	<button type="button" name="btnVolver" class="boton" onclick="submitForm('volver', '');">
  	    <bean:message bundle="base" key="abm.button.volver"/>
	</button>

	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="false"/>

</html:form>
<!-- Fin formulario -->