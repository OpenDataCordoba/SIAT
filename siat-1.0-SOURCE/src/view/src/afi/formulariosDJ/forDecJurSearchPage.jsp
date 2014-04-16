<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/afi/BuscarForDecJur.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="afi" key="afi.forDecJurSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="forDecJurSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="afi" key="afi.forDecJur.label"/>
					</logic:equal>
					<logic:notEqual name="forDecJurSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="afi" key="afi.forDecJurSearchPage.legend"/>
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
				<td class="normal" colspan="4">
					<html:select name="forDecJurSearchPageVO" property="forDecJur.recurso.id" styleClass="select" >
						<bean:define id="includeRecursoList" name="forDecJurSearchPageVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="forDecJurSearchPageVO" property="forDecJur.recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
				</td>
			</tr>	
			<tr>
				<td><label><bean:message bundle="bal" key="bal.envioOsiris.idEnvioAfip.label"/>: </label></td>
				<td class="normal"><html:text name="forDecJurSearchPageVO" property="forDecJur.envioOsiris.idEnvioAfipView" size="10" maxlength="6"/></td>			
			
				<td><label><bean:message bundle="bal" key="bal.tranAfip.idTransaccionAfip.label"/>: </label></td>
				<td class="normal"><html:text name="forDecJurSearchPageVO" property="forDecJur.tranAfip.idTransaccionAfipView" size="20" maxlength="15"/></td>			
			</tr>
			<tr>
				<td><label><bean:message bundle="afi" key="afi.forDecJur.nroFormulario.label"/>: </label></td>
				<td class="normal">
					<html:select name="forDecJurSearchPageVO" property="forDecJur.nroFormulario" styleClass="select" >
						<html:optionsCollection name="forDecJurSearchPageVO" property="listFormulario" label="fullValue" value="id" />
					</html:select>
				</td>	

				<td><label><bean:message bundle="afi" key="afi.forDecJur.cuit.label"/>: </label></td>
				<td class="normal"><html:text name="forDecJurSearchPageVO" property="forDecJur.cuit" size="15" maxlength="11" /></td>			
			</tr>	
			<tr>			
				<td><label><bean:message bundle="afi" key="afi.forDecJurSearchPage.fechaPresentacionDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="forDecJurSearchPageVO" property="fechaPresentacionDesdeView" styleId="fechaPresentacionDesdeView" size="10" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaPresentacionDesdeView');" id="a_fechaPresentacionDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				<td><label><bean:message bundle="afi" key="afi.forDecJurSearchPage.fechaPresentacionHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="forDecJurSearchPageVO" property="fechaPresentacionHastaView" styleId="fechaPresentacionHastaView" size="10" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaPresentacionHastaView');" id="a_fechaPresentacionHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="afi" key="afi.forDecJurSearchPage.baseImponibleDesde.label"/>: </label></td>
				<td class="normal"><html:text name="forDecJurSearchPageVO" property="baseImponibleDesde" size="10" maxlength="10"/></td>			
			
				<td><label><bean:message bundle="afi" key="afi.forDecJurSearchPage.baseImponibleHasta.label"/>: </label></td>
				<td class="normal"><html:text name="forDecJurSearchPageVO" property="baseImponibleHasta" size="10" maxlength="10"/></td>			
			</tr>
			<tr>
				<td><label><bean:message bundle="afi" key="afi.forDecJurSearchPage.alicuotaDesde.label"/>: </label></td>
				<td class="normal"><html:text name="forDecJurSearchPageVO" property="alicuotaDesde" size="10" maxlength="10"/></td>			
			
				<td><label><bean:message bundle="afi" key="afi.forDecJurSearchPage.alicuotaHasta.label"/>: </label></td>
				<td class="normal"><html:text name="forDecJurSearchPageVO" property="alicuotaHasta" size="10" maxlength="10"/></td>			
			</tr>
			<tr>
				<td><label><bean:message bundle="afi" key="afi.forDecJurSearchPage.adiPublicidadDesde.label"/>: </label></td>
				<td class="normal"><html:text name="forDecJurSearchPageVO" property="adiPublicidadDesde" size="10" maxlength="10"/></td>			
			
				<td><label><bean:message bundle="afi" key="afi.forDecJurSearchPage.adiPublicidadHasta.label"/>: </label></td>
				<td class="normal"><html:text name="forDecJurSearchPageVO" property="adiPublicidadHasta" size="10" maxlength="10"/></td>			
			</tr>
			<tr>
				<td><label><bean:message bundle="afi" key="afi.forDecJurSearchPage.adiMesasYSillasDesde.label"/>: </label></td>
				<td class="normal"><html:text name="forDecJurSearchPageVO" property="adiMesasYSillasDesde" size="10" maxlength="10"/></td>			
			
				<td><label><bean:message bundle="afi" key="afi.forDecJurSearchPage.adiMesasYSillasHasta.label"/>: </label></td>
				<td class="normal"><html:text name="forDecJurSearchPageVO" property="adiMesasYSillasHasta" size="10" maxlength="10"/></td>			
			</tr>
			<tr>
				<td><label><bean:message bundle="afi" key="afi.forDecJurSearchPage.codActividad.label"/>: </label></td>
				<td class="normal"><html:text name="forDecJurSearchPageVO" property="codActividad" size="15" maxlength="15"/></td>			
						
				<td><label>&nbsp;<bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td class="normal">
					<html:select name="forDecJurSearchPageVO" property="forDecJur.estForDecJur.id" styleClass="select" >
						<html:optionsCollection name="forDecJurSearchPageVO" property="listEstForDecJur" label="descripcion" value="id" />
					</html:select>
				</td>	
			</tr>		
			<tr>
				<td><label>&nbsp;<bean:message bundle="afi" key="afi.otrosPagos.tipoPago.label"/>: </label></td>
				<td class="normal">
					<html:select name="forDecJurSearchPageVO" property="tipPagDecJur.id" styleClass="select" >
						<html:optionsCollection name="forDecJurSearchPageVO" property="listTipPagDecJur" label="desTipPag" value="id" />
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
		<logic:equal name="forDecJurSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="forDecJurSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<th width="1">&nbsp;</th> <!-- Generar DecJur -->		
							<th align="left"><bean:message bundle="afi" key="afi.forDecJur.periodoFiscal.label"/></th>
							<th align="left"><bean:message bundle="afi" key="afi.forDecJur.cuit.label"/></th>
							<th align="left"><bean:message bundle="afi" key="afi.forDecJur.fechaPresentacion.label"/></th>
						    <th align="left"><bean:message bundle="afi" key="afi.forDecJur.nroFormulario.label"/></th>						
   						    <th align="left"><bean:message bundle="afi" key="afi.forDecJur.codRectif.label"/></th>						
   						    <th align="left"><bean:message bundle="base" key="base.estado.label"/></th>	
						</tr>
							
						<logic:iterate id="ForDecJurVO" name="forDecJurSearchPageVO" property="listResult">
							<tr>																
								<logic:notEqual name="forDecJurSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="forDecJurSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="ForDecJurVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="ForDecJurVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>		
									<!-- GenerarTransaccion -->
									<td>
										<logic:equal name="forDecJurSearchPageVO" property="generarDecJurEnabled" value="enabled">		
											<logic:equal name="ForDecJurVO" property="generarDecJurEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('generarDecJur', '<bean:write name="ForDecJurVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="afi" key="afi.forDecJurSearchPage.button.generarDecJur"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/aplicar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="ForDecJurVO" property="generarDecJurEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/aplicar1.gif"/>
											</logic:notEqual>									
										</logic:equal>							
										<logic:notEqual name="forDecJurSearchPageVO" property="generarDecJurEnabled" value="enabled">		
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/aplicar1.gif"/>
										</logic:notEqual>
									</td>							
								</logic:notEqual>					
									<td><bean:write name="ForDecJurVO" property="periodoFiscalView"/>&nbsp;</td>		
									<td><bean:write name="ForDecJurVO" property="cuit"/>&nbsp;</td>						
									<td><bean:write name="ForDecJurVO" property="fechaPresentacionView"/>&nbsp;</td>						
									<td><bean:write name="ForDecJurVO" property="nroFormularioView"/>&nbsp;</td>																
									<td><bean:write name="ForDecJurVO" property="codRectifView"/>&nbsp;</td>																
									<td><bean:write name="ForDecJurVO" property="estForDecJur.descripcion"/>&nbsp;</td>	
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="forDecJurSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="forDecJurSearchPageVO" property="listResult">
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
		</tr>
	</table>
	<input type="hidden" name="name"  value="<bean:write name='forDecJurSearchPageVO' property='name'/>" id="name"/>
   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
		
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