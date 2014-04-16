<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/swe/submitForm.js"%>	    
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/seg/AdministrarCloneUsrApl.do">

		<!-- Mensajes/Advertencias -->
		<%@ include file="/swe/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="seg" key="cloneUsrAplAdapter.title"/></h1>
		
		<p><bean:message bundle="seg" key="cloneUsrAplAdapter.label.legend"/></p>
		
		<!-- UsrApl -->
		<fieldset>
			<legend><bean:message bundle="seg" key="cloneUsrAplAdapter.subtitle"/></legend>
			
				<table class="tabladatos">
					<!-- Permite Web -->
					<tr>
						<td><label><bean:message bundle="seg" key="cloneUsrAplAdapter.aplicacion.label.codigo"/>:</label></td>
						<td class="normal"><bean:write name="cloneUsrAplAdapterVO" property="usrApl.aplicacion.codigo"/></td>
						<td><label><bean:message bundle="seg" key="cloneUsrAplAdapter.aplicacion.label.descripcion"/>:</label></td>
						<td class="normal"><bean:write name="cloneUsrAplAdapterVO" property="usrApl.aplicacion.descripcion"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="seg" key="cloneUsrAplAdapter.usrApl.label.usernameToClone"/>:</label></td>
						<td class="normal"><bean:write name="cloneUsrAplAdapterVO" property="usrAplToClone.username"/></td>				
						<td><label><bean:message bundle="seg" key="cloneUsrAplAdapter.permiteWeb.label"/>:</label></td>
						<td class="normal">
							<bean:define id="listPermiteWeb" name="cloneUsrAplAdapterVO" property="listPermiteWeb"/>
							<html:select name="cloneUsrAplAdapterVO" property="usrApl.permiteWeb.id">
								<html:optionsCollection name="listPermiteWeb" label="value" value="id"/>
							</html:select>
	                    </td>
	                </tr>
					<tr>
						<td><label><bean:message bundle="seg" key="cloneUsrAplAdapter.usrApl.label.username"/>:</label></td>
						<td class="normal"><html:text name="cloneUsrAplAdapterVO" property="usrApl.username" size="15" maxlength="10"/></td>
					    <input type="text" style="display:none"/>
						<td><label><bean:message bundle="seg" key="cloneUsrAplAdapter.usrApl.label.estado"/>:</label></td>											   
						<td class="normal">
								<bean:define id="listEstado" name="cloneUsrAplAdapterVO" property="listEstado"/>
								<html:select name="cloneUsrAplAdapterVO" property="usrApl.estado.id">
									<html:optionsCollection name="listEstado" label="value" value="id"/>
								</html:select>
                        </td>                    
                    </tr>
                    
                    <logic:equal name="cloneUsrAplAdapterVO" property="usrApl.aplicacion.tipoAuth.id" value="1">
					<tr>
						<td><label><bean:message bundle="seg" key="cloneUsrAplAdapter.usrApl.label.userpass"/>:</label></td>
						<td class="normal"><html:password name="cloneUsrAplAdapterVO" property="usrApl.password" size="15" maxlength="10"/></td>
						
						<td><label><bean:message bundle="seg" key="cloneUsrAplAdapter.usrApl.label.userpassretype"/>:</label></td>
						<td class="normal"><html:password name="cloneUsrAplAdapterVO" property="usrApl.passRetype" size="15" maxlength="10"/></td>								
						
					</tr>		
					</logic:equal>
		
				                    
				</table>
		</fieldset>	
		<!-- UsrApl -->
		
		<!-- lista de aplicaciones permitidas-->
		<logic:equal name="cloneUsrAplAdapterVO" property="usrApl.aplicacion.id" value="1">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:message bundle="seg" key="cloneUsrAplAdapter.listaAplicaciones.label"/></caption>
               	<tbody>
               	<tr>
					<th width="1">&nbsp;</th> <!-- Seleccionar-->
					<th align="left"><bean:message bundle="seg" key="cloneUsrAplAdapter.aplicacion.label.codigo"/></th>
					<th align="left"><bean:message bundle="seg" key="cloneUsrAplAdapter.aplicacion.label.descripcion"/></th>
				</tr>
				<logic:iterate name="cloneUsrAplAdapterVO" property="listAplicaciones" id="app">
					<tr>
							<!-- Check para seleccion multiple -->
								<td align="center">
									<html:multibox name="cloneUsrAplAdapterVO" property="listIdsAppSelected">
										<bean:write name="app" property="id" bundle="base" formatKey="general.format.id"/>
									</html:multibox>
								</td>						
						<td><bean:write name="app" property="codigo"/></td>
						<td><bean:write name="app" property="descripcion"/></td>
					</tr>			
				</logic:iterate>
			</table>
		</logic:equal>		
		
		
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
				<td align="right" width="50%">
					<logic:equal name="userSession" property="navModel.act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					&nbsp;
					<logic:equal name="userSession" property="navModel.act" value="agregar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
							<bean:message bundle="base" key="abm.button.agregar"/>
						</html:button>
					</logic:equal>
					&nbsp;
					<logic:equal name="userSession" property="navModel.act" value="clonar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('clonar', '');">
							<bean:message bundle="base" key="abm.button.clonar"/>
						</html:button>
					</logic:equal>
				</td>
	   	    </tr>
	   	</table>
	   	
		<html:hidden name="cloneUsrAplAdapterVO" property="usrApl.aplicacion.id"/>
		<html:hidden name="cloneUsrAplAdapterVO" property="usrApl.aplicacion.codigo" />
		<html:hidden name="cloneUsrAplAdapterVO" property="usrApl.aplicacion.descripcion" />						
		<html:hidden name="cloneUsrAplAdapterVO" property="usrApl.id" />
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
