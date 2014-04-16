<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarDeudaExcProMasAgregar.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="gde" key="gde.deudaExcProMasAgregarSelecIndSearchPage.title"/></h1>	
		
	<p><bean:message bundle="gde" key="gde.deudaExcProMasAgregarSelecIndSearchPage.legend"/></p>
		
	<!-- Parametros de la Deuda -->
    <fieldset>
    	<legend><bean:message bundle="gde" key="gde.deudaExcProMasAgregarSelecIndSearchPage.paramDeuda.title"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="deudaExcProMasAgregarSearchPageVO" property="procesoMasivo.recurso.desRecurso" />	</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="deudaExcProMasAgregarSearchPageVO" property="cuenta.numeroCuenta" />	</td>
			</tr>
			<logic:iterate id="item" name="deudaExcProMasAgregarSearchPageVO" property="listRecClaDeu">
				<tr>					
					<td>
						<logic:notPresent name="verLabelRecClaDeu">
							<label><bean:message bundle="def" key="def.recClaDeu.label"/>: </label>
						</logic:notPresent>&nbsp;
					</td>
					<bean:define id="verLabelRecClaDeu" value="false"/>
					<td class="normal" colspan="3"> 								
					   <bean:write name="item" property="desClaDeu"/> &nbsp;
					</td> 
				</tr>
			</logic:iterate>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.deudaExcProMasAgregarSearchPage.fechaVencimientoDesde.label"/>: </label></td>
				<td class="normal">
					<bean:write name="deudaExcProMasAgregarSearchPageVO" property="fechaVencimientoDesdeView" /> 
				</td>
				<td><label><bean:message bundle="gde" key="gde.deudaExcProMasAgregarSearchPage.fechaVencimientoHasta.label"/>: </label></td>
				<td class="normal">
					<bean:write name="deudaExcProMasAgregarSearchPageVO" property="fechaVencimientoHastaView" />
				</td>
			</tr>
		</table>
	</fieldset>
	<!-- Fin Parametros de la Deuda -->
	
	<!-- Fin Filtro -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="deudaExcProMasAgregarSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="deudaExcProMasAgregarSearchPageVO" property="listResult">	
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
						<logic:iterate id="DeudaAdminVO" name="deudaExcProMasAgregarSearchPageVO" property="listResult">
							<tr>
								<!-- Check -->
								<td>	
									<html:multibox name="deudaExcProMasAgregarSearchPageVO" property="listIdDeudaAdmin">
								   		<bean:write name="DeudaAdminVO" property="id" bundle="base" formatKey="general.format.id"/> 
								  	</html:multibox> 
								</td>
								<!-- cuenta -->
								<td> <bean:write name="DeudaAdminVO" property="cuenta.numeroCuenta"/>&nbsp;</td>
								<!-- anio -->
								<td> <bean:write name="DeudaAdminVO" property="anioView"/> &nbsp;</td>
								<!-- periodo -->
								<td> <bean:write name="DeudaAdminVO" property="periodoView"/> &nbsp;</td>
								<!-- clasif. deuda -->
								<td> <bean:write name="DeudaAdminVO" property="recClaDeu.desClaDeu"/> &nbsp;</td>								
								<!-- fec. vto -->
								<td> <bean:write name="DeudaAdminVO" property="fechaVencimientoView"/> &nbsp;</td>
								<!-- importe -->
								<td><bean:write name="DeudaAdminVO" property="importe"          bundle="base" formatKey="general.format.currency" />&nbsp;</td>
								<!-- saldo -->
								<td><bean:write name="DeudaAdminVO" property="saldo"            bundle="base" formatKey="general.format.currency" />&nbsp;</td>
								<!-- saldo actualizado -->
								<td><bean:write name="DeudaAdminVO" property="saldoActualizado" bundle="base" formatKey="general.format.currency" />&nbsp;</td>
								<!-- titular principal  -->
								<td> <bean:write name="DeudaAdminVO" property="cuenta.nomTitPri"/> &nbsp;</td>
							</tr>
						</logic:iterate>
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="deudaExcProMasAgregarSearchPageVO" property="listResult">
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
   	    	<logic:equal name="deudaExcProMasAgregarSearchPageVO" property="viewResult" value="true">
				<logic:notEmpty  name="deudaExcProMasAgregarSearchPageVO" property="listResult">	
					<td align="right">
						<html:button property="btnSeleccionIndividual"  styleClass="boton" onclick="submitForm('agregarSeleccionIndividual', '');">
							<bean:message bundle="gde" key="gde.deudaExcProMasAgregarSelecIndSearchPage.agregar.label"/>
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
