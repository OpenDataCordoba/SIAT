<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarRecConADec.do">
<script>
	function inicializar(){
		var act = document.getElementById('act').value;
		var fieldEdicion = document.getElementById('edicionRecConADec');
		var fieldVer= document.getElementById('verRecConADec');
		var botonAgregar = document.getElementById('btnAdd');
		var botonModificar = document.getElementById('btnMdf');
		var botonMdfRecCon=  document.getElementById('btnMdfRecConADec');
		var secListaValUni=document.getElementById('bloqueListaVal');
		if (act=="agregar"){
			fieldEdicion.style.display="block";
			fieldVer.style.display="none";
			botonAgregar.style.display="block";
			botonModificar.style.display="none";	
		}
		if (act=="modificar"){
			fieldEdicion.style.display="none";
			fieldVer.style.display="block";
			botonAgregar.style.display="none";
			botonModificar.style.display="none";
			botonMdfRecCon.style.display="block";	
			secListaValUni.style.display="block";
		}
	}
	
	function modificarRecConADec(){
		var fieldEdicion = document.getElementById('edicionRecConADec');
		var fieldVer= document.getElementById('verRecConADec');
		var botonModificar = document.getElementById('btnMdf');
		var botonMdfRecCon=  document.getElementById('btnMdfRecConADec');
		var secListaValUni=document.getElementById('bloqueListaVal');
		fieldEdicion.style.display="block";
		fieldVer.style.display="none";
		botonModificar.style.display="inline";
		botonMdfRecCon.style.display="none";
		secListaValUni.style.display="none";
	
	}
</script>

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
		<fieldset id="edicionRecConADec" style="display:none">
			<legend><bean:message bundle="def" key="def.recConADecAdapter.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.RecConADec.tipRecConADec"/>: </label></td>
					<td class="normal">
						<html:select name="recConADecAdapterVO" property="recConADec.tipRecConADec.id" onchange="submitForm('paramTipRecConADec', '');">
							<html:optionsCollection name="recConADecAdapterVO" property="listTipRecConADec" label="descripcion" value="id"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recConADec.codConcepto"/>: </label></td>
					<td class="normal">
							<html:text name="recConADecAdapterVO" property="recConADec.codConcepto" size="10"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recConADec.desConcepto"/>: </label></td>
					<td class="normal" colspan="4"><html:text name="recConADecAdapterVO" property="recConADec.desConcepto" size="60"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recConADec.observacion.label"/>: </label></td>
					<td class="normal" colspan="4"><html:text name="recConADecAdapterVO" property="recConADec.observacion" size="60"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.valUnRecConADeAdapter.unidad.label"/>: </label></td>
					<td class="normal">
						<html:select name="recConADecAdapterVO" property="recConADec.recTipUni.id">
							<html:optionsCollection name="recConADecAdapterVO" property="listRecTipUni" label="nomenclatura" value="id"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recConADec.fechaDesde"/>: </label></td>
					<td class="normal">
						<html:text name="recConADecAdapterVO" property="recConADec.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					<td><label><bean:message bundle="def" key="def.recConADec.fechaHasta"/>: </label>
					<td class="normal">
						<html:text name="recConADecAdapterVO" property="recConADec.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>
				<logic:equal name="recConADecAdapterVO" property="paramCodigoAfip" value="true">
					<tr>
						<td><label><bean:message bundle="def" key="def.recConADec.codigoAfip.label"/>: </label></td>
						<td class="normal" colspan="4"><html:text name="recConADecAdapterVO" property="recConADec.codigoAfip" size="10"/></td>
					</tr>
				</logic:equal>
				
			</table>
		</fieldset>
		<!-- Fin RecConADec -->
		<fieldset id="verRecConADec" style="display:block">
			<legend><bean:message bundle="def" key="def.recConADecAdapter.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.RecConADec.tipRecConADec"/>: </label></td>
						<td class="normal">
							<bean:write name="recConADecAdapterVO" property="recConADec.tipRecConADec.descripcion"/>
						</td>
				</tr>
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
			<p align="right" id="btnMdfRecConADec" style="display:none">
				<html:button property="btnMdf" onclick="modificarRecConADec();">
					<bean:message bundle="base" key="abm.button.modificar"/>
				</html:button>
			</p>
		</fieldset>
		
		<!-- Lista de Costo Unitario -->
		<logic:equal name="recConADecAdapterVO" property="act" value="modificar">
			<fieldset id="bloqueListaVal">
				<legend><bean:message bundle="def" key="def.recCOnADecAdapter.listValUnRecConADe.label"/></legend>
				<logic:notEmpty  name="recConADecAdapterVO" property="recConADec.listValUnRecConADe">
						<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
							<caption><bean:message bundle="def" key="def.recCOnADecAdapter.listValUnRecConADe.label"/></caption>
				           	<tbody>
				           		<tr>
					           		<th width="1">&nbsp;</th><!-- Ver -->
					           		<th width="1">&nbsp;</th><!-- Modificar -->
					           		<th width="1">&nbsp;</th><!-- Eliminar -->
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
						        			<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarValUnRecConADe', '<bean:write name="ValUnRecConADeVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
						        		</td>
						        		<td>
						        			<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarValUnRecConADe', '<bean:write name="ValUnRecConADeVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
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
		</logic:equal>
		<table class="tablabotones" width="100%">
			<tr>				
	   	    	<td align="right">
					<html:button property="btnAccionBase"  styleId="btnAdd" styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>
				</td>
				<td align="right">
					<html:button property="btnAccionBase" styleId="btnMdf"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</td>
 			</tr>
 			<tr>
 				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
		<input type="hidden" name="act" id="act" value="<bean:write name="recConADecAdapterVO" property="act"/>"/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		<script>
			inicializar();
		</script>
		<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
		
		