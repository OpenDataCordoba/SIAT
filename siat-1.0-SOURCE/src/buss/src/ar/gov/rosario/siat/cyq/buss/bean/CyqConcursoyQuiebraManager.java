//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.bean;

import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;

/**
 * Manejador del m&oacute;dulo Cyq y submodulo 
 * 
 * @author tecso
 *
 */
public class CyqConcursoyQuiebraManager {
			
	private static final CyqConcursoyQuiebraManager INSTANCE = new CyqConcursoyQuiebraManager();
	
	/**
	 * Constructor privado
	 */
	private CyqConcursoyQuiebraManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static CyqConcursoyQuiebraManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM Procedimiento
	public Procedimiento createProcedimiento(Procedimiento procedimiento) throws Exception {

		// Validaciones de negocio
		if (!procedimiento.validateCreate()) {
			return procedimiento;
		}

		CyqDAOFactory.getProcedimientoDAO().update(procedimiento);

		return procedimiento;
	}
	
	public Procedimiento updateProcedimiento(Procedimiento procedimiento) throws Exception {
		
		// Validaciones de negocio
		if (!procedimiento.validateUpdate()) {
			return procedimiento;
		}

		CyqDAOFactory.getProcedimientoDAO().update(procedimiento);
		
		return procedimiento;
	}
	
	public Procedimiento deleteProcedimiento(Procedimiento procedimiento) throws Exception {
	
		// Validaciones de negocio
		if (!procedimiento.validateDelete()) {
			return procedimiento;
		}
		
		CyqDAOFactory.getProcedimientoDAO().delete(procedimiento);
		
		return procedimiento;
	}
	
	public Procedimiento bajaProcedimiento(Procedimiento procedimiento) throws Exception {
		
		// Validaciones de negocio
		if (!procedimiento.validateBaja()) {
			return procedimiento;
		}

		CyqDAOFactory.getProcedimientoDAO().update(procedimiento);
		
		return procedimiento;
	}
	
	
	public Procedimiento informeProcedimiento(Procedimiento procedimiento) throws Exception {
		
		// Validaciones de negocio
		if (!procedimiento.validateInforme()) {
			return procedimiento;
		}

		CyqDAOFactory.getProcedimientoDAO().update(procedimiento);
		
		return procedimiento;
	}	
	// <--- ABM Procedimiento
	
	
	public ProCueNoDeu updateProCueNoDeu(ProCueNoDeu proCueNoDeu) throws Exception {
		
		// Validaciones de negocio
		if (!proCueNoDeu.validateUpdate()) {
			return proCueNoDeu;
		}

		CyqDAOFactory.getProCueNoDeuDAO().update(proCueNoDeu);
		
		return proCueNoDeu;		
	}
	
	// ---> ABM ProCueNoDeu
	public ProCueNoDeu deleteProCueNoDeu(ProCueNoDeu proCueNoDeu) throws Exception {
		
		// Validaciones de negocio
		if (!proCueNoDeu.validateDelete()) {
			return proCueNoDeu;
		}
		
		CyqDAOFactory.getProCueNoDeuDAO().delete(proCueNoDeu);
		
		return proCueNoDeu;
	}
	// <--- ABM ProCueNoDeu

}
