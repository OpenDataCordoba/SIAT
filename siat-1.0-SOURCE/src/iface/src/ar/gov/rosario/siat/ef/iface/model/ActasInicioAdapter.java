//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Adapter de la asignacion de  Investigador
 * 
 * @author tecso
 */
public class ActasInicioAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "actasInicioAdapterVO";
	
    private InvestigadorVO investigador = new InvestigadorVO();
    
    private Date fechaVisita;
    
    private String fechaVisitaView="";
    
    private String urlMapa ="";
    
    private String[] idsSelectedForAsignar = {}; // son los seleccionados en el adapter para asignarles investigador
    
    private String[] idsSelectedFromSearch = {};// son los seleccionados en el searchPage
    
    private List<OpeInvConVO> listOpeInvCon = new ArrayList<OpeInvConVO>();
    
    private List<InvestigadorVO> listInvestigador = new ArrayList<InvestigadorVO>();
    
    private boolean visualizarMapa = true;
    
    // Constructores
    public ActasInicioAdapter(){
    	super(EfSecurityConstants.ABM_ASIG_ACTAS);
    }
    
    //  Getters y Setters
	public InvestigadorVO getInvestigador() {
		return investigador;
	}

	public void setInvestigador(InvestigadorVO investigadorVO) {
		this.investigador = investigadorVO;
	}

	public Date getFechaVisita() {
		return fechaVisita;
	}

	public void setFechaVisita(Date fechaVisita) {
		this.fechaVisita = fechaVisita;
		this.fechaVisitaView = DateUtil.formatDate(fechaVisita, DateUtil.ddSMMSYY_MASK);
	}

	public String getFechaVisitaView() {
		return fechaVisitaView;
	}

	public void setFechaVisitaView(String fechaVisitaView) {
		this.fechaVisitaView = fechaVisitaView;
	}

	public List<OpeInvConVO> getListOpeInvCon() {
		return listOpeInvCon;
	}

	public void setListOpeInvCon(List<OpeInvConVO> listOpeInvCon) {
		this.listOpeInvCon = listOpeInvCon;
	}

	public List<InvestigadorVO> getListInvestigador() {
		return listInvestigador;
	}

	public void setListInvestigador(List<InvestigadorVO> listInvestigador) {
		this.listInvestigador = listInvestigador;
	}

	public String[] getIdsSelectedForAsignar() {
		return idsSelectedForAsignar;
	}

	public void setIdsSelectedForAsignar(String[] idsSelected) {
		this.idsSelectedForAsignar = idsSelected;
	}

	public String[] getIdsSelectedFromSearch() {
		return idsSelectedFromSearch;
	}

	public void setIdsSelectedFromSearch(String[] idsSelectedFromSearch) {
		this.idsSelectedFromSearch = idsSelectedFromSearch;
	}

	public Integer getCantOpeInvCon(){
		return listOpeInvCon!=null?listOpeInvCon.size():0;
	}

	
	public String getUrlMapa() {
		return urlMapa;
	}

	public void setUrlMapa(String urlMapa) {
		this.urlMapa = urlMapa;
	}

	public boolean getVisualizarMapa() {
		return visualizarMapa;
	}

	public void setVisualizarMapa(boolean visualizarMapa) {
		this.visualizarMapa = visualizarMapa;
	}
	
	
	
	// View getters
}
