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

import ar.gov.rosario.siat.ef.buss.bean.DetAjuDet;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a MultaDet
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_multaDet")
public class MultaDet extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idMulta") 
	private Multa multa;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idDetAjuDet") 
	private DetAjuDet detAjuDet;

	@Column(name = "periodo")
	private Integer periodo;
	
	@Column(name = "anio")
	private Integer anio;
	
	@Column(name = "porOri")
	private Double porOri;
	
	@Column(name = "porDes")
	private Double porDes;
	
	@Column(name = "porApl")
	private Double porApl;
	
	@Column(name = "importeBase")
	private Double importeBase;
	
	@Column(name="importeAct")
	private Double importeAct;
	
	@Column(name = "importeAplicado")
	private Double importeAplicado;
	
	@Column(name = "pagoContadoOBueno")
	private Double pagoContadoOBueno;
	
	@Column(name = "resto")
	private Double resto;
	
	@Column(name = "pagoActualizado")
	private Double pagoActualizado;
	
	@Column(name = "restoActualizado")
	private Double restoActualizado;
	
	@Column(name = "aplicado")
	private Double aplicado;
	
	// Constructores
	public MultaDet(){
		super();
		// Seteo de valores default	
	}
	
	public MultaDet(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static MultaDet getById(Long id) {
		return (MultaDet) GdeDAOFactory.getMultaDetDAO().getById(id);
	}
	
	public static MultaDet getByIdNull(Long id) {
		return (MultaDet) GdeDAOFactory.getMultaDetDAO().getByIdNull(id);
	}
	
	public static List<MultaDet> getList() {
		return (ArrayList<MultaDet>) GdeDAOFactory.getMultaDetDAO().getList();
	}
	
	public static List<MultaDet> getListActivos() {			
		return (ArrayList<MultaDet>) GdeDAOFactory.getMultaDetDAO().getListActiva();
	}
	
	
	// Getters y setters
	
	public Multa getMulta() {
		return multa;
	}

	public void setMulta(Multa multa) {
		this.multa = multa;
	}

	public DetAjuDet getDetAjuDet() {
		return detAjuDet;
	}

	public void setDetAjuDet(DetAjuDet detAjuDet) {
		this.detAjuDet = detAjuDet;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Double getPorOri() {
		return porOri;
	}

	public void setPorOri(Double porOri) {
		this.porOri = porOri;
	}

	public Double getPorDes() {
		return porDes;
	}

	public void setPorDes(Double porDes) {
		this.porDes = porDes;
	}

	public Double getPorApl() {
		return porApl;
	}

	public void setPorApl(Double porApl) {
		this.porApl = porApl;
	}

	public Double getImporteBase() {
		return importeBase;
	}

	public void setImporteBase(Double importeBase) {
		this.importeBase = importeBase;
	}

	public Double getImporteAplicado() {
		return importeAplicado;
	}

	public void setImporteAplicado(Double importeAplicado) {
		this.importeAplicado = importeAplicado;
	}

	public Double getImporteAct() {
		return importeAct;
	}

	public void setImporteAct(Double importeAct) {
		this.importeAct = importeAct;
	}

	public Double getPagoContadoOBueno() {
		return pagoContadoOBueno;
	}

	public void setPagoContadoOBueno(Double pagoContadoOBueno) {
		this.pagoContadoOBueno = pagoContadoOBueno;
	}

	public Double getResto() {
		return resto;
	}

	public void setResto(Double resto) {
		this.resto = resto;
	}

	public Double getPagoActualizado() {
		return pagoActualizado;
	}

	public void setPagoActualizado(Double pagoActualizado) {
		this.pagoActualizado = pagoActualizado;
	}

	public Double getRestoActualizado() {
		return restoActualizado;
	}

	public void setRestoActualizado(Double restoActualizado) {
		this.restoActualizado = restoActualizado;
	}

	public Double getAplicado() {
		return aplicado;
	}

	public void setAplicado(Double aplicado) {
		this.aplicado = aplicado;
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
	
		/*Ejemplo:
		if (GenericDAO.hasReference(this, BeanRelacionado .class, " bean ")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.${BEAN}_LABEL, GdeError. BEAN_RELACIONADO _LABEL );
		}*/
		
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
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el MultaDet. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getMultaDetDAO().update(this);
	}

	/**
	 * Desactiva el MultaDet. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getMultaDetDAO().update(this);
	}
	
	/**
	 * Valida la activacion del MultaDet
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del MultaDet
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
