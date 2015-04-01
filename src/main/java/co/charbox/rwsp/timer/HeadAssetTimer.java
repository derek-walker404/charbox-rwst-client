package co.charbox.rwsp.timer;

import org.apache.commons.httpclient.methods.HeadMethod;

public class HeadAssetTimer extends AssetTimer<HeadMethod> {

	@Override
	protected HeadMethod getHttpMethod(String assetLocation) {
		return new HeadMethod(assetLocation);
	}

}
