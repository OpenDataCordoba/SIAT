//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdminVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Adapter de Deuda para Compensaciones
 * 
 * @author tecso
 */
public class ComDeuAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "comDeuAdapterVO";
	
	private ComDeuVO comDeu = new ComDeuVO();
	private CuentaVO cuenta = new CuentaVO();
	
	private Double saldoRestante;
	private String saldoRestanteView = "";
	
	private RecursoVO recurso = new RecursoVO();
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	LiqDeudaAdminVO liqDeudaAdmin = new LiqDeudaAdminVO();
	
	Map<String, Boolean> mapaCancelaPorMenos = new HashMap<String, Boolean>();
	
	Map<String, Double> mapaValorDeuda = new HashMap<String, Double>();
	
	private String[] listIdDeudaSelected;
	
	public ComDeuAdapter(){
		super(BalSecurityConstants.ABM_COMDEU);
	}

	// Getters Y Setters
	public ComDeuVO getComDeu() {
		return comDeu;
	}
	public void setComDeu(ComDeuVO comDeu) {
		this.comDeu = comDeu;
	}
	public Double getSaldoRestante() {
		return saldoRestante;
	}
	public void setSaldoRestante(Double saldoRestante) {
		this.saldoRestante = saldoRestante;
		this.saldoRestanteView = StringUtil.formatDouble(saldoRestante);
	}
	public String getSaldoRestanteView() {
		return saldoRestanteView;
	}
	public void setSaldoRestanteView(String saldoRestanteView) {
		this.saldoRestanteView = saldoRestanteView;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public LiqDeudaAdminVO getLiqDeudaAdmin() {
		return liqDeudaAdmin;
	}
	public void setLiqDeudaAdmin(LiqDeudaAdminVO liqDeudaAdmin) {
		this.liqDeudaAdmin = liqDeudaAdmin;
	}
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	public Map<String, Boolean> getMapaCancelaPorMenos() {
		return mapaCancelaPorMenos;
	}
	public void setMapaCancelaPorMenos(Map<String, Boolean> mapaCancelaPorMenos) {
		this.mapaCancelaPorMenos = mapaCancelaPorMenos;
	}
	public Map<String, Double> getMapaValorDeuda() {
		return mapaValorDeuda;
	}
	public void setMapaValorDeuda(Map<String, Double> mapaValorDeuda) {
		this.mapaValorDeuda = mapaValorDeuda;
	}
	public String[] getListIdDeudaSelected() {
		return listIdDeudaSelected;
	}
	public void setListIdDeudaSelected(String[] listIdDeudaSelected) {
		this.listIdDeudaSelected = listIdDeudaSelected;
	}
	
	
}
