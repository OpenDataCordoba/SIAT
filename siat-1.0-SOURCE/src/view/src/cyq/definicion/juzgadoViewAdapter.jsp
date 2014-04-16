<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/cyq/AdministrarJuzgado.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="cyq" key="cyq.juzgadoViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Juzgado -->
		<fieldset>
			<legend><bean:message bundle="cyq" key="cyq.juzgado.title"/></legend>
			<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.juzgado.desJuzgado.label"/>: </label></td>
				<td class="normal"><bean:write name="juzgadoAdapterVO" property="juzgado.desJuzgado"/></td>				
			</tr>
			
				<!-- <#BeanWrites#> -->
			</table>
		</fieldset>	
		<!-- Juzgado -->
      <!-- ${Bean_Detalle} -->
		<logic:equal name="juzgadoAdapterVO" property="act" value="ver">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="cyq" key="cyq.juzgado.listAbogado.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="juzgadoAdapterVO" property="juzgado.listAbogado">
				    	<tr>
							<th align="left"><bean:message bundle="cyq" key="cyq.juzgado.descripcion.label"/></th>
							<th align="left"><bean:message bundle="cyq" key="cyq.juzgado.domicilio.label"/></th>							
							<th align="left"><bean:message bundle="cyq" key="cyq.juzgado.telefono.label"/></th>
						</tr>
						<logic:iterate id="abogadoVO" name="juzgadoAdapterVO" property="juzgado.listAbogado">
				        	<tr>
								<td><bean:write name="abogadoVO" property="descripcion"/>&nbsp;</td>
								<td><bean:write name="abogadoVO" property="domicilio"/>&nbsp;</td>								
								<td><bean:write name="abogadoVO" property="telefono"/>&nbsp;</td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="juzgadoAdapterVO" property="juzgado.listAbogado">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
			</table>
		</logic:equal>
		<!-- ${Bean_Detalle} -->
		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="left">
		   	    	<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						<bean:message bundle="base" key="abm.button.imprimir"/>
					</html:button>
	   	    	</td>   	   	    			
	   	    	<td align="right">
					<logic:equal name="juzgadoAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="juzgadoAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="juzgadoAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='juzgadoAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	     	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
