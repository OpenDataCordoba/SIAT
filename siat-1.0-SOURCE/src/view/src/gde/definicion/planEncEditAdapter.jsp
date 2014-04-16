<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/gde/AdministrarEncPlan.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="gde" key="gde.planEditAdapter.title"/></h1>		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- Plan -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.plan.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.plan.desPlan.label"/>: </label></td>
				<td class="normal"><html:text name="encPlanAdapterVO" property="plan.desPlan" size="30" maxlength="100"/></td>			
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.plan.ordenanza.label"/>: </label></td>
				<td class="normal"><html:text name="encPlanAdapterVO" property="plan.ordenanza" size="30"/></td>
			</tr>
			<logic:equal name="encPlanAdapterVO" property="act" value="agregar">
				<tr>	
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.viaDeuda.label"/>: </label></td>
					<td class="normal">
						<html:select name="encPlanAdapterVO" property="plan.viaDeuda.id" styleClass="select">
							<html:optionsCollection name="encPlanAdapterVO" property="listViaDeuda" label="desViaDeuda" value="id" />
						</html:select>
					</td>					
				</tr>
			</logic:equal>
			<logic:equal name="encPlanAdapterVO" property="act" value="modificar">
				<tr>	
					<td><label><bean:message bundle="def" key="def.viaDeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="encPlanAdapterVO" property="plan.viaDeuda.desViaDeuda"/></td>					
				</tr>
			</logic:equal>
			
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.sistema.label"/>: </label></td>
				<td class="normal">
					<html:select name="encPlanAdapterVO" property="plan.sistema.id" styleClass="select">
						<html:optionsCollection name="encPlanAdapterVO" property="listSistema" label="desSistema" value="id" />
					</html:select>
				</td>					
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.plan.fechaAlta.label"/>: </label></td>
				<td class="normal">
					<html:text name="encPlanAdapterVO" property="plan.fechaAltaView" styleId="fechaAltaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaAltaView');" id="a_fechaAltaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			
			<!-- Es Manual -->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.plan.esManual.label"/>: </label></td>
				<td class="normal">
					<html:select name="encPlanAdapterVO" property="plan.esManual.id" styleClass="select" onchange="submitForm('paramEsManual', '');">
						<html:optionsCollection name="encPlanAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
			
	<!-- Campos condicianales a esManual -->
	<logic:equal name="encPlanAdapterVO" property="plan.esManual.value" value="No">
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.plan.fecVenDeuDes.label"/>: </label></td>
				<td class="normal">
					<html:text name="encPlanAdapterVO" property="plan.fecVenDeuDesView" styleId="fecVenDeuDesView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fecVenDeuDesView');" id="a_fecVenDeuDesView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.plan.fecVenDeuHas.label"/>: </label></td>
				<td class="normal">
					<html:text name="encPlanAdapterVO" property="plan.fecVenDeuHasView" styleId="fecVenDeuHasView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fecVenDeuHasView');" id="a_fecVenDeuHasView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.plan.aplicaTotalImpago.label"/>: </label></td>
				<td class="normal">
					<html:select name="encPlanAdapterVO" property="plan.aplicaTotalImpago.id" styleClass="select">
						<html:optionsCollection name="encPlanAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.plan.canMaxCuo.label"/>: </label></td>
				<td class="normal"><html:text name="encPlanAdapterVO" property="plan.canMaxCuoView" size="20" maxlength="100"/></td>			
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.plan.canMinPer.label"/>: </label></td>
				<td class="normal"><html:text name="encPlanAdapterVO" property="plan.canMinPerView" size="20" maxlength="100"/></td>			
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.plan.canMinCuoParCuoSal.label"/>: </label></td>
				<td class="normal"><html:text name="encPlanAdapterVO" property="plan.canMinCuoParCuoSalView" size="20" maxlength="100"/></td>			
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.plan.canCuoAImpEnForm.label"/>: </label></td>
				<td class="normal"><html:text name="encPlanAdapterVO" property="plan.canCuoAImpEnFormView" size="20" maxlength="100"/></td>			
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.plan.cuoDesParaRec.label"/>: </label></td>
				<td class="normal"><html:text name="encPlanAdapterVO" property="plan.cuoDesParaRecView" size="20" maxlength="100"/></td>			
			</tr>
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.plan.poseeActEsp.label"/>: </label></td>
				<td class="normal">
					<html:select name="encPlanAdapterVO" property="plan.poseeActEsp.id" styleClass="select">
						<html:optionsCollection name="encPlanAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.tipoDeudaPlan.label"/>: </label></td>
				<td class="normal">
					<html:select name="encPlanAdapterVO" property="plan.tipoDeudaPlan.id" styleClass="select">
						<html:optionsCollection name="encPlanAdapterVO" property="listTipoDeudaPlan" label="desTipoDeudaPlan" value="id" />
					</html:select>
				</td>					
			</tr>
			
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.plan.aplicaPagCue.label"/>: </label></td>
				<td class="normal">
					<html:select name="encPlanAdapterVO" property="plan.aplicaPagCue.id" styleClass="select">
						<html:optionsCollection name="encPlanAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
			</tr>
	</logic:equal>			
		
			<!-- Fin Campos condicianales a esManual -->
			
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.plan.nameSequence.label"/>: </label></td>
				<td class="normal"><html:text name="encPlanAdapterVO" property="plan.nameSequence" size="20" maxlength="100"/></td>			
			</tr>
			
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.plan.formulario.label"/>: </label></td>
				<td class="normal">
					<html:text name="encPlanAdapterVO" property="plan.formulario.codFormulario" size="20" maxlength="100" disabled="true"/>
					<button type="button" onclick="submitForm('buscarFormulario', '');">
		      			<bean:message bundle="gde" key="gde.planEditAdapter.buscarFormulario.button"/>	      			
		      		</button>
				</td>				
			</tr>
			
			<tr>
				<td><label><bean:message bundle="gde" key="gde.plan.formulario.leyenda.label"/>: </label></td>
				<td class="normal">
					<html:textarea name="encPlanAdapterVO" property="plan.leyendaForm" cols="80" rows="15"/>
				</td>				
			</tr>
			
			<tr> 
				<td>&nbsp;</td>
				<td class="normal"><bean:message bundle="gde" key="gde.plan.leyendaForm.help"/></td>
			</tr>
			
			<!-- Inclucion de Caso -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="encPlanAdapterVO" property="plan"/>
					<bean:define id="voName" value="plan" />
					<%@ include file="/cas/caso/includeCaso.jsp" %>
				</td>
			</tr>
			<!-- Fin Inclucion de Caso -->	
			
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.plan.leyendaPlan.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:textarea name="encPlanAdapterVO" property="plan.leyendaPlan" cols="80" rows="15">
					</html:textarea>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.plan.linkNormativa.label"/>: </label></td>
				<td class="normal"><html:text name="encPlanAdapterVO" property="plan.linkNormativa" size="50" maxlength="100"/></td>			
			</tr>			
			<!-- <#Campos#> -->
		</table>
	</fieldset>
	<!-- Plan -->
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right">
				<logic:equal name="encPlanAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="encPlanAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>
				</logic:equal>				
			</td>
			</tr>
	</table>
   	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>	
</html:form>
<!-- Fin formulario -->
