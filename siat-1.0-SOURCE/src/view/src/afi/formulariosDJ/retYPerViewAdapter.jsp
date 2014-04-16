<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/afi/AdministrarRetYPer.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="afi" key="afi.retYPerViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- RetYPer -->
		<fieldset>
			<legend><bean:message bundle="afi" key="afi.retYPer.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="afi" key="afi.retYPer.tipoDeduccion.label"/>: </label></td>
					<td class="normal"><bean:write name="retYPerAdapterVO" property="retYPer.tipoDeduccionView"/></td>
			
					<td><label><bean:message bundle="afi" key="afi.retYPer.cuitAgente.label"/>: </label></td>
					<td class="normal"><bean:write name="retYPerAdapterVO" property="retYPer.cuitAgente"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.retYPer.denominacion.label"/>: </label></td>
					<td class="normal"><bean:write name="retYPerAdapterVO" property="retYPer.denominacion"/></td>
			
					<td><label><bean:message bundle="afi" key="afi.retYPer.fecha.label"/>: </label></td>
					<td class="normal"><bean:write name="retYPerAdapterVO" property="retYPer.fechaView"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.retYPer.nroConstancia.label"/>: </label></td>
					<td class="normal"><bean:write name="retYPerAdapterVO" property="retYPer.nroConstancia"/></td>
			
					<td><label><bean:message bundle="afi" key="afi.retYPer.importe.label"/>: </label></td>
					<td class="normal"><bean:write name="retYPerAdapterVO" property="retYPer.importeView"/></td>
				</tr>
	
			</table>
		</fieldset>	
		<!-- RetYPer -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="retYPerAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>					
	   	    	</td>
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='retYPerAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->