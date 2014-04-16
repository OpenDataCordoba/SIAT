//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.buss.bean;

import ar.gov.rosario.siat.frm.buss.dao.FrmDAOFactory;

/**
 * Manejador del m&oacute;dulo Frm y submodulo Formulario
 * 
 * @author tecso
 *
 */
public class FrmFormularioManager {
		
	//private static Logger log = Logger.getLogger(FrmFormularioManager.class);
	
	private static final FrmFormularioManager INSTANCE = new FrmFormularioManager();
	
	/**
	 * Constructor privado
	 */
	private FrmFormularioManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static FrmFormularioManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM Formulario
	public Formulario createFormulario(Formulario formulario) throws Exception {

		// Validaciones de negocio
		if (!formulario.validateCreate()) {
			return formulario;
		}

		FrmDAOFactory.getFormularioDAO().update(formulario);

		return formulario;
	}
	
	public Formulario updateFormulario(Formulario formulario) throws Exception {
		
		// Validaciones de negocio
		if (!formulario.validateUpdate()) {
			return formulario;
		}

		FrmDAOFactory.getFormularioDAO().update(formulario);
		
		return formulario;
	}
	
	public Formulario deleteFormulario(Formulario formulario) throws Exception {
	
		// Validaciones de negocio
		if (!formulario.validateDelete()) {
			return formulario;
		}
		
		FrmDAOFactory.getFormularioDAO().delete(formulario);
		
		return formulario;
	}
	// <--- ABM Formulario
	
	
		

}
