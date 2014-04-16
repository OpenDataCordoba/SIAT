<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarCobranza.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>


	<h1><bean:message bundle="gde" key="gde.cobranzaSearchPage.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<bean:message bundle="gde" key="gde.cobranzaSearchPage.legend"/>
				</p>
			</td>				
			<td align="right">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<fieldset>
	<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="pad" key="pad.contribuyente.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="cobranzaSearchPageVO" property="contribuyente.persona.represent" disabled="true" size="20" />
					<html:button property="btnBuscarCuenta" styleClass="boton" onclick="submitForm('buscarPersona', '');">
						<bean:message bundle="base" key="abm.button.buscar"/>
					</html:button>			
				</td>				
			</tr>
			<tr>
	      		<td><label><bean:message bundle="gde" key="gde.cobranzaSearchPage.ordenControl.numero.label"/>: </label></td>
	      		<td class="normal" colspan="3">
	      			<html:text name="cobranzaSearchPageVO" property="cobranza.ordenControl.numeroOrdenView" size="8"  styleClass="datos"/> /
	      			<html:text name="cobranzaSearchPageVO" property="cobranza.ordenControl.anioOrdenView" size="8"/>&nbsp;
	      			<bean:message bundle="gde" key="gde.cobranzaSearchPage.ordenControl.legend"/>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.cobranzaSearchPage.tipoOrden.label"/>: </label></td>
				<td class="normal">
					<html:select name="cobranzaSearchPageVO" property="cobranza.ordenControl.tipoOrden.id">
						<html:optionsCollection name="cobranzaSearchPageVO" property="listTipoOrden" label="desTipoOrden" value="id"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<!-- Inclucion de CasoView -->
				<td colspan="3">
					<bean:define id="IncludedVO" name="cobranzaSearchPageVO" property="cobranza.ordenControl"/>
					<bean:define id="voName" value="cobranza.ordenControl" />
					<%@ include file="/cas/caso/includeCaso.jsp" %>				
				</td>
			</tr>
			<tr>
	      		<td><label><bean:message bundle="gde" key="gde.cobranzaSearchPage.asignada.label"/>: </label></td>
	      		<td class="normal">
	      			<html:select name="cobranzaSearchPageVO" property="cobranza.perCob.id">
	      				 <html:optionsCollection name="cobranzaSearchPageVO" property="listPerCob" label="nombreApellido" value="id"/>
	      			</html:select>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.cobranzaSearchPage.estadoCobranza.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="cobranzaSearchPageVO" property="cobranza.estadoCobranza.id">
						<html:optionsCollection name="cobranzaSearchPageVO" property="listEstadoCobranza" label="desEstado" value="id"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.cobranzaSearchPage.proFecConDes.label"/>: </label></td>
				<td class="normal">
					<html:text name="cobranzaSearchPageVO" property="proConDesView" styleId="proConDesView" size="10"></html:text>
					<a class="link_siat" onclick="return show_calendar('proConDesView');" id="a_proConDesView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				<td><label><bean:message bundle="gde" key="gde.cobranzaSearchPage.proFecConHas.label"/>: </label></td>
				<td class="normal">
					<html:text name="cobranzaSearchPageVO" property="proConHasView" styleId="proConHasView" size="10"></html:text>
					<a class="link_siat" onclick="return show_calendar('proConHasView');" id="a_proConHasView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				
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
		<logic:equal name="cobranzaSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="cobranzaSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver-->
							<th width="1">&nbsp;</th> <!-- Modificar-->
							<th width="1">&nbsp;</th> <!-- Asignar-->
							<th align="left"><bean:message bundle="pad" key="pad.contribuyente.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.cobranza.fechaInicio.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.cobranzaSearchPage.ordenControl.numero.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.cobranza.proFecCon.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.cobranza.fechaFin.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.cobranza.estadoCobranza.label"/></th>
						</tr>
							
						<logic:iterate id="CobranzaVO" name="cobranzaSearchPageVO" property="listResult">
							<tr>
								<!-- Ver -->
								<td>
									<logic:equal name="cobranzaSearchPageVO" property="verEnabled" value="enabled">									
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="CobranzaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="cobranzaSearchPageVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</td>
								<!-- Modificar -->
								<td>
									<logic:equal name="cobranzaSearchPageVO" property="modificarEnabled" value="enabled">
										<logic:equal name="CobranzaVO" property="modificarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="CobranzaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="CobranzaVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="cobranzaSearchPageVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</td>
								<!-- Eliminar -->
								<td>
									<logic:equal name="cobranzaSearchPageVO" property="modificarEnabled" value="enabled">
										<logic:equal name="CobranzaVO" property="eliminarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('asignar', '<bean:write name="CobranzaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.asignar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/admOrdCon0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="CobranzaVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/admOrdCon0.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="cobranzaSearchPageVO" property="modificarEnabled" value="enabled">										
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/admOrdCon0.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="CobranzaVO" property="contribuyente.persona.view"/>&nbsp;</td>
								<td><bean:write name="CobranzaVO" property="fechaInicioView"/>&nbsp;</td>
								<td><bean:write name="CobranzaVO" property="ordenControl.ordenControlyTipoView"/>&nbsp;</td>
								<td><bean:write name="CobranzaVO" property="proFecConView"/>&nbsp;</td>
								<td><bean:write name="CobranzaVO" property="fechaFinView"/>&nbsp;</td>
								<td><bean:write name="CobranzaVO" property="estadoCobranza.desEstado"/>&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="cobranzaSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="cobranzaSearchPageVO" property="listResult">
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
