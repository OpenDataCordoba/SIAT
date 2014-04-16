<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/cas/AdministrarUsoExpediente.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="cas" key="cas.usoExpedienteViewAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- UsoExpediente -->
		<fieldset>
			<legend><bean:message bundle="cas" key="cas.usoExpediente.title"/></legend>
			<table class="tabladatos">
				<!-- Fecha Accion -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.usoExpendiente.fechaAccion.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="usoExpedienteAdapterVO" property="usoExpediente.fechaAccionView"/></td>
				</tr>
				<!-- Sistema Origen -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.sistemaOrigen.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="usoExpedienteAdapterVO" property="usoExpediente.sistemaOrigen.desSistemaOrigen"/></td>
				</tr>
				<!-- Numero -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.usoExpendiente.numero.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="usoExpedienteAdapterVO" property="usoExpediente.numero"/></td>				
				</tr>
				<!-- Recurso y Cuenta -->
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="usoExpedienteAdapterVO" property="usoExpediente.cuenta.recurso.desRecurso"/></td>
					<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
					<td class="normal"><bean:write name="usoExpedienteAdapterVO" property="usoExpediente.cuenta.numeroCuenta"/></td>
				</tr>
				<!-- Descripcion -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.usoExpendiente.descripcion.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="usoExpedienteAdapterVO" property="usoExpediente.descripcion"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- UsoExpediente -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
		   	    	<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						<bean:message bundle="base" key="abm.button.imprimir"/>
					</html:button>
	   	    	</td>	   	    	
	   	    </tr>
	   	 </table>
	   	 <input type="hidden" name="name"  value="<bean:write name='usoExpedienteAdapterVO' property='name'/>" id="name"/>
	   	 <input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	   	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
