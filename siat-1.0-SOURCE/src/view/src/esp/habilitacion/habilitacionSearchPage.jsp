<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/esp/BuscarHabilitacion.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="esp" key="esp.habilitacionSearchPage.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p><bean:message bundle="esp" key="esp.habilitacionSearchPage.legend"/></p>
					
				</td>				
				<td align="right">
		 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>

		<!-- Filtro -->
		<fieldset>
			<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
				<table class="tabladatos">

					<tr>
						<!-- Recurso -->
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal" colspan="3">
						<html:select name="habilitacionSearchPageVO" property="habilitacion.recurso.id" styleClass="select" >
							<bean:define id="includeRecursoList" name="habilitacionSearchPageVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="habilitacionSearchPageVO" property="habilitacion.recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
						</td>	
					</tr>
					
					<tr>
						<!-- Cuenta -->
						<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
					    <td class="normal" colspan="3">
						<html:text name="habilitacionSearchPageVO" property="habilitacion.cuenta.numeroCuenta" size="20"/>
						<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
							<bean:message bundle="esp" key="esp.habilitacion.button.buscarCuenta"/>
						</html:button>
						</td>	
				    </tr>

					<tr>
						<!-- Descripcion -->
						<td><label><bean:message bundle="esp" key="esp.habilitacion.descripcion.label"/>: </label></td>
						<td class="normal"><html:text name="habilitacionSearchPageVO" property="habilitacion.descripcion" size="25" maxlength="20" /></td>
					</tr>
					
					<!-- Datos Persona -->	
					<bean:define id="personaVO" name="habilitacionSearchPageVO" property="titular"/>		
					 <tr>
					 	<td colspan="4">
							<table width="100%">
							 	<tr>
								    <!-- Ingreso de datos -->
								    <td class="normal"><label>(*)&nbsp;<bean:message bundle="pad" key="pad.persona.sexo.label"/>: </label>								
										<select name="titular.sexo.id" class="select">
											<option value="-1" <logic:equal name="personaVO" property="sexo.codigo" value="S">selected="selected"</logic:equal>>Seleccionar...</option>									
											
											<option value="1" <logic:equal name="personaVO" property="sexo.codigo" value="M">selected="selected"</logic:equal>>Masculino</option>
											
											<option value="0" <logic:equal name="personaVO" property="sexo.codigo" value="F">selected="selected" </logic:equal>>Femenino</option>
										</select>
										
										<label>(*)&nbsp;<bean:message bundle="pad" key="pad.documento.numero.ref"/>: </label> 
										<input type="text" name="titular.documento.numeroView" value='<bean:write name="personaVO" property="documento.numeroView"/>' />
											
									   	&nbsp;
									
									   	<html:button property="btnBuscarSolicitante"  styleClass="boton" onclick="submitForm('buscarPersonaSimple', '');">
											<bean:message bundle="pad" key="pad.button.buscarPersona"/>
									   	</html:button>
									   	&nbsp;
									   	<html:button property="btnBuscarSolicitante"  styleClass="boton" onclick="submitForm('limpiarPersona', '');">
											<bean:message bundle="base" key="abm.button.limpiar"/>
										</html:button>		   
									</td>							
							    </tr>
							  </table>
						 </td>
					 </tr>
					
						
					<logic:equal name="personaVO" property="personaBuscada" value="true">
						<!-- Errores -->
						<logic:equal name="personaVO" property="hasError" value="true">
							<tr>
								<td colspan="6">
									<table width="100%">
										<tr>
											<td class="normal" colspan="6">
												<ul class="error" id="errorsSearch">
													<logic:iterate id="valueError" name="personaVO" property="listErrorValues">
														<li>
													  		<bean:write name="valueError"/>
														</li>
													</logic:iterate>
												</ul>
											</td>
										</tr>
										
										<logic:equal name="personaVO" property="personaEncontrada" value="false">
											<tr>
												<td></td>
												<td></td>
												<td></td>
												<td>
													<div align="center">
														<html:button property="btnBuscarSolicitante"  styleClass="boton" onclick="submitForm('buscarPersona', '');">
															<bean:message bundle="pad" key="pad.button.busquedaAvanzada"/>
														</html:button>
													</div>
												</td>
											</tr>
										</logic:equal>
									</table>						
								</td>
							</tr>				
						</logic:equal>
						
						<logic:notEqual name="personaVO" property="hasError" value="true">
							<!-- Messages -->
							<tr>
								<td colspan="6">
									<table width="100%">
									
										<tr>
											<logic:equal name="personaVO" property="personaEncontrada" value="true">
												<%@ include file="/pad/persona/includePersonaReducida.jsp"%>
											</logic:equal>
										</tr>
										<tr>
											<td></td>
											<td></td>
											<td></td>
											<td>
												<div align="center">
													<html:button property="btnBuscarSolicitante"  styleClass="boton" onclick="submitForm('buscarPersona', '');">
														<bean:message bundle="pad" key="pad.button.busquedaAvanzada"/>
													</html:button>
												</div>
											</td>
										</tr>
									</table>						
								</td>
							</tr>
						</logic:notEqual>	
					</logic:equal>
				<!--Fin Datos Persona -->
				    
				</table>
				<table class="tabladatos">
					<tr>	
						<!-- Numero -->
						<td><label><bean:message bundle="esp" key="esp.habilitacion.numero.label"/>: </label></td>
						<td class="normal"><html:text name="habilitacionSearchPageVO" property="habilitacion.numeroView" size="10" maxlength="20"/></td>
						
						<!-- Anio -->
						<td><label><bean:message bundle="esp" key="esp.habilitacion.anio.label"/>: </label></td>
						<td class="normal"><html:text name="habilitacionSearchPageVO" property="habilitacion.anioView" size="10" maxlength="20" /></td>

						<!-- Tipo Habilitacion -->
						<td><label><bean:message bundle="esp" key="esp.tipoHab.label"/>: </label></td>
						<td class="normal">
							<html:select name="habilitacionSearchPageVO" property="habilitacion.tipoHab.id" styleClass="select">
								<html:optionsCollection name="habilitacionSearchPageVO" property="listTipoHab" label="descripcion" value="id" />
							</html:select>
						</td>
					</tr>
					<tr>
						<!-- Fecha Desde -->
						<td><label><bean:message bundle="esp" key="esp.habilitacionSearchPage.fechaDesde.label"/>: </label></td>
						<td class="normal">
							<html:text name="habilitacionSearchPageVO" property="fechaDesdeView" styleId="fechaDesdeView" size="10" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
					
						<!-- Fecha Hasta -->
						<td><label><bean:message bundle="esp" key="esp.habilitacionSearchPage.fechaHasta.label"/>: </label></td>
						<td class="normal">
							<html:text name="habilitacionSearchPageVO" property="fechaHastaView" styleId="fechaHastaView" size="10" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
					</tr>
				</table>
	
				<p align="center">
				  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
						<bean:message bundle="base" key="abm.button.limpiar"/>
					</html:button>
					&nbsp;
					<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('baseImprimir', '1');">
						<bean:message bundle="base" key="abm.button.imprimir"/>
					</html:button>			
					&nbsp;
			  		<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
						<bean:message bundle="base" key="abm.button.buscar"/>
					</html:button>
				</p>
		</fieldset>	
		<!-- Fin Filtro -->

	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="habilitacionSearchPageVO" property="viewResult" value="true">		
			<logic:notEmpty  name="habilitacionSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
	                	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="habilitacionSearchPageVO" property="modoSeleccionar" value="true">
							<th width="1">&nbsp;</th> <!-- Modificar -->
							<th width="1">&nbsp;</th> <!-- Eliminar -->
							<th width="1">&nbsp;</th> <!-- Entradas vendidas -->
							<th width="1">&nbsp;</th> <!-- Cambiar Estado-->
							</logic:notEqual>
  							<th align="left"><bean:message bundle="esp" key="esp.habilitacion.numero.label"/></th>
						  	<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
							<th align="left"><bean:message bundle="esp" key="esp.habilitacion.descripcion.label"/></th>
						  	<th align="left"><bean:message bundle="esp" key="esp.habilitacion.fechaHab.label"/></th>  							
						</tr>
						<logic:iterate id="HabilitacionVO" name="habilitacionSearchPageVO" property="listResult">
							<tr>						
								<!-- Ver/Seleccionar -->
								<td>
									<logic:notEqual name="habilitacionSearchPageVO" property="modoSeleccionar" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="HabilitacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:notEqual>
									<logic:equal name="habilitacionSearchPageVO" property="modoSeleccionar" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="HabilitacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>									
								</td>

								<logic:notEqual name="habilitacionSearchPageVO" property="modoSeleccionar" value="true">								
								<td>
									<!-- Modificar-->								
									<logic:equal name="habilitacionSearchPageVO" property="modificarEnabled" value="enabled">									
										<logic:equal name="HabilitacionVO" property="modificarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="HabilitacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="HabilitacionVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="habilitacionSearchPageVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</td>
	
								<td>
									<!-- Eliminar-->								
									<logic:equal name="habilitacionSearchPageVO" property="eliminarEnabled" value="enabled">		
										<logic:equal name="HabilitacionVO" property="eliminarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="HabilitacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="HabilitacionVO" property="eliminarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>									
									</logic:equal>							
									<logic:notEqual name="habilitacionSearchPageVO" property="eliminarEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</td>
								
								<td>
									<!-- Entradas vendidas-->								
									<logic:equal name="HabilitacionVO" property="tipoHab.codigo" value="EXT">
											<logic:equal name="habilitacionSearchPageVO" property="entVenExternaEnabled" value="enabled">	
												
												<logic:equal name="HabilitacionVO" property="entVenExternaEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('admEntVen', '<bean:write name="HabilitacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="esp" key="abm.button.entradasVendidas"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/entVenExt.gif"/>
													</a>
												</logic:equal>
											
											
												<logic:notEqual name="HabilitacionVO" property="entVenExternaEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/entVenExt1.gif"/>
												</logic:notEqual>
																				
										</logic:equal>							
										<logic:notEqual name="habilitacionSearchPageVO" property="entVenExternaEnabled" value="enabled">		
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/entVenExt1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<!-- Entradas vendidas hab. interna -->
									<logic:notEqual name="HabilitacionVO" property="tipoHab.codigo" value="EXT">								
										<logic:equal name="habilitacionSearchPageVO" property="entVenInternaEnabled" value="enabled">
										
											<logic:equal name="HabilitacionVO" property="entVenInternaEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('admEntVenInt', '<bean:write name="HabilitacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="esp" key="abm.button.entradasVendidasInt"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/entVenInt.gif"/>
												</a>
											</logic:equal>
											
											<logic:notEqual name="HabilitacionVO" property="entVenInternaEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/entVenInt1.gif"/>
											</logic:notEqual>
																			
										</logic:equal>							
										<logic:notEqual name="habilitacionSearchPageVO" property="entVenInternaEnabled" value="enabled">		
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/entVenInt1.gif"/>
										</logic:notEqual>
									</logic:notEqual>
								</td>
								<td>
									<!-- Modificar Estado-->								
										<logic:equal name="habilitacionSearchPageVO" property="cambiarEstadoEnabled" value="enabled">		
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('cambiarEstado', '<bean:write name="HabilitacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.cambiarEstado"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstadoHab0.gif"/>
											</a>
										</logic:equal>							
										<logic:notEqual name="habilitacionSearchPageVO" property="cambiarEstadoEnabled" value="enabled">		
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstadoHab1.gif"/>
										</logic:notEqual>
								</td>
							</logic:notEqual>								
  							    <td><bean:write name="HabilitacionVO" property="numeroView"/>/<bean:write name="HabilitacionVO" property="anioView"/></td>
						  	    <td><bean:write name="HabilitacionVO" property="recurso.desRecurso"/>&nbsp;</td>
								<logic:equal name="HabilitacionVO" property="tipoHab.codigo" value="EXT">								
	   						  	    <td><bean:write name="HabilitacionVO" property="cuenta.numeroCuenta"/>&nbsp;</td>
								</logic:equal>								
								<logic:notEqual name="HabilitacionVO" property="tipoHab.codigo" value="EXT">								
							  	    <td><bean:write name="HabilitacionVO" property="valoresCargados.descripcion"/>&nbsp;</td>
								</logic:notEqual>								
						  	    <td><bean:write name="HabilitacionVO" property="descripcion"/>&nbsp;</td>
   						  	    <td><bean:write name="HabilitacionVO" property="fechaHabView"/>&nbsp;</td>						 					
							</tr>
						</logic:iterate>
					<tr>
						<td class="paginador" align="center" colspan="12">
							<bean:define id="pager" name="habilitacionSearchPageVO"/>
							<%@ include file="/base/pager.jsp" %>
						</td>
					</tr>
				</tbody>
			</table>
		</logic:notEmpty>
		
		<logic:empty name="habilitacionSearchPageVO" property="listResult">
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
       	    	<tbody>
					<tr><td align="center">
							<bean:message bundle="base" key="base.resultadoVacio"/>
						</td>
					</tr>
				</tbody>
			</table>
		</logic:empty>
	</logic:equal>				
	</div>
	<!-- Fin Resultado Filtro -->
		
	<table class="tablabotones">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<logic:equal name="habilitacionSearchPageVO" property="viewResult" value="true">
    			<td align="right">
				<logic:equal name="habilitacionSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="habilitacionSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="habilitacionSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="habilitacionSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="habilitacionSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
						</logic:equal>
					</logic:equal>					
					</td>
						<td align="right">
						<html:button property="btnImprimir"  styleClass="boton" onclick="submitForm('imprimirHabSinEntVen', '1');">
							<bean:message bundle="esp" key="esp.button.imprimirHabSinEntVen"/>
						</html:button>
					</td>
			</logic:equal>
		</tr>
	</table>
		
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<input type="hidden" name="name" value="<bean:write name='habilitacionSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>	

	<input type="text" style="display:none"/>
	
	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->		
