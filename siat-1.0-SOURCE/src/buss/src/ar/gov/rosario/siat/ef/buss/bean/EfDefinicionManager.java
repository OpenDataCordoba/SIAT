//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;

/**
 * Manejador del m&oacute;dulo Ef y submodulo Definicion
 * 
 * @author tecso
 *
 */
public class EfDefinicionManager {
		
	private static Logger log = Logger.getLogger(EfDefinicionManager.class);
	
	private static final EfDefinicionManager INSTANCE = new EfDefinicionManager();
	
	/**
	 * Constructor privado
	 */
	private EfDefinicionManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static EfDefinicionManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM Inspector
	public Inspector createInspector(Inspector inspector) throws Exception {

		// Validaciones de negocio
		if (!inspector.validateCreate()) {
			return inspector;
		}

		EfDAOFactory.getInspectorDAO().update(inspector);

		return inspector;
	}
	
	public Inspector updateInspector(Inspector inspector) throws Exception {
		
		// Validaciones de negocio
		if (!inspector.validateUpdate()) {
			return inspector;
		}

		EfDAOFactory.getInspectorDAO().update(inspector);
		
		return inspector;
	}
	
	public Inspector deleteInspector(Inspector inspector) throws Exception {
	
		// Validaciones de negocio
		if (!inspector.validateDelete()) {
			return inspector;
		}
		
		EfDAOFactory.getInspectorDAO().delete(inspector);
		
		return inspector;
	}
	// <--- ABM Inspector
	
	// ---> ABM InsSup
	public InsSup createInsSup(InsSup insSup) throws Exception {

		// Validaciones de negocio
		if (!insSup.validateCreate()) {
			return insSup;
		}

		EfDAOFactory.getInsSupDAO().update(insSup);

		return insSup;
	}
	
	public InsSup updateInsSup(InsSup insSup) throws Exception {
		
		// Validaciones de negocio
		if (!insSup.validateUpdate()) {
			return insSup;
		}
       
		EfDAOFactory.getInsSupDAO().update(insSup);
		
		return insSup;
	}
	
	public InsSup deleteInsSup(InsSup insSup) throws Exception {
	
		// Validaciones de negocio
		if (!insSup.validateDelete()) {
			return insSup;
		}
		
		EfDAOFactory.getInsSupDAO().delete(insSup);
		
		return insSup;
	}
	// <--- ABM InsSup
		
	// ---> ABM Investigador
	public Investigador createInvestigador(Investigador investigador) throws Exception {

		// Validaciones de negocio
		if (!investigador.validateCreate()) {
			return investigador;
		}

		EfDAOFactory.getInvestigadorDAO().update(investigador);

		return investigador;
	}
	
	public Investigador updateInvestigador(Investigador investigador) throws Exception {
		
		// Validaciones de negocio
		if (!investigador.validateUpdate()) {
			return investigador;
		}

		EfDAOFactory.getInvestigadorDAO().update(investigador);
		
		return investigador;
	}
	
	public Investigador deleteInvestigador(Investigador investigador) throws Exception {
	
		// Validaciones de negocio
		if (!investigador.validateDelete()) {
			return investigador;
		}
		
		EfDAOFactory.getInvestigadorDAO().delete(investigador);
		
		return investigador;
	}
	// <--- ABM Investigador
	
			
	// ---> ABM Supervisor
	public Supervisor createSupervisor(Supervisor supervisor) throws Exception {

		// Validaciones de negocio
		if (!supervisor.validateCreate()) {
			return supervisor;
		}

		EfDAOFactory.getSupervisorDAO().update(supervisor);

		return supervisor;
	}
	
	public Supervisor updateSupervisor(Supervisor supervisor) throws Exception {
		
		// Validaciones de negocio
		if (!supervisor.validateUpdate()) {
			return supervisor;
		}

		EfDAOFactory.getSupervisorDAO().update(supervisor);
		
		return supervisor;
	}
	
	public Supervisor deleteSupervisor(Supervisor supervisor) throws Exception {
	
		// Validaciones de negocio
		if (!supervisor.validateDelete()) {
			return supervisor;
		}
		
		EfDAOFactory.getSupervisorDAO().delete(supervisor);
		
		return supervisor;
	}
	// <--- ABM Supervisor
	
	// ---> ABM FuenteInfo
	public FuenteInfo createFuenteInfo(FuenteInfo fuenteInfo) throws Exception {

		// Validaciones de negocio
		if (!fuenteInfo.validateCreate()) {
			return fuenteInfo;
		}

		EfDAOFactory.getFuenteInfoDAO().update(fuenteInfo);

		return fuenteInfo;
	}
	
	public FuenteInfo updateFuenteInfo(FuenteInfo fuenteInfo) throws Exception {
		
		// Validaciones de negocio
		if (!fuenteInfo.validateUpdate()) {
			return fuenteInfo;
		}

		EfDAOFactory.getFuenteInfoDAO().update(fuenteInfo);
		
		return fuenteInfo;
	}
	
	public FuenteInfo deleteFuenteInfo(FuenteInfo fuenteInfo) throws Exception {
	
		// Validaciones de negocio
		if (!fuenteInfo.validateDelete()) {
			return fuenteInfo;
		}
		
		EfDAOFactory.getFuenteInfoDAO().delete(fuenteInfo);
		
		return fuenteInfo;
	}
	// <--- ABM FuenteInfo
	
	// ---> ABM DocSop
	public DocSop createDocSop(DocSop docSup) throws Exception {

		// Validaciones de negocio
		if (!docSup.validateCreate()) {
			
			return docSup;
		}

		EfDAOFactory.getDocSopDAO().update(docSup);

		return docSup;
	}
	
	public DocSop updateDocSop(DocSop doSup) throws Exception {
		
		// Validaciones de negocio
		if (!doSup.validateUpdate()) {
			return doSup;
		}

		EfDAOFactory.getDocSopDAO().update(doSup);
		
		return doSup;
	}
	
	public DocSop deleteDocSop(DocSop docSup) throws Exception {
	
		// Validaciones de negocio
		if (!docSup.validateDelete()) {
			return docSup;
		}
		
		EfDAOFactory.getDocSopDAO().delete(docSup);
		
		return docSup;
	}
	// <--- ABM DocSop
		

}
