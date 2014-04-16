<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarCierreComercio.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="pad" key="pad.cierreComercioEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- CierreComercio -->
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.cierreComercio.title"/></legend>
		
		<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cierreComercio.fechaCierreDefinitivo.label"/>: </label></td>
					<td class="normal">
						<html:text name="cierreComercioAdapterVO" property="cierreComercio.fechaCierreDefView" styleId="fechaCierreDefView" size="15" maxlength="10" styleClass="datos"/>
						<a class="link_siat" onclick="return show_calendar('fechaCierreDefView');" id="a_fechaCierreDefView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cierreComercio.fechaCeseActividad.label"/>: </label></td>
					<td class="normal">
						<html:text name="cierreComercioAdapterVO" property="cierreComercio.fechaCeseActividadView" styleId="fechaCeseActividadView" size="15" maxlength="10" styleClass="datos"/>
						<a class="link_siat" onclick="return show_calendar('fechaCeseActividadView');" id="a_fechaCeseActividadView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
					<td class="normal"><bean:write name="cierreComercioAdapterVO" property="cierreComercio.cuentaVO.numeroCuenta"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="cierreComercioAdapterVO" property="cierreComercio.cuentaVO.recurso.desRecurso"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cierreComercio.fechaInicioTramite.label"/>: </label></td>
					<td class="normal"><bean:write name="cierreComercioAdapterVO" property="cierreComercio.fechaTramiteView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<!-- Inclucion de CasoView -->
					<td colspan="3">
						<bean:define id="IncludedVO" name="cierreComercioAdapterVO" property="cierreComercio"/>
						<bean:define id="voName" value="cierreComercio" />
						<%@ include file="/cas/caso/includeCaso.jsp" %>				
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
					<td class="normal"><bean:write name="cierreComercioAdapterVO" property="cierreComercio.cuentaVO.numeroCuenta"/></td>
				</tr>
		</table>
	</fieldset>	
	<!-- CierreComercio -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="cierreComercioAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificarCierreDefComercio', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="cierreComercioAdapterVO" property="act" value="agregar">
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
