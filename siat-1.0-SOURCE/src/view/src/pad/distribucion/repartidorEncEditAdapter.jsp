<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/pad/AdministrarEncRepartidor.do">

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
					
		<!-- Repartidor Enc -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.repartidor.title"/></legend>
			
			<table class="tabladatos">
				<!-- Recurso -->		
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal">
					<logic:equal name="encRepartidorAdapterVO" property="act" value="agregar">		
						<html:select name="encRepartidorAdapterVO" property="repartidor.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');">
							<html:optionsCollection name="encRepartidorAdapterVO" property="listRecurso" label="desRecurso" value="id" />
						</html:select>
		  			</logic:equal>
					<logic:equal name="encRepartidorAdapterVO" property="act" value="modificar">		
						<bean:write name="encRepartidorAdapterVO" property="repartidor.recurso.desRecurso"/>
					</logic:equal>
					</td>
				</tr>

				<!-- Tipo Repartidor -->		
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.tipoRepartidor.label"/>: </label></td>
					<td class="normal">
						<logic:equal name="encRepartidorAdapterVO" property="act" value="agregar">		
						<logic:equal name="encRepartidorAdapterVO" property="paramRecurso" value="1">		
							<html:select name="encRepartidorAdapterVO" property="repartidor.tipoRepartidor.id" styleClass="select" disabled="false" onchange="submitForm('paramTipoRepartidor', '');">
								<html:optionsCollection name="encRepartidorAdapterVO" property="listTipoRepartidor" label="desTipoRepartidor" value="id" />
							</html:select>
						</logic:equal>
						<logic:equal name="encRepartidorAdapterVO" property="paramRecurso" value="0">		
							<html:select name="encRepartidorAdapterVO" property="repartidor.tipoRepartidor.id" styleClass="select" disabled="true">
								<html:optionsCollection name="encRepartidorAdapterVO" property="listTipoRepartidor" label="desTipoRepartidor" value="id" />
							</html:select>
						</logic:equal>
						</logic:equal>
						<logic:equal name="encRepartidorAdapterVO" property="act" value="modificar">		
							<bean:write name="encRepartidorAdapterVO" property="repartidor.tipoRepartidor.desTipoRepartidor"/>
						</logic:equal>
					</td>	
				</tr>
				<!-- Persona -->	
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.persona.label"/>: </label></td>
					<td class="normal">			
						<html:text name="encRepartidorAdapterVO" property="repartidor.desRepartidor" size="50" maxlength="100"/>							
					</td>
				</tr>
				
				<!-- <tr>
					<td><label>(*)&nbsp;bean:message bundle="pad" key="pad.persona.label"/>: </label></td>
					<td class="normal">			
						html:text name="encRepartidorAdapterVO" property="repartidor.personaView" size="50" maxlength="100" disabled="true"/>
							html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('buscarPersona', '');">
								bean:message bundle="pad" key="pad.repartidorAdapter.adm.button.buscarPersona"/>
							/html:button>
					</td>
				</tr>-->
				
				
				<!-- Legajo -->		
				<tr>
					<td><label><bean:message bundle="pad" key="pad.repartidor.legajo.label"/>: </label></td>
					<td class="normal"><html:text name="encRepartidorAdapterVO" property="repartidor.legajo" size="15" maxlength="50" styleClass="datos"/></td>					
				</tr>
				<!-- Zona -->		
				<tr>
					<td><label><bean:message bundle="def" key="def.zona.label"/>: </label></td>
					<td class="normal">
						<html:select name="encRepartidorAdapterVO" property="repartidor.zona.id" styleClass="select">
							<html:optionsCollection name="encRepartidorAdapterVO" property="listZona" label="descripcion" value="id" />
						</html:select>
					</td>
				</tr>
				<!-- Broche -->	
				<tr>
					<td><label><bean:message bundle="pad" key="pad.broche.label"/>: </label></td>
					<td class="normal">
						<logic:equal name="encRepartidorAdapterVO" property="paramRecurso" value="1">		
							<html:select name="encRepartidorAdapterVO" property="repartidor.broche.id" styleClass="select" disabled="false">
								<html:optionsCollection name="encRepartidorAdapterVO" property="listBroche" label="represent" value="id" />
							</html:select>
						</logic:equal>
						<logic:equal name="encRepartidorAdapterVO" property="paramRecurso" value="0">		
							<html:select name="encRepartidorAdapterVO" property="repartidor.broche.id" styleClass="select" disabled="true">
								<html:optionsCollection name="encRepartidorAdapterVO" property="listBroche" label="represent" value="id" />
							</html:select>
						</logic:equal>
					</td>	
				</tr>
			</table>
		</fieldset>
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="encRepartidorAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="encRepartidorAdapterVO" property="act" value="agregar">
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
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
		