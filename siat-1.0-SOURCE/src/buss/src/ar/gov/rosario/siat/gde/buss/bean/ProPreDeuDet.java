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
import javax.persistence.Transient;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a ProPreDeuDet: detalle de 
 * Prescripcion Masiva de Deuda
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_proPreDeuDet")
public class ProPreDeuDet extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	public static final int NO_PRESCRIBIR = 0;
	public static final int PRESCRIBIR    = 1;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idProPreDeu") 
	private ProPreDeu proPreDeu;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idCuenta") 
	private Cuenta cuenta;

    @Column(name="idDeuda") 
	private Long idDeuda;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idVia") 
	private ViaDeuda viaDeuda;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idEstado") 
	private EstadoDeuda estadoDeuda;

	@Column(name = "accion")
	private Integer accion;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idEstProPreDeuDet") 
	private EstProPreDeuDet estProPreDeuDet;

	@Column(name = "observacion")
	private String observacion;
	
	@Transient
	private Deuda deuda;
	
	// Constructores
	public ProPreDeuDet(){
		super();
	}
	
	public ProPreDeuDet(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ProPreDeuDet getById(Long id) {
		return (ProPreDeuDet) GdeDAOFactory.getProPreDeuDetDAO().getById(id);
	}
	
	public static ProPreDeuDet getByIdNull(Long id) {
		return (ProPreDeuDet) GdeDAOFactory.getProPreDeuDetDAO().getByIdNull(id);
	}
	
	public static List<ProPreDeuDet> getList() {
		return (ArrayList<ProPreDeuDet>) GdeDAOFactory.getProPreDeuDetDAO().getList();
	}
	
	public static List<ProPreDeuDet> getListActivos() {			
		return (ArrayList<ProPreDeuDet>) GdeDAOFactory.getProPreDeuDetDAO().getListActiva();
	}
	
	// Getters y setters
	public ProPreDeu getProPreDeu() {
		return proPreDeu;
	}

	public void setProPreDeu(ProPreDeu proPreDeu) {
		this.proPreDeu = proPreDeu;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public Long getIdDeuda() {
		return idDeuda;
	}

	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}

	public ViaDeuda getViaDeuda() {
		return viaDeuda;
	}

	public void setViaDeuda(ViaDeuda viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

	public EstadoDeuda getEstadoDeuda() {
		return estadoDeuda;
	}

	public void setEstadoDeuda(EstadoDeuda estadoDeuda) {
		this.estadoDeuda = estadoDeuda;
	}

	public Integer getAccion() {
		return accion;
	}

	public void setAccion(Integer accion) {
		this.accion = accion;
	}

	public EstProPreDeuDet getEstProPreDeuDet() {
		return estProPreDeuDet;
	}

	public void setEstProPreDeuDet(EstProPreDeuDet estProPreDeuDet) {
		this.estProPreDeuDet = estProPreDeuDet;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public Deuda getDeuda() {
		return Deuda.getById(getIdDeuda());
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
		if (getProPreDeu() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROPREDEUDET_PROPREDEU);
		}
		
		if (getCuenta() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROPREDEUDET_CUENTA);
		}

		if (getIdDeuda() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROPREDEUDET_DEUDA);
		}

		if (getViaDeuda() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROPREDEUDET_VIADEUDA);
		}

		if (getEstado() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROPREDEUDET_ESTADO);
		}

		if (getAccion() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROPREDEUDET_ACCION);
		}

		if (getEstProPreDeuDet() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROPREDEUDET_ESTPROPREDEUDET);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	public boolean prescribe() {
		return accion == PRESCRIBIR;
	}
}
