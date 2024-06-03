package ugis.service;

import java.util.HashMap;
import java.util.List;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface CISC007Service {

	List<EgovMap> selectAeroList(HashMap<String, Object> params);

	List<EgovMap> selectUsgsList(HashMap<String, Object> params);

	List<EgovMap> selectEtcList(HashMap<String, Object> params);

	List<EgovMap> selectUsgsAirList(HashMap<String, Object> params);

	List<EgovMap> selectUsgsDroneList(HashMap<String, Object> params);

	List<EgovMap> selectSoilList(HashMap<String, Object> params);

}
