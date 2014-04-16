//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;

public class ClaComReport extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "claComReportVO";
	
	private List<PlanillaVO> listReporteGenerado = new ArrayList<PlanillaVO>();
	private String tituloReporte = "";
	
	private List<ClasificadorVO> listClasificador = new ArrayList<ClasificadorVO>();
	
	private List<IntegerVO> listNivel = new ArrayList<IntegerVO>();
	
	private Date priFechaDesde;
	private Date priFechaHasta;
	private Date segFechaDesde;
	private Date segFechaHasta;
	private ClasificadorVO clasificador = new ClasificadorVO();
	private Integer nivel = -1;

	private String priFechaDesdeView = "";
	private String priFechaHastaView = "";
	private String segFechaDesdeView = "";
	private String segFechaHastaView = "";
	private String nivelView = "";

	private String desRunningRun = "";
	private String estRunningRun = "";
	private String desErrorRun = "";
	private String estErrorRun = "";

	//Flags
	private boolean procesando = false;
	private boolean existeReporteGenerado = false;
	private boolean error = false;
	
	// Constructores
	public ClaComReport() {       
       super(BalSecurityConstants.CONSULTAR_CLACOM);        
    }

	// Getters y Setters
	public String getDesErrorRun() {
		return desErrorRun;
	}
	public void setDesErrorRun(String desErrorRun) {
		this.desErrorRun = desErrorRun;
	}
	public String getDesRunningRun() {
		return desRunningRun;
	}
	public void setDesRunningRun(String desRunningRun) {
		this.desRunningRun = desRunningRun;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getEstErrorRun() {
		return estErrorRun;
	}
	public void setEstErrorRun(String estErrorRun) {
		this.estErrorRun = estErrorRun;
	}
	public String getEstRunningRun() {
		return estRunningRun;
	}
	public void setEstRunningRun(String estRunningRun) {
		this.estRunningRun = estRunningRun;
	}
	public boolean isExisteReporteGenerado() {
		return existeReporteGenerado;
	}
	public void setExisteReporteGenerado(boolean existeReporteGenerado) {
		this.existeReporteGenerado = existeReporteGenerado;
	}
	public boolean isProcesando() {
		return procesando;
	}
	public void setProcesando(boolean procesando) {
		this.procesando = procesando;
	}
	public Date getPriFechaDesde() {
		return priFechaDesde;
	}
	public void setPriFechaDesde(Date priFechaDesde) {
		this.priFechaDesde = priFechaDesde;
		this.priFechaDesdeView = DateUtil.formatDate(priFechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getPriFechaDesdeView() {
		return priFechaDesdeView;
	}
	public void setPriFechaDesdeView(String priFechaDesdeView) {
		this.priFechaDesdeView = priFechaDesdeView;
	}
	public Date getPriFechaHasta() {
		return priFechaHasta;
	}
	public void setPriFechaHasta(Date priFechaHasta) {
		this.priFechaHasta = priFechaHasta;
		this.priFechaHastaView = DateUtil.formatDate(priFechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getPriFechaHastaView() {
		return priFechaHastaView;
	}
	public void setPriFechaHastaView(String priFechaHastaView) {
		this.priFechaHastaView = priFechaHastaView;
	}
	public Date getSegFechaDesde() {
		return segFechaDesde;
	}
	public void setSegFechaDesde(Date segFechaDesde) {
		this.segFechaDesde = segFechaDesde;
		this.segFechaDesdeView = DateUtil.formatDate(segFechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getSegFechaDesdeView() {
		return segFechaDesdeView;
	}
	public void setSegFechaDesdeView(String segFechaDesdeView) {
		this.segFechaDesdeView = segFechaDesdeView;
	}
	public Date getSegFechaHasta() {
		return segFechaHasta;
	}
	public void setSegFechaHasta(Date segFechaHasta) {
		this.segFechaHasta = segFechaHasta;
		this.segFechaHastaView = DateUtil.formatDate(segFechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getSegFechaHastaView() {
		return segFechaHastaView;
	}
	public void setSegFechaHastaView(String segFechaHastaView) {
		this.segFechaHastaView = segFechaHastaView;
	}
	public List<PlanillaVO> getListReporteGenerado() {
		return listReporteGenerado;
	}
	public void setListReporteGenerado(List<PlanillaVO> listReporteGenerado) {
		this.listReporteGenerado = listReporteGenerado;
	}
	public String getTituloReporte() {
		return tituloReporte;
	}
	public void setTituloReporte(String tituloReporte) {
		this.tituloReporte = tituloReporte;
	}
	public ClasificadorVO getClasificador() {
		return clasificador;
	}
	public void setClasificador(ClasificadorVO clasificador) {
		this.clasificador = clasificador;
	}
	public Integer getNivel() {
		return nivel;
	}
	public void setNivel(Integer nivel) {
		this.nivel = nivel;
		this.nivelView = StringUtil.formatInteger(nivel);
	}
	public String getNivelView() {
		return nivelView;
	}
	public void setNivelView(String nivelView) {
		this.nivelView = nivelView;
	}
	public List<ClasificadorVO> getListClasificador() {
		return listClasificador;
	}
	public void setListClasificador(List<ClasificadorVO> listClasificador) {
		this.listClasificador = listClasificador;
	}
	public List<IntegerVO> getListNivel() {
		return listNivel;
	}
	public void setListNivel(List<IntegerVO> listNivel) {
		this.listNivel = listNivel;
	}


}
