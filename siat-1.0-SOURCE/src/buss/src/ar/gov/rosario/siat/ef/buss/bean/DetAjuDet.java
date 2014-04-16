//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.ValUnRecConADe;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.AliComFueColVO;
import ar.gov.rosario.siat.ef.iface.model.DetAjuDetVO;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.gde.buss.bean.ActualizaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.CobranzaDet;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAct;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.MultaDet;
import ar.gov.rosario.siat.gde.buss.bean.TipoMulta;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a DetAjuDet (Detalle de determinacion de Ajuste)
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_detAjuDet")
public class DetAjuDet extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Transient
	private Log log = LogFactory.getLog(DetAjuDet.class);
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idDetAju")
	private DetAju detAju;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idPeriodoOrden")
	private PeriodoOrden periodoOrden;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idPlaFueDatCom")
	private PlaFueDatCom plaFueDatCom;

	@Column(name = "tributo")
	private Double tributo;

	@Column(name = "cantPersonal")
	private Integer cantPersonal;

	@Column(name = "minimo")
	private Double minimo;

	@Column(name = "triDet")
	private Double triDet;

	@Column(name = "publicidad")
	private Double publicidad;

	@Column(name = "mesasYSillas")
	private Double mesasYSillas;

	@Column(name = "pago")
	private Double pago;
	
	@Column(name = "pagPosFecIni")
	private Double pagPosFecIni;

	@Column(name = "retencion")
	private Double retencion;

	@Column(name = "ajuste")
	private Double ajuste;

	@Column(name = "totalTributo")
	private Double totalTributo;

	@Column(name = "compensacion")
	private Double compensacion;

	@Column(name = "porMulta")
	private Double porMulta;
	
	@Column(name = "idDeuda")
	private Long idDeuda;
	
	@Transient
	private List<AliComFueCol> listAliComFueCol = new ArrayList<AliComFueCol>();
	
	@Transient
	private Double ajusteActualizado;
	
	@Transient
	private Double noPagado;
	
	@Transient
	private Double pagado;
	
	@Transient
	private Double convenio;
	
	// <#Propiedades#>

	// Constructores
	public DetAjuDet() {
		super();
		// Seteo de valores default
	}

	public DetAjuDet(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static DetAjuDet getById(Long id) {
		return (DetAjuDet) EfDAOFactory.getDetAjuDetDAO().getById(id);
	}

	public static DetAjuDet getByIdNull(Long id) {
		return (DetAjuDet) EfDAOFactory.getDetAjuDetDAO().getByIdNull(id);
	}

	public static List<DetAjuDet> getList() {
		return (ArrayList<DetAjuDet>) EfDAOFactory.getDetAjuDetDAO().getList();
	}

	public static List<DetAjuDet> getListActivos() {
		return (ArrayList<DetAjuDet>) EfDAOFactory.getDetAjuDetDAO()
				.getListActiva();
	}

	public static List<DetAjuDet> getByCuentaAjusteOrden(Long idCuenta, Long idOrdenControl) {
		return EfDAOFactory.getDetAjuDetDAO().getByCuentaAjusteOrden(idCuenta, idOrdenControl);
	}
	
	// Getters y setters
	public DetAju getDetAju() {
		return detAju;
	}

	public void setDetAju(DetAju detAju) {
		this.detAju = detAju;
	}

	public PeriodoOrden getPeriodoOrden() {
		return periodoOrden;
	}

	public void setPeriodoOrden(PeriodoOrden periodoOrden) {
		this.periodoOrden = periodoOrden;
	}

	public PlaFueDatCom getPlaFueDatCom() {
		return plaFueDatCom;
	}

	public void setPlaFueDatCom(PlaFueDatCom plaFueDatCom) {
		this.plaFueDatCom = plaFueDatCom;
	}

	public Double getTributo() {
		return tributo;
	}

	public void setTributo(Double tributo) {
		this.tributo = tributo;
	}

	public Integer getCantPersonal() {
		return cantPersonal;
	}

	public void setCantPersonal(Integer cantPersonal) {
		this.cantPersonal = cantPersonal;
	}

	public Double getMinimo() {
		return minimo;
	}

	public void setMinimo(Double minimo) {
		this.minimo = minimo;
	}

	public Double getTriDet() {
		return triDet;
	}

	public void setTriDet(Double triDet) {
		this.triDet = triDet;
	}

	public Double getPublicidad() {
		return publicidad;
	}

	public void setPublicidad(Double publicidad) {
		this.publicidad = publicidad;
	}

	public Double getMesasYSillas() {
		return mesasYSillas;
	}

	public void setMesasYSillas(Double mesasYSillas) {
		this.mesasYSillas = mesasYSillas;
	}

	public Double getPago() {
		return pago;
	}

	public void setPago(Double pago) {
		this.pago = pago;
	}

	public Double getRetencion() {
		return retencion;
	}

	public void setRetencion(Double retencion) {
		this.retencion = retencion;
	}

	public Double getAjuste() {
		return ajuste;
	}

	public void setAjuste(Double ajuste) {
		this.ajuste = ajuste;
	}
	
	public Double getTotalTributo() {
		return totalTributo;
	}

	public void setTotalTributo(Double totalTributo) {
		this.totalTributo = totalTributo;
	}

	public List<AliComFueCol> getListAliComFueCol() {
		return listAliComFueCol;
	}

	public void setListAliComFueCol(List<AliComFueCol> listAliComFueCol) {
		this.listAliComFueCol = listAliComFueCol;
	}

	public Double getCompensacion() {
		return compensacion;
	}

	public void setCompensacion(Double compensacion) {
		this.compensacion = compensacion;
	}

	public Double getPorMulta() {
		return porMulta;
	}
	public void setPorMulta(Double porMulta) {
		this.porMulta = porMulta;
	}

	public Long getIdDeuda() {
		return idDeuda;
	}

	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}
	
	public Deuda getDeuda() {
		return Deuda.getById(getIdDeuda());
	}

	public Double getPagPosFecIni() {
		return pagPosFecIni;
	}

	public void setPagPosFecIni(Double pagPosFecIni) {
		this.pagPosFecIni = pagPosFecIni;
	}

	public Double getAjusteActualizado() {
		return ajusteActualizado;
	}

	public void setAjusteActualizado(Double ajusteActualizado) {
		this.ajusteActualizado = ajusteActualizado;
	}

	// Validaciones
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio

		return true;
	}

	public boolean validateDelete() {
		// limpiamos la lista de errores
		clearError();

		if (GenericDAO.hasReference(this, CobranzaDet.class, "detAjuDet")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
					EfError.DETAJUDET_LABEL, GdeError.COBRANZA_LABEL);
		}
		if (GenericDAO.hasReference(this, MultaDet.class, "detAjuDet")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
					EfError.DETAJUDET_LABEL, GdeError.MULTA_LABEL);
		}
		
		
		// <#ValidateDelete#>

		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones

		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio
	/**Verifica si en la lista de AliComfeCol hay alguna con valorAlicuota=0 */
	public boolean tieneAlicuotasIndefinidas(){
		for(AliComFueCol aliComFueCol: listAliComFueCol){
			if((aliComFueCol.getValorAlicuota()==null || aliComFueCol.getValorAlicuota().equals(0D))&& (aliComFueCol.getCantidad()==null 
					|| aliComFueCol.getCantidad()==0|| aliComFueCol.getValorUnitario()==null || aliComFueCol.getValorUnitario()==0) && (aliComFueCol.getTipoUnidad()==null
							|| aliComFueCol.getRadio()==null))
				return true;
		}
		
		return false;
	}
	
	/** carga en la lista de AliComfueCol (Transient) las alicuotas de cada periodo, para la planilla de ajustes
	 * 
	 */
	public void cargarAlicuotas(){
    	int nroCol =1;
    	while(nroCol<=12){
        	Double valorCol = null;
        	switch(nroCol){
	        	case 1:valorCol=plaFueDatCom.getCol1();break;
        		case 2:valorCol=plaFueDatCom.getCol2();break;
        		case 3:valorCol=plaFueDatCom.getCol3();break;
        		case 4:valorCol=plaFueDatCom.getCol4();break;
        		case 5:valorCol=plaFueDatCom.getCol5();break;
        		case 6:valorCol=plaFueDatCom.getCol6();break;	        	
	        	case 7:valorCol=plaFueDatCom.getCol7();break;
        		case 8:valorCol=plaFueDatCom.getCol8();break;
        		case 9:valorCol=plaFueDatCom.getCol9();break;
        		case 10:valorCol=plaFueDatCom.getCol10();break;
        		case 11:valorCol=plaFueDatCom.getCol11();break;
        		case 12:valorCol=plaFueDatCom.getCol12();break;
        	}
        	
        	if(valorCol!=null){
        		// obtiene la columna para el nroCol actual (CompFuenteCol, a traves del compFuente del detalle actual)
        		// a la columna obtenida le pide la alicuota vigente(AliComFueCol) para el periodo y anio del detalle actual (PlaFueDatCom)
        		// agrega la alicuota obtenida a la lista que se le agrego al VO de DetAjuDet, para que las pueda visualizar
        		Integer periodoPlaFueDatCom = plaFueDatCom.getPeriodo();
        		Integer anioPlaFueDatCom = plaFueDatCom.getAnio();
        		
        		CompFuente compFuente = CompFuente.getById(plaFueDatCom.getCompFuente().getId());
        		CompFuenteCol compFuenteCol = compFuente.getCompFuenteCol(nroCol);
        		AliComFueCol aliComFueColVigente = compFuenteCol.getAliComFueCol(periodoPlaFueDatCom, anioPlaFueDatCom, null,this.getDetAju());
        		listAliComFueCol.add((aliComFueColVigente!=null?aliComFueColVigente:new AliComFueCol()));
        	}
        	nroCol++;
    	}
	}
	
	/**
	 * Activa el DetAjuDet. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getDetAjuDetDAO().update(this);
	}

	/**
	 * Desactiva el DetAjuDet. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getDetAjuDetDAO().update(this);
	}

	/**
	 * Valida la activacion del DetAjuDet
	 * 
	 * @return boolean
	 */
	private boolean validateActivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	/**
	 * Valida la desactivacion del DetAjuDet
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	/**
	 * carga los datos de nivel 1 y las alicuotas
	 * @return
	 * @throws Exception 
	 */
	public DetAjuDetVO toVO4Planilla() throws Exception {
		if (this.ajuste != null && this.ajuste>0D){
			Deuda deuda = Deuda.getPerOriByCuentaPeriodoAnio(this.getDetAju().getOrdConCue().getCuenta(), this.getPeriodoOrden().getPeriodo().longValue(), this.getPeriodoOrden().getAnio());
			log.debug("fechaActualizacion1: "+DateUtil.formatDate(this.getDetAju().getFechaActualizacion(), DateUtil.ddSMMSYYYY_MASK));
			Date fechaAct = (this.getDetAju().getFechaActualizacion()!=null)?this.getDetAju().getFechaActualizacion():new Date();
			log.debug("fechaActualizacion: "+DateUtil.formatDate(fechaAct, DateUtil.ddSMMSYYYY_MASK));
			if (deuda !=null){
				DeudaAct deudaAct = ActualizaDeuda.actualizar(fechaAct, deuda.getFechaVencimiento(), this.getAjuste(), false, true);
				this.setAjusteActualizado(deudaAct.getImporteAct());	
				log.debug("IMPORTE ACTUALIZADO: "+this.getAjusteActualizado());
			}
		}
		
		DetAjuDetVO detAjuDetVO = (DetAjuDetVO) this.toVO(1, false);
		cargarAlicuotas();
		
		Date fechaAjuste = DateUtil.getDate("01/"+StringUtil.completarCerosIzq(this.getPeriodoOrden().getPeriodo().toString(), 2)+
				"/"+this.getPeriodoOrden().getAnio(), DateUtil.ddSMMSYYYY_MASK);
		
		for(AliComFueCol aliComFueCol : listAliComFueCol){
			AliComFueColVO aliComFueColVO =(AliComFueColVO) aliComFueCol.toVO(0,false);
			aliComFueColVO.setEsOrdConCueEtur(aliComFueCol.getEsOrdConCueEtur());
			if(aliComFueColVO.getEsOrdConCueEtur() && aliComFueCol.getTipoUnidad()!=null){
				ValUnRecConADe valUn = aliComFueCol.getTipoUnidad().getValUnRecConADeVigente(fechaAjuste);
				if(valUn !=null && valUn.getRecAli()!=null)
					aliComFueColVO.setValorAlicuota(valUn.getRecAli().getAlicuota());
			}
			detAjuDetVO.getListAliComFueCol().add(aliComFueColVO);
		}
		
		return detAjuDetVO;
	}

	/**
	 * carga los datos de nivel 1 solamente.
	 * @return
	 * @throws Exception 
	 */
	public DetAjuDetVO toVO4View() throws Exception {
		DetAjuDetVO detAjuDetVO = (DetAjuDetVO) this.toVO(1, false);		
		return detAjuDetVO;
	}	
	// <#MetodosBeanDetalle#>
	
	// metodos para el reportes
	public List<AliComFueCol> getListAliComFueCol4Report(){
		cargarAlicuotas();
		return listAliComFueCol;
	}
	
	public String getAjustePos(){
		if(ajuste !=null && ajuste.doubleValue()>0D)
			return StringUtil.formatDoubleWithComa(NumberUtil.round(ajuste, 2));
		return "";
	}
	
	public String getAjusteNeg(){
		if(ajuste !=null && ajuste.doubleValue()<0D)
			return StringUtil.formatDoubleWithComa(NumberUtil.round(ajuste, 2));
		return "";
	}
	
	public Double getNoPagado(){
		return noPagado;
	}
	
	public Double getPagado(){
		return pagado;
	}
	
	public Double getConvenio(){
		return convenio;
	}
	
	public String getTributoView(){
		return StringUtil.formatDoubleWithComa(NumberUtil.round(tributo, 2));
	}
	
	public String getMinimoView(){
		return StringUtil.formatDoubleWithComa(NumberUtil.round(minimo, SiatParam.DEC_IMPORTE_VIEW));
	}
	
	public String getTriDetView(){
		return StringUtil.formatDoubleWithComa(NumberUtil.round(triDet, 2));
	}
	
	public String getPublicidadView(){
		if(triDet!=null && publicidad!=null){
			return StringUtil.formatDoubleWithComa(NumberUtil.round(triDet*publicidad, 2));
		}
		return "";
	}
	
	public String getMesasYSillasView(){
		if(triDet!=null && mesasYSillas!=null){
			return StringUtil.formatDoubleWithComa(NumberUtil.round(triDet*mesasYSillas,2));
		}
		return "";
	}
	
	public String getTotalTributoView(){
		return StringUtil.formatDoubleWithComa(NumberUtil.round(totalTributo, 2));
	}
	
	public String getPagoView(){
		return StringUtil.formatDoubleWithComa(NumberUtil.round(pago, 2));
	}
	
	public String getRetencionView(){
		return StringUtil.formatDoubleWithComa(NumberUtil.round(retencion, 2));
	}

	public String getNoPagadoView(){
		List<RecClaDeu> listRecClaDeu = new ArrayList<RecClaDeu>();
		
		for (TipoMulta tipoMulta: TipoMulta.getList()){
			if (tipoMulta.getRecClaDeu()!=null && !listRecClaDeu.contains(tipoMulta.getRecClaDeu())){
				listRecClaDeu.add(tipoMulta.getRecClaDeu());
			}
		}
		
		List<DeudaAdmin>listDeudaAdmin = DeudaAdmin.getList(this.getDetAju().getOrdConCue().getCuenta(), this.periodoOrden.getPeriodo(), this.periodoOrden.getAnio(),listRecClaDeu);
		Double noPago=0D;
		for (DeudaAdmin deudaAdmin:listDeudaAdmin){
			if (deudaAdmin.getFechaPago()==null && deudaAdmin.getConvenio()==null)
				noPago+=deudaAdmin.getImporte();
		}
		this.noPagado=noPago;
		return (noPago>0D)?StringUtil.formatDoubleWithComa(NumberUtil.round(noPago, SiatParam.DEC_IMPORTE_VIEW)):"";
	}
	
	public String getPagadoView(){
		List<RecClaDeu> listRecClaDeu = new ArrayList<RecClaDeu>();
		
		for (TipoMulta tipoMulta: TipoMulta.getList()){
			if (tipoMulta.getRecClaDeu()!=null && !listRecClaDeu.contains(tipoMulta.getRecClaDeu())){
				listRecClaDeu.add(tipoMulta.getRecClaDeu());
			}
		}
		
		List<DeudaAdmin>listDeudaAdmin = DeudaAdmin.getList(this.getDetAju().getOrdConCue().getCuenta(), this.periodoOrden.getPeriodo(), this.periodoOrden.getAnio(),listRecClaDeu);
		Double pagado=0D;
		for (DeudaAdmin deudaAdmin:listDeudaAdmin){
			if (deudaAdmin.getFechaPago()!=null)
				pagado+=deudaAdmin.getImporte();
		}
		this.pagado=pagado;
		
		return (pagado>0D)?StringUtil.formatDoubleWithComa(NumberUtil.round(pagado, SiatParam.DEC_IMPORTE_VIEW)):"";
	}
	
	public String getConvenioView(){
		
		List<RecClaDeu> listRecClaDeu = new ArrayList<RecClaDeu>();
		
		for (TipoMulta tipoMulta: TipoMulta.getList()){
			if (tipoMulta.getRecClaDeu()!=null && !listRecClaDeu.contains(tipoMulta.getRecClaDeu())){
				listRecClaDeu.add(tipoMulta.getRecClaDeu());
			}
		}
		
		List<DeudaAdmin>listDeudaAdmin = DeudaAdmin.getList(this.getDetAju().getOrdConCue().getCuenta(), this.periodoOrden.getPeriodo(), this.periodoOrden.getAnio(),listRecClaDeu);
		Double enConvenio=0D;
		for (DeudaAdmin deudaAdmin:listDeudaAdmin){
			if (deudaAdmin.getConvenio()!=null)
				enConvenio+=deudaAdmin.getSaldo();
		}
		
		this.convenio=enConvenio;
		
		return (enConvenio>0D)?StringUtil.formatDoubleWithComa(NumberUtil.round(enConvenio, SiatParam.DEC_IMPORTE_VIEW)):"";
	}
}
