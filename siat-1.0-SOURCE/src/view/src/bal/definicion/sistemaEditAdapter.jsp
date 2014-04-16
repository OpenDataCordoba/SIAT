<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarSistema.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="bal" key="bal.sistemaEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- Sistema -->
	<fieldset>
		<legend><bean:message bundle="bal" key="bal.sistema.title"/></legend>
		
		<table class="tabladatos">
			<!-- TextCodigo -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.sistema.nroSistema.label"/>: </label></td>
				<td class="normal">
					<logic:equal name="sistemaAdapterVO" property="act" value="modificar">
						<bean:write name="sistemaAdapterVO" property="sistema.nroSistemaView"/>
				 	</logic:equal>
					<logic:equal name="sistemaAdapterVO" property="act" value="agregar">
						<html:text name="sistemaAdapterVO" property="sistema.nroSistemaView" size="15" maxlength="20" />
				 	</logic:equal>	
				</td>
			</tr>
			<!-- TextDescripcion -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.sistema.desSistema.label"/>: </label></td>
				<td class="normal"><html:text name="sistemaAdapterVO" property="sistema.desSistema" size="20" maxlength="100"/></td>			
			</tr>
			<!-- Combo Servicio Banco-->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.sistema.servicioBanco.label"/>: </label></td>
				<td class="normal">
					<html:select name="sistemaAdapterVO" property="sistema.servicioBanco.id" styleClass="select">
						<html:optionsCollection name="sistemaAdapterVO" property="listServicioBanco" label="desServicioBanco" value="id" />
					</html:select>
				</td>					
			</tr>
			<!-- Combo es Servicio Banco -->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.sistema.esServicioBanco.label"/>: </label></td>
				<td class="normal">
					<html:select name="sistemaAdapterVO" property="sistema.esServicioBanco.id" styleClass="select">
						<html:optionsCollection name="sistemaAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>			
		
			<!-- Combo Tipo Deuda -->
			<tr>	
				<td><label><bean:message bundle="bal" key="bal.sistema.tipoDeuda.label"/>: </label></td>
				<td class="normal">
					<html:select name="sistemaAdapterVO" property="sistema.tipoDeuda.id" styleClass="select">
						<html:optionsCollection name="sistemaAdapterVO" property="listTipoDeuda" label="desTipoDeuda" value="id" />
					</html:select>
				</td>					
			</tr>
			
		</table>
	</fieldset>	
	<!-- Sistema -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="sistemaAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="sistemaAdapterVO" property="act" value="agregar">
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
