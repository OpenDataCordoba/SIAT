<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarReingreso.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.reingresoAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<!-- Datos del Balance -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.balance.title"/></legend>			
				<table class="tabladatos" width="100%">
				<!-- Fecha Balance -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.balance.fechaBalance.label"/>: </label></td>
					<td class="normal"><bean:write name="reingresoAdapterVO" property="balance.fechaBalanceView"/></td>
				</tr>		
				<!-- Ejercicio -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.ejercicio.label"/>: </label></td>
					<td class="normal"><bean:write name="reingresoAdapterVO" property="balance.ejercicio.desEjercicio"/></td>
				</tr>	
				<!-- Fecha Desde y Hasta -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.balance.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="reingresoAdapterVO" property="balance.fechaDesdeView"/></td>
					<td><label><bean:message bundle="bal" key="bal.balance.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="reingresoAdapterVO" property="balance.fechaHastaView"/></td>
				</tr>			
				<!-- Observacion-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.balance.observacion.label"/>: </label></td>
					<td colspan="3" class="normal"><bean:write name="reingresoAdapterVO" property="balance.observacion"/></td>					
				</tr>
				<!-- Estado Corrida-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.balance.estadoProceso.label"/>: </label></td>
					<td class="normal"><bean:write name="reingresoAdapterVO" property="balance.corrida.estadoCorrida.desEstadoCorrida"/></td>					
				</tr>
			</table>
		</fieldset>
		
		<!-- Lista de Reingresos -->		
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.reingreso.label"/></caption>
	       	<tbody>
		               	<tr>
							<th align="left"><bean:message bundle="bal" key="bal.indet.sistema.abr"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.indet.nroComprobante.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.indet.clave.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.indet.resto.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.indet.importeCobrado.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.indet.recargo.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.indet.codIndet.abr"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.indet.fechaPago.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.indet.fechaBalance.label"/></th>
						</tr>
						<logic:notEmpty  name="reingresoAdapterVO" property="listIndet">
						<logic:iterate id="IndetVO" name="reingresoAdapterVO" property="listIndet">
							<tr>
								<td><bean:write name="IndetVO" property="sistema"/>&nbsp;</td>
								<td><bean:write name="IndetVO" property="nroComprobante"/>&nbsp;</td>
								<td><bean:write name="IndetVO" property="clave"/>&nbsp;</td>
								<td><bean:write name="IndetVO" property="resto"/>&nbsp;</td>
								<td><bean:write name="IndetVO" property="importeCobradoView"/>&nbsp;</td>
								<td><bean:write name="IndetVO" property="recargoView"/>&nbsp;</td>
								<td><bean:write name="IndetVO" property="codIndetView"/>&nbsp;</td>
								<td><bean:write name="IndetVO" property="fechaPagoView"/>&nbsp;</td>							
								<td><bean:write name="IndetVO" property="fechaBalanceView" />&nbsp;</td>
							</tr>
						</logic:iterate>
						</logic:notEmpty>	
						<logic:empty  name="reingresoAdapterVO" property="listIndet">
							<tr><td colspan="11" align="center">
								<bean:message bundle="base" key="base.noExistenRegitros"/>
							</td></tr>
						</logic:empty>	
						<tr>
					</tr>
			</td>
			</tbody>
			</table>
			</div>
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
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->		