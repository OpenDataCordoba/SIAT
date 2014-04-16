<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rec/AdministrarObraPlanillaCuadra.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="rec" key="rec.planillaCuadraViewAdapter.title"/></h1>

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Obra -->
		<fieldset>
			<legend><bean:message bundle="rec" key="rec.obra.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3">
						<bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.obra.recurso.desRecurso"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.obra.numeroObra.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.obra.numeroObraView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.obra.desObra.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.obra.desObra"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="rec" key="rec.obra.permiteCamPlaMay.label"/>: </label></td>
					<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.obra.permiteCamPlaMay.value"/></td>				
				</tr>

				<tr>
					<td><label><bean:message bundle="rec" key="rec.obra.esPorValuacion.label"/>: </label></td>
					<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.obra.esPorValuacion.value"/></td>				
				</tr>

				<tr>	
					<td><label><bean:message bundle="rec" key="rec.obra.esCostoEsp.label"/>: </label></td>
					<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.obra.esCostoEsp.value"/></td>				
					<logic:equal name="planillaCuadraAdapterVO" property="planillaCuadra.obra.costoEspEnabled" value="true">					
						<td><label><bean:message bundle="rec" key="rec.obra.costoEsp.label"/>: </label></td>
						<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.obra.costoEspView"/></td>
					</logic:equal>
				</tr>

				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="planillaCuadraAdapterVO" property="planillaCuadra.obra"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<!-- Fin Inclucion de CasoView -->

				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.obra.estadoObra.desEstadoObra"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- Obra -->

		<!-- PlanillaCuadra -->
		<fieldset>
			<legend><bean:message bundle="rec" key="rec.planillaCuadra.title"/></legend>
			<table class="tabladatos">
				<!-- Recurso -->
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.recurso.desRecurso"/></td>
				</tr>
				<!-- Contrato -->
				<tr>	
					<td><label><bean:message bundle="rec" key="rec.contrato.label"/>: </label></td>
					<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.contrato.descripcion"/></td>				
				</tr>
				<!-- TipoObra -->
				<tr>	
					<td><label><bean:message bundle="rec" key="rec.tipoObra.label"/>: </label></td>
					<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.tipoObra.desTipoObra"/></td>				
				</tr>
				<tr>
					<!-- Fecha -->
					<td><label><bean:message bundle="rec" key="rec.planillaCuadra.fechaCarga.label"/>: </label></td>
					<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.fechaCargaView"/></td>				
				</tr>
				<tr>
					<!-- Descripcion -->
					<td><label><bean:message bundle="rec" key="rec.planillaCuadra.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.descripcion"/></td>				
				</tr>
				<tr>
					<!-- costo cuadra -->
					<td><label><bean:message bundle="rec" key="rec.planillaCuadra.costoCuadra.label"/>: </label></td>
					<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.costoCuadraView"/></td>				
				</tr>
	
				<tr>
					<!-- Calle Principal -->
					<td><label><bean:message bundle="rec" key="rec.planillaCuadra.callePpal.label"/>: </label></td>
					<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.callePpal.nombreCalle"/></td>
				</tr>
				<tr>
					<!-- Calle Desde -->
					<td><label><bean:message bundle="rec" key="rec.planillaCuadra.calleDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.calleDesde.nombreCalle"/></td>				
				</tr>
				<tr>
					<!-- Calle Hasta -->
					<td><label><bean:message bundle="rec" key="rec.planillaCuadra.calleHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.calleHasta.nombreCalle"/></td>				
				</tr>
				<tr>
					<!-- Observacion -->
					<td><label><bean:message bundle="rec" key="rec.planillaCuadra.observacion.label"/>: </label></td>
					<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.observacion"/></td>				
				</tr>
			</table>
		</fieldset>	
		<!-- PlanillaCuadra -->
		
		<logic:equal name="planillaCuadraAdapterVO" property="act" value="cambiarEstado">
			<fieldset>
				<legend><bean:message bundle="rec" key="rec.estPlaCua.title"/></legend>
				<table class="tabladatos">
					<tr>
						<!-- Estado Actual -->
						<td><label><bean:message bundle="rec" key="rec.planillaCuadra.estadoActual.label"/>: </label></td>
						<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.estPlaCua.desEstPlaCua"/></td>				
					</tr>
					<!-- Estado planilla -->
					<tr>	
						<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.estPlaCua.label"/>: </label></td>
						<td class="normal">
							<html:select name="planillaCuadraAdapterVO" property="planillaCuadra.estPlaCua.id" styleClass="select">
								<html:optionsCollection name="planillaCuadraAdapterVO" property="listEstPlaCua" label="desEstPlaCua" value="id" />
							</html:select>
						</td>					
					</tr>
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
					<logic:equal name="planillaCuadraAdapterVO" property="act" value="cambiarEstado">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cambiarEstado', '');">
							<bean:message bundle="rec" key="rec.planillaCuadraViewAdapter.button.cambiarEstado"/>
						</html:button>
					</logic:equal>
					<logic:equal name="planillaCuadraAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="planillaCuadraAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="planillaCuadraAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
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