<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarDecJur.do">
	<script>
		function editaAdicionales(){
			
			var edita = document.getElementById("editar");
			if (edita.value=="true"){
				var alipub= document.getElementById("aliPubEdit");
				alipub.style.display="inline";
				var aliMes= document.getElementById("aliMesEdit");
				aliMes.style.display="inline";
				var aliPubView = document.getElementById("aliPubView");
				aliPubView.style.display="none";
				var aliMesView = document.getElementById("aliMesView");
				aliMesView.style.display="none";
				var btnAdd = document.getElementById("btnGrabar");
				btnAdd.style.display="inline";
				var btnModif=document.getElementById("btnMdfAdic");
				btnModif.style.display="none";
			}else{
				var alipub= document.getElementById("aliPubEdit");
				alipub.style.display="none";
				var aliMes= document.getElementById("aliMesEdit");
				aliMes.style.display="none";
				var aliPubView = document.getElementById("aliPubView");
				aliPubView.style.display="inline-block";
				var aliMesView = document.getElementById("aliMesView");
				aliMesView.style.display="inline-block";
				var btnAdd = document.getElementById("btnGrabar");
				btnAdd.style.display="none";
				var btnModif=document.getElementById("btnMdfAdic");
				btnModif.style.display="inline";
			}
		}
		
	
	
		function recalcular(){
		var subtotal;
		var aliPub;
		var aliMesas;
		
		subtotal = document.getElementById('subtot').value;
		
		var ali = document.getElementById('aliPub');
		var indice = ali.selectedIndex;
		if (indice==0){
			aliPub = 0;
		}else{
			aliPub = ali.options[indice].value;
		}
		var totalPub = document.getElementById("totalPub");
		var valorPub = subtotal * aliPub;
		totalPub.value = valorPub.toFixed(2);
		
		var ali2 = document.getElementById('aliMesas');
		var indice2 = ali2.selectedIndex;
		if (indice2 == 0){
			aliMesas = 0;
		}else{
			aliMesas = parseFloat(ali2.options[indice2].value);
		}
		
		var totalMesas = document.getElementById("totalMesas");
		var valorMesas = subtotal * aliMesas;
		totalMesas.value = valorMesas.toFixed(2);
		
		
		var subtotal2 = document.getElementById("totalDeclarado");
		subtotal2.value= (parseFloat(subtotal) + parseFloat(valorPub) + parseFloat(valorMesas)).toFixed(2);
		
		}
		
		function modificaAdicionales(){
			var alipub= document.getElementById("aliPubEdit");
			alipub.style.display="inline-block";
			var aliMes= document.getElementById("aliMesEdit");
			aliMes.style.display="inline-block";
			var aliPubView = document.getElementById("aliPubView");
			aliPubView.style.display="none";
			var aliMesView = document.getElementById("aliMesView");
			aliMesView.style.display="none";
			var btnAdd = document.getElementById("btnGrabar");
			btnAdd.style.display="inline";
			var btnModif=document.getElementById("btnMdfAdic");
			btnModif.style.display="none";
		}

	</script>
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
 	<h1><bean:message bundle="gde" key="gde.decJurAdapter.title"/></h1>
	<p align="right">
		<button type="button" name="btn2" class="boton" onclick="submitForm('volver','');"><bean:message bundle="base" key="abm.button.volver"/></button>
	</p>
	 

	<!-- DecJur -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.decJurAdapter.legend"/></legend>
		
		<logic:equal name="decJurAdapterVO" property="decJur.id" value="">
			<table class="tabladatos">
				<tr>
					<td>
						<label><bean:message bundle="gde" key="gde.decJurSearchPage.cuenta.label"/>: </label>
					</td>
					<td class="normal">
						<bean:write name="decJurAdapterVO" property="decJur.cuenta.numeroCuenta"/>
					</td>
				</tr>
				<tr>
					<td>
						<label><bean:message bundle="def" key="def.recurso.label"/>: </label>
					</td>
					<td class="normal">
						<bean:write name="decJurAdapterVO" property="decJur.cuenta.recurso.desRecurso"/>
					</td>
				</tr>
				
				<!-- Usuarios no CMD -->
				<logic:notEqual name="userSession" property="esUsuarioCMD" value="true">
					<tr>
						<td>
							<label><bean:message bundle="gde" key="gde.decJur.periodo.label"/>: </label>
						</td>
						<td class="normal">
							<html:text name="decJurAdapterVO" property="decJur.periodo" size="2"></html:text>
							/<html:text name="decJurAdapterVO" property="decJur.anio" size="4"></html:text>
							<bean:message bundle="gde" key="gde.decJurSearchPage.mascaraPeriodo"/>
						</td>
					</tr>
				</logic:notEqual>
				
				<!-- Usuarios CMD -->
				<logic:equal name="userSession" property="esUsuarioCMD" value="true">
					<tr>
			      		<td><label><bean:message bundle="gde" key="gde.decJurAdapter.periodoDesde.label"/>: </label></td>
			      		<td class="normal">
			      			<html:text name="decJurAdapterVO" property="periodoDesde" size="3" maxlength="2" styleClass="datos"/>
			      				 / <html:text name="decJurAdapterVO" property="anioDesde" size="8" maxlength="4" styleClass="datos"/>
			      				 <bean:message bundle="gde" key="gde.decJurSearchPage.mascaraPeriodo"/>
						</td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.periodoHasta.label"/>: </label></td>
			      		<td class="normal">
			      			<html:text name="decJurAdapterVO" property="periodoHasta" size="3" maxlength="2" styleClass="datos"/>
			      				 / <html:text name="decJurAdapterVO" property="anioHasta" size="8" maxlength="4" styleClass="datos"/>
			      				 <bean:message bundle="gde" key="gde.decJurSearchPage.mascaraPeriodo"/>
						</td>
					</tr>
				</logic:equal>
				
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurAdapter.tipoDJ"/>: </label></td>
					<td class="normal">
						<html:select name="decJurAdapterVO" property="decJur.tipDecJurRec.id" styleClass="select" >
							<html:optionsCollection name="decJurAdapterVO" property="listTipDecJurRec" label="tipDecJur.desTipo" value="id" />
						</html:select>
					</td>
				</tr>
				<logic:equal name="decJurAdapterVO" property="decJur.esDrei" value="true">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.canPer.label"/>: </label></td>
						<td class="normal"><html:text name="decJurAdapterVO" property="decJur.valRefMin" size="5"/></td>
					</tr>
				</logic:equal>
				<logic:equal name="decJurAdapterVO" property="decJur.esEtur" value="true">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.radio.label"/>: </label></td>
						<td class="normal">
							<html:select name="decJurAdapterVO" property="decJur.valRefMin" styleClass="select">
								<html:optionsCollection name="decJurAdapterVO" property="genericAtrDefinition.atributo.domAtr.listDomAtrVal" label="desValor" value="valor" />
							</html:select>
						</td>			
					</tr>
				</logic:equal>
			</table>	
			<p align="right">
				<button type="button" class="boton" name="btnAddDec" onclick="submitForm('crearDecJur','');">
					<bean:message bundle="base" key="abm.button.agregar"/>
				</button>
			</p>
		</logic:equal>
		
		<logic:notEqual name="decJurAdapterVO" property="decJur.id" value="">
			<table class="tabladatos">
				<tr>
					<td>
						<label><bean:message bundle="gde" key="gde.decJurSearchPage.cuenta.label"/>: </label>
					</td>
					<td class="normal">
						<bean:write name="decJurAdapterVO" property="decJur.cuenta.numeroCuenta"/>
					</td>
				</tr>
				<tr>
					<td>
						<label><bean:message bundle="def" key="def.recurso.label"/>: </label>
					</td>
					<td class="normal">
						<bean:write name="decJurAdapterVO" property="decJur.recurso.desRecurso"/>
					</td>
				</tr>
				<tr>
					<td>
						<label><bean:message bundle="gde" key="gde.decJur.periodo.label"/>: </label>
					</td>
					<td class="normal">
						<bean:write name="decJurAdapterVO" property="decJur.desPeriodo"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurAdapter.tipoDJ"/>: </label></td>
					<td class="normal">
						<bean:write name="decJurAdapterVO" property="decJur.tipDecJurRec.tipDecJur.desTipo"/>
					</td>
				</tr>
				<logic:equal name="decJurAdapterVO" property="decJur.esDrei" value="true">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.canPer.label"/>: </label></td>
						<td class="normal"><bean:write name="decJurAdapterVO" property="decJur.valRefMinIntView"/></td>
					</tr>
				</logic:equal>
				<logic:equal name="decJurAdapterVO" property="decJur.esEtur" value="true">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.radio.label"/>: </label></td>
						<td class="normal"><bean:write name="decJurAdapterVO" property="decJur.valRefMinIntView"/></td>			
					</tr>
				</logic:equal>
				<logic:notEmpty name="decJurAdapterVO" property="decJur.minRec">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.minimo.label"/>: </label></td>
						<td class="normal">
							<p class="msgBold"><bean:write name="decJurAdapterVO" property="decJur.minRecView"/></p>
						</td>
					</tr>
				</logic:notEmpty>
			</table>	
		</logic:notEqual>
	</fieldset>
	
	<logic:notEqual name="decJurAdapterVO" property="decJur.id" value="">
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.decJurAdapter.detalle.legend"/></legend>
			<logic:notEmpty  name="decJurAdapterVO" property="decJur.listDecJurDet">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="gde" key="gde.decJurAdapter.actividades.label"/></caption>
		           	<tbody>
		           		<tr>
			           		<th width="1">&nbsp;</th><!-- Ver -->
			           		<th width="1">&nbsp;</th><!-- Modificar -->
			           		<th width="1">&nbsp;</th><!-- Eliminar -->
			           		<th><bean:message bundle="gde" key="gde.decJurAdapter.actividad.label"/></th>
			           		<th><bean:message bundle="gde" key="gde.decJurAdapter.subtotal.label"/></th>
			           	</tr>           
				        <logic:iterate name="decJurAdapterVO" property="decJur.listDecJurDet" id="DecJurDetVO">
				        	<tr>
				        		<td>
				        			<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verDecJurDet', '<bean:write name="DecJurDetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
				        		</td>
				        		<td>
				        			<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarDecJurDet', '<bean:write name="DecJurDetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
									</a>
				        		</td>
				        		<td>
				        			<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarDecJurDet', '<bean:write name="DecJurDetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
									</a>
				        		</td>
				        		<td>
				        			<bean:write name="DecJurDetVO" property="recConADec.codYDescripcion"/>
				        		</td>
				        		<td>
				        			<bean:write name="DecJurDetVO" property="totalConceptoView"/>
				        		</td>
				        	</tr>
				        </logic:iterate>
				        <tr>
				        	<td colspan="5" class="celdatotales" align="right">
					        	<bean:message bundle="gde" key="gde.decJurAdapter.totalAct.label"/>: 
					        	<b><bean:write name="decJurAdapterVO" property="decJur.subtotalDeclaradoView"/></b>
					        </td>
				        </tr>
			               
			               	
		            </tbody>
		    	</table>
		    </logic:notEmpty>
		    <logic:empty name="decJurAdapterVO" property="decJur.listDecJurDet">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="gde" key="gde.decJurAdapter.actividades.label"/></caption>
                	<tbody>
						<tr><td align="center">
							<bean:message bundle="base" key="base.resultadoVacio"/>
						</td></tr>
					</tbody>			
				</table>
			</logic:empty>
			<p align="right">
				<button type="button" class="boton" onclick="submitForm('agregarDecJurDet','');">
					<bean:message bundle="base" key="abm.button.agregar"/>
				</button>
			</p>
		</fieldset>
		
		<logic:equal name="decJurAdapterVO" property="decJur.declaraAdicionales" value="true">
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.decJurAdapter.adicionales.legend"/></legend>
				<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.subtotalDeterminado.label"/>: </label></td>
						<td class="normal"><p class="msgBold"><bean:write name="decJurAdapterVO" property="decJur.subtotalView"/></p></td>
					</tr>
					<logic:equal name="decJurAdapterVO" property="decJur.esDrei" value="true">
						<tr>
							<td><label><bean:message bundle="gde" key="gde.decJurAdapter.aliPub.label"/>: </label></td>
							<td id="aliPubEdit" class="normal">
								<html:select name="decJurAdapterVO" property="decJur.aliPub" styleId="aliPub" styleClass="select" onchange="recalcular();">
									<html:optionsCollection filter="false" name="decJurAdapterVO" property="listAliPub" label="alicuotaView" value="alicuota" />
								</html:select>
							</td>
							<td id="aliPubView" class="normal">
								<bean:write name="decJurAdapterVO" property="decJur.aliPubView" filter="false"/>
							</td>
							<td><label><bean:message bundle="gde" key="gde.decJurAdapter.totalPublicidad.label"/>: </label></td>
							<td class="normal">
								<html:text name="decJurAdapterVO" property="decJur.totalPublicidadView" styleId="totalPub" disabled="true"/>
							</td>
						</tr>
						<tr>
							<td><label><bean:message bundle="gde" key="gde.decJurAdapter.aliMesYSil"/>: </label></td>
							<td class="normal" id="aliMesEdit" style="inline">
								<html:select name="decJurAdapterVO" property="decJur.aliMesYSil" styleId="aliMesas" styleClass="select" onchange="recalcular();">
									<html:optionsCollection filter="false" name="decJurAdapterVO" property="listAliMesYSil" label="alicuotaView" value="alicuota" />
								</html:select>
							</td>
							<td class="normal" id="aliMesView" style="inline">
								<bean:write name="decJurAdapterVO" property="decJur.aliMesYSilView" filter="false"/>
							</td>
							<td><label><bean:message bundle="gde" key="gde.decJurAdapter.totalMesYSil.label"/>: </label></td>
							<td class="normal">
								<html:text name="decJurAdapterVO" property="decJur.totMesYSilView" styleId="totalMesas" disabled="true"/>
							</td>
						</tr>
					</logic:equal>
					<tr>
						<td>&nbsp;</td><td>&nbsp;</td>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.totalDeclarado.label"/>: </label></td>
						<td class="normal"><html:text name="decJurAdapterVO" property="decJur.totalDeclarado" styleId="totalDeclarado" disabled="true"/></td>
					</tr>
				</table>
					<p align="right">
						<button type="button" id="btnGrabar" style="display: none" class="boton" onclick="submitForm('grabarAdicionales','<bean:write name="decJurAdapterVO" property="decJur.id" bundle="base" formatKey="general.format.id"/>');">
							<bean:message bundle="base" key="abm.button.guardar"/>
						</button>
						<button type="button" id="btnMdfAdic" style="display: none" class="boton" onclick="modificaAdicionales();">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</button>
					</p>
			</fieldset>
		</logic:equal>
		<logic:equal name="decJurAdapterVO" property="decJur.declaraOtrosPagos" value="true">
				<fieldset>
					<legend><bean:message bundle="gde" key="gde.decJurAdapter.otrosPagos.legend"/></legend>
					<logic:notEmpty  name="decJurAdapterVO" property="decJur.listDecJurPag">
						<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
							<caption><bean:message bundle="gde" key="gde.decJurAdapter.detallePagos.label"/></caption>
				           	<tbody>
				           		<tr>
					           		<th width="1">&nbsp;</th><!-- Ver -->
					           		<th width="1">&nbsp;</th><!-- Modificar -->
					           		<th width="1">&nbsp;</th><!-- Eliminar -->
					           		<th><bean:message bundle="gde" key="gde.decJurAdapter.tipoPago.label"/></th>
					           		<th><bean:message bundle="gde" key="gde.decJurAdapter.tipoPago.importe.label"/></th>
					           	</tr>           
						        <logic:iterate name="decJurAdapterVO" property="decJur.listDecJurPag" id="DecJurPagVO">
						        	<tr>
						        		<td>
						        			<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verDecJurPag', '<bean:write name="DecJurPagVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
						        		</td>
						        		<td>
						        			<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarDecJurPag', '<bean:write name="DecJurPagVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
						        		</td>
						        		<td>
						        			<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarDecJurPag', '<bean:write name="DecJurPagVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
						        		</td>
						        		<td>
						        			<bean:write name="DecJurPagVO" property="tipPagDecJur.desTipPag"/>
						        		</td>
						        		<td>
						        			<bean:write name="DecJurPagVO" property="importeView"/>
						        		</td>
						        	</tr>
						        </logic:iterate>
						        <tr>
						        	<td colspan="5" class="celdatotales" align="right">
							        	<bean:message bundle="gde" key="gde.decJurAdapter.totalAct.label"/>: 
							        	<b><bean:write name="decJurAdapterVO" property="decJur.otrosPagosView"/></b>
							        </td>
						        </tr>
					               
					               	
				            </tbody>
				    	</table>
				    </logic:notEmpty>
				    <logic:empty name="decJurAdapterVO" property="decJur.listDecJurPag">
						<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
							<caption><bean:message bundle="gde" key="gde.decJurAdapter.detallePagos.label"/></caption>
		                	<tbody>
								<tr><td align="center">
									<bean:message bundle="base" key="base.resultadoVacio"/>
								</td></tr>
							</tbody>			
						</table>
					</logic:empty>
					<p align="right">
						<button type="button" class="boton" onclick="submitForm('agregarDecJurPag','');">
							<bean:message bundle="base" key="abm.button.agregar"/>
						</button>
					</p>
				</fieldset>
		</logic:equal>
		<logic:equal name="decJurAdapterVO" property="decJur.mostrarTotales" value="true">
				<fieldset>
					<legend><bean:message bundle="gde" key="gde.decJurAdapter.totales.legend"/></legend>
					<table class="tabladatos">
						<tr>
							<td><label><bean:message bundle="gde" key="gde.decJurAdapter.totDerDet.label"/>: </label></td>
							<td class="normal">
								<p class="msgBold"><bean:write name="decJurAdapterVO" property="decJur.totalDeclaradoView"/></p>
							</td>
						</tr>
						<logic:equal name="decJurAdapterVO" property="decJur.declaraOtrosPagos" value="true">
							<tr>
								<td><label><bean:message bundle="gde" key="gde.decJurAdapter.otrosPagos.label"/>: </label></td>
								<td class="normal">
									<p class="msgBold"><bean:write name="decJurAdapterVO" property="decJur.otrosPagosView"/></p>
								</td>
							</tr>
							<tr>
								<td><label><bean:message bundle="gde" key="gde.decJurAdapter.totalDJ.label"/>: </label></td>
								<td class="normal">
									<p class="msgBold"><bean:write name="decJurAdapterVO" property="decJur.totalDJView"/></p>
								</td>
							</tr>
						</logic:equal>
					</table>
				</fieldset>
				<p align="right">
					<button type="button" class="boton" onclick="submitForm('procesarDJ','<bean:write name="decJurAdapterVO" property="decJur.id" bundle="base" formatKey="general.format.id"/>');">
						<bean:message bundle="gde" key="gde.decJurAdapter.button.procesar"/>
					</button>
				</p>
			</logic:equal>
	</logic:notEqual>
	
	<table class="tablabotones">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>			
		</tr>
	</table>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="editar"  value="<bean:write name="decJurAdapterVO" property="decJur.editaAdicionales"/>" id="editar"/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="idDecJur" value="<bean:write name="decJurAdapterVO" property="decJur.id" bundle="base" formatKey="general.format.id"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<input type="hidden" name="subtotal" value="<bean:write name="decJurAdapterVO" property="decJur.subtotalView"/>" id="subtot"/>
		
	<script>
		recalcular();
		editaAdicionales();
	</script>

</html:form>
<!-- Fin formulario -->