<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

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
		
		<!-- Saldo a Generar -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.dupliceAdapter.generarSaldo.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal">
					<html:select name="dupliceAdapterVO" property="recurso.id" styleClass="select" >
						<bean:define id="includeRecursoList" name="dupliceAdapterVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="dupliceAdapterVO" property="recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
		   		 </tr>
				<tr>
				<!-- Cuenta -->		
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
				    <td class="normal" colspan="4">
							<html:text name="dupliceAdapterVO" property="cuenta.numeroCuenta" size="20"/>
							<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
								<bean:message bundle="bal" key="bal.duplice.button.buscarCuenta"/>
							</html:button>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.saldoAFavor.importe.ref"/>: </label></td>
					<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.importeCobradoView"/></td>
				</tr>
				<!-- Descripcion -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.saldoAFavor.descripcion.ref"/>: </label></td>
					<td class="normal"><html:text name="dupliceAdapterVO" property="saldoAFavor.descripcion" size="20" maxlength="100"/></td>			
				</tr>
			</table>
		</fieldset>
		<!-- Fin Buscar Cuenta -->
			
		<!-- Duplice -->
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
				<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('genSaldoAFavor', '');">
					<bean:message bundle="bal" key="bal.dupliceAdapter.adm.button.generarSaldoAFavor"/>
				</html:button>
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
		