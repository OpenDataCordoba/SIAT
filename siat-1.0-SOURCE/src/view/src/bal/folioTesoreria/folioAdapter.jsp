<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarFolio.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.folioAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
			
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.folio.title"/></legend>			
			<table class="tabladatos" width="100%">
				<tr>
					<!-- Fecha Folio -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.folio.fechaFolio.label"/>: </label></td>
					<td class="normal"><bean:write name="folioAdapterVO" property="folio.fechaFolioView"/></td>
				</tr>	
				<tr>
					<!-- Numero -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.folio.numero.label"/>: </label></td>
					<td class="normal"><bean:write name="folioAdapterVO" property="folio.numeroView"/></td>
				</tr>
				<tr>
					<!-- Descripcion -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.folio.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="folioAdapterVO" property="folio.descripcion"/></td>
				</tr>
			</table>
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="right">
					<bean:define id="modificarEncabezadoEnabled" name="folioAdapterVO" property="modificarEncabezadoEnabled"/>
					<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
						'<bean:write name="folioAdapterVO" property="folio.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.modificar"/>"/>
				</td>
			</tr>
		</table>
		</fieldset>
		
		<!-- FolDiaCob -->		
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:write name="folioAdapterVO" property="folio.desDiaCob"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="bal" key="bal.folDiaCob.fechaCob.label"/></th>
						<logic:notEmpty  name="folioAdapterVO" property="listTipoCob">	    	
							<logic:iterate id="TipoCobVO" name="folioAdapterVO" property="listTipoCob">				
								<th align="left"><bean:write name="TipoCobVO" property="descripcion"/>&nbsp;</th>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty  name="folioAdapterVO" property="listTipoCob">
								<th align="left"><bean:message bundle="bal" key="bal.tipoCob.noExistenColumnas.label"/></th>
						</logic:empty>	
						<th align="left"><bean:message bundle="bal" key="bal.tipoCob.totalesCol.label"/></th>
					</tr>
					<logic:notEmpty  name="folioAdapterVO" property="folio.listFolDiaCob">	    	
					<logic:iterate id="FolDiaCobVO" name="folioAdapterVO" property="folio.listFolDiaCob">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="folioAdapterVO" property="verFolDiaCobEnabled" value="enabled">							
									<logic:equal name="FolDiaCobVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verFolDiaCob', '<bean:write name="FolDiaCobVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="FolDiaCobVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="folioAdapterVO" property="verFolDiaCobEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<td>
							<!-- Modificar-->
							<logic:equal name="folioAdapterVO" property="folio.paramEnviado" value="false">
									<logic:equal name="folioAdapterVO" property="modificarFolDiaCobEnabled" value="enabled">
										<logic:equal name="FolDiaCobVO" property="modificarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarFolDiaCob', '<bean:write name="FolDiaCobVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="FolDiaCobVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="folioAdapterVO" property="modificarFolDiaCobEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
							</logic:equal>
							<logic:equal name="folioAdapterVO" property="folio.paramEnviado" value="true">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
							</logic:equal>
							</td>
							<td>
								<!-- Eliminar-->
							<logic:equal name="folioAdapterVO" property="folio.paramEnviado" value="false">
								<logic:equal name="folioAdapterVO" property="eliminarFolDiaCobEnabled" value="enabled">
									<logic:equal name="FolDiaCobVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarFolDiaCob', '<bean:write name="FolDiaCobVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="FolDiaCobVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="folioAdapterVO" property="eliminarFolDiaCobEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:equal name="folioAdapterVO" property="folio.paramEnviado" value="true">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
							</logic:equal>
					
							</td>
							<td align="right"><bean:write name="FolDiaCobVO" property="fechaOrDesc"/>&nbsp;</td>
							<logic:notEmpty  name="FolDiaCobVO" property="listFolDiaCobCol">	    	
								<logic:iterate id="FolDiaCobColVO" name="FolDiaCobVO" property="listFolDiaCobCol">				
									<td align="right"><bean:write name="FolDiaCobColVO" property="importeView"/>&nbsp;</td>
								</logic:iterate>
							</logic:notEmpty>
							<logic:empty  name="FolDiaCobVO" property="listFolDiaCobCol">
									<td>&nbsp;</td>
							</logic:empty>	
							<td align="right"><bean:write name="FolDiaCobVO" property="totalView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
					<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<logic:notEmpty  name="folioAdapterVO" property="listTotales">	    	
						<logic:iterate id="FolDiaCobColVO" name="folioAdapterVO" property="listTotales">				
							<td align="right"><bean:write name="FolDiaCobColVO" property="importeView"/>&nbsp;</td>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="folioAdapterVO" property="listTotales">
							<td>&nbsp;</td>
					</logic:empty>
					<td align="right"><bean:write name="folioAdapterVO" property="totalDiaCobView"/>&nbsp;</td>
					</tr>
				</logic:notEmpty>
				<logic:empty  name="folioAdapterVO" property="folio.listFolDiaCob">
					<tr><td colspan="20" align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			<td colspan="20" align="right">
  				<bean:define id="agregarFolioEnabled" name="folioAdapterVO" property="agregarFolDiaCobEnabled"/>
				<input type="button" <%=agregarFolioEnabled%> class="boton" 
					onClick="submitForm('agregarFolDiaCob', '<bean:write name="folioAdapterVO" property="folio.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
			</td>
			</tbody>
			</table>
			</div>
		<!-- FolDiaCob -->		
		
		<!-- OtrIngTes -->		
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.otrIngTes.label"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
					  	<th align="left"><bean:message bundle="bal" key="bal.otrIngTes.fechaOtrIngTes.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.otrIngTes.descripcion.label"/></th>
					  	<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.cuentaBanco.label"/></th>
  						<th align="left"><bean:message bundle="bal" key="bal.otrIngTes.importe.label"/></th>
					</tr>
					<logic:notEmpty  name="folioAdapterVO" property="folio.listOtrIngTes">	    	
					<logic:iterate id="OtrIngTesVO" name="folioAdapterVO" property="folio.listOtrIngTes">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="folioAdapterVO" property="verOtrIngTesEnabled" value="enabled">							
									<logic:equal name="OtrIngTesVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verOtrIngTes', '<bean:write name="OtrIngTesVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="OtrIngTesVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="folioAdapterVO" property="verOtrIngTesEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<td>
							<logic:equal name="folioAdapterVO" property="folio.paramEnviado" value="false">
								<!-- Excluir-->
								<logic:equal name="folioAdapterVO" property="excluirOtrIngTesEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('excluirOtrIngTes', '<bean:write name="OtrIngTesVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
									</a>
								</logic:equal>
								<logic:notEqual name="folioAdapterVO" property="excluirOtrIngTesEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:equal name="folioAdapterVO" property="folio.paramEnviado" value="true">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
							</logic:equal>
							</td>
						  	<td><bean:write name="OtrIngTesVO" property="fechaOtrIngTesView"/>&nbsp;</td>
						  	<td><bean:write name="OtrIngTesVO" property="descripcion"/>&nbsp;</td>
						  	<td><bean:write name="OtrIngTesVO" property="recurso.desRecurso"/>&nbsp;</td>
   						 	<td><bean:write name="OtrIngTesVO" property="cueBanOrigen.nroCuenta"/>&nbsp;</td>
							<td><bean:write name="OtrIngTesVO" property="importeView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="folioAdapterVO" property="folio.listOtrIngTes">
					<tr><td colspan="9" align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			<td colspan="20" align="right">
  				<bean:define id="agregarFolioEnabled" name="folioAdapterVO" property="incluirOtrIngTesEnabled"/>
				<input type="button" <%=agregarFolioEnabled%> class="boton" 
					onClick="submitForm('incluirOtrIngTes', '<bean:write name="folioAdapterVO" property="folio.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
			</td>
			</tbody>
			</table>
			</div>
		<!-- OtrIngTes -->	
			
		<!-- FolCom -->		
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.folCom.label"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="bal" key="bal.folCom.fecha.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.folCom.nroComp.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.folCom.concepto.label"/></th>						
						<th align="left"><bean:message bundle="bal" key="bal.folCom.desCueBan.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.folCom.importe.label"/></th>						
					</tr>
					<logic:notEmpty  name="folioAdapterVO" property="folio.listFolCom">	    	
					<logic:iterate id="FolComVO" name="folioAdapterVO" property="folio.listFolCom">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="folioAdapterVO" property="verFolComEnabled" value="enabled">							
									<logic:equal name="FolComVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verFolCom', '<bean:write name="FolComVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="FolComVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="folioAdapterVO" property="verFolComEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<td>
							<logic:equal name="folioAdapterVO" property="folio.paramEnviado" value="false">
								<!-- Modificar-->
								<logic:equal name="folioAdapterVO" property="modificarFolComEnabled" value="enabled">
									<logic:equal name="FolComVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarFolCom', '<bean:write name="FolComVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="FolComVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="folioAdapterVO" property="modificarFolComEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:equal name="folioAdapterVO" property="folio.paramEnviado" value="true">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
							</logic:equal>
							</td>
							<td>
							<logic:equal name="folioAdapterVO" property="folio.paramEnviado" value="false">
							<!-- Eliminar-->
								<logic:equal name="folioAdapterVO" property="eliminarFolComEnabled" value="enabled">
									<logic:equal name="FolComVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarFolCom', '<bean:write name="FolComVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="FolComVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="folioAdapterVO" property="eliminarFolComEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:equal name="folioAdapterVO" property="folio.paramEnviado" value="true">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
							</logic:equal>
							</td>
							<td><bean:write name="FolComVO" property="fechaView"/>&nbsp;</td>
							<td><bean:write name="FolComVO" property="nroComp"/>&nbsp;</td>
							<td><bean:write name="FolComVO" property="concepto"/>&nbsp;</td>
							<td><bean:write name="FolComVO" property="desCueBan"/>&nbsp;</td>
							<td><bean:write name="FolComVO" property="importeView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="folioAdapterVO" property="folio.listFolCom">
					<tr><td colspan="9" align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			<td colspan="20" align="right">
  				<bean:define id="agregarFolioEnabled" name="folioAdapterVO" property="agregarFolComEnabled"/>
				<input type="button" <%=agregarFolioEnabled%> class="boton" 
					onClick="submitForm('agregarFolCom', '<bean:write name="folioAdapterVO" property="folio.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
			</td>
			</tbody>
			</table>
			</div>
		<!-- FolCom -->			
			
		<table class="tablabotones">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
					    <bean:message bundle="base" key="abm.button.imprimir"/>
					</html:button>	
				</td>
   	    	</tr>
		</table>
		<input type="hidden" name="name"  value="<bean:write name='folioAdapterVO' property='name'/>" id="name"/>
	  	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->		