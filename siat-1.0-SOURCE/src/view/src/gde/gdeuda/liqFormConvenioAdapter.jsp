<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqFormConvenio.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>	
	
	
	<h1><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>Permite realizar la formalización de un Plan de Pago para un Contribuyente, a partir de los Registros de Deuda seleccionados.</p>
				<p>Selección de la Fecha Probable de Formalización.</p>
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
	
	<!-- Dias disponibles -->
		<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.listaFechas.title"/></legend>
			<p><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.listaFechas.legend"/></p>
			<logic:notEmpty name="liqFormConvenioAdapterVO" property="listFechasFormView">				
				<table border="0" width="100%">
		      		<tr>
						<td width="100%" align="center">
							<html:select name="liqFormConvenioAdapterVO" property="fechaFormSelected" styleClass="select" >
								<logic:iterate id="fecha" name="liqFormConvenioAdapterVO" property="listFechasFormView">			
										<option value="<bean:write name="fecha"/>">
											<bean:write name="fecha"/>
										</option>	
								</logic:iterate>
							</html:select>
						</td>
					</tr>
				</table>
			</logic:notEmpty>
			<logic:empty name="liqFormConvenioAdapterVO" property="listFechasFormView">
				<p><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.listaFechas.vacia"/></p>				
			</logic:empty>
		</fieldset>
	<!-- Fin Dias disponibles -->
	
	<!--  boton Seleccionar Plan -->
	<logic:notEmpty name="liqFormConvenioAdapterVO" property="listFechasFormView">			
		<p align="right">
			<button type="button" name="btnFormConvenioar" class="boton" onclick="submitForm('seleccionarPlan', '');">
		  	    <bean:message bundle="gde" key="gde.liqFormConvenioAdapter.button.seleccionarPlan"/>
			</button>
		</p>
	</logic:notEmpty>
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

</html:form>
<!-- Fin formulario -->