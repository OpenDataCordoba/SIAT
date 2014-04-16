//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.view.util;

import org.apache.log4j.Logger;

import coop.tecso.demoda.iface.model.Common;


public class SweUtil {
    
    static Logger log = Logger.getLogger(SweUtil.class);

    public static String getActionNameForDisplay(String actionName){
		String actionNameForDisplay = "";

		/*if (actionName.equals(SweConstants.ACTION_BUSCAR_USUARIO)) {
			actionNameForDisplay = "SEG -> Buscar Usuario";
		}		
		if (actionName.equals(SweConstants.ACTION_ADMINISTRAR_USUARIO)) {
			actionNameForDisplay = "SEG -> Administrar Usuario";
		}
		if (actionName.equals(SweConstants.ACTION_ADMINISTRAR_USUARIO_ROL)) {
			actionNameForDisplay = "SEG -> Administrar Usuario Rol APS";
		}
		if (actionName.equals(SweConstants.ACTION_BUSCAR_ROL)) {
			actionNameForDisplay = "SEG -> Buscar Rol APS";
		}
		if (actionName.equals(SweConstants.ACTION_ADMINISTRAR_ROL)) {
			actionNameForDisplay = "SEG -> Administrar Rol APS";
		}
		if (actionName.equals(SweConstants.ACTION_BUSCAR_ROL_ACCION_MOD)) {
			actionNameForDisplay = "SEG -> Buscar Rol APS/Accion ModApl ";
		}
		if (actionName.equals(SweConstants.ACTION_ADMINISTRAR_ROL_ACCIONMOD)) {
			actionNameForDisplay = "SEG -> Administrar Rol APS/Accion ModApl";
		}*/
	

		return actionNameForDisplay;
	}
	
	public static String getActNameForDisplay(String actName){
		String actNameForDisplay = "";
		
		if( !actName.equals("") && !actName.startsWith("seleccionar") 
				&& !actName.startsWith("volver") && !actName.startsWith("inicializar")
				&& !actName.startsWith("adapter") && !actName.startsWith("buscar")){
			actNameForDisplay = " [ " + actName + " ]";
		}
		
		return actNameForDisplay;
	}

	public static boolean isABMAct(String act) {
		boolean result = false;
		
		if ( act==null ) act = "";
		if ( act.equals(SweConstants.ACT_VER) || act.equals(SweConstants.ACT_AGREGAR) || 
			 act.equals(SweConstants.ACT_MODIFICAR) || act.equals(SweConstants.ACT_ELIMINAR) ||
			 act.equals(SweConstants.ACT_ALTA) || act.equals(SweConstants.ACT_BAJA)	 ){
			 result = true;
		}
		
		return result;
	}
	
	// Metodos a pasar a demoda -----------------------------------------------------------------------------------
	public static String currentMethodName(){
		String funcName = new Exception().fillInStackTrace().getStackTrace()[1].getMethodName();
				
		return funcName;
	}
	
	
	public static String getVONameByClass(Common vo){
		String nameVO = "";
		nameVO = vo.getClass().toString();
		
		nameVO = nameVO + "VO";
		
		return nameVO;
	}
	
	public static String getNameClassByVO(Common vo){
		String nameVO = "";
		nameVO = vo.getClass().toString();
		nameVO = nameVO.substring(0, nameVO.length() - 2);
		
		return nameVO;
	}
}
