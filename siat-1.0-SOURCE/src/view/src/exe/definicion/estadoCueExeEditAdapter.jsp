<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/exe/AdministrarEstadoCueExe.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="exe" key="exe.estadoCueExeEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- EstadoCueExe -->
	<fieldset>
		<legend><bean:message bundle="exe" key="exe.estadoCueExe.title"/></legend>
		
		<table class="tabladatos">
			
			<!-- Descripcion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.estadoCueExe.desEstadoCueExe.label"/>: </label></td>
				<td class="normal"><html:text name="estadoCueExeAdapterVO" property="estadoCueExe.desEstadoCueExe" size="20" maxlength="100"/></td>			
			</tr>
			
			<!-- Tipo -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.estadoCueExe.tipo.label"/>: </label></td>
				<td class="normal">
				    <html:select name="estadoCueExeAdapterVO" property="estadoCueExe.tipoEstadoCueExe.id" styleClass="select">
                         <html:optionsCollection name="estadoCueExeAdapterVO" property="listTipoEstadoCueExe" label="value" value="id" />
                    </html:select>
				</td>			
			</tr>
			<!-- esResolucion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.estadoCueExe.esResolucion.label"/>: </label></td>
				<td class="normal">
				    <html:select name="estadoCueExeAdapterVO" property="estadoCueExe.esResolucion.id" styleClass="select">
                         <html:optionsCollection name="estadoCueExeAdapterVO" property="listSiNo" label="value" value="id" />
                    </html:select>
				</td>
			</tr>
		</table>
	</fieldset>	
	<!-- EstadoCueExe -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="estadoCueExeAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="estadoCueExeAdapterVO" property="act" value="agregar">
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
