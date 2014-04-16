<!-- Columna Atributo -->
		<td>
			<bean:write name="AtrVal" property="atributo.desAtributo"/>&nbsp;
		</td>
	
<!-- Columna Valor -->
		<!-- No posee Dominio -->
		<logic:equal name="AtrVal" property="poseeDominio" value="false">
			<td> 
				<bean:write name="AtrVal" property="valorView"/>&nbsp;
			</td>
		</logic:equal>
		
		<!-- Posee Dominio -->
		<logic:equal name="AtrVal" property="poseeDominio" value="true">
			<!-- No es Multivalor -->
			<logic:equal name="AtrVal" property="esMultivalor" value="false">
				<td class="normal"> 
					<!-- Valor del dominio -->
					<bean:write name="AtrVal" property="valorFromDominioView"/>&nbsp;
				</td>
			</logic:equal>
			
			<!-- Es Multivalor -->
			<logic:equal name="AtrVal" property="esMultivalor" value="true">
				<td> 
					<table width="100%" border="0.5">
					
						<!-- Valores del dominio -->
						<logic:iterate id="AtrValChk" name="AtrVal" property="atributo.domAtr.listDomAtrVal">
							<logic:iterate id="ChkdVal" name="AtrVal" property="listMultiValor">
						    	<bean:define id="chkVal" name="ChkdVal"/> 
						    	
						    	<% String chekedVal = ""+chkVal+""; %>						    						    	
						    	
						    	<logic:equal name="AtrValChk" property="valor" value="<%=chekedVal%>">
									<tr>
										<td>	
											<bean:write name="AtrValChk" property="valor"/> - 
											<bean:write name="AtrValChk" property="desValor"/>
										</td>
									</tr>		 
								</logic:equal>						    	
							</logic:iterate>
						</logic:iterate>
					</table>
				</td>
			</logic:equal>
		</logic:equal>

<!-- Columna Vigencia -->
	<td>
	
	
		<!-- Posee Vigencia -->
		<logic:equal name="AtrVal" property="poseeVigencia" value="true">
		
			<logic:equal name="AtrVal" property="esMultivalor" value="true">
				<table width="100%" border="0.5">
			</logic:equal>
		
		
			<logic:notEmpty name="AtrVal" property="listAtrValVig">
				<logic:iterate id="AtrValVig" name="AtrVal" property="listAtrValVig">					
					
					<logic:equal name="AtrVal" property="esMultivalor" value="true">
						<tr><td>						
					</logic:equal>
					
					Desde <bean:write name="AtrValVig" property="fechaDesdeView"/>: 
					<logic:equal name="AtrVal" property="esMultivalor" value="true"><br>&nbsp;&nbsp;</logic:equal>
					
					<bean:write name="AtrValVig" property="valor"/> <br/>
					
					<logic:equal name="AtrVal" property="esMultivalor" value="true">
						</td></tr>						
					</logic:equal>
					
				</logic:iterate>
			</logic:notEmpty>
			
			<logic:equal name="AtrVal" property="esMultivalor" value="true">
				</table>
			</logic:equal>
			
		</logic:equal>
		
		
		<logic:notEqual name="AtrVal" property="poseeVigencia" value="true">
			- 
		</logic:notEqual>
		&nbsp;
	
	</td>
