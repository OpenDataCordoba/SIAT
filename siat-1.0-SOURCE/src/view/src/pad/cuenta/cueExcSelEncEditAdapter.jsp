<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/pad/AdministrarEncCueExcSel.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="pad" key="pad.cueExcSelAdapter.title"/></h1>		
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- CueExcSel -->
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.cueExcSel.title"/></legend>
		
		<table class="tabladatos">
			<!-- Recurso -->
			<tr>
			<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cueExcSel.cuenta.recurso.label"/>: </label></td>
			<td class="normal">
				<html:select name="encCueExcSelAdapterVO" property="cueExcSel.cuenta.recurso.id" styleClass="select" >
					<bean:define id="includeRecursoList" name="encCueExcSelAdapterVO" property="listRecurso"/>
					<bean:define id="includeIdRecursoSelected" name="encCueExcSelAdapterVO" property="cueExcSel.cuenta.recurso.id"/>
					<%@ include file="/def/gravamen/includeRecurso.jsp" %>
				</html:select>			
			</td>
			
			<!-- Cuenta -->			
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cueExcSel.cuenta.label"/>: </label></td>
				<td class="normal"><html:text name="encCueExcSelAdapterVO" property="cueExcSel.cuenta.numeroCuenta" size="20" maxlength="100" styleClass="datos" />
					<button type="button" onclick="submitForm('buscarCuenta', '');">
	    				<bean:message bundle="pad" key="pad.cueExcSelAdapter.adm.button.buscarCuenta"/>
		      		</button>
		      	</td>
			</tr>
			
			<!-- Area -->		
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cueExcSel.area.label"/>: </label></td>
				<td class="normal">
				<bean:write name="encCueExcSelAdapterVO" property="cueExcSel.area.desArea"/>				
				</td>
			</tr>

		</table>
	</fieldset>
	<!-- CueExcSel -->
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right">
				<logic:equal name="encCueExcSelAdapterVO" property="act" value="agregar">
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
<!-- Fin formulario -->
