<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/exe/AdministrarTipSujExe.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="exe" key="exe.tipSujExeEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
	
			</td>				
			<td align="right">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- TipoSujeto -->
	<fieldset>
		<legend><bean:message bundle="exe" key="exe.tipoSujeto.title"/></legend>
		<table class="tabladatos">
			<!-- Codigo -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.tipoSujeto.codTipoSujeto.label"/>: </label></td>
				<td class="normal"><bean:write name="tipSujExeAdapterVO" property="tipSujExe.tipoSujeto.codTipoSujeto"/></td>
			</tr>
			<!-- Descricion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.tipoSujeto.desTipoSujeto.label"/>: </label></td>
				<td class="normal"><bean:write name="tipSujExeAdapterVO" property="tipSujExe.tipoSujeto.desTipoSujeto"/></td>
			</tr>
							
			<!-- Estado -->
			<tr>
				<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td class="normal"><bean:write name="tipSujExeAdapterVO" property="tipSujExe.tipoSujeto.estado.value"/></td>
			</tr>
		</table>
	</fieldset>	
	<!-- TipoSujeto -->
		
	<!-- TipSujExe -->
	<fieldset>
		<legend><bean:message bundle="exe" key="exe.tipSujExe.title"/></legend>
		
		<table class="tabladatos">
		<!-- Recurso -->
		<tr>	
			<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
			<td class="normal">				
				<bean:write name="tipSujExeAdapterVO" property="tipSujExe.exencion.recurso.desRecurso"/>				
			</td>					
		</tr>
		<!-- Exencion -->
		<tr>	
			<td><label><bean:message bundle="exe" key="exe.exencion.label"/>: </label></td>
			<td class="normal">
				<bean:write name="tipSujExeAdapterVO" property="tipSujExe.exencion.desExencion"/>					
			</td>					
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- TipSujExe -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="tipSujExeAdapterVO" property="act" value="eliminar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('eliminar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
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
