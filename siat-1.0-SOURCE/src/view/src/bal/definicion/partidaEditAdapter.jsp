<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarPartida.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="bal" key="bal.partidaEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	<!-- Partida -->
	<fieldset>
		<legend><bean:message bundle="bal" key="bal.partida.title"/></legend>
		
		<table class="tabladatos">
			<!-- TextCodigo -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.partida.codPartida.label"/>: </label></td>
				<td class="normal">
					<logic:equal name="partidaAdapterVO" property="act" value="modificar">
						<bean:write name="partidaAdapterVO" property="partida.codPartida"/>
				 	</logic:equal>
					<logic:equal name="partidaAdapterVO" property="act" value="agregar">
						<html:text name="partidaAdapterVO" property="partida.codPartida" size="15" maxlength="20" />
				 	</logic:equal>	
				</td>
			</tr>
			<!-- Descripcion -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.partida.desPartida.label"/>: </label></td>
				<td class="normal"><html:text name="partidaAdapterVO" property="partida.desPartida" size="20" maxlength="100"/></td>			
			</tr>
			<!-- preEjeAct -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.partida.preEjeAct.label"/>: </label></td>
				<td class="normal"><html:text name="partidaAdapterVO" property="partida.preEjeAct" size="15" maxlength="20" /></td>			
			</tr>
			<!-- preEjeVen -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.partida.preEjeVen.label"/>: </label></td>
				<td class="normal"><html:text name="partidaAdapterVO" property="partida.preEjeVen" size="15" maxlength="20" /></td>			
			</tr>
		</table>
	</fieldset>	
	<!-- Partida -->
	
	<table class="tablabotones">
	   	<tr>
  	   		<td align="left">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="left">
   	   	 		<logic:equal name="partidaAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="partidaAdapterVO" property="act" value="agregar">
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
