//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;

public class ItemMenuSearchPage extends SwePageModel {
	
	private static final long serialVersionUID = 1L;
	//private Log log = LogFactory.getLog(getClass());
	
	public static final String NAME = "itemMenuSearchPageVO";
	private AplicacionVO aplicacion = new AplicacionVO();
	private ItemMenuVO itemMenu = new ItemMenuVO();
	private Integer nivelMenu = new Integer(0);

    public ItemMenuSearchPage() {
        super(SweSecurityConstants.ABM_MENU);
    }
    public ItemMenuSearchPage(AplicacionVO aplicacionVO) {
    	this();
    	this.aplicacion = aplicacionVO;
    }
	public String infoString(){
		String infoString = 
			" aplicacion.id: " + this.aplicacion.getId() + "\r\n" +
			" itemMenu.id  : " + this.itemMenu.getId() + "\r\n";
		return infoString;
	}
	public AplicacionVO getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(AplicacionVO aplicacion) {
		this.aplicacion = aplicacion;
	}
	public ItemMenuVO getItemMenu() {
		return itemMenu;
	}
	public void setItemMenu(ItemMenuVO itemMenu) {
		this.itemMenu = itemMenu;
	}
	public Integer getNivelMenu() {
		return nivelMenu;
	}
	public void setNivelMenu(Integer nivelMenu) {
		this.nivelMenu = nivelMenu;
	}
	public boolean getEsRoot(){
		return ModelUtil.isNullOrEmpty(itemMenu);
	}

}
