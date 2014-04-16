<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarGesJud.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="gde" key="gde.gesJudSearchPage.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="gesJudSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="gde" key="gde.gesJud.label"/>
					</logic:equal>
					<logic:notEqual name="gesJudSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="gde" key="gde.gesJudSearchPage.legend"/>
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
		
			<!-- Procurador -->
			<tr>	
				<td><label><bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
				<td class="normal">
					<html:select name="gesJudSearchPageVO" property="gesJud.procurador.id" styleClass="select">
						<html:optionsCollection name="gesJudSearchPageVO" property="listProcurador" label="descripcion" value="id" />
					</html:select>
				</td>
				<!-- Estado -->
				<td><label><bean:message bundle="gde" key="gde.gesJud.estadoGesjud.label"/>: </label></td>
				<td class="normal">
					<html:select name="gesJudSearchPageVO" property="gesJud.estadoGesJudVO.idEstadoGesJud" styleClass="select">
						<html:optionsCollection name="gesJudSearchPageVO" property="listEstadoGesJud" label="desEstadoGesJud" value="idEstadoGesJud" />
					</html:select>
				</td>					
			</tr>
			
			<!-- Descripcion -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.gesJud.desGesJud.label"/>: </label></td>
				<td class="normal"><html:text name="gesJudSearchPageVO" property="gesJud.desGesJud" size="20" maxlength="100"/></td>			
				<!-- juzgado -->
				<td><label><bean:message bundle="gde" key="gde.gesJud.juzgado.label"/>: </label></td>
				<td class="normal"><html:text name="gesJudSearchPageVO" property="gesJud.juzgado" size="20" maxlength="100"/></td>			
			</tr>
			
			<!-- Nro Expediente Judicial -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.gesJud.nroExpediente.label"/>: </label></td>
				<td class="normal"><html:text name="gesJudSearchPageVO" property="gesJud.nroExpedienteView" size="20" maxlength="100"/></td>			
				<!-- Anio Expediente Judicial -->
				<td><label><bean:message bundle="gde" key="gde.gesJud.anioExpediente.label"/>: </label></td>
				<td class="normal"><html:text name="gesJudSearchPageVO" property="gesJud.anioExpedienteView" size="20" maxlength="100"/></td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="gesJudSearchPageVO" property="cuenta.numeroCuenta" size="20"/>
					<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
						<bean:message bundle="gde" key="gde.gesJudSearchPage.button.buscarCuenta"/>
					</html:button>
				</td>			
			</tr>
					
		</table>
			
		<p align="center">
		  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
				<bean:message bundle="base" key="abm.button.limpiar"/>
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
		<logic:equal name="gesJudSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="gesJudSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="gesJudSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th width="1">&nbsp;</th> <!-- Registrar Caducidad -->
							</logic:notEqual>
							<!-- <#ColumnTitles#> -->
								<th width="1"><bean:message bundle="gde" key="gde.gesJud.desGesJud.label"/></th>
								<th width="1"><bean:message bundle="gde" key="gde.procurador.label"/></th>
								<th width="1"><bean:message bundle="gde" key="gde.gesJud.juzgado.label"/></th>
								<th width="1"><bean:message bundle="gde" key="gde.gesJud.caso.label"/></th>
								<th width="1"><bean:message bundle="gde" key="gde.gesJud.estadoGesjud.label"/></th>
						</tr>
							
						<logic:iterate id="GesjudVO" name="gesJudSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="gesJudSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="GesjudVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="gesJudSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="gesJudSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="GesjudVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="gesJudSearchPageVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="gesJudSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="GesjudVO" property="modificarBussEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="GesjudVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="GesjudVO" property="modificarBussEnabled" value="true">											
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="gesJudSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
									<!-- Eliminar-->								
									<td>									
										<logic:equal name="gesJudSearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="GesjudVO" property="eliminarBussEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="GesjudVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="GesjudVO" property="eliminarBussEnabled" value="true">											
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="gesJudSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
									<td>
										<!-- Registrar Caducidad -->
											<logic:equal name="gesJudSearchPageVO" property="registrarCaducidadEnabled" value="enabled">
												<logic:equal name="GesjudVO" property="registrarCaducidadBussEnabled" value="true">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('registrarCaducidad', '<bean:write name="GesjudVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="gde" key="gde.gesJudSearchPageVO.button.registrarCaducidad"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/caducidad_gesJud0.gif"/>
													</a>
												</logic:equal> 
												<logic:notEqual name="GesjudVO" property="registrarCaducidadBussEnabled" value="true">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/caducidad_gesJud0.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="gesJudSearchPageVO" property="registrarCaducidadEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/caducidad_gesJud0.gif"/>
											</logic:notEqual>										 									
									</td>
									<td><bean:write name="GesjudVO" property="desGesJud"/>&nbsp;</td>
									<td><bean:write name="GesjudVO" property="procurador.descripcion"/>&nbsp;</td>
									<td><bean:write name="GesjudVO" property="juzgado"/>&nbsp;</td>
									<td>&nbsp;</td>
									<td><bean:write name="GesjudVO" property="estadoGesJudVO.desEstadoGesJud"/>&nbsp;</td>
								</logic:notEqual>
								<!-- <#ColumnFiedls#> -->
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="gesJudSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="gesJudSearchPageVO" property="listResult">
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
			<logic:equal name="gesJudSearchPageVO" property="viewResult" value="true">
  	    		<td align="right">
  	    			<logic:equal name="gesJudSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="gesJudSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="gesJudSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="gesJudSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="gesJudSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
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

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->