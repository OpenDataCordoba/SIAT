<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarPlanImpMin.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.planImpMinViewAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- PlanImpMin -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.planImpMin.title"/></legend>
		<table class="tabladatos">
			<!-- desPlan -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.plan.desPlan.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="planImpMinAdapterVO" property="planImpMin.plan.desPlan" /></td>			
			</tr>
			<!-- recurso -->
			<tr>
			<!-- viaDeuda -->
				<td><label><bean:message bundle="def" key="def.viaDeuda.label"/>: </label></td>
				<td class="normal"><bean:write name="planImpMinAdapterVO" property="planImpMin.plan.viaDeuda.desViaDeuda"/></td>
			</tr>
			<tr>
				<!-- cantidadCuotas -->
				<td><label><bean:message bundle="gde" key="gde.planImpMin.cantidadCuotas.label"/>: </label></td>
				<td class="normal"><bean:write name="planImpMinAdapterVO" property="planImpMin.cantidadCuotasView"/></td>			
			</tr>
			<tr>
				<!-- impMinDeu -->
				<td><label><bean:message bundle="gde" key="gde.planImpMin.impMinDeu.label"/>: </label></td>
				<td class="normal"><bean:write name="planImpMinAdapterVO" property="planImpMin.impMinDeuView"/></td>
			</tr>
			<tr>			
				<!-- impMinCuo -->
				<td><label><bean:message bundle="gde" key="gde.planImpMin.impMinCuo.label"/>: </label></td>
				<td class="normal"><bean:write name="planImpMinAdapterVO" property="planImpMin.impMinCuoView"/></td>			
			</tr>
			
			<!-- fechaDesde -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.planImpMin.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<bean:write name="planImpMinAdapterVO" property="planImpMin.fechaDesdeView"/>
				</td>
				<!-- fechaHasta -->
				<td><label><bean:message bundle="gde" key="gde.planImpMin.fechaHasta.label"/>: </label></td>
				<td class="normal">
					<bean:write name="planImpMinAdapterVO" property="planImpMin.fechaHastaView"/>
				</td>
			</tr>
		</table>
	</fieldset>	
	<!-- PlanImpMin -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
	   	    <td align="right" width="50%">
					<logic:equal name="planImpMinAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
   	    	</td>
   	    </tr>
   	</table>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
