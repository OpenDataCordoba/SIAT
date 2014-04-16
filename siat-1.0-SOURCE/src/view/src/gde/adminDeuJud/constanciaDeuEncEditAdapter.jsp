<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/gde/AdministrarEncConstanciaDeu.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>

	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="encConstanciaDeuAdapterVO"/>
		<bean:define id="poseeParam" value="true" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
		
		<h1><bean:message bundle="gde" key="gde.constanciaDeuEditAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<!-- ConstanciaDeu -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.constanciaDeu.title"/></legend>
			
			<table class="tabladatos">
				<logic:equal name="encConstanciaDeuAdapterVO" property="act" value="agregar">
					<tr>	
						<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal">
							<html:select name="encConstanciaDeuAdapterVO" property="recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');" styleId="cboRecurso" style="width:90%">
								<bean:define id="includeRecursoList" name="encConstanciaDeuAdapterVO" property="listRecurso"/>
								<bean:define id="includeIdRecursoSelected" name="encConstanciaDeuAdapterVO" property="recurso.id"/>
								<%@ include file="/def/gravamen/includeRecurso.jsp" %>
							</html:select>

							<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
								<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
								src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
							</a>
							
						</td>					
					</tr>
					<tr>	
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
						<td class="normal">
							<html:select name="encConstanciaDeuAdapterVO" property="constanciaDeu.procurador.id" styleClass="select">
								<html:optionsCollection name="encConstanciaDeuAdapterVO" property="listProcurador" label="descripcion" value="id" />
							</html:select>
						</td>					
					</tr>
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
						<td class="normal">
							<html:text name="encConstanciaDeuAdapterVO" property="constanciaDeu.cuenta.numeroCuenta" styleClass="select" size="20" maxlength="100" readonly="true"/>
							<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
								<bean:message bundle="exe" key="exe.cueExeSearchPage.button.buscarCuenta"/>
							</html:button>
						</td>			
					</tr>
				</logic:equal>
			
				<logic:equal name="encConstanciaDeuAdapterVO" property="act" value="modificar">
					<tr>	
						<td><label><bean:message bundle="gde" key="gde.constanciaDeu.numero.label"/>: </label></td>
						<td class="normal">
							<bean:write name="encConstanciaDeuAdapterVO" property="constanciaDeu.numeroView"/>
						</td>
						<td><label><bean:message bundle="gde" key="gde.constanciaDeu.anio.label"/>: </label></td>
						<td class="normal">
							<bean:write name="encConstanciaDeuAdapterVO" property="constanciaDeu.anioView"/>
						</td>
					</tr>
					<tr>	
						<td><label><bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
						<td class="normal">
							<bean:write name="encConstanciaDeuAdapterVO" property="constanciaDeu.procurador.descripcion"/>
						</td>					
						<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
						<td class="normal">
							<bean:write name="encConstanciaDeuAdapterVO" property="constanciaDeu.cuenta.numeroCuenta"/>
						</td>			
					</tr>
							
					<tr>	
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal">
							<bean:write name="encConstanciaDeuAdapterVO" property="constanciaDeu.cuenta.recurso.desRecurso" />
						</td>
						<td><label><bean:message bundle="gde" key="gde.constanciaDeu.estConDeu.label"/>: </label></td>
						<td class="normal">
							<bean:write name="encConstanciaDeuAdapterVO" property="constanciaDeu.estConDeu.desEstConDeu" />
						</td>
					</tr>
		
					<tr>	
						<td><label><bean:message bundle="gde" key="gde.constanciaDeu.fechaEnvio.label"/>: </label></td>
						<td class="normal">
							<bean:write name="encConstanciaDeuAdapterVO" property="constanciaDeu.procesoMasivo.fechaEnvioView" />
						</td>
						<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.ref"/>: </label></td>
						<td class="normal">
							<bean:write name="encConstanciaDeuAdapterVO" property="constanciaDeu.plaEnvDeuPro.nroBarraAnioPlanillaView" />
						</td>
					</tr>
				</logic:equal>
				
				<!-- Domicilio Envio -->
				<tr>
			    	<td><label><bean:message bundle="pad" key="pad.domicilio.envio.title"/>: </label></td>
			    	<td class="normal" colspan="3"><html:text name="encConstanciaDeuAdapterVO" property="constanciaDeu.desDomEnv" size="60"/></td>
			    </tr>
			    
			    <!-- Ubicacion -->
				<tr>
			    	<td><label><bean:message bundle="gde" key="gde.constanciaDeu.ubicacion.label"/>: </label></td>
			    	<td class="normal" colspan="3"><html:text name="encConstanciaDeuAdapterVO" property="constanciaDeu.desDomUbi" size="60"/></td>
			    </tr>		    
			    
			    <!-- desTitulares -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.constanciaDeu.desTitulares.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:textarea name="encConstanciaDeuAdapterVO" property="constanciaDeu.desTitulares" cols="80" rows="15"/>
					</td>
				</tr>
				
				<!-- Observacion -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.constanciaDeu.observaciones.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:textarea name="encConstanciaDeuAdapterVO" property="constanciaDeu.observacion" cols="80" rows="15"/>
					</td>
				</tr>
				
				<!-- <#Campos#> -->
			</table>
		</fieldset>
		<!-- ConstanciaDeu -->
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="encConstanciaDeuAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="encConstanciaDeuAdapterVO" property="act" value="agregar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
							<bean:message bundle="base" key="abm.button.agregar"/>
						</html:button>
					</logic:equal>				
				</td>
				</tr>
		</table>
   	
   	</span>
   	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>	
</html:form>
<!-- Fin formulario -->
