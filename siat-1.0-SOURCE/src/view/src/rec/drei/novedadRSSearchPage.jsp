<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rec/BuscarNovedadRS.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="rec" key="rec.novedadRSSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="rec" key="rec.novedadRSSearchPage.legend"/></p>
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
	<legend><bean:message bundle="rec" key="rec.novedadRSAdapter.filtros.label"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="rec" key="rec.tipoTramiteRS.label"/>: </label></td>
				<td class="normal">
					<html:select name="novedadRSSearchPageVO" property="novedadRS.tipoTramiteRS.id" styleClass="select">
						<html:optionsCollection name="novedadRSSearchPageVO" property="listTipoTramiteRS" label="desTipoTramite" value="id"/>
					</html:select>
				</td>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal">
					<bean:write name="novedadRSSearchPageVO" property="recurso.desRecurso"/>
				</td>
			</tr>		
			<tr>
				<td><label><bean:message bundle="rec" key="rec.novedadRSSearchPage.fechaNovedadDesde.label"/>: </label></td>
				<td class="normal"><html:text name="novedadRSSearchPageVO" property="fechaNovedadDesdeView" size="12" styleId="fechaDesdeView"/>
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				<td><label><bean:message bundle="rec" key="rec.novedadRSSearchPage.fechaNovedadHasta.label"/>: </label></td>
				<td class="normal"><html:text name="novedadRSSearchPageVO" property="fechaNovedadHastaView" size="12" styleId="fechaHastaView"/>
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
				<td class="normal"><html:text name="novedadRSSearchPageVO" property="novedadRS.nroCuenta" size="20" styleClass="datos" /></td>
				<td><label><bean:message bundle="rec" key="rec.tipoTramiteRS.estadoNovedad.ref"/>: </label></td>
				<td class="normal">
					<html:select name="novedadRSSearchPageVO" property="novedadRS.estado.id" styleClass="select">
						<html:optionsCollection name="novedadRSSearchPageVO" property="listEstadoRS" label="descripcion" value="value"/>
					</html:select>
				</td>
			</tr>
			 <!-- Filtro para buscar solo los que tengan observaciones -->
			<tr>
				<td class="normal">
				</td>
			    <td class="normal" colspan="4">
			    <logic:equal name="novedadRSSearchPageVO" property="filtroConObservacion" value="true">
					<input type="checkbox" name="filtroConObservacion" checked>
					<label><bean:message bundle="rec" key="rec.novedadRSSearchPage.filtroConObservacion"/></label>	
			    </logic:equal>
			    <logic:equal name="novedadRSSearchPageVO" property="filtroConObservacion" value="false">
					<input type="checkbox" name="filtroConObservacion">
					<label><bean:message bundle="rec" key="rec.novedadRSSearchPage.filtroConObservacion"/></label>	
			    </logic:equal>			    
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
		<logic:equal name="novedadRSSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="novedadRSSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<th width="1">&nbsp;</th> <!-- Aplicar -->
							<th align="left"><bean:message bundle="rec" key="rec.novedadRS.fechaTransaccion.label"/></th>
							<th align="left"><bean:message bundle="rec" key="rec.tipoTramiteRS.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
							<th align="left"><bean:message bundle="rec" key="rec.novedadRS.tipoTransaccion.label"/></th>
							<th align="left"><bean:message bundle="rec" key="rec.novedadRSSearchPage.mesAnioInicio.label"/></th>
							<th align="left"><bean:message bundle="rec" key="rec.novedadRSSearchPage.procesado.label"/></th>
						</tr>
							
						<logic:iterate id="NovedadRSVO" name="novedadRSSearchPageVO" property="listResult">
							<tr>								
									<!-- Ver -->
									<td>
										<logic:equal name="novedadRSSearchPageVO" property="verBussEnabled" value="true">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="NovedadRSVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="novedadRSSearchPageVO" property="verBussEnabled" value="true">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Aplicar -->
									<td>
										<logic:equal name="novedadRSSearchPageVO" property="aplicarEnabled" value="enabled">		
											<logic:equal name="NovedadRSVO" property="aplicarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('aplicar', '<bean:write name="NovedadRSVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="rec" key="rec.novedadRSSearchPage.button.aplicar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/aplicar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="NovedadRSVO" property="aplicarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/aplicar1.gif"/>
											</logic:notEqual>									
										</logic:equal>							
										<logic:notEqual name="novedadRSSearchPageVO" property="aplicarEnabled" value="enabled">		
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/aplicar1.gif"/>
										</logic:notEqual>
									</td>
								<td><bean:write name="NovedadRSVO" property="fechaTransaccionView"/>&nbsp;</td>
								<td><bean:write name="NovedadRSVO" property="tipoTramiteRS.desTipoTramite"/>&nbsp;</td>
								<td><bean:write name="NovedadRSVO" property="nroCuenta"/>&nbsp;</td>
								<td><bean:write name="NovedadRSVO" property="tipoTransaccion"/>&nbsp;</td>
								<td><bean:write name="NovedadRSVO" property="mesAnioInicioView"/>&nbsp;</td>
								<td><bean:write name="NovedadRSVO" property="estadoView"/>&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="novedadRSSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="novedadRSSearchPageVO" property="listResult">
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
			<logic:equal name="novedadRSSearchPageVO" property="viewResult" value="true">
  	    			<td align="right" width="50%">
					<bean:define id="aplicarMasivoEnabled" name="novedadRSSearchPageVO" property="aplicarMasivoBussEnabled"/>
					<input type="button" <%=aplicarMasivoEnabled%> class="boton" 
						onClick="submitForm('aplicarMasivo', '0');" 
						value="<bean:message bundle="rec" key="rec.novedadRSSearchPage.button.aplicarMasivo"/>"
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
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
