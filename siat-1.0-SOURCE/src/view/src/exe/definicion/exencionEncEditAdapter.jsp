<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/exe/AdministrarEncExencion.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="exe" key="exe.exencionEditAdapter.title"/></h1>		

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
				<logic:equal name="encExencionAdapterVO" property="act" value="modificar">
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal">
						<bean:write name="encExencionAdapterVO" property="exencion.recurso.desRecurso"/>
					</td>	
				</logic:equal>
				<logic:equal name="encExencionAdapterVO" property="act" value="agregar">
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal">
						<html:select name="encExencionAdapterVO" property="exencion.recurso.id" styleClass="select" >
							<bean:define id="includeRecursoList" name="encExencionAdapterVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="encExencionAdapterVO" property="exencion.recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
					</td>
				</logic:equal>				
			</tr>
			<tr>
				<logic:equal name="encExencionAdapterVO" property="act" value="modificar">
					<td><label><bean:message bundle="exe" key="exe.exencion.codExencion.label"/>: </label></td>
					<td class="normal">
						<bean:write name="encExencionAdapterVO" property="exencion.codExencion"/>
				 	</td>
				 </logic:equal>
				<logic:equal name="encExencionAdapterVO" property="act" value="agregar">
					<td><label>(*)&nbsp;<bean:message bundle="exe" key="exe.exencion.codExencion.label"/>: </label></td>
					<td class="normal">	
						<html:text name="encExencionAdapterVO" property="exencion.codExencion" size="15" maxlength="20" />						
					</td>
				</logic:equal>	
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="exe" key="exe.exencion.desExencion.label"/>: </label></td>
				<td class="normal"><html:text name="encExencionAdapterVO" property="exencion.desExencion" size="20" maxlength="100"/></td>			
			</tr>
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="exe" key="exe.exencion.actualizaDeuda.label"/>: </label></td>
				<td class="normal">
					<html:select name="encExencionAdapterVO" property="exencion.actualizaDeuda.id" styleClass="select">
						<html:optionsCollection name="encExencionAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="exe" key="exe.exencion.enviaJudicial.label"/>: </label></td>
				<td class="normal">
					<html:select name="encExencionAdapterVO" property="exencion.enviaJudicial.id" styleClass="select">
						<html:optionsCollection name="encExencionAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="exe" key="exe.exencion.enviaCyQ.label"/>: </label></td>
				<td class="normal">
					<html:select name="encExencionAdapterVO" property="exencion.enviaCyQ.id" styleClass="select">
						<html:optionsCollection name="encExencionAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="exe" key="exe.exencion.afectaEmision.label"/>: </label></td>
				<td class="normal">
					<html:select name="encExencionAdapterVO" property="exencion.afectaEmision.id" styleClass="select">
						<html:optionsCollection name="encExencionAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="exe" key="exe.exencion.esParcial.label"/>: </label></td>
				<td class="normal">
					<html:select name="encExencionAdapterVO" property="exencion.esParcial.id" styleClass="select">
						<html:optionsCollection name="encExencionAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
			
			<tr>	
				<td><label><bean:message bundle="exe" key="exe.exencion.aplicaMinimo.label"/>: </label></td>
				<td class="normal">
					<html:select name="encExencionAdapterVO" property="exencion.aplicaMinimo.id" styleClass="select" onchange="submitForm('paramAplicaMinimo','')">
						<html:optionsCollection name="encExencionAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
			
			<logic:equal name="encExencionAdapterVO" property="exencion.aplicaMinimo.id" value="1">
				<tr>
					<td><label><bean:message bundle="exe" key="exe.exencion.montoMinimo.label"/>: </label></td>
					<td class="normal"><html:text name="encExencionAdapterVO" property="exencion.montoMinimoView" size="20" maxlength="100"/></td>			
				</tr>
			</logic:equal>
			
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="exe" key="exe.exencion.permiteManPad.label"/>: </label></td>
				<td class="normal">
					<html:select name="encExencionAdapterVO" property="exencion.permiteManPad.id" styleClass="select">
						<html:optionsCollection name="encExencionAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
			
			<!-- Inclucion de Caso -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td>
					<bean:define id="IncludedVO" name="encExencionAdapterVO" property="exencion"/>
					<bean:define id="voName" value="exencion" />
					<%@ include file="/cas/caso/includeCaso.jsp" %>
				</td>
			</tr>
			<!-- Fin Inclucion de Caso -->	
			
		</table>
	</fieldset>
	<!-- Exencion -->
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right">
				<logic:equal name="encExencionAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="encExencionAdapterVO" property="act" value="agregar">
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
<!-- Fin formulario -->