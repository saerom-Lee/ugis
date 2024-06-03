package ugis.service;

import egovframework.rte.fdl.cmmn.exception.EgovBizException;
import ugis.util.map.BaseMap;

public interface TileService {
    byte[] getByteFromMapImageResource(BaseMap baseMap) throws EgovBizException;
}
