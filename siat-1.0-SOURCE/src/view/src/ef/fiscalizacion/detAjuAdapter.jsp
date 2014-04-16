	<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarDetAju.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.detAjuAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- DetAju -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.detAju.label"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="ef" key="ef.detAju.fecha.label"/>: </label></td>
					<td class="normal"><bean:write name="detAjuAdapterVO" property="detAju.fechaView"/></td>
					
					<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal">
						<bean:write name="detAjuAdapterVO" property="detAju.ordConCue.cuenta.recurso.codRecurso" />
						&nbsp;
						<bean:write name="detAjuAdapterVO" property="detAju.ordConCue.cuenta.numeroCuenta" />
					</td>
				</tr>
				<tr>
					<td>
						<label><bean:message bundle="ef" key="ef.detAju.fechaActualizacion.label"/>: </td></label>
					</td>
					<td class="normal" colspan="3">
						<html:text name="detAjuAdapterVO" property="detAju.fechaActualizacionView" styleId="fechaActualizacionView"/>
						<a class="link_siat" onclick="return show_calendar('fechaActualizacionView');" id="a_fechaActualizacionView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
							<html:button property="btnImprimirAjuPos"  styleClass="boton" onclick="submitForm('recalcularAct', '');">
						   		<bean:message bundle="ef" key="ef.detAjuAdapter.button.actualizarAct"/>
					   		</html:button>
					</td>
				</tr>
															
			</table>
		</fieldset>	
		<!-- DetAju -->
		
		<!-- DetAjuDet -->		
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.aliComFueColAdapter.listaDetAjuDet.label"/></legend>		
			
			<div id="listaPlaFueDatCom" class="horizscroll" style="height: 350px;">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">				
			    	<tbody>
						<logic:notEmpty  name="detAjuAdapterVO" property="detAju.listDetAjuDet">	    	
					    	<tr>
								<th width="1">&nbsp;</th> <!-- Retenciones -->
								<th align="left"><bean:message bundle="ef" key="ef.plaFueDatCom.periodo.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.ordConBasImp.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.detAju.alicuota.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.detAju.tributo.label"/></th>						
								<th align="left"><bean:message bundle="ef" key="ef.detAju.cantPers.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.detAju.minimo.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.detAju.tributoDet.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.detAju.publicidad.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.detAju.mesasSillas.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.detAju.totalTributo.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.detAju.declarado.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.detAju.retencion.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.detAju.ajustePos.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.detAju.ajusteNeg.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.detAju.ajAct.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.detAju.porMulta.label"/></th>
							</tr>
							<logic:iterate id="DetAjuDetVO" name="detAjuAdapterVO" property="detAju.listDetAjuDet">
					
								<tr>
									<!-- Modificar retencion-->								
									<td>
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('irModificarRetencion', '<bean:write name="DetAjuDetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</td>
									
									<!-- Periodo -->
									<td>
										<bean:write name="DetAjuDetVO" property="plaFueDatCom.periodoAnioView"/>&nbsp;
									</td>
									
									<!-- bases imponibles -->
									<td align="left" valign="top">
										<table cellpadding="0" cellspacing="0" width="100%" >
											<tbody>
												<logic:present name="DetAjuDetVO" property="plaFueDatCom.col1">
													<tr><td nowrap="nowrap"><bean:write name="DetAjuDetVO" property="plaFueDatCom.col1BasRos" bundle="base" formatKey="general.format.currency"/></td></tr>
												</logic:present>
												<logic:present name="DetAjuDetVO" property="plaFueDatCom.col2">
													<tr><td nowrap="nowrap"><bean:write name="DetAjuDetVO" property="plaFueDatCom.col2BasRos" bundle="base" formatKey="general.format.currency"/></td></tr>
												</logic:present>
												<logic:present name="DetAjuDetVO" property="plaFueDatCom.col3">
													<tr><td nowrap="nowrap"><bean:write name="DetAjuDetVO" property="plaFueDatCom.col3BasRos" bundle="base" formatKey="general.format.currency"/></td></tr>
												</logic:present>
												<logic:present name="DetAjuDetVO" property="plaFueDatCom.col4">
													<tr><td nowrap="nowrap"><bean:write name="DetAjuDetVO" property="plaFueDatCom.col4BasRos" bundle="base" formatKey="general.format.currency"/></td></tr>
												</logic:present>											
												<logic:present name="DetAjuDetVO" property="plaFueDatCom.col5">
													<tr><td nowrap="nowrap"><bean:write name="DetAjuDetVO" property="plaFueDatCom.col5BasRos" bundle="base" formatKey="general.format.currency"/></td></tr>
												</logic:present>
												<logic:present name="DetAjuDetVO" property="plaFueDatCom.col6">
													<tr><td nowrap="nowrap"><bean:write name="DetAjuDetVO" property="plaFueDatCom.col6BasRos" bundle="base" formatKey="general.format.currency"/></td></tr>
												</logic:present>
												<logic:present name="DetAjuDetVO" property="plaFueDatCom.col7">
													<tr><td nowrap="nowrap"><bean:write name="DetAjuDetVO" property="plaFueDatCom.col7BasRos" bundle="base" formatKey="general.format.currency"/></td></tr>
												</logic:present>
												<logic:present name="DetAjuDetVO" property="plaFueDatCom.col8">
													<tr><td nowrap="nowrap"><bean:write name="DetAjuDetVO" property="plaFueDatCom.col8BasRos" bundle="base" formatKey="general.format.currency"/></td></tr>
												</logic:present>
												<logic:present name="DetAjuDetVO" property="plaFueDatCom.col9">
													<tr><td nowrap="nowrap"><bean:write name="DetAjuDetVO" property="plaFueDatCom.col9BasRos" bundle="base" formatKey="general.format.currency"/></td></tr>
												</logic:present>
												<logic:present name="DetAjuDetVO" property="plaFueDatCom.col10">
													<tr><td nowrap="nowrap"><bean:write name="DetAjuDetVO" property="plaFueDatCom.col10BasRos" bundle="base" formatKey="general.format.currency"/></td></tr>
												</logic:present>
												<logic:present name="DetAjuDetVO" property="plaFueDatCom.col11">
													<tr><td nowrap="nowrap"><bean:write name="DetAjuDetVO" property="plaFueDatCom.col11BasRos" bundle="base" formatKey="general.format.currency"/></td></tr>
												</logic:present>
												<logic:present name="DetAjuDetVO" property="plaFueDatCom.col12">
													<tr><td nowrap="nowrap"><bean:write name="DetAjuDetVO" property="plaFueDatCom.col12BasRos" bundle="base" formatKey="general.format.currency"/></td></tr>
												</logic:present>																											
											</tbody>
										</table>
									</td>
									
									<!-- alicuotas -->
									<td align="left" valign="top">
										<table cellpadding="0" cellspacing="0" width="100%" >
											<tbody>
												<logic:iterate id="AliComFueColVO" name="DetAjuDetVO" property="listAliComFueCol">
													<tr><td>
														&nbsp;<bean:write name="AliComFueColVO" property="multiploView" filter="false"/></td></tr>
												</logic:iterate>
											</tbody>
										</table>										
									</td>
									
									<!-- tributo -->
									<td>&nbsp;<bean:write name="DetAjuDetVO" property="tributoView"/></td>
									
									<!-- cant. Personal -->
									<td>&nbsp;<bean:write name="DetAjuDetVO" property="cantPersonalView"/></td>
									
									<!-- Minimo -->
									<td>&nbsp;<bean:write name="DetAjuDetVO" property="minimoView"/></td>
									
									<!-- Tributo determinado -->
									<td>&nbsp;<bean:write name="DetAjuDetVO" property="triDetView"/></td>
									
									<!-- Adic. publicidad -->
									<td>&nbsp;<bean:write name="DetAjuDetVO" property="valorPublicidad"/></td>
									
									<!-- Adic. Mesas y sillas -->
									<td>&nbsp;<bean:write name="DetAjuDetVO" property="valorMesasYSillas"/></td>
									
									<!-- Total Tributo -->
									<td>&nbsp;<bean:write name="DetAjuDetVO" property="totalTributoView"/></td>
									
									<!-- Declarado -->
									<td>&nbsp;<bean:write name="DetAjuDetVO" property="pagoView"/></td>
									
									<!-- Retencion -->
									<td>&nbsp;<bean:write name="DetAjuDetVO" property="retencionView"/></td>
									
									<!-- Ajuste -->					
									<logic:greaterEqual name="DetAjuDetVO" property="ajuste" value="0">
										<td>&nbsp;<bean:write name="DetAjuDetVO" property="ajusteView"/></td>
										<td>&nbsp;</td>									
									</logic:greaterEqual>
									<logic:lessThan name="DetAjuDetVO" property="ajuste" value="0">
										<td>&nbsp;</td>
										<td>&nbsp;<bean:write name="DetAjuDetVO" property="ajusteView"/></td>
									</logic:lessThan>
									
									<!-- Ajuste Actualizado -->
									<td>&nbsp;<bean:write name="DetAjuDetVO" property="ajusteActualizadoView"/></td>
									
									<!-- Porcentaje Multa -->
									<td>&nbsp;<bean:write name="DetAjuDetVO" property="porMultaView"/></td>
								</tr>
							</logic:iterate>
							
								<tr>
									<!-- total positivo -->
									<td colspan="13" align="right">
										<b>TOTALES:</b>
									</td>
									<td>	
										<bean:write name="detAjuAdapterVO" property="detAju.totalAjustePosView"/>
									</td>
									<!-- total negativo -->
									<td>
										<bean:write name="detAjuAdapterVO" property="detAju.totalAjusteNegView"/>
									</td>	
									<!-- total actualizado -->
									<td>
										<bean:write name="detAjuAdapterVO" property="detAju.totalAjusteActView"/>
									</td>	
						</logic:notEmpty>
						<logic:empty  name="detAjuAdapterVO" property="detAju.listDetAjuDet">
							<tr><td align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
							</td></tr>
						</logic:empty>
		
					</tbody>
				</table>
			</div>	
		</fieldset>	
	<!-- DetAjuDet -->
				
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<logic:greaterThan name="detAjuAdapterVO" property="detAju.cantDetAjuDet" value="0">
					<td align="right">
						<bean:define id="calcularBasesEnabled" name="detAjuAdapterVO" property="calcularBasesEnabled"/>
						<input type="button" class="boton" <%=calcularBasesEnabled%> onClick="submitForm('calcularBases', 
							'<bean:write name="detAjuAdapterVO" property="detAju.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="ef" key="ef.detAjuAdapter.button.calcularBases"/>"/>

						<bean:define id="agregarPersonalEnabled" name="detAjuAdapterVO" property="agregarPersonalEnabled"/>
						<input type="button" class="boton" <%=agregarPersonalEnabled%> onClick="submitForm('irAgregarMasivo','1');" 
							value="<bean:message bundle="ef" key="ef.detAjuAdapter.button.agregarPersonal"/>"/>

						<bean:define id="adicionalPublicidadEnabled" name="detAjuAdapterVO" property="adicionalPublicidadEnabled"/>
						<input type="button" class="boton" <%=adicionalPublicidadEnabled%> onClick="submitForm('irAgregarMasivo','2');" 
							value="<bean:message bundle="ef" key="ef.detAjuAdapter.button.adicionalPublicidad"/>"/>

						<bean:define id="adicionalMesasYSillasEnabled" name="detAjuAdapterVO" property="adicionalMesasYSillasEnabled"/>
						<input type="button" class="boton" <%=adicionalMesasYSillasEnabled%> onClick="submitForm('irAgregarMasivo','3');" 
							value="<bean:message bundle="ef" key="ef.detAjuAdapter.button.adicionalMYS"/>"/>
						
						<bean:define id="determPorMultaEnabled" name="detAjuAdapterVO" property="determPorMultaEnabled"/>
						<input type="button" class="boton" <%=determPorMultaEnabled%> onClick="submitForm('irAgregarMasivo','4');" 
							value="<bean:message bundle="ef" key="ef.detAjuAdapter.button.determPorMulta"/>"/>
									
						<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						   <bean:message bundle="base" key="abm.button.imprimir"/>
					   </html:button>
						
						<html:button property="btnImprimirAjuPos"  styleClass="boton" onclick="submitImprimir('imprimirAjuPosReportFromAdapter', '1');">
						   <bean:message bundle="ef" key="ef.detAjuAdapter.button.imprimirAjuPos"/>
					   </html:button>
								
					</td>				
				</logic:greaterThan>   	    			
			</tr>
		</table>
	   	
	   	<input type="hidden" name="name"  value="<bean:write name='detAjuAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	   	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		<!-- Inclusion del Codigo Javascript del Calendar-->
		<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- detAjuAdapter.jsp -->