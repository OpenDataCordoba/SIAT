<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/BuscarTipoCob.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="bal" key="bal.tipoCobSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="tipoCobSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="bal" key="bal.tipoCob.label"/>
					</logic:equal>
					<logic:notEqual name="tipoCobSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="bal" key="bal.tipoCobSearchPage.legend"/>
					</logic:notEqual>		
				</p>
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
				<td><label><bean:message bundle="bal" key="bal.tipoCob.codColumna.label"/>: </label></td>
				<td class="normal"><html:text name="tipoCobSearchPageVO" property="tipoCob.codColumna" size="10" maxlength="10" styleClass="datos" /></td>
		
				<td><label><bean:message bundle="bal" key="bal.tipoCob.descripcion.label"/>: </label></td>
				<td class="normal"><html:text name="tipoCobSearchPageVO" property="tipoCob.descripcion" size="20" maxlength="60" styleClass="datos" /></td>
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
		<logic:equal name="tipoCobSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="tipoCobSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="tipoCobSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th width="1">&nbsp;</th> <!-- Activar/Desactivar -->
							</logic:notEqual>
							<th align="left"><bean:message bundle="bal" key="bal.tipoCob.orden.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.tipoCob.codColumna.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.tipoCob.descripcion.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.partida.label"/></th>
							<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
						</tr>
							
						<logic:iterate id="TipoCobVO" name="tipoCobSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="tipoCobSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="${Bean}VO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="tipoCobSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="tipoCobSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="TipoCobVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="TipoCobVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="tipoCobSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="TipoCobVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="TipoCobVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="TipoCobVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="tipoCobSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
									<!-- Eliminar-->								
									<td>
										<logic:equal name="tipoCobSearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="TipoCobVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="TipoCobVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="TipoCobVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="tipoCobSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>									
									<td>
									<!-- Activar -->
										<logic:notEqual name="TipoCobVO" property="estado.esActivo" value="true">
												<logic:equal name="tipoCobSearchPageVO" property="activarEnabled" value="enabled">
													<logic:equal name="TipoCobVO" property="activarEnabled" value="enabled">
														<a style="cursor: pointer; cursor: hand;" onclick="submitForm('activar', '<bean:write name="TipoCobVO" property="id" bundle="base" formatKey="general.format.id"/>');">
															<img title="<bean:message bundle="base" key="abm.button.activar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/activar0.gif"/>
														</a>
													</logic:equal> 
													<logic:notEqual name="TipoCobVO" property="activarEnabled" value="enabled">
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
													</logic:notEqual>
												</logic:equal>
												<logic:notEqual name="tipoCobSearchPageVO" property="activarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
												</logic:notEqual>
											</logic:notEqual> 
										<!-- Desactivar -->
											<logic:equal name="TipoCobVO" property="estado.esActivo" value="true">
												<logic:equal name="tipoCobSearchPageVO" property="desactivarEnabled" value="enabled">
													<logic:equal name="TipoCobVO" property="desactivarEnabled" value="enabled">
														<a style="cursor: pointer; cursor: hand;" onclick="submitForm('desactivar', '<bean:write name="TipoCobVO" property="id" bundle="base" formatKey="general.format.id"/>');">
															<img title="<bean:message bundle="base" key="abm.button.desactivar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar0.gif"/>
														</a>
													</logic:equal>
													<logic:notEqual name="TipoCobVO" property="desactivarEnabled" value="enabled">
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
													</logic:notEqual>
												</logic:equal>
												<logic:notEqual name="tipoCobSearchPageVO" property="desactivarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
												</logic:notEqual>										
											</logic:equal>
									</td>
							</logic:notEqual>

								<td><bean:write name="TipoCobVO" property="ordenView" />&nbsp;</td>								
								<td><bean:write name="TipoCobVO" property="codColumna" />&nbsp;</td>
								<td><bean:write name="TipoCobVO" property="descripcion" />&nbsp;</td>
								<td><bean:write name="TipoCobVO" property="partida.desPartidaView" />&nbsp;</td>
								<td><bean:write name="TipoCobVO" property="estado.value"/>&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="tipoCobSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="tipoCobSearchPageVO" property="listResult">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
						<tr><td align="center">
							<bean:message bundle="base" key="base.resultadoVacio"/>
						</td></tr>
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
			<logic:equal name="tipoCobSearchPageVO" property="viewResult" value="true">
				<td align="right">
  	    			<logic:equal name="tipoCobSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="tipoCobSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="tipoCobSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="tipoCobSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="tipoCobSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
							</logic:equal>
					</logic:equal>
				</td>				
			</logic:equal>
		</tr>
	</table>
	<input type="hidden" name="name"  value="<bean:write name='tipoCobSearchPageVO' property='name'/>" id="name"/>
   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
		
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->