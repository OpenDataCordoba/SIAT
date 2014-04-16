//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a PlanFiscal
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_planfiscal")
public class PlanFiscal extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "desPlanFiscal")
	private String desPlanFiscal;

	@Column(name = "fechaDesde")
	private Date fechaDesde;

	@Column(name = "fechaHasta")
	private Date fechaHasta;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idEstadoPlanFis")
	private EstadoPlanFis estadoPlanFis;

	@Column(name = "numero")
	private String numero;

	@Column(name = "objetivo")
	private String objetivo;

	@Column(name = "fundamentos")
	private String fundamentos;

	@Column(name = "propuestas")
	private String propuestas;

	@Column(name = "metTrab")
	private String metTrab;

	@Column(name = "necesidades")
	private String necesidades;

	@Column(name = "resEsp")
	private String resEsp;

	// <#Propiedades#>

	// Constructores
	public PlanFiscal() {
		super();
		// Seteo de valores default
	}

	public PlanFiscal(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static PlanFiscal getById(Long id) {
		return (PlanFiscal) EfDAOFactory.getPlanFiscalDAO().getById(id);
	}

	public static PlanFiscal getByIdNull(Long id) {
		return (PlanFiscal) EfDAOFactory.getPlanFiscalDAO().getByIdNull(id);
	}

	public static List<PlanFiscal> getList() {
		return (List<PlanFiscal>) EfDAOFactory.getPlanFiscalDAO().getList();
	}

	public static List<PlanFiscal> getListActivos() {
		return (List<PlanFiscal>) EfDAOFactory.getPlanFiscalDAO()
				.getListActiva();
	}

	public static List<PlanFiscal> getListByEstado(Long idEstadoPlanFis){
		return EfDAOFactory.getPlanFiscalDAO().getListByEstado(idEstadoPlanFis);
	}
	
	// Getters y setters
	public String getDesPlanFiscal() {
		return desPlanFiscal;
	}

	public void setDesPlanFiscal(String desPlanFiscal) {
		this.desPlanFiscal = desPlanFiscal;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public EstadoPlanFis getEstadoPlanFis() {
		return estadoPlanFis;
	}

	public void setEstadoPlanFis(EstadoPlanFis estadoPlanFis) {
		this.estadoPlanFis = estadoPlanFis;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

	public String getFundamentos() {
		return fundamentos;
	}

	public void setFundamentos(String fundamentos) {
		this.fundamentos = fundamentos;
	}

	public String getPropuestas() {
		return propuestas;
	}

	public void setPropuestas(String propuestas) {
		this.propuestas = propuestas;
	}

	public String getMetTrab() {
		return metTrab;
	}

	public void setMetTrab(String metTrab) {
		this.metTrab = metTrab;
	}

	public String getNecesidades() {
		return necesidades;
	}

	public void setNecesidades(String necesidades) {
		this.necesidades = necesidades;
	}

	public String getResEsp() {
		return resEsp;
	}

	public void setResEsp(String resEsp) {
		this.resEsp = resEsp;
	}

	// Validaciones
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}

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

	public boolean validateDelete() throws Exception {
		// limpiamos la lista de errores
		clearError();
		
		if(!validate())
			return false;
		
		// Valida si tiene referencia a OpeInv
		if(GenericDAO.hasReference(this, OpeInv.class, "planFiscal")){
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					EfError.PLANFISCAL_LABEL, EfError.OPEINV_LABEL);
		}

		if (hasError()) {
			return false;
		}
		
		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones
		if(fechaDesde==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,EfError.PLANFISCAL_FECHA_DESDE_LABEL);
		}else if(fechaHasta!=null){
			if (DateUtil.isDateAfter(getFechaDesde(), getFechaHasta())){
					addRecoverableError(BaseError.MSG_VALORMENORQUE,
							EfError.PLANFISCAL_FECHA_HASTA_LABEL, EfError.PLANFISCAL_FECHA_DESDE_LABEL);					
				}

		}
		
		if(estadoPlanFis==null || estadoPlanFis.getId().longValue()<=0){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,EfError.PLANFISCAL_ESTADOPLANFIS_LABEL);
		}else if(estadoPlanFis.getId().equals(EstadoPlanFis.ID_EST_CERRADO) && fechaHasta==null){
			// Valida que si cambia a estado CERRADO ingrese una fecha Hasta
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,EfError.PLANFISCAL_FECHA_HASTA_LABEL);
		}
		
		if (StringUtil.isNullOrEmpty(getDesPlanFiscal())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,EfError.PLANFISCAL_DESPLANFISCAL);
		}

		// Valida que el nro sea unico, si ingreso un nro
		if(!StringUtil.isNullOrEmpty(getNumero())){
			UniqueMap uniqueMap = new UniqueMap();
			uniqueMap.addString("numero");
			if (!GenericDAO.checkIsUnique(this, uniqueMap)) {
				addRecoverableError(BaseError.MSG_CAMPO_UNICO,
						EfError.PLANFISCAL_NUMERO_LABEL);			
			}
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el PlanFiscal. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getPlanFiscalDAO().update(this);
	}

	/**
	 * Desactiva el PlanFiscal. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getPlanFiscalDAO().update(this);
	}

	/**
	 * Valida la activacion del PlanFiscal
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
	 * Valida la desactivacion del PlanFiscal
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	// <#MetodosBeanDetalle#>
}
