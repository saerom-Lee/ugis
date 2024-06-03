package ugis.service;

import egovframework.rte.fdl.cmmn.exception.EgovBizException;
import ugis.util.map.BaseMap;

public interface GeoserverService {
    byte[] getByteFromMapImageResource(String url) throws EgovBizException;
}
