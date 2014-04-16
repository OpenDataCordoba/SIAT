//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * SearchPage del Estadocuenta
 * 
 * @author Tecso
 *
 */
public class EstadoCuentaSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "EstadoCuentaSearchPageVO";
	
	private CuentaVO cuenta = new CuentaVO();
	
	private ViaDeudaVO viaDeuda = new ViaDeudaVO();
	
	private EstadoDeudaVO estadoDeuda = new EstadoDeudaVO();
	
	private RecClaDeuVO recClaDeu = new RecClaDeuVO();
	
	private Date fechaEmisionDesde;
	private Date fechaEmisionHasta;
	
	private Date fechaVtoDesde = new Date();
	private Date fechaVtoHasta = new Date();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<ViaDeudaVO> listViaDeuda = new ArrayList<ViaDeudaVO>();
	private List<EstadoDeudaVO> listEstadoDeuda = new ArrayList<EstadoDeudaVO>();
	private List<RecClaDeuVO> listRecClaDeu = new ArrayList<RecClaDeuVO>(); 
	
	private String fechaEmisionDesdeView = "";
	private String fechaEmisionHastaView = "";
	private String fechaVtoDesdeView = "";
	private String fechaVtoHastaView = "";

	private boolean habilitarCuentaEnabled=true;

	// Constructores
	public EstadoCuentaSearchPage() {       
       super(GdeSecurityConstants.CONSULTAR_ESTADOCUENTA);        
    }

	// Getters y Setters
	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public ViaDeudaVO getViaDeuda() {
		return viaDeuda;
	}

	public void setViaDeuda(ViaDeudaVO viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

	public EstadoDeudaVO getEstadoDeuda() {
		return estadoDeuda;
	}

	public void setEstadoDeuda(EstadoDeudaVO estadoDeuda) {
		this.estadoDeuda = estadoDeuda;
	}

	public RecClaDeuVO getRecClaDeu() {
		return recClaDeu;
	}

	public void setRecClaDeu(RecClaDeuVO recClaDeu) {
		this.recClaDeu = recClaDeu;
	}

	public Date getFechaEmisionDesde() {
		return fechaEmisionDesde;
	}

	public void setFechaEmisionDesde(Date fechaEmisionDesde) {
		this.fechaEmisionDesde = fechaEmisionDesde;
		this.fechaEmisionDesdeView = DateUtil.formatDate(fechaEmisionDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaEmisionHasta() {
		return fechaEmisionHasta;
	}

	public void setFechaEmisionHasta(Date fechaEmisionHasta) {
		this.fechaEmisionHasta = fechaEmisionHasta;
		this.fechaEmisionHastaView = DateUtil.formatDate(fechaEmisionHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaVtoDesde() {
		return fechaVtoDesde;
	}

	public void setFechaVtoDesde(Date fechaVtoDesde) {
		this.fechaVtoDesde = fechaVtoDesde;
		this.fechaVtoDesdeView = DateUtil.formatDate(fechaVtoDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaVtoHasta() {
		return fechaVtoHasta;
	}

	public void setFechaVtoHasta(Date fechaVtoHasta) {
		this.fechaVtoHasta = fechaVtoHasta;
		this.fechaVtoHastaView = DateUtil.formatDate(fechaVtoHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<ViaDeudaVO> getListViaDeuda() {
		return listViaDeuda;
	}

	public void setListViaDeuda(List<ViaDeudaVO> listViaDeuda) {
		this.listViaDeuda = listViaDeuda;
	}

	public List<EstadoDeudaVO> getListEstadoDeuda() {
		return listEstadoDeuda;
	}

	public void setListEstadoDeuda(List<EstadoDeudaVO> listEstadoDeuda) {
		this.listEstadoDeuda = listEstadoDeuda;
	}

	public List<RecClaDeuVO> getListRecClaDeu() {
		return listRecClaDeu;
	}

	public void setListRecClaDeu(List<RecClaDeuVO> listRecClaDeu) {
		this.listRecClaDeu = listRecClaDeu;
	}

	// View getters
	public void setFechaEmisionDesdeView(String fechaEmisionDesdeView) {
		this.fechaEmisionDesdeView = fechaEmisionDesdeView;
	}
	public String getFechaEmisionDesdeView() {
		return fechaEmisionDesdeView;
	}

	public void setFechaEmisionHastaView(String fechaEmisionHastaView) {
		this.fechaEmisionHastaView = fechaEmisionHastaView;
	}
	public String getFechaEmisionHastaView() {
		return fechaEmisionHastaView;
	}

	public void setFechaVtoDesdeView(String fechaVtoDesdeView) {
		this.fechaVtoDesdeView = fechaVtoDesdeView;
	}
	public String getFechaVtoDesdeView() {
		return fechaVtoDesdeView;
	}

	public void setFechaVtoHastaView(String fechaVtoHastaView) {
		this.fechaVtoHastaView = fechaVtoHastaView;
	}
	public String getFechaVtoHastaView() {
		return fechaVtoHastaView;
	}


	// flags getters y setters
	public boolean getHabilitarCuentaEnabled() {
		return habilitarCuentaEnabled;
	}

	public void setHabilitarCuentaEnabled(boolean habilitarCuentaEnabled) {
		this.habilitarCuentaEnabled = habilitarCuentaEnabled;
	}

	
}
