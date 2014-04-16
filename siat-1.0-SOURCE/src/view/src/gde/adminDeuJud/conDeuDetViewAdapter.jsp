<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarConDeuDet.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.conDeuDetViewAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		<!-- ConstanciaDeu -->		
	 		<bean:define id="act" name="conDeuDetAdapterVO" property="act"/>	 		
	 		<bean:define id="constanciaDeu" name="conDeuDetAdapterVO" property="conDeuDet.constanciaDeu"></bean:define>
			<%@ include file="/gde/adminDeuJud/includeConDeuViewDatos.jsp" %>
	 	<!-- ConstanciaDeu -->
		
		<!-- Domicilio de envio -->
         <fieldset>
			<legend><bean:message bundle="pad" key="pad.domicilio.envio.title"/></legend>
			<tr>&nbsp </tr>
			<tr>
		    	<td><label><bean:message bundle="pad" key="pad.domicilio.label"/>: </label></td>
		    	<td class="normal"><bean:write name="conDeuDetAdapterVO" property="strDomEnv"/></td>
		    </tr>
	    </fieldset>
      	<!-- Fin Domicilio de Envio -->
		
		
		<!-- ConDeuDet -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.conDeuDet.title"/></legend>
			<table class="tabladatos">
				<!-- fecha Emision -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.conDeuDet.fechaEmision.label"/>: </label></td>
					<td class="normal"><bean:write name="conDeuDetAdapterVO" property="conDeuDet.deuda.fechaEmisionView"/></td>
				<!-- Recurso -->	
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="conDeuDetAdapterVO" property="conDeuDet.deuda.cuenta.recurso.desRecurso"/></td>					
				</tr>
				
				<!-- Via Deuda -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.conDeuDet.deuda.viaDeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="conDeuDetAdapterVO" property="conDeuDet.deuda.viaDeuda.desViaDeuda"/></td>
				<!-- Clasificacion -->	
					<td><label><bean:message bundle="gde" key="gde.conDeuDet.deuda.clasificacion.label"/>: </label></td>
					<td class="normal"><bean:write name="conDeuDetAdapterVO" property="conDeuDet.deuda.recClaDeu.desClaDeu"/></td>					
				</tr>				
				
				<!-- Importe -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.conDeuDet.deuda.importe.label"/>: </label></td>
					<td class="normal"><bean:write name="conDeuDetAdapterVO" property="conDeuDet.deuda.importeView"/></td>
				<!-- -Saldo -->	
					<td><label><bean:message bundle="gde" key="gde.conDeuDet.deuda.saldo.label"/>: </label></td>
					<td class="normal"><bean:write name="conDeuDetAdapterVO" property="conDeuDet.deuda.saldoView"/></td>
				</tr>
				
				<!--  fecha Vto -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.conDeuDet.deuda.fechaVencimiento.label"/>: </label></td>
					<td class="normal"><bean:write name="conDeuDetAdapterVO" property="conDeuDet.deuda.fechaVencimientoView"/></td>
				<!-- -Estado -->	
					<td><label><bean:message bundle="gde" key="gde.conDeuDet.deuda.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="conDeuDetAdapterVO" property="conDeuDet.deuda.estadoDeuda.desEstadoDeuda"/></td>					
					
				</tr>

				<!--  Observaciones -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.conDeuDet.observacion.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="conDeuDetAdapterVO" property="conDeuDet.observacion"/></td>
				</tr>					
			</table>
		</fieldset>			
		<!-- ConDeuDet -->

  		<!-- Conceptos -->	  			
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:message bundle="gde" key="gde.conDeuDet.deuda.listConceptos.label"/></caption>
               	<tbody>
					<logic:notEmpty  name="conDeuDetAdapterVO" property="conDeuDet.deuda.listDeuRecCon">									

		               	<tr>
							<th align="left"><bean:message bundle="gde" key="gde.conDeuDet.deuda.concepto.descripcion"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.conDeuDet.deuda.concepto.importe.label"/></th>							
						</tr>
							
						<logic:iterate id="deuJudRecConVO" name="conDeuDetAdapterVO" property="conDeuDet.deuda.listDeuRecCon">
							<tr>
								<td><bean:write name="deuJudRecConVO" property="descripcion"/>&nbsp;</td>
								<td><bean:write name="deuJudRecConVO" property="importe" />&nbsp;</td>														
							</tr>
						</logic:iterate>
					</logic:notEmpty>			
					<logic:empty name="conDeuDetAdapterVO" property="conDeuDet.deuda.listDeuRecCon">
							<tr><td align="center"><bean:message bundle="base" key="base.noExistenRegitros"/></td></tr>
					</logic:empty>
				</tbody>
			</table>	
		<!-- FIN Conceptos -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="conDeuDetAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="conDeuDetAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="conDeuDetAdapterVO" property="act" value="desactivar">
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
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
