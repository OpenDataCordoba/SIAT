<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarDeuJudSinConstancia.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="deuJudSinConstanciaSearchPageVO"/>
		<bean:define id="poseeParam" value="false" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
		
		<h1><bean:message bundle="gde" key="gde.deuJudSinConstanciaSearchPage.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p>
						<logic:equal name="deuJudSinConstanciaSearchPageVO" property="modoSeleccionar" value="true">
							<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
							<bean:message bundle="gde" key="gde.deuJudSinConstancia.label"/>
						</logic:equal>
						<logic:notEqual name="deuJudSinConstanciaSearchPageVO" property="modoSeleccionar" value="true">
							<bean:message bundle="gde" key="gde.deuJudSinConstanciaSearchPage.legend"/>
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
				<!-- Procurador -->
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.deuJudSinConstancia.procurador.label"/>: </label></td>
					<td class="normal">
						<logic:equal name="userSession" property="esProcurador" value="true" >
							<html:select name="deuJudSinConstanciaSearchPageVO" property="deuda.procurador.id" styleClass="select" disabled ="true">
								<html:optionsCollection name="deuJudSinConstanciaSearchPageVO" property="listProcurador" label="descripcion" value="id" />
							</html:select>
						</logic:equal>			
						<logic:notEqual name="userSession" property="esProcurador" value="true">
							<html:select name="deuJudSinConstanciaSearchPageVO" property="deuda.procurador.id" styleClass="select" disabled ="false">
								<html:optionsCollection name="deuJudSinConstanciaSearchPageVO" property="listProcurador" label="descripcion" value="id" />
							</html:select>
						</logic:notEqual>			
					</td>
				</tr>
				<!-- Recurso -->
				<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.deuJudSinConstancia.cuenta.recurso.label"/>: </label></td>
				<td class="normal">
					<html:select name="deuJudSinConstanciaSearchPageVO" property="deuda.cuenta.recurso.id" styleClass="select" styleId="cboRecurso" style="width:90%">
						<bean:define id="includeRecursoList" name="deuJudSinConstanciaSearchPageVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="deuJudSinConstanciaSearchPageVO" property="deuda.cuenta.recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
					
					<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
						<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
						src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
					</a>
										
				</td>
				<!-- Cuenta -->			
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.deuJudSinConstancia.cuenta.label"/>: </label></td>
					<td class="normal"><html:text name="deuJudSinConstanciaSearchPageVO" property="deuda.cuenta.numeroCuenta" size="20" maxlength="100" styleClass="datos" />
						<button type="button" onclick="submitForm('buscarCuenta', '');">
		    				<bean:message bundle="gde" key="gde.deuJudSinConstanciaSearchPage.button.buscarCuenta"/>
			      		</button>
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
			<logic:equal name="deuJudSinConstanciaSearchPageVO" property="viewResult" value="true">
				<logic:notEmpty  name="deuJudSinConstanciaSearchPageVO" property="listResult">	
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
		               	<tbody>
			               	<tr>
								<th width="1">&nbsp;</th> <!-- Ver -->
								<th width="1"><input type="checkbox" checked="checked" onclick="changeChk('filter', 'listIdSelected', this)"/></th> <!-- Check -->
								<th align="left"><bean:message bundle="gde" key="gde.deuJudSinConstancia.deuda.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.deuJudSinConstancia.importe.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.deuJudSinConstancia.saldo.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.deuJudSinConstancia.fechaVto.label"/></th>
							</tr>
								
							<logic:iterate id="DeudaVO" name="deuJudSinConstanciaSearchPageVO" property="listResult">
								<tr>
									<logic:notEqual name="deuJudSinConstanciaSearchPageVO" property="modoSeleccionar" value="true">
										<!-- Ver detalle Deuda -->
						  				<td>
					  						<logic:equal name="deuJudSinConstanciaSearchPageVO" property="verEnabled" value="enabled">
								  				<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verDetalleDeuda', '<bean:write name="DeudaVO" property="id" bundle="base" formatKey="general.format.id"/>-<bean:write name="DeudaVO" property="estadoDeuda.id" bundle="base" formatKey="general.format.id"/>');">
									    	    	<img title="Ver" src="<%=request.getContextPath()%>/images/iconos/selec0.gif" border="0"/>
								    	    	</a>
						  					</logic:equal>
							  				<logic:notEqual name="deuJudSinConstanciaSearchPageVO" property="verEnabled" value="enabled">
							  					<img title="Ver" src="<%=request.getContextPath()%>/images/iconos/selec1.gif" border="0"/>
							  				</logic:notEqual>
							  			</td>
										<!-- CheckBox -->
										<td>
											<logic:equal name="DeudaVO" property="esValidaParaConstancia" value="true">										
												<html:multibox name="deuJudSinConstanciaSearchPageVO" property="listIdSelected">
													<bean:write name="DeudaVO" property="idView"/>
												</html:multibox>
											</logic:equal>										
											<logic:notEqual name="DeudaVO" property="esValidaParaConstancia" value="true">										
												<input type="checkbox" disabled="disabled"/>
											</logic:notEqual>
										</td>	
							  			
									</logic:notEqual>
									<td><bean:write name="DeudaVO" property="periodoView"/>/<bean:write name="DeudaVO" property="anioView"/>&nbsp;</td>
									<td><bean:write name="DeudaVO" property="importeView" />&nbsp;</td>
									<td><bean:write name="DeudaVO" property="saldoView" />&nbsp;</td>
									<td><bean:write name="DeudaVO" property="fechaVencimientoView" />&nbsp;</td>
								</tr>
							</logic:iterate>
					
						</tbody>
					</table>
				</logic:notEmpty>
				
				<logic:empty name="deuJudSinConstanciaSearchPageVO" property="listResult">
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
				<logic:equal name="deuJudSinConstanciaSearchPageVO" property="viewResult" value="true">
					<td align="right">
	  	    			<logic:equal name="deuJudSinConstanciaSearchPageVO" property="modoSeleccionar" value="false">
							<bean:define id="agregarEnabled" name="deuJudSinConstanciaSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('incluirDeudaAConstancia', '0');" 
								value="<bean:message bundle="gde" key="gde.deuJudSinConstanciaSearchPage.button.incluirDeuda"/>"/>
						</logic:equal>
	  	    			<logic:equal name="deuJudSinConstanciaSearchPageVO" property="modoSeleccionar" value="true">
	  	    				<logic:equal name="deuJudSinConstanciaSearchPageVO" property="agregarEnSeleccion" value="true">
								<bean:define id="agregarEnabled" name="deuJudSinConstanciaSearchPageVO" property="agregarEnabled"/>
								<input type="button" <%=agregarEnabled%> class="boton" 
									onClick="submitForm('incluirDeudaAConstancia', '0');" 
									value="<bean:message bundle="gde" key="gde.deuJudSinConstanciaSearchPage.button.incluirDeuda"/>"/>
								</logic:equal>
						</logic:equal>
					</td>				
				</logic:equal>
			</tr>
		</table>	
	</span>

	<input type="text" style="display:none"/>		
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
