//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.fra.iface.util;

import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.MessageResources;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.fra.iface.model.FraProperties;
import ar.gov.rosario.siat.fra.iface.service.FraServiceLocator;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.UserContext;

public class Frase extends MessageResources {
	
	private static final long serialVersionUID = 1L;

	private Log log = LogFactory.getLog(Frase.class);
	
	private static Frase INSTANCE = new Frase();
	
	FraProperties fraProperties = new FraProperties(); 
	
		
	public static Frase getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Frase();
		}
		return INSTANCE;
	}
	
	
	
	public Frase() {
		super(defaultFactory, "frase", false);
		
		log.info("#######  Frase() #########");
				
		try {

			Integer checkNewFrase = SiatParam.getInteger("CheckNewFrase");
			
			if (checkNewFrase == 1) {
				log.debug("Checkear frases desde DB: SI");

				this.checkNew();
				this.load();
			} else {
				log.debug("Checkear frases desde DB: NO");
				
				this.loadFromFile();
			}
			
			if(log.isDebugEnabled()){
				log.debug("propiedades cargadas - privadas: --------------");
				Properties p = this.fraProperties.getPropiedadesPrivadas();
				for(Object key: p.keySet()){
					log.debug(key+": "+p.getProperty((String) key));
				}
				
				log.debug("propiedades cargadas - publicas: --------------");
				p = this.fraProperties.getPropiedadesPublicas();
				for(Object key: p.keySet()){
					log.debug(key+": "+p.getProperty((String) key));
				}			
			}
				
		} catch (Exception e){
			log.error(e);
		} 
	}
	
	
	
	/**
	 * Metodo utilizado por struts para obtener el valor de una key y dibujar en jsp. 
	 * 
	 */
	public String getMessageBykey(String arg1) {
	
		try {
			
			log.info("####### Frase -> getMessage key " + arg1);
			
			UserContext userContext = DemodaUtil.currentUserContext();
			
			String roles = userContext.getCodsRolUsuario();
		
			// Si el usuario es un administrador de frases. 
			// Busca en la lista de privadas, si no la encuentra, la busca en las publicas
			log.info("####### Frase -> roles: " + roles); 
			
			if (roles.contains("frases")){
				
				log.info("####### Frase -> buscando frase Privada ...");
				if (this.fraProperties.getPropiedadesPrivadas().containsKey(arg1)){
					log.info("####### Frase -> frase Privada encontrada");	
					return this.fraProperties.getPropiedadesPrivadas().getProperty(arg1);
				}
				
				log.info("####### Frase -> frase Privada no encontrada, retornando frase Publica ...");
				return this.fraProperties.getPropiedadesPublicas().getProperty(arg1);
			} else {
				
				log.info("####### Frase -> buscando frase Publica ...");
				return this.fraProperties.getPropiedadesPublicas().getProperty(arg1);
			}
		
		} catch (Exception e){
		    e.printStackTrace();
		    return "key no encontrada";
		}
	}

	public synchronized void reload() throws DemodaServiceException {
		log.info("####### Frase -> reload() ");
		
		this.fraProperties.getPropiedadesPublicas().clear();
		this.fraProperties.getPropiedadesPrivadas().clear();
		this.formats.clear();
	    
		this.load();
	}

	
	private void load() throws DemodaServiceException{
		
		// levantamos los valores de las properties desde la db
		this.loadFromService();

	}
	
	
	/**
	 *  Levanta y devuelve el un propertie con el contenido del archivo fra.properties.
	 * 
	 * @author Cristian
	 * @return properties
	 */
	private Properties getPropertiesFromFile(){
		Properties properties = new Properties();

		try {

	    	InputStream stream = getClass().getResourceAsStream("/resources/frase.properties");
	    	if (stream != null) {
	    		properties.load(stream);
	    	}
	    	
	    	return properties;
	    } catch (Exception e){
	    	e.printStackTrace();
	    	log.error("Ocurrio un error al intentar cargar el archivo de propiedades ");
	    	return null;
	    }
	}
	
	/**	 
	 * Carga las propiedaes desde el archivo.
	 * 
	 * @author tecso
	 */
	private void loadFromFile() throws DemodaServiceException {
		  
		this.fraProperties.getPropiedadesPublicas().putAll( this.getPropertiesFromFile());
		
	}

	

	/**
	 * Carga en "fraProperties" las propiedades publicas y privadas(para el usuario publicador) desde la DB. 
	 * @throws DemodaServiceException 
	 *  
	 * 
	 * 
	 */
	private void loadFromService() throws DemodaServiceException{
				
		FraServiceLocator.getFraseService().loadFraProperties(this.fraProperties);
		
	}
	
	
	
	/**
	 * Sincroniza las propiedades del archivo fra.properties con la DB.
	 * @throws DemodaServiceException 
	 * 
	 * 
	 */ 
	private void checkNew() throws DemodaServiceException{		
		Properties propiedades = getPropertiesFromFile();
		FraServiceLocator.getFraseService().checkNew(propiedades);		
	}

	@Override
	public String getMessage(Locale arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	} 
	
}