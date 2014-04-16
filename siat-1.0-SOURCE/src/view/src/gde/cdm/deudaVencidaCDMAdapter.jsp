<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarCambioPlanCDM.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				

	<h1><bean:message bundle="rec" key="rec.deudaVencidaCDMAdapter.title"/></h1>
	
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
    	<caption><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.title"/></caption>
      	<tbody>
	       	<tr>
			  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/></th>
			  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaVto"/></th>
			  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.saldo"/></th>
			  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>
			</tr>
			
			<!-- Item LiqDeudaVO -->
			<logic:iterate id="DeudaVencida" name="cambioPlanCDMAdapterVO" property="listDeudaVencida">
				<tr>
		  			<td><bean:write name="DeudaVencida" property="periodoDeuda"/>&nbsp;</td>
			        <td><bean:write name="DeudaVencida" property="fechaVto"/>&nbsp;</td>
			        <td><bean:write name="DeudaVencida" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
			        <td><bean:write name="DeudaVencida" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
		       	</tr>
			</logic:iterate>
			<!-- Fin Item LiqDeudaVO -->
	       	<tr>       		
	       		<td colspan="7" class="celdatotales" align="right">
		        	<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.total.label"/>: 
		        	<b><bean:write name="cambioPlanCDMAdapterVO" property="totalDeudaVencida" bundle="base" formatKey="general.format.currency"/></b>
		        </td>
	       	</tr>	       	
       	<!-- Fin LiqProcuradorVO -->
      	</tbody>
	</table>
	
	<!-- Capital cancelado -->
	<div class="borde">
		<h3>
			<bean:message bundle="rec" key="rec.deudaVencidaCDMAdapter.capitalAComputar.label"/>: 
			<bean:write name="cambioPlanCDMAdapterVO" property="capitalCancelado" bundle="base" formatKey="general.format.currency"/>
		</h3>
	</div>
	
	<div align="center">
	
		<logic:equal name="cambioPlanCDMAdapterVO" property="esCuotaSaldo" value="true">
			<button type="button" name="btnContinuarl" class="boton" onclick="submitForm('continuarCuotaSaldo', '<bean:write name="cambioPlanCDMAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
	  	    	<bean:message bundle="base" key="abm.button.continuar"/>
			</button>
		</logic:equal>
		<logic:notEqual name="cambioPlanCDMAdapterVO" property="esCuotaSaldo" value="true">
			<button type="button" name="btnContinuarl" class="boton" onclick="submitForm('continuarCambioPlan', '<bean:write name="cambioPlanCDMAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
	  	    	<bean:message bundle="base" key="abm.button.continuar"/>
			</button>
		</logic:notEqual>
	</div>
		

	<!-- Volver -->
	<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '<bean:write name="cambioPlanCDMAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
  	    <bean:message bundle="base" key="abm.button.volver"/>
	</button>

	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="cuentaId" value="<bean:write name="cambioPlanCDMAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>"/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->
