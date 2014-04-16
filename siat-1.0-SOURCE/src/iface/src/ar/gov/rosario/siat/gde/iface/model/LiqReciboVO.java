//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoVO;
import ar.gov.rosario.siat.def.iface.model.RecConVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 *  Clase de tipo "liq" utilizada para la impresion de recibos de deuda y para recibos de cuotas de convenio.
 * 
 * 
 * @author Tecso
 *
 */
public class LiqReciboVO {

	private Logger log = Logger.getLogger(LiqReciboVO.class);
	
	private Long id;
	private Date fechaVto;
	private String desDescuento;
	private Double total=0D;
	private Double recargo=0D;
	private Double sellado=0D;
	private Double importeSellado=0D;
	private Double totalPagar=0D;

	private String codRefPag;
	private Long nroSistema;
	private ServicioBancoVO servicioBanco;
	private RecursoVO recurso = new RecursoVO();
	private ProcuradorVO procurador = new ProcuradorVO();
	private LiqCuentaVO cuenta = new LiqCuentaVO();
	private ProcedimientoVO procedimiento = new ProcedimientoVO();
	private Long numeroRecibo=0L;
	private Integer anioRecibo;
	private Date fechaGeneracion;
	private Long idDescuento;
	private Double totCapitalOriginal =0D;
	private Double desCapitalOrigina =0D;
	private Double totActualizacion = 0D;
	private Double totInteres=0D;
	private Double desActualizacion = 0D;
	private Double desInteres=0D;
	private Long idRepartidor;
	private Date fechaPago;	
	private String propietario;
	private String nroCodigoBarra;
	private String nroCodigoBarraDelim;
	private String codBarForTxt;
	private String codBarForTxtDelim;
	private Boolean esCuotaSaldo=false;
	private Boolean esCambioPlanCdM=false;
	private Boolean esComercio=false;
	private String cuotaDesde ="";
	private Integer noLiqComPro =0;
	private LiqConvenioVO convenio = new LiqConvenioVO();
	private Long idBroche;
	private Boolean estaPago=false;
	private String tipoDeudaStr="";
	private String selladoView="";
	private String desEstado = "";
	private String observacion="";
	private boolean estaImpreso = true;
	private boolean esRectificativa=false;
	private boolean esRecNoVencida=false;
	
	private String desCuotaSaldo = "";
	
	private boolean pagaVencido=true;
	
	private String localidad = "";

	private boolean esVolPagIntRS=false;
	private String esVolPagIntRSView = "false";
	
	// Marca para mostrar CUMUR en recibo
	private boolean esDeudaRS = false;
	private String esDeudaRSView = "false";
	private String cumur = "";
	
	private List<LiqReciboDeudaVO> listReciboDeuda = new ArrayList<LiqReciboDeudaVO>();
	
	private List<LiqCuotaVO> listCuota = new ArrayList<LiqCuotaVO>();
	
	private List<RecConVO> listRecCon = new ArrayList<RecConVO>();
	
	private CaratulaPreEnvioVO caratulaPreEnvio = null; // inicializada en null
	/**
	 * Datos especificos a un recibo de 
	 * Contribucion de Mejoras
	 * */
	private DatosReciboCdMVO datosReciboCdMVO= new DatosReciboCdMVO();
	
	private List<String> listDeudasView = new ArrayList<String>();
	
	private String fechaActualizacionEspView = "";
	
	private Boolean esReconfeccionEspecial = false;
	
	private String casoView = "";
	
	// Getters y Setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDesDescuento() {
		return desDescuento;
	}
	public void setDesDescuento(String desDescuento) {
		this.desDescuento = desDescuento;
	}
	public Date getFechaVto() {
		return fechaVto;
	}
	public void setFechaVto(Date fechaVto) {
		this.fechaVto = fechaVto;
	}
	public List<LiqReciboDeudaVO> getListReciboDeuda() {
		return listReciboDeuda;
	}
	public void setListReciboDeuda(List<LiqReciboDeudaVO> listReciboDeuda) {
		this.listReciboDeuda = listReciboDeuda;
	}
	public Double getRecargo() {
		return recargo;
	}
	public void setRecargo(Double recargo) {
		this.recargo = recargo;
	}
	public Double getSellado() {
		return sellado;
	}
	public void setSellado(Double sellado) {
		this.sellado = sellado;
		this.selladoView = StringUtil.formatDouble(sellado);
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Double getTotalPagar() {
		return totalPagar;
	}
	public void setTotalPagar(Double totalPagar) {
		this.totalPagar = totalPagar;
	}
	public Integer getAnioRecibo() {
		return anioRecibo;
	}
	public void setAnioRecibo(Integer anioRecibo) {
		this.anioRecibo = anioRecibo;
	}
	public String getCodRefPag() {
		return codRefPag;
	}
	public void setCodRefPag(String codRefPag) {
		this.codRefPag = codRefPag;
	}
	public LiqCuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(LiqCuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	public Double getDesActualizacion() {
		return desActualizacion;
	}
	public void setDesActualizacion(Double desActualizacion) {
		this.desActualizacion = desActualizacion;
	}
	public Double getDesCapitalOrigina() {
		return desCapitalOrigina;
	}
	public void setDesCapitalOrigina(Double desCapitalOrigina) {
		this.desCapitalOrigina = desCapitalOrigina;
	}
	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}
	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}
	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}
	public Long getIdDescuento() {
		return idDescuento;
	}
	public void setIdDescuento(Long idDescuento) {
		this.idDescuento = idDescuento;
	}
	public Long getIdRepartidor() {
		return idRepartidor;
	}
	public void setIdRepartidor(Long idRepartidor) {
		this.idRepartidor = idRepartidor;
	}
	public Long getNumeroRecibo() {
		return numeroRecibo;
	}
	public void setNumeroRecibo(Long numeroRecibo) {
		this.numeroRecibo = numeroRecibo;
	}
	public ProcuradorVO getProcurador() {
		return procurador;
	}
	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public ServicioBancoVO getServicioBanco() {
		return servicioBanco;
	}
	public void setServicioBanco(ServicioBancoVO servicioBanco) {
		this.servicioBanco = servicioBanco;
	}
	public Double getTotActualizacion() {
		return totActualizacion;
	}
	public void setTotActualizacion(Double totActualizacion) {
		this.totActualizacion = totActualizacion;
	}
	public Double getTotCapitalOriginal() {
		return totCapitalOriginal;
	}
	public void setTotCapitalOriginal(Double totCapitalOriginal) {
		this.totCapitalOriginal = totCapitalOriginal;
	} 
	public String getPropietario() {
		return propietario;
	}
	public void setPropietario(String propietario) {
		this.propietario = propietario;
	}
	public String getNroCodigoBarra() {
		return nroCodigoBarra;
	}
	public void setNroCodigoBarra(String nroCodigoBarra) {
		this.nroCodigoBarra = nroCodigoBarra;
	}

	public Boolean getEsCuotaSaldo() {
		return esCuotaSaldo;
	}
	public void setEsCuotaSaldo(Boolean esCuotaSaldo) {
		this.esCuotaSaldo = esCuotaSaldo;
	}
	public String getNroCodigoBarraDelim() {
		return nroCodigoBarraDelim;
	}
	public void setNroCodigoBarraDelim(String nroCodigoBarraDelim) {
		this.nroCodigoBarraDelim = nroCodigoBarraDelim;
	}
	
	public String getCodBarForTxt() {
		return codBarForTxt;
	}
	public void setCodBarForTxt(String codBarForTxt) {
		this.codBarForTxt = codBarForTxt;
	}
	
	public String getCodBarForTxtDelim() {
		return codBarForTxtDelim;
	}
	public void setCodBarForTxtDelim(String codBarForTxtDelim) {
		this.codBarForTxtDelim = codBarForTxtDelim;
	}

	public List<LiqCuotaVO> getListCuota() {
		return listCuota;
	}
	public void setListCuota(List<LiqCuotaVO> listCuota) {
		this.listCuota = listCuota;
	}
	
	public LiqConvenioVO getConvenio() {
		return convenio;
	}
	public void setConvenio(LiqConvenioVO convenio) {
		this.convenio = convenio;
	}
	
	public Double getTotInteres() {
		return totInteres;
	}
	public void setTotInteres(Double totInteres) {
		this.totInteres = totInteres;
	}
	
	public Double getDesInteres() {
		return desInteres;
	}
	public void setDesInteres(Double desInteres) {
		this.desInteres = desInteres;
	}

	
	public boolean getEsRecNoVencida() {
		return esRecNoVencida;
	}
	public void setEsRecNoVencida(boolean esRecNoVencida) {
		this.esRecNoVencida = esRecNoVencida;
	}
	public Integer getNoLiqComPro() {
		return noLiqComPro;
	}
	public void setNoLiqComPro(Integer noLiqComPro) {
		this.noLiqComPro = noLiqComPro;
	}
	
	public String getDesEstado() {
		return desEstado;
	}
	public void setDesEstado(String desEstado) {
		this.desEstado = desEstado;
	}
	
	public DatosReciboCdMVO getDatosReciboCdMVO() {
		return datosReciboCdMVO;
	}
	public void setDatosReciboCdMVO(DatosReciboCdMVO datosReciboCdMVO) {
		this.datosReciboCdMVO = datosReciboCdMVO;
	}
	
	public List<String> getListDeudasView() {
		return listDeudasView;
	}
	public void setListDeudasView(List<String> listDeudasView) {
		this.listDeudasView = listDeudasView;
	}
	
	public Double getImporteSellado() {
		return importeSellado;
	}
	public void setImporteSellado(Double importeSellado) {
		this.importeSellado = importeSellado;
	}
	
	public String getDesCuotaSaldo() {
		return desCuotaSaldo;
	}
	public void setDesCuotaSaldo(String desCuotaSaldo) {
		this.desCuotaSaldo = desCuotaSaldo;
	}
	
	public Boolean getEsCambioPlanCdM() {
		return esCambioPlanCdM;
	}
	public void setEsCambioPlanCdM(Boolean esCambioPlanCdM) {
		this.esCambioPlanCdM = esCambioPlanCdM;
	}
	
	public String getTipoDeudaStr() {
		return tipoDeudaStr;
	}
	public void setTipoDeudaStr(String tipoDeudaStr) {
		this.tipoDeudaStr = tipoDeudaStr;
	}
	
	public String getTipoDeudaStrLowCase(){
		return !tipoDeudaStr.equals("")? tipoDeudaStr.substring(0,1)+tipoDeudaStr.substring(1).toLowerCase():"";
	}
	
	public ProcedimientoVO getProcedimiento() {
		return procedimiento;
	}
	
	public void setProcedimiento(ProcedimientoVO procedimiento) {
		this.procedimiento = procedimiento;
	}
	
	public boolean isEsVolPagIntRS() {
		return esVolPagIntRS;
	}
	public void setEsVolPagIntRS(boolean esVolPagIntRS) {
		this.esVolPagIntRS = esVolPagIntRS;
		this.esVolPagIntRSView = String.valueOf(esVolPagIntRS);
	}	
	public String getEsVolPagIntRSView() {
		return esVolPagIntRSView;
	}
	public void setEsVolPagIntRSView(String esVolPagIntRSView) {
		this.esVolPagIntRSView = esVolPagIntRSView;
	}
	
	public boolean isEsDeudaRS() {
		return esDeudaRS;
	}
	public void setEsDeudaRS(boolean esDeudaRS) {
		this.esDeudaRS = esDeudaRS;
		this.esDeudaRSView = String.valueOf(esDeudaRS);
	}
	public String getEsDeudaRSView() {
		return esDeudaRSView;
	}
	public void setEsDeudaRSView(String esDeudaRSView) {
		this.esDeudaRSView = esDeudaRSView;
	}
	public String getCumur() {
		return cumur;
	}
	public void setCumur(String cumur) {
		this.cumur = cumur;
	}
	
	/**
	 * Elimina las columnas de los conceptos(Tasa, Sobretasa, etc.) para las que TODOS los reciboDeuda del recibo tienen valor 0
	 * <BR>Las columnas deben estar ya ordenadas tanto en el recibo como en los reciboDeuda
	 */
	public void eliminarColumnasConceptoEnCero(){		
		if(getListRecCon()!=null){
			 int cantColumnas = getListRecCon().size();
			 for(int i=0;i<cantColumnas;i++){
				 boolean sacarColumna = true;
				 for(LiqReciboDeudaVO reciboDeuda: getListReciboDeuda()){
					 // Si algun reciboDeuda tiene un valor en la columna que se esta validando, se muestra la columna
					 LiqConceptoDeudaVO concepto = reciboDeuda.getListConceptosDeuda().get(i);
					 if(concepto.getImporte()!=null && concepto.getImporte()>0){
						 sacarColumna=false;
						 break;
					 }
				 }
				 if(sacarColumna){
					 // Se elimina la columna de la cabecera del recibo
					 getListRecCon().remove(i);
					 // Se elimina la columna para cada reciboDeuda
					 for(LiqReciboDeudaVO liqReciboDeuda: getListReciboDeuda()){
						 liqReciboDeuda.getListConceptosDeuda().remove(liqReciboDeuda.getListConceptosDeuda().get(i));
					 }
					 cantColumnas = getListRecCon().size();
					 i--;
				 }

			 }
		}
	}
	
	public boolean getPagaVencido() {
		return pagaVencido;
	}
	public void setPagaVencido(boolean pagaVencido) {
		this.pagaVencido = pagaVencido;
	}
	public Long getIdBroche() {
		return idBroche;
	}
	
	public CaratulaPreEnvioVO getCaratulaPreEnvio() {
		return caratulaPreEnvio;
	}
	public void setCaratulaPreEnvio(CaratulaPreEnvioVO caratulaPreEnvio) {
		this.caratulaPreEnvio = caratulaPreEnvio;
	}
	
	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	public void setIdBroche(Long idBroche) {
		this.idBroche = idBroche;
	}
	public Long getNroSistema() {
		return nroSistema;
	}
	public void setNroSistema(Long nroSistema) {
		this.nroSistema = nroSistema;
	}
	//view getters
	public String getIdView(){
		return String.valueOf(id);
	}
	
	public List<RecConVO> getListRecCon() {
		return listRecCon;
	}
	public void setListRecCon(List<RecConVO> listRecCon) {
		this.listRecCon = listRecCon;
	}
	public String getNumeroReciboView(){
		if (numeroRecibo !=null){
			return StringUtil.completarCerosIzq(String.valueOf(numeroRecibo), 9);
		}else{
			return StringUtil.completarCerosIzq(String.valueOf(0), 9);
		}
	}
		
	public String getCodRefPagView(){
		return StringUtil.completarCerosIzq(String.valueOf(codRefPag), 11);
	}
	
	public String getCodRefPagSinCerosView(){
		return String.valueOf(codRefPag);
	}
	
	public String getFechaVtoView(){
		return DateUtil.formatDate(fechaVto, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public String getTotalPagarView() {
		return  StringUtil.redondearDecimales(totalPagar, 1, 2); 
	}

	public void setSelladoView(String selladoView) {
		this.selladoView = selladoView;
	}
	public String getSelladoView(){
		return StringUtil.redondearDecimales(sellado, 1, 2);
	}
	public String getCuotaDesde() {
		return cuotaDesde;
	}
	public void setCuotaDesde(String cuotaDesde) {
		this.cuotaDesde = cuotaDesde;
	}
	public String getEsCuotaSaldoView (){
		return (esCuotaSaldo==true)?"SI":"NO";
	}
	public Boolean getEstaPago() {
		return estaPago;
	}
	public void setEstaPago(Boolean estaPago) {
		this.estaPago = estaPago;
	}

	public Double getTotalDeudaConInteres(){
		return totCapitalOriginal + totActualizacion + desActualizacion;
	}

	public Double getTotalDeudaConDescuento(){
		return totCapitalOriginal + totActualizacion ;
	}

	public String getTotalDeudaConInteresView(){
		return StringUtil.redondearDecimales(getTotalDeudaConInteres(), 1, 2);
	}

	public String getTotalDeudaConDescuentoView(){
		return StringUtil.redondearDecimales(getTotalDeudaConDescuento(), 1, 2);
	}
	
	public String getImporteSelladoView(){
		return StringUtil.redondearDecimales(getImporteSellado(), 1, 2);
	}
	
	public String getTotActualizacionView(){  
		return StringUtil.redondearDecimales(getTotActualizacion(), 1, 2);
	}
	
	public String getTotalView(){  
		return StringUtil.redondearDecimales(getTotal(), 1, 2);
	}
	
	public String getNroSistemaView() {
		return StringUtil.formatLong(getNroSistema());
	}

	
	public String getLeyendaSubtitulo(){
		
		String leyenda = "Reimpresi\u00F3n";
		
		if (esCuotaSaldo){
			leyenda = "Cuota Saldo";
		} else if(esCambioPlanCdM){
			leyenda = "Cambio de Plan";
		} else if (!estaImpreso) {
			leyenda = "";
		}
		
		if(esVolPagIntRS){
			leyenda = "Volante de Pago de Intereses RS";
		}
		
		return leyenda;
	}

	public String getFechaGeneracionView(){
		if(fechaGeneracion!=null)
			return DateUtil.formatDate(fechaGeneracion, DateUtil.ddSMMSYYYY_MASK);
		return "";
	}	

	public Boolean getEsReconfeccionEspecial() {
		return esReconfeccionEspecial;
	}
	public void setEsReconfeccionEspecial(Boolean esReconfeccionEspecial) {
		this.esReconfeccionEspecial = esReconfeccionEspecial;
	}
	
	public String getFechaActualizacionEspView() {
		return fechaActualizacionEspView;
	}
	public void setFechaActualizacionEspView(String fechaActualizacionView) {
		this.fechaActualizacionEspView = fechaActualizacionView;
	}

	public String getCasoView() {
		return casoView;
	}
	public void setCasoView(String casoView) {
		this.casoView = casoView;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Boolean getEsComercio() {
		return esComercio;
	}
	public void setEsComercio(Boolean esComercio) {
		this.esComercio = esComercio;
	}
	public boolean isEsRectificativa() {
		return esRectificativa;
	}
	public void setEsRectificativa(boolean esRectificativa) {
		this.esRectificativa = esRectificativa;
	}
	public String getFechaVtoOriginalView(){
		if(listReciboDeuda.size()==1){
			return listReciboDeuda.get(0).getDeuda().getFechaVto();
		}
		return "";
	}	

	public boolean getEstaImpreso() {
		return estaImpreso;
	}
	public void setEstaImpreso(boolean estaImpreso) {
		this.estaImpreso = estaImpreso;
	}
	/**
	 * pregunta si el recibo es Cero
	 * 
	 * @return boolean
	 */
	public boolean getPoseePeriodoValorCero() {
		Boolean rta=false;
		 
	 if (this.getListReciboDeuda().size()==1 && this.getRecurso().getEsAutoliquidable().getEsSI()) {
    	  if(this.getListReciboDeuda().get(0).isValorCero()){ 
    		  rta=true;
    		 log.debug("poseePeriodoValorCero: "+rta.toString());
    	  }
       }
		return rta;
	}
	
	public boolean getEsReimpresion(){
		return(this.numeroRecibo==null || this.numeroRecibo==0L || esRecNoVencida)?true:false;
	}
	
	

}
