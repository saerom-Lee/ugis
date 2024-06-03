package ugis.service;

import java.util.HashMap;
import java.util.List;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface CTSC005Service<T> {

	List<EgovMap> selectYearStaticFree (HashMap<String, Object> params);
	
	List<EgovMap> selectYearStaticEmer (HashMap<String, Object> params);
	
	List<EgovMap> selectKindsStatic (HashMap<String, Object> params);
	
}
