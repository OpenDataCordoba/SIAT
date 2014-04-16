<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	    
</script>
	
	
	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarServicioBanco.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.servicioBancoAdapter.title"/></h1>	
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
					<td class="normal"><bean:write name="servicioBancoAdapterVO" property="servicioBanco.codServicioBanco"/></td>					
					<td><label><bean:message bundle="def" key="def.servicioBanco.desServicioBanco.ref"/>: </label></td>
					<td class="normal"><bean:write name="servicioBancoAdapterVO" property="servicioBanco.desServicioBanco" /></td>					
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.servicioBanco.partidaIndet.label"/>: </label></td>
					<td colspan="4" class="normal"><bean:write name="servicioBancoAdapterVO" property="servicioBanco.partidaIndet.desPartidaView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.servicioBanco.parCuePue.label"/>: </label></td>
					<td colspan="4" class="normal"><bean:write name="servicioBancoAdapterVO" property="servicioBanco.parCuePue.desPartidaView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.servicioBanco.esAutoliquidable.label"/>: </label></td>
					<td class="normal"><bean:write name="servicioBancoAdapterVO" property="servicioBanco.esAutoliquidable.value"/></td>			
					<td><ul class="vinieta"><li style="text-align:left;"><bean:message bundle="def" key="def.servicioBanco.esAutoliquidable.description"/></li></ul></td>				
				</tr>
				<tr>
					<!-- Tipo Asentamiento -->
					<td align="right"><label><bean:message bundle="def" key="def.servicioBanco.tipoAsentamiento.label"/>: </label></td>
					<td class="normal">	
						<html:select name="servicioBancoAdapterVO" property="servicioBanco.tipoAsentamiento" styleClass="select" disabled="true">
							<html:optionsCollection name="servicioBancoAdapterVO" property="listTipoAsentamiento" label="descripcion" value="value" />
						</html:select>
					</td>
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.servicioBanco.tipoAsentamiento.description"/></li></ul></td>				
				</tr>
			</table>
		</fieldset>
		<!-- Fin Servicio Banco Enc -->		

		<!-- SerBanRec -->
		<logic:equal name="servicioBancoAdapterVO" property="act" value="ver">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="def" key="def.servicioBanco.listSerBanRec.ref"/></caption>
			    	<tbody>
						<logic:notEmpty  name="servicioBancoAdapterVO" property="servicioBanco.listSerBanRec">						
					    	<tr>
								<th width="1">&nbsp;</th> <!-- Ver -->
								<th align="left"><bean:message bundle="def" key="def.serBanRec.desRecurso.label"/></th>
								<th width="1"><bean:message bundle="def" key="def.serBanRec.fechaDesde.label"/></th>
								<th width="1"><bean:message bundle="def" key="def.serBanRec.fechaDesde.label"/></th>
							</tr>
							<logic:iterate id="SerBanRecVO" name="servicioBancoAdapterVO" property="servicioBanco.listSerBanRec">
					
								<tr>
									<!-- Ver -->
									<td>
										<logic:equal name="SerBanRecVO" property="verEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verSerBanRec', '<bean:write name="SerBanRecVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										
										<logic:notEqual name="SerBanRecVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
										</logic:notEqual>
									</td>
									<td><bean:write name="SerBanRecVO" property="recurso.desRecurso" />&nbsp;</td>
									<td><bean:write name="SerBanRecVO" property="fechaDesdeView"/>&nbsp;</td>
									<td><bean:write name="SerBanRecVO" property="fechaHastaView"/>&nbsp;</td>
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty  name="servicioBancoAdapterVO" property="servicioBanco.listSerBanRec">
							<tr><td align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
							</td></tr>
						</logic:empty>					
					</tbody>
				</table>
		</logic:equal>
		<!-- Fin SerBanRec -->

		<!-- SerBanDesGen -->
		<logic:equal name="servicioBancoAdapterVO" property="act" value="ver">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="def" key="def.servicioBanco.listSerBanDesGen.ref"/></caption>
			    	<tbody>
						<logic:notEmpty  name="servicioBancoAdapterVO" property="servicioBanco.listSerBanDesGen">						
					    	<tr>
								<th width="1">&nbsp;</th> <!-- Ver -->
								<th align="left"><bean:message bundle="gde" key="gde.serBanDesGen.desDesGen.label"/></th>
								<th width="1"><bean:message bundle="gde" key="gde.serBanDesGen.fechaDesde.label"/></th>
								<th width="1"><bean:message bundle="gde" key="gde.serBanDesGen.fechaDesde.label"/></th>
							</tr>
							<logic:iterate id="SerBanDesGenVO" name="servicioBancoAdapterVO" property="servicioBanco.listSerBanDesGen">
					
								<tr>
									<!-- Ver -->
									<td>
										<logic:equal name="SerBanDesGenVO" property="verEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verSerBanDesGen', '<bean:write name="SerBanDesGenVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										
										<logic:notEqual name="SerBanDesGenVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
										</logic:notEqual>
									</td>
									<td><bean:write name="SerBanDesGenVO" property="desGen.desDesGen" />&nbsp;</td>
									<td><bean:write name="SerBanDesGenVO" property="fechaDesdeView"/>&nbsp;</td>
									<td><bean:write name="SerBanDesGenVO" property="fechaHastaView"/>&nbsp;</td>
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty  name="servicioBancoAdapterVO" property="servicioBanco.listSerBanDesGen">
							<tr><td align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
							</td></tr>
						</logic:empty>					
					</tbody>
				</table>
			</logic:equal>
		<!-- Fin SerBanDesGen -->
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="servicioBancoAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="servicioBancoAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="servicioBancoAdapterVO" property="act" value="desactivar">
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