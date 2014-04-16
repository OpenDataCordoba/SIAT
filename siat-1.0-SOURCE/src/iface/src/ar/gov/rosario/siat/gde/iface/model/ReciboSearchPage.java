//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.TipoRecibo;

/**
 * SearchPage del Recibo
 * 
 * @author Tecso
 *
 */
public class ReciboSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "reciboSearchPageVO";
	
	private List<TipoRecibo> listTipoRecibo = new ArrayList<TipoRecibo>();
	private TipoRecibo	tipoRecibo = TipoRecibo.SELECCIONAR; 
	private List<RecursoVO>  listRecurso = new ArrayList<RecursoVO>();
	private ReciboVO recibo = new ReciboVO();
	
	// Constructores
	public ReciboSearchPage() {       
       super(GdeSecurityConstants.CONSULTAR_RECIBO);        
    }
	
	// Getters y Setters
	public ReciboVO getRecibo() {
		return recibo;
	}
	public void setRecibo(ReciboVO recibo) {
		this.recibo = recibo;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<TipoRecibo> getListTipoRecibo() {
		return listTipoRecibo;
	}
	public void setListTipoRecibo(List<TipoRecibo> listTipoRecibo) {
		this.listTipoRecibo = listTipoRecibo;
	}

	public TipoRecibo getTipoRecibo() {
		return tipoRecibo;
	}
	public void setTipoRecibo(TipoRecibo tipoRecibo) {
		this.tipoRecibo = tipoRecibo;
	}
	
	
	// View getters
}
