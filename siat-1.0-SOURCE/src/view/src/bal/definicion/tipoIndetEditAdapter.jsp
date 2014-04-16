<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarTipoIndet.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="bal" key="bal.tipoIndetEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- TipoIndet -->
	<fieldset>
		<legend><bean:message bundle="bal" key="bal.tipoIndet.title"/></legend>
		
		<table class="tabladatos">
			<!-- codTipoIndet -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.tipoIndet.codTipoIndet.label"/>: </label></td>
				<td class="normal">
					<logic:equal name="tipoIndetAdapterVO" property="act" value="modificar">
						<bean:write name="tipoIndetAdapterVO" property="tipoIndet.codTipoIndet"/>
				 	</logic:equal>
					<logic:equal name="tipoIndetAdapterVO" property="act" value="agregar">
						<html:text name="tipoIndetAdapterVO" property="tipoIndet.codTipoIndet" size="15" maxlength="20" />
				 	</logic:equal>	
				</td>
			</tr>
			<!-- desTipoIndet -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.tipoIndet.desTipoIndet.label"/>: </label></td>
				<td class="normal"><html:text name="tipoIndetAdapterVO" property="tipoIndet.desTipoIndet" size="30" maxlength="100"/></td>			
			</tr>
			<!-- codIndetMR -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.tipoIndet.codIndetMR.label"/>: </label></td>
				<td class="normal"><html:text name="tipoIndetAdapterVO" property="tipoIndet.codIndetMR" size="15" maxlength="100"/></td>			
			</tr>

		</table>
	</fieldset>	
	<!-- TipoIndet -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="100%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="100%">
				<logic:equal name="tipoIndetAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="tipoIndetAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
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

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->