//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.buss.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.bean.BaseBean;
import coop.tecso.demoda.iface.model.BussImageModel;


public class ListUtilBean {

	
	static Logger log = Logger.getLogger(ListUtilBean.class);
	
	/** 
	 * Retorna una nueva lista de BO dejando solo los de estado == 1 
	 * 
	 * @param listBO
	 * @return
	 */
	static public List<? extends BaseBO> filterByActivo(List<? extends BaseBO> listBO) {
		if (listBO == null)
			return null;
		List<BaseBO> ret = new ArrayList<BaseBO>(); 
		for(BaseBO bo: listBO) {
			if (bo.getEstado().intValue() == 1) {
				ret.add(bo);
			}
		}
		return ret;
	}
	
	/**
	 * Pasa una lista de BaseBO a VO
	 *
	 * @param listVO lista de objetos BO 
	 * @return lista de VO
	 */
	public static List toVO(List listBO) throws Exception {
		return toVO(listBO,0);
	}
	
	public static List toVO(List listBO, int prof) throws Exception {
		List listVO = null;
		
		if (listBO != null){
			listVO = new ArrayList();
			
			for(Iterator it = listBO.iterator(); it.hasNext();){
				BaseBO baseBO = (BaseBO) it.next();
				listVO.add(baseBO.toVO(prof));
			}
		}
		
		return listVO;
	}
	
	public static List toVO(List listBO, int prof, boolean copiarList) throws Exception {
		List listVO = null;
		
		if (listBO != null){
			listVO = new ArrayList();
			
			for(Iterator it = listBO.iterator(); it.hasNext();){
				BaseBO baseBO = (BaseBO) it.next();
				listVO.add(baseBO.toVO(prof, copiarList));
			}
		}
		
		return listVO;
	}
	
	public static List toVO(Collection blanca, List listBO, int prof) throws Exception {
		List listVO = null;
		
		if (listBO != null){
			listVO = new ArrayList();
			
			for(Iterator it = listBO.iterator(); it.hasNext();){
				BaseBO baseBO = (BaseBO) it.next();
				listVO.add(baseBO.toVO(blanca, prof));
			}
		}
		
		return listVO;
	}
	
	public static List toVO(List listBO, int prof, BussImageModel bussImageModel) throws Exception {

		List listBussImageModel = new ArrayList();
		listBussImageModel.add(bussImageModel);
		listBussImageModel.addAll(toVO(listBO,prof));
		
		return listBussImageModel;
	}

	public static List toVO(List listBO, int prof, boolean copiarList, BussImageModel bussImageModel) throws Exception {

		List listBussImageModel = new ArrayList();
		listBussImageModel.add(bussImageModel);
		listBussImageModel.addAll(toVO(listBO,prof,copiarList));
		
		return listBussImageModel;
	}
	
	
	public static List toVO(List listBO, BussImageModel bussImageModel) throws Exception {

		return toVO(listBO, 0, bussImageModel);
	}

	public static List<Long> getListLongIdFromListBaseBO( List listBaseBO ) {

		List<Long> listLongId = new ArrayList<Long>();
		
		Iterator iteratorBaseBO = listBaseBO.iterator();
		while (iteratorBaseBO.hasNext()) {
			BaseBO baseBO = (BaseBO) iteratorBaseBO.next();
			listLongId.add(baseBO.getId());
		}
    	return listLongId;
    }
	
	public static Long[] getArrLongIdFromListBaseBO( List listBaseBO ) {
		
		if (listBaseBO == null)
			return null;
		
		Long[] listLongId = new Long[listBaseBO.size()];
		
		int i=0;
		Iterator iteratorBaseBO = listBaseBO.iterator();
		while (iteratorBaseBO.hasNext()) {
			BaseBO baseBO = (BaseBO) iteratorBaseBO.next();
			listLongId[i] = baseBO.getId();
			i++;
		}
    	return listLongId;
    }	
	
	
	
	public static Boolean contains(List listBaseBean, BaseBean baseBean) {
		if (baseBean == null){
			return false;
		}
		if (listBaseBean == null){
			return false;
		}
		
		for (Iterator iter = listBaseBean.iterator(); iter.hasNext();) {

			BaseBean baseBean2 = (BaseBean) iter.next();
			
			if (baseBean2 == null){
				return false;
			}
			
			if (baseBean.getId() ==  null || baseBean2.getId() == null){
				return false;
			}
			
			if (baseBean.getId().longValue() == baseBean2.getId().longValue()){
				return true;
			}
		}
		return false;
	}

	/**
	 * Recibe una lista de Beans y otra lista de Bean a excluir.
	 * Devuelve la primer lista sin los que figuren el al segunda.
	 * 
	 * @author Cristian
	 * @param listBean
	 * @param listBeanExclude
	 * @return
	 */
	public static List<? extends BaseBO> getListExclude(List<? extends BaseBO> listBean, List<? extends BaseBO> listBeanExclude){
		
		if (listBean == null)
			return null;
		
		List<BaseBO> ret = new ArrayList<BaseBO>(); 
		for(BaseBO bo: listBean) {
			if (!contains(listBeanExclude, bo)) {
				ret.add(bo);
			}
		}
		return ret;
	} 
	
	
	/**
	 * Recibe una lista de Beans donde pueden encontrarse elementos repetidos.
	 * Devuelve otra lista de Beans con solo los elementos que no se repiten. 
	 * 
	 * @author Cristian
	 * @param listBean
	 * @param listBeanExclude
	 * @return
	 */
	public static List<? extends BaseBO> getListDistinct(List<? extends BaseBO> listBean){
		
		if (listBean == null)
			return null;
		
		List<BaseBO> listRet = new ArrayList<BaseBO>(); 
		for(BaseBO bo: listBean) {
			if (!contains(listRet, bo)) {
				listRet.add(bo);
			}
		}
		return listRet;
	}
	
	/**
	 * Rebibe una lista de bean y un array de Long con id.
	 * Devuelve otra lista de Bean con los que el id se encuentre en la lista de Long. 
	 * 
	 * @author Cristian
	 * @param listBaseBO
	 * @param listLingId
	 * @return
	 */
	public static List<? extends BaseBO> getListBeanByListId( List listBaseBO, Long[] listLingId) {

		List<BaseBO> listBeanRet = new ArrayList<BaseBO>();
		
		Iterator iteratorBaseBO = listBaseBO.iterator();
		while (iteratorBaseBO.hasNext()) {
			BaseBO baseBO = (BaseBO) iteratorBaseBO.next();
			
			for (Long id: listLingId){
				if (baseBO.getId().longValue() == id.longValue()){
					listBeanRet.add(baseBO);
				}
			}
		}
    	
		return listBeanRet;
    }

}
