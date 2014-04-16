<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/exe/AdministrarTipoSujeto.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="exe" key="exe.tipoSujetoViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
		
				</td>				
				<td align="right">
		 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- TipoSujeto -->
		<fieldset>
			<legend><bean:message bundle="exe" key="exe.tipoSujeto.title"/></legend>
			<table class="tabladatos">
				<!-- Codigo -->
				<tr>
					<td><label><bean:message bundle="exe" key="exe.tipoSujeto.codTipoSujeto.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoSujetoAdapterVO" property="tipoSujeto.codTipoSujeto"/></td>
				</tr>
				<!-- Descricion -->
				<tr>
					<td><label><bean:message bundle="exe" key="exe.tipoSujeto.desTipoSujeto.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoSujetoAdapterVO" property="tipoSujeto.desTipoSujeto"/></td>
				</tr>
				
				
								
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoSujetoAdapterVO" property="tipoSujeto.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- TipoSujeto -->
		<!-- TipSujExe -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="exe" key="exe.tipoSujeto.listTipSujExe.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="tipoSujetoAdapterVO" property="tipoSujeto.listTipSujExe">	    	
			    	<tr>
						<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
						<th align="left"><bean:message bundle="exe" key="exe.exencion.label"/></th>						
					</tr>
					<logic:iterate id="TipSujExeVO" name="tipoSujetoAdapterVO" property="tipoSujeto.listTipSujExe">
			
						<tr>										
							<td><bean:write name="TipSujExeVO" property="exencion.recurso.desRecurso"/>&nbsp;</td>
							<td><bean:write name="TipSujExeVO" property="exencion.desExencion"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="tipoSujetoAdapterVO" property="tipoSujeto.listTipSujExe">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				
			</tbody>
		</table>
		<!-- TipSujExe -->
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="tipoSujetoAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="tipoSujetoAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="tipoSujetoAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
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
