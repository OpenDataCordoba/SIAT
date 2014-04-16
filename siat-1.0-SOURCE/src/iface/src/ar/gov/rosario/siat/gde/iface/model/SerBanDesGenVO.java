//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Servicio Banco - DesGen
 * @author tecso
 *
 */
public class SerBanDesGenVO extends SiatBussImageModel {
	private static final long serialVersionUID = 1L;

	public static final String NAME = "serBanRecVO";

	private ServicioBancoVO		servicioBanco = new ServicioBancoVO();
	private DesGenVO	desGen = new DesGenVO();
	private Date	fechaDesde;
	private String	fechaDesdeView = "";
	private Date	fechaHasta;
	private String	fechaHastaView = "";


	// Constructores
	public SerBanDesGenVO() {
		super();
	}

	// Getters y Setters
	public ServicioBancoVO getServicioBanco(){
		return servicioBanco;
	}
	public void setServicioBanco(ServicioBancoVO servicioBanco){
		this.servicioBanco = servicioBanco;
	}
	public DesGenVO getDesGen(){
		return desGen;
	}
	public void setDesGen(DesGenVO desGen){
		this.desGen = desGen;
	}
	public Date getFechaDesde(){
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde){
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaDesdeView(){
		return fechaDesdeView;
	}
	public void setFechaDesdeView(){
	}
	
	public Date getFechaHasta(){
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta){
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaHastaView(){
		return fechaHastaView;
	}
	public void setFechaHastaView(){
	}
	
}
