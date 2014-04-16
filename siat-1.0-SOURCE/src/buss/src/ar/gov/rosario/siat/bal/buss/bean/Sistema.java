//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.TipoDeuda;
import ar.gov.rosario.siat.emi.buss.bean.AuxDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Sistema
 * Representa los sistemas actuales. Se utilizara como elemento de conversion mientras dure el periodo transitorio.
 * @author tecso
 *
 */
@Entity
@Table(name = "bal_sistema")
public class Sistema extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "nroSistema")
	private Long nroSistema;
	
	@Column(name = "desSistema")
	private String desSistema;
	
	@Column(name = "esServicioBanco")
	private Integer esServicioBanco;
	
	@ManyToOne()//fetch=FetchType.LAZY)
    @JoinColumn(name="idTipoDeuda")
	private TipoDeuda tipoDeuda;
	
	@ManyToOne()//fetch=FetchType.LAZY)
    @JoinColumn(name="idServicioBanco")	
	private ServicioBanco servicioBanco;
	
	@OneToMany()
	@JoinColumn(name="idSistema")
	private List<Plan> listPlan;
	
	// Constructores
	public Sistema(){
		super();
	}

	// Getters y Setters 
	
	public String getDesSistema() {
		return desSistema;
	}

	public void setDesSistema(String desSistema) {
		this.desSistema = desSistema;
	}

	public Long getNroSistema() {
		return nroSistema;
	}

	public void setNroSistema(Long nroSistema) {
		this.nroSistema = nroSistema;
	}
		
	public TipoDeuda getTipoDeuda() {
		return tipoDeuda;
	}

	public void setTipoDeuda(TipoDeuda tipoDeuda) {
		this.tipoDeuda = tipoDeuda;
	}	
	
	public Integer getEsServicioBanco() {
		return esServicioBanco;
	}

	public void setEsServicioBanco(Integer esServicioBanco) {
		this.esServicioBanco = esServicioBanco;
	}

	public ServicioBanco getServicioBanco() {
		return servicioBanco;
	}

	public void setServicioBanco(ServicioBanco servicioBanco) {
		this.servicioBanco = servicioBanco;
	}
	
	public List<Plan> getListPlan() {
		return listPlan;
	}
	
	public void setListPlan(List<Plan> listPlan) {
		this.listPlan = listPlan;
	}

	// Metodos de clase
	public static Sistema getById(Long id) {
		return (Sistema) BalDAOFactory.getSistemaDAO().getById(id);
	}
	
	public static Sistema getByIdNull(Long id) {
		return (Sistema) BalDAOFactory.getSistemaDAO().getByIdNull(id);
	}
	
	public static Sistema getByNroSistema(Long nroSistema) throws Exception {
		return (Sistema) BalDAOFactory.getSistemaDAO().getByNroSistema(nroSistema);
	}
	
	public static List<Sistema> getList() {
		return (ArrayList<Sistema>) BalDAOFactory.getSistemaDAO().getList();
	}
	
	public static List<Sistema> getListActivos() {			
		return (ArrayList<Sistema>) BalDAOFactory.getSistemaDAO().getListActiva();
	}
	
	public static List<Sistema> getListByServicioBanco(ServicioBanco servicioBanco) {
		return (ArrayList<Sistema>) BalDAOFactory.getSistemaDAO().getListByServicioBanco(servicioBanco);
	}
	
	/**
	 * Obtiene el sistema que es servicioBanco (esServicioBanco=1)
	 * de todos los sistemas los que pertenece el recurso pasado 
	 * como parametro
	 * 
	 * @param recurso
	 * @return Sistema
	 */
	public static Sistema getSistemaEmision(Recurso recurso){
		return BalDAOFactory.getSistemaDAO().getSistemaEmision(recurso);
	}
	
	// Metodos de Instancia
	public Sistema getSistemaEsServicioBanco(){
		
		/* 
		 * si this es un ServicioBanco
		 *    devolver this
		 *    
		 * sino
		 *    obtener el ServicioBanco relacionado con this (servicioBanco)
		 *    obtener el Sistema que esServicioBanco y sistema.getServicioBanco() == servicioBanco obtenido
		 *    
		 *    si se obtiene m'as que uno devolver null
		 * 
		 */
		
		if (getEsServicioBanco().intValue() == 1)
			return this;
		
		Sistema sistemaEsServBanc = BalDAOFactory.getSistemaDAO().getSistemaServicioBanco(this); 
		
		return sistemaEsServBanc;

	}
	
	
	
	// Validaciones
	/**
	 * Valida la creacion
	 * @author 
	 * @throws Exception 
	 */
	public boolean validateCreate() throws Exception {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (!this.validate()) {
			return false;
		}
		
		//Validaciones de Negocio
		
		return true;
	}

	/**
	 * Valida la actualizacion
	 * @author
	 * @throws Exception 
	 */
	public boolean validateUpdate() throws Exception {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (!this.validate()) {
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
		if (GenericDAO.hasReference(this, Convenio.class, "sistema")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.SISTEMA_LABEL , GdeError.CONVENIO_LABEL);
		}
		if (GenericDAO.hasReference(this, Plan.class, "sistema")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.SISTEMA_LABEL , GdeError.PLAN_LABEL);
		}
		if (GenericDAO.hasReference(this, AuxRecaudado.class, "sistema") ||
				GenericDAO.hasReference(this, AuxSellado.class, "sistema") ||
				GenericDAO.hasReference(this, AuxDeuda.class, "sistema")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.SISTEMA_LABEL , BalError.ASENTAMIENTO_AUXILIAR_LABEL);
		}
		// Nota: Existe tambien otra validacion en el BalDefinicionServiceHbmImpl. En el deleteSistema, se catchea alguna posible
		// excepcion al tratar de eliminar y se lanza un RecoverableError con referencia a Deuda.
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;
	}

	// Metodos de negocio
	
	/**
	 * Activa el Atributo. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getSistemaDAO().update(this);
	}

	/**
	 * Desactiva el Atributo. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getSistemaDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Atributo
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Atributo
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	private boolean validate() throws Exception {
		//	Validaciones de Requeridos
		if (getNroSistema()==null || getNroSistema().longValue()<0) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.SISTEMA_NROSISTEMA);
		}
		if (StringUtil.isNullOrEmpty(getDesSistema())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.SISTEMA_DESSISTEMA);
		}
		if (getServicioBanco() == null || getServicioBanco().getId()<0) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.SISTEMA_SERVICIOBANCO_LABEL);
		}
		if (getEsServicioBanco()== null || getEsServicioBanco().intValue()<0) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.SISTEMA_ESSERVICIOBANCO_LABEL);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		if (!BalDAOFactory.getSistemaDAO().chkUniqueSistemaServicioBanco(this)){
			addRecoverableValueError("Ya existe un sistema que es Servicio Banco para el Servicio Banco Seleccionado");			
		}
		
		if (hasError()) {
			return false;
		}

		return true;		
	}
}
