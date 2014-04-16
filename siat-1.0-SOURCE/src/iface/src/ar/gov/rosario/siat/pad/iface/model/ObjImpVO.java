//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.TipObjImpVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Vigencia;

public class ObjImpVO extends SiatBussImageModel {
	private static final long serialVersionUID = 0;

	TipObjImpVO tipObjImp = new TipObjImpVO(); 
	List<ObjImpAtrValVO> listObjImpAtrVal = new ArrayList<ObjImpAtrValVO>();
	
	private String clave;
	private String claveFuncional;
	private String desClave;
	private String desClaveFuncional;	
	private Date fechaAlta;
	private Date fechaBaja;
	
	private String fechaAltaView;
	private String fechaBajaView;
	private Vigencia vigencia;
	
	// Constructores
	public ObjImpVO() {
		super();
	}
	
	public ObjImpVO(String clave) {
		super();
		this.clave = clave;
	}
	
	
	// Getters y Setters
	public List<ObjImpAtrValVO> getListObjImpAtrVal() {
		return listObjImpAtrVal;
	}
	public void setListObjImpAtrVal(List<ObjImpAtrValVO> listObjImpAtrVal) {
		this.listObjImpAtrVal = listObjImpAtrVal;
	}

	public TipObjImpVO getTipObjImp() {
		return tipObjImp;
	}
	public void setTipObjImp(TipObjImpVO tipObjImp) {
		this.tipObjImp = tipObjImp;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getClaveFuncional() {
		return claveFuncional;
	}

	public void setClaveFuncional(String claveFuncional) {
		this.claveFuncional = claveFuncional;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
		this.fechaAltaView = DateUtil.formatDate(fechaAlta, DateUtil.ddSMMSYYYY_MASK);
	}


	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
		this.fechaBajaView = DateUtil.formatDate(fechaBaja, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public String getDesClaveFuncional() {
		return desClaveFuncional;
	}

	public void setDesClaveFuncional(String desClaveFuncional) {
		this.desClaveFuncional = desClaveFuncional;
	}

	public String getDesClave() {
		return desClave;
	}

	public void setDesClave(String desClave) {
		this.desClave = desClave;
	}

	// Getters y Setters del View 
	public String getFechaAltaView() {
		return fechaAltaView;
	}
	
	public void setFechaAltaView(String fechaAltaView) {
		this.fechaAltaView = fechaAltaView;
	}

	public String getFechaBajaView() {
		return fechaBajaView;
	}
	public void setFechaBajaView(String fechaBajaView) {
		this.fechaBajaView = fechaBajaView;
	}
	
	public Vigencia getVigencia() {
		return vigencia;
	}

	public void setVigencia(Vigencia vigencia) {
		this.vigencia = vigencia;
	}
	
}
