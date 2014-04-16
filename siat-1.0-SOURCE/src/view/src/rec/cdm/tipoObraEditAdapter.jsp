<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rec/AdministrarTipoObra.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				

	<h1><bean:message bundle="rec" key="rec.tipoObraEditAdapter.title"/></h1>

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

	<!-- TipoObra -->
	<fieldset>
		<legend><bean:message bundle="rec" key="rec.tipoObra.title"/></legend>

		<table class="tabladatos">
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>														   
				<td class="normal">
					<html:select name="tipoObraAdapterVO" property="tipoObra.recurso.id" styleClass="select">
						<html:optionsCollection name="tipoObraAdapterVO" property="listRecurso" label="desRecurso" value="id" />
					</html:select>
				</td>					
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.tipoObra.desTipoObra.label"/>: </label></td>
				<td class="normal"><html:text name="tipoObraAdapterVO" property="tipoObra.desTipoObra" size="40" maxlength="100"/></td>			
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.tipoObra.costoCuadra.label"/>: </label></td>
				<td class="normal"><html:text name="tipoObraAdapterVO" property="tipoObra.costoCuadraView" size="10" maxlength="10"/></td>			
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.tipoObra.costoMetroFrente.label"/>: </label></td>
				<td class="normal"><html:text name="tipoObraAdapterVO" property="tipoObra.costoMetroFrenteView" size="10" maxlength="10"/></td>			
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.tipoObra.costoUT.label"/>: </label></td>
				<td class="normal"><html:text name="tipoObraAdapterVO" property="tipoObra.costoUTView" size="10" maxlength="10"/></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.tipoObra.costoModulo.label"/>: </label></td>
				<td class="normal"><html:text name="tipoObraAdapterVO" property="tipoObra.costoModuloView" size="10" maxlength="10"/></td>			
			</tr>

			<logic:equal name="tipoObraAdapterVO" property="act" value="modificar">
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoObraAdapterVO" property="tipoObra.estado.value"/></td>
				</tr>
			</logic:equal>

		</table>
	</fieldset>	
	<!-- TipoObra -->

	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="tipoObraAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="tipoObraAdapterVO" property="act" value="agregar">
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
