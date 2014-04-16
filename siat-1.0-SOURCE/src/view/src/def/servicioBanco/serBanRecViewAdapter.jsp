<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarSerBanRec.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.serBanRecAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<!-- Servicio Banco Enc -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.servicioBanco.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.servicioBanco.codServicioBanco.ref"/>: </label></td>
					<td class="normal"><bean:write name="serBanRecAdapterVO" property="serBanRec.servicioBanco.codServicioBanco"/></td>
			
					<td><label><bean:message bundle="def" key="def.servicioBanco.desServicioBanco.ref"/>: </label></td>
					<td class="normal"><bean:write name="serBanRecAdapterVO" property="serBanRec.servicioBanco.desServicioBanco"/></td>
				</tr>
			</table>
		</fieldset>
		<!-- Servicio Banco Enc-->

		<!-- SerBanRec -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.serBanRec.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.serBanRec.desRecurso.label"/>: </label></td>
					<td class="normal"><bean:write name="serBanRecAdapterVO" property="serBanRec.recurso.desRecurso"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.serBanRec.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="serBanRecAdapterVO" property="serBanRec.fechaDesdeView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.serBanRec.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="serBanRecAdapterVO" property="serBanRec.fechaHastaView"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- SerBanRec -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
  	    		<td align="right" width="50%">	   	    	
					<logic:equal name="serBanRecAdapterVO" property="act" value="eliminar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
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
		
			
