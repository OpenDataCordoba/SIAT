<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarOrdConBasImp.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="ef" key="ef.ordConBasImpEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- OrdConBasImp -->
					
		<!-- CompFuenteRes -->	
		<fieldset id="seccionCompFuenteRes">
			<legend><bean:message bundle="ef" key="ef.ordConBasImpAdapter.origenFuente.label"/></legend>
			
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
		    	<tbody>
						    	
				    	<tr>
							<th width="1">&nbsp;</th> <!-- seleccionar -->
							<th align="left"><bean:message bundle="ef" key="ef.comparacion.descripcion.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.compFuenteRes.operacion.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.compFuenteRes.diferencia.label"/></th>						
						</tr>
						
						<tr>
							
							<!-- seleccionar-->								
							<td>								
								<html:radio name="ordConBasImpAdapterVO" property="idCompFuenteResSelec"
									 value="-1" onchange="submitForm('paramCompFuenteRes', '');"/>
							</td>
							
							<td><bean:message bundle="ef" key="ef.plaFueDat.legend"/></td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>						
						
						<logic:iterate id="CompFuenteResVO" name="ordConBasImpAdapterVO" property="listCompFuenteRes">
				
							<tr>
								
								<!-- seleccionar-->								
								<td>
									<bean:define name="CompFuenteResVO" property="idView" id="idCompFuenteResVO"/>
									<html:radio name="ordConBasImpAdapterVO" property="idCompFuenteResSelec"
										 value="<%=idCompFuenteResVO.toString()%>"
										  onchange="submitForm('paramCompFuenteRes', '');"/>									
								</td>
								
								<td><bean:write name="CompFuenteResVO" property="comparacion.descripcion"/></td>
								<td><bean:write name="CompFuenteResVO" property="operacion"/></td>
								<td>$&nbsp;<bean:write name="CompFuenteResVO" property="diferenciaView"/></td>
							</tr>
						</logic:iterate>	
				</tbody>
			</table>
		</fieldset>	
		<!-- CompFuenteRes -->
		
		<!-- fuentes -->
		<logic:notEmpty name="ordConBasImpAdapterVO" property="listCompFuente">
			<fieldset id="seccionFuentes">
				<legend><bean:message bundle="ef" key="ef.ordConBasImpAdapter.selecFuentes.label"/></legend>
				
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			    	<tbody>
							    	
					    	<tr>
								<th width="1">&nbsp;</th> <!-- seleccionar -->
								<th align="left"><bean:message bundle="ef" key="ef.fuenteInfo.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.comparacion.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.ordConBasImpAdapter.fechaDesde.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.ordConBasImpAdapter.fechaHasta.label"/></th>
								<th align="left"><bean:message bundle="ef" key="ef.comparacion.total.label"/></th>						
							</tr>
							
							<logic:iterate id="CompFuenteVO" name="ordConBasImpAdapterVO" property="listCompFuente">
					
								<tr>
																	
										<!-- seleccionar-->								
									<logic:greaterThan name="ordConBasImpAdapterVO" property="idCompFuenteResSelec" value="0">
										<td>
											<logic:equal name="CompFuenteVO" property="plaFueDat.fuenteInfo.tipoPeriodicidad.id" value="0">				                       	        
				                       	        
				                       	        <bean:define name="CompFuenteVO" property="idView" id="idCompFuente"/>
												<html:radio name="ordConBasImpAdapterVO" property="idSelecFuente"
													 value="<%=idCompFuente.toString()%>"
													  onchange="submitForm('paramSelecFuente', '');"/>
												 
											</logic:equal>
											<logic:notEqual name="CompFuenteVO" property="plaFueDat.fuenteInfo.tipoPeriodicidad.id" value="0">
												<input type="radio" disabled="disabled"/>
											</logic:notEqual>
										</td>
									
										<td><bean:write name="CompFuenteVO" property="plaFueDat.tituloView"/></td>
										<td><bean:write name="CompFuenteVO" property="comparacion.descripcion"/></td>
										<td><bean:write name="CompFuenteVO" property="periodoDesde4View"/></td>
										<td><bean:write name="CompFuenteVO" property="periodoHasta4View"/></td>
										<td>$&nbsp;<bean:write name="CompFuenteVO" property="totalView"/></td>									
									</logic:greaterThan>
									
									<logic:lessThan name="ordConBasImpAdapterVO" property="idCompFuenteResSelec" value="0">
										<!-- seleccionar-->								
										<td>
											<logic:equal name="CompFuenteVO" property="plaFueDat.fuenteInfo.tipoPeriodicidad.id" value="0">				                       	        
				                       	        
				                       	        <bean:define name="CompFuenteVO" property="plaFueDat.idView" id="idPlaFueDat"/>
												<html:radio name="ordConBasImpAdapterVO" property="idSelecFuente"
													 value="<%=idPlaFueDat.toString()%>"
													  onchange="submitForm('paramSelecFuente', '');"/>
												 
											</logic:equal>
											<logic:notEqual name="CompFuenteVO" property="plaFueDat.fuenteInfo.tipoPeriodicidad.id" value="0">
												<input type="radio" disabled="disabled"/>
											</logic:notEqual>																					
										</td>
									
										<td><bean:write name="CompFuenteVO" property="plaFueDat.fuenteInfo.nombreFuente"/></td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>										
									</logic:lessThan>
								</tr>
							</logic:iterate>	
					</tbody>
				</table>
			</fieldset>			
		</logic:notEmpty>
		<!-- FIN fuentes -->	
		
		<!-- campos vigencia -->
		<logic:equal name="ordConBasImpAdapterVO" property="verCamposVigencia" value="true">
			<fieldset id="seccionVigencia">
				<legend><bean:message bundle="ef" key="ef.ordConBasImpAdapter.vigencia.legend"/></legend>
				
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			    	<tbody>
			    		<tr>
			    			<td align="right"><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
			    			<td class="normal">
			    				<html:select name="ordConBasImpAdapterVO" property="ordConBasImp.ordConCue.id">
			    					<html:optionsCollection name="ordConBasImpAdapterVO" property="listOrdConCue" label="cuenta.recursoCuentaView" value="id"/>
			    				</html:select>
			    			</td>
			    		</tr>
						<tr>
							<td align="right"><label>(*)&nbsp;<bean:message bundle="ef" key="ef.compFuente.periodoDesde.label"/>: </label></td>
							<td class="normal">
								<html:text name="ordConBasImpAdapterVO" property="ordConBasImp.compFuente.periodoDesde" size="2" maxlength="2"/>
								&nbsp;/&nbsp;
				
						<!-- anioDesde -->
								<html:text name="ordConBasImpAdapterVO" property="ordConBasImp.compFuente.anioDesde" size="4" maxlength="4"/>
								&nbsp;(mm/aaaa)				
							</td>			
						</tr>
				
						<!-- periodoHasta -->
						<tr>
							<td align="right"><label>(*)&nbsp;<bean:message bundle="ef" key="ef.compFuente.periodoHasta.label"/>: </label></td>
							<td class="normal">
								<html:text name="ordConBasImpAdapterVO" property="ordConBasImp.compFuente.periodoHasta" size="2" maxlength="2"/>
								&nbsp;/&nbsp;				
						
						<!-- anioHasta -->						
								<html:text name="ordConBasImpAdapterVO" property="ordConBasImp.compFuente.anioHasta" size="4" maxlength="4"/>
								&nbsp;(mm/aaaa)			
							</td>			
						</tr>							    	
					</tbody>
				</table>
			</fieldset>					
		</logic:equal>
		
	<!-- OrdConBasImp -->
	
	<table class="tablabotones">
	   	<tr>
  	   		<td align="left">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="left">
				<logic:equal name="ordConBasImpAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="ordConBasImpAdapterVO" property="act" value="agregar">
					<logic:equal name="ordConBasImpAdapterVO" property="verCamposVigencia" value="true">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
							<bean:message bundle="base" key="abm.button.agregar"/>
						</html:button>
					</logic:equal>	
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

		<logic:present name="irA">
			<script type="text/javascript">document.location = document.URL + '#<bean:write name="irA"/>';</script>
		</logic:present>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
<!-- ordConBasImpEditAdapter.jsp -->