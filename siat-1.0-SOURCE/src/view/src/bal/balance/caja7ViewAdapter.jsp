<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarCaja7.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.caja7ViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Caja7 -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.caja7.title"/></legend>
			<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="bal" key="bal.caja7.fecha.label"/>: </label></td>
				<td class="normal"><bean:write name="caja7AdapterVO" property="caja7.fechaView"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.caja7.importe.label"/>: </label></td>
				<td class="normal"><bean:write name="caja7AdapterVO" property="caja7.importeView"/>&nbsp;$</td>				
			</tr>
	    	<tr>
				<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td class="normal"><bean:write name="caja7AdapterVO" property="caja7.estado.value"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.partida.label"/>: </label></td>
				<td class="normal"><bean:write name="caja7AdapterVO" property="caja7.partida.desPartida"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.caja7.descripcion.label"/>: </label></td>
				<td class="normal"><bean:write name="caja7AdapterVO" property="caja7.descripcion"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.caja7.observacion.label"/>: </label></td>
				<td class="normal"><bean:write name="caja7AdapterVO" property="caja7.observacion"/></td>				
			</tr>
			</table>
		</fieldset>	
		<!-- Caja7 -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="caja7AdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					<logic:equal name="caja7AdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='caja7AdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
