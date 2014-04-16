<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/BuscarEmisionPuntual.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>

	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="emisionPuntualSearchPageVO"/>
		<bean:define id="poseeParam" value="false" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
		
		<h1><bean:message bundle="emi" key="emi.emisionPuntualSearchPage.title"/></h1>	
			
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p>
						<logic:equal name="emisionPuntualSearchPageVO" property="modoSeleccionar" value="true">
							<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
							<bean:message bundle="emi" key="emi.emisionPuntualSearchPage.label"/>
						</logic:equal>
						<logic:notEqual name="emisionPuntualSearchPageVO" property="modoSeleccionar" value="true">
							<bean:message bundle="emi" key="emi.emisionPuntualSearchPage.legend"/>
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
				<tr>
					<!-- Recurso -->
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:select name="emisionPuntualSearchPageVO" property="emision.recurso.id" styleClass="select" styleId="cboRecurso" style="width:90%">
							<bean:define id="includeRecursoList" name="emisionPuntualSearchPageVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="emisionPuntualSearchPageVO" property="emision.recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
						
						<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
							<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
							src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
						</a>
							
					</td>
				</tr>
				
				<tr>
					<!-- Cuenta -->	
					<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>:</label></td>
					<td class="normal" colspan="3">
						<html:text name="emisionPuntualSearchPageVO" property="emision.cuenta.numeroCuenta" size="10" maxlength="12"/>
						<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
							<bean:message bundle="emi" key="emi.emisionPuntualSearchPage.button.buscarCuenta"/>
						</html:button>
					</td>
				</tr>
	
				<tr>
					<!-- Fecha Desde -->
					<td><label><bean:message bundle="emi" key="emi.emisionPuntualSearchPage.fechaDesde.label"/>: </label></td>
					<td class="normal">
						<html:text name="emisionPuntualSearchPageVO" property="fechaDesdeView" styleId="fechaDesdeView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					
					<!-- Fecha Hasta -->
					<td><label><bean:message bundle="emi" key="emi.emisionPuntualSearchPage.fechaHasta.label"/>: </label></td>
					<td class="normal">
						<html:text name="emisionPuntualSearchPageVO" property="fechaHastaView" styleId="fechaHastaView" size="10" maxlength="10" styleClass="datos" />
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
			  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
					<bean:message bundle="base" key="abm.button.buscar"/>
				</html:button>
			</p>
		</fieldset>	
		<!-- Fin Filtro -->
			
		<!-- Resultado Filtro -->
		<div id="resultadoFiltro">
			<logic:equal name="emisionPuntualSearchPageVO" property="viewResult" value="true">
				<logic:notEmpty  name="emisionPuntualSearchPageVO" property="listResult">	
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
		               	<tbody>
			               	<tr>
								<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
								<th align="left"><bean:message bundle="emi" key="emi.emision.recurso.label"/></th>
								<th align="left"><bean:message bundle="emi" key="emi.emision.cuenta.label"/></th>
								<th align="left"><bean:message bundle="emi" key="emi.emision.fechaEmision.label"/></th>
							</tr>
								
							<logic:iterate id="EmisionVO" name="emisionPuntualSearchPageVO" property="listResult">
								<tr>
									<!-- Seleccionar -->
									<logic:equal name="emisionPuntualSearchPageVO" property="modoSeleccionar" value="true">
										<td>	
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="EmisionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</td>
									</logic:equal>									
									<logic:notEqual name="emisionPuntualSearchPageVO" property="modoSeleccionar" value="true">
										<!-- Ver -->
										<td>
											<logic:equal name="emisionPuntualSearchPageVO" property="verEnabled" value="enabled">									
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="EmisionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="EmisionVO" property="verEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
											</logic:notEqual>
										</td>
									</logic:notEqual>	
									<td><bean:write name="EmisionVO" property="recurso.desRecurso"/>&nbsp;</td>
									<td><bean:write name="EmisionVO" property="cuenta.numeroCuenta"/>&nbsp;</td>
									<td><bean:write name="EmisionVO" property="fechaEmisionView"/>&nbsp;</td>
								</tr>
							</logic:iterate>
					
							<tr>
								<td class="paginador" align="center" colspan="20">
									<bean:define id="pager" name="emisionPuntualSearchPageVO"/>
									<%@ include file="/base/pager.jsp" %>
								</td>
							</tr>
							
						</tbody>
					</table>
				</logic:notEmpty>
				
				<logic:empty name="emisionPuntualSearchPageVO" property="listResult">
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
				<logic:equal name="emisionPuntualSearchPageVO" property="viewResult" value="true">
					<td align="right" width="50%">
	  	    			<logic:equal name="emisionPuntualSearchPageVO" property="modoSeleccionar" value="false">
							<bean:define id="agregarEnabled" name="emisionPuntualSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
						</logic:equal>
	  	    			<logic:equal name="emisionPuntualSearchPageVO" property="modoSeleccionar" value="true">
	  	    				<logic:equal name="emisionPuntualSearchPageVO" property="agregarEnSeleccion" value="true">
								<bean:define id="agregarEnabled" name="emisionPuntualSearchPageVO" property="agregarEnabled"/>
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
<!-- emisionPuntualSearchPage.jsp -->