package ugis.service.mapper;

import java.util.HashMap;
import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

// unused.. sample
@Mapper("mainMapper")
public interface MainMapper {

	List<?> selectRadarList() throws Exception;

	List<?> selectFileDown(HashMap<String, Object> param) throws Exception;
	
}
