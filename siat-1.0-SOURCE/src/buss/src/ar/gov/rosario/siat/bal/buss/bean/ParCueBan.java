//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.Date;
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
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a ParCueBan
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_parCueBan")
public class ParCueBan extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idPartida") 
	private Partida partida;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idCueBan") 
	private CuentaBanco cuentaBanco;

	@Column(name = "fechaDesde")
	private Date fechaDesde;

	@Column(name = "fechaHasta")
	private Date fechaHasta;


	//<#Propiedades#>
	
	// Constructores
	public ParCueBan(){
		super();
		// Seteo de valores default			
	}
	
	public ParCueBan(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ParCueBan getById(Long id) {
		return (ParCueBan) BalDAOFactory.getParCueBanDAO().getById(id);
	}
	
	public static ParCueBan getByIdNull(Long id) {
		return (ParCueBan) BalDAOFactory.getParCueBanDAO().getByIdNull(id);
	}
	
	public static List<ParCueBan> getList() {
		return (List<ParCueBan>) BalDAOFactory.getParCueBanDAO().getList();
	}
	
	public static List<ParCueBan> getListActivos() {			
		return (List<ParCueBan>) BalDAOFactory.getParCueBanDAO().getListActiva();
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
		
		if (getCuentaBanco()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.PARCUEBAN_CUENTABANCO );
		}
		
		if (getFechaDesde()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.PARCUEBAN_FECHADESDE );
		}
		
		if( getFechaDesde()!=null && getFechaHasta()!=null && getFechaDesde().after(getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, BalError.PARCUEBAN_FECHAHASTA, BalError.PARCUEBAN_FECHADESDE);
		}
		//	Validaciones        
		
		
				
		if (hasError()) {
			return false;
		}
		
		
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el ParCueBan. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getParCueBanDAO().update(this);
	}

	/**
	 * Desactiva el ParCueBan. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getParCueBanDAO().update(this);
	}
	
	/**
	 * Valida la activacion del ParCueBan
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del ParCueBan
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	public CuentaBanco getCuentaBanco() {
		return cuentaBanco;
	}

	public void setCuentaBanco(CuentaBanco cuentaBanco) {
		this.cuentaBanco = cuentaBanco;
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

	
	//<#MetodosBeanDetalle#>
}
