<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/def/AdministrarCodEmi.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.codEmiViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- CodEmi -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.codEmi.title"/></legend>
			<table class="tabladatos">
				<tr>
					<!-- Nombre -->
					<td><label><bean:message bundle="def" key="def.codEmi.nombre.label"/>: </label></td>
					<td class="normal"><bean:write name="codEmiAdapterVO" property="codEmi.nombre"/></td>
				</tr>

				<tr>
					<!-- Descripcion -->
					<td><label><bean:message bundle="def" key="def.codEmi.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="codEmiAdapterVO" property="codEmi.descripcion"/></td>
				</tr>

				<tr>
					<!-- Tipo de Codigo -->
					<td><label><bean:message bundle="def" key="def.codEmi.tipCodEmi.label"/>: </label></td>
					<td class="normal"><bean:write name="codEmiAdapterVO" property="codEmi.tipCodEmi.desTipCodEmi"/></td>
				</tr>

				<tr>
					<!-- Recurso -->
					<td><label><bean:message bundle="def" key="def.codEmi.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="codEmiAdapterVO" property="codEmi.recurso.desRecurso"/></td>
				</tr>

				<tr>
               		<td><label><bean:message bundle="def" key="def.codEmi.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="codEmiAdapterVO" property="codEmi.fechaDesdeView" /></td>

					<logic:notEqual name="codEmiAdapterVO" property="act" value="desactivar">
	               		<td><label><bean:message bundle="def" key="def.codEmi.fechaHasta.label"/>: </label></td>
						<td class="normal"><bean:write name="codEmiAdapterVO" property="codEmi.fechaHastaView" /></td>
					</logic:notEqual>
						
					<logic:equal name="codEmiAdapterVO" property="act" value="desactivar">
						<td><label>(*)&nbsp;<bean:message bundle="def" key="def.codEmi.fechaHasta.label"/>: </label></td>
						<td class="normal">
							<html:text name="codEmiAdapterVO" property="codEmi.fechaHastaView" styleId="fechaHastaView" size="10" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
							</a>
						</td>
					</logic:equal>
				</tr>
				
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="codEmiAdapterVO" property="codEmi.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- CodEmi -->

		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="def" key="def.histCodEmi.label"/></caption>
			<tbody>	    	
			    <tr>
					<th align="left"><bean:message bundle="def" key="def.histCodEmi.fechaUltMdf.label"/></th>
					<th align="left"><bean:message bundle="def" key="def.histCodEmi.usuario.label"/></th>
					<th align="left"><bean:message bundle="def" key="def.histCodEmi.desHistCodEmi.label"/></th>
				</tr>
				<logic:iterate id="HistCodEmi" name="codEmiAdapterVO" property="codEmi.listHistCodEmi">
					<tr>
						<td><bean:write name="HistCodEmi" property="fechaUltMdfView"/>&nbsp;</td>
						<td><bean:write name="HistCodEmi" property="usuario"/>&nbsp;</td>
						<td><bean:write name="HistCodEmi" property="desHistCodEmi" />&nbsp;</td>
					</tr>
				</logic:iterate>
			</tbody>
		</table>			

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="codEmiAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="codEmiAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="codEmiAdapterVO" property="act" value="desactivar">
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
<!-- codEmiViewAdapter.jsp -->