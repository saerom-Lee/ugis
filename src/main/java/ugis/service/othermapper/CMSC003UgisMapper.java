package ugis.service.othermapper;

import java.sql.SQLException;
import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import ugis.service.vo.CMSC003DataSetVO;
import ugis.service.vo.CMSC003VO;

@Mapper("cmsc003UgisMapper")
public interface CMSC003UgisMapper {

	List<?> selectAll();

	List<?> selectOrtOrientMap(CMSC003VO cmsc003VO) throws SQLException;

	List<?> selectAirOrientMap(CMSC003VO cmsc003VO) throws SQLException;

	List<?> selectDemMap(CMSC003VO cmsc003VO) throws SQLException;

	List<?> selectDatasetInfo(String disasterId) throws SQLException;

	void insertDatasetInfo(CMSC003DataSetVO datasetvo) throws SQLException;

	void deleteDatasetInfo(String disasterId) throws SQLException;

	void insertDatasetInfoMap(EgovMap dataset) throws SQLException;

	

}
