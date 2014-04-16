<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/ConsultaCodRefPag.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.codRefPagAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.codRefPagAdapter.filtros.legend"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="gde" key="gde.liqCodRefPagAdapter.tipoBoleta.label"/>: </td>
				<td class="normal">
					<html:select name="liqCodRefPagAdapterVO" property="tipoBoleta" styleClass="select">
						<html:optionsCollection name="liqCodRefPagAdapterVO" property="listTipoBoleta" label="codValue" value="id"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.liqCodRefPagAdapter.codRefPag.label"/>: </label></td>
				<td class="normal"><html:text name="liqCodRefPagAdapterVO" property="codRefPagView" size="15"/></td>
			</tr>
		</table>
			<p align="center">
		<html:button property="btnBuscar" styleClass="boton" onclick="submitForm('buscar','');">
			<bean:message bundle="base" key="abm.button.buscar"/>
		</html:button>
	</p>
	</fieldset>
	
	<logic:equal name="liqCodRefPagAdapterVO" property="showResults" value="true">
	<fieldset>
		<legend><bean:message bundle="base" key="base.resultadoBusqueda"/></legend>
		<logic:equal name="liqCodRefPagAdapterVO" property="tieneResultados" value="true">
			<logic:equal name="liqCodRefPagAdapterVO" property="tipoBoletaView" value="3">
				<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.liqReclamo.NumeroCuota.label"/>: </label></td>
						<td class="normal"><bean:write name="liqCodRefPagAdapterVO" property="liqCuota.nroCuota"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.liqReclamo.Importe.label"/>: </label></td>
						<td class="normal"><bean:write name="liqCodRefPagAdapterVO" property="liqCuota.importeCuotaView"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.nroConvenio.label"/>: </label></td>
						<td class="normal"><bean:write name="liqCodRefPagAdapterVO" property="liqCuota.nroConvenio"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.liqReclamo.Cuenta.label"/>: </label></td>
						<td class="normal"><bean:write name="liqCodRefPagAdapterVO" property="liqCuota.nroCuenta"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.planRecurso.label"/>: </label></td>
						<td class="normal"><bean:write name="liqCodRefPagAdapterVO" property="liqCuota.desRecurso"/></td>
					</tr>
				
				</table>
			</logic:equal>
			<logic:equal name="liqCodRefPagAdapterVO" property="tipoBoletaView" value="1">
				<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/>: </label></td>
						<td class="normal"><bean:write name="liqCodRefPagAdapterVO" property="liqDeuda.periodoDeuda"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.liqReclamo.Importe.label"/>: </label></td>
						<td class="normal"><bean:write name="liqCodRefPagAdapterVO" property="liqDeuda.saldoView"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.liqReclamo.Cuenta.label"/>: </label></td>
						<td class="normal"><bean:write name="liqCodRefPagAdapterVO" property="liqDeuda.nroCuenta"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.planRecurso.label"/>: </label></td>
						<td class="normal"><bean:write name="liqCodRefPagAdapterVO" property="liqDeuda.desRecurso"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.estadoCuenta.estado.label"/>: </label></td>
						<td class="normal"><bean:write name="liqCodRefPagAdapterVO" property="liqDeuda.desEstado"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.estadoCuenta.viaDeuda.label"/>: </label></td>
						<td class="normal"><bean:write name="liqCodRefPagAdapterVO" property="liqDeuda.desViaDeuda"/></td>
					</tr>
				
				</table>
			</logic:equal>
			<logic:equal name="liqCodRefPagAdapterVO" property="tipoBoletaView" value="2">
				<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.tramite.nroRecibo.label"/>: </label></td>
						<td class="normal"><bean:write name="liqCodRefPagAdapterVO" property="liqRecibo.numeroReciboView"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.planRecurso.label"/>: </label></td>
						<td class="normal"><bean:write name="liqCodRefPagAdapterVO" property="liqRecibo.recurso.desRecurso"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.liqReclamo.Importe.label"/>: </label></td>
						<td class="normal"><bean:write name="liqCodRefPagAdapterVO" property="liqRecibo.totalView"/></td>
					</tr>
				
				</table>
			</logic:equal>
			
			<logic:equal name="liqCodRefPagAdapterVO" property="tipoBoletaView" value="4">
				<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.tramite.nroRecibo.label"/>: </label></td>
						<td class="normal"><bean:write name="liqCodRefPagAdapterVO" property="liqRecibo.numeroReciboView"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.planRecurso.label"/>: </label></td>
						<td class="normal"><bean:write name="liqCodRefPagAdapterVO" property="liqRecibo.recurso.desRecurso"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.nroConvenio.label"/>: </label></td>
						<td class="normal"><bean:write name="liqCodRefPagAdapterVO" property="liqRecibo.convenio.nroConvenio"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.liqReclamo.Importe.label"/>: </label></td>
						<td class="normal"><bean:write name="liqCodRefPagAdapterVO" property="liqRecibo.totalView"/></td>
					</tr>
				
				</table>
			</logic:equal>
		</logic:equal>
		<logic:notEqual name="liqCodRefPagAdapterVO" property="tieneResultados" value="true">
			<p align="center">	No se encontraron resultados. </p>
		</logic:notEqual>
		
	</fieldset>
	</logic:equal>
	

	
	
	
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