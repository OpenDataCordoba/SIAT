<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarDeudaExcProMasEliminar.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="gde" key="gde.deudaExcProMasEliminarSearchPage.title"/></h1>	
		
	<p><bean:message bundle="gde" key="gde.deudaExcProMasEliminarSearchPage.legend"/></p>
		
	<!-- Parametros de la Deuda -->
    <fieldset>
    	<legend><bean:message bundle="gde" key="gde.deudaExcProMasEliminarSearchPage.paramDeuda.title"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="deudaExcProMasEliminarSearchPageVO" property="procesoMasivo.recurso.desRecurso" />	</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="deudaExcProMasEliminarSearchPageVO" property="cuenta.numeroCuenta" size="10" maxlength="10" styleClass="datos" />
				</td>
			</tr>
			<logic:iterate id="item" name="deudaExcProMasEliminarSearchPageVO" property="procesoMasivo.recurso.listRecClaDeu">
				<tr>					
					<td>
						<logic:notPresent name="verLabelRecClaDeu">
							<label><bean:message bundle="def" key="def.recClaDeu.label"/>: </label>
						</logic:notPresent>
					</td>
					<bean:define id="verLabelRecClaDeu" value="false"/>
					<td class="normal" colspan="3"> 								
						<html:multibox name="deudaExcProMasEliminarSearchPageVO" property="listIdRecClaDeu">
					   		<bean:write name="item" property="id" bundle="base" formatKey="general.format.id"/> 
					  	</html:multibox> 
					   <bean:write name="item" property="desClaDeu"/> 
					</td> 
				</tr>
			</logic:iterate>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.deudaExcProMasEliminarSearchPage.fechaVencimientoDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="deudaExcProMasEliminarSearchPageVO" property="fechaVencimientoDesdeView" styleId="fechaVencimientoDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaVencimientoDesdeView');" id="a_fechaVencimientoDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				<td><label><bean:message bundle="gde" key="gde.deudaExcProMasEliminarSearchPage.fechaVencimientoHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="deudaExcProMasEliminarSearchPageVO" property="fechaVencimientoHastaView" styleId="fechaVencimientoHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaVencimientoHastaView');" id="a_fechaVencimientoHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
		</table>
	</fieldset>
	<!-- Fin Parametros de la Deuda -->
	
	<p align="center">
	  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
			<bean:message bundle="base" key="abm.button.limpiar"/>
		</html:button>
		&nbsp;
	  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
			<bean:message bundle="base" key="abm.button.buscar"/>
		</html:button>
	</p>

	<!-- Fin Filtro -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="deudaExcProMasEliminarSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="deudaExcProMasEliminarSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver -->
							<th align="left"><bean:message bundle="gde" key="gde.deudaExcProMasEliminarSearchPage.fileName.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.deudaExcProMasEliminarSearchPage.ctdResultado.label"/></th>
						</tr>
						<logic:iterate id="PlanillaVO" name="deudaExcProMasEliminarSearchPageVO" property="listResult">
							<tr>
								<!-- Ver -->
								<td>	
									<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('verPlanilla', '<bean:write name="PlanillaVO" property="fileName" />');">
										<img title="<bean:message bundle="gde" key="gde.deudaExcProMasEliminarSearchPage.button.verPlanilla"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</td>
								<td><bean:write name="PlanillaVO" property="fileNameView"/> &nbsp;</td>
								<td><bean:write name="PlanillaVO" property="ctdResultadosView" /> &nbsp;</td>
							</tr>
						</logic:iterate>
						
						<!-- Ctd Total de resultados -->
						<tr>
							<td> &nbsp;	 
							</td>
							<td>Cantidad total de registros del resultado</td>
							<td><bean:write name="deudaExcProMasEliminarSearchPageVO" property="ctdTotalResultadosView" /></td>
						</tr>
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="deudaExcProMasEliminarSearchPageVO" property="listResult">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
						<tr>
							<td align="center">
								<bean:message bundle="base" key="base.resultadoVacio"/>
							</td>
						</tr>
					</tbody>			
				</table>
			</logic:empty>
		</logic:equal>
	</div>
	<!-- Fin Resultado -->

	<table class="tablabotones">
    	<tr>
    		<td align="left">
	   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>	   	    			
   	    	</td>
   	    	<logic:equal name="deudaExcProMasEliminarSearchPageVO" property="viewResult" value="true">
				<logic:notEmpty  name="deudaExcProMasEliminarSearchPageVO" property="listResult">	
					<logic:greaterThan name="deudaExcProMasEliminarSearchPageVO" property="ctdTotalResultadosView" value="0">
						<td align="right">
							<html:button property="btnEliminarTodos"  styleClass="boton" onclick="submitForm('eliminarTodaDeudaProMasicialSeleccionada', '');">
								<bean:message bundle="gde" key="gde.deudaExcProMasEliminarSearchPage.eliminarTodos.label"/>
							</html:button>
						</td>
						<td align="right">
							<html:button property="btnSeleccionIndividual"  styleClass="boton" onclick="submitForm('eliminarSeleccionIndividual', '');">
								<bean:message bundle="gde" key="gde.deudaExcProMasEliminarSearchPage.seleccionIndividual.label"/>
							</html:button>
						</td>
					</logic:greaterThan>
					<logic:equal name="deudaExcProMasEliminarSearchPageVO" property="ctdTotalResultadosView" value="0">
						<td align="right">
							<html:button property="btnEliminarTodos"  styleClass="boton" disabled="true">
								<bean:message bundle="gde" key="gde.deudaExcProMasEliminarSearchPage.eliminarTodos.label"/>
							</html:button>
						</td>
						<td align="right">
							<html:button property="btnSeleccionIndividual"  styleClass="boton" disabled="true">
								<bean:message bundle="gde" key="gde.deudaExcProMasEliminarSearchPage.seleccionIndividual.label"/>
							</html:button>
						</td>
					</logic:equal>
					
				</logic:notEmpty>
			</logic:equal>
		</tr>
   	 </table>
		
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="fileParam" value=""/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Exclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>

</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
