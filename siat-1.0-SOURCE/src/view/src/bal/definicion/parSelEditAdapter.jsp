<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarParSel.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="bal" key="bal.parSelEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	<!-- ParSel -->
	<fieldset>
		<legend><bean:message bundle="bal" key="bal.parSel.title"/></legend>
		
		<table class="tabladatos">
		<!-- Sellado -->		
		<tr>
			<td><label><bean:message bundle="bal" key="bal.sellado.codSellado.ref"/>: </label></td>
			<td class="normal">
				<bean:write name="parSelAdapterVO" property="parSel.sellado.codSellado"/>				
			</td>
			<td><label><bean:message bundle="bal" key="bal.sellado.desSellado.ref"/>: </label></td>
			<td class="normal">
				<bean:write name="parSelAdapterVO" property="parSel.sellado.desSellado"/>				
			</td>
		</tr>
		<!-- Partida -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.parSel.partida.label"/>: </label></td>
			<td class="normal">
				<html:select name="parSelAdapterVO" property="parSel.partida.id" styleClass="select" >
					<html:optionsCollection name="parSelAdapterVO" property="listPartida" label="desPartidaView" value="id" />
				</html:select>
			</td>
		</tr>
		<!--Tipo Distribucion -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.parSel.tipoDistrib.label"/>: </label></td>
			<td class="normal">
				<html:select name="parSelAdapterVO" property="parSel.tipoDistrib.id" styleClass="select" >
					<html:optionsCollection name="parSelAdapterVO" property="listTipoDistrib" label="desTipoDistrib" value="id" />
				</html:select>
			</td>
		</tr>
		<!--Monto -->
		<tr>
			<td ><label><bean:message bundle="bal" key="bal.parSel.monto.label"/>: </label></td>
			<td class="normal"><html:text name="parSelAdapterVO" property="parSel.montoView" size="15" maxlength="100"/></td>			
		</tr>

		</table>
	</fieldset>	
	<!-- ParSel -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="parSelAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="parSelAdapterVO" property="act" value="agregar">
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
