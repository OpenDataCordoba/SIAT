<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarDesEsp.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.desEspViewAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- DesEsp -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.desEsp.title"/></legend>
			<table class="tabladatos">
				<!-- Descripcion -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.desEsp.desDesEsp.label"/>: </label></td>
					<td class="normal"><bean:write name="desEspAdapterVO" property="desEsp.desDesEsp"/></td>
				</tr>
				<!-- Recurso -->
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="desEspAdapterVO" property="desEsp.recurso.desRecurso"/></td>
				</tr>				
				<!-- Tipo Deuda -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.desEsp.tipoDeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="desEspAdapterVO" property="desEsp.tipoDeuda.desTipoDeuda"/></td>
				</tr>
				<!-- Via Deuda -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.desEsp.viaDeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="desEspAdapterVO" property="desEsp.viaDeuda.desViaDeuda"/></td>
				</tr>
				<!-- fecha Vto. desde -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.desEsp.fechaVtoDeudaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="desEspAdapterVO" property="desEsp.fechaVtoDeudaDesdeView"/></td>
				</tr>
				<!-- fecha Vto. hasta -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.desEsp.fechaVtoDeudaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="desEspAdapterVO" property="desEsp.fechaVtoDeudaHastaView"/></td>
				</tr>
				<!-- % desc. capital -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.desEsp.porDesCap.label"/>: </label></td>
					<td class="normal"><bean:write name="desEspAdapterVO" property="desEsp.porDesCapView"/></td>
				</tr>
				<!-- % desc. actualiz -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.desEsp.porDesAct.label"/>: </label></td>
					<td class="normal"><bean:write name="desEspAdapterVO" property="desEsp.porDesActView"/></td>
				</tr>
				<!-- % desc. Int -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.desEsp.porDesInt.label"/>: </label></td>
					<td class="normal"><bean:write name="desEspAdapterVO" property="desEsp.porDesIntView"/></td>
				</tr>
				<!-- Leyenda -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.desEsp.leyenda"/>: </label></td>
					<td class="normal"><bean:write name="desEspAdapterVO" property="desEsp.leyendaDesEsp"/></td>
				</tr>				
				
				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="desEspAdapterVO" property="desEsp"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<!-- Fin Inclucion de CasoView -->
				
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="desEspAdapterVO" property="desEsp.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- DesEsp -->

		<!-- ClasificDeuda -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.desEsp.listDesRecClaDeu.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="desEspAdapterVO" property="desEsp.listDesRecClaDeu">	    	
			    	<tr>
						<th align="left"><bean:message bundle="gde" key="gde.desRecClaDeu.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.desRecClaDeu.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.desRecClaDeu.desClasificacion.label"/></th>						
					</tr>
					<logic:iterate id="desRecClaDeuVO" name="desEspAdapterVO" property="desEsp.listDesRecClaDeu">
						<tr>					
							<td><bean:write name="desRecClaDeuVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="desRecClaDeuVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="desRecClaDeuVO" property="recClaDeu.desClaDeu" />&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="desEspAdapterVO" property="desEsp.listDesRecClaDeu">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- ClasificDeuda -->
				
		<!-- Atributos -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.desEsp.listDesAtrVal.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="desEspAdapterVO" property="desEsp.listDesAtrVal">	    	
			    	<tr>
						<th align="left"><bean:message bundle="gde" key="gde.desAtrVal.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.desAtrVal.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.atributo.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.desAtrVal.valor.label"/></th>
					</tr>
					<logic:iterate id="desAtrValVO" name="desEspAdapterVO" property="desEsp.listDesAtrVal">			
						<tr>					
							<td><bean:write name="desAtrValVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="desAtrValVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="desAtrValVO" property="atributo.desAtributo"/>&nbsp;</td>
							<td><bean:write name="desAtrValVO" property="valor"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="desEspAdapterVO" property="desEsp.listDesAtrVal">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- Atributos -->

		<!-- Exenciones -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.desEsp.listDesEspExe.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="desEspAdapterVO" property="desEsp.listDesEspExe">	    	
			    	<tr>
						<th align="left"><bean:message bundle="gde" key="gde.desAtrVal.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.desAtrVal.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="exe" key="exe.exencion.codExencion.ref"/></th>
						<th align="left"><bean:message bundle="exe" key="exe.exencion.desExencion.ref"/></th>
					</tr>
					<logic:iterate id="desEspExeVO" name="desEspAdapterVO" property="desEsp.listDesEspExe">			
						<tr>					
							<td><bean:write name="desEspExeVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="desEspExeVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="desEspExeVO" property="exencion.codExencion"/>&nbsp;</td>
							<td><bean:write name="desEspExeVO" property="exencion.desExencion"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="desEspAdapterVO" property="desEsp.listDesEspExe">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- Exenciones -->


		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	    <logic:equal name="desEspAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					<logic:equal name="desEspAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="desEspAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="desEspAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	<input type="hidden" name="name"  value="<bean:write name='desEspAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
