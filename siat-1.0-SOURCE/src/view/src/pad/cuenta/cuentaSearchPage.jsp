<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/BuscarCuenta.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>

	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="cuentaSearchPageVO"/>
		<bean:define id="poseeParam" value="false" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
		
		<h1><bean:message bundle="pad" key="pad.cuentaSearchPage.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p><bean:message bundle="pad" key="pad.cuentaSearchPage.legend"/></p>
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
							<html:select name="cuentaSearchPageVO" property="cuentaTitular.cuenta.recurso.id" styleClass="select" styleId="cboRecurso" style="width:90%">
								<bean:define id="includeRecursoList" name="cuentaSearchPageVO" property="listRecurso"/>
								<bean:define id="includeIdRecursoSelected" name="cuentaSearchPageVO" property="cuentaTitular.cuenta.recurso.id"/>
								<%@ include file="/def/gravamen/includeRecurso.jsp" %>
							</html:select>
							
							<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
								<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
								src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
							</a>
							
						</td>
					</tr>
					<tr>
						<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/>: </label></td>
						<td class="normal"><html:text name="cuentaSearchPageVO" property="cuentaTitular.cuenta.numeroCuenta" size="15" maxlength="10" styleClass="datos" /></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="pad" key="pad.contribuyente.label"/>: </label></td>
						<td class="normal">
							<html:text name="cuentaSearchPageVO" property="cuentaTitular.contribuyente.persona.view" size="20" maxlength="100" styleClass="datos"  disabled="true"/>
							<html:button property="btnBucarTitular" styleClass="boton" onclick="submitForm('buscarTitular', '');">
								<bean:message bundle="pad" key="pad.cuentaSearchPage.button.buscarTitular"/>						
							</html:button>
						</td>
					</tr>
				</table>
			
				<p align="center">
					<!-- logi c:n otEqual name="cuentaSearchPageVO" property="modoSeleccionar" value="true" -->
					  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
							<bean:message bundle="base" key="abm.button.limpiar"/>
						</html:button>
						&nbsp;
					<!-- /l og ic: no tEqual-->
				  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
						<bean:message bundle="base" key="abm.button.buscar"/>
					</html:button>
				</p>
				
		</fieldset>	
		<!-- Fin Filtro -->
			
		<!-- Resultado Filtro -->
		<div id="resultadoFiltro">
			<logic:equal name="cuentaSearchPageVO" property="viewResult" value="true">
				<logic:notEmpty  name="cuentaSearchPageVO" property="listResult">	
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
		            	<tbody>
							<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
		                	<tr>
								<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
								<logic:notEqual name="cuentaSearchPageVO" property="modoSeleccionar" value="true">
									<th width="1">&nbsp;</th> <!-- Modificar -->
									<th width="1">&nbsp;</th> <!-- Eliminar -->
									<th width="1">&nbsp;</th> <!-- Activar Desactivar -->							
									<th width="1">&nbsp;</th> <!-- Relacionar Cuentas -->	
								</logic:notEqual>
								<th align="left"><bean:message bundle="pad" key="pad.cuenta.fechaAlta.label"/></th>
								<th align="left"><bean:message bundle="pad" key="pad.cuenta.fechaBaja.label"/></th>
								<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
								<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
							</tr>
									
							<logic:iterate id="CuentaVO" name="cuentaSearchPageVO" property="listResult">
								<tr>
									<!-- Seleccionar -->
									<logic:equal name="cuentaSearchPageVO" property="modoSeleccionar" value="true">
										<td>
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="CuentaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</td>
									</logic:equal>
									
									<logic:notEqual name="cuentaSearchPageVO" property="modoSeleccionar" value="true">
										<td>
											<logic:equal name="cuentaSearchPageVO" property="verEnabled" value="enabled">									
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="CuentaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="cuentaSearchPageVO" property="verEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
											</logic:notEqual>
										</td>
										<td>
											<!-- Modificar-->
											<logic:equal name="cuentaSearchPageVO" property="modificarEnabled" value="enabled">								
												<logic:equal name="CuentaVO" property="modificarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="CuentaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="CuentaVO" property="modificarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="cuentaSearchPageVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</td>
										<td>
											<!-- Eliminar-->
											<logic:equal name="cuentaSearchPageVO" property="eliminarEnabled" value="enabled">												
												<logic:equal name="CuentaVO" property="eliminarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="CuentaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
													</a>
												</logic:equal>	
												<logic:notEqual name="CuentaVO" property="eliminarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="cuentaSearchPageVO" property="eliminarEnabled" value="enabled">										
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</td>
										<td>
											<!-- Activar -->
											<logic:notEqual name="CuentaVO" property="estado.esActivo" value="true">
												<logic:equal name="cuentaSearchPageVO" property="activarEnabled" value="enabled">
													<logic:equal name="CuentaVO" property="activarEnabled" value="enabled">
														<a style="cursor: pointer; cursor: hand;" onclick="submitForm('activar', '<bean:write name="CuentaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
															<img title="<bean:message bundle="base" key="abm.button.activar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/activar0.gif"/>
														</a>
													</logic:equal> 
													<logic:notEqual name="CuentaVO" property="activarEnabled" value="enabled">
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
													</logic:notEqual>
												</logic:equal>
												<logic:notEqual name="cuentaSearchPageVO" property="activarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
												</logic:notEqual>
											</logic:notEqual> 
											<!-- Desactivar -->
											<logic:equal name="CuentaVO" property="estado.esActivo" value="true">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
											</logic:equal>
										</td>
										<td>
											<!-- Relacionar-->
											<logic:equal name="cuentaSearchPageVO" property="relacionarEnabled" value="enabled">								
												<logic:equal name="CuentaVO" property="relacionarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('relacionar', '<bean:write name="CuentaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.relacionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/relacionar0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="CuentaVO" property="relacionarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/relacionar1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="cuentaSearchPageVO" property="relacionarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/relacionar1.gif"/>
											</logic:notEqual>
										</td>
										
									</logic:notEqual>									
									
									<td><bean:write name="CuentaVO" property="fechaAltaView"/>&nbsp;</td>
									<td><bean:write name="CuentaVO" property="fechaBajaView" />&nbsp;</td>
									<td><bean:write name="CuentaVO" property="recurso.desRecurso" />&nbsp;</td>
									<td><bean:write name="CuentaVO" property="numeroCuenta" />&nbsp;</td>
								</tr>
							</logic:iterate>
							<tr>
								<td class="paginador" align="center" colspan="20">
									<bean:define id="pager" name="cuentaSearchPageVO"/>
									<%@ include file="/base/pager.jsp" %>
								</td>
							</tr>
						</tbody>
					</table>
				</logic:notEmpty>
				
				<logic:empty name="cuentaSearchPageVO" property="listResult">
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
		            	<tbody>
							<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
							<tr>
								<td align="center">
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
				<logic:equal name="cuentaSearchPageVO" property="viewResult" value="true">
	  	    		<td align="right">
	  	    			<logic:equal name="cuentaSearchPageVO" property="modoSeleccionar" value="false">
							<bean:define id="agregarEnabled" name="cuentaSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
						</logic:equal>
	  	    			<logic:equal name="cuentaSearchPageVO" property="modoSeleccionar" value="true">
	  	    				<logic:equal name="cuentaSearchPageVO" property="agregarEnSeleccion" value="true">
								<bean:define id="agregarEnabled" name="cuentaSearchPageVO" property="agregarEnabled"/>
								<input type="button" <%=agregarEnabled%> class="boton" 
									onClick="submitForm('agregar', '0');" 
									value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
							</logic:equal>
						</logic:equal>
					</td>
				</logic:equal>
			</tr>
		</table>

	</span>

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
