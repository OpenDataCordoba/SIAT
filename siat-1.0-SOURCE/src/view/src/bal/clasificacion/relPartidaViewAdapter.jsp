<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarRelPartida.do">
	
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
	
		<!-- RelPartida -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.relPartida.title"/></legend>			
			<table class="tabladatos">
				<!-- Nodo -->		
				<tr>
					<!-- Clave -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.clave.ref"/>: </label></td>
					<td class="normal"><bean:write name="relPartidaAdapterVO" property="relPartida.nodo.clave"/></td>
				</tr>
				<tr>
					<!-- Descripcion -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.descripcion.ref"/>: </label></td>
					<td colspan="3" class="normal"><bean:write name="relPartidaAdapterVO" property="relPartida.nodo.descripcion"/></td>
				</tr>
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.partida.label"/>: </label></td>
					<td class="normal"><bean:write name="relPartidaAdapterVO" property="relPartida.partida.desPartidaView"/></td>				
				</tr>
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.relPartida.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="relPartidaAdapterVO" property="relPartida.fechaDesdeView"/></td>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.relPartida.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="relPartidaAdapterVO" property="relPartida.fechaHastaView"/></td>
				</tr>		
			</table>
		</fieldset>
		<!-- Fin RelPartida -->
	
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="relPartidaAdapterVO" property="act" value="eliminar">
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