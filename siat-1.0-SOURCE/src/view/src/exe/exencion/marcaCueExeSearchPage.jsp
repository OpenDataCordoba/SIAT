<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/exe/BuscarMarcaCueExe.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="exe" key="exe.marcaCueExeSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="exe" key="exe.marcaCueExeSearchPage.legend"/></p>
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
				<td class="normal" colspan="3">
					<logic:equal name="marcaCueExeSearchPageVO" property="disableCombo" value="true">
						<html:select name="marcaCueExeSearchPageVO" property="cueExe.recurso.id" styleClass="select" disabled="true">
							<html:optionsCollection name="marcaCueExeSearchPageVO" property="listRecurso" label="desRecurso" value="id" />
						</html:select>				
					</logic:equal>
					<logic:equal name="marcaCueExeSearchPageVO" property="disableCombo" value="false">
						<html:select name="marcaCueExeSearchPageVO" property="cueExe.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');">
							<bean:define id="includeRecursoList" name="marcaCueExeSearchPageVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="marcaCueExeSearchPageVO" property="cueExe.recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
					</logic:equal>	
				</td>
			<tr>
			
			<tr>	
				<td><label><bean:message bundle="exe" key="exe.exencion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<logic:equal name="marcaCueExeSearchPageVO" property="disableCombo" value="true">
						<html:select name="marcaCueExeSearchPageVO" property="cueExe.exencion.id" styleClass="select" disabled="true">
							<html:optionsCollection name="marcaCueExeSearchPageVO" property="listExencion" label="desExencion" value="id" />
						</html:select>
					</logic:equal>
					<logic:equal name="marcaCueExeSearchPageVO" property="disableCombo" value="false">
						<html:select name="marcaCueExeSearchPageVO" property="cueExe.exencion.id" styleClass="select">
							<html:optionsCollection name="marcaCueExeSearchPageVO" property="listExencion" label="desExencion" value="id" />
						</html:select>
					</logic:equal>	
				</td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="marcaCueExeSearchPageVO" property="cueExe.cuenta.numeroCuenta" size="20"/>
					<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
						<bean:message bundle="exe" key="exe.cueExeSearchPage.button.buscarCuenta"/>
					</html:button>
				</td>			
			</tr>
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="marcaCueExeSearchPageVO" property="cueExe.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="marcaCueExeSearchPageVO" property="cueExe.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="marcaCueExeSearchPageVO" property="cueExe.estadoCueExe.id" styleClass="select">
						<html:optionsCollection name="marcaCueExeSearchPageVO" property="listEstadoCueExe" label="desEstadoCueExe" value="id" />
					</html:select>
				</td>
			</tr>
			
			
			<tr>
				<td><label>Estado en Historico: </label></td>
				<td class="normal" colspan="3">
					<html:select name="marcaCueExeSearchPageVO" property="cueExe.hisEstCueExe.estadoCueExe.id" styleClass="select">
						<html:optionsCollection name="marcaCueExeSearchPageVO" property="listEstadoCueExe" label="desEstadoCueExe" value="id" />
					</html:select>
					
					&nbsp; Incluir
					<html:radio name="marcaCueExeSearchPageVO" property="estadoEnHistorico" value="true"/>
					&nbsp; Excluir
					<html:radio name="marcaCueExeSearchPageVO" property="estadoEnHistorico" value="false"/>
				</td>
			</tr>
			
			<!-- <#Filtros#> -->
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
		<logic:equal name="marcaCueExeSearchPageVO" property="viewResult" value="true">			
			<logic:notEmpty  name="marcaCueExeSearchPageVO" property="listResult">	
				<div id="resultadoFiltro" class="scrolable" style="height: 500px;">			
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<caption><bean:message bundle="base" key="base.resultadoBusqueda"/>(<bean:write name="marcaCueExeSearchPageVO" property="cantResult"/>)</caption>
		               	<tbody>
			               	<tr>
								<th width="1"><input type="checkbox" onclick="changeChk('filter', 'idsSelected', this)"/></th> <!-- Seleccionar -->
								<th align="left"><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/></th>
								<th align="left"><bean:message bundle="exe" key="exe.cueExe.fechaSolicitud.label"/></th>							
								<th align="left"><bean:message bundle="exe" key="exe.exencion.label"/></th>
								<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
								<!-- <#ColumnTitles#> -->
							</tr>
								
							<logic:iterate id="CueExeVO" name="marcaCueExeSearchPageVO" property="listResult">
								<tr>
									<td>	
										<html:multibox name="marcaCueExeSearchPageVO" property="idsSelected">
											<bean:write name="CueExeVO" property="idView"/>										
										</html:multibox>
									</td>																
									
									<td><bean:write name="CueExeVO" property="cuenta.numeroCuenta" />&nbsp;</td>
									<td><bean:write name="CueExeVO" property="fechaSolicitudView" />&nbsp;</td>								
									<td><bean:write name="CueExeVO" property="exencion.desExencion" />&nbsp;</td>
									<td><bean:write name="CueExeVO" property="estadoCueExe.desEstadoCueExe" />&nbsp;</td>
									<!-- <#ColumnFiedls#> -->
								</tr>
							</logic:iterate>
							
						</tbody>
					</table>
				</div>	
				<table width="100%">
						<tr>
							<td class="normal" align="center" width="100%">
								<label>(*)&nbsp;<bean:message bundle="exe" key="exe.marcaCueExeSearchPage.marcarEstado.label"/>: </label>
								<html:select name="marcaCueExeSearchPageVO" property="estadoACambiar.id" styleClass="select">
									<html:optionsCollection name="marcaCueExeSearchPageVO" property="listEstadoAMarcar" label="desEstadoCueExe" value="id" />
								</html:select>
							</td>
						</tr>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="marcaCueExeSearchPageVO" property="listResult">
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
	<!-- Fin Resultado Filtro -->

	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left" width="50%">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<logic:equal name="marcaCueExeSearchPageVO" property="viewResult" value="true">	
				<logic:notEmpty name="marcaCueExeSearchPageVO" property="listResult">			
					<td align="right" width="50%">
		    			<html:button property="btnMarcar"  styleClass="boton" onclick="submitForm('marcarCueExe', '');">
							<bean:message bundle="exe" key="exe.marcaCueExeSearchPage.button.marcarEstado"/>
						</html:button>
					</td>
				</logic:notEmpty>
			</logic:equal>		
		</tr>
	</table>
	<input type="hidden" name="name"  value="<bean:write name='marcaCueExeSearchPageVO' property='name'/>" id="name"/>
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
