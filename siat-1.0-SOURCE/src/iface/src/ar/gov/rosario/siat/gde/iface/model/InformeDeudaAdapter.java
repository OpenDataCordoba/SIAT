//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;

public class InformeDeudaAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	Date fechaConfeccion;
	String fechaConfeccionView;
	boolean esAutoliquidable=false;
	
	InformeDeudaCaratula infomeDeudaCaratula = new InformeDeudaCaratula();
	
	/** Adapter que contiene la cuenta principal */
	LiqDeudaAdapter liqDeudaAdapterCuentaPpal;
	
	/** cuentas relacionadas por ObjImp con sus deudas*/
	List<LiqDeudaAdapter> listCuentaRelObjImp = new ArrayList<LiqDeudaAdapter>();
	
	/** cuentas relacionadas por unificacion/desgloce con sus deudas*/
	List<LiqDeudaAdapter> listCuentaRelDesUni = new ArrayList<LiqDeudaAdapter>();
	
	private boolean poseeCaratula = false;
	
	private boolean poseeComercio=false;
	
	private DatosReciboCdMVO datosCdMVO= new DatosReciboCdMVO();
	
	public InformeDeudaAdapter(String sweActionName) {
		super(sweActionName);		
	}

	// Contructores
	public InformeDeudaAdapter() {
		super(GdeSecurityConstants.LIQ_DEUDA);//Ver si es esta constante
	}

	// Getters y Setter
	public Date getFechaConfeccion() {
		return fechaConfeccion;
	}

	public void setFechaConfeccion(Date fechaConfeccion) {
		this.fechaConfeccion = fechaConfeccion;
	}

	public InformeDeudaCaratula getInfomeDeudaCaratula() {
		return infomeDeudaCaratula;
	}

	public void setInfomeDeudaCaratula(InformeDeudaCaratula infomeDeudaCaratula) {
		this.infomeDeudaCaratula = infomeDeudaCaratula;
	}
	
	public LiqDeudaAdapter getLiqDeudaAdapterCuentaPpal() {
		return liqDeudaAdapterCuentaPpal;
	}

	public void setLiqDeudaAdapterCuentaPpal(
			LiqDeudaAdapter liqDeudaAdapterCuentaPpal) {
		this.liqDeudaAdapterCuentaPpal = liqDeudaAdapterCuentaPpal;
	}

	public List<LiqDeudaAdapter> getListCuentaRelObjImp() {
		return listCuentaRelObjImp;
	}

	public void setListCuentaRelObjImp(List<LiqDeudaAdapter> listCuentaRelObjImp) {
		this.listCuentaRelObjImp = listCuentaRelObjImp;
	}

	public List<LiqDeudaAdapter> getListCuentaRelDesUni() {
		return listCuentaRelDesUni;
	}

	public void setListCuentaRelDesUni(List<LiqDeudaAdapter> listCuentaRelDesUni) {
		this.listCuentaRelDesUni = listCuentaRelDesUni;
	}

	// view getters
	public String getFechaConfeccionView() {
		return DateUtil.formatDate(fechaConfeccion, DateUtil.ddSMMSYYYY_MASK);
	}

	
	
	public boolean getPoseeCaratula() {
		return poseeCaratula;
	}

	public void setPoseeCaratula(boolean poseeCaratula) {
		this.poseeCaratula = poseeCaratula;
	}

	public DatosReciboCdMVO getDatosCdMVO() {
		return datosCdMVO;
	}
	public void setDatosCdMVO(DatosReciboCdMVO datosCdMVO) {
		this.datosCdMVO = datosCdMVO;
	}

	public boolean getEsAutoliquidable() {
		return esAutoliquidable;
	}

	public void setEsAutoliquidable(boolean esAutoliquidable) {
		this.esAutoliquidable = esAutoliquidable;
	}

	public boolean getPoseeComercio() {
		return poseeComercio;
	}

	public void setPoseeComercio(boolean poseeComercio) {
		this.poseeComercio = poseeComercio;
	}
	
	
	
}
