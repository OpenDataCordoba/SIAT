<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarBalance.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.balanceAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
		</table>		
		<!-- Balance -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.balance.title"/></legend>
			
			<table class="tabladatos">
				<!-- Fecha Balance -->
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.balance.fechaBalance.label"/>: </label></td>
					<td class="normal">
						<html:text name="balanceAdapterVO" property="balance.fechaBalanceView" styleId="fechaBalanceView" size="10" maxlength="10" styleClass="datos"/>
							<a class="link_siat" onclick="return show_calendar('fechaBalanceView');" id="a_fechaBalanceView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>					
					</td>
				</tr>		
				<tr>
					<!-- Ejercicio -->
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.ejercicio.label"/>: </label></td>
					<td class="normal">	
						<html:select name="balanceAdapterVO" property="balance.ejercicio.id" styleClass="select"  onchange="submitForm('paramEjercicio', '');">
							<html:optionsCollection name="balanceAdapterVO" property="listEjercicio" label="desEjercicio" value="id" />
						</html:select>
					</td>
					<!-- Estado Ejercicio -->
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.estEjercicio.label"/>: </label></td>
					<td  class="normal">
						<html:text name="balanceAdapterVO" property="balance.ejercicio.estEjercicio.desEjeBal" size="10" maxlength="100" disabled="true"/>
					</td>
				</tr>		
				<!-- Mensaje que depende del estado del ejercicio -->
				<tr>
				<logic:equal name="balanceAdapterVO" property="paramEstadoEjercicio" value="CERRADO">
				<td class="normal" colspan="4" align="center"><label>&nbsp;<bean:message bundle="bal" key="bal.balance.ejercicioCerrado.label"/></label></td>
				</logic:equal>
				<logic:equal name="balanceAdapterVO" property="paramEstadoEjercicio" value="ABIERTO">
				<td class="normal" colspan="4" align="center"><label>&nbsp;<bean:message bundle="bal" key="bal.balance.ejercicioAbierto.label"/></label></td>
				</logic:equal>
				</tr>	
				<!-- Observacion-->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.balance.observacion.label"/>: </label></td>
					<td colspan="3" class="normal"><html:textarea name="balanceAdapterVO" property="balance.observacion" cols="75" rows="15"/></td>					
				</tr>
			</table>
		</fieldset>
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="balanceAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="balanceAdapterVO" property="act" value="agregar">
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
		