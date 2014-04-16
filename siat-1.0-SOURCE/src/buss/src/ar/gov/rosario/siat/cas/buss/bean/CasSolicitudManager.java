//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.cas.buss.dao.CasDAOFactory;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;

/**
 * Manejador del m&oacute;dulo Cas y submodulo Solicitud
 * 
 * @author tecso
 *
 */
public class CasSolicitudManager {
	
	private Log log = LogFactory.getLog(CasSolicitudManager.class);
	
	private static final CasSolicitudManager INSTANCE = new CasSolicitudManager();
	
	/**
	 * Constructor privado
	 */
	private CasSolicitudManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static CasSolicitudManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM Solicitud
	public Solicitud createSolicitud(Solicitud solicitud) throws Exception {

		// Validaciones de negocio
		if (!solicitud.validateCreate()) {
			return solicitud;
		}

		CasDAOFactory.getSolicitudDAO().update(solicitud);

		return solicitud;
	}
	
	
	public Solicitud createSolicitud(TipoSolicitud tipoSolicitud, String asunto, String descripcion) 
		throws Exception {
		
		log.debug("createSolicitud() tipoSolicitud.codTipoSolicitud=" + tipoSolicitud.getCodigo()  + 
									" asunto=" + asunto +
									" descripcion=" + descripcion +" )");
		
		Solicitud solicitud = new Solicitud(tipoSolicitud, asunto, descripcion);  

		solicitud = this.createSolicitud(solicitud);
		
		return solicitud;
	}
	
	public Solicitud createSolicitud(TipoSolicitud tipoSolicitud, String asunto, String descripcion, Cuenta cuenta) 
		throws Exception {
		
		log.debug("createSolicitud() tipoSolicitud.codTipoSolicitud=" + tipoSolicitud.getCodigo() + 
				" asunto=" + asunto +
				" descripcion=" +  descripcion +
				" cuenta=" + cuenta.getId() + " )");
		
		Solicitud solicitud = new Solicitud(tipoSolicitud, asunto, descripcion);  
		solicitud.setCuenta(cuenta);
		solicitud = this.createSolicitud(solicitud);
		
		return solicitud;
	}	

	public Solicitud createSolicitud(String codTipoSolicitud, String asunto, String descripcion)	
		throws Exception {
		
		log.debug("createSolicitud() codTipoSolicitud=" + codTipoSolicitud + 
				" asunto=" + asunto +
				" descripcion=" +  descripcion + " )");
		
		TipoSolicitud tipoSolicitud = TipoSolicitud.getByCodigo(codTipoSolicitud);  
		Solicitud solicitud = this.createSolicitud(tipoSolicitud, asunto, descripcion);
		
		return solicitud;
	}

	public Solicitud createSolicitud(String codTipoSolicitud, String asunto, String descripcion, Cuenta cuenta) 
		throws Exception {
		
		log.debug("createSolicitud() codTipoSolicitud=" + codTipoSolicitud + 
											" asunto=" + asunto +
											" descripcion=" +  descripcion +
											" cuenta=" + cuenta.getId() + " )");
		
		TipoSolicitud tipoSolicitud = TipoSolicitud.getByCodigo(codTipoSolicitud);		
		Solicitud solicitud = new Solicitud(tipoSolicitud, asunto, descripcion, cuenta);  

		solicitud = this.createSolicitud(solicitud);

		return solicitud;
	}
	
	public Solicitud createSolicitud(String codTipoSolicitud, String asunto, String descripcion, Cuenta cuenta, String codAreaOrigen) 
		throws Exception {
			
			Area areaOrigen = Area.getByCodigo(codAreaOrigen);
			log.debug("createSolicitud() codTipoSolicitud=" + codTipoSolicitud + 
												" areaOrigen=" + areaOrigen.getDesArea() +
												" asunto=" + asunto +
												" descripcion=" +  descripcion +
												" cuenta=" + cuenta.getId() + " )");
			
			TipoSolicitud tipoSolicitud = TipoSolicitud.getByCodigo(codTipoSolicitud);		
			Solicitud solicitud = new Solicitud(tipoSolicitud, asunto, descripcion, cuenta);  
			solicitud.setAreaOrigen(areaOrigen);
			
			solicitud = this.createSolicitud(solicitud);

			return solicitud;
		}

	
	public Solicitud createSolicitud(TipoSolicitud tipoSolicitud, String asunto, String descripcion, Area areaDestino) throws Exception {
			
		log.debug("createSolicitud() tipoSolicitud.codTipoSolicitud=" + tipoSolicitud.getCodigo()  + 
									" asunto=" + asunto +
									" descripcion=" + descripcion +"" +
									" areaDestino.codArea=" + areaDestino.getCodArea() + ")");
		
		Solicitud solicitud = new Solicitud(tipoSolicitud, asunto, descripcion, areaDestino);  

		solicitud = this.createSolicitud(solicitud);
		
		return solicitud;
	}

	
	public Solicitud updateSolicitud(Solicitud solicitud) throws Exception {
		
		// Validaciones de negocio
		if (!solicitud.validateUpdate()) {
			return solicitud;
		}

		CasDAOFactory.getSolicitudDAO().update(solicitud);
		
		return solicitud;
	}

	public Solicitud deleteSolicitud(Solicitud solicitud) throws Exception {

		// Validaciones de negocio
		if (!solicitud.validateDelete()) {
			return solicitud;
		}

		CasDAOFactory.getSolicitudDAO().delete(solicitud);

		return solicitud;
	}
	// <--- ABM Solicitud

	public Solicitud cambiarEstadoSolicitud(Solicitud solicitud) throws Exception {
		// Validaciones de negocio
		if (!solicitud.validateCambiarEstado()) {
			return solicitud;
		}

		CasDAOFactory.getSolicitudDAO().update(solicitud);
		
		return solicitud;
	}

	public SolicitudCUIT createSolicitudCUIT(SolicitudCUIT solicitudCUIT) throws Exception {
		// Validaciones de negocio
		if (!solicitudCUIT.validateCreate()) {
			return solicitudCUIT;
		}

		CasDAOFactory.getSolicitudCUITDAO().update(solicitudCUIT);

		return solicitudCUIT;
		
	}

	public SolicitudCUIT updateSolicitudCUIT(SolicitudCUIT solicitudCUIT) throws Exception {
		// Validaciones de negocio
		if (!solicitudCUIT.validateUpdate()) {
			return solicitudCUIT;
		}

		CasDAOFactory.getSolicitudCUITDAO().update(solicitudCUIT);

		return solicitudCUIT;
		
	}

	// ---> ABM TipoSolicitud
	public TipoSolicitud createTipoSolicitud(TipoSolicitud tipoSolicitud) throws Exception {

		// Validaciones de negocio
		if (!tipoSolicitud.validateCreate()) {
			return tipoSolicitud;
		}

		CasDAOFactory.getTipoSolicitudDAO().update(tipoSolicitud);

		return tipoSolicitud;
	}
	
	public TipoSolicitud updateTipoSolicitud(TipoSolicitud tipoSolicitud) throws Exception {
		
		// Validaciones de negocio
		if (!tipoSolicitud.validateUpdate()) {
			return tipoSolicitud;
		}

		CasDAOFactory.getTipoSolicitudDAO().update(tipoSolicitud);
		
		return tipoSolicitud;
	}
	
	public TipoSolicitud deleteTipoSolicitud(TipoSolicitud tipoSolicitud) throws Exception {
	
		// Validaciones de negocio
		if (!tipoSolicitud.validateDelete()) {
			return tipoSolicitud;
		}
		
		CasDAOFactory.getTipoSolicitudDAO().delete(tipoSolicitud);
		
		return tipoSolicitud;
	}
	// <--- ABM TipoSolicitud

	
	// ---> ABM AreaSolicitud
	public AreaSolicitud createAreaSolicitud(AreaSolicitud areaSolicitud) throws Exception {

		// Validaciones de negocio
		if (!areaSolicitud.validateCreate()) {
			return areaSolicitud;
		}

		CasDAOFactory.getAreaSolicitudDAO().update(areaSolicitud);

		return areaSolicitud;
	}
	
	public AreaSolicitud updateAreaSolicitud(AreaSolicitud areaSolicitud) throws Exception {
		
		// Validaciones de negocio
		if (!areaSolicitud.validateUpdate()) {
			return areaSolicitud;
		}

		CasDAOFactory.getAreaSolicitudDAO().update(areaSolicitud);
		
		return areaSolicitud;
	}
	
	public AreaSolicitud deleteAreaSolicitud(AreaSolicitud areaSolicitud) throws Exception {
	
		// Validaciones de negocio
		if (!areaSolicitud.validateDelete()) {
			return areaSolicitud;
		}
		
		CasDAOFactory.getAreaSolicitudDAO().delete(areaSolicitud);
		
		return areaSolicitud;
	}
	// <--- ABM AreaSolicitud
	

}