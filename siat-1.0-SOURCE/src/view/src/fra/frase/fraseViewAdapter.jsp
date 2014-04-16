<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/fra/AdministrarFrase.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="fra" key="fra.fraseViewAdapter.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
			
		<!-- Frase -->
		<fieldset>
			<legend><bean:message bundle="fra" key="fra.frase.title"/></legend>
			<table class="tabladatos">
				<!-- modulo -->
				<tr>
					<td><label><bean:message bundle="fra" key="fra.fraseEditAdapter.modulo.label"/>: </label></td>
					<td class="normal"><bean:write name="fraseAdapterVO" property="frase.modulo"/></td>
				</tr>
				<!-- pagina -->
				<tr>
					<td><label><bean:message bundle="fra" key="fra.fraseEditAdapter.pagina.label"/>: </label></td>
					<td class="normal"><bean:write name="fraseAdapterVO" property="frase.pagina"/></td>
				</tr>
				<!-- etiqueta -->
				<tr>
					<td><label><bean:message bundle="fra" key="fra.fraseEditAdapter.etiqueta.label"/>: </label></td>
					<td class="normal"><bean:write name="fraseAdapterVO" property="frase.etiqueta"/></td>
				</tr>
				<!-- valorPublico -->
				<tr>
					<td><label><bean:message bundle="fra" key="fra.fraseEditAdapter.valorPublico.label"/>: </label></td>
					<td class="normal"><bean:write name="fraseAdapterVO" property="frase.valorPublico"/></td>
				</tr>
				<!-- valorPublico -->
				<tr>
					<td><label><bean:message bundle="fra" key="fra.fraseEditAdapter.valorPrivado.label"/>: </label></td>
					<td class="normal"><bean:write name="fraseAdapterVO" property="frase.valorPrivado"/></td>
				</tr>
				<!-- Descricion -->
				<tr>
					<td><label><bean:message bundle="fra" key="fra.frase.desFrase.label"/>: </label></td>
					<td class="normal"><bean:write name="fraseAdapterVO" property="frase.desFrase"/></td>
				</tr>
			
			</table>
		</fieldset>	
		<!-- Frase -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	    <logic:equal name="fraseAdapterVO" property="act" value="ver">
		   	    	   <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					   </html:button>
	   	            </logic:equal>
					<logic:equal name="fraseAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="fraseAdapterVO" property="act" value="publicar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('publicar', '');">
							<bean:message bundle="fra" key="fra.fraseEditAdapter.button.publicar"/>
						</html:button>
					</logic:equal>
			
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	   	 
	   	<input type="hidden" name="name"  value="<bean:write name='fraseAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	 	   		   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->