<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarRescate.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				

	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="rescateAdapterVO"/>
		<bean:define id="poseeParam" value="true" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
		
		<h1><bean:message bundle="gde" key="gde.rescateAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>
				<td align="right"><html:button property="btnVolver"
					styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver" />
				</html:button></td>
			</tr>
		</table>
		<!-- Rescate -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.rescate.title"/></legend>
			
			<logic:equal name="rescateAdapterVO" property="act" value="agregar">
			<!-- AGREGACION -->
				<table class="tabladatos">
					<!-- combo Recurso -->
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:select name="rescateAdapterVO" property="rescate.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');" styleId="cboRecurso" style="width:90%">
								<bean:define id="includeRecursoList"       name="rescateAdapterVO" property="listRecurso"/>
								<bean:define id="includeIdRecursoSelected" name="rescateAdapterVO" property="rescate.recurso.id"/>
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
							<html:select name="rescateAdapterVO" property="rescate.plan.id" styleClass="select">
								<html:optionsCollection name="rescateAdapterVO" 
									property="listPlan" label="desPlan" value="id" />
							</html:select>
						</td>
					</tr>
					<!-- fechaDesde y fechaHasta -->
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.rescate.fechaRescateDesde"/>: </label></td>
						<td class="normal">
							<html:text name="rescateAdapterVO" property="rescate.fechaRescateView" styleId="fechaRescateView" size="15" maxlength="10" styleClass="datos"/>
							<a class="link_siat" onclick="return show_calendar('fechaRescateView');" id="a_fechaRescateView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.rescate.fechaRescateHasta"/>: </label></td>
						<td class="normal">
							<html:text name="rescateAdapterVO" property="rescate.fechaVigRescateView" styleId="fechaVigRescateView" size="15" maxlength="10" styleClass="datos"/>
							<a class="link_siat" onclick="return show_calendar('fechaVigRescateView');" id="a_fechaVigRescateView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
					</tr>
					<!-- Caso -->
					<tr>
						<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
						<td colspan="3">
							<bean:define id="IncludedVO" name="rescateAdapterVO" property="rescate"/>
							<bean:define id="voName" value="rescate" />
							<%@ include file="/cas/caso/includeCaso.jsp" %>
						</td>
					</tr>
					<!-- Observacion -->				
					<tr>
						<td><label><bean:message bundle="gde" key="gde.rescate.observacion"/>: </label></td>
						<td class="normal" colspan="3">	
							<html:textarea name="rescateAdapterVO" property="rescate.observacion" cols="80" rows="15"/>
						</td>
					</tr>
				</table>
			</logic:equal>
			<logic:notEqual name="rescateAdapterVO" property="act" value="agregar">
			<!-- MODIFICACION o VER -->		
				<table class="tabladatos">
					<!-- combo Recurso -->
					<tr>
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal" colspan="3"><bean:write name="rescateAdapterVO" property="rescate.recurso.desRecurso" /> </td>
					</tr>
					<!-- combo Plan -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.plan.label"/>: </label></td>				
						<td class="normal" colspan="3"><bean:write name="rescateAdapterVO" property="rescate.plan.desPlan" /> </td>
					</tr>
					<!-- fechaDesde y fechaHasta -->
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.rescate.fechaRescateDesde"/>: </label></td>
						<logic:equal name="rescateAdapterVO" property="act" value="modificar">
							<td class="normal">
								<html:text name="rescateAdapterVO" property="rescate.fechaRescateView" styleId="fechaRescateView" size="15" maxlength="10" styleClass="datos"/>
								<a class="link_siat" onclick="return show_calendar('fechaRescateView');" id="a_fechaRescateView">
									<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
							</td>
						</logic:equal>
						<logic:notEqual name="rescateAdapterVO" property="act" value="modificar">
							<td class="normal">
								<bean:write name="rescateAdapterVO" property="rescate.fechaRescateView"/>
							</td>
						</logic:notEqual>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.rescate.fechaRescateHasta"/>: </label></td>
						<logic:equal name="rescateAdapterVO" property="act" value="modificar">
							<td class="normal">
								<html:text name="rescateAdapterVO" property="rescate.fechaVigRescateView" styleId="fechaVigRescateView" size="15" maxlength="10" styleClass="datos"/>
								<a class="link_siat" onclick="return show_calendar('fechaVigRescateView');" id="a_fechaVigRescateView">
									<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
							</td>
						</logic:equal>
						<logic:notEqual name="rescateAdapterVO" property="act" value="modificar">
							<td class="normal">
								<bean:write name="rescateAdapterVO" property="rescate.fechaVigRescateView"/>
							</td>
						</logic:notEqual>
					</tr>
					<!-- Caso -->
					<tr>
						<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
						<logic:equal name="rescateAdapterVO" property="act" value="modificar">
							<td colspan="3">
								<bean:define id="IncludedVO" name="rescateAdapterVO" property="rescate"/>
								<bean:define id="voName" value="rescate" />
								<%@ include file="/cas/caso/includeCaso.jsp" %>
							</td>
						</logic:equal>
						<logic:notEqual name="rescateAdapterVO" property="act" value="modificar">
							<!-- Inclucion de CasoView -->
							<td colspan="3">
								<bean:define id="IncludedVO" name="rescateAdapterVO" property="rescate"/>
								<%@ include file="/cas/caso/includeCasoView.jsp" %>				
							</td>
						</logic:notEqual>
					</tr>		
					<!-- Observacion -->				
					<tr>
						<td><label><bean:message bundle="gde" key="gde.rescate.observacion"/>: </label></td>
						<td class="normal" colspan="3">	
							<logic:equal name="rescateAdapterVO" property="act" value="modificar">
								<html:textarea name="rescateAdapterVO" property="rescate.observacion" cols="80" rows="15"/>
							</logic:equal>
							<logic:notEqual name="rescateAdapterVO" property="act" value="modificar">
								<bean:write name="rescateAdapterVO" property="rescate.observacion"/>
							</logic:notEqual>
						</td>
					</tr>
				</table>
			</logic:notEqual>
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
					<logic:equal name="rescateAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="rescateAdapterVO" property="act" value="agregar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
							<bean:message bundle="base" key="abm.button.agregar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="rescateAdapterVO" property="act" value="eliminar">
						<html:button property="btnAceptar" styleClass="boton" onclick="submitForm('eliminar','');">
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