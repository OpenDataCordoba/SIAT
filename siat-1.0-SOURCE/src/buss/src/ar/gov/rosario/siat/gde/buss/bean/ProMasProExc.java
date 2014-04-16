//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Procurador a excluir del envio a judicial 
 * @author tecso
 *
 */
@Entity
@Table(name = "gde_ProMasProExc")
public class ProMasProExc extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "observacion")
	private String observacion;  // varchar(255)
	
	@ManyToOne(optional=false) 
    @JoinColumn(name="idProcesoMasivo") 
	private ProcesoMasivo procesoMasivo;

	@ManyToOne(optional=false) 
    @JoinColumn(name="idprocurador") 
	private Procurador procurador;

	// Constructores
	public ProMasProExc(){
		super();
	}
	
	// Getters y setters
	public ProcesoMasivo getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivo procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Procurador getProcurador() {
		return procurador;
	}
	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}

	// Metodos de clase
	public static ProMasProExc getById(Long id) {
		return (ProMasProExc) GdeDAOFactory.getProMasProExcDAO().getById(id);
	}
	
	public static ProMasProExc getByIdNull(Long id) {
		return (ProMasProExc) GdeDAOFactory.getProMasProExcDAO().getByIdNull(id);
	}
	
	public static List<ProMasProExc> getList() {
		return (ArrayList<ProMasProExc>) GdeDAOFactory.getProMasProExcDAO().getList();
	}
	
	public static List<ProMasProExc> getListActivos() {			
		return (ArrayList<ProMasProExc>) GdeDAOFactory.getProMasProExcDAO().getListActiva();
	}
	
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author 
	 */
	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}
		
		//Validaciones de Negocio
		
		return true;
	}

	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
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
