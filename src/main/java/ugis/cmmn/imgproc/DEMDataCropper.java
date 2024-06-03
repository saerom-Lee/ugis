package ugis.cmmn.imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

public class DEMDataCropper {

	public void crop(String inputPath, ReferencedEnvelope envelope, String outputPath,
			CoordinateReferenceSystem targetCrs) throws Exception {

		// CRS
		Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
		CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", hints);
		Integer targetEPSG = CRS.lookupEpsgCode(targetCrs, false);
		Envelope en = new Envelope(envelope.getMinX(), envelope.getMaxX(), envelope.getMinY(), envelope.getMaxY());

		// ROI
		double minx = Double.MAX_VALUE; // 좌하단 x
		double miny = Double.MAX_VALUE; // 좌하단 y
		double maxx = Double.MIN_VALUE; // 우상단 x
		double maxy = Double.MIN_VALUE; // 우상단 y

		// read dem to txt
		File file = new File(inputPath);
		List<String> orgLines = FileUtils.readLines(file, "EUC-KR");

		// 좌표계
		String prjStr = orgLines.get(15);
		String epsg = "EPSG:";
		if (prjStr.contains("중부원점")) {
			epsg += "5186";
		}
		if (prjStr.contains("동부원점")) {
			epsg += "5187";
		}
		if (prjStr.contains("서부원점")) {
			epsg += "5185";
		}
		if (!epsg.contains(targetEPSG.toString())) {
			// transform
			CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem(epsg);
			MathTransform transform = CRS.findMathTransform(targetCrs, crs, true);
			en = JTS.transform(en, transform);
		}
		System.out.println(">>>>>>>>>>>>>>>>>>> DEM Inputpath : " + inputPath);
		System.out.println(">>>>>>>>>>>>>>>>>>> DEM Envelop : " + en.toString());
		
		String[] ptSplits = null;
		// 좌표값
		List<String> ptLines = new ArrayList<>();
		for (int l = 28; l < orgLines.size(); l++) {
			String coorStr = orgLines.get(l);
			String[] strArr = coorStr.split(" ");

			double[] coors = new double[3];
			int idx = 0;
			// 공백제거
			for (int c = 0; c < strArr.length; c++) {
				String str = strArr[c];
				double coor;
				if (str.equals("")) {
					continue;
				} else if (str.equals(".00")) {
					coor = 0;
				} else {
					coor = Double.valueOf(str);
				}
				coors[idx] = coor;
				idx++;
			}
			double coorX = Math.round(Double.valueOf(coors[0]) * 100 / 100.0);
			double coorY = Math.round(Double.valueOf(coors[1]) * 100 / 100.0);
			double coorZ = Math.round(Double.valueOf(coors[2]) * 100 / 100.0);

			Coordinate coordinate = new Coordinate(coorX, coorY, coorZ);
			if (en.intersects(coordinate)) {
				String intersectedPt = coordinate.x + " " + coordinate.y;
				if (coordinate.z == 0) {
					intersectedPt += " " + ".00";
				} else if (coordinate.z > 0 && coordinate.z < 1) {
					String zStr = String.valueOf(coordinate.z);
					zStr = zStr.replace("0.", ".");
					intersectedPt += " " + zStr;
				} else {
					intersectedPt += " " + coordinate.z;
				}
				ptLines.add(intersectedPt);

				if (ptLines.size() == 1) {
					ptSplits = intersectedPt.split(" ");
				}
				minx = Double.min(minx, coordinate.x);
				maxx = Double.max(maxx, coordinate.x);
				miny = Double.min(miny, coordinate.y);
				maxy = Double.max(maxy, coordinate.y);
			}
		}

		String xStr = ptSplits[0];
		String yStr = ptSplits[1];
		String zStr = ptSplits[2];

		int xhalfLength = xStr.length() / 2;
		int yhalfLength = yStr.length() / 2;
		int zhalfLength = zStr.length() / 2;

		String xMeta = "";
		String yMeta = "";
		String zMeta = "";

		for (int l = 0; l < xStr.length(); l++) {
			if (l == xhalfLength) {
				xMeta += "X";
			} else {
				xMeta += " ";
			}
		}
		for (int l = 0; l < yStr.length(); l++) {
			if (l == yhalfLength) {
				yMeta += "Y";
			} else {
				yMeta += " ";
			}
		}
		for (int l = 0; l < zStr.length(); l++) {
			if (l == zhalfLength) {
				zMeta += "Z";
			} else {
				zMeta += " ";
			}
		}
		String xyzMeta = xMeta + " " + yMeta + " " + zMeta;

		// 메타데이터
		List<String> metaLines = new ArrayList<String>();
		for (int l = 0; l < 27; l++) {
			if (l == 11) {
				if (orgLines.get(l).contains(":")) {
					int idx = orgLines.get(l).indexOf(":");
					String rlxy = orgLines.get(l).substring(0, idx + 1) + minx + ", " + miny; // 좌하단 x y
					metaLines.add(rlxy);
				} else {
					String rlxy = minx + ", " + miny; // 좌하단 x y
					metaLines.add(rlxy);
				}
			} else if (l == 12) {
				if (orgLines.get(l).contains(":")) {
					int idx = orgLines.get(l).indexOf(":");
					String rlxy = orgLines.get(l).substring(0, idx + 1) + maxx + ", " + maxy; // 우상단 x y
					metaLines.add(rlxy);
				} else {
					String rlxy = maxx + ", " + maxy; // 우상단 x y
					metaLines.add(rlxy);
				}
			} else {
				metaLines.add(orgLines.get(l));
			}
		}
		metaLines.add(xyzMeta);

		// write cropped dem
		List<String> croppedLines = new ArrayList<String>();
		croppedLines.addAll(metaLines);
		croppedLines.addAll(croppedLines.size(), ptLines);
		FileUtils.writeLines(new File(outputPath), "EUC-KR", croppedLines);
	}
}
