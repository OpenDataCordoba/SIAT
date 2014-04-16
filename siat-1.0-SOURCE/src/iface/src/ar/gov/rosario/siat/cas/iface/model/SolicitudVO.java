//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean correspondiente a SolicitudVO
 * 
 * @author tecso
 */
public class SolicitudVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private Date fechaAlta;
	private AreaVO areaOrigen = new AreaVO();
	private AreaVO areaDestino = new AreaVO();
	private String usuarioAlta;
	private TipoSolicitudVO tipoSolicitud = new TipoSolicitudVO(); 
	private String asuntoSolicitud;
	private RecursoVO recurso = new RecursoVO();
	private CuentaVO cuenta = new CuentaVO();
	private CasoVO caso = new CasoVO();
	private Date fechaCamEst;
	private String descripcion;
	private EstSolicitudVO estSolicitud = new EstSolicitudVO();
	private String obsestsolicitud;
	private String logsolicitud;
	
	private long idEstSolicitudAnterior; //es el idEstSolicitud que trae de la base de datos, se usa para comprobar si cambió el estado o no
	
	private Boolean cambiarEstadoBussEnabled=true;
	
	// view
	private String fechaAltaView = "";
	private String fechaCamEstView = "";	

	// Constructores
	public SolicitudVO(){
		super();
	}

	public SolicitudVO(Long id){
		super();
		setId(id);
	}

	// Geters y setters	
	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
		this.setFechaAltaView(DateUtil.formatDate(fechaAlta, DateUtil.ddSMMSYYYY_MASK));
	}

	public AreaVO getAreaOrigen() {
		return areaOrigen;
	}

	public void setAreaOrigen(AreaVO areaOrigen) {
		this.areaOrigen = areaOrigen;
	}

	public String getUsuarioAlta() {
		return usuarioAlta;
	}

	public void setUsuarioAlta(String usuarioAlta) {
		this.usuarioAlta = usuarioAlta;
	}

	public TipoSolicitudVO getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(TipoSolicitudVO tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	public String getAsuntoSolicitud() {
		return asuntoSolicitud;
	}

	public void setAsuntoSolicitud(String asuntoSolicitud) {
		this.asuntoSolicitud = asuntoSolicitud;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public CasoVO getCaso() {
		return caso;
	}

	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}

	public Date getFechaCamEst() {
		return fechaCamEst;
	}

	public void setFechaCamEst(Date fechaCamEst) {
		this.fechaCamEst = fechaCamEst;
		this.setFechaCamEstView(DateUtil.formatDate(fechaCamEst, DateUtil.ddSMMSYYYY_MASK));		
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public EstSolicitudVO getEstSolicitud() {
		return estSolicitud;
	}

	public void setEstSolicitud(EstSolicitudVO estSolicitud) {
		this.estSolicitud = estSolicitud;
	}

	public String getObsestsolicitud() {
		return obsestsolicitud;
	}

	public void setObsestsolicitud(String obsestsolicitud) {
		this.obsestsolicitud = obsestsolicitud;
	}

	public String getLogsolicitud() {
		return logsolicitud;
	}

	public void setLogsolicitud(String logsolicitud) {
		this.logsolicitud = logsolicitud;
	}

	public long getIdEstSolicitudAnterior() {
		return idEstSolicitudAnterior;
	}

	public void setIdEstSolicitudAnterior(long idEstSolicitudAnterior) {
		this.idEstSolicitudAnterior = idEstSolicitudAnterior;
	}

	public AreaVO getAreaDestino() {
		return areaDestino;
	}

	public void setAreaDestino(AreaVO areaDestino) {
		this.areaDestino = areaDestino;
	}

	public Boolean getCambiarEstadoBussEnabled() {
		return cambiarEstadoBussEnabled;
	}

	public void setCambiarEstadoBussEnabled(Boolean cambiarEstadoBussEnabled) {
		this.cambiarEstadoBussEnabled = cambiarEstadoBussEnabled;
	}

	public String getFechaAltaView() {
		return fechaAltaView;
	}

	public void setFechaAltaView(String fechaAltaView) {
		this.fechaAltaView = fechaAltaView;
	}

	public String getFechaCamEstView() {
		return fechaCamEstView;
	}

	public void setFechaCamEstView(String fechaCamEstView) {
		this.fechaCamEstView = fechaCamEstView;
	}

	/**
	 * Devuelve los primeros 256 caracters de la descripcion
	 * @return
	 */
	public String getDescripcionCorta(){
		if(!StringUtil.isNullOrEmpty(descripcion)){
			if(descripcion.length()>256)
				return StringUtil.substring(descripcion, 256)+"...";
			return descripcion;
		}
		return "";
	}
}
