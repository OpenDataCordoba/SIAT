<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarRecConADec.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.recursoAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>

		<!-- Datos del Recurso -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recurso.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<!-- Tipo de Categoria -->										
					<td align="right"><label><bean:message bundle="def" key="def.categoria.tipo.label"/>:</label></td>
					<td class="normal"><bean:write name="recConADecAdapterVO" property="recConADec.recurso.categoria.tipo.desTipo"/></td>
					<!-- Categoria -->										
					<td align="right"><label><bean:message bundle="def" key="def.categoria.label"/>:</label></td>
					<td class="normal"><bean:write name="recConADecAdapterVO" property="recConADec.recurso.categoria.desCategoria"/></td>
				</tr>
				<tr>
					<!-- Codigo -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.codRecurso.label"/>:</label></td>
					<td class="normal"><bean:write name="recConADecAdapterVO" property="recConADec.recurso.codRecurso"/></td>
					<!-- Descripcion -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.desRecurso.label"/>: </label></td>
					<td class="normal"><bean:write name="recConADecAdapterVO" property="recConADec.recurso.desRecurso"/></td>					
				</tr>
				<tr>
					<!-- TipObjImp -->
					<td align="right"><label><bean:message bundle="def" key="def.tipObjImp.label"/>:</label></td>
					<td class="normal"><bean:write name="recConADecAdapterVO" property="recConADec.recurso.tipObjImp.desTipObjImp"/></td>
				</tr>
			</table>
		</fieldset>
		<!-- Fin Datos del Recurso -->
		
		<!-- RecConADec -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recConADecAdapter.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.recConADec.codConcepto"/>: </label></td>
					<td class="normal">
							<bean:write name="recConADecAdapterVO" property="recConADec.codConcepto"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recConADec.desConcepto"/>: </label></td>
					<td class="normal" colspan="4">
						<bean:write name="recConADecAdapterVO" property="recConADec.desConcepto"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recConADec.observacion.label"/>: </label></td>
					<td class="normal" colspan="4">
						<bean:write name="recConADecAdapterVO" property="recConADec.observacion"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.valUnRecConADeAdapter.unidad.label"/>: </label></td>
					<td class="normal">
						<bean:write name="recConADecAdapterVO" property="recConADec.recTipUni.nomenclatura"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recConADec.fechaDesde"/>: </label></td>
					<td class="normal">
						<bean:write name="recConADecAdapterVO" property="recConADec.fechaDesdeView"/>
					</td>
					<td><label><bean:message bundle="def" key="def.recConADec.fechaHasta"/>: </label>
					<td class="normal">
						<bean:write name="recConADecAdapterVO" property="recConADec.fechaHastaView"/>
					</td>
				</tr>
				<logic:equal name="recConADecAdapterVO" property="paramCodigoAfip" value="true">
					<tr>
						<td><label><bean:message bundle="def" key="def.recConADec.codigoAfip.label"/>: </label></td>
						<td class="normal" colspan="4">
							<bean:write name="recConADecAdapterVO" property="recConADec.codigoAfip"/>
						</td>
					</tr>
				</logic:equal>
			</table>
		</fieldset>
		<!-- Fin RecGenCueAtrVa -->
		
		<!-- Lista de valores unitarios -->
		<fieldset>
				<legend><bean:message bundle="def" key="def.recCOnADecAdapter.listValUnRecConADe.label"/></legend>
				<logic:notEmpty  name="recConADecAdapterVO" property="recConADec.listValUnRecConADe">
						<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
							<caption><bean:message bundle="def" key="def.recCOnADecAdapter.listValUnRecConADe.label"/></caption>
				           	<tbody>
				           		<tr>
					           		<th width="1">&nbsp;</th><!-- Ver -->
					           		<th><bean:message bundle="def" key="def.recConADecAdapter.valUni.fechaDesde.label"/></th>
					           		<th><bean:message bundle="def" key="def.recConADecAdapter.valUni.fechaHasta.label"/></th>
					           		<th><bean:message bundle="def" key="def.recConADecAdapter.valUni.valor.label"/></th>
					           	</tr>           
						        <logic:iterate name="recConADecAdapterVO" property="recConADec.listValUnRecConADe" id="ValUnRecConADeVO">
						        	<tr>
						        		<td>
						        			<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verValUnRecConADe', '<bean:write name="ValUnRecConADeVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
						        		</td>
						        		<td>
						        			<bean:write name="ValUnRecConADeVO" property="fechaDesdeView"/>
						        		</td>
						        		<td>
						        			<bean:write name="ValUnRecConADeVO" property="fechaHastaView"/>
						        		</td>
						        		<td>
						        			<bean:write name="ValUnRecConADeVO" property="valorUnitarioView"/>
						        		</td>
						        	</tr>
						        </logic:iterate>
				            </tbody>
				    	</table>
				    </logic:notEmpty>
				    <logic:empty name="recConADecAdapterVO" property="recConADec.listValUnRecConADe">
						<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
							<caption><bean:message bundle="def" key="def.recCOnADecAdapter.listValUnRecConADe.label"/></caption>
		                	<tbody>
								<tr><td align="center">
									<bean:message bundle="base" key="base.resultadoVacio"/>
								</td></tr>
							</tbody>			
						</table>
					</logic:empty>
					<p align="right">
						<html:button property="btnAgregar" onclick="submitForm('agregarValUnRecConADe','');">
							<bean:message bundle="base" key="abm.button.agregar"/>
						</html:button>
					</p>
				</fieldset>
				

		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="recConADecAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
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
		
		