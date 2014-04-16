//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.Sexo;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class DesgloseAjusteAdapter extends SiatAdapterModel {

	public static final String NAME = "desgloseAjusteAdapterVO";
	private static final long serialVersionUID = 1L;
	
	private LiqCuentaVO cuenta = new LiqCuentaVO();
	private List<LiqDeudaVO> listDeuda = new ArrayList<LiqDeudaVO>();
	private Double total = 0D; // Total que suma los periodos de deuda seleccionados
	private List<Date> listFechasForm = new ArrayList<Date>();
		  
	private String fechaFormSelected = ""; // Fecha probable de formalizacion seleccionada
	private List<LiqPlanVO> listPlan = new ArrayList<LiqPlanVO>(); // Lista de planes disponibles para la formalizacion
	private LiqConvenioVO convenio = new LiqConvenioVO(); // Soporte para mostrar los datos del convenio a formalizar
	
	private LiqPlanVO planSelected = new LiqPlanVO();
	private String nroCuotaSelected = "";	
	
    // Lista para dibujar los combos en la ultima pantalla.
    private List<TipoPerForVO> listTipoPerFor = new ArrayList<TipoPerForVO>();
	private List<TipoDocApoVO> listTipoDocApo = new ArrayList<TipoDocApoVO>();

	// Lista de ids de Dedua seleccionada
	private String[] listIdDeudaSelected;
	
	// Lista de ids de Cuota Seleccionada para la reimpresion.
	private String[] listIdCuotaSelected;
	
	

	
	// Propiedades para la formalizacion de Plan Manual
	private Date fechaFormalizacion;
	private Integer cantMaxCuo;
	private Double impMinCuo;
	private Double descCapital;
	private Double descActualizacion;
	private Double interes;
	private Date venPrimeraCuota;
	private Double importeAnticipo;
	private Boolean esEspecial=false;
	private Date 	fechaLimite;
	private String 	observacion="";
	private DesgloseVO 	desglose= new DesgloseVO();
	
	private String 	fechaLimiteView="";
	private String fechaFormalizacionView="";
	private String cantMaxCuoView="";
	private String impMinCuoView="";
	private String descCapitalView="";
	private String descActualizacionView="";
	private String interesView="";
	private String venPrimeraCuotaView="";
	private String importeAnticipoView;
	private boolean tieneSellado=false;
	private String importeSelladoView="";
	
	
    // Propiedades para la asignacion de permisos
    private boolean verDeudaContribEnabled = false; // Poder ver el resto de las cuentas de un contribuyente  
    private boolean verCuentaEnabled = false;		// Poder ver desgloses y unificaciones o cuentas relacionadas
    private boolean verConvenioEnabled = false; 	// Poder ver detalles de convenios
    private boolean verCuentaDesgUnifEnabled = false;  // Poder ver desgloses y unificaciones
    private boolean verCuentaRelEnabled = false; // Poder Ver Cuentas Relacionadas al objeto Imponible   
    private boolean verDetalleObjImpEnabled = false;    // Permiso para ver el Detalle del Objeto Imponible
    private boolean verHistoricoContribEnabled = false;	// Permiso para ver el Historico de los Contribuyentes de la cuenta
    private boolean poseeDeudaProcurador = false;
    private boolean buzonCambiosEnabled = false; 		// Permiso para ir al buzon de cambio de datos de persona.
    
    
    private List<Sexo> listSexo = new ArrayList<Sexo>();
    
    
	// Constructores
	public DesgloseAjusteAdapter() {
		super(GdeSecurityConstants.MTD_DESGLOSE_AJUSTE);		
	}

	
    //  Getters y Setters
	
	public Date getFechaFormalizacion(){
		return fechaFormalizacion;
	}
	public void setFechaFormalizacion(Date fechaFormalizacion){
		this.fechaFormalizacion= fechaFormalizacion;
		this.fechaFormalizacionView=DateUtil.formatDate(fechaFormalizacion, DateUtil.ddSMMSYYYY_MASK);
	}
	public Integer getCantMaxCuo(){
		return cantMaxCuo;
	}
	public void setCantMaxCuo(Integer cantMaxCuo){
		this.cantMaxCuo = cantMaxCuo;
		this.cantMaxCuoView= StringUtil.formatInteger(cantMaxCuo);
	}
	public Double getImpMinCuo(){
		return impMinCuo;
	}
	public void setImpMinCuo (Double impMinCuo){
		this.impMinCuo = impMinCuo;
		this.impMinCuoView= StringUtil.formatDouble(impMinCuo);
	}
	public Double getDescCapital(){
		return descCapital;
	}
	public void setDescCapital (Double descCapital){
		this.descCapital=descCapital;
		this.descCapitalView = StringUtil.formatDouble(descCapital);
	}
	public Double getDescActualizacion (){
		return descActualizacion;
	}
	public void setDescActualizacion(Double descActualizacion){
		this.descActualizacion = descActualizacion;
		this.descActualizacionView= StringUtil.formatDouble(descActualizacion);
	}
	public Double getInteres (){
		return interes;
	}
	public void setInteres(Double interes){
		this.interes = interes;
		this.interesView = StringUtil.formatDouble(interes);
	}
	public Date getVenPrimeraCuota(){
		return venPrimeraCuota;
	}
	public void setVenPrimeraCuota(Date venPrimeraCuota){
		this.venPrimeraCuota=venPrimeraCuota;
		this.venPrimeraCuotaView=DateUtil.formatDate(venPrimeraCuota, DateUtil.ddSMMSYYYY_MASK);
	}
	public Double getImporteAnticipo(){
		return importeAnticipo;
	}
	public void setImporteAnticipo(Double importeAnticipo){
		this.importeAnticipo=importeAnticipo;
		this.importeAnticipoView = StringUtil.formatDouble(importeAnticipo);
	}
	public Boolean getEsEspecial(){
		return esEspecial;
	}
	public void setEsEspecial(Boolean esEspecial){
		this.esEspecial= esEspecial;
	}
	
	public LiqCuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(LiqCuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public List<LiqDeudaVO> getListDeuda() {
		return listDeuda;
	}

	public void setListDeuda(List<LiqDeudaVO> listDeuda) {
		this.listDeuda = listDeuda;
	}
	
	public LiqConvenioVO getConvenio() {
		return convenio;
	}
	public void setConvenio(LiqConvenioVO convenio) {
		this.convenio = convenio;
	}

	public String getFechaFormSelected() {
		return fechaFormSelected;
	}
	public void setFechaFormSelected(String fechaFormSelected) {
		this.fechaFormSelected = fechaFormSelected;
	}

	public List<Date> getListFechasForm() {
		return listFechasForm;
	}
	public void setListFechasForm(List<Date> listFechasForm) {
		this.listFechasForm = listFechasForm;
	}

	public List<LiqPlanVO> getListPlan() {
		return listPlan;
	}
	public void setListPlan(List<LiqPlanVO> listPlan) {
		this.listPlan = listPlan;
	}

	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}

	public String[] getListIdDeudaSelected() {
		return listIdDeudaSelected;
	}
	public void setListIdDeudaSelected(String[] listIdDeudaSelected) {
		this.listIdDeudaSelected = listIdDeudaSelected;
	}
	
	public LiqPlanVO getPlanSelected() {
		return planSelected;
	}
	public void setPlanSelected(LiqPlanVO planSelected) {
		this.planSelected = planSelected;
	}
	
	
	public String getNroCuotaSelected() {
		return nroCuotaSelected;
	}
	public void setNroCuotaSelected(String nroCuotaSelected) {
		this.nroCuotaSelected = nroCuotaSelected;
	}
	
	public List<TipoDocApoVO> getListTipoDocApo() {
		return listTipoDocApo;
	}
	public void setListTipoDocApo(List<TipoDocApoVO> listTipoDocApo) {
		this.listTipoDocApo = listTipoDocApo;
	}

	public List<TipoPerForVO> getListTipoPerFor() {
		return listTipoPerFor;
	}
	public void setListTipoPerFor(List<TipoPerForVO> listTipoPerFor) {
		this.listTipoPerFor = listTipoPerFor;
	}


	public boolean isTieneSellado() {
		return tieneSellado;
	}
	public void setTieneSellado(boolean tieneSellado) {
		this.tieneSellado = tieneSellado;
	}
	


	public String getImporteSelladoView() {
		return importeSelladoView;
	}
	public void setImporteSelladoView(String importeSelladoView) {
		this.importeSelladoView = importeSelladoView;
	}

	
	public List<Sexo> getListSexo() {
		return listSexo;
	}
	public void setListSexo(List<Sexo> listSexo) {
		this.listSexo = listSexo;
	}


	// Getters para los permisos
    public boolean isVerConvenioEnabled() {
		return verConvenioEnabled;
	}
	public void setVerConvenioEnabled(boolean verConvenioEnabled) {
		this.verConvenioEnabled = verConvenioEnabled;
	}

	public boolean isVerCuentaEnabled() {
		return verCuentaEnabled;
	}
	public void setVerCuentaEnabled(boolean verCuentaEnabled) {
		this.verCuentaEnabled = verCuentaEnabled;
	}

	public boolean isVerDeudaContribEnabled() {
		return verDeudaContribEnabled;
	}
	public void setVerDeudaContribEnabled(boolean verDeudaContribEnabled) {
		this.verDeudaContribEnabled = verDeudaContribEnabled;
	}
	
	public boolean getPoseeDeudaProcurador() {
		return poseeDeudaProcurador;
	}
	public void setPoseeDeudaProcurador(boolean poseeDeudaProcurador) {
		this.poseeDeudaProcurador = poseeDeudaProcurador;
	}
	
	public boolean getVerCuentaDesgUnifEnabled() {
		return verCuentaDesgUnifEnabled;
	}
	public void setVerCuentaDesgUnifEnabled(boolean verCuentaDesgUnifEnabled) {
		this.verCuentaDesgUnifEnabled = verCuentaDesgUnifEnabled;
	}
	
	public boolean getVerCuentaRelEnabled() {
		return verCuentaRelEnabled;
	}
	public void setVerCuentaRelEnabled(boolean verCuentaRelEnabled) {
		this.verCuentaRelEnabled = verCuentaRelEnabled;
	}

	public boolean isVerDetalleObjImpEnabled() {
		return verDetalleObjImpEnabled;
	}
	public void setVerDetalleObjImpEnabled(boolean verDetalleObjImpEnabled) {
		this.verDetalleObjImpEnabled = verDetalleObjImpEnabled;
	}
	
	public boolean isVerHistoricoContribEnabled() {
		return verHistoricoContribEnabled;
	}
	public void setVerHistoricoContribEnabled(boolean verHistoricoContribEnabled) {
		this.verHistoricoContribEnabled = verHistoricoContribEnabled;
	}
	
	public boolean isBuzonCambiosEnabled() {
		return buzonCambiosEnabled;
	}
	public void setBuzonCambiosEnabled(boolean buzonCambiosEnabled) {
		this.buzonCambiosEnabled = buzonCambiosEnabled;
	}
	// Getters View





	public String getFechaFormalizacionView() {
		return fechaFormalizacionView;
	}

	public void setFechaFormalizacionView(String fechaFormalizacionView) {
		this.fechaFormalizacionView = fechaFormalizacionView;
	}

	public String getCantMaxCuoView() {
		return cantMaxCuoView;
	}

	public void setCantMaxCuoView(String cantMaxCuoView) {
		this.cantMaxCuoView = cantMaxCuoView;
	}

	public String getImpMinCuoView() {
		return impMinCuoView;
	}

	public void setImpMinCuoView(String impMinCuoView) {
		this.impMinCuoView = impMinCuoView;
	}

	public String getDescCapitalView() {
		return descCapitalView;
	}

	public void setDescCapitalView(String descCapitalView) {
		this.descCapitalView = descCapitalView;
	}

	public String getDescActualizacionView() {
		return descActualizacionView;
	}

	public void setDescActualizacionView(String descActualizacionView) {
		this.descActualizacionView = descActualizacionView;
	}

	public String getInteresView() {
		return interesView;
	}

	public void setInteresView(String interesView) {
		this.interesView = interesView;
	}

	public String getVenPrimeraCuotaView() {
		return venPrimeraCuotaView;
	}

	public void setVenPrimeraCuotaView(String venPrimeraCuotaView) {
		this.venPrimeraCuotaView = venPrimeraCuotaView;
	}

	public String getImporteAnticipoView() {
		return importeAnticipoView;
	}

	public void setImporteAnticipoView(String importeAnticipoView) {
		this.importeAnticipoView = importeAnticipoView;
	}

	
	public String[] getListIdCuotaSelected() {
		return listIdCuotaSelected;
	}
	public void setListIdCuotaSelected(String[] listIdCuotaSelected) {
		this.listIdCuotaSelected = listIdCuotaSelected;
	}
	
	public Date getFechaLimite() {
		return fechaLimite;
	}
	public void setFechaLimite(Date fechaLimite) {
		this.fechaLimite = fechaLimite;
		this.fechaLimiteView = DateUtil.formatDate(getFechaLimite(), DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaLimiteView() {
		return fechaLimiteView;
	}
	public void setFechaLimiteView(String fechaLimiteView) {
		this.fechaLimiteView = fechaLimiteView;
	}
	



	//View getters
	public List<String> getListFechasFormView() {
		List<String> diasStr = new ArrayList<String>();
		for(Date fecha: listFechasForm){
			diasStr.add(DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK));
		}
		return diasStr;
	}
	
	
	// Metodos para resolucion de cuestiones de la vista
	public void calcularTotalHistorio(){
		Double total = 0D;
		
		// Totalizacion de deuda seleccionada
		for (LiqDeudaVO liqDeuda:this.getListDeuda()){
			total += new Double(liqDeuda.getSaldo());
		}
		
		setTotal(total);
	}

	public void calcularTotalActualizado(){
		Double total = 0D;
		
		// Totalizacion de deuda seleccionada
		for (LiqDeudaVO liqDeuda:this.getListDeuda()){
			total += new Double(liqDeuda.getTotal());
		}
		
		setTotal(total);
	}

	public String getStrPeriodosDeudaView(){
		
		String ret = "";
		
		for(LiqDeudaVO liqDeuda: getListDeuda()){
			ret += liqDeuda.getPeriodoDeuda() + " ";
		}
		
		return ret;
	}

	public String getTotalView(){
		return StringUtil.redondearDecimales(total, 1, 2);
	}

	/**
	 * Verifica si al menos 1 plan de los que contiene es seleccionable. 
	 * @return Cadena vacia si al menos 1 es selecionable.<br>
	 * 		   	"disabled=disabled" si ninguno es seleccionable
	 */
	public String getAlternativasCuotasEnabled(){
		for(LiqPlanVO plan:listPlan){
			if(plan.isEsSeleccionable())
				return "";
		}
		return "disabled=disabled";
	}


	public String getObservacion() {
		return observacion;
	}


	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}


	public DesgloseVO getDesglose() {
		return desglose;
	}


	public void setDesglose(DesgloseVO desglose) {
		this.desglose = desglose;
	}



}
