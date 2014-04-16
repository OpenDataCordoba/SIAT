<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarCuentaBanco.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="bal" key="bal.cuentaBancoEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- CuentaBanco -->
	<fieldset>
		<legend><bean:message bundle="bal" key="bal.cuentaBanco.title"/></legend>
		
		<table class="tabladatos">
		<!-- Banco -->
		<tr>	
			<td><label><bean:message bundle="def" key="def.banco.label"/>: </label></td>
			<td class="normal">
				<html:select name="cuentaBancoAdapterVO" property="cuentaBanco.banco.id" styleClass="select">
					<html:optionsCollection name="cuentaBancoAdapterVO" property="listBanco" label="desBanco" value="id" />
				</html:select>
			</td>					
		</tr>
		<!-- TipCueBan -->
		<tr>	
			<td><label><bean:message bundle="bal" key="bal.tipCueBan.label"/>: </label></td>
			<td class="normal">
				<html:select name="cuentaBancoAdapterVO" property="cuentaBanco.tipCueBan.id" styleClass="select">
					<html:optionsCollection name="cuentaBancoAdapterVO" property="listTipCueBan" label="descripcion" value="id" />
				</html:select>
			</td>					
		</tr>
		<!-- Area -->
		<tr>	
			<td><label><bean:message bundle="def" key="def.area.label"/>: </label></td>
			<td class="normal">
				<html:select name="cuentaBancoAdapterVO" property="cuentaBanco.area.id" styleClass="select">
					<html:optionsCollection name="cuentaBancoAdapterVO" property="listArea" label="desArea" value="id" />
				</html:select>
			</td>					
		</tr>
		<!-- nroCuenta -->
		<tr>
			<td><label><bean:message bundle="bal" key="bal.cuentaBanco.nroCuenta.label"/>: </label></td>
			<td class="normal"><html:text name="cuentaBancoAdapterVO" property="cuentaBanco.nroCuenta" size="20" maxlength="100"/></td>			
		</tr>
		<tr>
			<td><label><bean:message bundle="bal" key="bal.cuentaBanco.observaciones.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:textarea name="cuentaBancoAdapterVO" property="cuentaBanco.observaciones" cols="80" rows="15"/>
			</td>
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- CuentaBanco -->
	
	<table class="tablabotones">
	   	<tr>
  	   		<td align="left">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="left">
				<logic:equal name="cuentaBancoAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="cuentaBancoAdapterVO" property="act" value="agregar">
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
