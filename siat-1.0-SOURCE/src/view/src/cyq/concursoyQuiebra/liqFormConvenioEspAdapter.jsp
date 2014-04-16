<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/cyq/AdministrarLiqFormConvenioEsp.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="cyq" key="cyq.liqFormConvenioEspAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>Permite realizar la formalización de un Plan Especial de Pago para un Contribuyente, a partir de los Registros de Deuda seleccionados.</p>
				<p>Carga de los Parámetros del Plan Especial.</p>
			</td>				
			<td align="right">
	 			<button type="button" name="btnVolver" class="boton" onclick="submitForm('volver', '');">
			  	    <bean:message bundle="base" key="abm.button.volver"/>
				</button>
			</td>
		</tr>
	</table>


	<!-- Procedimiento -->
		<bean:define id="procedimientoVO" name="liqFormConvenioAdapterVO" property="procedimiento"/>
		<%@ include file="/cyq/concursoyQuiebra/includeEncProcedimiento.jsp"%>
	<!-- Procedimiento -->
		
	<br>
		
	<!-- Lista de Deuda -->	
	<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="cyq" key="cyq.liqFormConvenioAdapter.deudaVerifHomo.label"/></caption>
	      	<tbody>
		       	<tr>
				  	<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
				  	<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
				  	<th align="left"><bean:message bundle="cyq" key="cyq.tipoPrivilegioEspecial.label"/></th>
				  	<th align="left"><bean:message bundle="cyq" key="cyq.tipoPrivilegioGeneral.label"/></th>
				  	<th align="left"><bean:message bundle="cyq" key="cyq.tipoPrivilegioQuirografario.label"/></th>
				  	<th align="left"><bean:message bundle="cyq" key="cyq.liqFormConvenioAdapter.total.label"/></th>
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqDeudaCyq" name="liqFormConvenioAdapterVO" property="listDeuda">
					<tr>
			  			<!-- Especial -->			  			
			  			<td><bean:write name="LiqDeudaCyq" property="desRecurso"/> </td>
			  			<td><bean:write name="LiqDeudaCyq" property="numeroCuenta"/> </td>
			  			<logic:equal name="LiqDeudaCyq" property="idTipoPrivilegio" value="1">
							<td><b><bean:write name="LiqDeudaCyq" property="importe" bundle="base" formatKey="general.format.currency"/></b></td>
			  				<td>&nbsp;</td><td>&nbsp;</td>
			  			</logic:equal>
			  			<!-- General -->
						<logic:equal name="LiqDeudaCyq" property="idTipoPrivilegio" value="2">
							<td>&nbsp;</td>
							<td><b><bean:write name="LiqDeudaCyq" property="importe" bundle="base" formatKey="general.format.currency"/></b></td>
			  				<td>&nbsp;</td>
			  			</logic:equal>
			  			<!-- Quiro -->
			  			<logic:equal name="LiqDeudaCyq" property="idTipoPrivilegio" value="3">
			  				<td>&nbsp;</td><td>&nbsp;</td>
							<td><b><bean:write name="LiqDeudaCyq" property="importe" bundle="base" formatKey="general.format.currency"/></b></td>
			  			</logic:equal>
						<!-- Total -->
						<td><b><bean:write name="LiqDeudaCyq" property="importe" bundle="base" formatKey="general.format.currency"/></b></td>
			  			
			       	</tr>
				</logic:iterate>
				<!-- Fin Item LiqDeudaVO -->
		       	<tr>       		
		       		<td class="celdatotales" align="right" colspan="2">
			        	<bean:message bundle="cyq" key="cyq.liqDeudaCyqAdapter.totales.label"/>: 
			        </td>
			        <td class="celdatotales">
			        	<b><bean:write name="liqFormConvenioAdapterVO" property="totalGeneral" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
			        <td class="celdatotales">
			        	<b><bean:write name="liqFormConvenioAdapterVO" property="totalEspecial" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
			        <td class="celdatotales">
			        	<b><bean:write name="liqFormConvenioAdapterVO" property="totalQuirografario" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
			        <td class="celdatotales">
			        	<b><bean:write name="liqFormConvenioAdapterVO" property="total" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>
	      	</tbody>
		</table>
	</div>	
	<!-- Fin Lista de Deuda -->
	
	<!-- Ingreso de parámetros -->
		<fieldset>
		<legend><bean:message bundle="cyq" key="cyq.liqFormConvenioEspAdapter.parametros.title"/></legend>
		<table border="0" width="100%" align="left">
			<tr>&nbsp;</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.liqFormConvenioEspAdapter.parametros.fechaForm.legend"/></label></td>
				<td class="normal">
					<html:text name="liqFormConvenioAdapterVO" property="fechaFormalizacionView" styleId="fechaFormalizacionView" size="15" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaFormalizacionView');" id="a_fechaFormalizacionView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.liqFormConvenioEspAdapter.parametros.cantMaxCuo.legend"/></label></td>
				<td class="normal"><html:text name="liqFormConvenioAdapterVO" property="cantMaxCuoView" size="15"/></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.liqFormConvenioEspAdapter.parametros.descCapital.legend"/></label></td>
				<td class="normal"><html:text name="liqFormConvenioAdapterVO" property="descCapitalView" size="15"/></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.liqFormConvenioEspAdapter.parametros.coeficiente.legend"/></label></td>
				<td class="normal"><html:text name="liqFormConvenioAdapterVO" property="interesView" size="15"/></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.liqFormConvenioEspAdapter.parametros.venPrimeraCuota.legend"/></label></td>
				<td class="normal">
					<html:text name="liqFormConvenioAdapterVO" property="venPrimeraCuotaView" styleId="venPrimeraCuotaView" styleClass="datos" size="15" />
				<a class="link_siat" onclick="return show_calendar('venPrimeraCuotaView');" id="a_venPrimeraCuotaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>	
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.liqFormConvenioEspAdapter.parametros.importeAnticipo.legend"/></label></td>
				<td class="normal"><html:text name="liqFormConvenioAdapterVO" property="importeAnticipoView" size="15"/></td>
			</tr>
			
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td colspan="3" align="right">
					<p>(*) Campos obligatorios</p>
					<p><bean:message bundle="cyq" key="cyq.liqFormConvenioEspAdapter.formatoPorcentajes"/></p>
				</td>
			</tr>
		</table>
		</fieldset>	
		
	<!--  boton Seleccionar Plan -->
		<p align="right">
			<button type="button" name="btnFormConvenioar" class="boton" onclick="submitForm('seleccionarPlan', '');">
		  	    <bean:message bundle="cyq" key="cyq.liqFormConvenioEspAdapter.button.seleccionarPlan"/>
			</button>
		</p>
	<!--  FIN boton Seleccionar Plan -->
	
	<!-- Volver -->
	<button type="button" name="btnVolver" class="boton" onclick="submitForm('volver', '');">
  	    <bean:message bundle="base" key="abm.button.volver"/>
	</button>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin formulario -->