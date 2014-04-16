//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del ActaVO
 * 
 * @author tecso
 */
public class ActaAdapter extends SiatAdapterModel{

	private static final long serialVersionUID = 1L;

	public static final String NAME = "actaAdapterVO";

	public static final String ENC_NAME = "encActaAdapterVO";

	private ActaVO acta = new ActaVO();

	private String listadoOrdConCue="";
	private String listadoRecursos="";
  
	private OrdConDocVO ordConDoc = new OrdConDocVO();

	private List<TipoActaVO> listTipoActa = new ArrayList<TipoActaVO>();

	List<OrdConDocVO> listOrdConDoc = new ArrayList<OrdConDocVO>();

	private String[] idsOrdConDocSelected;

	// flags
	private boolean agregarOrdConDocEnabled=true;
	private boolean eliminarOrdConDocEnabled=true;
	private boolean imprimirActaEnabled=true;

	// Constructores
	public ActaAdapter(){
		super(EfSecurityConstants.ABM_ACTA);
		ACCION_MODIFICAR_ENCABEZADO = EfSecurityConstants.ABM_ACTA_ENC;
	}

	//  Getters y Setters
	public ActaVO getActa() {
		return acta;
	}

	public void setActa(ActaVO ActaVO) {
		this.acta = ActaVO;
	}

	public List<TipoActaVO> getListTipoActa() {
		return listTipoActa;
	}

	public void setListTipoActa(List<TipoActaVO> listTipoActa) {
		this.listTipoActa = listTipoActa;
	}

	public String[] getIdsOrdConDocSelected() {
		return idsOrdConDocSelected;
	}

	public void setIdsOrdConDocSelected(String[] idsOrdConDocSelected) {
		this.idsOrdConDocSelected = idsOrdConDocSelected;
	}


	public List<OrdConDocVO> getListOrdConDoc() {
		return listOrdConDoc;
	}

	public void setListOrdConDoc(List<OrdConDocVO> listOrdConDoc) {
		this.listOrdConDoc = listOrdConDoc;
	}

	public OrdConDocVO getOrdConDoc() {
		return ordConDoc;
	}

	public void setOrdConDoc(OrdConDocVO ordConDoc) {
		this.ordConDoc = ordConDoc;
	}

	public String getName(){
		return NAME;
	}

	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Reporte de ActaVO");     
		report.setReportBeanName("ActaVO");
		report.setReportFileName(this.getClass().getName());

		// carga de filtros: ninguno
		// Order by: no 

		ReportVO reportActaVO = new ReportVO();
		reportActaVO.setReportTitle("Datos del ActaVO");
		// carga de datos

		//Cï¿½digo
		reportActaVO.addReportDato("Cï¿½digo", "codActaVO");
		//Descripciï¿½n
		reportActaVO.addReportDato("Descripciï¿½n", "desActaVO");

		report.getListReport().add(reportActaVO);

	}

	// View getters

	//flag getters
	public String getAgregarOrdConDocEnabled(){
		return SiatBussImageModel.hasEnabledFlag(agregarOrdConDocEnabled, EfSecurityConstants.ABM_ORDCONDOC, BaseSecurityConstants.AGREGAR);
	}

	public String getEliminarOrdConDocEnabled(){
		return SiatBussImageModel.hasEnabledFlag(eliminarOrdConDocEnabled, EfSecurityConstants.ABM_ORDCONDOC, BaseSecurityConstants.ELIMINAR);
	}

	public String getImprimirActaEnabled(){
		return SiatBussImageModel.hasEnabledFlag(imprimirActaEnabled, EfSecurityConstants.ABM_ACTA, EfSecurityConstants.IMPRIMIR);
	}

	public String getListadoOrdConCue(){
		return listadoOrdConCue;
	}
	public String setListadoOrdConCue(){
		List<PeriodoOrdenVO> listPeriodoOrden = this.getActa().getOrdenControl().getListPeriodoOrden();
		String recursoAnt ="";
		String recurso ="";
		String periodoAnt ="";
		String anioAnt ="";
		
		for (PeriodoOrdenVO periodoOrden: listPeriodoOrden){

			recurso = periodoOrden.getOrdConCue().getCuenta().getRecurso().getDesRecurso();
			
			if(!recurso.equals(recursoAnt)){
				listadoOrdConCue+=recurso+" por los períodos ";
				anioAnt="";
			}
			if(!periodoOrden.getAnioView().equals(anioAnt)){
				if(!anioAnt.equals("")){
					listadoOrdConCue+=" al "+periodoAnt+"/"+anioAnt+", ";
				}
				listadoOrdConCue+=periodoOrden.getPeriodoView()+"/"+periodoOrden.getAnioView();
			}
			
			recursoAnt = recurso;
			anioAnt=periodoOrden.getAnioView();
			periodoAnt=periodoOrden.getPeriodoView();
		}
		
		if(!periodoAnt.equals("1"))	listadoOrdConCue+=" al "+periodoAnt+"/"+anioAnt+", ";

		return listadoOrdConCue;
	}
	

	public String getListadoRecursos(){
		return listadoRecursos;
	}
	
	public String setListadoRecursos(){
		List<PeriodoOrdenVO> listPeriodoOrden = this.getActa().getOrdenControl().getListPeriodoOrden();
		String recursoAnt ="";
		String recurso ="";
		for (PeriodoOrdenVO periodoOrden: listPeriodoOrden){

			recurso = periodoOrden.getOrdConCue().getCuenta().getRecurso().getDesRecurso();
			
			if(!recurso.equals(recursoAnt)){

				listadoRecursos+=recurso+", ";
			}
			recursoAnt = recurso;

		}
	
		return listadoRecursos;
	}


	
	

	
}
