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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.ef.buss.bean.OrdenControl;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Bean correspondiente a Multa
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_multa")
public class Multa extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idOrdenControl")
	private OrdenControl ordenControl;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idCuenta")
	private Cuenta cuenta;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idTipoMulta")
	private TipoMulta tipoMulta;
	
	@Column(name="idDeuda")
	private Long idDeuda;
	
	@Column(name = "fechaEmision")
	private Date fechaEmision;
	
	@Column(name = "importe")
	private Double importe;
	
	@Column(name = "fechaNotificacion")
	private Date fechaNotificacion;
	
	@Column(name = "observacion")
	private String observacion;
	
	@Column(name = "fechaActualizacion")
	private Date fechaActualizacion;
	
	@Column(name = "fechaVencimiento")
	private Date fechaVencimiento;
	
	@Column(name="idCaso")
	private String idCaso;
	
	@Column(name="fechaResolucion")
	private Date fechaResolucion;
	
	@Column(name="totalAplicado")
	private Double totalAplicado;
	
	@Column(name="importeMultaAnterior")
	private Double importeMultaAnterior;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idDescuentoMulta")
	private DescuentoMulta descuentoMulta;
	
	// Listas de Entidades Relacionadas con multa
	@OneToMany(mappedBy="multa")
	@JoinColumn(name="idMulta")
	private List<MultaDet> listMultaDet= new ArrayList<MultaDet>();
	
	@OneToMany(mappedBy="multa")
	@JoinColumn(name="idMulta")
	private List<MultaHistorico> listMultaHistorico = new ArrayList<MultaHistorico>();

	// Constructores
	public Multa(){
		super();
	}
	
	public Multa(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static Multa getById(Long id) {
		return (Multa) GdeDAOFactory.getMultaDAO().getById(id);
	}
	
	public static Multa getByIdNull(Long id) {
		return (Multa) GdeDAOFactory.getMultaDAO().getByIdNull(id);
	}
	
	public static List<Multa> getList() {
		return (ArrayList<Multa>) GdeDAOFactory.getMultaDAO().getList();
	}
	
	public static List<Multa> getListActivos() {			
		return (ArrayList<Multa>) GdeDAOFactory.getMultaDAO().getListActiva();
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
	
	// Getters y setters
	
	public OrdenControl getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControl ordenControl) {
		this.ordenControl = ordenControl;
	}


	public Cuenta getCuenta() {
		return cuenta;
	}


	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}


	public TipoMulta getTipoMulta() {
		return tipoMulta;
	}


	public void setTipoMulta(TipoMulta tipoMulta) {
		this.tipoMulta = tipoMulta;
	}


	public Long getIdDeuda() {
		return idDeuda;
	}


	public void setIdDeuda(Long iddeuda) {
		this.idDeuda = iddeuda;
	}


	public Date getFechaEmision() {
		return fechaEmision;
	}


	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}


	public Double getImporte() {
		return importe;
	}


	public void setImporte(Double importe) {
		this.importe = importe;
	}


	public Date getFechaNotificacion() {
		return fechaNotificacion;
	}


	public void setFechaNotificacion(Date fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
	}


	public String getObservacion() {
		return observacion;
	}


	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public DescuentoMulta getDescuentoMulta() {
		return descuentoMulta;
	}

	public void setDescuentoMulta(DescuentoMulta descuentoMulta) {
		this.descuentoMulta = descuentoMulta;
	}

	public List<MultaDet> getListMultaDet() {
		return listMultaDet;
	}

	public void setListMultaDet(List<MultaDet> listMultaDet) {
		this.listMultaDet = listMultaDet;
	}

	public String getIdCaso() {
		return idCaso;
	}

	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public Date getFechaResolucion() {
		return fechaResolucion;
	}

	public void setFechaResolucion(Date fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}

	public Double getTotalAplicado() {
		return totalAplicado;
	}

	public void setTotalAplicado(Double totalAplicado) {
		this.totalAplicado = totalAplicado;
	}

	public Double getImporteMultaAnterior() {
		return importeMultaAnterior;
	}

	public void setImporteMultaAnterior(Double importeMultaAnterior) {
		this.importeMultaAnterior = importeMultaAnterior;
	}

	public static List<Multa> getListByCuenta(Long idCuenta){
		return (ArrayList<Multa>) GdeDAOFactory.getMultaDAO().getListByCuenta(idCuenta);
	}
	
	public static List<Multa> getListByCuentaTipoMulta(Long idCuenta,Long idTipoMulta){
		return (ArrayList<Multa>) GdeDAOFactory.getMultaDAO().getListByCuentaTipoMulta(idCuenta,idTipoMulta);
	}
	
	public static List<Multa> getListByOrdenControlTipoMulta(Long idOrdenControl,Long idTipoMulta){
		return (ArrayList<Multa>) GdeDAOFactory.getMultaDAO().getListByOrdenControl(idOrdenControl,idTipoMulta);
	}
	
	public List<MultaHistorico> getListMultaHistorico() {
		return listMultaHistorico;
	}

	public void setListMultaHistorico(List<MultaHistorico> listMultaHistorico) {
		this.listMultaHistorico = listMultaHistorico;
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
	
		
		if(GenericDAO.hasReference(this, MultaDet.class, "multa")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.MULTA_LABEL, GdeError.MULTADET_LABEL );
		}
		/*if(GenericDAO.hasReference(this, CierreComercio.class, "multa")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.CIERRECOMERCIO_LABEL, GdeError.CIERRECOMERCIO_LABEL );
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if(tipoMulta.getEsImporteManual()==SiNo.NO.getId() && getFechaActualizacion()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.MULTA_FECHAACTUALIZACION);
		}
		
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
