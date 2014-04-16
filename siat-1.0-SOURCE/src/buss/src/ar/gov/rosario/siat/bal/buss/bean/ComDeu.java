//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Representa la lista de deuda que se esta compensando en una compensacion
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_comDeu")
public class ComDeu extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "idDeuda")
	private Long idDeuda;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idCompensacion") 
	private Compensacion compensacion;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idTipComDeuOCon") 
	private TipoComDeu tipoComDeu;

	@Column(name = "importe")
	private Double importe;

	//Constructores 
	public ComDeu(){
		super();
	}

	// Getters Y Setters
	public Compensacion getCompensacion() {
		return compensacion;
	}
	public void setCompensacion(Compensacion compensacion) {
		this.compensacion = compensacion;
	}
	public Long getIdDeuda() {
		return idDeuda;
	}
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	public TipoComDeu getTipoComDeu() {
		return tipoComDeu;
	}
	public void setTipoComDeu(TipoComDeu tipoComDeu) {
		this.tipoComDeu = tipoComDeu;
	}
	
	// Metodos de clase	
	public static ComDeu getById(Long id) {
		return (ComDeu) BalDAOFactory.getComDeuDAO().getById(id);
	}
	
	public static ComDeu getByIdNull(Long id) {
		return (ComDeu) BalDAOFactory.getComDeuDAO().getByIdNull(id);
	}
		
	public static List<ComDeu> getList() {
		return (ArrayList<ComDeu>) BalDAOFactory.getComDeuDAO().getList();
	}
	
	public static List<ComDeu> getListActivos() {			
		return (ArrayList<ComDeu>) BalDAOFactory.getComDeuDAO().getListActiva();
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
		if(getCompensacion() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.COMPENSACION_LABEL);
		}
		if(getTipoComDeu() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.TIPOCOMDEU_LABEL);
		}
		if(getImporte() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.COMDEU_IMPORTE);
		}
		if(getIdDeuda() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDA_LABEL);
		}
		
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
		
		/*if (GenericDAO.hasReference(this, TranArc.class, "comDeu")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.COMDEU_LABEL , BalError.DISPARDET_LABEL);
		}

		if (GenericDAO.hasReference(this, ComDeuRec.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.DISPAR_LABEL , BalError.DISPARREC_LABEL);
		}
		
		if (GenericDAO.hasReference(this, ComDeuPla.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.DISPAR_LABEL , BalError.DISPARPLA_LABEL);
		}*/
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
}
