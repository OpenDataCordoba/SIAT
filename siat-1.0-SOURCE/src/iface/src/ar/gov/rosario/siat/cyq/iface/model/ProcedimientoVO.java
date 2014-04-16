//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.AreaSolicitudVO;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdminVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaProcuradorVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pro.iface.model.ProcesoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del Procedimiento
 * @author tecso
 *
 */
public class ProcedimientoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "procedimientoVO";
	
	private String[] listIdAreaSolicSelected;
	private List<AreaSolicitudVO> listAreaSolicitud = new ArrayList<AreaSolicitudVO>();
	
	private Integer numero;
	private Integer anio;
	private Date fechaAlta;		// fecha del procedimiento
	private Date fechaBoletin;
    
    private Long idContribuyente;
    private PersonaVO contribuyente = new PersonaVO();
    
	private String desContribuyente;
	private String domicilio;
	private TipoProcesoVO tipoProceso = new TipoProcesoVO();
	private JuzgadoVO juzgado = new JuzgadoVO();
	private AbogadoVO abogado = new AbogadoVO();
	private String caratula;
	private Integer numExp; // numero del expediente del juzgado
	private Integer anioExp;  // anio del expediente del juzgado
	private CasoVO caso = new CasoVO();		// Expediente de la Municipalidad.
	private Date fechaVerOpo;	// Fecha de Verificacion / Oposicion
	private Date fechaAltaVer;	// Fecha en que se cargó por primera vez la Fecha de Verificación.
	private Date fechaInfInd;	// Fecha Informe Individual
	private String perOpoDeu;	// Sindico Designado / Persona Oponer Deuda
	private String lugarOposicion;	// Domicilio Síndico/Lugar Oposición
	private String telefonoOposicion;	// Teléfono Sindico/Lugar Oposición
	private String observacion;
	private Date fechaHomo;	// Fecha Homologacion
	private String auto;
	private Date fechaAuto; // es la "Fecha de Actualizacion de Deuda" mostrada en la liquidacion de la deuda.
	private ProcedimientoVO procedAnt;	// Procedimiento Anterior
	private Long idProcedAnt;
	private EstadoProcedVO estadoProced = new EstadoProcedVO();
	private ProcesoVO proCyQ;	
	
	private Date fechaBaja;
	private MotivoBajaVO motivoBaja = new MotivoBajaVO();
	private String observacionBaja;
	private String nroSentenciaBaja;
	private MotivoResInfVO motivoResInf = new MotivoResInfVO();
	private SiNo recursoRes = SiNo.OpcionSelecionar;
	private String nuevaCaratulaRes;
	private String codExpJudRes; // Codigo Expediente Judicial Informe Resolucion
	private Double privGeneral;
	private Double privEspecial;
	private Double quirografario;
	
	private List<HisEstProcedVO> listHisEstProced = new ArrayList<HisEstProcedVO>();
	private List<CuentaVO> listCuenta = new ArrayList<CuentaVO>();
	
	private List<LiqDeudaAdminVO> listGestionDeudaAdmin = new ArrayList<LiqDeudaAdminVO>();
	private List<LiqDeudaProcuradorVO> listProcurador = new ArrayList<LiqDeudaProcuradorVO>();
	
	private List<OrdenControlVO> listOrdenControl = new ArrayList<OrdenControlVO>(); 
	
	private List<ProCueNoDeuVO> listProCueNoDeu = new ArrayList<ProCueNoDeuVO>();
	
	// Buss Flags
	
	// View Constants
	private String numeroView = "";
	private String anioView = "";
	private String fechaAltaView = "";
	private String fechaBoletinView = "";
	private String numExpView = "";
	private String anioExpView = "";
	private String fechaVerOpoView = "";
	private String fechaAltaVerView = "";
	private String fechaInfIndView = "";
	private String fechaHomoView = "";
	private String fechaAutoView = "";
	
	private String fechaBajaView = "";
	private String privGeneralView = "";
	private String privEspecialView = "";
	private String quirografarioView = "";
	private String idProcedAntView = "";
	
	private HisEstProcedVO hisEstProced = new HisEstProcedVO();
	
	
	private Boolean cambiarEstadoBussEnabled=false;
	private Boolean liquidacionDeudaBussEnabled=false;
	
	// Constructores
	public ProcedimientoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ProcedimientoVO(int id) {
		super();
		setId(new Long(id));
	}
	
	// Getters y Setters
	
	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
		this.anioView = StringUtil.formatInteger(anio);
	}

	public Integer getAnioExp() {
		return anioExp;
	}

	public void setAnioExp(Integer anioExp) {
		this.anioExp = anioExp;
		this.anioExpView = StringUtil.formatInteger(anioExp);
	}

	public String getCaratula() {
		return caratula;
	}

	public void setCaratula(String caratula) {
		this.caratula = caratula;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
		this.fechaAltaView = DateUtil.formatDate(fechaAlta, DateUtil.ddSMMSYYYY_MASK);
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
		this.numeroView = StringUtil.formatInteger(numero);
	}

	public Integer getNumExp() {
		return numExp;
	}

	public void setNumExp(Integer numExp) {
		this.numExp = numExp;
		this.numExpView = StringUtil.formatInteger(numExp);
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Date getFechaBoletin() {
		return fechaBoletin;
	}
	
	public void setFechaBoletin(Date fechaBoletin) {
		this.fechaBoletin = fechaBoletin;
		this.fechaBoletinView = DateUtil.formatDate(fechaBoletin, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public Long getIdContribuyente() {
		return idContribuyente;
	}
	public void setIdContribuyente(Long idContribuyente) {
		this.idContribuyente = idContribuyente;
	}

	public PersonaVO getContribuyente() {
		return contribuyente;
	}
	public void setContribuyente(PersonaVO contribuyente) {
		this.contribuyente = contribuyente;
	}

	public String getDesContribuyente() {
		return desContribuyente;
	}
	
	public void setDesContribuyente(String desContribuyente) {
		this.desContribuyente = desContribuyente;
	}
	
	public String getDomicilio() {
		return domicilio;
	}
	
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	
	public TipoProcesoVO getTipoProceso() {
		return tipoProceso;
	}
	
	public void setTipoProceso(TipoProcesoVO tipoProceso) {
		this.tipoProceso = tipoProceso;
	}
	
	public JuzgadoVO getJuzgado() {
		return juzgado;
	}
	
	public void setJuzgado(JuzgadoVO juzgado) {
		this.juzgado = juzgado;
	}
	
	public AbogadoVO getAbogado() {
		return abogado;
	}
	
	public void setAbogado(AbogadoVO abogado) {
		this.abogado = abogado;
	}
	
	public CasoVO getCaso() {
		return caso;
	}
	
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	
	public Date getFechaVerOpo() {
		return fechaVerOpo;
	}
	
	public void setFechaVerOpo(Date fechaVerOpo) {
		this.fechaVerOpo = fechaVerOpo;
		this.fechaVerOpoView = DateUtil.formatDate(fechaVerOpo, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public Date getFechaAltaVer() {
		return fechaAltaVer;
	}
	
	public void setFechaAltaVer(Date fechaAltaVer) {
		this.fechaAltaVer = fechaAltaVer;
		this.fechaAltaVerView = DateUtil.formatDate(fechaAltaVer, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public Date getFechaInfInd() {
		return fechaInfInd;
	}
	
	public void setFechaInfInd(Date fechaInfInd) {
		this.fechaInfInd = fechaInfInd;
		this.fechaInfIndView = DateUtil.formatDate(fechaInfInd, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public String getPerOpoDeu() {
		return perOpoDeu;
	}
	
	public void setPerOpoDeu(String perOpoDeu) {
		this.perOpoDeu = perOpoDeu;
	}
	
	public String getLugarOposicion() {
		return lugarOposicion;
	}
	
	public void setLugarOposicion(String lugarOposicion) {
		this.lugarOposicion = lugarOposicion;
	}
	
	public String getTelefonoOposicion() {
		return telefonoOposicion;
	}
	
	public void setTelefonoOposicion(String telefonoOposicion) {
		this.telefonoOposicion = telefonoOposicion;
	}
	
	public Date getFechaHomo() {
		return fechaHomo;
	}
	
	public void setFechaHomo(Date fechaHomo) {
		this.fechaHomo = fechaHomo;
		this.fechaHomoView = DateUtil.formatDate(fechaHomo, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public String getAuto() {
		return auto;
	}
	
	public void setAuto(String auto) {
		this.auto = auto;
	}
	
	public Date getFechaAuto() {
		return fechaAuto;
	}
	
	public void setFechaAuto(Date fechaAuto) {
		this.fechaAuto = fechaAuto;
		this.fechaAutoView = DateUtil.formatDate(fechaAuto, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public ProcedimientoVO getProcedAnt() {
		return procedAnt;
	}
	
	public void setProcedAnt(ProcedimientoVO procedAnt) {
		this.procedAnt = procedAnt;
		if (procedAnt != null){
			setIdProcedAnt(procedAnt.getId());
		}
	}
	
	public EstadoProcedVO getEstadoProced() {
		return estadoProced;
	}
	
	public void setEstadoProced(EstadoProcedVO estadoProced) {
		this.estadoProced = estadoProced;
	}
	
	public ProcesoVO getProCyQ() {
		return proCyQ;
	}
	
	public void setProCyQ(ProcesoVO proCyQ) {
		this.proCyQ = proCyQ;
	}
	
	public Boolean getCambiarEstadoBussEnabled() {
		return cambiarEstadoBussEnabled;
	}
	public void setCambiarEstadoBussEnabled(Boolean cambiarEstadoBussEnabled) {
		this.cambiarEstadoBussEnabled = cambiarEstadoBussEnabled;
	}
	
	public Date getFechaBaja() {
		return fechaBaja;
	}
	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
		this.fechaBajaView = DateUtil.formatDate(fechaBaja, DateUtil.ddSMMSYYYY_MASK);
	}

	public MotivoBajaVO getMotivoBaja() {
		return motivoBaja;
	}
	public void setMotivoBaja(MotivoBajaVO motivoBaja) {
		this.motivoBaja = motivoBaja;
	}

	public String getObservacionBaja() {
		return observacionBaja;
	}
	public void setObservacionBaja(String observacionBaja) {
		this.observacionBaja = observacionBaja;
	}

	public String getNroSentenciaBaja() {
		return nroSentenciaBaja;
	}
	public void setNroSentenciaBaja(String nroSentenciaBaja) {
		this.nroSentenciaBaja = nroSentenciaBaja;
	}

	public MotivoResInfVO getMotivoResInf() {
		return motivoResInf;
	}
	public void setMotivoResInf(MotivoResInfVO motivoResInf) {
		this.motivoResInf = motivoResInf;
	}

	public SiNo getRecursoRes() {
		return recursoRes;
	}
	public void setRecursoRes(SiNo recursoRes) {
		this.recursoRes = recursoRes;
	}

	public String getNuevaCaratulaRes() {
		return nuevaCaratulaRes;
	}
	public void setNuevaCaratulaRes(String nuevaCaratulaRes) {
		this.nuevaCaratulaRes = nuevaCaratulaRes;
	}

	public String getCodExpJudRes() {
		return codExpJudRes;
	}
	public void setCodExpJudRes(String codExpJudRes) {
		this.codExpJudRes = codExpJudRes;
	}

	public Double getPrivGeneral() {
		return privGeneral;
	}
	public void setPrivGeneral(Double privGeneral) {
		this.privGeneral = privGeneral;
		this.privGeneralView = StringUtil.formatDouble(privGeneral);
	}

	public Double getPrivEspecial() {
		return privEspecial;
	}
	public void setPrivEspecial(Double privEspecial) {
		this.privEspecial = privEspecial;
		this.privEspecialView = StringUtil.formatDouble(privEspecial);
	}

	public Double getQuirografario() {
		return quirografario;
	}
	public void setQuirografario(Double quirografario) {
		this.quirografario = quirografario;
		this.quirografarioView = StringUtil.formatDouble(quirografario);
	}
	
	public Long getIdProcedAnt() {
		return idProcedAnt;
	}
	public void setIdProcedAnt(Long idProcedAnt) {
		this.idProcedAnt = idProcedAnt;
		this.idProcedAntView = StringUtil.formatLong(idProcedAnt);
	}
	
	public String getIdProcedAntView() {
		return idProcedAntView;
	}
	public void setIdProcedAntView(String idProcedAntView) {
		this.idProcedAntView = idProcedAntView;
	}
	
	public List<OrdenControlVO> getListOrdenControl() {
		return listOrdenControl;
	}
	public void setListOrdenControl(List<OrdenControlVO> listOrdenControl) {
		this.listOrdenControl = listOrdenControl;
	}
	
	public List<ProCueNoDeuVO> getListProCueNoDeu() {
		return listProCueNoDeu;
	}
	public void setListProCueNoDeu(List<ProCueNoDeuVO> listProCueNoDeu) {
		this.listProCueNoDeu = listProCueNoDeu;
	}
	
	// Buss flags getters y setters




	// View flags getters
	public boolean getPoseeFechaBaja(){
		
		if (getFechaBaja() == null){
			return false;
		} else {
			return true;
		}
	}

	public boolean getPoseeProcedAnt(){
		
		if (!ModelUtil.isNullOrEmpty(getProcedAnt())){
			return true;
		} else {
			return false;
		}
		
	}
	
	// View getters
	public void setAnioView(String anioView) {
		this.anioView = anioView;
	}
	public String getAnioView() {
		return anioView;
	}

	public void setAnioExpView(String anioExpView) {
		this.anioExpView = anioExpView;
	}
	public String getAnioExpView() {
		return anioExpView;
	}

	public void setFechaAltaView(String fechaAltaView) {
		this.fechaAltaView = fechaAltaView;
	}
	public String getFechaAltaView() {
		return fechaAltaView;
	}

	public void setNumExpView(String numExpView) {
		this.numExpView = numExpView;
	}
	public String getNumExpView() {
		return numExpView;
	}

	public void setNumeroView(String numeroView) {
		this.numeroView = numeroView;
	}
	public String getNumeroView() {
		return numeroView;
	}

	public String getRepresent(){
		String rep = "";
		
		if (getId().longValue() == -1)
			return StringUtil.SELECT_OPCION_SELECCIONAR;
		
		return rep + getAnioView() + " - " + getNumeroView();
	}

	public String getFechaBoletinView() {
		return fechaBoletinView;
	}

	public void setFechaBoletinView(String fechaBoletinView) {
		this.fechaBoletinView = fechaBoletinView;
	}

	public String getFechaVerOpoView() {
		return fechaVerOpoView;
	}

	public void setFechaVerOpoView(String fechaVerOpoView) {
		this.fechaVerOpoView = fechaVerOpoView;
	}

	public String getFechaAltaVerView() {
		return fechaAltaVerView;
	}

	public void setFechaAltaVerView(String fechaAltaVerView) {
		this.fechaAltaVerView = fechaAltaVerView;
	}

	public String getFechaInfIndView() {
		return fechaInfIndView;
	}

	public void setFechaInfIndView(String fechaInfIndView) {
		this.fechaInfIndView = fechaInfIndView;
	}

	public String getFechaHomoView() {
		return fechaHomoView;
	}

	public void setFechaHomoView(String fechaHomoView) {
		this.fechaHomoView = fechaHomoView;
	}

	public String getFechaAutoView() {
		return fechaAutoView;
	}

	public void setFechaAutoView(String fechaAutoView) {
		this.fechaAutoView = fechaAutoView;
	}

	public HisEstProcedVO getHisEstProced() {
		return hisEstProced;
	}
	public void setHisEstProced(HisEstProcedVO hisEstProced) {
		this.hisEstProced = hisEstProced;
	}

	public List<HisEstProcedVO> getListHisEstProced() {
		return listHisEstProced;
	}

	public void setListHisEstProced(List<HisEstProcedVO> listHisEstProced) {
		this.listHisEstProced = listHisEstProced;
	}

	public List<CuentaVO> getListCuenta() {
		return listCuenta;
	}

	public void setListCuenta(List<CuentaVO> listCuenta) {
		this.listCuenta = listCuenta;
	}

	public List<LiqDeudaAdminVO> getListGestionDeudaAdmin() {
		return listGestionDeudaAdmin;
	}
	public void setListGestionDeudaAdmin(List<LiqDeudaAdminVO> listGestionDeudaAdmin) {
		this.listGestionDeudaAdmin = listGestionDeudaAdmin;
	}

	public List<LiqDeudaProcuradorVO> getListProcurador() {
		return listProcurador;
	}
	public void setListProcurador(List<LiqDeudaProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}

	public String getFechaBajaView() {
		return fechaBajaView;
	}
	public void setFechaBajaView(String fechaBajaView) {
		this.fechaBajaView = fechaBajaView;
	}

	public String getPrivGeneralView() {
		return privGeneralView;
	}
	public void setPrivGeneralView(String privGeneralView) {
		this.privGeneralView = privGeneralView;
	}

	public String getPrivEspecialView() {
		return privEspecialView;
	}
	public void setPrivEspecialView(String privEspecialView) {
		this.privEspecialView = privEspecialView;
	}

	public String getQuirografarioView() {
		return quirografarioView;
	}
	public void setQuirografarioView(String quirografarioView) {
		this.quirografarioView = quirografarioView;
	}

	public String[] getListIdAreaSolicSelected() {
		return listIdAreaSolicSelected;
	}
	public void setListIdAreaSolicSelected(String[] listIdAreaSolicSelected) {
		this.listIdAreaSolicSelected = listIdAreaSolicSelected;
	}

	public List<AreaSolicitudVO> getListAreaSolicitud() {
		return listAreaSolicitud;
	}
	public void setListAreaSolicitud(List<AreaSolicitudVO> listAreaSolicitud) {
		this.listAreaSolicitud = listAreaSolicitud;
	}
	
	
	public boolean getPermiteConversion(){
		
		if (getTipoProceso() != null && !StringUtil.isNullOrEmpty(getTipoProceso().getTipo())){
			return true;
		} else {
			return false;
		}		
	}
	
	public String getContribuyenteView(){	
		if (!ModelUtil.isNullOrEmpty(this.getContribuyente())){
			return contribuyente.getRepresent();	
		} 
		return this.getDesContribuyente();
	}

	public Boolean getLiquidacionDeudaBussEnabled() {
		return liquidacionDeudaBussEnabled;
	}

	public void setLiquidacionDeudaBussEnabled(Boolean liquidacionDeudaBussEnabled) {
		this.liquidacionDeudaBussEnabled = liquidacionDeudaBussEnabled;
	}

}
