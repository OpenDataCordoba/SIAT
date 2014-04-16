//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import java.util.ArrayList;
import java.util.List;

public class OrganizadorForReport {

		private String nombreOrganizador = "";
		private String cuitOrganizador = "";
		
		private List<HabilitacionVO> listHabilitacionVO = new ArrayList<HabilitacionVO>();
		
		public OrganizadorForReport(String nombre, String cuit){
			this.nombreOrganizador = nombre;
			this.cuitOrganizador = cuit;
		}

		// Getters Y Setters
		public String getCuitOrganizador() {
			return cuitOrganizador;
		}
		public void setCuitOrganizador(String cuitOrganizador) {
			this.cuitOrganizador = cuitOrganizador;
		}
		public List<HabilitacionVO> getListHabilitacionVO() {
			return listHabilitacionVO;
		}
		public void setListHabilitacionVO(List<HabilitacionVO> listHabilitacionVO) {
			this.listHabilitacionVO = listHabilitacionVO;
		}
		public String getNombreOrganizador() {
			return nombreOrganizador;
		}
		public void setNombreOrganizador(String nombreOrganizador) {
			this.nombreOrganizador = nombreOrganizador;
		}
		
		public void addHabilitacion(HabilitacionVO habilitacionVO){
			this.listHabilitacionVO.add(habilitacionVO);
		}
	
		
}
