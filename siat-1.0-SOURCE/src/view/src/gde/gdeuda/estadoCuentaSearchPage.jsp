<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarLiqEstadoCuenta.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>

	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="EstadoCuentaSearchPageVO"/>
		<bean:define id="poseeParam" value="true" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
			
		<h1><bean:message bundle="gde" key="gde.estadoCuentaSearchPage.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p>
						<logic:equal name="EstadoCuentaSearchPageVO" property="modoSeleccionar" value="true">
							<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
							<bean:message bundle="gde" key="gde.estadoCuenta.label"/>
						</logic:equal>
						<logic:notEqual name="EstadoCuentaSearchPageVO" property="modoSeleccionar" value="true">
							<bean:message bundle="gde" key="gde.estadoCuentaSearchPage.legend"/>
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
			<!-- Recurso -->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="EstadoCuentaSearchPageVO" property="cuenta.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');" styleId="cboRecurso" style="width:90%">
						<bean:define id="includeRecursoList" name="EstadoCuentaSearchPageVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="EstadoCuentaSearchPageVO" property="cuenta.recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
					
					<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
						<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
						src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
					</a>
				</td>					
			</tr>
			
			<!-- Cuenta -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.estadoCuenta.numeroCuenta.label"/>: </label></td>
				<td class="normal" colspan="3">
					<logic:equal name="EstadoCuentaSearchPageVO" property="habilitarCuentaEnabled" value="true" >
						<html:text name="EstadoCuentaSearchPageVO" property="cuenta.numeroCuenta" size="20" disabled ="false"/>
						<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
							<bean:message bundle="exe" key="exe.cueExeSearchPage.button.buscarCuenta"/>
						</html:button>
					</logic:equal>			
					<logic:notEqual name="EstadoCuentaSearchPageVO" property="habilitarCuentaEnabled" value="true">	
						<html:text name="EstadoCuentaSearchPageVO" property="cuenta.numeroCuenta" size="20" disabled ="true"/>
							<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
							<bean:message bundle="exe" key="exe.cueExeSearchPage.button.buscarCuenta"/>
						</html:button>
					</logic:notEqual>
				</td>
			</tr>
					
			<!-- ViaDeuda -->
			<tr>	
				<td><label><bean:message bundle="def" key="def.viaDeuda.label"/>: </label></td>
				<td class="normal">
					<html:select name="EstadoCuentaSearchPageVO" property="viaDeuda.id" styleClass="select">
						<html:optionsCollection name="EstadoCuentaSearchPageVO" property="listViaDeuda" label="desViaDeuda" value="id" />
					</html:select>
				</td>					
			
			
			<!-- recClaDeu -->		
				<td><label><bean:message bundle="gde" key="gde.estadoCuenta.recClaDeu.label"/>: </label></td>
				<td class="normal">
					<html:select name="EstadoCuentaSearchPageVO" property="recClaDeu.id" styleClass="select">
						<html:optionsCollection name="EstadoCuentaSearchPageVO" property="listRecClaDeu" label="desClaDeu" value="id" />
					</html:select>
				</td>					
			</tr>
			
			<!-- EstadoDeuda -->
			<tr>	
				<td><label><bean:message bundle="gde" key="gde.estadoCuenta.estado.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="EstadoCuentaSearchPageVO" property="estadoDeuda.id" styleClass="select">
						<html:optionsCollection name="EstadoCuentaSearchPageVO" property="listEstadoDeuda" label="desEstadoDeuda" value="id" />
					</html:select>
				</td>					
			</tr>
	
	
			<!-- fechaVtoDesde -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.estadoCuenta.fechaVtoDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="EstadoCuentaSearchPageVO" property="fechaVtoDesdeView" styleId="fechaVtoDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaVtoDesdeView');" id="a_fechaVtoDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				
			<!-- fechaVtoHasta -->
				<td><label><bean:message bundle="gde" key="gde.estadoCuenta.fechaVtoHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="EstadoCuentaSearchPageVO" property="fechaVtoHastaView" styleId="fechaVtoHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaVtoHastaView');" id="a_fechaVtoHastaView">
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
			  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
					<bean:message bundle="base" key="abm.button.buscar"/>
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
