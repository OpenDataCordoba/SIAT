//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del OpeInvBus
 * @author tecso
 *
 */
public class OpeInvBusVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String SEPARADOR_PARAMETROS =";";
	
	public static final String NAME = "opeInvBusVO";
	
	private OpeInvVO opeInv= new OpeInvVO();

	private Integer tipBus;
	
	private Date fechaBusqueda;
	
	private String descripcion="";
	
	private String paramSel="";
	
	private CorridaVO corrida = new CorridaVO(); 

	private String fechaBusquedaView="";
	
	// Buss Flags
	private boolean administrarProcesoBussEnabled= true;
	
	// View Constants
	
	
	// Constructores
	public OpeInvBusVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public OpeInvBusVO(int id, String desc) {
		super();
		setId(new Long(id));	
		setDescripcion(desc);
	}


	// Getters y Setters

	public OpeInvVO getOpeInv() {
		return opeInv;
	}

	public void setOpeInv(OpeInvVO opeInv) {
		this.opeInv = opeInv;
	}

	public Date getFechaBusqueda() {
		return fechaBusqueda;
	}

	public void setFechaBusqueda(Date fechaBusqueda) {
		this.fechaBusqueda = fechaBusqueda;
		this.fechaBusquedaView = DateUtil.formatDate(fechaBusqueda, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getParamSel() {
		return paramSel;
	}

	public void setParamSel(String paramSel) {
		this.paramSel = paramSel;
	}

	public CorridaVO getCorrida() {
		return corrida;
	}

	public void setCorrida(CorridaVO corrida) {
		this.corrida = corrida;
	}

	public Integer getTipBus() {
		return tipBus;
	}

	public void setTipBus(Integer tipBus) {
		this.tipBus = tipBus;
	}
	
	// Buss flags getters y setters

	// View flags getters
	public boolean isAdministrarProcesoBussEnabled() {
		return administrarProcesoBussEnabled;
	}
	
	public void setAdministrarProcesoBussEnabled(
			boolean administrarProcesoBussEnabled) {
		this.administrarProcesoBussEnabled = administrarProcesoBussEnabled;
	}
	
	
	// View getters
	public String getFechaBusquedaView() {
		return fechaBusquedaView;
	}
	
	public void setFechaBusquedaView(String fechaBusquedaView) {
		this.fechaBusquedaView = fechaBusquedaView;
	}

	/**
	 * Devuelve la cadena de parametros formateada para salida por HTML
	 */
	public String getParamSelForHtml(){
		String tmp[] = paramSel.split(SEPARADOR_PARAMETROS);
		String ret = "";
		
		for(String str:tmp){
			ret +="<b>"+str+"</b><br>";
		}
		
		return ret;
	}
}
