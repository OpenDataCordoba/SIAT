//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.RecConADec;
import ar.gov.rosario.siat.def.buss.bean.RecTipUni;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a DecJurDet
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_decJurDet")
public class DecJurDet extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idDecJur")
	private DecJur decJur;
	
	@Column(name="detalle")
	private String detalle;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idRecConADec")
	private RecConADec recConADec;
	
	@Column(name="base")
	private Double base;
	
	@Column(name="multiplo")
	private Double multiplo;
	
	@Column(name="subtotal1")
	private Double subtotal1;
	
	@Column(name="canUni")
	private Double canUni;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idRecTipUni")
	private RecTipUni recTipUni;
	
	@Column(name="unidad")
	private String unidad;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idTipoUnidad")
	private RecConADec tipoUnidad;
	
	@Column(name="desTipoUnidad")
	private String desTipoUnidad;
	
	@Column(name="valRef")
	private Double valRef;
	
	@Column(name="valUnidad")
	private Double valUnidad;
	
	@Column(name="subtotal2")
	private Double subtotal2;
	
	@Column(name="minimo")
	private Double minimo;
	
	@Column(name="totalConcepto")
	private Double totalConcepto;
	
	@Column(name="alcanceEtur")
	private Integer alcanceEtur;

	
	//<#Propiedades#>
	
	// Constructores
	public DecJurDet(){
		super();
		// Seteo de valores default	
		// propiedad_ejemplo = valorDefault;
	}
	
	
	// Metodos de Clase
	public static DecJurDet getById(Long id) {
		return (DecJurDet) GdeDAOFactory.getDecJurDetDAO().getById(id);
	}
	
	public static DecJurDet getByIdNull(Long id) {
		return (DecJurDet) GdeDAOFactory.getDecJurDetDAO().getByIdNull(id);
	}
	
	public static List<DecJurDet> getList() {
		return (List<DecJurDet>) GdeDAOFactory.getDecJurDetDAO().getList();
	}
	
	public static List<DecJurDet> getListActivos() {			
		return (List<DecJurDet>) GdeDAOFactory.getDecJurDetDAO().getListActiva();
	}
	
	public static DecJurDet getByCuentaPeriodoAnioyActividad(Cuenta cuenta, Integer periodo, Integer anio, Long idActividad){
		return (DecJurDet) GdeDAOFactory.getDecJurDetDAO().getByCuentaPeriodoAnioyActividad(cuenta, periodo, anio, idActividad);
	}
	
	// Getters y setters
	
	


	public DecJur getDecJur() {
		return decJur;
	}


	public void setDecJur(DecJur decJur) {
		this.decJur = decJur;
	}


	public String getDetalle() {
		return detalle;
	}


	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}


	public RecConADec getRecConADec() {
		return recConADec;
	}


	public void setRecConADec(RecConADec recConADec) {
		this.recConADec = recConADec;
	}


	public Double getBase() {
		return base;
	}


	public void setBase(Double base) {
		this.base = base;
	}


	public Double getMultiplo() {
		return multiplo;
	}


	public void setMultiplo(Double multiplo) {
		this.multiplo = multiplo;
	}


	public Double getSubtotal1() {
		return subtotal1;
	}


	public void setSubtotal1(Double subtotal1) {
		this.subtotal1 = subtotal1;
	}


	public Double getCanUni() {
		return canUni;
	}


	public void setCanUni(Double canUni) {
		this.canUni = canUni;
	}


	public String getUnidad() {
		return unidad;
	}


	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}


	public RecTipUni getRecTipUni() {
		return recTipUni;
	}


	public void setRecTipUni(RecTipUni recTipUni) {
		this.recTipUni = recTipUni;
	}


	public RecConADec getTipoUnidad() {
		return tipoUnidad;
	}


	public void setTipoUnidad(RecConADec tipoUnidad) {
		this.tipoUnidad = tipoUnidad;
	}


	public String getDesTipoUnidad() {
		return desTipoUnidad;
	}


	public void setDesTipoUnidad(String desTipoUnidad) {
		this.desTipoUnidad = desTipoUnidad;
	}


	public Double getValRef() {
		return valRef;
	}


	public void setValRef(Double valRef) {
		this.valRef = valRef;
	}


	public Double getValUnidad() {
		return valUnidad;
	}


	public void setValUnidad(Double valUnidad) {
		this.valUnidad = valUnidad;
	}


	public Double getSubtotal2() {
		return subtotal2;
	}


	public void setSubtotal2(Double subtotal2) {
		this.subtotal2 = subtotal2;
	}


	public Double getMinimo() {
		return minimo;
	}


	public void setMinimo(Double minimo) {
		this.minimo = minimo;
	}


	public Double getTotalConcepto() {
		return totalConcepto;
	}


	public void setTotalConcepto(Double totalConcepto) {
		this.totalConcepto = totalConcepto;
	}

	public Integer getAlcanceEtur() {
		return alcanceEtur;
	}

	public void setAlcanceEtur(Integer alcanceEtur) {
		this.alcanceEtur = alcanceEtur;
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
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (recConADec == null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DECJURDET_ACTIVIDAD_LABEL);
			return false;
		}
		
		//Valido que el concepto no haya sido declarada en la declaracion
		if (!recConADec.equals(getRecConADec()) && GdeDAOFactory.getDecJurDetDAO().getRecConADecExistente(recConADec, getDecJur())){
			this.addRecoverableError(GdeError.DECJURDET_RECCONADEC_YAEXISTE);
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio

	
	
	//<#MetodosBeanDetalle#>
}
