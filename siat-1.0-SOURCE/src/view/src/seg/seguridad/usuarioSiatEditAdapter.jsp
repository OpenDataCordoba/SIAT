<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/seg/AdministrarUsuarioSiat.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="seg" key="seg.usuarioSiatEditAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
	<!-- UsuarioSiat -->
	<fieldset>
		<legend><bean:message bundle="seg" key="seg.usuarioSiat.title"/></legend>
		
		<table class="tabladatos">
			<!-- nombre -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="seg" key="seg.usuarioSiat.usuarioSIAT.label"/>: </label></td>
				<td class="normal">
					<logic:equal name="usuarioSiatAdapterVO" property="act" value="modificar">
						<bean:write name="usuarioSiatAdapterVO" property="usuarioSiat.usuarioSIAT"/>
				 	</logic:equal>
					<logic:equal name="usuarioSiatAdapterVO" property="act" value="agregar">
						<html:text name="usuarioSiatAdapterVO" property="usuarioSiat.usuarioSIAT" size="15" maxlength="20" />
				 	</logic:equal>	
				</td>
			</tr>
			<!-- Area -->
			<tr>	
				<td><label><bean:message bundle="def" key="def.area.label"/>: </label></td>
				<td class="normal">
					<html:select name="usuarioSiatAdapterVO" property="usuarioSiat.area.id" styleClass="select">
						<html:optionsCollection name="usuarioSiatAdapterVO" property="listArea" label="desArea" value="id" />
					</html:select>
				</td>					
			</tr>
			<!-- Procurador -->
			<tr>	
				<td><label><bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
				<td class="normal">
					<html:select name="usuarioSiatAdapterVO" property="usuarioSiat.procurador.id" styleClass="select">
						<html:optionsCollection name="usuarioSiatAdapterVO" property="listProcurador" label="descripcion" value="id" />
					</html:select>
				</td>					
			</tr>

			<!-- Abogado -->
			<tr>	
				<td><label><bean:message bundle="cyq" key="cyq.abogado.label"/>: </label></td>
				<td class="normal">
					<html:select name="usuarioSiatAdapterVO" property="usuarioSiat.abogado.id" styleClass="select">
						<html:optionsCollection name="usuarioSiatAdapterVO" property="listAbogado" label="descripcion" value="id" />
					</html:select>
				</td>					
			</tr>

			<!-- Investigador -->
			<tr>	
				<td><label><bean:message bundle="ef" key="ef.investigador.label"/>: </label></td>
				<td class="normal">
					<html:select name="usuarioSiatAdapterVO" property="usuarioSiat.investigador.id" styleClass="select">
						<html:optionsCollection name="usuarioSiatAdapterVO" property="listInvestigador" label="desInvestigador" value="id" />
					</html:select>
				</td>					
			</tr>
			
			<!-- Inspector -->
			<tr>	
				<td><label><bean:message bundle="ef" key="ef.inspector.label"/>: </label></td>
				<td class="normal">
					<html:select name="usuarioSiatAdapterVO" property="usuarioSiat.inspector.id" styleClass="select">
						<html:optionsCollection name="usuarioSiatAdapterVO" property="listInspector" label="desInspector" value="id" />
					</html:select>
				</td>					
			</tr>			
			
			<!-- Supervisor -->
			<tr>	
				<td><label><bean:message bundle="ef" key="ef.supervisor.label"/>: </label></td>
				<td class="normal">
					<html:select name="usuarioSiatAdapterVO" property="usuarioSiat.supervisor.id" styleClass="select">
						<html:optionsCollection name="usuarioSiatAdapterVO" property="listSupervisor" label="desSupervisor" value="id" />
					</html:select>
				</td>					
			</tr>
			
			<!-- Mandatario -->
			<tr>	
				<td><label><bean:message bundle="gde" key="gde.mandatario.label"/>: </label></td>
				<td class="normal">
					<html:select name="usuarioSiatAdapterVO" property="usuarioSiat.mandatario.id" styleClass="select">
						<html:optionsCollection name="usuarioSiatAdapterVO" property="listMandatario" label="descripcion" value="id" />
					</html:select>
				</td>					
			</tr>
			
		</table>
	</fieldset>	
	<!-- UsuarioSiat -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="usuarioSiatAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="usuarioSiatAdapterVO" property="act" value="agregar">
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