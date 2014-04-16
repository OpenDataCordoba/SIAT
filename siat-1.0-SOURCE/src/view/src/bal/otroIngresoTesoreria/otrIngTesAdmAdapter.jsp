<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/DistribuirOtrIngTes.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.otrIngTesAdapter.title"/></h1>		
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
			<legend><bean:message bundle="bal" key="bal.otrIngTes.title"/></legend>			
			<table class="tabladatos" width="100%">
			<!-- Recurso -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="otrIngTesAdapterVO" property="otrIngTes.recurso.desRecurso"/></td>
				</tr>
				<tr>
			<!-- Area -->		
					<td><label>&nbsp;<bean:message bundle="def" key="def.area.label"/>: </label></td>
					<td  colspan="4" class="normal"><bean:write name="otrIngTesAdapterVO" property="otrIngTes.areaOrigen.desArea"/></td>
				</tr>
				<tr>
					<!-- CuentaBanco Origen -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.otrIngTes.cueBanOrigen.label"/>: </label></td>
					<td class="normal"><bean:write name="otrIngTesAdapterVO" property="otrIngTes.cueBanOrigen.nroCuenta"/></td>
				</tr>	
				<tr>
					<!-- Fecha -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.otrIngTes.fechaOtrIngTes.label"/>: </label></td>
					<td class="normal"><bean:write name="otrIngTesAdapterVO" property="otrIngTes.fechaOtrIngTesView"/></td>
				</tr>
				<!-- Importe -->	
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.otrIngTes.importe.label"/>: </label></td>
					<td class="normal"><bean:write name="otrIngTesAdapterVO" property="otrIngTes.importeView"/></td>
				</tr>	
				<!-- Descripcion -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.otrIngTes.descripcion.label"/>: </label></td>
					<td colspan="4" class="normal"><bean:write name="otrIngTesAdapterVO" property="otrIngTes.descripcion"/></td>
				</tr>
				<!-- Observacion-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.otrIngTes.observacion.label"/>: </label></td>
					<td colspan="3" class="normal"><bean:write name="otrIngTesAdapterVO" property="otrIngTes.observaciones"/></td>					
				</tr>
				<logic:equal name="otrIngTesAdapterVO" property="act" value="distribuir">
				<!-- Descripcion DisPar -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.disPar.label"/>: </label></td>
					<td colspan="4" class="normal"><bean:write name="otrIngTesAdapterVO" property="disPar.desDisPar"/>
					<logic:equal name="otrIngTesAdapterVO" property="modificarDistribucion" value="true">
						&nbsp;
						<logic:equal name="otrIngTesAdapterVO" property="distribuirBussEnabled" value="true">
							<html:button property="btnDistribuir"  styleClass="boton" onclick="submitForm('distribuir', '');">
										<bean:message bundle="bal" key="bal.adm.button.distribuir"/>
							</html:button>
						</logic:equal>
						<logic:equal name="otrIngTesAdapterVO" property="distribuirBussEnabled" value="false">
							<html:button property="btnDistribuir"  styleClass="boton" onclick="submitForm('distribuir', '');" disabled="true">
										<bean:message bundle="bal" key="bal.adm.button.distribuir"/>
							</html:button>
						</logic:equal>
					</logic:equal>
					</td>
				</tr>
				</logic:equal>
				<!-- Lista de conceptos -->
				<tr><td>&nbsp;</td></tr>
				<tr>
					<td>&nbsp;</td>				
					<td>				
						<table class="tramonline" border="0" cellpadding="0" cellspacing="1">
							<caption>Conceptos</caption>
	          				<tbody>
								<logic:notEmpty name="otrIngTesAdapterVO" property="otrIngTes.listOtrIngTesRecCon">
									<logic:iterate id="OtrIngTesRecConVO" name="otrIngTesAdapterVO" property="otrIngTes.listOtrIngTesRecCon">
										<tr>
											<td><bean:write name="OtrIngTesRecConVO" property="recCon.desRecCon"/></td>
											<td><bean:write name="OtrIngTesRecConVO" property="importeView"/></td>
	  									</tr>	
									</logic:iterate>
								</logic:notEmpty>
								<logic:empty name="otrIngTesAdapterVO" property="otrIngTes.listOtrIngTesRecCon">								
									<tr><td colspan="2"><bean:message bundle="bal" key="bal.otrIngTesAdapter.listOtrIngTesRecCon.vacia"/></td></tr>
								</logic:empty>			
							</tbody>
						</table>	
					</td>
					<td>&nbsp;</td>				
				</tr>	
			</table>
		</fieldset>
	
		<!-- OtrIngTesPar -->		
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.otrIngTesPar.label"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<logic:equal name="otrIngTesAdapterVO" property="act" value="distribuir">
							<logic:equal name="otrIngTesAdapterVO" property="modificarDistribucion" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
							</logic:equal>
						</logic:equal>
						<th align="left"><bean:message bundle="bal" key="bal.otrIngTesPar.importe.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.partida.label"/></th>
					</tr>
					<logic:notEmpty  name="otrIngTesAdapterVO" property="otrIngTes.listOtrIngTesPar">	    	
					<logic:iterate id="OtrIngTesParVO" name="otrIngTesAdapterVO" property="otrIngTes.listOtrIngTesPar">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="otrIngTesAdapterVO" property="verOtrIngTesParEnabled" value="enabled">							
									<logic:equal name="OtrIngTesParVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verOtrIngTesPar', '<bean:write name="OtrIngTesParVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="OtrIngTesParVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="otrIngTesAdapterVO" property="verOtrIngTesParEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<logic:equal name="otrIngTesAdapterVO" property="act" value="distribuir">
								<logic:equal name="otrIngTesAdapterVO" property="modificarDistribucion" value="true">
								<td>
									<!-- Modificar-->
									<logic:equal name="otrIngTesAdapterVO" property="modificarOtrIngTesParEnabled" value="enabled">
										<logic:equal name="OtrIngTesParVO" property="modificarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarOtrIngTesPar', '<bean:write name="OtrIngTesParVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="OtrIngTesParVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="otrIngTesAdapterVO" property="modificarOtrIngTesParEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</td>
								<td>
									<!-- Eliminar-->
									<logic:equal name="otrIngTesAdapterVO" property="eliminarOtrIngTesParEnabled" value="enabled">
										<logic:equal name="OtrIngTesParVO" property="eliminarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarOtrIngTesPar', '<bean:write name="OtrIngTesParVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="OtrIngTesParVO" property="eliminarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="otrIngTesAdapterVO" property="eliminarOtrIngTesParEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</td>
								</logic:equal>
							</logic:equal>
							<td><bean:write name="OtrIngTesParVO" property="importeView"/>&nbsp;</td>
							<td><bean:write name="OtrIngTesParVO" property="partida.desPartidaView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="otrIngTesAdapterVO" property="otrIngTes.listOtrIngTesPar">
					<tr><td colspan="9" align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			<logic:equal name="otrIngTesAdapterVO" property="act" value="distribuir">
				<logic:equal name="otrIngTesAdapterVO" property="modificarDistribucion" value="true">
					<td colspan="20" align="right">
	  					<bean:define id="agregarOtrIngTesParEnabled" name="otrIngTesAdapterVO" property="agregarOtrIngTesParEnabled"/>
						<input type="button" <%=agregarOtrIngTesParEnabled%> class="boton" 
							onClick="submitForm('agregarOtrIngTesPar', '<bean:write name="otrIngTesAdapterVO" property="otrIngTes.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
					</td>
				</logic:equal>
			</logic:equal>
			</tbody>
			</table>
			</div>
		<!-- OtrIngTesPar -->	
	
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<input type="button" class="boton" 
							onClick="submitForm('generarRecibo', '<bean:write name="otrIngTesAdapterVO" property="otrIngTes.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="bal" key="bal.adm.button.generarRecibo"/>"	align="right" />
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