<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarTramite.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="gde" key="gde.tramiteSearchPage.title"/></h1>	
		
	<p>
		<bean:message bundle="gde" key="gde.tramiteSearchPage.legend"/>
	</p>
		
	<!-- Filtro -->
	<fieldset>
	<legend>Datos del Certificado</legend>
		<table class="tabladatos">
			<tr>	
				<td><label><bean:message bundle="gde" key="gde.tipoTramite.label"/>: </label></td>
				<td class="normal">
					<html:select name="tramiteSearchPageVO" property="tramite.tipoTramite.codTipoTramite" styleClass="select">
						<html:optionsCollection name="tramiteSearchPageVO" property="listTipoTramite" label="desTipoTramite" value="codTipoTramite" />
					</html:select>
				</td>					
			</tr>
			<tr>
				<td><label>Recibo Nro/Año: </label></td>
				<td class="normal">
					<html:text name="tramiteSearchPageVO" property="tramite.nroRecibo" size="10" maxlength="100"/>/ 
					<html:text name="tramiteSearchPageVO" property="tramite.anioRecibo" size="10" maxlength="100"/>
				</td>
			</tr>
		</table>
			
		<p align="center">
		  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
				<bean:message bundle="base" key="abm.button.limpiar"/>
			</html:button>
			&nbsp;
		  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
				<bean:message bundle="base" key="abm.button.buscar"/>
			</html:button>
		</p>
	</fieldset>	
	<!-- Fin Filtro -->

	<table class="tablabotones">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>			
		</tr>
	</table>
		
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
