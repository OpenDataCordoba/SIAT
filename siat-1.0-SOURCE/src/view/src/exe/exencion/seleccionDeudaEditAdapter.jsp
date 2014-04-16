<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/exe/AdministrarSeleccionDeuda.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="exe" key="exe.cueExeEditAdapter.title"/></h1>
	
	<!-- datos del Objeto Imponible -->
	<logic:greaterThan name="cambioEstadoCueExeAdapterVO" property="cueExe.cuenta.id" value="0">
		<fieldset>
			<legend><bean:message bundle="exe" key="exe.objImp.title"/></legend>		
				<table class="tabladatos">
					<tr>
						<td><label><bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.cuenta.objImp.desClave"/>:</label></td>
						<td class="normal">
							&nbsp;<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.cuenta.objImp.clave"/>
						</td>
					</tr>
					<tr>
						<td><label><bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.cuenta.objImp.desClaveFuncional"/>:</label></td>
						<td class="normal">
							&nbsp;<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.cuenta.objImp.claveFuncional"/>
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
					<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.fechaSolicitudView"/>
				</td>	
			</tr>
			
			<!-- Recurso -->
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.recurso.desRecurso"/>
				</td>	
			</tr>
			
			<!-- Exencion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.exencion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.exencion.desExencion"/>
				</td>
			</tr>
			
			<!-- Cuenta -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.cuenta.numeroCuenta"/>
				</td>
			</tr>
			
			<!-- Tipo Sujeto Solicitante -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.tipoSujeto.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.tipoSujeto.desTipoSujeto"/>
				</td>	
			</tr>
			
			<!-- Fecha Desde / Hasta  -->
			<tr>

				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaDesde.label"/>: </label></td>
				<td class="normal"><bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.fechaDesdeView" /></td>
				
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaHasta.label"/>: </label></td>
				<td class="normal"><bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.fechaHastaView" /></td>
			</tr>
		
			<!-- Solicitante -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.solicitante.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.solicitante.view"/>
				</td>	
			</tr>
			
			<!-- Descripcion Solicitante -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.solicDescripcion.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.solicDescripcion"/></td>			
			</tr>
			<!-- Documentacion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.documentacion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.documentacion"/>
				</td>
			</tr>
			
			<!-- Fecha Resolucion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaResolucion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.fechaResolucionView"/>
				</td>	
			</tr>
			
			<!-- Fecha Desde / Hasta  -->
			<logic:equal name="cambioEstadoCueExeAdapterVO" property="act" value="modificar">
				<tr>
					<td><label><bean:message bundle="exe" key="exe.cueExe.solicFechaDesde.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.solicFechaDesdeView"/>
					</td>
					
					<td><label><bean:message bundle="exe" key="exe.cueExe.solicFechaHasta.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.solicFechaHastaView"/>					
					</td>
				</tr>
			</logic:equal>
			
			<!-- Ordenanza / Articulo -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.ordenanza.label"/>: </label></td>
				<td class="normal"><bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.ordenanza"/></td>			
				<td><label><bean:message bundle="exe" key="exe.cueExe.articulo.label"/>: </label></td>
				<td class="normal"><bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.articulo"/></td>			
			</tr>

			<!-- Inciso -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.inciso.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.inciso"/></td>
			</tr>
			
			<!-- Fecha Resolucion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaResolucion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.fechaResolucionView"/>
				</td>	
			</tr>
			
			<!-- Fecha Ultima Inspeccion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaUltIns.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.fechaUltInsView"/>
				</td>
			</tr>
			
			<!-- Fecha Presentacion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaPresent.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.fechaPresentView"/>
				</td>
			</tr>
						
			<!-- Observaciones -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.observaciones.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.observaciones"/>
				</td>
			</tr>
			
			<!-- Caso -->
			<!-- Inclucion de CasoView -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="cambioEstadoCueExeAdapterVO" property="cueExe"/>
					<%@ include file="/cas/caso/includeCasoView.jsp" %>				
				</td>
			</tr>
			<!-- Fin Inclucion de CasoView -->
			
		</table>
	</fieldset>

<logic:equal name="cambioEstadoCueExeAdapterVO" property="esExencionJubilado" value="true">
	<fieldset>
		<legend><bean:message bundle="exe" key="exe.cueExeAdapter.datosExtraJubilado"/></legend>
		<table class="tabladatos">		
			
			<!--  Nro Beneficiario / Caja -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.nroBeneficiario.label"/>: </label></td>
				<td class="normal"><bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.nroBeneficiario" /></td>			
				<td><label><bean:message bundle="exe" key="exe.cueExe.caja.label"/>: </label></td>
				<td class="normal"><bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.caja"/></td>			
			</tr>
			
			<!-- Clase -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.clase.label"/>: </label></td>
				<td class="normal"><bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.claseView"/></td>			
			</tr>
			
			<!-- Fecha Vencimiento Contrato inquilino  -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaVencContInq.label"/>: </label></td>
				<td class="normal">
					<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.fechaVencContInqView"/>
				</td>
			</tr>
			
		</table>
	</fieldset>		
</logic:equal>	


	<fieldset>
		<legend><bean:message bundle="exe" key="exe.cueExeAdapter.nuevoEstado.title"/></legend>
	
		<table width="100%" class="tabladatos">
			<!-- Estado -->
			<tr>
				<td width="50%"><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td width="50%" class="normal">
					<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.hisEstCueExe.estadoCueExe.desEstadoCueExe"/>
				</td>	
			</tr>
			
			<!-- Observacion de cambio de estado -->
			<tr>
				<td width="50%"><label><bean:message bundle="exe" key="exe.hisEstCueExe.observaciones.label"/>: </label></td>
				<td width="50%" class="normal">
					<bean:write name="cambioEstadoCueExeAdapterVO" property="cueExe.hisEstCueExe.observaciones"/>
				</td>	
			</tr>
		</table>		
	</fieldset>

	   	 
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="exe" key="gde.cueExeAdapter.listDeuda.title"/></caption>
           	<tbody>
               	<tr>
					<th align="center">
						<bean:message bundle="base" key="base.seleccionar.label"/>
						<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				       	<input type="checkbox" onclick="changeChk('filter', 'listIdDeudaSeleccionada', this)"/>
					</th>               		
					<th align="left"><bean:message bundle="gde" key="gde.deuda.periodo.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.deuda.importe.label"/></th>					
					<th align="left"><bean:message bundle="gde" key="gde.deuda.viaDeuda.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.deuda.estado.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.deuda.observacion.label"/></th>
				</tr>
				<logic:iterate id="LiqDeudaVO" name="cambioEstadoCueExeAdapterVO" property="listDeudaASeleccionar">
					<tr>
						<td align="center"> 
							<html:multibox name="cambioEstadoCueExeAdapterVO" property="listIdDeudaSeleccionada" >
                            	<bean:write name="LiqDeudaVO" property="idDeuda" bundle="base" formatKey="general.format.id"/>
                            </html:multibox>
                        </td>
						<td><bean:write name="LiqDeudaVO" property="periodoDeuda"/>&nbsp;</td>
						<td><bean:write name="LiqDeudaVO" property="importe" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>						
						<td><bean:write name="LiqDeudaVO" property="desViaDeuda" />&nbsp;</td>
						<td><bean:write name="LiqDeudaVO" property="desEstado" />&nbsp;</td>
						<td><bean:write name="LiqDeudaVO" property="observacion" />&nbsp;</td>
					</tr>
				</logic:iterate>
			</tbody>
		</table>
			
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
  	    			<logic:equal name="cambioEstadoCueExeAdapterVO" property="cueExe.esCreadaHaLugar" value="false">  	    			
			   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
							<bean:message bundle="base" key="abm.button.volver"/>
						</html:button>	   	    			
  	    			</logic:equal>
  	    			<logic:equal name="cambioEstadoCueExeAdapterVO" property="cueExe.esCreadaHaLugar" value="true">  	    			
			   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverASearchPageCueExe', '');">
							<bean:message bundle="base" key="abm.button.volver"/>
						</html:button>	   	    			
  	    			</logic:equal>  	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('cambiarEstado', '');">
					<bean:message bundle="exe" key="exe.cueExeAdapter.button.cambiarEstado"/>
				</html:button>
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
