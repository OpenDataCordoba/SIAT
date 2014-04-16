<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/esp/AdministrarPrecioEvento.do">
	
		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="esp" key="esp.habilitacionAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
	
		<!-- PrecioEvento -->
		<fieldset>
			<legend><bean:message bundle="esp" key="esp.precioEvento.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.tipoEntrada.label"/>: </label></td>
					<td class="normal"><bean:write name="precioEventoAdapterVO" property="precioEvento.tipoEntrada.descripcion"/></td>
				</tr>	
				<tr>
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.precioEvento.precioPublico.label"/>: </label></td>
					<td class="normal">$<bean:write name="precioEventoAdapterVO" property="precioEvento.precioPublicoView"/></td>
				</tr>	
				<tr>
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.precioEvento.precio.label"/>: </label></td>
					<td class="normal">$<bean:write name="precioEventoAdapterVO" property="precioEvento.precioView"/></td>
				</tr>	
			</table>
		</fieldset>
		<!-- Fin PrecioEvento -->
	
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="precioEventoAdapterVO" property="act" value="eliminar">
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
				
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->