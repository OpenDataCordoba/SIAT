//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del CueExe
 * @author tecso
 *
 */
public class CueExeVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	static private Log log = LogFactory.getLog(CueExeVO.class);
	
	public static final String NAME = "cueExeVO";
	
	private Date fechaSolicitud;
	private Date fechaDesde;
	private Date fechaHasta;
	private CuentaVO cuenta = new CuentaVO();
	private ExencionVO exencion = new ExencionVO();
	private EstadoCueExeVO estadoCueExe = new EstadoCueExeVO();
	private PersonaVO solicitante = new PersonaVO();
	private String solicDescripcion;
	private TipoSujetoVO tipoSujeto = new TipoSujetoVO();
	private Date solicFechaDesde;
	private Date solicFechaHasta;
	private Date fechaResolucion;
	private String ordenanza;
	private String articulo;
	private String inciso;
	private Date fechaUltIns;
	private String nroBeneficiario;
	private String caja;
	private String observaciones;
	private String documentacion;
	private Date fechaPresent;
	private Integer clase;
	private Date fechaVencContInq; 
	private CasoVO caso = new CasoVO();
	private String tipoDocumento;
	private String nroDocumento;
	private Date fechaCadHab;
	
	private String fechaSolicitudView="";
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	private String solicFechaDesdeView = "";
	private String solicFechaHastaView = "";
	private String fechaResolucionView = "";
	private String fechaUltInsView = "";
	private String fechaPresentView = "";
	private String fechaVencContInqView = ""; 
	private String claseView="";
	private String fechaCadHabView = "";
	
	private List<HisEstCueExeVO> listHisEstCueExe = new ArrayList<HisEstCueExeVO>();
	
	private List<ConvivienteVO> listConviviente = new ArrayList<ConvivienteVO>();
	
	// Utilizado para la validacion de la cuenta
	private RecursoVO recurso = new RecursoVO();	
	private boolean permSelecEstadoInicial = false; // Permite Seleccionar Estado Inicial 
	
	private HisEstCueExeVO hisEstCueExe = new HisEstCueExeVO(); 
	
	private boolean esCreadaHaLugar = false;
	
	// Constructores
	public CueExeVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public CueExeVO(int id) {
		super();
		setId(new Long(id));		
	}
	
	//	 Getters y Setters	
	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public ExencionVO getExencion() {
		return exencion;
	}

	public void setExencion(ExencionVO exencion) {
		this.exencion = exencion;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public EstadoCueExeVO getEstadoCueExe() {
		return estadoCueExe;
	}

	public void setEstadoCueExe(EstadoCueExeVO estadoCueExe) {
		this.estadoCueExe = estadoCueExe;
	}

	public PersonaVO getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(PersonaVO solicitante) {
		this.solicitante = solicitante;
	}

	public String getSolicDescripcion() {
		return solicDescripcion;
	}

	public void setSolicDescripcion(String solicDescripcion) {
		this.solicDescripcion = solicDescripcion;
	}

	public TipoSujetoVO getTipoSujeto() {
		return tipoSujeto;
	}

	public void setTipoSujeto(TipoSujetoVO tipoSujeto) {
		this.tipoSujeto = tipoSujeto;
	}

	public Date getSolicFechaDesde() {
		return solicFechaDesde;
	}

	public void setSolicFechaDesde(Date solicFechaDesde) {
		this.solicFechaDesde = solicFechaDesde;
		this.solicFechaDesdeView = DateUtil.formatDate(solicFechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getSolicFechaHasta() {
		return solicFechaHasta;
	}

	public void setSolicFechaHasta(Date solicFechaHasta) {
		this.solicFechaHasta = solicFechaHasta;
		this.solicFechaHastaView = DateUtil.formatDate(solicFechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaResolucion() {
		return fechaResolucion;
	}

	public void setFechaResolucion(Date fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
		this.fechaResolucionView = DateUtil.formatDate(fechaResolucion, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getOrdenanza() {
		return ordenanza;
	}

	public void setOrdenanza(String ordenanza) {
		this.ordenanza = ordenanza;
	}

	public String getArticulo() {
		return articulo;
	}

	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}

	public String getInciso() {
		return inciso;
	}

	public void setInciso(String inciso) {
		this.inciso = inciso;
	}

	public Date getFechaUltIns() {
		return fechaUltIns;
	}

	public void setFechaUltIns(Date fechaUltIns) {
		this.fechaUltIns = fechaUltIns;
		this.fechaUltInsView = DateUtil.formatDate(fechaUltIns, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getNroBeneficiario() {
		return nroBeneficiario;
	}

	public void setNroBeneficiario(String nroBeneficiario) {
		this.nroBeneficiario = nroBeneficiario;
	}

	public String getCaja() {
		return caja;
	}

	public void setCaja(String caja) {
		this.caja = caja;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getDocumentacion() {
		return documentacion;
	}

	public void setDocumentacion(String documentacion) {
		this.documentacion = documentacion;
	}

	public Date getFechaPresent() {
		return fechaPresent;
	}

	public void setFechaPresent(Date fechaPresent) {
		this.fechaPresent = fechaPresent;
		this.fechaPresentView = DateUtil.formatDate(fechaPresent, DateUtil.ddSMMSYYYY_MASK);
	}

	public Integer getClase() {
		return clase;
	}

	public void setClase(Integer clase) {
		this.clase = clase;
		this.claseView = StringUtil.formatInteger(clase);
	}

	public Date getFechaVencContInq() {
		return fechaVencContInq;
	}

	public void setFechaVencContInq(Date fechaVencContInq) {
		this.fechaVencContInq = fechaVencContInq;
		this.fechaVencContInqView = DateUtil.formatDate(fechaVencContInq, DateUtil.ddSMMSYYYY_MASK); 
	}
	
	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}
	
	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
		this.fechaSolicitudView  = DateUtil.formatDate(fechaSolicitud, DateUtil.ddSMMSYYYY_MASK);		
	}
	
	public CasoVO getCaso() {
		return caso;
	}
	
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	
	public RecursoVO getRecurso() {
		return recurso;
	}
	
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	
	public boolean isPermSelecEstadoInicial() {
		return permSelecEstadoInicial;
	}

	public void setPermSelecEstadoInicial(boolean permSelecEstadoInicial) {
		this.permSelecEstadoInicial = permSelecEstadoInicial;
	}
	
	public List<HisEstCueExeVO> getListHisEstCueExe() {
		return listHisEstCueExe;
	}
	public void setListHisEstCueExe(List<HisEstCueExeVO> listHisEstCueExe) {
		this.listHisEstCueExe = listHisEstCueExe;
	}
	
	public HisEstCueExeVO getHisEstCueExe() {
		return hisEstCueExe;
	}
	
	public void setHisEstCueExe(HisEstCueExeVO hisEstCueExe) {
		this.hisEstCueExe = hisEstCueExe;
	}
	
	public List<ConvivienteVO> getListConviviente() {
		return listConviviente;
	}
	
	public void setListConviviente(List<ConvivienteVO> listConviviente) {
		this.listConviviente = listConviviente;
	}
	
	public boolean getEsCreadaHaLugar() {
		return esCreadaHaLugar;
	}
	
	public void setEsCreadaHaLugar(boolean esCreadaHaLugar) {
		this.esCreadaHaLugar = esCreadaHaLugar;
	}
	
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}
	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}
	public Date getFechaCadHab() {
		return fechaCadHab;
	}	
	public void setFechaCadHab(Date fechaCadHab) {
		this.fechaCadHab = fechaCadHab;
		this.fechaCadHabView  = DateUtil.formatDate(fechaCadHab, DateUtil.ddSMMSYYYY_MASK);
	}
	
	// Buss flags getters y setters

	
	// View flags getters


	// View getters y setters
	public String getFechaCadHabView() {
		return fechaCadHabView;
	}
	public void setFechaCadHabView(String fechaCadHabView) {
		this.fechaCadHabView = fechaCadHabView;
	}

	public String getFechaSolicitudView() {
		return fechaSolicitudView;
	}

	public void setFechaSolicitudView(String fechaSolicitudView) {
		this.fechaSolicitudView = fechaSolicitudView;
	}
	
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}

	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}

	public String getFechaHastaView() {
		return fechaHastaView;
	}

	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}

	public String getSolicFechaDesdeView() {
		return solicFechaDesdeView;
	}

	public void setSolicFechaDesdeView(String solicFechaDesdeView) {
		this.solicFechaDesdeView = solicFechaDesdeView;
	}

	public String getSolicFechaHastaView() {
		return solicFechaHastaView;
	}

	public void setSolicFechaHastaView(String solicFechaHastaView) {
		this.solicFechaHastaView = solicFechaHastaView;
	}

	public String getFechaResolucionView() {
		return fechaResolucionView;
	}

	public void setFechaResolucionView(String fechaResolucionView) {
		this.fechaResolucionView = fechaResolucionView;
	}

	public String getFechaUltInsView() {
		return fechaUltInsView;
	}

	public void setFechaUltInsView(String fechaUltInsView) {
		this.fechaUltInsView = fechaUltInsView;
	}

	public String getFechaPresentView() {
		return fechaPresentView;
	}

	public void setFechaPresentView(String fechaPresentView) {
		this.fechaPresentView = fechaPresentView;
	}

	public String getFechaVencContInqView() {
		return fechaVencContInqView;
	}

	public void setFechaVencContInqView(String fechaVencContInqView) {
		this.fechaVencContInqView = fechaVencContInqView;
	}

	public String getClaseView() {
		return claseView;
	}
	public void setClaseView(String claseView) {
		this.claseView = claseView;
	}

	public String getSolicitanteView(){
		
		if (!ModelUtil.isNullOrEmpty(getSolicitante()) ){
			return getSolicitante().getRepresent();
		} else if (!StringUtil.isNullOrEmpty(getSolicDescripcion())){
			return getSolicDescripcion();
		} else {
			return "No posee.";
		}			
	}
}
