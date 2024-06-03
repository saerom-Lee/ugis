package ugis.service.mapper;

import java.util.HashMap;
import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import ugis.service.vo.CISC005VO;

@Mapper("cisc005Mapper")
public interface CISC005Mapper {

	List<CISC005VO> selectSoilSatList(HashMap<String, Object> params);
	List<CISC005VO> selectEtcSatList(HashMap<String, Object> params);
	List<CISC005VO> selectAeroSatList(HashMap<String, Object> params);

}
