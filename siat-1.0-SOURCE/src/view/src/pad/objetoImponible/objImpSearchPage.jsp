<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/BuscarObjImp.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="pad" key="pad.objImpSearchPage.title"/></h1>	
		
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="pad" key="pad.objImpSearchPage.legend"/></p>				
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
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImp.label"/>: </label></td>
				<td class="normal">
					<html:select name="objImpSearchPageVO" property="objImp.tipObjImp.id" styleClass="select" onchange="submitForm('paramTipObjImp', '');">
						<html:optionsCollection name="objImpSearchPageVO" property="listTipObjImp" label="desTipObjImp" value="id" />
					</html:select>
				</td>
				
				<logic:empty name="objImpSearchPageVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition" >
					<td><label><bean:message bundle="pad" key="pad.objImp.clave.label"/>: </label></td>
					<td class="normal"><html:text name="objImpSearchPageVO" property="objImp.claveFuncional" size="20" maxlength="100" styleClass="datos" disabled="true"/></td>
				</logic:empty>
				<logic:notEmpty name="objImpSearchPageVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition" >
					<td><label><bean:write name="objImpSearchPageVO" property="tipObjImpDefinition.desClaveFunc" />: </label></td>
					<td class="normal"><html:text name="objImpSearchPageVO" property="objImp.claveFuncional" size="20" maxlength="100" styleClass="datos" /></td>
				</logic:notEmpty>
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
			&nbsp;
		  	<html:button property="btnBuscarAva"  styleClass="boton" onclick="submitForm('buscarAvaInit', '');">
				<bean:message bundle="base" key="abm.button.busquedaAvanzada"/>
			</html:button>
		</p>
	</fieldset>	
	<!-- Fin Filtro -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="objImpSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="objImpSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="objImpSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th width="1">&nbsp;</th> <!-- Activar Desactivar -->
							</logic:notEqual>
							<th align="left">
								<logic:empty name="objImpSearchPageVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition" >
									<bean:message bundle="pad" key="pad.objImp.claveFuncional.label"/>
								</logic:empty>
								<logic:notEmpty name="objImpSearchPageVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition" >
									<bean:write name="objImpSearchPageVO" property="tipObjImpDefinition.desClaveFunc" />
								</logic:notEmpty>
							</th>
							<th align="left">
								<logic:empty name="objImpSearchPageVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition" >
									<bean:message bundle="pad" key="pad.objImp.clave.label"/>
								</logic:empty>
								<logic:notEmpty name="objImpSearchPageVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition" >
									<bean:write name="objImpSearchPageVO" property="tipObjImpDefinition.desClave" />
								</logic:notEmpty>
							</th>							
							<th align="left"><bean:message bundle="base" key="base.vigencia.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.objImp.fechaAlta.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.objImp.fechaBaja.label"/></th>
						</tr>
							
						<logic:iterate id="ObjImpVO" name="objImpSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="objImpSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="ObjImpVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="objImpSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="objImpSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="ObjImpVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="ObjImpVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="objImpSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="ObjImpVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="ObjImpVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="ObjImpVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="objImpSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
									<!-- Eliminar-->								
									<td>
										<logic:equal name="objImpSearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="ObjImpVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="ObjImpVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="ObjImpVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="objImpSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
									<td>
										<!-- Activar -->
										<logic:equal name="ObjImpVO" property="vigencia.id" value="0">
											<logic:equal name="objImpSearchPageVO" property="activarEnabled" value="enabled">
												<logic:equal name="ObjImpVO" property="activarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('activar', '<bean:write name="ObjImpVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.activar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/activar0.gif"/>
													</a>
												</logic:equal> 
												<logic:notEqual name="ObjImpVO" property="activarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="objImpSearchPageVO" property="activarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
											</logic:notEqual>
										</logic:equal> 
										<!-- Desactivar -->
										<logic:equal name="ObjImpVO" property="vigencia.id" value="1">
											<logic:equal name="objImpSearchPageVO" property="desactivarEnabled" value="enabled">
												<logic:equal name="ObjImpVO" property="desactivarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('desactivar', '<bean:write name="ObjImpVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.desactivar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="ObjImpVO" property="desactivarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="objImpSearchPageVO" property="desactivarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
											</logic:notEqual>										
										</logic:equal>
										<!-- En estado creado -->
										<logic:equal name="ObjImpVO" property="vigencia.id" value="-1">
											<a style="cursor: pointer; cursor: hand;">
											<img border="0" title="<bean:message bundle="base" key="abm.button.creado"/>" src="<%=request.getContextPath()%>/images/iconos/creado0.gif"/>
											</a>
										</logic:equal> 
									</td>									
								</logic:notEqual>
								<td><bean:write name="ObjImpVO" property="claveFuncional"/>&nbsp;</td>
								<td><bean:write name="ObjImpVO" property="clave"/>&nbsp;</td>
								<td><bean:write name="ObjImpVO" property="vigencia.value" />&nbsp;</td>
								<td><bean:write name="ObjImpVO" property="fechaAltaView" />&nbsp;</td>
								<td><bean:write name="ObjImpVO" property="fechaBajaView" />&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="objImpSearchPageVO"/>
								<%@ include file="/base/pagerSinPU.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="objImpSearchPageVO" property="listResult">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
						<logic:greaterThan name="objImpSearchPageVO" property="pageNumber" value="1">
							<tr>
								<td align="center">
									<bean:message bundle="base" key="base.resultadoPaginaVacio"/>
								</td>
							</tr>
							<tr>
								<td class="paginador" align="center">
									<bean:define id="pager" name="objImpSearchPageVO"/>
									<%@ include file="/base/pagerSinPU.jsp" %>
								</td>
							</tr>
						</logic:greaterThan>
						<logic:lessEqual name="objImpSearchPageVO" property="pageNumber" value="1">
							<tr>
								<td align="center">
									<bean:message bundle="base" key="base.resultadoVacio"/>
								</td>
							</tr>
						</logic:lessEqual>
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
			<logic:equal name="objImpSearchPageVO" property="viewResult" value="true">
  	    		<logic:equal name="objImpSearchPageVO" property="modoSeleccionar" value="false">
  	    			<td align="right">  
						<bean:define id="agregarEnabled" name="objImpSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</logic:equal>
			</logic:equal>
		</tr>
	</table>
	
	<input type="hidden" name="name"  value="<bean:write name='objImpSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
		
	<input type="text" style="display:none"/>	
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
