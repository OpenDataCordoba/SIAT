//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del CompFuenteCol
 * @author tecso
 *
 */
public class CompFuenteColVO extends SiatBussImageModel {

	private Logger log = Logger.getLogger(CompFuenteColVO.class);
			
	private static final long serialVersionUID = 1L;

	public static final String NAME = "compFuenteColVO";
	
	private CompFuenteVO compFuente;

	private String colName;

	private Integer nroColumna;

	private Integer orden;

	private SiNo oculta;

	private SiNo sumaEnTotal;

	private List<AliComFueColVO> listAliComFueCol = new ArrayList<AliComFueColVO>();
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public CompFuenteColVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public CompFuenteColVO(int id, String desc) {
		super();
		setId(new Long(id));
	}
	
	// Getters y Setters

	public CompFuenteVO getCompFuente() {
		return compFuente;
	}

	public void setCompFuente(CompFuenteVO compFuente) {
		this.compFuente = compFuente;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public Integer getNroColumna() {
		return nroColumna;
	}

	public void setNroColumna(Integer nroColumna) {
		this.nroColumna = nroColumna;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public SiNo getOculta() {
		return oculta;
	}

	public void setOculta(SiNo oculta) {
		this.oculta = oculta;
	}

	public SiNo getSumaEnTotal() {
		return sumaEnTotal;
	}

	public void setSumaEnTotal(SiNo sumaEnTotal) {
		this.sumaEnTotal = sumaEnTotal;
	}


	public List<AliComFueColVO> getListAliComFueCol() {
		return listAliComFueCol;
	}

	public void setListAliComFueCol(List<AliComFueColVO> listAliComFueCol) {
		this.listAliComFueCol = listAliComFueCol;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public AliComFueColVO getAlicuotaVigente(){
		try{
			for(AliComFueColVO aliComFueColVO: listAliComFueCol){
				if(getCompFuente().getPeriodoDesde()<=aliComFueColVO.getPeriodoDesde() &&
					getCompFuente().getPeriodoHasta()>=aliComFueColVO.getPeriodoHasta() &&
					getCompFuente().getAnioDesde()<=aliComFueColVO.getAnioDesde() &&
					getCompFuente().getAnioHasta()>=aliComFueColVO.getAnioHasta()){
					
					return aliComFueColVO;
				}
			}
		}catch(Exception e){
			log.error(e);
		}
		
		return new AliComFueColVO();
	}
	
	public String getHistAlicuota4View(){
		String ret ="";
		for(AliComFueColVO aliComFueColVO: listAliComFueCol){
			ret +="Desde "+aliComFueColVO.getPeriodoAnioDesdeView()+"    Hasta:"+
			aliComFueColVO.getPeriodoAnioHastaView();
			if(aliComFueColVO.getEsOrdConCueEtur()){
				ret+="    Actividad: "+aliComFueColVO.getTipoUnidad().getCodYDescripcion();
				if(aliComFueColVO.getRadio()!=null){
					ret+=" ; Radio: "+ aliComFueColVO.getRadio().toString();
				}
			}else{
				ret+="    Al\u00EDcuota:"+aliComFueColVO.getValorAlicuotaView();
				if (aliComFueColVO.getCantidad()!=null && aliComFueColVO.getValorUnitario()!=null)
					ret+=" ; Cantidad: "+ aliComFueColVO.getCantidadView()+" Valor Unitario: "+aliComFueColVO.getValorUnitarioView();
				
				ret+="<br>";
			}
		}
		return ret;
	}
}
