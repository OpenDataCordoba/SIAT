<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/exe/AdministrarCueExeConviv.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="exe" key="exe.cueExeConvivAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<logic:equal name="cueExeConvivAdapterVO" property="act" value="eliminar">
  	   				<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volverAVer', '');">
		   	    		<bean:message bundle="base" key="abm.button.volver"/>
		   	    	</html:button>
  	   			</logic:equal>
  	   			<logic:notEqual name="cueExeConvivAdapterVO" property="act" value="eliminar">
		   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
		   	    		<bean:message bundle="base" key="abm.button.volver"/>
		   	    	</html:button>
		   	    </logic:notEqual>
			</td>
		</tr>
	</table>
	
	<!-- datos del Objeto Imponible -->
	<logic:greaterThan name="cueExeConvivAdapterVO" property="cueExe.cuenta.id" value="0">
		<fieldset>
			<legend><bean:message bundle="exe" key="exe.objImp.title"/></legend>		
				<table class="tabladatos">
					<tr>
						<td><label><bean:write name="cueExeConvivAdapterVO" property="cueExe.cuenta.objImp.desClave"/>:</label></td>
						<td class="normal">
							&nbsp;<bean:write name="cueExeConvivAdapterVO" property="cueExe.cuenta.objImp.clave"/>
						</td>
					</tr>
					<tr>
						<td><label><bean:write name="cueExeConvivAdapterVO" property="cueExe.cuenta.objImp.desClaveFuncional"/>:</label></td>
						<td class="normal">
							&nbsp;<bean:write name="cueExeConvivAdapterVO" property="cueExe.cuenta.objImp.claveFuncional"/>
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
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.fechaSolicitudView"/>
				</td>	
			</tr>
			
			<!-- Recurso -->
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.recurso.desRecurso"/>
				</td>	
			</tr>
			
			<!-- Exencion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.exencion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.exencion.desExencion"/>
				</td>
			</tr>
			
			<!-- Cuenta -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.cuenta.numeroCuenta"/>
				</td>
			</tr>
			
			<!-- Tipo Sujeto Solicitante -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.tipoSujeto.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.tipoSujeto.desTipoSujeto"/>
				</td>	
			</tr>
								
			<!-- Fecha Desde / Hasta  -->
			<tr>

				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaDesde.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeConvivAdapterVO" property="cueExe.fechaDesdeView" /></td>
				
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaHasta.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeConvivAdapterVO" property="cueExe.fechaHastaView" /></td>
			</tr>
		
			<!-- Solicitante -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.solicitante.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.solicitante.represent"/>
				</td>	
			</tr>
			
			<!-- Descripcion Solicitante -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.solicDescripcion.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cueExeConvivAdapterVO" property="cueExe.solicDescripcion"/></td>			
			</tr>
			
			<!-- Tipo Documento -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.tipoDocumento.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cueExeConvivAdapterVO" property="cueExe.tipoDocumento"/></td>			
			</tr>
			
			<!-- Nro Documento -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.nroDocumento.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cueExeConvivAdapterVO" property="cueExe.nroDocumento"/></td>			
			</tr>
			
			<!-- Fecha Resolucion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaResolucion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.fechaResolucionView"/>
				</td>	
			</tr>
			
			<!-- Fecha Ultima Inspeccion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaUltIns.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.fechaUltInsView"/>
				</td>
			</tr>
			
			<!-- Fecha Presentacion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaPresent.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.fechaPresentView"/>
				</td>
			</tr>
			
			<!-- Documentacion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.documentacion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.documentacion"/>
				</td>
			</tr>
			
			<!-- Ordenanza / Articulo -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.ordenanza.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeConvivAdapterVO" property="cueExe.ordenanza"/></td>			
				<td><label><bean:message bundle="exe" key="exe.cueExe.articulo.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeConvivAdapterVO" property="cueExe.articulo"/></td>			
			</tr>

			<!-- Inciso -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.inciso.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cueExeConvivAdapterVO" property="cueExe.inciso"/></td>
			</tr>
			
			<!-- Observaciones -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.observaciones.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.observaciones"/>
				</td>
			</tr>
			
			<!-- Caso -->
			<!-- Inclucion de CasoView -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="cueExeConvivAdapterVO" property="cueExe"/>
					<%@ include file="/cas/caso/includeCasoView.jsp" %>				
				</td>
			</tr>
			<!-- Fin Inclucion de CasoView -->
			
			<!-- solic Fecha Desde / solic Hasta  -->
			<logic:equal name="cueExeConvivAdapterVO" property="poseeSolicFechas" value="true">
				<tr>
					<td><bean:message bundle="exe" key="exe.cueExe.solicFechaMigracion.msg"/>: </td>
					<td class="normal" colspan="3">
						<bean:message bundle="exe" key="exe.cueExe.solicFechaDesde.label"/>: &nbsp;
						<bean:write name="cueExeConvivAdapterVO" property="cueExe.solicFechaDesdeView"/>
						&nbsp; -
						<bean:message bundle="exe" key="exe.cueExe.solicFechaHasta.label"/>: &nbsp;
						<bean:write name="cueExeConvivAdapterVO" property="cueExe.solicFechaHastaView"/>					
					</td>
				</tr>
			</logic:equal>
		</table>
	</fieldset>

<logic:equal name="cueExeConvivAdapterVO" property="esExencionJubilado" value="true">
	<fieldset>
		<legend><bean:message bundle="exe" key="exe.cueExeAdapter.datosExtraJubilado"/></legend>
		<table class="tabladatos">		
			
			<!--  Nro Beneficiario / Caja -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.nroBeneficiario.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeConvivAdapterVO" property="cueExe.nroBeneficiario" /></td>			
				<td><label><bean:message bundle="exe" key="exe.cueExe.caja.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeConvivAdapterVO" property="cueExe.caja"/></td>			
			</tr>
			
			<!-- Clase -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.clase.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeConvivAdapterVO" property="cueExe.claseView"/></td>			
			</tr>
			
			<!-- Fecha Vencimiento Contrato inquilino  -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaVencContInq.label"/>: </label></td>
				<td class="normal">
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.fechaVencContInqView"/>
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
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.estadoCueExe.desEstadoCueExe"/>
				</td>	
			</tr>
		</table>		
	</fieldset>
				
   		<logic:equal name="cueExeConvivAdapterVO" property="act" value="eliminar">
   		<!-- Eliminar un conviviente -->
			<fieldset>
				<legend><bean:message bundle="exe" key="exe.cueExeConvivAdapter.conviviente.legend"/></legend>		
				<table class="tabladatos">			            
					<tbody>
			    		<!-- nombre -->
			    		<tr>
			    			<td><label><bean:message bundle="exe" key="exe.conviviente.nombre"/>:</label>
			    			<td class="normal" colspan="3">
			    				<bean:write name="cueExeConvivAdapterVO" property="conviviente.convNombre"/>
			    			</td>	    			
			    		</tr>
			    		
			    		<!-- tipo y nro doc -->
			    		<tr>
			    			<td><label><bean:message bundle="exe" key="exe.conviviente.documento.tipo"/>:</label>
			    			<td class="normal">
			    				<bean:write name="cueExeConvivAdapterVO" property="conviviente.convTipodoc"/>
			    			</td>
			    			<td><label><bean:message bundle="exe" key="exe.conviviente.documento.nro"/></label>
			    			<td class="normal">
			    				<bean:write name="cueExeConvivAdapterVO" property="conviviente.convNrodoc"/>
			    			</td>
			    		</tr>
			    		
			    		<tr>
			    			<!-- Parentezco -->
			    			<td><label><bean:message bundle="exe" key="exe.conviviente.parentezco"/>:</label>
			    			<td class="normal">
			    				<bean:write name="cueExeConvivAdapterVO" property="conviviente.convParentesco"/>
			    			</td>
			    			
			    			<!-- Edad -->	
			    			<td><label><bean:message bundle="exe" key="exe.conviviente.edad"/>:</label>
			    			<td class="normal">
			    				<bean:write name="cueExeConvivAdapterVO" property="conviviente.convEdadView"/>
			    			</td>
			    		</tr>	    		
					</tbody>
				</table>
			</fieldset>   			
   		</logic:equal>
   		
   		<logic:notEqual name="cueExeConvivAdapterVO" property="act" value="eliminar">				    
<!-- Convivientes -->		
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="exe" key="exe.cueExe.listConviviente.label"/></caption>
			    	<tbody>
			    			<logic:notEmpty  name="cueExeConvivAdapterVO" property="cueExe.listConviviente">
						    	<tr>
						    		<th align="left">&nbsp;</th> <!-- modificar -->
						    		<th align="left">&nbsp;</th> <!-- eliminar -->
									<th align="left"><bean:message bundle="exe" key="exe.conviviente.nombre"/></th>
									<th align="left"><bean:message bundle="exe" key="exe.conviviente.documento"/></th>
									<th align="left"><bean:message bundle="exe" key="exe.conviviente.parentezco"/></th>
									<th align="left"><bean:message bundle="exe" key="exe.conviviente.edad"/></th>
								</tr>
								<logic:iterate id="ConvivienteVO" name="cueExeConvivAdapterVO" property="cueExe.listConviviente">
							
									<tr>
										<td>
											<logic:equal name="cueExeConvivAdapterVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('irModificar', '<bean:write name="ConvivienteVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="cueExeConvivAdapterVO" property="modificarEnabled" value="enabled">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>									
											</logic:notEqual>								
										</td>
										<td>
											<logic:equal name="cueExeConvivAdapterVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('irEliminar', '<bean:write name="ConvivienteVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="cueExeConvivAdapterVO" property="eliminarEnabled" value="enabled">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>									
											</logic:notEqual>
											
										</td>
										
										<td><bean:write name="ConvivienteVO" property="convNombre"/>&nbsp;</td>
										<td>
											<bean:write name="ConvivienteVO" property="convTipodoc"/>&nbsp;
											<bean:write name="ConvivienteVO" property="convNrodoc"/>								
										</td>
										<td><bean:write name="ConvivienteVO" property="convParentesco"/>&nbsp;</td>							
										<td><bean:write name="ConvivienteVO" property="convEdadView"/>&nbsp;</td>
									</tr>
								</logic:iterate>
							</logic:notEmpty>
							<logic:empty  name="cueExeConvivAdapterVO" property="cueExe.listConviviente">
								<tr><td align="center">
								<bean:message bundle="base" key="base.noExistenRegitros"/>
								</td></tr>
							</logic:empty>
					</tbody>
				</table>
<!-- Convivientes -->
		</logic:notEqual>

	<!-- CueExe -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
  	   			<logic:equal name="cueExeConvivAdapterVO" property="act" value="eliminar">
  	   				<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volverAVer', '');">
		   	    		<bean:message bundle="base" key="abm.button.volver"/>
		   	    	</html:button>
  	   			</logic:equal>
  	   			<logic:notEqual name="cueExeConvivAdapterVO" property="act" value="eliminar">
		   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
		   	    		<bean:message bundle="base" key="abm.button.volver"/>
		   	    	</html:button>
		   	    </logic:notEqual>	
   	    	</td> 
   	    	<logic:equal name="cueExeConvivAdapterVO" property="esExencionJubilado" value="true">
		   	    <logic:equal name="cueExeConvivAdapterVO" property="act" value="eliminar">
		   	    	<td align="right" width="50%">
			   	    	<html:button property="btnAgregar" styleClass="boton" onclick="submitForm('eliminar', '');">
			   	    		<bean:message bundle="base" key="abm.button.eliminar"/>
			   	    	</html:button>
		   	    	</td>
		   	    </logic:equal>
		   	    <logic:notEqual name="cueExeConvivAdapterVO" property="act" value="eliminar">		   	    
	   	    		<logic:equal name="cueExeConvivAdapterVO" property="agregarConvivEnabled" value="enabled">	
			   	    	<td align="right" width="50%">
				   	    	<html:button property="btnAgregar" styleClass="boton" onclick="submitForm('irAgregar', '');">
				   	    		<bean:message bundle="base" key="abm.button.agregar"/>
				   	    	</html:button>
			   	    	</td>
			   	    </logic:equal>
		   	    </logic:notEqual>	
	   	    </logic:equal>	   	    		    	
   	    </tr>
   	</table>
	
	<input type="hidden" name="method" value=""/>
<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
