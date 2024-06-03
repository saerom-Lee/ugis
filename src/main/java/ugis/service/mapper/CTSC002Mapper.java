package ugis.service.mapper;

import ugis.service.vo.CTSC001VO;
import ugis.service.vo.CTSC002VO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;

@Mapper("ctsc002Mapper")
public interface CTSC002Mapper extends CTSCBaseMapper<CTSC002VO> {

    List<CTSC002VO> ctsc002_search(CTSC002VO vo);
}
