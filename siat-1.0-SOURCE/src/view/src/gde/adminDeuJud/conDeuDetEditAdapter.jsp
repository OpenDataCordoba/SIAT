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
	 		<bean:define id="constanciaDeu" name="conDeuDetAdapterVO" property="conDeuDet.constanciaDeu"/>	 		
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
		
		<logic:equal name="conDeuDetAdapterVO" property="act" value="agregar">
			<!-- lista de deudas -->
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="gde" key="gde.constanciaDeu.deudasAIncluir.label"/></caption>
					<tbody>
						<logic:notEmpty  name="conDeuDetAdapterVO" property="listDeuda">          	
			               	<tr>
								<th width="1"><input type="checkbox" checked="checked" onclick="changeChk('filter', 'idsDeudaSelected', this)"/></th> <!-- Check -->
								<th align="left"><bean:message bundle="gde" key="gde.deuda.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.deuda.importe.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.deuda.saldo.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.deuda.fechaVencimiento.label"/></th>
							</tr>
								
							<logic:iterate id="deuJudVO" name="conDeuDetAdapterVO" property="listDeuda">
								<tr>
									<!-- check -->
									<td>
										<logic:equal name="deuJudVO" property="esValidaParaConstancia" value="true">										
											<html:multibox name="conDeuDetAdapterVO" property="idsDeudaSelected">
												<bean:write name="deuJudVO" property="idView"/>
											</html:multibox>
										</logic:equal>										
										<logic:notEqual name="deuJudVO" property="esValidaParaConstancia" value="true">										
											<input type="checkbox" disabled="disabled"/>
										</logic:notEqual>
									</td>																	
									<td><bean:write name="deuJudVO" property="nroBarraAnio"/>&nbsp;</td>
									<td><bean:write name="deuJudVO" property="importeView"/>&nbsp;</td>
									<td><bean:write name="deuJudVO" property="saldoView"/>&nbsp;</td>
									<td><bean:write name="deuJudVO" property="fechaVencimientoView"/>&nbsp;</td>
								</tr>
							</logic:iterate>
						</logic:notEmpty>
			
						<logic:empty name="conDeuDetAdapterVO" property="listDeuda">
							<tr><td align="center">
								<bean:message bundle="base" key="base.noExistenRegitros"/>
							</td></tr>			
						</logic:empty>
					</tbody>
				</table>
			<!-- FIN lista de deudas -->
		</logic:equal>
		
		<logic:equal name="conDeuDetAdapterVO" property="act" value="modificar">
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
					
					<!-- Servicio Banco -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.conDeuDet.deuda.servicioBanco.label"/>: </label></td>
						<td class="normal"><bean:write name="conDeuDetAdapterVO" property="conDeuDet.deuda.servicioBanco.desServicioBanco"/></td>
	
					<!-- -Estado -->	
						<td><label><bean:message bundle="gde" key="gde.conDeuDet.deuda.estado.label"/>: </label></td>
						<td class="normal"><bean:write name="conDeuDetAdapterVO" property="conDeuDet.deuda.estadoDeuda.desEstadoDeuda"/></td>					
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
						<td class="normal" colspan="3"><bean:write name="conDeuDetAdapterVO" property="conDeuDet.deuda.fechaVencimientoView"/></td>
					</tr>
	
					<!--  Observaciones -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.conDeuDet.observacion.label"/>: </label></td>
						<td class="normal" colspan="3"><html:textarea name="conDeuDetAdapterVO" property="conDeuDet.observacion" cols="85" rows="15"/></td>
					</tr>					
				</table>
			</fieldset>	
			<!-- ConDeuDet -->
		</logic:equal>	

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="conDeuDetAdapterVO" property="act" value="modificar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="conDeuDetAdapterVO" property="act" value="agregar">
						<logic:notEmpty  name="conDeuDetAdapterVO" property="listDeuda">   
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('agregar', '');">
								<bean:message bundle="base" key="abm.button.agregar"/>
							</html:button>
						</logic:notEmpty>
							
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
