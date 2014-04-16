//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del Obra
 * @author tecso
 *
 */
public class ObraVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "obraVO";
	
	private String 		desObra = "";
	private Integer 	numeroObra;
	private EstadoObraVO estadoObra = new EstadoObraVO();
	private SiNo 		permiteCamPlaMay = SiNo.OpcionSelecionar;
	private SiNo 		esPorValuacion = SiNo.OpcionSelecionar;
	private SiNo 		esCostoEsp = SiNo.OpcionSelecionar;
	private Double 		costoEsp;

	//Leyendas
 	private String leyCon;
	private String leyPriCuo;
	private String leyResCuo;
	private String leyCamPla;

	private CasoVO 		caso = new CasoVO();
	private RecursoVO   recurso = new RecursoVO();
	// no estan en el bean
	private Double montoMinimoCuota;
	private Double interesFinanciero;
	
	private List<ObraFormaPagoVO> listObraFormaPago = new ArrayList<ObraFormaPagoVO>();
	private List<PlanillaCuadraVO>  listPlanillaCuadra = new ArrayList<PlanillaCuadraVO>();	
	private List<ObrRepVenVO>  listObrRepVen = new ArrayList<ObrRepVenVO>();
	private List<HisEstadoObraVO> listHisEstadoObra = new ArrayList<HisEstadoObraVO>();
	
	private String	numeroObraView = "";
	private String  costoEspView = "";
	private String  fechaDesdeView = "";	
	private String 	fechaHastaView = "";
	private String  montoMinimoCuotaView = "";
	private String  interesFinancieroView = "";
	
	// Buss Flags
	private Boolean emitirInformeBussEnabled = true;
	private Boolean cambiarEstadoBussEnabled = true;
	private Boolean asignarRepartidorBussEnabled = true;	
	
	// Monto Total a Pagar de la Obra
	Double montoTotal;
	String montoTotalView = "";
	// Total de Cuentas de la Obra
	Integer totalCuentas;
	String totalCuentasView = "";


	// Constructores
	public ObraVO() {
		super();
	}

	public ObraVO(long id, String desObra){
		super();
		setId(id);
		setDesObra(desObra);
	}
	
	// Getters y Setters
	public CasoVO getCaso() {
		return caso;
	}

	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}

	public String getDesObra() {
		return desObra;
	}
	
	public void setDesObra(String desObra) {
		this.desObra = desObra;
	}

	public SiNo getPermiteCamPlaMay() {
		return permiteCamPlaMay;
	}

	public void setPermiteCamPlaMay(SiNo permiteCamPlaMay) {
		this.permiteCamPlaMay = permiteCamPlaMay;
	}

	public EstadoObraVO getEstadoObra() {
		return estadoObra;
	}

	public void setEstadoObra(EstadoObraVO estadoObra) {
		this.estadoObra = estadoObra;
	}


	public Integer getNumeroObra() {
		return numeroObra;
	}

	public void setNumeroObra(Integer numeroObra) {
		this.numeroObra = numeroObra;
		this.numeroObraView = StringUtil.formatInteger(numeroObra);
	}

	public List<ObraFormaPagoVO> getListObraFormaPago() {
		return listObraFormaPago;
	}
	public void setListObraFormaPago(List<ObraFormaPagoVO> listObraFormaPago) {
		this.listObraFormaPago = listObraFormaPago;
	}

	public List<HisEstadoObraVO> getListHisEstadoObra() {
		return listHisEstadoObra;
	}

	public void setListHisEstadoObra(List<HisEstadoObraVO> listHisEstadoObra) {
		this.listHisEstadoObra = listHisEstadoObra;
	}

	public Double getMontoMinimoCuota() {
		return montoMinimoCuota;
	}

	public void setMontoMinimoCuota(Double montoMinimoCuota) {
		this.montoMinimoCuota = montoMinimoCuota;
		this.montoMinimoCuotaView = StringUtil.formatDouble(montoMinimoCuota);
	}

	public Double getInteresFinanciero() {
		return interesFinanciero;
	}

	public void setInteresFinanciero(Double interesFinanciero) {
		this.interesFinanciero = interesFinanciero;
		this.interesFinancieroView = StringUtil.formatDouble(interesFinanciero);
	}
	
	public String getLeyCamPla() {
		return leyCamPla;
	}

	public void setLeyCamPla(String leyCamPla) {
		this.leyCamPla = leyCamPla;
	}

	public String getLeyCon() {
		return leyCon;
	}

	public void setLeyCon(String leyCon) {
		this.leyCon = leyCon;
	}

	public String getLeyPriCuo() {
		return leyPriCuo;
	}

	public void setLeyPriCuo(String leyPriCuo) {
		this.leyPriCuo = leyPriCuo;
	}

	public String getLeyResCuo() {
		return leyResCuo;
	}

	public void setLeyResCuo(String leyResCuo) {
		this.leyResCuo = leyResCuo;
	}

	public String getDesObraConNumeroObra() {
		String desObraConNumeroObra = this.getDesObra();
		
		//Si descripcion es TODOS, no agregamos nada 
		if (!ModelUtil.isNullOrEmpty(this)) {
			// (Nro. Obra) - DesObra
			desObraConNumeroObra = 
				"(" + StringUtil.completarCerosIzq(this.getNumeroObra().toString(), 4) + ") - " + desObraConNumeroObra;
		}
		
		return desObraConNumeroObra;
	}
	
	//	 Buss flags getters y setters
	public List<PlanillaCuadraVO> getListPlanillaCuadra() {
		return listPlanillaCuadra;
	}

	public void setListPlanillaCuadra(List<PlanillaCuadraVO> listPlanillaCuadra) {
		this.listPlanillaCuadra = listPlanillaCuadra;
	}
	
	public List<ObrRepVenVO> getListObrRepVen() {
		return listObrRepVen;
	}

	public void setListObrRepVen(List<ObrRepVenVO> listObrRepVen) {
		this.listObrRepVen = listObrRepVen;
	}

	public Boolean getCambiarEstadoBussEnabled() {
		return cambiarEstadoBussEnabled;
	}

	public void setCambiarEstadoBussEnabled(Boolean cambiarEstadoBussEnabled) {
		this.cambiarEstadoBussEnabled = cambiarEstadoBussEnabled;
	}
	
	public Boolean getEmitirInformeBussEnabled() {
		return emitirInformeBussEnabled;
	}
	public void setEmitirInformeBussEnabled(Boolean emitirInformeBussEnabled) {
		this.emitirInformeBussEnabled = emitirInformeBussEnabled;
	}
	
	public Boolean getAsignarRepartidorBussEnabled() {
		return asignarRepartidorBussEnabled;
	}

	public void setAsignarRepartidorBussEnabled(Boolean asignarRepartidorBussEnabled) {
		this.asignarRepartidorBussEnabled = asignarRepartidorBussEnabled;
	}
	
	public Double getCostoEsp() {
		return costoEsp;
	}

	public void setCostoEsp(Double costoEsp) {
		this.costoEsp = costoEsp;
		this.costoEspView = StringUtil.formatDouble(costoEsp);
	}

	public SiNo getEsCostoEsp() {
		return esCostoEsp;
	}

	public void setEsCostoEsp(SiNo esCostoEsp) {
		this.esCostoEsp = esCostoEsp;
	}

	public SiNo getEsPorValuacion() {
		return esPorValuacion;
	}

	public void setEsPorValuacion(SiNo esPorValuacion) {
		this.esPorValuacion = esPorValuacion;
	}

	// View getters y Setters
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

	public String getNumeroObraView() {
		return numeroObraView;
	}
	public void setNumeroObraView(String numeroObraView) {
		this.numeroObraView = numeroObraView;
	}

	public String getMontoMinimoCuotaView() {
		return montoMinimoCuotaView;
	}

	public void setMontoMinimoCuotaView(String montoMinimoCuotaView) {
		this.montoMinimoCuotaView = montoMinimoCuotaView;
	}

	public String getInteresFinancieroView() {
		return interesFinancieroView;
	}

	public void setInteresFinancieroView(String interesFinancieroView) {
		this.interesFinancieroView = interesFinancieroView;
	}

	public String getCostoEspView() {
		return costoEspView;
	}

	public void setCostoEspView(String costoEspView) {
		this.costoEspView = costoEspView;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	
	public Long getIdRecurso() {
		Long idRecurso = this.getRecurso().getId();
		if (idRecurso == null) {
			idRecurso = new Long(-1);
		}
		return idRecurso;
	}
	
	public boolean getCostoEspEnabled() {
		return this.getEsCostoEsp().equals(SiNo.SI);
	}

	public boolean getEsCostoEspEnabled() {
		return this.getEsPorValuacion().equals(SiNo.SI);
	}

	public Double getMontoTotal() {
		return montoTotal;
	}

/*	public void setMontoTotal(Double montoTotal) {
		this.montoTotal = montoTotal;
		this.montoTotalView = StringUtil.redondearDecimales(montoTotal, 1, 2);
	}

	public String getMontoTotalView() {
		return montoTotalView;
	}

	public void setMontoTotalView(String montoTotalView) {
		this.montoTotalView = montoTotalView;
	}

	public Integer getTotalCuentas() {
		return totalCuentas;
	}

	public void setTotalCuentas(Integer totalCuentas) {
		this.totalCuentas = totalCuentas;
		this.totalCuentasView = StringUtil.formatInteger(totalCuentas);
	}

	public String getTotalCuentasView() {
		return totalCuentasView;
	}

	public void setTotalCuentasView(String totalCuentasView) {
		this.totalCuentasView = totalCuentasView;
	}*/

}