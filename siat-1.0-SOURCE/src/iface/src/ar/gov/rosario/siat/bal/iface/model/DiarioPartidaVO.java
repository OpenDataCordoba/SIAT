//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class DiarioPartidaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private Date fecha;
	private BalanceVO balance = new BalanceVO();
	private TipOriMovVO tipOriMov = new TipOriMovVO();
	private Long idOrigen;
	private PartidaVO partida = new PartidaVO();
	private Double importeEjeAct;
	private Double importeEjeVen;
	private EjercicioVO ejercicio = new EjercicioVO();
	private EstDiaParVO estDiaPar = new EstDiaParVO();

	private String fechaView = "";
	private String importeEjeActView = "";
	private String importeEjeVenView = "";
	
	// Constructores 
	public DiarioPartidaVO(){
		super();
	}

	// Getters y Setters
	
	public String getFechaView() {
		return fechaView;
	}
	public void setFechaView(String fechaView) {
		this.fechaView = fechaView;
	}
	public EjercicioVO getEjercicio() {
		return ejercicio;
	}
	public void setEjercicio(EjercicioVO ejercicio) {
		this.ejercicio = ejercicio;
	}
	public EstDiaParVO getEstDiaPar() {
		return estDiaPar;
	}
	public void setEstDiaPar(EstDiaParVO estDiaPar) {
		this.estDiaPar = estDiaPar;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
		this.fechaView = DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK);
	}
	public Long getIdOrigen() {
		return idOrigen;
	}
	public void setIdOrigen(Long idOrigen) {
		this.idOrigen = idOrigen;
	}
	public Double getImporteEjeAct() {
		return importeEjeAct;
	}
	public void setImporteEjeAct(Double importeEjeAct) {
		this.importeEjeAct = importeEjeAct;
		this.importeEjeActView = StringUtil.formatDouble(importeEjeAct);
	}
	public String getImporteEjeActView() {
		return importeEjeActView;
	}
	public void setImporteEjeActView(String importeEjeActView) {
		this.importeEjeActView = importeEjeActView;
	}
	public Double getImporteEjeVen() {
		return importeEjeVen;
	}
	public void setImporteEjeVen(Double importeEjeVen) {
		this.importeEjeVen = importeEjeVen;
		this.importeEjeVenView = StringUtil.formatDouble(importeEjeVen);
	}
	public String getImporteEjeVenView() {
		return importeEjeVenView;
	}
	public void setImporteEjeVenView(String importeEjeVenView) {
		this.importeEjeVenView = importeEjeVenView;
	}
	public PartidaVO getPartida() {
		return partida;
	}
	public void setPartida(PartidaVO partida) {
		this.partida = partida;
	}
	public TipOriMovVO getTipOriMov() {
		return tipOriMov;
	}
	public void setTipOriMov(TipOriMovVO tipOriMov) {
		this.tipOriMov = tipOriMov;
	}
	public BalanceVO getBalance() {
		return balance;
	}
	public void setBalance(BalanceVO balance) {
		this.balance = balance;
	}
	

}
