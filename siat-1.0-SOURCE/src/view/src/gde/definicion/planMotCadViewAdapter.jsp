<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarPlanMotCad.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.planMotCadViewAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- PlanMotCad -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.planMotCad.title"/></legend>
			<table class="tabladatos">
				<!-- desPlan -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.desPlan.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="planMotCadAdapterVO" property="planMotCad.plan.desPlan" /></td>			
				</tr>
				<!-- recurso -->
				<tr>
						<!-- viaDeuda -->
					<td><label><bean:message bundle="def" key="def.viaDeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="planMotCadAdapterVO" property="planMotCad.plan.viaDeuda.desViaDeuda"/></td>
				</tr>
				<!-- desPlanMotCad -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planMotCad.desPlanMotCad.label"/>: </label></td>
					<td class="normal"><bean:write name="planMotCadAdapterVO" property="planMotCad.desPlanMotCad" /></td>
				</tr>
				<!-- esEspecial -->
				<tr>	
					<td><label><bean:message bundle="gde" key="gde.planMotCad.esEspecial.label"/>: </label></td>
					<td class="normal" colspan="3">
						<bean:write name="planMotCadAdapterVO" property="planMotCad.esEspecial.value" />
					</td>					
				</tr>
				<!-- cantCuoCon -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planMotCad.cantCuoCon.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="planMotCadAdapterVO" property="planMotCad.cantCuoConView" /></td>			
				</tr>
				<!-- cantCuoAlt -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planMotCad.cantCuoAlt.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="planMotCadAdapterVO" property="planMotCad.cantCuoAltView" /></td>		
				</tr>
				<!-- cantDias -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planMotCad.cantDias.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="planMotCadAdapterVO" property="planMotCad.cantDiasView"/></td>			
				</tr>
				<!-- className -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planMotCad.className.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="planMotCadAdapterVO" property="planMotCad.className" /></td>
				</tr>
				<!-- fechaDesde -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planMotCad.fechaDesde.label"/>: </label></td>
					<td class="normal">
						<bean:write name="planMotCadAdapterVO" property="planMotCad.fechaDesdeView" />
					</td>
				
					<td><label><bean:message bundle="gde" key="gde.planMotCad.fechaHasta.label"/>: </label></td>
					<td class="normal">
						<bean:write name="planMotCadAdapterVO" property="planMotCad.fechaHastaView" />
					</td>
				</tr>
				
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="planMotCadAdapterVO" property="planMotCad.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- PlanMotCad -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="planMotCadAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="planMotCadAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="planMotCadAdapterVO" property="act" value="desactivar">
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
