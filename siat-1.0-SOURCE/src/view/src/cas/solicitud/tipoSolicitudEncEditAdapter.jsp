<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/cas/AdministrarEncTipoSolicitud.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="cas" key="cas.tipoSolicitudEditAdapter.title"/></h1>		

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- TipoSolicitud -->
	<fieldset>
		<legend><bean:message bundle="cas" key="cas.tipoSolicitud.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="cas" key="cas.tipoSolicitud.codigo.ref"/>: </label></td>
				<td class="normal">
					<logic:equal name="encTipoSolicitudAdapterVO" property="act" value="modificar">
						<bean:write name="encTipoSolicitudAdapterVO" property="tipoSolicitud.codigo"/>
				 	</logic:equal>
					<logic:equal name="encTipoSolicitudAdapterVO" property="act" value="agregar">
						<html:text name="encTipoSolicitudAdapterVO" property="tipoSolicitud.codigo" size="20" maxlength="20" />
				 	</logic:equal>	
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="cas" key="cas.tipoSolicitud.descripcion.label"/>: </label></td>
				<td class="normal"><html:text name="encTipoSolicitudAdapterVO" property="tipoSolicitud.descripcion" size="20" maxlength="100"/></td>			
			</tr>
			<!--Area Destino-->
			<tr>	
				<td><label><bean:message bundle="cas" key="cas.tipoSolicitud.areaDestino.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="encTipoSolicitudAdapterVO" property="tipoSolicitud.areaDestino.id" styleClass="select">
						<html:optionsCollection name="encTipoSolicitudAdapterVO" property="listArea" label="desArea" value="id" />
					</html:select>
				</td>					
			</tr>
			
		</table>
	</fieldset>
	<!-- TipoSolicitud -->
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right">
				<logic:equal name="encTipoSolicitudAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="encTipoSolicitudAdapterVO" property="act" value="agregar">
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