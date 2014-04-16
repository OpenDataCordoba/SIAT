//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a EnvNotObl - Notas de Obligacion que vienen en los EnvioOsiris
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_envNotObl")
public class EnvNotObl extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "fechaRegistro")
	private Date fechaRegistro;
	
	@Column(name = "banco")
	private Integer banco;
	
	@Column(name = "nroCierreBanco")
	private Integer nroCierreBanco;
	
	@Column(name = "bancoOriginal")
	private Integer bancoOriginal;
	
	@Column(name = "nroCieBanOrig")
	private Integer nroCieBanOrig;
	
	@Column(name = "totalAcreditado")
	private Double totalAcreditado;
	
	@Column(name = "totalCredito")
	private Double totalCredito;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idEnvioOsiris")
	private EnvioOsiris envioOsiris;
	
	
	// Constructores
	public EnvNotObl(){
		super();
		// Seteo de valores default			
	}
	
	public EnvNotObl(Long id){
		super();
		setId(id);
	}
	
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Integer getBanco() {
		return banco;
	}

	public void setBanco(Integer banco) {
		this.banco = banco;
	}

	public Integer getNroCierreBanco() {
		return nroCierreBanco;
	}

	public void setNroCierreBanco(Integer nroCierreBanco) {
		this.nroCierreBanco = nroCierreBanco;
	}

	public void setTotalAcreditado(Double totalAcreditado) {
		this.totalAcreditado = totalAcreditado;
	}

	public Double getTotalAcreditado() {
		return totalAcreditado;
	}

	public void setTotalCredito(Double totalCredito) {
		this.totalCredito = totalCredito;
	}

	public Double getTotalCredito() {
		return totalCredito;
	}

	public EnvioOsiris getEnvioOsiris() {
		return envioOsiris;
	}

	public void setEnvioOsiris(EnvioOsiris envioOsiris) {
		this.envioOsiris = envioOsiris;
	}

	public Integer getBancoOriginal() {
		return bancoOriginal;
	}

	public void setBancoOriginal(Integer bancoOriginal) {
		this.bancoOriginal = bancoOriginal;
	}

	public Integer getNroCieBanOrig() {
		return nroCieBanOrig;
	}

	public void setNroCieBanOrig(Integer nroCieBanOrig) {
		this.nroCieBanOrig = nroCieBanOrig;
	}

	// Metodos de Clase
	public static EnvNotObl getById(Long id) {
		return (EnvNotObl) BalDAOFactory.getEnvNovOblDAO().getById(id);
	}
	
	public static EnvNotObl getByIdNull(Long id) {
		return (EnvNotObl) BalDAOFactory.getEnvNovOblDAO().getByIdNull(id);
	}
	
	public static List<EnvNotObl> getList() {
		return (ArrayList<EnvNotObl>) BalDAOFactory.getEnvNovOblDAO().getList();
	}
	
	public static List<EnvNotObl> getListActivos() {			
		return (ArrayList<EnvNotObl>) BalDAOFactory.getEnvNovOblDAO().getListActiva();
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
	
//		if (GenericDAO.hasReference(this, ${BeanRelacionado}.class, "envNovObl")) {
//			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
//					BalError.${BEAN}_LABEL, BalError.${BEAN_RELACIONADO}_LABEL );
//		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
//		if (StringUtil.isNullOrEmpty(getCodEnvNovObl())) {
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.${BEAN}_COD${BEAN} );
//		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
//		UniqueMap uniqueMap = new UniqueMap();
//		uniqueMap.addString("codEnvNovObl");
//		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
//			addRecoverableError(BaseError.MSG_CAMPO_UNICO, BalError.${BEAN}_COD${BEAN});			
//		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el EnvNovObl. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getEnvNovOblDAO().update(this);
	}

	/**
	 * Desactiva el EnvNovObl. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getEnvNovOblDAO().update(this);
	}
	
	/**
	 * Valida la activacion del EnvNovObl
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del EnvNovObl
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

}
