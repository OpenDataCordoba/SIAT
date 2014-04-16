<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/BuscarCierreBanco.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="bal" key="bal.cierreBancoSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">				
				<p>
					<logic:equal name="cierreBancoSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="bal" key="bal.cierreBanco.label"/>
					</logic:equal>
					<logic:notEqual name="cierreBancoSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="bal" key="bal.cierreBancoSearchPage.legend"/>
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
			<!-- Fecha Cierre Desde/Hasta -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.cierreBancoSearchPage.fechaDesde.label"/>: </label></td>
				<td class="normal">
				<html:text name="cierreBancoSearchPageVO" property="fechaDesdeView" styleId="fechaDesdeView" size="10" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>	
				
				<td><label><bean:message bundle="bal" key="bal.cierreBancoSearchPage.fechaHasta.label"/>: </label></td>
				<td class="normal">
				<html:text name="cierreBancoSearchPageVO" property="fechaHastaView" styleId="fechaHastaView" size="10" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>	
		    </tr>
		    <!-- Banco y Nro Cierre -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.cierreBanco.banco.label"/>: </label></td>
				<td class="normal"><html:text name="cierreBancoSearchPageVO" property="cierreBanco.bancoView" size="10" maxlength="3" styleClass="datos" /></td>
				
				<td><label><bean:message bundle="bal" key="bal.cierreBanco.nroCierreBanco.label"/>: </label></td>
				<td class="normal"><html:text name="cierreBancoSearchPageVO" property="cierreBanco.nroCierreBancoView" size="10" maxlength="8" styleClass="datos" /></td>
			</tr>
			<!-- Envio -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.envioOsiris.idEnvioAfip.label"/>: </label></td>
				<td class="normal"><html:text name="cierreBancoSearchPageVO" property="cierreBanco.envioOsiris.idEnvioAfipView" size="15" maxlength="20" styleClass="datos" /></td>
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
		<logic:equal name="cierreBancoSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="cierreBancoSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Conciliar -->
							<th align="left"><bean:message bundle="bal" key="bal.cierreBanco.fechaCierre.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.cierreBanco.banco.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.cierreBanco.nroCierreBanco.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.cierreBanco.cantTransaccion.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.cierreBanco.importeTotalCalPorNotaImpto.label"/></th>
						</tr>
							
						<logic:iterate id="CierreBancoVO" name="cierreBancoSearchPageVO" property="listResult">
							<tr>
								<td>
									<!-- Conciliar -->
									<logic:equal name="cierreBancoSearchPageVO" property="conciliarEnabled" value="enabled">
										<logic:equal name="CierreBancoVO" property="conciliarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('conciliar', '<bean:write name="CierreBancoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.conciliar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/aceptar0.gif"/>
											</a>
										</logic:equal> 
										<logic:notEqual name="CierreBancoVO" property="conciliarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/aceptar1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="cierreBancoSearchPageVO" property="conciliarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/aceptar1.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="CierreBancoVO" property="fechaCierreView"/>&nbsp;</td>
								<td><bean:write name="CierreBancoVO" property="bancoView" />&nbsp;</td>
								<td><bean:write name="CierreBancoVO" property="nroCierreBancoView" />&nbsp;</td>
								<td><bean:write name="CierreBancoVO" property="cantTransaccionView" />&nbsp;</td>
								<td><bean:write name="CierreBancoVO" property="importeTotalView"/>&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="cierreBancoSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="cierreBancoSearchPageVO" property="listResult">
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
  	<input type="hidden" name="name"  value="<bean:write name='cierreBancoSearchPageVO' property='name'/>" id="name"/>
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
