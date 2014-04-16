//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del DetAju
 * @author tecso
 *
 */
public class DetAjuVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "detAjuVO";
	
    private Date fecha;
	
	private OrdenControlVO ordenControl;

    private Double totalAjustePos;
	
    private Double totalAjusteNeg;
    
    private Double totalAjusteAct;
    
    private Date fechaActualizacion=new Date();
	
	private OrdConCueVO ordConCue= new OrdConCueVO();

	private List<DetAjuDetVO> listDetAjuDet = new ArrayList<DetAjuDetVO>();
	// Buss Flags
	
	
	// View Constants
	
		
	private String fechaView = "";
	private String totalAjusteNegView = "";
	private String totalAjustePosView = "";


	// Constructores
	public DetAjuVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public DetAjuVO(int id, String desc) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters

	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControlVO ordenControl) {
		this.ordenControl = ordenControl;
	}

	public OrdConCueVO getOrdConCue() {
		return ordConCue;
	}

	public void setOrdConCue(OrdConCueVO ordConCue) {
		this.ordConCue = ordConCue;
	}

	public List<DetAjuDetVO> getListDetAjuDet() {
		return listDetAjuDet;
	}

	public void setListDetAjuDet(List<DetAjuDetVO> listDetAjuDet) {
		this.listDetAjuDet = listDetAjuDet;
	}


	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;		
		this.fechaView = DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK);
	}

	public Double getTotalAjusteAct() {
		return totalAjusteAct;
	}

	public void setTotalAjusteAct(Double totalAjusteAct) {
		this.totalAjusteAct = totalAjusteAct;
	}
	// Buss flags getters y setters
	
	
	// View flags getters
	
	


	public Double getTotalAjustePos() {
		return totalAjustePos;
	}

	public void setTotalAjustePos(Double totalAjustePos) {
		this.totalAjustePos = totalAjustePos;
		this.totalAjustePosView = StringUtil.formatDouble(totalAjustePos);
	}

	public Double getTotalAjusteNeg() {
		return totalAjusteNeg;
	}

	public void setTotalAjusteNeg(Double totalAjusteNeg) {
		this.totalAjusteNeg = totalAjusteNeg;
		this.totalAjusteNegView = StringUtil.formatDouble(totalAjusteNeg);
	}

	// View getters
	public void setFechaView(String fechaView) {
		this.fechaView = fechaView;
	}
	public String getFechaView() {
		return fechaView;
	}

	public void setTotalAjusteNegView(String totalAjusteNegView) {
		this.totalAjusteNegView = totalAjusteNegView;
	}
	public String getTotalAjusteNegView() {
		return totalAjusteNegView;
	}

	public void setTotalAjustePosView(String totalAjustePosView) {
		this.totalAjustePosView = totalAjustePosView;
	}
	public String getTotalAjustePosView() {
		return totalAjustePosView;
	}


	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}
	
	public String getFechaActualizacionView(){
		return (this.fechaActualizacion!=null)?DateUtil.formatDate(fechaActualizacion, DateUtil.ddSMMSYYYY_MASK):DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK);
	}

	public Integer getCantDetAjuDet(){
		return listDetAjuDet.size();
	}
	
	public String getTotalAjusteActView(){
		return (totalAjusteAct!=null)?NumberUtil.round(totalAjusteAct, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
		
}
