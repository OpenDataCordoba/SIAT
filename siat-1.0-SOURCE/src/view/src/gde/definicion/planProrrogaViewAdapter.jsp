<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarPlanProrroga.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.planProrrogaViewAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- PlanProrroga -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.planProrroga.title"/></legend>
			<table class="tabladatos">
				<!-- desPlan -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.desPlan.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="planProrrogaAdapterVO" property="planProrroga.plan.desPlan" /></td>			
				</tr>
				<!-- viaDeuda -->
				<tr>
					<td><label><bean:message bundle="def" key="def.viaDeuda.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="planProrrogaAdapterVO" property="planProrroga.plan.viaDeuda.desViaDeuda"/></td>
				</tr>
				<!-- desPlanProrroga -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planProrroga.desPlanProrroga.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="planProrrogaAdapterVO" property="planProrroga.desPlanProrroga"/></td>			
				</tr>
				<!-- fecVto -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planProrroga.fecVto.label"/>: </label></td>
					<td class="normal">
						<bean:write name="planProrrogaAdapterVO" property="planProrroga.fecVtoView"/>
					</td>
					<!-- fecVtoNue -->
					<td><label><bean:message bundle="gde" key="gde.planProrroga.fecVtoNue.label"/>: </label></td>
					<td class="normal">
						<bean:write name="planProrrogaAdapterVO" property="planProrroga.fecVtoNueView" />
					</td>
				</tr>
				
				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="planProrrogaAdapterVO" property="planProrroga"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<!-- Fin Inclucion de CasoView -->
				
				<!-- fechaDesde -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planProrroga.fechaDesde.label"/>: </label></td>
					<td class="normal">
						<bean:write name="planProrrogaAdapterVO" property="planProrroga.fechaDesdeView" />
					</td>
					<!-- fechaHasta -->
					<td><label><bean:message bundle="gde" key="gde.planProrroga.fechaHasta.label"/>: </label></td>
					<td class="normal">
						<bean:write name="planProrrogaAdapterVO" property="planProrroga.fechaHastaView"/>
					</td>
				</tr>
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="planProrrogaAdapterVO" property="planProrroga.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- PlanProrroga -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="planProrrogaAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="planProrrogaAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="planProrrogaAdapterVO" property="act" value="desactivar">
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
