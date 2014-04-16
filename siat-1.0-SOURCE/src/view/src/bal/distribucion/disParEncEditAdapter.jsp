<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarEncDisPar.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.disParAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
				
		<!-- DisPar Enc -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.disPar.title"/></legend>
			
			<table class="tabladatos">
				<!-- Recurso -->		
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal">
					<logic:equal name="encDisParAdapterVO" property="act" value="agregar">		
						<html:select name="encDisParAdapterVO" property="disPar.recurso.id" styleClass="select" >
							<bean:define id="includeRecursoList" name="encDisParAdapterVO" property="listRecurso"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
		  			</logic:equal>
					<logic:equal name="encDisParAdapterVO" property="act" value="modificar">		
						<bean:write name="encDisParAdapterVO" property="disPar.recurso.desRecurso"/>
					</logic:equal>
					</td>
				</tr>
				<!-- DesDisPar -->		
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.disPar.desDisPar.label"/>: </label></td>
					<td class="normal"><html:text name="encDisParAdapterVO" property="disPar.desDisPar" size="35" maxlength="100" styleClass="datos"/></td>					
				</tr>
				<tr>
					<!-- TipoImporte -->		
					<logic:equal name="encDisParAdapterVO" property="act" value="agregar">		
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.tipoImporte.label"/>: </label></td>
						<td class="normal">	
						<html:select name="encDisParAdapterVO" property="disPar.tipoImporte.id" styleClass="select">
							<html:optionsCollection name="encDisParAdapterVO" property="listTipoImporte" label="desTipoImporte" value="id" />
						</html:select>
						</td>
		  			</logic:equal>
					<logic:equal name="encDisParAdapterVO" property="act" value="modificar">		
						<logic:equal name="encDisParAdapterVO" property="disPar.paramTipoImporte" value="true">
							<td><label>&nbsp;<bean:message bundle="bal" key="bal.tipoImporte.label"/>: </label></td>
							<td class="normal">	
								<bean:write name="encDisParAdapterVO" property="disPar.tipoImporte.desTipoImporte"/>
							</td>
						</logic:equal>
					</logic:equal>
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
					<logic:equal name="encDisParAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="encDisParAdapterVO" property="act" value="agregar">
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
		