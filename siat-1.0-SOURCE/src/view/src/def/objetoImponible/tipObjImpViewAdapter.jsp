<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	    
	    <%@include file="/base/calendar.js"%>   	       	          	    
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarTipObjImp.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.tipObjImpAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
			
		<!-- TipObjImp -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.tipObjImp.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.tipObjImp.codTipObjImp.label"/>: </label></td>
					<td class="normal"><bean:write name="tipObjImpAdapterVO" property="tipObjImp.codTipObjImp"/></td>
					
					<td><label><bean:message bundle="def" key="def.tipObjImp.desTipObjImp.label"/>: </label></td>
					<td class="normal"><bean:write name="tipObjImpAdapterVO" property="tipObjImp.desTipObjImp" /></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.tipObjImp.esSiat.label"/>: </label></td>
					<td class="normal" colspan="1"><bean:write name="tipObjImpAdapterVO" property="tipObjImp.esSiat.value"/></td>
					
					<logic:equal name="tipObjImpAdapterVO" property="tipObjImp.esSiat.value" value="No">
	           		<td><label><bean:message bundle="def" key="def.tipObjImp.proceso.label"/>: </label></td>
	               	<td class="normal"><bean:write name="tipObjImpAdapterVO" property="tipObjImp.proceso.codProceso" /></td> 	                                        					
 		 		 	</logic:equal>	
				</tr>				
				<tr>
					<td><label><bean:message bundle="def" key="def.tipObjImp.fechaAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="tipObjImpAdapterVO" property="tipObjImp.fechaAltaView"/></td>
					<logic:notEqual name="tipObjImpAdapterVO" property="act" value="desactivar">
						<td><label><bean:message bundle="def" key="def.tipObjImp.fechaBaja.label"/>: </label></td>
						<td class="normal"><bean:write name="tipObjImpAdapterVO" property="tipObjImp.fechaBajaView" /></td>
					</logic:notEqual>
						
					<logic:equal name="tipObjImpAdapterVO" property="act" value="desactivar">
						<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImp.fechaBaja.label"/>: </label></td>
						<td class="normal">
							<html:text name="tipObjImpAdapterVO" property="tipObjImp.fechaBajaView" styleId="fechaBajaView" size="10" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaBajaView');" id="a_fechaBajaView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
							</a>
						</td>
					</logic:equal>
				</tr>
			</table>
			<logic:equal name="tipObjImpAdapterVO" property="act" value="ver">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="def" key="def.tipObjImp.listTipObjImpAtr.label"/></caption>
			    	<tbody>
						<logic:notEmpty  name="tipObjImpAdapterVO" property="tipObjImp.listTipObjImpAtr">						
					    	<tr>
								<th width="1">&nbsp;</th> <!-- Ver -->
								<th align="left"><bean:message bundle="def" key="def.atributo.codAtributo.label"/></th>
								<th align="left"><bean:message bundle="def" key="def.atributo.desAtributo.label"/></th>
								<th align="left"><bean:message bundle="def" key="def.tipObjImpAtr.fechaDesde.label"/></th>
								<th align="left"><bean:message bundle="def" key="def.tipObjImpAtr.fechaHasta.label"/></th>							
							</tr>
							<logic:iterate id="TipObjImpAtrVO" name="tipObjImpAdapterVO" property="tipObjImp.listTipObjImpAtr">
					
								<tr>
									<!-- Ver -->
									<td>
										<logic:equal name="TipObjImpAtrVO" property="verEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verTipObjImpAtr', '<bean:write name="TipObjImpAtrVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										
										<logic:notEqual name="TipObjImpAtrVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
										</logic:notEqual>
									</td>
									<td><bean:write name="TipObjImpAtrVO" property="atributo.codAtributo"/>&nbsp;</td>
									<td><bean:write name="TipObjImpAtrVO" property="atributo.desAtributo" />&nbsp;</td>
									<td><bean:write name="TipObjImpAtrVO" property="fechaDesdeView" />&nbsp;</td>
									<td><bean:write name="TipObjImpAtrVO" property="fechaHastaView" />&nbsp;</td>								
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty  name="tipObjImpAdapterVO" property="tipObjImp.listTipObjImpAtr">
							<tr><td align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
							</td></tr>
						</logic:empty>					
					</tbody>
				</table>
			</logic:equal>
		</fieldset>
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="tipObjImpAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="tipObjImpAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="tipObjImpAdapterVO" property="act" value="desactivar">
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

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->