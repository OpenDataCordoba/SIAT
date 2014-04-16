<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarDomAtr.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.domAtrAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- DomAtrVal -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.domAtr.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.domAtr.codDomAtr.ref"/>: </label></td>
					<td class="normal"><bean:write name="domAtrAdapterVO" property="domAtr.codDomAtr"/></td>					
					<td><label><bean:message bundle="def" key="def.domAtr.desDomAtr.ref"/>: </label></td>
					<td class="normal"><bean:write name="domAtrAdapterVO" property="domAtr.desDomAtr" /></td>					
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.tipoAtributo.label"/>: </label></td>
					<td class="normal"><bean:write name="domAtrAdapterVO" property="domAtr.tipoAtributo.desTipoAtributo" /></td>
					<td><label><bean:message bundle="def" key="def.domAtr.classForName.label"/>: </label></td>
					<td class="normal"><bean:write name="domAtrAdapterVO" property="domAtr.classForName" /></td>					
				</tr>
				<tr>				
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="domAtrAdapterVO" property="domAtr.estado.value" /></td>					
				</tr>
			</table>
		</fieldset>
		<logic:equal name="domAtrAdapterVO" property="act" value="ver">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="def" key="def.domAtr.listDomAtrVal.ref"/></caption>
		    	<tbody>
					<logic:notEmpty  name="domAtrAdapterVO" property="domAtr.listDomAtrVal">						
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Ver -->
							<th width="1"><bean:message bundle="def" key="def.domAtrVal.fechaDesde.label"/></th>
							<th width="1"><bean:message bundle="def" key="def.domAtrVal.fechaDesde.label"/></th>
							<th align="left"><bean:message bundle="def" key="def.domAtrVal.strValor.label"/></th>
							<th align="left"><bean:message bundle="def" key="def.domAtrVal.desValor.label"/></th>
						</tr>
						<logic:iterate id="DomAtrValVO" name="domAtrAdapterVO" property="domAtr.listDomAtrVal">
				
							<tr>
								<!-- Ver -->
								<td>
									<logic:equal name="DomAtrValVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verDomAtrVal', '<bean:write name="DomAtrValVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="DomAtrValVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="DomAtrValVO" property="fechaDesdeView"/>&nbsp;</td>
								<td><bean:write name="DomAtrValVO" property="fechaHastaView"/>&nbsp;</td>
								<td><bean:write name="DomAtrValVO" property="valor"/>&nbsp;</td>
								<td><bean:write name="DomAtrValVO" property="desValor"/>&nbsp;</td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="domAtrAdapterVO" property="domAtr.listDomAtrVal">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
			</table>
		</logic:equal>
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="domAtrAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="domAtrAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="domAtrAdapterVO" property="act" value="desactivar">
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
