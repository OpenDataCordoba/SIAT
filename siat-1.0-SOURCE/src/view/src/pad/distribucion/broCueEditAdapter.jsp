<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/pad/AdministrarBroCue.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pad" key="pad.broCueAdapter.title"/></h1>		
		
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
			
				<!-- Broche -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.broche.title"/></legend>			
			<table class="tabladatos" width="100%">
				<!-- Recurso -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="broCueAdapterVO" property="broCue.broche.recurso.desRecurso"/></td>
					<!-- Tipo Broche -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.tipoBroche.label"/>: </label></td>
					<td class="normal"><bean:write name="broCueAdapterVO" property="broCue.broche.tipoBroche.desTipoBroche"/></td>
					</td>	
				</tr>
				<!-- Numero -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.nro.label"/>: </label></td>
					<td class="normal"><bean:write name="broCueAdapterVO" property="broCue.broche.idView"/></td>					
				</tr>
				<!-- Titular o Descripcion-->		
				<tr>
					<logic:equal name="broCueAdapterVO" property="paramTipoBroche" value="1">	<!-- TipoBroche=Administrativo -->	
						<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.titular.label"/>: </label></td>
					</logic:equal>
					<logic:notEqual name="broCueAdapterVO" property="paramTipoBroche" value="1">	<!-- TipoBroche<>Administrativo -->		
						<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.desBroche.label"/>: </label></td>
					</logic:notEqual>
					<td class="normal"><bean:write name="broCueAdapterVO" property="broCue.broche.desBroche"/></td>					
				</tr>
				<logic:equal name="broCueAdapterVO" property="paramTipoBroche" value="1">	<!-- TipoBroche=Administrativo -->	
				<!-- Domicilio -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.strDomicilioEnvio.label"/>: </label></td>
					<td class="normal"><bean:write name="broCueAdapterVO" property="broCue.broche.strDomicilioEnvio"/></td>					
				</tr>
				<!-- Telefono -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.telefono.label"/>: </label></td>
					<td class="normal"><bean:write name="broCueAdapterVO" property="broCue.broche.telefono"/></td>					
				</tr>
				</logic:equal>
				<!-- Repartidor -->	
				<tr>
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.repartidor.label"/>: </label></td>
					<td class="normal">
						<logic:notEqual name="broCueAdapterVO" property="paramTipoBroche" value="2">	<!-- TipoBroche<>'Repartidor Fuera de Zona' & TipoBroche<>'Repartido Zona' -->		
							<bean:write name="broCueAdapterVO" property="broCue.broche.repartidor.descripcionForCombo"/>
						</logic:notEqual>
					</td>	
				</tr>			
			</table>
		</fieldset>
		<!-- Fin Broche -->
				
		<!-- BroCue -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.broCue.title"/></legend>
			
			<table class="tabladatos">
				<!-- Cuenta -->
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal">			
						<logic:equal name="broCueAdapterVO" property="act" value="agregar">
							<html:text name="broCueAdapterVO" property="broCue.cuenta.numeroCuenta" size="30" maxlength="100" disabled="false"/>
						</logic:equal>
						<logic:equal name="broCueAdapterVO" property="act" value="modificar">
							<html:text name="broCueAdapterVO" property="broCue.cuenta.numeroCuenta" size="30" maxlength="100" disabled="true"/>
						</logic:equal>
					</td>
					<logic:equal name="broCueAdapterVO" property="act" value="agregar">
					<td>	
							<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
								<bean:message bundle="pad" key="pad.broCueAdapter.adm.button.buscarCuenta"/>
							</html:button>
					</td>										
					</logic:equal>
				</tr>
				
				<!-- Inclucion de Caso -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="broCueAdapterVO" property="broCue"/>
						<bean:define id="voName" value="broCue" />
						<%@ include file="/cas/caso/includeCaso.jsp" %>
					</td>
				</tr>
				<!-- Fin Inclucion de Caso -->
				
				<!-- Fecha Alta/Baja -->
				<tr>
					<logic:equal name="broCueAdapterVO" property="act" value="agregar">
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.broCue.fechaAlta.label"/>: </label></td>
					<td class="normal">
						<html:text name="broCueAdapterVO" property="broCue.fechaAltaView" styleId="fechaAltaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaAltaView');" id="a_fechaAltaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					</logic:equal>
					<logic:equal name="broCueAdapterVO" property="act" value="modificar">
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.broCue.fechaBaja.label"/>: </label></td>
					<td class="normal">
						<html:text name="broCueAdapterVO" property="broCue.fechaBajaView" styleId="fechaBajaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaBajaView');" id="a_fechaBajaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					</logic:equal>
				</tr>		
			</table>
		</fieldset>
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="broCueAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="broCueAdapterVO" property="act" value="agregar">
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
		