		<!-- ${Bean_Detalle} -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="${modulo}" key="${modulo}.${bean}.list${Bean_Detalle}.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="${bean}AdapterVO" property="${bean}.list${Bean_Detalle}">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="${modulo}" key="${modulo}.${bean_Detalle}.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="${modulo}" key="${modulo}.${bean_Detalle}.fechaHasta.label"/></th>
					</tr>
					<logic:iterate id="${Bean_Detalle}VO" name="${bean}AdapterVO" property="${bean}.list${Bean_Detalle}">
			
						<tr>
							<!-- Modificar-->								
							<td>
								<logic:equal name="${bean}AdapterVO" property="modificar${Bean_Detalle}Enabled" value="enabled">
									<logic:equal name="${Bean_Detalle}VO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar${Bean_Detalle}', '<bean:write name="${Bean_Detalle}VO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="${Bean_Detalle}VO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="${bean}AdapterVO" property="modificar${Bean_Detalle}Enabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="${bean}AdapterVO" property="eliminar${Bean_Detalle}Enabled" value="enabled">
									<logic:equal name="${Bean_Detalle}VO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar${Bean_Detalle}', '<bean:write name="${Bean_Detalle}VO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="${Bean_Detalle}VO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="${bean}AdapterVO" property="eliminar${Bean_Detalle}Enabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="${Bean_Detalle}VO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="${Bean_Detalle}VO" property="fechaHastaView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="${bean}AdapterVO" property="${bean}.list${Bean_Detalle}">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="${bean}AdapterVO" property="agregar${Bean_Detalle}Enabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar${Bean_Detalle}', '<bean:write name="${bean}AdapterVO" property="${bean}.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
			</tbody>
		</table>
		<!-- ${Bean_Detalle} -->