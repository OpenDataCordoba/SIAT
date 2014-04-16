//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.Vencimiento;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Manejador del m&oacute;dulo Gde y submodulo GDeudaAuto
 * 
 * @author tecso
 *
 */
public class GdeGDeudaAutoManager {
		
	private static Logger log = Logger.getLogger(GdeGDeudaAutoManager.class);
	
	private static final GdeGDeudaAutoManager INSTANCE = new GdeGDeudaAutoManager();
	
	/**
	 * Constructor privado
	 */
	private GdeGDeudaAutoManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static GdeGDeudaAutoManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM Multa
	public Multa createMulta(Multa multa) throws Exception {

		// Validaciones de negocio
		if (!multa.validateCreate()) {
			return multa;
		}

		GdeDAOFactory.getMultaDAO().update(multa);

		return multa;
	}
	
	public Multa updateMulta(Multa multa) throws Exception {
		
		// Validaciones de negocio
		if (!multa.validateUpdate()) {
			return multa;
		}

		GdeDAOFactory.getMultaDAO().update(multa);
		
		return multa;
	}
	
	public Multa deleteMulta(Multa multa) throws Exception {
	
		// Validaciones de negocio
		if (!multa.validateDelete()) {
			return multa;
		}
		
		GdeDAOFactory.getMultaDAO().delete(multa);
		
		return multa;
	}
	// <--- ABM Multa
	
	// ---> ABM MultaDet
	public MultaDet createMultaDet(MultaDet multaDet) throws Exception {

		// Validaciones de negocio
		if (!multaDet.validateCreate()) {
			return multaDet;
		}

		GdeDAOFactory.getMultaDetDAO().update(multaDet);

		return multaDet;
	}
	
	public MultaDet deleteMultaDet(MultaDet multaDet) throws Exception {
		
		// Validaciones de negocio
		if (!multaDet.validateDelete()) {
			return multaDet;
		}
		
		GdeDAOFactory.getMultaDAO().delete(multaDet);
		
		return multaDet;
	}
	// ---> MultaDet
	
	// ---> Desglose
	public Desglose createDesglose(Desglose desglose, List<DesgloseDet> listDesgloseDet) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = SiatHibernateUtil.currentSession();
		
		try {
			
		// Validaciones de negocio
		if (!desglose.validateCreate()) {
			return desglose;
		}

		GdeDAOFactory.getDesgloseDAO().update(desglose);
		session.flush();
		log.debug("----------------------------------creamos el deglose");

		// Graba la lista de Desglose detalle
		for(DesgloseDet desgloseDet: listDesgloseDet){
			desgloseDet.setDesglose(desglose);
			desgloseDet = desgloseDet.createDesgloseDet(desgloseDet);
			
			if (desgloseDet.hasError()){
				desgloseDet.passErrorMessages(desglose);
				return desglose;
			}
			}
		log.debug("----------------------------------creamos los degloses detalles");
		
		
	} catch (Exception e) {
		log.error(e);
		e.printStackTrace();
	}
    
	return desglose;
}
	
	public Desglose updateDesglose(Desglose desglose) throws Exception {
		
		// Validaciones de negocio
		if (!desglose.validateUpdate()) {
			return desglose;
		}

		GdeDAOFactory.getDesgloseDAO().update(desglose);
		
		return desglose;
	}
	
	public Desglose deleteDesglose(Desglose deglose) throws Exception {
	
		// Validaciones de negocio
		if (!deglose.validateDelete()) {
			return deglose;
		}
		
		GdeDAOFactory.getDesgloseDAO().delete(deglose);
		
		return deglose;
	}
	// <--- ABM Desglose
	

	// ---> Cierre Comercio
	public CierreComercio createCierreComercio(CierreComercio cierreComercio) throws Exception {

		// Validaciones de negocio
		if (!cierreComercio.validateCreate()) {
			return cierreComercio;
		}

		GdeDAOFactory.getCierreComercioDAO().update(cierreComercio);

		return cierreComercio;
	}
	
	public CierreComercio updateCierreComercio(CierreComercio cierreComercio) throws Exception {
		
		// Validaciones de negocio
		if (!cierreComercio.validateUpdate()) {
			return cierreComercio;
		}

		GdeDAOFactory.getCierreComercioDAO().update(cierreComercio);
		
		return cierreComercio;
	}
	
	// <--- Cierre Comercio
	
	
	public Contribuyente updateFecVenListDeuForTipoContribuyente(Contribuyente contribuyente, Date fechaDesde){
		
		contribuyente.clearError();
		
		
		Vencimiento venConDirDrei = Recurso.getDReI().getVencimiento();
		
		Vencimiento venConDirEtur = Recurso.getETur().getVencimiento();
		
		if(venConDirDrei==null){
			contribuyente.addRecoverableValueError("El recurso DReI no posee definido un vencimiento");
		}
		
		if(venConDirEtur==null){
			contribuyente.addRecoverableValueError("El recurso ETuR no posee definido un vencimiento");
		}
		
		if(contribuyente.hasError())
			return contribuyente;
		
		List<DeudaAdmin>listDeuda=GdeDAOFactory.getDeudaAdminDAO().getListDreiEturNoDeclaradaByContribuyente(contribuyente, fechaDesde);
		
		log.debug("Lista de Deudas: "+listDeuda.size());
		
		Vencimiento venConvenioMultilateral = Vencimiento.getById(Vencimiento.ID_VENCIMIENTO_CONV_MULT_DREI_ETUR);
		boolean esConvenioMultilateral=contribuyente.getEsConvenioMultilateral();
		for (DeudaAdmin deuda:listDeuda){
			log.debug("periodo"+deuda.getPeriodo()+"/"+deuda.getAnio());
			Date fechaPeriodo = DateUtil.getDate("01/"+StringUtil.completarCerosIzq(deuda.getPeriodo().toString(), 2)+"/"+deuda.getAnio().toString(), DateUtil.ddSMMSYYYY_MASK);
			if (esConvenioMultilateral){
				deuda.setFechaVencimiento(Vencimiento.getFechaVencimiento(fechaPeriodo, venConvenioMultilateral.getId()));
				log.debug("ES CONVENIO MULTILATERAL");
			}else{
				if(deuda.getRecurso().equals(Recurso.getDReI())){
					deuda.setFechaVencimiento(Vencimiento.getFechaVencimiento(fechaPeriodo, venConDirDrei.getId()));
				}
				if(deuda.getRecurso().equals(Recurso.getETur())){
					deuda.setFechaVencimiento(Vencimiento.getFechaVencimiento(fechaPeriodo, venConDirEtur.getId()));
				}
			}
			
			if(deuda.hasError()){
				deuda.passErrorMessages(contribuyente);
				break;
			}
			
			GdeDAOFactory.getDeudaAdminDAO().update(deuda);
			
		}
		return contribuyente;
	}
}