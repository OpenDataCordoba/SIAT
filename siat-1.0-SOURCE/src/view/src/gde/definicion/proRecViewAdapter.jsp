<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarProRec.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.proRecViewAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- ProRec -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.proRec.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procurador.descripcion.ref"/>: </label></td>
					<td class="normal"><bean:write name="proRecAdapterVO" property="proRec.procurador.descripcion"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procurador.domicilio.ref"/>: </label></td>
					<td class="normal"><bean:write name="proRecAdapterVO" property="proRec.procurador.domicilio"/></td>

					<td><label><bean:message bundle="gde" key="gde.procurador.telefono.ref"/>: </label></td>
					<td class="normal"><bean:write name="proRecAdapterVO" property="proRec.procurador.telefono"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procurador.horarioAtencion.ref"/>: </label></td>
					<td class="normal"><bean:write name="proRecAdapterVO" property="proRec.procurador.horarioAtencion"/></td>

					<td><label><bean:message bundle="gde" key="gde.procurador.tipoProcurador.ref"/>: </label></td>
					<td class="normal"><bean:write name="proRecAdapterVO" property="proRec.procurador.tipoProcurador.desTipoProcurador"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procurador.observacion.ref"/>: </label></td>
					<td class="normal"><bean:write name="proRecAdapterVO" property="proRec.procurador.observacion"/></td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="gde" key="gde.proRec.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="proRecAdapterVO" property="proRec.recurso.desRecurso"/></td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.proRec.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="proRecAdapterVO" property="proRec.fechaDesdeView"/></td>				

					<td><label><bean:message bundle="gde" key="gde.proRec.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="proRecAdapterVO" property="proRec.fechaHastaView"/></td>				
				</tr>
			</table>
		</fieldset>	
		<!-- ProRec -->
		
		<!-- ProRecDesHas -->		
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="gde" key="gde.proRec.listProRecDesHas.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="proRecAdapterVO" property="proRec.listProRecDesHas">
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Ver -->
							<th align="left"><bean:message bundle="gde" key="gde.proRecDesHas.desde.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.proRecDesHas.hasta.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.proRecDesHas.fechaDesde.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.proRecDesHas.fechaHasta.label"/></th>
						</tr>
						<logic:iterate id="ProRecDesHasVO" name="proRecAdapterVO" property="proRec.listProRecDesHas">
				
							<tr>
								<!-- Ver -->
								<td>
									<logic:equal name="ProRecDesHasVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verProRecDesHas', '<bean:write name="ProRecDesHasVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									
									<logic:notEqual name="ProRecDesHasVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="ProRecDesHasVO" property="desde" />&nbsp;</td>
								<td><bean:write name="ProRecDesHasVO" property="hasta" />&nbsp;</td>	
								<td><bean:write name="ProRecDesHasVO" property="fechaDesdeView" />&nbsp;</td>
								<td><bean:write name="ProRecDesHasVO" property="fechaHastaView" />&nbsp;</td>	
						   </tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="proRecAdapterVO" property="proRec.listProRecDesHas">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
			</table>
		<!-- ProRecDesHas -->
		
		<!-- ProRecCom -->		
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="gde" key="gde.proRec.listProRecCom.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="proRecAdapterVO" property="proRec.listProRecCom">
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Ver -->
							<th align="left"><bean:message bundle="gde" key="gde.proRecCom.fecVtoDeuDes.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.proRecCom.fecVtoDeuHas.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.proRecCom.porcentajeComision.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.proRecCom.fechaDesde.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.proRecCom.fechaHasta.label"/></th>
						</tr>
						<logic:iterate id="ProRecComVO" name="proRecAdapterVO" property="proRec.listProRecCom">
				
							<tr>
								<!-- Ver -->
								<td>
									<logic:equal name="ProRecComVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verProRecCom', '<bean:write name="ProRecComVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									
									<logic:notEqual name="ProRecComVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="ProRecComVO" property="fecVtoDeuDesView" />&nbsp;</td>
								<td><bean:write name="ProRecComVO" property="fecVtoDeuHasView" />&nbsp;</td>	
								<td><bean:write name="ProRecComVO" property="porcentajeComisionView" />&nbsp;</td>
								<td><bean:write name="ProRecComVO" property="fechaDesdeView" />&nbsp;</td>
								<td><bean:write name="ProRecComVO" property="fechaHastaView" />&nbsp;</td>
						   </tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="proRecAdapterVO" property="proRec.listProRecCom">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
			</table>
		<!-- ProRecCom -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="proRecAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="proRecAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="proRecAdapterVO" property="act" value="desactivar">
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
