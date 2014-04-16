<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/esp/AdministrarEntHab.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="esp" key="esp.habilitacionAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>

		<!-- EntHab -->
		<fieldset>
			<legend><bean:message bundle="esp" key="esp.entHab.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="esp" key="esp.precioEvento.label"/>: </label></td>
					<td class="normal">	
					<logic:equal name="entHabAdapterVO" property="act" value="agregar">
							<html:select name="entHabAdapterVO" property="entHab.precioEvento.id" styleClass="select">
								<html:optionsCollection name="entHabAdapterVO" property="listPrecioEvento" label="descripcion" value="id" />
							</html:select>
					</logic:equal>
					<logic:equal name="entHabAdapterVO" property="act" value="modificar">		
						<bean:write name="entHabAdapterVO" property="entHab.precioEvento.descripcion"/>
					</logic:equal>
					</td>
				</tr>	
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="esp" key="esp.entHab.nroDesde.label"/>: </label></td>
					<td class="normal"><html:text name="entHabAdapterVO" property="entHab.nroDesdeView" size="10" maxlength="22" /></td>
					<td><label>(*)&nbsp;<bean:message bundle="esp" key="esp.entHab.nroHasta.label"/>: </label></td>
					<td class="normal"><html:text name="entHabAdapterVO" property="entHab.nroHastaView" size="10" maxlength="22" /></td>
				</tr>	
				<tr>
					<td><label><bean:message bundle="esp" key="esp.entHab.serie.label"/>: </label></td>
					<td class="normal"><html:text name="entHabAdapterVO" property="entHab.serie" size="10" maxlength="10" /></td>
					<td><label><bean:message bundle="esp" key="esp.entHab.descripcion.label"/>: </label></td>
					<td class="normal"><html:text name="entHabAdapterVO" property="entHab.descripcion" size="10" maxlength="10" /></td>
				</tr>	
			</table>
		</fieldset>
		<!-- Fin EntHab -->
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="entHabAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="entHabAdapterVO" property="act" value="agregar">
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