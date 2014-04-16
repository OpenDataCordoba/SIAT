<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/pad/AdministrarRepartidor.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pad" key="pad.repartidorAdapter.title"/></h1>		
		
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
			<legend><bean:message bundle="pad" key="pad.repartidor.title"/></legend>			
			<table class="tabladatos" width="100%">
				<tr>
					<!-- Recurso -->		
					<td><label>&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="repartidorAdapterVO" property="repartidor.recurso.desRecurso"/></td>
					<!-- Tipo Repartidor -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.tipoRepartidor.label"/>: </label></td>
					<td class="normal"><bean:write name="repartidorAdapterVO" property="repartidor.tipoRepartidor.desTipoRepartidor"/></td>
					</td>	
				</tr>
				<tr>
					<!-- Persona -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.persona.label"/>: </label></td>
					<td class="normal"><bean:write name="repartidorAdapterVO" property="repartidor.personaView"/></td>
					<!-- Legajo -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.repartidor.legajo.label"/>: </label></td>
					<td class="normal"><bean:write name="repartidorAdapterVO" property="repartidor.legajo"/></td>
				</tr>
				<tr>
					<!-- Zona -->		
					<td><label>&nbsp;<bean:message bundle="def" key="def.zona.label"/>: </label></td>
					<td class="normal"><bean:write name="repartidorAdapterVO" property="repartidor.zona.descripcion"/></td>
					<!-- Broche -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.label"/>: </label></td>
					<td class="normal"><bean:write name="repartidorAdapterVO" property="repartidor.broche.idView"/></td>
				</tr>
			</table>
		</fieldset>
	
		<logic:equal name="repartidorAdapterVO" property="act" value="ver">		
		
		<logic:equal name="repartidorAdapterVO" property="repartidor.tipoRepartidor.criRep.codCriRep" value="CATASTRAL">		
		<!-- CriRepCat -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="pad" key="pad.criRepCat.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="repartidorAdapterVO" property="repartidor.listCriRepCat">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th align="left"><bean:message bundle="pad" key="pad.criRepCat.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.criRepCat.fechaHasta.label"/></th>						
						<th align="left"><bean:message bundle="def" key="def.seccion.label"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.criRepCat.catastralDesde.label"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.criRepCat.catastralHasta.label"/></th>
					</tr>
					<logic:iterate id="CriRepCatVO" name="repartidorAdapterVO" property="repartidor.listCriRepCat">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="repartidorAdapterVO" property="verCriRepCatEnabled" value="enabled">							
									<logic:equal name="CriRepCatVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verCriRepCat', '<bean:write name="CriRepCatVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="CriRepCatVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="repartidorAdapterVO" property="verCriRepCatEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="CriRepCatVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="CriRepCatVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="CriRepCatVO" property="seccion.descripcion" />&nbsp;</td>
							<td><bean:write name="CriRepCatVO" property="catastralDesde" />&nbsp;</td>
							<td><bean:write name="CriRepCatVO" property="catastralHasta" />&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="repartidorAdapterVO" property="repartidor.listCriRepCat">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		<!-- CriRepCat -->			
		</logic:equal>
		
		<logic:equal name="repartidorAdapterVO" property="repartidor.tipoRepartidor.criRep.codCriRep" value="CALLE">		
		<!-- CriRepCalle -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="pad" key="pad.criRepCalle.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="repartidorAdapterVO" property="repartidor.listCriRepCalle">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th align="left"><bean:message bundle="pad" key="pad.criRepCalle.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.criRepCalle.fechaHasta.label"/></th>						
						<th align="left"><bean:message bundle="pad" key="pad.criRepCalle.zona.ref"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.calle.nombreCalle.ref"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.criRepCalle.nroDesde.label"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.criRepCalle.nroHasta.label"/></th>
					</tr>
					<logic:iterate id="CriRepCalleVO" name="repartidorAdapterVO" property="repartidor.listCriRepCalle">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="repartidorAdapterVO" property="verCriRepCalleEnabled" value="enabled">							
									<logic:equal name="CriRepCalleVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verCriRepCalle', '<bean:write name="CriRepCalleVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="CriRepCalleVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="repartidorAdapterVO" property="verCriRepCalleEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="CriRepCalleVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="CriRepCalleVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="CriRepCalleVO" property="zona.descripcion" />&nbsp;</td>
							<td><bean:write name="CriRepCalleVO" property="calle.nombreCalle" />&nbsp;</td>
							<td><bean:write name="CriRepCalleVO" property="nroDesdeView" />&nbsp;</td>
							<td><bean:write name="CriRepCalleVO" property="nroHastaView" />&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="repartidorAdapterVO" property="repartidor.listCriRepCalle">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		<!-- CriRepCalle -->			
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
					<logic:equal name="repartidorAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="repartidorAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="repartidorAdapterVO" property="act" value="desactivar">
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
		
		<!-- Inclusion del Codigo Javascript del Calendar-->
		<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
		
		