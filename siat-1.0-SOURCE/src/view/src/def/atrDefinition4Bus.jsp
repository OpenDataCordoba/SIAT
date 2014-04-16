	<tr>
		<!-- No posee Dominio -->
		<logic:equal name="AtrVal" property="poseeDominio" value="false">
			<td><label><bean:write name="AtrVal" property="atributo.desAtributo"/></label></td>
			
			<!-- No Admite Busqueda por Rango -->
			<logic:equal name="AtrVal" property="admBusPorRan" value="false">
				<td class="normal"> 
					<input type="text" size="10" maxlength="100" 
						name='<bean:write name="AtrVal" property="atributo.codAtributo"/>'
						id='<bean:write name="AtrVal" property="atributo.codAtributo"/>'
						value='<bean:write name="AtrVal" property="valorView"/>' 
					/>
					<!-- Si el tipo de dato es una fecha muestro el calendario -->
					<logic:equal name="AtrVal" property="atributo.tipoAtributo.codTipoAtributo" value="Date">								
						<a class="link_siat" onclick="return show_calendar('<bean:write name="AtrVal" property="atributo.codAtributo"/>');" 
							id="a_<bean:write name="AtrVal" property="atributo.codAtributo"/>">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</logic:equal>
				</td>
			</logic:equal>
			
			<!-- Admite Busqueda por Rango -->
			<logic:equal name="AtrVal" property="admBusPorRan" value="true">
				<td class="normal"> 
					Desde: <input type="text" size="10" maxlength="100" 
								name='<bean:write name="AtrVal" property="atributo.codAtributo"/>.desde'
								id='<bean:write name="AtrVal" property="atributo.codAtributo"/>.desde'
								value='<bean:write name="AtrVal" property="valorDesdeView"/>'
							/>
							<!-- Si el tipo de dato es una fecha muestro el calendario -->
							<logic:equal name="AtrVal" property="atributo.tipoAtributo.codTipoAtributo" value="Date">								
								<a class="link_siat" onclick="return show_calendar('<bean:write name="AtrVal" property="atributo.codAtributo"/>.desde');" 
									id="a_<bean:write name="AtrVal" property="atributo.codAtributo"/>.desde">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
							</logic:equal>
							
					Hasta: <input type="text" size="10" maxlength="100" 
								name='<bean:write name="AtrVal" property="atributo.codAtributo"/>.hasta'
								id='<bean:write name="AtrVal" property="atributo.codAtributo"/>.hasta'
								value='<bean:write name="AtrVal" property="valorHastaView"/>'
							/> 
							<!-- Si el tipo de dato es una fecha muestro el calendario -->
							<logic:equal name="AtrVal" property="atributo.tipoAtributo.codTipoAtributo" value="Date">
								<a class="link_siat" onclick="return show_calendar('<bean:write name="AtrVal" property="atributo.codAtributo"/>.hasta');" 
									id="a_<bean:write name="AtrVal" property="atributo.codAtributo"/>.hasta">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
							</logic:equal>
				</td>
			</logic:equal>
		</logic:equal>
		
		<!-- Posee Dominio -->
		<logic:equal name="AtrVal" property="poseeDominio" value="true">
			<td><label><bean:write name="AtrVal" property="atributo.desAtributo"/></label></td>
			
			<!-- No Admite Busqueda por Rango -->
			<logic:equal name="AtrVal" property="admBusPorRan" value="false">
			
				<!-- No es Multivalor -->
				<logic:equal name="AtrVal" property="esMultivalor" value="false">
					<td class="normal"> 
						<!-- Combo -->
						<bean:define id="selVal" name="AtrVal" property="valorView"/>
						<% String selectedVal = ""+selVal+"";%>
						
						<select name='<bean:write name="AtrVal" property="atributo.codAtributo"/>'>
							<option value='-1'>
								<logic:empty name="SeleccionarTodos">
									Seleccionar...
								</logic:empty>						
								<logic:notEmpty name="SeleccionarTodos">
									<bean:write name="SeleccionarTodos" />	
								</logic:notEmpty>
							</option>
							<logic:iterate id="AtrValChk" name="AtrVal" property="atributo.domAtr.listDomAtrVal">
								<option value='<bean:write name="AtrValChk" property="valor" bundle="base" formatKey="general.format.id"/>'
									<logic:equal name="AtrValChk" property="valor" value="<%=selectedVal%>">
										selected="selected"
									</logic:equal>
									> <!-- Cierre del opcion -->
									<bean:write name="AtrValChk" property="desValor"/>
								</option>
							</logic:iterate>
						</select>
					</td>
				</logic:equal>
			
				<!-- Es Multivalor -->
				<logic:equal name="AtrVal" property="esMultivalor" value="true">
					<td>&nbsp;</td>
					
				</tr>
				<tr>
					<td class="normal" colspan="2">
						<div id="listaMultivalor" class="scrolable" style="height: 100px;"> 					
							<!-- Checkbox -->
							<logic:iterate id="AtrValChk" name="AtrVal" property="atributo.domAtr.listDomAtrVal">
								<input type="checkbox" name='<bean:write name="AtrVal" property="atributo.codAtributo"/>.<bean:write name="AtrValChk" property="valor" bundle="base" formatKey="general.format.id"/>'
									<logic:iterate id="ChkdVal" name="AtrVal" property="listMultiValor">
								    	<bean:define id="chkVal" name="ChkdVal"/> 
								    	
								    	<% String chekedVal = ""+chkVal+""; %>						    						    	
								    	
								    	<logic:equal name="AtrValChk" property="valor" value="<%=chekedVal%>">
											checked='checked'
										</logic:equal>						    	
									</logic:iterate>
								/> 
								<bean:write name="AtrValChk" property="valor"/> - 
								<bean:write name="AtrValChk" property="desValor"/> <br/>
							</logic:iterate>
						</div>	
					</td>
				</logic:equal>
				
			</logic:equal>
			
			<!-- Admite Busqueda por Rango -->
			<logic:equal name="AtrVal" property="admBusPorRan" value="true">
				<td class="normal"> 
					<!-- Checkbox -->
					<logic:iterate id="AtrValChk" name="AtrVal" property="atributo.domAtr.listDomAtrVal">
						<input type="checkbox" name='<bean:write name="AtrVal" property="atributo.codAtributo"/>.<bean:write name="AtrValChk" property="valor" bundle="base" formatKey="general.format.id"/>'
							<logic:iterate id="ChkdVal" name="AtrVal" property="listValorView">
						    	<bean:define id="chkVal" name="ChkdVal"/> 
						    	<% String chekedVal = ""+chkVal+""; %>
						    	
						    	<logic:equal name="AtrValChk" property="valor" value="<%=chekedVal%>">
									checked="checked"
								</logic:equal>						    	
							</logic:iterate>
						/> 
						<bean:write name="AtrValChk" property="desValor"/> <br/>
					</logic:iterate>
				</td>
			</logic:equal>
		</logic:equal>
	<!-- Para evitar el error del enter en las cajas de texto -->
    <input type="text" style="display:none"/>	 
	</tr>