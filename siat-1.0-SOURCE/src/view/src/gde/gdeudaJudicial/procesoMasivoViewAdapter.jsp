<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/gde/AdministrarProcesoMasivo.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.procesoMasivoAdapter.title"/></h1>	
		
		<!-- Envio Judicial -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.procesoMasivo.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.id.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.idView" /></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.tipProMas.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.tipProMas.desTipProMas" />	</td>
				</tr>
			    <!-- ViaDeuda -->
			    <tr>
		    		<td><label><bean:message bundle="gde" key="gde.procesoMasivo.viaDeuda.label"/>: </label></td>
					<td class="normal" colspan="3">	
						<bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.viaDeuda.desViaDeuda" />
					</td>
			    </tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.id.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.idView" />	</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.fechaEnvio.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.fechaEnvioView" />	</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.recurso.desRecurso" />	</td>
				</tr>

				<!-- genera constacia de deudas? -->
				<logic:equal name="procesoMasivoAdapterVO" property="procesoMasivo.generaConstanciaEnabled" value="true">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.generaConstancia.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.generaConstancia.value" /></td>
				</tr>
				</logic:equal>

				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="procesoMasivoAdapterVO" property="procesoMasivo"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.conCuentaExcSel.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.conCuentaExcSel.value" />	</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.observacion.label"/>: </label> </td>
					<td class="normal" colspan="3">	<bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.observacion" /> </td>
				</tr>
				<logic:equal name="procesoMasivoAdapterVO" property="procesoMasivo.criterioProcuradorEnabled" value="true">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.procesoMasivo.utilizaCriterio.label"/>: </label></td>
						<td class="normal"><bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.utilizaCriterio.value" />	</td>
						<td><label><bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
						<td class="normal">	<bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.procurador.descripcion" />	</td>
					</tr>
				</logic:equal>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.usuarioAlta.label"/>: </label></td>
					<td class="normal">	<bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.usuarioAlta" /> </td>
					<td><label><bean:message bundle="pro" key="pro.corrida.usuario.ref"/>: </label></td>
					<td class="normal">	<bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.corrida.usuario" /> </td>
				</tr>								
				<tr>
					<td><label><bean:message bundle="pro" key="pro.estadoCorrida.proceso.label"/>: </label></td>
					<td class="normal" colspan="3">	<bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.corrida.estadoCorrida.desEstadoCorrida" /> </td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.enviadoContr.label"/></label></td>
					<td class="normal" colspan="3"><bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.enviadoContrSiNo" /></td>
				</tr>					
												
			</table>
		</fieldset>	
		<!-- ProcesoMasivo -->
		
		<!-- Si la seleccion de Formularios esta habilitada -->
		<logic:equal name="procesoMasivoAdapterVO" property="procesoMasivo.seleccionFormularioEnabled" value="true">
			<!-- Datos del Reporte -->
			<fieldset >
				<legend><bean:message bundle="gde" key="gde.procesoMasivoAdapter.reportes.title"/></legend>
				<table width="100%">
					<tr>
						<td width="50%" align="right"  style="font-weight: bold;color: #006699;"><bean:message bundle="gde" key="gde.procesoMasivoAdapter.formulario.label"/>:</td>
						<td width="50%"><bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.formulario.desFormulario"/></td>
					</tr>

					<tr>
						<td align="right" style="font-weight: bold;color: #006699;"><bean:message bundle="gde" key="gde.procesoMasivoAdapter.formatoSalida.label"/>:</td>
						<td><bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.formulario.formatoSalida.value"/></td>
					</tr>

					<logic:iterate id="ForCamVO" name="procesoMasivoAdapterVO" property="procesoMasivo.formulario.listForCam">
						<tr>
							<td align="right"  style="font-weight: bold;color: #006699;"><bean:write name="ForCamVO" property="desForCam"/>:</td>
							<td> <bean:write name="ForCamVO" property="valorDefecto"/> </td>
						</tr>
					</logic:iterate>

				</table>
			</fieldset>	
								
		</logic:equal>		

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="procesoMasivoAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
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
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
