//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a TipoDistrib
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_tipoDistrib")
public class TipoDistrib extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final Long ID_PORCENTAJE = 1L;
	public static final Long ID_MONTO_FIJO = 2L;

	@Column(name = "desTipoDistrib")
	private String desTipoDistrib;
	
	// Constructores
	public TipoDistrib(){
		super();
		// Seteo de valores default	
	}
	
	public TipoDistrib(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipoDistrib getById(Long id) {
		return (TipoDistrib) BalDAOFactory.getTipoDistribDAO().getById(id);
	}
	
	public static TipoDistrib getByIdNull(Long id) {
		return (TipoDistrib) BalDAOFactory.getTipoDistribDAO().getByIdNull(id);
	}
	
	public static List<TipoDistrib> getList() {
		return (ArrayList<TipoDistrib>) BalDAOFactory.getTipoDistribDAO().getList();
	}
	
	public static List<TipoDistrib> getListActivos() {			
		return (ArrayList<TipoDistrib>) BalDAOFactory.getTipoDistribDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesTipoDistrib() {
		return desTipoDistrib;
	}

	public void setDesTipoDistrib(String desTipoDistrib) {
		this.desTipoDistrib = desTipoDistrib;
	}
	
	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		//	Validaciones        
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
}
