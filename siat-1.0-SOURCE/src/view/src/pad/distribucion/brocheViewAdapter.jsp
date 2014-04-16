<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/pad/AdministrarBroche.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pad" key="pad.brocheAdapter.title"/></h1>		
		
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
			
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.broche.title"/></legend>			
			<table class="tabladatos" width="100%">
				<tr>
					<!-- Recurso -->		
					<td><label>&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="brocheAdapterVO" property="broche.recurso.desRecurso"/></td>
					<!-- Tipo Broche -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.tipoBroche.label"/>: </label></td>
					<td class="normal"><bean:write name="brocheAdapterVO" property="broche.tipoBroche.desTipoBroche"/></td>					
				</tr>
				<!-- Numero -->
				<tr>
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.nro.label"/>: </label></td>
					<td class="normal"><bean:write name="brocheAdapterVO" property="broche.idView"/></td>					
				</tr>
				
				<!-- Titular o Descripcion-->		
				<tr>
					<logic:equal name="brocheAdapterVO" property="paramTipoBroche" value="1">	<!-- TipoBroche=Administrativo -->	
						<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.titular.label"/>: </label></td>
					</logic:equal>
					<logic:notEqual name="brocheAdapterVO" property="paramTipoBroche" value="1">	<!-- TipoBroche<>Administrativo -->		
						<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.desBroche.label"/>: </label></td>
					</logic:notEqual>
					<td class="normal"><bean:write name="brocheAdapterVO" property="broche.desBroche"/></td>					
				</tr>

				<!-- Exento Envio Judicial -->
				<tr>	
					<td><label><bean:message bundle="pad" key="pad.broche.exentoEnvioJud.label"/>: </label></td>
					<td class="normal"><bean:write name="brocheAdapterVO" property="broche.exentoEnvioJud.value"/>
					</td>
				</tr>
				
				<!-- Permite Impresion -->
				<tr>
					<td><label><bean:message bundle="pad" key="pad.broche.permiteImpresion.label"/>: </label></td>
					<td class="normal"><bean:write name="brocheAdapterVO" property="broche.permiteImpresion.value"/>
					</td>
				</tr>
				
				<logic:equal name="brocheAdapterVO" property="paramTipoBroche" value="1">	<!-- TipoBroche=Administrativo -->	
				<!-- Domicilio -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.strDomicilioEnvio.label"/>: </label></td>
					<td class="normal"><bean:write name="brocheAdapterVO" property="broche.strDomicilioEnvio"/></td>					
				</tr>
				<!-- Telefono -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.telefono.label"/>: </label></td>
					<td class="normal"><bean:write name="brocheAdapterVO" property="broche.telefono"/></td>					
				</tr>
				</logic:equal>
				<!-- Repartidor -->	
				<tr>
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.repartidor.label"/>: </label></td>
					<td class="normal">
						<logic:notEqual name="brocheAdapterVO" property="paramTipoBroche" value="2">	<!-- TipoBroche<>'Repartidor Fuera de Zona' & TipoBroche<>'Repartido Zona' -->		
							<bean:write name="brocheAdapterVO" property="broche.repartidor.descripcionForCombo"/>
						</logic:notEqual>	
					</td>	
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
	   	    	<td align="right" width="50%">
					<logic:equal name="brocheAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="brocheAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="brocheAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
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