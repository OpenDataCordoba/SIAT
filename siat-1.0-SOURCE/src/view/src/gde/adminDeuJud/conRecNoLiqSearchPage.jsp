<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarConRecNoLiq.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>

	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="conRecNoLiqSearchPageVO"/>
		<bean:define id="poseeParam" value="true" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
			
		<h1><bean:message bundle="gde" key="gde.conRecNoLiqSearchPage.title"/></h1>	
	
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p>
						<logic:equal name="conRecNoLiqSearchPageVO" property="modoSeleccionar" value="true">
							<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
							<bean:message bundle="gde" key="gde.conRecNoLiqSearchPage.label"/>
						</logic:equal>
						<logic:notEqual name="conRecNoLiqSearchPageVO" property="modoSeleccionar" value="true">
							<bean:message bundle="gde" key="gde.conRecNoLiqSearchPage.legend"/>
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
			
			<!-- Tipo de Pago (hecho a mano) -->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.conRecNoLiqSearchPage.tipoPago.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="conRecNoLiqSearchPageVO" property="tipoPago" styleClass="select" onchange="submitForm('paramTipoPago', '');">
						<html:option value="-1">Seleccionar...</html:option>
						<html:option value="1"><bean:message bundle="gde" key="gde.convenio.label"/></html:option>
						<html:option value="2"><bean:message bundle="gde" key="gde.conRecNoLiqSearchPage.reciboDeuda.label"/></html:option>
					</html:select>
				</td>
				
			</tr>
			
			<tr>	
			<!-- Recurso -->	
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">			
					<html:select name="conRecNoLiqSearchPageVO" property="idRecurso" styleClass="select" onchange="submitForm('paramRecurso', '');" styleId="cboRecurso" style="width:90%">
						<bean:define id="includeRecursoList" name="conRecNoLiqSearchPageVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="conRecNoLiqSearchPageVO" property="idRecurso"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
					
					<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
						<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
						src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
					</a>
							
				</td>
				
			</tr>
			
			<!-- Numero -->
			<tr>	
				<td><label><bean:message bundle="gde" key="gde.conRecNoLiqSearchPage.numero.label"/>: </label></td>
				<td class="normal"><html:text name="conRecNoLiqSearchPageVO" property="numero" size="15" maxlength="100"/></td>
				<td><label><bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
				<td class="normal">			
					<html:select name="conRecNoLiqSearchPageVO" property="idProcurador" styleClass="select">
						<html:optionsCollection name="conRecNoLiqSearchPageVO" property="listProcurador" label="descripcion" value="id"/>
					</html:select>							
				</td>					
											
			</tr>
	
			<logic:notEqual name="conRecNoLiqSearchPageVO" property="tipoPago" value="-1">
				<!-- fechaDesde -->
				<tr>
					<td>
						<label>
							<logic:equal name="conRecNoLiqSearchPageVO" property="tipoPago" value="1">
								<!-- fecha formalizacion convenio Desde-->
								<bean:message bundle="gde" key="gde.conRecNoLiqSearchPage.convenio.fechaFormalizacionDesde.label"/>
							</logic:equal>
							<logic:equal name="conRecNoLiqSearchPageVO" property="tipoPago" value="2">
								<!-- fecha emision recibo Desde -->
								<bean:message bundle="gde" key="gde.conRecNoLiqSearchPage.recibo.fechaEmisionDesde.label"/> 
							</logic:equal>
						:</label>
					</td>
					<td class="normal">
						<html:text name="conRecNoLiqSearchPageVO" property="fechaDesdeView" styleId="fechaDesdeView" size="12" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					
				<!-- fecha Hasta -->	
					<td>
						<label>
							<logic:equal name="conRecNoLiqSearchPageVO" property="tipoPago" value="1">
								<!-- fecha formalizacion convenio Hasta -->
								<bean:message bundle="gde" key="gde.conRecNoLiqSearchPage.convenio.fechaFormalizacionHasta.label"/>
							</logic:equal>
							<logic:equal name="conRecNoLiqSearchPageVO" property="tipoPago" value="2">
								<!-- fecha emision recibo Hasta -->
								<bean:message bundle="gde" key="gde.conRecNoLiqSearchPage.recibo.fechaEmisionHasta.label"/> 
							</logic:equal>
						:</label>
					</td>
					<td class="normal">
						<html:text name="conRecNoLiqSearchPageVO" property="fechaHastaView" styleId="fechaHastaView" size="12" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>				
				</tr>
			</logic:notEqual>
				<!-- <#Filtros#> -->
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
			<logic:equal name="conRecNoLiqSearchPageVO" property="viewResult" value="true">
				<logic:notEmpty  name="conRecNoLiqSearchPageVO" property="listResult">
					<!-- div class="scrolable" style="height: 500px;"-->	
						<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
							<caption>
								<bean:message bundle="base" key="base.resultadoBusqueda"/>
								(<bean:write name="conRecNoLiqSearchPageVO" property="maxRegistros" bundle="base" formatKey="general.format.id"/>)	
							</caption>
			               	<tbody>
				               	<tr>
									<th width="1"><input type="checkbox" onclick="changeChk('filter', 'idsSelected', this)"/></th> <!-- Check -->
									<th width="1">&nbsp;</th> <!-- volver liquidable -->
									<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
									<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
									<th align="left"><bean:message bundle="gde" key="gde.procurador.label"/></th>
									
									<!-- titulo de la columna depende del tipo de resultado -->
									<logic:equal name="conRecNoLiqSearchPageVO" property="resultTipoRecibo" value="true">
										<th align="left"><bean:message bundle="gde" key="gde.conRecNoLiqSearchPage.recibo.label"/></th>
										<th align="left"><bean:message bundle="gde" key="gde.conRecNoLiqSearchPage.recibo.fechaEmision.label"/></th>
									</logic:equal>
									<logic:equal name="conRecNoLiqSearchPageVO" property="resultTipoRecibo" value="false">
											<th align="left"><bean:message bundle="gde" key="gde.convenio.label"/></th>							
											<th align="left"><bean:message bundle="gde" key="gde.conRecNoLiqSearchPage.convenio.fechafor.label"/></th>
									</logic:equal>
									
									<th align="left"><bean:message bundle="gde" key="gde.conRecNoLiqSearchPage.importe.label"/></th>
									<!-- <#ColumnTitles#> -->
								</tr>
									
								<logic:iterate id="objectVO" name="conRecNoLiqSearchPageVO" property="listResult">
									<tr>
										<!-- check -->
										<td>											
											<logic:equal name="objectVO" property="noLiqComPro" value="0">
												<html:multibox name="conRecNoLiqSearchPageVO" property="idsSelected">
													<bean:write name="objectVO" property="idView"/>										
												</html:multibox>
											</logic:equal>
											<logic:equal name="objectVO" property="noLiqComPro" value="1">
												<input type="checkbox" disabled="disabled"/>
											</logic:equal>
										</td>
																		
										<!-- volver liquidable -->
										<td>
											<logic:equal name="objectVO" property="noLiqComPro" value="1">
												<logic:equal name="conRecNoLiqSearchPageVO" property="volverLiquidable" value="enabled">
													<a style="cursor: pointer; cursor: hand;" 
														onclick="submitForm('volverLiquidableConRecNoLiq', '<bean:write name="objectVO" property="idView"/>');">											
														<img title="<bean:message bundle="gde" key="gde.conRecNoLiqSearchPage.button.volveLiquidable"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/activar0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="conRecNoLiqSearchPageVO" property="volverLiquidable" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
												</logic:notEqual>	
											</logic:equal>
											<logic:equal name="objectVO" property="noLiqComPro" value="0">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>										
											</logic:equal>												
										</td>								
																										
										<!-- Si es un recibo -->
										<logic:equal name="conRecNoLiqSearchPageVO" property="resultTipoRecibo" value="true">									
											<td><bean:write name="objectVO" property="recurso.desRecurso"/></td>
											<td><bean:write name="objectVO" property="cuenta.nroCuenta"/></td>	
											<td><bean:write name="objectVO" property="procurador.descripcion"/></td>
											<td><bean:write name="objectVO" property="numeroReciboView"/></td>
											<td><bean:write name="objectVO" property="fechaVtoView"/></td>
											<td><bean:write name="objectVO" property="totalPagar" bundle="base" formatKey="general.format.currency"/></td>									
										</logic:equal>
										
										<!-- Si es un convenioCuota -->
										<logic:equal name="conRecNoLiqSearchPageVO" property="resultTipoRecibo" value="false">
											<td><bean:write name="objectVO" property="recurso.desRecurso"/>&nbsp;</td>
											<td><bean:write name="objectVO" property="cuenta.numeroCuenta"/>&nbsp;</td>	
											<td><bean:write name="objectVO" property="procurador.descripcion"/>&nbsp;</td>
											<td><bean:write name="objectVO" property="nroConvenioView"/>&nbsp;</td>
											<td><bean:write name="objectVO" property="fechaForView"/>&nbsp;</td>
											<td><bean:write name="objectVO" property="totImporteConvenio" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>								
										</logic:equal>																				
									</tr>
								</logic:iterate>
							
								<tr>
									<td class="paginador" align="center" colspan="20">
										<bean:define id="pager" name="conRecNoLiqSearchPageVO"/>
										<%@ include file="/base/pager.jsp" %>
									</td>
								</tr>
								
							</tbody>
						</table>
					<!-- /div-->	
				</logic:notEmpty>
				
				<logic:empty name="conRecNoLiqSearchPageVO" property="listResult">
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
				
				<!--  boton procesar -->
				<logic:equal name="conRecNoLiqSearchPageVO" property="viewResult" value="true">
					<logic:notEmpty name="conRecNoLiqSearchPageVO" property="listResult">
		  	    		<td align="right"  width="50%">
		  	    			<logic:equal name="conRecNoLiqSearchPageVO" property="procesarConRecnoLiq" value="enabled">
								<input type="button" class="boton" onClick="submitForm('procesarConRecNoLiq', '');" 
									value="<bean:message bundle="gde" key="gde.conRecNoLiqSearchPage.button.procesar"/>"/>
							</logic:equal>
						</td>
					</logic:notEmpty>	
				</logic:equal>
			</tr>
		</table>

	</span>
		
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="0" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
