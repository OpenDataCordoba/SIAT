<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/BuscarAuxCaja7.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="bal" key="bal.auxCaja7SearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">				
				<p>
					<logic:equal name="auxCaja7SearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegend"/>
						<bean:message bundle="bal" key="bal.auxCaja7.label"/>
					</logic:equal>
					<logic:notEqual name="auxCaja7SearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="bal" key="bal.auxCaja7SearchPage.legend"/>
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
	<logic:notEqual name="auxCaja7SearchPageVO" property="modoSeleccionar" value="true">
	<fieldset>
	<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
		<table class="tabladatos">
			<!-- Fecha Desde/Hasta -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.auxCaja7SearchPage.fechaDesde.label"/>: </label></td>
				<td class="normal">
				<html:text name="auxCaja7SearchPageVO" property="fechaDesdeView" styleId="fechaDesdeView" size="10" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>	
				<td><label><bean:message bundle="bal" key="bal.auxCaja7SearchPage.fechaHasta.label"/>: </label></td>
				<td class="normal">
				<html:text name="auxCaja7SearchPageVO" property="fechaHastaView" styleId="fechaHastaView" size="10" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>	
		    </tr>
		    <!-- Estado -->
			<tr>
				<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td class="normal">
					<html:select name="auxCaja7SearchPageVO" property="auxCaja7.estado.id" styleClass="select" >
						<html:optionsCollection name="auxCaja7SearchPageVO" property="listEstado" label="value" value="id" />
					</html:select>
				</td>	
			</tr>
		</table>
			
		<p align="center">
		  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
				<bean:message bundle="base" key="abm.button.limpiar"/>
			</html:button>
			&nbsp;
			<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromSearchPage', '1');">
					<bean:message bundle="base" key="abm.button.imprimir"/>
			</html:button>
			&nbsp;
		  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
				<bean:message bundle="base" key="abm.button.buscar"/>
			</html:button>
		</p>
	</fieldset>	
	</logic:notEqual>
	<!-- Fin Filtro -->
		
	<!-- Resultado Filtro --> 
	<div id="resultadoFiltro">
		<logic:equal name="auxCaja7SearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="auxCaja7SearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th align="center">
					       			<br>
					       			&nbsp;
								<logic:equal name="auxCaja7SearchPageVO" property="modoSeleccionar" value="true">
					       			<input type="checkbox" onclick="changeChk('filter', 'listIdAuxCaja7Selected', this)" id="checkAll"/>
								</logic:equal>
				       		</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="auxCaja7SearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th width="1">&nbsp;</th> <!-- Activar -->
							</logic:notEqual>
							<th align="left"><bean:message bundle="bal" key="bal.auxCaja7.fecha.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.partida.label"/></th>
							<th align="center"><bean:message bundle="bal" key="bal.auxCaja7.importe.label"/></th>
							<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
						</tr>
							
						<logic:iterate id="AuxCaja7VO" name="auxCaja7SearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="auxCaja7SearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<!-- Seleccionar -->
	                          			&nbsp;
	                          			<html:multibox name="auxCaja7SearchPageVO" property="listIdAuxCaja7Selected" >
	                         			   	<bean:write name="AuxCaja7VO" property="id" bundle="base" formatKey="general.format.id"/>
		                                </html:multibox>	  		
									</td>
								</logic:equal>									
								<logic:notEqual name="auxCaja7SearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="auxCaja7SearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="AuxCaja7VO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="AuxCaja7VO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="auxCaja7SearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="AuxCaja7VO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="AuxCaja7VO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="AuxCaja7VO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="auxCaja7SearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
									<!-- Eliminar-->								
									<td>
										<logic:equal name="auxCaja7SearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="AuxCaja7VO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="AuxCaja7VO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="AuxCaja7VO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="auxCaja7SearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
									<td>
									<!-- Activar -->
									<logic:equal name="AuxCaja7VO" property="estado.esCreado" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('activar', '<bean:write name="AuxCaja7VO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.activar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/activar0.gif"/>
										</a>
									</logic:equal> 
									<logic:equal name="AuxCaja7VO" property="estado.esCreado" value="false">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
									</logic:equal> 
									</td>	
								</logic:notEqual>
								<td><bean:write name="AuxCaja7VO" property="fechaView"/>&nbsp;</td>
								<td><bean:write name="AuxCaja7VO" property="partida.desPartidaView"/>&nbsp;</td>
								<td align="center"><bean:write name="AuxCaja7VO" property="importeView"/>&nbsp;$</td>
								<td><bean:write name="AuxCaja7VO" property="estado.value"/>&nbsp;</td>
							</tr>
						</logic:iterate>
						<logic:notEqual name="auxCaja7SearchPageVO" property="modoSeleccionar" value="true">
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="auxCaja7SearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						</logic:notEqual>
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="auxCaja7SearchPageVO" property="listResult">
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
			<logic:equal name="auxCaja7SearchPageVO" property="viewResult" value="true">
				<td align="right">
				
  	    			<logic:equal name="auxCaja7SearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="auxCaja7SearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="auxCaja7SearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="auxCaja7SearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="auxCaja7SearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
							</logic:equal>
					</logic:equal>
				</td>				
			</logic:equal>
			<logic:equal name="auxCaja7SearchPageVO" property="modoSeleccionar" value="true">
				<td align="right">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('incluir', '');">
							<bean:message bundle="base" key="abm.button.incluir"/>
						</html:button>
				</td>
			</logic:equal>
		</tr>
	</table>
  	<input type="hidden" name="name"  value="<bean:write name='auxCaja7SearchPageVO' property='name'/>" id="name"/>
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
	<script type="text/javascript">
		function setChk(){
			var form = document.getElementById('filter');		
			form.elements['checkAll'].click();		
		}
	</script>

	<script type="text/javascript">setChk();</script>
<!-- Fin Tabla que contiene todos los formularios -->
