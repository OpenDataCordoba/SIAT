<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>


	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarDuplice.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.dupliceAdapter.title"/></h1>		
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
			<legend><bean:message bundle="bal" key="bal.duplice.title"/></legend>			
			<table class="tabladatos" width="100%">
				<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.sistema.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.sistema"/></td>

				<td><label><bean:message bundle="bal" key="bal.indet.nroComprobante.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.nroComprobante"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.clave.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.clave"/></td>
			
				<td><label><bean:message bundle="bal" key="bal.indet.partida.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.partida"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.resto.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.resto"/></td>
			
				<td><label><bean:message bundle="bal" key="bal.indet.importeCobrado.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.importeCobradoView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.recargo.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.recargoView"/></td>

				<td><label><bean:message bundle="bal" key="bal.indet.importeBasico.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.importeBasicoView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.codIndet.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.codIndetView"/></td>

				<td><label><bean:message bundle="bal" key="bal.indet.importeCalculado.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.importeCalculadoView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.fechaPago.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.fechaPagoView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.caja.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.cajaView"/></td>

				<td><label><bean:message bundle="bal" key="bal.indet.paquete.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.paqueteView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.codPago.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.codPagoView"/></td>
			
				<td><label><bean:message bundle="bal" key="bal.indet.fechaBalance.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.fechaBalanceView"/></td>		
			</tr>
			<tr>			
				<td><label><bean:message bundle="bal" key="bal.indet.reciboTr.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.reciboTrView"/></td>
				
				<td><label><bean:message bundle="bal" key="bal.indet.usuario.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.usuario"/></td>				
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
				<td align="right">
	   	    		<logic:equal name="dupliceAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
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
		