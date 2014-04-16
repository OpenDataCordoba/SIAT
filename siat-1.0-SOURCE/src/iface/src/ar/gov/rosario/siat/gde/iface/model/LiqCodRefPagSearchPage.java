//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.TipoBoleta;

/**
 * SearchPage del LiqCodRefPag
 * 
 * @author Tecso
 *
 */
public class LiqCodRefPagSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "liqCodRefPagSearchPageVO";
	
	private Long codRefPag = 0L;
    
    private Long tipoBoleta = 0L;
    
    private Double importe; 
    
    private List<TipoBoleta> listTipoBoleta = new ArrayList<TipoBoleta>();
    
    private boolean showResults=false;
    
    private boolean tieneResultados=false;

    private String codRefPagView = "";
    private String importeView="";
    private String tipoBoletaView="";
    
	// Constructores
	public LiqCodRefPagSearchPage() {       
       super(GdeSecurityConstants.LIQ_CODREFPAG);        
    }

	// Getters y Setters
	public Long getCodRefPag() {
		return codRefPag;
	}
	public void setCodRefPag(Long codRefPag) {
		this.codRefPag = codRefPag;
		this.codRefPagView = StringUtil.formatLong(codRefPag);	
	}

	public Long getTipoBoleta() {
		return tipoBoleta;
	}
	public void setTipoBoleta(Long tipoBoleta) {
		this.tipoBoleta = tipoBoleta;
		this.tipoBoletaView = StringUtil.formatLong(tipoBoleta);
	}

	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.formatDouble(importe);
	}

	public List<TipoBoleta> getListTipoBoleta() {
		return listTipoBoleta;
	}
	public void setListTipoBoleta(List<TipoBoleta> listTipoBoleta) {
		this.listTipoBoleta = listTipoBoleta;
	}

	public boolean isShowResults() {
		return showResults;
	}
	public void setShowResults(boolean showResults) {
		this.showResults = showResults;
	}

	public boolean isTieneResultados() {
		return tieneResultados;
	}
	public void setTieneResultados(boolean tieneResultados) {
		this.tieneResultados = tieneResultados;
	}

	
	// View getters
	public String getCodRefPagView() {
		return codRefPagView;
	}
	public void setCodRefPagView(String codRefPagView) {
		this.codRefPagView = codRefPagView;
	}
	
	public String getImporteView() {
		return importeView;
	}
	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}

	public String getTipoBoletaView() {
		return tipoBoletaView;
	}
	public void setTipoBoletaView(String tipoBoletaView) {
		this.tipoBoletaView = tipoBoletaView;
	}
	
}
