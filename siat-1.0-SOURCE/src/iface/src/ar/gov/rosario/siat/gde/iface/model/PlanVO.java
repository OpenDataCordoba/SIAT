//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.model.SistemaVO;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.frm.iface.model.FormularioVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del Plan
 * @author tecso
 *
 */
public class PlanVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "planVO";
	
	private String desPlan;
	private RecursoVO recurso = new RecursoVO();
	private ViaDeudaVO viaDeuda = new ViaDeudaVO();
	private ProcuradorVO procurador = new ProcuradorVO();
	private SistemaVO sistema = new SistemaVO();
	private Date fecVenDeuDes;
	private Date fecVenDeuHas;
	@Deprecated
	private Double impMinDeu;
	private SiNo aplicaTotalImpago = SiNo.OpcionSelecionar;
	@Deprecated
	private Double impMinCuo;
	private Integer canMaxCuo;
	private Integer canMinPer;
	private Integer canCuoAImpEnForm;
	private Integer canMinCuoParCuoSal;
	private Integer cuoDesParaRec;
	private SiNo poseeActEsp = SiNo.OpcionSelecionar;
	private SiNo esManual  = SiNo.OpcionSelecionar;
	private TipoDeudaPlanVO tipoDeudaPlan = new TipoDeudaPlanVO();
	private CasoVO caso = new CasoVO();
	private String leyendaPlan;
	private String linkNormativa;
	private Date fechaAlta;
	private Date fechaBaja;
	private String leyendaForm ="";
	private String ordenanza = "";
	
	private SiNo aplicaPagCue = SiNo.SI;
	private String nameSequence = "";
	private FormularioVO formulario = new FormularioVO();
	
	private List<PlanClaDeuVO> listPlanClaDeu 		= new ArrayList<PlanClaDeuVO>();
	private List<PlanMotCadVO> listPlanMotCad 		= new ArrayList<PlanMotCadVO>();
	private List<PlanForActDeuVO> listPlanForActDeu = new ArrayList<PlanForActDeuVO>();
	private List<PlanDescuentoVO> listPlanDescuento = new ArrayList<PlanDescuentoVO>();
	private List<PlanIntFinVO> listPlanIntFin  		= new ArrayList<PlanIntFinVO>();
	private List<PlanVenVO> listPlanVen 			= new ArrayList<PlanVenVO>();	
	private List<PlanAtrValVO> listPlanAtrVal 		= new ArrayList<PlanAtrValVO>();	
	private List<PlanExeVO> listPlanExe 			= new ArrayList<PlanExeVO>();
	private List<PlanProrrogaVO> listPlanProrroga 	= new ArrayList<PlanProrrogaVO>();
	private List<PlanImpMinVO> listPlanImpMin 		= new ArrayList<PlanImpMinVO>();
	private List<PlanRecursoVO> listPlanRecurso		= new ArrayList<PlanRecursoVO>();
	// Buss Flags
		
	// View Constants
		
	private String canCuoAImpEnFormView = "";
	private String canMaxCuoView = "";
	private String canMinCuoParCuoSalView = "";
	private String canMinPerView = "";
	private String cuoDesParaRecView = "";
	private String fecVenDeuDesView = "";
	private String fecVenDeuHasView = "";
	private String fechaAltaView = "";
	private String fechaBajaView = "";
	private String impMinCuoView = "";
	private String impMinDeuView = "";


	// Constructores
	public PlanVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public PlanVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesPlan(desc);
	}

	// Getters y Setters
	
	public ViaDeudaVO getViaDeuda() {
		return viaDeuda;
	}

	public void setViaDeuda(ViaDeudaVO viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

	public ProcuradorVO getProcurador() {
		return procurador;
	}

	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}

	public TipoDeudaPlanVO getTipoDeudaPlan() {
		return tipoDeudaPlan;
	}

	public void setTipoDeudaPlan(TipoDeudaPlanVO tipoDeudaPlan) {
		this.tipoDeudaPlan = tipoDeudaPlan;
	}

	public SiNo getAplicaTotalImpago() {
		return aplicaTotalImpago;
	}

	public void setAplicaTotalImpago(SiNo aplicaTotalImpago) {
		this.aplicaTotalImpago = aplicaTotalImpago;
	}

	public Integer getCanCuoAImpEnForm() {
		return canCuoAImpEnForm;
	}

	public void setCanCuoAImpEnForm(Integer canCuoAImpEnForm) {
		this.canCuoAImpEnForm = canCuoAImpEnForm;
		this.canCuoAImpEnFormView = StringUtil.formatInteger(canCuoAImpEnForm);
	}

	public Integer getCanMaxCuo() {
		return canMaxCuo;
	}

	public void setCanMaxCuo(Integer canMaxCuo) {
		this.canMaxCuo = canMaxCuo;
		this.canMaxCuoView = StringUtil.formatInteger(canMaxCuo);
	}

	public Integer getCanMinCuoParCuoSal() {
		return canMinCuoParCuoSal;
	}

	public void setCanMinCuoParCuoSal(Integer canMinCuoParCuoSal) {
		this.canMinCuoParCuoSal = canMinCuoParCuoSal;
		this.canMinCuoParCuoSalView = StringUtil.formatInteger(canMinCuoParCuoSal);
	}

	public Integer getCanMinPer() {
		return canMinPer;
	}

	public void setCanMinPer(Integer canMinPer) {
		this.canMinPer = canMinPer;
		this.canMinPerView = StringUtil.formatInteger(canMinPer);
	}

	public CasoVO getCaso() {
		return caso;
	}

	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}

	public Integer getCuoDesParaRec() {
		return cuoDesParaRec;
	}

	public void setCuoDesParaRec(Integer cuoDesParaRec) {
		this.cuoDesParaRec = cuoDesParaRec;
		this.cuoDesParaRecView = StringUtil.formatInteger(cuoDesParaRec);
	}

	public String getDesPlan() {
		return desPlan;
	}

	public void setDesPlan(String desPlan) {
		this.desPlan = desPlan;
	}

	public SiNo getEsManual() {
		return esManual;
	}

	public void setEsManual(SiNo esManual) {
		this.esManual = esManual;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
		this.fechaAltaView = DateUtil.formatDate(fechaAlta, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
		this.fechaBajaView = DateUtil.formatDate(fechaBaja, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFecVenDeuDes() {
		return fecVenDeuDes;
	}

	public void setFecVenDeuDes(Date fecVenDeuDes) {
		this.fecVenDeuDes = fecVenDeuDes;
		this.fecVenDeuDesView = DateUtil.formatDate(fecVenDeuDes, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFecVenDeuHas() {
		return fecVenDeuHas;
	}

	public void setFecVenDeuHas(Date fecVenDeuHas) {
		this.fecVenDeuHas = fecVenDeuHas;
		this.fecVenDeuHasView = DateUtil.formatDate(fecVenDeuHas, DateUtil.ddSMMSYYYY_MASK);
	}

	public Double getImpMinCuo() {
		return impMinCuo;
	}

	public void setImpMinCuo(Double impMinCuo) {
		this.impMinCuo = impMinCuo;
		this.impMinCuoView = StringUtil.formatDouble(impMinCuo);
	}

	public Double getImpMinDeu() {
		return impMinDeu;
	}

	public void setImpMinDeu(Double impMinDeu) {
		this.impMinDeu = impMinDeu;
		this.impMinDeuView = StringUtil.formatDouble(impMinDeu);
	}

	public String getLeyendaPlan() {
		return leyendaPlan;
	}

	public void setLeyendaPlan(String leyendaPlan) {
		this.leyendaPlan = leyendaPlan;
	}

	public String getLinkNormativa() {
		return linkNormativa;
	}

	public void setLinkNormativa(String linkNormativa) {
		this.linkNormativa = linkNormativa;
	}

	public SiNo getPoseeActEsp() {
		return poseeActEsp;
	}

	public void setPoseeActEsp(SiNo poseeActEsp) {
		this.poseeActEsp = poseeActEsp;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public SistemaVO getSistema() {
		return sistema;
	}

	public void setSistema(SistemaVO sistema) {
		this.sistema = sistema;
	}
		
	public SiNo getAplicaPagCue() {
		return aplicaPagCue;
	}

	public void setAplicaPagCue(SiNo aplicaPagCue) {
		this.aplicaPagCue = aplicaPagCue;
	}

	public FormularioVO getFormulario() {
		return formulario;
	}

	public void setFormulario(FormularioVO formulario) {
		this.formulario = formulario;
	}

	public String getNameSequence() {
		return nameSequence;
	}

	public void setNameSequence(String nameSequence) {
		this.nameSequence = nameSequence;
	}

	public String getLeyendaForm() {
		return leyendaForm;
	}

	public void setLeyendaForm(String leyendaForm) {
		this.leyendaForm = leyendaForm;
	}

	public String getOrdenanza() {
		return ordenanza;
	}

	public void setOrdenanza(String ordenanza) {
		this.ordenanza = ordenanza;
	}

	public List<PlanAtrValVO> getListPlanAtrVal() {
		return listPlanAtrVal;
	}
	public void setListPlanAtrVal(List<PlanAtrValVO> listPlanAtrVal) {
		this.listPlanAtrVal = listPlanAtrVal;
	}

	public List<PlanClaDeuVO> getListPlanClaDeu() {
		return listPlanClaDeu;
	}
	public void setListPlanClaDeu(List<PlanClaDeuVO> listPlanClaDeu) {
		this.listPlanClaDeu = listPlanClaDeu;
	}

	public List<PlanDescuentoVO> getListPlanDescuento() {
		return listPlanDescuento;
	}
	public void setListPlanDescuento(List<PlanDescuentoVO> listPlanDescuento) {
		this.listPlanDescuento = listPlanDescuento;
	}

	public List<PlanExeVO> getListPlanExe() {
		return listPlanExe;
	}
	public void setListPlanExe(List<PlanExeVO> listPlanExe) {
		this.listPlanExe = listPlanExe;
	}

	public List<PlanForActDeuVO> getListPlanForActDeu() {
		return listPlanForActDeu;
	}
	public void setListPlanForActDeu(List<PlanForActDeuVO> listPlanForActDeu) {
		this.listPlanForActDeu = listPlanForActDeu;
	}

	public List<PlanIntFinVO> getListPlanIntFin() {
		return listPlanIntFin;
	}
	public void setListPlanIntFin(List<PlanIntFinVO> listPlanIntFin) {
		this.listPlanIntFin = listPlanIntFin;
	}

	public List<PlanMotCadVO> getListPlanMotCad() {
		return listPlanMotCad;
	}
	public void setListPlanMotCad(List<PlanMotCadVO> listPlanMotCad) {
		this.listPlanMotCad = listPlanMotCad;
	}

	public List<PlanProrrogaVO> getListPlanProrroga() {
		return listPlanProrroga;
	}
	public void setListPlanProrroga(List<PlanProrrogaVO> listPlanProrroga) {
		this.listPlanProrroga = listPlanProrroga;
	}

	public List<PlanVenVO> getListPlanVen() {
		return listPlanVen;
	}
	public void setListPlanVen(List<PlanVenVO> listPlanVen) {
		this.listPlanVen = listPlanVen;
	}
	
	public List<PlanImpMinVO> getListPlanImpMin() {
		return listPlanImpMin;
	}
	
	public void setListPlanImpMin(List<PlanImpMinVO> listPlanImpMin) {
		this.listPlanImpMin = listPlanImpMin;
	}
	
	public List<PlanRecursoVO> getListPlanRecurso() {
		return listPlanRecurso;
	}

	public void setListPlanRecurso(List<PlanRecursoVO> listPlanRecurso) {
		this.listPlanRecurso = listPlanRecurso;
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	



	// View getters
	public void setCanCuoAImpEnFormView(String canCuoAImpEnFormView) {
		this.canCuoAImpEnFormView = canCuoAImpEnFormView;
	}
	public String getCanCuoAImpEnFormView() {
		return canCuoAImpEnFormView;
	}

	public void setCanMaxCuoView(String canMaxCuoView) {
		this.canMaxCuoView = canMaxCuoView;
	}
	public String getCanMaxCuoView() {
		return canMaxCuoView;
	}

	public void setCanMinCuoParCuoSalView(String canMinCuoParCuoSalView) {
		this.canMinCuoParCuoSalView = canMinCuoParCuoSalView;
	}
	public String getCanMinCuoParCuoSalView() {
		return canMinCuoParCuoSalView;
	}

	public void setCanMinPerView(String canMinPerView) {
		this.canMinPerView = canMinPerView;
	}
	public String getCanMinPerView() {
		return canMinPerView;
	}

	public void setCuoDesParaRecView(String cuoDesParaRecView) {
		this.cuoDesParaRecView = cuoDesParaRecView;
	}
	public String getCuoDesParaRecView() {
		return cuoDesParaRecView;
	}

	public void setFecVenDeuDesView(String fecVenDeuDesView) {
		this.fecVenDeuDesView = fecVenDeuDesView;
	}
	public String getFecVenDeuDesView() {
		return fecVenDeuDesView;
	}

	public void setFecVenDeuHasView(String fecVenDeuHasView) {
		this.fecVenDeuHasView = fecVenDeuHasView;
	}
	public String getFecVenDeuHasView() {
		return fecVenDeuHasView;
	}

	public void setFechaAltaView(String fechaAltaView) {
		this.fechaAltaView = fechaAltaView;
	}
	public String getFechaAltaView() {
		return fechaAltaView;
	}

	public void setFechaBajaView(String fechaBajaView) {
		this.fechaBajaView = fechaBajaView;
	}
	public String getFechaBajaView() {
		return fechaBajaView;
	}

	public void setImpMinCuoView(String impMinCuoView) {
		this.impMinCuoView = impMinCuoView;
	}
	public String getImpMinCuoView() {
		return impMinCuoView;
	}

	public void setImpMinDeuView(String impMinDeuView) {
		this.impMinDeuView = impMinDeuView;
	}
	public String getImpMinDeuView() {
		return impMinDeuView;
	}
	public String getDesPlanVia(){
		String desPlanVia = desPlan+ " - ";
		
		if(getId().longValue()==-1){
			return desPlan;
		}
		
		if(getViaDeuda()!=null)
			desPlanVia += getViaDeuda().getDesViaDeuda();
		else
			desPlanVia += "error";
		return desPlanVia;
	}

	/**
	 * Si el link tiene mas de 60 caracteres lo trunca y le agrega puntos suspesivos.
	 * 
	 * @author Cristian
	 * @return
	 */
	public String getLinkNormativaView(){
		if (StringUtil.isNullOrEmpty(getLinkNormativa())){ 
			return getLinkNormativa();
		} else if (getLinkNormativa().length() <= 60 ){
			return getLinkNormativa();
		} else {
			return getLinkNormativa().substring(0, 60) + " ...";
		}
	}
}
