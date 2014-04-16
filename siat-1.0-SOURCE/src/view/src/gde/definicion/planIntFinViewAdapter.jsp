<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarPlanIntFin.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.planIntFinViewAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- PlanIntFin -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.planIntFin.title"/></legend>
			<table class="tabladatos">
				<!-- desPlan -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.desPlan.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="planIntFinAdapterVO" property="planIntFin.plan.desPlan" /></td>			
				</tr>
				<!-- recurso -->
				<tr>
					<!-- viaDeuda -->
					<td><label><bean:message bundle="def" key="def.viaDeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="planIntFinAdapterVO" property="planIntFin.plan.viaDeuda.desViaDeuda"/></td>
				</tr>
				<!-- cuotaHasta -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planIntFin.cuotaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="planIntFinAdapterVO" property="planIntFin.cuotaHastaView" /></td>			
					<!-- interes -->
					<td><label><bean:message bundle="gde" key="gde.planIntFin.interes.label"/>: </label></td>
					<td class="normal"><bean:write name="planIntFinAdapterVO" property="planIntFin.interesView" /></td>			
				</tr>
				<!-- fechaDesde -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planIntFin.fechaDesde.label"/>: </label></td>
					<td class="normal">
						<bean:write name="planIntFinAdapterVO" property="planIntFin.fechaDesdeView" />
					</td>
					<!-- fechaHasta -->
					<td><label><bean:message bundle="gde" key="gde.planIntFin.fechaHasta.label"/>: </label></td>
					<td class="normal">
						<bean:write name="planIntFinAdapterVO" property="planIntFin.fechaHastaView" />
					</td>
				</tr>
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="planIntFinAdapterVO" property="planIntFin.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- PlanIntFin -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="planIntFinAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="planIntFinAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="planIntFinAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
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
