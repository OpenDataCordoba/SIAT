//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a TipDecJur
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_tipDecJurRec")
public class TipDecJurRec extends BaseBO {
	
	public static final Long ID_DJ_ORIGINAL=1L;
	public static final Long ID_DJ_RECTIFICATIVA=2L;
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idTipDecJur")
	private TipDecJur tipDecJur;
	
	@Column(name="fechaDesde")
	private Date fechaDesde;
	
	@Column(name="fechaHasta")
	private Date fechaHasta;

	//<#Propiedades#>
	
	// Constructores
	public TipDecJurRec(){
		super();
		// Seteo de valores default	
		// propiedad_ejemplo = valorDefault;
	}
	
	
	// Metodos de Clase
	public static TipDecJurRec getById(Long id) {
		return (TipDecJurRec) GdeDAOFactory.getTipDecJurRecDAO().getById(id);
	}
	
	public static TipDecJurRec getByIdNull(Long id) {
		return (TipDecJurRec) GdeDAOFactory.getTipDecJurRecDAO().getByIdNull(id);
	}
	
	public static List<TipDecJurRec> getList() {
		return (List<TipDecJurRec>) GdeDAOFactory.getTipDecJurRecDAO().getList();
	}
	
	public static List<TipDecJurRec> getListActivos() {			
		return (List<TipDecJurRec>) GdeDAOFactory.getTipDecJurRecDAO().getListActiva();
	}
	
	public static List<TipDecJurRec> getListVigenteByRecurso (Date fechaVigencia, Recurso recurso){
		return GdeDAOFactory.getTipDecJurRecDAO().getListVigenteByRecurso(fechaVigencia, recurso);
	}
	
	public static TipDecJurRec getVigenteByRecursoYTipDecJur(Date fechaVigencia, Recurso recurso, TipDecJur tipDecJur){
		return GdeDAOFactory.getTipDecJurRecDAO().getVigenteByRecursoYTipDecJur(fechaVigencia, recurso, tipDecJur);
	}
	
	// Getters y setters
	public Recurso getRecurso() {
		return recurso;
	}


	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}


	public TipDecJur getTipDecJur() {
		return tipDecJur;
	}


	public void setTipDecJur(TipDecJur tipDecJur) {
		this.tipDecJur = tipDecJur;
	}


	public Date getFechaDesde() {
		return fechaDesde;
	}


	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}


	public Date getFechaHasta() {
		return fechaHasta;
	}


	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
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
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
	
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipoDeudaPlan. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getTipDecJurDAO().update(this);
	}

	/**
	 * Desactiva el TipoDeudaPlan. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getTipDecJurDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipoDeudaPlan
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipoDeudaPlan
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//<#MetodosBeanDetalle#>
}
