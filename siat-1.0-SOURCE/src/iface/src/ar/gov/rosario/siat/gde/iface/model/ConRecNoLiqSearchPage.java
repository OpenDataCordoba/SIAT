//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Representa el searchPage para la consulta de convenios/recibos no liquidables
 * @author Tecso
 *
 */
public class ConRecNoLiqSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "conRecNoLiqSearchPageVO";
	
	private String tipoPago;// no se saca de ninguna tabla
	
	private Integer numero;
	
	private String idRecurso;
	
	private String idProcurador;
	
	private Date fechaDesde;
	
	private Date fechaHasta;
	
	private String[] idsSelected;
	
	private boolean resultTipoRecibo = false;
	
	private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	private String numeroView = "";

	// view flags
	private boolean procesarConRecnoLiq=true;
	private boolean volverLiquidable=true;
	
	// Constructores
	public ConRecNoLiqSearchPage() {       
       super(GdeSecurityConstants.CONSULTAR_CONRECNOLIQ);        
    }

	// Getters y Setters
	public String getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
		this.numeroView = StringUtil.formatInteger(numero);
	}

	public String getIdRecurso() {
		return idRecurso;
	}

	public void setIdRecurso(String idRecurso) {
		this.idRecurso = idRecurso;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public String[] getIdsSelected() {
		return idsSelected;
	}

	public void setIdsSelected(String[] idsSelected) {
		this.idsSelected = idsSelected;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	

	public boolean getResultTipoRecibo() {
		return resultTipoRecibo;
	}

	public void setResultTipoRecibo(boolean resultTipoRecibo) {
		this.resultTipoRecibo = resultTipoRecibo;
	}

	public String getIdProcurador() {
		return idProcurador;
	}

	public void setIdProcurador(String idProcurador) {
		this.idProcurador = idProcurador;
	}

	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}

	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}

	// View getters
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}

	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	public String getFechaHastaView() {
		return fechaHastaView;
	}

	public void setNumeroView(String numeroView) {
		this.numeroView = numeroView;
	}
	public String getNumeroView() {
		return numeroView;
	}


	public String getProcesarConRecnoLiq() {
		return SiatBussImageModel.hasEnabledFlag(procesarConRecnoLiq, GdeSecurityConstants.CONSULTAR_CONRECNOLIQ, GdeSecurityConstants.MTD_CONRECNOLIQ_PROCESAR);
	}
	
	public String getVolverLiquidable() {
		return SiatBussImageModel.hasEnabledFlag(volverLiquidable, GdeSecurityConstants.CONSULTAR_CONRECNOLIQ, GdeSecurityConstants.MTD_CONRECNOLIQ_VOLVER_LIQUIDABLE);
	}



}
