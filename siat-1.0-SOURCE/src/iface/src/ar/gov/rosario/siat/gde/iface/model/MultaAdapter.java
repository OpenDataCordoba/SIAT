//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Adapter del Multa
 * 
 * @author tecso
 */
public class MultaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "multaAdapterVO";
	
    private MultaVO multa = new MultaVO();
    private List<TipoMultaVO> listTipoMulta=new ArrayList<TipoMultaVO>();
    private TipoMultaVO tipoMulta = new TipoMultaVO();
    private CasoVO caso = new CasoVO();
    private List<DescuentoMultaVO> listDescuentoMulta = new ArrayList<DescuentoMultaVO>();
    private boolean detalleValido = false;
    private Double totalMulta= 0D;
    private boolean enCierreComercio = false;
    private Long idCuenta;
    private List<MultaVO> listMulta=new ArrayList<MultaVO>();
    private MultaHistoricoVO multaHistorico= new MultaHistoricoVO();
    private boolean historico = false;
	private String totalMultaView;
	private Integer esTipoRevision = 0;
	
	
	// Constructores
    public MultaAdapter(){
    	super(GdeSecurityConstants.ABM_MULTA);
    }
    
    //  Getters y Setters
	public MultaVO getMulta() {
		return multa;
	}

	public void setMulta(MultaVO multaVO) {
		this.multa = multaVO;
	}

	public String getName(){
		return NAME;
	}

    public List<TipoMultaVO> getListTipoMulta() {
		return listTipoMulta;
	}

	public void setListTipoMulta(List<TipoMultaVO> listTipoMulta) {
		this.listTipoMulta = listTipoMulta;
	}
		
	public List<DescuentoMultaVO> getListDescuentoMulta() {
		return listDescuentoMulta;
	}

	public void setListDescuentoMulta(List<DescuentoMultaVO> listDescuentoMulta) {
		this.listDescuentoMulta = listDescuentoMulta;
	}

	public Double getTotalMulta() {
		return totalMulta;
	}

	public void setTotalMulta(Double totalMulta) {
		this.totalMulta = totalMulta;
		this.totalMultaView = StringUtil.formatDouble(totalMulta);
	}
	
 	public TipoMultaVO getTipoMulta() {
		return tipoMulta;
	}

	public void setTipoMulta(TipoMultaVO tipoMulta) {
		this.tipoMulta = tipoMulta;
	}

	public CasoVO getCaso() {
		return caso;
	}

	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	
	public boolean isDetalleValido() {
		return detalleValido;
	}

	public void setDetalleValido(boolean detalleValido) {
		this.detalleValido = detalleValido;
	}

	// View getters
	public String getTotalMultaView() {
		return NumberUtil.round(totalMulta, SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	public void setTotalMultaView(String totalMultaView) {
		this.totalMultaView = totalMultaView;
	}

	public boolean isEnCierreComercio() {
		return enCierreComercio;
	}

	public void setEnCierreComercio(boolean enCierreComercio) {
		this.enCierreComercio = enCierreComercio;
	}
	

	public Long getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(Long idCuenta) {
		this.idCuenta = idCuenta;
	}

	public List<MultaVO> getListMulta() {
		return listMulta;
	}

	public void setListMulta(List<MultaVO> listMulta) {
		this.listMulta = listMulta;
	}

	public MultaHistoricoVO getMultaHistorico() {
		return multaHistorico;
	}

	public void setMultaHistorico(MultaHistoricoVO multaHistorico) {
		this.multaHistorico = multaHistorico;
	}

	public boolean isHistorico() {
		return historico;
	}

	public void setHistorico(boolean historico) {
		this.historico = historico;
	}

	public Integer getEsTipoRevision() {
		return esTipoRevision;
	}

	public void setEsTipoRevision(Integer esTipoRevision) {
		this.esTipoRevision = esTipoRevision;
	}


}