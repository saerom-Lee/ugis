package ugis.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import lombok.RequiredArgsConstructor;
import ugis.service.CmmnCodeService;
import ugis.service.mapper.CmmnCodeMapper;
import ugis.service.vo.CmmnCodeVO;

@RequiredArgsConstructor
@Service("cmmnCodeService")
public class CmmnCodeServiceImpl extends EgovAbstractServiceImpl implements CmmnCodeService {

    private final CmmnCodeMapper cmmnCodeMapper;

    @Override
    public List<CmmnCodeVO> selectCmmnCodeList(CmmnCodeVO vo) {
        return cmmnCodeMapper.selectCmmnCodeList(vo);
    }

}
