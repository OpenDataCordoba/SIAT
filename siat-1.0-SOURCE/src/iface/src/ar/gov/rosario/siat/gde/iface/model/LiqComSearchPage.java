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
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * SearchPage del LiqCom
 * 
 * @author Tecso
 *
 */
public class LiqComSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "liqComSearchPageVO";
	
	private LiqComVO liqCom= new LiqComVO();
	
	private Date fechaLiqDesde = new Date();
	private Date fechaLiqHasta = new Date();
	
	private List<ServicioBancoVO> listServicioBanco = new ArrayList<ServicioBancoVO>();
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
	private List<EstadoCorridaVO> listEstadoCorrida = new ArrayList<EstadoCorridaVO>();
	
	private String fechaLiqDesdeView = "";
	private String fechaLiqHastaView = "";


	// Constructores
	public LiqComSearchPage() {       
       super(GdeSecurityConstants.ABM_LIQCOM);        
    }
	
	// Getters y Setters
	public LiqComVO getLiqCom() {
		return liqCom;
	}
	public void setLiqCom(LiqComVO liqCom) {
		this.liqCom = liqCom;
	}

	public Date getFechaLiqDesde() {
		return fechaLiqDesde;
	}

	public void setFechaLiqDesde(Date fechaLiqDesde) {
		this.fechaLiqDesde = fechaLiqDesde;
		this.fechaLiqDesdeView = DateUtil.formatDate(fechaLiqDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaLiqHasta() {
		return fechaLiqHasta;
	}

	public void setFechaLiqHasta(Date fechaLiqHasta) {
		this.fechaLiqHasta = fechaLiqHasta;
		this.fechaLiqHastaView = DateUtil.formatDate(fechaLiqHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<EstadoCorridaVO> getListEstadoCorrida() {
		return listEstadoCorrida;
	}

	public void setListEstadoCorrida(List<EstadoCorridaVO> listEstadoCorrida) {
		this.listEstadoCorrida = listEstadoCorrida;
	}

	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}

	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}

	public List<ServicioBancoVO> getListServicioBanco() {
		return listServicioBanco;
	}
	
	public void setListServicioBanco(List<ServicioBancoVO> listServicioBanco) {
		this.listServicioBanco = listServicioBanco;
	}
	// View getters
	
	/*/ Se utiliza en la inclusion del caso
	public List<SistemaOrigenVO> getListSistemaOrigen(){
		return liqCom.getCaso().getListSistemaOrigen();
	}*/


	public void setFechaLiqDesdeView(String fechaLiqDesdeView) {
		this.fechaLiqDesdeView = fechaLiqDesdeView;
	}
	public String getFechaLiqDesdeView() {
		return fechaLiqDesdeView;
	}

	public void setFechaLiqHastaView(String fechaLiqHastaView) {
		this.fechaLiqHastaView = fechaLiqHastaView;
	}
	public String getFechaLiqHastaView() {
		return fechaLiqHastaView;
	}

	//  Flags getters
	public String getAdministrarProcesoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_LIQCOM, GdeSecurityConstants.MTD_ADM_PROCESO);
	}	
}
