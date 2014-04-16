//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import ar.gov.rosario.siat.rec.iface.model.ObraVO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter de la Impresion de CdM
 * 
 * @author tecso
 */
public class ImpresionCdMAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "impresionAdapterVO";
	
	public static final String ANIO = "anio";
	public static final String MES = "mes";
	public static final String FORMATOSALIDA = "formatoSalida";
	public static final String IMPRESIONTOTAL = "impresionTotal";

	private Integer anio;
	private Integer mes; 
	private ObraVO obra = new ObraVO();
	private FormatoSalida formatoSalida = FormatoSalida.OpcionSelecionar;
	
	private SiNo impresionTotal = SiNo.SI;
	
	private String anioView;
	private String mesView;

	private List<ObraVO> listObra = new ArrayList<ObraVO>();
    private List<FormatoSalida> listFormatoSalida = new ArrayList<FormatoSalida>();
	
    // Constructores
    public ImpresionCdMAdapter(){
    	super(EmiSecurityConstants.ABM_EMISIONMAS);
    }

    //  Getters y Setters
    public ObraVO getObra() {
		return obra;
	}

    public void setObra(ObraVO obra) {
		this.obra = obra;
	}
    
    public List<ObraVO> getListObra() {
		return listObra;
	}

	public void setListObra(List<ObraVO> listObra) {
		this.listObra = listObra;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
		this.mesView = StringUtil.formatInteger(mes);	
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
		this.anioView = StringUtil.formatInteger(anio);
	}
	
	public FormatoSalida getFormatoSalida() {
		return formatoSalida;
	}

	public void setFormatoSalida(FormatoSalida formatoSalida) {
		this.formatoSalida = formatoSalida;
	}

	public List<FormatoSalida> getListFormatoSalida() {
		return listFormatoSalida;
	}

	public void setListFormatoSalida(List<FormatoSalida> listFormatoSalida) {
		this.listFormatoSalida = listFormatoSalida;
	}

	// View Getters
	public String getMesView() {
		return mesView;
	}

	public void setMesView(String mesView) {
		this.mesView = mesView;
	}

	public String getAnioView() {
		return anioView;
	}

	public void setAnioView(String anioView) {
		this.anioView = anioView;
	}

	public SiNo getImpresionTotal() {
		return impresionTotal;
	}

	public void setImpresionTotal(SiNo impresionTotal) {
		this.impresionTotal = impresionTotal;

	}
}