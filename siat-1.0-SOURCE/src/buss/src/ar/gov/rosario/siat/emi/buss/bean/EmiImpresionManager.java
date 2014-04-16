//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.bean;

import java.util.Date;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.pro.iface.util.ProError;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Manejador del modulo Emi y submodulo Impresion
 * 
 * @author tecso
 *
 */
public class EmiImpresionManager {
		
	private static Logger log = Logger.getLogger(EmiImpresionManager.class);
	
	private static final EmiImpresionManager INSTANCE = new EmiImpresionManager();
	
	/**
	 * Constructor privado
	 */
	private EmiImpresionManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static EmiImpresionManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM ImpMasDeu
	public ImpMasDeu createImpMasDeu(ImpMasDeu impMasDeu) throws Exception {

		// Validaciones de negocio
		if (!impMasDeu.validateCreate()) {
			return impMasDeu;
		}
		
		// Creamos la corrida
		AdpRun run = AdpRun.newRun(Proceso.PROCESO_IMPRESION_MASIVA_DEUDA, 
						"Corrida del Proceso de Impresion Masiva de Deuda " + 
						DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK));

		run.create();
		Corrida corrida = Corrida.getByIdNull(run.getId());
        
		if (corrida == null){
        	log.error("no se pudo obtener la corrida creada");
        	impMasDeu.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.CORRIDA_LABEL);
			return impMasDeu;
		}
        
        // Seteamos la corrida
        impMasDeu.setCorrida(corrida);
        
        EmiDAOFactory.getImpMasDeuDAO().update(impMasDeu);
        
		// carga de parametros para adp
		run.putParameter(ImpMasDeu.ID_IMPMASDEU, impMasDeu.getId().toString());

		return impMasDeu;
	}
	
	public ImpMasDeu updateImpMasDeu(ImpMasDeu impMasDeu) throws Exception {
		
		// Validaciones de negocio
		if (!impMasDeu.validateUpdate()) {
			return impMasDeu;
		}
		
		// recupero la corrida
		AdpRun run = AdpRun.getRun(impMasDeu.getCorrida().getId());

        if (run == null){
        	log.error("no se pudo obtener la corrida creada");
        	impMasDeu.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.CORRIDA_LABEL);
			return impMasDeu;
		}

        // carga de parametros para adp
		run.putParameter(ImpMasDeu.ID_IMPMASDEU, impMasDeu.getId().toString());

		EmiDAOFactory.getImpMasDeuDAO().update(impMasDeu);
		
		return impMasDeu;
	}
	
	public ImpMasDeu deleteImpMasDeu(ImpMasDeu impMasDeu) throws Exception {
	
		// Validaciones de negocio
		if (!impMasDeu.validateDelete()) {
			return impMasDeu;
		}

		EmiDAOFactory.getImpMasDeuDAO().delete(impMasDeu);
		
		return impMasDeu;
	}
	// <--- ABM ImpMasDeu
	
}
