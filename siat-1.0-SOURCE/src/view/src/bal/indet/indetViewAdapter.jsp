<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarIndet.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.indetAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<fieldset>
			<logic:notEqual name="indetAdapterVO" property="act" value="desgloce">
				<legend><bean:message bundle="bal" key="bal.indet.title"/></legend>			
			</logic:notEqual>
			<logic:equal name="indetAdapterVO" property="act" value="desgloce">
				<legend><bean:message bundle="bal" key="bal.indet.desgloce.title"/></legend>			
			</logic:equal>
			<table class="tabladatos" width="100%">
				<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.sistema.label"/>: </label></td>
				<td class="normal"><bean:write name="indetAdapterVO" property="indet.sistema"/></td>

				<td><label><bean:message bundle="bal" key="bal.indet.nroComprobante.label"/>: </label></td>
				<td class="normal"><bean:write name="indetAdapterVO" property="indet.nroComprobante"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.clave.label"/>: </label></td>
				<td class="normal"><bean:write name="indetAdapterVO" property="indet.clave"/></td>
			
				<td><label><bean:message bundle="bal" key="bal.indet.partida.label"/>: </label></td>
				<td class="normal"><bean:write name="indetAdapterVO" property="indet.partida"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.resto.label"/>: </label></td>
				<td class="normal"><bean:write name="indetAdapterVO" property="indet.resto"/></td>
			
				<td><label><bean:message bundle="bal" key="bal.indet.importeCobrado.label"/>: </label></td>
				<td class="normal"><bean:write name="indetAdapterVO" property="indet.importeCobradoView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.recargo.label"/>: </label></td>
				<td class="normal"><bean:write name="indetAdapterVO" property="indet.recargoView"/></td>

				<td><label><bean:message bundle="bal" key="bal.indet.importeBasico.label"/>: </label></td>
				<td class="normal"><bean:write name="indetAdapterVO" property="indet.importeBasicoView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.codIndet.label"/>: </label></td>
				<td class="normal"><bean:write name="indetAdapterVO" property="indet.codIndetView"/></td>

				<td><label><bean:message bundle="bal" key="bal.indet.importeCalculado.label"/>: </label></td>
				<td class="normal"><bean:write name="indetAdapterVO" property="indet.importeCalculadoView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.fechaPago.label"/>: </label></td>
				<td class="normal"><bean:write name="indetAdapterVO" property="indet.fechaPagoView"/></td>
				
				<td><label><bean:message bundle="bal" key="bal.indet.tipoIngreso.label"/>: </label></td>
				<td class="normal"><bean:write name="indetAdapterVO" property="indet.tipoIngresoView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.caja.label"/>: </label></td>
				<td class="normal"><bean:write name="indetAdapterVO" property="indet.cajaView"/></td>

				<td><label><bean:message bundle="bal" key="bal.indet.paquete.label"/>: </label></td>
				<td class="normal"><bean:write name="indetAdapterVO" property="indet.paqueteView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.codPago.label"/>: </label></td>
				<td class="normal"><bean:write name="indetAdapterVO" property="indet.codPagoView"/></td>
			
				<td><label><bean:message bundle="bal" key="bal.indet.fechaBalance.label"/>: </label></td>
				<td class="normal"><bean:write name="indetAdapterVO" property="indet.fechaBalanceView"/></td>		
			</tr>
			<tr>			
				<td><label><bean:message bundle="bal" key="bal.indet.reciboTr.label"/>: </label></td>
				<td class="normal"><bean:write name="indetAdapterVO" property="indet.reciboTrView"/></td>
				
				<td><label><bean:message bundle="bal" key="bal.indet.usuario.label"/>: </label></td>
				<td class="normal"><bean:write name="indetAdapterVO" property="indet.usuario"/></td>
			</tr>
			</table>
		</fieldset>
	
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="indetAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="indetAdapterVO" property="act" value="reingresar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reingresar', '');">
							<bean:message bundle="bal" key="bal.indetSearchPage.adm.button.reingresar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="indetAdapterVO" property="act" value="desgloce">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desgloce', '');">
							<bean:message bundle="bal" key="bal.indetSearchPage.adm.button.desgloce"/>
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
		
		