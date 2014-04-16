<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarEncProRec.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.proRecEncEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- ProRec -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.proRec.title"/></legend>
		
		<table class="tabladatos">
		<!-- Procurador -->		
		<tr>
			<td><label><bean:message bundle="gde" key="gde.procurador.descripcion.ref"/>: </label></td>
			<td class="normal">
				<bean:write name="encProRecAdapterVO" property="proRec.procurador.descripcion"/>				
			</td>
		</tr>
		<tr>
			<td><label><bean:message bundle="gde" key="gde.procurador.domicilio.ref"/>: </label></td>
			<td class="normal">
				<bean:write name="encProRecAdapterVO" property="proRec.procurador.domicilio"/>				
			</td>

			<td><label><bean:message bundle="gde" key="gde.procurador.telefono.ref"/>: </label></td>
			<td class="normal">
				<bean:write name="encProRecAdapterVO" property="proRec.procurador.telefono"/>				
			</td>
		</tr>
		<tr>
			<td><label><bean:message bundle="gde" key="gde.procurador.horarioAtencion.ref"/>: </label></td>
			<td class="normal">
				<bean:write name="encProRecAdapterVO" property="proRec.procurador.horarioAtencion"/>				
			</td>

			<td><label><bean:message bundle="gde" key="gde.procurador.tipoProcurador.ref"/>: </label></td>
			<td class="normal">
				<bean:write name="encProRecAdapterVO" property="proRec.procurador.tipoProcurador.desTipoProcurador"/>				
			</td>
		</tr>
		<tr>
			<td><label><bean:message bundle="gde" key="gde.procurador.observacion.ref"/>: </label></td>
			<td class="normal">
				<bean:write name="encProRecAdapterVO" property="proRec.procurador.observacion"/>				
			</td>
		</tr>
		
		<!-- Recurso -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.proRec.recurso.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:select name="encProRecAdapterVO" property="proRec.recurso.id" styleClass="select" >
					<bean:define id="includeRecursoList" name="encProRecAdapterVO" property="listRecurso"/>
					<bean:define id="includeIdRecursoSelected" name="encProRecAdapterVO" property="proRec.recurso.id"/>
					<%@ include file="/def/gravamen/includeRecurso.jsp" %>
				</html:select>			
			</td>
		</tr>
		
		<tr>
		<!-- fechaDesde -->
			<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.proRec.fechaDesde.label"/>: </label></td>
			<td class="normal">
				<html:text name="encProRecAdapterVO" property="proRec.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>

		<!-- fechaHasta -->
			<td><label><bean:message bundle="gde" key="gde.proRec.fechaHasta.label"/>: </label></td>
			<td class="normal">
				<html:text name="encProRecAdapterVO" property="proRec.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
			
		</table>
	</fieldset>	
	<!-- ProRec -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="encProRecAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="encProRecAdapterVO" property="act" value="agregar">
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
