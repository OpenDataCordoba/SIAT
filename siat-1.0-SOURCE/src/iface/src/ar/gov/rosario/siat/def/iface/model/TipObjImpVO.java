//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.pro.iface.model.ProcesoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * TipoObjImp
 * @author tecso
 *
 */
public class TipObjImpVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final long ID_TIPOBJIMP_PARCELA = 1; 
	
	public static final String NAME = "tipObjImpVO";
	
	private String codTipObjImp;
	private String desTipObjImp;
	private SiNo esSiat = SiNo.SI;
	
	private Date fechaAlta;
	private Date fechaBaja;
	
	private ProcesoVO proceso = new ProcesoVO();
	
	private String fechaAltaView = "";
	private String fechaBajaView = "";
	
	private List<TipObjImpAtrVO> listTipObjImpAtr = new ArrayList<TipObjImpAtrVO>();
	private List<TipObjImpAreOVO> listTipObjImpAreO = new ArrayList<TipObjImpAreOVO>();
	
		
	// Buss Flags
	
	private Boolean admAreaOrigenBussEnabled = true;
	
	// View Constants
	

	// Constructores
	public TipObjImpVO() {
		super();
	}
	public TipObjImpVO(int id, String desTipObjImp) {
		super(id);
		setDesTipObjImp(desTipObjImp);
	}

	// Getters y Setters
	public String getCodTipObjImp() {
		return codTipObjImp;
	}
	public void setCodTipObjImp(String codTipObjImp) {
		this.codTipObjImp = codTipObjImp;
	}
	public String getDesTipObjImp() {
		return desTipObjImp;
	}
	public void setDesTipObjImp(String desTipObjImp) {
		this.desTipObjImp = desTipObjImp;
	}
	public SiNo getEsSiat() {
		return esSiat;
	}
	public void setEsSiat(SiNo esSiat) {
		this.esSiat = esSiat;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAltaView = DateUtil.formatDate(fechaAlta, DateUtil.ddSMMSYYYY_MASK);
		this.fechaAlta = fechaAlta;
	}
	public String getFechaAltaView() {
		return fechaAltaView;
	}
	public void setFechaAltaView(String fechaAltaView) {
		this.fechaAltaView = fechaAltaView;
	}
	public Date getFechaBaja() {
		return fechaBaja;
	}
	public void setFechaBaja(Date fechaBaja) {
		this.fechaBajaView = DateUtil.formatDate(fechaBaja, DateUtil.ddSMMSYYYY_MASK);
		this.fechaBaja = fechaBaja;
	}
	public String getFechaBajaView() {
		return fechaBajaView;
	}
	public void setFechaBajaView(String fechaBajaView) {
		this.fechaBajaView = fechaBajaView;
	}
	public List<TipObjImpAreOVO> getListTipObjImpAreO() {
		return listTipObjImpAreO;
	}
	public void setListTipObjImpAreO(List<TipObjImpAreOVO> listTipObjImpAreO) {
		this.listTipObjImpAreO = listTipObjImpAreO;
	}
	public List<TipObjImpAtrVO> getListTipObjImpAtr() {
		return listTipObjImpAtr;
	}
	public void setListTipObjImpAtr(List<TipObjImpAtrVO> listTipObjImpAtr) {
		this.listTipObjImpAtr = listTipObjImpAtr;
	}
	
	// Buss flags getters y setters
	public Boolean getAdmAreaOrigenBussEnabled() {
		return admAreaOrigenBussEnabled;
	}
	public void setAdmAreaOrigenBussEnabled(Boolean admAreaOrigenBussEnabled) {
		this.admAreaOrigenBussEnabled = admAreaOrigenBussEnabled;
	}

	public String getAdmAreaOrigenEnabled() {
		return this.getAdmAreaOrigenBussEnabled() ? ENABLED : DISABLED;
	}
	
	public void setProceso(ProcesoVO proceso) {
		this.proceso = proceso;
	}
	public ProcesoVO getProceso() {
		return proceso;
	}
	
	// View flags getters
	
	
	// View getters
		
	public List<AtributoVO> getListAtributo(){
		List<AtributoVO> listAtributos = new ArrayList<AtributoVO>();
		
		for (TipObjImpAtrVO toiaVO : this.getListTipObjImpAtr()) {
			listAtributos.add(toiaVO.getAtributo());
		}
		return listAtributos;
	}


}
