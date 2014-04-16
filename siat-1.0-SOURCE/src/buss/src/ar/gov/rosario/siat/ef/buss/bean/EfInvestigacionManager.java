//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.Date;

import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Manejador del m&oacute;dulo Ef y submodulo Investigacion
 * 
 * @author tecso
 *
 */
public class EfInvestigacionManager {
		
	//private static Logger log = Logger.getLogger(EfInvestigacionManager.class);
	
	private static final EfInvestigacionManager INSTANCE = new EfInvestigacionManager();
	
	/**
	 * Constructor privado
	 */
	private EfInvestigacionManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static EfInvestigacionManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM PlanFiscal
	public PlanFiscal createPlanFiscal(PlanFiscal planFiscal) throws Exception {

		// Validaciones de negocio
		if (!planFiscal.validateCreate()) {
			return planFiscal;
		}

		EfDAOFactory.getPlanFiscalDAO().update(planFiscal);

		return planFiscal;
	}
	
	public PlanFiscal updatePlanFiscal(PlanFiscal planFiscal) throws Exception {
		
		// Validaciones de negocio
		if (!planFiscal.validateUpdate()) {
			return planFiscal;
		}

		EfDAOFactory.getPlanFiscalDAO().update(planFiscal);
		
		return planFiscal;
	}
	
	public PlanFiscal deletePlanFiscal(PlanFiscal planFiscal) throws Exception {
	
		// Validaciones de negocio
		if (!planFiscal.validateDelete()) {
			return planFiscal;
		}
		
		EfDAOFactory.getPlanFiscalDAO().delete(planFiscal);
		
		return planFiscal;
	}
	// <--- ABM PlanFiscal
	
	// ---> ABM OpeInv
	
	/**
	 * Graba el Operativo de Investigacion y graba ademas el historico "Operativo Creado"
	 */
	public OpeInv createOpeInv(OpeInv opeInv) throws Exception {

		// Validaciones de negocio
		if (!opeInv.validateCreate()) {
			return opeInv;
		}

		EfDAOFactory.getOpeInvDAO().update(opeInv);
		
		HisEstOpeInv historico = grabarHistorico(opeInv, opeInv.getEstOpeInv().getId(), EstOpeInv.OBS_CREADO);
		historico.passErrorMessages(opeInv);
		
		return opeInv;
	}
	
	public OpeInv updateOpeInv(OpeInv opeInv) throws Exception {
		
		// Validaciones de negocio
		if (!opeInv.validateUpdate()) {
			return opeInv;
		}

		EfDAOFactory.getOpeInvDAO().update(opeInv);
		
		return opeInv;
	}
	
	public HisEstOpeInv grabarHistorico(OpeInv opeInv, Long idEstOpeInv, String observacion) throws Exception{
		HisEstOpeInv hisEstOpeInv = new HisEstOpeInv();
		hisEstOpeInv.setOpeInv(opeInv);
		hisEstOpeInv.setEstOpeInv(EstOpeInv.getById(idEstOpeInv));
		hisEstOpeInv.setObservacion(observacion);
		hisEstOpeInv = opeInv.createHisEstOpeInv(hisEstOpeInv);
		return hisEstOpeInv;
	}
	
	/**
	 * Eliminar el registro y los historicos asociados, de OpeInv
	 * @param opeInv
	 * @return
	 * @throws Exception
	 */
	public OpeInv deleteOpeInv(OpeInv opeInv) throws Exception {
	
		// Validaciones de negocio
		if (!opeInv.validateDelete()) {
			return opeInv;
		}
		
		opeInv.deleteHistoricos();
		
		EfDAOFactory.getOpeInvDAO().delete(opeInv);
		
		return opeInv;
	}
		
	// <--- ABM OpeInv	
	
	// ---> emitir Orden de control
	public OrdenControl create(OrdenControl ordenControl, String obsHistorico) throws Exception {

		// Validaciones de negocio
		if (!ordenControl.validateCreate()) {
			return ordenControl;
		}

		EfDAOFactory.getOrdenControlDAO().update(ordenControl);

		if(!StringUtil.isNullOrEmpty(obsHistorico)){
   			// crea el historico
   			HisEstOrdCon hisEstOrdCon = new HisEstOrdCon();
   			hisEstOrdCon.setEstadoOrden(ordenControl.getEstadoOrden());
   			hisEstOrdCon.setOrdenControl(ordenControl);
   			hisEstOrdCon.setObservacion(obsHistorico);
   			hisEstOrdCon.setFecha(new Date());
   			ordenControl.createHisEstOrdCon(hisEstOrdCon);
		}

		return ordenControl;
	}
	
	/**
	 * graba la orden de control y si la obsHistorico!=null también el historico, con el estado de la ordenControl 
	 * @param ordenControl
	 * @param obsHistorico
	 * @return
	 * @throws Exception
	 */
	public OrdenControl updateOrdenControl(OrdenControl ordenControl, String obsHistorico) throws Exception {
		
		// Validaciones de negocio
		if (!ordenControl.validateUpdate()) {
			return ordenControl;
		}

		EfDAOFactory.getOrdenControlDAO().update(ordenControl);
		
		if(!StringUtil.isNullOrEmpty(obsHistorico)){
   			// crea el historico
   			HisEstOrdCon hisEstOrdCon = new HisEstOrdCon();
   			hisEstOrdCon.setEstadoOrden(ordenControl.getEstadoOrden());
   			hisEstOrdCon.setOrdenControl(ordenControl);
   			hisEstOrdCon.setObservacion(obsHistorico);
   			hisEstOrdCon.setFecha(new Date());
   			ordenControl.createHisEstOrdCon(hisEstOrdCon);
		}
		
		return ordenControl;
	}
	
	public OrdenControl deleteOrdenControl(OrdenControl ordenControl) throws Exception {
	
		// Validaciones de negocio
		if (!ordenControl.validateDelete()) {
			return ordenControl;
		}
		
		EfDAOFactory.getOrdenControlDAO().delete(ordenControl);
		
		return ordenControl;
	}
	// <--- emitir Orden de control
	

}
