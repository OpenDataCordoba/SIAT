//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.model.SiNo;

public class BrocheVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private String desBroche;
	private RecursoVO recurso = new RecursoVO();
	private TipoBrocheVO tipoBroche = new TipoBrocheVO();
	private String strDomicilioEnvio;
	private RepartidorVO repartidor;
	private String telefono;
	private SiNo exentoEnvioJud = SiNo.OpcionSelecionar;
	private SiNo permiteImpresion = SiNo.OpcionSelecionar;
	
	// Listas de Entidades Relacionadas con Broche
	private List<BroCueVO> listBroCue = new ArrayList<BroCueVO>();
	
	// Buss Flags
	private Boolean asignarCuentaBussEnabled = true;
	
	// Constructores
	
	public BrocheVO(){
		super();
	}

	public BrocheVO(int id, String desBroche) {
		super(id);
		setDesBroche(desBroche);
	}

	//Getters y Setters
	
	public String getDesBroche() {
		return desBroche;
	}
	public void setDesBroche(String desBroche) {
		this.desBroche = desBroche;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public RepartidorVO getRepartidor() {
		return repartidor;
	}
	public void setRepartidor(RepartidorVO repartidor) {
		this.repartidor = repartidor;
	}
	public String getStrDomicilioEnvio() {
		return strDomicilioEnvio;
	}
	public void setStrDomicilioEnvio(String strDomicilioEnvio) {
		this.strDomicilioEnvio = strDomicilioEnvio;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public TipoBrocheVO getTipoBroche() {
		return tipoBroche;
	}
	public void setTipoBroche(TipoBrocheVO tipoBroche) {
		this.tipoBroche = tipoBroche;
	}
	public List<BroCueVO> getListBroCue() {
		return listBroCue;
	}
	public void setListBroCue(List<BroCueVO> listBroCue) {
		this.listBroCue = listBroCue;
	}
	public String getIdView() {
		if("-1".equals(super.getIdView()))
			return this.getDesBroche();
		else
			return super.getIdView(); 
	}
	
	// Buss flags getters y setters
	public Boolean getAsignarCuentaBussEnabled() {
		return asignarCuentaBussEnabled;
	}
	public void setAsignarCuentaBussEnabled(Boolean AsignarCuentaBussEnabled) {
		this.asignarCuentaBussEnabled = AsignarCuentaBussEnabled;
	}

	public String getAsignarCuentaEnabled() {
		return this.getAsignarCuentaBussEnabled() ? ENABLED : DISABLED;
	}

	public void setExentoEnvioJud(SiNo exentoEnvioJud) {
		this.exentoEnvioJud = exentoEnvioJud;
	}
	public SiNo getExentoEnvioJud() {
		return exentoEnvioJud;
	}

	public SiNo getPermiteImpresion() {
		return permiteImpresion;
	}
	public void setPermiteImpresion(SiNo permiteImpresion) {
		this.permiteImpresion = permiteImpresion;
	}
	
	public String getRepresent() {
		if("-1".equals(super.getIdView()))
			return this.getDesBroche();
		else {
			return this.getTipoBroche().getDesTipoBroche() + " - " + super.getIdView() + " - " + this.getDesBroche();
		}
	}

}
