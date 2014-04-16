<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

						<!-- Boton Activar -->
						<logic:equal name="corridaVO" property="activarEnabled" value="enabled">
								<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='false'>
									<bean:message bundle="pro" key="pro.abm.button.activar"/>
								</html:button>&nbsp;&nbsp;
						</logic:equal>
						<logic:equal name="corridaVO" property="activarEnabled" value="disabled">
								<html:button property="btnAccionBase"  styleClass="boton" disabled='true'>
									<bean:message bundle="pro" key="pro.abm.button.activar"/>
								</html:button>&nbsp;&nbsp;
							</logic:equal>
							
						<!-- Boton Reprogramar -->							
<!--					<logic:present name="corridaVO" property="reprogramarEnabled">						
	 						<logic:equal name="corridaVO" property="reprogramarEnabled" value="enabled">
								<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reprogramar', '');" disabled='false'>
									<bean:message bundle="pro" key="pro.abm.button.reprogramar"/>
								</html:button>&nbsp;&nbsp;
							</logic:equal>
							<logic:equal name="corridaVO" property="reprogramarEnabled" value="disabled">
								<html:button property="btnAccionBase"  styleClass="boton" disabled='true'>
									<bean:message bundle="pro" key="pro.abm.button.reprogramar"/>
								</html:button>&nbsp;&nbsp;
							</logic:equal>
						</logic:present>						
 -->						
						
						<!-- Boton Cancelar -->							
						<logic:present name="corridaVO" property="cancelarEnabled">						
							<logic:equal name="corridaVO" property="cancelarEnabled" value="enabled">
								<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cancelar', '');" disabled='false'>
									<bean:message bundle="pro" key="pro.abm.button.cancelar"/>
								</html:button>&nbsp;&nbsp;
							</logic:equal>
							<logic:equal name="corridaVO" property="cancelarEnabled" value="disabled">
								<html:button property="btnAccionBase"  styleClass="boton" disabled='true'>
									<bean:message bundle="pro" key="pro.abm.button.cancelar"/>
								</html:button>&nbsp;&nbsp;
							</logic:equal>
						</logic:present>
						
						<!-- Boton SiguientePaso -->							
						<logic:present name="corridaVO" property="siguientePasoEnabled">
							<logic:equal name="corridaVO" property="siguientePasoEnabled" value="enabled">
								<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('siguientePaso', '');" disabled='false'>
									<bean:message bundle="pro" key="pro.abm.button.siguiente"/>
								</html:button>&nbsp;&nbsp;
							</logic:equal>
							<logic:equal name="corridaVO" property="siguientePasoEnabled" value="disabled">
								<html:button property="btnAccionBase"  styleClass="boton" disabled='true'>
									<bean:message bundle="pro" key="pro.abm.button.siguiente"/>
								</html:button>&nbsp;&nbsp;
							</logic:equal>
						</logic:present>

						<!-- Boton RetrocederPaso -->							
						<logic:present name="corridaVO" property="retrocederPasoEnabled">
							<logic:equal name="corridaVO" property="retrocederPasoEnabled" value="enabled">
								<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('retroceder', '');" disabled='false'>
									<bean:message bundle="pro" key="pro.abm.button.retroceder"/>
								</html:button>&nbsp;&nbsp;
							</logic:equal>
							<logic:equal name="corridaVO" property="retrocederPasoEnabled" value="disabled">
								<html:button property="btnAccionBase"  styleClass="boton" disabled='true'>
									<bean:message bundle="pro" key="pro.abm.button.retroceder"/>
								</html:button>&nbsp;&nbsp;
							</logic:equal>
						</logic:present>
						
						<!-- Boton Reiniciar -->
						<logic:present name="corridaVO" property="reiniciarEnabled">
							<logic:equal name="corridaVO" property="reiniciarEnabled" value="enabled">
								<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reiniciar', '');" disabled='false'>
									<bean:message bundle="pro" key="pro.abm.button.reiniciar"/>
								</html:button>&nbsp;&nbsp;
							</logic:equal>
							<logic:equal name="corridaVO" property="reiniciarEnabled" value="disabled">
								<html:button property="btnAccionBase"  styleClass="boton" disabled='true'>
									<bean:message bundle="pro" key="pro.abm.button.reiniciar"/>
								</html:button>&nbsp;&nbsp;
							</logic:equal>
						</logic:present>

						<!-- Boton Refrescar -->
						<logic:present name="corridaVO" property="refrescarEnabled">
							<logic:equal name="corridaVO" property="refrescarEnabled" value="enabled">
								<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('refill', '');" disabled='false' styleId="locate_paso">
									<bean:message bundle="pro" key="pro.abm.button.refill"/>
								</html:button>&nbsp;&nbsp;
							</logic:equal>
							<logic:equal name="corridaVO" property="refrescarEnabled" value="disabled">
								<html:button property="btnAccionBase"  styleClass="boton" disabled='true' styleId="locate_paso">
									<bean:message bundle="pro" key="pro.abm.button.refill"/>
								</html:button>&nbsp;&nbsp;
							</logic:equal>
						</logic:present>
						
						<!-- Boton Modificar datos Form -->
 						<logic:present name="corridaVO" property="modifDatosFormEnabled"> 							
							<logic:equal name="corridaVO" property="modifDatosFormEnabled" value="enabled">
								<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('modificarProcesoMasivo', '');" disabled='false' styleId="locate_paso">
									<bean:message bundle="pro" key="pro.abm.button.modifDatosForm"/>
								</html:button>&nbsp;&nbsp;
							</logic:equal>							
						</logic:present>