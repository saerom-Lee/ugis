package ugis.service.othermapper;

import java.sql.SQLException;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import ugis.service.vo.CMSC003DataSetVO;

@Mapper("cmsc003DataSetMapper")
public interface CMSC003DataSetMapper {

	void insertDatasetInfo(CMSC003DataSetVO datasetvo) throws SQLException;

}
