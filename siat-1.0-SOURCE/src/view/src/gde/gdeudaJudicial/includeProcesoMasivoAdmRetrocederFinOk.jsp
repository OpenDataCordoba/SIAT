<!-- Paso 4: Fin Ok - Retroceder Paso -->

<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida.pasoActualView" value="4">
  <fieldset>
    <legend><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.finOk.title"/> - <bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.tipProMas.desTipProMas" /></legend>
</logic:equal>

<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida.pasoActualView" value="4">
  <fieldset style="background-color:#eeeeee">
    <legend style="background-color:#eeeeee"><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.finOk.title"/> - <bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.tipProMas.desTipProMas" /></legend>
</logic:notEqual>


<!-- Control del Proceso  paso 4 -->
<fieldset>

  <p><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.finOk.legend"/></p>

  <!-- acciones del proceso  -->
  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida.pasoActualView" value="4">

    <!-- Fecha Inicio, Hora Inicio, Estado Corrida y Observacion -->
	<bean:define id="idCorrida" name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida.idView"/>
	<jsp:include page="/pro/AdministrarAdpCorrida.do">
		<jsp:param name="method" value="estadoPaso" />
		<jsp:param name="paso" value="4" />
		<jsp:param name="id" value="<%= idCorrida %>" />
	</jsp:include>

    <table class="tablabotones" width="100%">
      <tr>
		<td align="left">
	  	  <input name="btnAccionBase" value="Retroceder Paso" onclick="submitForm('retroceder', '');" class="boton" type="button">&nbsp;&nbsp;
		</td>
      </tr>
	</table>      
    <table class="tablabotones" width="100%">
      <tr>
		<td align="left">
		  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.enviadoContr" value="1">
		    <input type="hidden" name="enviadoContr" value="0"/>
	        <input name="btnAccionBase" value="Cambiar a No enviado" onclick="submitForm('enviadoContr', '');" class="boton" type="button"/>
	        <label><bean:message bundle="gde" key="gde.procesoMasivo.enviadoContr.label"/></label>:<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.enviadoContrSiNo" />
	      </logic:equal>
		  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.enviadoContr" value="1">
		    <input type="hidden" name="enviadoContr" value="1"/>
	        <input name="btnAccionBase" value="Cambiar a enviado" onclick="submitForm('enviadoContr', '');" class="boton" type="button"/>
	        <label><bean:message bundle="gde" key="gde.procesoMasivo.enviadoContr.label"/></label>:<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.enviadoContrSiNo" />
	      </logic:notEqual>
	        
		</td>
      </tr>
    </table>
  </logic:equal>
</fieldset>