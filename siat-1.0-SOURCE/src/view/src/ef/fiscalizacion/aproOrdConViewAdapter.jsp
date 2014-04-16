<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarAproOrdCon.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.aproOrdConViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- AproOrdCon -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.aproOrdCon.title"/></legend>
			<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="ef" key="ef.aproOrdCon.fecha.label"/>: </label></td>
				<td class="normal"><bean:write name="aproOrdConAdapterVO" property="aproOrdCon.fechaView"/></td>				
			</tr>
			
			<tr>
				<td><label><bean:message bundle="ef" key="ef.aproOrdCon.observacion.label"/>: </label></td>
				<td class="normal"><bean:write name="aproOrdConAdapterVO" property="aproOrdCon.observacion"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.estadoOrden.desEstadoOrden.label"/>: </label></td>
				<td class="normal"><bean:write name="aproOrdConAdapterVO" property="aproOrdCon.estadoOrden.desEstadoOrden"/></td>
			</tr>
				<!-- <#BeanWrites#> -->
			</table>
		</fieldset>	
		<!-- AproOrdCon -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right">
	   	    	   <logic:equal name="aproOrdConAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='aproOrdConAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- aproOrdConViewAdapter.jsp -->
