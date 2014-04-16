//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package alone; 

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import ar.gov.rosario.siat.gde.buss.dao.ProcesoMasivoDAO;

public class ProMas {

	public static void main(String[] args) throws Exception {
		ProcesoMasivoDAO main = new ProcesoMasivoDAO();
		
		Alone.init(false, false);
		
		ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(82L);
		
		SiatHibernateUtil.currentSession().beginTransaction();
		main.generarPlanillasConstancias(procesoMasivo);
		
	}

	
}

