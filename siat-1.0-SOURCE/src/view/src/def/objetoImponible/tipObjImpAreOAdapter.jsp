<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarTipObjImp.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.tipObjImpAreOAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- TipObjImp -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.tipObjImp.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.tipObjImp.codTipObjImp.label"/>: </label></td>
					<td class="normal"><bean:write name="tipObjImpAdapterVO" property="tipObjImp.codTipObjImp"/></td>
					
					<td><label><bean:message bundle="def" key="def.tipObjImp.desTipObjImp.label"/>: </label></td>
					<td class="normal"><bean:write name="tipObjImpAdapterVO" property="tipObjImp.desTipObjImp" /></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.tipObjImp.esSiat.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="tipObjImpAdapterVO" property="tipObjImp.esSiat.value"/></td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="def" key="def.tipObjImp.fechaAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="tipObjImpAdapterVO" property="tipObjImp.fechaAltaView"/></td>
					<td><label><bean:message bundle="def" key="def.tipObjImp.fechaBaja.label"/>: </label></td>
					<td class="normal"><bean:write name="tipObjImpAdapterVO" property="tipObjImp.fechaBajaView" /></td>
				</tr>
			</table>
		</fieldset>	
		<!-- Fin TipObjImp -->
		<!-- TipObjImpAreO -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="def" key="def.tipObjImp.listTipObjImpAreO.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="tipObjImpAdapterVO" property="tipObjImp.listTipObjImpAreO">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th width="1">&nbsp;</th> <!-- Activar Desactivar -->
						<th align="left"><bean:message bundle="def" key="def.area.codArea.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.area.desArea.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.area.estado.label"/></th>						
					</tr>
					<logic:iterate id="TipObjImpAreOVO" name="tipObjImpAdapterVO" property="tipObjImp.listTipObjImpAreO">
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="tipObjImpAdapterVO" property="verTipObjImpAreOEnabled" value="enabled">
									<logic:equal name="TipObjImpAreOVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verTipObjImpAreO', '<bean:write name="TipObjImpAreOVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="TipObjImpAreOVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="tipObjImpAdapterVO" property="verTipObjImpAreOEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="tipObjImpAdapterVO" property="eliminarTipObjImpAreOEnabled" value="enabled">
									<logic:equal name="TipObjImpAreOVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarTipObjImpAreO', '<bean:write name="TipObjImpAreOVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="TipObjImpAreOVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="tipObjImpAdapterVO" property="eliminarTipObjImpAreOEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>

							<td>
								<!-- Activar -->
								<logic:equal name="TipObjImpAreOVO" property="estado.value" value="Inactivo">
									<logic:equal name="tipObjImpAdapterVO" property="activarTipObjImpAreOEnabled" value="enabled">
										<logic:equal name="TipObjImpAreOVO" property="activarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('activarTipObjImpAreO', '<bean:write name="TipObjImpAreOVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.activar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/activar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="TipObjImpAreOVO" property="activarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="tipObjImpAdapterVO" property="activarTipObjImpAreOEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
									</logic:notEqual>
								</logic:equal> 
								<!-- Desactivar -->
								<logic:equal name="TipObjImpAreOVO" property="estado.value" value="Activo">
									<logic:equal name="tipObjImpAdapterVO" property="desactivarTipObjImpAreOEnabled" value="enabled">
										<logic:equal name="TipObjImpAreOVO" property="desactivarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('desactivarTipObjImpAreO', '<bean:write name="TipObjImpAreOVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.desactivar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="TipObjImpAreOVO" property="desactivarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="tipObjImpAdapterVO" property="desactivarTipObjImpAreOEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
									</logic:notEqual>
								</logic:equal> 
							</td>
							
							<td><bean:write name="TipObjImpAreOVO" property="areaOrigen.codArea"/>&nbsp;</td>
							<td><bean:write name="TipObjImpAreOVO" property="areaOrigen.desArea"/>&nbsp;</td>
							<td><bean:write name="TipObjImpAreOVO" property="estado.value" />&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="tipObjImpAdapterVO" property="tipObjImp.listTipObjImpAreO">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
		</table>
		<!-- TipObjImpAreO -->
				
		<table class="tablabotones">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
   	    			<td align="right">
   	    				<bean:define id="agregarEnabled" name="tipObjImpAdapterVO" property="agregarTipObjImpAreOEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarTipObjImpAreO', '<bean:write name="tipObjImpAdapterVO" property="tipObjImp.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
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