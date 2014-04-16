<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>
   	    <%@include file="/base/calendar.js"%>
</script>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/exe/AdministrarExeRecCon.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="exe" key="exe.exeRecConEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- Exencion -->
	<fieldset>
		<legend><bean:message bundle="exe" key="exe.exencion.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal">
					<bean:write name="exeRecConAdapterVO" property="exeRecCon.exencion.recurso.desRecurso"/>
				</td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="exe" key="exe.exencion.codExencion.label"/>: </label></td>
				<td class="normal"><bean:write name="exeRecConAdapterVO" property="exeRecCon.exencion.codExencion"/></td>
			</tr>
			
			<tr>	
				<td><label><bean:message bundle="exe" key="exe.exencion.desExencion.label"/>: </label></td>
				<td class="normal"><bean:write name="exeRecConAdapterVO" property="exeRecCon.exencion.desExencion" /></td>
			</tr>
			
			<tr>	
				<td><label><bean:message bundle="exe" key="exe.exencion.actualizaDeuda.label"/>: </label></td>
				<td class="normal"><bean:write name="exeRecConAdapterVO" property="exeRecCon.exencion.actualizaDeuda.value"/></td>					
			</tr>
			
			<tr>	
				<td><label><bean:message bundle="exe" key="exe.exencion.enviaJudicial.label"/>: </label></td>
				<td class="normal"><bean:write name="exeRecConAdapterVO" property="exeRecCon.exencion.enviaJudicial.value"/></td>					
			</tr>
			
			<tr>	
				<td><label><bean:message bundle="exe" key="exe.exencion.enviaCyQ.label"/>: </label></td>
				<td class="normal"><bean:write name="exeRecConAdapterVO" property="exeRecCon.exencion.enviaCyQ.value"/></td>					
			</tr>
			<tr>	
				<td><label><bean:message bundle="exe" key="exe.exencion.afectaEmision.label"/>: </label></td>
				<td class="normal"><bean:write name="exeRecConAdapterVO" property="exeRecCon.exencion.afectaEmision.value"/></td>					
			</tr>
			
			<tr>	
				<td><label><bean:message bundle="exe" key="exe.exencion.esParcial.label"/>: </label></td>
				<td class="normal"><bean:write name="exeRecConAdapterVO" property="exeRecCon.exencion.esParcial.value"/></td>					
			</tr>
			
			<!-- Aca va buscar Caso -->		
			
			<tr>
				<td><label><bean:message bundle="exe" key="exe.exencion.montoMinimo.label"/>: </label></td>
				<td class="normal"><bean:message bundle="base" key="base.currency"/><bean:write name="exeRecConAdapterVO" property="exeRecCon.exencion.montoMinimoView"/></td>
			</tr>
			<tr>	
				<td><label><bean:message bundle="exe" key="exe.exencion.aplicaMinimo.label"/>: </label></td>
				<td class="normal"><bean:write name="exeRecConAdapterVO" property="exeRecCon.exencion.aplicaMinimo.value"/></td>					
			</tr>
			
		</table>			
	</fieldset>	
	<!-- Exencion -->
		
	<!-- ExeRecCon -->
	<fieldset>
		<legend><bean:message bundle="exe" key="exe.exeRecCon.title"/></legend>
		
		<table class="tabladatos">
			<tr>	
				<logic:equal name="exeRecConAdapterVO" property="act" value="agregar">
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recCon.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:select name="exeRecConAdapterVO" property="exeRecCon.recCon.id" styleClass="select">
							<html:optionsCollection name="exeRecConAdapterVO" property="listRecCon" label="desRecCon" value="id" />
						</html:select>
					</td>
				</logic:equal>
				<logic:equal name="exeRecConAdapterVO" property="act" value="modificar">
					<td><label><bean:message bundle="def" key="def.recCon.label"/>: </label></td>
					<td class="normal" colspan="3">
						<bean:write name="exeRecConAdapterVO" property="exeRecCon.recCon.desRecCon"/>
					</td>
				</logic:equal>					
			</tr>
			<tr>
				<td><label><bean:message bundle="exe" key="exe.exeRecCon.porcentaje.label"/>: </label></td>
				<td class="normal"><html:text name="exeRecConAdapterVO" property="exeRecCon.porcentajeView" size="20" maxlength="100"/></td>			
	
				<td><label><bean:message bundle="exe" key="exe.exeRecCon.montoFijo.label"/>: </label></td>
				<td class="normal"><bean:message bundle="base" key="base.currency"/><html:text name="exeRecConAdapterVO" property="exeRecCon.montoFijoView" size="20" maxlength="100"/></td>			
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="exe" key="exe.exeRecCon.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="exeRecConAdapterVO" property="exeRecCon.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
		
				<td><label><bean:message bundle="exe" key="exe.exeRecCon.fechaHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="exeRecConAdapterVO" property="exeRecCon.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
		</table>
	</fieldset>	
	<!-- ExeRecCon -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="exeRecConAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="exeRecConAdapterVO" property="act" value="agregar">
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
