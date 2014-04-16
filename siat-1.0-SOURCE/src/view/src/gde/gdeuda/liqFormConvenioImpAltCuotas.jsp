<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqFormConvenio.do">
	
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
    			<button type="button" name="btnVolver" class="boton" onclick="submitForm('alternativaCuotas', '');">
			  	    <bean:message bundle="base" key="abm.button.volver"/>
				</button>
			</td>
		</tr>
	</table>
	<fieldset style="clear: both;">
		<p>
			<iframe src="<%=request.getContextPath()%>/gde/AdministrarLiqFormConvenio.do?method=getAltCuotasPDF" height="500" width="650">
			</iframe>
		</p>
    </fieldset>	
				
	<!-- Volver -->
	<button type="button" name="btnVolver" class="boton" onclick="submitForm('alternativaCuotas', '');">
  	    <bean:message bundle="base" key="abm.button.volver"/>
	</button>
	<input type="hidden" name="idPlan" value="<bean:write name="liqFormConvenioAdapterVO" property="planSelected.idPlan" bundle="base" formatKey="general.format.id"/>"/>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->