<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/swe/submitForm.js"%>	    
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/seg/AdministrarUsrApl.do">

		<!-- Mensajes/Advertencias -->
		<%@ include file="/swe/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="seg" key="usrAplAdapter.title"/></h1>
		
		<!-- UsrApl -->
		<fieldset>
			<legend><bean:message bundle="seg" key="usrAplAdapter.subtitle"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="seg" key="usrAplAdapter.aplicacion.label.codigo"/>: </label></td>
					<td class="normal"><bean:write name="usrAplAdapterVO" property="usrApl.aplicacion.codigo"/></td>  
					<td><label><bean:message bundle="seg" key="usrAplAdapter.aplicacion.label.descripcion"/>: </label></td>
					<td class="normal"><bean:write name="usrAplAdapterVO" property="usrApl.aplicacion.descripcion"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="seg" key="usrAplAdapter.usrApl.label.username"/>:</label></td>
					<td class="normal"><bean:write name="usrAplAdapterVO" property="usrApl.username"/></td>
					<td><label><bean:message bundle="seg" key="usrAplAdapter.usrApl.label.estado"/>:</label></td>
					<td class="normal"><bean:write name="usrAplAdapterVO" property="usrApl.estado.value"/></td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="seg" key="usrAplAdapter.permiteWeb.label"/>:</label></td>
					<td class="normal"><bean:write name="usrAplAdapterVO" property="usrApl.permiteWeb.value"/></td>
				</tr>
			</table>
		</fieldset>	

		<!-- lista de aplicaciones permitidas-->
		<logic:equal name="usrAplAdapterVO" property="usrApl.aplicacion.id" value="1">
			<logic:notEmpty name="usrAplAdapterVO" property="listAplicaciones">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="seg" key="usrAplAdapter.listaAplicaciones.label"/></caption>
	               	<tbody>
	               	<tr>
						<th align="left"><bean:message bundle="seg" key="usrAplAdapter.aplicacion.label.codigo"/></th>
						<th align="left"><bean:message bundle="seg" key="usrAplAdapter.aplicacion.label.descripcion"/></th>
					</tr>
					<logic:iterate name="usrAplAdapterVO" property="listAplicaciones" id="app">
						<tr>
							<td><bean:write name="app" property="codigo"/></td>
							<td><bean:write name="app" property="descripcion"/></td>
						</tr>			
					</logic:iterate>
				</table>
			</logic:notEmpty>
			<logic:empty name="usrAplAdapterVO" property="listAplicaciones">
				<bean:message bundle="seg" key="usrAplAdapter.listaAplicaciones.label.vacio"/>
			</logic:empty>
		</logic:equal>		
		
				
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
				<td align="right" width="50%">
	    			<logic:equal name="userSession" property="navModel.act" value="eliminar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
				</td>
	   	    </tr>
	   	</table>
	   	
		<html:hidden name="usrAplAdapterVO" property="usrApl.id" />
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
