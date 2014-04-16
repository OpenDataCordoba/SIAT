<!-- Paso 3: Realizar Envio -->

<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida.pasoActualView" value="3">
  <fieldset>
    <legend><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.realizarEnvio.title"/> - <bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.tipProMas.desTipProMas" /></legend>
</logic:equal>
<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida.pasoActualView" value="3">
  <fieldset style="background-color:#eeeeee">
    <legend style="background-color:#eeeeee"><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.realizarEnvio.title"/> - <bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.tipProMas.desTipProMas" /></legend>
</logic:notEqual>

<!-- Control del Proceso  paso 3 -->
<fieldset>
  <p><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.realiazarEnvioAdp.legend"/></p>

  <table class="tabladatos">
    <logic:notEmpty name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.pasoCorridaRealizarEnvio">
      <!-- acciones del proceso  -->
	  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida.pasoActualView" value="3">
	    <!-- Fecha Inicio, Hora Inicio, Estado Corrida y Observacion -->
		<bean:define id="idCorrida" name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida.idView"/>
		<jsp:include page="/pro/AdministrarAdpCorrida.do">
			<jsp:param name="method" value="estadoPaso" />
			<jsp:param name="paso" value="3" />
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
    </logic:empty>
  </table>
</fieldset>

<!-- Resultado del Envio -->
<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%" >
  <caption><bean:message bundle="gde" key="gde.procesoMasivo.listFileCorridaRealizarEnvio.label"/></caption>
  <tbody>
    <logic:notEmpty  name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.listFileCorridaRealizarEnvio">	    	
      <tr>
	<th width="1">&nbsp;</th> <!-- ver -->
	<th align="left"><bean:message bundle="pro" key="pro.fileCorrida.nombre.label"/></th>
	<th align="left"><bean:message bundle="pro" key="pro.fileCorrida.observacion.label"/></th>
      </tr>
      <logic:iterate id="FileCorridaVO" name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.listFileCorridaRealizarEnvio">
	<tr>
	  <td>
	    <a style="cursor: pointer; cursor: hand;" onclick="submitForm('verReporteEnvioRealizado', '<bean:write name="FileCorridaVO" property="fileName"/>');">
	      <img title="<bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.button.reporteEnvioRealizado"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
	    </a>
	  </td>
	  <td><bean:write name="FileCorridaVO" property="nombre" />&nbsp;</td>
	  <td><bean:write name="FileCorridaVO" property="observacion" />&nbsp;</td>
	</tr>
      </logic:iterate>
    </logic:notEmpty>
    <logic:empty  name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.listFileCorridaRealizarEnvio">
      <tr><td align="center"><bean:message bundle="base" key="base.noExistenRegitros"/></td></tr>
    </logic:empty>
  </tbody>
</table>
<!-- Fin Resultado del Envio -->

</fieldset>	
