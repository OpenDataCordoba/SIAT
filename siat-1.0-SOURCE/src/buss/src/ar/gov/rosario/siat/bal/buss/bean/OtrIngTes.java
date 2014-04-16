//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

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
import javax.persistence.Transient;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean que representa los Otros Ingresos de Tesoreria
 * 
 * @author Tecso
 *
 */
@Entity
@Table(name = "bal_otrIngTes")
public class OtrIngTes extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final String COD_FRM_RECIBO_OTRINGTES="RECIBO_OTRINGTES";
	
	@Column(name = "fechaOtrIngTes")
	private Date fechaOtrIngTes;
	
	@Column(name = "fechaAlta")
	private Date fechaAlta;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idRecurso") 
	private Recurso recurso;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idAreaOrigen") 
	private Area areaOrigen;
	
	@Column(name = "observaciones")
	private String observaciones;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idCueBanOrigen") 
	private CuentaBanco cueBanOrigen;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idEstOtrIngTes") 
	private EstOtrIngTes estOtrIngTes;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "importe")
	private Double importe;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idFolio") 
	private Folio folio;

	@OneToMany(mappedBy="otrIngTes")
	@JoinColumn(name="idOtrIngTes")
	private List<OtrIngTesRecCon> listOtrIngTesRecCon = new ArrayList<OtrIngTesRecCon>();

	@OneToMany(mappedBy="otrIngTes")
	@JoinColumn(name="idOtrIngTes")
	private List<OtrIngTesPar> listOtrIngTesPar = new ArrayList<OtrIngTesPar>();

	// Flags
	@Transient
	private boolean paramIncluido = false;
	
	// Constructores 
	public OtrIngTes(){
		super();
	}

	// Getters y Setters
	public Area getAreaOrigen() {
		return areaOrigen;
	}
	public void setAreaOrigen(Area areaOrigen) {
		this.areaOrigen = areaOrigen;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public EstOtrIngTes getEstOtrIngTes() {
		return estOtrIngTes;
	}
	public void setEstOtrIngTes(EstOtrIngTes estOtrIngTes) {
		this.estOtrIngTes = estOtrIngTes;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public Date getFechaOtrIngTes() {
		return fechaOtrIngTes;
	}
	public void setFechaOtrIngTes(Date fechaOtrIngTes) {
		this.fechaOtrIngTes = fechaOtrIngTes;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}
	public CuentaBanco getCueBanOrigen() {
		return cueBanOrigen;
	}
	public void setCueBanOrigen(CuentaBanco cueBanOrigen) {
		this.cueBanOrigen = cueBanOrigen;
	}
	public List<OtrIngTesPar> getListOtrIngTesPar() {
		return listOtrIngTesPar;
	}
	public void setListOtrIngTesPar(List<OtrIngTesPar> listOtrIngTesPar) {
		this.listOtrIngTesPar = listOtrIngTesPar;
	}
	public List<OtrIngTesRecCon> getListOtrIngTesRecCon() {
		return listOtrIngTesRecCon;
	}
	public void setListOtrIngTesRecCon(List<OtrIngTesRecCon> listOtrIngTesRecCon) {
		this.listOtrIngTesRecCon = listOtrIngTesRecCon;
	}
	public boolean isParamIncluido() {
		return paramIncluido;
	}
	public void setParamIncluido(boolean paramIncluido) {
		this.paramIncluido = paramIncluido;
	}
	public Folio getFolio() {
		return folio;
	}
	public void setFolio(Folio folio) {
		this.folio = folio;
	}

	// Metodos de clase	
	public static OtrIngTes getById(Long id) {
		return (OtrIngTes) BalDAOFactory.getOtrIngTesDAO().getById(id);
	}
	
	public static OtrIngTes getByIdNull(Long id) {
		return (OtrIngTes) BalDAOFactory.getOtrIngTesDAO().getByIdNull(id);
	}
	
	public static List<OtrIngTes> getList() {
		return (ArrayList<OtrIngTes>) BalDAOFactory.getOtrIngTesDAO().getList();
	}
	
	public static List<OtrIngTes> getListActivos() {			
		return (ArrayList<OtrIngTes>) BalDAOFactory.getOtrIngTesDAO().getListActiva();
	}
	
		
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();

		
		if (hasError()) {
			return false;
		}
		return !hasError();
	}
	
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
				
		if (hasError()) {
			return false;
		}
		return !hasError();
	}

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
		if(StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.OTRINGTES_DESCRIPCION);
		}
		if(getRecurso()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.OTRINGTES_RECURSO);
		}
		if(getAreaOrigen()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.OTRINGTES_AREAORIGEN);
		}
		if(getEstOtrIngTes()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.OTRINGTES_ESTOTRINGTES);
		}
		if(getFechaOtrIngTes()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.OTRINGTES_FECHAOTRINGTES);
		}
		if(getFechaAlta()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.OTRINGTES_FECHAALTA);
		}
		if(getImporte()==null || NumberUtil.isDoubleEqualToDouble(getImporte(), 0D, 0.01)){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.OTRINGTES_IMPORTE);
		}
		
		if (hasError()) {
			return false;
		}

		return !hasError();
	}
	
	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		if (GenericDAO.hasReference(this, OtrIngTesPar.class, "otrIngTes")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.OTRINGTES_LABEL , BalError.OTRINGTESPAR_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
	// Administracion de OtrIngTesRecCon
	public OtrIngTesRecCon createOtrIngTesRecCon(OtrIngTesRecCon otrIngTesRecCon) throws Exception {
		
		// Validaciones de negocio
		if (!otrIngTesRecCon.validateCreate()) {
			return otrIngTesRecCon;
		}

		BalDAOFactory.getOtrIngTesRecConDAO().update(otrIngTesRecCon);
		
		return otrIngTesRecCon;
	}	

	public OtrIngTesRecCon updateOtrIngTesRecCon(OtrIngTesRecCon otrIngTesRecCon) throws Exception {
		
		// Validaciones de negocio
		if (!otrIngTesRecCon.validateUpdate()) {
			return otrIngTesRecCon;
		}

		BalDAOFactory.getOtrIngTesRecConDAO().update(otrIngTesRecCon);
		
		return otrIngTesRecCon;
	}	

	public OtrIngTesRecCon deleteOtrIngTesRecCon(OtrIngTesRecCon otrIngTesRecCon) throws Exception {
		
		// Validaciones de negocio
		if (!otrIngTesRecCon.validateDelete()) {
			return otrIngTesRecCon;
		}
				
		BalDAOFactory.getOtrIngTesRecConDAO().delete(otrIngTesRecCon);
		
		return otrIngTesRecCon;
	}	

	
	// Administracion de OtrIngTesPar
	public OtrIngTesPar createOtrIngTesPar(OtrIngTesPar otrIngTesPar) throws Exception {
		
		// Validaciones de negocio
		if (!otrIngTesPar.validateCreate()) {
			return otrIngTesPar;
		}

		BalDAOFactory.getOtrIngTesParDAO().update(otrIngTesPar);
		
		return otrIngTesPar;
	}	

	public OtrIngTesPar updateOtrIngTesPar(OtrIngTesPar otrIngTesPar) throws Exception {
		
		// Validaciones de negocio
		if (!otrIngTesPar.validateUpdate()) {
			return otrIngTesPar;
		}

		BalDAOFactory.getOtrIngTesParDAO().update(otrIngTesPar);
		
		return otrIngTesPar;
	}	

	public OtrIngTesPar deleteOtrIngTesPar(OtrIngTesPar otrIngTesPar) throws Exception {
		
		// Validaciones de negocio
		if (!otrIngTesPar.validateDelete()) {
			return otrIngTesPar;
		}
		for(OtrIngTesRecCon otrIngTesRecCon: this.getListOtrIngTesRecCon()){
			if(!otrIngTesPar.validateDelete()) {
				this.deleteOtrIngTesRecCon(otrIngTesRecCon);
			}
		}
		BalDAOFactory.getOtrIngTesParDAO().delete(otrIngTesPar);
		
		return otrIngTesPar;
	}	

	/**
	 * Cambiar Estado de Ingreso No Tributario. Guarda el historico de cambio de estado.
	 * @param long idEstOtrIngTes
	 * 
	 */
	public void cambiarEstOtrIngTes(Long idEstOtrIngTes){
		EstOtrIngTes estOtrIngTes = EstOtrIngTes.getById(idEstOtrIngTes);
		EstOtrIngTes estOtrIngTesAnterior = this.getEstOtrIngTes();
		
		this.setEstOtrIngTes(estOtrIngTes);
		
		BalDAOFactory.getOtrIngTesDAO().update(this);
		
		HisEstOtrIngTes hisEstOtrIngTes = new HisEstOtrIngTes();
		
		hisEstOtrIngTes.setOtrIngTes(this);
		hisEstOtrIngTes.setEstOtrIngTes(estOtrIngTes);
		hisEstOtrIngTes.setFecha(new Date());
		hisEstOtrIngTes.setLogCambios("Se cambió del estado "+estOtrIngTesAnterior.getDesEstOtrIngTes()+" al estado "+estOtrIngTes.getDesEstOtrIngTes());
		
		BalDAOFactory.getHisEstOtrIngTesDAO().update(hisEstOtrIngTes);		
	}
	
	/**
	 *  Verifica si tiene el idFolio asignado.
	 * 
	 * @return
	 */
	public boolean estaIncluidoEnFolio(){
		return (this.getFolio() != null);
	}
	
	/** 
	 *  Formateo el valor de importe para el Imprimir Reporte del Search Page.
	 *  
	 * @return
	 */
	public String getImporteForReport(){
		if(importe != null)
			return StringUtil.formatDouble(NumberUtil.truncate(this.importe, SiatParam.DEC_IMPORTE_DB));
		else 
			return "";
	}
	
	
}

