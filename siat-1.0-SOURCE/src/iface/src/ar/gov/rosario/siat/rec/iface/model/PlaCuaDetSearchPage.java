//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * SearchPage de los detallas de la plnilla cuadra
 * 
 * @author Tecso
 *
 */
public class PlaCuaDetSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "plaCuaDetSearchPageVO";

	private PlaCuaDetVO plaCuaDet = new PlaCuaDetVO();

	private String[] listId;
	// Constructores
	public PlaCuaDetSearchPage() {       
       super(RecSecurityConstants.ABM_PLANILLACUADRA_DETALLE);
    }
	
	public PlaCuaDetSearchPage(String manzana1, String manzana2) {       
		this();
		PlanillaCuadraVO planillaCuadra = this.getPlaCuaDet().getPlanillaCuadra();
		planillaCuadra.setManzana1(manzana1);
		planillaCuadra.setManzana2(manzana2);
    }
	
	public PlaCuaDetSearchPage(PlanillaCuadraVO planillaCuadraParam) {       
		this();
		this.getPlaCuaDet().setPlanillaCuadra(planillaCuadraParam);
    }
	

	// Getters y Setters
	public PlaCuaDetVO getPlaCuaDet() {
		return plaCuaDet;
	}
	public void setPlaCuaDet(PlaCuaDetVO plaCuaDet) {
		this.plaCuaDet = plaCuaDet;
	}

	public String[] getListId() {
		return listId;
	}

	public void setListId(String[] listId) {
		this.listId = listId;
	}

	public boolean hasManzana() {
		boolean hasManzana = true;
		PlanillaCuadraVO planillaCuadraVO = this.getPlaCuaDet().getPlanillaCuadra();
		
		
		if (StringUtil.isNullOrEmpty(planillaCuadraVO.getManzana1())
			&& StringUtil.isNullOrEmpty(planillaCuadraVO.getManzana2())) {
			hasManzana = false;
		}
		
		return hasManzana;
	}
	
	public List<PlaCuaDetVO> getListPlaCuaDetSelected(){
		List<PlaCuaDetVO> listPlaCuaDetVO = new ArrayList<PlaCuaDetVO>();
		List<PlaCuaDetVO> listResultVO = (ArrayList<PlaCuaDetVO>) this.getListResult();

		if (this.getListId() != null) {
			// itero los id seleccionados
			for (String id :this.getListId()) {
				
				// itero los plaCuaDetVO
				for (PlaCuaDetVO plaCuaDetVO:listResultVO) {
					if (id.trim().equals(plaCuaDetVO.getIdSelect())){
						listPlaCuaDetVO.add(plaCuaDetVO);
						break;
					}
				}
	
			}
		}

		return listPlaCuaDetVO;

	}
	
	
	// View getters
}
