//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.gde.iface.model.ConvenioCuotaVO;
import ar.gov.rosario.siat.gde.iface.model.DeudaVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Value Object del SaldoAFavor
 * 
 * @author tecso
 * 
 */
public class SaldoAFavorVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "saldoAFavorVO";
	
	// atributos que siempre estan
	private AreaVO area = new AreaVO();
	private TipoOrigenVO tipoOrigen = new TipoOrigenVO();
	private CuentaVO cuenta = new CuentaVO();
	private String descripcion;
	private Date fechaGeneracion;
	private String fechaGeneracionView;
	private Double importe;
	private String importeView;

	// atributos que estan para el caso de area
	private String nroComprobante;
	private String desComprobante;
	private CuentaBancoVO cuentaBanco = new CuentaBancoVO();
	private CasoVO caso = new CasoVO();

	// atributos que no sabemos si se usa
	private EstSalAFavVO estSalAFav = new EstSalAFavVO();
	
	// atributos para el tipo asentamiento
	private DeudaVO deuda = new DeudaVO();
    private Long idDeuda = 0L;
    private ConvenioCuotaVO convenioCuotaVO = new ConvenioCuotaVO();
    private Long idConvenioCuota = 0L;
	private Date fechaPago;
	private String fechaPagoView;
	private PartidaVO partida = new PartidaVO();
	private AsentamientoVO asentamiento = new AsentamientoVO();
	
	private Boolean enCompensacion = false;
	
	// atributo para relacionar con compensacion
	// private CompensacionVO compensacion = new CompensacionVO();
	
	
	// Buss Flags
	
	// View Constants
	
	
	
	// Constructor 
	public SaldoAFavorVO(){
		super();
	}
	// 
	public SaldoAFavorVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDescripcion(desc);
	}


	public AreaVO getArea() {
		return area;
	}
	public void setArea(AreaVO area) {
		this.area = area;
	}


	public AsentamientoVO getAsentamiento() {
		return asentamiento;
	}
	public void setAsentamiento(AsentamientoVO asentamiento) {
		this.asentamiento = asentamiento;
	}


	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}


	public ConvenioCuotaVO getConvenioCuotaVO() {
		return convenioCuotaVO;
	}
	public void setConvenioCuotaVO(ConvenioCuotaVO convenioCuotaVO) {
		this.convenioCuotaVO = convenioCuotaVO;
	}


	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}


	public CuentaBancoVO getCuentaBanco() {
		return cuentaBanco;
	}
	public void setCuentaBanco(CuentaBancoVO cuentaBanco) {
		this.cuentaBanco = cuentaBanco;
	}


	public String getDesComprobante() {
		return desComprobante;
	}
	public void setDesComprobante(String desComprobante) {
		this.desComprobante = desComprobante;
	}


	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public DeudaVO getDeuda() {
		return deuda;
	}
	public void setDeuda(DeudaVO deuda) {
		this.deuda = deuda;
	}


	public EstSalAFavVO getEstSalAFav() {
		return estSalAFav;
	}
	public void setEstSalAFav(EstSalAFavVO estSalAFav) {
		this.estSalAFav = estSalAFav;
	}


	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}
	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
		this.fechaGeneracionView = DateUtil.formatDate(fechaGeneracion, DateUtil.ddSMMSYYYY_MASK);
	}


	public String getFechaGeneracionView() {
		return fechaGeneracionView;
	}
	public void setFechaGeneracionView(String fechaGeneracionView) {
		this.fechaGeneracionView = fechaGeneracionView;
	}


	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
		this.fechaPagoView = DateUtil.formatDate(fechaPago, DateUtil.ddSMMSYYYY_MASK);

	}


	public String getFechaPagoView() {
		return fechaPagoView;
	}
	public void setFechaPagoView(String fechaPagoView) {
		this.fechaPagoView = fechaPagoView;
	}


	public Long getIdConvenioCuota() {
		return idConvenioCuota;
	}


	public void setIdConvenioCuota(Long idConvenioCuota) {
		this.idConvenioCuota = idConvenioCuota;
	}


	public Long getIdDeuda() {
		return idDeuda;
	}


	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}


	public Double getImporte() {
		return importe;
	}


	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView=StringUtil.formatDouble(importe);
	}

	public String getImporteView() {
		return importeView;
	}
	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}


	public String getNroComprobante() {
		return nroComprobante;
	}
	public void setNroComprobante(String nroComprobante) {
		this.nroComprobante = nroComprobante;
	}


	public PartidaVO getPartida() {
		return partida;
	}


	public void setPartida(PartidaVO partida) {
		this.partida = partida;
	}


	public TipoOrigenVO getTipoOrigen() {
		return tipoOrigen;
	}


	public void setTipoOrigen(TipoOrigenVO tipoOrigen) {
		this.tipoOrigen = tipoOrigen;
	}
	
	public Boolean getEnCompensacion() {
		return enCompensacion;
	}
	
	public void setEnCompensacion(Boolean enCompensacion) {
		this.enCompensacion = enCompensacion;
	}
	
	public Boolean getEstaCancelado(){
		if(this.getEstado().getId().intValue() == Estado.INACTIVO.getId().intValue())
			return true;
		else
			return false;
	}
	
	/*public String getEstadoView(){
		if(this.getEstaCancelado())
			return Estado.CANCELADO.getValue();
		if(this.getEnCompensacion())
			
		return this.getEstado().getValue();
	}*/
	
	// Buss flags getters y setters
	// View flags getters
	// View getters
}
