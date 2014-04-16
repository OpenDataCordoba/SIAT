//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean correspondiente a PeriodoDeuda
 * Representa la periodicidad de la deuda a emitir para un determinado Recurso.
 * 
 * @author tecso
 */

@Entity
@Table(name = "def_periodoDeuda")
public class PeriodoDeuda extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "periodoDeuda";
	
	public static final Long ESPORADICO = 1L;
	public static final Long MENSUAL = 2L;
	public static final Long BIMESTRAL = 3L;
	public static final Long TRIMESTRAL = 4L;
	public static final Long ANUAL = 5L;

	@Column(name = "desPeriodoDeuda")
	private String desPeriodoDeuda;

	@Column(name = "periodos")
	private Integer periodos;

	
	// Constructores
	public PeriodoDeuda(){
		super();
	}
	
	// Getters y Setters
	public String getDesPeriodoDeuda(){
		return desPeriodoDeuda;
	}
	
	public void setDesPeriodoDeuda(String desPeriodoDeuda){
		this.desPeriodoDeuda = desPeriodoDeuda;
	}
	
	public Integer getPeriodos() {
		return periodos;
	}

	public void setPeriodos(Integer periodos) {
		this.periodos = periodos;
	}

	// Metodos de Clase
	public static PeriodoDeuda getById(Long id) {
		return (PeriodoDeuda) DefDAOFactory.getPeriodoDeudaDAO().getById(id);  
	}
	
	public static PeriodoDeuda getByIdNull(Long id) {
		return (PeriodoDeuda) DefDAOFactory.getPeriodoDeudaDAO().getByIdNull(id);
	}
	
	public static List<PeriodoDeuda> getList() {
		return (List<PeriodoDeuda>) DefDAOFactory.getPeriodoDeudaDAO().getList();
	}
	
	public static List<PeriodoDeuda> getListActivos() {			
		return (List<PeriodoDeuda>) DefDAOFactory.getPeriodoDeudaDAO().getListActiva();
	}

	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		
		if (hasError()) {
			return false;
		}

				
		return !hasError();
	}
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
	
		if (hasError()) {
			return false;
		}

			
		return !hasError();
	}

	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de Requeridos
		
		if (StringUtil.isNullOrEmpty(getDesPeriodoDeuda())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.PERIODODEUDA_DESPERIODODEUDA);
		}
		
		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
		
		// Otras Validaciones
		
		// Valida que la Fecha Desde no sea mayor que la fecha Hasta
		
		
		return true;
	}

	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;
	}

	// Metodos de negocio
	
}
