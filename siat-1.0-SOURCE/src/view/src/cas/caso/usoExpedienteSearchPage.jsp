<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/cas/BuscarUsoExpediente.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="cas" key="cas.usoExpedienteSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="usoExpedienteSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="cas" key="cas.usoExpediente.label"/>
					</logic:equal>
					<logic:notEqual name="usoExpedienteSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="cas" key="cas.usoExpedienteSearchPage.legend"/>
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
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="usoExpedienteSearchPageVO" property="usoExpediente.cuenta.recurso.id" styleClass="select" >
						<bean:define id="includeRecursoList" name="usoExpedienteSearchPageVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="usoExpedienteSearchPageVO" property="usoExpediente.cuenta.recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
				</td>
			</tr>			
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="usoExpedienteSearchPageVO" property="usoExpediente.cuenta.numeroCuenta" size="20" maxlength="100"/></td>			
			</tr>
			<tr>
				<td><label><bean:message bundle="cas" key="cas.sistemaOrigen.label"/>: </label></td>
				<td class="normal">
					<html:select name="usoExpedienteSearchPageVO" property="usoExpediente.caso.sistemaOrigen.id" styleClass="select" >
						<html:optionsCollection name="usoExpedienteSearchPageVO" property="listSistemaOrigen" label="desSistemaOrigen" value="id" />
					</html:select>
				</td>
				<td><label><bean:message bundle="cas" key="cas.caso.numero.label"/>: </label></td>
				<td class="normal">
					<html:text name="usoExpedienteSearchPageVO" property="usoExpediente.caso.numero" size="20" maxlength="100"/>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="cas" key="cas.usoExpedienteSearchPage.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="usoExpedienteSearchPageVO" property="fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				<td><label><bean:message bundle="cas" key="cas.usoExpedienteSearchPage.fechaHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="usoExpedienteSearchPageVO" property="fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
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
	<div id="resultadoFiltro">
		<logic:equal name="usoExpedienteSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="usoExpedienteSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<th width="1"><bean:message bundle="cas" key="cas.usoExpendiente.fechaAccion.label"/></th>
							<th width="1"><bean:message bundle="cas" key="cas.sistemaOrigen.label"/></th>
							<th width="1"><bean:message bundle="cas" key="cas.usoExpendiente.numero.label"/></th>
							<th width="1"><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/></th>
						</tr>
							
						<logic:iterate id="UsoExpedienteVO" name="usoExpedienteSearchPageVO" property="listResult">
							<tr>
								<!-- Ver -->
								<td>
									<logic:equal name="usoExpedienteSearchPageVO" property="verEnabled" value="enabled">									
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="UsoExpedienteVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="UsoExpedienteVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</td>	
								
								<td><bean:write name="UsoExpedienteVO" property="fechaAccionView"/>&nbsp;</td>
								<td><bean:write name="UsoExpedienteVO" property="sistemaOrigen.desSistemaOrigen"/>&nbsp;</td>
								<td><bean:write name="UsoExpedienteVO" property="numero"/>&nbsp;</td>
								<td><bean:write name="UsoExpedienteVO" property="cuenta.numeroCuenta"/>&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="usoExpedienteSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="usoExpedienteSearchPageVO" property="listResult">
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
		
	<input type="hidden" name="name"  value="<bean:write name='usoExpedienteSearchPageVO' property='name'/>" id="name"/>
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
