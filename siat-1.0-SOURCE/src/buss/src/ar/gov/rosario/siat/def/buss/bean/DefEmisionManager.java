//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;

/**
 * Manager del Modulo Definicion y el submodulo
 * Emision
 * 
 * @author tecso
 *
 */
public class DefEmisionManager {
	//
	private static final DefEmisionManager INSTANCE = new DefEmisionManager();
	
	/**
	 * Constructor privado
	 */
	private DefEmisionManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static DefEmisionManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM EmiMat
	public EmiMat createEmiMat(EmiMat emiMat) throws Exception {

		// Validaciones de negocio
		if (!emiMat.validateCreate()) {
			return emiMat;
		}

		DefDAOFactory.getEmiMatDAO().update(emiMat);

		return emiMat;
	}
	
	public EmiMat updateEmiMat(EmiMat emiMat) throws Exception {
		
		// Validaciones de negocio
		if (!emiMat.validateUpdate()) {
			return emiMat;
		}

		DefDAOFactory.getEmiMatDAO().update(emiMat);
		
		return emiMat;
	}
	
	public EmiMat deleteEmiMat(EmiMat emiMat) throws Exception {
	
		// Validaciones de negocio
		if (!emiMat.validateDelete()) {
			return emiMat;
		}
		
		DefDAOFactory.getEmiMatDAO().delete(emiMat);
		
		return emiMat;
	}
	// <--- ABM EmiMat

	// ---> ABM CodEmi
	public CodEmi createCodEmi(CodEmi codEmi) throws Exception {

		// Validaciones de negocio
		if (!codEmi.validateCreate()) {
			return codEmi;
		}

		DefDAOFactory.getEmiMatDAO().update(codEmi);
		
        // Creamos una entrada en el historico de cambios
        HistCodEmi histCodEmi = new HistCodEmi("Creado");
        codEmi.createHistCodEmi(histCodEmi);

        return codEmi;
	}
	
	public CodEmi updateCodEmi(CodEmi codEmi) throws Exception {
		
		// Validaciones de negocio
		if (!codEmi.validateUpdate()) {
			return codEmi;
		}

		DefDAOFactory.getEmiMatDAO().update(codEmi);
		
		return codEmi;
	}
	
	public CodEmi deleteCodEmi(CodEmi codEmi) throws Exception {
	
		// Validaciones de negocio
		if (!codEmi.validateDelete()) {
			return codEmi;
		}
		
		// Eliminamos el historial de cambios
		for (HistCodEmi hist: codEmi.getListHistCodEmi()) {
			codEmi.deleteHistCodEmi(hist);
		}
		
		DefDAOFactory.getEmiMatDAO().delete(codEmi);
		
		return codEmi;
	}
	// <--- ABM CodEmi

}
