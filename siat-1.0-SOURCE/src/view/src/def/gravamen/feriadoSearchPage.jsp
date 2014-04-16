<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
	<%@include file="/base/submitForm.js"%>
	<%@include file="/base/calendar.js"%>
</script>

<!-- Formulario -->
<html:form styleId="filter" action="/def/BuscarFeriado.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="def" key="def.feriadoSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="def" key="def.feriadoSearchPage.legend"/></p>
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
				<td><label><bean:message bundle="def" key="def.feriadoSearchPage.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="feriadoSearchPageVO" property="fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				
				<td><label><bean:message bundle="def" key="def.feriadoSearchPage.fechaHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="feriadoSearchPageVO" property="fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.feriadoSearchPage.desFeriado.label"/>: </label></td>
				<td class="normal">
					<html:text name="feriadoSearchPageVO" property="desFeriado" size="20" maxlength="100" styleClass="datos" />
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
		<logic:equal name="feriadoSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="feriadoSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>							
							<th width="1">&nbsp;</th> <!-- Activar Desactivar -->							
							<th align="left"><bean:message bundle="def" key="def.feriado.fechaFeriado.label"/></th>
							<th align="left"><bean:message bundle="def" key="def.feriado.desFeriado.label"/></th>
							<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
						</tr>
							
						<logic:iterate id="FeriadoVO" name="feriadoSearchPageVO" property="listResult">
							<tr>
								<td>
									<!-- Activar -->
									<logic:equal name="FeriadoVO" property="estado.value" value="Inactivo">
										<logic:equal name="feriadoSearchPageVO" property="activarEnabled" value="enabled">
											<logic:equal name="FeriadoVO" property="activarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('activar', '<bean:write name="FeriadoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.activar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/activar0.gif"/>
												</a>
											</logic:equal> 
											<logic:notEqual name="FeriadoVO" property="activarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="feriadoSearchPageVO" property="activarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
										</logic:notEqual>
									</logic:equal> 
									<!-- Desactivar -->
									<logic:equal name="FeriadoVO" property="estado.value" value="Activo">
										<logic:equal name="feriadoSearchPageVO" property="desactivarEnabled" value="enabled">
											<logic:equal name="FeriadoVO" property="desactivarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('desactivar', '<bean:write name="FeriadoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.desactivar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="FeriadoVO" property="desactivarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="feriadoSearchPageVO" property="desactivarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
										</logic:notEqual>										
									</logic:equal>
								</td>
								<td><bean:write name="FeriadoVO" property="fechaFeriadoView"/>&nbsp;</td>
								<td><bean:write name="FeriadoVO" property="desFeriado"/>&nbsp;</td>
								<td><bean:write name="FeriadoVO" property="estado.value"/>&nbsp;</td>
							</tr>
						</logic:iterate>
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="feriadoSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="feriadoSearchPageVO" property="listResult">
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
			<logic:equal name="feriadoSearchPageVO" property="viewResult" value="true">
  	    		<td align="right">
					<bean:define id="agregarEnabled" name="feriadoSearchPageVO" property="agregarEnabled"/>
					<input type="button" <%=agregarEnabled%> class="boton" 
						onClick="submitForm('agregar', '0');" 
						value="<bean:message bundle="base" key="abm.button.agregar"/>"
					/>
				</td>
			</logic:equal>
		</tr>
	</table>
		
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<input type="hidden" name="name"         value="<bean:write name='feriadoSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	
	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin formulario -->