<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarDeudaIncProMasEliminar.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="gde" key="gde.deudaIncProMasEliminarSelecIndSearchPage.title"/></h1>	
		
	<p><bean:message bundle="gde" key="gde.deudaIncProMasEliminarSelecIndSearchPage.legend"/></p>
		
	<!-- Parametros de la Deuda -->
    <fieldset>
    	<legend><bean:message bundle="gde" key="gde.deudaIncProMasEliminarSelecIndSearchPage.paramDeuda.title"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="deudaIncProMasEliminarSearchPageVO" property="procesoMasivo.recurso.desRecurso" />	</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="deudaIncProMasEliminarSearchPageVO" property="cuenta.numeroCuenta" />	</td>
			</tr>
			<logic:iterate id="item" name="deudaIncProMasEliminarSearchPageVO" property="listRecClaDeu">
				<tr>					
					<td>
						<logic:notPresent name="verLabelRecClaDeu">
							<label><bean:message bundle="def" key="def.recClaDeu.label"/>: </label>
						</logic:notPresent>&nbsp;
					</td>
					<bean:define id="verLabelRecClaDeu" value="false"/>
					<td class="normal" colspan="3"> 								
					   <bean:write name="item" property="desClaDeu"/>&nbsp; 
					</td> 
				</tr>
			</logic:iterate>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.deudaIncProMasEliminarSearchPage.fechaVencimientoDesde.label"/>: </label></td>
				<td class="normal">
					<bean:write name="deudaIncProMasEliminarSearchPageVO" property="fechaVencimientoDesdeView" /> 
				</td>
				<td><label><bean:message bundle="gde" key="gde.deudaIncProMasEliminarSearchPage.fechaVencimientoHasta.label"/>: </label></td>
				<td class="normal">
					<bean:write name="deudaIncProMasEliminarSearchPageVO" property="fechaVencimientoHastaView" />
				</td>
			</tr>
		</table>
	</fieldset>
	<!-- Fin Parametros de la Deuda -->
	
	<!-- Fin Filtro -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="deudaIncProMasEliminarSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="deudaIncProMasEliminarSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- cheks -->
							<!-- cuenta -->
							<th align="left"> <bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/> </th>
							<!-- anio -->
							<th align="left"> <bean:message bundle="gde" key="gde.deuda.anio.label"/> </th>
							<!-- periodo -->
							<th align="left"> <bean:message bundle="gde" key="gde.deuda.periodo.label"/> </th>
							<!-- clasif. deuda -->
							<th align="left"> <bean:message bundle="def" key="def.recClaDeu.abrev"/> </th>							
							<!-- fec. vto -->
							<th align="left"> <bean:message bundle="gde" key="gde.deuda.fechaVencimiento.label"/> </th>
							<!-- importe -->
							<th align="left"> <bean:message bundle="gde" key="gde.deuda.importe.label"/> </th>
							<!-- saldo -->
							<th align="left"> <bean:message bundle="gde" key="gde.deuda.saldo.label"/> </th>
							<!-- importe actualizado -->
							<th align="left"> <bean:message bundle="gde" key="gde.deuda.saldoActualizado.label"/> </th>
							<!-- titular principal  -->
							
							<th align="left"> <bean:message bundle="gde" key="gde.deuda.titularPrincipal.label"/> </th>
						</tr>
						<logic:iterate id="SelAlmDetVO" name="deudaIncProMasEliminarSearchPageVO" property="listResult">
							<tr>
								<!-- Check -->
								<td>	
									<html:multibox name="deudaIncProMasEliminarSearchPageVO" property="listIdSelAlmDet">
								   		<bean:write name="SelAlmDetVO" property="id" bundle="base" formatKey="general.format.id"/> 
								  	</html:multibox> 
								</td>
								<!-- cuenta -->
								<td> <bean:write name="SelAlmDetVO" property="deudaAdmin.cuenta.numeroCuenta"/>&nbsp;</td>
								<!-- anio -->
								<td> <bean:write name="SelAlmDetVO" property="deudaAdmin.anioView"/> &nbsp;</td>
								<!-- periodo -->
								<td> <bean:write name="SelAlmDetVO" property="deudaAdmin.periodoView"/> &nbsp;</td>
								<!-- clasif. deuda -->
								<td> <bean:write name="SelAlmDetVO" property="deudaAdmin.recClaDeu.desClaDeu"/> &nbsp;</td>								
								<!-- fec. vto -->
								<td> <bean:write name="SelAlmDetVO" property="deudaAdmin.fechaVencimientoView"/> &nbsp;</td>
								<!-- importe -->
								<td><bean:write name="SelAlmDetVO"  property="deudaAdmin.importe"          bundle="base" formatKey="general.format.currency" />&nbsp;</td>
								<!-- saldo -->
								<td><bean:write name="SelAlmDetVO"  property="deudaAdmin.saldo"            bundle="base" formatKey="general.format.currency" />&nbsp;</td>
								<!-- saldo actualizado -->
								<td><bean:write name="SelAlmDetVO"  property="deudaAdmin.saldoActualizado" bundle="base" formatKey="general.format.currency" />&nbsp;</td>
								<!-- titular principal  -->
								<td> <bean:write name="SelAlmDetVO" property="deudaAdmin.cuenta.nomTitPri"/> &nbsp;</td>
							</tr>
						</logic:iterate>
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="deudaIncProMasEliminarSearchPageVO" property="listResult">
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
   	    	<logic:equal name="deudaIncProMasEliminarSearchPageVO" property="viewResult" value="true">
				<logic:notEmpty  name="deudaIncProMasEliminarSearchPageVO" property="listResult">	
					<td align="right">
						<html:button property="btnSeleccionIndividual"  styleClass="boton" onclick="submitForm('eliminarSeleccionIndividual', '');">
							<bean:message bundle="gde" key="gde.deudaIncProMasEliminarSelecIndSearchPage.eliminar.label"/>
						</html:button>
					</td>
				</logic:notEmpty>
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

</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
