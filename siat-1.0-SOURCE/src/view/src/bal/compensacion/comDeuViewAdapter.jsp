<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarComDeu.do">
	
		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.folioAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
	
		<!-- ComDeu -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.comDeu.title"/></legend>			
			<table class="tabladatos">
				<tr>
				<!-- Recurso -->
					<td><label>&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="comDeuAdapterVO" property="comDeu.deuda.desRecurso"/></td>				
				<tr>
				<!-- Cuenta -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="comDeuAdapterVO" property="comDeu.deuda.nroCuenta"/></td>
				</tr>
				<tr>
				<!-- Periodo -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.comDeu.periodoDeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="comDeuAdapterVO" property="comDeu.deuda.periodoDeuda"/></td>
				</tr>	
				<tr>
				<!-- Saldo -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.comDeu.saldoDeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="comDeuAdapterVO" property="comDeu.deuda.saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				</tr>
				<tr>
				<!-- Saldo a Imputar -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.comDeu.importe.label"/>: </label></td>
					<td class="normal"><bean:write name="comDeuAdapterVO" property="comDeu.importeView"/></td>
				</tr>	
			</table>
		</fieldset>
		<!-- Fin ComDeu -->
	
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="comDeuAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
 			</tr>
		</table>
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
		<!-- Inclusion del Codigo Javascript del Calendar-->
		<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->