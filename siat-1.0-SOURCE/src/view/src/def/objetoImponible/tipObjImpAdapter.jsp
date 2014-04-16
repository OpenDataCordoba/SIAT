<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarTipObjImp.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.tipObjImpAdapter.title"/></h1>	
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
					<td class="normal" colspan="1"><bean:write name="tipObjImpAdapterVO" property="tipObjImp.esSiat.value"/></td>
					
					<logic:equal name="tipObjImpAdapterVO" property="tipObjImp.esSiat.value" value="No">
	           			<td><label><bean:message bundle="def" key="def.tipObjImp.proceso.label"/>: </label></td>
	               		<td class="normal"><bean:write name="tipObjImpAdapterVO" property="tipObjImp.proceso.codProceso" /></td> 	                                        					
 		 		 	</logic:equal>	
					
				</tr>				
				<tr>
					<td><label><bean:message bundle="def" key="def.tipObjImp.fechaAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="tipObjImpAdapterVO" property="tipObjImp.fechaAltaView"/></td>
					<td><label><bean:message bundle="def" key="def.tipObjImp.fechaBaja.label"/>: </label></td>
					<td class="normal"><bean:write name="tipObjImpAdapterVO" property="tipObjImp.fechaBajaView" /></td>
				</tr>
			</table>
			<p align="right">
				<bean:define id="modificarEncabezadoEnabled" name="tipObjImpAdapterVO" property="modificarEncabezadoEnabled"/>
				<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
					'<bean:write name="tipObjImpAdapterVO" property="tipObjImp.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.modificar"/>"/>	
			</p>
		</fieldset>	
		<!-- TipObjImp -->
		<!-- TipObjImpAtr -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="def" key="def.tipObjImp.listTipObjImpAtr.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="tipObjImpAdapterVO" property="tipObjImp.listTipObjImpAtr">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="def" key="def.atributo.codAtributo.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.atributo.desAtributo.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.tipObjImpAtr.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.tipObjImpAtr.fechaHasta.label"/></th>							
					</tr>
					<logic:iterate id="TipObjImpAtrVO" name="tipObjImpAdapterVO" property="tipObjImp.listTipObjImpAtr">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="tipObjImpAdapterVO" property="verTipObjImpAtrEnabled" value="enabled">
									<logic:equal name="TipObjImpAtrVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verTipObjImpAtr', '<bean:write name="TipObjImpAtrVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="TipObjImpAtrVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="tipObjImpAdapterVO" property="verTipObjImpAtrEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Modificar-->								
							<td>
								<logic:equal name="tipObjImpAdapterVO" property="modificarTipObjImpAtrEnabled" value="enabled">
									<logic:equal name="TipObjImpAtrVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarTipObjImpAtr', '<bean:write name="TipObjImpAtrVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="TipObjImpAtrVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="tipObjImpAdapterVO" property="modificarTipObjImpAtrEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="tipObjImpAdapterVO" property="eliminarTipObjImpAtrEnabled" value="enabled">
									<logic:equal name="TipObjImpAtrVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarTipObjImpAtr', '<bean:write name="TipObjImpAtrVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="TipObjImpAtrVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="tipObjImpAdapterVO" property="eliminarTipObjImpAtrEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="TipObjImpAtrVO" property="atributo.codAtributo"/>&nbsp;</td>
							<td><bean:write name="TipObjImpAtrVO" property="atributo.desAtributo" />&nbsp;</td>
							<td><bean:write name="TipObjImpAtrVO" property="fechaDesdeView" />&nbsp;</td>
							<td><bean:write name="TipObjImpAtrVO" property="fechaHastaView" />&nbsp;</td>								
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="tipObjImpAdapterVO" property="tipObjImp.listTipObjImpAtr">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
		</table>
		<!-- TipObjImpAtr -->
				
		<table class="tablabotones">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
   	    			<td align="right">
   	    				<bean:define id="agregarEnabled" name="tipObjImpAdapterVO" property="agregarTipObjImpAtrEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarTipObjImpAtr', '<bean:write name="tipObjImpAdapterVO" property="tipObjImp.id" bundle="base" formatKey="general.format.id"/>');" 
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