//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.Date;

import coop.tecso.demoda.iface.helper.DateUtil;

public class EnvioProcCD {
	
	public EnvioProcCD() {
		for(int i = 0; i < periodos.length; i++) {
			periodos[i] = new PeriodosEnvioProc();
		}
	}
	
	public Long procurador = 0L; // (4)
	public String nroCuenta = "";  // (10) 
	public Long anio = 0L;       // (4) 
	public String catastral = "";     // (14) catastral
	public String nomTitPri;     // (26) nombre titular principal
	public PeriodosEnvioProc[] periodos = new PeriodosEnvioProc[12];
	public String desDomEnv;     // (30) domicilio envio
	public String ubicacion = "";     // (68) ubicacion finca o terreno
	public Long constancia = 0L; // (8) sin uso

	private Object nvl(Object v, Object dv) {
		return v == null ? dv : v;
	}

	private Object nvl(Object v, Object dv, int len) {
		if (v != null) {
			try { v = ((String) v).substring(0,len); } catch (Exception e) {}
		} 
		return v == null ? dv : v;
	}
		
    private String evl(String v, String dv) {
        return v == null || v.equals("") ? dv : v;
    }
	
	//quita los separadores decimales . y/o ,
	private String rmdsep(String v) {
		String ret;
		ret = v.replaceAll("\\.", "");
		ret = ret.replaceAll(",", "");
		return ret;
	}

	public String toString() {
		StringBuilder line = new StringBuilder();
		
		line.append(String.format("%04d", nvl(procurador, 0L)));
		line.append(String.format("%-10s", nvl(nroCuenta, "", 10)));
		line.append(String.format("%04d", nvl(anio, 0L)));
		line.append(String.format("%-14s", catastral.replace("/", "")));
		for(PeriodosEnvioProc periodo: periodos) {
			line.append(periodo.toString());
		}
		line.append(String.format("%-26s", nvl(nomTitPri, "",26)));
		line.append(String.format("%-30s", nvl(desDomEnv, "",30)));
		line.append(String.format("%-68s", nvl(ubicacion, "",68)));
		line.append(String.format("%08d", nvl(constancia, 0L)));
		
		return line.toString();
	}

	public class PeriodosEnvioProc { 
		
		public Long periodo;
		public Date fechaVencimiento;
		public Double conc1;     //contribucion tgi
		public Double conc2;     //tasa tgi
		public Double conc3;     //sobre tasa tgi
		public Double indice;    //sin uso
		public Double recargo;   //sin uso 
		public Double total;
		
		public String toString() {
			StringBuilder ret = new StringBuilder();
			
			ret.append(String.format("%02d", nvl(periodo, 0)));
			ret.append(evl(DateUtil.formatDate(fechaVencimiento, "yyyyMMdd"), "00000000").toString());
			ret.append(rmdsep(String.format("%011.2f", nvl(conc1, 0D)))); 
			ret.append(rmdsep(String.format("%011.2f", nvl(conc2, 0D)))); 
			ret.append(rmdsep(String.format("%011.2f", nvl(conc3, 0D)))); 
			ret.append(rmdsep(String.format("%07.3f", nvl(indice, 0D)))); 
			ret.append(rmdsep(String.format("%011.2f", nvl(recargo, 0D))));
			ret.append(rmdsep(String.format("%011.2f", nvl(total, 0D))));
			return ret.toString();
		}
	}
}
