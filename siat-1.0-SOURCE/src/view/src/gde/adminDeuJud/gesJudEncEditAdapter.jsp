<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/gde/AdministrarEncGesJud.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="gde" key="gde.gesJudEditAdapter.title"/></h1>		
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	<!-- GesJud -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.gesJud.title"/></legend>
		
		<table class="tabladatos">
		<tr>	
			<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
			<td class="normal">
				<logic:empty name="encGesJudAdapterVO" property="listProcurador">				
					<bean:write name="encGesJudAdapterVO" property="gesJud.procurador.descripcion"/>
				</logic:empty>
				<logic:notEmpty name="encGesJudAdapterVO" property="listProcurador">
					<html:select name="encGesJudAdapterVO" property="gesJud.procurador.id" styleClass="select">
						<html:optionsCollection name="encGesJudAdapterVO" property="listProcurador" label="descripcion" value="id" />
					</html:select>				
				</logic:notEmpty>
			</td>
			<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.gesJud.fechaAlta.label"/>: </label></td>
			<td class="normal">
				<logic:equal name="encGesJudAdapterVO" property="act" value="agregar">
					<html:text name="encGesJudAdapterVO" property="gesJud.fechaAltaView" styleId="fechaAltaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaAltaView');" id="a_fechaAltaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
					</a>
				</logic:equal>
				<logic:equal name="encGesJudAdapterVO" property="act" value="modificar">
					<bean:write name="encGesJudAdapterVO" property="gesJud.fechaAltaView"/>				
				</logic:equal>
			</td>
		</tr>
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.gesJud.desGesJud.label"/>: </label></td>
			<td class="normal"><html:text name="encGesJudAdapterVO" property="gesJud.desGesJud" size="20" maxlength="100"/></td>
			<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.gesJud.tipoJuzgado.label"/>: </label></td>
			<td class="normal">
				<html:select name="encGesJudAdapterVO" property="gesJud.tipoJuzgado.codTipoJuzgado" styleClass="select">
					<html:optionsCollection name="encGesJudAdapterVO" property="listTipoJuzgado" label="desTipoJuzgado" value="codTipoJuzgado" />
				</html:select>				
			</td>
		</tr>
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.gesJud.juzgado.label"/>: </label></td>
			<td class="normal"><html:text name="encGesJudAdapterVO" property="gesJud.juzgado" size="20" maxlength="100"/></td>			
		</tr>
		
		<!-- Inclusion de Caso -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
			<td colspan="3">
				<bean:define id="IncludedVO" name="encGesJudAdapterVO" property="gesJud"/>
				<bean:define id="voName" value="gesJud" />
				<%@ include file="/cas/caso/includeCaso.jsp" %>
			</td>
		</tr>
		<!-- Fin Inclusion de Caso -->	
		
		<!-- Nro y Anio Expediente Judicial -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.gesJud.nroExpediente.label"/>: </label></td>
			<td class="normal"><html:text name="encGesJudAdapterVO" property="gesJud.nroExpedienteView" size="20" maxlength="100"/></td>			
			<td><label><bean:message bundle="gde" key="gde.gesJud.anioExpediente.label"/>: </label></td>
			<td class="normal"><html:text name="encGesJudAdapterVO" property="gesJud.anioExpedienteView" size="20" maxlength="100"/></td>
		</tr>
			
		<tr>	
		<!-- Estado -->
			<td><label><bean:message bundle="gde" key="gde.gesJud.estadoGesjud.label"/>: </label></td>
			<td class="normal" colspan="3">
				<bean:write name="encGesJudAdapterVO" property="gesJud.estadoGesJudVO.desEstadoGesJud"/>
			</td>
		</tr>	
		
		<tr>
			<td><label><bean:message bundle="gde" key="gde.gesJud.observaciones.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:textarea name="encGesJudAdapterVO" property="gesJud.observacion" cols="80" rows="15"/>
			</td>
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>
	<!-- GesJud -->
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right">
				<logic:equal name="encGesJudAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="encGesJudAdapterVO" property="act" value="agregar">
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
