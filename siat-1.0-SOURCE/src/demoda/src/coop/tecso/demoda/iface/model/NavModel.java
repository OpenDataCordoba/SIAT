//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NavModel extends Common{

	public static final int NAVMODEL_MESSAGE_TYPE_CONFIRMATION = 0;
	public static final int NAVMODEL_MESSAGE_TYPE_ERROR = 1;
	public static final int NAVMODEL_MESSAGE_TYPE_EXCEPTION = 2;
	
	private String act = "";
	
	// volver
    private String prevAction = "";
    private String prevActionParameter = "";
    
    // confirmar
    private String confAction = "";
    private String confActionParameter = "";
    
    // seleccionar
    private String selectAction = "";
    private String selectActionParameter = "";
    private String selectAct = "";
    private boolean agregarEnSeleccion = false;    
    
    private String selectedId = "";
    private int    messageType = 0;
    private String messageStr = "";
    private String excepcionStr = "";
    
    // mapa con parametros para pasar entre action y action
    private HashMap parametersMap = new HashMap<String, Object>();
    
    // contiene una lista de Value Object a excluir en las busquedas
    private List listVOExcluidos = new ArrayList<BussImageModel>();   
    
    @Deprecated
    private NavModel prevNavModel = null;

	public NavModel(){
		
	}
	
	public String getAct() {
		return act;
	}
	public void setAct(String act) {
		this.act = act;
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
	public String getSelectedId() {
		return selectedId;
	}
	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}
	public String getMessageStr() {
		return messageStr;
	}
	public void setMessageStr(String messageStr) {
		this.messageStr = messageStr;
	}
	public int getMessageType() {
		return messageType;
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
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
	public String getConfAction() {
		return confAction;
	}
	public void setConfAction(String confAction) {
		this.confAction = confAction;
	}
	public String getConfActionParameter() {
		return confActionParameter;
	}
	public void setConfActionParameter(String confActionParameter) {
		this.confActionParameter = confActionParameter;
	}
	public void setSelectActionParameter(String selectActionParameter) {
		this.selectActionParameter = selectActionParameter;
	}
	public String getSelectAct() {
		return selectAct;
	}
	public void setSelectAct(String selectAct) {
		this.selectAct = selectAct;
	}
	
	public boolean getAgregarEnSeleccion() {
		return agregarEnSeleccion;
	}

	public void setAgregarEnSeleccion(boolean agregarEnSeleccion) {
		this.agregarEnSeleccion = agregarEnSeleccion;
	}

	public List getListVOExcluidos() {
		return listVOExcluidos;
	}

	public void setListVOExcluidos(List listVOExcluidos) {
		this.listVOExcluidos = listVOExcluidos;
	}
	
	public void cleanListVOExcluidos() {
		this.listVOExcluidos = new ArrayList<BussImageModel>();
	}

	public void setValuesFromCommonNavegableView (CommonNavegableView cnv) {
		// Volver
		this.setPrevAction(cnv.getPrevAction());
		this.setPrevActionParameter(cnv.getPrevActionParameter());
		// Seleccionar
		this.setSelectAction(cnv.getSelectAction());
		this.setSelectActionParameter(cnv.getSelectActionParameter());
		this.setSelectAct(cnv.getSelectAct());
		this.setAgregarEnSeleccion(cnv.getAgregarEnSeleccion());
		
		// Selected id y act
		this.setSelectedId(cnv.getSelectedId());
		this.setAct(cnv.getAct());
		
		// lista de excluidos
		this.getListVOExcluidos().clear();
		this.getListVOExcluidos().addAll(cnv.getListVOExcluidos());
	}
	
	@Deprecated
	public NavModel copyTo(NavModel navModelDest) {
		navModelDest.setPrevAction(this.getPrevAction());
		navModelDest.setPrevActionParameter(this.getPrevActionParameter());
		navModelDest.setSelectedId(this.getSelectedId());
		navModelDest.setAct(this.getAct());
		
		if (this.getPrevNavModel()!=null) {
			navModelDest.setPrevNavModel(new NavModel());
			navModelDest.getPrevNavModel().setPrevAction(this.getPrevNavModel().getPrevAction());
			navModelDest.getPrevNavModel().setPrevActionParameter(this.getPrevNavModel().getPrevActionParameter());
			navModelDest.getPrevNavModel().setSelectedId(this.getPrevNavModel().getSelectedId());
			navModelDest.getPrevNavModel().setAct(this.getPrevNavModel().getAct());
		}
		return navModelDest;
	}
	
	public NavModel getPrevNavModel() {
		return prevNavModel;
	}
	public void setPrevNavModel(NavModel prevNavModel) {
		this.prevNavModel = prevNavModel;
	}

	protected HashMap getParametersMap() {
		return parametersMap;
	}
	protected void setParametersMap(HashMap parametersMap) {
		this.parametersMap = parametersMap;
	}
	public void clearParametersMap() {
		this.parametersMap.clear();
	}
	public Object getParameter(String key){
		return this.parametersMap.get(key);
	}
	
	public void putParameter(String key, Object value){
		this.parametersMap.put(key, value);
	}

	public String getExcepcionStr() {
		return excepcionStr;
	}

	public void setExcepcionStr(String excepcionStr) {
		this.excepcionStr = excepcionStr;
	}

	
}
