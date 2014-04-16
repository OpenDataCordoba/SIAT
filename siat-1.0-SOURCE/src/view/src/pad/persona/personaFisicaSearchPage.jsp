<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	    
   	    
	function submitFormContribuyente(method, selectedId, esContribuyente) {
		var form = document.getElementById('filter');
		form.elements["esContribuyente"].value = esContribuyente;
		submitForm(method, selectedId);
	}   	    
</script>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/BuscarPersona.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="pad" key="pad.personaSearchPage.title"/></h1>	
		
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="pad" key="pad.personaSearchPage.legend"/></p>				
			</td>				
			<td align="right">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
	<!-- Filtro -->
	<fieldset>
	<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="pad" key="pad.persona.tipoPersona.label"/>: </label></td>
				<td class="normal"><html:radio name="personaSearchPageVO" property="persona.tipoPersona" value="F" onclick="submitForm('paramTipoPersona', '');limpiaResultadoFiltro();"/>Persona F&iacute;sica</td>
				<td class="normal" colspan="2"><html:radio name="personaSearchPageVO" property="persona.tipoPersona" value="J" onclick="submitForm('paramTipoPersona', '');limpiaResultadoFiltro();"/>Persona Jur&iacute;dica</td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.persona.apellido.label"/>: </label></td>
				<td class="normal"><html:text name="personaSearchPageVO" property="persona.apellido" size="20" maxlength="20" styleClass="datos" /></td>
				<td><label><bean:message bundle="pad" key="pad.persona.nombres.label"/>: </label></td>
				<td class="normal"><html:text name="personaSearchPageVO" property="persona.nombres" size="20" maxlength="20" styleClass="datos" /></td>
			</tr>
			<tr>
				<td><label>&nbsp;<bean:message bundle="pad" key="pad.tipoDocumento.label"/>: </label></td>
				<td class="normal">
					<html:select name="personaSearchPageVO" property="persona.documento.tipoDocumento.id" styleClass="select">
						<html:optionsCollection name="personaSearchPageVO" property="listTipoDocumento" label="abreviatura" value="id" />
					</html:select>
				</td>
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.documento.numero.label"/>: </label></td>				
				<td class="normal"><html:text name="personaSearchPageVO" property="persona.documento.numeroView" size="10" maxlength="10" styleClass="datos"/></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.persona.cuit.label"/>: </label></td>				
				<td class="normal">
					<html:text name="personaSearchPageVO" property="persona.cuit" size="13" maxlength="13" styleClass="datos"/>
				</td>
				<td><label><bean:message bundle="pad" key="pad.persona.sexo.label"/>: </label></td>
				<td class="normal">
					<html:select name="personaSearchPageVO" property="persona.sexo.id" styleClass="select">
						<html:optionsCollection name="personaSearchPageVO" property="listSexo" label="value" value="id" />
					</html:select>
				</td>
			</tr>
		</table>
			
		<p align="center">
		  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
				<bean:message bundle="base" key="abm.button.limpiar"/>
			</html:button>
			&nbsp;
			<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('baseImprimir', '1');">
				<bean:message bundle="base" key="abm.button.imprimir"/>
			</html:button>			
			&nbsp;
		  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
				<bean:message bundle="base" key="abm.button.buscar"/>
			</html:button>
		</p>
	</fieldset>	
	<!-- Fin Filtro -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="personaSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="personaSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="personaSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
							</logic:notEqual>
							<th align="left"><bean:message bundle="pad" key="pad.persona.nombres.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.persona.apellido.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.tipoDocumento.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.documento.numero.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.persona.cuit.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.persona.esContribuyente.label"/></th>
							<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
						</tr>
							
						<logic:iterate id="PersonaVO" name="personaSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="personaSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<logic:equal name="personaSearchPageVO" property="paramSelectSoloContrib" value="true">
											<logic:equal name="PersonaVO" property="esContribuyente" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="PersonaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="PersonaVO" property="esContribuyente" value="true">
												<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
											</logic:notEqual>			
										</logic:equal>	
										
										<logic:equal name="personaSearchPageVO" property="paramSelectSoloContrib" value="false">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="PersonaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>	
									</td>
								</logic:equal>									
								
								<logic:notEqual name="personaSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="personaSearchPageVO" property="verEnabled" value="enabled">
											<logic:equal name="PersonaVO" property="verEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitFormContribuyente('ver', 
												'<bean:write name="PersonaVO" property="id" bundle="base" formatKey="general.format.id"/>',
												'<bean:write name="PersonaVO" property="esContribuyente"/>'	);">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="PersonaVO" property="verEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="personaSearchPageVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>
									<!-- Modificar-->								
									<td>
										<logic:equal name="personaSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="PersonaVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitFormContribuyente('modificar', 
												'<bean:write name="PersonaVO" property="id" bundle="base" formatKey="general.format.id"/>',
												'<bean:write name="PersonaVO" property="esContribuyente"/>'	);">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="PersonaVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="personaSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
								</logic:notEqual>
								<td><bean:write name="PersonaVO" property="nombres"/>&nbsp;</td>
								<td><bean:write name="PersonaVO" property="apellido" />&nbsp;</td>
								<td><bean:write name="PersonaVO" property="documento.tipoDocumento.abreviatura" />&nbsp;</td>
								<td><bean:write name="PersonaVO" property="documento.numeroView" />&nbsp;</td>
								<td><bean:write name="PersonaVO" property="cuit" />&nbsp;</td>
								<td><bean:write name="PersonaVO" property="esContribuyenteView" />&nbsp;</td>
								<td><bean:write name="PersonaVO" property="estado.value" />&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="personaSearchPageVO"/>
								<%@ include file="/base/pagerSinPU.jsp" %>
							</td>
						</tr>
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="personaSearchPageVO" property="listResult">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
						<logic:greaterThan name="personaSearchPageVO" property="pageNumber" value="1">
							<tr>
								<td align="center">
									<bean:message bundle="base" key="base.resultadoPaginaVacio"/>
								</td>
							</tr>
							<tr>
								<td class="paginador" align="center">
									<bean:define id="pager" name="personaSearchPageVO"/>
									<%@ include file="/base/pagerSinPU.jsp" %>
								</td>
							</tr>
						</logic:greaterThan>
						<logic:lessEqual name="personaSearchPageVO" property="pageNumber" value="1">
							<tr>
								<td align="center">
									<bean:message bundle="base" key="base.resultadoVacio"/>
								</td>
							</tr>
						</logic:lessEqual>
					</tbody>			
				</table>
			</logic:empty>
		</logic:equal>			
	</div>
	<!-- Fin Resultado Filtro -->

	<table class="tablabotones">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<logic:equal name="personaSearchPageVO" property="viewResult" value="true">
  	    		<td align="right">
  	    			<logic:equal name="personaSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="personaSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="personaSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="personaSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="personaSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
						</logic:equal>
						
					</logic:equal>
				</td>
			</logic:equal>
		</tr>
	</table>
		
	<input type="hidden" name="method" value=""/>
<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<input type="hidden" name="esContribuyente" value="false"/>
	
	<input type="hidden" name="name"         value="<bean:write name='personaSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	
		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
