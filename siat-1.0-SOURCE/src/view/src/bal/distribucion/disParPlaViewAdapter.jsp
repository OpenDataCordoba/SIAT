<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarDisParPla.do">
	
		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.disParAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
	
		<!-- DisParPla -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.disParPla.title"/></legend>			
			<table class="tabladatos">
				<!-- Recurso -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="disParPlaAdapterVO" property="disParPla.disPar.recurso.desRecurso"/></td>
				</tr>
				<!-- DesDisPar-->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.disPar.desDisPar.label"/>: </label></td>
					<td class="normal"><bean:write name="disParPlaAdapterVO" property="disParPla.disPar.desDisPar"/></td>					
				</tr>
				<!-- Plan -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.disParPla.planVia.label"/>: </label></td>
					<td class="normal"><bean:write name="disParPlaAdapterVO" property="disParPla.plan.desPlanVia"/></td>					
				</tr>	
				<!-- Atributo -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="def" key="def.atributo.label"/>: </label></td>
					<logic:equal name="disParPlaAdapterVO" property="disParPla.tieneAtributo" value="true">	
						<td class="normal"><bean:write name="disParPlaAdapterVO" property="disParPla.disPar.recurso.atributoAse.desAtributo"/></td>
					</logic:equal>
					<logic:equal name="disParPlaAdapterVO" property="disParPla.tieneAtributo" value="false">	
						<td class="normal">No posee</td>
					</logic:equal>
				</tr>
				<logic:equal name="disParPlaAdapterVO" property="disParPla.tieneAtributo" value="true">	
				<!-- Valor -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.disParPla.valor.label"/>: </label></td>
					<td class="normal"><bean:write name="disParPlaAdapterVO" property="disParPla.valorView"/></td>									
				</tr>
				</logic:equal>
				
				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="disParPlaAdapterVO" property="disParPla"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<!-- Fin Inclucion de CasoView -->
				
				<!-- Fechas Desde/Hasta -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.disParPla.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="disParPlaAdapterVO" property="disParPla.fechaDesdeView"/></td>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.disParPla.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="disParPlaAdapterVO" property="disParPla.fechaHastaView"/></td>
				</tr>		
			</table>
		</fieldset>
		<!-- Fin DisParPla -->
	
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="disParPlaAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
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
	<!-- Fin Tabla que contiene todos los formularios -->