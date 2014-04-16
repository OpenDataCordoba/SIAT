package ar.gov.rosario.siat.${modulo}.iface.service;

import org.apache.log4j.Logger;

/**
 * ${Nombre Largo Modulo} - Service locator
 * @author tecso
 */
public class ${Modulo}ServiceLocator {

	static Logger log = Logger.getLogger(${Modulo}ServiceLocator.class);

	// Implementaciones de servicio	
	private static String MODULO = "ar.gov.rosario.siat.${modulo}.buss.service.";
	private static String ${SUBMODULO}_SERVICE_IMPL = MODULO + "${Modulo}${Submodulo}ServiceHbmImpl";
														  
	
	// Instancia
	public static final ${Modulo}ServiceLocator INSTANCE = new ${Modulo}ServiceLocator();

	private I${Modulo}${Submodulo}Service   ${submodulo}ServiceHbmImpl;
	
	
	
	// Constructor de instancia
	public ${Modulo}ServiceLocator() {
		try {

			this.${submodulo}ServiceHbmImpl = (I${Modulo}${Submodulo}Service)   Class.forName(${SUBMODULO}_SERVICE_IMPL).newInstance();			
			
		} catch (Exception e) {
			log.error("No se pudo crear la clase" + e);
		}
	}

	public static I${Modulo}${Submodulo}Service get${Submodulo}Service(){
		return INSTANCE.${submodulo}ServiceHbmImpl;		
	}
	
}
