//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;
                                                                         
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Page define las propiedades que deben respetar las clases que utilicen listas
 * de otros objetos del model
 *
 * @author tecso 
 * @version  2.0
 */
public class PageModel extends CommonNavegableView {
	    
    private List listResult = new ArrayList();
    
    private Long pageNumber = new Long(0);
    private Long maxPageNumber = new Long(1);
    private Long recsByPage = new Long(5);
    private Long maxRegistros = new Long(0);
    private boolean isPaged   = true; // esta bandera la uso para decirle al pageModel si pagina o no.
    private String multiSelectEnabled = DISABLED; // indica si el searchPage permite multiple seleccion


    @Deprecated
    private String aBMEnabled = ENABLED;

    public PageModel(){
    	super();
    }
    		
	public PageModel(String sweActionName){
		super(sweActionName);
	}
	
    /**
     * @deprecated
     */
    private Collection colModel = new LinkedList();
    /**
     * @deprecated
     */
    private boolean inactivo = false; // false para que considere en la busqueda solo los activos, true busca todos.
    
    // pageNumber
    public Long getPageNumber() { return this.pageNumber;}
    public void setPageNumber(Long pageNumber) 
    { this.pageNumber = pageNumber;}
    
    // devuelve el indice del FirstResult
    public int getFirstResult() {
    	int i = (this.pageNumber.intValue() - 1) * this.recsByPage.intValue();
    	return i;
    }
    
    // maxNumber
    public Long getMaxPageNumber() { 
    	return new Long((long)Math.ceil( this.maxRegistros.doubleValue()
				/ this.getRecsByPage().doubleValue()));
	}

    // recsByPage
    public Long getRecsByPage() { return this.recsByPage;}
    public void setRecsByPage(Long recsByPage) 
    { this.recsByPage = recsByPage;}
    
    // maxRegistros
    public Long getMaxRegistros() {
		return maxRegistros;
	}
	public void setMaxRegistros(Long maxRegistros) {
		this.maxRegistros = maxRegistros;
	}

	// isPaged
    public boolean isPaged() {
		return isPaged;
	}
	public void setPaged(boolean isPaged) {
		this.isPaged = isPaged;
	}

	// list de resultados
    public List getListResult() {
		return listResult;
	}
	public void setListResult(List listResult) {
		this.listResult = listResult;
	}
	
	// inactivo
	@Deprecated
	public boolean getInactivo() {
		return inactivo;
	}
	public void setInactivo(boolean inactivo) {
		this.inactivo = inactivo;
	}

	// ABMEnabled
	public String getABMEnabled() {
		return aBMEnabled;
	}
	public void setABMEnabled(String aBMEnabled) {
		this.aBMEnabled = aBMEnabled;
	}

	
	public String getMultiSelectEnabled() {
		return multiSelectEnabled;
	}
	public void setMultiSelectEnabled(String multiSelectEnabled) {
		this.multiSelectEnabled = multiSelectEnabled;
	}
	
	
	public boolean getHasNextPage() {
		if (this.getPageNumber().intValue() >= this.getMaxPageNumber().intValue()){
			return false;
		}else
			return true;
	}
	
	public boolean getHasPrevPage() {
		if (this.getPageNumber().intValue() <= 1){
			return false;
		} else 
			return true;
		
	}
	
	public Long getNextPage() {
		if (this.getHasNextPage()){
			return new Long(this.getPageNumber().intValue() + 1);
		}else{
			return new Long(-1);}  
		
	}
	
	public Long getPrevPage() {
		if (this.getHasPrevPage()){
			return new Long(this.getPageNumber().intValue() - 1);
		}else{
			return new Long(-1);}
	}
		
	public void setMaxPageNumber(Long maxPageNumber) {
		this.maxPageNumber = maxPageNumber;
	}
	
	/*public boolean getModoSeleccionar() {
		return modoSeleccionar;
	}
	public void setModoSeleccionar(boolean modoSeleccionar) {
		this.modoSeleccionar = modoSeleccionar;
	}*/
	
	public boolean getViewResult(){
		return this.pageNumber.longValue() > 0;
	}

	/**
	 * Recalcula la ctd maxima de registros a partir del parametro size
	 * @param size tamanio de la lista de resultados obtenida de la pagina actual 
	 * Ej: paginamos 5 registros por pag, estamos en la pagina 3, y obtuvimos 4 resultados en la pag 3.
	 * maxRegistros = 3 * 5 - (5 - 4) = 14 
	 */
	public void recalcularMaxRegistros(int size){
		// diferencia entre la ctd de registros por pagina y el size de la lista de la pagina
		int diferencia = this.getRecsByPage().intValue() - size;
		if (diferencia > 0){
			// calculo la nueva max ctd de registros  
			this.setMaxRegistros(
					this.getPageNumber() * this.getRecsByPage() - diferencia);
		}
	}

}
