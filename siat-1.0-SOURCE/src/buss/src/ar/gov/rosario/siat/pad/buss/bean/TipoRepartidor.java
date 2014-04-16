//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Tipo de Repartidor
 * 
 * @author tecso
 */
@Entity
@Table(name = "pad_tipoRepartidor")
public class TipoRepartidor extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final long ID_TGI_ZONA_=1;
	public static final long ID_TGI_FUERA_DE_ZONA_=2;
	
	@Column(name = "desTipoRepartidor")
	private String desTipoRepartidor;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idRecurso") 
	private Recurso recurso;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idCriRep") 
	private CriRep criRep;
	
	// Constructores
	
	public TipoRepartidor(){
		super();
	}

	// Getters y Setters
	
	public CriRep getCriRep() {
		return criRep;
	}
	public void setCriRep(CriRep criRep) {
		this.criRep = criRep;
	}
	public String getDesTipoRepartidor() {
		return desTipoRepartidor;
	}
	public void setDesTipoRepartidor(String desTipoRepartidor) {
		this.desTipoRepartidor = desTipoRepartidor;
	}
	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	// Metodos de clase	
	public static TipoRepartidor getById(Long id) {
		return (TipoRepartidor) PadDAOFactory.getTipoRepartidorDAO().getById(id);
	}
	
	public static TipoRepartidor getByIdNull(Long id) {
		return (TipoRepartidor) PadDAOFactory.getTipoRepartidorDAO().getByIdNull(id);
	}
	
	public static List<TipoRepartidor> getList() {
		return (ArrayList<TipoRepartidor>) PadDAOFactory.getTipoRepartidorDAO().getList();
	}
	
	public static List<TipoRepartidor> getListActivos() {			
		return (ArrayList<TipoRepartidor>) PadDAOFactory.getTipoRepartidorDAO().getListActiva();
	}
	
	public static List<TipoRepartidor> getListActivosByIdRecurso(Long idRecurso) {			
		return (ArrayList<TipoRepartidor>) PadDAOFactory.getTipoRepartidorDAO().getListActivosByIdRecurso(idRecurso);
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
		//Validaciones de Negocio
				
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

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
	
		
		if (hasError()) {
			return false;
		}

		return !hasError();
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

	
}
