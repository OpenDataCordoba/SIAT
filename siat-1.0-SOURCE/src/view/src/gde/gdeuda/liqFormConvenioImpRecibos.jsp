<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/ImprimirConvenio.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>	
	
	
	<h1><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>	
	<fieldset style="clear: both;">
		<p>
			<iframe src="<%=request.getContextPath()%>/gde/ImprimirConvenio.do?method=getRecibosPDF" height="500" width="650">
			</iframe>
		</p>
    </fieldset>	
				
	<logic:notEqual name="userSession" property="isAnonimoView" value="1">
		<!-- Volver -->
		<button type="button" name="btnVolver" class="boton" onclick="submitForm('volver', '');">
	  	    <bean:message bundle="base" key="abm.button.volver"/>
		</button>
	</logic:notEqual>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->