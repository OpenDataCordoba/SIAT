<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarPlanClaDeu.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.planClaDeuEditAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- PlanClaDeu -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.planClaDeu.title"/></legend>
		
		<table class="tabladatos">
			<!-- desPlan -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.plan.desPlan.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="planClaDeuAdapterVO" property="planClaDeu.plan.desPlan" /></td>			
			</tr>
			<!-- viaDeuda -->
			<tr>
				<td><label><bean:message bundle="def" key="def.viaDeuda.label"/>: </label></td>
				<td class="normal"><bean:write name="planClaDeuAdapterVO" property="planClaDeu.plan.viaDeuda.desViaDeuda"/></td>
			</tr>
			<!-- recurso -->
			<tr>
				<logic:equal name="planClaDeuAdapterVO" property="act" value="agregar">
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:select name="planClaDeuAdapterVO" property="planClaDeu.plan.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');">
							<html:optionsCollection name="planClaDeuAdapterVO" property="listRecurso" label="desRecurso" value="id" />
						</html:select>
					</td>
				</logic:equal>
				<logic:equal name="planClaDeuAdapterVO" property="act" value="modificar">
					<td><label><bean:message bundle="def" key="def.recurso.label"/></label></td>
					<td class="normal" colspan="3">
						<bean:write name="planClaDeuAdapterVO" property="planClaDeu.recClaDeu.recurso.desRecurso" />
					</td>
				</logic:equal>
			</tr>
			<!-- RecClaDeu -->
			<tr>
				<logic:equal name="planClaDeuAdapterVO" property="act" value="agregar">
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recClaDeu.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:select name="planClaDeuAdapterVO" property="planClaDeu.recClaDeu.id" styleClass="select">
							<html:optionsCollection name="planClaDeuAdapterVO" property="listRecClaDeu" label="desClaDeu" value="id" />
						</html:select>
					</td>
				</logic:equal>
				
				<logic:equal name="planClaDeuAdapterVO" property="act" value="modificar">
					<td><label><bean:message bundle="def" key="def.recClaDeu.label"/>: </label></td>
					<td class="normal" colspan="3">
						<bean:write name="planClaDeuAdapterVO" property="planClaDeu.recClaDeu.desClaDeu" />
					</td>		
				</logic:equal>
			</tr>
			<!-- fechaDesde -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.planClaDeu.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="planClaDeuAdapterVO" property="planClaDeu.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			
				<!-- fechaHasta -->
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.planClaDeu.fechaHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="planClaDeuAdapterVO" property="planClaDeu.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- PlanClaDeu -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="planClaDeuAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="planClaDeuAdapterVO" property="act" value="agregar">
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
