<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarEmiMat.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.emiMatAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- EmiMat -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.emiMat.title"/></legend>
			
			<table class="tabladatos">
				<tr>	
					<td><label><bean:message bundle="def" key="def.emiMat.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="emiMatAdapterVO" property="emiMat.recurso.desRecurso" /></td>
				</tr>

				<tr>
					<td><label><bean:message bundle="def" key="def.emiMat.codEmiMat.label"/>: </label></td>
					<td class="normal"><bean:write name="emiMatAdapterVO" property="emiMat.codEmiMat"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- EmiMat -->
		
		<!-- ColEmiMat -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="def" key="def.emiMat.listColEmiMat.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="emiMatAdapterVO" property="emiMat.listColEmiMat">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th align="left"><bean:message bundle="def" key="def.colEmiMat.codColumna.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.colEmiMat.tipoDato.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.colEmiMat.tipoColumna.label"/></th>
					</tr>
					<logic:iterate id="ColEmiMatVO" name="emiMatAdapterVO" property="emiMat.listColEmiMat">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="emiMatAdapterVO" property="verColEmiMatEnabled" value="enabled">
									<logic:equal name="ColEmiMatVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verColEmiMat', '<bean:write name="ColEmiMatVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="ColEmiMatVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="emiMatAdapterVO" property="verColEmiMatEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>

							<td><bean:write name="ColEmiMatVO" property="codColumna"/>&nbsp;</td>
							<td><bean:write name="ColEmiMatVO" property="tipoDato.value"/>&nbsp;</td>
							<td><bean:write name="ColEmiMatVO" property="tipoColumna.value"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="emiMatAdapterVO" property="emiMat.listColEmiMat">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- ColEmiMat -->
				
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left" width="50%">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>

	   	    	   <logic:equal name="emiMatAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
				</td>
				<td align="right" width="50%">
					<logic:equal name="emiMatAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="emiMatAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="emiMatAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
			</tr>
		</table>
	   	<input type="hidden" name="name"  value="<bean:write name='emiMatAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	

		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- emiMatAdapter.jsp -->