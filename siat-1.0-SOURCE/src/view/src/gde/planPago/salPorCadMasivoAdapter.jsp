<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarSalPorCadMasivo.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="salPorCadMasivoAdapterVO"/>
		<bean:define id="poseeParam" value="true" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
		
		<h1><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.title"/></h1>
			
		<table class="tablabotones" width="100%">
			<tr>
				<td align="right"><html:button property="btnVolver"
					styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver" />
				</html:button></td>
			</tr>
		</table>
		<!-- Saldo Por Caducidad -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.add.legend"/></legend>
			
			<logic:equal name="salPorCadMasivoAdapterVO" property="act" value="agregar">
			
				<table class="tabladatos">
					<!-- combo Recurso -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.salPorCad.fechaSalCad"/></td>
						<td class="normal"><bean:write name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.fechaSaldoView"/></td>
					</tr>
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:select name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');" styleId="cboRecurso" style="width:90%">
								<bean:define id="includeRecursoList"       name="salPorCadMasivoAdapterVO" property="listRecurso"/>
								<bean:define id="includeIdRecursoSelected" name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.recurso.id"/>
								<%@ include file="/def/gravamen/includeRecurso.jsp" %>
							</html:select>
							
							<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
								<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
								src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
							</a>
							
						</td>
					</tr>
					<!-- combo Plan -->
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.plan.label"/>: </label></td>				
						<td class="normal" colspan="3">
							<html:select name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.plan.id" styleClass="select">
								<html:optionsCollection name="salPorCadMasivoAdapterVO" 
									property="listPlan" label="desPlan" value="id" />
							</html:select>
						</td>
					</tr>
					<!-- fechaDesde y fechaHasta -->
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.fechaForDesde"/>: </label></td>
						<td class="normal">
							<html:text name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.fechaFormDesdeSaldoView" styleId="fechaFormDesdeSaldoView" size="15" maxlength="10" styleClass="datos"/>
							<a class="link_siat" onclick="return show_calendar('fechaFormDesdeSaldoView');" id="a_fechaFormDesdeSaldoView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.fechaForHasta"/>: </label></td>
						<td class="normal">
							<html:text name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.fechaFormHastaSaldoView" styleId="fechaFormHastaSaldoView" size="15" maxlength="10" styleClass="datos"/>
							<a class="link_siat" onclick="return show_calendar('fechaFormHastaSaldoView');" id="a_fechaFormHastaSaldoView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
					</tr>
					<!-- Cuota Superior A -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.montoSuperior"/>: $</td>
						<td class="normal"><html:text name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.cuotaSuperiorAView"/></td>
					</tr>
					<!-- Caso -->
					<tr>
						<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
						<td colspan="3">
							<bean:define id="IncludedVO" name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad"/>
							<bean:define id="voName" value="saldoPorCaducidad" />
							<%@ include file="/cas/caso/includeCaso.jsp" %>
						</td>
					</tr>
					<!-- Observacion -->				
					<tr>
						<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.observacion"/>: </label></td>
						<td class="normal" colspan="3">	
							<html:textarea name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.observacion" cols="80" rows="15"/>
						</td>
					</tr>
				</table>
			</logic:equal>
			<logic:equal name="salPorCadMasivoAdapterVO" property="act" value="modificar">
			<!-- MODIFICACION -->		
				<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.salPorCad.fechaSalCad"/></td>
						<td class="normal"><bean:write name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.fechaSaldoView"/></td>
					</tr>
					<!-- combo Recurso -->
					<tr>
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal" colspan="3"><bean:write name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.recurso.desRecurso" /> </td>
					</tr>
					<!-- combo Plan -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.plan.label"/>: </label></td>				
						<td class="normal" colspan="3"><bean:write name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.plan.desPlan" /> </td>
					</tr>
					<!-- fechaDesde y fechaHasta -->
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.fechaForDesde"/>: </label></td>
						<td class="normal">
							<html:text name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.fechaFormDesdeSaldoView" styleId="fechaFormDesdeSaldoView" size="15" maxlength="10" styleClass="datos"/>
							<a class="link_siat" onclick="return show_calendar('fechaFormDesdeSaldoView');" id="a_fechaFormDesdeSaldoView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.fechaForHasta"/>: </label></td>
						<td class="normal">
							<html:text name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.fechaFormHastaSaldoView" styleId="fechaFormHastaSaldoView" size="15" maxlength="10" styleClass="datos"/>
							<a class="link_siat" onclick="return show_calendar('fechaFormHastaSaldoView');" id="a_fechaFormHastaSaldoView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
					</tr>
					<!-- Cuota Superior A -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.montoSuperior"/></td>
						<td class="normal"><html:text name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.cuotaSuperiorAView"/></td>
					</tr>
					<!-- Caso -->
					<tr>
						<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
						<!-- Inclucion de CasoView -->
						<td colspan="3">
							<bean:define id="IncludedVO" name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad"/>
							<bean:define id="voName" value="saldoPorCaducidad" />
							<%@ include file="/cas/caso/includeCaso.jsp" %>		
						</td>
					</tr>
					<!-- Observacion -->				
					<tr>
						<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.observacion"/>: </label></td>
						<td class="normal" colspan="3">	
							<html:textarea name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.observacion" cols="80" rows="15"/>
						</td>
					</tr>
				</table>
			</logic:equal>
			
			<logic:equal name="salPorCadMasivoAdapterVO" property="act" value="ver">
			<!-- VER -->		
				<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.salPorCad.fechaSalCad"/></td>
						<td class="normal"><bean:write name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.fechaSaldoView"/></td>
					</tr>
					<!-- combo Recurso -->
					<tr>
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal" colspan="3"><bean:write name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.recurso.desRecurso" /> </td>
					</tr>
					<!-- combo Plan -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.plan.label"/>: </label></td>				
						<td class="normal" colspan="3"><bean:write name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.plan.desPlan" /> </td>
					</tr>
					<!-- fechaDesde y fechaHasta -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.fechaForDesde"/>: </label></td>
						<td class="normal">
							<bean:write name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.fechaFormDesdeSaldoView"/>
						</td>
						<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.fechaForHasta"/>: </label></td>
						<td class="normal">
							<bean:write name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.fechaFormHastaSaldoView"/>
						</td>
					</tr>
					<!-- Cuota Superior A -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.montoSuperior"/></td>
						<td class="normal"><bean:write name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.cuotaSuperiorAView"/></td>
					</tr>
					<!-- Caso -->
					<tr>
						<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
						<!-- Inclucion de CasoView -->
						<td colspan="3">
							<bean:define id="IncludedVO" name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad"/>
							<%@ include file="/cas/caso/includeCasoView.jsp" %>				
						</td>
					</tr>
					<!-- Observacion -->				
					<tr>
						<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.observacion"/>: </label></td>
						<td class="normal" colspan="3">	
							<bean:write name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.observacion"/>
						</td>
					</tr>
				</table>
			</logic:equal>
			<logic:equal name="salPorCadMasivoAdapterVO" property="act" value="eliminar">
			<!-- MODIFICACION -->		
				<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.salPorCad.fechaSalCad"/></td>
						<td class="normal"><bean:write name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.fechaSaldoView"/></td>
					</tr>
					<!-- combo Recurso -->
					<tr>
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal" colspan="3"><bean:write name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.recurso.desRecurso" /> </td>
					</tr>
					<!-- combo Plan -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.plan.label"/>: </label></td>				
						<td class="normal" colspan="3"><bean:write name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.plan.desPlan" /> </td>
					</tr>
					<!-- fechaDesde y fechaHasta -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.fechaForDesde"/>: </label></td>
						<td class="normal">
							<bean:write name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.fechaFormDesdeSaldoView"/>
						</td>
						<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.fechaForHasta"/>: </label></td>
						<td class="normal">
							<bean:write name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.fechaFormHastaSaldoView"/>
						</td>
					</tr>
					<!-- Cuota Superior A -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.montoSuperior"/></td>
						<td class="normal"><bean:write name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.cuotaSuperiorAView"/></td>
					</tr>
					<!-- Caso -->
					<tr>
						<!-- Inclucion de CasoView -->
						<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
						<td colspan="3">
							<bean:define id="IncludedVO" name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad"/>
							<%@ include file="/cas/caso/includeCasoView.jsp" %>				
						</td>
					</tr>
					<!-- Observacion -->				
					<tr>
						<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.observacion"/>: </label></td>
						<td class="normal" colspan="3">	
							<bean:write name="salPorCadMasivoAdapterVO" property="saldoPorCaducidad.observacion"/>
						</td>
					</tr>
				</table>
			</logic:equal>
		</fieldset>	
		<!-- Rescate -->
		
		<table class="tablabotones" width="100%" >
		   	<tr>
	  	   		<td align="left" width="50%">
		   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
		   	    		<bean:message bundle="base" key="abm.button.volver"/>
		   	    	</html:button>
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="salPorCadMasivoAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="salPorCadMasivoAdapterVO" property="act" value="agregar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
							<bean:message bundle="base" key="abm.button.agregar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="salPorCadMasivoAdapterVO" property="act" value="eliminar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
	   	    	</td>   	    	
	   	    </tr>
	   	</table>
	
	</span>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->