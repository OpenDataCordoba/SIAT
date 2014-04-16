<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarAnularDeuda.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.anularDeudaAnularAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- LiqDeuda -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.anularDeudaAnularAdapter.fieldset.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal"><bean:write name="liqDeudaAdapterVO" property="cuenta.desRecurso"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
	      		<td class="normal"><bean:write name="liqDeudaAdapterVO" property="cuenta.nroCuenta"/></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.anulacion.fechaAnulacion.label"/>: </label></td>
				<td class="normal">
					<html:text name="liqDeudaAdapterVO" property="anulacion.fechaAnulacionView" styleId="fechaAnulacionView" size="10" maxlength="10" styleClass="datos"/>
					<a class="link_siat" onclick="return show_calendar('fechaAnulacionView');" id="a_fechaAnulacionView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.motAnuDeu.label"/>: </label></td>
				<td class="normal">
					<html:select name="liqDeudaAdapterVO" property="anulacion.motAnuDeu.id" styleClass="select">
						<html:optionsCollection name="liqDeudaAdapterVO" property="listMotAnuDeu" label="desMotAnuDeu" value="id" />
					</html:select>
				</td>
			</tr>
			
			<!-- Inclucion de Caso -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="liqDeudaAdapterVO" property="anulacion"/>
					<bean:define id="voName" value="anulacion" />
					<%@ include file="/cas/caso/includeCaso.jsp" %>
				</td>
			</tr>
			<!-- Fin Inclucion de Caso -->	
			
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.anulacion.observacion.label"/>: </label></td>
				<td class="normal"><html:textarea name="liqDeudaAdapterVO" property="anulacion.observacion" cols="80" rows="15"/></td>
			</tr>
			<tr>
				<td colspan="2">				  	
		  		<button type="button" name="btnEnviar" onclick="submitForm('anular', '');" class="boton">
		  			<bean:message bundle="gde" key="gde.anularDeudaAnularAdapter.button.anular"/>
		  		</button>
		  		</td>
		  	</tr>
		  </table>
	</fieldset>	
	<!-- LiqDeuda -->
	
	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
  	    <bean:message bundle="base" key="abm.button.volver"/>
	</html:button>

    <input type="text" style="display:none"/>	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin formulario -->