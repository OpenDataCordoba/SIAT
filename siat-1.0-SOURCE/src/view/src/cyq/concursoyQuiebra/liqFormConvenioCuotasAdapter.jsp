<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/cyq/AdministrarLiqFormConvenio.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	
	<h1><bean:message bundle="cyq" key="cyq.liqFormConvenioAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>Selección de la cantidad de cuotas del Plan de Pago.</p>
			</td>				
			<td align="right">
	 			<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverAPlanes', '');">
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
		sin considerar los descuentos del mismo.
	</p>
	
	<!-- Alternativa Cuotas del Planes  -->
	<logic:notEmpty name="liqFormConvenioAdapterVO" property="planSelected.listAltCuotas">
		<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption> Alternativas de Cuotas del Plan</caption>
	      	<tbody>
	      		<tr>
		       		<td colspan="6">
		       			<b>Plan:&nbsp;&nbsp;</b> 
		       			<bean:write name="liqFormConvenioAdapterVO" property="planSelected.desPlan"/>
		       		</td>
		       	</tr>
		       	<tr>
		       		<th align="left">&nbsp;</th>
		       		<th align="left">&nbsp;</th>
				  	<th align="left">Cant. Cuotas</th>
		       		<th align="left">Anticipo</th>
				  	<th align="left">Restantes</th>
				  	<th align="left">Importe Total Convenio</th>
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqCuotaVO" name="liqFormConvenioAdapterVO" property="planSelected.listAltCuotas">
					<tr>
						<logic:equal name="LiqCuotaVO" property="esSeleccionable" value="true">
							<td>
								<input type="radio" name="nroCuota" value="<bean:write name="LiqCuotaVO" property="nroCuota"/>"/>
							</td>
							<td>
								<a style="cursor: pointer;" onclick="submitForm('verSimulacionCuotas', '<bean:write name="LiqCuotaVO" property="nroCuota"/>');">
									<img title="Ver Detalle del Plan" src="<%=request.getContextPath()%>/images/iconos/selec0.gif" border="0"/>
				        		</a>
				        	</td>
							<td><bean:write name="LiqCuotaVO" property="nroCuota"/>&nbsp;</td>
							<td><bean:write name="LiqCuotaVO" property="anticipo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
							<td><bean:write name="LiqCuotaVO" property="valorCuotasRestantes" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
							<td><bean:write name="LiqCuotaVO" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						</logic:equal>
			        	
			        	<logic:notEqual name="LiqCuotaVO" property="esSeleccionable" value="true">
							<td>
								<input type="radio" name="nroCuota" disabled="disabled"/>
							</td>
							
							<td>&nbsp;</td>
							
							<td><bean:write name="LiqCuotaVO" property="nroCuota"/>&nbsp;</td>
							
				        	<td colspan="3">
				        		<bean:write name="LiqCuotaVO" property="msgErrorCuota"/>
				        	</td>
						</logic:notEqual>			        	
					</tr>
				</logic:iterate>	
			</tbody>
		</table>
		</div>
	</logic:notEmpty>
	<!-- Fin Alternativa Cuotas del Planes -->
	
	<!--  boton Seleccionar Plan -->
		<p align="center">
			<button type="button" name="btnImprimir" class="boton" onclick="submitForm('printAltCuotas', '');">
		  	    <bean:message bundle="cyq" key="cyq.liqFormConvenioAdapter.button.imprimirAltCuotas"/>
			</button>

			<!-- Anomino -->
			<logic:notEqual name="userSession" property="esAnonimo" value="true">
				<button type="button" name="btnFormConvenioar" class="boton" onclick="submitForm('formalizarPlan', '');">		  	    
			  	    <bean:message bundle="cyq" key="cyq.liqFormConvenioAdapter.button.formalizarPlan"/>
				</button>
			</logic:notEqual>
		</p>
	<!--  FIN boton Seleccionar Plan -->
			
	<!-- Volver -->
	<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverAPlanes', '');">
  	    <bean:message bundle="base" key="abm.button.volver"/>
	</button>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->