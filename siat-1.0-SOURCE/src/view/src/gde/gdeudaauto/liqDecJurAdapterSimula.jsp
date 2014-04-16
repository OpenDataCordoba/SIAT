<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript">
   	
</script>
    
<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqDecJur.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>	
	
	
	<h1><bean:message bundle="gde" key="gde.liqDecJurAdapterInit.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="gde" key="gde.liqDecJurAdapter.general.legend"/></p>
			</td>				
		</tr>
	</table>

	<fieldset>
		<legend>
			<bean:message bundle="gde" key="gde.liqDeudaAdapter.cuentaSeleccionada"/>: 
			<bean:write name="liqDecJurAdapterVO" property="cuenta.nroCuenta"/>
		</legend>
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuenta.desRecurso.label"/>:</label>
	      		<bean:write name="liqDecJurAdapterVO" property="cuenta.desRecurso"/>
			</p>
	</fieldset>
	
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqDecJurAdapter.parametros.title"/> </legend>
		
		<table border="0" width="100%">
      		<tr>
				<td width="100%" align="center">
					<label><bean:message bundle="gde" key="gde.decJurDetAdapter.fechaFormalizacion.label"/>: </label>
					
					<html:text name="liqDecJurAdapterVO" property="fechaFormalizacionView" styleId="fechaFormalizacionView" size="10" maxlength="10" styleClass="datos"/>
					<a class="link_siat" onclick="return show_calendar('fechaFormalizacionView');" id="a_fechaFormalizacionView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				
					&nbsp;
				
					<html:button property="btnSimular"  styleClass="boton" onclick="submitForm('simularAFecha', '');">
						<bean:message bundle="gde" key="gde.liqDecJurAdapter.simular"/>
					</html:button>
	   	    	
				</td>
			</tr>
		</table>
	</fieldset>
	
	
	<!-- listDeuda -->
	<logic:notEmpty name="liqDecJurAdapterVO" property="listDeudaSimulada">
		<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.liqDecJurAdapter.listaDeudaSimulada.title"/></caption>
	      	<tbody>
		       	<tr>
		       		<th align="left">&nbsp;</th>		       		
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.liqDeuda.periodoDeuda"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.liqDeuda.fechaVto"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.liqDeuda.importe"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.liqDeuda.saldo"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.liqDeuda.actualizacion"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.liqDeuda.total"/></th>
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqDeudaVO" name="liqDecJurAdapterVO" property="listDeudaSimulada">
					<tr>
			  			<logic:notEmpty name="LiqDeudaVO" property="desEstado">
							<td><b><bean:write name="LiqDeudaVO" property="desEstado"/></b></td>
						</logic:notEmpty>
						<logic:empty name="LiqDeudaVO" property="desEstado">
							<td>&nbsp;</td>		  			
						</logic:empty>			  			
			  			<td><bean:write name="LiqDeudaVO" property="periodoDeuda"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaVO" property="fechaVto"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaVO" property="importe" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaVO" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaVO" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					    <td><bean:write name="LiqDeudaVO" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
			       	</tr>
				</logic:iterate>
				<!-- Fin Item LiqDeudaVO -->
		       	<tr>       		
		       		<td colspan="3" align="right">
			        	<bean:message bundle="gde" key="gde.liqDecJurAdapter.liqDeuda.total"/>: 
		       		</td>
		       		<td>
			        	<b><bean:write name="liqDecJurAdapterVO" property="totalImporte" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
			        <td>
			        	<b><bean:write name="liqDecJurAdapterVO" property="totalSaldo" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       		<td>
			        	<b><bean:write name="liqDecJurAdapterVO" property="totalActualizacion" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
			        <td>
			        	<b><bean:write name="liqDecJurAdapterVO" property="total" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>
	      	</tbody>
		</table>
		</div>
	</logic:notEmpty>
	<!-- Fin listDeuda -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="33%">
				<!-- Volver -->
				<button type="button" name="btnAtras" class="boton" onclick="submitForm('volverGeneral', '');">
			  	    <bean:message bundle="gde" key="gde.liqDecJurAdapter.atras"/>
				</button>
   	    	</td>
   	    	<td align="center" width="33%">
   	    		<html:button property="btnSiguiente"  styleClass="boton" onclick="submitForm('imprimirSimulacion', '');">
					<bean:message bundle="gde" key="gde.liqDecJurAdapter.imprimir"/>
				</html:button>
   	    	</td>
   	    	<td align="right" width="33 %">
   	    		<logic:equal name="liqDecJurAdapterVO" property="generarDeudaEnabled" value="true">
					<html:button property="btnGenerar"  styleClass="boton" onclick="submitConfirmForm('generar', '', 'Usted va a generar deuda para la cuenta seleccionada, Desea continuar?');">
						<bean:message bundle="gde" key="gde.liqDecJurAdapter.generar"/>
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
<!-- Fin formulario -->