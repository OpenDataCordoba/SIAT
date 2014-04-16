//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * SearchPage del Procedimiento
 * 
 * @author Tecso
 *
 */
public class ProcedimientoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "procedimientoSearchPageVO";
	
	private ProcedimientoVO procedimiento= new ProcedimientoVO();
	
	private Date fechaVerOpoDesde;
	private Date fechaVerOpoHasta;	
	private String fechaVerOpoDesdeView = "";
	private String fechaVerOpoHastaView = "";
	
	private List<TipoProcesoVO> listTipoProceso = new ArrayList<TipoProcesoVO>();
	private List<JuzgadoVO> listJuzgado = new ArrayList<JuzgadoVO>();
	private List<AbogadoVO> listAbogado = new ArrayList<AbogadoVO>();
	private List<EstadoProcedVO> listEstadoProced = new ArrayList<EstadoProcedVO>();
	
	private List<SiNo> listSiNo = new ArrayList<SiNo>();
		
	private SiNo poseeOrdenControl = SiNo.OpcionSelecionar;

	// Bandera para consulta estado en historico o no en historico
	private Boolean estadoEnHistorico = true;
	
	// Constructores
	public ProcedimientoSearchPage() {       
       super(CyqSecurityConstants.ABM_PROCEDIMIENTO_CyQ);        
    }
	
	// Getters y Setters
	public ProcedimientoVO getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(ProcedimientoVO procedimiento) {
		this.procedimiento = procedimiento;
	}

	public List<AbogadoVO> getListAbogado() {
		return listAbogado;
	}

	public void setListAbogado(List<AbogadoVO> listAbogado) {
		this.listAbogado = listAbogado;
	}

	public List<JuzgadoVO> getListJuzgado() {
		return listJuzgado;
	}

	public void setListJuzgado(List<JuzgadoVO> listJuzgado) {
		this.listJuzgado = listJuzgado;
	}

	public List<EstadoProcedVO> getListEstadoProced() {
		return listEstadoProced;
	}

	public void setListEstadoProced(List<EstadoProcedVO> listEstadoProced) {
		this.listEstadoProced = listEstadoProced;
	}

	public Boolean getEstadoEnHistorico() {
		return estadoEnHistorico;
	}

	public void setEstadoEnHistorico(Boolean estadoEnHistorico) {
		this.estadoEnHistorico = estadoEnHistorico;
	}

	public List<TipoProcesoVO> getListTipoProceso() {
		return listTipoProceso;
	}

	public void setListTipoProceso(List<TipoProcesoVO> listTipoProceso) {
		this.listTipoProceso = listTipoProceso;
	}
	
	public Date getFechaVerOpoDesde() {
		return fechaVerOpoDesde;
	}
	public void setFechaVerOpoDesde(Date fechaVerOpoDesde) {
		this.fechaVerOpoDesde = fechaVerOpoDesde;
		this.fechaVerOpoDesdeView = DateUtil.formatDate(fechaVerOpoDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaVerOpoHasta() {
		return fechaVerOpoHasta;
	}
	public void setFechaVerOpoHasta(Date fechaVerOpoHasta) {
		this.fechaVerOpoHasta = fechaVerOpoHasta;
		this.fechaVerOpoHastaView = DateUtil.formatDate(fechaVerOpoHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaVerOpoDesdeView() {
		return fechaVerOpoDesdeView;
	}
	public void setFechaVerOpoDesdeView(String fechaVerOpoDesdeView) {
		this.fechaVerOpoDesdeView = fechaVerOpoDesdeView;
	}

	public String getFechaVerOpoHastaView() {
		return fechaVerOpoHastaView;
	}
	public void setFechaVerOpoHastaView(String fechaVerOpoHastaView) {
		this.fechaVerOpoHastaView = fechaVerOpoHastaView;
	}

	public SiNo getPoseeOrdenControl() {
		return poseeOrdenControl;
	}
	public void setPoseeOrdenControl(SiNo poseeOrdenControl) {
		this.poseeOrdenControl = poseeOrdenControl;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}
	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	// View getters
	public String getLiquidacionDeudaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(CyqSecurityConstants.ABM_PROCEDIMIENTO_CyQ, 
				CyqSecurityConstants.MTD_CAMBIAR_ESTADO); 
	}
	
}
