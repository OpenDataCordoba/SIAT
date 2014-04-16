<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarDetAjuDocSop.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="ef" key="ef.detAjuDocSopEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- DetAjuDocSop -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.detAjuDocSop.title"/></legend>
		
		<table class="tabladatos">
		<!-- DocSop -->
		<tr>	
			<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.docSop.label"/>: </label></td>
			<td class="normal">
				<html:select name="detAjuDocSopAdapterVO" property="detAjuDocSop.docSop.id" styleClass="select" onchange="submitForm('paramDocumentacion', '');">
					<html:optionsCollection name="detAjuDocSopAdapterVO" property="listDocSop" label="desDocSop" value="id" />
				</html:select>
			</td>					
		</tr>
		<!-- fechaGenerada -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.detAjuDocSop.fechaGenerada.label"/>: </label></td>
			<td class="normal">
				<bean:write name="detAjuDocSopAdapterVO" property="detAjuDocSop.fechaGeneradaView"/>
			</td>
		</tr>
		<!-- fechaNotificada -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.detAjuDocSop.fechaNotificada.label"/>: </label></td>
			<td class="normal">
				<html:text name="detAjuDocSopAdapterVO" property="detAjuDocSop.fechaNotificadaView" styleId="fechaNotificadaView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaNotificadaView');" id="a_fechaNotificadaView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
		<!-- notificadaPor -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.detAjuDocSop.notificadaPor.label"/>: </label></td>
			<td class="normal"><html:text name="detAjuDocSopAdapterVO" property="detAjuDocSop.notificadaPor" size="20" maxlength="100"/></td>			
		</tr>
		<!-- Ubicación Plantilla -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.docSop.plantilla.label"/>: </label></td>
			<td class="normal"><html:text name="detAjuDocSopAdapterVO" property="detAjuDocSop.docSop.plantilla" size="20" maxlength="100"/>			
		      <a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="detAjuDocSopAdapterVO" property="detAjuDocSop.docSop.plantilla"/>');">
					<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/excel0.gif"/>
			  </a>	
			 </td>
		</tr>
		<!-- observacion -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.detAjuDocSop.observacion.label"/>: </label></td>
			<td class="normal"><html:textarea name="detAjuDocSopAdapterVO" property="detAjuDocSop.observacion"/></td>			
		</tr>
			
		</table>
	</fieldset>	
	<!-- DetAjuDocSop -->
	
	<table class="tablabotones" width="100%">
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="detAjuDocSopAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="detAjuDocSopAdapterVO" property="act" value="agregar">
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
	<input type="hidden" name="fileParam" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
<!-- detAjuDocSopEditAdapter.jsp -->
