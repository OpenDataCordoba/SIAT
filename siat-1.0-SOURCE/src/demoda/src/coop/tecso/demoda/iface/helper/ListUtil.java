//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import coop.tecso.demoda.iface.model.BussImageModel;
import coop.tecso.demoda.iface.model.Common;

public class ListUtil {
	
	public static BussImageModel searchVOInList(List listVO, BussImageModel searchVO) throws Exception {
		
		BussImageModel bim = searchVO.getClass().newInstance() ;

		for (Iterator iter = listVO.iterator(); iter.hasNext();) {
			BussImageModel vo = (BussImageModel) iter.next();			
			if (vo.getId().equals(searchVO.getId())) {
				bim = vo;
				break;
			}
		}
		return bim;
	}

	public static boolean existInList(List<Long> list, Long searchLong) throws Exception {
		boolean exist = false;
		
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Long myLong = (Long) iter.next();
			
			if ( myLong.longValue() == searchLong.longValue() ) {
				exist = true;
				break;
			}			
		}
		return exist;
	}
	
	/**
	 * Obtiene el String de la lista de valores separada por coma.
	 * Devuelve el String null si la lista es vacia. Es muy adecuado en la formacion de los IN( stringList ) de los SELECT
	 * Devuelve el objeto null si la lista es nula
	 * @author David
	 * @param list
	 * @return String
	 */
	public static String getStringList(List<Long> list){
    	
		if(list == null){
			return null;
		}
		
    	String listStr = "null";
    	boolean flagFirst = true;
    	
    	for (Long id : list) {
    	
    		if(flagFirst){
    			listStr = id.toString().trim();
    			flagFirst = false;
    		}else{
    			listStr += "," + id.toString().trim();
    		}
		}
    	
    	return listStr;
    }
	
	public static String getStringList1(List<String> list){
    	
		if(list == null){
			return null;
		}
		
    	String listStr = "null";
    	boolean flagFirst = true;
    	
    	for (String id : list) {
    	
    		if(flagFirst){
    			listStr = ("'" + id.trim() + "'");
    			flagFirst = false;
    		}else{
    			listStr += ("," +  "'" + id.trim()+ "'");
    		}
		}
    	
    	return listStr;
    }


	/**
	 * Obtiene el String de la lista de id separada por coma de una lista de objetos Common
	 * Devuelve "" si la lista es nula
	 * @author fedel
	 * @param list
	 * @return String
	 */
	public static String getStringIds(List list){
      	if(list == null || list.size() == 0){
			return "";
		}

    	StringBuilder listStr = new StringBuilder("");
    	for (Object o : list) {
			Common common = (Common) o;
			listStr.append(",").append(common.getId());
		}

    	return listStr.substring(1);
    }

	/** Devuelve un String de Ids separados por coma, obtenidos
	 *  de la lista de BussImageModel pasados como parametros
	 * 
	 * @param listBussImageModel
	 * @return
	 */
	
	public static String getStringIdsFromListModel(List listBussImageModel) {

		String stringIds = "";
		
		if ( !ListUtil.isNullOrEmpty(listBussImageModel)) {
			List<Long> listLongId = ListUtil.getListLongIdFromListModel(listBussImageModel);
			stringIds = ListUtil.getStringList(listLongId);
		}

    	return stringIds;
    }
	
	public static List<Long> getListLongIdFromListModel( List listBussImageModel ) {

		List<Long> listLongId = new ArrayList<Long>();
		
		Iterator iteratorBussImageModel = listBussImageModel.iterator();
		
		while ( iteratorBussImageModel.hasNext() ) {
			BussImageModel bim = (BussImageModel) iteratorBussImageModel.next();
			listLongId.add(bim.getId());
		}

    	return listLongId;
    }
	
	
    /**
     * Se fija si una Lista es null o esta vaicia
     * @param List la lista a comprobar 
     * @return true si es null o vacia
     */
    public static boolean isNullOrEmpty(List list) {
        
        if (list == null || list.size() == 0) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Se fija si un determinado common esta
     * en un lista
     */
    public static boolean isInList(List list, Common common) {
    	boolean existInList = false;
    	
        for (Object objectIt:list) {
			Common commonIt = (Common) objectIt;
        	if (commonIt.getId().equals(common.getId())) {
        		existInList = true;
        		break;
        	}
		}
        
        return existInList;
    }
    
    /** Devuelve la interseccion de dos listas pasadas como paremetro
     * 
     * @param list1
     * @param list2
     * @return
     */
    public static List<Long> interseccionLong(List<Long> list1, List<Long> list2) throws Exception {
    	List<Long> listInterseccion = new ArrayList<Long>();

    	for(Long val:list1) {
    		if (existInList(list2, val)) {
    			listInterseccion.add(val);
    		}
    	}

    	return listInterseccion;
    }
    
    /** Devuelve la interseccion de dos listas pasadas como paremetro
     * 
     * @param list1
     * @param list2
     * @return
     */
    public static List<String> interseccionString(List<String> list1, List<String> list2) throws Exception {
    	List<String> listInterseccion = new ArrayList<String>();

    	for(String val:list1) {
    		if (list2.contains(val)) {
    			listInterseccion.add(val);
    		}
    	}

    	return listInterseccion;

    }
    
    
	/**
	 * Recibe una lista de String de Id y devuelve una lista Long de Id
	 * 
	 * @author Cristian
	 * @param listStrId
	 * @return listLongId
	 */
	public static Long[] getArrLongIdFromArrStringId( String[] listStrId ) {
		
		if (listStrId == null)
			return null;
		
		Long[] listLongId = new Long[listStrId.length];
		
		for (int i=0; i < listStrId.length; i++){
			listLongId[i] = new Long(listStrId[i]);
		}

    	return listLongId;
    }

	/**
	 * Recibe una lista Long de Id y devuelve una lista de String de Id
	 * 
	 * @param listLongId
	 * @return listStrId
	 */
	public static String[] getArrStringIdFromArrLongId(Long[] listLongId) {
		
		if (listLongId == null)
			return null;
		
		String[] listStrId = new String[listLongId.length];
		
		for (int i=0; i < listLongId.length; i++){
			listStrId[i] = listLongId[i].toString();
		}

    	return listStrId;
    }

	
    /**
     * Se fija si una String[] es null o esta vacia
     * @param List la lista a comprobar 
     * @return true si es null o vacia
     */
    public static boolean isNullOrEmpty(String[] list) {
        
        if (list == null || list.length == 0) {
            return true;
        }
        
        return false;
    }
	
    /**
     * Se fija si un long se encuetra dentro de un array de longs 
     * 
     * @param id
     * @param listLongId
     * @return boolean
     */
    public static boolean isInArrLong(Long id, Long[] listLongId) {
    	boolean existInList = false;
    	
        for (Long longIt:listLongId) {
        	if (longIt.equals(id)) {
        		existInList = true;
        		break;
        	}
		}
        
        return existInList;
    }
    
    /**
     * Recibe una String con ids separados por coma y devuelve un ArrayList de Long con los respectivos ids.
     * 
     * @param idsSeparadosPorComas
     * @return
     */
    public static List<Long> getListIdFromStringWithCommas(String idsSeparadosPorComas){
    	List<Long> listId= new ArrayList<Long>();
    	while (idsSeparadosPorComas.length() > 0){
    		int indiceComa = idsSeparadosPorComas.indexOf(",");
    		if (indiceComa==-1)indiceComa=idsSeparadosPorComas.length();
    		String idAProcesar = idsSeparadosPorComas.substring(0, indiceComa).trim();
    		if (StringUtil.isNumeric(idAProcesar))
    			listId.add(Long.parseLong(idAProcesar));
    		
    		
    		if (indiceComa != idsSeparadosPorComas.length())indiceComa++;
    		
    		idsSeparadosPorComas = idsSeparadosPorComas.substring(indiceComa, idsSeparadosPorComas.length());
    	}
    	return listId;
    }
    
    /**
     * Se fija si una Long[] es null o esta vacia
     * @param List la lista a comprobar 
     * @return true si es null o vacia
     */
    public static boolean isNullOrEmpty(Long[] list) {
        
        if (list == null || list.length == 0) {
            return true;
        }
        
        return false;
    }
}
