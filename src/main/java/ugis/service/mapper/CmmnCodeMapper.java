package ugis.service.mapper;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import ugis.service.vo.CmmnCodeVO;

@Mapper("cmmnCodeMapper")
public interface CmmnCodeMapper {

    List<CmmnCodeVO> selectCmmnCodeList(CmmnCodeVO vo);

}
