//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.ef.iface.model.TipoOrdenVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * SearchPage del Cobranza
 * 
 * @author Tecso
 *
 */
public class CobranzaSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "cobranzaSearchPageVO";
	
	private ContribuyenteVO contribuyente = new ContribuyenteVO();
	
	private CobranzaVO cobranza = new CobranzaVO();
	
	private List<PerCobVO> listPerCob = new ArrayList<PerCobVO>();
	
	private List<TipoOrdenVO> listTipoOrden = new ArrayList<TipoOrdenVO>();
	
	private CasoVO caso = new CasoVO();
	
	private List<EstadoCobranzaVO>listEstadoCobranza = new ArrayList<EstadoCobranzaVO>();
	
	private Date proConDes;
	
	private Date proConHas;
	
	// Constructores
	public CobranzaSearchPage() {       
       super(GdeSecurityConstants.ABM_COBRANZA);        
    }
	
	// Getters y Setters

	public ContribuyenteVO getContribuyente() {
		return contribuyente;
	}

	public void setContribuyente(ContribuyenteVO contribuyente) {
		this.contribuyente = contribuyente;
	}

	public CobranzaVO getCobranza() {
		return cobranza;
	}

	public void setCobranza(CobranzaVO cobranza) {
		this.cobranza = cobranza;
	}

	public List<PerCobVO> getListPerCob() {
		return listPerCob;
	}

	public void setListPerCob(List<PerCobVO> listPerCob) {
		this.listPerCob = listPerCob;
	}

	public List<TipoOrdenVO> getListTipoOrden() {
		return listTipoOrden;
	}

	public void setListTipoOrden(List<TipoOrdenVO> listTipoOrden) {
		this.listTipoOrden = listTipoOrden;
	}

	public CasoVO getCaso() {
		return caso;
	}

	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}

	public List<EstadoCobranzaVO> getListEstadoCobranza() {
		return listEstadoCobranza;
	}

	public void setListEstadoCobranza(List<EstadoCobranzaVO> listEstadoCobranza) {
		this.listEstadoCobranza = listEstadoCobranza;
	}

	public Date getProConDes() {
		return proConDes;
	}

	public void setProConDes(Date proConDes) {
		this.proConDes = proConDes;
	}

	public Date getProConHas() {
		return proConHas;
	}

	public void setProConHas(Date proConHas) {
		this.proConHas = proConHas;
	}


	//View getters
	
	public String getProConDesView(){
		return (this.proConDes!=null)?DateUtil.formatDate(proConDes, DateUtil.ddSMMSYYYY_MASK):"";
	}
	
	public String getProConHasView(){
		return (this.proConHas!=null)?DateUtil.formatDate(proConHas, DateUtil.ddSMMSYYYY_MASK):"";
	}
	
	
}
