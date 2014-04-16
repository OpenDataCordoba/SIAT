<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" src="<%= request.getContextPath()%>/base/calculator.js"></script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarPlaFueDat.do">
		<script>
			function posicionar(){
				var div = document.getElementById("listaPendientes");
				var elementos = div.getElementsByTagName("input");
				if (elementos.length>0){
					for (i=0;ele=elementos[i];i++){
						var scrollPos=ele.offsetParent.offsetTop;
						var altoStr = div.style.height.toString();
						var alto=altoStr.substring(0,altoStr.length-2)*1;
						if (alto < scrollPos){
							div.scrollTop=ele.offsetParent.offsetTop-(alto/2);
						}
						
						div.focus();
						ele.focus();
						break;
					}
					
				}
			}
		</script>
		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.plaFueDatAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<input type="button" class="boton" onclick="submitForm('volver', '<bean:write name="plaFueDatAdapterVO" property="plaFueDat.ordenControl.idView" />');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"/>					
				</td>
			</tr>
		</table>
		
		<!-- PlaFueDat -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.plaFueDat.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="ef" key="ef.fuenteInfo.label"/>: </label></td>
					<td class="normal"><bean:write name="plaFueDatAdapterVO" property="plaFueDat.fuenteInfo.nombreFuente"/></td>
					
					<td><label><bean:message bundle="ef" key="ef.plaFueDat.observacion.label"/>: </label></td>
					<td class="normal"><bean:write name="plaFueDatAdapterVO" property="plaFueDat.observacion" /></td>
				</tr>
												
				<tr>
					<td colspan="4"> 
						<bean:define id="modificarEncabezadoEnabled" name="plaFueDatAdapterVO" property="modificarEncabezadoEnabled"/>
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
							'<bean:write name="plaFueDatAdapterVO" property="plaFueDat.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>		
					</td>
				</tr>
			</table>
		</fieldset>	
		<!-- PlaFueDat -->
				
		<!-- PlaFueDatCol -->	
		<fieldset>
			<legend>
				<a onclick="toggle(this, 'bloquePlaFueDatCol')" style="cursor: pointer;"> (+) &nbsp; </a> 
				<bean:message bundle="ef" key="ef.plaFueDatCol.legend"/>
			</legend>
			
			<span id="bloquePlaFueDatCol" style="display:yes">
			
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="ef" key="ef.plaFueDat.listPlaFueDatCol.label"/></caption>
			    	<tbody>
						<logic:notEmpty  name="plaFueDatAdapterVO" property="plaFueDat.listPlaFueDatCol">	    	
					    	<tr>
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th align="left"><bean:message bundle="ef" key="ef.plaFueDatCol.campo.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.plaFueDatCol.orden.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.plaFueDatCol.oculta.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.plaFueDatCol.sumaEnTotal.label"/></th>						
							</tr>
							<logic:iterate id="PlaFueDatColVO" name="plaFueDatAdapterVO" property="plaFueDat.listPlaFueDatCol">
					
								<tr>
									
									<!-- Modificar-->								
									<td>
										<logic:equal name="plaFueDatAdapterVO" property="modificarPlaFueDatColEnabled" value="enabled">
											<logic:equal name="PlaFueDatColVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPlaFueDatCol', '<bean:write name="PlaFueDatColVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="PlaFueDatColVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="plaFueDatAdapterVO" property="modificarPlaFueDatColEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
									
									<!-- Eliminar-->								
									<td>
										<logic:equal name="plaFueDatAdapterVO" property="eliminarPlaFueDatColEnabled" value="enabled">
											<logic:equal name="PlaFueDatColVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPlaFueDatCol', '<bean:write name="PlaFueDatColVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="PlaFueDatColVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="plaFueDatAdapterVO" property="eliminarPlaFueDatColEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
									<td><bean:write name="PlaFueDatColVO" property="colName"/>&nbsp;</td>
									<td><bean:write name="PlaFueDatColVO" property="ordenView"/>&nbsp;</td>
									<td><bean:write name="PlaFueDatColVO" property="oculta.value"/>&nbsp;</td>
									<td><bean:write name="PlaFueDatColVO" property="sumaEnTotal.value"/>&nbsp;</td>
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty  name="plaFueDatAdapterVO" property="plaFueDat.listPlaFueDatCol">
							<tr><td align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
							</td></tr>
						</logic:empty>
						<tr>
							<td align="right" colspan="10">
								<logic:equal name="plaFueDatAdapterVO" property="agregarPlaFueDatColBussEnabled" value="true">
			   	    				<bean:define id="agregarEnabled" name="plaFueDatAdapterVO" property="agregarPlaFueDatColEnabled"/>
									<input type="button" <%=agregarEnabled%> class="boton" 
										onClick="submitForm('agregarPlaFueDatCol', '<bean:write name="plaFueDatAdapterVO" property="plaFueDat.id" bundle="base" formatKey="general.format.id"/>');" 
										value="<bean:message bundle="base" key="abm.button.agregar"/>"
									/>
								</logic:equal>
							</td>
						</tr>
					</tbody>
				</table>
			</span>
		</fieldset>		
		<!-- PlaFueDatCol -->
				
		<!-- planilla -->
		<logic:equal name="plaFueDatAdapterVO" property="verPlanilla" value="true">
		
			<fieldset width="100%">
				<legend>
					<a onclick="toggle(this, 'bloquePlanilla')" style="cursor: pointer;"> (+) &nbsp; </a> 
					<bean:message bundle="ef" key="ef.plaFueDat.Planilla.title"/>
				</legend>
				
				<span id="bloquePlanilla" style="display:yes">
					<div id="listaPendientes" class="scrolable" style="height: 450px; width: 680px;">

						<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
		               		<tr style="	font-weight:bold;color:white;background-color:#006699;padding: 10px 15px 5px 30px;text-align: center;font-size:125%;">
		               			<td colspan="15"><bean:write name="plaFueDatAdapterVO" property="plaFueDat.fuenteInfo.nombreFuente"/></td>
		               		</tr>	
						
				    	<tbody>
						    	<tr>
									<th width="1">&nbsp;</th> <!-- Modificar -->
									<th width="1">&nbsp;</th> <!-- Eliminar -->
									<th width="1"><bean:message bundle="ef" key="ef.plaFueDat.periodo.label"/></th> <!-- Periodo -->
								<!-- genera la cabecera con la lista de PlaFueDatCol -->	
									<logic:iterate id="PlaFueDatColVO" name="plaFueDatAdapterVO" property="plaFueDat.listPlaFueDatCol">
										<logic:equal name="PlaFueDatColVO" property="oculta.id" value="0">
											<th align="left">
												<bean:write name="PlaFueDatColVO" property="colName"/>
											</th>
										</logic:equal>	    	
									</logic:iterate>
									<th align="left" width="16">
										<bean:message bundle="ef" key="ef.plaFueDatAdapter.total.label"/>
									</th>							
								</tr>
								
								
								<!-- itera los detalles -->
								<bean:define id="idModificar" name="plaFueDatAdapterVO" property="plaFueDatDet.idView"/>
								<logic:iterate id="PlaFueDatDetVO" name="plaFueDatAdapterVO" property="plaFueDat.listPlaFueDatDet">
															
									<tr>
										<logic:notEqual name="PlaFueDatDetVO" property="idView" value="<%=idModificar.toString() %>">
									
											<!-- Modificar-->								
											<td>
												<logic:equal name="plaFueDatAdapterVO" property="modificarPlaFueDatDetEnabled" value="enabled">
													<logic:equal name="PlaFueDatDetVO" property="modificarEnabled" value="enabled">
														<a style="cursor: pointer; cursor: hand;" onclick="submitForm('irModificarPlaFueDatDet', '<bean:write name="PlaFueDatDetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
															<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
														</a>
													</logic:equal>	
													<logic:notEqual name="PlaFueDatDetVO" property="modificarEnabled" value="enabled">
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
													</logic:notEqual>
												</logic:equal>
												<logic:notEqual name="plaFueDatAdapterVO" property="modificarPlaFueDatDetEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
												</logic:notEqual>
											</td>
											
											<!-- Eliminar-->								
											<td>
												<logic:equal name="plaFueDatAdapterVO" property="eliminarPlaFueDatDetEnabled" value="enabled">
													<logic:equal name="PlaFueDatDetVO" property="eliminarEnabled" value="enabled">
														<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPlaFueDatDet', '<bean:write name="PlaFueDatDetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
															<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
														</a>
													</logic:equal>	
													<logic:notEqual name="PlaFueDatDetVO" property="eliminarEnabled" value="enabled">
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
													</logic:notEqual>
												</logic:equal>
												<logic:notEqual name="plaFueDatAdapterVO" property="eliminarPlaFueDatDetEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
												</logic:notEqual>
											</td>
											
											<td>
												<!-- periodo/anio -->
												<bean:write name="PlaFueDatDetVO" property="periodoView"/>/
												<bean:write name="PlaFueDatDetVO" property="anioView"/>
											</td>
											
											
											<logic:present name="PlaFueDatDetVO" property="col1">
												<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatDetVO" property="col1" bundle="base" formatKey="general.format.currency"/></td>
											</logic:present>
				
											<logic:present name="PlaFueDatDetVO" property="col2" >
												<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatDetVO" property="col2" bundle="base" formatKey="general.format.currency"/></td>
											</logic:present>
				
											<logic:present name="PlaFueDatDetVO" property="col3" >
												<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatDetVO" property="col3" bundle="base" formatKey="general.format.currency"/></td>
											</logic:present>
				
											<logic:present name="PlaFueDatDetVO" property="col4" >
												<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatDetVO" property="col4" bundle="base" formatKey="general.format.currency"/></td>
											</logic:present>
																									
											<logic:present name="PlaFueDatDetVO" property="col5" >
												<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatDetVO" property="col5" bundle="base" formatKey="general.format.currency"/></td>
											</logic:present>
				
											<logic:present name="PlaFueDatDetVO" property="col6" >
												<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatDetVO" property="col6" bundle="base" formatKey="general.format.currency"/></td>
											</logic:present>
				
											<logic:present name="PlaFueDatDetVO" property="col7" >
												<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatDetVO" property="col7" bundle="base" formatKey="general.format.currency"/></td>
											</logic:present>
				
											<logic:present name="PlaFueDatDetVO" property="col8" >
												<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatDetVO" property="col8" bundle="base" formatKey="general.format.currency"/></td>
											</logic:present>
											
											<logic:present name="PlaFueDatDetVO" property="col9" >
												<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatDetVO" property="col9" bundle="base" formatKey="general.format.currency"/></td>
											</logic:present>
				
											<logic:present name="PlaFueDatDetVO" property="col10" >
												<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatDetVO" property="col10" bundle="base" formatKey="general.format.currency"/></td>
											</logic:present>
											
											<logic:present name="PlaFueDatDetVO" property="col11" >
												<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatDetVO" property="col11" bundle="base" formatKey="general.format.currency"/></td>
											</logic:present>
				
											<logic:present name="PlaFueDatDetVO" property="col12" >
												<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatDetVO" property="col12" bundle="base" formatKey="general.format.currency"/></td>
											</logic:present>																																										
									</logic:notEqual>
									
									<logic:equal name="PlaFueDatDetVO" property="idView" value="<%=idModificar.toString() %>">
											<!-- Modificar-->								
											<td>
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPlaFueDatDet', '<bean:write name="plaFueDatAdapterVO" property="plaFueDat.idView"/>');">
															<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/guardar.gif"/>
												</a>
											</td>
											
											<!-- Eliminar-->								
											<td><img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/></td>
											
											<td>
												<!-- periodo/anio -->
												<bean:write name="PlaFueDatDetVO" property="periodoView"/>/
												<bean:write name="PlaFueDatDetVO" property="anioView"/>
											</td>
											
											
											<logic:present name="PlaFueDatDetVO" property="col1">
												<td nowrap="nowrap">
													<html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col1" styleId="plaFueDatDet.col1" size="10"/>
													<a style="cursor: pointer; cursor: hand;" onclick="viewCalc('plaFueDatDet.col1');" >
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/calc0.gif"/>
													</a>
												</td>
											</logic:present>
				
											<logic:present name="PlaFueDatDetVO" property="col2">											
												<td nowrap="nowrap"><html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col2" styleId="plaFueDatDet.col2" size="10"/>
													<a style="cursor: pointer; cursor: hand;" onclick="viewCalc('plaFueDatDet.col2');" >
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/calc0.gif"/>
													</a>												
												</td>
												
											</logic:present>
				
											<logic:present name="PlaFueDatDetVO" property="col3" >
												<td nowrap="nowrap"><html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col3" styleId="plaFueDatDet.col3" size="10"/>
													<a style="cursor: pointer; cursor: hand;" onclick="viewCalc('plaFueDatDet.col3');" >
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/calc0.gif"/>
													</a>												
												</td>
											</logic:present>
				
											<logic:present name="PlaFueDatDetVO" property="col4" >
												<td nowrap="nowrap">
													<html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col4" styleId="plaFueDatDet.col4" size="10"/>
													<a style="cursor: pointer; cursor: hand;" onclick="viewCalc('plaFueDatDet.col4');" >
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/calc0.gif"/>
													</a>												
												</td>
											</logic:present>
																									
											<logic:present name="PlaFueDatDetVO" property="col5" >
												<td nowrap="nowrap">
													<html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col5" styleId="plaFueDatDet.col5" size="10"/>
													<a style="cursor: pointer; cursor: hand;" onclick="viewCalc('plaFueDatDet.col5');" >
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/calc0.gif"/>
													</a>												
												</td>
											</logic:present>
				
											<logic:present name="PlaFueDatDetVO" property="col6" >
												<td nowrap="nowrap">
													<html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col6" styleId="plaFueDatDet.col6" size="10"/>
													<a style="cursor: pointer; cursor: hand;" onclick="viewCalc('plaFueDatDet.col6');" >
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/calc0.gif"/>
													</a>												
												</td>
											</logic:present>
				
											<logic:present name="PlaFueDatDetVO" property="col7" >
												<td nowrap="nowrap">
													<html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col7" styleId="plaFueDatDet.col7" size="10"/>
													<a style="cursor: pointer; cursor: hand;" onclick="viewCalc('plaFueDatDet.col7');" >
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/calc0.gif"/>
													</a>												
												</td>
											</logic:present>
				
											<logic:present name="PlaFueDatDetVO" property="col8" >
												<td nowrap="nowrap">
													<html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col8" styleId="plaFueDatDet.col8" size="10"/>
													<a style="cursor: pointer; cursor: hand;" onclick="viewCalc('plaFueDatDet.col8');" >
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/calc0.gif"/>
													</a>												
												</td>
											</logic:present>
											
											<logic:present name="PlaFueDatDetVO" property="col9" >
												<td nowrap="nowrap">
													<html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col9" styleId="plaFueDatDet.col9" size="10"/>
													<a style="cursor: pointer; cursor: hand;" onclick="viewCalc('plaFueDatDet.col9');" >
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/calc0.gif"/>
													</a>												
												</td>
											</logic:present>
				
											<logic:present name="PlaFueDatDetVO" property="col10" >
												<td nowrap="nowrap">
													<html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col10" styleId="plaFueDatDet.col10" size="10"/>
													<a style="cursor: pointer; cursor: hand;" onclick="viewCalc('plaFueDatDet.col10');" >
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/calc0.gif"/>
													</a>												
												</td>
											</logic:present>
											
											<logic:present name="PlaFueDatDetVO" property="col11" >
												<td nowrap="nowrap">
													<html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col11" styleId="plaFueDatDet.col11" size="10"/>
													<a style="cursor: pointer; cursor: hand;" onclick="viewCalc('plaFueDatDet.col11');" >
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/calc0.gif"/>
													</a>												
												</td>
											</logic:present>
				
											<logic:present name="PlaFueDatDetVO" property="col12" >
												<td nowrap="nowrap">
													<html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col12" styleId="plaFueDatDet.col12" size="10"/>
													<a style="cursor: pointer; cursor: hand;" onclick="viewCalc('plaFueDatDet.col12');" >
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/calc0.gif"/>
													</a>												
												</td>
											</logic:present>																																																															
										</logic:equal>					
											
										<td class="datos" align="right"><bean:write name="PlaFueDatDetVO" property="total" bundle="base" formatKey="general.format.currency"/></td>
									</tr>
								</logic:iterate>
								
								<tr>
									<bean:define id="cantPlaFueDatCol" name="plaFueDatAdapterVO" property="plaFueDat.cantPlaFueDatColVisible"/>
									<td colspan="3>" align="right">
										<bean:message bundle="ef" key="ef.plaFueDatAdapter.total.label"/>:
									</td>
									<logic:present name="plaFueDatAdapterVO" property="plaFueDat.totalCol1">
												<td class="datos" nowrap="nowrap" ><bean:write name="plaFueDatAdapterVO" property="plaFueDat.totalCol1" bundle="base" formatKey="general.format.currency"/></td>
									</logic:present>
									<logic:present name="plaFueDatAdapterVO" property="plaFueDat.totalCol2" >
												<td class="datos" nowrap="nowrap" ><bean:write name="plaFueDatAdapterVO" property="plaFueDat.totalCol2" bundle="base" formatKey="general.format.currency"/></td>
									</logic:present>
									<logic:present name="plaFueDatAdapterVO" property="plaFueDat.totalCol3" >
												<td class="datos" nowrap="nowrap" ><bean:write name="plaFueDatAdapterVO" property="plaFueDat.totalCol3" bundle="base" formatKey="general.format.currency"/></td>
									</logic:present>
									<logic:present name="plaFueDatAdapterVO" property="plaFueDat.totalCol4" >
												<td class="datos" nowrap="nowrap" ><bean:write name="plaFueDatAdapterVO" property="plaFueDat.totalCol4" bundle="base" formatKey="general.format.currency"/></td>
									</logic:present>
									<logic:present name="plaFueDatAdapterVO" property="plaFueDat.totalCol5" >
												<td class="datos" nowrap="nowrap" ><bean:write name="plaFueDatAdapterVO" property="plaFueDat.totalCol5" bundle="base" formatKey="general.format.currency"/></td>
									</logic:present>
									<logic:present name="plaFueDatAdapterVO" property="plaFueDat.totalCol6" >
												<td class="datos" nowrap="nowrap" ><bean:write name="plaFueDatAdapterVO" property="plaFueDat.totalCol6" bundle="base" formatKey="general.format.currency"/></td>
									</logic:present>
									<logic:present name="plaFueDatAdapterVO" property="plaFueDat.totalCol7" >
												<td class="datos" nowrap="nowrap" ><bean:write name="plaFueDatAdapterVO" property="plaFueDat.totalCol7" bundle="base" formatKey="general.format.currency"/></td>
									</logic:present>
									<logic:present name="plaFueDatAdapterVO" property="plaFueDat.totalCol8" >
												<td class="datos" nowrap="nowrap" ><bean:write name="plaFueDatAdapterVO" property="plaFueDat.totalCol8" bundle="base" formatKey="general.format.currency"/></td>
									</logic:present>
									<logic:present name="plaFueDatAdapterVO" property="plaFueDat.totalCol9" >
												<td class="datos" nowrap="nowrap" ><bean:write name="plaFueDatAdapterVO" property="plaFueDat.totalCol9" bundle="base" formatKey="general.format.currency"/></td>
									</logic:present>
									<logic:present name="plaFueDatAdapterVO" property="plaFueDat.totalCol10" >
												<td class="datos" nowrap="nowrap" ><bean:write name="plaFueDatAdapterVO" property="plaFueDat.totalCol10" bundle="base" formatKey="general.format.currency"/></td>
									</logic:present>
									<logic:present name="plaFueDatAdapterVO" property="plaFueDat.totalCol11" >
												<td class="datos" nowrap="nowrap" ><bean:write name="plaFueDatAdapterVO" property="plaFueDat.totalCol11" bundle="base" formatKey="general.format.currency"/></td>
									</logic:present>
									<logic:present name="plaFueDatAdapterVO" property="plaFueDat.totalCol12" >
												<td class="datos" nowrap="nowrap" ><bean:write name="plaFueDatAdapterVO" property="plaFueDat.totalCol12" bundle="base" formatKey="general.format.currency"/></td>
									</logic:present>
									<td class="datos" align="right" nowrap="nowrap"><bean:write name="plaFueDatAdapterVO" property="plaFueDat.total" bundle="base" formatKey="general.format.currency"/></td>
								</tr>															
						</table>
					</div>
				</span>
			</fieldset>
		</logic:equal>		
		<!-- FIN planilla -->		
		
		
		<table class="tablabotones">
			<tr>				
				<td align="left">
		   			<input type="button" class="boton" onclick="submitForm('volver', '<bean:write name="plaFueDatAdapterVO" property="plaFueDat.ordenControl.idView" />');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"/>
				</td>  
				<td align="left">
					<bean:define id="imprimirPlaFueDatEnabled" name="plaFueDatAdapterVO" property="imprimirPlaFueDatEnabled"/>
						<input type="button" class="boton" <%=imprimirPlaFueDatEnabled%> onClick="submitForm('imprimir', '');" 
							value="<bean:message bundle="base" key="abm.button.imprimir"/>"/>		
			 	</td>     
				<logic:equal name="plaFueDatAdapterVO" property="verPlanilla" value="false">
					<logic:greaterThan name="plaFueDatAdapterVO" property="plaFueDat.cantPlaFueDatCol" value="0">
						<td align="right" width="50%">
							<bean:define id="planillaEnabled" name="plaFueDatAdapterVO" property="planillaEnabled"/>
				   			<input type="button" <%=planillaEnabled%> class="boton" onclick="submitForm('generarModificarPlanilla', '');"
				   				value="<bean:message bundle="ef" key="ef.plaFueDatAdapter.button.planilla"/>"/>
						</td>
					</logic:greaterThan >
					<logic:equal name="plaFueDatAdapterVO" property="plaFueDat.cantPlaFueDatCol" value="0">
						<logic:greaterThan name="plaFueDatAdapterVO" property="plaFueDat.cantPlaFueDatDet" value="0">
							<td align="right" width="50%">
								<bean:define id="planillaEnabled" name="plaFueDatAdapterVO" property="planillaEnabled"/>
					   			<input type="button" <%=planillaEnabled%> class="boton" onclick="submitForm('generarModificarPlanilla', '');"
					   				value="<bean:message bundle="ef" key="ef.plaFueDatAdapter.button.verPlanilla"/>"/>						
				   			</td>	
						</logic:greaterThan>
					</logic:equal>
				</logic:equal>

				<logic:equal name="plaFueDatAdapterVO" property="verPlanilla" value="true">
					<td align="right">
					
					
						<bean:define id="agregarPlaFueDatDetEnabled" name="plaFueDatAdapterVO" property="agregarPlaFueDatDetEnabled"/>
						<input type="button" class="boton" <%=agregarPlaFueDatDetEnabled%> onClick="submitForm('agregarPlaFueDatDet', 
							'<bean:write name="plaFueDatAdapterVO" property="plaFueDat.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="ef" key="ef.plaFueDatAdapter.button.agregarPeriodos"/>"/>		
					</td>								
				</logic:equal>
				
				<logic:greaterThan name="plaFueDatAdapterVO" property="plaFueDatDet.id" value="0">
					<td align="center">
			   			<input type="button" class="boton" onclick="submitForm('modificarPlaFueDatDet', '<bean:write name="plaFueDatAdapterVO" property="plaFueDat.idView"/>');"
			   				value="<bean:message bundle="ef" key="ef.plaFueDatAdapter.button.planilla.modificarRegistro"/>"/>
					</td>   	    			
				</logic:greaterThan>
				
			</tr>
		</table>
	   	<input type="hidden" name="name"  value="<bean:write name='plaFueDatAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
		<logic:present name="irA">
			<script type="text/javascript">document.location = document.URL + '#<bean:write name="irA"/>';</script>
		</logic:present>		
		<script>
			posicionar();
		</script>
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- plaFueDatAdapter.jsp -->