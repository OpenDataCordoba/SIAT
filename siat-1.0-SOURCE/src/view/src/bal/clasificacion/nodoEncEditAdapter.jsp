<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarEncNodo.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.nodoAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
				
		<!-- Nodo Enc -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.nodo.title"/></legend>
			
			<table class="tabladatos">
				<!-- Clasificador -->		
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.clasificador.label"/>: </label></td>
					<td class="normal">
					<logic:equal name="encNodoAdapterVO" property="act" value="agregar">		
					<logic:equal name="encNodoAdapterVO" property="paramClasificador" value="false">		
						<html:select name="encNodoAdapterVO" property="nodo.clasificador.id" styleClass="select">
							<html:optionsCollection name="encNodoAdapterVO" property="listClasificador" label="descripcion" value="id" />
						</html:select>
					</logic:equal>
					<logic:equal name="encNodoAdapterVO" property="paramClasificador" value="true">		
						<bean:write name="encNodoAdapterVO" property="nodo.clasificador.descripcion"/>
					</logic:equal>
					</logic:equal>
					<logic:equal name="encNodoAdapterVO" property="act" value="modificar">		
						<bean:write name="encNodoAdapterVO" property="nodo.clasificador.descripcion"/>
					</logic:equal>
					</td>
					<!-- Nivel -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.nivel.label"/>: </label></td>
					<td class="normal"><bean:write name="encNodoAdapterVO" property="nodo.nivelView"/></td>
				</tr>
				<!-- Codigo -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.codigo.label"/>: </label></td>
					<td class="normal"><html:text name="encNodoAdapterVO" property="nodo.codigo" size="15" maxlength="2" styleClass="datos"/></td>					
				</tr>
				<!-- Descripcion -->		
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.nodo.descripcion.label"/>: </label></td>
					<td colspan="3" class="normal"><html:text name="encNodoAdapterVO" property="nodo.descripcion" size="35" maxlength="255" styleClass="datos"/></td>					
				</tr>
				<tr>
				<logic:equal name="encNodoAdapterVO" property="nodo.esNodoRaiz" value="false">		
						<!-- Clave Padre-->		
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.clave.label"/>&nbsp;<bean:message bundle="bal" key="bal.nodo.nodoPadre.label"/>: </label></td>
						<td class="normal"><bean:write name="encNodoAdapterVO" property="nodo.nodoPadre.clave"/></td>
				</tr>
				<tr>
						<!-- Descripcion Padre -->		
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.descripcion.label"/>&nbsp;<bean:message bundle="bal" key="bal.nodo.nodoPadre.label"/>: </label></td>
						<td colspan="3" class="normal"><bean:write name="encNodoAdapterVO" property="nodo.nodoPadre.descripcion"/></td>
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
					<logic:equal name="encNodoAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="encNodoAdapterVO" property="act" value="agregar">
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
		