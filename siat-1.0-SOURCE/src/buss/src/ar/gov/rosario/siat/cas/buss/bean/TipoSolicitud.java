//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.dao.CasDAOFactory;
import ar.gov.rosario.siat.cas.iface.util.CasError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a TipoSolicitud
 * 
 * @author tecso
 */
@Entity
@Table(name = "cas_tiposolicitud")
public class TipoSolicitud extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final String COD_ANULACIONES_PERIODOS_DEUDA = "1";
	public static final String COD_RETROACTIVOS_PERIODOS_DEUDA = "2";
	public static final String COD_CONDONACIONES_DE_PERIODOS_DEUDA = "3";
	public static final String COD_LIBERACIONES_PERIODOS_DEUDA = "4";
	public static final String COD_VERIFICAR_EXENCION_CUENTA = "5";
	public static final String COD_ASIGNACION_BROCHE_CUENTA = "6";
	public static final String COD_VERIFICAR_CUENTA_CASO_SOCIAL = "7";
	public static final String COD_MODIFICACION_DATOS = "31";
	public static final String COD_MODIFICACION_IDENTIFICACION_PERSONA = "8";
	public static final String COD_RESCATE_CONVENIO_VIA_ADM = "9";
	public static final String COD_RESCATE_CONVENIO_VIA_JUD = "10";
	public static final String COD_VERIFICAR_CONVENIO_RECOMPUESTO = "11";
	public static final String COD_VERIFICAR_CONVENIO_PAGO_PAGOS_A_CUENTA = "12";
	
	public static final String COD_REGULARIZACION_PERIODOS_DEUDA_ADM = "29";
	public static final String COD_REGULARIZACION_PERIODOS_DEUDA_JUD = "30";
	
	public static final String COD_ALTA_PROCED_CYQ = "32";
	public static final String COD_MODIFICACION_PROCED_CYQ = "33";
	public static final String COD_BAJA_PROCED_CYQ = "34";
	public static final String COD_CONVERSION_PROCED_CYQ = "35";
	
	public static final String COD_DISTRIBUCION_OTRINGTES = "36";
	public static final String COD_DOMICILIO_SIN_CATASTRAL = "39";
	
	@Column(name = "codigo")
	private String codigo;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@ManyToOne(optional=false) 
    @JoinColumn(name="idareadestino") 
	private Area areaDestino;
	
	@OneToMany()
	@JoinColumn(name="idTipoSolicitud")
	private List<AreaSolicitud> listAreaSolicitud;
	
	// Constructores
	public TipoSolicitud(){
		super();
	}
	
	public TipoSolicitud(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipoSolicitud getById(Long id) {
		return (TipoSolicitud) CasDAOFactory.getTipoSolicitudDAO().getById(id);
	}
	
	public static TipoSolicitud getByIdNull(Long id) {
		return (TipoSolicitud) CasDAOFactory.getTipoSolicitudDAO().getByIdNull(id);
	}
	
	public static List<TipoSolicitud> getList() {
		return (List<TipoSolicitud>) CasDAOFactory.getTipoSolicitudDAO().getList();
	}
	
	public static List<TipoSolicitud> getListActivos() {			
		return (List<TipoSolicitud>) CasDAOFactory.getTipoSolicitudDAO().getListActiva();
	}
	
	public static List<TipoSolicitud> getListActivosByArea(Area area) {			
		List<TipoSolicitud> listTipoSolicitud = (List<TipoSolicitud>) CasDAOFactory.getTipoSolicitudDAO().getListActiva();
		List<TipoSolicitud> listTipoSolicitudByArea = new ArrayList<TipoSolicitud>();
		
		for (TipoSolicitud item: listTipoSolicitud){				
			
			if (item.getAreaDestino().equals(area)) {
				listTipoSolicitudByArea.add(item);	
			}	
		}
		return listTipoSolicitudByArea;
	}
	
	public static List<TipoSolicitud> getListActivosHasAreaList(String listIds) throws Exception {			
		return (ArrayList<TipoSolicitud>) CasDAOFactory.getTipoSolicitudDAO().getListActivosHasAreaList(listIds);
	}
	
	public static TipoSolicitud getByCodigo(String codigo) throws Exception {			
		return CasDAOFactory.getTipoSolicitudDAO().getByCodigo(codigo);
	}

	// Getters y setters
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Area getAreaDestino() {
		return areaDestino;
	}

	public void setAreaDestino(Area areaDestino) {
		this.areaDestino = areaDestino;
	}

	public List<AreaSolicitud> getListAreaSolicitud() {
		return listAreaSolicitud;
	}
	public void setListAreaSolicitud(List<AreaSolicitud> listAreaSolicitud) {
		this.listAreaSolicitud = listAreaSolicitud;
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
		// limpiamos la lista de errores
		clearError();

		if (GenericDAO.hasReference(this, AreaSolicitud.class, "tipoSolicitud")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.ATRIBUTO_LABEL , DefError.CONATR_LABEL);
		}
				
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		//	Validaciones de Requeridos
		

		if (null == getCodigo() || StringUtil.isNullOrEmpty(getCodigo())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.TIPOSOLICITUD_CODIGO);			
		}
		
		if (null == getDescripcion() || StringUtil.isNullOrEmpty(getDescripcion())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.TIPOSOLICITUD_DESCRIPCION);
		}
		
		if (null == getAreaDestino()) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.AREASOLICITUD_LABEL);
		}

		if (hasError()) {
			return false;
		}

		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codigo");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, CasError.TIPOSOLICITUD_CODIGO);			
		}

		if (hasError()) {
			return false;
		}

		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipoSolicitud. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		CasDAOFactory.getTipoSolicitudDAO().update(this);
	}

	/**
	 * Desactiva el TipoSolicitud. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		CasDAOFactory.getTipoSolicitudDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipoSolicitud
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipoSolicitud
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

}
