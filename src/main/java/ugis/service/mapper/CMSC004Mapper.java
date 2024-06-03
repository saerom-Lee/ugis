package ugis.service.mapper;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import ugis.service.vo.CMSC003VO;

@Mapper("cmsc004Mapper")
public interface CMSC004Mapper {

	List<?> selectAeroList(CMSC003VO cmsc003vo) throws Exception;

}
