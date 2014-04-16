<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarFuenteInfo.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="ef" key="ef.fuenteInfoEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- FuenteInfo -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.fuenteInfo.title"/></legend>
		
		<table class="tabladatos">
		<!-- nombreFuente -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.fuenteInfo.nombreFuente.label"/>: </label></td>
			<td class="normal"><html:text name="fuenteInfoAdapterVO" property="fuenteInfo.nombreFuente" size="20" maxlength="100"/></td>			
		</tr>
		<!-- TipoPeriodicidad -->
		<tr>	
			<td><label><bean:message bundle="ef" key="ef.fuenteInfo.tipoPeriodicidad.label"/>: </label></td>
			<td class="normal">
				<html:select name="fuenteInfoAdapterVO" property="fuenteInfo.tipoPeriodicidad.id" styleClass="select">
					<html:optionsCollection name="fuenteInfoAdapterVO" property="listTipoPeriodicidad" label="value" value="id" />
				</html:select>
			</td>					
		</tr>
		<!-- apertura -->
		<tr>	
			<td><label><bean:message bundle="ef" key="ef.fuenteInfo.apertura.label"/>: </label></td>
			<td class="normal">
				<html:select name="fuenteInfoAdapterVO" property="fuenteInfo.apertura.id" styleClass="select">
					<html:optionsCollection name="fuenteInfoAdapterVO" property="listSiNo" label="value" value="id" />
				</html:select>
			</td>					
		</tr>
		<tr>
			<td><label><bean:message bundle="ef" key="ef.fuenteInfo.desCol1.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:textarea name="fuenteInfoAdapterVO" property="fuenteInfo.desCol1" cols="80" rows="15"/>
			</td>
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- FuenteInfo -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="fuenteInfoAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="fuenteInfoAdapterVO" property="act" value="agregar">
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
