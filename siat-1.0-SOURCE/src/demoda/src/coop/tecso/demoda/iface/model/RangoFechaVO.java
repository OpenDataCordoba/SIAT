//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.Date;

import coop.tecso.demoda.iface.helper.DateUtil;


/**
 *	Objecto que contiene dos valores string para guardar FechaDesde y Fecha Hasta. Ademas tiene un campo indice. 
 * 
 * @author tecso
 *
 */
public class RangoFechaVO {
		
	
		private String indice;
		private String fechaDesdeView;
		private String fechaHastaView;
		
		private Date fechaDesde = null;
		private Date fechaHasta = null;
		
		public RangoFechaVO() {
			super();
		}
		
		public RangoFechaVO(String indice, String fechaDesdeView, String fechaHastaView) {
			super();
			this.indice = indice;
			this.fechaDesdeView = fechaDesdeView;
			this.fechaHastaView = fechaHastaView;
		}
		
		public RangoFechaVO(String indice) {
			super();
			this.indice = indice;
		}

		// Getters Y Setters
		public String getFechaDesdeView() {
			return fechaDesdeView;
		}
		public void setFechaDesdeView(String fechaDesdeView) {
			this.fechaDesdeView = fechaDesdeView;
		}
		public String getFechaHastaView() {
			return fechaHastaView;
		}
		public void setFechaHastaView(String fechaHastaView) {
			this.fechaHastaView = fechaHastaView;
		}
		public String getIndice() {
			return indice;
		}
		public void setIndice(String indice) {
			this.indice = indice;
		}
		public Date getFechaDesde() {
			return fechaDesde;
		}
		public void setFechaDesde(Date fechaDesde) {
			this.fechaDesde = fechaDesde;
			this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
		}
		public Date getFechaHasta() {
			return fechaHasta;
		}
		public void setFechaHasta(Date fechaHasta) {
			this.fechaHasta = fechaHasta;
			this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
		}
		
					
}
