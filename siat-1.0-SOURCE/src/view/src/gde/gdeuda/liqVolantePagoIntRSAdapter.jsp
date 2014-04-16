<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarVolantePagoInteresesRS.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>	
	
	<h1><bean:message bundle="gde" key="gde.liqVolantePagoIntRSAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>Para imprimir el volante de pago de intereses para el periodo que a aparece en esta pantalla, debe hacer click en el botón "Generar". Recuerde que para realizar la impresión debe tener instalado Acrobat Reader. </p>
			</td>				
			<td align="right">
	 			<logic:notEqual name="userSession" property="isAnonimoView" value="1">
					<!-- Volver -->
					<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '');">
				  	    <bean:message bundle="base" key="abm.button.volver"/>
					</button>
				</logic:notEqual>
			</td>
		</tr>
	</table>

	<logic:notEqual name="liqReconfeccionAdapterVO" property="cuenta" value="">
		<!-- LiqCuenta -->
			<bean:define id="DeudaAdapterVO" name="liqReconfeccionAdapterVO"/>
			<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp" %>
		<!-- LiqCuenta -->
	
		<!-- CuentasRel -->
		<logic:notEmpty name="liqReconfeccionAdapterVO" property="listCuentaRel" >
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuentasRelacionadas.title"/> </legend>			
				<ul>
					<logic:iterate id="CuentaRel" name="liqReconfeccionAdapterVO" property="listCuentaRel">
						<li>
							<!-- Permitido ver Cuentas Relacionadas al Objeto Imponible -->
							<logic:equal name="liqReconfeccionAdapterVO" property="verCuentaRelEnabled" value="true">
					      		<a href="/siat/gde/AdministrarLiqDeuda.do?method=verCuentaRel&selectedId=<bean:write name="CuentaRel" property="idCuenta" bundle="base" formatKey="general.format.id"/>" >
						      		<bean:write name="CuentaRel" property="nroCuenta"/> -
						      		<bean:write name="CuentaRel" property="desCategoria"/> -
						      		<bean:write name="CuentaRel" property="desRecurso"/>
					      		</a>
					      	</logic:equal>
					      	<logic:notEqual name="liqReconfeccionAdapterVO" property="verCuentaRelEnabled" value="true">
					      		<bean:write name="CuentaRel" property="nroCuenta"/> -
						      		<bean:write name="CuentaRel" property="desCategoria"/> -
						      		<bean:write name="CuentaRel" property="desRecurso"/>
					      	</logic:notEqual>
						</li>
					</logic:iterate>
				</ul>		
			</fieldset>
		</logic:notEmpty>
		<!-- Fin CuentasRel -->
			
		<!-- listDeuda -->
		<logic:notEmpty name="liqReconfeccionAdapterVO" property="listDeuda">
			<div class="horizscroll">
		    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
		    	<caption><bean:message bundle="gde" key="gde.liqVolantePagoIntRSAdapter.listaDeudas.title"/></caption>
		      	<tbody>
			       	<tr>
					  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/></th>
					  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaVto"/></th>
					  	<th align="left"><bean:message bundle="gde" key="gde.liqVolantePagoIntRSAdapter.liqDeuda.importeCategoria"/></th>
					  	<th align="left"><bean:message bundle="gde" key="gde.liqVolantePagoIntRSAdapter.liqDeuda.interes"/></th>
					</tr>
					
					<!-- Item LiqDeudaVO -->
					<logic:iterate id="LiqDeudaVO" name="liqReconfeccionAdapterVO" property="listDeuda">
						<tr>
				  			<!-- Ver detalle Deuda -->
				  			<td><bean:write name="LiqDeudaVO" property="periodoDeuda"/>&nbsp;</td>
					        <td><bean:write name="LiqDeudaVO" property="fechaVto"/>&nbsp;</td>
					        <td><bean:write name="LiqDeudaVO" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="LiqDeudaVO" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				       	</tr>
					</logic:iterate>
					<!-- Fin Item LiqDeudaVO -->
		      	</tbody>
			</table>
			</div>
		</logic:notEmpty>
		<!-- Fin listDeuda -->
		
	</logic:notEqual>
	
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqVolantePagoIntRSAdapter.fechaPago.title"/></legend>
		<table border="0">
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.liqVolantePagoIntRSAdapter.fechaPago.label"/></label></td>
				<td class="normal">
					<html:text name="liqReconfeccionAdapterVO" property="fechaPagoView" styleId="fechaPagoView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaPagoView');" id="a_fechaPagoView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="left">
				<p><bean:message bundle="gde" key="gde.liqVolantePagoIntRSAdapter.fechaPago.leyenda"/></p>
				</td>
			</tr>	
		</table>		
	</fieldset>	
	
	<!--  boton generar volante -->
	<p align="center">
		<button type="button" name="btnReconfeccionar" class="boton" onclick="submitForm('generarVolante', '');">
	  	    <bean:message bundle="gde" key="gde.liqVolantePagoIntRSAdapter.button.generar"/>
		</button>
	</p>
	<!--  FIN boton generar volante -->
			
	<logic:notEqual name="userSession" property="isAnonimoView" value="1">
		<!-- Volver -->
		<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '');">
	  	    <bean:message bundle="base" key="abm.button.volver"/>
		</button>
	</logic:notEqual>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin formulario -->