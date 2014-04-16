<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarIndet.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.indetAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
		</table>		
		<!-- Indet -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.indet.title"/></legend>
			<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.sistema.label"/>: </label></td>
				<td class="normal"><html:text name="indetAdapterVO" property="indet.sistema" size="10" maxlength="2" styleClass="datos" /></td>
			
				<td><label><bean:message bundle="bal" key="bal.indet.nroComprobante.label"/>: </label></td>
				<td class="normal"><html:text name="indetAdapterVO" property="indet.nroComprobante" size="20" maxlength="10" styleClass="datos" /></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.clave.label"/>: </label></td>
				<td class="normal"><html:text name="indetAdapterVO" property="indet.clave" size="10" maxlength="6" styleClass="datos" /></td>
			
				<td><label><bean:message bundle="bal" key="bal.indet.partida.label"/>: </label></td>
				<td class="normal"><html:text name="indetAdapterVO" property="indet.partida" size="20" maxlength="14" styleClass="datos" /></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.resto.label"/>: </label></td>
				<td class="normal"><html:text name="indetAdapterVO" property="indet.resto" size="10" maxlength="4" styleClass="datos" /></td>
			
				<td><label><bean:message bundle="bal" key="bal.indet.importeCobrado.label"/>: </label></td>
				<td class="normal"><html:text name="indetAdapterVO" property="indet.importeCobradoView" size="10" maxlength="100" styleClass="datos" /></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.recargo.label"/>: </label></td>
				<td class="normal"><html:text name="indetAdapterVO" property="indet.recargoView" size="10" maxlength="100" styleClass="datos" /></td>
				<logic:equal name="indetAdapterVO" property="act" value="agregar">
					<td><label><bean:message bundle="bal" key="bal.indet.importeBasico.label"/>: </label></td>
					<td class="normal"><html:text name="indetAdapterVO" property="indet.importeBasicoView" size="10" maxlength="100" styleClass="datos" /></td>
				</logic:equal>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.codIndet.label"/>: </label></td>
				<td class="normal"><html:text name="indetAdapterVO" property="indet.codIndetView" size="5" maxlength="2" styleClass="datos" /></td>

				<logic:equal name="indetAdapterVO" property="act" value="agregar">
					<td><label><bean:message bundle="bal" key="bal.indet.importeCalculado.label"/>: </label></td>
					<td class="normal"><html:text name="indetAdapterVO" property="indet.importeCalculadoView" size="10" maxlength="100" styleClass="datos" /></td>
				</logic:equal>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.fechaPago.label"/>: </label></td>
				<td class="normal">
					<html:text name="indetAdapterVO" property="indet.fechaPagoView" styleId="fechaPagoView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaPagoView');" id="a_fechaPagoView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				
				<td><label><bean:message bundle="bal" key="bal.indet.tipoIngreso.label"/>: </label></td>
				<td class="normal"><html:text name="indetAdapterVO" property="indet.tipoIngresoView" size="5" maxlength="10" styleClass="datos" /></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.caja.label"/>: </label></td>
				<td class="normal"><html:text name="indetAdapterVO" property="indet.cajaView" size="5" maxlength="100" styleClass="datos" /></td>

				<td><label><bean:message bundle="bal" key="bal.indet.paquete.label"/>: </label></td>
				<td class="normal"><html:text name="indetAdapterVO" property="indet.paqueteView" size="5" maxlength="20" styleClass="datos" /></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.codPago.label"/>: </label></td>
				<td class="normal"><html:text name="indetAdapterVO" property="indet.codPagoView" size="5" maxlength="5" styleClass="datos" /></td>
			
				<td><label><bean:message bundle="bal" key="bal.indet.fechaBalance.label"/>: </label></td>
				<td class="normal">
					<html:text name="indetAdapterVO" property="indet.fechaBalanceView" styleId="fechaBalanceView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaBalanceView');" id="a_fechaBalanceView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>		
			</tr>
			<tr>			
				<td><label><bean:message bundle="bal" key="bal.indet.reciboTr.label"/>: </label></td>
				<td class="normal"><html:text name="indetAdapterVO" property="indet.reciboTr" size="15" maxlength="100" styleClass="datos" /></td>
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
				<logic:equal name="indetAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificarYreingresar', '');">
						<bean:message bundle="bal" key="bal.indetSearchPage.adm.button.reingresar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="indetAdapterVO" property="act" value="agregar">
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
		