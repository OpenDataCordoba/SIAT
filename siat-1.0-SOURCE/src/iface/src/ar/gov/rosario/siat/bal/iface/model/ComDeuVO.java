//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import coop.tecso.demoda.iface.helper.StringUtil;

public class ComDeuVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private Long idDeuda;
	private CompensacionVO compensacion = new CompensacionVO();
	private TipoComDeuVO tipoComDeu = new TipoComDeuVO();
	private Double importe;
	private LiqDeudaVO deuda = new LiqDeudaVO();
	
	private String idDeudaView = "";
	private String importeView = "";
	
	//Constructores 
	public ComDeuVO(){
		super();
	}
	
	// Getters Y Setters
	public CompensacionVO getCompensacion() {
		return compensacion;
	}
	public void setCompensacion(CompensacionVO compensacion) {
		this.compensacion = compensacion;
	}
	public Long getIdDeuda() {
		return idDeuda;
	}
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
		this.idDeudaView = StringUtil.formatLong(idDeuda);
	}
	public String getIdDeudaView() {
		return idDeudaView;
	}
	public void setIdDeudaView(String idDeudaView) {
		this.idDeudaView = idDeudaView;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.formatDouble(importe);
	}
	public String getImporteView() {
		return importeView;
	}
	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}
	public TipoComDeuVO getTipoComDeu() {
		return tipoComDeu;
	}
	public void setTipoComDeu(TipoComDeuVO tipoComDeu) {
		this.tipoComDeu = tipoComDeu;
	}
	public LiqDeudaVO getDeuda() {
		return deuda;
	}
	public void setDeuda(LiqDeudaVO deuda) {
		this.deuda = deuda;
	}
	
	
}
