package ugis.service.mapper;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import ugis.service.vo.CISC004VO;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper("cisc004Mapper")
public interface CISC004Mapper {

	CISC004VO select(CISC004VO cisc004VO);

	long insert(CISC004VO cisc004VO);

}
