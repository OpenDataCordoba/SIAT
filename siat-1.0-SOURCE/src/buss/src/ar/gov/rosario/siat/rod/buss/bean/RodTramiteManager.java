//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.rod.buss.dao.RodDAOFactory;

/**
 * Manejador del m&oacute;dulo Rod y submodulo Tramite
 * 
 * @author tecso
 *
 */
public class RodTramiteManager {
		
	private static Logger log = Logger.getLogger(RodTramiteManager.class);
	
	private static final RodTramiteManager INSTANCE = new RodTramiteManager();
	
	/**
	 * Constructor privado
	 */
	private RodTramiteManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static RodTramiteManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM TramiteRA
	public TramiteRA createTramiteRA(TramiteRA tramiteRA,String logCambios) throws Exception {

		// Validaciones de negocio
		if (!tramiteRA.validateCreate()) {
			return tramiteRA;
		}

		RodDAOFactory.getTramiteRADAO().update(tramiteRA);
		
		// Guardamos un evento en el historial
		HisEstTra hisEstTra = tramiteRA.createHisEstTra(tramiteRA.getEstTra(), SiatUtil.getValueFromBundle("rod.tramiteRA.tramiteRAIngresado")+" "+logCambios);

		tramiteRA.getListError().addAll(hisEstTra.getListError());


		return tramiteRA;
	}
	
	public TramiteRA updateTramiteRA(TramiteRA tramiteRA) throws Exception {
		
		// Validaciones de negocio
		if (!tramiteRA.validateUpdate()) {
			return tramiteRA;
		}

		RodDAOFactory.getTramiteRADAO().update(tramiteRA);
		
		
		return tramiteRA;
	}
	
	public TramiteRA deleteTramiteRA(TramiteRA tramiteRA) throws Exception {
	
		// Validaciones de negocio
		if (!tramiteRA.validateDelete()) {
			return tramiteRA;
		}
		
		for (HisEstTra item: tramiteRA.getListHisEstTra()) {
			tramiteRA.deleteHisEstTra(item);
		}
		RodDAOFactory.getTramiteRADAO().delete(tramiteRA);
		
		return tramiteRA;
	}
	// <--- ABM TramiteRA
	
	// ---> ABM Propietario
	public Propietario createPropietario(Propietario propietario) throws Exception {

		// Validaciones de negocio
		if (!propietario.validateCreate()) {
			return propietario;
		}

		RodDAOFactory.getPropietarioDAO().update(propietario);

		return propietario;
	}
	
	public Propietario updatePropietario(Propietario propietario) throws Exception {
		
		// Validaciones de negocio
		if (!propietario.validateUpdate()) {
			return propietario;
		}

		RodDAOFactory.getPropietarioDAO().update(propietario);
		
		return propietario;
	}
	
	public Propietario deletePropietario(Propietario propietario) throws Exception {
	
		// Validaciones de negocio
		if (!propietario.validateDelete()) {
			return propietario;
		}
		
		RodDAOFactory.getPropietarioDAO().delete(propietario);
		
		return propietario;
	}
	
	public Propietario validateCreatePropietario(Propietario propietario) throws Exception {

		// Validaciones de negocio
		if (!propietario.validateCreate()) {
			return propietario;
		}

		return propietario;
	}

	// Propietario
		

}