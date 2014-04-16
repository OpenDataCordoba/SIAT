//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.service;

/**
 * Implementacion de servicios del submodulo Reclamo del modulo Padron
 * @author tecso
 */

import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cas.buss.bean.CasSolicitudManager;
import ar.gov.rosario.siat.cas.buss.bean.EstSolicitud;
import ar.gov.rosario.siat.cas.buss.bean.Solicitud;
import ar.gov.rosario.siat.cas.buss.bean.SolicitudCUIT;
import ar.gov.rosario.siat.cas.buss.bean.TipoSolicitud;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.iface.model.BuzonCambiosAdapter;
import ar.gov.rosario.siat.pad.iface.model.BuzonCambiosVO;
import ar.gov.rosario.siat.pad.iface.model.DocumentoVO;
import ar.gov.rosario.siat.pad.iface.model.TipoDocumentoVO;
import ar.gov.rosario.siat.pad.iface.service.IPadReclamoService;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.TipoModificacion;
import coop.tecso.demoda.iface.model.TipoPersona;
import coop.tecso.demoda.iface.model.UserContext;

public class PadReclamoServiceHbmImpl implements IPadReclamoService {
	private Logger log = Logger.getLogger(PadReclamoServiceHbmImpl.class);
	
	// ---> ABM BuzonCambios 	
	public BuzonCambiosAdapter getBuzonCambiosAdapterForCreate(UserContext userContext, CommonKey commonKeyTitular) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			BuzonCambiosAdapter buzonCambiosAdapter = new BuzonCambiosAdapter();
			
			// Cargamos las listas para los combos			
			buzonCambiosAdapter.setListTipoModificacion(TipoModificacion.getList());
			buzonCambiosAdapter.getBuzonCambios().setTipoModificacion(TipoModificacion.CUIT);
			
			buzonCambiosAdapter.setListTipoPersona(TipoPersona.getList());
			
			// Se quitan estas opciones por solicitud en Mantis 5023.
			//TipoDocumentoVO tipoDocDNI = new TipoDocumentoVO(1L, "DNI", "DNI");
			//TipoDocumentoVO tipoDocLE = new TipoDocumentoVO(2L, "LE", "LE");
			//TipoDocumentoVO tipoDocLC = new TipoDocumentoVO(3L, "LC", "LC");
			TipoDocumentoVO tipoDocCuit = new TipoDocumentoVO(4L, "Cuit", "Cuit");
			
			//buzonCambiosAdapter.getListTipoDocumento().add(tipoDocDNI);
			//buzonCambiosAdapter.getListTipoDocumento().add(tipoDocLE);
			//buzonCambiosAdapter.getListTipoDocumento().add(tipoDocLC);
			buzonCambiosAdapter.getListTipoDocumento().add(tipoDocCuit);
			
			Persona persona = Persona.getById(commonKeyTitular.getId());
			
			buzonCambiosAdapter.getBuzonCambios().setIdPersona(persona.getId());
			
			if (persona.getEsPersonaFisica()){
				buzonCambiosAdapter.getBuzonCambios().setNombres(persona.getNombres());
				buzonCambiosAdapter.getBuzonCambios().setApellido(persona.getApellido());
				buzonCambiosAdapter.getBuzonCambios().setTipoPersona(TipoPersona.FISICA);
				if (persona.getDocumento() != null){
					buzonCambiosAdapter.getBuzonCambios().setDocumento((DocumentoVO)persona.getDocumento().toVO(2));
				}
				
			} else {
				buzonCambiosAdapter.getBuzonCambios().setRazonSocial(persona.getRazonSocial());
				buzonCambiosAdapter.getBuzonCambios().setTipoPersona(TipoPersona.JURIDICA);
			}
			
			buzonCambiosAdapter.getBuzonCambios().setCuit00(persona.getCuit00());
			buzonCambiosAdapter.getBuzonCambios().setCuit01(persona.getCuit01());
			buzonCambiosAdapter.getBuzonCambios().setCuit02(persona.getCuit02());
			buzonCambiosAdapter.getBuzonCambios().setCuit03(persona.getCuit03());
			
			
			log.debug(funcName + " cuit00: " + persona.getCuit00() +
					" cuit01: " + persona.getCuit01() +
					" cuit02: " + persona.getCuit02() +
					" cuit03: " + persona.getCuit03() );
			
			
			log.debug(funcName + ": exit");
			return buzonCambiosAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}


	public BuzonCambiosVO createBuzonCambios(UserContext userContext, BuzonCambiosVO buzonCambiosVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			buzonCambiosVO.clearErrorMessages();
			 
			
			SolicitudCUIT solicitudCUIT = new SolicitudCUIT();
			
			if (!buzonCambiosVO.validate()){
				
				return buzonCambiosVO;
			}
						
			String fullDescripcion = "";
			
			fullDescripcion = "TipoModificacion=" + buzonCambiosVO.getTipoModificacion().getValue();
			
			fullDescripcion += "; TipoPersona=" + buzonCambiosVO.getTipoPersona().getValue();
			
			fullDescripcion += "; Cuit00=" +  buzonCambiosVO.getCuit00();
			fullDescripcion += "; Cuit01=" +  buzonCambiosVO.getCuit01();
			fullDescripcion += "; Cuit02=" +  buzonCambiosVO.getCuit02();
			fullDescripcion += "; Cuit03=" +  buzonCambiosVO.getCuit03();
			
			// Modificacion de Cuit
			if (buzonCambiosVO.getTipoModificacion().getEsCuit()){
				if (buzonCambiosVO.getTipoPersona().getEsFisica()){
					fullDescripcion += "; TipoDocumento=" +  buzonCambiosVO.getDocumento().getTipoDocumento().getAbreviatura();
					solicitudCUIT.setTipoDocReportado(buzonCambiosVO.getDocumento().getTipoDocumento().getAbreviatura());
				} else {
					fullDescripcion += "; TipoDocumento=Cuit"; 
					solicitudCUIT.setTipoDocReportado("CUIT");
				}
				
				fullDescripcion += "; NumeroDocumento=" +  buzonCambiosVO.getDocumento().getNumeroView();
				solicitudCUIT.setDocReportado(buzonCambiosVO.getDocumento().getNumero());
			} else {
				// Modificacion de Denominacion
				
				if (buzonCambiosVO.getTipoPersona().getEsFisica()){
					fullDescripcion += "; Apellido=" + buzonCambiosVO.getApellido();
					fullDescripcion += "; Nombres=" + buzonCambiosVO.getNombres();
					solicitudCUIT.setApellidoReportado(buzonCambiosVO.getApellido());
					solicitudCUIT.setNombreReportado(buzonCambiosVO.getNombres());
				} else {
					fullDescripcion += "; RazonSocial=" + buzonCambiosVO.getRazonSocial();
					solicitudCUIT.setRazonSocReportado(buzonCambiosVO.getRazonSocial());
				}
			}
			
			fullDescripcion += "; Contacto=" + buzonCambiosVO.getContacto() + 
			 				   "; Observaciones=" + buzonCambiosVO.getObservaciones();
			
			Solicitud solicitud = new Solicitud();
						
			Area areaOrigen = Area.getById(userContext.getIdArea());
			Area areaDestino = Area.getByCodigo(Area.COD_EMISION_PADRONES);
			
			solicitud.setAreaOrigen(areaOrigen);
			solicitud.setAreaDestino(areaDestino);
			
			solicitud.setFechaAlta(new Date());
			
			solicitud.setUsuarioAlta(userContext.getUserName());
			
			TipoSolicitud tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_MODIFICACION_IDENTIFICACION_PERSONA);
			
			solicitud.setTipoSolicitud(tipoSolicitud);
			
			solicitud.setAsuntoSolicitud("Solicitud de Cambio de datos de Persona");
			solicitud.setDescripcion(fullDescripcion);
			
			EstSolicitud estSolicitud = EstSolicitud.getById(EstSolicitud.ID_PENDIENTE);
			solicitud.setEstSolicitud(estSolicitud);
			solicitud.addLogCreateSolicitud(userContext.getUserName());
			
			solicitud =CasSolicitudManager.getInstance().createSolicitud(solicitud);
			
			
            if (solicitud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				log.debug("Creo la solicitud - va a crear la SolicitudCUIT");
				session.flush();
				
				// crear SolicitudCUIT
				solicitudCUIT.setSolicitud(solicitud);
				solicitudCUIT.setIdPersona(buzonCambiosVO.getIdPersona());
				solicitudCUIT.setTipoPersona(buzonCambiosVO.getTipoPersona().getId());
				solicitudCUIT.setTipoModificacion(buzonCambiosVO.getTipoModificacion().getId());
				// setea el string de los datos reportados (es lo que se muestra en el view o edit, en el label "Datos Registrados en Padron Contr.:")
				Persona persona = Persona.getById(buzonCambiosVO.getIdPersona());
				solicitudCUIT.setDatosRegPadron(buzonCambiosVO.getTipoPersona().getValue()+" - "+
											buzonCambiosVO.getCuit00()+"-"+buzonCambiosVO.getCuit01()+"-"+
											buzonCambiosVO.getCuit02()+"-"+buzonCambiosVO.getCuit03()+" - "+
											persona.getRepresent());
				
				CasSolicitudManager.getInstance().createSolicitudCUIT(solicitudCUIT);
				
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
            solicitud.passErrorMessages(buzonCambiosVO);
            
            
            log.debug(funcName + ": exit");
            return buzonCambiosVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM BuzonCambios

}
