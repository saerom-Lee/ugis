package ugis.service.mapper;

import java.util.HashMap;
import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Mapper("cisc007Mapper")
public interface CISC007Mapper {

	List<EgovMap> selectAeroList(HashMap<String, Object> params);

	List<EgovMap> selectEtcList(HashMap<String, Object> params);

	List<EgovMap> selectUsgsList(HashMap<String, Object> params);

	List<EgovMap> selectUsgsAirList(HashMap<String, Object> params);

	List<EgovMap> selectUsgsDroneList(HashMap<String, Object> params);

	List<EgovMap> selectSoilList(HashMap<String, Object> params);

}
