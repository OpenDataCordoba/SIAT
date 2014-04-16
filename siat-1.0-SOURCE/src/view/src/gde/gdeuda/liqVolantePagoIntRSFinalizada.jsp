<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarVolantePagoInteresesRS.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>	
	
	<h1><bean:message bundle="gde" key="gde.liqVolantePagoIntRSAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>Para imprimir el volante de pago de intereses para el periodo que a aparece en esta pantalla, debe hacer click en el botón "Generar". Recuerde que para realizar la impresión debe tener instalado Acrobat Reader. </p>
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
			    	<caption><bean:message bundle="gde" key="gde.liqVolantePagoIntRSAdapter.recibo.vencimiento"/>&nbsp;<bean:write name="recibo" property="fechaVtoView"/></caption>
			      	<tbody>
				       	<tr>
						  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/></th>
						  	<th align="left"><bean:message bundle="gde" key="gde.liqVolantePagoIntRSAdapter.liqDeuda.importeCategoria"/></th>
					  		<th align="left"><bean:message bundle="gde" key="gde.liqVolantePagoIntRSAdapter.liqDeuda.interes"/></th>
						  	<th align="left"><bean:message bundle="gde" key="gde.liqVolantePagoIntRSAdapter.liqDeuda.totalAPagar"/></th>
						</tr>
						
						<!-- Item LiqReciboDeudaVO -->
						<logic:iterate id="reciboDeuda" name="recibo" property="listReciboDeuda">
							<tr>
					  			<td><bean:write name="reciboDeuda" property="periodoDeuda"/>&nbsp;</td>
						        <td><bean:write name="reciboDeuda" property="totCapital" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        	<td><bean:write name="reciboDeuda" property="totActualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>						
						        <td><bean:write name="reciboDeuda" property="totActualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					       	</tr>
						</logic:iterate>
						<!-- Fin Item LiqReciboDeudaVO -->
						
						<!-- total recibo -->
							<tr>
								<td colspan="2">&nbsp;</td>
								<td class="celdatotales"><bean:message bundle="gde" key="gde.liqVolantePagoIntRSAdapter.recibo.total"/>:</td>
								<td class="celdatotales" ><bean:write name="recibo" property="totCapitalOriginal" bundle="base" formatKey="general.format.currency"/></td>
							</tr>
						<!-- Fin total recibo -->
	
						<!-- Recargo recibo -->
							<tr>
								<td colspan="2">&nbsp;</td>
								<td class="celdatotales" ><bean:message bundle="gde" key="gde.liqVolantePagoIntRSAdapter.recibo.recargo"/>:</td>
								<td class="celdatotales" ><bean:write name="recibo" property="totActualizacion" bundle="base" formatKey="general.format.currency"/></td>
							</tr>
						<!-- Fin Recargo recibo -->
						
						<!-- Sellado recibo -->
							<tr>
								<td colspan="2">&nbsp;</td>
								<td class="celdatotales" ><bean:message bundle="gde" key="gde.liqVolantePagoIntRSAdapter.recibo.sellado"/>:</td>
								<td class="celdatotales" ><bean:write name="recibo" property="sellado" bundle="base" formatKey="general.format.currency"/></td>
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
		</logic:notEmpty>
		<!-- Fin listRecibos -->
	</logic:notEqual>	
		
	<!-- Boton imprimir -->
	<div align="center">
		<button type="button" class="boton" onclick="submitForm('impRecibos','');">
			<bean:message bundle="gde" key="gde.liqVolantePagoIntRSAdapter.button.imprimir"/>
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