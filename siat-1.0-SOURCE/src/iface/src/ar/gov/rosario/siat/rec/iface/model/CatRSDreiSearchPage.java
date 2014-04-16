//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * SearchPage del CatRSDrei
 * 
 * @author Tecso
 *
 */
public class CatRSDreiSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "catRSDreiSearchPageVO";
	
	private CatRSDreiVO catRSDrei= new CatRSDreiVO();
	
	private Date fechaCatRSDreiDesde;
	
	private Date fechaCatRSDreiHasta;
	
	public Date getFechaCatRSDreiDesde() {
		return fechaCatRSDreiDesde;
	}

	public void setFechaCatRSDreiDesde(Date fechaCatRSDreiDesde) {
		this.fechaCatRSDreiDesde = fechaCatRSDreiDesde;
	}

	public Date getFechaCatRSDreiHasta() {
		return fechaCatRSDreiHasta;
	}

	public void setFechaCatRSDreiHasta(Date fechaCatRSDreiHasta) {
		this.fechaCatRSDreiHasta = fechaCatRSDreiHasta;
	}

	// Constructores
	public CatRSDreiSearchPage() {       
       super(RecSecurityConstants.ABM_CATEGORIARS);        
    }
	
	// Getters y Setters
	public CatRSDreiVO getCatRSDrei() {
		return catRSDrei;
	}
	public void setCatRSDrei(CatRSDreiVO catRSDrei) {
		this.catRSDrei = catRSDrei;
	}           

    public String getName(){    
		return NAME;
	}
	
	
	// View getters
    
    public String getFechaCatRSDreiDesdeView(){
		return (this.fechaCatRSDreiDesde!=null)? DateUtil.formatDate(this.fechaCatRSDreiDesde, DateUtil.ddSMMSYYYY_MASK):"";
	}
	
	public String getFechaCatRSDreiHastaView(){
		return (this.fechaCatRSDreiHasta!=null)?DateUtil.formatDate(this.fechaCatRSDreiHasta, DateUtil.ddSMMSYYYY_MASK):"";
	}
}
