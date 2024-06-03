package ugis.service.mapper;

import java.util.HashMap;
import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;


@Mapper("cmsc002Mapper")
public interface CMSC002Mapper {
    List<?> selectSiList();
    List<?> selectSggList(String si);
    List<?> selectEmdList(String sgg);

}
