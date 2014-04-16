//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del ProPreDeu
 * @author tecso
 *
 */
public class ProPreDeuVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	public  static final String ADP_PARAM_ID   = "ID";
	
	public static String NAME = "proPreDeuVO";
	private ViaDeudaVO viaDeuda = new ViaDeudaVO(); 
	private ServicioBancoVO servicioBanco = new ServicioBancoVO();
	private CorridaVO corrida = new CorridaVO();
	private Date fechaTope;
	
	// View 
	private String fechaTopeView = "";
	
	// View Flags
	private boolean administrarProcesoEnabled = true;
	private boolean modificarVia = true;
	
	// Constructores
	public ProPreDeuVO() {
		super();
	}

	// Getters y Setters
	public ViaDeudaVO getViaDeuda() {
		return viaDeuda;
	}

	public void setViaDeuda(ViaDeudaVO viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

	public ServicioBancoVO getServicioBanco() {
		return servicioBanco;
	}


	public void setServicioBanco(ServicioBancoVO servicioBanco) {
		this.servicioBanco = servicioBanco;
	}


	public CorridaVO getCorrida() {
		return corrida;
	}


	public void setCorrida(CorridaVO corrida) {
		this.corrida = corrida;
	}


	public Date getFechaTope() {
		return fechaTope;
	}

	public void setFechaTope(Date fechaTope) {
		this.fechaTope = fechaTope;
		this.fechaTopeView = DateUtil.formatDate(fechaTope, DateUtil.ddSMMSYYYY_MASK); 
	}

	// View getters
	public String getFechaTopeView() {
		return fechaTopeView;
	}

	public void setFechaTopeView(String fechaTopeView) {
		this.fechaTopeView = fechaTopeView;
	}

	// View Flags
	public boolean isAdministrarProcesoEnabled() {
		return administrarProcesoEnabled;
	}

	public void setAdministrarProcesoEnabled(boolean administrarProcesoEnabled) {
		this.administrarProcesoEnabled = administrarProcesoEnabled;
	}

	public boolean isModificarVia() {
		return modificarVia;
	}

	public void setModificarVia(boolean modificarVia) {
		this.modificarVia = modificarVia;
	}

}
