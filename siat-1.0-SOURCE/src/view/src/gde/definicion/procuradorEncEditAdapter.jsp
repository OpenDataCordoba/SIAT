<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/gde/AdministrarEncProcurador.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="gde" key="gde.procuradorEncEditAdapter.title"/></h1>		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- Procurador -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.procurador.title"/></legend>
		<table class="tabladatos">
			<!-- Descripcion -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procurador.descripcion.label"/>: </label></td>
				<td class="normal"><html:text name="encProcuradorAdapterVO" property="procurador.descripcion" size="20" maxlength="100"/></td>			
			</tr>

			<!-- Domicilio -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.procurador.domicilio.label"/>: </label></td>
				<td class="normal"><html:text name="encProcuradorAdapterVO" property="procurador.domicilio" size="20" maxlength="100"/></td>			
			</tr>

			<!-- Telefono -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.procurador.telefono.label"/>: </label></td>
				<td class="normal"><html:text name="encProcuradorAdapterVO" property="procurador.telefono" size="50" maxlength="100"/></td>			
			</tr>

			<!-- HorarioAtencion -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.procurador.horarioAtencion.label"/>: </label></td>
				<td class="normal"><html:text name="encProcuradorAdapterVO" property="procurador.horarioAtencion" size="20" maxlength="100"/></td>			
			</tr>

			<!-- TipoProcurador -->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procurador.tipoProcurador.label"/>: </label></td>
				<td class="normal">
					<html:select name="encProcuradorAdapterVO" property="procurador.tipoProcurador.id" styleClass="select">
						<html:optionsCollection name="encProcuradorAdapterVO" property="listTipoProcurador" label="desTipoProcurador" value="id" />
					</html:select>
				</td>					
			</tr>

			<!-- Observacion -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.procurador.observacion.label"/>: </label></td>
				<td class="normal"><html:textarea name="encProcuradorAdapterVO" property="procurador.observacion" rows="10" cols="30"/></td>			
			</tr>

		</table>
	</fieldset>
	<!-- Procurador -->
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right">
				<logic:equal name="encProcuradorAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="encProcuradorAdapterVO" property="act" value="agregar">
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
