<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarEncComAju.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="ef" key="ef.comAjuEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- ComAju -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.comAju.label"/></legend>
		
		<table class="tabladatos">
		<!-- fechaSolicitud -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.comAju.fechaSolicitud.label"/>: </label></td>
			<td class="normal">
				<html:text name="encComAjuAdapterVO" property="comAju.fechaSolicitudView" styleId="fechaSolicitudView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaSolicitudView');" id="a_fechaSolicitudView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
		
		<!-- fechaAplicacion -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.comAju.fechaAplicacion.label"/>: </label></td>
			<td class="normal">
				<html:text name="encComAjuAdapterVO" property="comAju.fechaAplicacionView" styleId="fechaAplicacionView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaAplicacionView');" id="a_fechaAplicacionView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
		<!-- DetAju -->
		<tr>	
				<logic:greaterThan name="encComAjuAdapterVO" property="comAju.id" value="0">
					<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal">
						<bean:write name="encComAjuAdapterVO" property="comAju.detAju.ordConCue.cuenta.numeroCuenta"/>
					</td>	
				</logic:greaterThan>
				<logic:lessEqual name="encComAjuAdapterVO" property="comAju.id" value="0">
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal">						
						<html:select name="encComAjuAdapterVO" property="comAju.detAju.id" styleClass="select">
							<html:option value="-1">Seleccionar...</html:option>
							<html:optionsCollection name="encComAjuAdapterVO" property="listDetAju" label="ordConCue.cuenta.numeroCuenta" value="id" />
						</html:select>
					</td>					
				</logic:lessEqual>
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- ComAju -->
	

	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
	   	     	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="encComAjuAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="encComAjuAdapterVO" property="act" value="agregar">
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
<!-- comAjuEditAdapter.jsp -->
