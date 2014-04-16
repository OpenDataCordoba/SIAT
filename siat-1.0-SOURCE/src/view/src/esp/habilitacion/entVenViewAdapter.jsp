<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/esp/AdministrarEntVen.do">
	
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
	
		<!-- EntVen -->
		<fieldset>
			<legend><bean:message bundle="esp" key="esp.entVen.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.precioEvento.label"/>: </label></td>					
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.entHab.precioEvento.descripcion"/></td>
				</tr>
				<tr>
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.entVen.nroDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.entHab.nroDesdeView"/></td>
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.entVen.nroHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.entHab.nroHastaView"/></td>
				</tr>	
				<tr>
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.entVen.serie.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.entHab.serie"/></td>
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.entVen.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.entHab.descripcion"/></td>
				</tr>	
				<tr>
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.entVen.totalVendidas.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.totalVendidasView"/></td>
					
				</tr>	
			</table>
		</fieldset>
		<!-- Fin EntVen -->
	
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="entVenAdapterVO" property="act" value="eliminar">
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