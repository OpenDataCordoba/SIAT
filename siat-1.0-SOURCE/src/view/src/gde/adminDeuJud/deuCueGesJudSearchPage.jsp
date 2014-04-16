<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarDeuCueGesJud.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="deuCueGesJudSearchPageVO"/>
		<bean:define id="poseeParam" value="false" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
			
		<h1><bean:message bundle="gde" key="gde.deuCueGesJudSearchPage.title"/></h1>	
			
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p>
						<bean:message bundle="gde" key="gde.deuCueGesJudSearchPage.legend"/>		
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
				<!-- combo Recurso -->
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:select name="deuCueGesJudSearchPageVO" property="cuenta.recurso.id" styleClass="select" styleId="cboRecurso" style="width:90%">
							<bean:define id="includeRecursoList"       name="deuCueGesJudSearchPageVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="deuCueGesJudSearchPageVO" property="cuenta.recurso.id"/>
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
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal" colspan="3">
							<html:text name="deuCueGesJudSearchPageVO" property="cuenta.numeroCuenta" size="20"/>
							<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
								<bean:message bundle="gde" key="gde.deuCueGesJudSearchPage.button.buscarCuenta"/>
							</html:button>									
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
			<logic:equal name="deuCueGesJudSearchPageVO" property="viewResult" value="true">
				<logic:notEmpty  name="deuCueGesJudSearchPageVO" property="listResult">
					<logic:greaterThan name="deuCueGesJudSearchPageVO" property="cantResult" value="10">
						<div class="scrolable" style="height: 400px;">
					</logic:greaterThan>
					
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
		               	<tbody>
			               	<tr>							
								<th width="1"><bean:message bundle="gde" key="gde.gesJud.label"/>&nbsp;</th> <!-- GesJud -->
								<th width="1"><bean:message bundle="gde" key="gde.procurador.label"/>&nbsp;</th> <!-- procurador -->
								<th width="1"><bean:message bundle="gde" key="gde.gesJud.juzgado.label"/>&nbsp;</th> <!-- juzgado -->
								<th width="1"><bean:message bundle="gde" key="gde.gesJud.caso.label"/>&nbsp;</th> <!-- Caso -->
								<th width="1"><bean:message bundle="gde" key="gde.gesJud.observaciones.label"/>&nbsp;</th> <!-- Obs -->
								
								<th width="1"><bean:message bundle="gde" key="gde.deuda.label"/>&nbsp;</th> <!-- deuda -->
								<th width="1"><bean:message bundle="gde" key="gde.constanciaDeu.label"/>&nbsp;</th> <!-- constancia -->
								<th width="1"><bean:message bundle="gde" key="gde.deuCueGesJudSearchPage.estadoDeuda.label"/>&nbsp;</th> <!-- estado Deuda -->														
								<!-- <#ColumnTitles#> -->
							</tr>
								
							<logic:iterate id="GesJudDeuVO" name="deuCueGesJudSearchPageVO" property="listResult">
								<tr>
									<td><bean:write name="GesJudDeuVO" property="gesJud.desGesJud"/></td>
									<td><bean:write name="GesJudDeuVO" property="gesJud.procurador.descripcion"/></td>
									<td><bean:write name="GesJudDeuVO" property="gesJud.juzgado"/></td>
									<td>
										<logic:present name="GesJudDeuVO" property="gesJud.caso.sistemaOrigen.desSistemaOrigen">
											<bean:write name="GesJudDeuVO" property="gesJud.caso.sistemaOrigen.desSistemaOrigen"/>
											&nbsp;							
											<bean:write name="GesJudDeuVO" property="gesJud.caso.numero"/>
										</logic:present>	
									</td>
									<td><bean:write name="GesJudDeuVO" property="gesJud.observacion"/></td>								
									<td><bean:write name="GesJudDeuVO" property="deuda.periodoView"/>&nbsp;/&nbsp;<bean:write name="GesJudDeuVO" property="deuda.anioView"/></td>
									<td><bean:write name="GesJudDeuVO" property="constanciaDeu.numeroBarraAnioConstanciaView"/></td>
									<td><bean:write name="GesJudDeuVO" property="deuda.estadoDeuda.desEstadoDeuda"/></td>
								</tr>
							</logic:iterate>									
						</tbody>
					</table>
					
					<logic:greaterThan name="deuCueGesJudSearchPageVO" property="cantResult" value="10">
						</div>
					</logic:greaterThan>
					
				</logic:notEmpty>
				
				<logic:empty name="deuCueGesJudSearchPageVO" property="listResult">
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
	</span>
	
	<input type="hidden" name="name"  value="<bean:write name='deuCueGesJudSearchPageVO' property='name'/>" id="name"/>
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
<!-- deuCueGesJudSearchPage.jsp -->