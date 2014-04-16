<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarIndetReing.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.indetReingAdapter.title"/></h1>		
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
			<legend><bean:message bundle="bal" key="bal.reingreso.title"/></legend>			
			<table class="tabladatos" width="100%">
				<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.sistema.label"/>: </label></td>
				<td class="normal"><bean:write name="indetReingAdapterVO" property="indetReing.sistema"/></td>

				<td><label><bean:message bundle="bal" key="bal.indet.nroComprobante.label"/>: </label></td>
				<td class="normal"><bean:write name="indetReingAdapterVO" property="indetReing.nroComprobante"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.clave.label"/>: </label></td>
				<td class="normal"><bean:write name="indetReingAdapterVO" property="indetReing.clave"/></td>
			
				<td><label><bean:message bundle="bal" key="bal.indet.partida.label"/>: </label></td>
				<td class="normal"><bean:write name="indetReingAdapterVO" property="indetReing.partida"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.resto.label"/>: </label></td>
				<td class="normal"><bean:write name="indetReingAdapterVO" property="indetReing.resto"/></td>
			
				<td><label><bean:message bundle="bal" key="bal.indet.importeCobrado.label"/>: </label></td>
				<td class="normal"><bean:write name="indetReingAdapterVO" property="indetReing.importeCobradoView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.recargo.label"/>: </label></td>
				<td class="normal"><bean:write name="indetReingAdapterVO" property="indetReing.recargoView"/></td>

				<td><label><bean:message bundle="bal" key="bal.indet.importeBasico.label"/>: </label></td>
				<td class="normal"><bean:write name="indetReingAdapterVO" property="indetReing.importeBasicoView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.codIndet.label"/>: </label></td>
				<td class="normal"><bean:write name="indetReingAdapterVO" property="indetReing.codIndetView"/></td>

				<td><label><bean:message bundle="bal" key="bal.indet.importeCalculado.label"/>: </label></td>
				<td class="normal"><bean:write name="indetReingAdapterVO" property="indetReing.importeCalculadoView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.fechaPago.label"/>: </label></td>
				<td class="normal"><bean:write name="indetReingAdapterVO" property="indetReing.fechaPagoView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.caja.label"/>: </label></td>
				<td class="normal"><bean:write name="indetReingAdapterVO" property="indetReing.cajaView"/></td>

				<td><label><bean:message bundle="bal" key="bal.indet.paquete.label"/>: </label></td>
				<td class="normal"><bean:write name="indetReingAdapterVO" property="indetReing.paqueteView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.codPago.label"/>: </label></td>
				<td class="normal"><bean:write name="indetReingAdapterVO" property="indetReing.codPagoView"/></td>
			
				<td><label><bean:message bundle="bal" key="bal.indet.fechaBalance.label"/>: </label></td>
				<td class="normal"><bean:write name="indetReingAdapterVO" property="indetReing.fechaBalanceView"/></td>		
			</tr>
			<tr>			
				<td><label><bean:message bundle="bal" key="bal.indet.reciboTr.label"/>: </label></td>
				<td class="normal"><bean:write name="indetReingAdapterVO" property="indetReing.reciboTrView"/></td>
				
				<td><label><bean:message bundle="bal" key="bal.indet.usuario.label"/>: </label></td>
				<td class="normal"><bean:write name="indetReingAdapterVO" property="indetReing.usuario"/></td>
			</tr>
			</table>
		</fieldset>
	
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.reingreso.original.title"/></legend>			
			<logic:equal name="indetReingAdapterVO" property="paramOriginal" value="true">	
				<table class="tabladatos" width="100%">
				<tr>
					<td><label><bean:message bundle="bal" key="bal.indet.sistema.label"/>: </label></td>
					<td class="normal"><bean:write name="indetReingAdapterVO" property="original.sistema"/></td>
	
					<td><label><bean:message bundle="bal" key="bal.indet.nroComprobante.label"/>: </label></td>
					<td class="normal"><bean:write name="indetReingAdapterVO" property="original.nroComprobante"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.indet.clave.label"/>: </label></td>
					<td class="normal"><bean:write name="indetReingAdapterVO" property="original.clave"/></td>
				
					<td><label><bean:message bundle="bal" key="bal.indet.partida.label"/>: </label></td>
					<td class="normal"><bean:write name="indetReingAdapterVO" property="original.partida"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.indet.resto.label"/>: </label></td>
					<td class="normal"><bean:write name="indetReingAdapterVO" property="original.resto"/></td>
				
					<td><label><bean:message bundle="bal" key="bal.indet.importeCobrado.label"/>: </label></td>
					<td class="normal"><bean:write name="indetReingAdapterVO" property="original.importeCobradoView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.indet.recargo.label"/>: </label></td>
					<td class="normal"><bean:write name="indetReingAdapterVO" property="original.recargoView"/></td>
	
					<td><label><bean:message bundle="bal" key="bal.indet.importeBasico.label"/>: </label></td>
					<td class="normal"><bean:write name="indetReingAdapterVO" property="original.importeBasicoView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.indet.codIndet.label"/>: </label></td>
					<td class="normal"><bean:write name="indetReingAdapterVO" property="original.codIndetView"/></td>
	
					<td><label><bean:message bundle="bal" key="bal.indet.importeCalculado.label"/>: </label></td>
					<td class="normal"><bean:write name="indetReingAdapterVO" property="original.importeCalculadoView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.indet.fechaPago.label"/>: </label></td>
					<td class="normal"><bean:write name="indetReingAdapterVO" property="original.fechaPagoView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.indet.caja.label"/>: </label></td>
					<td class="normal"><bean:write name="indetReingAdapterVO" property="original.cajaView"/></td>
	
					<td><label><bean:message bundle="bal" key="bal.indet.paquete.label"/>: </label></td>
					<td class="normal"><bean:write name="indetReingAdapterVO" property="original.paqueteView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.indet.codPago.label"/>: </label></td>
					<td class="normal"><bean:write name="indetReingAdapterVO" property="original.codPagoView"/></td>
				
					<td><label><bean:message bundle="bal" key="bal.indet.fechaBalance.label"/>: </label></td>
					<td class="normal"><bean:write name="indetReingAdapterVO" property="original.fechaBalanceView"/></td>		
				</tr>
				<tr>			
					<td><label><bean:message bundle="bal" key="bal.indet.reciboTr.label"/>: </label></td>
					<td class="normal"><bean:write name="indetReingAdapterVO" property="original.reciboTrView"/></td>
					
					<td><label><bean:message bundle="bal" key="bal.indet.usuario.label"/>: </label></td>
					<td class="normal"><bean:write name="indetReingAdapterVO" property="original.usuario"/></td>
				</tr>
				</table>
			</logic:equal>
			<logic:equal name="indetReingAdapterVO" property="paramOriginal" value="false">	
				<table class="normal" border="0" cellpadding="0" cellspacing="0" width="100%">
                	<tr>
                		<td align="center">
							<bean:message bundle="bal" key="bal.indetReingAdapter.noExisteOriginal"/>
						</td>
					</tr>
				</table>
			</logic:equal>
		</fieldset>

	
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right" width="50%">
					<logic:equal name="indetReingAdapterVO" property="act" value="vueltaAtras">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('vueltaAtras', '');">
							<bean:message bundle="bal" key="bal.indetReingSearchPage.button.vueltaAtras"/>
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
		
		