<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/esp/AdministrarLugarEvento.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="esp" key="esp.lugarEventoViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- LugarEvento -->
		<fieldset>
			<legend><bean:message bundle="esp" key="esp.lugarEvento.title"/></legend>
			<table class="tabladatos">
				<!-- Descricion -->
				<tr>
					<td><label><bean:message bundle="esp" key="esp.lugarEvento.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="lugarEventoAdapterVO" property="lugarEvento.descripcion"/></td>
				</tr>
				<!-- Domicilio -->
				<tr>
					<td><label><bean:message bundle="esp" key="esp.lugarEvento.domicilio.label"/>: </label></td>
					<td class="normal"><bean:write name="lugarEventoAdapterVO" property="lugarEvento.domicilio"/></td>
				</tr>
				<!-- Factor Ocupacional -->
				<tr>
					<td><label><bean:message bundle="esp" key="esp.lugarEvento.factorOcupacional.label"/>: </label></td>
					<td class="normal"><bean:write name="lugarEventoAdapterVO" property="lugarEvento.factorOcupacionalView"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- LugarEvento -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="lugarEventoAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					<logic:equal name="lugarEventoAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	<input type="hidden" name="name"  value="<bean:write name='lugarEventoAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->