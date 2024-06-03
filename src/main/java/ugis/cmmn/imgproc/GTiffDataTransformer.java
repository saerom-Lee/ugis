package ugis.cmmn.imgproc;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.Operations;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class GTiffDataTransformer {

	public GridCoverage2D transform(GridCoverage2D coverage, String outputPath, CoordinateReferenceSystem targetCRS)
			throws Exception {

		return (GridCoverage2D) Operations.DEFAULT.resample(coverage, targetCRS);
	}
}
