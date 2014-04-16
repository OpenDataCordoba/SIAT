<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarAproOrdCon.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="ef" key="ef.aproOrdConEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- AproOrdCon -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.aproOrdCon.title"/></legend>
		
		<table class="tabladatos">
		
		<tr>
			<td><label><bean:message bundle="ef" key="ef.aproOrdCon.fecha.label"/>: </label></td>
			<td class="normal"><bean:write name="aproOrdConAdapterVO" property="aproOrdCon.fechaView"/></td>				
		</tr>
		
		<!-- observacion -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.aproOrdCon.observacion.label"/>: </label></td>
			<td class="normal"><html:textarea name="aproOrdConAdapterVO" property="aproOrdCon.observacion"/></td>			
		</tr>
		<!-- EstadoOrden -->
		<tr>	
			<td><label><bean:message bundle="ef" key="ef.estadoOrden.label"/>: </label></td>
			<td class="normal">
				<html:select name="aproOrdConAdapterVO" property="aproOrdCon.estadoOrden.id" styleClass="select" onchange="submitForm('paramEstadoOrden', '');">
					<html:optionsCollection name="aproOrdConAdapterVO" property="listEstadoOrden" label="desEstadoOrden" value="id" />
				</html:select>
			</td>					
		</tr>
		</table>
	</fieldset>		
	<!-- AproOrdCon -->
	
	<!-- AplicarAjustes -->
	<fieldset>
	<legend><bean:message bundle="ef" key="ef.aproOrdCon.incorporacionAjustes.title"/></legend>
		
		<table class="tabladatos">
		<!-- EstadoOrden -->
		<logic:equal name="aproOrdConAdapterVO" property="aplicarAjusteEnabled" value="true">
			<tr>
				<td><label><bean:message bundle="ef" key="ef.aproOrdCon.aplicarAjustes.label"/>: </label></td>
				<td><div align="left"><html:checkbox disabled="false" value="off" name="aproOrdConAdapterVO" property="aplicarAjuste" onclick="submitForm('paramAplicarAjuste', '');" styleId="chkNewFile"/></div></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.multaEditAdapter.resolucion.label"/>: </label></td>
				<!-- Inclucion de CasoView -->
				<td>
					<bean:define id="IncludedVO" name="aproOrdConAdapterVO" property="aproOrdCon.cobranza"/>
					<bean:define id="voName" value="aproOrdCon.cobranza" />
					<%@ include file="/cas/caso/includeCasoSubmitQuitar.jsp" %>				
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.multaEditAdapter.fechaResolucion.label"/>: </label></td>
				<td class="normal">
					<html:text name="aproOrdConAdapterVO" property="aproOrdCon.cobranza.fechaResolucionView" styleId="fechaResolucionView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar_change('fechaResolucionView');" id="a_fechaResolucionView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
						</a>
				</td>
			</tr>
		</logic:equal>
		<logic:equal name="aproOrdConAdapterVO" property="aplicarAjusteEnabled" value="false">
			<logic:equal name="aproOrdConAdapterVO" property="aprobarOrdenEnabled" value="false">
				<td><label><bean:message bundle="ef" key="ef.aproOrdCon.aplicarAjustes.label"/>: </label></td>
				<td><div align="left"><html:checkbox disabled="true" value="off" name="aproOrdConAdapterVO" property="aplicarAjuste"   onclick="submitForm('paramAplicarAjuste', '');" styleId="chkNewFile"/></div></td>
			</logic:equal>
			<logic:equal name="aproOrdConAdapterVO" property="aprobarOrdenEnabled" value="true">
				<td><label><bean:message bundle="ef" key="ef.aproOrdCon.aplicarAjustes.label"/>: </label></td>
				<td><div align="left"><input type="checkbox" id="chkNewFile" onclick="submitForm('paramAplicarAjuste', '');" value="on" name="aplicarAjuste"></div></td>
			</logic:equal>
		</logic:equal>
		</table>
	</fieldset>	
	<!-- AplicarAjustes -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="aproOrdConAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="aproOrdConAdapterVO" property="act" value="agregar">
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
<!-- aproOrdConEditAdapter.jsp -->
