//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * VO correspondiente a un Detalle de Seleccion Almacenada
 * 
 * @author tecso
 *
 */
public class SelAlmDetVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "selAlmDetVO";
	
	private SelAlmVO selAlm = new SelAlmVO();
	
	// Corresponde al id de una deuda, contribuyente, cuenta, plan o recibo
	private Long idElemento;
   
	private DeudaAdminVO deudaAdmin = new DeudaAdminVO();
	
	private TipoSelAlmVO tipoSelAlmDet = new TipoSelAlmVO();  
 
	// Contructores
	public SelAlmDetVO(){
		super();
	}

	// Getters y Setters
	public Long getIdElemento() {
		return idElemento;
	}
	public void setIdElemento(Long idElemento) {
		this.idElemento = idElemento;
	}
	public SelAlmVO getSelAlm() {
		return selAlm;
	}
	public void setSelAlm(SelAlmVO selAlm) {
		this.selAlm = selAlm;
	}
	public DeudaAdminVO getDeudaAdmin() {
		return deudaAdmin;
	}
	public void setDeudaAdmin(DeudaAdminVO deudaAdmin) {
		this.deudaAdmin = deudaAdmin;
	}
	public TipoSelAlmVO getTipoSelAlmDet() {
		return tipoSelAlmDet;
	}
	public void setTipoSelAlmDet(TipoSelAlmVO tipoSelAlmDet) {
		this.tipoSelAlmDet = tipoSelAlmDet;
	}
	
	
	
}
