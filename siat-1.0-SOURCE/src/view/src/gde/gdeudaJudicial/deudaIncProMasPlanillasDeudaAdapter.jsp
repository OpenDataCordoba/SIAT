<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/gde/AdministrarDeudaIncProMasPlanillasDeuda.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.deudaProMasPlanillasDeudaAdapter.title"/></h1>	
		
		<!-- Envio Judicial -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.procesoMasivo.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.fechaEnvio.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="deudaProMasPlanillasDeudaAdapterVO" property="procesoMasivo.fechaEnvioView" />	</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="deudaProMasPlanillasDeudaAdapterVO" property="procesoMasivo.recurso.desRecurso" />	</td>
				</tr>
				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="deudaProMasPlanillasDeudaAdapterVO" property="procesoMasivo"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.observacion.label"/>: </label> </td>
					<td class="normal" colspan="3">	<bean:write name="deudaProMasPlanillasDeudaAdapterVO" property="procesoMasivo.observacion" /> </td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.conCuentaExcSel.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="deudaProMasPlanillasDeudaAdapterVO" property="procesoMasivo.conCuentaExcSel.value" />	</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.utilizaCriterio.label"/>: </label></td>
					<td class="normal"><bean:write name="deudaProMasPlanillasDeudaAdapterVO" property="procesoMasivo.utilizaCriterio.value" />	</td>
				</tr>
			</table>
		</fieldset>	
		<!-- Fin Envio Judicial -->
		
		<!-- Planillas de Deuda y ConvenioCuota de la seleccion de deuda incluida. -->
		<fieldset>

		      <logic:equal name="deudaProMasPlanillasDeudaAdapterVO" property="deudas" value="true">
					<legend><bean:message bundle="gde" key="gde.deudaProMasPlanillasDeudaAdapter.planillasDeudaDeudaIncluir.title"/></legend>
		      </logic:equal>
		      <logic:notEqual name="deudaProMasPlanillasDeudaAdapterVO" property="deudas" value="true">
					<legend><bean:message bundle="gde" key="gde.deudaProMasPlanillasDeudaAdapter.planillasConvenioCuotaIncluir.title"/></legend>
		      </logic:notEqual>

			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:message bundle="gde" key="gde.deudaProMasPlanillasDeudaAdapter.planillasDeuda.title"/></caption>
	           	<tbody>
					<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th align="left"><bean:message bundle="gde" key="gde.deudaProMasPlanillasDeudaAdapter.fileName.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.deudaProMasPlanillasDeudaAdapter.ctdResultado.label"/></th>
					</tr>
					<logic:iterate id="PlanillaVO" name="deudaProMasPlanillasDeudaAdapterVO" property="procesoMasivo.selAlmInc.listPlanilla">
						<tr>
							<!-- Ver -->
							<td>	
								<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('verPlanilla', '<bean:write name="PlanillaVO" property="fileName" />');">
									<img title="<bean:message bundle="gde" key="gde.deudaProMasPlanillasDeudaAdapter.button.verPlanilla"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
								</a>
							</td>
							<td><bean:write name="PlanillaVO" property="fileNameView"/>&nbsp;</td>
							<td><bean:write name="PlanillaVO" property="ctdResultadosView" />&nbsp;</td>
						</tr>
					</logic:iterate>
					<!-- Ctd Total de resultados -->
					<tr>
						<td> &nbsp;</td>
						<td>Cantidad total de registros del resultado</td>
						<td><bean:write name="deudaProMasPlanillasDeudaAdapterVO" property="procesoMasivo.selAlmInc.ctdTotalRegistrosView" /></td>
					</tr>
				</tbody>
			</table>
		</fieldset>
		<!-- Fin Logs de la seleccion de deuda incluida -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	 		
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="fileParam" value=""/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
