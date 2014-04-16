//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.service;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import coop.tecso.demoda.iface.DemodaServiceException;



public class CasCasoFacade {

	private static Logger log = Logger.getLogger(CasCasoFacade.class);
	
	private static final CasCasoFacade INSTANCE = new CasCasoFacade();

	public static CasCasoFacade getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Valida el caso segun el sistema origen mediante WS provistos por MR. 
	 * 
	 * @author Cristian
	 * @param idSistemaOrigen
	 * @param numero
	 * @param anio
	 * @return
	 * @throws DemodaServiceException
	 */
	public boolean validarCaso(int idSistemaOrigen, int numero, int anio) throws DemodaServiceException {
	
		try {
			
			log.debug("validarCaso: enter idSistemaOrigen=" + idSistemaOrigen +
						" numeor=" + numero + " anio=" + anio);
			
			Integer wsCasoActivo = SiatParam.getInteger("WSCasoActivo");
			
			log.debug("validarCaso -> wsCasoActivo: " + wsCasoActivo);
			
			if (wsCasoActivo == 0){
				return true;
			}
			
			// Validacion contra WS Expediente
			if (idSistemaOrigen == 1 ){
				return this.validarExpediente(numero, anio);
				
			} 
			
			//	Validacion contra WS Nota	
			if (idSistemaOrigen == 2 ){
				return this.validarNota(numero, anio);				
			}
			
			return false;
			
		} catch(Exception e){
			e.printStackTrace();
			throw new DemodaServiceException(e);
		}
	}
	
	/**
	 * 
	 * Valida Expedientes contra WS.
	 * 
	 * @author Cristian
	 * @param numero
	 * @param anio
	 * @return
	 * @throws DemodaServiceException
	 */
	private boolean validarExpediente(int numero, int anio) throws DemodaServiceException {
		//XXX siatgpl implementar
		return true;
	}
	
	/**
	 * 
	 * Valida Notas contra WS.
	 * 
	 * @author Cristian
	 * @param numero
	 * @param anio
	 * @return
	 * @throws DemodaServiceException
	 */
	private boolean validarNota(int numero, int anio) throws DemodaServiceException {
		//XXX siatgpl implementar
		return true;
	}
}
