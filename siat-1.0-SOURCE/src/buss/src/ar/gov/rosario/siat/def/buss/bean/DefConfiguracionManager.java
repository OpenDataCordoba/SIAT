//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;

/**
 * Manejador del submodulo de configuracion
 * 
 * @author tecso
 *
 */
public class DefConfiguracionManager {
		
	private static Logger log = Logger.getLogger(DefConfiguracionManager.class);
	
	private static final DefConfiguracionManager INSTANCE = new DefConfiguracionManager();
	
	/**
	 * Constructor privado
	 */
	private DefConfiguracionManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static DefConfiguracionManager getInstance() {
		return INSTANCE;
	}

	
	public Area createArea(Area area) throws Exception {

		// Validaciones de negocio
		if (!area.validateCreate()) {
			return area;
		}

		DefDAOFactory.getAreaDAO().update(area);

		return area;
	}
	
	
	public Area updateArea(Area area) throws Exception {
		
		// Validaciones de negocio
		if (!area.validateUpdate()) {
			return area;
		}

		DefDAOFactory.getAreaDAO().update(area);

		return area;
	}
	
	
	public Area deleteArea(Area area) throws Exception {
		
		// Validaciones de negocio
		if (!area.validateDelete()) {
			return area;
		}

		DefDAOFactory.getAreaDAO().delete(area);

		return area;
	}
	
	
	// ---> ABM Parametro
	public Parametro createParametro(Parametro parametro) throws Exception {

		// Validaciones de negocio
		if (!parametro.validateCreate()) {
			return parametro;
		}

		DefDAOFactory.getParametroDAO().update(parametro);

		return parametro;
	}
	
	public Parametro updateParametro(Parametro parametro) throws Exception {
		
		// Validaciones de negocio
		if (!parametro.validateUpdate()) {
			return parametro;
		}

		DefDAOFactory.getParametroDAO().update(parametro);
		
		return parametro;
	}
	
	public Parametro deleteParametro(Parametro parametro) throws Exception {
	
		// Validaciones de negocio
		if (!parametro.validateDelete()) {
			return parametro;
		}
		
		DefDAOFactory.getParametroDAO().delete(parametro);
		
		return parametro;
	}
	// <--- ABM Parametro
	
	
	// ---> ABM SiatScript
	public SiatScript createSiatScript(SiatScript siatScript) throws Exception {

		// Validaciones de negocio
		if (!siatScript.validateCreate()) {
			return siatScript;
		}

		DefDAOFactory.getSiatScriptDAO().update(siatScript);

		return siatScript;
	}
	
	public SiatScript updateSiatScript(SiatScript siatScript) throws Exception {
		
		// Validaciones de negocio
		if (!siatScript.validateUpdate()) {
			return siatScript;
		}

		DefDAOFactory.getSiatScriptDAO().update(siatScript);
		
		return siatScript;
	}
	
	public SiatScript deleteSiatScript(SiatScript siatScript) throws Exception {
	
		// Validaciones de negocio
		if (!siatScript.validateDelete()) {
			return siatScript;
		}
		
		DefDAOFactory.getSiatScriptDAO().delete(siatScript);
		
		return siatScript;
	}
	// <--- ABM SiatScript
	
	
	// ----> Actualiza la siat Param
	/**
	 * Actualiza el contenido del mapa de los parametros del singleton SiatParam en Iface.
	 * @throws Exception
	 */
	public void updateSiatParam() throws Exception {
		//-- recorrer todos los parametros
		
		
		log.debug("********	  UPDATE SIAT PARAM  ********");
		
		//-- armar Map<String, String>
		Map<String, String> mapa = new HashMap<String, String>();
		
		for (Parametro p : Parametro.getList()) {
			log.debug("CodParam: " + p.getCodParam() + " valor: " + p.getValor());
			mapa.put(p.getCodParam(), p.getValor());
		}
		//-- SiatParam.getInstance.updateValues(mapa);
		SiatParam.getInstance().updateValues(mapa);
		
		//--al servicio DefConfiguracionService hascerle updateSiatParam
		//--llamarlo desde BaseDispatch static
	}
	// <---- Actualiza la siat Param

	// ---> ABM SiatScriptUsr
	public SiatScriptUsr createSiatScriptUsr(SiatScriptUsr siatScriptUsr) throws Exception {
		// Validaciones de negocio
		if (!siatScriptUsr.validateCreate()) {
			return siatScriptUsr;
		}

		DefDAOFactory.getSiatScriptUsrDAO().update(siatScriptUsr);

		return siatScriptUsr;
	}


	public SiatScriptUsr updateSiatScriptUsr(SiatScriptUsr siatScriptUsr) throws Exception {
		
		if (!siatScriptUsr.validateUpdate()) {
			return siatScriptUsr;
		}

		DefDAOFactory.getSiatScriptUsrDAO().update(siatScriptUsr);

		return siatScriptUsr;
	}

	public SiatScriptUsr deleteSiatScriptUsr(SiatScriptUsr siatScriptUsr) {
		
		DefDAOFactory.getSiatScriptUsrDAO().delete(siatScriptUsr);
		
		return siatScriptUsr;
	}
	
	// <--- ABM SiatScriptUsr
}
