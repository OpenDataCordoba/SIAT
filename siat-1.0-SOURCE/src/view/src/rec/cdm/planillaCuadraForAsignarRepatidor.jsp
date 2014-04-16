<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rec/BuscarPlanillaCuadra.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="rec" key="rec.planillaCuadraForAsignarRepartidor.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="rec" key="rec.planillaCuadraForAsignarRepartidor.legend"/></p>
			</td>				
			<td align="right">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>


	<!-- Obra -->
	<fieldset>
		<legend><bean:message bundle="rec" key="rec.obra.title"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="planillaCuadraSearchPageVO" property="planillaCuadra.obra.recurso.desRecurso"/>
				</td>
			</tr>

			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.numeroObra.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="planillaCuadraSearchPageVO" property="planillaCuadra.obra.numeroObraView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.desObra.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="planillaCuadraSearchPageVO" property="planillaCuadra.obra.desObra"/></td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.permiteCamPlaMay.label"/>: </label></td>
				<td class="normal"><bean:write name="planillaCuadraSearchPageVO" property="planillaCuadra.obra.permiteCamPlaMay.value"/></td>				
			</tr>

			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.esPorValuacion.label"/>: </label></td>
				<td class="normal"><bean:write name="planillaCuadraSearchPageVO" property="planillaCuadra.obra.esPorValuacion.value"/></td>				
			</tr>

			<tr>	
				<td><label><bean:message bundle="rec" key="rec.obra.esCostoEsp.label"/>: </label></td>
				<td class="normal"><bean:write name="planillaCuadraSearchPageVO" property="planillaCuadra.obra.esCostoEsp.value"/></td>				
				<logic:equal name="planillaCuadraSearchPageVO" property="planillaCuadra.obra.costoEspEnabled" value="true">					
					<td><label><bean:message bundle="rec" key="rec.obra.costoEsp.label"/>: </label></td>
					<td class="normal"><bean:write name="planillaCuadraSearchPageVO" property="planillaCuadra.obra.costoEspView"/></td>
				</logic:equal>
			</tr>

			<!-- Inclucion de CasoView -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="planillaCuadraSearchPageVO" property="planillaCuadra.obra"/>
					<%@ include file="/cas/caso/includeCasoView.jsp" %>				
				</td>
			</tr>
			<!-- Fin Inclucion de CasoView -->

			<tr>
				<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td class="normal"><bean:write name="planillaCuadraSearchPageVO" property="planillaCuadra.obra.estadoObra.desEstadoObra"/></td>
			</tr>
		</table>
	</fieldset>	
	<!-- Obra -->
		
	<!-- Filtro -->
	<fieldset>
	<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
		<table class="tabladatos">
			<!-- Nro planilla -->			
			<tr>
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.numeroPlanilla.label"/>: </label></td>
				<td class="normal"><html:text name="planillaCuadraSearchPageVO" property="planillaCuadra.idView" size="15" maxlength="20" styleClass="datos" /></td>
			</tr>
			<!-- Descripcion -->			
			<tr>
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.descripcion.label"/>: </label></td>
				<td class="normal"><html:text name="planillaCuadraSearchPageVO" property="planillaCuadra.descripcion" size="40" maxlength="100" styleClass="datos" /></td>
			</tr>
			<!-- Calle Principal -->
			<tr>
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.callePpal.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="planillaCuadraSearchPageVO" property="planillaCuadra.callePpal.nombreCalle" size="20" maxlength="100" styleClass="datos" disabled="true"/>
					<html:button property="btnBuscarCallePpal"  styleClass="boton" onclick="submitForm('buscarCalleForAsignarRepartidor', '');">
						<bean:message bundle="rec" key="rec.planillaCuadraEditAdapter.button.buscarCalle"/>
					</html:button>
				</td>
			</tr>
		</table>

		<p align="center">
		  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiarForAsignarRepartidores', '');">
				<bean:message bundle="base" key="abm.button.limpiar"/>
			</html:button>
			&nbsp;
		  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscarForAsignarRepartidor', '');">
				<bean:message bundle="base" key="abm.button.buscar"/>
			</html:button>
		</p>
	</fieldset>	
	<!-- Fin Filtro -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="planillaCuadraSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="planillaCuadraSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<th align="left"><bean:message bundle="rec" key="rec.planillaCuadra.numeroPlanilla.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.repartidor.label"/></th>
							<th align="left"><bean:message bundle="rec" key="rec.tipoObra.label"/></th>
							<th align="left"><bean:message bundle="rec" key="rec.planillaCuadra.descripcion.label"/></th>							
							<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
						</tr>
							
						<logic:iterate id="PlanillaCuadraVO" name="planillaCuadraSearchPageVO" property="listResult" indexId="count">
							<tr>
								<!-- Seleccionar -->
								<td>
									<html:multibox name="planillaCuadraSearchPageVO" property="listId">
										<bean:write name="PlanillaCuadraVO" property="idView"/>
									</html:multibox>	
								</td>
								<td><bean:write name="PlanillaCuadraVO" property="idView"/>&nbsp;</td>
								<td><bean:write name="PlanillaCuadraVO" property="repartidor.desRepartidorView"/>&nbsp;</td>
								<td><bean:write name="PlanillaCuadraVO" property="tipoObra.desTipoObra" />&nbsp;</td>
								<td><bean:write name="PlanillaCuadraVO" property="descripcion" />&nbsp;</td>								
								<td><bean:write name="PlanillaCuadraVO" property="estPlaCua.desEstPlaCua" />&nbsp;</td>
							</tr>
						</logic:iterate>
					</tbody>
				</table>

				<!-- Lista de repartidores a asignar -->
				<fieldset>
					<legend><bean:message bundle="pad" key="pad.repartidor.title"/></legend>
					<table class="tabladatos">
						<tr>	
							<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.repartidor.label"/>: </label></td>
							<td class="normal">
								<html:select name="planillaCuadraSearchPageVO" property="repartidor.id" styleClass="select">
									<html:optionsCollection name="planillaCuadraSearchPageVO" property="listRepartidor" label="desRepartidorView" value="id" />
								</html:select>
							</td>					
						</tr>
					</table>
				</fieldset>	
				
			</logic:notEmpty>
			
			<logic:empty name="planillaCuadraSearchPageVO" property="listResult">
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
			<logic:equal name="planillaCuadraSearchPageVO" property="viewResult" value="true">
				<td align="right">
					<input type="button" class="boton" onClick="submitForm('asignarRepartidor', '0');" 
						value="<bean:message bundle="rec" key="rec.planillaCuadraForAsignarRepartidor.button.asignarRepartidor"/>"/>
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

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->