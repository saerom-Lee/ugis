package ugis.service;

import java.util.List;

import ugis.service.vo.CmmnCodeVO;

public interface CmmnCodeService {

    List<CmmnCodeVO> selectCmmnCodeList(CmmnCodeVO vo);

}
