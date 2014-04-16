<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida.pasoActualView" value="1">
  <fieldset>
    <legend><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.seleccionDeuda.title"/> - <bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.tipProMas.desTipProMas"/></legend>
</logic:equal>
<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida.pasoActualView" value="1">
  <fieldset style="background-color:#eeeeee">
    <legend style="background-color:#eeeeee"><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.seleccionDeuda.title"/> - <bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.tipProMas.desTipProMas"/></legend>
</logic:notEqual>

<!-- Definir Deuda a Incluir -->
<fieldset>
  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="verPlanillaConvenioCuotaEnviar" value="true">
	  <legend><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.seleccionDeudaConvenioCuotaIncluir.title"/></legend>
  </logic:equal>
  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="verPlanillaConvenioCuotaEnviar" value="true">
	  <legend><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.seleccionDeudaIncluir.title"/></legend>
  </logic:notEqual>

  <table>
    <!-- parametros de seleccion -->
    <tr>				
      <td>
	<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="seleccionarDeudaEnviarEnabled" value="enabled">
	  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmInc.seleccionarDeudaEnviarBussEnabled" value="true">
	    <button type="button" class="boton" onclick="submitForm('seleccionarDeudaEnviarIncluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.seleccionDeudaIncluir"/>
	    </button>
	  </logic:equal>
	  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmInc.seleccionarDeudaEnviarBussEnabled" value="true">
	    <button disabled="true" type="button" class="boton" onclick="submitForm('seleccionarDeudaEnviarIncluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.seleccionDeudaIncluir"/>
	    </button>
	  </logic:notEqual>
	</logic:equal>
	<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="seleccionarDeudaEnviarEnabled" value="enabled">
	  <button disabled="true" type="button" class="boton" onclick="submitForm('seleccionarDeudaEnviarIncluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	    <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.seleccionDeudaIncluir"/>
	  </button>
	</logic:notEqual>
      </td>
      <td><ul class="vinieta"><li><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.msg.seleccionDeudaIncluir"/></li></ul></td>
    </tr>
    
    <!-- Eliminar DeudaEnviar Incluir -->
    <tr>
      <td>
	<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="eliminarDeudaEnviarEnabled" value="enabled">
	  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmInc.eliminarDeudaEnviarBussEnabled" value="true">
	    <button type="button" class="boton" onclick="submitForm('eliminarDeudaEnviarIncluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.eliminarDeudaIncluir"/>
	    </button>
	  </logic:equal>
	  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmInc.eliminarDeudaEnviarBussEnabled" value="true">
	    <button disabled="true" type="button" class="boton" onclick="submitForm('eliminarDeudaEnviarIncluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.eliminarDeudaIncluir"/>
	    </button>
	  </logic:notEqual>
	</logic:equal>
	<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="eliminarDeudaEnviarEnabled" value="enabled">
	  <button disabled="true" type="button" class="boton" onclick="submitForm('eliminarDeudaEnviarIncluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	    <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.eliminarDeudaIncluir"/>
	  </button>
	</logic:notEqual>
      </td>
      <td><ul class="vinieta"><li><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.msg.eliminarDeudaIncluir"/></li></ul></td>
    </tr>	

    <!-- Limpiar Seleccion Incluir -->
    <tr>
      <td>
	<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="limpiarSeleccionDeudaEnviarEnabled" value="enabled">
	  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmInc.limpiarSeleccionDeudaEnviarBussEnabled" value="true">
	    <button type="button" class="boton" onclick="submitForm('limpiarSeleccionDeudaEnviarIncluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.limpiarSeleccionDeudaIncluir"/>
	    </button>
	  </logic:equal>
	  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmInc.limpiarSeleccionDeudaEnviarBussEnabled" value="true">
	    <button disabled="true" type="button" class="boton" onclick="submitForm('limpiarSeleccionDeudaEnviarIncluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.limpiarSeleccionDeudaIncluir"/>
	    </button>
	  </logic:notEqual>
	</logic:equal>
	<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="limpiarSeleccionDeudaEnviarEnabled" value="enabled">
	  <button disabled="true" type="button" class="boton" onclick="submitForm('limpiarSeleccionDeudaEnviarIncluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	    <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.limpiarSeleccionDeudaIncluir"/>
	  </button>
	</logic:notEqual>
      </td>
      <td><ul class="vinieta"><li><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.msg.limpiarSeleccionDeudaIncluir"/></li></ul></td>
    </tr>	

  </table>
</fieldset>

<!-- Controlar para proceso de incluir deuda en selalm ADP -->
<fieldset>				
  <!-- acciones del proceso  -->
  <p><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.seleccionDeudaAdpControl.legend"/></p>

  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida.pasoActualView" value="1">
    <!-- Fecha Inicio, Hora Inicio, Estado Corrida y Observacion -->
	<bean:define id="idCorrida" name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida.idView"/>
	<jsp:include page="/pro/AdministrarAdpCorrida.do">
		<jsp:param name="method" value="estadoPaso" />
		<jsp:param name="paso" value="1" />
		<jsp:param name="id" value="<%= idCorrida %>" />
	</jsp:include>

    <table class="tablabotones" width="100%">
      <tr>
		<td align="center">
		  <bean:define id="corridaVO" name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida"/>
		  <%@ include file="/pro/adpProceso/includeAdpBotones.jsp" %>
		</td>
      </tr>
    </table>
  </logic:equal>
</fieldset>

<!-- Ver Listas de deuda incluida -->
<fieldset style="background:#ffffff">
  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="verPlanillaConvenioCuotaEnviar" value="true">
	  <p><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.listasDeudaCuotaIncluir.legend"/></p>
  </logic:equal>
  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="verPlanillaConvenioCuotaEnviar" value="true">
	  <p><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.listasDeudaIncluir.legend"/></p>
  </logic:notEqual>
  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="verSeleccionAlmacenada" value="true">
	  <table width="100%" align="center">
	    <tr>
	      <td> <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.deudas.label"/>: </td>
	      <td>
	      	<label><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.cantidadRegistros.label"/>: </label>
			<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmInc.cantidadDeudasView"/>
	      </td>
	      <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="visualizarTotalesDeuda" value="true">
		      <td>
		      	<label><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.importeHistoricoTotal.label"/>: </label>
				<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmInc.importeHistoricoDeudaView"/>
		      </td>
		      <td>
		      	<label><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.cantidadCuentas.label"/>: </label> 
				<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmInc.cantidadCuentasDeudaView"/>
		      </td>
	      </logic:equal>
	    </tr>
	    
	      <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="verPlanillaConvenioCuotaEnviar" value="true">
		    <tr>
		      <td>
		      	<bean:message bundle="gde" key="gde.selAlmDeudaAdapter.cuotasConvenios.label"/>:
		      </td>
		      <td>
		      	<label><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.cantidadRegistros.label"/>: </label>
				<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmInc.cantidadCuotasConvenioView"/>
		      </td>
		      <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="visualizarTotalesDeuda" value="true">
			      <td>
			      	<label><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.importeHistoricoTotal.label"/>: </label>
					<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmInc.importeHistoricoCuotaConvenioView"/>
			      </td>
			      <td>
			      	<label><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.cantidadCuentas.label"/>: </label> 
					<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmInc.cantidadCuentasCuotaConvenioView"/>
			      </td>
		      </logic:equal>
		    </tr>
		  </logic:equal>
	    
	  </table>
	
	  <table class="tablabotones" width="100%">
	    <tr>
	      <td align="center">
	
		<!-- Planilla DeudaEnviar Incluir -->
		<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="planillaDeudaEnviarEnabled" value="enabled">
		  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmInc.planillaDeudaEnviarBussEnabled" value="true">
		    <button type="button" class="boton" onclick="submitForm('planillasDeudaEnviarIncluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
		      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.planillasDeudaIncluir"/>
		    </button>
		    <button type="button" class="boton" onclick="submitForm('planillasCuentaEnviarIncluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
		      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.planillasCuentaIncluir"/>
		    </button>
		  </logic:equal>
		  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmInc.planillaDeudaEnviarBussEnabled" value="true">
		    <button disabled="true" type="button" class="boton" >
		      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.planillasDeudaIncluir"/>
		    </button>
		  </logic:notEqual>
		</logic:equal>
		<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="planillaDeudaEnviarEnabled" value="enabled">
		  <button disabled="true" type="button" class="boton" >
		    <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.planillasDeudaIncluir"/>
		  </button>
		</logic:notEqual>
		
		<!-- Consulta DeudaEnviar Incluir -->
		<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="consultarDeudaEnviarEnabled" value="enabled">
		  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmInc.consultarDeudaEnviarBussEnabled" value="true">
		    <button type="button" class="boton" onclick="submitForm('consultarDeudaEnviarIncluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
		      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.consultarDeudaIncluir"/>
		    </button>
		  </logic:equal>
		  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmInc.consultarDeudaEnviarBussEnabled" value="true">
		    <button disabled="true" type="button" class="boton" >
		      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.consultarDeudaIncluir"/>
		    </button>
		  </logic:notEqual>
		</logic:equal>
		<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="consultarDeudaEnviarEnabled" value="enabled">
		  <button disabled="true" type="button" class="boton" >
		    <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.consultarDeudaIncluir"/>
		  </button>
		</logic:notEqual>
	
	    <!-- Planilla ConvenioCuotaEnviar Incluir -->
		<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="verPlanillaConvenioCuotaEnviar" value="true">
			<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="planillaConvenioCuotaEnviarEnabled" value="enabled">
			  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmInc.planillaConvenioCuotaEnviarBussEnabled" value="true">
			    <button type="button" class="boton" onclick="submitForm('planillasConvenioCuotaEnviarIncluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
			      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.planillasConvenioCuotaIncluir"/>
			    </button>
			  </logic:equal>
			  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmInc.planillaConvenioCuotaEnviarBussEnabled" value="true">
			    <button disabled="true" type="button" class="boton" >
			      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.planillasConvenioCuotaIncluir"/>
			    </button>
			  </logic:notEqual>
			</logic:equal>
			<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="planillaConvenioCuotaEnviarEnabled" value="enabled">
			  <button disabled="true" type="button" class="boton" >
			    <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.planillasConvenioCuotaIncluir"/>
			  </button>
			</logic:notEqual>
		</logic:equal>
		
		<!-- Ver Totales de Deuda -->
		<html:button property="btnVerTotalesDeuda"  styleClass="boton" onclick="submitForm('verTotalesDeuda', 'locate_verTotal_inc');" disabled='false' styleId="locate_verTotal_inc">
			<bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.verTotales"/>
		</html:button>	 
	   
	   <!--  Logs de Armado -->
	   	<button type="button" class="boton" onclick="submitForm('logsArmadoDeudaEnviarIncluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	  		<bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.logsArmadoDeudaIncluir"/>
		</button>
		 
	      </td>
	    </tr>				
	  </table>
  </logic:equal>
  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="verSeleccionAlmacenada" value="true">
	  <table class="tablabotones" width="100%">
	    <tr>
	      <td align="center">
			   <!--  Logs de Armado -->
			   	<button type="button" class="boton" onclick="submitForm('logsArmadoDeudaEnviarIncluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
			  		<bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.logsArmadoDeudaIncluir"/>
				</button>
	      </td>
	    </tr>				
	  </table>  
  </logic:notEqual>

</fieldset>

<br/>

<!-- Definir Deuda a Excluir -->
<fieldset>
  <legend><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.seleccionDeudaExcluir.title"/></legend>

  <table>
    
    <!-- Seleccionar Deuda Excluir -->
    <tr>
      <td>
	<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="seleccionarDeudaEnviarEnabled" value="enabled">
	  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmExc.seleccionarDeudaEnviarBussEnabled" value="true">
	    <button type="button" class="boton" onclick="submitForm('seleccionarDeudaEnviarExcluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.seleccionDeudaExcluir"/>
	    </button>
	  </logic:equal>
	  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmExc.seleccionarDeudaEnviarBussEnabled" value="true">
	    <button disabled="true" type="button" class="boton" onclick="submitForm('seleccionarDeudaEnviarExcluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.seleccionDeudaExcluir"/>
	    </button>
	  </logic:notEqual>
	</logic:equal>
	<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="seleccionarDeudaEnviarEnabled" value="enabled">
	  <button disabled="true" type="button" class="boton" onclick="submitForm('seleccionarDeudaEnviarExcluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	    <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.seleccionDeudaExcluir"/>
	  </button>
	</logic:notEqual>
      </td>
      <td><ul class="vinieta"><li><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.msg.seleccionDeudaExcluir"/></li></ul></td>
    </tr>	
    
    <!-- Eliminar DeudaEnviar Excluir -->
    <tr>
      <td>
	<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="eliminarDeudaEnviarEnabled" value="enabled">
	  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmExc.eliminarDeudaEnviarBussEnabled" value="true">
	    <button type="button" class="boton" onclick="submitForm('eliminarDeudaEnviarExcluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.eliminarDeudaExcluir"/>
	    </button>
	  </logic:equal>
	  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmExc.eliminarDeudaEnviarBussEnabled" value="true">
	    <button disabled="true" type="button" class="boton" onclick="submitForm('eliminarDeudaEnviarExcluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.eliminarDeudaExcluir"/>
	    </button>
	  </logic:notEqual>
	</logic:equal>
	<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="eliminarDeudaEnviarEnabled" value="enabled">
	  <button disabled="true" type="button" class="boton" onclick="submitForm('eliminarDeudaEnviarExcluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	    <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.eliminarDeudaExcluir"/>
	  </button>
	</logic:notEqual>
      </td>
      <td><ul class="vinieta"><li><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.msg.eliminarDeudaExcluir"/></li></ul></td>
    </tr>	

    <!-- Limpiar Seleccion Excluir -->
    <tr>
      <td>
	<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="limpiarSeleccionDeudaEnviarEnabled" value="enabled">
	  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmExc.limpiarSeleccionDeudaEnviarBussEnabled" value="true">
	    <button type="button" class="boton" onclick="submitForm('limpiarSeleccionDeudaEnviarExcluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.limpiarSeleccionDeudaExcluir"/>
	    </button>
	  </logic:equal>
	  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmExc.limpiarSeleccionDeudaEnviarBussEnabled" value="true">
	    <button disabled="true" type="button" class="boton" onclick="submitForm('limpiarSeleccionDeudaEnviarExcluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.limpiarSeleccionDeudaExcluir"/>
	    </button>
	  </logic:notEqual>
	</logic:equal>
	<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="limpiarSeleccionDeudaEnviarEnabled" value="enabled">
	  <button disabled="true" type="button" class="boton" onclick="submitForm('limpiarSeleccionDeudaEnviarExcluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	    <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.limpiarSeleccionDeudaExcluir"/>
	  </button>
	</logic:notEqual>
      </td>
      <td><ul class="vinieta"><li><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.msg.limpiarSeleccionDeudaExcluir"/></li></ul></td>
    </tr>

  </table>
</fieldset>

<!-- Ver Listas de deuda excluida -->
<fieldset style="background:#ffffff">
  <!--legend><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.listasDeudaExcluir.title"/></legend-->
  <p><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.listasDeudaExcluir.legend"/></p>
  
  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="verSeleccionAlmacenada" value="true">
	  <table width="100%" align="center">
	    <tr>
	      <td>
	      	<bean:message bundle="gde" key="gde.selAlmDeudaAdapter.deudas.label"/>:
	      </td>
	      <td>
	      	<label><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.cantidadRegistros.label"/>: </label>
			<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmExc.cantidadRegistrosView"/>
	      </td>
	      <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="visualizarTotalesDeuda" value="true">
		      <td>
				<label><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.importeHistoricoTotal.label"/>: </label>
				<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmExc.importeHistoricoTotalView"/>
		      </td>
		      <td>
		      	<label><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.cantidadCuentas.label"/>: </label> 
				<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmExc.cantidadCuentasView"/>
		      </td>
	      </logic:equal>
	    </tr>
	  </table>
	  <table class="tablabotones" width="100%">
	    <tr>
	      <!-- Planilla DeudaEnviar Excluir -->
	      <td align="center">
		<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="planillaDeudaEnviarEnabled" value="enabled">
		  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmExc.planillaDeudaEnviarBussEnabled" value="true">
		    <button type="button" class="boton" onclick="submitForm('planillasDeudaEnviarExcluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
		      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.planillasDeudaExcluir"/>
		    </button>
<!--
		   	<button type="button" class="boton" onclick="submitForm('planillasCuentaEnviarExcluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
		      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.planillasCuentaExcluir"/>
			</button>
-->
		  </logic:equal>
		  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmExc.planillaDeudaEnviarBussEnabled" value="true">
		    <button disabled="true" type="button" class="boton" onclick="submitForm('planillasDeudaEnviarExcluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
		      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.planillasDeudaExcluir"/>
		    </button>
		  </logic:notEqual>
		</logic:equal>
		<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="planillaDeudaEnviarEnabled" value="enabled">
		  <button disabled="true" type="button" class="boton" onclick="submitForm('planillasDeudaEnviarExcluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
		    <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.planillasDeudaExcluir"/>
		  </button>
		</logic:notEqual>
	
		<!-- Consulta DeudaEnviar Excluir -->
		<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="consultarDeudaEnviarEnabled" value="enabled">
		  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmExc.consultarDeudaEnviarBussEnabled" value="true">
		    <button type="button" class="boton" onclick="submitForm('consultarDeudaEnviarExcluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
		      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.consultarDeudaExcluir"/>
		    </button>
		  </logic:equal>
		  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.selAlmExc.consultarDeudaEnviarBussEnabled" value="true">
		    <button disabled="true" type="button" class="boton" onclick="submitForm('consultarDeudaEnviarExcluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
		      <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.consultarDeudaExcluir"/>
		    </button>
		  </logic:notEqual>
		</logic:equal>
		<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="consultarDeudaEnviarEnabled" value="enabled">
		  <button disabled="true" type="button" class="boton" onclick="submitForm('consultarDeudaEnviarExcluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
		    <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.consultarDeudaExcluir"/>
		  </button>
		</logic:notEqual>
		<!-- Ver Totales de Deuda -->
		<html:button property="btnVerTotalesDeuda"  styleClass="boton" onclick="submitForm('verTotalesDeuda', 'locate_verTotal_exc');" disabled='false' styleId="locate_verTotal_exc">
			<bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.verTotales"/>
		</html:button>	  
		<!-- LogsArmado -->
		<button type="button" class="boton" onclick="submitForm('logsArmadoDeudaEnviarExcluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
		  <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.logsArmadoDeudaExcluir"/>
		</button>
		
	    </td></tr>
  	</table>
  </logic:equal>
  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="verSeleccionAlmacenada" value="true">
	  <table class="tablabotones" width="100%">
	    <tr><td align="center">
			<!-- LogsArmado -->
			<button type="button" class="boton" onclick="submitForm('logsArmadoDeudaEnviarExcluir', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
			  <bean:message bundle="gde" key="gde.selAlmDeudaAdapter.buttom.logsArmadoDeudaExcluir"/>
			</button>
	    </td></tr>
  	</table>  	
  </logic:notEqual>
  
</fieldset>
</fieldset>
