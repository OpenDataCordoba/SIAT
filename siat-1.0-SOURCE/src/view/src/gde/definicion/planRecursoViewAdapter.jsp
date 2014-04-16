<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarPlanRecurso.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.planRecursoEditAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- PlanRecurso -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.planRecurso.title"/></legend>
		
		<table class="tabladatos">
			<!-- desPlan -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.plan.desPlan.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="planRecursoAdapterVO" property="planRecurso.plan.desPlan" /></td>			
			</tr>
			<tr>
				<!-- viaDeuda -->
				<td><label><bean:message bundle="def" key="def.viaDeuda.label"/>: </label></td>
				<td class="normal"><bean:write name="planRecursoAdapterVO" property="planRecurso.plan.viaDeuda.desViaDeuda"/></td>
			</tr>
			<!-- Recurso -->
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal">
					<bean:write name="planRecursoAdapterVO" property="planRecurso.recurso.desRecurso"/>
				</td>
			</tr>
			<!-- fechaDesde -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.planDescuento.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<bean:write name="planRecursoAdapterVO" property="planRecurso.fechaDesdeView"/>
				</td>
				<!-- fechaHasta -->
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.planDescuento.fechaHasta.label"/>: </label></td>
				<td class="normal">
					<bean:write name="planRecursoAdapterVO" property="planRecurso.fechaHastaView"/>
				</td>
			</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- PlanRecurso -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="planRecursoAdapterVO" property="act" value="eliminar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('eliminar', '');">
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

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
