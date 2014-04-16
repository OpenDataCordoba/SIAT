//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.pad.buss.bean.ConAtrVal;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Bean correspondiente a Atributos del contribuyente
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_conAtr")
public class ConAtr extends BaseBO {
	
	
	
	// Propiedades
	private static final long serialVersionUID = 1L;

	@ManyToOne() 
    @JoinColumn(name="idAtributo")
	private Atributo atributo;
	
	@Column(name = "esAtrSegmentacion")
	private Integer esAtrSegmentacion;
	
	@Column (name = "valorDefecto")
	private String valorDefecto;
	
	@Column (name= "esVisConDeu")
	private Integer esVisConDeu;
	
	@Column (name= "esAtributoBus")
	private Integer esAtributoBus;
	
	@Column (name= "admBusPorRan")
	private Integer admBusPorRan;
	
	// Constructores
	public ConAtr(){
		super();
	}
	
	// Getters y setters
	public Atributo getAtributo() {
		return atributo;
	}
	public void setAtributo(Atributo atributo) {
		this.atributo = atributo;
	}
	public Integer getEsAtrSegmentacion() {
		return esAtrSegmentacion;
	}
	public void setEsAtrSegmentacion(Integer esAtrSegmentacion) {
		this.esAtrSegmentacion = esAtrSegmentacion;
	}

	public String getValorDefecto() {
		return valorDefecto;
	}

	public void setValorDefecto(String valorDefecto) {
		this.valorDefecto = valorDefecto;
	}

	public Integer getEsVisConDeu() {
		return esVisConDeu;
	}

	public void setEsVisConDeu(Integer esVisConDeu) {
		this.esVisConDeu = esVisConDeu;
	}

	public Integer getEsAtributoBus() {
		return esAtributoBus;
	}

	public void setEsAtributoBus(Integer esAtributoBus) {
		this.esAtributoBus = esAtributoBus;
	}

	public Integer getAdmBusPorRan() {
		return admBusPorRan;
	}

	public void setAdmBusPorRan(Integer admBusPorRan) {
		this.admBusPorRan = admBusPorRan;
	}

	// Metodos de clase
	public static ConAtr getById(Long id) {
		return (ConAtr) DefDAOFactory.getConAtrDAO().getById(id);
	}

	public static ConAtr getByIdNull(Long id) {
		return (ConAtr) DefDAOFactory.getConAtrDAO().getByIdNull(id);
	}

	public static List<ConAtr> getListActivos() {
		return (ArrayList<ConAtr>) DefDAOFactory.getConAtrDAO().getListActiva();
	}
	
	/**
	 * Obtiene una lista de los Atributos del Contribuyente marcados como visibles en la consulta de deuda.
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public static List<ConAtr> getListActivosForWeb() throws Exception {
		return (ArrayList<ConAtr>) DefDAOFactory.getConAtrDAO().getListActivosForWeb();
	}
	
	public static List<ConAtr> getList() {
		return (ArrayList<ConAtr>) DefDAOFactory.getConAtrDAO().getList();
	}

	// Metodos de Instancia
	// Validaciones
	
	/**
	 * Valida la creacion
	 * @author Ivan
	 */
	public boolean validateCreate() throws Exception {
		//limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}
	/**
	 * Valida la actualizacion
	 * @author Ivan
	 */
	public boolean validateUpdate() throws Exception {
		//limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}
	/**
	 * Valida la eliminacion
	 * @author Ivan
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		if (GenericDAO.hasReference(this, ConAtrVal.class, "conAtr")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.CONATR_LABEL , PadError.CONATRVAL_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio
	
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		DefDAOFactory.getConAtrDAO().update(this);
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
		DefDAOFactory.getConAtrDAO().update(this);
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
		
		if (getAtributo() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.ATRIBUTO_LABEL);
		}

		if (StringUtil.isNullOrEmpty(getValorDefecto())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.CONATR_VALORDEFECTO);
		}
		
		if (getEsAtrSegmentacion() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.CONATR_ESATRSEGMENTACION);
		}

		if (getEsVisConDeu() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.CONATR_ESVISCONDEU);
		}
		
		if (getEsAtributoBus() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.CONATR_ESATRIBUTOBUS);
		}
		
		if (getAdmBusPorRan() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.CONATR_ADMBUSPORRAN);
		}
		
		if (hasError()) {
			return false;
		}
		
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addEntity("atributo");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, DefError.ATRIBUTO_LABEL);			
		}

		if (hasError()) {
			return false;
		}

		return true;
	}
	
	
	 /**
	 * Obtiene la definicion con su valor por defecto del ConAtr.
	 * <p>Este metodo sirve para obtener el atributo de contribuyente y su valor por defecto.
	 * @return el genericAtrDefinition cargado con el atributo y su valor.
	 * @throws Exception 
	 */
	public GenericAtrDefinition getDefinitionValue() throws Exception {		

		GenericAtrDefinition genericAtrDefinition = this.getAtributo().getDefinition();
		genericAtrDefinition.addValor(this.getValorDefecto());
		genericAtrDefinition.setEsRequerido(true);
			
		return genericAtrDefinition;
	}

	public String getEsAtrSegmentacionView(){
		return SiNo.getById(this.getEsAtrSegmentacion()).getValue();
	}
	public String getEsVisConDeuView(){
		return SiNo.getById(this.getEsVisConDeu()).getValue();
	}
	public String getEsAtributoBusView(){
		return SiNo.getById(this.getEsAtributoBus()).getValue();
	}	
	public String getAdmBusPorRanView(){
		return SiNo.getById(this.getAdmBusPorRan()).getValue();
	}
}
