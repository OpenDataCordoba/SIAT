<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarRecibo.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.reciboAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	<!-- LiqCuenta -->
		<bean:define id="DeudaAdapterVO" name="reciboAdapterVO"/>
		<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp" %>
	<!-- LiqCuenta -->
	
	
	<!-- CuentasRel -->
	<logic:notEmpty name="reciboAdapterVO" property="listCuentaRel" >
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuentasRelacionadas.title"/> </legend>			
			<ul>
				<logic:iterate id="CuentaRel" name="reciboAdapterVO" property="listCuentaRel">
					<li>
						<!-- Permitido ver Cuentas Relacionadas al Objeto Imponible -->
						<logic:equal name="reciboAdapterVO" property="verCuentaRelEnabled" value="true">
				      		<a href="/siat/gde/AdministrarLiqDeuda.do?method=verCuentaRel&selectedId=<bean:write name="CuentaRel" property="idCuenta" bundle="base" formatKey="general.format.id"/>" >
					      		<bean:write name="CuentaRel" property="nroCuenta"/> -
					      		<bean:write name="CuentaRel" property="desCategoria"/> -
					      		<bean:write name="CuentaRel" property="desRecurso"/>
				      		</a>
				      	</logic:equal>
				      	<logic:notEqual name="reciboAdapterVO" property="verCuentaRelEnabled" value="true">
				      		<bean:write name="CuentaRel" property="nroCuenta"/> -
					      		<bean:write name="CuentaRel" property="desCategoria"/> -
					      		<bean:write name="CuentaRel" property="desRecurso"/>
				      	</logic:notEqual>
					</li>
				</logic:iterate>
			</ul>		
		</fieldset>
	</logic:notEmpty>
	<!-- Fin CuentasRel -->
	
	<!-- Datos del Recibo -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.recibo.title"/></legend>
			<logic:iterate id="recibo" name="reciboAdapterVO" property="listRecibos">
				<!-- Numero -->
				<p>
		      		<label><bean:message bundle="gde" key="gde.recibo.nroRecibo.label"/>:</label>
		      		<bean:write name="recibo" property="numeroRecibo" bundle="base" formatKey="general.format.id"/>
				</p>
				<p>
					<label><bean:message bundle="gde" key="gde.recibo.esCuotaSaldo.label"/>:</label>
					<bean:write name="recibo" property="esCuotaSaldoView"/>
				</p>
				<p>
					<label><bean:message bundle="gde" key="gde.recibo.observacion.label"/>:</label>
					<bean:write name="recibo" property="observacion"/>
				</p>
			</logic:iterate>		
			<logic:equal name="reciboAdapterVO" property="tipoRecibo.id" value="0">
				<!-- Origen -->
			</logic:equal>				
	</fieldset>
	<!-- Datos del Recibo -->
	
	<!-- Recibos Deuda -->
	<logic:equal name="reciboAdapterVO" property="tipoRecibo.id" value="0">
		
		<!-- No es Cuota Saldo -->
		<logic:equal name="reciboAdapterVO" property="esCuotaSaldo" value="false">
		
			<logic:iterate id="recibo" name="reciboAdapterVO" property="listRecibos">
				<p><bean:write name="recibo" property="desDescuento"/></p>
				<div class="horizscroll">
			    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
			    	<caption>
			    		<bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.vencimiento"/>&nbsp;
			    		<bean:write name="recibo" property="fechaVtoView"/>&nbsp; - &nbsp;
			    		<bean:message bundle="base" key="base.estado.label"/>: 
			    		<bean:write name="recibo" property="desEstado"/>
			    	</caption>
			      	<tbody>
				       	<tr>
						  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/></th>
						  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.saldo"/></th>
						  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
						  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>
						  	<logic:equal name="recibo" property="estaPago" value="true">
						  		<th align="left"><bean:message bundle="gde" key="gde.estadoCuenta.pago.tipoPago.label"/></th>
						  	</logic:equal>
						</tr>
						
						<!-- Item LiqReciboDeudaVO -->
						<logic:iterate id="reciboDeuda" name="recibo" property="listReciboDeuda">
							<tr>
					  			<td><bean:write name="reciboDeuda" property="periodoDeuda"/>&nbsp;</td>
						        <td><bean:write name="reciboDeuda" property="totCapital" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="reciboDeuda" property="totActualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="reciboDeuda" property="totalReciboDeudaCalculado" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <logic:equal name="recibo" property="estaPago" value="true">
						        	<td><bean:write name="reciboDeuda" property="tipoPago"/>&nbsp;</td>
						        </logic:equal>
					       	</tr>
						</logic:iterate>
						<!-- Fin Item LiqReciboDeudaVO -->
						
						<!-- total recibo -->
							<tr>
								<td colspan="2">&nbsp;</td>
								<td class="celdatotales"><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.total"/>:</td>
								<td class="celdatotales" ><bean:write name="recibo" property="totCapitalOriginal" bundle="base" formatKey="general.format.currency"/></td>
							</tr>
						<!-- Fin total recibo -->
	
						<!-- Recargo recibo -->
							<tr>
								<td colspan="2">&nbsp;</td>
								<td class="celdatotales" ><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.recargo"/>:</td>
								<td class="celdatotales" ><bean:write name="recibo" property="totActualizacion" bundle="base" formatKey="general.format.currency"/></td>
							</tr>
						<!-- Fin Recargo recibo -->
						
						<!-- Sellado recibo -->
							<tr>
								<td colspan="2">&nbsp;</td>
								<td class="celdatotales" ><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.sellado"/>:</td>
								<td class="celdatotales" ><bean:write name="recibo" property="importeSellado" bundle="base" formatKey="general.format.currency"/></td>
							</tr>
						<!-- Fin Sellado recibo -->
						
						<!-- Total recibo -->
							<tr>
								<td colspan="2">&nbsp;</td>
								<td class="celdatotales" ><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.totalImporteRecibo"/>:</td>
								<td class="celdatotales" ><bean:write name="recibo" property="totalPagar" bundle="base" formatKey="general.format.currency"/></td>
							</tr>
						<!-- Fin Total recibo -->
			      	</tbody>
				</table>
				</div>
			</logic:iterate>
		</logic:equal>
		
		<!-- Es Cuota Saldo -->
		<logic:equal name="reciboAdapterVO" property="esCuotaSaldo" value="true">
			<logic:iterate id="recibo" name="reciboAdapterVO" property="listRecibos">
				<p><bean:write name="recibo" property="desDescuento"/></p>
				<div class="horizscroll">
			    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
			    	<caption>
			    		<bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.vencimiento"/>&nbsp;
			    		<bean:write name="recibo" property="fechaVtoView"/>&nbsp; - &nbsp;
			    		<bean:message bundle="base" key="base.estado.label"/>: 
			    		<bean:write name="recibo" property="desEstado"/>
			    	</caption>
			      	<tbody>
				       	<tr>
						  	<th align="left" colspan="3">Periodos Incluidos</th>
						  	<logic:equal name="recibo" property="estaPago" value="true">
						  		<th align="left"><bean:message bundle="gde" key="gde.estadoCuenta.pago.tipoPago.label"/></th>
						  	</logic:equal>
						</tr>
						
						<!-- Item LiqReciboDeudaVO -->
						<tr>
				  			<td colspan="3"><bean:write name="recibo" property="desCuotaSaldo"/>&nbsp;</td>
					        
					        <logic:equal name="recibo" property="estaPago" value="true">
					        	<td><bean:write name="reciboDeuda" property="tipoPago"/>&nbsp;</td>
					        </logic:equal>
				       	</tr>
						<!-- Fin Item LiqReciboDeudaVO -->
						
						<!-- Total recibo -->
							<tr>
								<td width="50%">&nbsp;</td>
								<td class="celdatotales" ><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.totalImporteRecibo"/>:</td>
								<td class="celdatotales" ><bean:write name="recibo" property="totalPagar" bundle="base" formatKey="general.format.currency"/></td>
							</tr>
						<!-- Fin Total recibo -->
			      	</tbody>
				</table>
				</div>
			</logic:iterate>	
		
		</logic:equal>	
	</logic:equal>
	<!-- Fin Recibos Deuda -->
	
	<!-- Recibos Cuotas -->
	<logic:equal name="reciboAdapterVO" property="tipoRecibo.id" value="1">
	
		<!-- LiqConvenio -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.title"/> </legend>
				
				<!-- Nro Convenio -->
				<p>
		      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.nroConvenio.label"/>:</label>
		      		<bean:write name="reciboAdapterVO" property="convenio.nroConvenio"/>
				</p>
				
				<!-- Plan de Pago -->
				<p>
		      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.planPago.label"/>:</label>
		      		<bean:write name="reciboAdapterVO" property="convenio.desPlan"/>
		      	</p>
		      	<logic:equal name="reciboAdapterVO" property="convenio.poseeCaso" value="true" >
		      		<p>	
						<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.caso.label"/>:</label>
						<bean:write name="reciboAdapterVO" property="convenio.caso.sistemaOrigen.desSistemaOrigen"/>
						&nbsp;							
						<bean:write name="reciboAdapterVO" property="convenio.caso.numero"/>	
					</p>
		      	</logic:equal>
				<!-- Via Deuda -->
				<p>
		      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.viaDeuda.label"/>:</label>
		      		<bean:write name="reciboAdapterVO" property="convenio.desViaDeuda"/>
				</p>
				<logic:equal name="reciboAdapterVO" property="convenio.poseeProcurador" value="true" >
					<p>	
						<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.procurador.label"/>:</label>
			      		<bean:write name="reciboAdapterVO" property="convenio.procurador.descripcion"/>
					</p>
		      	</logic:equal>
				
				<p>
				<!-- Estado Convenio -->
		      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.estadoConvenio.label"/>:</label>
		      		<bean:write name="reciboAdapterVO" property="convenio.desEstadoConvenio"/>
				</p>
				<!-- Cantidad Cuotas -->
				<p>
		      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.cantCuotas.label"/>:</label>
		      		<bean:write name="reciboAdapterVO" property="convenio.canCuotasPlan"/>
				</p>
				<!-- Total Conveniado -->
				<p>
		      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.totalImporteConvenido.label"/>:</label>
		      		<bean:write name="reciboAdapterVO" property="convenio.totImporteConvenio" bundle="base" formatKey="general.format.currency"/>
				</p>
					
		</fieldset>
		<!-- LiqConvenio -->
	
	
		<logic:iterate id="reciboCuo" name="reciboAdapterVO" property="listRecibos">
			<p><bean:write name="reciboCuo" property="desDescuento"/></p>
			<div class="horizscroll">
		    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
		    	<caption>
		    		<bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.vencimiento"/>&nbsp;
		    		<bean:write name="reciboCuo" property="fechaVtoView"/>&nbsp; - &nbsp;
		    		<bean:message bundle="base" key="base.estado.label"/>: 
		    		<bean:write name="reciboCuo" property="desEstado"/>	
		    	</caption>
		      	<tbody>
			       	<tr>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.nroCuota.label"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.capital.label"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.interes.label"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.total.label"/></th>
				  	<logic:equal name="reciboCuo" property="estaPago" value="true">
					  	<th align="left"><bean:message bundle="gde" key="gde.estadoCuenta.pago.tipoPago.label"/></th>
					</logic:equal>
					</tr>
					
					<!-- Item LiqReciboDeudaVO -->
					<logic:iterate id="reciboCuota" name="reciboCuo" property="listCuota">
						<tr>
				  			<td><bean:write name="reciboCuota" property="nroCuota"/>&nbsp;</td>
					        <td><bean:write name="reciboCuota" property="capital" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="reciboCuota" property="interes" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="reciboCuota" property="recargo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="reciboCuota" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <logic:equal name="reciboCuo" property="estaPago" value="true">
					        	<td><bean:write name="reciboCuota" property="tipoPago"/>&nbsp;</td>
					        </logic:equal>
				       	</tr>
					</logic:iterate>
					<!-- Fin Item LiqReciboDeudaVO -->
					
					<!-- total recibo -->
						<tr>
							<td colspan="3">&nbsp;</td>
							<td class="celdatotales"><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.total"/>:</td>
							<td class="celdatotales" ><bean:write name="reciboCuo" property="totCapitalOriginal" bundle="base" formatKey="general.format.currency"/></td>
						</tr>
					<!-- Fin total recibo -->
					
					<!-- total interes -->
						<tr>
							<td colspan="3">&nbsp;</td>
							<td class="celdatotales"><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.interes"/>:</td>
							<td class="celdatotales" ><bean:write name="reciboCuo" property="totInteres" bundle="base" formatKey="general.format.currency"/></td>
						</tr>
					<!-- Fin total interes -->
					

					<!-- Recargo recibo -->
						<tr>
							<td colspan="3">&nbsp;</td>
							<td class="celdatotales" ><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.recargo"/>:</td>
							<td class="celdatotales" ><bean:write name="reciboCuo" property="totActualizacion" bundle="base" formatKey="general.format.currency"/></td>
						</tr>
					<!-- Fin Recargo recibo -->
					
					<!-- Sellado recibo -->
						<tr>
							<td colspan="3">&nbsp;</td>
							<td class="celdatotales" ><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.sellado"/>:</td>
							<td class="celdatotales" ><bean:write name="reciboCuo" property="importeSellado" bundle="base" formatKey="general.format.currency"/></td>
						</tr>
					<!-- Fin Sellado recibo -->
					
					<!-- Total recibo -->
						<tr>
							<td colspan="3">&nbsp;</td>
							<td class="celdatotales" ><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.totalImporteRecibo"/>:</td>
							<td class="celdatotales" ><bean:write name="reciboCuo" property="totalPagar" bundle="base" formatKey="general.format.currency"/></td>
						</tr>
					<!-- Fin Total recibo -->
		      	</tbody>
			</table>
			</div>
		</logic:iterate>	
	</logic:equal>
	<!-- Fin Recibos Cuotas -->
	
	<!-- Volver -->
	<button type="button" name="btnVolver" class="boton" onclick="submitForm('volver', '');">
  	    <bean:message bundle="base" key="abm.button.volver"/>
	</button>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->