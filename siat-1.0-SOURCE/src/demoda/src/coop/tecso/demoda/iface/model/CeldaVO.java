//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 *	Estructura de Celda para usar en tablas. (Para generar PDFs)  
 * 
 * @author tecso
 *
 */
public class CeldaVO {
		
	private Logger log = Logger.getLogger(CeldaVO.class);
	
		public static final String TEXT_ALIGN_CENTER = "center";
		public static final String TEXT_ALIGN_RIGHT = "right";
		public static final String TEXT_ALIGN_LEFT = "left";
	
		private String nombreColumna;
		private String etiqueta;
		
		// [ 1- encabezado | 2- eje horizontal | 3- eje vertical | 4- Valor]
		private String tipoCelda; 
		
		// siempre el valor a mostrar es un String. 
		private String valor = "";

		// [ 1- visible | 0- invisible ]
		private Integer mostrar;

		private Integer width = new Integer(0);

		private List<CeldaVO> listCelda = new ArrayList<CeldaVO>();
		
		private String textAlign = CeldaVO.TEXT_ALIGN_CENTER;
		private String whiteSpaceCollapse = "true";
			
		public CeldaVO() {
			super();
		}
		
		public CeldaVO(String valor, String nombreColumna, Integer mostrar) {
			super();
			this.valor = valor;
			this.nombreColumna = nombreColumna;
			this.mostrar = mostrar;
		}
		
		public CeldaVO(String valor) {
			super();
			this.valor = valor;
			this.mostrar = 1;
		}
		
		public CeldaVO(String valor, Integer width) {
			this(valor);
			this.width = width;
		}

		public CeldaVO(String valor, String nombreColumna) {
			super();
			this.valor = valor;
			this.nombreColumna = nombreColumna;
			this.mostrar = 1;
		}
		
		public CeldaVO(String valor, String nombreColumna, String etiqueta) {
			this(valor,nombreColumna);
			this.etiqueta = etiqueta;
		}
		
		// Getters Y Setters
		
		public String getEtiqueta() {
			return etiqueta;
		}
		public void setEtiqueta(String etiqueta) {
			this.etiqueta = etiqueta;
		}
		public Integer getMostrar() {
			return mostrar;
		}
		public void setMostrar(Integer mostrar) {
			this.mostrar = mostrar;
		}
		public String getNombreColumna() {
			return nombreColumna;
		}
		public void setNombreColumna(String nombreColumna) {
			this.nombreColumna = nombreColumna;
		}
		public String getTipoCelda() {
			return tipoCelda;
		}
		public void setTipoCelda(String tipoCelda) {
			this.tipoCelda = tipoCelda;
		}
		public String getValor() {
			return valor;
		}
		public void setValor(String valor) {
			this.valor = valor;
		}
		public Integer getWidth() {
			return width;
		}
		public void setWidth(Integer width) {
			this.width = width;
		}
		public List<CeldaVO> getListCelda() {
			return listCelda;
		}
		public void setListCelda(List<CeldaVO> listCelda) {
			this.listCelda = listCelda;
		}
		public void addCelda(CeldaVO subCelda) {
			this.listCelda.add(subCelda);
		}
		
		/**
		 * Obtiene todos los valores de las subceldas de la celda separados por \n
		 * @return
		 */
		public String getValorSubCeldas(){
			String valor = "";
			boolean primero = true;
			for (Iterator iter = this.listCelda.iterator(); iter.hasNext() && !primero;) {
				CeldaVO celda = (CeldaVO) iter.next();
				if(!primero){
					valor +="\n";
					primero = false;
				}
				valor += celda.getValor();
			}
			
			return valor;
		}
		
		public void logearContenido(){
			String s = 
			"etiqueta: " + this.getEtiqueta() + "// " + 
			"nombreColumna: " + this.getNombreColumna() + "// " +
			"valor: " + this.getValor() + "// " +
			"valorSubCeldas: " + this.getValorSubCeldas() + "// " +
			"width: " + this.getWidth() + "// " +
			"tipoCelda: " + this.getTipoCelda() + "// " +
			"mostrar: " + this.getMostrar() + "// " +
			"listCelda: " + this.getListCelda();
			
			log.debug(s);
		}
			
		public void setFalseWhiteSpaceCollapse(){
			this.whiteSpaceCollapse = "false";
		}
		
		public void setTrueWhiteSpaceCollapse(){
			this.whiteSpaceCollapse = "true";
		}
		
		public void setTextAlignCenter(){
			this.textAlign = CeldaVO.TEXT_ALIGN_CENTER;
		}
		
		public void setTextAlignLeft(){
			this.textAlign = CeldaVO.TEXT_ALIGN_LEFT;
		}
		
		public void setTextAlignRight(){
			this.textAlign = CeldaVO.TEXT_ALIGN_RIGHT;
		}

		public String getTextAlign() {
			return textAlign;
		}

		public String getWhiteSpaceCollapse() {
			return whiteSpaceCollapse;
		}
		
		
}
