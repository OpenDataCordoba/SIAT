<!-- Paso 2: Asignacion de procuradores -->

<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida.pasoActualView" value="2">
  <fieldset>
    <legend><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.asignarProcuradores.title"/>
    - <bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.tipProMas.desTipProMas" /></legend>
</logic:equal>
<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida.pasoActualView" value="2">
  <fieldset style="background-color:#eeeeee">
    <legend style="background-color:#eeeeee"><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.asignarProcuradores.title"/>
    - <bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.tipProMas.desTipProMas" /></legend>
</logic:notEqual>

<!-- la tabla de exclusion de procuradores para criterios solo la habilitamos si este proceso realiza la asignacion a procuradores -->
<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.criterioProcuradorEnabled" value="true">
  <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
    <caption><bean:message bundle="gde" key="gde.procesoMasivo.listProMasProExc.label"/></caption>
    <tbody>
      <logic:notEmpty  name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.listProMasProExc">	    	
	<tr>
	  <th width="1">&nbsp;</th> <!-- Eliminar -->
	  <th align="left"><bean:message bundle="gde" key="gde.procurador.descripcion.label"/></th>
	  <th align="left"><bean:message bundle="gde" key="gde.procurador.domicilio.label"/></th>
	  <th align="left"><bean:message bundle="gde" key="gde.procurador.telefono.label"/></th>
	  <th align="left"><bean:message bundle="gde" key="gde.procurador.horarios.label"/></th>							
	</tr>
	<logic:iterate id="ProMasProExcVO" name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.listProMasProExc">	
	  <tr>
	    <!-- Eliminar-->								
	    <td>
	      <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="eliminarProMasProExcEnabled" value="enabled">
		<logic:equal name="ProMasProExcVO" property="eliminarEnabled" value="enabled">
		  <a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarProMasProExc', '<bean:write name="ProMasProExcVO" property="id" bundle="base" formatKey="general.format.id"/>');">
		    <img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
		  </a>
		</logic:equal>	
		<logic:notEqual name="ProMasProExcVO" property="eliminarEnabled" value="enabled">
		  <img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
		</logic:notEqual>
	      </logic:equal>
	      <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="eliminarProMasProExcEnabled" value="enabled">
		<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
	      </logic:notEqual>
	    </td>
	    <td><bean:write name="ProMasProExcVO" property="procurador.descripcion" />&nbsp;</td>
	    <td><bean:write name="ProMasProExcVO" property="procurador.domicilio" />&nbsp;</td>
	    <td><bean:write name="ProMasProExcVO" property="procurador.telefono" />&nbsp;</td>
	    <td><bean:write name="ProMasProExcVO" property="procurador.horarioAtencion" />&nbsp;</td>
	  </tr>
	</logic:iterate>
      </logic:notEmpty>
      <logic:empty  name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.listProMasProExc">
	<tr><td align="center"><bean:message bundle="base" key="base.noExistenRegitros"/></td></tr>
      </logic:empty>
      <tr>				
	<td align="right" colspan="20">
	  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="agregarProMasProExcEnabled" value="enabled">
	    <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.agregarProMasProExcBussEnabled" value="true">
	      <html:button property="btnAgregarProMasProExc"  styleClass="boton" onclick="submitForm('agregarProMasProExc','');">
		<bean:message bundle="base" key="abm.button.agregar"/>
	      </html:button>	   	    			
	    </logic:equal>
	    <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.agregarProMasProExcBussEnabled" value="true">
	      <html:button property="btnAgregarProMasProExc"  styleClass="boton" disabled="true"> 
		<bean:message bundle="base" key="abm.button.agregar"/>
	      </html:button>	   	    			
	    </logic:notEqual>
	  </logic:equal>
	  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="agregarProMasProExcEnabled" value="enabled">
	    <html:button property="btnAgregarProMasProExc"  styleClass="boton" disabled="true"> 
	      <bean:message bundle="base" key="abm.button.agregar"/>
	    </html:button>	   	    			
	  </logic:notEqual>
	</td>
      </tr>
    </tbody>
  </table>
</logic:equal>

<!-- Control del Proceso  paso 2 -->
<fieldset>
  <p><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.asignarProcuradoresAdp.legend"/></p>
  <!-- acciones del proceso  -->
  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida.pasoActualView" value="2">
    <!-- Fecha Inicio, Hora Inicio, Estado Corrida y Observacion -->
	<bean:define id="idCorrida" name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida.idView"/>
	<jsp:include page="/pro/AdministrarAdpCorrida.do">
		<jsp:param name="method" value="estadoPaso" />
		<jsp:param name="paso" value="2" />
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

<!-- Reportes para el Perfeccionamiento -->
<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%" style="background-color:#ffffff">
  <caption><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.reportesDeuda.title"/></caption>
  <tbody>
    <tr>
      <th width="1">&nbsp;</th> <!-- ver reportes -->
      <th align="left"><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.reportesDeuda.nombre.label"/></th>
      <th align="left"><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.reportesDeuda.descripcion.label"/></th>
    </tr>
    <tr> <!-- Ver Reportes de deuda a incluir -->
      <td>
	<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="verReportesDeudaEnabled" value="enabled">
	  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.verReportesDeudaIncBussEnabled" value="true">
	    <a style="cursor: pointer; cursor: hand;" onclick="submitForm('verReportesDeudaIncluida', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	      <img title="<bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.button.reportesDeudaIncluir"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
	    </a>
	  </logic:equal>
	  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.verReportesDeudaIncBussEnabled" value="true">
	    <img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
	  </logic:notEqual>
	</logic:equal>
	<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="verReportesDeudaEnabled" value="enabled">
	  <img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
	</logic:notEqual>
      </td>
      <td><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.deudaIncluir.label"/></td>
      <td><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.deudaIncluir.descripcion"/></td>
    </tr>
    <tr> <!-- Ver Reportes de deuda a excluir -->
      <td>
	<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="verReportesDeudaEnabled" value="enabled">
	  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.verReportesDeudaExcBussEnabled" value="true">
	    <a style="cursor: pointer; cursor: hand;" onclick="submitForm('verReportesDeudaExcluida', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
	      <img title="<bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.button.reportesDeudaExcluir"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
	    </a>
	  </logic:equal>
	  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.verReportesDeudaExcBussEnabled" value="true">
	    <img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
	  </logic:notEqual>
	</logic:equal>
	<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="verReportesDeudaEnabled" value="enabled">
	  <img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
	</logic:notEqual>
      </td>
      <td><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.deudaExcluir.label"/></td>
      <td><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.deudaExcluir.descripcion"/></td>
    </tr>

    <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="verPlanillaConvenioCuotaEnviar" value="true">
    	<!-- Ver Reportes de ConvenioCuota a incluir -->
	    <tr> 
	      <td>
		<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="verReportesDeudaEnabled" value="enabled">
		  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.verReportesDeudaIncBussEnabled" value="true">
		    <a style="cursor: pointer; cursor: hand;" onclick="submitForm('verReportesConvenioCuotaIncluida', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
		      <img title="<bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.button.reportesConvenioCuotaIncluir"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
		    </a>
		  </logic:equal>
		  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.verReportesDeudaIncBussEnabled" value="true">
		    <img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
		  </logic:notEqual>
		</logic:equal>
		<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="verReportesDeudaEnabled" value="enabled">
		  <img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
		</logic:notEqual>
	      </td>
	      <td><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.convenioCuotaIncluir.label"/></td>
	      <td><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.convenioCuotaIncluir.descripcion"/></td>
	    </tr>
	    <tr> <!-- Ver Reportes de ConvenioCuota a excluir -->
	      <td>
		<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="verReportesDeudaEnabled" value="enabled">
		  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.verReportesDeudaExcBussEnabled" value="true">
		    <a style="cursor: pointer; cursor: hand;" onclick="submitForm('verReportesConvenioCuotaExcluida', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
		      <img title="<bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.button.reportesConvenioCuotaExcluir"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
		    </a>
		  </logic:equal>
		  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.verReportesDeudaExcBussEnabled" value="true">
		    <img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
		  </logic:notEqual>
		</logic:equal>
		<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="verReportesDeudaEnabled" value="enabled">
		  <img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
		</logic:notEqual>
	      </td>
	      <td><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.convenioCuotaExcluir.label"/></td>
	      <td><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.convenioCuotaExcluir.descripcion"/></td>
	    </tr>

	    <tr> 
	      <td>
		<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="verReportesDeudaEnabled" value="enabled">
		  <logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.verReportesDeudaIncBussEnabled" value="true">
		    <a style="cursor: pointer; cursor: hand;" onclick="submitForm('verReportesCuentaIncluida', '<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.id" bundle="base" formatKey="general.format.id"/>');">
		      <img title="<bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.button.reportesCuentaIncluir"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
		    </a>
		  </logic:equal>
		  <logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.verReportesDeudaIncBussEnabled" value="true">
		    <img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
		  </logic:notEqual>
		</logic:equal>
		<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="verReportesDeudaEnabled" value="enabled">
		  <img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
		</logic:notEqual>
	      </td>
	      <td><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.cuentaIncluir.label"/></td>
	      <td><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.cuentaIncluir.descripcion"/></td>
	    </tr>

     </logic:equal>
    
  </tbody>
</table>
<!-- Fin Reportes para el Perfeccionamiento -->

</fieldset>
