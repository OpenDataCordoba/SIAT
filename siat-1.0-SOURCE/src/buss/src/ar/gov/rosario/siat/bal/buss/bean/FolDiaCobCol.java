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
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean que representa cada valor de la grilla de días de cobranza
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_folDiaCobCol")
public class FolDiaCobCol extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "importe")
	private Double importe;
		
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idTipoCob") 
	private TipoCob tipoCob;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idFolDiaCob") 
	private FolDiaCob folDiaCob;

	//Constructores 
	public FolDiaCobCol(){
		super();
	}

	// Getters Y Setters
	public FolDiaCob getFolDiaCob() {
		return folDiaCob;
	}
	public void setFolDiaCob(FolDiaCob folDiaCob) {
		this.folDiaCob = folDiaCob;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	public TipoCob getTipoCob() {
		return tipoCob;
	}
	public void setTipoCob(TipoCob tipoCob) {
		this.tipoCob = tipoCob;
	}

	// Metodos de clase	
	public static FolDiaCobCol getById(Long id) {
		return (FolDiaCobCol) BalDAOFactory.getFolDiaCobColDAO().getById(id);
	}
	
	public static FolDiaCobCol getByIdNull(Long id) {
		return (FolDiaCobCol) BalDAOFactory.getFolDiaCobColDAO().getByIdNull(id);
	}
		
	public static List<FolDiaCobCol> getList() {
		return (ArrayList<FolDiaCobCol>) BalDAOFactory.getFolDiaCobColDAO().getList();
	}
	
	public static List<FolDiaCobCol> getListActivos() {			
		return (ArrayList<FolDiaCobCol>) BalDAOFactory.getFolDiaCobColDAO().getListActiva();
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
		/*if(StringUtil.isNullOrEmpty(getDesFolDiaCobCol())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPAR_DESDISPAR);
		}
		if(getRecurso()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPAR_RECURSO);
		}*/
		
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
		
		/*if (GenericDAO.hasReference(this, TranArc.class, "folio")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.ARCHIVO_LABEL , BalError.DISPARDET_LABEL);
		}

		if (GenericDAO.hasReference(this, FolDiaCobColRec.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.DISPAR_LABEL , BalError.DISPARREC_LABEL);
		}
		
		if (GenericDAO.hasReference(this, FolDiaCobColPla.class, "disPar")) {
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
