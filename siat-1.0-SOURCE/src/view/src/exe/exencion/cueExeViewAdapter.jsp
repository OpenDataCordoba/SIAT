<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/exe/AdministrarCueExe.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="exe" key="exe.cueExeViewAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>

	<!-- datos del Objeto Imponible -->
	<logic:greaterThan name="cueExeAdapterVO" property="cueExe.cuenta.id" value="0">
		<fieldset>
			<legend><bean:message bundle="exe" key="exe.objImp.title"/></legend>		
				<table class="tabladatos">
					<tr>
						<td><label><bean:write name="cueExeAdapterVO" property="cueExe.cuenta.objImp.desClave"/>:</label></td>
						<td class="normal">
							&nbsp;<bean:write name="cueExeAdapterVO" property="cueExe.cuenta.objImp.clave"/>
						</td>
					</tr>
					<tr>
						<td><label><bean:write name="cueExeAdapterVO" property="cueExe.cuenta.objImp.desClaveFuncional"/>:</label></td>
						<td class="normal">
							&nbsp;<bean:write name="cueExeAdapterVO" property="cueExe.cuenta.objImp.claveFuncional"/>
						</td>
					</tr>
				</table>				
		</fieldset>
	</logic:greaterThan>
	
	<!-- CueExe -->
	<fieldset>
		<legend><bean:message bundle="exe" key="exe.cueExe.title"/></legend>
		
		<table class="tabladatos">
			
			<!-- Fecha Solicitud -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaSolicitud.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.fechaSolicitudView"/>
				</td>	
			</tr>
			
			<!-- Recurso -->
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.recurso.desRecurso"/>
				</td>	
			</tr>
			
			<!-- Exencion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.exencion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.exencion.desExencion"/>
				</td>
			</tr>
			
			<!-- Cuenta -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.cuenta.numeroCuenta"/>
				</td>
			</tr>
			
			<!-- Tipo Sujeto Solicitante -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.tipoSujeto.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.tipoSujeto.desTipoSujeto"/>
				</td>	
			</tr>
			
			<!-- Fecha Desde / Hasta  -->
			<tr>

				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaDesde.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeAdapterVO" property="cueExe.fechaDesdeView" /></td>
				
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaHasta.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeAdapterVO" property="cueExe.fechaHastaView" /></td>
			</tr>
		
			<!-- Solicitante -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.solicitante.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.solicitante.represent"/>
				</td>	
			</tr>
			
			<!-- Descripcion Solicitante -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.solicDescripcion.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cueExeAdapterVO" property="cueExe.solicDescripcion"/></td>			
			</tr>
			
			<!-- Tipo Documento -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.tipoDocumento.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cueExeAdapterVO" property="cueExe.tipoDocumento"/></td>			
			</tr>
			
			<!-- Nro Documento -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.nroDocumento.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cueExeAdapterVO" property="cueExe.nroDocumento"/></td>			
			</tr>
						
			<!-- Fecha Resolucion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaResolucion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.fechaResolucionView"/>
				</td>	
			</tr>
			
			<!-- Fecha Ultima Inspeccion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaUltIns.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.fechaUltInsView"/>
				</td>
			</tr>
			
			<!-- Fecha Presentacion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaPresent.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.fechaPresentView"/>
				</td>
			</tr>
			
			<!-- Documentacion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.documentacion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.documentacion"/>
				</td>
			</tr>
			
			<!-- Ordenanza / Articulo -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.ordenanza.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeAdapterVO" property="cueExe.ordenanza"/></td>			
				<td><label><bean:message bundle="exe" key="exe.cueExe.articulo.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeAdapterVO" property="cueExe.articulo"/></td>			
			</tr>

			<!-- Inciso -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.inciso.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cueExeAdapterVO" property="cueExe.inciso"/></td>
			</tr>
			
			<!-- Observaciones -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.observaciones.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.observaciones"/>
				</td>
			</tr>
			
			<!-- Caso -->
			<!-- Inclucion de CasoView -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="cueExeAdapterVO" property="cueExe"/>
					<%@ include file="/cas/caso/includeCasoView.jsp" %>				
				</td>
			</tr>
			<!-- Fin Inclucion de CasoView -->
			
			<!-- solic Fecha Desde / solic Hasta  -->
			<logic:equal name="cueExeAdapterVO" property="poseeSolicFechas" value="true">
				<tr>
					<td><bean:message bundle="exe" key="exe.cueExe.solicFechaMigracion.msg"/>: </td>
					<td class="normal" colspan="3">
						<bean:message bundle="exe" key="exe.cueExe.solicFechaDesde.label"/>: &nbsp;
						<bean:write name="cueExeAdapterVO" property="cueExe.solicFechaDesdeView"/>
						&nbsp; -
						<bean:message bundle="exe" key="exe.cueExe.solicFechaHasta.label"/>: &nbsp;
						<bean:write name="cueExeAdapterVO" property="cueExe.solicFechaHastaView"/>					
					</td>
				</tr>
			</logic:equal>
		</table>
	</fieldset>

	<logic:equal name="cueExeAdapterVO" property="esExencionJubilado" value="true">
		<fieldset>
			<legend><bean:message bundle="exe" key="exe.cueExeAdapter.datosExtraJubilado"/></legend>
			<table class="tabladatos">		
				
				<!--  Nro Beneficiario / Caja -->
				<tr>
					<td><label><bean:message bundle="exe" key="exe.cueExe.nroBeneficiario.label"/>: </label></td>
					<td class="normal"><bean:write name="cueExeAdapterVO" property="cueExe.nroBeneficiario" /></td>			
					<td><label><bean:message bundle="exe" key="exe.cueExe.caja.label"/>: </label></td>
					<td class="normal"><bean:write name="cueExeAdapterVO" property="cueExe.caja"/></td>			
				</tr>
				
				<!-- Clase -->
				<tr>
					<td><label><bean:message bundle="exe" key="exe.cueExe.clase.label"/>: </label></td>
					<td class="normal"><bean:write name="cueExeAdapterVO" property="cueExe.claseView"/></td>			
				</tr>
				
				<!-- Fecha Vencimiento Contrato inquilino  -->
				<tr>
					<td><label><bean:message bundle="exe" key="exe.cueExe.fechaVencContInq.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cueExeAdapterVO" property="cueExe.fechaVencContInqView"/>
					</td>
				</tr>
			</table>
		</fieldset>		
	</logic:equal>

	<!-- Estado -->
	<fieldset>
		<legend><bean:message bundle="exe" key="exe.cueExeAdapter.estado.title"/></legend>
	
		<table width="100%" class="tabladatos">
			<!-- Estado -->
			<tr>
				<td width="50%"><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td width="50%" class="normal">
					<bean:write name="cueExeAdapterVO" property="cueExe.estadoCueExe.desEstadoCueExe"/>
				</td>	
			</tr>
		</table>		
	</fieldset>

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left"" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right"" width="50%">
	   	    	   <logic:equal name="cueExeAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					<logic:equal name="cueExeAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="cueExeAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="cueExeAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	<input type="hidden" name="name"  value="<bean:write name='cueExeAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	   	 		
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
