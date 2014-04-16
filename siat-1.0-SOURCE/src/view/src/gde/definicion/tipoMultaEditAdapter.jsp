<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formulariº1qos -->
<html:form styleId="filter" action="/gde/AdministrarTipoMulta.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.tipoMultaEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- TipoMulta -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.tipoMulta.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.tipoMulta.descripcion.label"/>: </label></td>
				<td class="normal">
					<html:text name="tipoMultaAdapterVO" property="tipoMulta.desTipoMulta" size="20" maxlength="100"/>
				</td>
			</tr>
			<tr>
				<logic:equal name="tipoMultaAdapterVO" property="act" value="modificar">
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoMultaAdapterVO" property="tipoMulta.recurso.desRecurso"/></td>
				</logic:equal>
				<logic:equal name="tipoMultaAdapterVO" property="act" value="agregar">
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>	
					<td class="normal">
						<html:select name="tipoMultaAdapterVO" property="tipoMulta.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');">
							<bean:define id="includeRecursoList" name="tipoMultaAdapterVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="tipoMultaAdapterVO" property="tipoMulta.recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
					</td>
				</logic:equal>					
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.tipoMulta.recClaDeu.label"/>: </label></td>
				<td class="normal">
					<html:select name="tipoMultaAdapterVO" property="tipoMulta.recClaDeu.id" styleClass="select" >
						<html:optionsCollection name="tipoMultaAdapterVO" property="tipoMulta.recurso.listRecClaDeu" label="desClaDeu" value="id" />
					</html:select>
				</td>
			</tr>
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.tipoMulta.asociadaAOrden.label"/>: </label></td>
				<td class="normal">
					<html:select name="tipoMultaAdapterVO" property="tipoMulta.asociadaAOrden.id" styleClass="select">
						<html:optionsCollection name="tipoMultaAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.tipoMulta.esImporteManual.label"/>: </label></td>
				<td class="normal">
					<html:select name="tipoMultaAdapterVO" property="tipoMulta.esImporteManual.id" styleClass="select">
						<html:optionsCollection name="tipoMultaAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.tipoMulta.canMinDes.label"/>: </label></td>
				<td class="normal"><html:text name="tipoMultaAdapterVO" property="tipoMulta.canMinDesView" size="20" maxlength="100"/></td>			
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.tipoMulta.canMinHas.label"/>: </label></td>
				<td class="normal"><html:text name="tipoMultaAdapterVO" property="tipoMulta.canMinHasView" size="20" maxlength="100"/></td>			
			</tr>
		</table>
	</fieldset>	
	<!-- TipoMulta -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="tipoMultaAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="tipoMultaAdapterVO" property="act" value="agregar">
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
