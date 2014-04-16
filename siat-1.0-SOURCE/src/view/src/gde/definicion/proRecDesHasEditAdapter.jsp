<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarProRecDesHas.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.proRecDesHasEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- ProRecDesHas -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.proRecDesHas.title"/></legend>
		
		<table class="tabladatos">
				<tr>
					<!-- Descripcion Procurador -->
					<td><label><bean:message bundle="gde" key="gde.procurador.descripcion.ref"/>: </label></td>
					<td class="normal"><bean:write name="proRecDesHasAdapterVO" property="proRecDesHas.proRec.procurador.descripcion"/></td>
				</tr>
				<tr>
					<!-- Domicilio Procurador -->
					<td><label><bean:message bundle="gde" key="gde.procurador.domicilio.ref"/>: </label></td>
					<td class="normal"><bean:write name="proRecDesHasAdapterVO" property="proRecDesHas.proRec.procurador.domicilio"/></td>

					<!-- Telefono Procurador -->
					<td><label><bean:message bundle="gde" key="gde.procurador.telefono.ref"/>: </label></td>
					<td class="normal"><bean:write name="proRecDesHasAdapterVO" property="proRecDesHas.proRec.procurador.telefono"/></td>
				</tr>
				<tr>
					<!-- Horario Atencion Procurador -->
					<td><label><bean:message bundle="gde" key="gde.procurador.horarioAtencion.ref"/>: </label></td>
					<td class="normal"><bean:write name="proRecDesHasAdapterVO" property="proRecDesHas.proRec.procurador.horarioAtencion"/></td>

					<!-- Tipo Procurador -->
					<td><label><bean:message bundle="gde" key="gde.procurador.tipoProcurador.ref"/>: </label></td>
					<td class="normal"><bean:write name="proRecDesHasAdapterVO" property="proRecDesHas.proRec.procurador.tipoProcurador.desTipoProcurador"/></td>
				</tr>
				<tr>
					<!-- Observacion Procurador -->
					<td><label><bean:message bundle="gde" key="gde.procurador.observacion.ref"/>: </label></td>
					<td class="normal"><bean:write name="proRecDesHasAdapterVO" property="proRecDesHas.proRec.procurador.observacion"/></td>
				</tr>				
				<tr>
					<!-- Descripcion Recurso -->
					<td><label><bean:message bundle="gde" key="gde.proRec.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="proRecDesHasAdapterVO" property="proRecDesHas.proRec.recurso.desRecurso"/></td>				
				</tr>
				<tr>
					<!-- Fecha Desde Recurso -->
					<td><label><bean:message bundle="gde" key="gde.proRec.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="proRecDesHasAdapterVO" property="proRecDesHas.proRec.fechaDesdeView"/></td>				

					<!-- Fecha Hasta Recurso -->
					<td><label><bean:message bundle="gde" key="gde.proRec.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="proRecDesHasAdapterVO" property="proRecDesHas.proRec.fechaHastaView"/></td>				
				</tr>
			<!-- Desde -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.proRecDesHas.desde.label"/>: </label></td>
				<td class="normal"><html:text name="proRecDesHasAdapterVO" property="proRecDesHas.desde" size="20" maxlength="100"/></td>			
	
			
			<!-- Hasta -->
	
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.proRecDesHas.hasta.label"/>: </label></td>
				<td class="normal"><html:text name="proRecDesHasAdapterVO" property="proRecDesHas.hasta" size="20" maxlength="100"/></td>			
			</tr>
			
			<!-- Fecha  Desde-->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.proRecDesHas.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="proRecDesHasAdapterVO" property="proRecDesHas.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
	
			
			<!-- Fecha  Hasta-->
	
				<td><label><bean:message bundle="gde" key="gde.proRecDesHas.fechaHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="proRecDesHasAdapterVO" property="proRecDesHas.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
		</table>
	</fieldset>	
	<!-- ProRecDesHas -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="proRecDesHasAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="proRecDesHasAdapterVO" property="act" value="agregar">
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
