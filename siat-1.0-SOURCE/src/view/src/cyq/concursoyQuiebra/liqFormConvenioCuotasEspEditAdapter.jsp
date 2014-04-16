<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/cyq/AdministrarLiqFormConvenioEsp.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="cyq" key="cyq.liqFormConvenioAdapter.title"/></h1>
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>Detalle de las Cuotas del Plan de Pago seleccionado.</p>
			</td>				
			<td align="right">
	 			<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuotas', '');">
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
	
	<p>
		(*) Deuda actualizada a la Fecha de Formalización del Plan, considerando la forma de actualizacion del Plan y 
		los descuentos definidos para el mismo.
	</p>
	
	<!-- Alternativa Cuotas del Planes  -->
	<logic:notEmpty name="liqFormConvenioAdapterVO" property="planSelected.listCuotasForm">
		<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption>Detalle de las Cuotas del Plan Seleccionado</caption>
	      	<tbody>
	      		<tr>
		       		<td colspan="6">
		       			<p>
		       				<label>Plan:</label>
		       				<bean:write name="liqFormConvenioAdapterVO" property="planSelected.desPlan"/>
		       			</p>
		       			<p>
		       				<label>V&iacute;a Deuda:</label>
		       				<bean:write name="liqFormConvenioAdapterVO" property="planSelected.desViaDeuda"/>
		       			</p>
		       		</td>
		       	</tr>
		       	<tr>
		       		<th align="left">Nro Cuota</th>
		       		<th align="left">Capital</th>
				  	<th align="left">Coef.</th>
				  	<th align="left">Intereses</th>
		       		<th align="left">Importe</th>
				  	<th align="left">Fec. Vto.</th>
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqCuotaVO" name="liqFormConvenioAdapterVO" property="planSelected.listCuotasForm">
					<tr>
						<td><bean:write name="LiqCuotaVO" property="nroCuota"/>&nbsp;</td>
						
						<td class="normal"> 
							<input type="text" size="10" maxlength="10" 
								name='capital<bean:write name="LiqCuotaVO" property="nroCuota"/>'
								id='capital<bean:write name="LiqCuotaVO" property="nroCuota"/>'
								value='<bean:write name="LiqCuotaVO" property="capitalView" />' 
							/>
						</td>
						<td class="normal"> 
							<input type="text" size="10" maxlength="10" 
								name='interes<bean:write name="LiqCuotaVO" property="nroCuota"/>'
								id='interes<bean:write name="LiqCuotaVO" property="nroCuota"/>'
								value='<bean:write name="LiqCuotaVO" property="interesView" />' 
							/>
						</td>
						
						<td class="normal"> 
							<bean:write name="LiqCuotaVO" property="actualizacion" bundle="base" formatKey="general.format.currency" />
						</td>
						<td class="normal"> 
							<bean:write name="LiqCuotaVO" property="importeCuota" bundle="base" formatKey="general.format.currency" />
						</td>
						
						<td class="normal"> 
							<input type="text" size="10" maxlength="10" 
								name='fechaVto<bean:write name="LiqCuotaVO" property="nroCuota"/>'
								id='fechaVto<bean:write name="LiqCuotaVO" property="nroCuota"/>'
								value='<bean:write name="LiqCuotaVO" property="fechaVto"/>' 
							/>
							
							<a class="link_siat" onclick="return show_calendar('fechaVto<bean:write name="LiqCuotaVO" property="nroCuota"/>');" 
								id="a_fechaVto<bean:write name="LiqCuotaVO" property="nroCuota"/>">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
						
					</tr>
				</logic:iterate>
				
				<tr>
					<td align="right"> <b>Total:</b> </td>
					<td><bean:write name="liqFormConvenioAdapterVO" property="planSelected.totalCapital" bundle="base" formatKey="general.format.currency"/></td>
					<td>&nbsp;</td>
					<td><bean:write name="liqFormConvenioAdapterVO" property="planSelected.totalInteres" bundle="base" formatKey="general.format.currency"/></td>
					<td><bean:write name="liqFormConvenioAdapterVO" property="planSelected.totalImporte" bundle="base" formatKey="general.format.currency"/></td>
					<td>&nbsp;</td>				
				</tr>
			</tbody>
		</table>
		<logic:equal name="liqFormConvenioAdapterVO" property="tieneSellado" value="true">
				<p>(*)El importe de la cuota incluye $ <bean:write name="liqFormConvenioAdapterVO" property="importeSelladoView"/> referente a sellado de formalizaci&oacute;n del convenio</p>
		</logic:equal>
		</div>
	</logic:notEmpty>
	<!-- Fin Alternativa Cuotas del Planes -->
	
	
	<!--  boton Seleccionar Plan -->
		<p align="center">

			<button type="button" name="btnModificarCuotas" class="boton" onclick="submitForm('validarCuotas', '');">
		  	    <bean:message bundle="cyq" key="cyq.liqFormConvenioAdapter.button.validarCuotas"/>
			</button>

		</p>
	<!--  FIN boton Seleccionar Plan -->
	
	<!-- Volver -->
	<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuotas', '');">
  	    <bean:message bundle="base" key="abm.button.volver"/>
	</button>
	
	<input type="hidden" name="idPlan" value="<bean:write name="liqFormConvenioAdapterVO" property="planSelected.idPlan" bundle="base" formatKey="general.format.id"/>"/>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin formulario -->