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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.cyq.buss.bean.DeudaPrivilegio;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a ConvenioDeuda
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_convenioDeuda")
public class ConvenioDeuda extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false) 
	@JoinColumn(name="idConvenio") 
	private Convenio convenio;
	
	@Column(name = "idDeuda")
	private Long idDeuda;
	
	@Column(name = "capitalOriginal")
	private Double capitalOriginal;
	
	@Column(name = "capitalEnPlan")
	private Double capitalEnPlan;
	
	@Column(name = "actualizacion")
	private Double actualizacion;
	
	@Column(name = "actEnPlan")
	private Double actEnPlan;
	
	@Column(name = "fechaPago")
	private Date fechaPago;
	
	@Column(name = "fecVenDeuda")
	private Date fecVenDeuda;
	
	@Column(name = "totalEnPlan")
	private Double totalEnPlan;
	
	@Column(name = "saldoEnPlan")
	private Double saldoEnPlan;

	@Column(name = "esDeudaPriv")
	private Integer esDeudaPriv = 0;
	
	//<#Propiedades#>
	
	// Constructores
	public ConvenioDeuda(){
		super();
	}
	
	public ConvenioDeuda(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ConvenioDeuda getById(Long id) {
		return (ConvenioDeuda) GdeDAOFactory.getConvenioDeudaDAO().getById(id);
	}
	
	public static ConvenioDeuda getByIdNull(Long id) {
		return (ConvenioDeuda) GdeDAOFactory.getConvenioDeudaDAO().getByIdNull(id);
	}
	
	/**
	 *  Obtiene el registro de ConvenioDeuda con la Deuda a Cancelar. Buscando los registros para el convenio
	 *  pasado y con SaldoEnPlan mayor que cero ordenados por fecha de vencimiento de la deuda. Luego elije el que
	 *  contiene la deuda mas antigua.
	 * 
	 * @param convenio
	 * @return convenioDeuda
	 * @throws Exception
	 */
	public static ConvenioDeuda getDeudaACancelarByConvenioYAse(Convenio convenio, Asentamiento asentamiento) throws Exception {
		return (ConvenioDeuda) GdeDAOFactory.getConvenioDeudaDAO().getDeudaACancelarByConvenioYAse(convenio, asentamiento);
	}
	
	public static List<ConvenioDeuda> getList() {
		return (ArrayList<ConvenioDeuda>) GdeDAOFactory.getConvenioDeudaDAO().getList();
	}
	
	public static List<ConvenioDeuda> getListActivos() {			
		return (ArrayList<ConvenioDeuda>) GdeDAOFactory.getConvenioDeudaDAO().getListActiva();
	}
	
	/**
	 *  Obtiene la lista de ConvenioDeudas para el Convenio indicado y que tengan saldo > 0.
	 *  (Se ordena de forma descendente por fecha de vencimieto para usarla en una lista LIFO)
	 *  
	 * @param convenio
	 * @return listConvenioDeuda
	 * @throws Exception
	 */
	public static List<ConvenioDeuda> getListWithSaldo(Convenio convenio) throws Exception {			
		return (ArrayList<ConvenioDeuda>) GdeDAOFactory.getConvenioDeudaDAO().getListWithSaldo(convenio);
	}
	
	// Getters y setters
	public Double getActEnPlan() {
		return actEnPlan;
	}

	public void setActEnPlan(Double actEnPlan) {
		this.actEnPlan = actEnPlan;
	}

	public Double getActualizacion() {
		return actualizacion;
	}

	public void setActualizacion(Double actualizacion) {
		this.actualizacion = actualizacion;
	}

	public Double getCapitalEnPlan() {
		return capitalEnPlan;
	}

	public void setCapitalEnPlan(Double capitalEnPlan) {
		this.capitalEnPlan = capitalEnPlan;
	}

	public Double getCapitalOriginal() {
		return capitalOriginal;
	}

	public void setCapitalOriginal(Double capitalOriginal) {
		this.capitalOriginal = capitalOriginal;
	}

	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}
	
	public Long getIdDeuda() {
		return idDeuda;
	}

	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}

	public Integer getEsDeudaPriv() {
		return esDeudaPriv;
	}
	public void setEsDeudaPriv(Integer esDeudaPriv) {
		this.esDeudaPriv = esDeudaPriv;
	}

	/**
	 * 
	 * Este metodo Devuelve la deuda que corresponda segun la via.
	 * 
	 * @author Cristian
	 * @return
	 */
	public Deuda getDeuda() {
		return Deuda.getById(getIdDeuda());
	}
	
	public DeudaPrivilegio getDeudaPrivilegio(){
		return DeudaPrivilegio.getById(getIdDeuda());
	}
	
	/**
	 * Es el getById de deuda pero primero busca en judicial y cancelada
	 * @return
	 */
	public Deuda getDeudaForLiqCom() {
		return Deuda.getByIdFirstJud(getIdDeuda());
	}
	
	public Deuda getDeudaFirstJud() {
		return Deuda.getByIdFirstJud(getIdDeuda());
	}
	
	public Deuda getDeudaForSalPorCad(){
		return Deuda.getByIdForSalPorCad(getIdDeuda());
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}
	
	public Date getFecVenDeuda() {
		return fecVenDeuda;
	}

	public void setFecVenDeuda(Date fecVenDeuda) {
		this.fecVenDeuda = fecVenDeuda;
	}

	public Double getSaldoEnPlan() {
		return saldoEnPlan;
	}

	public void setSaldoEnPlan(Double saldoEnPlan) {
		this.saldoEnPlan = saldoEnPlan;
	}

	public Double getTotalEnPlan() {
		return totalEnPlan;
	}

	public void setTotalEnPlan(Double totalEnPlan) {
		this.totalEnPlan = totalEnPlan;
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
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el ConvenioDeuda. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getConvenioDeudaDAO().update(this);
	}

	/**
	 * Desactiva el ConvenioDeuda. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getConvenioDeudaDAO().update(this);
	}
	
	/**
	 * Valida la activacion del ConvenioDeuda
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del ConvenioDeuda
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//<#MetodosBeanDetalle#>
	
	public Double getTotal() {
		return this.getCapitalOriginal() + this.getActualizacion();
	}
	
	public Double getImporteIndexEnvio() throws Exception{
		if(DateUtil.isDateAfter(this.getConvenio().getFechaFor(), DateUtil.getDate("12/10/2008", DateUtil.ddSMMSYYYY_MASK)))
			return 0D;
		else
			return this.getDeuda().getImporteIndexEnvio();
	}
}
