<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/esp/AdministrarEntVen.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="esp" key="esp.entVen.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
				
		<!-- OtrIngTes -->
		<fieldset>
			<legend><bean:message bundle="esp" key="esp.entVen.title"/></legend>
			
			<table class="tabladatos">
				<!-- Recurso -->		
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>					
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.recurso.desRecurso"/></td>
				</tr>
				<tr>
					<!-- Area -->		
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.area.label"/>: </label></td>
					<td class="normal">
						<html:select name="entVenAdapterVO" property="area.id" styleClass="select" onchange="submitForm('paramArea', '');">
							<html:optionsCollection name="entVenAdapterVO" property="listArea" label="desArea" value="id" />
						</html:select>
					</td>
				</tr>
				<tr>
					<!-- CuentaBanco Origen -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.otrIngTes.cueBanOrigen.label"/>: </label></td>
					<td class="normal">
						<html:select name="entVenAdapterVO" property="cuentaBanco.id" styleClass="select">
							<html:optionsCollection name="entVenAdapterVO" property="listCuentaBanco" label="nroCuenta" value="id" />
						</html:select>					
					</td>
				</tr>	
				<tr>
					<!-- Fecha -->		
					<td><label><bean:message bundle="esp" key="esp.entVen.fechaEmision.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.fechaEmisionView"/></td>
				</tr>
				<!-- Importe -->	
				<tr>
					<td><label><bean:message bundle="bal" key="bal.otrIngTes.importe.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="importeView" /></td>
				</tr>	
				<!-- Descripcion -->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.otrIngTes.descripcion.label"/>: </label></td>
					<td colspan="4" class="normal"><bean:write name="entVenAdapterVO" property="otrIngTes.descripcion" /></td>					
				</tr>
				
			</table>
		</fieldset>
		
		
		<!-- Lista de conceptos del OtrIngTes (OtrIngTesRecCon)-->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.otrIngTesRecCon.title"/></legend>
			<table class="tabladatos">
			<!-- lista de conceptos -->
			<tr>
				<td>&nbsp;</td>				
				<td>				
					
						<table class="tramonline" border="0" cellpadding="0" cellspacing="1">
							<caption>Conceptos</caption>
              				<tbody>
								<logic:notEmpty name="entVenAdapterVO" property="otrIngTes.listOtrIngTesRecCon">
									<logic:iterate id="OtrIngTesRecConVO" name="entVenAdapterVO" property="otrIngTes.listOtrIngTesRecCon">
										<tr>
										<td><bean:write name="OtrIngTesRecConVO" property="recCon.desRecCon"/></td>
										<td>
											<input type="text" size="7" name="importe<bean:write name="OtrIngTesRecConVO" property="idView"/>"
												value="<bean:write name="OtrIngTesRecConVO" property="importeView"/>"/>
										</td>
	  									</tr>	
									</logic:iterate>
								</logic:notEmpty>
								<logic:empty name="entVenAdapterVO" property="otrIngTes.listOtrIngTesRecCon">								
									<tr><td colspan="2"><bean:message bundle="bal" key="bal.otrIngTesAdapter.listOtrIngTesRecCon.vacia"/></td></tr>
								</logic:empty>			
							</tbody>
							</table>	
					
				</td>
				<td>&nbsp;</td>				
			</tr>			
			</table>
		</fieldset>
		<!-- Fin Lista de conceptos del OtrIngTes (OtrIngTesRecCon)-->
		
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">					
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregarOTT', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>							
				</td>
 			</tr>
		</table>
	   	
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		<input type="hidden" name="isOTT" value="true"/>
	
		<!-- Inclusion del Codigo Javascript del Calendar-->
		<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
	
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
		