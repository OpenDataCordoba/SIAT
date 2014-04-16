<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarConvenio.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="convenioSearchPageVO"/>
		<bean:define id="poseeParam" value="false" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
		
		<h1><bean:message bundle="gde" key="gde.convenioSearchPage.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p>
						<bean:message bundle="gde" key="gde.convenioSearchPage.legend"/>
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
					<td class="normal">
						<html:select name="convenioSearchPageVO" property="recurso.id" styleClass="select" styleId="cboRecurso" style="width:90%">
							<bean:define id="includeRecursoList" name="convenioSearchPageVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="convenioSearchPageVO" property="recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
						
						<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
							<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
							src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
						</a>
					</td>
				</tr>
				<tr>
		      		<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
		      		<td class="normal">
		      			<html:text name="convenioSearchPageVO" property="convenio.cuenta.numeroCuenta" size="15" maxlength="20" styleClass="datos"/>
					</td>
				</tr>
				<tr>
		      		<td><label><bean:message bundle="gde" key="gde.convenio.nroConvenio.ref"/>: </label></td>
		      		<td class="normal">
		      			<html:text name="convenioSearchPageVO" property="convenio.nroConvenio" size="15" maxlength="20" styleClass="datos"/>
					</td>
				</tr>
				
				<tr>
					<td colspan="3" align="right"><p>(*) Se debe ingresar Recurso y Cuenta o Numero Convenio</p></td>
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
			<logic:equal name="convenioSearchPageVO" property="viewResult" value="true">
				<logic:notEmpty  name="convenioSearchPageVO" property="listResult">	
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
		               	<tbody>
			               	<tr>
								<th width="1">&nbsp;</th> <!-- Ver-->
								<th align="left"><bean:message bundle="gde" key="gde.convenio.nroConvenio.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.convenio.fechaFor.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.plan.label"/></th>
								<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
								<th align="left"><bean:message bundle="def" key="def.viaDeuda.label"/></th>
								<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
								<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
							</tr>
								
							<logic:iterate id="ConvenioVO" name="convenioSearchPageVO" property="listResult">
								<tr>
									<!-- Ver -->
									<td>
										<logic:equal name="convenioSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="ConvenioVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="convenioSearchPageVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>
									<td><bean:write name="ConvenioVO" property="nroConvenioView"/>&nbsp;</td>
									<td><bean:write name="ConvenioVO" property="fechaForView"/>&nbsp;</td>
									<td><bean:write name="ConvenioVO" property="plan.desPlan"/>&nbsp;</td>
									<td><bean:write name="ConvenioVO" property="recurso.desRecurso"/>&nbsp;</td>
									<td><bean:write name="ConvenioVO" property="viaDeuda.desViaDeuda"/>&nbsp;</td>
									<td><bean:write name="ConvenioVO" property="cuenta.numeroCuenta"/>&nbsp;</td>
									<td><bean:write name="ConvenioVO" property="descEstadoConvenio" />&nbsp;</td>
								</tr>
							</logic:iterate>
					
							<tr>
								<td class="paginador" align="center" colspan="20">
									<bean:define id="pager" name="convenioSearchPageVO"/>
									<%@ include file="/base/pager.jsp" %>
								</td>
							</tr>
							
						</tbody>
					</table>
				</logic:notEmpty>
				
				<logic:empty name="convenioSearchPageVO" property="listResult">
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
