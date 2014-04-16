<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarRelCla.do">
	
		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.nodoAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
	
		<!-- RelCla -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.relCla.title"/></legend>			
			<table class="tabladatos">
				<!-- Nodo 1 (Origen) -->		
				<tr>
					<!-- Clave -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.clave.label"/>&nbsp;<bean:message bundle="bal" key="bal.relCla.nodo1.label"/>: </label></td>
					<td class="normal"><bean:write name="relClaAdapterVO" property="relCla.nodo1.clave"/></td>
				</tr>
				<tr>
					<!-- Descripcion -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.descripcion.label"/>&nbsp;<bean:message bundle="bal" key="bal.relCla.nodo1.label"/>: </label></td>
					<td colspan="3" class="normal"><bean:write name="relClaAdapterVO" property="relCla.nodo1.descripcion"/></td>
				</tr>
				<!-- Nodo 2 (Destino) -->		
				<!-- Nodo 2 (Destino) -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.clasificador.label"/>&nbsp;<bean:message bundle="bal" key="bal.relCla.nodo2.label"/>: </label></td>
					<td class="normal"><bean:write name="relClaAdapterVO" property="relCla.nodo2.clasificador.descripcion"/></td>
				</tr>
				<tr>
					<!-- Nivel -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.nivel.label"/>&nbsp;<bean:message bundle="bal" key="bal.relCla.nodo2.label"/>: </label></td>
					<td class="normal"><bean:write name="relClaAdapterVO" property="relCla.nodo2.nivelView"/></td>
				</tr>
				<tr>
					<!-- Clave -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.clave.label"/>&nbsp;<bean:message bundle="bal" key="bal.relCla.nodo2.label"/>: </label></td>
					<td class="normal"><bean:write name="relClaAdapterVO" property="relCla.nodo2.clave"/></td>
				</tr>
				<tr>
					<!-- Descripcion -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.descripcion.label"/>&nbsp;<bean:message bundle="bal" key="bal.relCla.nodo2.label"/>: </label></td>
					<td colspan="3" class="normal"><bean:write name="relClaAdapterVO" property="relCla.nodo2.descripcion"/></td>
				</tr>
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.relCla.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="relClaAdapterVO" property="relCla.fechaDesdeView"/></td>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.relCla.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="relClaAdapterVO" property="relCla.fechaHastaView"/></td>
				</tr>		
			</table>
		</fieldset>
		<!-- Fin RelCla -->
	
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="relClaAdapterVO" property="act" value="eliminar">
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