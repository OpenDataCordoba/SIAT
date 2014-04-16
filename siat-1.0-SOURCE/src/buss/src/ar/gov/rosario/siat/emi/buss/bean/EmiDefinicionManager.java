//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.bean;

import ar.gov.rosario.siat.def.buss.bean.ColEmiMat;
import ar.gov.rosario.siat.def.buss.bean.EmiMat;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;

/**
 * Manejador del m&oacute;dulo Emi y submodulo Definicion
 * 
 * @author tecso
 *
 */
public class EmiDefinicionManager {
		
	//private static Logger log = Logger.getLogger(EmiDefinicionManager.class);
	
	private static final EmiDefinicionManager INSTANCE = new EmiDefinicionManager();
	
	/**
	 * Constructor privado
	 */
	private EmiDefinicionManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static EmiDefinicionManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM ValEmiMat
	public ValEmiMat createValEmiMat(ValEmiMat valEmiMat) throws Exception {

		// Validaciones de negocio
		if (!valEmiMat.validateCreate()) {
			return valEmiMat;
		}
		
		String valores = "";
		EmiMat emiMat = valEmiMat.getEmiMat();
		for (ColEmiMat c: emiMat.getListColEmiMat()) {
			valores += (c.getCodColumna() + "|N,"); 
		}
		valores += "; ";
		valEmiMat.setValores(valores);
		
		EmiDAOFactory.getValEmiMatDAO().update(valEmiMat);

		return valEmiMat;
	}
	
	public ValEmiMat updateValEmiMat(ValEmiMat valEmiMat) throws Exception {
		
		// Validaciones de negocio
		if (!valEmiMat.validateUpdate()) {
			return valEmiMat;
		}

		EmiDAOFactory.getValEmiMatDAO().update(valEmiMat);
		
		return valEmiMat;
	}
	
	public ValEmiMat deleteValEmiMat(ValEmiMat valEmiMat) throws Exception {
	
		// Validaciones de negocio
		if (!valEmiMat.validateDelete()) {
			return valEmiMat;
		}
		
		EmiDAOFactory.getValEmiMatDAO().delete(valEmiMat);
		
		return valEmiMat;
	}
	// <--- ABM ValEmiMat
	
}
