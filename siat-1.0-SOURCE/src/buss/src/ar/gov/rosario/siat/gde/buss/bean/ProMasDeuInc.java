//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a la Deuda Incluida del Envio de Deuda a Judiciales
 * 
 * @author tecso
 */

@Entity
@Table(name = "gde_proMasDeuInc")
public class ProMasDeuInc extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "proMasDeuInc";

	@ManyToOne(optional=false) 
    @JoinColumn(name="idProcesoMasivo") 
	private ProcesoMasivo procesoMasivo;

	@Column(name = "idDeuda")
	private Long idDeuda;              // no nulo
	
	@Column(name = "saldoActualizado") // no nulo
	private Double saldoActualizado;	
	
	@Column(name = "saldoHistorico") // no nulo
	private Double saldoHistorico;
	
	@ManyToOne(optional=true) 
    @JoinColumn(name="idProcurador") 
	private Procurador procurador;
	
	@Column(name = "incVueltaAtras") // nuleable
	private Integer incVueltaAtras;

	@Column(name = "obsMotNoVueAtr")
	private String obsMotNoVueAtr;
	
	@Column(name = "desTitulPrincipal") // nuleable varchar(255)
	private String desTitularPrincipal;
	 
	@Column(name = "procesada")
	private Integer procesada;

	@ManyToOne(optional=true) 
    @JoinColumn(name="idTipoSelAlmDet") 
	private TipoSelAlm tipoSelAlmDet;   

	// Constructores
	public ProMasDeuInc(){
		super();
	}
	

	// Getters y Setters
	public Long getIdDeuda() {
		return idDeuda;
	}
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}
	public ProcesoMasivo getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivo procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}
	public Integer getIncVueltaAtras() {
		return incVueltaAtras;
	}
	public void setIncVueltaAtras(Integer incVueltaAtras) {
		this.incVueltaAtras = incVueltaAtras;
	}
	public String getObsMotNoVueAtr() {
		return obsMotNoVueAtr;
	}
	public void setObsMotNoVueAtr(String obsMotNoVueAtr) {
		this.obsMotNoVueAtr = obsMotNoVueAtr;
	}
	public Procurador getProcurador() {
		return procurador;
	}
	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}
	public Double getSaldoActualizado() {
		return saldoActualizado;
	}
	public void setSaldoActualizado(Double saldoActualizado) {
		this.saldoActualizado = saldoActualizado;
	}
	public Double getSaldoHistorico() {
		return saldoHistorico;
	}
	public void setSaldoHistorico(Double saldoHistorico) {
		this.saldoHistorico = saldoHistorico;
	}
	public String getDesTitularPrincipal() {
		return desTitularPrincipal;
	}
	public void setDesTitularPrincipal(String desTitularPrincipal) {
		this.desTitularPrincipal = desTitularPrincipal;
	}
	public Integer getProcesada() {
		return procesada;
	}
	public void setProcesada(Integer procesada) {
		this.procesada = procesada;
	}
	public TipoSelAlm getTipoSelAlmDet() {
		return tipoSelAlmDet;
	}
	public void setTipoSelAlmDet(TipoSelAlm tipoSelAlmDet) {
		this.tipoSelAlmDet = tipoSelAlmDet;
	}


	// Metodos de Clase
	public static ProMasDeuInc getById(Long id) {
		return (ProMasDeuInc) GdeDAOFactory.getProMasDeuIncDAO().getById(id);  
	}
	
	public static ProMasDeuInc getByIdNull(Long id) {
		return (ProMasDeuInc) GdeDAOFactory.getProMasDeuIncDAO().getByIdNull(id);
	}
	
	public static List<ProMasDeuInc> getList() {
		return (ArrayList<ProMasDeuInc>) GdeDAOFactory.getProMasDeuIncDAO().getList();
	}
	
	public static List<ProMasDeuInc> getListActivos() {			
		return (ArrayList<ProMasDeuInc>) GdeDAOFactory.getProMasDeuIncDAO().getListActiva();
	}


	// Metodos de Instancia

	//Validaciones
	/**
	 * Valida la creacion
	 * @author 
	 */
	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}
		
		//Validaciones de Negocio
		
		return true;
	}

	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
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

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;
	}
	
	// Metodos de negocio

	/**
	 * Obtiene la deuda administrativa del ProMasDeuInc
	 * @return DeudaAdmin
	 */
	public DeudaAdmin getDeudaAdmin(){
		return (DeudaAdmin) GdeDAOFactory.getDeudaAdminDAO().getByIdNull(this.getIdDeuda());
	}
	
	public DeudaJudicial getDeudaJudicial(){
		return (DeudaJudicial) GdeDAOFactory.getDeudaJudicialDAO().getByIdNull(this.getIdDeuda());
	}
	
	public Deuda getDeuda(){
		return Deuda.getById(this.getIdDeuda());
	}

	public ConvenioCuota getConvenioCuota(){
		return (ConvenioCuota) GdeDAOFactory.getConvenioCuotaDAO().getByIdNull(this.getIdDeuda());
	}
}
