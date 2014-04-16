<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarCambioPlanCDM.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>	
	
	<h1><bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.title"/></h1>	

	<!-- LiqCuenta -->
		<bean:define id="DeudaAdapterVO" name="cambioPlanCDMAdapterVO"/>
		<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp" %>
	<!-- LiqCuenta -->
	
	<!-- Datos del plan al cual se cambio -->
	<fieldset>
		<legend><bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.obra.label"/></legend>
		
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.obra.label"/>: </label></td>
				<td class="normal"><bean:write name="cambioPlanCDMAdapterVO" property="plaCuaDet.planillaCuadra.obra.desObra"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.plan.label"/>: </label></td>
				<td class="normal"><bean:write name="cambioPlanCDMAdapterVO" property="plaCuaDet.obrForPag.desFormaPago"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.totalObra.label"/>: </label></td>
				<td class="normal"><bean:write name="cambioPlanCDMAdapterVO" property="plaCuaDet.importeTotal" bundle="base" formatKey="general.format.currency"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.cuotasAPagar.label"/>: </label></td>
				<td class="normal"><bean:write name="cambioPlanCDMAdapterVO" property="cuotasAPagar" bundle="base" formatKey="general.format.id"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.totalPendiente.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cambioPlanCDMAdapterVO" property="totalDeudaGenerada" bundle="base" formatKey="general.format.currency"/></td>
			</tr>
		</table>
	</fieldset>
	<!-- Fin Datos del plan al cual se cambio -->		
	
	<!-- Dedua Generada -->
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
    	<caption><bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.deudaGenerada.title"/></caption>
      	<tbody>
	       	<tr>
			  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/></th>
			  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaVto"/></th>
			  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.saldo"/></th>
			  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
			  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>
			</tr>
			
			<!-- Item LiqDeudaVO -->
			<logic:iterate id="DeudaGenerada" name="cambioPlanCDMAdapterVO" property="listDeudaGenerada">
				<tr>
		  			<td><bean:write name="DeudaGenerada" property="periodoDeuda"/>&nbsp;</td>
			        <td><bean:write name="DeudaGenerada" property="fechaVto"/>&nbsp;</td>
			        <td><bean:write name="DeudaGenerada" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
			        <td><bean:write name="DeudaGenerada" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
			        <td><bean:write name="DeudaGenerada" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
		       	</tr>
			</logic:iterate>
			<!-- Fin Item LiqDeudaVO -->
	       	<tr>       		
	       		<td colspan="7" class="celdatotales" align="right">
		        	<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.total.label"/>: 
		        	<b><bean:write name="cambioPlanCDMAdapterVO" property="totalDeudaGenerada" bundle="base" formatKey="general.format.currency"/></b>
		        </td>
	       	</tr>	       	
       	<!-- Fin LiqProcuradorVO -->
      	</tbody>
	</table>
	<!-- Fin Dedua Generada -->
	
	<div align="center">
		<button type="button" name="btnImprForm" class="boton" onclick="submitForm('printForm', '');">
	  	    <bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.button.imprimirForm"/>
		</button>
		&nbsp;	
		<button type="button" name="btnImprCuotas" class="boton" onclick="submitForm('printRecibo', '');">
	  	    <bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.button.imprimirRecibo"/>
		</button>
		&nbsp;
		<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '<bean:write name="cambioPlanCDMAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
	  	    <bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.button.finalizar"/>
		</button>
	</div>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->