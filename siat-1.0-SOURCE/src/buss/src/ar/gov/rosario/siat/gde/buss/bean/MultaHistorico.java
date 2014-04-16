//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Multa
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_multaHistorico")
public class MultaHistorico extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idMulta")
	private Multa multa;
		
	@Column(name = "periodoDesde")
	private Integer periodoDesde;
	
	@Column(name = "anioDesde")
	private Integer anioDesde;
	
	@Column(name="periodoHasta")
	private Integer periodoHasta;
	
	@Column(name="anioHasta")
	private Integer anioHasta;
	
	@Column(name="porcentaje")
	private Double porcentaje;
	
	@Column(name="importeTotal")
	private Double importeTotal;
	
	@Column(name = "observacion")
	private String observacion;
	
	@Column(name = "fecha")
	private Date fecha;
	
	@Column(name = "idCaso")
	private String idCaso;
	
	
	// Constructores
	public MultaHistorico(){
		super();
	}
	
	public MultaHistorico(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static MultaHistorico getById(Long id) {
		return (MultaHistorico) GdeDAOFactory.getMultaHistoricoDAO().getById(id);
	}
	
	public static MultaHistorico getByIdNull(Long id) {
		return (MultaHistorico) GdeDAOFactory.getMultaHistoricoDAO().getByIdNull(id);
	}
	
	public static List<MultaHistorico> getList() {
		return (ArrayList<MultaHistorico>) GdeDAOFactory.getMultaHistoricoDAO().getList();
	}
	
	public static List<MultaHistorico> getListActivos() {			
		return (ArrayList<MultaHistorico>) GdeDAOFactory.getMultaHistoricoDAO().getListActiva();
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

	// Getters y setters
	
	public Multa getMulta() {
		return multa;
	}

	public void setMulta(Multa multa) {
		this.multa = multa;
	}

	public Integer getPeriodoDesde() {
		return periodoDesde;
	}

	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde = periodoDesde;
	}

	public Integer getAnioDesde() {
		return anioDesde;
	}

	public void setAnioDesde(Integer anioDesde) {
		this.anioDesde = anioDesde;
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}

	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
	}

	public Integer getAnioHasta() {
		return anioHasta;
	}

	public void setAnioHasta(Integer anioHasta) {
		this.anioHasta = anioHasta;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}

	public Double getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(Double importeTotal) {
		this.importeTotal = importeTotal;
	}

	public String getIdCaso() {
		return idCaso;
	}

	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
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
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el Multa. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getMultaDAO().update(this);
	}

	/**
	 * Desactiva el Multa. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getMultaDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Multa
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Multa
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
