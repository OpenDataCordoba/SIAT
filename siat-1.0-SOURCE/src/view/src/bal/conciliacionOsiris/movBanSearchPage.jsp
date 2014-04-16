<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/BuscarMovBan.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="bal" key="bal.movBanSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">				
				<p>
					<logic:equal name="movBanSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="bal" key="bal.movBan.label"/>
					</logic:equal>
					<logic:notEqual name="movBanSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="bal" key="bal.movBanSearchPage.legend"/>
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
			<!-- Fecha Acreditacion Desde/Hasta -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.movBanSearchPage.fechaDesde.label"/>: </label></td>
				<td class="normal">
				<html:text name="movBanSearchPageVO" property="fechaDesdeView" styleId="fechaDesdeView" size="10" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>	
				
				<td><label><bean:message bundle="bal" key="bal.movBanSearchPage.fechaHasta.label"/>: </label></td>
				<td class="normal">
				<html:text name="movBanSearchPageVO" property="fechaHastaView" styleId="fechaHastaView" size="10" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>	
		    </tr>
		    <!-- Banco y Conciliado(si/no) -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.movBan.bancoAdm.label"/>: </label></td>
				<td class="normal"><html:text name="movBanSearchPageVO" property="movBan.bancoAdmView" size="10" maxlength="3" styleClass="datos" /></td>
				
				<td><label><bean:message bundle="bal" key="bal.movBan.conciliado.label"/>: </label></td>
				<td class="normal">
					<html:select name="movBanSearchPageVO" property="movBan.conciliado.id" styleClass="select" >
						<html:optionsCollection name="movBanSearchPageVO" property="listSiNo" label="value" value="id" />
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
		<logic:equal name="movBanSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="movBanSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<th width="1">&nbsp;</th> <!-- Eliminar -->
							<th align="left"><bean:message bundle="bal" key="bal.movBan.fechaAcredit.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.movBan.bancoAdm.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.movBan.totalDebito.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.movBan.totalCredito.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.movBan.conciliado.label"/></th>
						</tr>
							
						<logic:iterate id="MovBanVO" name="movBanSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="movBanSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="MovBanVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="movBanSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="movBanSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="MovBanVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="MovBanVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<td>
									<!-- Eliminar-->								
									<logic:equal name="movBanSearchPageVO" property="eliminarEnabled" value="enabled">		
										<logic:equal name="MovBanVO" property="eliminarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="MovBanVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="MovBanVO" property="eliminarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>									
									</logic:equal>							
									<logic:notEqual name="movBanSearchPageVO" property="eliminarEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</td>									
								</logic:notEqual>
								<td><bean:write name="MovBanVO" property="fechaAcreditView"/>&nbsp;</td>
								<td><bean:write name="MovBanVO" property="bancoAdmView" />&nbsp;</td>
								<td><bean:write name="MovBanVO" property="totalDebitoView" />&nbsp;</td>
								<td><bean:write name="MovBanVO" property="totalCreditoView" />&nbsp;</td>
								<td><bean:write name="MovBanVO" property="conciliado.value"/>&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="movBanSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="movBanSearchPageVO" property="listResult">
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
			<logic:equal name="movBanSearchPageVO" property="viewResult" value="true">
				<td align="right">
  	    			<logic:equal name="movBanSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="movBanSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.leerArchivo"/>"/>
					</logic:equal>
  	    			<logic:equal name="movBanSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="movBanSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="movBanSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.leerArchivo"/>"/>
							</logic:equal>
					</logic:equal>
				</td>				
			</logic:equal>
		</tr>
	</table>
  	<input type="hidden" name="name"  value="<bean:write name='movBanSearchPageVO' property='name'/>" id="name"/>
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
