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
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.descripcion.label"/>&nbsp;<bean:message bundle="bal" key="bal.nodo.nodoPadre.label"/>:</label></td>
					<td colspan="3" class="normal"><bean:write name="nodoAdapterVO" property="nodo.nodoPadre.descripcion"/></td>
				</tr>
				</logic:equal>
			</table>
		</fieldset>
	
		<logic:equal name="nodoAdapterVO" property="act" value="ver">		
		
		<logic:equal name="nodoAdapterVO" property="paramRelPartida" value="true">
		<!-- RelPartida -->		
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.relPartida.label"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
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
						<th align="left"><bean:message bundle="bal" key="bal.relCla.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.relCla.fechaHasta.label"/></th>						
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
							<td><bean:write name="RelClaVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="RelClaVO" property="fechaHastaView"/>&nbsp;</td>
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
			</tbody>
			</table>
			</div>
		<!-- RelCla -->			
		</logic:equal>
		</logic:equal>

		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="nodoAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
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
		
		<!-- Inclusion del Codigo Javascript del Calendar-->
		<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
		
		