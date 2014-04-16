//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import org.apache.log4j.Logger;

import coop.tecso.demoda.iface.helper.NumberUtil;

import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.CalleVO;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.model.LocalidadVO;

/**
 * Manejador del m&oacute;dulo Pad y submodulo Domicilio.
 * 
 * @author tecso
 *
 */
public class PadDomicilioManager {
private static Logger log = Logger.getLogger(PadDomicilioManager.class);
	
	private static final PadDomicilioManager INSTANCE = new PadDomicilioManager();
	
	/**
	 * Constructor privado
	 */
	private PadDomicilioManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static PadDomicilioManager getInstance() {
		return INSTANCE;
	}
	
	// ---> ABM Domicilio
	
	public Domicilio createDomicilio(Domicilio domicilio) throws Exception {

		// Validaciones de negocio
		if (!domicilio.validateCreate()) {
			return domicilio;
		}

		PadDAOFactory.getDomicilioDAO().update(domicilio);

		
		return domicilio;
	}
	
	public Domicilio updateDomicilio(Domicilio domicilio) throws Exception {
		
		// Validaciones de negocio
		if (!domicilio.validateUpdate()) {
			return domicilio;
		}

		PadDAOFactory.getDomicilioDAO().update(domicilio);
		
		return domicilio;
	}
	
	public Domicilio deleteDomicilio(Domicilio domicilio) throws Exception {
	
		// Validaciones de negocio
		if (!domicilio.validateDelete()) {
			return domicilio;
		}
		
		PadDAOFactory.getDomicilioDAO().delete(domicilio);
		
		return domicilio;
	}
	// <--- ABM Domicilio

	
	public Domicilio obtenerDomicilio(DomicilioVO domicilioVO) throws Exception {
		
		if (domicilioVO == null) {
			return null;
		}
		
		Domicilio domicilio = Domicilio.getByIdNull(domicilioVO.getId());
		
		if (domicilio == null){
			domicilio = new Domicilio();
		}
			//localidad
			LocalidadVO localidadVO = domicilioVO.getLocalidad();
			Localidad localidad = null;
			if ( localidadVO != null ) {  // no utilizar !ModelUtil.isNullOrEmpty(localidadVO)
				localidad = new Localidad();
				localidad.setId(localidadVO.getId());
				localidad.setCodPostal(localidadVO.getCodPostal());
				localidad.setCodSubPostal(localidadVO.getCodSubPostal());
								
				Long codPostal = NumberUtil.getLong(domicilioVO.getCodPostalFueraRosario());
				if (codPostal != null)  {
					localidad = UbicacionFacade.getInstance().getLocalidad(codPostal, 0L);
				}
			}
			
			
			//calle
			CalleVO calleVO = domicilioVO.getCalle();
			Calle calle = null;
			if ( calleVO != null ) { //No se utiliza !ModelUtil.isNullOrEmpty(calleVO).
				calle = new Calle();
				calle.setId(calleVO.getId());
				calle.setNombreCalle(calleVO.getNombreCalle());
			}

			// seteo los datos
			domicilio.setLocalidad(localidad);
			domicilio.setCalle(calle);
			domicilio.setNumero(domicilioVO.getNumero());
			domicilio.setLetraCalle(domicilioVO.getLetraCalle());
			domicilio.setPiso(domicilioVO.getPiso());				
			domicilio.setMonoblock(domicilioVO.getMonoblock());
			domicilio.setRefGeografica(domicilioVO.getRefGeografica());
			domicilio.setBis(domicilioVO.getBis().getBussId());
			domicilio.setDepto(domicilioVO.getDepto());
			try {
				if(domicilio.getLocalidad().isRosario())
					domicilio.setCodPostalFueraRosario("");
				else
					domicilio.setCodPostalFueraRosario(domicilioVO.getCodPostalFueraRosario());
			} catch (Exception e) {				
				log.error("Verificando si la localidad es de Rosario: "+e);
			}
			
			return domicilio;
	}

}
