<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/BuscarBroche.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pad" key="pad.brocheSearchPage.title"/></h1>	
		

	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="pad" key="pad.brocheSearchPage.legend"/></p>
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
						<!-- Recurso -->
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:select name="brocheSearchPageVO" property="broche.recurso.id" styleClass="select" >
								<bean:define id="includeRecursoList" name="brocheSearchPageVO" property="listRecurso"/>
								<bean:define id="includeIdRecursoSelected" name="brocheSearchPageVO" property="broche.recurso.id"/>
								<%@ include file="/def/gravamen/includeRecurso.jsp" %>
							</html:select>
						</td>				
					</tr>

					<tr>
						<td><label><bean:message bundle="pad" key="pad.tipoBroche.label"/>: </label></td>
						<td class="normal" colspan="3">
								<html:select name="brocheSearchPageVO" property="broche.tipoBroche.id" styleClass="select" >
								<html:optionsCollection name="brocheSearchPageVO" property="listTipoBroche" label="desTipoBroche" value="id" />
							</html:select>
						</td>
						<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.nro.label"/>: </label></td>
						<td class="normal"><html:text name="brocheSearchPageVO" property="broche.idView" size="10" maxlength="15" /></td>
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
		<logic:equal name="brocheSearchPageVO" property="viewResult" value="true">		
			<logic:notEmpty  name="brocheSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
	                	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="brocheSearchPageVO" property="modoSeleccionar" value="true">
							<th width="1">&nbsp;</th> <!-- Modificar -->
							<th width="1">&nbsp;</th> <!-- Eliminar -->
							<th width="1">&nbsp;</th> <!-- Asignar a Cuenta -->
							<th width="1">&nbsp;</th> <!-- Activar/Desactivar -->
							</logic:notEqual>
						  	<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.tipoBroche.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.broche.nro.label"/></th>
						  	<th align="left"><bean:message bundle="pad" key="pad.broche.desBroche.label"/></th>
						  	<th align="left"><bean:message bundle="pad" key="pad.broche.exentoEnvioJud.label"/></th>
  							<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
						</tr>
						
						<logic:iterate id="BrocheVO" name="brocheSearchPageVO" property="listResult">
							<tr>						
								<!-- Ver/Seleccionar -->
								<td>
									<logic:notEqual name="brocheSearchPageVO" property="modoSeleccionar" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="BrocheVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:notEqual>
									<logic:equal name="brocheSearchPageVO" property="modoSeleccionar" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="BrocheVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>									
								</td>


								<logic:notEqual name="brocheSearchPageVO" property="modoSeleccionar" value="true">								
								<td>
									<!-- Modificar-->								
									<logic:equal name="brocheSearchPageVO" property="modificarEnabled" value="enabled">									
										<logic:equal name="BrocheVO" property="modificarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="BrocheVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="BrocheVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="brocheSearchPageVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</td>
	
								<td>
									<!-- Eliminar-->								
									<logic:equal name="brocheSearchPageVO" property="eliminarEnabled" value="enabled">		
										<logic:equal name="BrocheVO" property="eliminarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="BrocheVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="BrocheVO" property="eliminarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>									
									</logic:equal>							
									<logic:notEqual name="brocheSearchPageVO" property="eliminarEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</td>
								<td>
									<!-- Asignar a Cuenta-->								
									<logic:equal name="brocheSearchPageVO" property="asignarCuentaEnabled" value="enabled">		
										<logic:equal name="BrocheVO" property="asignarCuentaEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('asignarCuenta', '<bean:write name="BrocheVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="pad" key="pad.brocheSearchPage.adm.button.asignarCuenta"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/asignarCuenta0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="BrocheVO" property="asignarCuentaEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/asignarCuenta1.gif"/>
										</logic:notEqual>									
									</logic:equal>							
									<logic:notEqual name="brocheSearchPageVO" property="asignarCuentaEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/asignarCuenta1.gif"/>
									</logic:notEqual>
								</td>
								<td>
								<!-- Activar -->
									<logic:notEqual name="BrocheVO" property="estado.esActivo" value="true">
										<logic:equal name="brocheSearchPageVO" property="activarEnabled" value="enabled">
											<logic:equal name="BrocheVO" property="activarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('activar', '<bean:write name="BrocheVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.activar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/activar0.gif"/>
												</a>
											</logic:equal> 
											<logic:notEqual name="BrocheVO" property="activarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="brocheSearchPageVO" property="activarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
										</logic:notEqual>
									</logic:notEqual> 
									<!-- Desactivar -->
									<logic:equal name="BrocheVO" property="estado.esActivo" value="true">
										<logic:equal name="brocheSearchPageVO" property="desactivarEnabled" value="enabled">
											<logic:equal name="BrocheVO" property="desactivarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('desactivar', '<bean:write name="BrocheVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.desactivar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="BrocheVO" property="desactivarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="brocheSearchPageVO" property="desactivarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
										</logic:notEqual>										
									</logic:equal>
							</td>
							</logic:notEqual>								


						  	    <td><bean:write name="BrocheVO" property="recurso.desRecurso"/>&nbsp;</td>
								<td><bean:write name="BrocheVO" property="tipoBroche.desTipoBroche" />&nbsp;</td>
						 		<td><bean:write name="BrocheVO" property="idView"/>&nbsp;</td>
						  	    <td><bean:write name="BrocheVO" property="desBroche"/>&nbsp;</td>
						 		<td><bean:write name="BrocheVO" property="exentoEnvioJud.value" />&nbsp;</td>
						 		<td><bean:write name="BrocheVO" property="estado.value" />&nbsp;</td>
							</tr>
						</logic:iterate>
					<tr>
						<td class="paginador" align="center" colspan="11">
							<bean:define id="pager" name="brocheSearchPageVO"/>
							<%@ include file="/base/pager.jsp" %>
						</td>
					</tr>
				</tbody>
			</table>
		</logic:notEmpty>
		
		<logic:empty name="brocheSearchPageVO" property="listResult">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
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
		
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left" width="50%">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<logic:equal name="brocheSearchPageVO" property="viewResult" value="true">
    			<td align="right" width="50%">
				<logic:equal name="brocheSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="brocheSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="brocheSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="brocheSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="brocheSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
						</logic:equal>
					</logic:equal>
				</td>
			</logic:equal>
		</tr>
	</table>
	<input type="hidden" name="name"  value="<bean:write name='brocheSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<input type="text" style="display:none"/>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->		