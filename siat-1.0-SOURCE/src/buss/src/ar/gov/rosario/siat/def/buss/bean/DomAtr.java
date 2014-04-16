//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Dominio Atributo
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_domatr")
public class DomAtr extends BaseBO {
	
	// Propiedades
	private static final long serialVersionUID = 1L;

	public static final long ID_DOMATR_ACTIVIDAD = 34L;
	
	@Column(name = "codDomAtr")
	private String codDomAtr;
	
	@Column(name = "desDomAtr")
	private String desDomAtr;
	
	@ManyToOne(optional=false) 
    @JoinColumn(name="idTipoAtributo")
	private TipoAtributo tipoAtributo;
	
	@Column(name = "classForName")	
	private String classForName; 
	
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="idDomAtr")
	@OrderBy(clause="desValor")
	private List<DomAtrVal> listDomAtrVal;
	
	// Constructores
	public DomAtr(){
		super();
	}
	
	//getters y setters
	public String getCodDomAtr() {
		return codDomAtr;
	}
	public void setCodDomAtr(String codDomAtr) {
		this.codDomAtr = codDomAtr;
	}
	public String getDesDomAtr() {
		return desDomAtr;
	}
	public void setDesDomAtr(String desDomAtr) {
		this.desDomAtr = desDomAtr;
	}
	public TipoAtributo getTipoAtributo() {
		return tipoAtributo;
	}
	public void setTipoAtributo(TipoAtributo tipoAtributo) {
		this.tipoAtributo = tipoAtributo;
	}
	public List<DomAtrVal> getListDomAtrVal() {
		return listDomAtrVal;
	}
	public void setListDomAtrVal(List<DomAtrVal> listDomAtrVal) {
		this.listDomAtrVal = listDomAtrVal;
	}
	public String getClassForName() {
		return classForName;
	}
	public void setClassForName(String classForName) {
		this.classForName = classForName;
	}

	// Metodos de Clase	
	public static DomAtr getById(Long id) {
		return (DomAtr) DefDAOFactory.getDomAtrDAO().getById(id);
	}
	
	public static DomAtr getByIdNull(Long id) {
		return (DomAtr) DefDAOFactory.getDomAtrDAO().getByIdNull(id);
	}
	
	public static List<DomAtr> getList() {
		return (ArrayList<DomAtr>) DefDAOFactory.getDomAtrDAO().getList();
	}
	
	public static List<DomAtr> getListActivos() {			
		return (ArrayList<DomAtr>) DefDAOFactory.getDomAtrDAO().getListActiva();
	}
	
	/**
	 * Obtiene la lista de Dominios Atributo activos para el id del tipo de atributo
	 * @param  idTipoAtributo
	 * @return List<DomAtr>
	 */
	public static List<DomAtr> getListActivosByIdTipoAtributo(Long idTipoAtributo) {			
		return (List<DomAtr>) DefDAOFactory.getDomAtrDAO().getListActivosByIdTipoAtributo(idTipoAtributo);
	}
	
	// Metodos de instancia
	// Validaciones
	public boolean validateCreate() throws Exception {
		//limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		//limpiamos la lista de errores
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
		
		// verificamos que no tenga Dom Atr val asociados
		if (GenericDAO.hasReference(this, DomAtrVal.class, "domAtr")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.DOMATR_LABEL , DefError.DOMATRVAL_LABEL);
			
		}
		
		// verificamos que no tenga Atributos asociados
		if (GenericDAO.hasReference(this, Atributo.class, "domAtr")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.DOMATR_LABEL, DefError.ATRIBUTO_LABEL);
			
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio
	public DomAtrVal createDomAtrVal(DomAtrVal domAtrVal) throws Exception {
		
		// Validaciones de negocio
		if (!domAtrVal.validateCreate()) {
			return domAtrVal;
		}

		DefDAOFactory.getDomAtrValDAO().update(domAtrVal);
		
		return domAtrVal;
	}	

	public DomAtrVal updateDomAtrVal(DomAtrVal domAtrVal) throws Exception {
		
		// Validaciones de negocio
		if (!domAtrVal.validateUpdate()) {
			return domAtrVal;
		}

		DefDAOFactory.getDomAtrValDAO().update(domAtrVal);
		
		return domAtrVal;
	}	

	public DomAtrVal deleteDomAtrVal(DomAtrVal domAtrVal) throws Exception {
		
		// Validaciones de negocio
		if (!domAtrVal.validateDelete()) {
			return domAtrVal;
		}

		DefDAOFactory.getDomAtrValDAO().delete(domAtrVal);
		
		return domAtrVal;
	}	
	
	/**
	 * Activa el Dominio Atributo. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		DefDAOFactory.getDomAtrDAO().update(this);
	}

	/**
	 * Desactiva el Dominio Atributo. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		DefDAOFactory.getDomAtrDAO().update(this);
	}
	
	/**
	 * Pasa el estado a creado 
	 *
	 */
	public void creado(){
		this.setEstado(Estado.CREADO.getId());
		DefDAOFactory.getDomAtrDAO().update(this);
	}	
	
	/**
	 * Valida la activacion del Dominio Atributo
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Dominio Atributo
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	private boolean validate() throws Exception {
		
		if (StringUtil.isNullOrEmpty(getCodDomAtr())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.DOMATR_CODDOMATR);
			
		}

		if (StringUtil.isNullOrEmpty(getDesDomAtr())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.DOMATR_DESDOMATR);
			
		}

		if (getTipoAtributo() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOATRIBUTO_LABEL);
		}
		
		if (hasError()) {
			return false;
		}
		
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codDomAtr");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, DefError.DOMATR_CODDOMATR);			
		}

		if (hasError()) {
			return false;
		}

		return true;
	}

	public String getDesValorByCodigo(String strCodigo){
		for(DomAtrVal davo: this.getListDomAtrVal() ){
			if (davo.getValor().trim().equals(strCodigo.trim())){				
				return davo.getDesValor();
			}
		}
		return "";
	}	
	
}
