//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

public class CommonNavegableView extends CommonView {
	
	// accion y metodo para volver
    private String prevAction = "";
    private String prevActionParameter = "";
    
    // accion y metodo para seleccionar
    private String selectAction = "";
    private String selectActionParameter = "";
    private String selectAct = "";

    // aca se guarda el id seleccionado, si hay uno.
    private String selectedId="";
    private String act="";
    
    // contiene una lista de Value Object a excluir en las busquedas
    private List listVOExcluidos = new ArrayList<BussImageModel>();   
    
    // Reporteador
    private ReportVO reportVO = new ReportVO();
    
    public CommonNavegableView(){
    	super();
    }
    		
	public CommonNavegableView(String sweActionName){
		super(sweActionName);
	}
    
	public String getPrevAction() {
		return prevAction;
	}
	public void setPrevAction(String prevAction) {
		this.prevAction = prevAction;
	}
	public String getPrevActionParameter() {
		return prevActionParameter;
	}
	public void setPrevActionParameter(String prevActionParameter) {
		this.prevActionParameter = prevActionParameter;
	}
	public String getSelectAction() {
		return selectAction;
	}
	public void setSelectAction(String selectAction) {
		this.selectAction = selectAction;
	}
	public String getSelectActionParameter() {
		return selectActionParameter;
	}
	public void setSelectActionParameter(String selectActionParameter) {
		this.selectActionParameter = selectActionParameter;
	}
	public String getSelectedId() {
		return selectedId;
	}
	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}
	public String getAct() {
		return act;
	}
	public void setAct(String act) {
		this.act = act;
	}
	public String getSelectAct() {
		return selectAct;
	}
	public void setSelectAct(String selectAct) {
		this.selectAct = selectAct;
	}

	public List getListVOExcluidos() {
		return listVOExcluidos;
	}

	public void setListVOExcluidos(List listVOExcluidos) {
		this.listVOExcluidos = listVOExcluidos;
	}

	public String getAccionVolver () {
		return StringUtil.getActionPath(this.getPrevAction(), this.getPrevActionParameter());
	}
	public String getAccionSeleccionar () {
		
		String actionSeleccionar = StringUtil.getActionPath(this.getSelectAction(), this.getSelectActionParameter());

		if (StringUtil.isNullOrEmpty(this.selectAction) && 
			StringUtil.isNullOrEmpty(this.selectActionParameter)) {
			
			actionSeleccionar = this.getAccionVolver();
			
		}
		
		return  actionSeleccionar;
	}
	
	public void setValuesFromNavModel (NavModel navModel) {
		// Volver
		this.setPrevAction(navModel.getPrevAction());
		this.setPrevActionParameter(navModel.getPrevActionParameter());
		// Seleccionar
		this.setSelectAction(navModel.getSelectAction());
		this.setSelectActionParameter(navModel.getSelectActionParameter());
		this.setSelectAct(navModel.getSelectAct());
		this.setAgregarEnSeleccion(navModel.getAgregarEnSeleccion());
		
		// selected id y act
		this.setSelectedId(navModel.getSelectedId());
		this.setAct(navModel.getAct());

		// lista de excluidos para los searchPaga
		this.getListVOExcluidos().clear();
		this.getListVOExcluidos().addAll(navModel.getListVOExcluidos());
	}
	
	/**
	 * Pasa los valores de navegacion del model actual al target  
	 * 
	 * 
	 * @param model
	 */
	public void passNavValuesFromModel(CommonNavegableView targetModel) {
		// Volver
		this.setPrevAction(targetModel.getPrevAction());
		this.setPrevActionParameter(targetModel.getPrevActionParameter());
		// Seleccionar
		this.setSelectAction(targetModel.getSelectAction());
		this.setSelectActionParameter(targetModel.getSelectActionParameter());
		this.setSelectAct(targetModel.getSelectAct());
		this.setAgregarEnSeleccion(targetModel.getAgregarEnSeleccion());
		
		// selected id y act
		this.setSelectedId(targetModel.getSelectedId());
		this.setAct(targetModel.getAct());

		// lista de excluidos para los searchPaga
		this.getListVOExcluidos().clear();
		this.getListVOExcluidos().addAll(targetModel.getListVOExcluidos());
	}
	
	public ReportVO getReport() {
		return reportVO;
	}
	public void setReport(ReportVO reportVO) {
		this.reportVO = reportVO;
	}

	public void prepareReport(Long format) {
		// redefinido en cada search page y adapter 
	}

}
