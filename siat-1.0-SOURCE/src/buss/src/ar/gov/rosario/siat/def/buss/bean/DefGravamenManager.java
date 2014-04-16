//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;

/**
 * Manejador del subm&oacute;dulo Gravamenes del subm&oacute;dulo Definici&oacute;n
 * 
 * @author tecso
 *
 */
public class DefGravamenManager {
		
	//private static Logger log = Logger.getLogger(DefGravamenManager.class);
	
	public static final DefGravamenManager INSTANCE = new DefGravamenManager();
	
	/**
	 * Constructor privado
	 */
	private DefGravamenManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static DefGravamenManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM Categoria
	public Categoria createCategoria(Categoria categoria) throws Exception {

		// Validaciones de VO y negocio
		if (!categoria.validateCreate()) {
			return categoria;
		}

		DefDAOFactory.getCategoriaDAO().update(categoria);
		
		return categoria;
	}
	
	public Categoria updateCategoria(Categoria categoria) throws Exception {
		
		// Validaciones de VO y negocio
		if (!categoria.validateUpdate()) {
			return categoria;
		}
		
		DefDAOFactory.getCategoriaDAO().update(categoria);

	    return categoria;
	}
	
	public Categoria deleteCategoria(Categoria categoria) throws Exception {
		
		// Validaciones de VO y negocio
		if (!categoria.validateDelete()) {
			return categoria;
		}
		
		DefDAOFactory.getCategoriaDAO().delete(categoria);
		
	    return categoria;
	}	
	// <--- ABM Categoria	
	
	// ---> ABM Feriado
	public Feriado createFeriado(Feriado feriado) throws Exception {
		// Validaciones de negocio
		if (!feriado.validateCreate()) {
			return feriado;
		}

		DefDAOFactory.getFeriadoDAO().update(feriado);

		return feriado;
	}	
	// <--- ABM Feriado
	
    // ---> ABM Vencimiento
	public Vencimiento createVencimiento(Vencimiento vencimiento) throws Exception {

		// Validaciones de negocio
		if (!vencimiento.validateCreate()) {
			return vencimiento;
		}

		DefDAOFactory.getVencimientoDAO().update(vencimiento);

		return vencimiento;
	}
	
	public Vencimiento updateVencimiento(Vencimiento vencimiento) throws Exception {
		
		// Validaciones de negocio
		if (!vencimiento.validateUpdate()) {
			return vencimiento;
		}

		DefDAOFactory.getVencimientoDAO().update(vencimiento);
		
		return vencimiento;
	}
	
	public Vencimiento deleteVencimiento(Vencimiento vencimiento) throws Exception {
	
		// Validaciones de negocio
		if (!vencimiento.validateDelete()) {
			return vencimiento;
		}
		
		DefDAOFactory.getVencimientoDAO().delete(vencimiento);
		
		return vencimiento;
	}
	// <--- ABM Vencimiento
	
	// ---> ABM Recurso	
	public Recurso createRecurso(Recurso recurso) throws Exception {

		// Validaciones de negocio
		if (!recurso.validateCreate()) {
			return recurso;
		}

		DefDAOFactory.getRecursoDAO().update(recurso);

		return recurso;
	}
	
	public Recurso updateRecurso(Recurso recurso) throws Exception {
		
		// Validaciones de negocio
		if (!recurso.validateUpdate()) {
			return recurso;
		}
		
		DefDAOFactory.getRecursoDAO().update(recurso);
		
	    return recurso;
	}
	
	public Recurso deleteRecurso(Recurso recurso) throws Exception {

		// Validaciones de negocio
		if (!recurso.validateDelete()) {
			return recurso;
		}
		
		DefDAOFactory.getRecursoDAO().delete(recurso);
		
		return recurso;
	}
	// <--- ABM Recurso	
	
	// ---> ABM Calendario
	public Calendario createCalendario(Calendario calendario) throws Exception {

		// Validaciones de negocio
		if (!calendario.validateCreate()) {
			return calendario;
		}

		DefDAOFactory.getCalendarioDAO().update(calendario);

		return calendario;
	}
	
	public Calendario updateCalendario(Calendario calendario) throws Exception {
		
		// Validaciones de negocio
		if (!calendario.validateUpdate()) {
			return calendario;
		}

		DefDAOFactory.getCalendarioDAO().update(calendario);
		
		return calendario;
	}
	
	public Calendario deleteCalendario(Calendario calendario) throws Exception {
	
		// Validaciones de negocio
		if (!calendario.validateDelete()) {
			return calendario;
		}
		
		DefDAOFactory.getCalendarioDAO().delete(calendario);
		
		return calendario;
	}
	// <--- ABM Calendario
}