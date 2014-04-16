<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarProPreDeuDet.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="gde" key="gde.proPreDeuDetSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="proPreDeuDetSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="gde" key="gde.proPreDeuDet.label"/>
					</logic:equal>
					<logic:notEqual name="proPreDeuDetSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="gde" key="gde.proPreDeuDetSearchPage.legend"/>
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
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.proPreDeuDet.cuenta.label"/>: </label></td>
				<td class="normal"><html:text name="proPreDeuDetSearchPageVO" property="proPreDeuDet.cuenta.numeroCuenta" size="10" maxlength="10" styleClass="datos" /></td>
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
		<logic:equal name="proPreDeuDetSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="proPreDeuDetSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Seleccionar -->
							<th align="left"><bean:message bundle="gde" key="gde.proPreDeuDet.deuda.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.proPreDeuDet.via.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.proPreDeuDet.deuda.importe.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.proPreDeuDet.deuda.saldo.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.proPreDeuDet.observacion.label"/></th>
						</tr>
							
						<logic:iterate id="ProPreDeuDetVO" name="proPreDeuDetSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<td>
									<html:multibox name="proPreDeuDetSearchPageVO" property="listId">
										<bean:write name="ProPreDeuDetVO" property="idView"/>
									</html:multibox>	
								</td>
								<td>
									<bean:write name="ProPreDeuDetVO" property="deuda.periodoView"/>/<bean:write name="ProPreDeuDetVO" property="deuda.anioView"/>&nbsp;
								</td>
								<td><bean:write name="ProPreDeuDetVO" property="viaDeuda.desViaDeuda"/>&nbsp;</td>
								<td><bean:write name="ProPreDeuDetVO" property="deuda.importeView"/>&nbsp;</td>
								<td><bean:write name="ProPreDeuDetVO" property="deuda.saldoView"/>&nbsp;</td>
								<td><bean:write name="ProPreDeuDetVO" property="observacion"/>&nbsp;</td>
							</tr>
						</logic:iterate>
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="proPreDeuDetSearchPageVO" property="listResult">
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
<!-- proPreDeuDetSearchPage.jsp -->