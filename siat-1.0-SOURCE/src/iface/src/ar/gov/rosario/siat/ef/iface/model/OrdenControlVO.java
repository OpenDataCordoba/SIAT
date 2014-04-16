//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoVO;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del OrdenControl
 * @author tecso
 *
 */
public class OrdenControlVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "ordenControlVO";
	
	private OpeInvConVO opeInvCon = new OpeInvConVO();

	private ContribuyenteVO contribuyente = new ContribuyenteVO();

	private Integer numeroOrden;

	private Integer anioOrden;

	private TipoOrdenVO tipoOrden = new TipoOrdenVO();

	private EstadoOrdenVO estadoOrden = new EstadoOrdenVO();

	private InspectorVO inspector =new InspectorVO();

	private String observacion="";
	
	private String obsInv="";

	private TipoPeriodoVO tipoPeriodo = new TipoPeriodoVO();

	private SiNo esPrincipal = SiNo.OpcionSelecionar;

	private OrigenOrdenVO origenOrden = new OrigenOrdenVO();

	private OrdenControlVO ordenControlOrigen;

	private Date fechaEmision;
	
	private Date fechaCierre;
	
	private Date fechaImpresion;
	
	private List<OrdConCueVO> listOrdConCue = new ArrayList<OrdConCueVO>();
	
	private List<HisEstOrdConVO> ListHisEstOrdCon = new ArrayList<HisEstOrdConVO>();
	
	private CasoVO caso = new CasoVO();
	
	private SupervisorVO supervisor;
	
	private ProcedimientoVO procedimiento = new ProcedimientoVO();
	
	private List<PeriodoOrdenVO> listPeriodoOrden = new ArrayList<PeriodoOrdenVO>();

	private List<ActaVO> listActa = new ArrayList<ActaVO>();
	
	private List<PlaFueDatVO> listPlaFueDat = new ArrayList<PlaFueDatVO>();
	
	private List<ComparacionVO> listComparacion = new ArrayList<ComparacionVO>();
	
	private List<OrdConBasImpVO> listOrdConBasImp = new ArrayList<OrdConBasImpVO>();
	
	private List<DetAjuVO> listDetAju = new ArrayList<DetAjuVO>();
	
	private List<MesaEntradaVO> listMesaEntrada = new ArrayList<MesaEntradaVO>();
	
	private List<AproOrdConVO> listAproOrdCon = new ArrayList<AproOrdConVO>();
	
	private List<DetAjuDocSopVO> listDetAjuDocSop = new ArrayList<DetAjuDocSopVO>();
	
	private List<ComAjuVO> listComAju = new ArrayList<ComAjuVO>();
	
	
	
	private String fechaEmisionView ="";
	private String fechaCierreView ="";
	private String fechaImpresionView="";
	private String numeroOrdenView="";
	private String anioOrdenView="";
	
	// Buss Flags
	private boolean opciones4CuentasBussEnabled=true;
	
	// View Constants
	
	// View properties
	private String strListCuentas ="";
	
	// Constructores
	public OrdenControlVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public OrdenControlVO(int id, String desc) {
		super();
		setId(new Long(id));
		setNumeroOrdenView(desc);
	}
	
	// Getters y Setters

	public OpeInvConVO getOpeInvCon() {
		return opeInvCon;
	}

	public void setOpeInvCon(OpeInvConVO opeInvCon) {
		this.opeInvCon = opeInvCon;
	}

	public ContribuyenteVO getContribuyente() {
		return contribuyente;
	}

	public void setContribuyente(ContribuyenteVO contribuyente) {
		this.contribuyente = contribuyente;
	}

	public Integer getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(Integer numeroOrden) {
		this.numeroOrden = numeroOrden;
		this.numeroOrdenView = StringUtil.formatInteger(numeroOrden);
	}

	public Integer getAnioOrden() {
		return anioOrden;
	}

	public void setAnioOrden(Integer anioOrden) {
		this.anioOrden = anioOrden;
		this.anioOrdenView = StringUtil.formatInteger(anioOrden);
	}

	public TipoOrdenVO getTipoOrden() {
		return tipoOrden;
	}


	public void setTipoOrden(TipoOrdenVO tipoOrden) {
		this.tipoOrden = tipoOrden;
	}

	public EstadoOrdenVO getEstadoOrden() {
		return estadoOrden;
	}

	public void setEstadoOrden(EstadoOrdenVO estadoOrden) {
		this.estadoOrden = estadoOrden;
	}

	public InspectorVO getInspector() {
		return inspector;
	}

	public void setInspector(InspectorVO inspector) {
		this.inspector = inspector;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public TipoPeriodoVO getTipoPeriodo() {
		return tipoPeriodo;
	}

	public void setTipoPeriodo(TipoPeriodoVO tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}

	public SiNo getEsPrincipal() {
		return esPrincipal;
	}

	public void setEsPrincipal(SiNo esPrincipal) {
		this.esPrincipal = esPrincipal;
	}

	public OrigenOrdenVO getOrigenOrden() {
		return origenOrden;
	}

	public void setOrigenOrden(OrigenOrdenVO origenOrden) {
		this.origenOrden = origenOrden;
	}

	public OrdenControlVO getOrdenControlOrigen() {
		return ordenControlOrigen;
	}

	public void setOrdenControlOrigen(OrdenControlVO ordenControlOrigen) {
		this.ordenControlOrigen = ordenControlOrigen;
	}

	public List<OrdConCueVO> getListOrdConCue() {
		return listOrdConCue;
	}

	public void setListOrdConCue(List<OrdConCueVO> listOrdConCueVO) {
		this.listOrdConCue = listOrdConCueVO;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
		this.fechaEmisionView = DateUtil.formatDate(fechaEmision, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
		this.fechaCierreView = DateUtil.formatDate(fechaCierre, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaImpresion() {
		return fechaImpresion;
	}

	public void setFechaImpresion(Date fechaImpresion) {
		this.fechaImpresion = fechaImpresion;
		this.fechaImpresionView = DateUtil.formatDate(fechaImpresion, DateUtil.ddSMMSYYYY_MASK);
	}

	public List<HisEstOrdConVO> getListHisEstOrdCon() {
		return ListHisEstOrdCon;
	}
	
	public void setListHisEstOrdCon(List<HisEstOrdConVO> listHisEstOrdCon) {
		ListHisEstOrdCon = listHisEstOrdCon;
	}
	

	public CasoVO getCaso() {
		return caso;
	}
	
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	
	public ProcedimientoVO getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(ProcedimientoVO procedimiento) {
		this.procedimiento = procedimiento;
	}

	public List<PeriodoOrdenVO> getListPeriodoOrden() {
		return listPeriodoOrden;
	}
	
	public void setListPeriodoOrden(List<PeriodoOrdenVO> listPeriodoOrdenVO) {
		this.listPeriodoOrden = listPeriodoOrdenVO;
	}
	
	public List<ActaVO> getListActa() {
		return listActa;
	}
	
	public void setListActa(List<ActaVO> listActaVO) {
		this.listActa = listActaVO;
	}
	
	public List<PlaFueDatVO> getListPlaFueDat() {
		return listPlaFueDat;
	}

	public void setListPlaFueDat(List<PlaFueDatVO> listPlaFueDat) {
		this.listPlaFueDat = listPlaFueDat;
	}

	public List<ComparacionVO> getListComparacion() {
		return listComparacion;
	}

	public void setListComparacion(List<ComparacionVO> listComparacionVO) {
		this.listComparacion = listComparacionVO;
	}

	public List<OrdConBasImpVO> getListOrdConBasImp() {
		return listOrdConBasImp;
	}
	
	public void setListOrdConBasImp(List<OrdConBasImpVO> listOrdConBasImp) {
		this.listOrdConBasImp = listOrdConBasImp;
	}
	
	public List<DetAjuVO> getListDetAju() {
		return listDetAju;
	}
	
	public void setListDetAju(List<DetAjuVO> listDetAju) {
		this.listDetAju = listDetAju;
	}
	
	public List<MesaEntradaVO> getListMesaEntrada() {
		return listMesaEntrada;
	}
	
	public void setListMesaEntrada(List<MesaEntradaVO> listMesaEntrada) {
		this.listMesaEntrada = listMesaEntrada;
	}
	
	public List<AproOrdConVO> getListAproOrdCon() {
		return listAproOrdCon;
	}
	
	public void setListAproOrdCon(List<AproOrdConVO> listAproOrdCon) {
		this.listAproOrdCon = listAproOrdCon;
	}

	public List<DetAjuDocSopVO> getListDetAjuDocSop() {
		return listDetAjuDocSop;
	}
	
	public void setListDetAjuDocSop(List<DetAjuDocSopVO> listDetAjuDocSop) {
		this.listDetAjuDocSop = listDetAjuDocSop;
	}
	
	public List<ComAjuVO> getListComAju() {
		return listComAju;
	}
	
	public void setListComAju(List<ComAjuVO> listComAju) {
		this.listComAju = listComAju;
	}
	
	public String getObsInv() {
		return obsInv;
	}

	public void setObsInv(String obsInv) {
		this.obsInv = obsInv;
	}

	
	// Buss flags getters y setters
	
	
	// View flags getters



	
	// View getters
	public String getStrListCuentas() {
		return strListCuentas;
	}
	
	public void setStrListCuentas(String strListCuentas) {
		this.strListCuentas = strListCuentas;
	}

	public SupervisorVO getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(SupervisorVO supervisor) {
		this.supervisor = supervisor;
	}

	public String getFechaEmisionView() {
		return fechaEmisionView;
	}

	public void setFechaEmisionView(String fechaEmisionView) {
		this.fechaEmisionView = fechaEmisionView;
	}
	
	public String getFechaCierreView() {
		return fechaCierreView;
	}

	public void setFechaCierreView(String fechaCierreView) {
		this.fechaCierreView = fechaCierreView;
	}

	public String getFechaImpresionView() {
		return fechaImpresionView;
	}

	public void setFechaImpresionView(String fechaImpresionView) {
		this.fechaImpresionView = fechaImpresionView;
	}

	public String getNumeroOrdenView() {
		return numeroOrdenView;
	}
	public void setNumeroOrdenView(String numeroOrdenView) {
		this.numeroOrdenView = numeroOrdenView;
	}

	public String getAnioOrdenView() {
		return anioOrdenView;
	}
	public void setAnioOrdenView(String anioOrdenView) {
		this.anioOrdenView = anioOrdenView;
	}

	public boolean getOpciones4CuentasBussEnabled() {
		return opciones4CuentasBussEnabled;
	}

	public void setOpciones4CuentasBussEnabled(boolean opciones4CuentasBussEnabled) {
		this.opciones4CuentasBussEnabled = opciones4CuentasBussEnabled;
	}
    
	public String getOrdenControlView() {
		String rta ="";
		if(this.getId().equals(-1L)){
			rta=this.getNumeroOrdenView();
		}else{
			rta=this.getNumeroOrdenView()+"/"+this.getAnioOrdenView()+"-"+this.getContribuyente().getView();
		}
		return rta;
	}
	
	public String getOrdenControlyTipoView() {
		String rta ="";
		if(this.getId().equals(-1L)){
			rta=this.getNumeroOrdenView();
		}else{
			rta=this.tipoOrden.getDesTipoOrden()+" - ";
			rta+=this.getNumeroOrdenView()+"/"+this.getAnioOrdenView();
			if (this.getCaso()!=null && !StringUtil.isNullOrEmpty(this.getCaso().getNumero()))
				rta+=" Exp. "+this.getCaso().getNumero();
		}
		return rta;
	}
	
	public String getNumeroYAnioView(){
		
		String rta="";
		
		if(this.numeroOrden!=null)
			rta+=this.numeroOrden;
		
		if(this.anioOrden!=null)
			rta +="/"+this.anioOrden.toString();
		
		return rta;
	}
	

}
