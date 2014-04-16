<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/esp/AdministrarLugarEvento.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="esp" key="esp.lugarEventoEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- LugarEvento -->
	<fieldset>
		<legend><bean:message bundle="esp" key="esp.lugarEvento.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="esp" key="esp.lugarEvento.descripcion.label"/>: </label></td>
				<td class="normal"><html:text name="lugarEventoAdapterVO" property="lugarEvento.descripcion" size="20" maxlength="255"/></td>			
			</tr>
			<tr>
				<td><label><bean:message bundle="esp" key="esp.lugarEvento.domicilio.label"/>: </label></td>
				<td class="normal"><html:text name="lugarEventoAdapterVO" property="lugarEvento.domicilio" size="15" maxlength="100" /></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="esp" key="esp.lugarEvento.factorOcupacional.label"/>: </label></td>
				<td class="normal"><html:text name="lugarEventoAdapterVO" property="lugarEvento.factorOcupacionalView" size="15" maxlength="20" /></td>
			</tr>			
		</table>
	</fieldset>	
	<!-- LugarEvento -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="lugarEventoAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="lugarEventoAdapterVO" property="act" value="agregar">
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