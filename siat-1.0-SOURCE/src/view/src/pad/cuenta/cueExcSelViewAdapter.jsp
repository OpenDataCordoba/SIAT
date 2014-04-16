<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/AdministrarCueExcSel.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pad" key="pad.cueExcSelAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
			
		<!-- CueExcSel -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.cueExcSel.title"/></legend>
			<table class="tabladatos">
				<!-- Recurso -->
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cueExcSel.cuenta.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="cueExcSelAdapterVO" property="cueExcSel.cuenta.recurso.desRecurso"/></td>
				</tr>
				<!-- Cuenta -->
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cueExcSel.cuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="cueExcSelAdapterVO" property="cueExcSel.cuenta.numeroCuenta"/></td>
				</tr>
				<!-- Area -->
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cueExcSel.area.label"/>: </label></td>
					<td class="normal"><bean:write name="cueExcSelAdapterVO" property="cueExcSel.area.desArea"/></td>				
				</tr>
			</table>
		</fieldset>	
		<!-- CueExcSel -->
	
		<!-- CueExcSelDeu -->
		<logic:equal name="cueExcSelAdapterVO" property="act" value="ver">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="pad" key="pad.cueExcSel.listCueExcSelDeu.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="cueExcSelAdapterVO" property="cueExcSel.listCueExcSelDeu">
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Ver -->
							<th align="left"><bean:message bundle="pad" key="pad.cueExcSelDeu.deuda.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.cueExcSelDeu.deuda.fechaVencimiento.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.cueExcSelDeu.deuda.importe.label"/></th>
							<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
						</tr>
						<logic:iterate id="CueExcSelDeuVO" name="cueExcSelAdapterVO" property="cueExcSel.listCueExcSelDeu">
				
							<tr>
								<!-- Ver -->
								<td>
									<logic:equal name="CueExcSelDeuVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verCueExcSelDeu', '<bean:write name="CueExcSelDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									
									<logic:notEqual name="CueExcSelDeuVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="CueExcSelDeuVO" property="deuda.periodoView" />
								   /<bean:write name="CueExcSelDeuVO" property="deuda.anioView" />&nbsp;</td>							
								<td><bean:write name="CueExcSelDeuVO" property="deuda.fechaVencimientoView"/>&nbsp;</td>							
								<td><bean:write name="CueExcSelDeuVO" property="deuda.importeView" />&nbsp;</td>
								<td><bean:write name="CueExcSelDeuVO" property="estado.value" />&nbsp;</td>
																
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="cueExcSelAdapterVO" property="cueExcSel.listCueExcSelDeu">
						<tr><td align="center">
						<bean:message bundle="pad" key="pad.cueExcSelAdapter.noExistenRegistros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
			</table>
		</logic:equal>
		<!-- CueExcSelDeu -->
		
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="cueExcSelAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="cueExcSelAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="cueExcSelAdapterVO" property="act" value="desactivar">
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
