<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/gde/AdministrarDeudaExcProMasArmadoSeleccion.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.deudaProMasArmadoSeleccionAdapter.title"/></h1>	
		
		<!-- Envio Judicial -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.procesoMasivo.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.fechaEnvio.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="deudaProMasArmadoSeleccionAdapterVO" property="procesoMasivo.fechaEnvioView" />	</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="deudaProMasArmadoSeleccionAdapterVO" property="procesoMasivo.recurso.desRecurso" />	</td>
				</tr>
				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="deudaProMasArmadoSeleccionAdapterVO" property="procesoMasivo"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.observacion.label"/>: </label> </td>
					<td class="normal" colspan="3">	<bean:write name="deudaProMasArmadoSeleccionAdapterVO" property="procesoMasivo.observacion" /> </td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.conCuentaExcSel.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="deudaProMasArmadoSeleccionAdapterVO" property="procesoMasivo.conCuentaExcSel.value" />	</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.utilizaCriterio.label"/>: </label></td>
					<td class="normal"><bean:write name="deudaProMasArmadoSeleccionAdapterVO" property="procesoMasivo.utilizaCriterio.value" />	</td>
				</tr>
			</table>
		</fieldset>	
		<!-- Fin Envio Judicial -->
		
		<!-- Logs de la seleccion de deuda excluida -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.deudaProMasArmadoSeleccionAdapter.logsSeleccionDeudaExcluir.title"/></legend>
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:message bundle="gde" key="gde.deudaProMasArmadoSeleccionAdapter.detalleLog.title"/></caption>
	           	<tbody>
	              	<tr>
						<th align="left"><bean:message bundle="gde" key="gde.selAlmLog.fechaHora.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.accionLog.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.selAlmLog.usuario.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.selAlmLog.detalleLog.label"/></th>
					</tr>
					<logic:iterate id="SelAlmLogVO" name="deudaProMasArmadoSeleccionAdapterVO" property="procesoMasivo.selAlmExc.listSelAlmLog">
						<tr>
							<td><bean:write name="SelAlmLogVO" property="fechaUltMdfView"/>&nbsp;</td>
							<td><bean:write name="SelAlmLogVO" property="accionLog.desAccionLog"/>&nbsp;</td>
							<td><bean:write name="SelAlmLogVO" property="usuario"/>&nbsp;</td>
							<td><bean:write name="SelAlmLogVO" property="detalleLog"/>&nbsp;</td>
						</tr>
					</logic:iterate>
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
	   	    	<logic:equal name="deudaProMasArmadoSeleccionAdapterVO" property="esLimpiarSeleccion" value="true">
	   	    		<td align="left">
			   	    	<html:button property="btnLimpiarSeleccion"  styleClass="boton" onclick="submitForm('limpiarSeleccion', '');">
							<bean:message bundle="gde" key="gde.deudaProMasArmadoSeleccionAdapter.button.limpiarSeleccion"/>
						</html:button>	   	    			
		   	    	</td>

	   	    	</logic:equal>
	   	    	
	   	    </tr>
	   	 </table>
	   	 		
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>


		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
