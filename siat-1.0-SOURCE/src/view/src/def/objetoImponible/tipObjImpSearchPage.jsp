<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	    
	    <%@include file="/base/calendar.js"%>   	    
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/BuscarTipObjImp.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.tipObjImpSearchPage.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p><bean:message bundle="def" key="def.tipObjImpSearchPage.legend"/></p>
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
					<td><label><bean:message bundle="def" key="def.tipObjImp.codTipObjImp.label"/>: </label></td>
					<td class="normal"><html:text name="tipObjImpSearchPageVO" property="tipObjImp.codTipObjImp" size="10" maxlength="10" styleClass="datos" /></td>
				
					<td><label><bean:message bundle="def" key="def.tipObjImp.desTipObjImp.label"/>: </label></td>
					<td class="normal"><html:text name="tipObjImpSearchPageVO" property="tipObjImp.desTipObjImp" size="20" maxlength="100" styleClass="datos" /></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="def" key="def.tipObjImp.esSiat.label"/>: </label></td>
					<td class="normal" colspan="3">	
						<html:select name="tipObjImpSearchPageVO" property="tipObjImp.esSiat.id" styleClass="select">
							<html:optionsCollection name="tipObjImpSearchPageVO" property="listSiNo" label="value" value="id" />
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
			<logic:equal name="tipObjImpSearchPageVO" property="viewResult" value="true">			
				<logic:notEmpty  name="tipObjImpSearchPageVO" property="listResult">	
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
						<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                		<tbody>
	                		<tr>
								<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
								<logic:notEqual name="tipObjImpSearchPageVO" property="modoSeleccionar" value="true">
									<th width="1">&nbsp;</th> <!-- Modificar -->
									<th width="1">&nbsp;</th> <!-- Eliminar -->
									<th width="1">&nbsp;</th> <!-- Activar/Desactivar -->
									<th width="1">&nbsp;</th> <!-- Areas -->									
								</logic:notEqual>
								<th align="left"><bean:message bundle="def" key="def.tipObjImp.codTipObjImp.label"/></th>
								<th align="left"><bean:message bundle="def" key="def.tipObjImp.desTipObjImp.label"/></th>
								<th align="left"><bean:message bundle="def" key="def.tipObjImp.esSiat.label"/></th>
								<th align="left"><bean:message bundle="def" key="def.tipObjImp.fechaAlta.label"/></th>
								<th align="left"><bean:message bundle="def" key="def.tipObjImp.fechaBaja.label"/></th>
								<th align="left"><bean:message bundle="def" key="def.tipObjImp.estado.label"/></th>								
							</tr>

						<logic:iterate id="TipObjImpVO" name="tipObjImpSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="tipObjImpSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="TipObjImpVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
									<logic:notEqual name="tipObjImpSearchPageVO" property="modoSeleccionar" value="true">
										<!-- Ver -->
										<td>
											<logic:equal name="tipObjImpSearchPageVO" property="verEnabled" value="enabled">
												<logic:equal name="TipObjImpVO" property="verEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="TipObjImpVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="TipObjImpVO" property="verEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="tipObjImpSearchPageVO" property="verEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
											</logic:notEqual>
										</td>
										<!-- Modificar-->								
										<td>
											<logic:equal name="tipObjImpSearchPageVO" property="modificarEnabled" value="enabled">
												<logic:equal name="TipObjImpVO" property="modificarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="TipObjImpVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="TipObjImpVO" property="modificarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="tipObjImpSearchPageVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</td>
										<!-- Eliminar-->								
										<td>
											<logic:equal name="tipObjImpSearchPageVO" property="eliminarEnabled" value="enabled">
												<logic:equal name="TipObjImpVO" property="eliminarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="TipObjImpVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
													</a>
												</logic:equal>	
												<logic:notEqual name="TipObjImpVO" property="eliminarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="tipObjImpSearchPageVO" property="eliminarEnabled" value="enabled">										
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</td>
										<td>
											<!-- Activar -->
											<logic:notEqual name="TipObjImpVO" property="estado.esActivo" value="true">
												<logic:equal name="tipObjImpSearchPageVO" property="activarEnabled" value="enabled">
													<logic:equal name="TipObjImpVO" property="activarEnabled" value="enabled">
														<a style="cursor: pointer; cursor: hand;" onclick="submitForm('activar', '<bean:write name="TipObjImpVO" property="id" bundle="base" formatKey="general.format.id"/>');">
															<img title="<bean:message bundle="base" key="abm.button.activar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/activar0.gif"/>
														</a>
													</logic:equal> 
													<logic:notEqual name="TipObjImpVO" property="activarEnabled" value="enabled">
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
													</logic:notEqual>
												</logic:equal>
												<logic:notEqual name="tipObjImpSearchPageVO" property="activarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
												</logic:notEqual>
											</logic:notEqual> 
											<!-- Desactivar -->
											<logic:equal name="TipObjImpVO" property="estado.esActivo" value="true">
												<logic:equal name="tipObjImpSearchPageVO" property="desactivarEnabled" value="enabled">
													<logic:equal name="TipObjImpVO" property="desactivarEnabled" value="enabled">
														<a style="cursor: pointer; cursor: hand;" onclick="submitForm('desactivar', '<bean:write name="TipObjImpVO" property="id" bundle="base" formatKey="general.format.id"/>');">
															<img title="<bean:message bundle="base" key="abm.button.desactivar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar0.gif"/>
														</a>
													</logic:equal>
													<logic:notEqual name="TipObjImpVO" property="desactivarEnabled" value="enabled">
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
													</logic:notEqual>
												</logic:equal>
												<logic:notEqual name="tipObjImpSearchPageVO" property="desactivarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
												</logic:notEqual>										
											</logic:equal>
										</td>
										<!-- Areas -->								
										<td>
											<logic:equal name="tipObjImpSearchPageVO" property="admAreaOrigenEnabled" value="enabled">
												<logic:equal name="TipObjImpVO" property="admAreaOrigenEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('admAreas', '<bean:write name="TipObjImpVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="def" key="def.tipObjImpSearchPage.adm.button.areas"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/areas0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="TipObjImpVO" property="admAreaOrigenEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/areas1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="tipObjImpSearchPageVO" property="admAreaOrigenEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/areas1.gif"/>
											</logic:notEqual>
										</td>
									</logic:notEqual>
	
								<td><bean:write name="TipObjImpVO" property="codTipObjImp"/>&nbsp;</td>
								<td><bean:write name="TipObjImpVO" property="desTipObjImp" />&nbsp;</td>
								<td><bean:write name="TipObjImpVO" property="esSiat.value" />&nbsp;</td>
								<td><bean:write name="TipObjImpVO" property="fechaAltaView"/>&nbsp;</td>
								<td><bean:write name="TipObjImpVO" property="fechaBajaView"/>&nbsp;</td>
								<td><bean:write name="TipObjImpVO" property="estado.value"/>&nbsp;</td>								
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="tipObjImpSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						</tbody>
					</table>
				</logic:notEmpty>
		
				<logic:empty name="tipObjImpSearchPageVO" property="listResult">
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
			<logic:equal name="tipObjImpSearchPageVO" property="viewResult" value="true">
  	    			<td align="right">
					<bean:define id="agregarEnabled" name="tipObjImpSearchPageVO" property="agregarEnabled"/>
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
<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="pageNumber" value="1" id="pageNumber">
		<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
		<input type="hidden" name="isSubmittedForm" value="true"/>

		<input type="hidden" name="name"         value="<bean:write name='tipObjImpSearchPageVO' property='name'/>" id="name"/>
		<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
		

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->