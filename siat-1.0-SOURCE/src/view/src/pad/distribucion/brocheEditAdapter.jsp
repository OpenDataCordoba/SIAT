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
					
		<!-- Broche -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.broche.title"/></legend>
			
			<table class="tabladatos">
				<!-- Recurso -->		
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal">
					<logic:equal name="brocheAdapterVO" property="act" value="agregar">		
						<html:select name="brocheAdapterVO" property="broche.recurso.id" styleClass="select" >
							<bean:define id="includeRecursoList" name="brocheAdapterVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="brocheAdapterVO" property="broche.recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
		  			</logic:equal>
					<logic:equal name="brocheAdapterVO" property="act" value="modificar">		
						<bean:write name="brocheAdapterVO" property="broche.recurso.desRecurso"/>
					</logic:equal>
					</td>
				</tr>
				
				<!-- Tipo Broche -->		
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.tipoBroche.label"/>: </label></td>
					<td class="normal">
						<logic:equal name="brocheAdapterVO" property="act" value="agregar">		
							<html:select name="brocheAdapterVO" property="broche.tipoBroche.id" styleClass="select" onchange="submitForm('paramTipoBroche', '');">
								<html:optionsCollection name="brocheAdapterVO" property="listTipoBroche" label="desTipoBroche" value="id" />
							</html:select>
						</logic:equal>
						<logic:equal name="brocheAdapterVO" property="act" value="modificar">		
							<bean:write name="brocheAdapterVO" property="broche.tipoBroche.desTipoBroche"/>
						</logic:equal>
					</td>	
				</tr>
				<!-- Numero -->		
				<logic:equal name="brocheAdapterVO" property="act" value="modificar">		
					<tr>
						<td><label><bean:message bundle="pad" key="pad.broche.nro.label"/>: </label></td>
						<td class="normal"><bean:write name="brocheAdapterVO" property="broche.idView"/></td>					
					</tr>
				</logic:equal>
				
				<!-- Titular o Descripcion-->
				<tr>
					<logic:equal name="brocheAdapterVO" property="paramTipoBroche" value="1">	<!-- TipoBroche=Administrativo -->	
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.broche.titular.label"/>: </label></td>
					</logic:equal>
					<logic:notEqual name="brocheAdapterVO" property="paramTipoBroche" value="1">	<!-- TipoBroche<>Administrativo -->		
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.broche.desBroche.label"/>: </label></td>
					</logic:notEqual>
					<td class="normal"><html:text name="brocheAdapterVO" property="broche.desBroche" size="20" maxlength="100" styleClass="datos"/></td>					
				</tr>
	
				<!-- Exento Envio Judicial -->
				<tr>	
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.broche.exentoEnvioJud.label"/>: </label></td>
					<td class="normal">
						<html:select name="brocheAdapterVO" property="broche.exentoEnvioJud.id" styleClass="select">
							<html:optionsCollection name="brocheAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
				</tr>
				
				<!-- Permite Impresion -->
				<tr>	
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.broche.permiteImpresion.label"/>: </label></td>
					<td class="normal">
						<html:select name="brocheAdapterVO" property="broche.permiteImpresion.id" styleClass="select">
							<html:optionsCollection name="brocheAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
				</tr>

				<logic:equal name="brocheAdapterVO" property="paramTipoBroche" value="1">	<!-- TipoBroche=Administrativo -->	
				<!-- Domicilio -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.strDomicilioEnvio.label"/>: </label></td>
					<td class="normal"><html:text name="brocheAdapterVO" property="broche.strDomicilioEnvio" size="20" maxlength="255" styleClass="datos"/></td>					
				</tr>
				<!-- Telefono -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.telefono.label"/>: </label></td>
					<td class="normal"><html:text name="brocheAdapterVO" property="broche.telefono" size="20" maxlength="50" styleClass="datos"/></td>					
				</tr>
				</logic:equal>
				<!-- Repartidor -->	
				<tr>
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.repartidor.label"/>: </label></td>
					<td class="normal">
						<logic:notEqual name="brocheAdapterVO" property="paramTipoBroche" value="2">	<!-- TipoBroche<>'Repartidor Fuera de Zona' & TipoBroche<>'Repartido Zona' -->		
							<html:select name="brocheAdapterVO" property="broche.repartidor.id" styleClass="select" disabled="false">
								<html:optionsCollection name="brocheAdapterVO" property="listRepartidor" label="descripcionForCombo" value="id" />
							</html:select>
						</logic:notEqual>
						<logic:equal name="brocheAdapterVO" property="paramTipoBroche" value="2">	<!-- TipoBroche='Repartidor Fuera de Zona' || TipoBroche='Repartido Zona' -->		
							<html:select name="brocheAdapterVO" property="broche.repartidor.id" styleClass="select" disabled="true">
								<html:optionsCollection name="brocheAdapterVO" property="listRepartidor" label="descripcionForCombo" value="id" />
							</html:select>
						</logic:equal>
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
				<td align="right">
					<logic:equal name="brocheAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="brocheAdapterVO" property="act" value="agregar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
							<bean:message bundle="base" key="abm.button.agregar"/>
						</html:button>
					</logic:equal>				
				</td>
 			</tr>
		</table>
	   	
	   	<input type="text" style="display:none"/>
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
		