<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/BuscarDisPar.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.disParSearchPage.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p><bean:message bundle="bal" key="bal.disParSearchPage.legend"/></p>
					
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
						<html:select name="disParSearchPageVO" property="disPar.recurso.id" styleClass="select" >
							<bean:define id="includeRecursoList" name="disParSearchPageVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="disParSearchPageVO" property="disPar.recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
						</td>	
				    </tr>
					<tr>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.disPar.desDisPar.label"/>: </label></td>
						<td class="normal"><html:text name="disParSearchPageVO" property="disPar.desDisPar" size="35" maxlength="100" /></td>
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
		<logic:equal name="disParSearchPageVO" property="viewResult" value="true">		
			<logic:notEmpty  name="disParSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
	                	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="disParSearchPageVO" property="modoSeleccionar" value="true">
							<th width="1">&nbsp;</th> <!-- Modificar -->
							<th width="1">&nbsp;</th> <!-- Eliminar -->
							<th width="1">&nbsp;</th> <!-- Activar/Desactivar -->
							<th width="1">&nbsp;</th> <!-- Asociar Recurso/Via -->
							<th width="1">&nbsp;</th> <!-- Asociar Plan -->
							</logic:notEqual>
						  	<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
						  	<th align="left"><bean:message bundle="bal" key="bal.disPar.desDisPar.label"/></th>
  							<th align="left"><bean:message bundle="bal" key="bal.tipoImporte.label"/></th>
  							<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
						</tr>
						
						<logic:iterate id="DisParVO" name="disParSearchPageVO" property="listResult">
							<tr>						
								<!-- Ver/Seleccionar -->
								<td>
									<logic:notEqual name="disParSearchPageVO" property="modoSeleccionar" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="DisParVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:notEqual>
									<logic:equal name="disParSearchPageVO" property="modoSeleccionar" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="DisParVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>									
								</td>


								<logic:notEqual name="disParSearchPageVO" property="modoSeleccionar" value="true">								
								<td>
									<!-- Modificar-->								
									<logic:equal name="disParSearchPageVO" property="modificarEnabled" value="enabled">									
										<logic:equal name="DisParVO" property="modificarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="DisParVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="DisParVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="disParSearchPageVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</td>
	
								<td>
									<!-- Eliminar-->								
									<logic:equal name="disParSearchPageVO" property="eliminarEnabled" value="enabled">		
										<logic:equal name="DisParVO" property="eliminarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="DisParVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="DisParVO" property="eliminarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>									
									</logic:equal>							
									<logic:notEqual name="disParSearchPageVO" property="eliminarEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</td>
								<td>
								<!-- Activar -->
									<logic:notEqual name="DisParVO" property="estado.esActivo" value="true">
										<logic:equal name="disParSearchPageVO" property="activarEnabled" value="enabled">
											<logic:equal name="DisParVO" property="activarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('activar', '<bean:write name="DisParVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.activar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/activar0.gif"/>
												</a>
											</logic:equal> 
											<logic:notEqual name="DisParVO" property="activarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="disParSearchPageVO" property="activarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
										</logic:notEqual>
									</logic:notEqual> 
									<!-- Desactivar -->
									<logic:equal name="DisParVO" property="estado.esActivo" value="true">
										<logic:equal name="disParSearchPageVO" property="desactivarEnabled" value="enabled">
											<logic:equal name="DisParVO" property="desactivarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('desactivar', '<bean:write name="DisParVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.desactivar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="DisParVO" property="desactivarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="disParSearchPageVO" property="desactivarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
										</logic:notEqual>										
									</logic:equal>
									</td>
									<!-- Asociar Recurso/Via-->								
									<td>
									<logic:equal name="disParSearchPageVO" property="asociarRecursoViaEnabled" value="enabled">		
										<logic:equal name="DisParVO" property="asociarRecursoViaEnabled" value="enabled">
											<logic:equal name="DisParVO" property="paramTipoImporte" value="false">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('asociarRecursoVia', '<bean:write name="DisParVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="bal" key="bal.disParSearchPage.adm.button.asociarRecursoVia"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/asociarRecursoVia0.gif"/>
												</a>
											</logic:equal>	
											<logic:equal name="DisParVO" property="paramTipoImporte" value="true">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/asociarRecursoVia1.gif"/>
											</logic:equal>	
										</logic:equal>	
										<logic:notEqual name="DisParVO" property="asociarRecursoViaEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/asociarRecursoVia1.gif"/>
										</logic:notEqual>									
									</logic:equal>							
									<logic:notEqual name="disParSearchPageVO" property="asociarRecursoViaEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/asociarRecursoVia1.gif"/>
									</logic:notEqual>
									</td>
									<!-- Asociar Plan-->								
									<td>
									<logic:equal name="disParSearchPageVO" property="asociarPlanEnabled" value="enabled">		
										<logic:equal name="DisParVO" property="asociarPlanEnabled" value="enabled">
											<logic:equal name="DisParVO" property="paramTipoImporte" value="false">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('asociarPlan', '<bean:write name="DisParVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="bal" key="bal.disParSearchPage.adm.button.asociarPlan"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/asociarPlan0.gif"/>
												</a>
											</logic:equal>	
											<logic:equal name="DisParVO" property="paramTipoImporte" value="true">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/asociarPlan1.gif"/>
											</logic:equal>	
										</logic:equal>	
										<logic:notEqual name="DisParVO" property="asociarPlanEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/asociarPlan1.gif"/>
										</logic:notEqual>									
									</logic:equal>							
									<logic:notEqual name="disParSearchPageVO" property="asociarPlanEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/asociarPlan1.gif"/>
									</logic:notEqual>
									</td>
							</logic:notEqual>								

						  	    <td><bean:write name="DisParVO" property="recurso.desRecurso"/>&nbsp;</td>
						  	    <td><bean:write name="DisParVO" property="desDisPar"/>&nbsp;</td>
								<td><bean:write name="DisParVO" property="tipoImporte.desTipoImporte"/>&nbsp;</td>
						 		<td><bean:write name="DisParVO" property="estado.value"/>&nbsp;</td>
							</tr>
						</logic:iterate>
					<tr>
						<td class="paginador" align="center" colspan="11">
							<bean:define id="pager" name="disParSearchPageVO"/>
							<%@ include file="/base/pager.jsp" %>
						</td>
					</tr>
				</tbody>
			</table>
		</logic:notEmpty>
		
		<logic:empty name="disParSearchPageVO" property="listResult">
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
			<logic:equal name="disParSearchPageVO" property="viewResult" value="true">
    			<td align="right">
				<logic:equal name="disParSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="disParSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="disParSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="disParSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="disParSearchPageVO" property="agregarEnabled"/>
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
	<input type="hidden" name="name"         value="<bean:write name='disParSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>	

	<input type="text" style="display:none"/>
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->		
