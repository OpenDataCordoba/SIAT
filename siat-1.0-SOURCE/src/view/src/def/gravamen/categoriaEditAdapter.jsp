<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/def/AdministrarCategoria.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="def" key="def.categoriaEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- Categoria -->
	<fieldset>
		<legend><bean:message bundle="def" key="def.categoria.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.categoria.desCategoria.ref"/>: </label></td>
				<td class="normal">
						<html:text name="categoriaAdapterVO" property="categoria.desCategoria" size="30" maxlength="100" />
				</td>
			</tr>
			<tr>
				<logic:equal name="categoriaAdapterVO" property="act" value="agregar">
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipo.label"/>: </label></td>
					<td class="normal">
						<html:select name="categoriaAdapterVO" property="categoria.tipo.id" styleClass="select" >
							<html:optionsCollection name="categoriaAdapterVO" property="listTipo" label="desTipo" value="id" />
						</html:select>
					</td>
				</logic:equal>
				<logic:equal name="categoriaAdapterVO" property="act" value="modificar">
					<td><label><bean:message bundle="def" key="def.tipo.label"/>: </label></td>
					<td class="normal">
						<bean:write name="categoriaAdapterVO" property="categoria.tipo.desTipo"/>
					</td>
				</logic:equal>				
			</tr>
		</table>
	</fieldset>	
	<!-- Categoria -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="categoriaAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="categoriaAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>
				</logic:equal>
   	    	</td>   	    	
   	    </tr>
   	</table>

	<input type="text" style="display:none"/>
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>
		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->