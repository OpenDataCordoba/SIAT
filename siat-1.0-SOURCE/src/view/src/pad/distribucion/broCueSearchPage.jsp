<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/pad/BuscarBroCue.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pad" key="pad.broCueAdapter.title"/></h1>		
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">&nbsp;</td>				
			<td align="right">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
			
		<!-- Broche -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.broche.title"/></legend>			
			<table class="tabladatos" width="100%">
				<!-- Recurso -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="broCueSearchPageVO" property="broCue.broche.recurso.desRecurso"/></td>
					<!-- Tipo Broche -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.tipoBroche.label"/>: </label></td>
					<td class="normal"><bean:write name="broCueSearchPageVO" property="broCue.broche.tipoBroche.desTipoBroche"/></td>
					</td>	
				</tr>
				<!-- Numero -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.nro.label"/>: </label></td>
					<td class="normal"><bean:write name="broCueSearchPageVO" property="broCue.broche.idView"/></td>					
				</tr>
				<!-- Titular o Descripcion-->		
				<tr>
					<logic:equal name="broCueSearchPageVO" property="paramTipoBroche" value="1">	<!-- TipoBroche=Administrativo -->	
						<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.titular.label"/>: </label></td>
					</logic:equal>
					<logic:notEqual name="broCueSearchPageVO" property="paramTipoBroche" value="1">	<!-- TipoBroche<>Administrativo -->		
						<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.desBroche.label"/>: </label></td>
					</logic:notEqual>
					<td class="normal"><bean:write name="broCueSearchPageVO" property="broCue.broche.desBroche"/></td>					
				</tr>
				<logic:equal name="broCueSearchPageVO" property="paramTipoBroche" value="1">	<!-- TipoBroche=Administrativo -->	
				<!-- Domicilio -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.strDomicilioEnvio.label"/>: </label></td>
					<td class="normal"><bean:write name="broCueSearchPageVO" property="broCue.broche.strDomicilioEnvio"/></td>					
				</tr>
				<!-- Telefono -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.telefono.label"/>: </label></td>
					<td class="normal"><bean:write name="broCueSearchPageVO" property="broCue.broche.telefono"/></td>					
				</tr>
				</logic:equal>
				<!-- Repartidor -->	
				<tr>
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.repartidor.label"/>: </label></td>
					<td class="normal">
						<logic:notEqual name="broCueSearchPageVO" property="paramTipoBroche" value="2">	<!-- TipoBroche<>'Repartidor Fuera de Zona' & TipoBroche<>'Repartido Zona' -->		
							<bean:write name="broCueSearchPageVO" property="broCue.broche.repartidor.descripcionForCombo"/>
						</logic:notEqual>	
					</td>	
				</tr>			
			</table>
		</fieldset>
		<!-- Fin Broche -->

		<!-- BroCue -->
		<div id="resultadoFiltro">
		<logic:equal name="broCueSearchPageVO" property="viewResult" value="true">		
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="pad" key="pad.broCue.label"/></caption>
	    	<tbody>
			<logic:notEmpty  name="broCueSearchPageVO" property="listBroCue">	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<!-- th width="1">&nbsp;</th --> <!-- Modificar -->
						<th align="left"><bean:message bundle="pad" key="pad.broCue.fechaAlta.label"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.broCue.fechaBaja.label"/></th>						
						<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.cuenta.domicilioEnvio.label"/></th>
					</tr>
					<logic:iterate id="BroCueVO" name="broCueSearchPageVO" property="listBroCue">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="broCueSearchPageVO" property="verEnabled" value="enabled">							
									<logic:equal name="BroCueVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="BroCueVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="BroCueVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="broCueSearchPageVO" property="verEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</td>
							<!-- td>
								<!-- Modificar--
								<logic:equal name="broCueSearchPageVO" property="modificarEnabled" value="enabled">
									<logic:equal name="BroCueVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="BroCueVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="BroCueVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="broCueSearchPageVO" property="modificarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td -->
							<td><bean:write name="BroCueVO" property="fechaAltaView"/>&nbsp;</td>
							<td><bean:write name="BroCueVO" property="fechaBajaView"/>&nbsp;</td>
							<td><bean:write name="BroCueVO" property="cuenta.numeroCuenta" />&nbsp;</td>
							<td>
								<logic:present name="BroCueVO" property="cuenta.domicilioEnvio">
									<bean:write name="BroCueVO" property="cuenta.domicilioEnvio.view" />&nbsp;
								</logic:present>
							</td>
						</tr>
					</logic:iterate>
					<tr>
						<td class="paginador" align="center" colspan="11">
							<bean:define id="pager" name="broCueSearchPageVO"/>
							<%@ include file="/base/pager.jsp" %>
						</td>
					</tr>
				</logic:notEmpty>
				<logic:empty  name="broCueSearchPageVO" property="listBroCue">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			<td colspan="20" align="right">
  				<bean:define id="agregarEnabled" name="broCueSearchPageVO" property="agregarEnabled"/>
				<input type="button" <%=agregarEnabled%> class="boton" 
					onClick="submitForm('agregar', '<bean:write name="broCueSearchPageVO" property="broCue.broche.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
			</td>
			</tbody>
			</table>
		</logic:equal>				
		</div>
		<!-- Fin BroCue -->
		
	<table class="tablabotones">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
				&nbsp;
				<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('baseImprimir', '1');">
				    <bean:message bundle="base" key="abm.button.imprimir"/>
			    </html:button>
			</td>
		</tr> 
	</table>
	<input type="hidden" name="name"     value="<bean:write name='broCueSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
		
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->		
		
		