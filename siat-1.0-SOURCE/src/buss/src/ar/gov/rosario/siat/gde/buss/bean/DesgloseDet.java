//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a DesgloseDet - 
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_desgloseDet")
public class DesgloseDet extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	

@Column(name = "iddeudaOrig")
private Long iddeudaOrig;

@Column(name = "idAjuFisHis")
private Long idAjuFisHis;

@Column(name = "idAjuFisAcc")
private Long idAjuFisAcc;


@ManyToOne(fetch=FetchType.LAZY) 
   @JoinColumn(name="idDesglose") 
private Desglose desglose;

@ManyToOne(fetch=FetchType.LAZY) 
@JoinColumn(name="idAnulacion") 
private Anulacion anulacion;

	//<#Propiedades#>
	
	// Constructores
	public DesgloseDet(){
		super();
		// Seteo de valores default			
	}
	
	public DesgloseDet(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static DesgloseDet getById(Long id) {
		return (DesgloseDet) GdeDAOFactory.getDesgloseDetDAO().getById(id);
	}
	
	public static DesgloseDet getByIdNull(Long id) {
		return (DesgloseDet) GdeDAOFactory.getDesgloseDetDAO().getByIdNull(id);
	}
	
	public static List<DesgloseDet> getList() {
		return (ArrayList<DesgloseDet>) GdeDAOFactory.getDesgloseDetDAO().getList();
	}
	
	public static List<DesgloseDet> getListActivos() {			
		return (ArrayList<DesgloseDet>) GdeDAOFactory.getDesgloseDetDAO().getListActiva();
	}
	
	
	// Getters y setters
	
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
	
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
	  /*	UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codDesgloseDet");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, GdeError.DESGLOSEDET_CODDESGLOSEDET);			
		}*/
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el DesgloseDet. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getDesgloseDetDAO().update(this);
	}

	/**
	 * Desactiva el DesgloseDet. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getDesgloseDetDAO().update(this);
	}
	
	/**
	 * Valida la activacion del DesgloseDet
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del DesgloseDet
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	// Administracion de DesgloseDet
	public DesgloseDet createDesgloseDet(DesgloseDet desgloseDet) throws Exception {
		
		// Validaciones de negocio
		if (!desgloseDet.validateCreate()) {
			return desgloseDet;
		}

		GdeDAOFactory.getDesgloseDetDAO().update(desgloseDet);
		
		return desgloseDet;
	}	

	public DesgloseDet updateDesgloseDet(DesgloseDet desgloseDet) throws Exception {
		
		// Validaciones de negocio
		if (!desgloseDet.validateUpdate()) {
			return desgloseDet;
		}

		GdeDAOFactory.getDesgloseDetDAO().update(desgloseDet);
		
		return desgloseDet;
	}	

	public DesgloseDet deleteDesgloseDet(DesgloseDet desgloseDet) throws Exception {
		
		// Validaciones de negocio
		if (!desgloseDet.validateDelete()) {
			return desgloseDet;
		}
				
		GdeDAOFactory.getDesgloseDetDAO().delete(desgloseDet);
		
		return desgloseDet;
	}

	

	public Long getIddeudaOrig() {
		return iddeudaOrig;
	}

	public void setIddeudaOrig(Long iddeudaOrig) {
		this.iddeudaOrig = iddeudaOrig;
	}

	public Long getIdAjuFisHis() {
		return idAjuFisHis;
	}

	public void setIdAjuFisHis(Long idAjuFisHis) {
		this.idAjuFisHis = idAjuFisHis;
	}

	public Long getIdAjuFisAcc() {
		return idAjuFisAcc;
	}

	public void setIdAjuFisAcc(Long idAjuFisAcc) {
		this.idAjuFisAcc = idAjuFisAcc;
	}


	public Anulacion getAnulacion() {
		return anulacion;
	}

	public void setAnulacion(Anulacion anulacion) {
		this.anulacion = anulacion;
	}

	public Desglose getDesglose() {
		return desglose;
	}

	public void setDesglose(Desglose desglose) {
		this.desglose = desglose;
	}
	
	//<#MetodosBeanDetalle#>
}
