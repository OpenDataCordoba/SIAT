<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqReconfeccion.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>	
	
	<logic:equal name="liqReconfeccionAdapterVO" property="esCuotaSaldo" value="false">
		<h1><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.title"/></h1>
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p>
					
						<%=ar.gov.rosario.siat.fra.iface.util.Frase.getInstance().getMessageBykey("gde.reimpresion.leyendaEncabezado")%>
									
					</p>
				</td>				
				<td align="right">
		 			<logic:notEqual name="userSession" property="isAnonimoView" value="1">
						<!-- Volver -->
						<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '');">
					  	    <bean:message bundle="base" key="abm.button.volver"/>
						</button>
					</logic:notEqual>
				</td>
			</tr>
		</table>
	</logic:equal>
	
	<logic:notEqual name="liqReconfeccionAdapterVO" property="esCuotaSaldo" value="false">
		<h1><bean:message bundle="gde" key="gde.liqConvenioCuotaSaldo.title"/></h1>
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p>Para imprimir el recibo de Cuota Saldo que aparece en esta pantalla, debe hacer click en el botón "Imprimir Recibo". 
						Recuerde que para realizar la impresión debe tener instalado Acrobat Reader. 
					</p>
				</td>				
				<td align="right">
		 			<logic:notEqual name="userSession" property="isAnonimoView" value="1">
						<!-- Volver -->
						<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '');">
					  	    <bean:message bundle="base" key="abm.button.volver"/>
						</button>
					</logic:notEqual>
				</td>
			</tr>
		</table>
		
	</logic:notEqual>
	
	
	<!-- Procedimiento -->
	<logic:equal name="liqReconfeccionAdapterVO" property="cuenta" value="">
		<bean:define id="procedimientoVO" name="liqReconfeccionAdapterVO" property="procedimiento"/>
		<%@ include file="/cyq/concursoyQuiebra/includeEncProcedimiento.jsp"%>
	<!-- Procedimiento -->
	</logic:equal>
	
	<logic:notEqual name="liqReconfeccionAdapterVO" property="cuenta" value="">
		<!-- LiqCuenta -->
			<bean:define id="DeudaAdapterVO" name="liqReconfeccionAdapterVO"/>
			<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp" %>
		<!-- LiqCuenta -->
		
		<!-- CuentasRel -->
		<logic:notEmpty name="liqReconfeccionAdapterVO" property="listCuentaRel" >
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuentasRelacionadas.title"/> </legend>			
				<ul>
					<logic:iterate id="CuentaRel" name="liqReconfeccionAdapterVO" property="listCuentaRel">
						<li>
							<!-- Permitido ver Cuentas Relacionadas al Objeto Imponible -->
							<logic:equal name="liqReconfeccionAdapterVO" property="verCuentaRelEnabled" value="true">
					      		<a href="/siat/gde/AdministrarLiqDeuda.do?method=verCuentaRel&selectedId=<bean:write name="CuentaRel" property="idCuenta" bundle="base" formatKey="general.format.id"/>" >
						      		<bean:write name="CuentaRel" property="nroCuenta"/> -
						      		<bean:write name="CuentaRel" property="desCategoria"/> -
						      		<bean:write name="CuentaRel" property="desRecurso"/>
					      		</a>
					      	</logic:equal>
					      	<logic:notEqual name="liqReconfeccionAdapterVO" property="verCuentaRelEnabled" value="true">
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
		
		<!-- listRecibos -->
		<logic:notEmpty name="liqReconfeccionAdapterVO" property="listDeuda">
			<logic:iterate id="recibo" name="liqReconfeccionAdapterVO" property="listRecibos">
				<p><bean:write name="recibo" property="desDescuento"/></p>
				<div class="horizscroll">
			    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
			    	<caption><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.vencimiento"/>&nbsp;<bean:write name="recibo" property="fechaVtoView"/></caption>
			      	<tbody>
				       	<tr>
						  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/></th>
						  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.saldo"/></th>
						  	<logic:notEqual name="recibo" property="poseePeriodoValorCero" value="true">
						  		<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
						  	</logic:notEqual>
						  	<logic:equal name="recibo" property="poseePeriodoValorCero" value="true">
						  		<th align="left"><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.indiceRecargo"/></th>
						  	</logic:equal>
						  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>
						</tr>
						
						<!-- Item LiqReciboDeudaVO -->
						<logic:iterate id="reciboDeuda" name="recibo" property="listReciboDeuda">
							<tr>
					  			<td><bean:write name="reciboDeuda" property="periodoDeuda"/>&nbsp;</td>
						        <td>
						        	<logic:notEqual name="reciboDeuda" property="valorCero" value="true">
						        		<bean:write name="reciboDeuda" property="totCapital" bundle="base" formatKey="general.format.currency"/>&nbsp;
						        	</logic:notEqual>
						        	<logic:equal name="reciboDeuda" property="valorCero" value="true">
						        		<bean:message bundle="gde" key="gde.liqReconfeccionAdapter.deuda.valorCero.legend"/>
						        	</logic:equal>
						        </td>
						        <logic:notEqual name="reciboDeuda" property="valorCero" value="true">
						        	<td><bean:write name="reciboDeuda" property="totActualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        </logic:notEqual>
						        <logic:equal name="reciboDeuda" property="valorCero" value="true">
						        	<td><bean:write name="reciboDeuda" property="totCoefActualizacionView"/></td>
						        </logic:equal>
						
						        <td>
						        	<logic:notEqual name="reciboDeuda" property="valorCero" value="true">
						        		<bean:write name="reciboDeuda" property="totalReciboDeuda" bundle="base" formatKey="general.format.currency"/>&nbsp;
						        	</logic:notEqual>
						        	<logic:equal name="reciboDeuda" property="valorCero" value="true">
						        		<bean:message bundle="gde" key="gde.liqReconfeccionAdapter.deuda.valorCero.legend"/>
						        	</logic:equal>
						        </td>
					       	</tr>
						</logic:iterate>
						<!-- Fin Item LiqReciboDeudaVO -->
						
						<!-- total recibo -->
							<tr>
								<td colspan="2">&nbsp;</td>
								<td class="celdatotales"><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.total"/>:</td>
								<td class="celdatotales" >
									<logic:notEqual name="reciboDeuda" property="valorCero" value="true">
										<bean:write name="recibo" property="totCapitalOriginal" bundle="base" formatKey="general.format.currency"/>
									</logic:notEqual>
									<logic:equal name="reciboDeuda" property="valorCero" value="true">
						        		<bean:message bundle="gde" key="gde.liqReconfeccionAdapter.deuda.valorCero.legend"/>
						        	</logic:equal>
								</td>
							</tr>
						<!-- Fin total recibo -->
	
						<!-- Recargo recibo -->
							<tr>
								<td colspan="2">&nbsp;</td>
								
								<logic:notEqual name="recibo" property="poseePeriodoValorCero" value="true">
									<td class="celdatotales" ><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.recargo"/>:</td>
									<td class="celdatotales" ><bean:write name="recibo" property="totActualizacion" bundle="base" formatKey="general.format.currency"/></td>
								</logic:notEqual>
								<logic:equal name="recibo" property="poseePeriodoValorCero" value="true">
									<td class="celdatotales" ><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.indiceRecargo"/>:</td>
									<td class="celdatotales" ><bean:write name="reciboDeuda" property="totCoefActualizacionView"/></td>
								</logic:equal>
							</tr>
						<!-- Fin Recargo recibo -->
						
						<!-- Sellado recibo -->
							<tr>
								<td colspan="2">&nbsp;</td>
								<td class="celdatotales" ><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.sellado"/>:</td>
								<td class="celdatotales" ><bean:write name="recibo" property="sellado" bundle="base" formatKey="general.format.currency"/></td>
							</tr>
						<!-- Fin Sellado recibo -->
						
						<!-- Total recibo -->
							<tr>
								<td colspan="2">&nbsp;</td>
								<td class="celdatotales" ><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.totalImporteRecibo"/>:</td>
								<td class="celdatotales" >
									<logic:notEqual name="reciboDeuda" property="valorCero" value="true">
										<bean:write name="recibo" property="totalPagar" bundle="base" formatKey="general.format.currency"/>
									</logic:notEqual>
									<logic:equal name="reciboDeuda" property="valorCero" value="true">
						        		<bean:message bundle="gde" key="gde.liqReconfeccionAdapter.deuda.valorCero.legend"/>
						        	</logic:equal>
								</td>
							</tr>
						<!-- Fin Total recibo -->
			      	</tbody>
				</table>
				</div>
			</logic:iterate>
		</logic:notEmpty>
		<!-- Fin listRecibos -->
	</logic:notEqual>	
	
		<!-- listCuotas -->
	<logic:notEmpty name="liqReconfeccionAdapterVO" property="listCuotas">
		<logic:iterate id="reciboCuo" name="liqReconfeccionAdapterVO" property="listRecibos">
			<p><bean:write name="reciboCuo" property="desDescuento"/></p>
			<div class="horizscroll">
		    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
		    	<caption>Convenio: <bean:write name="reciboCuo" property="convenio.nroConvenio"/>&nbsp; - <bean:message bundle="gde" key="gde.liqReconfeccionAdapter.recibo.vencimiento"/>&nbsp;<bean:write name="reciboCuo" property="fechaVtoView"/></caption>
		      	<tbody>
			       	<tr>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.nroCuota.label"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.capital.label"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.interes.label"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.total.label"/></th>
					</tr>
					
					<!-- Item LiqReciboDeudaVO -->
					<logic:iterate id="reciboCuota" name="reciboCuo" property="listCuota">
						<tr>
				  			<td><bean:write name="reciboCuota" property="nroCuota"/>&nbsp;</td>
					        <td><bean:write name="reciboCuota" property="capital" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="reciboCuota" property="interes" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="reciboCuota" property="recargo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="reciboCuota" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
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
							<td class="celdatotales" ><bean:write name="reciboCuo" property="sellado" bundle="base" formatKey="general.format.currency"/></td>
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
	</logic:notEmpty>
	<!-- Fin listCuotas -->
	
	<!-- Boton imprimir -->
	<div align="center">
		<button type="button" class="boton" onclick="submitForm('impRecibos','');">
			<bean:message bundle="gde" key="gde.liqReconfeccionAdapter.button.imprimir"/>
		</button>
	</div>
	<!-- FIN Boton imprimir -->
				
	<logic:notEqual name="userSession" property="isAnonimoView" value="1">
		<!-- Volver -->
		<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '');">
	  	    <bean:message bundle="base" key="abm.button.volver"/>
		</button>
	</logic:notEqual>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->