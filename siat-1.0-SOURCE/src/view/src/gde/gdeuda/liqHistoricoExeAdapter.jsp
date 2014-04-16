<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/gde/AdministrarLiqDeuda.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.liqHistoricoExeAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('refill', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<!-- HCuenta -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqHistoricoExeAdapter.recursoCuenta.label"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="liqHistoricoExeAdapterVO" property="cuenta.desRecurso"/></td>
				</tr>
				<tr>	
					<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="liqHistoricoExeAdapterVO" property="cuenta.nroCuenta" /></td>
				</tr>
			</table>
		</fieldset>	
		<!-- HCuenta -->
		
		<!-- CueExe -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.liqHistoricoExeAdapter.listExencinoes.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="liqHistoricoExeAdapterVO" property="listExencinoes">	    	
			    	<tr>
						<th align="left">Descripción</th>
						<th align="left">Fecha Vigencia Desde</th>
						<th align="left">Fecha Vigencia Hasta</th>
						<th align="left">Caso</th>
					</tr>
					<logic:iterate id="LiqExencionVO" name="liqHistoricoExeAdapterVO" property="listExencinoes">
						<tr>
							<td><bean:write name="LiqExencionVO" property="desExencion"/>&nbsp;</td>
							<td><bean:write name="LiqExencionVO" property="fechaDesde"/>&nbsp;</td>
							<td><bean:write name="LiqExencionVO" property="fechaHasta"/>&nbsp;</td>
							<td>
								<bean:write name="LiqExencionVO" property="caso.sistemaOrigen.desSistemaOrigen"/>
								&nbsp;							
								<bean:write name="LiqExencionVO" property="caso.numero"/>
							</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="liqHistoricoExeAdapterVO" property="listExencinoes">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				
			</tbody>
		</table>
		<!-- CueExe -->
				
		<table class="tablabotones">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('refill', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>   	    			
			</tr>
		</table>
	   	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
