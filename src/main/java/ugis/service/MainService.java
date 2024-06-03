package ugis.service;

import java.util.HashMap;
import java.util.List;

public interface MainService {

	List<?> selectRadarList() throws Exception;

	List<?> selectFileDown(HashMap<String, Object> param) throws Exception;
}
