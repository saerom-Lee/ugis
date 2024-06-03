package ugis.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {

	private Util() {
		throw new IllegalStateException("Utility Class");
	}

	/**
	 * <pre>
	 * Object를 Json 문자열로 반환
	 * </pre>
	 */
	public static String objToJsonStr(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		String jsonStr = "";

		if(obj == null) return jsonStr;

		try {
			jsonStr = mapper.writeValueAsString(obj);
		} catch (Exception e) {
		}

		return jsonStr;
	}

	/**
	 * <pre>
	 * Object를 HashMap으로 반환
	 * </pre>
	 */
	public static Map<String, Object> objToMap(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		map = mapper.convertValue(obj, new TypeReference<Map<String, Object>>() {});

		return map;
	}

	/**
	 * <pre>
	 * 마지막 문자가 Path Separator 이면 제거
	 * </pre>
	 */
	public static String removeLastSeperator(String path) {

		if(StringUtils.isEmpty(path)) {
			return path;
		}
		int lastPathIndex = path.lastIndexOf(File.separator);
		if(path.length() -1 != lastPathIndex) {
			return path;
		}

		return path.substring(0, lastPathIndex);

	}

}
