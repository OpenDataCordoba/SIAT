//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.model.PartidaVO;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.gde.iface.model.SerBanDesGenVO;
import ar.gov.rosario.siat.pro.iface.model.ProcesoVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Servicio Banco
 * @author tecso
 *
 */
public class ServicioBancoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "servicioBancoVO";
	
	private String codServicioBanco;
	private String desServicioBanco;
	private ProcesoVO procesoAse = new ProcesoVO();
	private PartidaVO partidaIndet = new PartidaVO();
	private PartidaVO parCuePue = new PartidaVO();
	private SiNo esAutoliquidable = SiNo.OpcionSelecionar;
	private Long tipoAsentamiento;

	
	private String tipoAsentamientoView = "";
	
	private List<SerBanRecVO> listSerBanRec = new ArrayList<SerBanRecVO>();
	private List<SerBanDesGenVO> listSerBanDesGen = new ArrayList<SerBanDesGenVO>();
	
	
	// Constructores
	public ServicioBancoVO(){
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ServicioBancoVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesServicioBanco(desc);
	}
	
	// Getters y Setters
	public String getCodServicioBanco(){
		return codServicioBanco;
	}
	public void setCodServicioBanco(String codServicioBanco){
		this.codServicioBanco = codServicioBanco;
	}
	public String getDesServicioBanco(){
		return desServicioBanco;
	}
	public void setDesServicioBanco(String desServicioBanco){
		this.desServicioBanco = desServicioBanco;
	}
	public List<SerBanRecVO> getListSerBanRec(){
		return listSerBanRec;
	}
	public void setListSerBanRec(List<SerBanRecVO> listSerBanRec){
		this.listSerBanRec = listSerBanRec;
	}
	public List<SerBanDesGenVO> getListSerBanDesGen(){
		return listSerBanDesGen;
	}
	public void setListSerBanDesGen(List<SerBanDesGenVO> listSerBanDesGen){
		this.listSerBanDesGen = listSerBanDesGen;
	}
	public ProcesoVO getProcesoAse() {
		return procesoAse;
	}
	public void setProcesoAse(ProcesoVO procesoAse) {
		this.procesoAse = procesoAse;
	}
	public PartidaVO getPartidaIndet() {
		return partidaIndet;
	}
	public void setPartidaIndet(PartidaVO partidaIndet) {
		this.partidaIndet = partidaIndet;
	}
	public SiNo getEsAutoliquidable(){
		return esAutoliquidable;
	}
	public void setEsAutoliquidable(SiNo esAutoliquidable){
		this.esAutoliquidable = esAutoliquidable;
	}
	public PartidaVO getParCuePue() {
		return parCuePue;
	}
	public void setParCuePue(PartidaVO parCuePue) {
		this.parCuePue = parCuePue;
	}
	public Long getTipoAsentamiento() {
		return tipoAsentamiento;
	}
	public void setTipoAsentamiento(Long tipoAsentamiento) {
		this.tipoAsentamiento = tipoAsentamiento;
	}
	public String getTipoAsentamientoView() {
		return tipoAsentamientoView;
	}
	public void setTipoAsentamientoView(String tipoAsentamientoView) {
		this.tipoAsentamientoView = tipoAsentamientoView;
	}
	
}
