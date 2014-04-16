//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Adapter del Reingreso
 * 
 * @author tecso
 */
public class ReingresoAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "reingresoAdapterVO";
	
    private ReingresoVO reingreso = new ReingresoVO();
    private BalanceVO balance = new BalanceVO();
    private Date fechaDesde;
    private Date fechaHasta;

    private String fechaDesdeView = "";
    private String fechaHastaView = "";

    private List<ReingresoVO> listReingreso = new ArrayList<ReingresoVO>(); 
    
    private List<IndetVO> listIndet = new ArrayList<IndetVO>();
        
    private String[] listNroReingresoSelected;
    
    private boolean permiteSeleccion = true; 
    private boolean mostrarFiltro = false;
    
    // Constructores
    public ReingresoAdapter(){
    	super(BalSecurityConstants.ABM_REINGRESO);
    }
    
    // Getters Y Setters
	public ReingresoVO getReingreso() {
		return reingreso;
	}
	public void setReingreso(ReingresoVO reingreso) {
		this.reingreso = reingreso;
	}
	public List<ReingresoVO> getListReingreso() {
		return listReingreso;
	}
	public void setListReingreso(List<ReingresoVO> listReingreso) {
		this.listReingreso = listReingreso;
	}
	public BalanceVO getBalance() {
		return balance;
	}
	public void setBalance(BalanceVO balance) {
		this.balance = balance;
	}
	public List<IndetVO> getListIndet() {
		return listIndet;
	}
	public void setListIndet(List<IndetVO> listIndet) {
		this.listIndet = listIndet;
	}
	public String[] getListNroReingresoSelected() {
		return listNroReingresoSelected;
	}
	public void setListNroReingresoSelected(String[] listNroReingresoSelected) {
		this.listNroReingresoSelected = listNroReingresoSelected;
	}
	public boolean isMostrarFiltro() {
		return mostrarFiltro;
	}
	public void setMostrarFiltro(boolean mostrarFiltro) {
		this.mostrarFiltro = mostrarFiltro;
	}

	public boolean isPermiteSeleccion() {
		return permiteSeleccion;
	}

	public void setPermiteSeleccion(boolean permiteSeleccion) {
		this.permiteSeleccion = permiteSeleccion;
	}
	
	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaDesdeView() {
		return fechaDesdeView;
	}

	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaHastaView() {
		return fechaHastaView;
	}

	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}

	public String getName(){
		return NAME;
	}
	
}
