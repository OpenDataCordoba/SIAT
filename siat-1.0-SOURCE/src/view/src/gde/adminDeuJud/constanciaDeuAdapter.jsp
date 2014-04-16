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
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
	 <!-- ConstanciaDeu -->		
	 	<bean:define id="act" name="constanciaDeuAdapterVO" property="act"/>
	 	<bean:define id="constanciaDeu" name="constanciaDeuAdapterVO" property="constanciaDeu"/>
		<%@ include file="/gde/adminDeuJud/includeConDeuViewDatos.jsp" %>
	 <!-- ConstanciaDeu -->
				

	  <!-- Titulares -->  			
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="gde" key="gde.constanciaDeu.titulares.label"/></caption>
	               	<tbody>			
						<logic:notEmpty  name="constanciaDeuAdapterVO" property="constanciaDeu.listConDeuTit">
				               	<tr>
									<th width="5%">&nbsp;</th> <!-- Ver/Seleccionar -->
									<th width="5%">&nbsp;</th> <!-- Eliminar -->
									<th align="left" width="40%"><bean:message bundle="pad" key="pad.persona.nombreYApellido.label"/> / <bean:message bundle="pad" key="pad.persona.razonSocial.label"/></th>
									<th align="left"width="30%"><bean:message bundle="pad" key="pad.tipoNroDocumento.label"/></th>
									<th align="left" width="20%"><bean:message bundle="pad" key="pad.persona.cuit.label"/></th>									
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
										<!-- Eliminar -->
										<td>
											<logic:equal name="constanciaDeuAdapterVO" property="eliminarTitularEnabled" value="enabled">									
												<a style="cursor: pointer; cursor: hand;" 
													onclick="submitForm('eliminarTitular', '<bean:write name="ConDeuTitVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="constanciaDeuAdapterVO" property="eliminarTitularEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</td>	
										
										<td><bean:write name="ConDeuTitVO" property="persona.represent"/>&nbsp;</td>
										<td><bean:write name="ConDeuTitVO" property="persona.documento.tipoyNumeroView"/>&nbsp;</td>
										<td><bean:write name="ConDeuTitVO" property="persona.cuit"/>&nbsp;</td>							
									</tr>
								</logic:iterate>
						</logic:notEmpty>	
						<logic:empty name="constanciaDeuAdapterVO" property="constanciaDeu.listConDeuTit">							
									<tr><td align="center">
										<bean:message bundle="base" key="base.noExistenRegitros"/>
									</td></tr>						
						</logic:empty>

						<logic:equal name="constanciaDeuAdapterVO" property="agregarTitularEnabled" value="enabled">
							<tr>
								<td colspan="7" align="right">				
									<html:button property="btnAgregarTitular" onclick="submitForm('agregarTitular', '');">
										<bean:message bundle="base" key="abm.button.agregar"/>
									</html:button>
								</td>
							</tr>		
						</logic:equal>
					</tbody>
				</table>	
	  <!-- FIN Titulares -->
	  
	  <!-- Deudas incluidas -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
			<caption><bean:message bundle="gde" key="gde.constanciaDeu.deudasIncluidas.label"/></caption>
			<tbody>
				<logic:notEmpty  name="constanciaDeuAdapterVO" property="constanciaDeu.listConDeuDet">          	
	               	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
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
							
							<!-- Modificar -->
							<td>
								<logic:equal name="constanciaDeuAdapterVO" property="modificarConDeuDetEnabled" value="enabled">
									<logic:equal name="constanciaDeuAdapterVO" property="modificarConDeuDetBussEnabled" value="true">									
										<a style="cursor: pointer; cursor: hand;" 
											onclick="submitForm('modificarConDeuDet', '<bean:write name="ConDeuDetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="constanciaDeuAdapterVO" property="modificarConDeuDetBussEnabled" value="true">									
										<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>										
									</logic:notEqual>									
								</logic:equal>
								<logic:notEqual name="constanciaDeuAdapterVO" property="modificarConDeuDetEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
								
							<!-- Eliminar -->
							<td>
								<logic:equal name="constanciaDeuAdapterVO" property="eliminarConDeuDetEnabled" value="enabled">
									<logic:equal name="constanciaDeuAdapterVO" property="eliminarConDeuDetBussEnabled" value="true">								
										<a style="cursor: pointer; cursor: hand;" 
											onclick="submitForm('eliminarConDeuDet', '<bean:write name="ConDeuDetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="constanciaDeuAdapterVO" property="eliminarConDeuDetBussEnabled" value="true">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="constanciaDeuAdapterVO" property="eliminarConDeuDetEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
																		
							<td><bean:write name="ConDeuDetVO" property="deuda.periodoView"/>/<bean:write name="ConDeuDetVO" property="deuda.anioView"/>&nbsp;</td>
							<td><bean:write name="ConDeuDetVO" property="deuda.importeView" />&nbsp;</td>
							<td><bean:write name="ConDeuDetVO" property="deuda.saldoView"/>&nbsp;</td>
							<td><bean:write name="ConDeuDetVO" property="deuda.fechaVencimientoView"/>&nbsp;</td>
							<td><bean:write name="ConDeuDetVO" property="estado.value"/>&nbsp;</td>
							<td><bean:write name="ConDeuDetVO" property="observacion"/>&nbsp;</td>								
						</tr>
					</logic:iterate>
				</logic:notEmpty>
	
				<logic:empty name="constanciaDeuAdapterVO" property="constanciaDeu.listConDeuDet">
					<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>			
				</logic:empty>
	
				<logic:equal name="constanciaDeuAdapterVO" property="agregarConDeuDetEnabled" value="enabled">
					<bean:define id="agregarConDeuDetBussEnabled" name="constanciaDeuAdapterVO" property="agregarConDeuDetBussEnabled"/>
						<tr>
							<td colspan="9" align="right">				
								<input type="button" <%=agregarConDeuDetBussEnabled%>" onclick="submitForm('agregarConDeuDet', '');" value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
							</td>
						</tr>	
				</logic:equal>
			</tbody>
		</table>
 <!-- FIN Deudas incluidas -->
	  
		<table class="tablabotones">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
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
