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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboConvenio;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Sellado
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_sellado")
public class Sellado extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	// <#Propiedades#>
	
	@Column(name = "codSellado")
	private String codSellado;

	@Column(name = "desSellado")
	private String desSellado;

	@OneToMany( mappedBy="sellado")
	@JoinColumn(name="idSellado")
	@OrderBy(clause="fechaDesde")
	@Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
	private List<ImpSel> listImpSel;  
	
	@OneToMany( mappedBy="sellado")
	@JoinColumn(name="idSellado")
	@OrderBy(clause="fechaDesde")
	@Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
	private List<AccionSellado> listAccionSellado;
	
	@OneToMany( mappedBy="sellado")
	@JoinColumn(name="idSellado")
	@OrderBy(clause="idPartida")
	@Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
	private List<ParSel> listParSel;
	
	@Transient
	double importeSellado=0;

	// Constructores
	public Sellado() {
		super();
	}

	public Sellado(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static Sellado getById(Long id) {
		return (Sellado) BalDAOFactory.getSelladoDAO().getById(id);
	}

	public static Sellado getByIdNull(Long id) {
		return (Sellado) BalDAOFactory.getSelladoDAO().getByIdNull(id);
	}

	public static Sellado getByCodigo(String codigo) throws Exception {
		return (Sellado) BalDAOFactory.getSelladoDAO().getByCodigo(codigo);
	}
	
	public static List<Sellado> getList() {
		return (ArrayList<Sellado>) BalDAOFactory.getSelladoDAO().getList();
	}

	public static List<Sellado> getListActivos() {
		return (ArrayList<Sellado>) BalDAOFactory.getSelladoDAO()
				.getListActiva();
	}

	// Getters y setters
	public String getCodSellado() {
		return codSellado;
	}

	public void setCodSellado(String codSellado) {
		this.codSellado = codSellado;
	}

	public String getDesSellado() {
		return desSellado;
	}

	public void setDesSellado(String desSellado) {
		this.desSellado = desSellado;
	}
	
	public List<ImpSel> getListImpSel() {
		return listImpSel;
	}

	public void setListImpSel(List<ImpSel> listImpSel) {
		this.listImpSel = listImpSel;
	}

	public List<AccionSellado> getListAccionSellado() {
		return listAccionSellado;
	}

	public void setListAccionSellado(List<AccionSellado> listAccionSellado) {
		this.listAccionSellado = listAccionSellado;
	}

	public List<ParSel> getListParSel() {
		return listParSel;
	}

	public void setListParSel(List<ParSel> listParSel) {
		this.listParSel = listParSel;
	}


	public double getImporteSellado() {
		return importeSellado;
	}

	public void setImporteSellado(double importeSellado) {
		this.importeSellado = importeSellado;
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
		// limpiamos la lista de errores
		clearError();

		if (GenericDAO.hasReference(this, AccionSellado.class, "sellado")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.SELLADO_LABEL , BalError.ACCIONSELLADO_LABEL);
		}
		if (GenericDAO.hasReference(this, AuxSellado.class, "sellado")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.SELLADO_LABEL , BalError.ASENTAMIENTO_LABEL);
		}
		if (GenericDAO.hasReference(this, ImpSel.class, "sellado")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.SELLADO_LABEL , BalError.IMPSEL_LABEL);
		}
		if (GenericDAO.hasReference(this, ParSel.class, "sellado")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.SELLADO_LABEL , BalError.PARSEL_LABEL);
		}
		if (GenericDAO.hasReference(this, Recibo.class, "sellado")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.SELLADO_LABEL , GdeError.RECIBO_LABEL);
		}
		if (GenericDAO.hasReference(this, ReciboConvenio.class, "sellado")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.SELLADO_LABEL , GdeError.RECIBO_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones
		if (StringUtil.isNullOrEmpty(getCodSellado())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					BalError.SELLADO_CODSELLADO);
		}

		if (StringUtil.isNullOrEmpty(getDesSellado())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					BalError.SELLADO_DESSELLADO);
		}

		if (hasError()) {
			return false;
		}

		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codSellado");
		if (!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO,
					BalError.SELLADO_CODSELLADO);
		}

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el Sellado. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getSelladoDAO().update(this);
	}

	/**
	 * Desactiva el Sellado. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getSelladoDAO().update(this);
	}

	/**
	 * Valida la activacion del Sellado
	 * 
	 * @return boolean
	 */
	private boolean validateActivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	/**
	 * Valida la desactivacion del Sellado
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	// Administracion de ImpSel
	public ImpSel createImpSel(ImpSel impSel) throws Exception {		
		// Validaciones de negocio
		if (!impSel.validateCreate()) {
			return impSel;
		}

		BalDAOFactory.getImpSelDAO().update(impSel);	
		
		return impSel;
	}

	public ImpSel deleteImpSel(ImpSel impSel) {
		// Validaciones de negocio
		if (!impSel.validateDelete()) {
			return impSel;
		}

		BalDAOFactory.getImpSelDAO().delete(impSel);	
		
		return impSel;
	}

	public ImpSel updateImpSel(ImpSel impSel) throws Exception {
		// Validaciones de negocio
		
		if (!impSel.validateUpdate()) {
			return impSel;
		}

		BalDAOFactory.getImpSelDAO().update(impSel);	
		
		return impSel;
	}
	
	//	 Administracion de Accion Sellado
	public AccionSellado createAccionSellado(AccionSellado accionSellado) throws Exception {		
		// Validaciones de negocio
		if (!accionSellado.validateCreate()) {
			return accionSellado;
		}

		BalDAOFactory.getAccionSelladoDAO().update(accionSellado);	
		
		return accionSellado;
	}
	
	public AccionSellado deleteAccionSellado(AccionSellado accionSellado) {
		// Validaciones de negocio
		if (!accionSellado.validateDelete()) {
			return accionSellado;
		}

		BalDAOFactory.getAccionSelladoDAO().delete(accionSellado);	
		
		return accionSellado;
	}

	public AccionSellado updateAccionSellado(AccionSellado accionSellado) throws Exception {
		// Validaciones de negocio
		if (!accionSellado.validateUpdate()) {
			return accionSellado;
		}

		BalDAOFactory.getAccionSelladoDAO().update(accionSellado);	
		
		return accionSellado;
	}
	
	//	Administracion de ParSel
	public ParSel createParSel(ParSel parSel) throws Exception {		
		// Validaciones de negocio
		if (!parSel.validateCreate()) {
			return parSel;
		}

		BalDAOFactory.getParSelDAO().update(parSel);	
		
		return parSel;
	}
	
	public ParSel deleteParSel(ParSel parSel) {
		// Validaciones de negocio
		if (!parSel.validateDelete()) {
			return parSel;
		}

		BalDAOFactory.getParSelDAO().delete(parSel);	
		
		return parSel;
	}

	public ParSel updateParSel(ParSel parSel) throws Exception {
		// Validaciones de negocio
		if (!parSel.validateUpdate()) {
			return parSel;
		}

		BalDAOFactory.getParSelDAO().update(parSel);	
		
		return parSel;
	}
}
