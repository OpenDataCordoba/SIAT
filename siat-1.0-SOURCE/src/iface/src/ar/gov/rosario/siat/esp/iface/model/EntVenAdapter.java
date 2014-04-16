//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.model.CuentaBancoVO;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesVO;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Adapter de Entradas Vendidas
 * 
 * @author tecso
 */
public class EntVenAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "entVenAdapterVO";

	EntVenVO entVen = new EntVenVO();
	
	private Double importe =0.0;
	
	private List<EntHabVO> listEntHab = new ArrayList<EntHabVO>();  
	private List<EntVenVO> listEntVen = new ArrayList<EntVenVO>();
	private HabilitacionVO habilitacion = new HabilitacionVO();
	private String importeView="0.0";
	private Date fechaAnulacion;
	private Date fechaVencimiento;
	private EmisionVO emision = new EmisionVO();
	
	private String fechaAnulacionView = "";
	private String fechaVencimientoView = "";
	
	private List<Long> listIdEntVen = new ArrayList<Long>();
	private Integer esInterna = 0; // 0 - externa
								   // 1 - interna
	private AreaVO area = new AreaVO();
	private List<AreaVO> listArea = new ArrayList<AreaVO>();
	private CuentaBancoVO cuentaBanco = new CuentaBancoVO();
	private OtrIngTesVO otrIngTes = new OtrIngTesVO();
	private List<CuentaBancoVO> listCuentaBanco = new ArrayList<CuentaBancoVO>();
 	
	// Variable que indica si debe reemplazar el selectedId por el id guardado
	private String idCuenta = null;	

	// Variable temporal para origen DDJJ EntVen
	private boolean paramDDJJ = false;
	
	private boolean paramDescuentoUtilizado = false;

	public EntVenAdapter(){
		super(EspSecurityConstants.ABM_ENTVEN);
	}

	// Getters & Setters
	public EntVenVO getEntVen() {
		return entVen;
	}
	public void setEntVen(EntVenVO entVen) {
		this.entVen = entVen;
	}
	public List<EntHabVO> getListEntHab() {
		return listEntHab;
	}
	public void setListEntHab(List<EntHabVO> listEntHab) {
		this.listEntHab = listEntHab;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.formatDouble(importe);
	}

	public String getImporteView() {
		return importeView;
	}

	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}

	public List<EntVenVO> getListEntVen() {
		return listEntVen;
	}

	public void setListEntVen(List<EntVenVO> listEntVen) {
		this.listEntVen = listEntVen;
	}

	public HabilitacionVO getHabilitacion() {
		return habilitacion;
	}

	public void setHabilitacion(HabilitacionVO habilitacion) {
		this.habilitacion = habilitacion;
	}

	public Date getFechaAnulacion() {
		return fechaAnulacion;
	}

	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
		this.fechaAnulacionView = DateUtil.formatDate(fechaAnulacion, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaAnulacionView() {
		return fechaAnulacionView;
	}

	public void setFechaAnulacionView(String fechaAnulacionView) {
		this.fechaAnulacionView = fechaAnulacionView;
	}

	public EmisionVO getEmision() {
		return emision;
	}

	public void setEmision(EmisionVO emision) {
		this.emision = emision;
	}

	public List<Long> getListIdEntVen() {
		return listIdEntVen;
	}

	public void setListIdEntVen(List<Long> listIdEntVen) {
		this.listIdEntVen = listIdEntVen;
	}

	public Integer getEsInterna() {
		return esInterna;
	}

	public void setEsInterna(Integer esInterna) {
		this.esInterna = esInterna;
	}

	public AreaVO getArea() {
		return area;
	}

	public void setArea(AreaVO area) {
		this.area = area;
	}

	public List<AreaVO> getListArea() {
		return listArea;
	}

	public void setListArea(List<AreaVO> listArea) {
		this.listArea = listArea;
	}

	public CuentaBancoVO getCuentaBanco() {
		return cuentaBanco;
	}

	public void setCuentaBanco(CuentaBancoVO cuentaBanco) {
		this.cuentaBanco = cuentaBanco;
	}

	public OtrIngTesVO getOtrIngTes() {
		return otrIngTes;
	}

	public void setOtrIngTes(OtrIngTesVO otrIngTes) {
		this.otrIngTes = otrIngTes;
	}

	public List<CuentaBancoVO> getListCuentaBanco() {
		return listCuentaBanco;
	}

	public void setListCuentaBanco(List<CuentaBancoVO> listCuentaBanco) {
		this.listCuentaBanco = listCuentaBanco;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
		this.fechaVencimientoView = DateUtil.formatDate(fechaVencimiento, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaVencimientoView() {
		return fechaVencimientoView;
	}

	public void setFechaVencimientoView(String fechaVencimientoView) {
		this.fechaVencimientoView = fechaVencimientoView;
	}

	public String getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}

	public boolean isParamDDJJ() {
		return paramDDJJ;
	}

	public void setParamDDJJ(boolean paramDDJJ) {
		this.paramDDJJ = paramDDJJ;
	}

	public boolean isParamDescuentoUtilizado() {
		return paramDescuentoUtilizado;
	}

	public void setParamDescuentoUtilizado(boolean paramDescuentoUtilizado) {
		this.paramDescuentoUtilizado = paramDescuentoUtilizado;
	}
	
	
}
