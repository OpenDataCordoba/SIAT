<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqConvenioCuenta.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	<h1><bean:message bundle="gde" key="gde.convenioConsistenciaAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<button type="button" name="btn2" class="boton" onclick="submitForm('verConvenio','<bean:write name="convenioConsistenciaAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">Volver</button>
			</td>
		</tr>
	</table>
	<!-- LiqConvenio -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.title"/> </legend>
			
			<!-- Nro Convenio -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.nroConvenio.label"/>:</label>
	      		<bean:write name="convenioConsistenciaAdapterVO" property="convenio.nroConvenio"/>
			</p>
			
			<!-- Plan de Pago -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.planPago.label"/>:</label>
	      		<bean:write name="convenioConsistenciaAdapterVO" property="convenio.desPlan"/>
			</p>
			<!-- Via Deuda -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.viaDeuda.label"/>:</label>
	      		<bean:write name="convenioConsistenciaAdapterVO" property="convenio.desViaDeuda"/>
			
			<!-- Estado Convenio -->
			
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.estadoConvenio.label"/>:</label>
	      		<bean:write name="convenioConsistenciaAdapterVO" property="convenio.desEstadoConvenio"/>
			</p>
			<!-- Cantidad Cuotas -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.cantCuotas.label"/>:</label>
	      		<bean:write name="convenioConsistenciaAdapterVO" property="convenio.canCuotasPlan"/>
			</p>
			<!-- Total Conveniado -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.totalImporteConvenido.label"/>:</label>
	      		<bean:write name="convenioConsistenciaAdapterVO" property="convenio.totImporteConvenio" bundle="base" formatKey="general.format.currency"/>
			</p>
			
			<p class="msgBold">
			<!-- Inconsistencias -->
	      		<label><bean:message bundle="gde" key="gde.convenioConsistenciaAdapter.title"/>:</label>
	      		<bean:write name="convenioConsistenciaAdapterVO" property="observacion"/>
			</p>
				
	</fieldset>
	<!-- LiqConvenio -->	
	
		
	<!-- Periodos Incluidos -->	
	<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%"><caption><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.periodosIncluidos.title"/></caption>
	    	<caption><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.periodosIncluidos.title"/></caption>
	      	<tbody>
	      	<!-- Item LiqDeudaVO -->
		       	<tr>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.periodoDeuda.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.fechaVto.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.saldo.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.actualizacion.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.estado"/></th>
				</tr>
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqPeriodo" name="convenioConsistenciaAdapterVO" property="convenio.listPeriodoIncluido">
			  		<tr>	
			  			<td><bean:write name="LiqPeriodo" property="periodoDeuda"/>&nbsp;</td>
				        <td><bean:write name="LiqPeriodo" property="fechaVto"/>&nbsp;</td>
				        <td><bean:write name="LiqPeriodo" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqPeriodo" property="actualizacion"  bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqPeriodo" property="desEstado"/>&nbsp;</td>
			       	</tr>
				</logic:iterate>
				<tr>
					<td colspan="5" class="celdatotales" align="right">
						<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.periodos.Total"/>:<bean:write name="convenioConsistenciaAdapterVO" property="convenio.totalPeriodos" bundle="base" formatKey="general.format.currency"/>
					</td>
				</tr>	
	      	</tbody>
		</table>
		<logic:equal name="convenioConsistenciaAdapterVO" property="botonesEnabled" value="true">
			<p>	
				<button type="button" name="btn2" class="boton" onclick="submitForm('createMoverDeuda','<bean:write name="convenioConsistenciaAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">
					<bean:message bundle="gde" key="gde.convenioConsistenciaAdapter.moverDeuda.legend"/></button>
			</p>
			<p> &nbsp; Mueve la deuda y sus conceptos a la via del convenio, actualiza su estado a impaga y su saldo al capital original que figura en el convenio, actualiza el id de Convenio y de procurador en la deuda</p>
		</logic:equal>
	</div>	
	<!-- Fin Periodos Incluidos -->
	
	<!-- Pagos de Periodos Incluidos -->	
	<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.convenioConsistenciaAdapter.listaPagos.title"/></caption>
	      	<tbody>
	      	<!-- Item LiqDeudaVO -->
		       	<tr>
		       		<th align="left"><bean:message bundle="gde" key="gde.convenioConsistenciaAdapter.idPagoDeuda"/></th>
		       		<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.periodoDeuda.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.convenioConsistenciaAdapter.listaPagos.fechaPago.legend"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.convenioConsistenciaAdapter.listaPagos.tipoPago.legend"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.convenioConsistenciaAdapter.listaPagos.importePago.legend"/></th>
				</tr>
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqPeriodoPago" name="convenioConsistenciaAdapterVO" property="listPagos">
			  		<tr>	
			  			<td><bean:write name="LiqPeriodoPago" property="idPagoDeudaView"/>&nbsp;</td>
			  			<td><bean:write name="LiqPeriodoPago" property="descripcion"/>&nbsp;</td>
				        <td><bean:write name="LiqPeriodoPago" property="fechaPagoView"/>&nbsp;</td>
				        <td><bean:write name="LiqPeriodoPago" property="desTipoPago" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqPeriodoPago" property="importeView"  bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
			       	</tr>
				</logic:iterate>
	      	</tbody>
		</table>
	</div>	
<button type="button" name="btn2" class="boton" onclick="submitForm('verConvenio','<bean:write name="convenioConsistenciaAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">Volver</button>
	


		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
</html:form>
<!-- Fin formulario -->