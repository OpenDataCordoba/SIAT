<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/esp/AdministrarHabExe.do">

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

		<!-- HabExe -->
		<fieldset>
			<legend><bean:message bundle="esp" key="esp.habExe.title"/></legend>			
			<table class="tabladatos">
				<tr>					
					<tr>	
						<td><label><bean:message bundle="exe" key="exe.exencion.label"/>: </label></td>
						<td class="normal"><bean:write name="habExeAdapterVO" property="habExe.exencion.desExencion"/></td>
					</tr>		
				</tr>
				<tr>
					<td><label><bean:message bundle="esp" key="esp.habExe.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="habExeAdapterVO" property="habExe.fechaDesdeView"/></td>
					<td><label><bean:message bundle="esp" key="esp.habExe.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="habExeAdapterVO" property="habExe.fechaHastaView"/></td>
				</tr>	
					
			</table>
		</fieldset>
		<!-- Fin HabExe -->
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="habExeAdapterVO" property="act" value="eliminar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('eliminar', '');">
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