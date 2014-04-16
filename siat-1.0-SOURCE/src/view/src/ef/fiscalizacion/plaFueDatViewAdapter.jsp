<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarPlaFueDat.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.plaFueDatAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<input type="button" class="boton" onclick="submitForm('volver', '');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"/>					
				</td>
			</tr>
		</table>
		
		<!-- PlaFueDat -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.plaFueDat.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="ef" key="ef.fuenteInfo.label"/>: </label></td>
					<td class="normal"><bean:write name="plaFueDatAdapterVO" property="plaFueDat.fuenteInfo.nombreFuente"/></td>
					
					<td><label><bean:message bundle="ef" key="ef.plaFueDat.observacion.label"/>: </label></td>
					<td class="normal"><bean:write name="plaFueDatAdapterVO" property="plaFueDat.observacion" /></td>
				</tr>
												
			</table>
		</fieldset>	
		<!-- PlaFueDat -->
				
		<!-- PlaFueDatCol -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="ef" key="ef.plaFueDat.listPlaFueDatCol.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="plaFueDatAdapterVO" property="plaFueDat.listPlaFueDatCol">	    	
			    	<tr>
						<th align="left"><bean:message bundle="ef" key="ef.plaFueDatCol.campo.label"/></th>
						<th align="left"><bean:message bundle="ef" key="ef.plaFueDatCol.orden.label"/></th>
						<th align="left"><bean:message bundle="ef" key="ef.plaFueDatCol.oculta.label"/></th>
						<th align="left"><bean:message bundle="ef" key="ef.plaFueDatCol.sumaEnTotal.label"/></th>						
					</tr>
					<logic:iterate id="PlaFueDatColVO" name="plaFueDatAdapterVO" property="plaFueDat.listPlaFueDatCol">
			
						<tr>							
							<td><bean:write name="PlaFueDatColVO" property="colName"/>&nbsp;</td>
							<td><bean:write name="PlaFueDatColVO" property="ordenView"/>&nbsp;</td>
							<td><bean:write name="PlaFueDatColVO" property="oculta.value"/>&nbsp;</td>
							<td><bean:write name="PlaFueDatColVO" property="sumaEnTotal.value"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="plaFueDatAdapterVO" property="plaFueDat.listPlaFueDatCol">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- PlaFueDatCol -->
				
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
		   			<input type="button" class="boton" onclick="submitForm('volver', '');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"/>
				</td>
				<td align="right"> 
					<bean:define id="eliminarEnabled" name="plaFueDatAdapterVO" property="eliminarEnabled"/>
					<input type="button" class="boton" <%=eliminarEnabled%> onClick="submitForm('eliminar', 
						'<bean:write name="plaFueDatAdapterVO" property="plaFueDat.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.eliminar"/>"/>		
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
<!-- plaFueDatAdapter.jsp -->