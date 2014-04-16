<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
	
<!-- ConstanciaDeu -->
<fieldset>
	<legend><bean:message bundle="gde" key="gde.constanciaDeu.title"/></legend>
	
	<table class="tabladatos">		
		<tr>
			
		<!-- numero -->
			<td><label><bean:message bundle="gde" key="gde.constanciaDeu.numero.label"/>: </label></td>
			<td class="normal">
				<bean:write name="constanciaDeu" property="numeroView"/>
			</td>
			
		<!-- Anio -->	
			<td><label><bean:message bundle="gde" key="gde.constanciaDeu.anio.label"/>: </label></td>
			<td class="normal">
				<bean:write name="constanciaDeu" property="anioView"/>
			</td>
		</tr>
		
		<!-- Procurador -->
		<tr>	
			<td><label><bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
			<td class="normal">
				<bean:write name="constanciaDeu" property="procurador.descripcion"/>
			</td>
					
		<!-- Cuenta -->				
			<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
			<td class="normal">
				<bean:write name="constanciaDeu" property="cuenta.numeroCuenta"/>
			</td>			
		</tr>
		
		<!--  Recurso -->		
		<tr>	
			<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
			<td class="normal">
				<bean:write name="constanciaDeu" property="cuenta.recurso.desRecurso" />
			</td>
			<td><label><bean:message bundle="gde" key="gde.constanciaDeu.estConDeu.label"/>: </label></td>
			<td class="normal">
				<bean:write name="constanciaDeu" property="estConDeu.desEstConDeu" />
			</td>
		</tr>

		<!-- fecha envio -->
		<tr>	
			<td><label><bean:message bundle="gde" key="gde.constanciaDeu.fechaEnvio.label"/>: </label></td>
			<td class="normal">
				<bean:write name="constanciaDeu" property="procesoMasivo.fechaEnvioView" />
			</td>
			<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.ref"/>: </label></td>
			<td class="normal">
				<bean:write name="constanciaDeu" property="plaEnvDeuPro.nroBarraAnioPlanillaView" />
			</td>
		</tr>

		<!-- fechaHabilitacion -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.constanciaDeu.fechaHabilitacion.label"/>: </label></td>
			<td class="normal" colspan="3">
				<bean:write name="constanciaDeu" property="fechaHabilitacionView"/>
			</td>
		</tr>
		<!-- Domicilio envio -->
		<tr>
	    	<td><label><bean:message bundle="pad" key="pad.domicilio.envio.title"/>: </label></td>
	    	<td class="normal" colspan="3"><bean:write name="constanciaDeu" property="desDomEnv"/></td>
	    </tr>
	    
	    <!-- Ubicacion -->
	    <tr>
	    	<td><label><bean:message bundle="gde" key="gde.constanciaDeu.ubicacion.label"/>: </label></td>
	    	<td class="normal" colspan="3"><bean:write name="constanciaDeu" property="desDomUbi"/></td>
	    </tr>
	    
	    <!--  Titulares -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.constanciaDeu.desTitulares.label"/>: </label></td>
			<td class="normal" colspan="3">
				<bean:write name="constanciaDeu" property="desTitulares"/>
			</td>
		</tr>		    
	    
	    <!-- Inclucion de CasoView -->
		<tr>
			<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
			<td colspan="3">
				<bean:define id="IncludedVO" name="constanciaDeu"/>
				<%@ include file="/cas/caso/includeCasoView.jsp" %>				
			</td>
		</tr>
		<!-- Fin Inclucion de CasoView -->
	    
		<!-- Observaciones -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.constanciaDeu.observaciones.label"/>: </label></td>
			<td class="normal" colspan="3">
				<bean:write name="constanciaDeu" property="observacion"/>
			</td>
		</tr>
		<logic:equal name="act" value="modificar">
			<logic:present name="constanciaDeuAdapterVO">
				<tr>
					<td colspan="4"> 
						<bean:define id="modificarEncabezadoEnabled" name="constanciaDeuAdapterVO" property="modificarEncabezadoEnabled"/>
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
							'<bean:write name="constanciaDeu" property="id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>		
					</td>
				</tr>
			</logic:present>	
		</logic:equal>
		<!-- <#Campos#> -->
	</table>
</fieldset>
<!-- ConstanciaDeu -->
