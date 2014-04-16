<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarComparacion.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.comparacionAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Comparacion -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.comparacion.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="ef" key="ef.comparacion.fecha.label"/>: </label></td>
					<td class="normal">
						<bean:write name="comparacionAdapterVO" property="comparacion.fechaView"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="ef" key="ef.comparacion.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="comparacionAdapterVO" property="comparacion.descripcion"/></td>			
				</tr>				
				<tr>
					<td colspan="4"> 
						<bean:define id="modificarEncabezadoEnabled" name="comparacionAdapterVO" property="modificarEncabezadoEnabled"/>
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
							'<bean:write name="comparacionAdapterVO" property="comparacion.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>		
					</td>
				</tr>
			</table>
		</fieldset>	
		<!-- Comparacion -->
		
		<!-- CompFuente -->	
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.compFuente.title"/></legend>
			
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="ef" key="ef.comparacion.listCompFuente.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="comparacionAdapterVO" property="comparacion.listCompFuente">	    	
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Ver -->
							<th width="1">&nbsp;</th> <!-- Eliminar -->
							<th align="left"><bean:message bundle="ef" key="ef.comparacionAdapter.difPos.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.comparacionAdapter.difNeg.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.comparacion.descripcion.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.comparacionAdapter.desde.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.comparacionAdapter.hasta.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.comparacion.total.label"/></th>						
						</tr>
						<logic:iterate id="CompFuenteVO" name="comparacionAdapterVO" property="comparacion.listCompFuente">
				
							<tr>
								<!-- Ver -->
								<td>
									<logic:equal name="comparacionAdapterVO" property="verCompFuenteEnabled" value="enabled">
										<logic:equal name="CompFuenteVO" property="verEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verCompFuente', '<bean:write name="CompFuenteVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="CompFuenteVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="comparacionAdapterVO" property="verCompFuenteEnabled" value="enabled">										
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</td>							
								
								<!-- Eliminar-->								
								<td>
									<logic:equal name="comparacionAdapterVO" property="eliminarCompFuenteEnabled" value="enabled">
										<logic:equal name="CompFuenteVO" property="eliminarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarCompFuente', '<bean:write name="CompFuenteVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="CompFuenteVO" property="eliminarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="comparacionAdapterVO" property="eliminarCompFuenteEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</td>
	
								<!-- difPos -->
								<td>
									<bean:define name="CompFuenteVO" property="idView" id="idCompFuenteVO"/>
									<html:radio name="comparacionAdapterVO" property="difPositivo"
										 value="<%=idCompFuenteVO.toString()%>"/>
								</td>
								
								<!-- difNeg -->
								<td>
									<html:radio name="comparacionAdapterVO" property="difNegativo"
										 value="<%=idCompFuenteVO.toString()%>"/>
	
								</td>
								
								<!-- descripcion -->
								<td>
									<bean:write name="CompFuenteVO" property="plaFueDat.tituloView"/>									
								</td>
								<td><bean:write name="CompFuenteVO" property="periodoDesde4View"/></td>
								<td><bean:write name="CompFuenteVO" property="periodoHasta4View"/></td>
								<td><bean:write name="CompFuenteVO" property="totalView"/></td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="comparacionAdapterVO" property="comparacion.listCompFuente">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>
					<tr>
						<td align="right" colspan="8">
	   	    				<bean:define id="agregarEnabled" name="comparacionAdapterVO" property="agregarCompFuenteEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregarCompFuente', '<bean:write name="comparacionAdapterVO" property="comparacion.id" bundle="base" formatKey="general.format.id"/>');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"
							/>
						</td>
					</tr>
					<logic:notEmpty  name="comparacionAdapterVO" property="comparacion.listCompFuente">
						<tr>
							<td align="center" colspan="8">
		   	    				<bean:define id="calcularDifEnabled" name="comparacionAdapterVO" property="calcularDifEnabled"/>
								<input type="button" <%=calcularDifEnabled%> class="boton" 
									onClick="submitForm('agregarCompFuenteRes', '<bean:write name="comparacionAdapterVO" property="comparacion.id" bundle="base" formatKey="general.format.id"/>');" 
									value="<bean:message bundle="ef" key="ef.comparacionAdapter.button.calcularDif"/>"
								/>
							</td>
						</tr>				
					</logic:notEmpty>				
				</tbody>
			</table>
		</fieldset>	
		<!-- CompFuente -->
				
		<!-- CompFuenteRes -->	
		<fieldset id="seccionCompFuenteRes">
			<legend><bean:message bundle="ef" key="ef.compFuenteRes.title"/></legend>
			
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="ef" key="ef.comparacion.listCompFuenteRes.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="comparacionAdapterVO" property="comparacion.listCompFuenteRes">	    	
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Eliminar -->
							<th align="left"><bean:message bundle="ef" key="ef.compFuenteRes.operacion.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.compFuenteRes.diferencia.label"/></th>						
						</tr>
						<logic:iterate id="CompFuenteResVO" name="comparacionAdapterVO" property="comparacion.listCompFuenteRes">
				
							<tr>
								
								<!-- Eliminar-->								
								<td>
									<logic:equal name="comparacionAdapterVO" property="eliminarCompFuenteResEnabled" value="enabled">
										<logic:equal name="CompFuenteResVO" property="eliminarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarCompFuenteRes', '<bean:write name="CompFuenteResVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="CompFuenteResVO" property="eliminarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="comparacionAdapterVO" property="eliminarCompFuenteResEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</td>
								
								<td>
									<bean:write name="CompFuenteResVO" property="operacion"/>
									<logic:equal name="CompFuenteResVO" property="ambasFuentesMensuales" value="true">
										<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
											<tr>
												<th><bean:message bundle="ef" key="ef.periodoOrden.periodo.label"/></th>
												<th><bean:message bundle="ef" key="ef.compFuenteResMensual.base1.label"/></th>
												<th><bean:message bundle="ef" key="ef.compFuenteResMensual.base2.label"/></th>
												<th><bean:message bundle="ef" key="ef.compFuenteResMensual.diferencia.label"/></th>
											</tr>
											<logic:iterate id="DifMensualVO" name="CompFuenteResVO" property="listCompFuenteResMensual">
												<tr>
													<td><bean:write name="DifMensualVO" property="periodoView"/></td>
													<td><bean:write name="DifMensualVO" property="baseFuenteMinView"/></td>
													<td><bean:write name="DifMensualVO" property="baseFuenteSusView"/></td>
													<td><bean:write name="DifMensualVO" property="diferenciaView"/></td>
												</tr>
											</logic:iterate>
										</table>
									</logic:equal>
								</td>
								<td>$&nbsp;<bean:write name="CompFuenteResVO" property="diferenciaView"/></td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="comparacionAdapterVO" property="comparacion.listCompFuenteRes">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>
	
				</tbody>
			</table>
		</fieldset>	
		<!-- CompFuenteRes -->
						
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>   	   			
				<td align="right">
						<bean:define id="imprimirComparacionEnabled" name="comparacionAdapterVO" property="imprimirComparacionEnabled"/>
							<bean:define id="imprimirComparacionEnabled" name="comparacionAdapterVO" property="imprimirComparacionEnabled"/>
							<input type="button" class="boton" <%=imprimirComparacionEnabled%> onclick="submitImprimir('imprimirReportFromAdapter', '1');" 
								value="<bean:message bundle="base" key="abm.button.imprimir"/>"/>			
		    	</td>				
			</tr>
		</table>
		<input type="hidden" name="name"  value="<bean:write name='comparacionAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	   	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
		<logic:present name="irA">
			<script type="text/javascript">document.location = document.URL + '#<bean:write name="irA"/>';</script>
		</logic:present>
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- comparacionAdapter.jsp -->