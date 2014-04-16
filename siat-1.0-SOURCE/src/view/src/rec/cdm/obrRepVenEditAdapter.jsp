<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rec/AdministrarObrRepVen.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="rec" key="rec.obrRepVenEditAdapter.title"/></h1>

	<table class="tablabotones" width="100%">
		<tr>			
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
					<bean:write name="obrRepVenAdapterVO" property="obrRepVen.obra.recurso.desRecurso"/>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.numeroObra.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="obrRepVenAdapterVO" property="obrRepVen.obra.numeroObraView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.desObra.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="obrRepVenAdapterVO" property="obrRepVen.obra.desObra"/></td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.permiteCamPlaMay.label"/>: </label></td>
				<td class="normal"><bean:write name="obrRepVenAdapterVO" property="obrRepVen.obra.permiteCamPlaMay.value"/></td>				
			</tr>
			
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.esPorValuacion.label"/>: </label></td>
				<td class="normal"><bean:write name="obrRepVenAdapterVO" property="obrRepVen.obra.esPorValuacion.value"/></td>				
			</tr>

			<tr>	
				<td><label><bean:message bundle="rec" key="rec.obra.esCostoEsp.label"/>: </label></td>
				<td class="normal"><bean:write name="obrRepVenAdapterVO" property="obrRepVen.obra.esCostoEsp.value"/></td>				
				<logic:equal name="obrRepVenAdapterVO" property="obrRepVen.obra.costoEspEnabled" value="true">					
					<td><label><bean:message bundle="rec" key="rec.obra.costoEsp.label"/>: </label></td>
					<td class="normal"><bean:write name="obrRepVenAdapterVO" property="obrRepVen.obra.costoEspView"/></td>
				</logic:equal>
			</tr>

			<!-- Inclucion de CasoView -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="obrRepVenAdapterVO" property="obrRepVen.obra"/>
					<%@ include file="/cas/caso/includeCasoView.jsp" %>				
				</td>
			</tr>
			<!-- Fin Inclucion de CasoView -->
			
			<tr>
				<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td class="normal"><bean:write name="obrRepVenAdapterVO" property="obrRepVen.obra.estadoObra.desEstadoObra"/></td>
			</tr>
		</table>
	</fieldset>	
	<!-- Obra -->
	
	<!-- ObrRepVen -->
	<fieldset>
		<legend><bean:message bundle="rec" key="rec.obrRepVen.title"/></legend>
		
		<table class="tabladatos">
			
			<!-- Cuota Desde -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obrRepVen.cuotaDesde.label"/>: </label></td>
				<td class="normal"><html:text name="obrRepVenAdapterVO" property="obrRepVen.cuotaDesdeView" size="10" maxlength="100"/></td>			
			</tr>
			
			<!-- NueFecVen -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obrRepVen.nueFecVen.label"/>: </label></td>
				<td class="normal">
					<html:text name="obrRepVenAdapterVO" property="obrRepVen.nueFecVenView" styleId="View" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('View');" id="a_View">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			
			<!-- Inclucion de Caso -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="obrRepVenAdapterVO" property="obrRepVen"/>
					<bean:define id="voName" value="obrRepVen" />
					<%@ include file="/cas/caso/includeCaso.jsp" %>
				</td>
			</tr>
			<!-- Fin Inclucion de Caso -->
			
			<!-- Descripcion -->
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obrRepVen.descripcion.label"/>: </label></td>
				<td class="normal"><html:text name="obrRepVenAdapterVO" property="obrRepVen.descripcion" size="20" maxlength="100"/></td>			
			</tr>

		</table>
	</fieldset>	
	<!-- ObrRepVen -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="obrRepVenAdapterVO" property="act" value="agregar">
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
<!-- Fin Tabla que contiene todos los formularios -->
