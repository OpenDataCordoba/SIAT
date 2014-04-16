//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.model.PartidaVO;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class InformeTotalPar extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private String fechaDesde = null;
	private String fechaHasta = null;

	private PartidaVO partida = new PartidaVO();
	private List<PartidaVO> listPartida = new ArrayList<PartidaVO>();
		
	private String rangosFechaExtra = "false";
	private String totalRangos = "";
	
	// Getters y Setters
	public String getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public List<PartidaVO> getListPartida() {
		return listPartida;
	}
	public void setListPartida(List<PartidaVO> listPartida) {
		this.listPartida = listPartida;
	}
	public PartidaVO getPartida() {
		return partida;
	}
	public void setPartida(PartidaVO partida) {
		this.partida = partida;
	}
	public String getRangosFechaExtra() {
		return rangosFechaExtra;
	}
	public void setRangosFechaExtra(String rangosFechaExtra) {
		this.rangosFechaExtra = rangosFechaExtra;
	}
	public String getTotalRangos() {
		return totalRangos;
	}
	public void setTotalRangos(String totalRangos) {
		this.totalRangos = totalRangos;
	}

	
}
