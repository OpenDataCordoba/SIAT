<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarTranAfip.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.tranAfipViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<!-- TranAfip -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.tranAfip.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="bal" key="bal.tranAfip.idTransaccionAfip.label"/>: </label></td>
					<td class="normal">
						<bean:write name="tranAfipAdapterVO" property="tranAfip.idTransaccionAfipView" />
					</td>
					<td><label><bean:message bundle="bal" key="bal.tranAfip.fechaProceso.label"/>: </label></td>
					<td class="normal">
						<bean:write name="tranAfipAdapterVO" property="tranAfip.fechaProcesoView" />
					</td>									
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.tranAfip.canPago.label"/>: </label></td>
					<td class="normal">
						<bean:write name="tranAfipAdapterVO" property="tranAfip.canPagoView" />
					</td>
					<td><label><bean:message bundle="bal" key="bal.tranAfip.canDecJur.label"/>: </label></td>
					<td class="normal">
						<bean:write name="tranAfipAdapterVO" property="tranAfip.canDecJurView"/>
					</td>				
				</tr>	
				<tr>
					<td><label><bean:message bundle="bal" key="bal.tranAfip.formulario.ref"/>: </label></td>
					<td class="normal">
						<bean:write name="tranAfipAdapterVO" property="tranAfip.formularioView"/>
					</td>
					<td><label><bean:message bundle="bal" key="bal.tranAfip.vepView.label"/>: </label></td>
					<td class="normal">
						<bean:write name="tranAfipAdapterVO" property="tranAfip.vepView" />
					</td>								
				</tr>	
				<tr>
					<td><label><bean:message bundle="bal" key="bal.tranAfip.cuit.ref"/>: </label></td>
					<td class="normal">
						<bean:write name="tranAfipAdapterVO" property="tranAfip.cuit"/>
					</td>
					<td><label><bean:message bundle="bal" key="bal.tranAfip.totMontoIngresado.label"/>: </label></td>
					<td class="normal">
						<bean:write name="tranAfipAdapterVO" property="tranAfip.totMontoIngresadoView" />
					</td>				
				</tr>	
				<tr>
					<td><label><bean:message bundle="bal" key="bal.tranAfip.observacion.ref"/>: </label></td>
					<td class="normal">
						<bean:write name="tranAfipAdapterVO" property="tranAfip.observacion"/>
					</td>
					<td><label><bean:message bundle="bal" key="bal.tranAfip.fechaAnulacion.ref"/>: </label></td>
					<td class="normal">
						<bean:write name="tranAfipAdapterVO" property="tranAfip.fechaAnulacionView"/>
					</td>	
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.tranAfip.estTranAfip.descripcion.ref"/>: </label></td>
					<td class="normal">
						<bean:write name="tranAfipAdapterVO" property="tranAfip.estTranAfip.descripcion"/>
					</td>
				</tr>		
			</table>
		</fieldset>	
		<!-- TranAfip -->
		
		<!-- Datos del Cheque -->	
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.tranAfip.datosCheque.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="bal" key="bal.tranAfip.nroCheque.label"/>: </label></td>
					<td class="normal">
						<bean:write name="tranAfipAdapterVO" property="tranAfip.nroChequeView"/>
					</td>
					<td><label><bean:message bundle="bal" key="bal.tranAfip.ctaCteCheque.label"/>: </label></td>
					<td class="normal">
						<bean:write name="tranAfipAdapterVO" property="tranAfip.ctaCteChequeView"/>
					</td>	
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.tranAfip.bancoCheque.label"/>: </label></td>
					<td class="normal">
						<bean:write name="tranAfipAdapterVO" property="tranAfip.bancoChequeView"/>
					</td>
					<td><label><bean:message bundle="bal" key="bal.tranAfip.sucursalCheque.label"/>: </label></td>
					<td class="normal">
						<bean:write name="tranAfipAdapterVO" property="tranAfip.sucursalChequeView"/>
					</td>	
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.tranAfip.codPostalCheque.label"/>: </label></td>
					<td class="normal">
						<bean:write name="tranAfipAdapterVO" property="tranAfip.codPostalCheque"/>
					</td>
					<td><label><bean:message bundle="bal" key="bal.tranAfip.fechaAcredCheque.label"/>: </label></td>
					<td class="normal">
						<bean:write name="tranAfipAdapterVO" property="tranAfip.fechaAcredChequeView"/>
					</td>	
				</tr>
			</table>
		</fieldset>	
		<!-- Datos del Cheque -->	

		<logic:notEqual name="tranAfipAdapterVO" property="act" value="eliminar">
			<logic:equal name="tranAfipAdapterVO" property="act" value="ver">
			<!-- DetallePago -->				
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="bal" key="bal.tranAfip.listDetallePago.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="tranAfipAdapterVO" property="tranAfip.listDetallePago">	    	
				    	<tr>
							<th width="1">&nbsp;</th><!-- Ver/Seleccionar -->	
							<th width="1">&nbsp;</th><!-- Eliminar -->						
							<th align="left"><bean:message bundle="bal" key="bal.detallePago.fechaPago.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.detallePago.numeroCuenta.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.detallePago.impuesto.ref"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.detallePago.anio.ref"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.detallePago.periodo.ref"/></th>				
							<th align="left"><bean:message bundle="bal" key="bal.detallePago.importePago.ref"/></th>
						</tr>
						<logic:iterate id="DetallePagoVO" name="tranAfipAdapterVO" property="tranAfip.listDetallePago">
							<tr>
								<td>
								<!-- Ver/Seleccionar -->
								<logic:notEqual name="tranAfipAdapterVO" property="modoSeleccionar" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verDetallePago', '<bean:write name="DetallePagoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:notEqual>	
								</td>
								<!-- Eliminar -->	
								<td>			
									<logic:equal name="tranAfipAdapterVO" property="eliminarDetallePagoEnabled" value="enabled">
										<logic:equal name="tranAfipAdapterVO" property="tranAfip.eliminarTranAfipEnabled" value="enabled">
											<!-- Futuras Validaciones  DetallePagoVO-->
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarTranAfip', '<bean:write name="DetallePagoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="tranAfipAdapterVO" property="tranAfip.eliminarTranAfipEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>	
									</logic:equal>
									<logic:notEqual name="tranAfipAdapterVO" property="eliminarDetallePagoEnabled" value="enabled">										
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</td>			
								<td><bean:write name="DetallePagoVO" property="fechaPagoView"/>&nbsp;</td>
								<td><bean:write name="DetallePagoVO" property="numeroCuenta"/>&nbsp;</td>
								<td><bean:write name="DetallePagoVO" property="impuestoView"/>&nbsp;</td>
								<td><bean:write name="DetallePagoVO" property="anioView"/>&nbsp;</td>
								<td><bean:write name="DetallePagoVO" property="periodoView"/>&nbsp;</td>
								<td><bean:write name="DetallePagoVO" property="importePagoView"/>&nbsp;</td>													
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="tranAfipAdapterVO" property="tranAfip.listDetallePago">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>				
				</tbody>
			</table>		
			<!-- DetallePago -->
			
			<!-- DetalleDJ -->		
				<div id="resultadoFiltro" class="scrolable" style="height: 300px;">		
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="bal" key="bal.tranAfip.listDetalleDJVO.label"/></caption>
			    	<tbody>
						<logic:notEmpty  name="tranAfipAdapterVO" property="tranAfip.listDetalleDJ">	    
						
					    	<tr>
								<th width="1">&nbsp;</th><!-- Ver/Seleccionar --> 		
								<th width="1">&nbsp;</th><!-- Eliminar -->						
								<th align="left"><bean:message bundle="bal" key="bal.detalleDJ.fila.label"/></th>
								<th align="left"><bean:message bundle="bal" key="bal.detalleDJ.registro.label"/></th>
								<th align="left"><bean:message bundle="bal" key="bal.detalleDJ.fechaProceso.label"/></th>
								<th align="left"><bean:message bundle="bal" key="bal.detalleDJ.estDetDJ.descripcion.ref"/></th>
							</tr>
							<logic:iterate id="DetalleDJVO" name="tranAfipAdapterVO" property="tranAfip.listDetalleDJ">
								<tr>
									<td><!-- Ver/Seleccionar -->
									<logic:notEqual name="tranAfipAdapterVO" property="modoSeleccionar" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verDetalleDJ', '<bean:write name="DetalleDJVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:notEqual>																
									</td>		
									<!-- Eliminar -->	
									<td>			
										<logic:equal name="tranAfipAdapterVO" property="eliminarDetalleDJEnabled" value="enabled">				
											<logic:equal name="tranAfipAdapterVO" property="tranAfip.eliminarTranAfipEnabled" value="enabled">
												<!-- Futuras Validaciones  DetalleDJVO-->
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarDetalleDJ', '<bean:write name="DetalleDJVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>	
											</logic:equal>						
											<logic:notEqual name="tranAfipAdapterVO" property="tranAfip.eliminarTranAfipEnabled" value="enabled">		
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="tranAfipAdapterVO" property="eliminarDetalleDJEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>							
									<td><bean:write name="DetalleDJVO" property="filaView"/>&nbsp;</td>
									<td><bean:write name="DetalleDJVO" property="desRegistroView"/>&nbsp;</td>
									<td><bean:write name="DetalleDJVO" property="fechaProcesoView"/>&nbsp;</td>
									<td><bean:write name="DetalleDJVO" property="estDetDJ.descripcion"/>&nbsp;</td>
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty  name="tranAfipAdapterVO" property="tranAfip.listDetalleDJ">
							<tr><td align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
							</td></tr>
						</logic:empty>
					</tbody>
				</table>
				</div>	
			<!-- DetalleDJ -->
			
			</logic:equal>
			
		</logic:notEqual>
		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
					<logic:equal name="tranAfipAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminarTranAfip', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>	  	   	    			
	   	    	</td>
	   	    	<logic:equal name="tranAfipAdapterVO" property="act" value="generarDecJur">
		   	    	<td align="right" width="100%">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('generarDecJur', '');">
								<bean:message bundle="bal" key="bal.tranAfipAdapter.button.generarDecJur"/>
							</html:button>
			    	</td>
				</logic:equal>
  
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='tranAfipAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->