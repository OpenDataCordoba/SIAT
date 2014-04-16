//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class LiqNotificacionVO {
	
	private static Logger log = Logger.getLogger(LiqNotificacionVO.class);
	
	int largoLinea = 65;
	
	LiqCuentaVO cuenta = new LiqCuentaVO(); 
	List<LiqDeudaVO> listDeuda = new ArrayList<LiqDeudaVO>();
	List<LiqCuotaVO> listCuota = new ArrayList<LiqCuotaVO>();
	List<LiqConvenioVO> listConvenio = new ArrayList<LiqConvenioVO>();
	private String totalDeudaView = "";
	private String totalCuotaView = "";
	
	private String strPeriodosDeuda ="";
	private String beneficios = "";
	private String lugarConcurrencia ="";
	
	private String lineaDeuda1 ="";
	private String lineaDeuda2 ="";
	private String lineaDeuda3 ="";
	private String lineaDeuda4 ="";
	
	private String lineaConvenioCuota1 ="";
	private String lineaConvenioCuota2 ="";
	private String lineaConvenioCuota3 ="";
	private String lineaConvenioCuota4 ="";
	
	public LiqCuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(LiqCuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	public List<LiqDeudaVO> getListDeuda() {
		return listDeuda;
	}
	public void setListDeuda(List<LiqDeudaVO> listDeuda) {
		this.listDeuda = listDeuda;
	}
	public List<LiqCuotaVO> getListCuota() {
		return listCuota;
	}
	public void setListCuota(List<LiqCuotaVO> listCuota) {
		this.listCuota = listCuota;
	}
	public List<LiqConvenioVO> getListConvenio() {
		return listConvenio;
	}
	public void setListConvenio(List<LiqConvenioVO> listConvenio) {
		this.listConvenio = listConvenio;
	}
	public String getTotalDeudaView() {
		return totalDeudaView;
	}
	public void setTotalDeudaView(String totalDeudaView) {
		this.totalDeudaView = totalDeudaView;
	}
	public String getTotalCuotaView() {
		return totalCuotaView;
	}
	public void setTotalCuotaView(String totalCuotaView) {
		this.totalCuotaView = totalCuotaView;
	}
	public String getLineaDeuda1() {
		return lineaDeuda1;
	}
	public void setLineaDeuda1(String lineaDeuda1) {
		this.lineaDeuda1 = lineaDeuda1;
	}
	public String getLineaDeuda2() {
		return lineaDeuda2;
	}
	public void setLineaDeuda2(String lineaDeuda2) {
		this.lineaDeuda2 = lineaDeuda2;
	}
	public String getLineaDeuda3() {
		return lineaDeuda3;
	}
	public void setLineaDeuda3(String lineaDeuda3) {
		this.lineaDeuda3 = lineaDeuda3;
	}
	public String getLineaDeuda4() {
		return lineaDeuda4;
	}
	public void setLineaDeuda4(String lineaDeuda4) {
		this.lineaDeuda4 = lineaDeuda4;
	}
	public String getStrPeriodosDeuda() {
		return strPeriodosDeuda;
	}
	public void setStrPeriodosDeuda(String strPeriodosDeuda) {
		this.strPeriodosDeuda = strPeriodosDeuda;
	}
	public String getBeneficios() {
		return beneficios;
	}
	public void setBeneficios(String beneficios) {
		this.beneficios = beneficios;
	}
	public String getLugarConcurrencia() {
		return lugarConcurrencia;
	}
	public void setLugarConcurrencia(String lugarConcurrencia) {
		this.lugarConcurrencia = lugarConcurrencia;
	}
	public boolean getTieneDeudas(){
		return this.listDeuda.size() > 0;
	}
	public boolean getTieneCuotas(){
		return this.listCuota.size() > 0;
	}

	
	
	
	
	// --> metodos para obtener las lineas de la cadena beneficios
	
	/**Obtiene la primer linea de la cadena beneficios, teniendo en cuenta el largo total de la cadena, que puede ser mayor o menor a una linea.<br>
	 * Se utiliza para la impresion en TXT*/
	public String getBeneficioLinea1(){
		boolean tieneLinea2 = beneficios.length()>=largoLinea;// Si el String es mas largo que una linea => tieneLinea2
		if(tieneLinea2)
			return beneficios.substring(0, largoLinea);
		
		return beneficios;// Si no tiene linea 2 => devuelve toda la cadena
	}

	/** Obtiene la segunda linea de la cadena beneficios, teniendo en cuenta el largo total de la cadena<br>
	 * Se utiliza para la impresion en TXT*/
	public String getBeneficioLinea2(){
		return getBeneficioLinea(2);
	}
	
	/** Obtiene la tercera linea de la cadena beneficios, teniendo en cuenta el largo total de la cadena<br>
	 * Se utiliza para la impresion en TXT*/
	public String getBeneficioLinea3(){
		return getBeneficioLinea(3);
	}	
	
	/** Obtiene la Cuarta linea de la cadena beneficios, teniendo en cuenta el largo total de la cadena<br>
	 * Se utiliza para la impresion en TXT*/
	public String getBeneficioLinea4(){
		return getBeneficioLinea(4);
	}
	
	/** Obtiene la quinta linea de la cadena beneficios, teniendo en cuenta el largo total de la cadena<br>
	 * Se utiliza para la impresion en TXT*/
	public String getBeneficioLinea5(){
		return getBeneficioLinea(5);
	}
	
	/** Obtiene la sexta linea de la cadena beneficios, teniendo en cuenta el largo total de la cadena<br>
	 * Se utiliza para la impresion en TXT*/	
	public String getBeneficioLinea6(){
		return getBeneficioLinea(6);
	}
	
	/**
	 * Obtiene una linea determinada de la cadena beneficios (cada linea tiene un largo fijo de 80 caracteres)
	 * @param nroLinea
	 * @return
	 */
	private String getBeneficioLinea(int nroLinea){
		boolean tieneLinea = beneficios.length()>largoLinea*(nroLinea-1);
		
		if(!tieneLinea)
			return "";
		
		boolean tieneLineaSigte = beneficios.length()>largoLinea*nroLinea;
		if(tieneLineaSigte)
			return beneficios.substring(largoLinea*(nroLinea-1), largoLinea*nroLinea);
		
		return beneficios.substring(largoLinea*(nroLinea-1));// Si no tiene una proxima linea => devuelve hasta el final
	}	
	
	// <-- metodos para obtener las lineas de la cadena beneficios
	
	
	
	
	// --> metodos para obtener las lineas de la cadena lugarConcurrencia
	
	/**Obtiene la primer linea de la cadena LugarConcurrencia, teniendo en cuenta el largo total de la cadena, que puede ser mayor o menor a una linea.<br>
	 * Se utiliza para la impresion en TXT*/
	public String getLugarConcurrenciaLinea1(){
		boolean tieneLinea2 = lugarConcurrencia.length()>=largoLinea;// Si el String es mas largo que una linea => tieneLinea2
		if(tieneLinea2)
			return lugarConcurrencia.substring(0, largoLinea);
		
		return lugarConcurrencia;// Si no tiene linea 2 => devuelve toda la cadena
	}

	/** Obtiene la segunda linea de la cadena LugarConcurrencia, teniendo en cuenta el largo total de la cadena<br>
	 * Se utiliza para la impresion en TXT*/
	public String getLugarConcurrenciaLinea2(){
		return getLugarConcurrenciaLinea(2);
	}
	
	/** Obtiene la tercera linea de la cadena LugarConcurrencia, teniendo en cuenta el largo total de la cadena<br>
	 * Se utiliza para la impresion en TXT*/
	public String getLugarConcurrenciaLinea3(){
		return getLugarConcurrenciaLinea(3);
	}	
	
	private String getLugarConcurrenciaLinea(int nroLinea){
		boolean tieneLinea = lugarConcurrencia.length()>largoLinea*(nroLinea-1);
		
		if(!tieneLinea)
			return "";
		
		boolean tieneLineaSigte = lugarConcurrencia.length()>largoLinea*nroLinea;
		if(tieneLineaSigte)
			return lugarConcurrencia.substring(largoLinea*(nroLinea-1), largoLinea*nroLinea);
		
		return lugarConcurrencia.substring(largoLinea*(nroLinea-1));// Si no tiene una proxima linea => devuelve hasta el final
	}	
	

	// <-- metodos para obtener las lineas de la cadena lugarConcurrencia	
	
	
	
	
	// --> Metodos para generar las lineas con los periodos de deuda
	
	/**
	 * <p>Genera un String con los periodos de deuda, de la forma :"pp-pp-pp-pp...-pp/yy pp-pp-pp-pp...-pp/yy".</p>
	 * <p>Supone que las deudas ya estan ordenadas por periodo/anio</p>
	 * @author arobledo
	 */
	public void GenerarStringPeriodosDeuda(){
		log.debug("GenerarStringPeriodosDeuda - enter - cant deudas:"+listDeuda.size());
		if(listDeuda.size()>0){
			String anioAnterior =listDeuda.get(0).getAnio();// son los 2 ultimos digitos del anio
			strPeriodosDeuda += listDeuda.get(0).getPeriodo().trim().length()==1?"0"+listDeuda.get(0).getPeriodo().trim():listDeuda.get(0).getPeriodo().trim();		
			
			if(listDeuda.size()==1){
				strPeriodosDeuda +="/"+listDeuda.get(0).getAnio();
			}else{
			
				for(int i=1;i<listDeuda.size();i++){
					
					LiqDeudaVO liqDeuda = listDeuda.get(i);
					// Obtiene el anio y periodo de la cadena periodoDeuda 
					String periodo =  listDeuda.get(i).getPeriodo().trim().length()==1?"0"+listDeuda.get(i).getPeriodo().trim():listDeuda.get(i).getPeriodo().trim();
					String anio = liqDeuda.getAnio();
					
					// Verifica si es el ultimo periodo del anio
					if(anio.equals(anioAnterior)){
						strPeriodosDeuda +="-"+periodo;
						// Si es el ultimo periodo de la lista, agrega el anio
						if(i==listDeuda.size()-1)
							strPeriodosDeuda +="/"+anioAnterior;
					}else{
						strPeriodosDeuda +="/"+anioAnterior+"   "+periodo;
						if (i==listDeuda.size()-1)
							strPeriodosDeuda += "/" + anio; 
					}
					
					anioAnterior = anio;
				}
			}
		}
		log.debug("strPeriodosDeuda generado:"+strPeriodosDeuda);
		log.debug("GenerarStringPeriodosDeuda - exit");
	}
	
	
	/*public String getStrCuotas() {
		
		String detalleCuotas= "";
		int cantCuotas= getListCuota().size();
		if (cantCuotas==0)return detalleCuotas;
		
		String convenioAnt="";
		int i=0;
		for (LiqCuotaVO cuota : getListCuota()){
			if (!convenioAnt.equals(cuota.getNroConvenio())){
				if (i!=0)detalleCuotas+="; ";
				detalleCuotas += "Convenio Nro "+cuota.getNroConvenio() + " Cuota/s: " + cuota.getNroCuota();
				
			}else{
				detalleCuotas += ", "+cuota.getNroCuota();
			}
			convenioAnt = cuota.getNroConvenio();
			i++;
		}
		log.debug("DETALLE CUOTAS; "+detalleCuotas);
		
		
		return detalleCuotas;
	}*/


	public String getStrCuotas() {
	
		String detalleCuotas= "";
		int cantCuotas= getListCuota().size();
		if (cantCuotas==0)return detalleCuotas;
		
		String convenioAnt="";
		int i=0;
		for (LiqCuotaVO cuota : getListCuota()){
			if (!convenioAnt.equals(cuota.getNroConvenio())){
//				if (i!=0)
//				detalleCuotas+=" del convenio " + cuota.getNroConvenio() + " de " + cuota.getOrdenanza() + "; ";
//				detalleCuotas += "Cuotas " + cuota.getNroCuota();
                                detalleCuotas += "Convenio " + cuota.getNroConvenio() + " de " + cuota.getOrdenanza() + ". ";
				
//			}else{
//				detalleCuotas += ", " + cuota.getNroCuota();
			}
			convenioAnt = cuota.getNroConvenio();
//			if (i == listCuota.size()-1) detalleCuotas+=" del convenio " + cuota.getNroConvenio() + " de " + cuota.getOrdenanza() + "; ";
			i++;
		}
		
		log.debug("DETALLE CUOTAS; "+detalleCuotas);
		
		
		return detalleCuotas;
	}

	
	
	
	/**
	 *  - Genera el strPeriodoDeuda.<br>
	 *  - Parte esa cadena en las 4 lineas, manteniendo un largo maximo de 80 caracteres por linea -> para la impresion en TXT
	 *  @author arobledo
	 */
	public void generarLineasDeuda(){
		GenerarStringPeriodosDeuda();		
				
		boolean tieneLinea2 = strPeriodosDeuda.length()>largoLinea;
		boolean tieneLinea3 = strPeriodosDeuda.length()>largoLinea*2;
		boolean tieneLinea4 = strPeriodosDeuda.length()>largoLinea*3;
		
		if(tieneLinea2){
			
			// Linea 1
			String tmp = strPeriodosDeuda.substring(0, largoLinea);			
			int posicionCorte =0;
			// si el proximo lugar es "-" o " ", corta ahí
			if(strPeriodosDeuda.charAt(largoLinea)=='-' || strPeriodosDeuda.charAt(largoLinea)==' '){
				lineaDeuda1 = tmp;
				posicionCorte = largoLinea;
			}else{
				//sino busca donde puede cortar (un "-" o " ")
				posicionCorte = tmp.lastIndexOf('-')>tmp.lastIndexOf(' ')?tmp.lastIndexOf('-'):tmp.lastIndexOf(' ');
				lineaDeuda1 = tmp.substring(0, posicionCorte);
			}
			
			//Linea 2
			if(tieneLinea3){ // si tiene linea 3 -> se busca donde cortar la linea 2, sino va hasta el final
					tmp = strPeriodosDeuda.substring(posicionCorte, posicionCorte+largoLinea);
				// si el proximo lugar es "-" o " ", corta ahí
				if(strPeriodosDeuda.charAt(posicionCorte+largoLinea)=='-' || strPeriodosDeuda.charAt(posicionCorte+largoLinea)==' '){
					lineaDeuda2 = tmp;
					posicionCorte = posicionCorte+largoLinea;
				}else{
					//sino busca donde puede cortar (un "-" o " ")
					int proxPosicionCorte = tmp.lastIndexOf('-')>tmp.lastIndexOf(' ')?tmp.lastIndexOf('-'):tmp.lastIndexOf(' ');
					lineaDeuda2 = tmp.substring(0, proxPosicionCorte);
					posicionCorte = posicionCorte+proxPosicionCorte;
				}
			}else{ 
				lineaDeuda2 = strPeriodosDeuda.substring(posicionCorte);
			}
			
						
			// linea 3
			if(tieneLinea3){
				if(tieneLinea4){ // si tiene linea 4 -> se busca donde cortar la linea 3, sino va hasta el final
					tmp = strPeriodosDeuda.substring(posicionCorte, posicionCorte+largoLinea);
					// si el proximo lugar es "-" o " ", corta ahí
					if(strPeriodosDeuda.charAt(posicionCorte+largoLinea)=='-' || strPeriodosDeuda.charAt(posicionCorte+largoLinea)==' '){
						lineaDeuda3 = tmp;
						posicionCorte = posicionCorte+largoLinea;
					}else{
						//sino busca donde puede cortar (un "-" o " ")
						int proxPosicionCorte = tmp.lastIndexOf('-')>tmp.lastIndexOf(' ')?tmp.lastIndexOf('-'):tmp.lastIndexOf(' ');
						lineaDeuda3 = tmp.substring(0, proxPosicionCorte);
						posicionCorte = posicionCorte+proxPosicionCorte;
					}
				}else{ 
					lineaDeuda3 = strPeriodosDeuda.substring(posicionCorte);
				}
			}

			
			// linea 4
			if(tieneLinea4){				
				if(strPeriodosDeuda.length()<=largoLinea*3)
					lineaDeuda4 = strPeriodosDeuda.substring(posicionCorte);
				else
					if (strPeriodosDeuda.length()> largoLinea*4){
						lineaDeuda4 = strPeriodosDeuda.substring(posicionCorte, largoLinea*4);
					}else{
						lineaDeuda4=strPeriodosDeuda.substring(posicionCorte);
					}
			}
		}else{
			lineaDeuda1 = strPeriodosDeuda;
		}
	}
	
	// <-- Metodos para generar las lineas con los periodos de deuda
	
	
	
	
	// ---> Metodos para obtener las lineas con las cuotas de los convenios
	
	/**
	 *  Genera las 4 lineas con los convenioCuota, manteniendo un largo maximo de 80 caracteres por linea -> para la impresion en TXT
	 *  @author arobledo
	 */
	public void generarLineasConvenioCuota(){
		
		// Genera el String con los convenios
		String strConvenioCuota = "";
		/*for(LiqCuotaVO c:listCuota){
			strConvenioCuota+=" - "+c.getNroCuota();
			
		}*/
		
		strConvenioCuota  = this.getStrCuotas();
		
		boolean tieneLinea2 = strConvenioCuota.length()>largoLinea;
		boolean tieneLinea3 = strConvenioCuota.length()>largoLinea*2;
		boolean tieneLinea4 = strConvenioCuota.length()>largoLinea*3;
		
		if(tieneLinea2){
			
			// Linea 1
			String tmp = strConvenioCuota.substring(0, largoLinea);			
			int posicionCorte =0;
			// si el proximo lugar es "-" o " ", corta ahí
			if(strConvenioCuota.charAt(largoLinea)=='-' || strConvenioCuota.charAt(largoLinea)==' '){
				lineaConvenioCuota1= tmp;
				posicionCorte = largoLinea;
			}else{
				//sino busca donde puede cortar (un "-" o " ")
				posicionCorte = tmp.lastIndexOf('-')>tmp.lastIndexOf(' ')?tmp.lastIndexOf('-'):tmp.lastIndexOf(' ');
				lineaConvenioCuota1 = tmp.substring(0, posicionCorte);
			}
			
			//Linea 2
			if(tieneLinea3){ // si tiene linea 3 -> se busca donde cortar la linea 2, sino va hasta el final
					tmp = strConvenioCuota.substring(posicionCorte, posicionCorte+largoLinea);
				// si el proximo lugar es "-" o " ", corta ahí
				if(strConvenioCuota.charAt(posicionCorte+largoLinea)=='-' || strConvenioCuota.charAt(posicionCorte+largoLinea)==' '){
					lineaConvenioCuota2 = tmp;
					posicionCorte = posicionCorte+largoLinea;
				}else{
					//sino busca donde puede cortar (un "-" o " ")
					int proxPosicionCorte = tmp.lastIndexOf('-')>tmp.lastIndexOf(' ')?tmp.lastIndexOf('-'):tmp.lastIndexOf(' ');
					lineaConvenioCuota2 = tmp.substring(0, proxPosicionCorte);
					posicionCorte = posicionCorte+proxPosicionCorte;
				}
			}else{ 
				lineaConvenioCuota2 = strConvenioCuota.substring(posicionCorte);
			}
			
						
			// linea 3
			if(tieneLinea3){
				if(tieneLinea4){ // si tiene linea 4 -> se busca donde cortar la linea 3, sino va hasta el final
					tmp = strConvenioCuota.substring(posicionCorte, posicionCorte+largoLinea);
					// si el proximo lugar es "-" o " ", corta ahí
					if(strConvenioCuota.charAt(posicionCorte+largoLinea)=='-' || strConvenioCuota.charAt(posicionCorte+largoLinea)==' '){
						lineaConvenioCuota3 = tmp;
						posicionCorte = posicionCorte+largoLinea;
					}else{
						//sino busca donde puede cortar (un "-" o " ")
						int proxPosicionCorte = tmp.lastIndexOf('-')>tmp.lastIndexOf(' ')?tmp.lastIndexOf('-'):tmp.lastIndexOf(' ');
						lineaConvenioCuota3 = tmp.substring(0, proxPosicionCorte);
						posicionCorte = posicionCorte+proxPosicionCorte;
					}
				}else{ 
					lineaConvenioCuota3 = strConvenioCuota.substring(posicionCorte);
				}
			}

			
			// linea 4
			if(tieneLinea4){				
				if(strConvenioCuota.length()<=largoLinea*3)
					lineaConvenioCuota4 = strConvenioCuota.substring(posicionCorte);
				else
					//lineaConvenioCuota4 = strConvenioCuota.substring(posicionCorte, largoLinea*4);
					if (strConvenioCuota.length()> largoLinea*4){
						lineaConvenioCuota4 = strConvenioCuota.substring(posicionCorte, largoLinea*4);
					}else{
						lineaConvenioCuota4=strConvenioCuota.substring(posicionCorte);
					}
			}
		}else{
			lineaConvenioCuota1 = strConvenioCuota;
		}
	}

	public String getLineaConvenioCuota1() {
		return lineaConvenioCuota1;
	}
	public void setLineaConvenioCuota1(String lineaConvenioCuota1) {
		this.lineaConvenioCuota1 = lineaConvenioCuota1;
	}
	public String getLineaConvenioCuota2() {
		return lineaConvenioCuota2;
	}
	public void setLineaConvenioCuota2(String lineaConvenioCuota2) {
		this.lineaConvenioCuota2 = lineaConvenioCuota2;
	}
	public String getLineaConvenioCuota3() {
		return lineaConvenioCuota3;
	}
	public void setLineaConvenioCuota3(String lineaConvenioCuota3) {
		this.lineaConvenioCuota3 = lineaConvenioCuota3;
	}
	public String getLineaConvenioCuota4() {
		return lineaConvenioCuota4;
	}
	public void setLineaConvenioCuota4(String lineaConvenioCuota4) {
		this.lineaConvenioCuota4 = lineaConvenioCuota4;
	}
	
	// <--- Metodos para obtener las lineas con las cuotas de los convenios	
}
