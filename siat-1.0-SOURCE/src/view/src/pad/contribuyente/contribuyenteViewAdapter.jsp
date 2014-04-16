<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/pad/AdministrarContribuyente.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pad" key="pad.contribuyenteAdapter.title"/></h1>
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Persona -->
		<fieldset>
			<logic:equal name="contribuyenteAdapterVO" property="contribuyente.persona.esPersonaFisica" value="true">
				<legend><bean:message bundle="pad" key="pad.persona.title"/></legend>
			</logic:equal>
			<logic:notEqual name="contribuyenteAdapterVO" property="contribuyente.persona.esPersonaFisica" value="true">
				<legend><bean:message bundle="pad" key="pad.persona.juridica.title"/></legend>
			</logic:notEqual>
			
			<!-- Inclusion de los datos de la persona -->
			<bean:define id="personaVO" name="contribuyenteAdapterVO" property="contribuyente.persona"/>
			<%@ include file="/pad/persona/includePersona.jsp" %>
		</fieldset>
		<!-- Fin Persona -->
		
		<!-- Contribuyente -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.contribuyente.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td>
						<label><bean:message bundle="pad" key="pad.altaOficio.contribuyente.nroIsib.label"/>: </label>
					</td>
					<td><bean:write name="contribuyenteAdapterVO" property="contribuyente.nroIsib"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.contribuyente.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="contribuyenteAdapterVO" property="contribuyente.fechaDesdeView"/></td>
					<td><label><bean:message bundle="pad" key="pad.contribuyente.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="contribuyenteAdapterVO" property="contribuyente.fechaHastaView"/></td>
				</tr>
			</table>
		</fieldset>
		<!-- Fin Contribuyente -->
		
	<!-- ConAtr -->
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.contribuyente.listConAtrVal.label"/></legend>
		<logic:notEmpty name="contribuyenteAdapterVO" property="contribuyenteDefinition.listConAtrDefinition">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				
				<tr>
					<th width="1">&nbsp;</th> <!-- Ver -->
					<th align="left"><bean:message bundle="def" key="def.atributo.label"/></th>
					<th align="left">Valor</th>
					<th align="left">Vigencia</th>
				</tr>
					
				<logic:iterate id="ConAtrDefinition" name="contribuyenteAdapterVO" property="contribuyenteDefinition.listConAtrDefinition">
					<tr>
						<bean:define id="AtrVal" name="ConAtrDefinition"/>					
						<!-- Ver -->
						<td>
							<logic:equal name="contribuyenteAdapterVO" property="verConAtrValEnabled" value="enabled">							
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verConAtrVal', '<bean:write name="AtrVal" property="idDefinition" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
								</a>
							</logic:equal>
							<logic:notEqual name="contribuyenteAdapterVO" property="verConAtrValEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
							</logic:notEqual>
						</td>
											
						<%@ include file="/def/atrDefinitionView4Edit.jsp" %>
					
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
	</fieldset>

		<!-- CuentaTitular -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="pad" key="pad.contribuyente.listCuentaTitular.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="contribuyenteAdapterVO" property="contribuyente.listCuentaTitular">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th align="left"><bean:message bundle="pad" key="pad.cuentaTitular.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.cuentaTitular.fechaHasta.label"/></th>						
						<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/></th>
					</tr>
					<logic:iterate id="CuentaTitularVO" name="contribuyenteAdapterVO" property="contribuyente.listCuentaTitular">
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="contribuyenteAdapterVO" property="verCuentaTitularEnabled" value="enabled">
									<logic:equal name="CuentaTitularVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verCuentaTitular', '<bean:write name="CuentaTitularVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="CuentaTitularVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="contribuyenteAdapterVO" property="verCuentaTitularEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="CuentaTitularVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="CuentaTitularVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="CuentaTitularVO" property="cuenta.recurso.desRecurso"/>&nbsp;</td>
							<td><bean:write name="CuentaTitularVO" property="cuenta.numeroCuenta" />&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="contribuyenteAdapterVO" property="contribuyente.listCuentaTitular">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- CuentaTitular -->
				
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