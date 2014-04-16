//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.model.CuentaBancoVO;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.TipoCancelacion;

/**
 * Value Object del PagoPriv
 * @author tecso
 *
 */
public class PagoPrivVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "pagoPrivVO";
	
	private ProcedimientoVO procedimiento = new ProcedimientoVO();

	private CuentaBancoVO cuentaBanco = new CuentaBancoVO();
	private Date fecha;
	private String descripcion;
	private Double importe;
	private TipoCancelacion tipoCancelacion = TipoCancelacion.OpcionSelecionar; 
	private CasoVO caso = new CasoVO();
	private Integer nroRecibo;
	
	private List<PagoPrivDeuVO> listPagoPrivDeu = new ArrayList<PagoPrivDeuVO>();
 	
	private String fechaView = "";
	private String importeView = "";
	private String nroReciboView = "";
	
	// Constructores
	public PagoPrivVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public PagoPrivVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDescripcion(desc);
	}

	// Getters y Setters
	public ProcedimientoVO getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(ProcedimientoVO procedimiento) {
		this.procedimiento = procedimiento;
	}

	public CuentaBancoVO getCuentaBanco() {
		return cuentaBanco;
	}
	public void setCuentaBanco(CuentaBancoVO cuentaBanco) {
		this.cuentaBanco = cuentaBanco;
	}

	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
		this.fechaView = DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.formatDouble(importe);
	}

	public List<PagoPrivDeuVO> getListPagoPrivDeu() {
		return listPagoPrivDeu;
	}
	
	public void setListPagoPrivDeu(List<PagoPrivDeuVO> listPagoPrivDeu) {
		this.listPagoPrivDeu = listPagoPrivDeu;
	}
	
	public TipoCancelacion getTipoCancelacion() {
		return tipoCancelacion;
	}
	public void setTipoCancelacion(TipoCancelacion tipoCancelacion) {
		this.tipoCancelacion = tipoCancelacion;
	}
	
	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	
	public Integer getNroRecibo() {
		return nroRecibo;
	}
	public void setNroRecibo(Integer nroRecibo) {
		this.nroRecibo = nroRecibo;
		
		if (nroRecibo == null){
			this.nroReciboView = "El Recibo no ha sido Impreso.";
		} else {
			this.nroReciboView = StringUtil.formatInteger(nroRecibo);			
		}
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public String getFechaView() {
		return fechaView;
	}
	public void setFechaView(String fechaView) {
		this.fechaView = fechaView;
	}
	
	public String getImporteView() {
		return importeView;
	}
	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}

	public String getNroReciboView() {
		return nroReciboView;
	}
	public void setNroReciboView(String nroReciboView) {
		this.nroReciboView = nroReciboView;
	}
	
	public String getImportePalabras(){
		return NumberUtil.getNroEnPalabras(importe, true);
	}

	
}
