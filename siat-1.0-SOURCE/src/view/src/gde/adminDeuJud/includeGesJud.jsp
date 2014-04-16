<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
		
		<!-- GesJud -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.gesJud.title"/></legend>
		<table class="tabladatos">
		
		<!-- Procurador -->
		<tr>	
			<td><label><bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
			<td class="normal">
				<bean:write name="gesJud" property="procurador.descripcion"/>					
			</td>
			<td><label><bean:message bundle="gde" key="gde.gesJud.fechaAlta.label"/>: </label></td>
			<td class="normal">
					<bean:write name="gesJud" property="fechaAltaView"/>				
			</td>								
		</tr>
		
		<!-- Tipojuzgado -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.gesJud.tipoJuzgado.label"/>: </label></td>
			<td class="normal">
				<bean:write name="gesJud" property="tipoJuzgado.desTipoJuzgado"/>					
			</td>
						
		<!-- Inclucion de CasoView -->
		<tr>
			<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
			<td colspan="3">
				<bean:define id="IncludedVO" name="gesJud"/>
				<%@ include file="/cas/caso/includeCasoView.jsp" %>				
			</td>
		</tr>
		<!-- Fin Inclucion de CasoView -->
		
		<!-- Nro y Anio Expediente Judicial -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.gesJud.nroExpediente.label"/>: </label></td>
			<td class="normal"><bean:write name="gesJud" property="nroExpedienteView"/></td>			
			<td><label><bean:message bundle="gde" key="gde.gesJud.anioExpediente.label"/>: </label></td>
			<td class="normal"><bean:write name="gesJud" property="anioExpedienteView"/></td>
		</tr>
		
		<!-- descripcion -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.gesJud.desGesJud.label"/>: </label></td>
			<td class="normal"><bean:write name="gesJud" property="desGesJud"/></td>

		<!-- Estado -->
			<td><label><bean:message bundle="gde" key="gde.gesJud.estadoGesjud.label"/>: </label></td>
			<td class="normal" colspan="3">
				<bean:write name="gesJud" property="estadoGesJudVO.desEstadoGesJud"/>
			</td>			
		</tr>
		
		<!-- Juzgado -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.gesJud.juzgado.label"/>: </label></td>
			<td class="normal"><bean:write name="gesJud" property="juzgado"/></td>			
                 
   		<!-- fechaCaducidad -->
   		<logic:notEmpty name="act">
        		<logic:equal name="act" value="registrarCaducidad">
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.gesJud.fechaCaducidad.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:text name="gesJudAdapterVO" property="gesJud.fechaCaducidadView" styleId="fechaCaducidadView" size="15" maxlength="10" styleClass="datos"/>
						<a class="link_siat" onclick="return show_calendar('fechaCaducidadView');" id="a_fechaCaducidadView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>         		
         		</logic:equal>
				<logic:notEqual name="act" value="registrarCaducidad">         					
					<td><label><bean:message bundle="gde" key="gde.gesJud.fechaCaducidad.label"/>: </label></td>
					<td class="normal">
						<bean:write name="gesJud" property="fechaCaducidadView"/>
					</td>
				</logic:notEqual>	
			</logic:notEmpty>	
		</tr>	
		
		<!-- Observaciones -->
		<tr>		
			<td><label><bean:message bundle="gde" key="gde.gesJud.observaciones.label"/>: </label></td>
			<td class="normal">
				<bean:write name="gesJud" property="observacion"/>
			</td>
		</tr>		
		
         <logic:notEmpty name="act">
         	<tr>         		      		
       			<logic:equal name="act" value="modificar">
       				<td colspan="4" align="right">
        				<logic:notEmpty name="modificarEncEnabled">
        					<logic:equal name="modificarEncEnabled" value="enabled">
							<html:button property="btnAccionModificarEnc"  styleClass="boton" onclick="submitForm('modificarEncabezado', '');">
			                     <bean:message bundle="base" key="abm.button.modificar"/>
                 				</html:button>
        					</logic:equal >
        					<logic:notEqual name="modificarEncEnabled" value="enabled">
							<html:button property="btnAccionModificarEnc" disabled="true" styleClass="boton" >
			                     <bean:message bundle="base" key="abm.button.modificar"/>
                 				</html:button>
        					</logic:notEqual >
        				</logic:notEmpty >
        			</td>	
       			</logic:equal>	         		
         	</tr>
         </logic:notEmpty >	
		</table>
		</fieldset>	
		<!-- GesJud -->
