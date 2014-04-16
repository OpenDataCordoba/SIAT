<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/BuscarOtrIngTes.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.otrIngTesSearchPage.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p><bean:message bundle="bal" key="bal.otrIngTesSearchPage.legend"/></p>
					
				</td>				
				<td align="right">
		 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>

		<!-- Filtro -->
		<logic:notEqual name="otrIngTesSearchPageVO" property="modoSeleccionar" value="true">
		<fieldset>
			<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
				<table class="tabladatos">

					<tr>
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td colspan="6" class="normal">
						<html:select name="otrIngTesSearchPageVO" property="otrIngTes.recurso.id" styleClass="select" >
							<bean:define id="includeRecursoList" name="otrIngTesSearchPageVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="otrIngTesSearchPageVO" property="otrIngTes.recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
						</td>	
				    </tr>
					<tr>
						<td><label><bean:message bundle="def" key="def.area.label"/>: </label></td>
						<td class="normal">
						<html:select name="otrIngTesSearchPageVO" property="otrIngTes.areaOrigen.id" styleClass="select" >
							<html:optionsCollection name="otrIngTesSearchPageVO" property="listAreaOrigen" label="desArea" value="id" />
						</html:select>
						</td>	
				    </tr>
					<tr>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.otrIngTes.descripcion.label"/>: </label></td>
						<td colspan="6" class="normal"><html:text name="otrIngTesSearchPageVO" property="otrIngTes.descripcion" size="35" maxlength="50" /></td>
				    </tr>
				    <tr>
						<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
						<td class="normal">
						<html:select name="otrIngTesSearchPageVO" property="otrIngTes.estOtrIngTes.id" styleClass="select" >
							<html:optionsCollection name="otrIngTesSearchPageVO" property="listEstOtrIngTes" label="desEstOtrIngTes" value="id" />
						</html:select>
						</td>	
				    </tr>
					<!-- Fecha Registro Desde/Hasta -->
					<tr>
						<td><label><bean:message bundle="bal" key="bal.otrIngTesSearchPage.fechaRegistroDesde.label"/>: </label></td>
						<td class="normal">
						<html:text name="otrIngTesSearchPageVO" property="fechaRegistroDesdeView" styleId="fechaRegistroDesdeView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaRegistroDesdeView');" id="a_fechaRegistroDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>	
						<td><label><bean:message bundle="bal" key="bal.otrIngTesSearchPage.fechaRegistroHasta.label"/>: </label></td>
						<td class="normal">
						<html:text name="otrIngTesSearchPageVO" property="fechaRegistroHastaView" styleId="fechaRegistroHastaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaRegistroHastaView');" id="a_fechaRegistroHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>	
				    </tr>		
				    <!-- Filtro de para solo buscar los que tengan errores de distribucion -->
					<tr>
						<td class="normal">
						</td>
					    <td class="normal" colspan="5">
					     <logic:equal name="otrIngTesSearchPageVO" property="filtroDistribucionErronea" value="true">
							<input type="checkbox" name="filtroDistribucionErronea" checked>
							<label><bean:message bundle="bal" key="bal.otrIngTesSearchPage.filtroDistribucionErronea"/></label>	
						 </logic:equal>
						   <logic:equal name="otrIngTesSearchPageVO" property="filtroDistribucionErronea" value="false">
							<input type="checkbox" name="filtroDistribucionErronea">
							<label><bean:message bundle="bal" key="bal.otrIngTesSearchPage.filtroDistribucionErronea"/></label>	
						 </logic:equal>
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
		</logic:notEqual>
		<!-- Fin Filtro -->

	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="otrIngTesSearchPageVO" property="viewResult" value="true">		
			<logic:notEmpty  name="otrIngTesSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
	                	<tr>
							<th align="center">
					       			<br>
					       			&nbsp;&nbsp;
								<logic:equal name="otrIngTesSearchPageVO" property="modoSeleccionar" value="true">
					       			<input type="checkbox" onclick="changeChk('filter', 'listIdOtrIngTesSelected', this)" id="checkAll"/>
								</logic:equal>
				       		</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="otrIngTesSearchPageVO" property="modoSeleccionar" value="true">
							<th width="1">&nbsp;</th> <!-- Modificar -->
							<th width="1">&nbsp;</th> <!-- Eliminar -->
							<th width="1">&nbsp;</th> <!-- Generar Recibo -->
							<th width="1">&nbsp;</th> <!-- Mostrar Distribucion -->
							</logic:notEqual>
						  	<th align="left"><bean:message bundle="bal" key="bal.otrIngTes.fechaOtrIngTes.label"/></th>
  							<th align="left"><bean:message bundle="bal" key="bal.otrIngTes.importe.label"/></th>
						  	<th align="left"><bean:message bundle="bal" key="bal.otrIngTes.descripcion.label"/></th>
						  	<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
						  	<th align="left"><bean:message bundle="def" key="def.area.label"/></th>
  							<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
						</tr>
						<logic:iterate id="OtrIngTesVO" name="otrIngTesSearchPageVO" property="listResult">
							<tr>						
								<!-- Ver/Seleccionar -->
								<td>
									<logic:notEqual name="otrIngTesSearchPageVO" property="modoSeleccionar" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="OtrIngTesVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:notEqual>
									<logic:equal name="otrIngTesSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Seleccionar -->
	                          			&nbsp;
	                          			<html:multibox name="otrIngTesSearchPageVO" property="listIdOtrIngTesSelected" >
	                         			   	<bean:write name="OtrIngTesVO" property="id" bundle="base" formatKey="general.format.id"/>
		                                </html:multibox>	  		
									</logic:equal>									
								</td>

								<logic:notEqual name="otrIngTesSearchPageVO" property="modoSeleccionar" value="true">								
								<td>
									<!-- Modificar-->								
									<logic:equal name="otrIngTesSearchPageVO" property="modificarEnabled" value="enabled">									
										<logic:equal name="OtrIngTesVO" property="modificarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="OtrIngTesVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="OtrIngTesVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="otrIngTesSearchPageVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</td>
	
								<td>
									<!-- Eliminar-->								
									<logic:equal name="otrIngTesSearchPageVO" property="eliminarEnabled" value="enabled">		
										<logic:equal name="OtrIngTesVO" property="eliminarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="OtrIngTesVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="OtrIngTesVO" property="eliminarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>									
									</logic:equal>							
									<logic:notEqual name="otrIngTesSearchPageVO" property="eliminarEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</td>
								<td>
									<!-- Generar/Ver Recibo-->								
									<logic:equal name="otrIngTesSearchPageVO" property="generarReciboEnabled" value="enabled">		
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('generarRecibo', '<bean:write name="OtrIngTesVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="bal" key="bal.adm.button.generarRecibo"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/generarRecibo0.gif"/>
										</a>
									</logic:equal>							
									<logic:notEqual name="otrIngTesSearchPageVO" property="generarReciboEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/generarRecibo1.gif"/>
									</logic:notEqual>
								</td>
								<td>
									<!-- Mostrar Distribucion-->								
									<logic:equal name="otrIngTesSearchPageVO" property="distribuirOtrIngTesEnabled" value="enabled">		
										<logic:equal name="OtrIngTesVO" property="distribuirBussEnabled" value="true">		
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('distribuir', '<bean:write name="OtrIngTesVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="bal" key="bal.adm.button.distribuir"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/mostrarDistribucion0.gif"/>
											</a>
										</logic:equal>	
										<logic:equal name="OtrIngTesVO" property="distribuirBussEnabled" value="false">		
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/mostrarDistribucion1.gif"/>
										</logic:equal>
									</logic:equal>							
									<logic:notEqual name="otrIngTesSearchPageVO" property="distribuirOtrIngTesEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/mostrarDistribucion1.gif"/>
									</logic:notEqual>
								</td>
							</logic:notEqual>								
  							    <td><bean:write name="OtrIngTesVO" property="fechaOtrIngTesView"/>&nbsp;</td>
								<td><bean:write name="OtrIngTesVO" property="importeView"/>&nbsp;</td>
						  	    <td><bean:write name="OtrIngTesVO" property="descripcion"/>&nbsp;</td>
						  	    <td><bean:write name="OtrIngTesVO" property="recurso.desRecurso"/>&nbsp;</td>
   						  	    <td><bean:write name="OtrIngTesVO" property="areaOrigen.desArea"/>&nbsp;</td>
								<logic:equal name="OtrIngTesVO" property="paramIncluido" value="false">		
						 			<td><bean:write name="OtrIngTesVO" property="estOtrIngTes.desEstOtrIngTes"/>&nbsp;</td>
								</logic:equal>
								<logic:equal name="OtrIngTesVO" property="paramIncluido" value="true">		
						 			<td><bean:message bundle="bal" key="bal.otrIngTes.incluidoEnFolio.label"/>&nbsp;</td>
								</logic:equal>		 			
							</tr>
						</logic:iterate>
					<logic:notEqual name="otrIngTesSearchPageVO" property="modoSeleccionar" value="true">
						<tr>
							<td class="paginador" align="center" colspan="12">
								<bean:define id="pager" name="otrIngTesSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
					</logic:notEqual>	
				</tbody>
			</table>
		</logic:notEmpty>
		
		<logic:empty name="otrIngTesSearchPageVO" property="listResult">
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
			<logic:equal name="otrIngTesSearchPageVO" property="viewResult" value="true">
    		<td align="right">
				<logic:equal name="otrIngTesSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="otrIngTesSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="otrIngTesSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="otrIngTesSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="otrIngTesSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
						</logic:equal>
				</logic:equal>
			</td>
			</logic:equal>
			<logic:equal name="otrIngTesSearchPageVO" property="modoSeleccionar" value="true">
				<td align="right">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('incluir', '');">
							<bean:message bundle="base" key="abm.button.incluir"/>
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
	<input type="hidden" name="name" value="<bean:write name='otrIngTesSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>	

	<input type="text" style="display:none"/>
	
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
