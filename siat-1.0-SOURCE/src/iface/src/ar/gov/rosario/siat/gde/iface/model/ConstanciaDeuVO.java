//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object de la agrupacion de Deuda por cada Cuenta incluida en el Envio de Deuda a un Procurador
 * @author tecso
 *
 */
public class ConstanciaDeuVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "constanciaDeuVO";
	
	private Long        numero;    
	private Integer     anio;    
	private ProcuradorVO procurador = new ProcuradorVO();           
	private CuentaVO     cuenta     = new CuentaVO();           
	private EstConDeuVO  estConDeu  = new EstConDeuVO(); 
	private String desDomEnv ="";
	private String desDomUbi ="";
	private DomicilioVO  domicilio  = new DomicilioVO(); 
	private ProcesoMasivoVO procesoMasivo = new ProcesoMasivoVO(); 
	private PlaEnvDeuProVO plaEnvDeuPro   = new PlaEnvDeuProVO();   
	private String observacion;    
	private boolean habilitarConstancia = false;
	private Date fechaHabilitacion;
	private Double total = 0D;
	
	private String usrCreador="";
	
	private String desTitulares="";
	
	private CasoVO caso = new CasoVO();
	
	private List<ConDeuDetVO> listConDeuDet = new ArrayList<ConDeuDetVO>();
	private List<ConDeuTitVO> listConDeuTit = new ArrayList<ConDeuTitVO>();
	private List<HistEstConDeuVO> listHistEstConDeu = new ArrayList<HistEstConDeuVO>();
	
	// Buss Flags
	private boolean recomponerConstanciaBussEnabled = true;
	private boolean traspasarConstanciaBussEnabled=true;
	private boolean imprimirBussEnabled=true;
	private boolean seleccionarBussEnabled=true;
	private boolean anularConstanciaBussEnabled=true;
	
	private String fechaHabilitacionView="";
	
	// View Constants
	
	//View Properties
	private String desClave="";
	


	// Constructores
	public ConstanciaDeuVO() {
		super();
	}

	public ConstanciaDeuVO(Long id) {
		super();
		setId(id);
	}
	
	// Getters y Setters
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	public DomicilioVO getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(DomicilioVO domicilio) {
		this.domicilio = domicilio;
	}
	public ProcesoMasivoVO getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivoVO procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}
	public EstConDeuVO getEstConDeu() {
		return estConDeu;
	}
	public void setEstConDeu(EstConDeuVO estConDeu) {
		this.estConDeu = estConDeu;
	}
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public PlaEnvDeuProVO getPlaEnvDeuPro() {
		return plaEnvDeuPro;
	}
	public void setPlaEnvDeuPro(PlaEnvDeuProVO plaEnvDeuPro) {
		this.plaEnvDeuPro = plaEnvDeuPro;
	}
	public ProcuradorVO getProcurador() {
		return procurador;
	}
	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}
	
	public void setTotal(Double total) {
		this.total = total;
	}


	public List<ConDeuDetVO> getListConDeuDet() {
		return listConDeuDet;
	}

	public List<ConDeuTitVO> getListConDeuTit() {
		return listConDeuTit;
	}


	public void setListConDeuTit(List<ConDeuTitVO> listConDeutit) {
		this.listConDeuTit = listConDeutit;
	}


	public void setListConDeuDet(List<ConDeuDetVO> listConDeuDet) {
		this.listConDeuDet = listConDeuDet;
	}
	public boolean getHabilitarConstancia() {
		return habilitarConstancia;
	}


	public void setHabilitarConstancia(boolean habilitarConstancia) {
		this.habilitarConstancia = habilitarConstancia;
	}


	public Date getFechaHabilitacion() {
		return fechaHabilitacion;
	}


	public void setFechaHabilitacion(Date fechaHabilitacion) {
		this.fechaHabilitacion = fechaHabilitacion;
		this.fechaHabilitacionView = DateUtil.formatDate(fechaHabilitacion, "dd/MM/yyyy");
	}


	public List<HistEstConDeuVO> getListHistEstConDeu() {
		return listHistEstConDeu;
	}


	public void setListHistEstConDeu(List<HistEstConDeuVO> listHistEstConDeu) {
		this.listHistEstConDeu = listHistEstConDeu;
	}


	public String getUsrCreador() {
		return usrCreador;
	}


	public void setUsrCreador(String usrCreador) {
		this.usrCreador = usrCreador;
	}
	
	public String getDesDomEnv() {
		return desDomEnv;
	}


	public void setDesDomEnv(String desDomEnv) {
		this.desDomEnv = desDomEnv;
	}


	public String getDesTitulares() {
		return desTitulares;
	}

	public void setDesTitulares(String desTitulares) {
		this.desTitulares = desTitulares;
	}

	
	public String getDesDomUbi() {
		return desDomUbi;
	}

	public void setDesDomUbi(String desDomUbi) {
		this.desDomUbi = desDomUbi;
	}

	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}

	// View getters
	public String getNumeroView(){
		return StringUtil.formatLong(getNumero());
	}
	
	public String getAnioView(){
		return StringUtil.formatInteger(getAnio());
	}	
	
	public String getNumeroBarraAnioConstanciaView(){
		if(StringUtil.isNullOrEmpty(this.getNumeroView()) && StringUtil.isNullOrEmpty(this.getAnioView()))
			return "";
		return this.getNumeroView() + "/" + this.getAnioView();
	}
	
	public String getTotalEnPalabrasView(){
		//hacer que funque con el totalView - arobledo
		String nroEnPalabras = NumberUtil.getNroEnPalabras(this.total, true);
		return nroEnPalabras;
	}

	public String getTotalView(){
		return StringUtil.redondearDecimales(this.total, 1, SiatParam.DEC_IMPORTE_VIEW);
	}
	
	public String getFechaHabilitacionView() {
		return fechaHabilitacionView;
	}


	public void setFechaHabilitacionView(String fechaHabilitacionView) {
		this.fechaHabilitacionView = fechaHabilitacionView;
	}
	
	


	public String getDesClave() {
		return desClave;
	}


	public void setDesClave(String desClave) {
		this.desClave = desClave;
	}


	/**
	 * Calcula el total sumando el importe de cada deuda en listConDeuDet y o setea en la propiedad "total"
	 * @return
	 */
/*	public void calcularTotalImporte(){
		this.total = 0D;
		if(listConDeuDet!=null){
			for(ConDeuDetVO conDeuDet: listConDeuDet){
				this.total += conDeuDet.getDeuda().getImporte();
			}
		}
		total = new Double(StringUtil.parseComaToPoint(StringUtil.redondearDecimales(total, 1, 2)));
	}
*/
	/**
	 * Calcula el total sumando el saldo de cada deuda en listConDeuDet y o setea en la propiedad "total"
	 * @return
	 */
	public void calcularTotalSaldo(){
		this.total = 0D;
		if(listConDeuDet!=null){
			for(ConDeuDetVO conDeuDet: listConDeuDet){
				this.total += conDeuDet.getDeuda().getSaldoActualizado();
			}
		}
		total = new Double(StringUtil.parseComaToPoint(StringUtil.redondearDecimales(total, 1, SiatParam.DEC_IMPORTE_VIEW)));
	}

	public String getFechaEnLetras(){
		return DateUtil.getDateEnLetras(new Date());
	}
	
	public String getNombreMes(){
		return StringUtil.cutAndUpperCase(DateUtil.getMesEnLetra(new Date()));
	}
		
	// Buss flags getters y setters
	
	
	// View flags getters
	
	public boolean getRecomponerConstanciaBussEnabled() {
		return recomponerConstanciaBussEnabled;
	}

	public void setRecomponerConstanciaBussEnabled(
			boolean recomponerConstanciaBussEnabled) {
		this.recomponerConstanciaBussEnabled = recomponerConstanciaBussEnabled;
	}


	public boolean getTraspasarConstanciaBussEnabled() {
		return traspasarConstanciaBussEnabled;
	}

	public void setTraspasarConstanciaBussEnabled(boolean traspasarConstanciaBussEnabled) {
		this.traspasarConstanciaBussEnabled = traspasarConstanciaBussEnabled;
	}

	public boolean getImprimirBussEnabled() {
		return imprimirBussEnabled;
	}


	public void setImprimirBussEnabled(boolean imprimirBussEnabled) {
		this.imprimirBussEnabled = imprimirBussEnabled;
	}

	public boolean isSeleccionarBussEnabled() {
		return seleccionarBussEnabled;
	}


	public void setSeleccionarBussEnabled(boolean seleccionarBussEnabled) {
		this.seleccionarBussEnabled = seleccionarBussEnabled;
	}

	public Double getTotal() {
		return total;
	}


	public boolean getAnularConstanciaBussEnabled() {
		return anularConstanciaBussEnabled;
	}

	public void setAnularConstanciaBussEnabled(boolean anularConstanciaBussEnabled) {
		this.anularConstanciaBussEnabled = anularConstanciaBussEnabled;
	}

	public Double getTotalSaldo() {
		double tot = 0.0;
		for(ConDeuDetVO cdd : this.getListConDeuDet()) {
			tot += NumberUtil.round(cdd.getDeuda().getSaldo(), 2);
		}
		
		return tot;
	}

	public Double getTotalSaldoActualizado() {
		double tot = 0.0;
		for(ConDeuDetVO cdd : this.getListConDeuDet()) {
			tot += cdd.getDeuda().getSaldoActualizado();//NumberUtil.round(cdd.getDeuda().getSaldoActualizado(), 2); Para igualar al calcularTotalSaldo (Fix Mantis 4940)
		}
		
		return NumberUtil.round(tot, 2); // Para igualar al calcularTotalSaldo (Fix Mantis 4940) (antes se redondeaba al acumular)
	}

	public String getTotalSaldoView() {
		return StringUtil.redondearDecimales(getTotalSaldo(), 1, SiatParam.DEC_IMPORTE_VIEW);
	}

	public String getTotalSaldoActualizadoView() {
		return StringUtil.redondearDecimales(getTotalSaldoActualizado(), 1, SiatParam.DEC_IMPORTE_VIEW);
	}

}
