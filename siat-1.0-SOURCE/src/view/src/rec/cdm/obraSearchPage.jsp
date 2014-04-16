<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rec/BuscarObra.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="rec" key="rec.obraSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="rec" key="rec.obraSearchPage.legend"/></p>
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
				<td><label><bean:message bundle="rec" key="rec.obra.numeroObra.label"/>: </label></td>
				<td class="normal"><html:text name="obraSearchPageVO" property="obra.numeroObraView" size="20" maxlength="100" styleClass="datos" /></td>
				
				<td><label><bean:message bundle="rec" key="rec.obra.desObra.label"/>: </label></td>
				<td class="normal"><html:text name="obraSearchPageVO" property="obra.desObra" size="20" maxlength="100" styleClass="datos" /></td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="rec" key="rec.estadoObra.desEstadoObra.label"/>: </label></td>
				<td class="normal">
					<html:select name="obraSearchPageVO" property="obra.estadoObra.id" styleClass="select">
						<html:optionsCollection name="obraSearchPageVO" property="listEstadoObra" label="desEstadoObra" value="id" />
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
		<logic:equal name="obraSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="obraSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="obraSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th width="1">&nbsp;</th> <!-- Activar Desactivar -->
								<th width="1">&nbsp;</th> <!-- ADM Planilla -->
							</logic:notEqual>
							<th align="left"><bean:message bundle="rec" key="rec.obra.numeroObra.label"/></th>
							<th align="left"><bean:message bundle="rec" key="rec.obra.desObra.label"/></th>
							<th align="left"><bean:message bundle="rec" key="rec.estadoObra.desEstadoObra.label"/></th>
						</tr>
							
						<logic:iterate id="ObraVO" name="obraSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="obraSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="ObraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="obraSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="obraSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="ObraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="ObraVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="obraSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="ObraVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="ObraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="ObraVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="obraSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
									<!-- Eliminar-->								
									<td>
										<logic:equal name="obraSearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="ObraVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="ObraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="ObraVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="obraSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
									<td>										
										<!-- cambiar estado -->
										<logic:equal name="obraSearchPageVO" property="cambiarEstadoEnabled" value="enabled">										
											<logic:equal name="ObraVO" property="cambiarEstadoBussEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('cambiarEstado', '<bean:write name="ObraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="rec" key="rec.obraSearchPage.button.cambiarEstado"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="ObraVO" property="cambiarEstadoBussEnabled" value="true">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado1.gif"/>
											</logic:notEqual>	
										</logic:equal>
										<logic:notEqual name="obraSearchPageVO" property="cambiarEstadoEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado1.gif"/>										
										</logic:notEqual>
									</td>
									<!-- Accion emitirInforme -->
									<td>
										<logic:equal name="obraSearchPageVO" property="emitirInformeEnabled" value="enabled">
											<logic:equal name="ObraVO" property="emitirInformeBussEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('imprimirInfObrRep', '<bean:write name="ObraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="rec" key="rec.obraSearchPage.button.emitirInforme"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/imprimir0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="ObraVO" property="emitirInformeBussEnabled" value="true">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/imprimir1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="obraSearchPageVO" property="emitirInformeEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/imprimir1.gif"/>
										</logic:notEqual>
									</td>
								</logic:notEqual>
								<td><bean:write name="ObraVO" property="numeroObraView"/>&nbsp;</td>
								<td><bean:write name="ObraVO" property="desObra"/>&nbsp;</td>
								<td><bean:write name="ObraVO" property="estadoObra.desEstadoObra"/>&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="obraSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="obraSearchPageVO" property="listResult">
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

	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left" width="50%">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<logic:equal name="obraSearchPageVO" property="viewResult" value="true">
  	    			<td align="right" width="50%">
					<bean:define id="agregarEnabled" name="obraSearchPageVO" property="agregarEnabled"/>
					<input type="button" <%=agregarEnabled%> class="boton" 
						onClick="submitForm('agregar', '0');" 
						value="<bean:message bundle="base" key="abm.button.agregar"/>"
					/>
				</td>
			</logic:equal>
		</tr>
	</table>
		
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
	
	<input type="hidden" name="name"         value="<bean:write name='obraSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
