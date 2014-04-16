<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarPlanMotCad.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.planMotCadEditAdapter.title"/></h1>	
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
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.planMotCad.desPlanMotCad.label"/>: </label></td>
				<td class="normal"><html:text name="planMotCadAdapterVO" property="planMotCad.desPlanMotCad" size="20" maxlength="100"/></td>			
			</tr>
			<!-- esEspecial -->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.planMotCad.esEspecial.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="planMotCadAdapterVO" property="planMotCad.esEspecial.id" styleClass="select" onchange="submitForm('paramEsEspecial', '');">
						<html:optionsCollection name="planMotCadAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
			<!-- No es Especial -->
			<logic:equal name="planMotCadAdapterVO" property="flagEsEspecial" value="false">
				<!-- cantCuoCon -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planMotCad.cantCuoCon.label"/>: </label></td>
					<td class="normal" colspan="3"><html:text name="planMotCadAdapterVO" property="planMotCad.cantCuoConView" size="20" maxlength="100"/></td>			
				</tr>
				<!-- cantCuoAlt -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planMotCad.cantCuoAlt.label"/>: </label></td>
					<td class="normal" colspan="3"><html:text name="planMotCadAdapterVO" property="planMotCad.cantCuoAltView" size="20" maxlength="100"/></td>			
				</tr>
				<!-- cantDias -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planMotCad.cantDias.label"/>: </label></td>
					<td class="normal" colspan="3"><html:text name="planMotCadAdapterVO" property="planMotCad.cantDiasView" size="20" maxlength="100"/></td>			
				</tr>
			</logic:equal>
			<logic:notEqual name="planMotCadAdapterVO" property="flagEsEspecial" value="false">
				<!-- cantCuoCon -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planMotCad.cantCuoCon.label"/>: </label></td>
					<td class="normal" colspan="3"><html:text name="planMotCadAdapterVO" property="planMotCad.cantCuoConView" disabled="true"/></td>			
				</tr>
				<!-- cantCuoAlt -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planMotCad.cantCuoAlt.label"/>: </label></td>
					<td class="normal" colspan="3"><html:text name="planMotCadAdapterVO" property="planMotCad.cantCuoAltView" disabled="true"/></td>			
				</tr>
				<!-- cantDias -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planMotCad.cantDias.label"/>: </label></td>
					<td class="normal" colspan="3"><html:text name="planMotCadAdapterVO" property="planMotCad.cantDiasView" disabled="true"/></td>			
				</tr>
			</logic:notEqual>
			
			<!-- Es Especial -->
			<logic:equal name="planMotCadAdapterVO" property="flagEsEspecial" value="true">
				<!-- className -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planMotCad.className.label"/>: </label></td>
					<td class="normal" colspan="3"><html:text name="planMotCadAdapterVO" property="planMotCad.className" size="20" maxlength="100"/></td>			
				</tr>
			</logic:equal>
			<logic:notEqual name="planMotCadAdapterVO" property="flagEsEspecial" value="true">
				<!-- className -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planMotCad.className.label"/>: </label></td>
					<td class="normal" colspan="3"><html:text name="planMotCadAdapterVO" property="planMotCad.className" disabled="true"/></td>			
				</tr>
			</logic:notEqual>
			
			<!-- fechaDesde -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.planMotCad.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="planMotCadAdapterVO" property="planMotCad.fechaDesdeView" styleId="fechaDesdeView" size="10" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.planMotCad.fechaHasta.label"/>: </label></td>
				<td class="normal" colspan="1">
					<html:text name="planMotCadAdapterVO" property="planMotCad.fechaHastaView" styleId="fechaHastaView" size="10" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- PlanMotCad -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="planMotCadAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="planMotCadAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
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
