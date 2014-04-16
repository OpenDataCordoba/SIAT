<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/BuscarEnvioOsiris.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.envioOsiris.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p><bean:message bundle="bal" key="bal.envioOsirisSearchPage.legend"/></p>		
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
					<!-- Fecha Balance Desde/Hasta -->
					<tr>
						<td><label><bean:message bundle="bal" key="bal.envioOsiris.idEnvioAfip.label"/>: </label></td>
						<td class="normal">
							<html:text name="envioOsirisSearchPageVO" property="envioOsiris.idEnvioAfipView"/>
						</td>
						<td><label><bean:message bundle="bal" key="bal.estadoEnvio.title"/>: </label></td>
						<td class="normal">
							<html:select name="envioOsirisSearchPageVO" property="envioOsiris.estadoEnvio.id">
								<html:optionsCollection name="envioOsirisSearchPageVO" property="listEstadoEnvio" label="desEstado" value="id"/>
							</html:select>
						</td>
					</tr>
					<tr>
						<td><label><bean:message bundle="bal" key="bal.envioOsirisSearchPage.fechaDesde.label"/>: </label></td>
						<td class="normal">
						<html:text name="envioOsirisSearchPageVO" property="fechaDesdeView" styleId="fechaDesdeView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>	
						<td><label><bean:message bundle="bal" key="bal.envioOsirisSearchPage.fechaHasta.label"/>: </label></td>
						<td class="normal">
						<html:text name="envioOsirisSearchPageVO" property="fechaHastaView" styleId="fechaHastaView" size="10" maxlength="10" styleClass="datos" />
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
		<logic:equal name="envioOsirisSearchPageVO" property="viewResult" value="true">		
			<logic:notEmpty  name="envioOsirisSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
	                	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<th width="1">&nbsp;</th> <!-- Generar Formularios de Declaraciones Juradas -->
							<th width="1">&nbsp;</th> <!-- Generar Archivo Transaccion -->
							<th width="1">&nbsp;</th> <!-- Cambiar estado del Envio -->
						  	<th align="left"><bean:message bundle="bal" key="bal.envioOsiris.fechaRegistroMulat.label"/></th>
						  	<th align="left"><bean:message bundle="bal" key="bal.envioOsiris.idEnvioAfip.label"/></th>
						  	<th align="left"><bean:message bundle="bal" key="bal.estadoEnvio.title"/></th>
  							<th align="left"><bean:message bundle="bal" key="bal.envioOsiris.cantidadPagos.label"/></th>
  							<th align="left"><bean:message bundle="bal" key="bal.envioOsiris.canDecJur.label"/></th>
						</tr>
						
						<logic:iterate id="EnvioVO" name="envioOsirisSearchPageVO" property="listResult">
							<tr>						
								<!-- Ver/Seleccionar -->
								<td>
									<logic:notEqual name="envioOsirisSearchPageVO" property="modoSeleccionar" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="EnvioVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:notEqual>
									<logic:equal name="envioOsirisSearchPageVO" property="modoSeleccionar" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="EnvioVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>									
								</td>																
								<!-- GenerarListForDecJur -->
								<td>
									<logic:equal name="envioOsirisSearchPageVO" property="generarForDecJurEnabled" value="enabled">		
										<logic:equal name="EnvioVO" property="generarForDecJurEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('generarDecJur', '<bean:write name="EnvioVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="bal" key="bal.envioOsirisSearchPage.button.generarForDecJur"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/genForDecJur0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="EnvioVO" property="generarForDecJurEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/genForDecJur1.gif"/>
										</logic:notEqual>									
									</logic:equal>							
									<logic:notEqual name="envioOsirisSearchPageVO" property="generarForDecJurEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/genForDecJur1.gif"/>
									</logic:notEqual>
								</td>
								<!-- GenerarTransaccion -->
								<td>
									<logic:equal name="envioOsirisSearchPageVO" property="generarTransaccionEnabled" value="enabled">		
										<logic:equal name="EnvioVO" property="generarTransaccionEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('generarTransaccion', '<bean:write name="EnvioVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="bal" key="bal.envioOsirisSearchPage.button.generarTransaccion"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/genArcTran0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="EnvioVO" property="generarTransaccionEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/genArcTran1.gif"/>
										</logic:notEqual>									
									</logic:equal>							
									<logic:notEqual name="envioOsirisSearchPageVO" property="generarTransaccionEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/genArcTran1.gif"/>
									</logic:notEqual>
								</td>
								<!-- CambiarEstado -->
								<td>
									<logic:equal name="envioOsirisSearchPageVO" property="cambiarEstadoEnabled" value="enabled">		
										<logic:equal name="EnvioVO" property="cambiarEstadoEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('cambiarEstado', '<bean:write name="EnvioVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="bal" key="bal.envioOsirisSearchPage.button.cambiarEstado"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/vueltaAtras0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="EnvioVO" property="cambiarEstadoEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/vueltaAtras1.gif"/>
										</logic:notEqual>									
									</logic:equal>							
									<logic:notEqual name="envioOsirisSearchPageVO" property="cambiarEstadoEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/vueltaAtras1.gif"/>
									</logic:notEqual>
								</td>
							    <td><bean:write name="EnvioVO" property="fechaRegistroMulatView"/>&nbsp;</td>
						  	    <td><bean:write name="EnvioVO" property="idEnvioAfipView"/>&nbsp;</td>
							    <td><bean:write name="EnvioVO" property="estadoEnvio.desEstado"/>&nbsp;</td>
						 		<td><bean:write name="EnvioVO" property="canTranPagoView" />&nbsp;</td>
						 		<td><bean:write name="EnvioVO" property="canTranDecJurView" />&nbsp;</td>
							</tr>
						</logic:iterate>
					<tr>
						<td class="paginador" align="center" colspan="11">
							<bean:define id="pager" name="envioOsirisSearchPageVO"/>
							<%@ include file="/base/pager.jsp" %>
						</td>
					</tr>
				</tbody>
			</table>
		</logic:notEmpty>
		
		<logic:empty name="envioOsirisSearchPageVO" property="listResult">
			<table class="tramonline" border="0" cellspacing="1" width="100%">            
        	    	<tbody>
						<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
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
			<logic:equal name="envioOsirisSearchPageVO" property="viewResult" value="true">
  	    		<td align="right" width="100%">
					<bean:define id="obtenerEnviosEnabled" name="envioOsirisSearchPageVO" property="obtenerEnviosEnabled"/>
					<input type="button" <%=obtenerEnviosEnabled%> class="boton" 
						onClick="submitForm('obtenerEnvios', '0');" 
						value="<bean:message bundle="bal" key="bal.envioOsirisSearchPage.button.obtenerEnvios"/>"
					/>
				</td>
  	    		<td align="right" width="100%">
					<bean:define id="procesarEnviosEnabled" name="envioOsirisSearchPageVO" property="procesarEnviosEnabled"/>
					<input type="button" <%=procesarEnviosEnabled%> class="boton" 
						onClick="submitForm('procesarEnvios', '0');" 
						value="<bean:message bundle="bal" key="bal.envioOsirisSearchPage.button.procesarEnvios"/>"
					/>
				</td>
			</logic:equal>
		</tr>
	</table>
	
	<input type="hidden" name="name"  value="<bean:write name='envioOsirisSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
		
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<input type="text" style="display:none"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>

</html:form>
<!-- Fin Tabla que contiene todos los formularios -->		