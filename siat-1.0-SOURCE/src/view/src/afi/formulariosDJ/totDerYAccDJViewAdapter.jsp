<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/afi/AdministrarTotDerYAccDJ.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="afi" key="afi.totDerYAccDJViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- TotDerYAccDJ -->
		<fieldset>
			<legend><bean:message bundle="afi" key="afi.totDerYAccDJ.title"/></legend>
			<table class="tabladatos">				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.totDerYAccDJ.codImpuesto.label"/>: </label></td>
					<td class="normal"><bean:write name="totDerYAccDJAdapterVO" property="totDerYAccDJ.codImpuestoView"/></td>

					<td><label><bean:message bundle="afi" key="afi.totDerYAccDJ.concepto.label"/>: </label></td>
					<td class="normal"><bean:write name="totDerYAccDJAdapterVO" property="totDerYAccDJ.conceptoView"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.totDerYAccDJ.totalMontoIngresado.label"/>: </label></td>
					<td class="normal"><bean:write name="totDerYAccDJAdapterVO" property="totDerYAccDJ.totalMontoIngresadoView"/></td>				
				</tr>

			</table>
		</fieldset>	
		<!-- TotDerYAccDJ -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="totDerYAccDJAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					<logic:equal name="totDerYAccDJAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="totDerYAccDJAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="totDerYAccDJAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='totDerYAccDJAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->