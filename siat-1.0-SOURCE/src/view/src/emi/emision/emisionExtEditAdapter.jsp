<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarEmisionExt.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="emi" key="emi.emisionExtEditAdapter.title"/></h1>	

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
		<legend><bean:message bundle="emi" key="emi.emision.title"/></legend>
		<table class="tabladatos">
			<tr>
				<!-- Recurso -->
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="emisionExtAdapterVO" property="emision.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');">
						<bean:define id="includeRecursoList" name="emisionExtAdapterVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="emisionExtAdapterVO" property="emision.idRecurso"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
				</td>				
			</tr>

			<tr>
				<!-- Fecha Emision -->
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emision.fechaEmision.label"/>: </label></td>
				<td class="normal">
					<html:text name="emisionExtAdapterVO" property="emision.fechaEmisionView" styleId="fechaEmisionView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaEmisionView');" id="a_fechaEmisionView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>				
			</tr>

		</table>
	</fieldset>

	<fieldset>
		<legend><bean:message bundle="emi" key="emi.emisionExtAdapter.deuda.title"/></legend>
		<table class="tabladatos">
			<tr>
				<!-- Cuenta -->	
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>:</label></td>
				<td class="normal" colspan="3">
					<html:text name="emisionExtAdapterVO" property="cuenta.numeroCuenta" size="20" maxlength="100"/>
					<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
						<bean:message bundle="emi" key="emi.emisionExtAdapter.button.buscarCuenta"/>
					</html:button>
				</td>
			</tr>
			
			<tr>
				<!--  Periodo -->
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emisionExt.periodo.label"/>:</label></td>
				<td class="normal">
					<html:text name="emisionExtAdapterVO" property="periodo" size="5" maxlength="3"/>				
				</td>
				
				<!-- Anio -->	
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emisionExt.anio.label"/>:</label></td>
				<td class="normal">
					<html:text name="emisionExtAdapterVO" property="anio" size="5" maxlength="4"/>
				</td>				
			</tr>
			
			<tr>
				<!--  Fecha Vencimiento -->
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emisionExt.fechaVto.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="emisionExtAdapterVO" property="fechaVtoView" styleId="fechaVtoView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaVtoView');" id="a_fechaVtoView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>	
			
			<tr>
				<!-- Inclusion de Caso -->
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="emisionExtAdapterVO" property="emision"/>
					<bean:define id="voName" value="emision" />
					<%@ include file="/cas/caso/includeCaso.jsp" %>
				</td>
				<!-- Fin Inclusion de Caso -->			
			</tr>
			
			<tr>
				<!-- RecClaDeu -->
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emisionExt.recClaDeu.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="emisionExtAdapterVO" property="idRecClaDeuVO" styleClass="select">
						<html:optionsCollection name="emisionExtAdapterVO" property="listRecClaDeu" label="desClaDeu" value="id" />
					</html:select>
				</td>				
			</tr>
						
			<tr>
				<!-- Observaciones -->
				<td><label><bean:message bundle="emi" key="emi.emisionExt.observaciones.label"/>:</label></td>
				<td class="normal" colspan="3">
					<html:textarea name="emisionExtAdapterVO" property="emision.observacion" cols="80" rows="15"/>
				</td>			
			</tr>
			
			<tr>
				<!-- Lista de Conceptos -->
				<td>&nbsp;</td>
				<td>				
					<logic:equal name="emisionExtAdapterVO" property="mostrarRecCon" value="true">
						<table class="tramonline" border="0" cellpadding="0" cellspacing="1">
							<caption>Conceptos</caption>
              				<tbody>
								<logic:notEmpty name="emisionExtAdapterVO" property="listDeuAdmRecConVO">
									<logic:iterate id="DeuAdmRecConVO" name="emisionExtAdapterVO" property="listDeuAdmRecConVO">
										<tr>
											<td><bean:write name="DeuAdmRecConVO" property="descripcion"/></td>
											<td>
												<input type="text" size="7" name="importe<bean:write name="DeuAdmRecConVO" property="idView"/>"
													value="<bean:write name="DeuAdmRecConVO" property="importe"/>"/>
											</td>
										</tr>	
									</logic:iterate>
								</logic:notEmpty>
								<logic:empty name="emisionExtAdapterVO" property="listDeuAdmRecConVO">								
									<tr><td colspan="2"><bean:message bundle="emi" key="emi.emisionExtAdapter.listDeuAdmRecConVO.vacia"/></td></tr>
								</logic:empty>			
							</tbody>
							</table>	
					</logic:equal>
				</td>
			</tr>				
			<!-- FIN Lista de Conceptos -->
		</table>
	</fieldset>

	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="emisionExtAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
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
<!-- emisionExtEditAdapter.jsp -->
