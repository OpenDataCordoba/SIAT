//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pro.iface.model.ProcesoVO;
import ar.gov.rosario.siat.rec.iface.model.NovedadRSVO;
import ar.gov.rosario.siat.rec.iface.model.TipoTramiteRSVO;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Adapter para adhesion regimen simplificado
 * 
 * @author tecso
 */
public class  NovedadRSAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "novedadRSAdapterVO";
	    
    private NovedadRSVO novedadRS = new NovedadRSVO();
    
    private RecursoVO recurso = new RecursoVO();
    
    private List<TipoTramiteRSVO> listTipoTramiteRS = new ArrayList<TipoTramiteRSVO>();
    
    private ProcesoVO proceso = new ProcesoVO();
    private Boolean paramPeriodic = false;
   
	private Date fechaNovedadDesde;
	private Date fechaNovedadHasta;
	private String fechaNovedadDesdeView = "";
	private String fechaNovedadHastaView = "";
	
    // Constructores
    public NovedadRSAdapter(){
    	super(RecSecurityConstants.ABM_NOVEDADRS);
    }


    //  Getters y Setters

	public List<TipoTramiteRSVO> getListTipoTramiteRS() {
		return listTipoTramiteRS;
	}

	public void setListTipoTramiteRS(List<TipoTramiteRSVO> listTipoTramiteRS) {
		this.listTipoTramiteRS = listTipoTramiteRS;
	}

	public NovedadRSVO getNovedadRS() {
		return novedadRS;
	}

	public void setNovedadRS(NovedadRSVO novedadRS) {
		this.novedadRS = novedadRS;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public Boolean getParamPeriodic() {
		return paramPeriodic;
	}

	public void setParamPeriodic(Boolean paramPeriodic) {
		this.paramPeriodic = paramPeriodic;
	}

	public ProcesoVO getProceso() {
		return proceso;
	}

	public void setProceso(ProcesoVO proceso) {
		this.proceso = proceso;
	}

	public Date getFechaNovedadDesde() {
		return fechaNovedadDesde;
	}

	public void setFechaNovedadDesde(Date fechaNovedadDesde) {
		this.fechaNovedadDesde = fechaNovedadDesde;
		this.fechaNovedadDesdeView = DateUtil.formatDate(fechaNovedadDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaNovedadDesdeView() {
		return fechaNovedadDesdeView;
	}

	public void setFechaNovedadDesdeView(String fechaNovedadDesdeView) {
		this.fechaNovedadDesdeView = fechaNovedadDesdeView;
	}

	public Date getFechaNovedadHasta() {
		return fechaNovedadHasta;
	}

	public void setFechaNovedadHasta(Date fechaNovedadHasta) {
		this.fechaNovedadHasta = fechaNovedadHasta;
		this.fechaNovedadHastaView = DateUtil.formatDate(fechaNovedadHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaNovedadHastaView() {
		return fechaNovedadHastaView;
	}

	public void setFechaNovedadHastaView(String fechaNovedadHastaView) {
		this.fechaNovedadHastaView = fechaNovedadHastaView;
	}

	// View getters
}
