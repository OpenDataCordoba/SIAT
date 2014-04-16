<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/frm/AdministrarForCam.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="frm" key="frm.forCamEditAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
	<!-- ForCam -->
	<fieldset>
		<legend><bean:message bundle="frm" key="frm.forCam.title"/></legend>
		
		<table class="tabladatos">
		<!-- codForCam -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="frm" key="frm.forCam.codForCam.label"/>: </label></td>
			<td class="normal" colspan="3">
					<html:text name="forCamAdapterVO" property="forCam.codForCam" size="15" maxlength="20" />
			</td>
		</tr>
		
		<!-- largoMax -->
		<tr>
			<td><label><bean:message bundle="frm" key="frm.forCam.largoMax.label"/>: </label></td>
			<td class="normal" colspan="3">
					<html:text name="forCamAdapterVO" property="forCam.largoMax" size="5" maxlength="4" />
			</td>
		</tr>
		
		<!-- desForCam -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="frm" key="frm.forCam.desForCam.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="forCamAdapterVO" property="forCam.desForCam" size="40" maxlength="100"/></td>			
		</tr>
		
		<!-- valorDefecto -->
		<tr>
			<td><label><bean:message bundle="frm" key="frm.forCam.valorDefecto.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="forCamAdapterVO" property="forCam.valorDefecto" size="40" maxlength="100"/></td>			
		</tr>
				
		<!-- fechaDesde -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="frm" key="frm.forCam.fechaDesde.label"/>: </label></td>
			<td class="normal">
				<html:text name="forCamAdapterVO" property="forCam.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		<!-- fechaHasta -->
			<td><label><bean:message bundle="frm" key="frm.forCam.fechaHasta.label"/>: </label></td>
			<td class="normal">
				<html:text name="forCamAdapterVO" property="forCam.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- ForCam -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="forCamAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="forCamAdapterVO" property="act" value="agregar">
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
