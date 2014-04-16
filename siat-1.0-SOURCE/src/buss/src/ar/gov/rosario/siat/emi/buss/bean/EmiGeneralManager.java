//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.bean;

import java.util.Date;

import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Manejador del modulo Emision y 
 * submodulo General
 * 
 * @author tecso
 */
public class EmiGeneralManager {
		
	private static final EmiGeneralManager INSTANCE = new EmiGeneralManager();
	
	/**
	 * Constructor privado
	 */
	private EmiGeneralManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static EmiGeneralManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM ResLiqDeu
	public ResLiqDeu createResLiqDeu(ResLiqDeu resLiqDeu) throws Exception {

		// Validaciones de negocio
		if (!resLiqDeu.validateCreate()) {
			return resLiqDeu;
		}
		
		// Creamos la corrida dependiendo si es o no para 
		// Emision Alfax
		AdpRun run = null;
		if (resLiqDeu.getEsAlfax() == SiNo.NO.getBussId()) {
			run = AdpRun.newRun(Proceso.PROCESO_RES_LIQ_DEU, 
				"Corrida del Proceso de Resumen de Deuda. Creacion:  " + 
				DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK));
		} else {
			run = AdpRun.newRun(Proceso.PROCESO_RES_LIQ_DEU_ALFAX, 
				"Corrida del Proceso de Resumen de Deuda para Alfax. Creacion:  " + 
				DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK));
		}

		run.create();
		
		Corrida corrida = Corrida.getByIdNull(run.getId());
        if (corrida == null){
        	throw new Exception("No se pudo obtener la corrida creada");
        }
        // Seteamos la corrida
        resLiqDeu.setCorrida(corrida);
        
		EmiDAOFactory.getResLiqDeuDAO().update(resLiqDeu);
        
		// Carga de parametros para adp
		run.putParameter(ResLiqDeu.ADP_PARAM_ID, resLiqDeu.getId().toString());

		return resLiqDeu;
	}
	
	public ResLiqDeu updateResLiqDeu(ResLiqDeu resLiqDeu) throws Exception {
		
		// Validaciones de negocio
		if (!resLiqDeu.validateUpdate()) {
			return resLiqDeu;
		}
		
		EmiDAOFactory.getResLiqDeuDAO().update(resLiqDeu);
		
		return resLiqDeu;
	}
	
	public ResLiqDeu deleteResLiqDeu(ResLiqDeu resLiqDeu) throws Exception {
	
		// Validaciones de negocio
		if (!resLiqDeu.validateDelete()) {
			return resLiqDeu;
		}
		
		// Si no es un resumen de deuda para ALFAX
		if (resLiqDeu.getEsAlfax() == SiNo.NO.getBussId()) {
			// Eliminamos las leyendas generadas 
			Recurso recurso = resLiqDeu.getRecurso();
			Integer anio = resLiqDeu.getAnio();
			Integer periodoDesde = resLiqDeu.getPeriodoDesde();
			Integer periodoHasta = resLiqDeu.getPeriodoHasta();
			EmiDAOFactory.getEmiInfCueDAO().deleteBy(recurso.getId(), anio, periodoDesde, periodoHasta);
		} else {
			// TODO: Borrar archivos generados
		}

		EmiDAOFactory.getResLiqDeuDAO().delete(resLiqDeu);
		
		return resLiqDeu;
	}
	// <--- ABM ResLiqDeu
	
	// ---> ABM EmInfCue
	public EmiInfCue createEmiInfCue(EmiInfCue emiInfCue) throws Exception {

		EmiDAOFactory.getEmiInfCueDAO().update(emiInfCue);

		return emiInfCue;
	}
	
	public EmiInfCue updateEmiInfCue(EmiInfCue emiInfCue) throws Exception {
		

		EmiDAOFactory.getEmiInfCueDAO().update(emiInfCue);
		
		return emiInfCue;
	}
	
	public EmiInfCue deleteEmiInfCue(EmiInfCue emiInfCue) throws Exception {
	
		
		EmiDAOFactory.getEmiInfCueDAO().delete(emiInfCue);
		
		return emiInfCue;
	}
	// <--- ABM EmiInfCue

	// ---> ABM ProPasDeb
	public ProPasDeb createProPasDeb(ProPasDeb proPasDeb) throws Exception {

		// Validaciones de negocio
		if (!proPasDeb.validateCreate()) {
			return proPasDeb;
		}
		
		// Creamos la corrida dependiendo si es o no para Alfax
		AdpRun run = null;
		run = AdpRun.newRun(Proceso.PROCESO_PROPASDEB, 
				"Corrida del Proceso de Generacion de archivos PAS y Debito. Creacion:  " + 
				DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK));
		run.create();
		
		Corrida corrida = Corrida.getByIdNull(run.getId());
        if (corrida == null){
        	throw new Exception("No se pudo obtener la corrida creada");
        }

        // Seteamos la corrida
        proPasDeb.setCorrida(corrida);
        EmiDAOFactory.getProPasDebDAO().update(proPasDeb);
        
		// Carga de parametros para adp
		run.putParameter(ProPasDeb.ADP_PARAM_ID, proPasDeb.getId().toString());

		return proPasDeb;
	}
	
	public ProPasDeb updateProPasDeb(ProPasDeb proPasDeb) throws Exception {
		
		// Validaciones de negocio
		if (!proPasDeb.validateUpdate()) {
			return proPasDeb;
		}
		
		// Recupero la corrida
		AdpRun run = AdpRun.getRun(proPasDeb.getCorrida().getId());

		// Update de parametros de adp
		run.putParameter(ProPasDeb.ADP_PARAM_ID, proPasDeb.getId().toString());

		EmiDAOFactory.getProPasDebDAO().update(proPasDeb);
		
		return proPasDeb;
	}
	
	public ProPasDeb deleteProPasDeb(ProPasDeb proPasDeb) throws Exception {
		// Validaciones de negocio
		if (!proPasDeb.validateDelete()) {
			return proPasDeb;
		}

		EmiDAOFactory.getProPasDebDAO().delete(proPasDeb);
		
		return proPasDeb;
	}
	// <--- ABM ProPasDeb
}
