//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del ObrRepVen
 * @author tecso
 *
 */
public class ObrRepVenVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "obrRepVenVO";
	
	private ObraVO obra = new ObraVO();
	private Integer cuotaDesde;
	private Date nueFecVen;
	private String descripcion;
	private Integer canDeuAct;
	private CasoVO caso = new CasoVO();
	
	private String cuotaDesdeView = "";
	private String nueFecVenView = "";
	private String canDeuActView = "";
	
	// Constructores
	public ObrRepVenVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ObrRepVenVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDescripcion(desc);
	}

	// Getters y Setters
	public ObraVO getObra() {
		return obra;
	}

	public void setObra(ObraVO obra) {
		this.obra = obra;
	}

	public Integer getCuotaDesde() {
		return cuotaDesde;
	}

	public void setCuotaDesde(Integer cuotaDesde) {
		this.cuotaDesde = cuotaDesde;
		this.cuotaDesdeView = StringUtil.formatInteger(cuotaDesde);
	}

	public Date getNueFecVen() {
		return nueFecVen;
	}

	public void setNueFecVen(Date nueFecVen) {
		this.nueFecVen = nueFecVen;
		this.nueFecVenView = DateUtil.formatDate(nueFecVen, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getCanDeuAct() {
		return canDeuAct;
	}

	public void setCanDeuAct(Integer canDeuAct) {
		this.canDeuAct = canDeuAct;
		this.canDeuActView = StringUtil.formatInteger(canDeuAct);
	}

	public CasoVO getCaso() {
		return caso;
	}

	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}

	
	// View getters
	public String getCuotaDesdeView() {
		return cuotaDesdeView;
	}

	public void setCuotaDesdeView(String cuotaDesdeView) {
		this.cuotaDesdeView = cuotaDesdeView;
	}

	public String getNueFecVenView() {
		return nueFecVenView;
	}

	public void setNueFecVenView(String nueFecVenView) {
		this.nueFecVenView = nueFecVenView;
	}

	public String getCanDeuActView() {
		return canDeuActView;
	}

	public void setCanDeuActView(String canDeuActView) {
		this.canDeuActView = canDeuActView;
	}
	
}
