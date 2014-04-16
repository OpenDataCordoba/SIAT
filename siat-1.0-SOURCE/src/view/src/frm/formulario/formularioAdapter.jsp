<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/frm/AdministrarFormulario.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="frm" key="frm.formularioAdapter.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
			
		<!-- Formulario -->
		<fieldset>
			<legend><bean:message bundle="frm" key="frm.formulario.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="frm" key="frm.formulario.codFormulario.label"/>: </label></td>
					<td class="normal"><bean:write name="formularioAdapterVO" property="formulario.codFormulario"/></td>
					
					<td><label><bean:message bundle="frm" key="frm.formulario.desFormulario.label"/>: </label></td>
					<td class="normal"><bean:write name="formularioAdapterVO" property="formulario.desFormulario" /></td>
				</tr>
				<tr>
					<td ><label><bean:message bundle="frm" key="frm.desImp.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="formularioAdapterVO" property="formulario.desImp.desDesImp"/></td>
				</tr>										
				<tr>
 					<td colspan="4"> 
						<bean:define id="modificarEncabezadoEnabled" name="formularioAdapterVO" property="modificarEncabezadoEnabled"/>
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
							'<bean:write name="formularioAdapterVO" property="formulario.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>		
					</td>
				</tr>
			</table>
		</fieldset>	
		<!-- Formulario -->
		
		<!-- ForCam -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="frm" key="frm.formulario.listForCam.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="formularioAdapterVO" property="formulario.listForCam">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="frm" key="frm.forCam.codForCam.label"/></th>
						<th align="left"><bean:message bundle="frm" key="frm.forCam.desForCam.label"/></th>
					</tr>
					<logic:iterate id="ForCamVO" name="formularioAdapterVO" property="formulario.listForCam">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="formularioAdapterVO" property="verForCamEnabled" value="enabled">
									<logic:equal name="ForCamVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verForCam', '<bean:write name="ForCamVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="ForCamVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="formularioAdapterVO" property="verEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Modificar-->								
							<td>
								<logic:equal name="formularioAdapterVO" property="modificarForCamEnabled" value="enabled">
									<logic:equal name="ForCamVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarForCam', '<bean:write name="ForCamVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="ForCamVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="formularioAdapterVO" property="modificarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="formularioAdapterVO" property="eliminarForCamEnabled" value="enabled">
									<logic:equal name="ForCamVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarForCam', '<bean:write name="ForCamVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="ForCamVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="formularioAdapterVO" property="eliminarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="ForCamVO" property="codForCam"/>&nbsp;</td>
							<td><bean:write name="ForCamVO" property="desForCam"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="formularioAdapterVO" property="formulario.listForCam">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="formularioAdapterVO" property="agregarForCamEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarForCam', '<bean:write name="formularioAdapterVO" property="formulario.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
			</tbody>
		</table>
		<!-- ForCam -->
				
		<!-- XSL y XML -->
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<tbody>
	    		<tr>
	    			<td><bean:message bundle="frm" key="frm.formulario.xsl.label"/>:</td>
	    			<td><html:textarea style="height:300px;width:600px" name="formularioAdapterVO" property="formulario.xsl"></html:textarea></td>
	    		</tr>
	    		<tr>
	    			<td><bean:message bundle="frm" key="frm.formulario.xslTxt.label"/>:</td>
	    			<td><html:textarea style="height:300px;width:600px" name="formularioAdapterVO" property="formulario.xslTxt"></html:textarea></td>
	    		</tr>
	    		<tr>
	    			<td><bean:message bundle="frm" key="frm.formulario.xml.label"/>:</td>
	    			<td><html:textarea style="height:300px;width:600px" name="formularioAdapterVO" property="formulario.xmlTest" cols="80" rows="15" />
	    				<br>
						<html:select name="formularioAdapterVO" property="idFormatoSalidaSelec" styleClass="select">
							<html:optionsCollection name="formularioAdapterVO" property="listFormatoSalida" label="value" value="id" />
						</html:select>
						&nbsp;
		    			<html:button property="btnTest"  styleClass="boton" onclick="submitForm('irTest', '');">
							<bean:message bundle="frm" key="frm.button.test"/>
						</html:button>

	    			</td>
	    		</tr>
			<tr>				
				<td align="right" colspan="2">
	    			<html:button property="btnModificar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</td>
			</tr>			
			</tbody>
		</table>
		
		<!-- abre la ventana para el test del formulario -->
		<logic:present name="abrirPopUpTest" scope="request">
			<script>
				window.open('<%=request.getContextPath()%>/frm/AdministrarFormulario.do?method=test','_blank');
			</script>
		</logic:present>	
		
		<!-- Fin XSL y XML -->
		
		
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
