<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarConstanciaDeu.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="gde" key="gde.constanciaDeuAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right">
    			<logic:equal name="constanciaDeuAdapterVO" property="act" value="verHistorico">
		    		<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverDeHistorico', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</logic:equal>
				<logic:notEqual name="constanciaDeuAdapterVO" property="act" value="verHistorico">				
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</logic:notEqual>
			</td>
		</tr>
	</table>

	 <!-- ConstanciaDeu -->		
	 	<bean:define id="act" name="constanciaDeuAdapterVO" property="act"/>
	 	<bean:define id="constanciaDeu" name="constanciaDeuAdapterVO" property="constanciaDeu"></bean:define>
		<%@ include file="/gde/adminDeuJud/includeConDeuViewDatos.jsp" %>
	 <!-- ConstanciaDeu -->

	<logic:equal name="constanciaDeuAdapterVO" property="act" value="verHistorico">
		<!-- historicos -->
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
			<caption><bean:message bundle="gde" key="gde.constanciaDeu.historicos.label"/></caption>
           	<tbody>
              	<tr>
					<th align="left"><bean:message bundle="gde" key="gde.constanciaDeu.estConDeu.fecha.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.constanciaDeu.estConDeu.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.constanciaDeu.observaciones.label"/></th>
				</tr>
				<logic:iterate id="historico" name="constanciaDeuAdapterVO" property="constanciaDeu.listHistEstConDeu">
					<tr>
						<td><bean:write name="historico" property="fechaDesdeView"/>&nbsp;</td>
						<td><bean:write name="historico" property="estConDeu.desEstConDeu"/>&nbsp;</td>
						<td><bean:write name="historico" property="logEstado"/>&nbsp;</td>
					</tr>
				</logic:iterate>
			</tbody>
		</table>
		<!-- fin historicos -->
	</logic:equal>
	
	<logic:notEqual name="constanciaDeuAdapterVO" property="act" value="verHistorico">
		<!-- Domicilio de envio -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.domicilio.envio.title"/></legend>
			<table class="tablabotones" width="100%">
				<tr>		
			    	<td><label><bean:message bundle="pad" key="pad.domicilio.label"/>: </label></td>
			    	<td class="normal"><bean:write name="constanciaDeuAdapterVO" property="constanciaDeu.desDomEnv"/></td>
			    </tr>
			</table>	    
	    </fieldset>
	      <!-- Fin Domicilio de Envio -->
	
		  <!-- Titulares -->	  			
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<logic:notEmpty  name="constanciaDeuAdapterVO" property="constanciaDeu.listConDeuTit">
							<caption><bean:message bundle="gde" key="gde.constanciaDeu.titulares.label"/></caption>
			               	<tbody>
				               	<tr>
									<th width="5%">&nbsp;</th> <!-- Ver/Seleccionar -->
									<th align="left" width="40%"><bean:message bundle="pad" key="pad.persona.nombreYApellido.label"/> / <bean:message bundle="pad" key="pad.persona.razonSocial.label"/></th>
									<th align="left" width="30%"><bean:message bundle="pad" key="pad.tipoNroDocumento.label"/></th>
									<th align="left" width="25%"><bean:message bundle="pad" key="pad.persona.cuit.label"/></th>									
								</tr>
									
								<logic:iterate id="ConDeuTitVO" name="constanciaDeuAdapterVO" property="constanciaDeu.listConDeuTit">
									<tr>
										<!-- Ver -->
										<td>
											<logic:equal name="constanciaDeuAdapterVO" property="verEnabled" value="enabled">									
												<a style="cursor: pointer; cursor: hand;" 
													onclick="submitForm('verTitular', '<bean:write name="ConDeuTitVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="constanciaDeuAdapterVO" property="verEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
											</logic:notEqual>
										</td>								
										<td><bean:write name="ConDeuTitVO" property="persona.represent"/>&nbsp;</td>
										<td><bean:write name="ConDeuTitVO" property="persona.documento.tipoyNumeroView"/>&nbsp;</td>
										<td><bean:write name="ConDeuTitVO" property="persona.cuit"/>&nbsp;</td>							
									</tr>
								</logic:iterate>
							</tbody>					
					</logic:notEmpty>
					<logic:empty name="constanciaDeuAdapterVO" property="constanciaDeu.listConDeuTit">
							<caption><bean:message bundle="base" key="base.noExistenRegitros"/></caption>
		                	<tbody>
								<tr><td align="center">
									<bean:message bundle="base" key="base.resultadoVacio"/>
								</td></tr>
							</tbody>									
					</logic:empty>
				</table>	
		  <!-- FIN Titulares -->
		  
		  <!-- Deudas incluidas -->	  			
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<logic:notEmpty  name="constanciaDeuAdapterVO" property="constanciaDeu.listConDeuDet">					
							<caption><bean:message bundle="gde" key="gde.constanciaDeu.deudasIncluidas.label"/></caption>
			               	<tbody>
				               	<tr>
									<th width="1">&nbsp;</th> <!-- Ver -->
									<th align="left"><bean:message bundle="gde" key="gde.deuda.label"/></th>
									<th align="left"><bean:message bundle="gde" key="gde.deuda.importe.label"/></th>
									<th align="left"><bean:message bundle="gde" key="gde.deuda.saldo.label"/></th>
									<th align="left"><bean:message bundle="gde" key="gde.deuda.fechaVencimiento.label"/></th>
									<th align="left"><bean:message bundle="gde" key="gde.constanciaDeu.conDeuDet.estado.label"/></th>
									<th align="left"><bean:message bundle="gde" key="gde.constanciaDeu.conDeuDet.observacion.label"/></th>
								</tr>
									
								<logic:iterate id="ConDeuDetVO" name="constanciaDeuAdapterVO" property="constanciaDeu.listConDeuDet">
									<tr>
										<!-- Ver -->
										<td>
											<logic:equal name="constanciaDeuAdapterVO" property="verEnabled" value="enabled">									
												<a style="cursor: pointer; cursor: hand;" 
													onclick="submitForm('verConDeuDet', '<bean:write name="ConDeuDetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="constanciaDeuAdapterVO" property="verEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
											</logic:notEqual>
										</td>																	
																					
										<td><bean:write name="ConDeuDetVO" property="deuda.nroBarraAnio"/>&nbsp;</td>
										<td><bean:write name="ConDeuDetVO" property="deuda.importeView"/>&nbsp;</td>
										<td><bean:write name="ConDeuDetVO" property="deuda.saldoView"/>&nbsp;</td>
										<td><bean:write name="ConDeuDetVO" property="deuda.fechaVencimientoView"/>&nbsp;</td>
										<td><bean:write name="ConDeuDetVO" property="estado.value"/>&nbsp;</td>
										<td><bean:write name="ConDeuDetVO" property="observacion"/>&nbsp;</td>								
									</tr>
								</logic:iterate>
							</tbody>
					</logic:notEmpty>			
					<logic:empty name="constanciaDeuAdapterVO" property="constanciaDeu.listConDeuDet">
						<caption><bean:message bundle="gde" key="gde.constanciaDeu.deudasIncluidas.label"/></caption>
	                	<tbody>
							<tr><td align="center"><bean:message bundle="base" key="base.noExistenRegitros"/></td></tr>
						</tbody>								
					</logic:empty>
				</table>	
		  <!-- FIN Deudas incluidas -->
		  
	  </logic:notEqual>
	  
	<table class="tablabotones" width="100%">
		<tr>
			<logic:equal name="constanciaDeuAdapterVO" property="act" value="verHistorico">
				<td align="left" width="50%">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverDeHistorico', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>				
			</logic:equal>
			<logic:notEqual name="constanciaDeuAdapterVO" property="act" value="verHistorico">				
				<td align="left" width="50%">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<logic:equal name="constanciaDeuAdapterVO" property="act" value="ver">
					<td align="right" width="50%">
		    			<html:button property="btnVerHist"  styleClass="boton" onclick="submitForm('verHistorico', '');">
							<bean:message bundle="gde" key="gde.constanciaDeuAdapter.button.historicos.label"/>
						</html:button>
					</td>
				</logic:equal>
				<logic:equal name="constanciaDeuAdapterVO" property="act" value="habilitar">
					<td align="right" width="50%">
		    			<html:button property="btnVerHist"  styleClass="boton" onclick="submitForm('habilitar', '');">
							<bean:message bundle="gde" key="gde.constanciaDeuAdapter.button.habilitar.label"/>
						</html:button>
					</td>
				</logic:equal>	
				<logic:equal name="constanciaDeuAdapterVO" property="act" value="recomponer">
					<td align="right" width="50%">
		    			<html:button property="btnVerHist"  styleClass="boton" onclick="submitForm('irRecomponer', '');">
							<bean:message bundle="gde" key="gde.constanciaDeuAdapter.button.recomponer.label"/>
						</html:button>
					</td>
				</logic:equal>						
				<logic:equal name="constanciaDeuAdapterVO" property="act" value="impresionConstancia">
					<td align="right" width="50%">
		    			<html:button property="btnVerHist"  styleClass="boton" onclick="submitForm('irImprimir', '');">
							<bean:message bundle="gde" key="gde.constanciaDeuAdapter.button.imprimir.label"/>
						</html:button>
					</td>
				</logic:equal>
				<logic:equal name="constanciaDeuAdapterVO" property="act" value="eliminar">
					<td align="right" width="50%">
		    			<html:button property="btnVerHist"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</td>
				</logic:equal>
				<logic:equal name="constanciaDeuAdapterVO" property="act" value="anular">
					<td align="right" width="50%">
		    			<html:button property="btnAnular"  styleClass="boton" onclick="submitForm('anular', '');">
							<bean:message bundle="gde" key="gde.constanciaDeuAdapter.button.anular"/>
						</html:button>
					</td>
				</logic:equal>					
					
			</logic:notEqual>
		</tr>
	</table>
   	
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
