//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.afi.buss.dao.AfiDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a HabLoc - Habilitaciones de los Locales 
 * para el Formulario de Declaración Jurada proveniente de AFIP.
 * 
 * @author tecso
 */
@Entity
@Table(name = "afi_habLoc")
public class HabLoc extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name="idLocal")
	private Local local;	

	@Column(name = "numerocuenta")
	private String  numeroCuenta;
	
	@Column(name = "codrubro")
	private Long codRubro;
	
	@Column(name = "fechahabilitacion")
	private Date fechaHabilitacion;
	

	
	// Constructores
	public HabLoc(){
		super();
		// Seteo de valores default			
	}
	
	public HabLoc(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static HabLoc getById(Long id) {
		return (HabLoc) AfiDAOFactory.getHabLocDAO().getById(id);
	}
	
	public static HabLoc getByIdNull(Long id) {
		return (HabLoc) AfiDAOFactory.getHabLocDAO().getByIdNull(id);
	}
	
	public static List<HabLoc> getList() {
		return (ArrayList<HabLoc>) AfiDAOFactory.getHabLocDAO().getList();
	}
	
	public static List<HabLoc> getListActivos() {			
		return (ArrayList<HabLoc>) AfiDAOFactory.getHabLocDAO().getListActiva();
	}
	
	
	// Getters y setters
	
	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public Long getCodRubro() {
		return codRubro;
	}

	public void setCodRubro(Long codRubro) {
		this.codRubro = codRubro;
	}

	public Date getFechaHabilitacion() {
		return fechaHabilitacion;
	}

	public void setFechaHabilitacion(Date fechaHabilitacion) {
		this.fechaHabilitacion = fechaHabilitacion;
	}
	
	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
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
//		if (StringUtil.isNullOrEmpty(getCodHabilitacionLocal())) {
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_COD${BEAN} );
//		}
//		
//		if (StringUtil.isNullOrEmpty(getDesHabilitacionLocal())){
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_DES${BEAN});
//		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
//		UniqueMap uniqueMap = new UniqueMap();
//		uniqueMap.addString("codHabilitacionLocal");
//		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
//			addRecoverableError(BaseError.MSG_CAMPO_UNICO, AfiError.${BEAN}_COD${BEAN});			
//		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el HabLoc. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		AfiDAOFactory.getHabLocDAO().update(this);
	}

	/**
	 * Desactiva el HabLoc. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		AfiDAOFactory.getHabLocDAO().update(this);
	}
	
	/**
	 * Valida la activacion del HabLoc
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del HabLoc
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

}

	