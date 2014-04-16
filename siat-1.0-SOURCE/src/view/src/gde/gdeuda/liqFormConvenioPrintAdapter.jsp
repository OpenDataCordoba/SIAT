<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqFormConvenio.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>	
	
	
	<h1>
		<bean:message bundle="gde" key="gde.liqFormConvenioPrintAdapter.title"/>: 
		<bean:write name="liqFormConvenioAdapterVO" property="convenio.nroConvenio"/>
	</h1>	
	
	<p>Detalle del Convenio Formalizado.</p>


	<!-- LiqCuenta -->
		<bean:define id="DeudaAdapterVO" name="liqFormConvenioAdapterVO"/>
		<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp" %>
	<!-- LiqCuenta -->
	
	<!-- listDeuda -->
	<logic:notEmpty name="liqFormConvenioAdapterVO" property="listDeuda">
		<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.listaDeudas.title"/></caption>
	      	<tbody>
		       	<tr>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.liqDeuda.periodoDeuda"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.liqDeuda.fechaVto"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.liqDeuda.importe"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.liqDeuda.actualizacion"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.liqDeuda.total"/></th>
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqDeudaVO" name="liqFormConvenioAdapterVO" property="listDeuda">
					<tr>
			  			<td><bean:write name="LiqDeudaVO" property="periodoDeuda"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaVO" property="fechaVto"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaVO" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaVO" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaVO" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
			       	</tr>
				</logic:iterate>
				<!-- Fin Item LiqDeudaVO -->
		       	<tr>       		
		       		<td colspan="5" align="right">
			        	<bean:message bundle="gde" key="gde.liqFormConvenioAdapter.total.label"/>: 
			        	<b><bean:write name="liqFormConvenioAdapterVO" property="total" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>
	      	</tbody>
		</table>
		</div>
	</logic:notEmpty>
	<!-- Fin listDeuda -->
	
	<p>
		(*) Deuda actualizada a la Fecha de Formalización del Plan, considerando la forma de actualizacion del Plan y 
		sin considerar los descuentos del mismo.
	</p>
	
	<!-- Alternativa Cuotas del Planes  -->
	<logic:notEmpty name="liqFormConvenioAdapterVO" property="planSelected.listCuotasForm">
		<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption> Detalle de las Cuotas del Convenio</caption>
	      	<tbody>
	      		<tr>
		       		<td colspan="5">
		       			<p>
		       				<label>Plan:</label>
		       				<bean:write name="liqFormConvenioAdapterVO" property="planSelected.desPlan"/>
		       			</p>
		       			<p>
		       				<label>V&iacute;a Deuda:</label>
		       				<bean:write name="liqFormConvenioAdapterVO" property="planSelected.desViaDeuda"/>
		       			</p>
		       			<logic:equal name="liqFormConvenioAdapterVO" property="esEspecial" value="false">
			       			<p>
			       				<label>Descuento Capital:</label>
			       				<bean:write name="liqFormConvenioAdapterVO" property="planSelected.desCapitalOriginal" bundle="base" formatKey="general.format.porcentaje"/>
			       			</p>
			       			<p>
			       				<label>Descuento Actualizaci&oacute;n:</label>
			       				<bean:write name="liqFormConvenioAdapterVO" property="planSelected.desActualizacion" bundle="base" formatKey="general.format.porcentaje"/>
			       			</p>
			       			<p>
			       				<label>Interes: </label>
			       				<bean:write name="liqFormConvenioAdapterVO" property="planSelected.interes" bundle="base" formatKey="general.format.porcentaje"/>
			       				&nbsp;
			       				<label>Descuento Interes: </label>
			       				<bean:write name="liqFormConvenioAdapterVO" property="planSelected.desInteres" bundle="base" formatKey="general.format.porcentaje"/>
			       				&nbsp;
			       				<label>Interes Aplicado: </label>
			       				<bean:write name="liqFormConvenioAdapterVO" property="planSelected.interesAplicado" bundle="base" formatKey="general.format.porcentaje"/>
			       			</p>			       			
			       		</logic:equal>
			       		<p>
		       				<label>Observaciones:</label>
		       				<bean:write name="liqFormConvenioAdapterVO" property="convenio.observacionFor"/>
		       			</p>
		       		</td>
		       	</tr>
		       	<tr>
		       		<th align="left">Nro Cuota</th>
		       		<th align="left">Capital</th>
				  	<th align="left">Interés Financiero</th>
		       		<th align="left">Importe</th>
				  	<th align="left">Fec. Vto.</th>
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqCuotaVO" name="liqFormConvenioAdapterVO" property="planSelected.listCuotasForm">
					<tr>
						<td><bean:write name="LiqCuotaVO" property="nroCuota"/>&nbsp;</td>
						<td><bean:write name="LiqCuotaVO" property="capital" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						<td><bean:write name="LiqCuotaVO" property="interes" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						<td><bean:write name="LiqCuotaVO" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						<td><bean:write name="LiqCuotaVO" property="fechaVto"/>&nbsp;</td>
					</tr>
				</logic:iterate>
				
				<tr>
					<td align="right"> <b>Total:</b> </td>
					<td><bean:write name="liqFormConvenioAdapterVO" property="planSelected.totalCapital" bundle="base" formatKey="general.format.currency"/></td>
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
	
	<!-- Volver -->
	<div align="center">
		<button type="button" name="btnImprForm" class="boton" onclick="submitForm('printForm', '');">
	  	    Imprimir Formulario Formalizaci&oacute;n
		</button>
		&nbsp;	
		<button type="button" name="btnImprCuotas" class="boton" onclick="submitForm('printRecibos', '');">
	  	    Imprimir Cuotas
		</button>
		&nbsp;
		<button type="button" name="btnVolver" class="boton" onclick="submitForm('finalizar', '');">
	  	    Finalizar
		</button>
	</div>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->