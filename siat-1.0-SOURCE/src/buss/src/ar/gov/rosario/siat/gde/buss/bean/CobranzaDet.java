//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.gov.rosario.siat.ef.buss.bean.DetAjuDet;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Gestion de Cobranza
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_cobranzaDet")
public class CobranzaDet extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idCobranza")
	private Cobranza cobranza;
	
	@Column(name="fecha")
	private Date fecha;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idCuenta")
	private Cuenta cuenta;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idDetAjuDet")
	private DetAjuDet detAjuDet;
	
	@Column(name="periodo")
	private Integer periodo;
	
	@Column(name="anio")
	private Integer anio;
	
	@Column(name="importeInicial")
	private Double importeInicial;
	
	@Column(name="ajuste")
	private Double ajuste;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idEstadoAjuste")
	private EstadoAjuste estadoAjuste;
	
	@Column(name="idDeuda")
	private Long idDeuda;
	
	@Transient
	private Double pagos;
	
	@Transient
	private Double enConVig;
	
	@Transient
	private Double enConCad;
	
	
	
	

	//<#Propiedades#>
	
	// Constructores
	public CobranzaDet(){
		super();
	}
	
	
	// Metodos de Clase
	public static GesCob getById(Long id) {
		return (GesCob) GdeDAOFactory.getGesCobDAO().getById(id);
	}
	
	public static GesCob getByIdNull(Long id) {
		return (GesCob) GdeDAOFactory.getGesCobDAO().getByIdNull(id);
	}
	
	public static List<GesCob> getList() {
		return (List<GesCob>) GdeDAOFactory.getGesCobDAO().getList();
	}
	
	public static List<GesCob> getListActivos() {			
		return (List<GesCob>) GdeDAOFactory.getGesCobDAO().getListActiva();
	}
	
	public static CobranzaDet getByDetAjuDetAndCobranza(DetAjuDet detAjuDet,Cobranza cobranza){
		return GdeDAOFactory.getCobranzaDetDAO().getByDetAjuDetAndCobranza(detAjuDet, cobranza);
	}
	
	
	// Getters y setters

	public Cobranza getCobranza() {
		return cobranza;
	}


	public void setCobranza(Cobranza cobranza) {
		this.cobranza = cobranza;
	}


	public Cuenta getCuenta() {
		return cuenta;
	}


	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
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


	public Double getImporteInicial() {
		return importeInicial;
	}


	public void setImporteInicial(Double importeInicial) {
		this.importeInicial = importeInicial;
	}


	public Double getAjuste() {
		return ajuste;
	}


	public void setAjuste(Double ajuste) {
		this.ajuste = ajuste;
	}


	public EstadoAjuste getEstadoAjuste() {
		return estadoAjuste;
	}


	public void setEstadoAjuste(EstadoAjuste estadoAjuste) {
		this.estadoAjuste = estadoAjuste;
	}


	public Long getIdDeuda() {
		return idDeuda;
	}


	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}


	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public Double getEnConVig() {
		return enConVig;
	}


	public void setEnConVig(Double enConVig) {
		this.enConVig = enConVig;
	}


	public Double getEnConCad() {
		return enConCad;
	}


	public void setEnConCad(Double enConCad) {
		this.enConCad = enConCad;
	}


	public Double getPagos() {
		return pagos;
	}


	public void setPagos(Double pagos) {
		this.pagos = pagos;
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
		
		/*/	Validaciones        
		if (StringUtil.isNullOrEmpty(getDesEstadoConCuo())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.ESTADOCONCUO_DESESTADOCONCUO);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codEstadoConCuo");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, GdeError.ESTADOCONCUO_CODESTADOCONCUO);			
		}*/
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el EstadoConCuo. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getEstadoAjusteDAO().update(this);
	}

	/**
	 * Desactiva el EstadoConCuo. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getEstadoAjusteDAO().update(this);
	}
	
	/**
	 * Valida la activacion del EstadoConCuo
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del EstadoConCuo
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//<#MetodosBeanDetalle#>
}
