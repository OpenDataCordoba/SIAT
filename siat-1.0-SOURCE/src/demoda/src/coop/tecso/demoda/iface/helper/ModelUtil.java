//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.helper;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import coop.tecso.demoda.buss.bean.BaseBean;
import coop.tecso.demoda.iface.model.BussImageModel;
import coop.tecso.demoda.iface.model.Common;


public class ModelUtil {
    
	
    static Logger log = Logger.getLogger(ModelUtil.class);

    /**
     * Se fija si un BussImageModel es nulo, su Id es nulo o es menor a 1
     * @param model el BussImageModel a comprobar 
     * @return true si es null o vacia o -1
     */
	public static boolean isNullOrEmpty( BussImageModel model ) {
		if (model == null || model.getId() == null ||
				model.getId() < 1 ) {
			return true;
		}
	    return false;
		
	}
	
	/**
	 * Obtiene el String formado por la lista de Ids separado por comas.
	 * @author David
	 * @param  listBussImageModel
	 * @return String
	 */
	public static String getListaStringId(List listBussImageModel){
		
		String  listaStringId = "";
		boolean flagPrimer    = true;
		for (Iterator iterList = listBussImageModel.iterator(); iterList.hasNext();) {
			BussImageModel model = (BussImageModel) iterList.next();
			if(!flagPrimer) listaStringId += ", ";
			listaStringId += model.getId();
			flagPrimer = false;
		}
		return listaStringId;
	}

    /**
     * Retorna true si el metodo pasado como parametro devuleve un
     * tipo: String, Integer, Long, Double, Float.  
     * 
     * @param method
     * @return
     */
    public static boolean isBasicType(Method method) {

    	String methodName = method.getReturnType().getName();
    	
        if ( methodName.equals("java.lang.String") ) {
            return true;
        } else if ( methodName.equals("java.lang.Integer") ) {
            return true;
        } else if ( methodName.equals("java.lang.Long") ) {
            return true;        	
        } else if ( methodName.equals("java.lang.Double") ) {
            return true;        	
        } else if ( methodName.equals("java.lang.Float") ) {
        	return true;
        } else {
        	return false;
        }
        
    }
    
    /**
     * Busca el BussImageModel a partir del id, en la lista 
     * @param  idBussImageModel
     * @param  listBussImageModel
     * @return BussImageModel
     */
    public static BussImageModel getBussImageModelByIdForList(Long idBussImageModel, List listBussImageModel){
		
		for (Iterator iterList = listBussImageModel.iterator(); iterList.hasNext();) {
			BussImageModel model = (BussImageModel) iterList.next();
			if (model.getId().equals(idBussImageModel)){
				return model;
			}
		}
		return null;
	}
    
    public static String  logDiffDate(Date fechaVO, Date fechaBO, String nombreCampo) {

    	String logCambios="";

    	if (fechaBO != null && fechaVO == null){ 

    		logCambios = "," + " se quit\u00F3"+ nombreCampo+ " "  + DateUtil.formatDate(fechaBO, DateUtil.ddSMMSYYYY_MASK);		
    	}

    	if (fechaBO == null && fechaVO != null){ 

    		logCambios = ", " + nombreCampo+ " "  + DateUtil.formatDate(fechaVO, DateUtil.ddSMMSYYYY_MASK);		
    	}

    	if (fechaVO != null && fechaBO != null && 
    			!DateUtil.isDateEqual(fechaVO, fechaBO)){

    		logCambios = ", " + nombreCampo+ " " + DateUtil.formatDate(fechaBO, DateUtil.ddSMMSYYYY_MASK) +
    		" por " + DateUtil.formatDate(fechaVO, DateUtil.ddSMMSYYYY_MASK);
    	}

    	return logCambios;

    }
    
    public static String logDiffString(String strVO, String strBO, String nombreCampo){
    	String logCambios="";

    	if (!StringUtil.isNullOrEmpty(strBO) && StringUtil.isNullOrEmpty(strVO)){ 

    		logCambios = ", " + " se quit\u00F3" + nombreCampo+ " " + strBO;		
    	}

    	if (StringUtil.isNullOrEmpty(strBO) && !StringUtil.isNullOrEmpty(strVO)){

    		logCambios = ", "+ nombreCampo+ " " + strVO;		
    	}

    	if (!StringUtil.isNullOrEmpty(strBO) && !StringUtil.isNullOrEmpty(strVO) 
    			&& 	!StringUtil.iguales(strBO, strVO)){ 

    		logCambios = ", " + nombreCampo + " "+ strBO +
    		" por " + strVO;		
    	}

    	return logCambios;
    }

    public static String logDiffInteger(Integer intVO, Integer intBO, String nombreCampo){
    	String logCambios="";

    	if (intBO != null && intVO == null){ 

    		logCambios = ", " + " se quit\u00F3 "+nombreCampo+ " " + intBO;		
    	}

    	if (intBO == null && intVO != null){ 

    		logCambios = ", " + nombreCampo +" " +intVO;		
    	}

    	if (intBO != null && intVO!= null && 
    			!intBO.equals(intVO) ){ 

    		logCambios = ", " + nombreCampo+ " " + intBO +" por " + intVO;		
    	}

    	return logCambios;

    }
    
    public static String logDiffDouble(Double doubleVO, Double doubleBO, String nombreCampo){
            
    	String logCambios="";

    	if (doubleBO != null && doubleVO == null){ 
    		logCambios = ", " + " se quit\u00F3 "+nombreCampo+ " " + doubleBO;		
    	}

    	if (doubleBO == null && doubleVO != null){ 
    		logCambios = ", " + nombreCampo+ " "  + doubleVO;		
    	}

    	if (doubleBO != null && doubleVO!= null && 
    			!doubleBO.equals(doubleVO) ){ 

    		logCambios = ", " + nombreCampo+ " "  + doubleBO +" por " + doubleVO;		
    	}

    	return logCambios;

    }

    /*El bean debe implementar getRepresent */
    public static String logDiffBaseBean(Common boVO, BaseBean boBO, String nombreCampo){
    	
    	String logCambios="";
    		
		if (boBO != null && boBO.getId() == null &&	boVO != null){
			logCambios =  ", " + nombreCampo + " " +boVO.getRepresent();
		}

		if(boBO != null && boBO.getId() != null && boVO == null){
			logCambios = ", " + " se quit\u00F3 el " + nombreCampo;
		}
		
		if (boBO != null && boBO.getId() != null &&  boVO != null && !boBO.getId().equals(boVO.getId())){
			logCambios = ", " + nombreCampo+ " " + boBO.getRepresent() + " por " + boVO.getRepresent();
		}
		
		return logCambios;     
    }
}
