<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarNodo.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.nodoAdapter.title"/></h1>		
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
			<legend><bean:message bundle="bal" key="bal.nodo.title"/></legend>			
			<table class="tabladatos" width="100%">
				<tr>
					<!-- Clasificador -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.clasificador.label"/>: </label></td>
					<td class="normal"><bean:write name="nodoAdapterVO" property="nodo.clasificador.descripcion"/></td>
					<!-- Nivel -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.nivel.label"/>: </label></td>
					<td class="normal"><bean:write name="nodoAdapterVO" property="nodo.nivelView"/></td>
				</tr>
				<tr>
					<!-- Clave -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.clave.label"/>: </label></td>
					<td class="normal"><bean:write name="nodoAdapterVO" property="nodo.clave"/></td>
				</tr>
				<tr>
					<!-- Descripcion -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.descripcion.label"/>: </label></td>
					<td colspan="3" class="normal"><bean:write name="nodoAdapterVO" property="nodo.descripcion"/></td>
				</tr>
				<logic:equal name="nodoAdapterVO" property="nodo.esNodoRaiz" value="false">
				<tr>
					<!-- Clave Padre-->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.clave.label"/>&nbsp;<bean:message bundle="bal" key="bal.nodo.nodoPadre.label"/>: </label></td>
					<td class="normal"><bean:write name="nodoAdapterVO" property="nodo.nodoPadre.clave"/></td>
				</tr>
				<tr>
					<!-- Descripcion Padre -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.descripcion.label"/>&nbsp;<bean:message bundle="bal" key="bal.nodo.nodoPadre.label"/>: </label></td>
					<td colspan="3" class="normal"><bean:write name="nodoAdapterVO" property="nodo.nodoPadre.descripcion"/></td>
				</tr>
				</logic:equal>
			</table>
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="right">
					<bean:define id="modificarEncabezadoEnabled" name="nodoAdapterVO" property="modificarEncabezadoEnabled"/>
					<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
						'<bean:write name="nodoAdapterVO" property="nodo.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.modificar"/>"/>
				</td>
			</tr>
		</table>

		</fieldset>
		
		<logic:equal name="nodoAdapterVO" property="paramRelPartida" value="true">
		<!-- RelPartida -->		
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.relPartida.label"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="bal" key="bal.relPartida.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.relPartida.fechaHasta.label"/></th>						
						<th align="left"><bean:message bundle="bal" key="bal.partida.label"/></th>
					</tr>
					<logic:notEmpty  name="nodoAdapterVO" property="nodo.listRelPartida">	    	
					<logic:iterate id="RelPartidaVO" name="nodoAdapterVO" property="nodo.listRelPartida">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="nodoAdapterVO" property="verRelPartidaEnabled" value="enabled">							
									<logic:equal name="RelPartidaVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verRelPartida', '<bean:write name="RelPartidaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="RelPartidaVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="nodoAdapterVO" property="verRelPartidaEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Modificar-->
								<logic:equal name="nodoAdapterVO" property="modificarRelPartidaEnabled" value="enabled">
									<logic:equal name="RelPartidaVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarRelPartida', '<bean:write name="RelPartidaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="RelPartidaVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="nodoAdapterVO" property="modificarRelPartidaEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Eliminar-->
								<logic:equal name="nodoAdapterVO" property="eliminarRelPartidaEnabled" value="enabled">
									<logic:equal name="RelPartidaVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarRelPartida', '<bean:write name="RelPartidaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="RelPartidaVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="nodoAdapterVO" property="eliminarRelPartidaEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="RelPartidaVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="RelPartidaVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="RelPartidaVO" property="partida.desPartidaView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="nodoAdapterVO" property="nodo.listRelPartida">
					<tr><td colspan="9" align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			<td colspan="20" align="right">
  				<bean:define id="agregarNodoEnabled" name="nodoAdapterVO" property="agregarRelPartidaEnabled"/>
				<input type="button" <%=agregarNodoEnabled%> class="boton" 
					onClick="submitForm('agregarRelPartida', '<bean:write name="nodoAdapterVO" property="nodo.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
			</td>
			</tbody>
			</table>
			</div>
		<!-- RelPartida -->			
		</logic:equal>
		<logic:equal name="nodoAdapterVO" property="paramRelCla" value="true">
		<!-- RelCla -->		
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.relCla.label"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="bal" key="bal.relCla.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.relCla.fechaHasta.label"/></th>						
						<th align="left"><bean:message bundle="bal" key="bal.clasificador.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.nodo.clave.ref"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.nodo.descripcion.ref"/></th>
					</tr>
					<logic:notEmpty  name="nodoAdapterVO" property="nodo.listRelCla">	    	
					<logic:iterate id="RelClaVO" name="nodoAdapterVO" property="nodo.listRelCla">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="nodoAdapterVO" property="verRelClaEnabled" value="enabled">							
									<logic:equal name="RelClaVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verRelCla', '<bean:write name="RelClaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="RelClaVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="nodoAdapterVO" property="verRelClaEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Modificar-->
								<logic:equal name="nodoAdapterVO" property="modificarRelClaEnabled" value="enabled">
									<logic:equal name="RelClaVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarRelCla', '<bean:write name="RelClaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="RelClaVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="nodoAdapterVO" property="modificarRelClaEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Eliminar-->
								<logic:equal name="nodoAdapterVO" property="eliminarRelClaEnabled" value="enabled">
									<logic:equal name="RelClaVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarRelCla', '<bean:write name="RelClaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="RelClaVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="nodoAdapterVO" property="eliminarRelClaEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="RelClaVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="RelClaVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="RelClaVO" property="nodo2.clasificador.descripcion"/>&nbsp;</td>
							<td><bean:write name="RelClaVO" property="nodo2.clave"/>&nbsp;</td>
							<td><bean:write name="RelClaVO" property="nodo2.descripcion"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="nodoAdapterVO" property="nodo.listRelCla">
					<tr><td colspan="9" align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			<td colspan="20" align="right">
  				<bean:define id="agregarNodoEnabled" name="nodoAdapterVO" property="agregarRelClaEnabled"/>
				<input type="button" <%=agregarNodoEnabled%> class="boton" 
					onClick="submitForm('agregarRelCla', '<bean:write name="nodoAdapterVO" property="nodo.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
			</td>
			</tbody>
			</table>
			</div>
		<!-- RelCla -->			
		</logic:equal>
		
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