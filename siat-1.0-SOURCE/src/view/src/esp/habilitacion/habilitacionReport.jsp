<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/esp/ReporteHabilitacion.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="esp" key="esp.habilitacionReport.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p><bean:message bundle="esp" key="esp.habilitacionReport.legend"/></p>
					
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
						<html:select name="habilitacionSearchPageVO" property="habilitacion.recurso.id" styleClass="select" >
							<bean:define id="includeRecursoList" name="habilitacionSearchPageVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="habilitacionSearchPageVO" property="habilitacion.recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
						</td>	
					</tr>
					<tr>
						<!-- Tipo Habilitacion -->
						<td><label><bean:message bundle="esp" key="esp.tipoHab.label"/>: </label></td>
						<td class="normal">
							<html:select name="habilitacionSearchPageVO" property="habilitacion.tipoHab.id" styleClass="select">
								<html:optionsCollection name="habilitacionSearchPageVO" property="listTipoHab" label="descripcion" value="id" />
							</html:select>
						</td>
					</tr>					
					<tr>							
						<!-- Anio -->
						<td><label><bean:message bundle="esp" key="esp.habilitacion.anio.label"/>: </label></td>
						<td class="normal"><html:text name="habilitacionSearchPageVO" property="habilitacion.anioView" size="10" maxlength="20" /></td>
					</tr>
					<tr>
						<!-- Fecha Desde -->
						<td><label><bean:message bundle="esp" key="esp.habilitacionReport.fechaDesde.label"/>: </label></td>
						<td class="normal">
							<html:text name="habilitacionSearchPageVO" property="fechaDesdeView" styleId="fechaDesdeView" size="10" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
					
						<!-- Fecha Hasta -->
						<td><label><bean:message bundle="esp" key="esp.habilitacionReport.fechaHasta.label"/>: </label></td>
						<td class="normal">
							<html:text name="habilitacionSearchPageVO" property="fechaHastaView" styleId="fechaHastaView" size="10" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
					</tr>
					<tr>
						<!-- Fecha Desde Evento-->
						<td><label><bean:message bundle="esp" key="esp.habilitacionReport.fechaEventoDesde.label"/>: </label></td>
						<td class="normal">
							<html:text name="habilitacionSearchPageVO" property="fechaEventoDesdeView" styleId="fechaEventoDesdeView" size="10" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaEventoDesdeView');" id="a_fechaEventoDesdeView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
					
						<!-- Fecha Hasta Evento -->
						<td><label><bean:message bundle="esp" key="esp.habilitacionReport.fechaEventoHasta.label"/>: </label></td>
						<td class="normal">
							<html:text name="habilitacionSearchPageVO" property="fechaEventoHastaView" styleId="fechaEventoHastaView" size="10" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaEventoHastaView');" id="a_fechaEventoHastaView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
					</tr>
					<tr>
						<td class="normal" colspan="4">
						<input type="checkbox" name="sinEntVen">
						<label><bean:message bundle="esp" key="esp.habilitacionReport.sinEntVen.label"/></label>
					</tr>
				</td>

				</table>
	
				<p align="center">
				  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
						<bean:message bundle="base" key="abm.button.limpiar"/>
					</html:button>
					&nbsp;
					<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('generarReporte', '1');">
						<bean:message bundle="base" key="abm.button.imprimir"/>
					</html:button>			
				</p>
		</fieldset>	
		<!-- Fin Filtro -->

	<table class="tablabotones">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<input type="hidden" name="name" value="<bean:write name='habilitacionSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>	

	<input type="text" style="display:none"/>
	
	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->		
