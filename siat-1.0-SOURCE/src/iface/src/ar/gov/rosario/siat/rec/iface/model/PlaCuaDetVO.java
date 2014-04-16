//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del PlaCuaDet
 * @author tecso
 *
 */
public class PlaCuaDetVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "plaCuaDetVO";

	private PlanillaCuadraVO planillaCuadra = new PlanillaCuadraVO();
	private CuentaVO cuentaTGI = new CuentaVO();
	private CuentaVO cuentaCdM = new CuentaVO();
	private Double cantidadMetros;
	private Double cantidadUnidades;
	private EstPlaCuaDetVO estPlaCuaDet = new EstPlaCuaDetVO();
	private TipPlaCuaDetVO tipPlaCuaDet = new TipPlaCuaDetVO();
	private Double valuacionTerreno;
	private Double radio;
	private String agrupador;
	private PlaCuaDetVO plaCuaDet; // no hacer new por la recursividad
	private Double porcPH;
	private Date fechaIncorporacion;
	private Date fechaUltMdfDatos;
	private Date fechaUltCmbOI;
	private Date fechaEmision;
	private Double importeTotal;
	private Integer usoCatastro;
	private UsoCdMVO usoCdM = new UsoCdMVO();
	private ObraFormaPagoVO obrForPag = new ObraFormaPagoVO();
	private Integer cantCuotas;
	private Date fechaForm;
	
	// lista de detalles hijos en el caso que
	// el detalle sea una carpeta
	private List<PlaCuaDetVO> listPlaCuaDet = new ArrayList<PlaCuaDetVO>();

	// view
	private String cantidadMetrosView;
	private String cantidadUnidadesView;
	private String valuacionTerrenoView;
	private String radioView;
	private String porcPHView;
	private String fechaIncorporacionView;
	private String fechaUltMdfDatosView;
	private String fechaUltCmbOIView;
	private String fechaEmisionView;
	private String importeTotalView;
	private String usoCatastroView;
	private String cantCuotasView;
	private String fechaFormView;
	private String ubicacionFinca;
	private String leyendaSeleccion;	

	// Buss Flags
	private boolean seleccionarBussEnabled= true;
	private boolean esConsistente = true;
	// View Constants
	
	
	// Constructores
	public PlaCuaDetVO() {
		super();
	}
	
	public PlaCuaDetVO(long id, String numeroCuenta) {
		super();
		setId(id);
		getCuentaCdM().setNumeroCuenta(numeroCuenta);
	}
	
	// Getters y Setters	
	public PlanillaCuadraVO getPlanillaCuadra() {
		return planillaCuadra;
	}

	public void setPlanillaCuadra(PlanillaCuadraVO planillaCuadra) {
		this.planillaCuadra = planillaCuadra;
	}

	public CuentaVO getCuentaTGI() {
		return cuentaTGI;
	}

	public void setCuentaTGI(CuentaVO cuentaTGI) {
		this.cuentaTGI = cuentaTGI;
	}

	public CuentaVO getCuentaCdM() {
		return cuentaCdM;
	}

	public void setCuentaCdM(CuentaVO cuentaCdM) {
		this.cuentaCdM = cuentaCdM;
	}

	public Double getCantidadMetros() {
		return cantidadMetros;
	}

	public void setCantidadMetros(Double cantidadMetros) {
		this.cantidadMetros = cantidadMetros;
		this.cantidadMetrosView = StringUtil.formatDouble(cantidadMetros);
	}

	public Double getCantidadUnidades() {
		return cantidadUnidades;
	}

	public void setCantidadUnidades(Double cantidadUnidades) {
		this.cantidadUnidades = cantidadUnidades;
		this.cantidadUnidadesView = StringUtil.formatDouble(cantidadUnidades);		
	}

	public EstPlaCuaDetVO getEstPlaCuaDet() {
		return estPlaCuaDet;
	}

	public void setEstPlaCuaDet(EstPlaCuaDetVO estPlaCuaDet) {
		this.estPlaCuaDet = estPlaCuaDet;
	}

	public String getCantidadMetrosView() {
		return cantidadMetrosView;
	}

	public void setCantidadMetrosView(String cantidadMetrosView) {
		this.cantidadMetrosView = cantidadMetrosView;
	}

	public String getCantidadUnidadesView() {
		return cantidadUnidadesView;
	}

	public void setCantidadUnidadesView(String cantidadUnidadesView) {
		this.cantidadUnidadesView = cantidadUnidadesView;
	}

	public TipPlaCuaDetVO getTipPlaCuaDet() {
		return tipPlaCuaDet;
	}

	public void setTipPlaCuaDet(TipPlaCuaDetVO tipPlaCuaDet) {
		this.tipPlaCuaDet = tipPlaCuaDet;
	}

	public Double getValuacionTerreno() {
		return valuacionTerreno;
	}

	public void setValuacionTerreno(Double valuacionTerreno) {
		this.valuacionTerreno = valuacionTerreno;
		this.valuacionTerrenoView = StringUtil.formatDouble(valuacionTerreno);
	}
	
	public Double getRadio() {
		return radio;
	}

	public void setRadio(Double radio) {
		this.radio = radio;
		this.radioView = StringUtil.formatDouble(radio);
	}

	public String getAgrupador() {
		return agrupador;
	}

	public void setAgrupador(String agrupador) {
		this.agrupador = agrupador;
	}

	public PlaCuaDetVO getPlaCuaDet() {
		return plaCuaDet;
	}

	public void setPlaCuaDet(PlaCuaDetVO plaCuaDet) {
		this.plaCuaDet = plaCuaDet;
	}

	public Double getPorcPH() {
		return porcPH;
	}

	public void setPorcPH(Double porcPH) {
		this.porcPH = porcPH;
		this.porcPHView = StringUtil.formatDouble(porcPH);
	}

	public Date getFechaIncorporacion() {
		return fechaIncorporacion;
	}

	public void setFechaIncorporacion(Date fechaIncorporacion) {
		this.fechaIncorporacion = fechaIncorporacion;
		this.fechaIncorporacionView = DateUtil.formatDate
			(fechaIncorporacion, DateUtil.ddSMMSYYYY_HH_MM_MASK);
	}

	public Date getFechaUltMdfDatos() {
		return fechaUltMdfDatos;
	}

	public void setFechaUltMdfDatos(Date fechaUltMdfDatos) {
		this.fechaUltMdfDatos = fechaUltMdfDatos;
		this.fechaUltMdfDatosView = DateUtil.formatDate
			(fechaUltMdfDatos, DateUtil.ddSMMSYYYY_HH_MM_MASK);		
	}

	public Date getFechaUltCmbOI() {
		return fechaUltCmbOI;
	}

	public void setFechaUltCmbOI(Date fechaUltCmbOI) {
		this.fechaUltCmbOI = fechaUltCmbOI;
		this.fechaUltCmbOIView = DateUtil.formatDate
			(fechaUltCmbOI, DateUtil.ddSMMSYYYY_HH_MM_MASK);		
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
		this.fechaEmisionView = DateUtil.formatDate
			(fechaEmision, DateUtil.ddSMMSYYYY_HH_MM_MASK);		
	}

	public String getValuacionTerrenoView() {
		return valuacionTerrenoView;
	}

	public void setValuacionTerrenoView(String valuacionTerrenoView) {
		this.valuacionTerrenoView = valuacionTerrenoView;
	}
	
	public String getRadioView() {
		return radioView;
	}

	public void setRadioView(String radioView) {
		this.radioView = radioView;
	}

	public String getPorcPHView() {
		return porcPHView;
	}

	public void setPorcPHView(String porcPHView) {
		this.porcPHView = porcPHView;
	}

	public String getFechaIncorporacionView() {
		return fechaIncorporacionView;
	}

	public void setFechaIncorporacionView(String fechaIncorporacionView) {
		this.fechaIncorporacionView = fechaIncorporacionView;
	}

	public String getFechaUltMdfDatosView() {
		return fechaUltMdfDatosView;
	}

	public void setFechaUltMdfDatosView(String fechaUltMdfDatosView) {
		this.fechaUltMdfDatosView = fechaUltMdfDatosView;
	}

	public String getFechaUltCmbOIView() {
		return fechaUltCmbOIView;
	}

	public void setFechaUltCmbOIView(String fechaUltCmbOIView) {
		this.fechaUltCmbOIView = fechaUltCmbOIView;
	}

	public String getFechaEmisionView() {
		return fechaEmisionView;
	}

	public void setFechaEmisionView(String fechaEmisionView) {
		this.fechaEmisionView = fechaEmisionView;
	}

	public List<PlaCuaDetVO> getListPlaCuaDet() {
		return listPlaCuaDet;
	}

	public void setListPlaCuaDet(List<PlaCuaDetVO> listPlaCuaDet) {
		this.listPlaCuaDet = listPlaCuaDet;
	}

	public String getUbicacionFinca() {
		return ubicacionFinca;
	}

	public void setUbicacionFinca(String ubicacionFinca) {
		this.ubicacionFinca = ubicacionFinca;
	}

	public String getLeyendaSeleccion() {
		return leyendaSeleccion;
	}

	public void setLeyendaSeleccion(String leyendaSeleccion) {
		this.leyendaSeleccion = leyendaSeleccion;
	}

	public Integer getCantCuotas() {
		return cantCuotas;
	}

	public void setCantCuotas(Integer cantCuotas) {
		this.cantCuotas = cantCuotas;
		this.cantCuotasView = StringUtil.formatInteger(cantCuotas);
	}

	public String getCantCuotasView() {
		return cantCuotasView;
	}

	public void setCantCuotasView(String cantCuotasView) {
		this.cantCuotasView = cantCuotasView;
	}

	public Date getFechaForm() {
		return fechaForm;
	}

	public void setFechaForm(Date fechaForm) {
		this.fechaForm = fechaForm;
		this.fechaFormView = DateUtil.formatDate
		(fechaForm, DateUtil.ddSMMSYYYY_HH_MM_MASK);
	}

	public String getFechaFormView() {
		return fechaFormView;
	}

	public void setFechaFormView(String fechaFormView) {
		this.fechaFormView = fechaFormView;
	}

	public Double getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(Double importeTotal) {
		this.importeTotal = importeTotal;
		this.importeTotalView = StringUtil.formatDouble(importeTotal);
	}

	public String getImporteTotalView() {
		return importeTotalView;
	}

	public void setImporteTotalView(String importeTotalView) {
		this.importeTotalView = importeTotalView;
	}

	public ObraFormaPagoVO getObrForPag() {
		return obrForPag;
	}

	public void setObrForPag(ObraFormaPagoVO obrForPag) {
		this.obrForPag = obrForPag;
	}

	public Integer getUsoCatastro() {
		return usoCatastro;
	}

	public void setUsoCatastro(Integer usoCatastro) {
		this.usoCatastro = usoCatastro;
		this.usoCatastroView = StringUtil.formatInteger(usoCatastro);
	}

	public String getUsoCatastroView() {
		return usoCatastroView;
	}

	public void setUsoCatastroView(String usoCatastroView) {
		this.usoCatastroView = usoCatastroView;
	}

	public UsoCdMVO getUsoCdM() {
		return usoCdM;
	}

	public void setUsoCdM(UsoCdMVO usoCdM) {
		this.usoCdM = usoCdM;
	}

	// Buss flags getters y setters
	public boolean getSeleccionarBussEnabled() {
		return seleccionarBussEnabled;
	}
	
	public void setSeleccionarBussEnabled(boolean seleccionarBussEnabled) {
		this.seleccionarBussEnabled = seleccionarBussEnabled;
	}

	public boolean getEsConsistente() {
		return esConsistente;
	}

	public void setEsConsistente(boolean esConsistente) {
		this.esConsistente = esConsistente;
	}

	/** Usado en la jsp para ver la catastral o carpeta seleccionada
	 * 
	 */
	public String getIdSelect() {
		String idSeleccion = getCuentaTGI().getObjImp().getClaveFuncional();
		if (!StringUtil.isNullOrEmpty(getAgrupador())) {
			idSeleccion = getAgrupador();
		}

		return idSeleccion.trim();
	}
	
	public boolean getIsCarpeta() {
		boolean isAgrupado = true;
		if (StringUtil.isNullOrEmpty(agrupador)) {
			isAgrupado = false;
		}
		return isAgrupado;
	}
	
	public String getTipPlaCuaDetView() {
		String tipoDetalle = this.getTipPlaCuaDet().getDesTipPlaCuaDet();
		
		if (this.getIsCarpeta()) {
			tipoDetalle = tipoDetalle + ": " + this.getAgrupador();
		}
		
		return tipoDetalle;
	}

}
