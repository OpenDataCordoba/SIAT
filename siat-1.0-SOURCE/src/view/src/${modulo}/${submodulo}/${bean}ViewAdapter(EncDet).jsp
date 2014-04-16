<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/${modulo}/Administrar${Bean}.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="${modulo}" key="${modulo}.${bean}ViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- ${Bean} -->
		<fieldset>
			<legend><bean:message bundle="${modulo}" key="${modulo}.${bean}.title"/></legend>
			<table class="tabladatos">
				<!-- BeanwriteCodigo -->
				<tr>
					<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.cod${Bean}.label"/>: </label></td>
					<td class="normal"><bean:write name="${bean}AdapterVO" property="${bean}.cod${Bean}"/></td>
				</tr>
				<!-- BeanwriteDescricion -->
				<tr>
					<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.des${Bean}.label"/>: </label></td>
					<td class="normal"><bean:write name="${bean}AdapterVO" property="${bean}.des${Bean}"/></td>
				</tr>
				<!-- BeanwritePropiedadComponente -->
				<tr>
					<td><label><bean:message bundle="${modulo}" key="${modulo}.tipo${Bean}.desTipo${Bean}.ref"/>: </label></td>
					<td class="normal"><bean:write name="${bean}AdapterVO" property="${bean}.tipo${Bean}.desTipo${Bean}" /></td>
				</tr>
				<!-- BeanwritePropiedad -->
				<tr>
					<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.${propiedad}.label"/>: </label></td>
					<td class="normal"><bean:write name="${bean}AdapterVO" property="${bean}.${propiedad}"/></td>				
				</tr>
				<!-- BeanwritePropiedadFecha -->
				<tr>
					<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.${propiedadFecha}.label"/>: </label></td>
					<td class="normal"><bean:write name="${bean}AdapterVO" property="${bean}.${propiedadFecha}View"/></td>				
				</tr>				
				<!-- FieldEstado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="${bean}AdapterVO" property="${bean}.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- ${Bean} -->
	
		<!-- ${Bean_Detalle} -->
		<logic:equal name="${bean}AdapterVO" property="act" value="ver">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="${modulo}" key="${modulo}.${bean}.list${Bean_Detalle}.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="${bean}AdapterVO" property="${bean}.list${Bean_Detalle}">
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Ver -->
							<th align="left"><bean:message bundle="${modulo}" key="${modulo}.${bean}.fechaDesde.label"/></th>
							<th align="left"><bean:message bundle="${modulo}" key="${modulo}.${bean}.fechaHasta.label"/></th>							
							<th align="left"><bean:message bundle="${modulo}" key="${modulo}.atributo.codAtributo.label"/></th>
							<th align="left"><bean:message bundle="${modulo}" key="${modulo}.atributo.desAtributo.label"/></th>
						</tr>
						<logic:iterate id="${Bean_Detalle}VO" name="${bean}AdapterVO" property="${bean}.list${Bean_Detalle}">
				
							<tr>
								<!-- Ver -->
								<td>
									<logic:equal name="${Bean_Detalle}VO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver${Bean_Detalle}', '<bean:write name="${Bean_Detalle}VO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									
									<logic:notEqual name="${Bean_Detalle}VO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="${Bean_Detalle}VO" property="fechaDesdeView"/>&nbsp;</td>
								<td><bean:write name="${Bean_Detalle}VO" property="fechaHastaView"/>&nbsp;</td>								
								<td><bean:write name="${Bean_Detalle}VO" property="atributo.codAtributo"/>&nbsp;</td>
								<td><bean:write name="${Bean_Detalle}VO" property="atributo.desAtributo"/>&nbsp;</td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="${bean}AdapterVO" property="${bean}.list${Bean_Detalle}">
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
	   	    	    <logic:equal name="${bean}AdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					<logic:equal name="${bean}AdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="${bean}AdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="${bean}AdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	<input type="hidden" name="name"  value="<bean:write name='${bean}AdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->