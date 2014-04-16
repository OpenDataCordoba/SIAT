<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/BuscarCompensacion.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.compensacionSearchPage.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p><bean:message bundle="bal" key="bal.compensacionSearchPage.legend"/></p>
					
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
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal">
						<html:select name="compensacionSearchPageVO" property="recurso.id" styleClass="select" >
							<bean:define id="includeRecursoList" name="compensacionSearchPageVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="compensacionSearchPageVO" property="recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
				    </tr>
				    <tr>
						<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
					    <td class="normal" colspan="3">
						<html:text name="compensacionSearchPageVO" property="compensacion.cuenta.numeroCuenta" size="20"/>
						<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
							<bean:message bundle="bal" key="bal.compensacion.button.buscarCuenta"/>
						</html:button>
						</td>	
				    </tr>
				    <tr>
						<td><label>&nbsp;<bean:message bundle="base" key="base.estado.label"/>: </label></td>
						<td class="normal">
						<html:select name="compensacionSearchPageVO" property="compensacion.estadoCom.id" styleClass="select" >
							<html:optionsCollection name="compensacionSearchPageVO" property="listEstadoCom" label="descripcion" value="id" />
						</html:select>
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
		<logic:equal name="compensacionSearchPageVO" property="viewResult" value="true">		
			<logic:notEmpty  name="compensacionSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
	                	<tr>
	                		<logic:equal name="compensacionSearchPageVO" property="modoSeleccionar" value="true">
		                		<logic:equal name="compensacionSearchPageVO" property="paramSeleccionPorLista" value="false">
									<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
								</logic:equal>
								<logic:equal name="compensacionSearchPageVO" property="paramSeleccionPorLista" value="true">
									<th align="center">
						       			<br>
						       			&nbsp;					
						       			<input type="checkbox" onclick="changeChk('filter', 'listIdCompensacionSelected', this)" id="checkAll"/>
						       		</th> <!-- Ver/Seleccionar -->
								</logic:equal>
							</logic:equal>
							<logic:notEqual name="compensacionSearchPageVO" property="modoSeleccionar" value="true">
							<th width="1">&nbsp;</th> <!-- Ver -->
							<th width="1">&nbsp;</th> <!-- Modificar -->
							<th width="1">&nbsp;</th> <!-- Eliminar -->
							<th width="1">&nbsp;</th> <!-- Listo para Folio -->
							</logic:notEqual>
						  	<th align="left"><bean:message bundle="bal" key="bal.compensacion.fechaAlta.label"/></th>
						  	<th align="left"><bean:message bundle="bal" key="bal.compensacion.descripcion.label"/></th>
  							<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
						</tr>
						
						<logic:iterate id="CompensacionVO" name="compensacionSearchPageVO" property="listResult">
							<tr>						
								<!-- Ver/Seleccionar -->
								<td>
									<logic:notEqual name="compensacionSearchPageVO" property="modoSeleccionar" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="CompensacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:notEqual>
									<logic:equal name="compensacionSearchPageVO" property="modoSeleccionar" value="true">
										<logic:equal name="compensacionSearchPageVO" property="paramSeleccionPorLista" value="false">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="CompensacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:equal name="compensacionSearchPageVO" property="paramSeleccionPorLista" value="true">		
											<!-- Seleccionar -->
		                          			<html:multibox name="compensacionSearchPageVO" property="listIdCompensacionSelected" >
		                         			   	<bean:write name="CompensacionVO" property="id" bundle="base" formatKey="general.format.id"/>
			                                </html:multibox>
           								</logic:equal>
									</logic:equal>									
								</td>
								<logic:notEqual name="compensacionSearchPageVO" property="modoSeleccionar" value="true">								
								<td>
									<!-- Modificar-->								
									<logic:equal name="compensacionSearchPageVO" property="modificarEnabled" value="enabled">									
										<logic:equal name="CompensacionVO" property="modificarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="CompensacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="CompensacionVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="compensacionSearchPageVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</td>
	
								<td>
									<!-- Eliminar-->								
									<logic:equal name="compensacionSearchPageVO" property="eliminarEnabled" value="enabled">		
										<logic:equal name="CompensacionVO" property="eliminarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="CompensacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="CompensacionVO" property="eliminarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>									
									</logic:equal>							
									<logic:notEqual name="compensacionSearchPageVO" property="eliminarEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</td>
								<td>
									<logic:equal name="CompensacionVO" property="paramEnviado" value="false">
									<!-- Pasar a listo para Folio-->								
									<logic:equal name="compensacionSearchPageVO" property="enviarEnabled" value="enabled">		
										<logic:equal name="CompensacionVO" property="enviarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('enviar', '<bean:write name="CompensacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.enviar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/enviar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="CompensacionVO" property="enviarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/enviar1.gif"/>
										</logic:notEqual>									
									</logic:equal>							
									<logic:notEqual name="compensacionSearchPageVO" property="enviarEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/enviar1.gif"/>
									</logic:notEqual>
									</logic:equal>							
									<logic:equal name="CompensacionVO" property="paramEnviado" value="true">
									<!-- Volver a estado Creado-->								
									<logic:equal name="compensacionSearchPageVO" property="devolverEnabled" value="enabled">		
										<logic:equal name="CompensacionVO" property="devolverEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('devolver', '<bean:write name="CompensacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.devolver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/devolver0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="CompensacionVO" property="devolverEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/devolver1.gif"/>
										</logic:notEqual>									
									</logic:equal>							
									<logic:notEqual name="compensacionSearchPageVO" property="devolverEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/devolver1.gif"/>
									</logic:notEqual>
									</logic:equal>							
								</td>
							</logic:notEqual>								
						  	    <td><bean:write name="CompensacionVO" property="fechaAltaView"/>&nbsp;</td>
   						  	    <td><bean:write name="CompensacionVO" property="descripcion"/>&nbsp;</td>
						 		<td><bean:write name="CompensacionVO" property="estadoCom.descripcion"/>&nbsp;</td>
							</tr>
						</logic:iterate>
					<tr>
						<td class="paginador" align="center" colspan="11">
							<bean:define id="pager" name="compensacionSearchPageVO"/>
							<%@ include file="/base/pager.jsp" %>
						</td>
					</tr>
				</tbody>
			</table>
		</logic:notEmpty>
		
		<logic:empty name="compensacionSearchPageVO" property="listResult">
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
			<logic:equal name="compensacionSearchPageVO" property="viewResult" value="true">
    			<td align="right">
				<logic:equal name="compensacionSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="compensacionSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="compensacionSearchPageVO" property="modoSeleccionar" value="true">
						<logic:equal name="compensacionSearchPageVO" property="paramSeleccionPorLista" value="false">									
	  	    				<logic:equal name="compensacionSearchPageVO" property="agregarEnSeleccion" value="true">
								<bean:define id="agregarEnabled" name="compensacionSearchPageVO" property="agregarEnabled"/>
								<input type="button" <%=agregarEnabled%> class="boton" 
									onClick="submitForm('agregar', '0');" 
									value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
							</logic:equal>
						</logic:equal>
						<logic:equal name="compensacionSearchPageVO" property="paramSeleccionPorLista" value="true">									
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('incluir', '');">
								<bean:message bundle="bal" key="bal.compensacionSearchPage.adm.button.incluir"/>
							</html:button>
						</logic:equal>
					</logic:equal>
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
	<input type="hidden" name="name" value="<bean:write name='compensacionSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>	

	<input type="text" style="display:none"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->		
