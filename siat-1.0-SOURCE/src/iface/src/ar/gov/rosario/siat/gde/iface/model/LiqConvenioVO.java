//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.Sexo;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class LiqConvenioVO extends SiatBussImageModel {
	
	private Long idConvenio;
	
	private String desPlan="";
	private String desViaDeuda="";
	private String canCuotasPlan="";
	private String desEstadoConvenio="";
	private String leyendaForm="";
	private Double totImporteConvenio=0D;
	private String nroConvenio="";
	private String idSistema;
	private String ordenanza="";
	
	private boolean poseeDatosPersona = true;
	private PersonaVO persona  = new PersonaVO(); // Persona que realiza la formalizacion

	private boolean poseeProcurador = false;  // Si la via del convenio es judicial,  posee procurador
	private ProcuradorVO procurador = new ProcuradorVO();
	
	private TipoPerForVO tipoPerFor = new TipoPerForVO(); // Tipo Persona Formaliza 
	private TipoDocApoVO tipoDocApo = new TipoDocApoVO(); // Tipo Documentacion Aportada
	private String fechaFor = "";
    private String observacionFor = "";
    private String ususarioFor = ""; //Agente interviniente
    private String lugarFor = "";  	 // Ahora guardan ip, hay que tocar modelo porque nos pidieron combo para lugar de logueo 
    private Integer cantidadCuotasPlan;
    private Double anticipo;
    private String fechaAltaView ="";
    private Double importeSellado=0D;
    
    private boolean poseeCaso = false;  
    private CasoVO caso = new CasoVO(); // Caso para los planes manuales
    
    private boolean cuoInpConSel = false;
    
    private boolean cuoPagConSel = false;
	    
	// Periodos de deuda incluidos (ConvenioDeuda)
	private List<LiqDeudaVO> listPeriodoIncluido = new ArrayList<LiqDeudaVO>();
	// Lista de Cuotas Pagas e Impagas (ConvenioCuota)
    private List<LiqCuotaVO> listCuotaPaga = new ArrayList<LiqCuotaVO>();
    private List<LiqCuotaVO> listCuotaInpaga = new ArrayList<LiqCuotaVO>();
    
    private List<CuotaDeudaVO> listCuotaDeudaImputadas = new ArrayList<CuotaDeudaVO>();
    
    private boolean estaCaduco = false;
    
    // Constructor
    public LiqConvenioVO(){
    	this.getPersona().setSexo(Sexo.OpcionVacia);
    }
    
    // Getters y Setters
	public String getLugarFor() {
		return lugarFor;
	}

	public void setLugarFor(String lugarFor) {
		this.lugarFor = lugarFor;
	}

	public String getObservacionFor() {
		return observacionFor;
	}

	public void setObservacionFor(String observacionFor) {
		this.observacionFor = observacionFor;
	}

	public String getUsusarioFor() {
		return ususarioFor;
	}

	public void setUsusarioFor(String ususarioFor) {
		this.ususarioFor = ususarioFor;
	}

	
	public String getFechaFor() {
		return fechaFor;
	}

	public void setFechaFor(String fechaFor) {
		this.fechaFor = fechaFor;
	}

	
	public PersonaVO getPersona() {
		return persona;
	}

	public void setPersona(PersonaVO persona) {
		this.persona = persona;
	}

	public TipoDocApoVO getTipoDocApo() {
		return tipoDocApo;
	}

	public void setTipoDocApo(TipoDocApoVO tipoDocApo) {
		this.tipoDocApo = tipoDocApo;
	}

	public TipoPerForVO getTipoPerFor() {
		return tipoPerFor;
	}

	public void setTipoPerFor(TipoPerForVO tipoPerFor) {
		this.tipoPerFor = tipoPerFor;
	}

	public Long getIdConvenio() {
		return idConvenio;
	}

	public void setIdConvenio(Long idConvenio) {
		this.idConvenio = idConvenio;
	}

	
	public String getNroConvenio() {
		return nroConvenio;
	}

	public void setNroConvenio(String nroConvenio) {
		this.nroConvenio = nroConvenio;
	}

	public Integer getCantidadCuotasPlan() {
		return cantidadCuotasPlan;
	}

	public void setCantidadCuotasPlan(Integer cantidadCuotasPlan) {
		this.cantidadCuotasPlan = cantidadCuotasPlan;
	}

	public Double getTotImporteConvenio() {
		return totImporteConvenio;
	}

	public void setTotImporteConvenio(Double totImporteConvenio) {
		this.totImporteConvenio = totImporteConvenio;
	}
	
	public Double getAnticipo() {
		return anticipo;
	}

	public void setAnticipo(Double anticipo) {
		this.anticipo = anticipo;
	}
	
	public String getCanCuotasPlan() {
		return canCuotasPlan;
	}

	public void setCanCuotasPlan(String canCuotasPlan) {
		this.canCuotasPlan = canCuotasPlan;
	}

	public String getDesEstadoConvenio() {
		return desEstadoConvenio;
	}

	public void setDesEstadoConvenio(String desEstadoConvenio) {
		this.desEstadoConvenio = desEstadoConvenio;
	}

	public String getDesPlan() {
		return desPlan;
	}

	public void setDesPlan(String desPlan) {
		this.desPlan = desPlan;
	}

	public String getDesViaDeuda() {
		return desViaDeuda;
	}

	public void setDesViaDeuda(String desViaDeuda) {
		this.desViaDeuda = desViaDeuda;
	}

	public String getIdSistema() {
		return idSistema;
	}

	public void setIdSistema(String idSistema) {
		this.idSistema = idSistema;
	}

	public List<LiqCuotaVO> getListCuotaInpaga() {
		return listCuotaInpaga;
	}

	public void setListCuotaInpaga(List<LiqCuotaVO> listCuotaInpaga) {
		this.listCuotaInpaga = listCuotaInpaga;
	}

	public String getLeyendaForm() {
		return leyendaForm;
	}

	public void setLeyendaForm(String leyendaForm) {
		this.leyendaForm = leyendaForm;
	}

	public List<LiqCuotaVO> getListCuotaPaga() {
		return listCuotaPaga;
	}

	public void setListCuotaPaga(List<LiqCuotaVO> listCuotaPaga) {
		this.listCuotaPaga = listCuotaPaga;
	}

	public List<LiqDeudaVO> getListPeriodoIncluido() {
		return listPeriodoIncluido;
	}

	public void setListPeriodoIncluido(List<LiqDeudaVO> listPeriodoIncluido) {
		this.listPeriodoIncluido = listPeriodoIncluido;
	}

	public List<CuotaDeudaVO> getListCuotaDeudaImputadas() {
		return listCuotaDeudaImputadas;
	}

	public void setListCuotaDeudaImputadas(
			List<CuotaDeudaVO> listCuotaDeudaImputadas) {
		this.listCuotaDeudaImputadas = listCuotaDeudaImputadas;
	}

	public boolean getPoseeDatosPersona() {
		return poseeDatosPersona;
	}

	public void setPoseeDatosPersona(boolean poseeDatosPersona) {
		this.poseeDatosPersona = poseeDatosPersona;
	}
	
	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	
	public String getOrdenanza() {
		return ordenanza;
	}

	public void setOrdenanza(String ordenanza) {
		this.ordenanza = ordenanza;
	}

	public boolean getPoseeProcurador() {
		return poseeProcurador;
	}
	public void setPoseeProcurador(boolean poseeProcurador) {
		this.poseeProcurador = poseeProcurador;
	}

	public ProcuradorVO getProcurador() {
		return procurador;
	}
	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}

	public boolean isPoseeCaso() {
		return poseeCaso;
	}
	public void setPoseeCaso(boolean poseeCaso) {
		this.poseeCaso = poseeCaso;
	}
	
	public boolean isCuoInpConSel() {
		return cuoInpConSel;
	}

	public void setCuoInpConSel(boolean cuoInpConSel) {
		this.cuoInpConSel = cuoInpConSel;
	}

	public boolean isCuoPagConSel() {
		return cuoPagConSel;
	}

	public void setCuoPagConSel(boolean cuoPagConSel) {
		this.cuoPagConSel = cuoPagConSel;
	}

	public Double getImporteSellado() {
		return importeSellado;
	}

	public void setImporteSellado(Double importeSellado) {
		this.importeSellado = importeSellado;
	}

	public boolean isEstaCaduco() {
		return estaCaduco;
	}
	public void setEstaCaduco(boolean estaCaduco) {
		this.estaCaduco = estaCaduco;
	}

	// Getter for View
	public String getTotImporteConvenioView(){
		return (totImporteConvenio!=null?StringUtil.redondearDecimales(totImporteConvenio, 0, SiatParam.DEC_IMPORTE_VIEW):"");
	}
	
	public String getImporteSelladoView(){
		return (StringUtil.redondearDecimales(importeSellado, 1, SiatParam.DEC_IMPORTE_VIEW));
	}
	
	public String getFechaAltaView() {
		return fechaAltaView;
	}

	public void setFechaAltaView(String fechaAltaView) {
		this.fechaAltaView = fechaAltaView;
	}

	/**
	 * 
	 * Devuelve true si posee al menos una cuota impaga "seleccionable"
	 * 
	 * 
	 * @author Cristian
	 * @return
	 */
	public boolean poseeCuotaImpagaSeleccionable(){
		boolean ret = false; 
		
		for (LiqCuotaVO liqCuotaVO:listCuotaInpaga){
			if (liqCuotaVO.getEsSeleccionable())
				ret = true;			
		}
		
		return ret;		
	}
	
	public boolean tienePagos(){
		return getListCuotaPaga().size()>0;
	}
	
	// Utilizada para la impresion del formulario de formalizacion
	public String getTotalMenosAnticipoView(){
		
		if (totImporteConvenio == null)
			setTotImporteConvenio(0D);
		
		if (anticipo == null)
			setAnticipo(0D);	
		
		Double totalMenosAnt = new Double(totImporteConvenio - anticipo);  
		
		return (totalMenosAnt!=null?StringUtil.redondearDecimales(totalMenosAnt, 0, SiatParam.DEC_IMPORTE_VIEW):"");
	}
	
	
	/**
	 * Calcula el monto total de todos los periodos
	 * 
	 * @return
	 */
    public Double getTotalPeriodos(){
    	Double totalPeriodos = 0D;
    	if(listPeriodoIncluido!=null){
    		for(LiqDeudaVO periodo: listPeriodoIncluido){
    			totalPeriodos += NumberUtil.round(periodo.getTotal(), SiatParam.DEC_IMPORTE_CALC);
    		}
    		totalPeriodos = NumberUtil.truncate(totalPeriodos, SiatParam.DEC_IMPORTE_VIEW);
    	}
    	return totalPeriodos;
    }

    /**
     * Subtotal de Importes de periodos incluidos 
     * 
     * @return
     */
    public Double getSubTotalImpDeuda(){
    	Double totalImpPer = 0D;
    	if(listPeriodoIncluido!=null){
    		for(LiqDeudaVO periodo: listPeriodoIncluido){
    			totalImpPer += NumberUtil.round(periodo.getSaldo(), SiatParam.DEC_IMPORTE_CALC);
    		}
    		totalImpPer = NumberUtil.truncate(totalImpPer, SiatParam.DEC_IMPORTE_VIEW);
    	}
    	return totalImpPer;
    }
    
    /**
     * Subtotal de Actulizacion de Periodos de deuda
     * 
     * @return
     */
    public Double getSubTotalActDeuda(){
    	Double totalActPer = 0D;
    	if(listPeriodoIncluido!=null){
    		for(LiqDeudaVO periodo: listPeriodoIncluido){
    			totalActPer += NumberUtil.round(periodo.getActualizacion(), SiatParam.DEC_IMPORTE_CALC);
    		}
    		totalActPer = NumberUtil.truncate(totalActPer, SiatParam.DEC_IMPORTE_VIEW);
    	}
    	return totalActPer;
    }
    
    /**
     * Calcula el monto total de las cuotas impagas 
     * 
     * @author Cristian
     * @return
     */
    public Double getTotalCuotasImpagas(){
    	Double total = 0D;
    	if(listCuotaInpaga!=null){
    		for(LiqCuotaVO cuota: listCuotaInpaga){
    			total += NumberUtil.round(cuota.getTotal(), SiatParam.DEC_IMPORTE_CALC);
    		}
    		total = NumberUtil.truncate(total, SiatParam.DEC_IMPORTE_VIEW);
    	}
    	return total;
    }
    
    public Double getSubTotalCapCuotasImpagas(){
    	Double total = 0D;
    	if(listCuotaInpaga!=null){
    		for(LiqCuotaVO cuota: listCuotaInpaga){
    			total += NumberUtil.round(cuota.getCapital(), SiatParam.DEC_IMPORTE_CALC);
    		}
    		total = NumberUtil.truncate(total, SiatParam.DEC_IMPORTE_VIEW);
    	}
    	return total;
    }

    public Double getSubTotalIntCuotasImpagas(){
    	Double total = 0D;
    	if(listCuotaInpaga!=null){
    		for(LiqCuotaVO cuota: listCuotaInpaga){
    			total += NumberUtil.round(cuota.getInteres(), SiatParam.DEC_IMPORTE_CALC);
    		}
    		total = NumberUtil.truncate(total, SiatParam.DEC_IMPORTE_VIEW);
    	}
    	return total;
    }
    
    public Double getSubTotalActCuotasImpagas(){
    	Double total = 0D;
    	if(listCuotaInpaga!=null){
    		for(LiqCuotaVO cuota: listCuotaInpaga){
    			total += NumberUtil.round(cuota.getActualizacion(), SiatParam.DEC_IMPORTE_CALC);
    		}
    		total = NumberUtil.truncate(total, SiatParam.DEC_IMPORTE_VIEW);
    	}
    	return total;
    }
    
    /**
     * Calcula el monto total de las cuotas impagas 
     * 
     * @author Cristian
     * @return
     */
    public Double getTotalCuotasPagas(){
    	Double total = 0D;
    	if(listCuotaPaga!=null){
    		for(LiqCuotaVO cuota: listCuotaPaga){
    			total += NumberUtil.round(cuota.getTotal(), SiatParam.DEC_IMPORTE_VIEW);
    		}
    		total = NumberUtil.truncate(total, SiatParam.DEC_IMPORTE_VIEW);
    	}
    	return total;
    }
    
    public Double getSubTotalCapCuotasPagas(){
    	Double total = 0D;
    	if(listCuotaPaga!=null){
    		for(LiqCuotaVO cuota: listCuotaPaga){
    			total += NumberUtil.round(cuota.getCapital(), SiatParam.DEC_IMPORTE_CALC);
    		}
    		total = NumberUtil.truncate(total, SiatParam.DEC_IMPORTE_VIEW);
    	}
    	return total;
    }

    public Double getSubTotalIntCuotasPagas(){
    	Double total = 0D;
    	if(listCuotaPaga!=null){
    		for(LiqCuotaVO cuota: listCuotaPaga){
    			total += NumberUtil.round(cuota.getInteres(), SiatParam.DEC_IMPORTE_CALC);
    		}
    		total = NumberUtil.truncate(total, SiatParam.DEC_IMPORTE_VIEW);
    	}
    	return total;
    }
    
    public Double getSubTotalActCuotasPagas(){
    	Double total = 0D;
    	if(listCuotaPaga!=null){
    		for(LiqCuotaVO cuota: listCuotaPaga){
    			total += NumberUtil.round(cuota.getActualizacion(), SiatParam.DEC_IMPORTE_CALC);
    		}
    		total = NumberUtil.truncate(total, SiatParam.DEC_IMPORTE_VIEW);
    	}
    	return total;
    }
    
    
}
