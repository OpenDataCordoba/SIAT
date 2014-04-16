<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarPlaEnvDeuPro.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="gde" key="gde.plaEnvDeuProViewAdapter.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
	<!-- PlaEnvDeuPro -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.plaEnvDeuPro.title"/></legend>
		<table class="tabladatos">
		<!-- nroPlanilla -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.nroPlanilla.label"/>: </label></td>
			<td class="normal"><bean:write name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.nroPlanillaView" /></td>
						
		<!-- anioPlanilla -->
			<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.anioPlanilla.label"/>: </label></td>
			<td class="normal"><bean:write name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.anioPlanillaView"/></td>			
		</tr>
		
		<!-- procurador -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
			<td class="normal"><bean:write name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.procurador.descripcion"/></td>			
		
		<!-- estado -->
			<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.EstPlaEnvDeuPr.label"/>: </label></td>
			<td class="normal"><bean:write name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.estPlaEnvDeuPr.desEstPlaEnvDeuPro"/></td>			
		</tr>
		
		<!-- fechaEnvio -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.fechaEnvio.label"/>: </label></td>
			<td class="normal"><bean:write name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.fechaEnvioView"/></td>			
		
		<!-- fechaHabilitacion -->
			<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.fechaHabilitacion.label"/>: </label></td>
			<td class="normal"><bean:write name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.fechaHabilitacionView"/></td>			
		</tr>
		
		<!-- Inclucion de CasoView -->
		<tr>
			<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
			<td colspan="3">
				<bean:define id="IncludedVO" name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro"/>
				<%@ include file="/cas/caso/includeCasoView.jsp" %>				
			</td>
		</tr>
		<!-- Fin Inclucion de CasoView -->
		
		<tr>
			<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.observaciones.label"/>: </label></td>
			<td class="normal" colspan="3">
				<bean:write name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.observaciones"/>
			</td>
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- PlaEnvDeuPro -->

	<logic:notEqual name="plaEnvDeuProAdapterVO" property="act" value="habilitarConstancia">
		<!-- historicos -->
		<logic:equal name="plaEnvDeuProAdapterVO" property="act" value="ver">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:message bundle="gde" key="gde.plaEnvDeuPro.listHistoricos"/></caption>
	              	<tbody>				
				<logic:notEmpty name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.listHistEstPlaEnvDP">	
			               	<tr>
			               		<!-- <#ColumnTitles#> -->
								<th align="left"><bean:message bundle="gde" key="gde.plaEnvDeuPro.EstPlaEnvDeuPr.fecha.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.plaEnvDeuPro.EstPlaEnvDeuPr.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.plaEnvDeuPro.observaciones.label"/></th>							
							</tr>
								
							<logic:iterate id="estado" name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.listHistEstPlaEnvDP">
								<tr>
									<!-- <#ColumnFiedls#> -->
									<td><bean:write name="estado" property="fechaDesdeView"/>&nbsp;</td>
									<td><bean:write name="estado" property="estPlaEnvDeuPr.desEstPlaEnvDeuPro"/>&nbsp;</td>
									<td><bean:write name="estado" property="logEstado"/>&nbsp;</td>
								</tr>
							</logic:iterate>
																	
				</logic:notEmpty>			
				<logic:empty name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.listHistEstPlaEnvDP">
					<tr>
						<td align="center" colspan="3">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td>
					</tr>
				</logic:empty>
			</tbody>
		</table>	
	</logic:equal>
	<!-- FIN historicos -->
			
	<!-- constancias -->   
	<logic:equal name="plaEnvDeuProAdapterVO" property="act" value="verConstancias">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<logic:equal name="plaEnvDeuProAdapterVO" property="modoSeleccionar" value="true">
					<caption><bean:message bundle="gde" key="gde.plaEnvDeuPro.listConstanciasHabilitar"/></caption>
				</logic:equal>
				<logic:notEqual name="plaEnvDeuProAdapterVO" property="modoSeleccionar" value="true">
					<caption><bean:message bundle="gde" key="gde.plaEnvDeuPro.listConstancias"/></caption>
               	</logic:notEqual>
               	<tbody>						
				<logic:notEmpty name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.listConstanciaDeu">	
		               	<tr>
		               		<logic:equal name="plaEnvDeuProAdapterVO" property="modoSeleccionar" value="true">
		               			<th width="1">&nbsp;</th> <!-- Seleccionar para habilitar -->
		               			<th width="1">&nbsp;</th> <!-- Recomponer -->
		               			<th width="1">&nbsp;</th> <!-- Traspasar -->
		               		</logic:equal>	
		               		<th width="1">&nbsp;</th> <!-- Ver -->
		               		<!-- <#ColumnTitles#> -->
							<th align="left"><bean:message bundle="gde" key="gde.constanciaDeu.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.constanciaDeu.cuenta.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.constanciaDeu.recurso.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.constanciaDeu.estConDeu.label"/></th>
						</tr>
							
						<logic:iterate id="constancia" name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.listConstanciaDeu">
							<tr>
								<logic:equal name="plaEnvDeuProAdapterVO" property="modoSeleccionar" value="true">
								<!-- Seleccionar para habilitar -->
			               			<td>
			               				<logic:equal name="constancia" property="habilitarConstancia" value="true">						               			
				               				<html:multibox name="plaEnvDeuProAdapterVO" property="idsConstanciasHabilitar">
				               					<bean:write name="constancia" property="idView"/>
				               				</html:multibox>
			               				</logic:equal>
			               				<logic:equal name="constancia" property="habilitarConstancia" value="false">						               			
				               				<html:multibox name="plaEnvDeuProAdapterVO" property="idsConstanciasHabilitar" disabled="true" >
				               					<bean:write name="constancia" property="idView"/>
				               				</html:multibox>
			               				</logic:equal>						               				
			               			</td>
									
									<!-- Recomponer Constancia -->               			
			               			<td>
			               				<logic:equal name="plaEnvDeuProAdapterVO" property="recomponerConstanciaEnabled" value="enabled">
			               					<logic:equal name="constancia" property="recomponerConstanciaBussEnabled" value="true">						               			
  													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('recomponerConstancia', '<bean:write name="constancia" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="gde" key="gde.constanciaDeuSearchPage.button.recomponerConstancia"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/recomponerPlaEnvDeuPr1.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="constancia" property="recomponerConstanciaBussEnabled" value="true">						               			
  													<img title="<bean:message bundle="gde" key="gde.constanciaDeuSearchPage.button.recomponerConstancia"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/recomponerPlaEnvDeuPr1.gif"/>
											</logic:notEqual>
										</logic:equal>	
			               				<logic:notEqual name="plaEnvDeuProAdapterVO" property="recomponerConstanciaEnabled" value="enabled">
				               				<img title="<bean:message bundle="gde" key="gde.constanciaDeuSearchPage.button.recomponerConstancia"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/recomponerPlaEnvDeuPr1.gif"/>
			               				</logic:notEqual>
			               			</td>
			               			
									<!-- Trapasar Constancia -->               			
			               			<td>
			               				<logic:equal name="plaEnvDeuProAdapterVO" property="traspasarConstanciaEnabled" value="enabled">
			               					<logic:equal name="constancia" property="traspasarConstanciaBussEnabled" value="true">						               			
  													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('traspasarConstancia', '<bean:write name="constancia" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="gde" key="gde.plaEnvDeuProViewAdapter.button.traspasarConstancia"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/traspasarDeu0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="constancia" property="traspasarConstanciaBussEnabled" value="true">																			               		
  													<img title="<bean:message bundle="gde" key="gde.plaEnvDeuProViewAdapter.button.traspasarConstancia"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/traspasarDeu0.gif"/>
											</logic:notEqual>
										</logic:equal>	
			               				<logic:notEqual name="plaEnvDeuProAdapterVO" property="traspasarConstanciaEnabled" value="enabled">
				               				<img title="<bean:message bundle="gde" key="gde.plaEnvDeuProViewAdapter.button.traspasarConstancia"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/traspasarDeu0.gif"/>
			               				</logic:notEqual>
			               			</td>
			               									               			 
			               		</logic:equal>
								<!-- Ver -->
								<td>
									<logic:equal name="plaEnvDeuProAdapterVO" property="verEnabled" value="enabled">									
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verConstancia', '<bean:write name="constancia" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="plaEnvDeuProAdapterVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</td>	
															
								<!-- <#ColumnFiedls#> -->
								<td><bean:write name="constancia" property="numeroBarraAnioConstanciaView"/></td>
								<td><bean:write name="constancia" property="cuenta.numeroCuenta"/></td>
								<td>
									<!-- obtiene el recurso del proceso masivo o de la cuenta, si no tiene proceso masivo -->
									<logic:empty name="constancia" property="procesoMasivo.recurso.desRecurso">
										<bean:write name="constancia" property="cuenta.recurso.desRecurso"/>
									</logic:empty>
									<logic:notEmpty name="constancia" property="procesoMasivo.recurso.desRecurso">
										<bean:write name="constancia" property="procesoMasivo.recurso.desRecurso"/>
									</logic:notEmpty>
								<td><bean:write name="constancia" property="estConDeu.desEstConDeu"/></td>
							</tr>
						</logic:iterate>															
				</logic:notEmpty>			
				<logic:empty name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.listConstanciaDeu">
					<tr>
						<td align="center" colspan="5">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td>
					</tr>
				</logic:empty>
			</tbody>
		</table>
		
	</logic:equal>
	<!-- FIN constancias -->
	</logic:notEqual>
	
	<table class="tablabotones" width="100%">
    	<tr>
 	    		<td align="left" width="15%">
				<logic:equal name ="plaEnvDeuProAdapterVO" property="act" value="verConstancias">
					<logic:notEqual name="plaEnvDeuProAdapterVO" property="modoSeleccionar" value="true">		
			   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverDeVerConstancias', '');">
							<bean:message bundle="base" key="abm.button.volver"/>
						</html:button>
					</logic:notEqual>	
					<logic:equal name="plaEnvDeuProAdapterVO" property="modoSeleccionar" value="true">
			   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverDeHabilitarConstancias', '');">
								<bean:message bundle="base" key="abm.button.volver"/>
						</html:button>						
					</logic:equal>
				</logic:equal>	
				<logic:notEqual name ="plaEnvDeuProAdapterVO" property="act" value="verConstancias">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>					
				</logic:notEqual>
   	    	</td>
   	    	<td align="right" width="85%">
   	    	
   	    		<!--  Si esta en la opcion RECOMPONER PLANILLA -->
				<logic:equal name="plaEnvDeuProAdapterVO" property="act" value="recomponerPlanilla">
					<logic:equal name="plaEnvDeuProAdapterVO" property="recomponerPlanillaEnabled" value="enabled">
						<html:button property="btnAccionBase" styleClass="boton" onclick="submitForm('irRecomponerPlanilla', '');">
							<bean:message bundle="gde" key="gde.plaEnvDeuProViewAdapter.button.recomponerPlanilla"/>
						</html:button>
					</logic:equal>
				</logic:equal>
					   	    		
   	    		<!--  si esta en la opcion VER -->
				<logic:equal name="plaEnvDeuProAdapterVO" property="act" value="ver">
					<logic:equal name="plaEnvDeuProAdapterVO" property="verConstanciasEnabled" value="enabled">						
						<html:button property="btnAccionBase" styleClass="boton" onclick="submitForm('verConstancias', '');">
							<bean:message bundle="gde" key="gde.plaEnvDeuProViewAdapter.button.verConstancias"/>
						</html:button>
					</logic:equal>
				</logic:equal>
				
				<!--  si esta en la opcion VER CONSTANCIAS -->
				<logic:equal name="plaEnvDeuProAdapterVO" property="act" value="verConstancias">
					
					<logic:equal name="plaEnvDeuProAdapterVO" property="modoSeleccionar" value="true">
						<!-- esta en HABILITAR CONSTANCIAS -->
						<logic:equal name="plaEnvDeuProAdapterVO" property="habilitarConstanciasDeudaEnabled" value="enabled">
							<html:button property="btnHbaConstDeuda" styleClass="boton" onclick="submitForm('habilitarConstanciasDeuda', '');">
								<bean:message bundle="gde" key="gde.plaEnvDeuProViewAdapter.button.habilitar"/>									
							</html:button>						
						</logic:equal>
						<script>chkAll(document.forms['filter'].idsConstanciasHabilitar)</script>
					</logic:equal>
						
				</logic:equal>
				
				<!-- si esta en la opcion HABILITAR PLANILLA -->
				<logic:equal name="plaEnvDeuProAdapterVO" property="act" value="habilitarPlanilla">
							
					<logic:equal name="plaEnvDeuProAdapterVO" property="recomponerPlanillaEnabled" value="enabled">
						<input type="button" "<bean:write name="plaEnvDeuProAdapterVO" property="btnRecomponerPlanillaBussEnabled"/>"
									 value="<bean:message bundle="gde" key="gde.plaEnvDeuProViewAdapter.button.recomponerPlanilla"/>" 
									 class="boton" onclick="submitForm('irRecomponerPlanilla', '');"/>							
					</logic:equal>
															
					<logic:equal name="plaEnvDeuProAdapterVO" property="habilitarPlanillaEnabled" value="enabled">
						<input type="button" "<bean:write name="plaEnvDeuProAdapterVO" property="btnHabilitarPlanillaBussEnabled"/>"
									class="boton" onclick="submitForm('habilitarPlanilla', '');"
									value="<bean:message bundle="gde" key="gde.plaEnvDeuProViewAdapter.button.habilitarPlanilla"/>"/>					
					</logic:equal>
					
					<logic:equal name="plaEnvDeuProAdapterVO" property="habilitarConstanciasDeudaEnabled" value="enabled">
						<input type="button" "<bean:write name="plaEnvDeuProAdapterVO" property="btnHabilitarConstanciasBussEnabled"/>"
							value="<bean:message bundle="gde" key="gde.plaEnvDeuProViewAdapter.button.habilitarConstancias"/>" 
							class="boton" onclick="submitForm('irHabilitarConstanciasDeuda', '');"/>					
					</logic:equal>						
				</logic:equal>					

				<!--  -->					
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
