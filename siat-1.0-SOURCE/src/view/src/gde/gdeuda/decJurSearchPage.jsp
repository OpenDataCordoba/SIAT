<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarDecJur.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>

	<!-- Si viene de la liquidacion de deuda -->
	<logic:equal name="decJurSearchPageVO" property="modoVer" value="true">

		<h1><bean:message bundle="gde" key="gde.decJurSearchPage.verDecJur.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p>
						<bean:message bundle="gde" key="gde.decJurSearchPage.verDecJur.legend"/>
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
		<legend><bean:message bundle="gde" key="gde.decJurSearchPage.verDecJur.datosPeriodo.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal">
						<bean:write name="decJurSearchPageVO" property="decJur.recurso.desRecurso"/>
					</td>
				</tr>
				<tr>
		      		<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
		      		<td class="normal">
		      			<bean:write name="decJurSearchPageVO" property="decJur.cuenta.numeroCuenta"/>
					</td>
				</tr>
				
				<tr>
		      		<td>
		      			<label>
		      				<bean:message bundle="gde" key="gde.decJur.periodo.label"/>/
		      				<bean:message bundle="gde" key="gde.decJur.anio.label"/>: 
		      			</label>
		      		</td>
		      		<td class="normal">
		      			<bean:write name="decJurSearchPageVO" property="decJur.desPeriodo"/>
					</td>
				</tr>
				
			</table>

		</fieldset>
	</logic:equal>
	
	
	<!-- Filtro -->
	<logic:notEqual name="decJurSearchPageVO" property="modoVer" value="true">
		<h1><bean:message bundle="gde" key="gde.decJurSearchPage.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p>
						<bean:message bundle="gde" key="gde.decJurSearchPage.legend"/>
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
					<td><label>(*)<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal">
						<html:select name="decJurSearchPageVO" property="decJur.recurso.id" styleClass="select" >
							<bean:define id="includeRecursoList" name="decJurSearchPageVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="decJurSearchPageVO" property="decJur.recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
					</td>
				</tr>
				<tr>
		      		<td><label>(*)<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
		      		<td class="normal">
		      			<html:text name="decJurSearchPageVO" property="decJur.cuenta.numeroCuenta" size="15" maxlength="20" styleClass="datos"/>
					</td>
				</tr>
				<tr>
		      		<td><label><bean:message bundle="gde" key="gde.decJurSearchPage.fechaDesde.label"/>: </label></td>
		      		<td class="normal">
		      			<html:text name="decJurSearchPageVO" property="periodoDesde" size="3" maxlength="2" styleClass="datos"/>
		      				 / <html:text name="decJurSearchPageVO" property="anioDesde" size="8" maxlength="4" styleClass="datos"/>
		      				 <bean:message bundle="gde" key="gde.decJurSearchPage.mascaraPeriodo"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurSearchPage.fechaHasta.label"/>: </label></td>
		      		<td class="normal">
		      			<html:text name="decJurSearchPageVO" property="periodoHasta" size="3" maxlength="2" styleClass="datos"/>
		      				 / <html:text name="decJurSearchPageVO" property="anioHasta" size="8" maxlength="4" styleClass="datos"/>
		      				 <bean:message bundle="gde" key="gde.decJurSearchPage.mascaraPeriodo"/>
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
	</logic:notEqual>	
	<!-- Fin Filtro -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="decJurSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="decJurSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver-->
							<th width="1">&nbsp;</th> <!-- Modificar-->
							<th width="1">&nbsp;</th> <!-- Eliminar-->
							<th width="1">&nbsp;</th> <!-- vueltaAtras-->
							<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.decJur.periodo.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.decJur.fechaPresentacion.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.decJur.fechaNovedad.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.decJur.tipoDecJur.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.decJur.totalDeclarado.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.decJur.estado.label"/></th>
						</tr>
							
						<logic:iterate id="DecJurVO" name="decJurSearchPageVO" property="listResult">
							<tr>
								<!-- Ver -->
								<td>
									<logic:equal name="decJurSearchPageVO" property="verEnabled" value="enabled">									
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="DecJurVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="decJurSearchPageVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</td>
								<!-- Modificar -->
								<td>
									<logic:equal name="decJurSearchPageVO" property="modificarEnabled" value="enabled">
										<logic:equal name="DecJurVO" property="modificarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="DecJurVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="DecJurVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="decJurSearchPageVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</td>
								<!-- Eliminar -->
								<td>
									<logic:equal name="decJurSearchPageVO" property="eliminarEnabled" value="enabled">
										<logic:equal name="DecJurVO" property="eliminarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="DecJurVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="DecJurVO" property="eliminarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="decJurSearchPageVO" property="eliminarEnabled" value="enabled">										
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</td>
								<!-- Vuelta Atras -->
								<td>
									<logic:equal name="decJurSearchPageVO" property="vueltaAtrasEnabled" value="enabled">
										<logic:equal name="DecJurVO" property="vueltaAtrasEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('vueltaAtras', '<bean:write name="DecJurVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="gde" key="gde.decJurSearchPage.abm.button.vueltaAtras"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/vueltaAtras0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="DecJurVO" property="vueltaAtrasEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/vueltaAtras1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="decJurSearchPageVO" property="vueltaAtrasEnabled" value="enabled">										
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/vueltaAtras1.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="DecJurVO" property="cuenta.numeroCuenta"/>&nbsp;</td>
								<td><bean:write name="DecJurVO" property="desPeriodo"/>&nbsp;</td>
								<td><bean:write name="DecJurVO" property="fechaPresentacionView"/>&nbsp;</td>
								<td><bean:write name="DecJurVO" property="fechaNovedadView"/>&nbsp;</td>
								<td><bean:write name="DecJurVO" property="tipDecJurRec.tipDecJur.desTipo"/>&nbsp;</td>
								<td><bean:write name="DecJurVO" property="totalDeclaradoView"/>&nbsp;</td>
								<td><bean:write name="DecJurVO" property="desEstado"/>&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="decJurSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="decJurSearchPageVO" property="listResult">
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
				<logic:equal name="decJurSearchPageVO" property="viewResult" value="true">
					<html:button property="btnAdd"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>
				</logic:equal>
			</td>			
		</tr>
	</table>
		
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="idCuenta" value="<bean:write name="decJurSearchPageVO" property="decJur.cuenta.id" bundle="base" formatKey="general.format.id"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
