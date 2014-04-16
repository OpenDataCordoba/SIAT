//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del ParSel
 * @author tecso
 *
 */
public class ParSelVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "parSelVO";
	
	private SelladoVO sellado = new SelladoVO();
	private PartidaVO partida = new PartidaVO();
	private TipoDistribVO tipoDistrib = new TipoDistribVO();
	
	private Double monto;
	
	// Buss Flags
	
	// View Constants
	private String montoView="";
	
	// Constructores
	public ParSelVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ParSelVO(int id, String desc) {
		super();
		setId(new Long(id));
	}

	//	 Getters y Setters
	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
		this.montoView = StringUtil.formatDouble(monto);
	}

	public PartidaVO getPartida() {
		return partida;
	}

	public void setPartida(PartidaVO partida) {
		this.partida = partida;
	}

	public SelladoVO getSellado() {
		return sellado;
	}

	public void setSellado(SelladoVO sellado) {
		this.sellado = sellado;
	}

	public TipoDistribVO getTipoDistrib() {
		return tipoDistrib;
	}

	public void setTipoDistrib(TipoDistribVO tipoDistrib) {
		this.tipoDistrib = tipoDistrib;
	}

	// Buss flags getters y setters
	
	// View getters
	public String getMontoView() {
		return montoView;
	}

	public void setMontoView(String montoView) {
		this.montoView = montoView;
	}
	
}
