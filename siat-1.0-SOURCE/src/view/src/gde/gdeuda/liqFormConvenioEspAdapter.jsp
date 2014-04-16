<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqFormConvenioEsp.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>	
	
	
	<h1><bean:message bundle="gde" key="gde.liqFormConvenioEspAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>Permite realizar la formalización de un Plan Especial de Pago para un Contribuyente, a partir de los Registros de Deuda seleccionados.</p>
				<p>Carga de los Parámetros del Plan Especial.</p>
			</td>				
			<td align="right">
	 			<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '<bean:write name="liqFormConvenioAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
			  	    <bean:message bundle="base" key="abm.button.volver"/>
				</button>
			</td>
		</tr>
	</table>
	<!-- LiqCuenta -->
		<bean:define id="DeudaAdapterVO" name="liqFormConvenioAdapterVO"/>
		<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp" %>
	<!-- LiqCuenta -->
	
	<!-- listDeuda -->
	<logic:notEmpty name="liqFormConvenioAdapterVO" property="listDeuda">
		<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.listaDeudas.title"/></caption>
	      	<tbody>
		       	<tr>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.liqDeuda.periodoDeuda"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.liqDeuda.fechaVto"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.liqDeuda.importeHistorico"/></th>
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqDeudaVO" name="liqFormConvenioAdapterVO" property="listDeuda">
					<tr>
			  			<td><bean:write name="LiqDeudaVO" property="periodoDeuda"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaVO" property="fechaVto"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaVO" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
			       	</tr>
				</logic:iterate>
				<!-- Fin Item LiqDeudaVO -->
				<!-- Fin Item LiqDeudaVO -->
		       	<tr>       		
		       		<td colspan="3" align="right">
			        	<bean:message bundle="gde" key="gde.liqFormConvenioAdapter.total.label"/>: 
			        	<b><bean:write name="liqFormConvenioAdapterVO" property="total" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>
	      	</tbody>
		</table>
		</div>
	</logic:notEmpty>
	<!-- Fin listDeuda -->
	<!-- Ingreso de parámetros -->
		<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqFormConvenioEspAdapter.parametros.title"/></legend>
		<table border="0" width="100%" align="left">
			<tr>&nbsp;</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.liqFormConvenioEspAdapter.parametros.fechaForm.legend"/></label></td>
				<td class="normal">
					<html:text name="liqFormConvenioAdapterVO" property="fechaFormalizacionView" styleId="fechaFormalizacionView" size="15" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaFormalizacionView');" id="a_fechaFormalizacionView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
	
			<tr>
				<td><label>(**)&nbsp;<bean:message bundle="gde" key="gde.liqFormConvenioEspAdapter.parametros.cantMaxCuo.legend"/></label></td>
				<td class="normal"><html:text name="liqFormConvenioAdapterVO" property="cantMaxCuoView" size="15"/></td>
			</tr>
			<tr>
				<td><label>(**)&nbsp;<bean:message bundle="gde" key="gde.liqFormConvenioEspAdapter.parametros.impMinCuo.legend"/></label></td>
				<td class="normal"><html:text name="liqFormConvenioAdapterVO" property="impMinCuoView" size="15"/></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.liqFormConvenioEspAdapter.parametros.descCapital.legend"/></label></td>
				<td class="normal"><html:text name="liqFormConvenioAdapterVO" property="descCapitalView" size="15"/></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.liqFormConvenioEspAdapter.parametros.descActualizacion.legend"/></label></td>
				<td class="normal"><html:text name="liqFormConvenioAdapterVO" property="descActualizacionView" size="15"/></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.liqFormConvenioEspAdapter.parametros.interes.legend"/></label></td>
				<td class="normal"><html:text name="liqFormConvenioAdapterVO" property="interesView" size="15"/></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.liqFormConvenioEspAdapter.parametros.venPrimeraCuota.legend"/></label></td>
				<td class="normal">
					<html:text name="liqFormConvenioAdapterVO" property="venPrimeraCuotaView" styleId="venPrimeraCuotaView" styleClass="datos" size="15" />
				<a class="link_siat" onclick="return show_calendar('venPrimeraCuotaView');" id="a_venPrimeraCuotaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>	
			<tr>
				<td><label><bean:message bundle="gde" key="gde.liqFormConvenioEspAdapter.parametros.importeAnticipo.legend"/></label></td>
				<td class="normal"><html:text name="liqFormConvenioAdapterVO" property="importeAnticipoView" size="15"/></td>
			</tr>
			
			<!-- Inclucion de Caso -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="liqFormConvenioAdapterVO" property="convenio"/>
					<bean:define id="voName" value="convenio" />
					<%@ include file="/cas/caso/includeCaso.jsp" %>
				</td>
			</tr>
			<!-- Fin Inclucion de Caso -->
			
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td colspan="3" align="right">
					<p>(*) Campos obligatorios</p>
					<p>(**) Se debe ingresar al menos un campo</p>
					<p><bean:message bundle="gde" key="gde.liqFormConvenioEspAdapter.formatoPorcentajes"/></p>
				</td>
			</tr>
		</table>
		</fieldset>	
		
	<!--  boton Seleccionar Plan -->
		<p align="right">
			<button type="button" name="btnFormConvenioar" class="boton" onclick="submitForm('seleccionarPlan', '');">
		  	    <bean:message bundle="gde" key="gde.liqFormConvenioAdapter.button.seleccionarPlan"/>
			</button>
		</p>
	<!--  FIN boton Seleccionar Plan -->
	
	<!-- Volver -->
	<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '<bean:write name="liqFormConvenioAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
  	    <bean:message bundle="base" key="abm.button.volver"/>
	</button>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin formulario -->