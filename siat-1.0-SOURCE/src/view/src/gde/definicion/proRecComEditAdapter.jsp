<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarProRecCom.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.proRecComEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- ProrecCom -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.proRecCom.title"/></legend>
		
		<table class="tabladatos">
				<tr>
					<!-- Descripcion Procurador -->
					<td><label><bean:message bundle="gde" key="gde.procurador.descripcion.ref"/>: </label></td>
					<td class="normal"><bean:write name="proRecComAdapterVO" property="proRecCom.proRec.procurador.descripcion"/></td>
				</tr>
				<tr>
					<!-- Domicilio Procurador -->
					<td><label><bean:message bundle="gde" key="gde.procurador.domicilio.ref"/>: </label></td>
					<td class="normal"><bean:write name="proRecComAdapterVO" property="proRecCom.proRec.procurador.domicilio"/></td>

					<!-- Telefono Procurador -->
					<td><label><bean:message bundle="gde" key="gde.procurador.telefono.ref"/>: </label></td>
					<td class="normal"><bean:write name="proRecComAdapterVO" property="proRecCom.proRec.procurador.telefono"/></td>
				</tr>
				<tr>
					<!-- Horario Atencion Procurador -->
					<td><label><bean:message bundle="gde" key="gde.procurador.horarioAtencion.ref"/>: </label></td>
					<td class="normal"><bean:write name="proRecComAdapterVO" property="proRecCom.proRec.procurador.horarioAtencion"/></td>

					<!-- Tipo Procurador -->
					<td><label><bean:message bundle="gde" key="gde.procurador.tipoProcurador.ref"/>: </label></td>
					<td class="normal"><bean:write name="proRecComAdapterVO" property="proRecCom.proRec.procurador.tipoProcurador.desTipoProcurador"/></td>
				</tr>
				<tr>
					<!-- Observacion Procurador -->
					<td><label><bean:message bundle="gde" key="gde.procurador.observacion.ref"/>: </label></td>
					<td class="normal"><bean:write name="proRecComAdapterVO" property="proRecCom.proRec.procurador.observacion"/></td>
				</tr>				
				<tr>
					<!-- Descripcion Recurso -->
					<td><label><bean:message bundle="gde" key="gde.proRec.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="proRecComAdapterVO" property="proRecCom.proRec.recurso.desRecurso"/></td>				
				</tr>
				<tr>
					<!-- Fecha Desde Recurso -->
					<td><label><bean:message bundle="gde" key="gde.proRec.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="proRecComAdapterVO" property="proRecCom.proRec.fechaDesdeView"/></td>				

					<!-- Fecha Hasta Recurso -->
					<td><label><bean:message bundle="gde" key="gde.proRec.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="proRecComAdapterVO" property="proRecCom.proRec.fechaHastaView"/></td>				
				</tr>

			<!-- FecVtoDeuDes-->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.proRecCom.fecVtoDeuDes.label"/>: </label></td>
				<td class="normal">
					<html:text name="proRecComAdapterVO" property="proRecCom.fecVtoDeuDesView" styleId="fecVtoDeuDesView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fecVtoDeuDesView');" id="a_fecVtoDeuDesView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			
			<!-- FecVtoDeuHas-->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.proRecCom.fecVtoDeuHas.label"/>: </label></td>
				<td class="normal">
					<html:text name="proRecComAdapterVO" property="proRecCom.fecVtoDeuHasView" styleId="fecVtoDeuHasView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fecVtoDeuHasView');" id="a_fecVtoDeuHasView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>

			<!-- Porcentaje Comision -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.proRecCom.porcentajeComision.label"/>: </label></td>
				<td class="normal"><html:text name="proRecComAdapterVO" property="proRecCom.porcentajeComisionView" size="20" maxlength="100"/></td>			
			</tr>
			<!-- Fecha  Desde-->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.proRecCom.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="proRecComAdapterVO" property="proRecCom.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			
			<!-- Fecha  Hasta-->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.proRecCom.fechaHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="proRecComAdapterVO" property="proRecCom.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
		</table>
	</fieldset>	
	<!-- ProrecCom -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="proRecComAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="proRecComAdapterVO" property="act" value="agregar">
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
