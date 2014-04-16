//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.Repartidor;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a ProDet - Detalle de Procedimiento
 * 
 * @author tecso
 */
@Entity
@Table(name = "cyq_proDet")
public class ProDet extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="IdProcedimiento") 
	private Procedimiento procedimiento;
	
	@Column(name = "idDeuda")
	private Long idDeuda;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idCuenta") 
	private Cuenta cuenta;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idViaDeuda") 
	private ViaDeuda viaDeuda;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idEstadoDeuda") 
	private EstadoDeuda estadoDeuda;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idConvenio") 
	private Convenio convenio;
	
	@Column(name = "importe")
	private Double importe;
	
	@Column(name = "saldo")
	private Double saldo;
	
	@Column(name = "actualizacionCyq")
	private Double actualizacionCyq;

	@Column(name = "codRefPag") 
	private Long codRefPag;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idRecClaDeu") 
	private RecClaDeu recClaDeu;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idEmision") 
	private Emision emision;

	@Column(name = "anio")
	private Long anio;

	@Column(name = "periodo")
	private Long periodo;

	@Column(name = "fechaEmision")
	private Date fechaEmision;

	@Column(name = "fechaVencimiento")
	private Date fechaVencimiento;

	@Column(name = "importeBruto")
	private Double importeBruto;

	@Column(name = "actualizacion")
	private Double actualizacion;
	
	@Column(name = "strConceptosProp")
	private String strConceptosProp;

	@Column(name = "strEstadoDeuda")
	private String strEstadoDeuda;

	@Column(name = "fechaPago")
	private Date fechaPago;

	@Column(name = "estaImpresa")
	private Integer estaImpresa;

	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idRepartidor") 
	private Repartidor repartidor;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idProcurador") 
	private Procurador procurador;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@Column(name = "obsMotNoPre")
	private String obsMotNoPre;

	@Column(name = "reclamada")
	private Integer reclamada;

	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idSistema") 
	private Sistema sistema;

	@Column(name = "resto")
	private Long resto;

	@Column(name = "atrAseVal")
	private String atrAseVal;
	
	
	// Constructores
	public ProDet(){
		super();
		// Seteo de valores default			
	}
	
	public ProDet(Long id){
		super();
		setId(id);
	}
	
	/**
	 * Constructor para migracion drei judicial
	 * @param deuda
	 */
	public ProDet(Deuda deuda){
		super();
		
		this.procedimiento = new  Procedimiento();
		this.procedimiento.setId(deuda.getIdProcedimientoCyQ());
		
		this.setIdDeuda(deuda.getId());
		this.setCuenta(deuda.getCuenta());
		this.setViaDeuda(deuda.getViaDeuda());
		this.setEstadoDeuda(deuda.getEstadoDeuda());
		this.setConvenio(deuda.getConvenio());
		this.setImporte(deuda.getImporte());
		this.setSaldo(deuda.getSaldo());
		this.setActualizacionCyq(deuda.getActualizacionCyQ());
		this.setCodRefPag(deuda.getCodRefPag());
		this.setRecClaDeu(deuda.getRecClaDeu());
		this.setEmision(deuda.getEmision());
		this.setAnio(deuda.getAnio());
		this.setPeriodo(deuda.getPeriodo());
		this.setFechaEmision(deuda.getFechaEmision());
		this.setFechaVencimiento(deuda.getFechaVencimiento());
		this.setImporteBruto(deuda.getImporteBruto());
		this.setActualizacion(deuda.getActualizacion());
		this.setStrConceptosProp(deuda.getStrConceptosProp());
		this.setStrEstadoDeuda(deuda.getStrEstadoDeuda());
		this.setFechaPago(deuda.getFechaPago());
		this.setEstaImpresa(deuda.getEstaImpresa());
		this.setRepartidor(deuda.getRepartidor());
		this.setProcurador(deuda.getProcurador());
		this.setRecurso(deuda.getRecurso());
		this.setObsMotNoPre(deuda.getObsMotNoPre());
		this.setReclamada(deuda.getReclamada());
		this.setSistema(deuda.getSistema());
		this.setResto(deuda.getResto());
		this.setAtrAseVal(deuda.getAtrAseVal());
		
	}
	
	// Metodos de Clase
	public static ProDet getById(Long id) {
		return (ProDet) CyqDAOFactory.getProDetDAO().getById(id);
	}
	
	public static ProDet getByIdNull(Long id) {
		return (ProDet) CyqDAOFactory.getProDetDAO().getByIdNull(id);
	}
		
	public static ProDet getByIdProcedimientoYDeuda(Long idProcedimiento, Long idDeuda) {
		return (ProDet) CyqDAOFactory.getProDetDAO().getByIdProcedimientoYDeuda(idProcedimiento, idDeuda);
	}
	
	public static List<ProDet> getList() {
		return (ArrayList<ProDet>) CyqDAOFactory.getProDetDAO().getList();
	}
	
	public static List<ProDet> getListActivos() {			
		return (ArrayList<ProDet>) CyqDAOFactory.getProDetDAO().getListActiva();
	}
	
	
	// Getters y setters
	public Procedimiento getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(Procedimiento procedimiento) {
		this.procedimiento = procedimiento;
	}

	public Long getIdDeuda() {
		return idDeuda;
	}
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}
	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public ViaDeuda getViaDeuda() {
		return viaDeuda;
	}
	public void setViaDeuda(ViaDeuda viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

	public EstadoDeuda getEstadoDeuda() {
		return estadoDeuda;
	}
	public void setEstadoDeuda(EstadoDeuda estadoDeuda) {
		this.estadoDeuda = estadoDeuda;
	}

	public Convenio getConvenio() {
		return convenio;
	}
	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public Double getActualizacionCyq() {
		return actualizacionCyq;
	}
	public void setActualizacionCyq(Double actualizacionCyq) {
		this.actualizacionCyq = actualizacionCyq;
	}
	
	public Long getCodRefPag() {
		return codRefPag;
	}

	public void setCodRefPag(Long codRefPag) {
		this.codRefPag = codRefPag;
	}

	public RecClaDeu getRecClaDeu() {
		return recClaDeu;
	}

	public void setRecClaDeu(RecClaDeu recClaDeu) {
		this.recClaDeu = recClaDeu;
	}

	public Emision getEmision() {
		return emision;
	}

	public void setEmision(Emision emision) {
		this.emision = emision;
	}

	public Long getAnio() {
		return anio;
	}

	public void setAnio(Long anio) {
		this.anio = anio;
	}

	public Long getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Long periodo) {
		this.periodo = periodo;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Double getImporteBruto() {
		return importeBruto;
	}

	public void setImporteBruto(Double importeBruto) {
		this.importeBruto = importeBruto;
	}

	public Double getActualizacion() {
		return actualizacion;
	}

	public void setActualizacion(Double actualizacion) {
		this.actualizacion = actualizacion;
	}

	public String getStrConceptosProp() {
		return strConceptosProp;
	}

	public void setStrConceptosProp(String strConceptosProp) {
		this.strConceptosProp = strConceptosProp;
	}

	public String getStrEstadoDeuda() {
		return strEstadoDeuda;
	}

	public void setStrEstadoDeuda(String strEstadoDeuda) {
		this.strEstadoDeuda = strEstadoDeuda;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public Integer getEstaImpresa() {
		return estaImpresa;
	}

	public void setEstaImpresa(Integer estaImpresa) {
		this.estaImpresa = estaImpresa;
	}

	public Repartidor getRepartidor() {
		return repartidor;
	}

	public void setRepartidor(Repartidor repartidor) {
		this.repartidor = repartidor;
	}

	public Procurador getProcurador() {
		return procurador;
	}

	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public String getObsMotNoPre() {
		return obsMotNoPre;
	}

	public void setObsMotNoPre(String obsMotNoPre) {
		this.obsMotNoPre = obsMotNoPre;
	}

	public Integer getReclamada() {
		return reclamada;
	}

	public void setReclamada(Integer reclamada) {
		this.reclamada = reclamada;
	}

	public Sistema getSistema() {
		return sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public Long getResto() {
		return resto;
	}

	public void setResto(Long resto) {
		this.resto = resto;
	}

	public String getAtrAseVal() {
		return atrAseVal;
	}

	public void setAtrAseVal(String atrAseVal) {
		this.atrAseVal = atrAseVal;
	}

	
    /**
     * Devuelve: 
     *   Abreviatura de Clasificacion Deuda + Periodo + "/" + Anio
     * 
     * @author Cristian
     * @return
     */
    public String getStrPeriodo(){
    	
    	String desRecClaDeu = "";
    	    	
    	if (getRecClaDeu() != null){
    		desRecClaDeu = getRecClaDeu().getAbrClaDeu();
    	}
    	
    	if (!StringUtil.isNullOrEmpty(getStrEstadoDeuda())){
    		return desRecClaDeu + " " + getStrEstadoDeuda();
    	} else {
    		return desRecClaDeu + " " + getPeriodo() + "/" + getAnio();
    	}
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
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el ProDet. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		CyqDAOFactory.getProDetDAO().update(this);
	}

	/**
	 * Desactiva el ProDet. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		CyqDAOFactory.getProDetDAO().update(this);
	}
	
	/**
	 * Valida la activacion del ProDet
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del ProDet
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	//#MetodosBeanDetalle#>
}
