//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import coop.tecso.demoda.buss.helper.StandaloneContext;

public class TestUtil {
	static private boolean siatInitialized = false;
	
	static public boolean initSiat() {
		if (TestUtil.siatInitialized)
				return true;
		try {
			InputStream stServer = StandaloneContext.findServerConf();
			InputStream stContext = StandaloneContext.findContextConf();
			
			Map<String, Map<String, String>> dsm = StandaloneContext.parseDatasoruces(stServer, stContext);
			StandaloneContext.bindDatasources(dsm);
			DefServiceLocator.getConfiguracionService().initializeSiat();
			TestUtil.siatInitialized = true;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			TestUtil.siatInitialized = false;
			return false;
		}
	}

	public static boolean compareFiles(String f1, String f2) throws FileNotFoundException, IOException {
		InputStream st1 = new BufferedInputStream(new FileInputStream(f1));
		InputStream st2 = new BufferedInputStream(new FileInputStream(f2));
		final int BufSize = 1024;
		byte[] b1 = new byte[BufSize], b2 = new byte[BufSize];
		int br1, br2;
		boolean ret = true;
		
		while ((br1 = st1.read(b1, 0, BufSize)) > 0) {
			br2 = st2.read(b2, 0, BufSize);
			Arrays.fill(b1, br1 - 1, BufSize - 1, (byte)0);
			Arrays.fill(b2, br2 - 1, BufSize - 1, (byte)0);
			if (!Arrays.equals(b1, b2)) {
				ret = false;
				break;
			}
		}
			
		return ret;
	}
}
