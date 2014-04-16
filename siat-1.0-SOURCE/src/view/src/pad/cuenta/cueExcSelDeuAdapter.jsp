<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/pad/AdministrarCueExcSelDeu.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pad" key="pad.cueExcSelDeuAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- CueExcSelDeu -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.cueExcSelDeu.title"/></legend>
			
			<table class="tabladatos">
				<!-- Recurso -->
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cueExcSel.cuenta.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="cueExcSelDeuAdapterVO" property="cueExcSelDeu.cueExcSel.cuenta.recurso.desRecurso"/></td>
				</tr>
				<!-- Cuenta -->
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cueExcSel.cuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="cueExcSelDeuAdapterVO" property="cueExcSelDeu.cueExcSel.cuenta.numeroCuenta"/></td>
				</tr>
				
				<!-- Inclucion de Caso -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="cueExcSelDeuAdapterVO" property="cueExcSelDeu"/>
						<bean:define id="voName" value="cueExcSelDeu" />
						<%@ include file="/cas/caso/includeCaso.jsp" %>
					</td>
				</tr>
				<!-- Fin Inclucion de Caso -->
								
				<!-- Observacion -->
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cueExcSelDeu.observacion.label"/>: </label></td>
					<td class="normal">
						<html:textarea name="cueExcSelDeuAdapterVO" property="cueExcSelDeu.observacion" cols="80" rows="15"/>	
			      	</td>
				</tr>
			</table>
		</fieldset>	
		<!-- CueExcSelDeu -->
		
	<!-- Deuda En Gestion Administrativa -->	
	<logic:notEmpty name="cueExcSelDeuAdapterVO" property="liqDeudaAdapter.listGestionDeudaAdmin">
		<bean:define id="liqDeudaAdapterVO" name="cueExcSelDeuAdapterVO" property="liqDeudaAdapter" />
			<%@ include file="/gde/gdeuda/includeDeudaGestionAdmin.jsp" %>
	</logic:notEmpty>
	
	<logic:empty name="cueExcSelDeuAdapterVO" property="liqDeudaAdapter.listGestionDeudaAdmin">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:message bundle="pad" key="pad.cueExcSelDeu.listGestionDeudaAdmin.label"/></caption>
               	<tbody>
					<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</tbody>			
			</table>
	</logic:empty>
	<!-- Fin Deuda En Gestion Administrativa -->
				
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left" width="50%">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>   	    			

   	    		<td align="right" width="50%">
					<html:button property="btnSeleccionar"  styleClass="boton" onclick="submitForm('seleccionar', '');">
						<bean:message bundle="base" key="abm.button.seleccionar"/>
					</html:button>
				</td>
			</tr>
		</table>
	   	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
