<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarConstanciaDeu.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>	
	
	<!-- Si es recomponer -->
	<logic:equal name="constanciaDeuAdapterVO" property="act" value="recomponer">
		<h1><bean:message bundle="gde" key="gde.constanciaDeuAdapter.recomponer.title"/></h1>
	</logic:equal>
			
	<!-- Si es imprimir -->
	<logic:equal name="constanciaDeuAdapterVO" property="act" value="impresionConstancia">
		<h1><bean:message bundle="gde" key="gde.constanciaDeuAdapter.imprimir.title"/></h1>	
	</logic:equal>
	
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
			<!-- Si es recomponer -->
			<logic:equal name="constanciaDeuAdapterVO" property="act" value="recomponer">			
				<iframe src="<%=request.getContextPath()%>/gde/AdministrarConstanciaDeu.do?method=recomponer" height="500" width="650">
				</iframe>
			</logic:equal>

			<!-- Si es imprimir -->
			<logic:equal name="constanciaDeuAdapterVO" property="act" value="impresionConstancia">
					<iframe src="<%=request.getContextPath()%>/gde/AdministrarConstanciaDeu.do?method=imprimir" height="500" width="650">
					</iframe>
			</logic:equal>
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